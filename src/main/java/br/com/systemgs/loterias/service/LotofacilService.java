package br.com.systemgs.loterias.service;

import br.com.systemgs.loterias.model.Lotofacil;
import br.com.systemgs.loterias.repository.LotofacilRepository;
import br.com.systemgs.loterias.service.interfaces.ILotofacilService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementação do serviço de Lotofacil
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LotofacilService implements ILotofacilService {

    private final LotofacilRepository lotofacilRepository;

    @Override
    public Optional<Lotofacil> buscarPorConcurso(Integer concurso) {
        log.debug("Buscando concurso: {}", concurso);
        return lotofacilRepository.findByConcurso(concurso);
    }

    @Override
    public List<Lotofacil> buscarTodos() {
        log.debug("Buscando todos os concursos");
        return lotofacilRepository.findAll();
    }

    @Override
    public List<Lotofacil> buscarUltimosSorteios(int quantidade) {
        log.debug("Buscando últimos {} concursos", quantidade);
        List<Lotofacil> todos = lotofacilRepository.findOrderByConcursoDesc();
        return todos.stream()
                .limit(quantidade)
                .toList();
    }

    @Override
    @Transactional
    public Lotofacil salvar(Lotofacil lotofacil) {
        if (lotofacilRepository.existsByConcurso(lotofacil.getConcurso())) {
            log.info("Concurso {} já existe no banco, atualizando...", lotofacil.getConcurso());
            return lotofacilRepository.findByConcurso(lotofacil.getConcurso())
                    .map(existing -> {
                        lotofacil.setId(existing.getId());
                        return lotofacilRepository.save(lotofacil);
                    })
                    .orElse(lotofacilRepository.save(lotofacil));
        }
        log.info("Salvando novo concurso: {}", lotofacil.getConcurso());
        return lotofacilRepository.save(lotofacil);
    }

    @Override
    @Transactional
    public List<Lotofacil> salvarTodos(List<Lotofacil> lotofacils) {
        log.info("Salvando {} concursos", lotofacils.size());
        return lotofacils.stream()
                .map(this::salvar)
                .toList();
    }

    @Override
    public long contarTotalSorteios() {
        return lotofacilRepository.count();
    }
}