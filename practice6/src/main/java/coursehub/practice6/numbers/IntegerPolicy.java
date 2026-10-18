package coursehub.practice6.numbers;

import java.util.Objects;

public class IntegerPolicy {
    public static int unsafeUnbox(Integer value) {
        return value;
    }

    public static boolean sameValue(Integer a, Integer b) {
        Objects.requireNonNull(a, "a не должен быть null");
        Objects.requireNonNull(b, "b не должен быть null");
        return a.equals(b);
    }

    public static void main(String[] args) {
        Integer a = 127, b = 127, c = 128, d = 128;
        System.out.println(a == b);
        System.out.println(c == d);
        System.out.println(c.equals(d));
        try {
            unsafeUnbox(null);
        } catch (NullPointerException e) {
            System.out.println("NPE при unboxing");
        }
    }
}
