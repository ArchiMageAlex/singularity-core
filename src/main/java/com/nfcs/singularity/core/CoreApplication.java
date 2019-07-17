package com.nfcs.singularity.core;

import com.nfcs.singularity.core.domain.Role;
import com.nfcs.singularity.core.domain.User;
import com.nfcs.singularity.core.generators.CRUDGenerator;
import com.nfcs.singularity.core.repos.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@SpringBootApplication
@EnableJpaRepositories(repositoryFactoryBeanClass = DefaultRepositoryFactoryBean.class)
public class CoreApplication {
    private static Logger log = Logger.getLogger(CoreApplication.class.getName());
    @Autowired
    RolesRepo rr;

    @Autowired
    UsersRepo usersRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CRUDGenerator gen;

    public static void main(String[] args) {
        SpringApplication.run(CoreApplication.class, args);
    }

    @PostConstruct
    private void setup() {
        List<Role> roles = new ArrayList<>();
        roles.add(rr.createRole("USER"));
        roles.add(rr.createRole("ADMIN"));
        rr.flush();
        usersRepo.createUser("admin", passwordEncoder.encode("admin"), true, roles);
    }
}