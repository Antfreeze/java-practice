package task1;

import java.io.IOException;
import java.nio.file.*;

public class Task1Main {
    public static void main(String[] args) {
        Path path = Path.of("courses.txt");
        CatalogFileStore store = new CatalogFileStore();
        try {
            store.save(path, new CourseCatalog("Java Basics\nOOP\nCollections\nFiles"));
            System.out.println("Записано: " + path.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Не удалось записать файл: " + e.getMessage());
            return;
        }
        CourseCatalog loaded;
        try {
            loaded = store.load(path);
        } catch (NoSuchFileException e) {
            System.err.println("Файл не найден: " + path.toAbsolutePath());
            return;
        } catch (FileSystemException e) {
            System.err.println("По пути не файл: " + e.getMessage());
            return;
        } catch (IOException e) {
            System.err.println("Ошибка ввода-вывода: " + e.getMessage());
            return;
        }
        System.out.println("Прочитано:\n" + loaded.getContent());
        System.out.println("Программа завершена успешно.");
    }
}
