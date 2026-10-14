package br.com.systemgs.loterias.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HeatmapParesDTO {

    private int[][] matriz; // 25x25 matriz de frequências
    private String dataGeracao;
    private String descricao;
    private int totalSorteios;
}