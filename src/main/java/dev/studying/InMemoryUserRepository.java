package dev.studying;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InMemoryUserRepository implements UserRepository{
    private final Map<String,User> user = new HashMap<>();

    @Override
    public void add(User user) {
        this.user.put(user.getUsername(), user);
    }

    @Override
    public Optional<User> lookup(String username) {
        return Optional.ofNullable(user.get(username));
    }
}
