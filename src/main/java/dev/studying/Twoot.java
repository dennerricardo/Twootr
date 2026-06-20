package dev.studying;

public class Twoot {
    private final String id ;
    private final String senderId;
    private final String content;
    private final Position position;


    public Twoot(String id, String senderId, String content, Position position) {
        this.id = id;
        this.senderId = senderId;
        this.content = content;
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public Position getPosition() {
        return position;
    }

    public String getSenderId() {
        return senderId;
    }

    public boolean isAfter(Position position) {
        return this.position.isAfter(position);
    }
}
