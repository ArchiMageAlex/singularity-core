package com.nfcs.singularity.core.controllers;

import com.nfcs.singularity.core.domain.BaseEntity;
import com.nfcs.singularity.core.generators.CRUDGenerator;
import com.nfcs.singularity.core.repos.BaseRepo;
import com.nfcs.singularity.core.repos.BaseRepoImpl;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.lib.Repository;
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
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

@Controller
@SessionScope
@Slf4j
public class BaseController<T extends BaseEntity> {

    @Autowired
    HttpServletRequest request;

    @Autowired
    CRUDGenerator gen;

    @Autowired
    GenericWebApplicationContext context;

    @Autowired
    EntityManager entityManager;

    @Autowired
    Repository repository;

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
            BaseRepo br = new BaseRepoImpl(entityType.getJavaType(), entityManager);

            if (id != null) {
                entity = (T) br.findById(id).orElse(null);
                entityManager.remove(entity);
            } else {
                log.error("Id is null, what to delete?");
            }
        } else {
            log.error("Entity " + entityClass + "(id=" + id.toString() + ") not deleted. Cause - class name not found at metamodel");
        }

        redirectAttributes.addFlashAttribute("entityClass", entityClass);
        ModelAndView modelAndView = new ModelAndView("redirect:/entities?entityClass=" + entityClass);

        return modelAndView;
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
            model.addAttribute("code", getCode(repository.getDirectory().getParentFile()
                    , "api.src.main.java." + entityType.getJavaType().getName()));
        } else {
            log.error("Entity " + entityClass + "(id=" + id.toString()
                    + ") not saved. Cause - class name not found at metamodel");
        }
    }

    private String getCode(File directory, String name) {
        if (name.indexOf('.') < 0) {
            File file = Objects.requireNonNull(directory.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String checkName) {
                    log.debug("Test for file/dir with name {}", checkName);
                    return checkName.equals(name + ".java");
                }
            }))[0];

            String code = null;

            try {
                log.debug("Found file {}. Try to read it.", file.getName());
                code = Files.readString(Paths.get(file.getPath()));
            } catch (IOException e) {
                log.error("Can't read file {}", file.getPath());
                log.error("---", e);
            }

            return code;
        } else {
            log.debug("Search in directory {} for file/dir {} by path {}"
                    , name.substring(0, name.indexOf('.'))
                    , name.substring(name.indexOf('.') + 1)
                    , name);
            return getCode(Objects.requireNonNull(directory.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String checkName) {
                    log.debug("Test for file/dir with name {}", checkName);
                    return checkName.equals(name.substring(0, name.indexOf('.')));
                }
            }))[0], name.substring(name.indexOf('.') + 1));
        }
    }

    private BindingResult bindEntity(WebRequest request, BaseEntity entity) {
        WebRequestDataBinder binder = new WebRequestDataBinder(entity);
        binder.bind(request);
        binder.validate();
        return binder.getBindingResult();
    }
}