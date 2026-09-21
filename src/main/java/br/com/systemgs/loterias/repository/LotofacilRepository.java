package br.com.systemgs.loterias.repository;

import br.com.systemgs.loterias.model.Lotofacil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LotofacilRepository extends JpaRepository<Lotofacil, Long> {

    Optional<Lotofacil> findByConcurso(Integer concurso);

    @Query("SELECT l FROM Lotofacil l ORDER BY l.concurso DESC")
    List<Lotofacil> findOrderByConcursoDesc();

    boolean existsByConcurso(Integer concurso);
}