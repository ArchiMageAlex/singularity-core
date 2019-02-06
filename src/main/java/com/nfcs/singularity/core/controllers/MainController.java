package com.nfcs.singularity.core.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller("/")
public class MainController {

    @GetMapping({"/", "/main"})
    public String home() {
        return "main.html";
    }

    @PostMapping("/logout")
    public String logout() {
        return "redirect:main";
    }

    @GetMapping("/login")
    public String login() {
        return "/login";
    }

}
