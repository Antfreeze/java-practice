package task4;

import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

public class LazyLines {
    public void process(Path file) throws IOException {
        try (Stream<String> lines = Files.lines(file)) {
            lines.forEach(System.out::println);
        }
    }

    public Stream<String> badReturn(Path file) throws IOException {
        try (Stream<String> lines = Files.lines(file)) {
            return lines;
        }
    }
}
