package com.nfcs.singularity.core.repos;

import com.nfcs.singularity.core.domain.Role;
import com.nfcs.singularity.core.domain.User;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.exact;

@Repository
public interface UsersRepo extends BaseRepo<User, Long> {
    default Optional<User> getUser(String userName) {
        return findOne(getUserExample(userName));
    }

    default Example<User> getUserExample(String userName) {
        return new Example<User>() {
            @Override
            public User getProbe() {
                User user = new User();
                user.setUsername(userName);
                return user;
            }

            @Override
            public ExampleMatcher getMatcher() {
                return ExampleMatcher.matching()
                        .withMatcher("username", exact())
                        .withIgnoreNullValues()
                        .withIgnorePaths("password", "activated", "roles", "activationCode");
            }
        };
    }

    default boolean activateUser(String code) {
        User user = this.findByActivationCode(code);

        if (user == null) {
            return false;
        }

        user.setActivated(true);
        this.save(user);
        return true;
    }

    User findByActivationCode(String code);

    default User createUser(String username, String password, boolean activated, List<Role> roles) {
        User user = getUser("admin").orElse(null);

        if (user == null) {
            final User user1 = new User();
            user1.setUsername(username);
            user1.setActivated(activated);
            user1.setPassword(password);
            user1.setActivationCode(null);
            save(user1);
            roles.forEach(r -> user1.addRole(r));
            save(user1);
            user = user1;
        }

        return user;
    }
}
