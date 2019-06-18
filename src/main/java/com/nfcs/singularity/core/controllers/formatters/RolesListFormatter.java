package com.nfcs.singularity.core.controllers.formatters;

import com.nfcs.singularity.core.domain.Role;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;
import java.util.logging.Logger;

public class RolesListFormatter implements Formatter<Role> {
    private static Logger log = Logger.getLogger(RolesListFormatter.class.getName());

    @Override
    public Role parse(String s, Locale locale) throws ParseException {
        Role entity = new Role();

        if (s != null) {
            log.info("Role: " + s);
            entity.setId(Long.getLong(s));
        }

        return entity;
    }

    @Override
    public String print(Role role, Locale locale) {
        return role.getId().toString();
    }
}