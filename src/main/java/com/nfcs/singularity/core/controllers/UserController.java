package com.nfcs.singularity.core.controllers;

import com.nfcs.singularity.core.domain.Role;
import com.nfcs.singularity.core.domain.User;
import com.nfcs.singularity.core.repos.BaseRepo;
import com.nfcs.singularity.core.repos.RolesRepo;
import com.nfcs.singularity.core.repos.UsersRepo;
import com.nfcs.singularity.core.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;
import java.util.logging.Logger;

@Controller
@RequestMapping("/users")
public class UserController extends BaseController<User> {
    private static Logger log = Logger.getLogger(UserController.class.getName());
    @Autowired
    RolesRepo rr;

    @Autowired
    PasswordEncoder passwordEncoder;

    UsersRepo ur;

    public UserController(@Autowired UsersRepo br) {
        super(br);
        this.ur = br;
    }

    @GetMapping
    public ModelAndView entities(ModelAndView model, @Qualifier("users") Pageable pageableUsers, @Qualifier("roles") Pageable pageableRoles) {
        model.addObject("roles", rr.findAll(pageableRoles));
        model.addObject("users", br.findAll(pageableUsers));
        model.addObject("user", new User());
        model.setViewName(this.getViewName(User.class));
        return model;
    }

    @PostMapping
    public ModelAndView addUser(@ModelAttribute User user, ModelAndView model) throws Exception {
        User user1 = ur.getUser(user.getUsername()).orElse(null);

        if (user1 == null) {
            br.save(user);
            user.addRole(rr.getRole("USER").orElse(null));
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            br.save(user);
            log.info("Created user: " + user.toString());
        } else {
            Exception e = new Exception("User already exist!");
            throw e;
        }

        model.addObject("roles", rr.findAll());
        model.addObject("users", br.findAll());
        model.addObject("user", user);
        model.setViewName(this.getViewName(User.class));

        return model;
    }

    @RequestMapping("/{id}")
    public String showUserForm(@PathVariable("id") User user, Model model) {

        model.addAttribute("user", user);
        return "userForm";
    }
}