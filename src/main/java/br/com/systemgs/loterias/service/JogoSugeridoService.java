package br.com.systemgs.loterias.service;

import br.com.systemgs.loterias.dto.JogoSugeridoDTO;
import br.com.systemgs.loterias.model.Lotofacil;
import br.com.systemgs.loterias.service.interfaces.ILotofacilService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JogoSugeridoService {

    private final ILotofacilService lotofacilService;

    /**
     * Gera jogo sugerido baseado nos últimos 5 sorteios
     * Estratégia: Foco em tendências muito recentes
     */
    public JogoSugeridoDTO gerarJogoUltimos5() {
        log.info("Gerando jogo sugerido baseado nos últimos 5 sorteios");
        List<Lotofacil> ultimos5 = lotofacilService.buscarUltimosSorteios(5);
        return gerarJogoComEstrategia(ultimos5, "Últimos 5 Sorteios", "Tendência Ultra-Recente", 0.85);
    }

    /**
     * Gera jogo sugerido baseado nos últimos 15 sorteios
     * Estratégia: Equilíbrio entre recentes e médio prazo
     */
    public JogoSugeridoDTO gerarJogoUltimos15() {
        log.info("Gerando jogo sugerido baseado nos últimos 15 sorteios");
        List<Lotofacil> ultimos15 = lotofacilService.buscarUltimosSorteios(15);
        return gerarJogoComEstrategia(ultimos15, "Últimos 15 Sorteios", "Equilíbrio Curto Prazo", 0.90);
    }

    /**
     * Gera jogo sugerido baseado nos últimos 30 sorteios
     * Estratégia: Padrões de médio prazo
     */
    public JogoSugeridoDTO gerarJogoUltimos30() {
        log.info("Gerando jogo sugerido baseado nos últimos 30 sorteios");
        List<Lotofacil> ultimos30 = lotofacilService.buscarUltimosSorteios(30);
        return gerarJogoComEstrategia(ultimos30, "Últimos 30 Sorteios", "Padrões Médio Prazo", 0.92);
    }

    /**
     * Gera jogo sugerido baseado nos últimos 100 sorteios
     * Estratégia: Tendências de longo prazo
     */
    public JogoSugeridoDTO gerarJogoUltimos100() {
        log.info("Gerando jogo sugerido baseado nos últimos 100 sorteios");
        List<Lotofacil> ultimos100 = lotofacilService.buscarUltimosSorteios(100);
        return gerarJogoComEstrategia(ultimos100, "Últimos 100 Sorteios", "Tendências Longo Prazo", 0.95);
    }

    private JogoSugeridoDTO gerarJogoComEstrategia(List<Lotofacil> sorteios, String periodo, String estrategia, double confianca) {
        // Calcular frequência de cada número nos sorteios
        Map<Integer, Integer> frequencia = new HashMap<>();
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

        // Ordenar números por frequência (decrescente)
        List<Integer> numerosOrdenados = frequencia.entrySet().stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // Estratégia híbrida:
        // - 60% dos números mais frequentes
        // - 30% dos números de média frequência
        // - 10% números aleatórios para variação
        Set<Integer> jogoSugerido = new HashSet<>();

        // Adicionar os mais frequentes (60% = 9 números)
        int quentes = 9;
        for (int i = 0; i < quentes && i < numerosOrdenados.size(); i++) {
            jogoSugerido.add(numerosOrdenados.get(i));
        }

        // Adicionar números de média frequência (30% = 4 números)
        int medios = 4;
        int inicioMedios = quentes;
        int fimMedios = Math.min(inicioMedios + medios, numerosOrdenados.size());
        for (int i = inicioMedios; i < fimMedios; i++) {
            jogoSugerido.add(numerosOrdenados.get(i));
        }

        // Completar com números aleatórios se necessário
        Random random = new Random();
        while (jogoSugerido.size() < 15) {
            int numero = random.nextInt(25) + 1;
            jogoSugerido.add(numero);
        }

        // Converter para lista ordenada
        List<Integer> jogoOrdenado = jogoSugerido.stream()
                .sorted()
                .collect(Collectors.toList());

        // Converter para string com zero à esquerda
        String jogoString = jogoOrdenado.stream()
                .map(n -> String.format("%02d", n))
                .collect(Collectors.joining(","));

        return new JogoSugeridoDTO(
                jogoString,
                periodo,
                estrategia,
                confianca,
                jogoOrdenado.stream().map(n -> String.format("%02d", n)).toArray(String[]::new)
        );
    }
}