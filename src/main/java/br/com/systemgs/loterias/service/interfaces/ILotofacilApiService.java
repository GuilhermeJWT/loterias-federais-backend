package br.com.systemgs.loterias.service.interfaces;

import br.com.systemgs.loterias.model.Lotofacil;

/**
 * Interface para operações de integração com API da Caixa
 */
public interface ILotofacilApiService {

    /**
     * Busca e salva o último concurso da API da Caixa
     * @return Último concurso salvo
     */
    Lotofacil buscarESalvarUltimoConcurso();

    /**
     * Busca e salva um concurso específico da API da Caixa
     * @param concurso Número do concurso
     * @return Concurso salvo
     */
    Lotofacil buscarESalvarConcursoEspecifico(Integer concurso);

    /**
     * Busca o último concurso da API da Caixa (sem salvar)
     * @return Último concurso
     */
    Lotofacil buscarUltimoConcursoApi();

    /**
     * Busca um concurso específico da API da Caixa (sem salvar)
     * @param concurso Número do concurso
     * @return Concurso específico
     */
    Lotofacil buscarConcursoEspecificoApi(Integer concurso);
}