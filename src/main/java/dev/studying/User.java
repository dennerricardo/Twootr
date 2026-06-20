package dev.studying;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class User {
    private final String username;
    private final String password;
    private final Set<User> followers = new HashSet<>();
    private final Set<User> follows = new HashSet<>();
    private Position lastSeenPosition = Position.INITIAL;
    private ReceiverEndPoint receiverEndPoint;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void addFollower(User user){
        followers.add(user);
    }

    public Set<User> getFollowers(){
        return followers;
    }

    public Position getLastSeenPosition() {
        return lastSeenPosition;
    }

    public void setLastSeenPosition(Position lastSeenPosition) {
        this.lastSeenPosition = lastSeenPosition;
    }

    public void addFollow(User user){
        follows.add(user);
    }

    public Set<User> getFollows() {
        return follows;
    }

    @Override
    public String toString() {
        return "User(" + username + ")";
    }

    public void onLogon(ReceiverEndPoint receiverEndPoint) {
        this.receiverEndPoint = receiverEndPoint;
    }

    public Set<String> getFollowing() {
        return follows.stream()
                .map(User::getUsername)
                .collect(Collectors.toSet());
    }

    public void receiveTwoot(Twoot twoot) {
        receiverEndPoint.onTwoot(twoot);
        setLastSeenPosition(twoot.getPosition());

    }
}
