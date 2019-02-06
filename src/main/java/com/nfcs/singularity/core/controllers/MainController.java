package com.nfcs.singularity.core.controllers;

import com.nfcs.singularity.core.domain.Role;
import com.nfcs.singularity.core.domain.User;
import com.nfcs.singularity.core.repos.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller("/")
public class MainController {

    @GetMapping({"/", "/main"})
    public String home(Map<String, Object> model) {
        return "main.html";
    }

    @Autowired
    UsersRepo ur;

    @GetMapping(path = "/users")
    public String users(Map<String, Object> model) {
        model.put("users", ur.findAll());
        model.put("user", new User());
        return "/users";
    }

    @PostMapping(path = "/users")
    public String addUser(@RequestParam String username
            , @RequestParam(required = false) List<Role> roles
            , @RequestParam(required = false, defaultValue = "true") boolean activated
            , Map<String, Object> model, @ModelAttribute User user) {
        /*User user = new User();
        user.setActivated(activated);
        user.setUsername(username);
        user.setRoles(roles);*/
        ur.save(user);
        model.put("users", ur.findAll());

        return "/users";
    }

    @PostMapping("/logout")
    public String logout() {
        return "redirect:main";
    }

    @GetMapping("/login")
    public String login() {
        return "/login";
    }

    @GetMapping(path = "/register")
    public String register(Map<String, Object> model) {
        model.put("user", new User());
        return "/register";
    }
    @PostMapping(path = "/register")
    public String register(Map<String, Object> model, @ModelAttribute User user) {
        user.setActivated(true);
        ur.save(user);
        model.put("users", ur.findAll());

        return "/main";
    }
}
