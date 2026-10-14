package br.com.systemgs.loterias.controller;

import br.com.systemgs.loterias.service.LotofacilImportacaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller para importação de concursos da Lotofácil
 */
@RestController
@RequestMapping("/importacao/lotofacil")
@RequiredArgsConstructor
@Slf4j
public class LotofacilImportacaoController {

    private final LotofacilImportacaoService lotofacilImportacaoService;

    @PostMapping("/concursos")
    public ResponseEntity<String> importarConcursos(
            @RequestParam int concursoInicial,
            @RequestParam int concursoFinal) {
        lotofacilImportacaoService.importarConcursos(concursoInicial, concursoFinal);
        return ResponseEntity.ok("Importação de concursos da Lotofácil de " + concursoInicial + " a " + concursoFinal +
                " iniciada em background. Consulte os logs para progresso.");
    }

    @PostMapping("/completa")
    public ResponseEntity<String> importarTodosConcursos() {
        var resultado = lotofacilImportacaoService.importarTodosConcursos();
        resultado.thenAccept(res -> {
            log.info("🎉 Importação completa da Lotofácil finalizada: {}", res.mensagem());
        });
        return ResponseEntity.ok("✅ Importação completa de todos os concursos da Lotofácil iniciada em background.");
    }

    @PostMapping("/ultimos")
    public ResponseEntity<String> importarUltimosConcursos(@RequestParam int quantidade) {
        lotofacilImportacaoService.importarUltimosConcursos(quantidade);
        return ResponseEntity.ok("Importação dos últimos " + quantidade + " concursos da Lotofácil iniciada em background.");
    }

    @PostMapping("/faltantes")
    public ResponseEntity<String> importarConcursosFaltantes() {
        lotofacilImportacaoService.importarConcursosFaltantes();
        return ResponseEntity.ok("Importação de concursos faltantes da Lotofácil iniciada em background.");
    }

    @GetMapping("/status")
    public ResponseEntity<StatusImportacao> verificarStatus() {
        return ResponseEntity.ok(new StatusImportacao(
                lotofacilImportacaoService.verificarUltimoConcursoSalvo(),
                lotofacilImportacaoService.verificarTotalConcursosSalvos(),
                "Status atual da importação da Lotofácil"
        ));
    }

    @GetMapping("/ultimo-concurso-salvo")
    public ResponseEntity<Integer> verificarUltimoConcursoSalvo() {
        return ResponseEntity.ok(lotofacilImportacaoService.verificarUltimoConcursoSalvo());
    }

    @GetMapping("/total-concursos-salvos")
    public ResponseEntity<Long> verificarTotalConcursosSalvos() {
        return ResponseEntity.ok(lotofacilImportacaoService.verificarTotalConcursosSalvos());
    }

    public record StatusImportacao(
            int ultimoConcursoSalvo,
            long totalConcursosSalvos,
            String mensagem
    ) {}
}