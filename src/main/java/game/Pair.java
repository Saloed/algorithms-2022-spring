package game;


public class Pair<T, U> {

    private  T first;
    private  U second;

    public Pair(T first, U second) {
        this.first = first;
        this.second = second;

    }

    public T getFirst() {
        return first;
    }

    public U getSecond() {
        return second;
    }

    public void setFirst(T first){
        this.first = first;
    }

    public void setSecond(U second){
        this.second = second;
    }

    public Pair<Integer, Integer> addTo(Pair<Integer,Integer> pair){
        return new Pair<>((int) this.getFirst() + pair.getFirst(),(int)this.getSecond() + pair.getSecond());
    }
}

