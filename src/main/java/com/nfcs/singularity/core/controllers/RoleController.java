package com.nfcs.singularity.core.controllers;

import com.nfcs.singularity.core.domain.Role;
import com.nfcs.singularity.core.generators.CRUDGenerator;
import com.nfcs.singularity.core.repos.BaseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/roles")
public class RoleController extends BaseController<Role> {
    @Autowired
    CRUDGenerator gen;

    public RoleController(@Autowired BaseRepo<Role, Long> br) {
        super(br);
    }

    @GetMapping
    public ModelAndView entities(ModelAndView model) {
        model.addObject("captions", gen.getEntityProperties(Role.class));
        model.addObject("entities", br.findAll());
        model.addObject("entity", new Role());

        model.setViewName(this.getViewName(Role.class));
        return model;
    }

    @RequestMapping("{id}")
    public String showRoleForm(@PathVariable("id") Role role, Model model) {
        model.addAttribute("entity", role);
        return "roleForm";
    }
}
