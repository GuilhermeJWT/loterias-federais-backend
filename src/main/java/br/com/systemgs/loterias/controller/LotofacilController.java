package br.com.systemgs.loterias.controller;

import br.com.systemgs.loterias.dto.AnaliseJogoDTO;
import br.com.systemgs.loterias.dto.GeracaoJogoDTO;
import br.com.systemgs.loterias.dto.HeatmapParesDTO;
import br.com.systemgs.loterias.dto.JogoSugeridoDTO;
import br.com.systemgs.loterias.dto.MelhoresJogosHistoricosDTO;
import br.com.systemgs.loterias.dto.RecomendacaoDoDiaDTO;
import br.com.systemgs.loterias.dto.SequenciasConsecutivasDTO;
import br.com.systemgs.loterias.dto.request.AnaliseJogoRequest;
import br.com.systemgs.loterias.model.Lotofacil;
import br.com.systemgs.loterias.service.AnaliseJogoService;
import br.com.systemgs.loterias.service.GeradorJogoService;
import br.com.systemgs.loterias.service.HeatmapParesService;
import br.com.systemgs.loterias.service.JogoSugeridoService;
import br.com.systemgs.loterias.service.MelhoresJogosHistoricosService;
import br.com.systemgs.loterias.service.RecomendacaoDoDiaService;
import br.com.systemgs.loterias.service.SequenciasConsecutivasService;
import br.com.systemgs.loterias.service.interfaces.ILotofacilApiService;
import br.com.systemgs.loterias.service.interfaces.ILotofacilService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller consolidado para operações de Lotofácil
 * Contém todos os endpoints relacionados a Lotofácil:
 * - CRUD de concursos
 * - Integração com API da Caixa
 * - Análise de jogos
 * - Geração de jogos
 * - Estatísticas
 */
@RestController
@RequestMapping("/lotofacil")
@RequiredArgsConstructor
@Slf4j
public class LotofacilController {

    // Services de CRUD e API
    private final ILotofacilService lotofacilService;
    private final ILotofacilApiService lotofacilApiService;

    // Services de funcionalidades específicas
    private final AnaliseJogoService analiseJogoService;
    private final GeradorJogoService geradorJogoService;
    private final JogoSugeridoService jogoSugeridoService;
    private final RecomendacaoDoDiaService recomendacaoDoDiaService;
    private final MelhoresJogosHistoricosService melhoresJogosHistoricosService;
    private final SequenciasConsecutivasService sequenciasConsecutivasService;
    private final HeatmapParesService heatmapParesService;

    // ==================== CRUD ====================

