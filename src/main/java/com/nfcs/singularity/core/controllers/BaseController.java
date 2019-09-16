package com.nfcs.singularity.core.controllers;

import com.nfcs.singularity.core.domain.BaseEntity;
import com.nfcs.singularity.core.domain.MenuModel;
import com.nfcs.singularity.core.generators.CRUDGenerator;
import com.nfcs.singularity.core.repos.BaseRepo;
import com.nfcs.singularity.core.repos.BaseRepoImpl;
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

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

@Controller
@SessionScope
public class BaseController<T extends BaseEntity> {
    private static Logger log = Logger.getLogger(BaseController.class.getName());

    @Autowired
    HttpServletRequest request;

    @Autowired
    CRUDGenerator gen;

    @Autowired
    GenericWebApplicationContext context;

    @Autowired
    EntityManager entityManager;

    @PostMapping(value = "/save",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @Transactional
    public String save(@RequestParam String entityClass,
                       @RequestParam(required = false) Long id,
                       Model model,
                       WebRequest request) throws Exception {
        saveEntity(entityClass, id, model, request, true);
        return "fragments/entitiesList :: entities-list";
    }

    @GetMapping("entities")
    public String listEntities(@RequestParam String entityClass,
                               @RequestParam(required = false) Long id,
                               WebRequest request,
                               Model model) throws Exception {
        saveEntity(entityClass, id, model, request, false);
        return "entities";
    }

    private void saveEntity(String entityClass, Long id, Model model, WebRequest request, boolean save /* TODO: Weirrrrd! Solution, I must refactor it*/) throws Exception {
        EntityType<T> entityType = (EntityType<T>) entityManager.getMetamodel().getEntities().stream()
                .filter(e -> e.getName().equals(entityClass)).findFirst().orElse(null);

        if (entityType != null) {

            T entity;
            BaseRepo br = new BaseRepoImpl(entityType.getJavaType(), entityManager);

            if (id != null) {
                entity = (T) br.findById(id).orElse(null);

                if (entity == null)
                    throw new Exception("Entity with id=" + id + " not found");
            } else {
                entity = entityType.getJavaType().getConstructor().newInstance();
            }

            bindEntity(request, entity);

            if (save) {
                entity = (T) br.save(entity);
                br.flush();
            }

            model.addAttribute("captions", gen.getEntityProperties(entityType.getJavaType()));
            model.addAttribute("entity", entity);
            model.addAttribute("entities", br.findAll());
        } else {
            log.severe("Entity " + entityClass + "(id=" + id.toString() + ") not saved. Cause - class name not found at metamodel");
        }
    }

    private BindingResult bindEntity(WebRequest request, BaseEntity entity) {
        WebRequestDataBinder binder = new WebRequestDataBinder(entity);
        binder.bind(request);
        binder.validate();
        return binder.getBindingResult();
    }
}