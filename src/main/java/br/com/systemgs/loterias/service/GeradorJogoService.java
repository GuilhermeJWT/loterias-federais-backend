package br.com.systemgs.loterias.service;

import br.com.systemgs.loterias.dto.GeracaoJogoDTO;
import br.com.systemgs.loterias.model.Lotofacil;
import br.com.systemgs.loterias.service.interfaces.IGeradorJogoService;
import br.com.systemgs.loterias.service.interfaces.ILotofacilService;
import br.com.systemgs.loterias.validator.JogoValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementação do serviço de geração de jogos
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GeradorJogoService implements IGeradorJogoService {

    private final ILotofacilService lotofacilService;
    private final JogoValidator jogoValidator;

    @Override
    public GeracaoJogoDTO gerarJogosBaseadosEmNumerosMaisSorteados(int quantidadeJogos, int ultimosSorteios) {
        log.info("Gerando {} jogos baseados nos números mais sorteados nos últimos {} sorteios",
                quantidadeJogos, ultimosSorteios);

        List<Lotofacil> ultimosSorteiosList = lotofacilService.buscarUltimosSorteios(ultimosSorteios);
        Map<Integer, Integer> frequenciaNumeros = calcularFrequenciaNumeros(ultimosSorteiosList);

        List<Integer> numerosMaisSorteados = frequenciaNumeros.entrySet().stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .toList();

        List<String> jogosGerados = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < quantidadeJogos; i++) {
            Set<Integer> jogo = new HashSet<>();

            while (jogo.size() < 15) {
                int indice = random.nextInt(Math.min(20, numerosMaisSorteados.size()));
                jogo.add(numerosMaisSorteados.get(indice));
            }

            jogosGerados.add(formatarJogo(jogo.stream().sorted().toList()));
        }

        return new GeracaoJogoDTO(
                "Números Mais Sorteados",
                jogosGerados,
                "Jogos gerados priorizando os números que mais apareceram nos últimos " +
                        ultimosSorteios + " sorteios",
                quantidadeJogos
        );
    }

    private Map<Integer, Integer> calcularFrequenciaNumeros(List<Lotofacil> sorteios) {
        Map<Integer, Integer> frequencia = new HashMap<>();

        for (int i = 1; i <= 25; i++) {
            frequencia.put(i, 0);
        }

        for (Lotofacil sorteio : sorteios) {
            List<Integer> numeros = jogoValidator.validarEConverterDezenas(sorteio.getDezenas());
            for (Integer numero : numeros) {
                frequencia.put(numero, frequencia.getOrDefault(numero, 0) + 1);
            }
        }

        return frequencia;
    }

    private String formatarJogo(List<Integer> numeros) {
        return numeros.stream()
                .map(n -> String.format("%02d", n))
                .collect(Collectors.joining(","));
    }
}