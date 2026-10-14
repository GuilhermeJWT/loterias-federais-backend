package br.com.systemgs.loterias.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para requisição de análise de jogo
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnaliseJogoRequest {

    @NotBlank(message = "O jogo não pode estar vazio")
    private String jogo;

    @Min(value = 1, message = "A quantidade de sorteios deve ser no mínimo 1")
    @Max(value = 4000, message = "A quantidade de sorteios deve ser no máximo 1000")
    private Integer ultimosSorteios;
}