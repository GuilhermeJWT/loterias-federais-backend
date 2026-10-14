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
}