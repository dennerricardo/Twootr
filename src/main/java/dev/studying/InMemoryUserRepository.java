package dev.studying;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class InMemoryUserRepository implements UserRepository{
    private final Map<String,User> user = new HashMap<>();


    @Override
    public boolean add(User user) {
        this.user.put(user.getUsername(),user);
        return true;
    }

    @Override
    public Optional<User> lookup(String userId) {
        return Optional.ofNullable(user.get(userId));
    }

    @Override
    public void update(User user) {

    }

    @Override
    public void clear() {

    }

    @Override
    public FollowStatus follow(User follower, User userToFollow) {
        return null;
    }

    @Override
    public void close() throws Exception {

    }
}
