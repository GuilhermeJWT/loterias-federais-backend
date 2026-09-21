package br.com.systemgs.loterias.service.interfaces;

import br.com.systemgs.loterias.model.Lotofacil;

import java.util.List;
import java.util.Optional;

/**
 * Interface para operações de negócio relacionadas à Lotofacil
 */
public interface ILotofacilService {

    /**
     * Busca um concurso específico pelo número
     * @param concurso Número do concurso
     * @return Optional contendo o concurso se encontrado
     */
    Optional<Lotofacil> buscarPorConcurso(Integer concurso);

    /**
     * Busca todos os concursos
     * @return Lista de todos os concursos
     */
    List<Lotofacil> buscarTodos();

    /**
     * Busca os últimos X concursos
     * @param quantidade Quantidade de concursos a buscar
     * @return Lista dos últimos concursos
     */
    List<Lotofacil> buscarUltimosSorteios(int quantidade);

    /**
     * Salva um concurso no banco de dados
     * @param lotofacil Concurso a ser salvo
     * @return Concurso salvo
     */
    Lotofacil salvar(Lotofacil lotofacil);

    /**
     * Salva uma lista de concursos no banco de dados
     * @param lotofacils Lista de concursos a serem salvos
     * @return Lista de concursos salvos
     */
    List<Lotofacil> salvarTodos(List<Lotofacil> lotofacils);

    /**
     * Conta o total de sorteios no banco de dados
     * @return Total de sorteios
     */
    long contarTotalSorteios();
}