package com.nfcs.singularity.core.controllers;

import com.nfcs.singularity.core.domain.User;
import com.nfcs.singularity.core.repos.UsersRepo;
import com.nfcs.singularity.core.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;
import java.util.logging.Logger;

@Controller
@RequestMapping("/register")
public class RegistrationController {
    private static Logger log = Logger.getLogger(RegistrationController.class.getName());

    @Autowired
    Environment env;

    @Autowired
    MailService mailService;

    @Autowired
    UsersRepo ur;

    @GetMapping
    public String register(Map<String, Object> model) {
        model.put("user", new User());
        return "register";
    }

    @PostMapping
    public ModelAndView register(ModelAndView model, @ModelAttribute User user, RedirectAttributes redirectAttributes) {
        user = ur.save(user);
        String message = String.format("Hello, %s! \n\n" +
                "Welcome to Singularity.\n" +
                "Please, activate Your account: http://localhost:" + env.getProperty("server.port") + "/register/activate/%s\n\n" +
                "Sincerely Yours,\n" +
                "Singularity Gods Team.", user.getUsername(), user.getActivationCode());
        mailService.send(user.getUsername(), "Activation code", message);

        redirectAttributes.addAttribute("message", "Activation code sent, please check e-mail (from rA)");
        RedirectView redirectView = new RedirectView("/");
        redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        model.setView(redirectView);
        log.info(message);
        return model;
    }

    @GetMapping("/activate/{code}")
    public ModelAndView activate(@PathVariable String code, ModelAndView model, RedirectAttributes redirectAttributes) {
        boolean isActivated = ur.activateUser(code);

        if (isActivated) {
            redirectAttributes.addAttribute("message", "User successfully activated");
        } else {
            redirectAttributes.addAttribute("message", "Activation code is not found!");
        }

        redirectAttributes.addAttribute("user", model.getModelMap().getAttribute("user"));
        RedirectView redirectView = new RedirectView("/");
        redirectView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
        model.setView(redirectView);
        return model;
    }
}