package coursehub.practice6.time;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

public class DateParser {
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.uuuu", Locale.ROOT).withResolverStyle(ResolverStyle.STRICT);
    public static LocalDate parse(String fieldName, String value) {
        try { return LocalDate.parse(value, FORMATTER); }
        catch (DateTimeParseException e) { throw new InvalidDateFieldException(fieldName, value, e); }
    }
}
