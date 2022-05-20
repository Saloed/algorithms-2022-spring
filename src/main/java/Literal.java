public class Literal {
    private final int value;
    private final boolean sign;

    public Literal(int value, boolean sign) {
        this.value = value;
        this.sign = sign;
    }

    public Literal(int value) {
        this.value = Math.abs(value);
        sign = value > 0;
    }

    public int getValue() {
        return value;
    }

    public boolean getSign() {
        return sign;
    }

    public int toInteger() {
        return sign ? value : value * -1;
    }
}