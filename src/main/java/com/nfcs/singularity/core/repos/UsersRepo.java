package com.nfcs.singularity.core.repos;

import com.nfcs.singularity.core.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepo extends JpaRepository<User, Long> {
}
