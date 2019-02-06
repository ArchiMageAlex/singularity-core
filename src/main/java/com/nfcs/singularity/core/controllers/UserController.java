package com.nfcs.singularity.core.controllers;

import com.nfcs.singularity.core.domain.Role;
import com.nfcs.singularity.core.domain.User;
import com.nfcs.singularity.core.repos.RolesRepo;
import com.nfcs.singularity.core.repos.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.logging.Logger;

@Controller
@RequestMapping("/users")
public class UserController {
    private static Logger log = Logger.getLogger(UserController.class.getName());
    @Autowired
    UsersRepo ur;
    @Autowired
    RolesRepo rr;

    @GetMapping
    public ModelAndView users(ModelAndView model) {
        Role role = rr.findByName("USER");

        if (role == null) {
            role = new Role();
            role.setName("USER");
            rr.save(role);
            log.info("Created role: " + role.toString());
        }

        model.addObject("users", ur.findAll());
        model.addObject("user", new User());
        model.setViewName("/users");
        return model;
    }

    @PostMapping
    public ModelAndView addUser(@ModelAttribute User user, ModelAndView model) {
        Role role = rr.findByName("USER");

        if (role == null) {
            role = new Role();
            role.setName("USER");
            rr.save(role);
            log.info("Created role: " + role.toString());
        }

        user.getRoles().add(role);
        ur.save(user);
        log.info("Created user: " + user.toString());

        model.addObject("users", ur.findAll());
        model.addObject("user", user);
        model.setViewName("/users");

        return model;
    }
}