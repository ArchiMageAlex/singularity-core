package com.nfcs.singularity.core.repos;

import com.nfcs.singularity.core.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UsersRepo extends CrudRepository<User, Long> {

    @Override
    List<User> findAll();
}
