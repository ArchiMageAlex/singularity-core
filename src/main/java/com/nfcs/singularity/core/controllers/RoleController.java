package com.nfcs.singularity.core.controllers;

import com.nfcs.singularity.core.domain.Role;
import com.nfcs.singularity.core.repos.RolesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/roles")
public class RoleController extends BaseController<Role> {
    RolesRepo rr;

    public RoleController(@Autowired RolesRepo br) {
        setRepo(br);
        this.rr = br;
    }
}