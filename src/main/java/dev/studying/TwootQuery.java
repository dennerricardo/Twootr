package dev.studying;

import java.util.Set;

public class TwootQuery {
    private Set<String> inUsers;
    private Position lastSeenPosition;


    public Set<String> getInUsers() {
        return inUsers;
    }

    public Position getLastSeenPosition() {
        return lastSeenPosition;
    }

    public TwootQuery inUsers(Set<String> inUsers){
        this.inUsers = inUsers;
        return this;

    }

    public TwootQuery lastSeenPosition(Position lastSeenPosition){
        this.lastSeenPosition = lastSeenPosition;
        return this;
    }

    public boolean hasUsers(){
        return inUsers != null && !inUsers.isEmpty();
    }

}
