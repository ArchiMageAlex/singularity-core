package com.nfcs.singularity.core.generators;

import com.nfcs.singularity.core.domain.BaseEntity;
import org.metawidget.inspector.annotation.UiHidden;
import org.metawidget.inspector.annotation.UiLabel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
public class CRUDGenerator {
    @Autowired
    EntityManager em;

    public List<EntityPropertyLabel> getEntityProperties(Class<? extends BaseEntity> entityClass) {
        EntityType<?> entity = em.getMetamodel().entity(entityClass);
        Set<?> attrs = entity.getSingularAttributes();
        Iterator<?> i = attrs.iterator();
        List<EntityPropertyLabel> labels = new ArrayList<>();

        while (i.hasNext()) {
            Attribute<?, ?> attr = (Attribute<?, ?>) i.next();
            EntityPropertyLabel entityPropertyLabel = new EntityPropertyLabel(attr.getName(), attr.getJavaMember()).invoke();
            labels.add(entityPropertyLabel);
        }

        return labels;
    }

    public class EntityPropertyLabel {
        private String name;
        private String label;
        private boolean hide = false;
        private Annotation[] as;

        public EntityPropertyLabel(String name, Member m) {
            this.name = name;
            this.label = name;

            if (m instanceof Method)
                this.as = ((Method) m).getAnnotations();
            else if (m instanceof Field)
                this.as = ((Field) m).getAnnotations();
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
                    hide = true;
                } else if (a.annotationType().equals(UiLabel.class)) {
                    try {
                        Method call = a.annotationType().getMethod("value",
                                new Class[0]);
                        setLabel((String) call.invoke(a, null));
                    } catch (SecurityException e) {
                        hide = true;
                    } catch (NoSuchMethodException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

            return this;
        }
    }
}
