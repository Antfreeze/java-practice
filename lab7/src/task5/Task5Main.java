package task5;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class Task5Main {
    public static void main(String[] args) throws IOException {
        Path file = Path.of("data.bin");
        BinaryStore store = new BinaryStore();
        List<String> data = List.of("Курс 1", "Курс 2");
        store.write(file, data);
        System.out.println("Записано: " + data);
        System.out.println("Прочитано: " + store.read(file));
        byte[] bytes = Files.readAllBytes(file);
        Files.write(file, Arrays.copyOf(bytes, bytes.length - 3));
        try {
            store.read(file);
        } catch (IOException e) {
            System.out.println("Ожидаемо: " + e.getMessage());
        }
    }
}
