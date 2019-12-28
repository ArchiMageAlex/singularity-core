package com.nfcs.singularity.core.generators;

import com.nfcs.singularity.core.domain.BaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.metawidget.inspector.annotation.UiHidden;
import org.metawidget.inspector.annotation.UiLabel;
import org.metawidget.inspector.annotation.UiMasked;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.PluralAttribute;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@Service
@Slf4j
public class CRUDGenerator {
    private static Logger LOG = Logger.getLogger(CRUDGenerator.class.getName());
    @Autowired
    EntityManager em;

    public List<EntityPropertyLabel> getEntityProperties(Class<? extends BaseEntity> entityClass) {
        List<EntityPropertyLabel> labels = new ArrayList<>();
        EntityType<?> entity = em.getMetamodel().entity(entityClass);
        Set<?> attrs = entity.getSingularAttributes();
        Iterator<?> i = attrs.iterator();

        while (i.hasNext()) {
            Attribute<?, ?> attr = (Attribute<?, ?>) i.next();
            EntityPropertyLabel entityPropertyLabel = new EntityPropertyLabel(attr.getName(), attr.getJavaMember()).invoke();
            labels.add(entityPropertyLabel);
        }

        attrs = entity.getPluralAttributes();
        i = attrs.iterator();

        while(i.hasNext()) {
            PluralAttribute<?, ?, ?> attribute = (PluralAttribute<?,?,?>) i.next();
            EntityPropertyLabel entityPropertyLabel = new EntityPropertyLabel(attribute.getName(), attribute.getJavaMember()).invoke();
            labels.add(entityPropertyLabel);
        }

        LOG.warning("Current implementation contains singular attributes and experimental plural attributes");
        return labels;
    }

    public class EntityPropertyLabel {
        private String name;
        private String label;
        private boolean hide = false;
        private Annotation[] as;
        private boolean masked = false;
        private String type = "";

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
                if (a.annotationType().equals(UiHidden.class)) {
                    log.debug("Hide property " + this.name);
                    this.hide = true;
                } else if (a.annotationType().equals(UiLabel.class)) {
                    try {
                        Method call = a.annotationType().getMethod("value");
                        setLabel((String) call.invoke(a, null));
                        log.debug("Set label " + this.label + " for property " + this.name);
                    } catch (SecurityException e) {
                        log.debug("Can't show property " + this.name + " with annotation: " + a.annotationType().getName(),e);
                        this.hide = true;
                    } catch (NoSuchMethodException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                        log.error("Something wrong with reflection", e);
                    }
                } else if (a.annotationType().equals(UiMasked.class)) {
                    this.masked = true;
                }
            }

            return this;
        }
    }
}
