package com.nfcs.singularity.core.controllers;

import com.nfcs.singularity.core.domain.MenuModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Controller("/")
public class MainController {
    private static Logger log = Logger.getLogger(MainController.class.getName());

    @GetMapping({"/", "/main"})
    public String home(Map<String, Object> model) {
        return "/main";
    }

    @GetMapping("/login")
    public String login() {
        return "/login";
    }
}
