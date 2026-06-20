package dev.studying;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public class InMemoryTwootRepository implements TwootRepository{
    private final Map<String, Twoot> twoots = new HashMap<>();
    private int currentPosition = 0;

    @Override
    public Twoot add(String id, String userId, String content) {
        Twoot twoot = new Twoot(id, userId, content, new Position(currentPosition++));
        twoots.put(id, twoot);
        return twoot;
    }

    @Override
    public Optional<Twoot> get(String id) {
        return Optional.ofNullable(twoots.get(id));
    }

    @Override
    public void delete(Twoot twoot) {

    }

    @Override
    public void query(TwootQuery twootQuery, Consumer<Twoot> callback) {
        if(!twootQuery.hasUsers()){
            return;
        }
        var lastSeenPosition = twootQuery.getLastSeenPosition();
        var inUsers = twootQuery.getInUsers();
        twoots.values()
                .stream()
                .filter(twoot -> inUsers.contains(twoot.getSenderId()))
                .filter(twoot -> twoot.isAfter(lastSeenPosition))
                .forEach(callback);
    }

    @Override
    public void clear() {

    }
}
