package dev.studying;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Twootr {
    private final Map<String, User> users = new HashMap<>();
    private final Map<User,ReceiverEndPoint> activeSessions = new HashMap<>();

    public void registerUser(String username, String password){
        User user = new User(username,password);
        users.put(username,user);
    }

    public Optional<SenderEndPoint> logon(String username, String password, ReceiverEndPoint receiverEndPoint) {
        User user = users.get(username);
        if(user != null && password.equals(user.getPassword())){
            activeSessions.put(user, receiverEndPoint);
            return Optional.of(new SenderEndPoint(user,this));
        }
        return Optional.empty();
    }


    public  void onSendTwoot(Twoot twoot, User sender){
        sender.getFollowers()
                .stream()
                .filter(activeSessions::containsKey)
                .forEach(follower -> activeSessions.get(follower).onTwoot(twoot));
    }

}
