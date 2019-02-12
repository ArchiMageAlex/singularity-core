package com.nfcs.singularity.core.controllers;

import com.nfcs.singularity.core.domain.Role;
import com.nfcs.singularity.core.repos.BaseRepo;
import com.nfcs.singularity.core.repos.RolesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/roles")
public class RoleController extends BaseController<Role> {
    public RoleController(@Autowired BaseRepo<Role, Long> br) {
        super(br);
    }

    @GetMapping
    public ModelAndView entities(ModelAndView model) {
        model.addObject("entities", br.findAll());
        model.addObject("entity", br.create());

        model.setViewName(this.getViewName(Role.class));
        return model;
    }
}
