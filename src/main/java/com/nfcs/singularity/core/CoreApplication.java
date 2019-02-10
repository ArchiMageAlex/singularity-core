package com.nfcs.singularity.core;

import com.nfcs.singularity.core.domain.Role;
import com.nfcs.singularity.core.repos.RolesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.logging.Logger;

@SpringBootApplication
public class CoreApplication {
    private static Logger log = Logger.getLogger(CoreApplication.class.getName());
    @Autowired
    RolesRepo rr;

    public static void main(String[] args) {
        SpringApplication.run(CoreApplication.class, args);
    }

    @PostConstruct
    private void post() {
        Role role = (Role) rr.findByName("USER");

        if (role == null) {
            role = new Role();
            role.setName("USER");
            rr.save(role);
            log.info("Created role: " + role.toString());
        }
    }
}