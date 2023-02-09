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
// TODO: make fields volatile, check SessionScoped beans
@SessionScope
@Slf4j
public class BaseController<T extends BaseEntity> {
    HttpServletRequest request;
    volatile CRUDGenerator gen;
    GenericWebApplicationContext context;
    EntityManager entityManager;

    public HttpServletRequest getRequest() {
        return request;
    }

    // TODO SessionScoped?
    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
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

    public void setEntityManager(@Autowired EntityManager entityManager) {
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
                               @RequestParam() Long id,
                               RedirectAttributes redirectAttributes) throws Exception {
        EntityType<T> entityType = (EntityType<T>) entityManager.getMetamodel().getEntities().stream()
                .filter(e -> e.getName().equals(entityClass)).findFirst().orElse(null);

        if (null != entityType) {
            if (null != id) {
                T entity;
                entity = new BaseRepoImpl<>(entityType.getJavaType(), entityManager).findById(id).orElse(null);

                if (null != entity)
                    entityManager.remove(entity);
                else
                    log.error("Entity {} with id {} can't be deleted. Cause - id not found", entityClass, id);
            } else {
                log.error("Id of type {} is null, what to delete?", entityType);
            }
        } else {
            log.error("Entity {} with id {} can't be deleted. Cause - class name not found at metamodel", entityClass, id);
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
        // TODO: huge metamodel should to slow search
        EntityType<T> entityType = (EntityType<T>) entityManager.getMetamodel().getEntities().stream()
                .filter(e -> e.getName().equals(entityClass)).findFirst().orElse(null);

        if (null != entityType) {
            T entity;
            BaseRepo<T, Long> br = new BaseRepoImpl<>(entityType.getJavaType(), entityManager);

            if (null != id) {
                entity = br.findById(id).orElse(null);

                if (null == entity)
                    throw new Exception("Entity with id=" + id + " not found");
            } else {
                entity = entityType.getJavaType().getConstructor().newInstance();
            }

            WebRequestDataBinder binder = new WebRequestDataBinder(entity);
            binder.bind(request);
            binder.validate();
            // TODO: Обработать binder.getBindingResult();

            if (save) {
                entity = br.save(entity);
                br.flush();
            }

            model.addAttribute("captions", gen.getEntityProperties(entityType.getJavaType()));
            model.addAttribute("entity", entity);
            model.addAttribute("entities", br.findAll());
            model.addAttribute("code", gitRepository.getCode(null
                    , "api.src.main.java." + entityType.getJavaType().getName()));
        } else {
            log.error("Entity {} id {} not saved. Class name not found at metamodel", entityClass, id);
        }
    }
}