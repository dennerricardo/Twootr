package dev.studying;


import java.util.*;

public class Twootr {
    private final Map<String, User> users = new HashMap<>();
    private final Map<User,ReceiverEndPoint> activeSessions = new HashMap<>();
    private int currentPosition = 0 ;
    private final List<Twoot> twoots = new ArrayList<>();

    public void registerUser(String username, String password){
        User user = new User(username,password);
        users.put(username,user);
    }

    public Optional<SenderEndPoint> logon(String username, String password, ReceiverEndPoint receiverEndPoint) {
        User user = users.get(username);
        if(user != null && password.equals(user.getPassword())){
            activeSessions.put(user, receiverEndPoint);
            System.out.println("follows: " + user.getFollows());
            System.out.println("twoots: " + twoots.size());
            twoots.stream()
                    .filter(twoot -> user.getFollows().contains(twoot.getSender()))
                    .filter(twoot -> twoot.isAfter(user.getLastSeenPosition()))
                    .forEach(twoot -> receiverEndPoint.onTwoot(twoot));
            return Optional.of(new SenderEndPoint(user,this));
        }
        return Optional.empty();
    }

    public void onSendTwoot(String id,String content, User sender){
        Twoot twoot = new Twoot(id,content,new Position(currentPosition++), sender);
        twoots.add(twoot);
        sender.getFollowers()
                .stream()
                .filter(activeSessions::containsKey)
                .forEach(follower -> activeSessions.get(follower).onTwoot(twoot));
    }

    public void logoff(User user){
        activeSessions.remove(user);
    }

}
