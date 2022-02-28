package com.nfcs.singularity.core.controllers;

import com.nfcs.singularity.core.domain.MenuModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@ControllerAdvice()
public class ParentInjector {
    HttpServletRequest request;
    EntityManager em;

    public HttpServletRequest getRequest() {
        return request;
    }

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public EntityManager getEm() {
        return em;
    }

    @Autowired
    public void setEm(EntityManager em) {
        this.em = em;
    }

    @ModelAttribute
    public List<MenuModel> injectParent() {
        List<MenuModel> topMenu = new MenuBuilder().invoke(request, em);
        return topMenu;
    }
}
