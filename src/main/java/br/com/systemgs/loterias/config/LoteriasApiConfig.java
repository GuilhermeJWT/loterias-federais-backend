package br.com.systemgs.loterias.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração de propriedades de APIs de loterias
 */
@Configuration
@EnableConfigurationProperties(LoteriasApiProperties.class)
public class LoteriasApiConfig {
    // Configuração de propriedades carregada automaticamente
}