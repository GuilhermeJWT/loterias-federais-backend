package br.com.systemgs.loterias.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JogoSugeridoDTO {

    private String jogo;
    private String periodo;
    private String estrategia;
    private double confianca;
    private String[] numeros; // Array para facilitar exibição
}