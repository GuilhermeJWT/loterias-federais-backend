package br.com.systemgs.loterias.service;

import br.com.systemgs.loterias.dto.MelhoresJogosHistoricosDTO;
import br.com.systemgs.loterias.model.Lotofacil;
import br.com.systemgs.loterias.service.interfaces.ILotofacilService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MelhoresJogosHistoricosService {

    private final ILotofacilService lotofacilService;

    /**
     * Analisa todos os jogos históricos e encontra os 3 com melhor performance
     */
    public MelhoresJogosHistoricosDTO analisarMelhoresJogosHistoricos() {
        log.info("INICIANDO ANÁLISE DE MELHORES JOGOS HISTÓRICOS");

        try {
            List<Lotofacil> todosSorteios = lotofacilService.buscarTodos();
            log.info("Total de sorteios encontrados: {}", todosSorteios.size());

            if (todosSorteios.isEmpty()) {
                log.warn("Nenhum sorteio encontrado na base de dados");
                return criarResultadoPadrao();
            }

            // Analisar performance de cada jogo único na base
            Map<String, PerformanceJogo> performanceMap = new HashMap<>();

            for (Lotofacil sorteioBase : todosSorteios) {
                String jogoBase = sorteioBase.getDezenas();
                PerformanceJogo perf = performanceMap.computeIfAbsent(jogoBase, k -> new PerformanceJogo(jogoBase));

                // Comparar este jogo com todos os outros sorteios
                for (Lotofacil sorteioComparado : todosSorteios) {
                    if (sorteioBase.getConcurso().equals(sorteioComparado.getConcurso())) {
                        continue; // Não comparar consigo mesmo
                    }

                    int acertos = contarAcertos(jogoBase, sorteioComparado.getDezenas());
                    perf.adicionarAcerto(acertos);
                }
            }

            log.info("Total de jogos únicos analisados: {}", performanceMap.size());

            // Calcular pontuação para cada jogo
            List<PerformanceJogo> listaPerformances = new ArrayList<>(performanceMap.values());
            listaPerformances.forEach(PerformanceJogo::calcularPontuacao);

            // Ordenar por pontuação total (descendente)
            listaPerformances.sort((p1, p2) -> Double.compare(p2.getPontuacaoTotal(), p1.getPontuacaoTotal()));

            // Pegar os top 3
            List<PerformanceJogo> top3 = new ArrayList<>();
            int count = 0;
            for (PerformanceJogo p : listaPerformances) {
                if (count >= 3) break;
                top3.add(p);
                count++;
            }

            log.info("TOP 3 JOGOS ENCONTRADOS:");
            for (int i = 0; i < top3.size(); i++) {
                PerformanceJogo p = top3.get(i);
                log.info("Jogo {}: {} - Pontuação: {} (15:{}, 14:{}, 13:{}, 12:{}, 11:{})",
                        i + 1, p.getJogo(), p.getPontuacaoTotal(),
                        p.getAcertos15(), p.getAcertos14(), p.getAcertos13(),
                        p.getAcertos12(), p.getAcertos11());
            }

            // Converter para DTO
            List<MelhoresJogosHistoricosDTO.JogoHistorico> jogosList = new ArrayList<>();
            for (PerformanceJogo p : top3) {
                jogosList.add(converterParaDTO(p));
            }

            return new MelhoresJogosHistoricosDTO(
                    jogosList.toArray(new MelhoresJogosHistoricosDTO.JogoHistorico[0]),
                    LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    "Análise completa de todos os jogos históricos contra todos os sorteios"
            );

        } catch (Exception e) {
            log.error("ERRO AO ANALISAR MELHORES JOGOS: {}", e.getMessage(), e);
            return criarResultadoPadrao();
        }
    }

    private int contarAcertos(String jogo1, String jogo2) {
        String[] nums1 = jogo1.split(",");
        String[] nums2 = jogo2.split(",");

        Set<Integer> set2 = new HashSet<>();
        for (String n : nums2) {
            set2.add(Integer.parseInt(n.trim()));
        }

        int acertos = 0;
        for (String n : nums1) {
            if (set2.contains(Integer.parseInt(n.trim()))) {
                acertos++;
            }
        }

        return acertos;
    }

    private MelhoresJogosHistoricosDTO.JogoHistorico converterParaDTO(PerformanceJogo perf) {
        String[] numeros = perf.getJogo().split(",");
        return new MelhoresJogosHistoricosDTO.JogoHistorico(
                perf.getJogo(),
                numeros,
                perf.getAcertos15(),
                perf.getAcertos14(),
                perf.getAcertos13(),
                perf.getAcertos12(),
                perf.getAcertos11(),
                perf.getPontuacaoTotal(),
                "Jogo com performance histórica excepcional"
        );
    }

    private MelhoresJogosHistoricosDTO criarResultadoPadrao() {
        log.info("Criando resultado padrão");
        MelhoresJogosHistoricosDTO.JogoHistorico jogo1 = new MelhoresJogosHistoricosDTO.JogoHistorico(
                "01,02,03,04,05,06,07,08,09,10,11,12,13,14,15",
                new String[]{"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15"},
                0, 0, 0, 0, 0, 0.0,
                "Resultado padrão (sem dados suficientes)"
        );

        MelhoresJogosHistoricosDTO.JogoHistorico jogo2 = new MelhoresJogosHistoricosDTO.JogoHistorico(
                "02,04,06,08,10,12,14,16,18,20,22,24,01,03,05",
                new String[]{"02","04","06","08","10","12","14","16","18","20","22","24","01","03","05"},
                0, 0, 0, 0, 0, 0.0,
                "Resultado padrão (sem dados suficientes)"
        );

        MelhoresJogosHistoricosDTO.JogoHistorico jogo3 = new MelhoresJogosHistoricosDTO.JogoHistorico(
                "05,10,15,20,25,01,06,11,16,21,02,07,12,17,22",
                new String[]{"05","10","15","20","25","01","06","11","16","21","02","07","12","17","22"},
                0, 0, 0, 0, 0, 0.0,
                "Resultado padrão (sem dados suficientes)"
        );

        return new MelhoresJogosHistoricosDTO(
                new MelhoresJogosHistoricosDTO.JogoHistorico[]{jogo1, jogo2, jogo3},
                LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                "Resultado padrão (sem dados históricos suficientes)"
        );
    }

    /**
     * Classe auxiliar para armazenar performance de um jogo
     */
    private static class PerformanceJogo {
        private final String jogo;
        private int acertos15 = 0;
        private int acertos14 = 0;
        private int acertos13 = 0;
        private int acertos12 = 0;
        private int acertos11 = 0;
        private double pontuacaoTotal = 0.0;

        public PerformanceJogo(String jogo) {
            this.jogo = jogo;
        }

        public void adicionarAcerto(int acertos) {
            switch (acertos) {
                case 15 -> acertos15++;
                case 14 -> acertos14++;
                case 13 -> acertos13++;
                case 12 -> acertos12++;
                case 11 -> acertos11++;
            }
        }

        public void calcularPontuacao() {
            // Pontuação ponderada: 15 acertos vale muito mais que 14, etc.
            pontuacaoTotal = (acertos15 * 100.0) +
                            (acertos14 * 50.0) +
                            (acertos13 * 20.0) +
                            (acertos12 * 10.0) +
                            (acertos11 * 5.0);
        }

        public String getJogo() {
            return jogo;
        }

        public int getAcertos15() {
            return acertos15;
        }

        public int getAcertos14() {
            return acertos14;
        }

        public int getAcertos13() {
            return acertos13;
        }

        public int getAcertos12() {
            return acertos12;
        }

        public int getAcertos11() {
            return acertos11;
        }

        public double getPontuacaoTotal() {
            return pontuacaoTotal;
        }
    }
}