package br.com.systemgs.loterias.service;

import br.com.systemgs.loterias.client.LoteriaApiClient;
import br.com.systemgs.loterias.dto.LotofacilApiDTO;
import br.com.systemgs.loterias.mapper.LotofacilMapper;
import br.com.systemgs.loterias.model.Lotofacil;
import br.com.systemgs.loterias.service.interfaces.ILotofacilApiService;
import br.com.systemgs.loterias.service.interfaces.ILotofacilService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Implementação do serviço de integração com API da Caixa
 * Usa o cliente genérico LoteriaApiClient
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LotofacilApiService implements ILotofacilApiService {

    private final LoteriaApiClient loteriaApiClient;
    private final ILotofacilService lotofacilService;
    private final LotofacilMapper lotofacilMapper;

    private static final String JOGO_LOTOFACIL = "lotofacil";

    @Override
    public Lotofacil buscarESalvarUltimoConcurso() {
        log.info("Buscando último concurso da API da Caixa para {}", JOGO_LOTOFACIL);
        LotofacilApiDTO apiDTO = loteriaApiClient.buscarUltimoConcurso(JOGO_LOTOFACIL);
        Lotofacil lotofacil = lotofacilMapper.toEntity(apiDTO);
        return lotofacilService.salvar(lotofacil);
    }

    @Override
    public Lotofacil buscarESalvarConcursoEspecifico(Integer concurso) {
        log.info("Buscando concurso {} da API da Caixa para {}", concurso, JOGO_LOTOFACIL);
        LotofacilApiDTO apiDTO = loteriaApiClient.buscarConcursoEspecifico(JOGO_LOTOFACIL, concurso);
        Lotofacil lotofacil = lotofacilMapper.toEntity(apiDTO);
        return lotofacilService.salvar(lotofacil);
    }

    @Override
    public Lotofacil buscarUltimoConcursoApi() {
        log.info("Buscando último concurso da API da Caixa (sem salvar) para {}", JOGO_LOTOFACIL);
        LotofacilApiDTO apiDTO = loteriaApiClient.buscarUltimoConcurso(JOGO_LOTOFACIL);
        return lotofacilMapper.toEntity(apiDTO);
    }

    @Override
    public Lotofacil buscarConcursoEspecificoApi(Integer concurso) {
        log.info("Buscando concurso {} da API da Caixa (sem salvar) para {}", concurso, JOGO_LOTOFACIL);
        LotofacilApiDTO apiDTO = loteriaApiClient.buscarConcursoEspecifico(JOGO_LOTOFACIL, concurso);
        return lotofacilMapper.toEntity(apiDTO);
    }
}