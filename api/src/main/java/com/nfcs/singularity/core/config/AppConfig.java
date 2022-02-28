package com.nfcs.singularity.core.config;

import com.nfcs.singularity.core.domain.Role;
import com.nfcs.singularity.core.domain.User;
import com.nfcs.singularity.core.repos.RolesRepo;
import com.nfcs.singularity.core.repos.UsersRepo;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Configuration
public class AppConfig {
    @Value("${com.nfcs.singularity.core.repositoryPath}")
    String repositoryPath;
    private Repository repo;

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

    @Bean
    public Repository initRepository() throws IOException {
        if (null == repo) {
            log.debug("Current dir: {}", System.getProperty("user.dir"));
            FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
            Repository repository = repositoryBuilder.setGitDir(new File(System.getProperty("user.dir") + "/.git"))
                    .readEnvironment() // scan environment GIT_* variables
                    .findGitDir() // scan up the file system tree
                    .setMustExist(true)
                    .build();
            log.debug("Having repository: " + repository.getDirectory());
            Ref head = repository.exactRef("refs/heads/master");
            log.debug("Ref of refs/heads/master: " + head);
            this.repo = repository;
        }

        return this.repo;
    }
}
