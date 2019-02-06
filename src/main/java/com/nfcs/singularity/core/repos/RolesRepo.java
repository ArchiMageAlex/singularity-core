package com.nfcs.singularity.core.repos;

import com.nfcs.singularity.core.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesRepo extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
