package br.com.systemgs.loterias.validator;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Validator responsável pela validação de jogos
 */
@Component
public class JogoValidator {

    private static final int QUANTIDADE_NUMEROS_LOTOFACIL = 15;
    private static final int MAXIMO_NUMERO_LOTOFACIL = 25;

    /**
     * Valida e converte uma string de jogo para lista de inteiros
     * @param jogo String contendo o jogo (números separados por vírgula ou espaço)
     * @return Lista de inteiros ordenada
     * @throws IllegalArgumentException se o formato for inválido
     */
    public List<Integer> validarEConverterJogo(String jogo) {
        if (jogo == null || jogo.trim().isEmpty()) {
            throw new IllegalArgumentException("O jogo não pode estar vazio");
        }

        List<Integer> numeros;
        try {
            numeros = Arrays.stream(jogo.split("[,\\s]+"))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Formato inválido do jogo. Use números separados por vírgula ou espaço");
        }

        if (numeros.size() != QUANTIDADE_NUMEROS_LOTOFACIL) {
            throw new IllegalArgumentException(
                    String.format("O jogo deve conter exatamente %d números. Encontrados: %d",
                            QUANTIDADE_NUMEROS_LOTOFACIL, numeros.size()));
        }

        // Verificar duplicatas
        long numerosUnicos = numeros.stream().distinct().count();
        if (numerosUnicos != numeros.size()) {
            throw new IllegalArgumentException("O jogo não pode conter números duplicados");
        }

        // Verificar range dos números
        for (Integer numero : numeros) {
            if (numero < 1 || numero > MAXIMO_NUMERO_LOTOFACIL) {
                throw new IllegalArgumentException(
                        String.format("Números devem estar entre 1 e %d. Encontrado: %d",
                                MAXIMO_NUMERO_LOTOFACIL, numero));
            }
        }

        return numeros.stream().sorted().toList();
    }

    /**
     * Valida uma string de dezenas para lista de inteiros
     * @param dezenas String contendo as dezenas (números separados por vírgula)
     * @return Lista de inteiros ordenada
     */
    public List<Integer> validarEConverterDezenas(String dezenas) {
        if (dezenas == null || dezenas.trim().isEmpty()) {
            return List.of();
        }

        try {
            return Arrays.stream(dezenas.split(","))
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .sorted()
                    .collect(Collectors.toList());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Formato inválido das dezenas");
        }
    }
}