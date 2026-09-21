package br.com.systemgs.loterias.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller para páginas web
 */
@Controller
public class WebController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/lotofacil")
    public String lotofacil() {
        return "lotofacil";
    }

    @GetMapping("/megasena")
    public String megasena() {
        return "em_breve";
    }

    @GetMapping("/lotomania")
    public String lotomania() {
        return "em_breve";
    }

    @GetMapping("/quina")
    public String quina() {
        return "em_breve";
    }
}