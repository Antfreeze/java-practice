package task1;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class CatalogFileStore {
    public void save(Path path, CourseCatalog catalog) throws IOException {
        Files.writeString(path, catalog.getContent(), StandardCharsets.UTF_8);
    }

    public CourseCatalog load(Path path) throws IOException {
        return new CourseCatalog(Files.readString(path, StandardCharsets.UTF_8));
    }
}
