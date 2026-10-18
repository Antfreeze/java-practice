package coursehub.practice6.time;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.zone.ZoneRules;
import java.util.List;

public class LocalDateTimeResolver {
    public static ZonedDateTime resolve(LocalDateTime local, ZoneId zone, boolean chooseLaterOffset) {
        ZoneRules rules = zone.getRules();
        List<ZoneOffset> offsets = rules.getValidOffsets(local);
        if (offsets.isEmpty())
            throw new InvalidLocalTimeException("Локальное время не существует: " + local + " " + zone);
        ZoneOffset chosen = chooseLaterOffset ? offsets.get(offsets.size() - 1) : offsets.get(0);
        return ZonedDateTime.ofLocal(local, zone, chosen);
    }

    public static void main(String[] args) {
        ZoneId berlin = ZoneId.of("Europe/Berlin");
        try {
            resolve(LocalDateTime.of(2026, 3, 29, 2, 30), berlin, false);
        } catch (InvalidLocalTimeException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(resolve(LocalDateTime.of(2026, 10, 25, 2, 30), berlin, false));
        System.out.println(resolve(LocalDateTime.of(2026, 10, 25, 2, 30), berlin, true));
    }
}