    @GetMapping("/concurso/{concurso}")
    public ResponseEntity<Lotofacil> buscarPorConcurso(@PathVariable Integer concurso) {
        return lotofacilService.buscarPorConcurso(concurso)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/todos")
    public ResponseEntity<List<Lotofacil>> buscarTodos() {
        return ResponseEntity.ok(lotofacilService.buscarTodos());
    }

    @GetMapping("/ultimos/{quantidade}")
    public ResponseEntity<List<Lotofacil>> buscarUltimosSorteios(@PathVariable int quantidade) {
        return ResponseEntity.ok(lotofacilService.buscarUltimosSorteios(quantidade));
    }

    @GetMapping("/total")
    public ResponseEntity<Long> contarTotalSorteios() {
        return ResponseEntity.ok(lotofacilService.contarTotalSorteios());
    }

    @GetMapping("/ultimo")
    public ResponseEntity<Lotofacil> buscarUltimoConcurso() {
        List<Lotofacil> ultimos = lotofacilService.buscarUltimosSorteios(1);
        return ultimos.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(ultimos.get(0));
    }

    // ==================== API da Caixa ====================

    @GetMapping("/api/ultimo")
    public ResponseEntity<Lotofacil> buscarUltimoConcursoApi() {
        return ResponseEntity.ok(lotofacilApiService.buscarUltimoConcursoApi());
    }

    @GetMapping("/api/concurso/{concurso}")
    public ResponseEntity<Lotofacil> buscarConcursoEspecificoApi(@PathVariable Integer concurso) {
        return ResponseEntity.ok(lotofacilApiService.buscarConcursoEspecificoApi(concurso));
    }

    @PostMapping("/api/salvar-ultimo")
    public ResponseEntity<Lotofacil> salvarUltimoConcurso() {
        return ResponseEntity.ok(lotofacilApiService.buscarESalvarUltimoConcurso());
    }

    @PostMapping("/api/salvar/{concurso}")
    public ResponseEntity<Lotofacil> salvarConcursoEspecifico(@PathVariable Integer concurso) {
        return ResponseEntity.ok(lotofacilApiService.buscarESalvarConcursoEspecifico(concurso));
    }

    // ==================== Análise de Jogos ====================

    @PostMapping("/analise/jogo")
    public ResponseEntity<AnaliseJogoDTO> analisarJogo(@Valid @RequestBody AnaliseJogoRequest request) {
        log.info("Recebida requisição para analisar jogo da Lotofácil: {}", request.getJogo());
        log.info("Últimos sorteios: {}", request.getUltimosSorteios());

        try {
            AnaliseJogoDTO resultado;
            if (request.getUltimosSorteios() != null && request.getUltimosSorteios() > 0) {
                log.info("Analisando jogo nos últimos {} sorteios", request.getUltimosSorteios());
                resultado = analiseJogoService.analisarJogoUltimosSorteios(
                        request.getJogo(), request.getUltimosSorteios());
            } else {
                log.info("Analisando jogo completo");
                resultado = analiseJogoService.analisarJogo(request.getJogo());
            }
            log.info("Análise concluída com sucesso");
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            log.error("Erro ao analisar jogo", e);
            throw e;
        }
    }

    // ==================== Geração de Jogos ====================

    @GetMapping("/analise/gerar/mais-sorteados")
    public ResponseEntity<GeracaoJogoDTO> gerarMaisSorteados(
            @RequestParam(defaultValue = "5") int quantidadeJogos,
            @RequestParam(defaultValue = "100") int ultimosSorteios) {
        log.info("Gerando {} jogos da Lotofácil mais sorteados baseados nos últimos {} sorteios",
                quantidadeJogos, ultimosSorteios);

        try {
            GeracaoJogoDTO resultado = geradorJogoService.gerarJogosBaseadosEmNumerosMaisSorteados(
                    quantidadeJogos, ultimosSorteios);
            log.info("Jogos gerados com sucesso");
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            log.error("Erro ao gerar jogos mais sorteados", e);
            throw e;
        }
    }

    // ==================== Jogos Sugeridos ====================

    @GetMapping("/sugerido/ultimos5")
    public ResponseEntity<JogoSugeridoDTO> gerarJogoUltimos5() {
        return ResponseEntity.ok(jogoSugeridoService.gerarJogoUltimos5());
    }

    @GetMapping("/sugerido/ultimos15")
    public ResponseEntity<JogoSugeridoDTO> gerarJogoUltimos15() {
        return ResponseEntity.ok(jogoSugeridoService.gerarJogoUltimos15());
    }

    @GetMapping("/sugerido/ultimos30")
    public ResponseEntity<JogoSugeridoDTO> gerarJogoUltimos30() {
        return ResponseEntity.ok(jogoSugeridoService.gerarJogoUltimos30());
    }

    @GetMapping("/sugerido/ultimos100")
    public ResponseEntity<JogoSugeridoDTO> gerarJogoUltimos100() {
        return ResponseEntity.ok(jogoSugeridoService.gerarJogoUltimos100());
    }

    // ==================== Recomendação do Dia ====================

    @GetMapping("/recomendacao-dia")
    public ResponseEntity<RecomendacaoDoDiaDTO> gerarRecomendacaoDoDia() {
        return ResponseEntity.ok(recomendacaoDoDiaService.gerarRecomendacaoDoDia());
    }

    // ==================== Melhores Jogos Históricos ====================

    @GetMapping("/melhores-jogos-historicos")
    public ResponseEntity<MelhoresJogosHistoricosDTO> analisarMelhoresJogosHistoricos() {
        return ResponseEntity.ok(melhoresJogosHistoricosService.analisarMelhoresJogosHistoricos());
    }

    // ==================== Análises Avançadas ====================

    @GetMapping("/analise/sequencias-consecutivas")
    public ResponseEntity<SequenciasConsecutivasDTO> analisarSequenciasConsecutivas() {
        return ResponseEntity.ok(sequenciasConsecutivasService.analisarSequenciasConsecutivas());
    }

    @GetMapping("/analise/heatmap-pares")
    public ResponseEntity<HeatmapParesDTO> gerarHeatmapPares() {
        return ResponseEntity.ok(heatmapParesService.gerarHeatmapPares());
    }

    // ==================== Estatísticas ====================

    @GetMapping("/frequencia")
    public ResponseEntity<Map<Integer, Integer>> buscarFrequenciaNumeros(
            @RequestParam(defaultValue = "100") int ultimosSorteios) {
        List<Lotofacil> sorteios = lotofacilService.buscarUltimosSorteios(ultimosSorteios);
        Map<Integer, Integer> frequencia = new java.util.HashMap<>();

        for (int i = 1; i <= 25; i++) {
            frequencia.put(i, 0);
        }

        for (Lotofacil sorteio : sorteios) {
            String[] dezenas = sorteio.getDezenas().split(",");
            for (String dezena : dezenas) {
                int numero = Integer.parseInt(dezena.trim());
                frequencia.put(numero, frequencia.getOrDefault(numero, 0) + 1);
            }
        }

        return ResponseEntity.ok(frequencia);
    }

    public record StatusImportacao(
            int ultimoConcursoSalvo,
            int totalConcursosSalvos,
            String mensagem
    ) {}
}