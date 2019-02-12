package com.nfcs.singularity.core.repos;

import com.nfcs.singularity.core.domain.Role;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepo extends BaseRepo<Role, Long> {
    default Role create() {
        return new Role();
    }

    Role findByName(String name);
}
