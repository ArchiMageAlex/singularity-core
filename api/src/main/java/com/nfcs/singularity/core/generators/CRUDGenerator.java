package com.nfcs.singularity.core.generators;

import com.nfcs.singularity.core.domain.BaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.PluralAttribute;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@Service
@Slf4j
public class CRUDGenerator {
    EntityManager em;

    @Autowired
    public void setEm(EntityManager em) {
        this.em = em;
    }

    public List<EntityPropertyLabel> getEntityProperties(Class<? extends BaseEntity> entityClass) {
        List<EntityPropertyLabel> labels = new ArrayList<>();
        EntityType<?> entity = em.getMetamodel().entity(entityClass);
        Set<?> attrs = entity.getSingularAttributes();
        Iterator<?> i = attrs.iterator();

        while (i.hasNext()) {
            Attribute<?, ?> attr = (Attribute<?, ?>) i.next();
            EntityPropertyLabel entityPropertyLabel = new EntityPropertyLabel(attr.getName(), attr.getJavaMember()).invoke();
            labels.add(entityPropertyLabel);
            log.debug("Get singular attribute {} with type {}", attr.getName(), attr.getJavaType().getName());
        }

        attrs = entity.getPluralAttributes();
        i = attrs.iterator();

        while(i.hasNext()) {
            PluralAttribute<?, ?, ?> attribute = (PluralAttribute<?,?,?>) i.next();
            EntityPropertyLabel entityPropertyLabel = new EntityPropertyLabel(attribute.getName(), attribute.getJavaMember()).invoke();
            labels.add(entityPropertyLabel);
            log.debug("Get plural attribute {} with type {}", attribute.getName(), attribute.getJavaType().getName());
        }

        attrs = entity.getAttributes();

        log.warn("Current implementation contains singular attributes and experimental plural attributes");
        return labels;
    }

}
