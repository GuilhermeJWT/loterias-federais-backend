package br.com.systemgs.loterias.service.interfaces;

import br.com.systemgs.loterias.service.ImportacaoService;

import java.util.concurrent.CompletableFuture;

/**
 * Interface para operações de importação de concursos
 */
public interface IImportacaoService {

    /**
     * Importa concursos em um intervalo específico
     * @param concursoInicial Concurso inicial
     * @param concursoFinal Concurso final
     * @return CompletableFuture com resultado da importação
     */
    CompletableFuture<ImportacaoService.ImportacaoResultado> importarConcursos(int concursoInicial, int concursoFinal);

    /**
     * Importa todos os concursos disponíveis na API
     * @return CompletableFuture com resultado da importação
     */
    CompletableFuture<ImportacaoService.ImportacaoResultado> importarTodosConcursos();

    /**
     * Importa os últimos X concursos
     * @param quantidade Quantidade de concursos a importar
     * @return CompletableFuture com resultado da importação
     */
    CompletableFuture<ImportacaoService.ImportacaoResultado> importarUltimosConcursos(int quantidade);

    /**
     * Importa apenas os concursos que estão faltando no banco
     * @return CompletableFuture com resultado da importação
     */
    CompletableFuture<ImportacaoService.ImportacaoResultado> importarConcursosFaltantes();

    /**
     * Verifica o último concurso salvo no banco
     * @return Número do último concurso salvo
     */
    int verificarUltimoConcursoSalvo();

    /**
     * Verifica o total de concursos salvos no banco
     * @return Total de concursos salvos
     */
    int verificarTotalConcursosSalvos();
}