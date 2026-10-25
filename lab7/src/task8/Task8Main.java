package task8;

import java.io.IOException;
import java.nio.file.*;

public class Task8Main {
    public static void main(String[] args) throws IOException {
        Path huge = Path.of("huge.txt");
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 100; i++)
            sb.append(i % 5 == 0 ? "" : "Данные " + i).append('\n');
        Files.writeString(huge, sb.toString());
        HugeFileImporter.Stats s = new HugeFileImporter().process(huge);
        System.out.println("Строк: " + s.getTotalLines() + ", ошибок: " + s.getTotalErrors());
        System.out.println("Первые ошибки: " + s.getSampleErrors());
    }
}
