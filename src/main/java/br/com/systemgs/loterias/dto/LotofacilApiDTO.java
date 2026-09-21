package br.com.systemgs.loterias.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LotofacilApiDTO {

    @JsonProperty("numero")
    private Integer numero;

    @JsonProperty("dataApuracao")
    private String dataApuracao;

    @JsonProperty("listaDezenas")
    private List<String> listaDezenas;

    @JsonProperty("listaRateioPremio")
    private List<RateioPremio> listaRateioPremio;

    @JsonProperty("valorArrecadado")
    private Double valorArrecadado;

    @JsonProperty("valorEstimadoProximoConcurso")
    private Double valorEstimadoProximoConcurso;

    @JsonProperty("valorAcumuladoConcursoEspecial")
    private Double valorAcumuladoConcursoEspecial;

    @JsonProperty("acumulado")
    private Boolean acumulado;

    @JsonProperty("observacao")
    private String observacao;

    @JsonProperty("localSorteio")
    private String localSorteio;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RateioPremio {
        @JsonProperty("descricaoFaixa")
        private String descricaoFaixa;

        @JsonProperty("faixa")
        private Integer faixa;

        @JsonProperty("numeroDeGanhadores")
        private Integer numeroDeGanhadores;

        @JsonProperty("valorPremio")
        private Double valorPremio;
    }
}