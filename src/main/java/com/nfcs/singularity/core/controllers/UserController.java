package com.nfcs.singularity.core.controllers;

import com.nfcs.singularity.core.domain.User;
import com.nfcs.singularity.core.repos.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.annotation.SessionScope;

import java.util.logging.Logger;

@Controller
@RequestMapping("/users")
public class UserController extends BaseController<User> {
    private static Logger log = Logger.getLogger(UserController.class.getName());

    UsersRepo ur;

    public UserController(@Autowired UsersRepo br) {
        setRepo(br);
        this.ur = br;
    }
}