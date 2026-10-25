package task6;

import java.io.IOException;
import java.nio.file.*;

public class Task6Main {
    public static void main(String[] args) throws IOException {
        Path target = Path.of("catalog.txt");
        Files.writeString(target, "Старые данные");
        System.out.println("Было:  " + Files.readString(target));
        new AtomicFileWriter().write(target, "Новые, правильные данные");
        System.out.println("Стало: " + Files.readString(target));
    }
}
