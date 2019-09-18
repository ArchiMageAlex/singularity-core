package com.nfcs.singularity.core.domain;

import org.metawidget.inspector.annotation.UiLabel;

import javax.annotation.security.RolesAllowed;
import javax.persistence.Entity;

@Entity
@RolesAllowed({"ADMIN"})
public class News extends BaseEntity {
    @UiLabel("Name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Role role = (Role) o;
        return getName().equals(role.getName());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
