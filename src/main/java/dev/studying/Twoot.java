package dev.studying;

public class Twoot {
    private final String id ;
    private final String content;
    private final Position position;
    private final User sender;


    public Twoot(String id, String content, Position position, User sender) {
        this.id = id;
        this.content = content;
        this.position = position;
        this.sender = sender;
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

    public boolean isAfter(Position position){
        return this.position.isAfter(position);
    }

    public User getSender() {
        return sender;
    }
}
