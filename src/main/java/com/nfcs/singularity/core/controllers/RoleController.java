package com.nfcs.singularity.core.controllers;

import com.nfcs.singularity.core.domain.Role;
import com.nfcs.singularity.core.repos.RolesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/roles")
public class RoleController extends BaseController<Role> {
    RoleController(@Autowired RolesRepo rr) {
        super(rr);
    }

    @GetMapping
    public ModelAndView entities(ModelAndView model) {
        model.addObject("entities", br.findAll());
        model.addObject("entity", br.create());

        model.setViewName( br.entityName());
        return model;
    }
}
