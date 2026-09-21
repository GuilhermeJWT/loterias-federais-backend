package br.com.systemgs.loterias.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lotofacil")
public class Lotofacil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private Integer concurso;

    @Column(nullable = false)
    private LocalDate data;

    @Column(nullable = false)
    private String dezenas;

    private Integer acertadores_15;
    private Integer acertadores_14;
    private Integer acertadores_13;
    private Integer acertadores_12;
    private Integer acertadores_11;

    private Double acumulou_15;
    private Double acumulou_14;
    private Double acumulou_13;
    private Double acumulou_12;
    private Double acumulou_11;

    private Double arrecadacao_total;
    private Double estimativa_premio;
    private Double acumulada_especial;

    // Campos adicionais da API
    private String local_sorteio;
    private String observacao;
    private Boolean acumulado;
}