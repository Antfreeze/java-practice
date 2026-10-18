package coursehub.practice6.time;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StudyCalendar {
    public static List<LocalDate> moduleStarts(LocalDate start, int count, int daysPerModule) {
        if (start == null || count < 1 || daysPerModule < 1) throw new IllegalArgumentException("Неверные параметры");
        List<LocalDate> result = new ArrayList<>();
        for (int i = 0; i < count; i++) result.add(start.plusDays((long) i * daysPerModule));
        return result;
    }
    public static LocalDate lastStudyDay(LocalDate start, int studyDays) {
        if (start == null || studyDays < 1) throw new IllegalArgumentException("studyDays должен быть >= 1");
        return start.plusDays(studyDays - 1L);
    }
    public static boolean isLeapYear(LocalDate date) {
        if (date == null) throw new IllegalArgumentException("Дата не должна быть null");
        return date.isLeapYear();
    }
}
