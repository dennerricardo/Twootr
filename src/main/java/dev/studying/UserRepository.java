package dev.studying;

import java.util.Optional;

public interface UserRepository {
    void add(User user);
    Optional<User> lookup(String username);

}
