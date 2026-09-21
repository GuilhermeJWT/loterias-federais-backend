package br.com.systemgs.loterias.service.interfaces;

import br.com.systemgs.loterias.dto.AnaliseJogoDTO;

/**
 * Interface para operações de análise de jogos
 */
public interface IAnaliseJogoService {

    /**
     * Analisa um jogo contra todos os sorteios históricos
     * @param jogoParaAnalise Jogo a ser analisado (15 números separados por vírgula ou espaço)
     * @return Resultado da análise
     */
    AnaliseJogoDTO analisarJogo(String jogoParaAnalise);

    /**
     * Analisa um jogo contra os últimos X sorteios
     * @param jogoParaAnalise Jogo a ser analisado
     * @param ultimosSorteios Quantidade de sorteios a analisar
     * @return Resultado da análise
     */
    AnaliseJogoDTO analisarJogoUltimosSorteios(String jogoParaAnalise, int ultimosSorteios);
}