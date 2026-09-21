package br.com.systemgs.loterias.service.interfaces;

import br.com.systemgs.loterias.dto.GeracaoJogoDTO;

/**
 * Interface para operações de geração de jogos
 */
public interface IGeradorJogoService {

    /**
     * Gera jogos baseados nos números mais sorteados
     * @param quantidadeJogos Quantidade de jogos a gerar
     * @param ultimosSorteios Base de análise (últimos X sorteios)
     * @return Jogos gerados
     */
    GeracaoJogoDTO gerarJogosBaseadosEmNumerosMaisSorteados(int quantidadeJogos, int ultimosSorteios);
}