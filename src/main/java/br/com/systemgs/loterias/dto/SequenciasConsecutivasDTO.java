package br.com.systemgs.loterias.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SequenciasConsecutivasDTO {

    private NumeroSequencia[] numeros;
    private String dataGeracao;
    private String descricao;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NumeroSequencia {
        private int numero;
        private int repeticoesConsecutivas;
        private int repeticoesUltimos30;
        private String status; // "QUENTE", "MOMENTO", "FRIO"
    }
}