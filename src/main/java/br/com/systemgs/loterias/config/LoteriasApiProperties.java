package br.com.systemgs.loterias.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Propriedades de configuração das APIs de loterias
 */
@Data
@ConfigurationProperties(prefix = "loterias.api")
public class LoteriasApiProperties {

    private String baseUrl;
    private Map<String, JogoConfig> jogos = new HashMap<>();

    @Data
    public static class JogoConfig {
        private String nome;
        private String endpoint;
        private int quantidadeDezenas;
        private int maximoNumero;
    }

    /**
     * Obtém a configuração de um jogo específico
     * @param jogo Nome do jogo (ex: lotofacil, megasena)
     * @return Configuração do jogo
     */
    public JogoConfig getJogoConfig(String jogo) {
        return jogos.get(jogo.toLowerCase());
    }

    /**
     * Obtém a URL completa para um jogo específico
     * @param jogo Nome do jogo
     * @return URL completa
     */
    public String getJogoUrl(String jogo) {
        JogoConfig config = getJogoConfig(jogo);
        if (config == null) {
            throw new IllegalArgumentException("Jogo não configurado: " + jogo);
        }
        return baseUrl + "/" + config.getEndpoint();
    }

    /**
     * Obtém a URL completa para um concurso específico
     * @param jogo Nome do jogo
     * @param concurso Número do concurso
     * @return URL completa do concurso
     */
    public String getConcursoUrl(String jogo, Integer concurso) {
        return getJogoUrl(jogo) + "/" + concurso;
    }
}