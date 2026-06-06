package dev.studying;

public class Twoot {
    public final String id ;
    public final String content;


    public Twoot(String id, String content) {
        this.id = id;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
