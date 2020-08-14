package com.nfcs.singularity.core.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/react")
public class ReactController {
    @GetMapping
    public String react() {
        return "react";
    }
}
