package com.nfcs.singularity.core.repos;

import com.nfcs.singularity.core.domain.Role;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolesRepo extends BaseRepo<Role, Long> {

    default Optional<Role> getRole(String roleName) {
        return findOne(getRoleExample(roleName));
    }

    default Example<Role> getRoleExample(String roleName) {
        return new Example<Role>() {
            @Override
            public Role getProbe() {
                Role role = new Role();
                role.setName(roleName);
                return role;
            }

            @Override
            public ExampleMatcher getMatcher() {
                return ExampleMatcher.matching();
            }
        };
    }
}
