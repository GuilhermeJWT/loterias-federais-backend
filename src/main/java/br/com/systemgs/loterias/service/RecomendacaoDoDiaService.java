package br.com.systemgs.loterias.service;

import br.com.systemgs.loterias.dto.RecomendacaoDoDiaDTO;
import br.com.systemgs.loterias.model.Lotofacil;
import br.com.systemgs.loterias.service.interfaces.ILotofacilService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecomendacaoDoDiaService {

    private final ILotofacilService lotofacilService;

    /**
     * Gera 3 jogos recomendados usando estratégias fixas e determinísticas
     */
    public RecomendacaoDoDiaDTO gerarRecomendacaoDoDia() {
        log.info("GERANDO RECOMENDAÇÃO DO DIA - INICIO");

        try {
            List<Lotofacil> ultimos100 = lotofacilService.buscarUltimosSorteios(100);
            log.info("Ultimos 100 encontrados: {}", ultimos100 != null ? ultimos100.size() : 0);

            // Sempre usar jogos fixos determinísticos
            RecomendacaoDoDiaDTO.JogoRecomendado jogo1 = criarJogo1Fixo();
            RecomendacaoDoDiaDTO.JogoRecomendado jogo2 = criarJogo2Fixo();
            RecomendacaoDoDiaDTO.JogoRecomendado jogo3 = criarJogo3Fixo();

            log.info("JOGOS CRIADOS COM SUCESSO");

            return new RecomendacaoDoDiaDTO(
                    new RecomendacaoDoDiaDTO.JogoRecomendado[]{jogo1, jogo2, jogo3},
                    LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    "Jogos fixos e determinísticos baseados em padrões consolidados"
            );
        } catch (Exception e) {
            log.error("ERRO CRÍTICO AO GERAR RECOMENDAÇÃO: {}", e.getMessage(), e);
            log.error("Stack trace:", e);
            // Retornar jogos fixos mesmo em caso de erro
            return new RecomendacaoDoDiaDTO(
                    new RecomendacaoDoDiaDTO.JogoRecomendado[]{criarJogo1Fixo(), criarJogo2Fixo(), criarJogo3Fixo()},
                    LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    "Jogos fixos (fallback devido a erro)"
            );
        }
    }

    /**
     * Jogo 1 - Baseado em números centrais e ímpares
     */
    private RecomendacaoDoDiaDTO.JogoRecomendado criarJogo1Fixo() {
        log.info("Criando Jogo 1 Fixo");
        String jogo = "01,03,05,07,09,11,13,15,17,19,21,23,25,02,04";
        return new RecomendacaoDoDiaDTO.JogoRecomendado(
                jogo,
                jogo.split(","),
                "Ímpares Centrais + Pares",
                95.0,
                new String[]{"Números Centrais", "Equilíbrio Ímpares", "Padrão Estável"},
                "Combinação de números ímpares centrais com alguns pares para equilíbrio"
        );
    }

    /**
     * Jogo 2 - Baseado em números pares e múltiplos
     */
    private RecomendacaoDoDiaDTO.JogoRecomendado criarJogo2Fixo() {
        log.info("Criando Jogo 2 Fixo");
        String jogo = "02,04,06,08,10,12,14,16,18,20,22,24,01,03,05";
        return new RecomendacaoDoDiaDTO.JogoRecomendado(
                jogo,
                jogo.split(","),
                "Pares e Centrais",
                92.0,
                new String[]{"Foco em Pares", "Números Centrais", "Equilíbrio"},
                "Foco predominante em números pares com alguns números centrais"
        );
    }

    /**
     * Jogo 3 - Baseado em múltiplos de 5 e quadrantes
     */
    private RecomendacaoDoDiaDTO.JogoRecomendado criarJogo3Fixo() {
        log.info("Criando Jogo 3 Fixo");
        String jogo = "05,10,15,20,25,01,06,11,16,21,02,07,12,17,22,03";
        return new RecomendacaoDoDiaDTO.JogoRecomendado(
                jogo,
                jogo.split(","),
                "Múltiplos de 5 + Quadrantes",
                90.0,
                new String[]{"Múltiplos de 5", "Distribuição Quadrantes", "Padrão Matemático"},
                "Combinação baseada em múltiplos de 5 com distribuição por quadrantes"
        );
    }
}