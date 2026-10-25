package task7;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class Task7Main {
    record ImportResult(List<String> valid, List<String> errors) {
        boolean canPublish() {
            return errors.isEmpty();
        }
    }

    static ImportResult importFile(Path file) throws IOException {
        List<String> valid = new ArrayList<>();
        List<String> errors = new ArrayList<>();
        List<String> lines = Files.readAllLines(file);
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            int no = i + 1;
            if (line.isEmpty())
                errors.add("Строка " + no + ": пустая");
            else if (!line.contains(";"))
                errors.add("Строка " + no + ": ожидалось 'название;часы'");
            else
                valid.add(line);
        }
        return new ImportResult(valid, errors);
    }

    public static void main(String[] args) throws IOException {
        Path file = Path.of("import.txt");
        Files.writeString(file, "Java Basics;40\n\nOOP;abc\nCollections;30");
        ImportResult r = importFile(file);
        if (r.canPublish())
            System.out.println("Публикуем: " + r.valid());
        else {
            System.out.println("Импорт ОТМЕНЁН (all-or-nothing). Ошибки:");
            r.errors().forEach(System.out::println);
        }
    }
}
