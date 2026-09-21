package br.com.systemgs.loterias.controller;

import br.com.systemgs.loterias.service.ImportacaoService;
import br.com.systemgs.loterias.service.interfaces.IImportacaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

/**
 * Controller para operações de importação de concursos
 */
@RestController
@RequestMapping("/importacao")
@RequiredArgsConstructor
@Slf4j
public class ImportacaoController {

    private final IImportacaoService importacaoService;

    @PostMapping("/concursos")
    public ResponseEntity<String> importarConcursos(
            @RequestParam int concursoInicial,
            @RequestParam int concursoFinal) {

        if (concursoInicial < 1 || concursoFinal < concursoInicial) {
            return ResponseEntity.badRequest()
                    .body("Parâmetros inválidos: concursoInicial deve ser >= 1 e concursoFinal >= concursoInicial");
        }

        CompletableFuture<ImportacaoService.ImportacaoResultado> resultado =
                importacaoService.importarConcursos(concursoInicial, concursoFinal);

        return ResponseEntity.ok("Importação de concursos " + concursoInicial + " a " + concursoFinal +
                " iniciada em background. Consulte os logs para progresso.");
    }

    @PostMapping("/todos")
    public ResponseEntity<String> importarTodosConcursos() {
        log.info("📡 Requisição para importar todos os concursos recebida");

        CompletableFuture<ImportacaoService.ImportacaoResultado> resultado =
                importacaoService.importarTodosConcursos();

        resultado.thenAccept(res -> {
            log.info("🎉 Importação completa finalizada: {}", res.mensagem());
        });

        return ResponseEntity.ok("✅ Importação completa de todos os concursos iniciada em background. " +
                "Este processo pode levar várias horas. " +
                "Use GET /importacao/status para acompanhar o progresso.");
    }

    @PostMapping("/lotofacil-completa")
    public ResponseEntity<String> importarLotofacilCompleta() {
        log.info("📡 Requisição para importar Lotofacil completa (1 a 3784) recebida");

        CompletableFuture<ImportacaoService.ImportacaoResultado> resultado =
                importacaoService.importarConcursos(1, 3784);

        resultado.thenAccept(res -> {
            log.info("🎉 Importação Lotofacil completa finalizada: {}", res.mensagem());
        });

        return ResponseEntity.ok("✅ Importação completa da Lotofacil (concursos 1 a 3784) iniciada em background. " +
                "Este processo pode levar várias horas devido ao rate limiting. " +
                "Use GET /importacao/status para acompanhar o progresso. " +
                "Consulte os logs para progresso detalhado em tempo real.");
    }

    @PostMapping("/ultimos")
    public ResponseEntity<String> importarUltimosConcursos(@RequestParam int quantidade) {

        if (quantidade < 1 || quantidade > 1000) {
            return ResponseEntity.badRequest()
                    .body("Quantidade deve ser entre 1 e 1000");
        }

        CompletableFuture<ImportacaoService.ImportacaoResultado> resultado =
                importacaoService.importarUltimosConcursos(quantidade);

        return ResponseEntity.ok("Importação dos últimos " + quantidade + " concursos iniciada em background.");
    }

    @PostMapping("/faltantes")
    public ResponseEntity<String> importarConcursosFaltantes() {
        log.info("📡 Requisição para importar concursos faltantes recebida");

        CompletableFuture<ImportacaoService.ImportacaoResultado> resultado =
                importacaoService.importarConcursosFaltantes();

        return ResponseEntity.ok("Importação de concursos faltantes iniciada em background. " +
                "Consulte os logs para progresso.");
    }

    @GetMapping("/status")
    public ResponseEntity<StatusImportacao> verificarStatus() {
        int ultimoConcursoSalvo = importacaoService.verificarUltimoConcursoSalvo();
        int totalConcursosSalvos = importacaoService.verificarTotalConcursosSalvos();

        return ResponseEntity.ok(new StatusImportacao(
                ultimoConcursoSalvo,
                totalConcursosSalvos,
                "Status atual da importação"
        ));
    }

    @GetMapping("/ultimo-concurso-salvo")
    public ResponseEntity<Integer> verificarUltimoConcursoSalvo() {
        return ResponseEntity.ok(importacaoService.verificarUltimoConcursoSalvo());
    }

    @GetMapping("/total-concursos-salvos")
    public ResponseEntity<Integer> verificarTotalConcursosSalvos() {
        return ResponseEntity.ok(importacaoService.verificarTotalConcursosSalvos());
    }

    public record StatusImportacao(
            int ultimoConcursoSalvo,
            int totalConcursosSalvos,
            String mensagem
    ) {}
}