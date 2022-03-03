package com.nfcs.singularity.core.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller("/")
@Configuration
public class MainController {
    @GetMapping({"/"})
    public String home() {
        return "main";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}