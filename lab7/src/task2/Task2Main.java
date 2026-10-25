package task2;

import java.io.IOException;
import java.nio.file.Path;

public class Task2Main {
    public static void main(String[] args) throws IOException {
        Path file = Path.of("buffered_test.txt");
        TextFileIO io = new TextFileIO();
        io.write(file, "Привет, Java!", "Это вторая строка.");
        int[] c = io.readAndCount(file);
        System.out.println("Строк: " + c[0] + ", символов (без \\n): " + c[1]);
    }
}
