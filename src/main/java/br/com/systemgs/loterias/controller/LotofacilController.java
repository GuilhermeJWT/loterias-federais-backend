package br.com.systemgs.loterias.controller;

import br.com.systemgs.loterias.model.Lotofacil;
import br.com.systemgs.loterias.service.interfaces.ILotofacilApiService;
import br.com.systemgs.loterias.service.interfaces.ILotofacilService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para operações de Lotofacil
 */
@RestController
@RequestMapping("/lotofacil")
@RequiredArgsConstructor
public class LotofacilController {

    private final ILotofacilService lotofacilService;
    private final ILotofacilApiService lotofacilApiService;

    @GetMapping("/concurso/{concurso}")
    public ResponseEntity<Lotofacil> buscarPorConcurso(@PathVariable Integer concurso) {
        return lotofacilService.buscarPorConcurso(concurso)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/todos")
    public ResponseEntity<List<Lotofacil>> buscarTodos() {
        return ResponseEntity.ok(lotofacilService.buscarTodos());
    }

    @GetMapping("/ultimos/{quantidade}")
    public ResponseEntity<List<Lotofacil>> buscarUltimosSorteios(@PathVariable int quantidade) {
        return ResponseEntity.ok(lotofacilService.buscarUltimosSorteios(quantidade));
    }

    @GetMapping("/api/ultimo")
    public ResponseEntity<Lotofacil> buscarUltimoConcursoApi() {
        return ResponseEntity.ok(lotofacilApiService.buscarUltimoConcursoApi());
    }

    @GetMapping("/api/concurso/{concurso}")
    public ResponseEntity<Lotofacil> buscarConcursoEspecificoApi(@PathVariable Integer concurso) {
        return ResponseEntity.ok(lotofacilApiService.buscarConcursoEspecificoApi(concurso));
    }

    @PostMapping("/api/salvar-ultimo")
    public ResponseEntity<Lotofacil> salvarUltimoConcurso() {
        return ResponseEntity.ok(lotofacilApiService.buscarESalvarUltimoConcurso());
    }

    @PostMapping("/api/salvar/{concurso}")
    public ResponseEntity<Lotofacil> salvarConcursoEspecifico(@PathVariable Integer concurso) {
        return ResponseEntity.ok(lotofacilApiService.buscarESalvarConcursoEspecifico(concurso));
    }

    @GetMapping("/total")
    public ResponseEntity<Long> contarTotalSorteios() {
        return ResponseEntity.ok(lotofacilService.contarTotalSorteios());
    }

    @GetMapping("/ultimo")
    public ResponseEntity<Lotofacil> buscarUltimoConcurso() {
        List<Lotofacil> ultimos = lotofacilService.buscarUltimosSorteios(1);
        return ultimos.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(ultimos.get(0));
    }

    @GetMapping("/frequencia")
    public ResponseEntity<java.util.Map<Integer, Integer>> buscarFrequenciaNumeros(
            @RequestParam(defaultValue = "100") int ultimosSorteios) {
        List<Lotofacil> sorteios = lotofacilService.buscarUltimosSorteios(ultimosSorteios);
        java.util.Map<Integer, Integer> frequencia = new java.util.HashMap<>();

        for (int i = 1; i <= 25; i++) {
            frequencia.put(i, 0);
        }

        for (Lotofacil sorteio : sorteios) {
            String[] dezenas = sorteio.getDezenas().split(",");
            for (String dezena : dezenas) {
                int numero = Integer.parseInt(dezena.trim());
                frequencia.put(numero, frequencia.getOrDefault(numero, 0) + 1);
            }
        }

        return ResponseEntity.ok(frequencia);
    }
}