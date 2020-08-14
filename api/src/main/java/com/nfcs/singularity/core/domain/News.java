package com.nfcs.singularity.core.domain;

import org.metawidget.inspector.annotation.UiLabel;

import javax.annotation.security.RolesAllowed;
import javax.persistence.Entity;

@Entity
@RolesAllowed({"ADMIN"})
public class News extends BaseEntity {
    @UiLabel("Text")
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String name) {
        this.text = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        News news = (News) o;
        return getText().equals(news.getText());
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
