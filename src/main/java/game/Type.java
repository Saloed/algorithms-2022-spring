package game;

public enum Type {
    SHEEP( "S"), WOLF1("W1"),
    WOLF2( "W2"), WOLF3 ("W3"),
    WOLF4( "W4"), WALL( "WA");

    private final String name;

    Type(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}