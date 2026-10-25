package task2;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class TextFileIO {
    public void write(Path file, String... lines) throws IOException {
        try (BufferedWriter out = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            for (int i = 0; i < lines.length; i++) {
                out.write(lines[i]);
                if (i < lines.length - 1)
                    out.newLine();
            }
        }
    }

    public int[] readAndCount(Path file) throws IOException {
        int lines = 0, chars = 0;
        try (BufferedReader in = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line;
            while ((line = in.readLine()) != null) {
                lines++;
                chars += line.length();
                System.out.println("Строка " + lines + ": " + line);
            }
        }
        return new int[] { lines, chars };
    }
}
