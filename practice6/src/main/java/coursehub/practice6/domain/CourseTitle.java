package coursehub.practice6.domain;

import java.util.Locale;
import java.util.Objects;

public final class CourseTitle {
    private final String displayValue, searchKey;
    public CourseTitle(String value) {
        Objects.requireNonNull(value, "Название не должно быть null");
        String display = value.strip().replaceAll("\\s+", " ");
        if (display.isEmpty()) throw new IllegalArgumentException("Название не должно быть пустым");
        displayValue = display;
        searchKey = display.toLowerCase(Locale.ROOT);
    }
    public String getDisplayValue() { return displayValue; }
    public String getSearchKey() { return searchKey; }
    @Override public String toString() { return displayValue; }
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CourseTitle)) return false;
        return displayValue.equals(((CourseTitle) o).displayValue);
    }
    @Override public int hashCode() { return Objects.hash(displayValue); }
}
