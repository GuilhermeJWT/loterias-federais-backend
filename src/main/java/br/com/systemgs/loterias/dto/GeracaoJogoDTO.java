package br.com.systemgs.loterias.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeracaoJogoDTO {

    private String nome;
    private List<String> jogos;
    private String descricao;
    private int quantidadeJogos;
}