package br.com.systemgs.loterias.service;

import br.com.systemgs.loterias.dto.AnaliseJogoDTO;
import br.com.systemgs.loterias.model.Lotofacil;
import br.com.systemgs.loterias.service.interfaces.IAnaliseJogoService;
import br.com.systemgs.loterias.service.interfaces.ILotofacilService;
import br.com.systemgs.loterias.validator.JogoValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Implementação do serviço de análise de jogos
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AnaliseJogoService implements IAnaliseJogoService {

    private final ILotofacilService lotofacilService;
    private final JogoValidator jogoValidator;

    @Override
    public AnaliseJogoDTO analisarJogo(String jogoParaAnalise) {
        log.info("Analisando jogo: {}", jogoParaAnalise);

        List<Integer> numerosJogo = jogoValidator.validarEConverterJogo(jogoParaAnalise);
        List<Lotofacil> todosSorteios = lotofacilService.buscarTodos();
        List<AnaliseJogoDTO.ResultadoAnalise> resultados = new ArrayList<>();

        int count15 = 0, count14 = 0, count13 = 0, count12 = 0, count11 = 0;

        for (Lotofacil sorteio : todosSorteios) {
            List<Integer> numerosSorteio = jogoValidator.validarEConverterDezenas(sorteio.getDezenas());
            int acertos = contarAcertos(numerosJogo, numerosSorteio);

            if (acertos >= 11) {
                List<Integer> numerosAcertados = numerosJogo.stream()
                        .filter(numerosSorteio::contains)
                        .sorted()
                        .toList();

                AnaliseJogoDTO.ResultadoAnalise resultado = new AnaliseJogoDTO.ResultadoAnalise(
                        sorteio.getConcurso(),
                        sorteio.getData().toString(),
                        sorteio.getDezenas(),
                        acertos,
                        formatarNumeros(numerosAcertados)
                );
                resultados.add(resultado);

                switch (acertos) {
                    case 15 -> count15++;
                    case 14 -> count14++;
                    case 13 -> count13++;
                    case 12 -> count12++;
                    case 11 -> count11++;
                }
            }
        }

        resultados.sort((r1, r2) -> Integer.compare(r2.getAcertos(), r1.getAcertos()));

        return new AnaliseJogoDTO(
                jogoParaAnalise,
                resultados,
                todosSorteios.size(),
                count14,
                count15,
                count13,
                count12,
                count11
        );
    }

    @Override
    public AnaliseJogoDTO analisarJogoUltimosSorteios(String jogoParaAnalise, int ultimosSorteios) {
        log.info("Analisando jogo {} nos últimos {} sorteios", jogoParaAnalise, ultimosSorteios);

        List<Integer> numerosJogo = jogoValidator.validarEConverterJogo(jogoParaAnalise);
        List<Lotofacil> ultimosSorteiosList = lotofacilService.buscarUltimosSorteios(ultimosSorteios);
        List<AnaliseJogoDTO.ResultadoAnalise> resultados = new ArrayList<>();

        int count15 = 0, count14 = 0, count13 = 0, count12 = 0, count11 = 0;

        for (Lotofacil sorteio : ultimosSorteiosList) {
            List<Integer> numerosSorteio = jogoValidator.validarEConverterDezenas(sorteio.getDezenas());
            int acertos = contarAcertos(numerosJogo, numerosSorteio);

            if (acertos >= 11) {
                List<Integer> numerosAcertados = numerosJogo.stream()
                        .filter(numerosSorteio::contains)
                        .sorted()
                        .toList();

                AnaliseJogoDTO.ResultadoAnalise resultado = new AnaliseJogoDTO.ResultadoAnalise(
                        sorteio.getConcurso(),
                        sorteio.getData().toString(),
                        sorteio.getDezenas(),
                        acertos,
                        formatarNumeros(numerosAcertados)
                );
                resultados.add(resultado);

                switch (acertos) {
                    case 15 -> count15++;
                    case 14 -> count14++;
                    case 13 -> count13++;
                    case 12 -> count12++;
                    case 11 -> count11++;
                }
            }
        }

        resultados.sort((r1, r2) -> Integer.compare(r2.getAcertos(), r1.getAcertos()));

        return new AnaliseJogoDTO(
                jogoParaAnalise,
                resultados,
                ultimosSorteiosList.size(),
                count14,
                count15,
                count13,
                count12,
                count11
        );
    }

    private int contarAcertos(List<Integer> jogo, List<Integer> sorteio) {
        return (int) jogo.stream().filter(sorteio::contains).count();
    }

    private String formatarNumeros(List<Integer> numeros) {
        return numeros.stream()
                .map(String::valueOf)
                .collect(java.util.stream.Collectors.joining(","));
    }
}