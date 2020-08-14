package com.nfcs.singularity.core.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/apis")
public class MainRESTController {
    @GetMapping("entities")
    public String getEntities() {
        return "Got entities!";
    }
}
