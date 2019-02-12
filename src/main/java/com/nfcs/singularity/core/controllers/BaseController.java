package com.nfcs.singularity.core.controllers;

import com.nfcs.singularity.core.domain.BaseEntity;
import com.nfcs.singularity.core.repos.BaseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.constraints.NotNull;

public class BaseController<T extends BaseEntity> {
    BaseRepo<T, Long> br;

    BaseController(@NotNull BaseRepo<T, Long> br) {
        this.br = br;
    }

    @PostMapping("/save")
    @ResponseBody
    public T save(@RequestBody T entity) {
        br.save(entity);
        return entity;
    }

    protected String getViewName(Class<T> entityClass) {
        return entityClass.getSimpleName() + "s";
    }
}
