package dev.studying;

public class Position {
    private final int value ;
    public static final Position INITIAL = new Position(0);

    public Position(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    public boolean isAfter(Position other){
        return this.value >= other.value;
    }
}
