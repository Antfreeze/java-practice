package coursehub.practice6.strings;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CourseCode {
    private static final Pattern PATTERN = Pattern.compile("([A-Z]+)-([0-9]{3})");
    private final String prefix;
    private final int number;

    public CourseCode(String value) {
        Objects.requireNonNull(value, "value");
        Matcher matcher = PATTERN.matcher(value);
        if (!matcher.matches())
            throw new IllegalArgumentException("Неверный код: " + value);
        prefix = matcher.group(1);
        number = Integer.parseInt(matcher.group(2));
    }

    public String getPrefix() {
        return prefix;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return prefix + "-" + String.format("%03d", number);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof CourseCode))
            return false;
        CourseCode c = (CourseCode) o;
        return number == c.number && prefix.equals(c.prefix);
    }

    @Override
    public int hashCode() {
        return Objects.hash(prefix, number);
    }

    public static void main(String[] args) {
        System.out.println(new CourseCode("JAVA-101"));
    }
}
