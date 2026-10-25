package task9;

import java.io.IOException;
import java.nio.file.*;

public class Task9Main {
    public static void main(String[] args) throws IOException {
        Path f = Path.of("versioned.bin");
        VersionedStore store = new VersionedStore();
        store.writeV1(f, "OldCourse");
        System.out.println("V1 мигрировано: " + store.readAndMigrate(f));
        store.writeV2(f, "NewCourse", 42);
        System.out.println("V2 прочитано:   " + store.readAndMigrate(f));
        byte[] b = Files.readAllBytes(f);
        b[b.length - 1] ^= 0xFF;
        Files.write(f, b);
        try {
            store.readAndMigrate(f);
        } catch (IOException e) {
            System.out.println("Ожидаемо: " + e.getMessage());
        }
        Files.write(f, new byte[] { 0, 0, 0, 99 });
        try {
            store.readAndMigrate(f);
        } catch (IOException e) {
            System.out.println("Ожидаемо: " + e.getMessage());
        }
    }
}
