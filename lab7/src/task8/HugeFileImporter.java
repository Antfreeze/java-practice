package task8;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class HugeFileImporter {
    public static final int MAX_ERRORS_KEPT = 10;
    public static final int CANCEL_AFTER = 50;

    public static class Stats {
        private int totalLines, totalErrors;
        private final List<String> sampleErrors = new ArrayList<>();

        void line() {
            totalLines++;
        }

        void error(String msg) {
            totalErrors++;
            if (sampleErrors.size() < MAX_ERRORS_KEPT)
                sampleErrors.add(msg);
        }

        boolean shouldCancel() {
            return totalErrors > CANCEL_AFTER;
        }

        public int getTotalLines() {
            return totalLines;
        }

        public int getTotalErrors() {
            return totalErrors;
        }

        public List<String> getSampleErrors() {
            return sampleErrors;
        }
    }

    public Stats process(Path file) throws IOException {
        Stats s = new Stats();
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line;
            while ((line = reader.readLine()) != null) {
                s.line();
                if (line.trim().isEmpty())
                    s.error("Строка " + s.getTotalLines() + ": пустая");
                if (s.shouldCancel()) {
                    System.out.println("Стоп на строке " + s.getTotalLines());
                    break;
                }
            }
        }
        return s;
    }
}
