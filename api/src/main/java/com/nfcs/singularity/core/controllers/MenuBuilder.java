package com.nfcs.singularity.core.controllers;

import com.nfcs.singularity.core.domain.MenuModel;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class MenuBuilder {
    public List<MenuModel> invoke(HttpServletRequest request, EntityManager em) {
        List<MenuModel> topMenu = new ArrayList<>();

        for (EntityType<?> e : em.getMetamodel().getEntities()) {
            boolean allowed = false;

            RolesAllowed a = e.getJavaType().getAnnotation(RolesAllowed.class);

            if (null != a) {
                String[] roles = a.value();

                for (String role : roles) {
                    allowed = request.isUserInRole(role);

                    if (allowed) {
                        break;
                    }
                }
            }

            if (allowed) {
                MenuModel menuModel = new MenuModel();
                menuModel.setName(e.getName());
                menuModel.setUrl("/entities?entityClass=" + e.getName());
                topMenu.add(menuModel);
            }
        }
        return topMenu;
    }
}
