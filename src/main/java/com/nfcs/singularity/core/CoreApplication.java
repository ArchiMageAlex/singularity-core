package com.nfcs.singularity.core;

import com.nfcs.singularity.core.domain.Role;
import com.nfcs.singularity.core.domain.User;
import com.nfcs.singularity.core.repos.BaseRepo;
import com.nfcs.singularity.core.repos.RolesRepo;
import com.nfcs.singularity.core.repos.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.logging.Logger;

@SpringBootApplication
public class CoreApplication {
    private static Logger log = Logger.getLogger(CoreApplication.class.getName());
    @Autowired
    RolesRepo rr;

    @Autowired
    UsersRepo usersRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(CoreApplication.class, args);
    }

    @PostConstruct
    private void post() {
        Role roleUser = createRole("USER");
        Role roleAdmin = createRole("ADMIN");

        User user = usersRepo.getUser("admin").orElse(null);

        if (user == null) {
            user = new User();
            user.setUsername("admin");
            user.setActivated(true);
            user.setPassword(passwordEncoder.encode("admin"));//new BCryptPasswordEncoder().encode("admin"));
            usersRepo.save(user);
            user.addRole(roleUser);
            user.addRole(roleAdmin);
            usersRepo.save(user);
            log.info("Created user: " + user.toString());
        }
    }

    private Role createRole(String name) {
        Role role = rr.getRole(name).orElse(null);

        if (role == null) {
            role = new Role();
            role.setName(name);
            role = rr.save(role);
            log.info("Created role: " + role.toString());
        }
        return role;
    }
}