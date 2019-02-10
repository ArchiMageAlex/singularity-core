package com.nfcs.singularity.core.repos;

import com.nfcs.singularity.core.domain.Role;

public interface RolesRepo extends BaseRepo<Role, Long> {
    default Role create() {
        return new Role();
    }

    default String entityName() {
        return Role.class.getSimpleName().toLowerCase() + "s";
    }

    Role findByName(String name);
}
