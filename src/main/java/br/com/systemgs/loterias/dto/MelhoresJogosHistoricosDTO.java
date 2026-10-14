package br.com.systemgs.loterias.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MelhoresJogosHistoricosDTO {

    private JogoHistorico[] jogos;
    private String dataGeracao;
    private String descricao;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JogoHistorico {
        private String jogo;
        private String[] numeros;
        private int acertos15;
        private int acertos14;
        private int acertos13;
        private int acertos12;
        private int acertos11;
        private double pontuacaoTotal;
        private String descricao;
    }
}