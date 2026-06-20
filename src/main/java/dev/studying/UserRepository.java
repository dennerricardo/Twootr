package dev.studying;

import java.util.Optional;

public interface UserRepository extends AutoCloseable{
    boolean add(User user);
    Optional<User> lookup(String userId);
    void update(User user);
    void clear();
    FollowStatus follow(User follower, User userToFollow);
}
