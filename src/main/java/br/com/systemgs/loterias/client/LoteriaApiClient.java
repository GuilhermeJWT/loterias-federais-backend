package br.com.systemgs.loterias.client;

import br.com.systemgs.loterias.config.LoteriasApiProperties;
import br.com.systemgs.loterias.dto.LotofacilApiDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Cliente genérico para APIs de loterias
 * Pode ser usado para Lotofacil, Mega-Sena, Lotomania, Quina, etc.
 */
@Component
@RequiredArgsConstructor
public class LoteriaApiClient {

    private final LoteriasApiProperties apiProperties;
    private final RestTemplate restTemplate;

    /**
     * Busca o último concurso de um jogo específico
     * @param jogo Nome do jogo (ex: lotofacil, megasena)
     * @return DTO com os dados do concurso
     */
    public LotofacilApiDTO buscarUltimoConcurso(String jogo) {
        String url = apiProperties.getJogoUrl(jogo);
        return restTemplate.getForObject(url, LotofacilApiDTO.class);
    }

    /**
     * Busca um concurso específico de um jogo
     * @param jogo Nome do jogo (ex: lotofacil, megasena)
     * @param concurso Número do concurso
     * @return DTO com os dados do concurso
     */
    public LotofacilApiDTO buscarConcursoEspecifico(String jogo, Integer concurso) {
        String url = apiProperties.getConcursoUrl(jogo, concurso);
        return restTemplate.getForObject(url, LotofacilApiDTO.class);
    }

    /**
     * Obtém a configuração de um jogo
     * @param jogo Nome do jogo
     * @return Configuração do jogo
     */
    public LoteriasApiProperties.JogoConfig getJogoConfig(String jogo) {
        return apiProperties.getJogoConfig(jogo);
    }
}