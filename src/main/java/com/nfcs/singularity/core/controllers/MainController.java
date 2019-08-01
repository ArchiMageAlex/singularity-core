package com.nfcs.singularity.core.controllers;

import com.nfcs.singularity.core.domain.MenuModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Controller("/")
public class MainController {
    private static Logger log = Logger.getLogger(MainController.class.getName());
    @Autowired
    EntityManager em;

    @Autowired
    HttpServletRequest request;

    @GetMapping({"/", "/main"})
    public String home(Map<String, Object> model) {
        List<MenuModel> topMenu = new ArrayList<>();

        for (EntityType<?> e : em.getMetamodel().getEntities()) {
            boolean allowed = false;

                RolesAllowed a = e.getJavaType().getAnnotation(RolesAllowed.class);

                if(null != a) {
                String[] roles = a.value();

                for (String role : roles) {
                    allowed = request.isUserInRole(role);

                    if (allowed) {
                        break;
                    }
                }}

            if (allowed) {
                MenuModel menuModel = new MenuModel();
                menuModel.setName(e.getName());
                menuModel.setUrl("/entities?entityClass=" + e.getName());
                topMenu.add(menuModel);
            }
        }

        model.put("topMenu", topMenu);
        return "/main";
    }

    @GetMapping("/login")
    public String login() {
        return "/login";
    }
}
