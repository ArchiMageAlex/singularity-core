package com.nfcs.singularity.core.generators;

import lombok.extern.slf4j.Slf4j;
import org.metawidget.inspector.annotation.UiHidden;
import org.metawidget.inspector.annotation.UiLabel;
import org.metawidget.inspector.annotation.UiLarge;
import org.metawidget.inspector.annotation.UiMasked;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

@Slf4j
public class EntityPropertyLabel {
    private String name;
    private String label;
    private boolean hide;
    private Annotation[] as;
    private boolean masked;
    private String type = "";
    private boolean large;

    public EntityPropertyLabel(String name, Member m) {
        this.name = name;
        this.label = name;

        if (m instanceof Method) {
            this.as = ((Method) m).getAnnotations();
        } else if (m instanceof Field) {
            this.as = ((Field) m).getAnnotations();
            this.type = ((Field) m).getType().getSimpleName();
        }
    }

    public boolean isLarge() {
        return large;
    }

    public void setLarge(boolean large) {
        this.large = large;
    }

    public boolean isMasked() {
        return masked;
    }

    public void setMasked(boolean masked) {
        this.masked = masked;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isHide() {
        return hide;
    }

    public String getName() {
        return name;
    }

    public EntityPropertyLabel invoke() {
        for (Annotation a : as) {
            this.masked = a.annotationType().equals(UiMasked.class) || this.masked;
            this.large = a.annotationType().equals(UiLarge.class) || this.large;
            this.hide = a.annotationType().equals(UiHidden.class) || this.hide;

            if (a.annotationType().equals(UiLabel.class)) {
                try {
                    Method call = a.annotationType().getMethod("value");
                    setLabel((String) call.invoke(a, null));
                    log.debug("Set label " + this.label + " for property " + this.name);
                } catch (SecurityException e) {
                    log.debug("Shouldn't show property " + this.name + " with annotation: " + a.annotationType().getName(), e);
                    this.hide = true;
                } catch (NoSuchMethodException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                    log.error("Something wrong with reflection", e);
                }
            }
        }

        return this;
    }
}
