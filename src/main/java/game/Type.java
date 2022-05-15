package game;

public enum Type {
    SHEEP( "S", new Pair<>(0, 0)), WOLF1("W1", new Pair<>(0, 0)),
    WOLF2( "W2", new Pair<>(0, 0)), WOLF3 ("W3", new Pair<>(0, 0)),
    WOLF4( "W4", new Pair<>(0, 0)), WALL( "WA", new Pair<>(0, 0));

    private final String name;
    private Pair<Integer, Integer> coordinate;

    Type(String name, Pair<Integer, Integer> coordinate) {
        this.coordinate = coordinate;
        this.name = name;
    }

    public void setCoordinate(Pair<Integer, Integer> coordinate) {
         this.coordinate = coordinate;
    }

    public Pair<Integer, Integer> getCoordinate() {
        return this.coordinate;
    }

    public int getY() {
        return this.coordinate.getFirst();
    }

    public int getX() {
        return this.coordinate.getSecond();
    }

    public String getName() {
        return name;
    }

}