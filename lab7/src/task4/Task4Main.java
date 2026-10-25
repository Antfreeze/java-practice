package task4;

import java.io.IOException;
import java.nio.file.*;

public class Task4Main {
    public static void main(String[] args) throws IOException {
        Path file = Path.of("lazy.txt");
        Files.writeString(file, "Строка 1\nСтрока 2\nСтрока 3");
        LazyLines lazy = new LazyLines();
        System.out.println("Правильно");
        lazy.process(file);
        System.out.println("Неправильно (ожидаем ошибку)");
        try {
            lazy.badReturn(file).forEach(System.out::println);
        } catch (IllegalStateException e) {
            System.out.println("Ожидаемо: " + e.getMessage());
        }
    }
}
