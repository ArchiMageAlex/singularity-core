package com.nfcs.singularity.core.config;

import com.nfcs.singularity.core.domain.Role;
import com.nfcs.singularity.core.domain.User;
import com.nfcs.singularity.core.repos.RolesRepo;
import com.nfcs.singularity.core.repos.UsersRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
public class AppConfig {

    public AppConfig(RolesRepo rr, UsersRepo usersRepo, BCryptPasswordEncoder encoder) {
        List<Role> roles = new ArrayList<>();
        roles.add(rr.createRole("USER"));
        log.debug("Created role USER");
        roles.add(rr.createRole("ADMIN"));
        log.debug("Created role ADMIN");
        rr.flush();
        User user = usersRepo.createUser("admin", encoder.encode("admin"), true, roles);
        log.debug("Created user " + user.toString());
        usersRepo.flush();
    }
}
