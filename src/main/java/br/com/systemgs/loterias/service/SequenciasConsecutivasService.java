package br.com.systemgs.loterias.service;

import br.com.systemgs.loterias.dto.SequenciasConsecutivasDTO;
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
public class SequenciasConsecutivasService {

    private final ILotofacilService lotofacilService;

    public SequenciasConsecutivasDTO analisarSequenciasConsecutivas() {
        log.info("INICIANDO ANÁLISE DE SEQUÊNCIAS CONSECUTIVAS");

        try {
            List<Lotofacil> todosSorteios = lotofacilService.buscarTodos();
            log.info("Total de sorteios encontrados: {}", todosSorteios.size());

            if (todosSorteios.isEmpty()) {
                log.warn("Nenhum sorteio encontrado");
                return criarResultadoPadrao();
            }

            // Ordenar por concurso (do mais antigo para o mais recente)
            todosSorteios.sort(Comparator.comparing(Lotofacil::getConcurso));

            // Analisar repetições consecutivas para cada número
            Map<Integer, AnaliseNumero> analisePorNumero = new HashMap<>();

            for (int i = 1; i <= 25; i++) {
                analisePorNumero.put(i, new AnaliseNumero(i));
            }

            // Análise de repetições consecutivas
            for (int i = 0; i < todosSorteios.size(); i++) {
                Lotofacil sorteio = todosSorteios.get(i);
                Set<Integer> numerosSorteio = parseDezenas(sorteio.getDezenas());

                for (int num = 1; num <= 25; num++) {
                    AnaliseNumero analise = analisePorNumero.get(num);

                    if (numerosSorteio.contains(num)) {
                        analise.incrementarSequenciaAtual();
                    } else {
                        if (analise.getSequenciaAtual() > analise.getMaiorSequencia()) {
                            analise.setMaiorSequencia(analise.getSequenciaAtual());
                        }
                        analise.resetarSequenciaAtual();
                    }

                    // Contar nos últimos 30
                    if (i >= todosSorteios.size() - 30) {
                        if (numerosSorteio.contains(num)) {
                            analise.incrementarRepeticoesUltimos30();
                        }
                    }
                }
            }

            // Finalizar análises
            for (AnaliseNumero analise : analisePorNumero.values()) {
                if (analise.getSequenciaAtual() > analise.getMaiorSequencia()) {
                    analise.setMaiorSequencia(analise.getSequenciaAtual());
                }

                // Definir status
                if (analise.getRepeticoesUltimos30() >= 15) {
                    analise.setStatus("QUENTE");
                } else if (analise.getSequenciaAtual() >= 3) {
                    analise.setStatus("MOMENTO");
                } else {
                    analise.setStatus("FRIO");
                }
            }

            // Converter para DTO e ordenar por repetições consecutivas
            List<SequenciasConsecutivasDTO.NumeroSequencia> numerosList = new ArrayList<>();
            for (AnaliseNumero analise : analisePorNumero.values()) {
                numerosList.add(new SequenciasConsecutivasDTO.NumeroSequencia(
                        analise.getNumero(),
                        analise.getMaiorSequencia(),
                        analise.getRepeticoesUltimos30(),
                        analise.getStatus()
                ));
            }

            // Ordenar por maior sequência consecutiva
            numerosList.sort((n1, n2) -> Integer.compare(n2.getRepeticoesConsecutivas(), n1.getRepeticoesConsecutivas()));

            return new SequenciasConsecutivasDTO(
                    numerosList.toArray(new SequenciasConsecutivasDTO.NumeroSequencia[0]),
                    LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    "Análise de repetições consecutivas e momentum de números"
            );

        } catch (Exception e) {
            log.error("ERRO AO ANALISAR SEQUÊNCIAS: {}", e.getMessage(), e);
            return criarResultadoPadrao();
        }
    }

    private Set<Integer> parseDezenas(String dezenas) {
        Set<Integer> numeros = new HashSet<>();
        String[] parts = dezenas.split(",");
        for (String part : parts) {
            numeros.add(Integer.parseInt(part.trim()));
        }
        return numeros;
    }

    private SequenciasConsecutivasDTO criarResultadoPadrao() {
        SequenciasConsecutivasDTO.NumeroSequencia[] numeros = new SequenciasConsecutivasDTO.NumeroSequencia[25];
        for (int i = 0; i < 25; i++) {
            numeros[i] = new SequenciasConsecutivasDTO.NumeroSequencia(
                    i + 1, 0, 0, "FRIO"
            );
        }
        return new SequenciasConsecutivasDTO(
                numeros,
                LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                "Resultado padrão (sem dados suficientes)"
        );
    }

    private static class AnaliseNumero {
        private final int numero;
        private int sequenciaAtual = 0;
        private int maiorSequencia = 0;
        private int repeticoesUltimos30 = 0;
        private String status;

        public AnaliseNumero(int numero) {
            this.numero = numero;
        }

        public void incrementarSequenciaAtual() {
            sequenciaAtual++;
        }

        public void resetarSequenciaAtual() {
            sequenciaAtual = 0;
        }

        public void incrementarRepeticoesUltimos30() {
            repeticoesUltimos30++;
        }

        public int getNumero() {
            return numero;
        }

        public int getSequenciaAtual() {
            return sequenciaAtual;
        }

        public int getMaiorSequencia() {
            return maiorSequencia;
        }

        public void setMaiorSequencia(int maiorSequencia) {
            this.maiorSequencia = maiorSequencia;
        }

        public int getRepeticoesUltimos30() {
            return repeticoesUltimos30;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}