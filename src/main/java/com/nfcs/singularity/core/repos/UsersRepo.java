package com.nfcs.singularity.core.repos;

import com.nfcs.singularity.core.domain.User;

public interface UsersRepo extends BaseRepo<User, Long> {
    default User create() {
        return new User();
    }

    default String entityName() {
        return User.class.getSimpleName().toLowerCase() + "s";
    }

    User findByUsername(String username);
}
