package br.com.systemgs.loterias.controller;

import br.com.systemgs.loterias.dto.AnaliseJogoDTO;
import br.com.systemgs.loterias.dto.GeracaoJogoDTO;
import br.com.systemgs.loterias.dto.request.AnaliseJogoRequest;
import br.com.systemgs.loterias.service.interfaces.IAnaliseJogoService;
import br.com.systemgs.loterias.service.interfaces.IGeradorJogoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller para operações de análise e geração de jogos
 */
@RestController
@RequestMapping("/analise")
@RequiredArgsConstructor
@Slf4j
public class AnaliseController {

    private final IAnaliseJogoService analiseJogoService;
    private final IGeradorJogoService geradorJogoService;

    @PostMapping("/jogo")
    public ResponseEntity<AnaliseJogoDTO> analisarJogo(@Valid @RequestBody AnaliseJogoRequest request) {
        log.info("Recebida requisição para analisar jogo: {}", request.getJogo());
        log.info("Últimos sorteios: {}", request.getUltimosSorteios());

        try {
            AnaliseJogoDTO resultado;
            if (request.getUltimosSorteios() != null && request.getUltimosSorteios() > 0) {
                log.info("Analisando jogo nos últimos {} sorteios", request.getUltimosSorteios());
                resultado = analiseJogoService.analisarJogoUltimosSorteios(
                        request.getJogo(), request.getUltimosSorteios());
            } else {
                log.info("Analisando jogo completo");
                resultado = analiseJogoService.analisarJogo(request.getJogo());
            }
            log.info("Análise concluída com sucesso");
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            log.error("Erro ao analisar jogo", e);
            throw e;
        }
    }

    @GetMapping("/gerar/mais-sorteados")
    public ResponseEntity<GeracaoJogoDTO> gerarMaisSorteados(
            @RequestParam(defaultValue = "5") int quantidadeJogos,
            @RequestParam(defaultValue = "100") int ultimosSorteios) {
        log.info("Gerando {} jogos mais sorteados baseados nos últimos {} sorteios",
                quantidadeJogos, ultimosSorteios);

        try {
            GeracaoJogoDTO resultado = geradorJogoService.gerarJogosBaseadosEmNumerosMaisSorteados(
                    quantidadeJogos, ultimosSorteios);
            log.info("Jogos gerados com sucesso");
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            log.error("Erro ao gerar jogos mais sorteados", e);
            throw e;
        }
    }
}