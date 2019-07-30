package com.nfcs.singularity.core.controllers;

import com.nfcs.singularity.core.domain.BaseEntity;
import com.nfcs.singularity.core.repos.BaseRepo;
import com.nfcs.singularity.core.repos.BaseRepoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Controller("/")
public class MainController {
    private static Logger log = Logger.getLogger(MainController.class.getName());

    @GetMapping({"/", "/main"})
    public String home(Map<String,Object> model) {
        List<String> topMenu = new ArrayList<>();

        model.put("TopMenu", topMenu);
        return "/main";
    }

    @GetMapping("/login")
    public String login() {
        return "/login";
    }
}
