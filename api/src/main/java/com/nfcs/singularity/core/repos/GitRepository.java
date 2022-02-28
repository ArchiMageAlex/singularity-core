package com.nfcs.singularity.core.repos;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

@Slf4j
@Configuration
public class GitRepository {
    @Value("${com.nfcs.singularity.core.repositoryPath}")
    String repositoryPath;
    private Repository repository;

    public GitRepository() throws IOException {
        if (null == this.repository) {
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
            this.repository = repository;
        }
    }

    public String getCode(File directory, @NotNull String name) {
        if (null == directory)
            directory = repository.getDirectory().getParentFile();
        // Java types path contains "." instead of "/", if we not found ".", then it is a file
        if (name.indexOf('.') < 0) {
            log.debug("Search for file {}.java", name);
            FilenameFilter filter = (dir, checkName) -> checkName.equals(name + ".java");
            File file = Objects.requireNonNull(directory.listFiles(filter))[0];

            String code = null;

            try {
                log.debug("Found file {}. Try to read it.", file.getName());
                code = Files.readString(Paths.get(file.getPath()));
            } catch (IOException e) {
                log.error("Can't read file {}", file.getPath());
                log.error("---", e);
            }

            return code;
        } else {
            log.debug("Search in directory {} for file/dir {} by path {}"
                    , name.substring(0, name.indexOf('.'))
                    , name.substring(name.indexOf('.') + 1)
                    , name);
            FilenameFilter filter = (dir, checkName) -> checkName.equals(name.substring(0, name.indexOf('.')));
            return getCode(Objects.requireNonNull(directory.listFiles(filter))[0], name.substring(name.indexOf('.') + 1));
        }
    }

}
