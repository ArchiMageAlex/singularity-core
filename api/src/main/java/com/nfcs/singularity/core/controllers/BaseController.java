package com.nfcs.singularity.core.controllers;

import com.nfcs.singularity.core.domain.BaseEntity;
import com.nfcs.singularity.core.generators.CRUDGenerator;
import com.nfcs.singularity.core.repos.BaseRepo;
import com.nfcs.singularity.core.repos.BaseRepoImpl;
import com.nfcs.singularity.core.repos.GitRepository;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

@Controller
@SessionScope
@Slf4j
public class BaseController<T extends BaseEntity> {
    HttpServletRequest request;
    CRUDGenerator gen;
    GenericWebApplicationContext context;
    EntityManager entityManager;

    public HttpServletRequest getRequest() {
        return request;
    }

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public CRUDGenerator getGen() {
        return gen;
    }

    @Autowired
    public void setGen(CRUDGenerator gen) {
        this.gen = gen;
    }

    public GenericWebApplicationContext getContext() {
        return context;
    }

    @Autowired
    public void setContext(GenericWebApplicationContext context) {
        this.context = context;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Autowired
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @PostMapping(value = "/save",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @Transactional
    public String save(@RequestParam String entityClass,
                       @RequestParam(required = false) Long id,
                       Model model,
                       WebRequest request,
                       GitRepository gitRepository) throws Exception {
        saveEntity(entityClass, id, model, request, true, gitRepository);
        return "fragments/entitiesList::entities-list";
    }

    @GetMapping(value = "entities/delete")
    @Transactional
    public ModelAndView delete(@RequestParam String entityClass,
                               @RequestParam(required = true) Long id,
                               Model model,
                               WebRequest request, RedirectAttributes redirectAttributes) throws Exception {
        EntityType<T> entityType = (EntityType<T>) entityManager.getMetamodel().getEntities().stream()
                .filter(e -> e.getName().equals(entityClass)).findFirst().orElse(null);


        if (entityType != null) {

            T entity;
            BaseRepo<T, Long> br = new BaseRepoImpl<>(entityType.getJavaType(), entityManager);

            if (id != null) {
                entity = br.findById(id).orElse(null);
                entityManager.remove(entity);
            } else {
                log.error("Id is null, what to delete?");
            }
        } else {
            log.error("Entity " + entityClass + "(id=" + id.toString() + ") not deleted. Cause - class name not found at metamodel");
        }

        redirectAttributes.addFlashAttribute("entityClass", entityClass);

        return new ModelAndView("redirect:/entities?entityClass=" + entityClass);
    }

    @GetMapping("entities")
    public String listEntities(@RequestParam String entityClass,
                               @RequestParam(required = false) Long id,
                               WebRequest request,
                               Model model, GitRepository gitRepository) throws Exception {
        saveEntity(entityClass, id, model, request, false, gitRepository);
        return "entities";
    }

    private void saveEntity(String entityClass
            , Long id
            , Model model
            , WebRequest request
            , boolean save // TODO: Weird solution, I must refactor it
            , GitRepository gitRepository) throws Exception {
        EntityType<T> entityType = (EntityType<T>) entityManager.getMetamodel().getEntities().stream()
                .filter(e -> e.getName().equals(entityClass)).findFirst().orElse(null);

        if (entityType != null) {
            T entity;
            BaseRepo<T, Long> br = new BaseRepoImpl<T, Long>(entityType.getJavaType(), entityManager);

            if (id != null) {
                entity = br.findById(id).orElse(null);

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
            model.addAttribute("code", gitRepository.getCode(null
                    , "api.src.main.java." + entityType.getJavaType().getName()));
        } else {
            log.error("Entity " + entityClass + "(id=" + id.toString()
                    + ") not saved. Cause - class name not found at metamodel");
        }
    }

    private BindingResult bindEntity(WebRequest request, BaseEntity entity) {
        WebRequestDataBinder binder = new WebRequestDataBinder(entity);
        binder.bind(request);
        binder.validate();
        return binder.getBindingResult();
    }
}