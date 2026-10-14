package br.com.systemgs.loterias.service;

import br.com.systemgs.loterias.model.Lotofacil;
import br.com.systemgs.loterias.service.interfaces.ILotofacilApiService;
import br.com.systemgs.loterias.service.interfaces.ILotofacilService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Serviço de importação de concursos da Lotofácil
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LotofacilImportacaoService {

    private final ILotofacilApiService lotofacilApiService;
    private final ILotofacilService lotofacilService;

    private static final int RATE_LIMIT_DELAY_MS = 500; // 500ms entre requisições
    private static final int BATCH_SIZE = 50; // Pausa maior a cada 50 requisições
    private static final int BATCH_DELAY_MS = 5000; // 5 segundos de pausa a cada batch

    /**
     * Importa concursos em um intervalo específico
     */
    @Async
    public CompletableFuture<ImportacaoResultado> importarConcursos(int concursoInicial, int concursoFinal) {
        log.info("🚀 Iniciando importação de concursos de {} a {}", concursoInicial, concursoFinal);

        List<Lotofacil> concursosImportados = new ArrayList<>();
        AtomicInteger erros = new AtomicInteger(0);
        AtomicInteger jaExistiam = new AtomicInteger(0);
        AtomicInteger processados = new AtomicInteger(0);

        long tempoInicio = System.currentTimeMillis();

        for (int concurso = concursoInicial; concurso <= concursoFinal; concurso++) {
            try {
                processados.incrementAndGet();

                // Verifica se já existe antes de buscar na API
                if (lotofacilService.buscarPorConcurso(concurso).isPresent()) {
                    jaExistiam.incrementAndGet();
                    log.debug("⏭️  Concurso {} já existe no banco, pulando", concurso);
                    continue;
                }

                // Busca e salva o concurso
                Lotofacil lotofacil = lotofacilApiService.buscarESalvarConcursoEspecifico(concurso);
                concursosImportados.add(lotofacil);

                // Log de progresso a cada 10 concursos
                if (concurso % 10 == 0) {
                    double progresso = ((double) (concurso - concursoInicial + 1) / (concursoFinal - concursoInicial + 1)) * 100;
                    log.info("📊 Progresso: {:.1f}% ({}/{} concursos processados)", progresso, concurso - concursoInicial + 1, concursoFinal - concursoInicial + 1);
                }

                // Rate limiting
                Thread.sleep(RATE_LIMIT_DELAY_MS);

                // Pausa maior a cada batch
                if (concurso % BATCH_SIZE == 0) {
                    log.info("⏸️  Pausa de {}ms após processar {} concursos", BATCH_DELAY_MS, BATCH_SIZE);
                    Thread.sleep(BATCH_DELAY_MS);
                }

            } catch (Exception e) {
                erros.incrementAndGet();
                log.error("❌ Erro ao importar concurso {}: {}", concurso, e.getMessage());

                // Continua mesmo com erro para não parar todo o processo
                try {
                    Thread.sleep(RATE_LIMIT_DELAY_MS);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }

        long tempoFim = System.currentTimeMillis();
        long duracaoSegundos = (tempoFim - tempoInicio) / 1000;

        ImportacaoResultado resultado = new ImportacaoResultado(
                concursoInicial,
                concursoFinal,
                processados.get(),
                concursosImportados.size(),
                jaExistiam.get(),
                erros.get(),
                duracaoSegundos,
                String.format("Importação concluída: %d novos, %d já existiam, %d erros em %d segundos",
                        concursosImportados.size(), jaExistiam.get(), erros.get(), duracaoSegundos)
        );

        log.info("✅ {}", resultado.mensagem());
        log.info("📈 Estatísticas finais: {} processados, {} novos, {} já existiam, {} erros",
                processados.get(), concursosImportados.size(), jaExistiam.get(), erros.get());

        return CompletableFuture.completedFuture(resultado);
    }

    /**
     * Importa todos os concursos disponíveis na API
     */
    @Async
    public CompletableFuture<ImportacaoResultado> importarTodosConcursos() {
        log.info("🎯 Iniciando importação completa de todos os concursos da Lotofácil");

        try {
            // Busca o último concurso para saber o total
            Lotofacil ultimoConcurso = lotofacilApiService.buscarUltimoConcursoApi();
            int concursoFinal = ultimoConcurso.getConcurso();
            int concursoInicial = 1;

            log.info("📋 Total de concursos a importar: {} (do {} ao {})", concursoFinal, concursoInicial, concursoFinal);

            return importarConcursos(concursoInicial, concursoFinal);
        } catch (Exception e) {
            log.error("❌ Erro ao iniciar importação completa: {}", e.getMessage());
            ImportacaoResultado resultado = new ImportacaoResultado(
                    0, 0, 0, 0, 0, 1, 0,
                    "Erro na importação: " + e.getMessage()
            );
            return CompletableFuture.completedFuture(resultado);
        }
    }

    /**
     * Importa os últimos X concursos
     */
    @Async
    public CompletableFuture<ImportacaoResultado> importarUltimosConcursos(int quantidade) {
        log.info("🔄 Importando últimos {} concursos", quantidade);

        try {
            // Primeiro busca o último concurso para saber onde começar
            Lotofacil ultimoConcurso = lotofacilApiService.buscarUltimoConcursoApi();
            int concursoFinal = ultimoConcurso.getConcurso();
            int concursoInicial = Math.max(1, concursoFinal - quantidade + 1);

            log.info("📋 Importando concursos de {} a {}", concursoInicial, concursoFinal);

            return importarConcursos(concursoInicial, concursoFinal);
        } catch (Exception e) {
            log.error("❌ Erro ao importar últimos concursos: {}", e.getMessage());
            ImportacaoResultado resultado = new ImportacaoResultado(
                    0, 0, 0, 0, 0, 1, 0,
                    "Erro na importação: " + e.getMessage()
            );
            return CompletableFuture.completedFuture(resultado);
        }
    }

    /**
     * Importa apenas os concursos que estão faltando no banco
     */
    @Async
    public CompletableFuture<ImportacaoResultado> importarConcursosFaltantes() {
        log.info("🔍 Verificando concursos faltantes...");

        try {
            // Busca o último concurso disponível na API
            Lotofacil ultimoConcursoApi = lotofacilApiService.buscarUltimoConcursoApi();
            int concursoFinal = ultimoConcursoApi.getConcurso();

            // Busca o último concurso salvo no banco
            int ultimoConcursoSalvo = verificarUltimoConcursoSalvo();

            if (ultimoConcursoSalvo >= concursoFinal) {
                log.info("✅ Todos os concursos já estão importados (último: {})", ultimoConcursoSalvo);
                ImportacaoResultado resultado = new ImportacaoResultado(
                        1, concursoFinal, concursoFinal, 0, concursoFinal, 0, 0,
                        "Todos os concursos já estão importados"
                );
                return CompletableFuture.completedFuture(resultado);
            }

            int concursoInicial = ultimoConcursoSalvo + 1;
            log.info("📋 Importando concursos faltantes de {} a {}", concursoInicial, concursoFinal);

            return importarConcursos(concursoInicial, concursoFinal);
        } catch (Exception e) {
            log.error("❌ Erro ao importar concursos faltantes: {}", e.getMessage());
            ImportacaoResultado resultado = new ImportacaoResultado(
                    0, 0, 0, 0, 0, 1, 0,
                    "Erro na importação: " + e.getMessage()
            );
            return CompletableFuture.completedFuture(resultado);
        }
    }

    /**
     * Verifica o último concurso salvo no banco
     */
    public int verificarUltimoConcursoSalvo() {
        return lotofacilService.buscarUltimosSorteios(1).stream()
                .findFirst()
                .map(Lotofacil::getConcurso)
                .orElse(0);
    }

    /**
     * Verifica o total de concursos salvos no banco
     */
    public long verificarTotalConcursosSalvos() {
        return lotofacilService.contarTotalSorteios();
    }

    /**
     * DTO para resultado da importação
     */
    public record ImportacaoResultado(
            int concursoInicial,
            int concursoFinal,
            int totalProcessados,
            int novosImportados,
            int jaExistiam,
            int erros,
            long duracaoSegundos,
            String mensagem
    ) {}
}