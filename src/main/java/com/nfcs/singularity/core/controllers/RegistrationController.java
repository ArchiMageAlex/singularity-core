package com.nfcs.singularity.core.controllers;

import com.nfcs.singularity.core.domain.User;
import com.nfcs.singularity.core.repos.RolesRepo;
import com.nfcs.singularity.core.repos.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;
import java.util.logging.Logger;

@Controller
@RequestMapping("/register")
public class RegistrationController {
    private static Logger log = Logger.getLogger(RegistrationController.class.getName());

    @Autowired
    UsersRepo ur;
    @Autowired
    RolesRepo rr;

    @GetMapping
    public String register(Map<String, Object> model) {
        model.put("user", new User());
        return "/register";
    }

    @PostMapping
    public String register(Map<String, Object> model, @ModelAttribute User user) {
        log.warning("Registered user activated by default. Remove that activation for administer activation later of registration");
        user.setActivated(true);
        ur.save(user);
        user.addRole(rr.getRole("USER").orElse(null));
        ur.save(user);
        model.put("users", ur.findAll());

        return "/main";
    }
}
