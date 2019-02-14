package com.nfcs.singularity.core.repos;

import com.nfcs.singularity.core.domain.User;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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
                return ExampleMatcher.matchingAny().withStringMatcher(ExampleMatcher.StringMatcher.EXACT);
            }
        };
    }
}
