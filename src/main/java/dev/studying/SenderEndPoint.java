package dev.studying;

public class SenderEndPoint {
    private final User user;
    private final Twootr twootr;

    public SenderEndPoint(User user, Twootr twootr){
        this.user = user;
        this.twootr = twootr;
    }

    public void onFollow(SenderEndPoint toFollow){
        toFollow.user.addFollower(this.user);

    }

    public void onSendTwoot(String id, String content){
        Twoot twoot = new Twoot(id,content);
        twootr.onSendTwoot(twoot, user);
    }
}
