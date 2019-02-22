package com.nfcs.singularity.core.controllers;

import com.nfcs.singularity.core.domain.BaseEntity;
import com.nfcs.singularity.core.domain.Role;
import com.nfcs.singularity.core.domain.User;
import com.nfcs.singularity.core.generators.CRUDGenerator;
import com.nfcs.singularity.core.repos.BaseRepo;
import com.nfcs.singularity.core.repos.RolesRepo;
import com.nfcs.singularity.core.repos.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.WebRequestDataBinder;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.util.logging.Logger;

@Controller
@SessionScope
public class BaseController<T extends BaseEntity> {
    private static Logger log = Logger.getLogger(BaseController.class.getName());
    protected BaseRepo<T, Long> br;

    @Autowired
    CRUDGenerator gen;

    @Autowired
    GenericWebApplicationContext context;

    @Autowired
    UsersRepo ur;

    @Autowired
    RolesRepo rr;

    @Autowired
    EntityManager entityManager;

    public void setRepo(BaseRepo<T, Long> br) {
        this.br = br;
    }

    @PostMapping(value = "/save",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String save(@RequestParam String entityClass, Model model, WebRequest request) {
        // TODO: Extract registry or use entityManager
        EntityType<?> entityType = entityManager.getMetamodel().getEntities().stream()
                .filter(e -> e.getName().equals(entityClass)).findFirst().orElse(null);

        if (entityType != null) {
            if (entityType.getName().equals("User")) {
                User user = new User();
                bindEntity(request, user);
                ur.save(user);
            } else if (entityType.getName().equals("Role")) {
                Role role = new Role();
                bindEntity(request, role);
                rr.save(role);
            }
        }

        return "redirect:/main";
    }

    private void bindEntity(WebRequest request, BaseEntity entity) {
        WebRequestDataBinder binder = new WebRequestDataBinder(entity);
        binder.bind(request);
        binder.validate();
        BindingResult result = binder.getBindingResult();
    }

    protected String getViewName(Class<T> entityClass) {
        return entityClass.getSimpleName() + "s";
    }

    @GetMapping("entities")
    public ModelAndView listEntities(@RequestParam String entityClass, ModelAndView model) {
        EntityType<T> entityType = (EntityType<T>) entityManager.getMetamodel().getEntities().stream()
                .filter(e -> e.getName().equals(entityClass)).findFirst().orElse(null);

        if (entityType != null) {
            model.addObject("captions", gen.getEntityProperties(entityType.getJavaType()));

            if (entityType.getName().equals("Role")) {
                model.addObject("entities", rr.findAll());
                model.addObject("entity", new Role());
            } else if (entityType.getName().equals("User")) {
                model.addObject("entities", ur.findAll());
                model.addObject("entity", new User());
            }
        }

        model.setViewName("entities");
        return model;
    }
}