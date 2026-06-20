package dev.studying;


import java.util.*;

public class Twootr {
    private final Map<User,ReceiverEndPoint> activeSessions = new HashMap<>();
    private int currentPosition = 0 ;
    private final List<Twoot> twoots = new ArrayList<>();
    private final UserRepository userRepository;
    private final TwootRepository twootRepository;


    public Twootr(UserRepository userRepository, TwootRepository twootRepository) {
        this.userRepository = userRepository;
        this.twootRepository = twootRepository;
    }

    public void registerUser(String username, String password){
        User user = new User(username,password);
        userRepository.add(user);
    }

    public Optional<SenderEndPoint> logon(String username, String password, ReceiverEndPoint receiverEndPoint) {
        User user = userRepository.lookup(username).orElse(null);
        if(user != null && password.equals(user.getPassword())){
            activeSessions.put(user, receiverEndPoint);
            user.onLogon(receiverEndPoint);
            twootRepository.query(
                    new TwootQuery()
                            .inUsers(user.getFollowing())
                            .lastSeenPosition(user.getLastSeenPosition()),
                    user::receiveTwoot
            );
//            twoots.stream()
//                    .filter(twoot -> user.getFollows().contains(twoot.getSender()))
//                    .filter(twoot -> twoot.isAfter(user.getLastSeenPosition()))
//                    .forEach(twoot -> receiverEndPoint.onTwoot(twoot));
            return Optional.of(new SenderEndPoint(user,this));
        }
        return Optional.empty();
    }

    public void onSendTwoot(String id,String content, User sender){
        Twoot twoot =  twootRepository.add(id,sender.getUsername(),content);
//        twoots.add(twoot);
        sender.getFollowers()
                .stream()
                .filter(activeSessions::containsKey)
                .forEach(follower -> activeSessions.get(follower).onTwoot(twoot));
    }

    public void logoff(User user){
        activeSessions.remove(user);
        if(!(twoots.isEmpty())){
            user.setLastSeenPosition(twoots.get(twoots.size() - 1 ).getPosition());
        }
    }

}
