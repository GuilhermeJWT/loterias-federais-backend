package br.com.systemgs.loterias.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para resposta de importação
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportacaoResultadoResponse {

    private int concursoInicial;
    private int concursoFinal;
    private int totalProcessados;
    private int novosImportados;
    private int jaExistiam;
    private int erros;
    private long duracaoSegundos;
    private String mensagem;
}