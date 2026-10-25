package task9;

public class Data {
    private final String name;
    private final int value;

    public Data(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Data{name='" + name + "', value=" + value + "}";
    }
}
