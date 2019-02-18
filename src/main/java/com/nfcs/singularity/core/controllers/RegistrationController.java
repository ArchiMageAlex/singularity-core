package com.nfcs.singularity.core.controllers;

import com.nfcs.singularity.core.domain.User;
import com.nfcs.singularity.core.repos.UsersRepo;
import com.nfcs.singularity.core.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.logging.Logger;

@Controller
@RequestMapping("/register")
public class RegistrationController {
    private static Logger log = Logger.getLogger(RegistrationController.class.getName());

    @Autowired
    MailService mailService;

    @Autowired
    UsersRepo ur;

    @GetMapping
    public String register(Map<String, Object> model) {
        model.put("user", new User());
        return "/register";
    }

    @PostMapping
    public String register(Map<String, Object> model, @ModelAttribute User user) {
        user.setActivated(false);
        user = ur.save(user);
        String message = String.format("Hello, %s! \n" +
                "Welcome to Singularity.\n" +
                "Please, activate Your account: http://localhost:8080/register/activate/%s", user.getUsername(), user.getActivationCode());
        mailService.send(user.getUsername(), "Activation code", message);
        model.put("users", ur.findAll());

        return "/main";
    }

    @GetMapping("/activate/{code}")
    public String activate(@PathVariable String code, Model model) {
        boolean isActivated = ur.activateUser(code);

        if (isActivated) {
            model.addAttribute("message", "User successfully activated");
        } else {
            model.addAttribute("message", "Activation code is not found!");
        }
        return "redirect:/main";
    }
}
