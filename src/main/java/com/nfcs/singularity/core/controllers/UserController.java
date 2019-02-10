package com.nfcs.singularity.core.controllers;

import com.nfcs.singularity.core.domain.Role;
import com.nfcs.singularity.core.domain.User;
import com.nfcs.singularity.core.repos.BaseRepo;
import com.nfcs.singularity.core.repos.RolesRepo;
import com.nfcs.singularity.core.repos.UsersRepo;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.NotNull;
import java.util.logging.Logger;

@Controller
@RequestMapping("/users")
public class UserController extends BaseController<User> {
    private static Logger log = Logger.getLogger(UserController.class.getName());
    @Autowired
    RolesRepo rr;

    public UserController(@Autowired UsersRepo br) {
        super(br);
    }

    @GetMapping
    public ModelAndView entities(ModelAndView model, @Qualifier("users") Pageable pageableUsers, @Qualifier("roles") Pageable pageableRoles) {
        model.addObject("roles", rr.findAll(pageableRoles));
        model.addObject("users", br.findAll(pageableUsers));
        model.addObject("user", new User());
        model.setViewName("/users");
        return model;
    }

    @PostMapping
    public ModelAndView addUser(@ModelAttribute User user, ModelAndView model) throws Exception {
        if (((UsersRepo) br).findByUsername(user.getUsername()) == null) {
            Role role = rr.findByName("USER");

            if (!user.getRoles().contains(role))
                user.getRoles().add(role);

            br.save(user);
            log.info("Created user: " + user.toString());
        } else {
            Exception e = new Exception("User already exist!");
            throw e;
        }

        model.addObject("roles", rr.findAll());
        model.addObject("users", br.findAll());
        model.addObject("user", user);
        model.setViewName("/users");

        return model;
    }

    @RequestMapping("/{id}")
    public String showUserForm(@PathVariable("id") User user, Model model) {

        model.addAttribute("user", user);
        return "userForm";
    }
}