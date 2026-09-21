package br.com.systemgs.loterias.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnaliseJogoDTO {

    private String jogo;
    private List<ResultadoAnalise> resultados;
    private int totalSorteios;
    private int acertos14;
    private int acertos15;
    private int acertos13;
    private int acertos12;
    private int acertos11;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResultadoAnalise {
        private Integer concurso;
        private String data;
        private String dezenas;
        private int acertos;
        private String numerosAcertados;
    }
}