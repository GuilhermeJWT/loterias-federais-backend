package br.com.systemgs.loterias.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecomendacaoDoDiaDTO {

    private JogoRecomendado[] jogos;
    private String dataGeracao;
    private String estrategiaGeral;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JogoRecomendado {
        private String jogo;
        private String[] numeros;
        private String estrategia;
        private double pontuacao;
        private String[] padroesUtilizados;
        private String descricao;
    }
}