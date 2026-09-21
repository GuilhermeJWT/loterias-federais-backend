package br.com.systemgs.loterias.client;

import br.com.systemgs.loterias.dto.LotofacilApiDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Cliente Feign específico para Lotofacil
 * Mantido para compatibilidade com código existente
 * Recomenda-se usar LoteriaApiClient para novos jogos
 */
@FeignClient(name = "lotofacilClient", url = "${loterias.api.base-url}")
public interface LotofacilClient {

    @GetMapping("/lotofacil")
    LotofacilApiDTO buscarUltimoConcurso();

    @GetMapping("/lotofacil/{concurso}")
    LotofacilApiDTO buscarConcursoEspecifico(@PathVariable("concurso") Integer concurso);
}