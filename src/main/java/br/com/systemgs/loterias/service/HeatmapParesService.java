package br.com.systemgs.loterias.service;

import br.com.systemgs.loterias.dto.HeatmapParesDTO;
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
public class HeatmapParesService {

    private final ILotofacilService lotofacilService;

    public HeatmapParesDTO gerarHeatmapPares() {
        log.info("INICIANDO GERAÇÃO DE HEATMAP DE PARES");

        try {
            List<Lotofacil> todosSorteios = lotofacilService.buscarTodos();
            log.info("Total de sorteios encontrados: {}", todosSorteios.size());

            if (todosSorteios.isEmpty()) {
                log.warn("Nenhum sorteio encontrado");
                return criarResultadoPadrao();
            }

            // Inicializar matriz 25x25
            int[][] matriz = new int[25][25];

            // Para cada sorteio, incrementar contagem de todos os pares
            for (Lotofacil sorteio : todosSorteios) {
                List<Integer> numeros = parseDezenas(sorteio.getDezenas());

                // Gerar todos os pares possíveis deste sorteio
                for (int i = 0; i < numeros.size(); i++) {
                    for (int j = i + 1; j < numeros.size(); j++) {
                        int num1 = numeros.get(i) - 1; // Converter para 0-indexed
                        int num2 = numeros.get(j) - 1;

                        // Incrementar ambas as direções (matriz simétrica)
                        matriz[num1][num2]++;
                        matriz[num2][num1]++;
                    }
                }
            }

            log.info("Heatmap gerado com sucesso");

            return new HeatmapParesDTO(
                    matriz,
                    LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    "Matriz de frequência de pares de números (25x25)",
                    todosSorteios.size()
            );

        } catch (Exception e) {
            log.error("ERRO AO GERAR HEATMAP: {}", e.getMessage(), e);
            return criarResultadoPadrao();
        }
    }

    private List<Integer> parseDezenas(String dezenas) {
        List<Integer> numeros = new ArrayList<>();
        String[] parts = dezenas.split(",");
        for (String part : parts) {
            numeros.add(Integer.parseInt(part.trim()));
        }
        Collections.sort(numeros);
        return numeros;
    }

    private HeatmapParesDTO criarResultadoPadrao() {
        int[][] matriz = new int[25][25];
        return new HeatmapParesDTO(
                matriz,
                LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                "Resultado padrão (sem dados suficientes)",
                0
        );
    }
}