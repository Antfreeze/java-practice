package task3;

public class Task3Main {
    static class Res implements AutoCloseable {
        final String name;

        Res(String name) {
            this.name = name;
            System.out.println(name + " открыт");
        }

        @Override
        public void close() {
            System.out.println(name + " закрыт");
        }
    }

    public static void main(String[] args) {
        try {
            try (Res reader = new Res("Reader");
                    Res writer = new Res("Writer")) {
                System.out.println("Работаем с данными...");
                throw new RuntimeException("Сбой в середине pipeline");
            }
        } catch (RuntimeException e) {
            System.out.println("Поймали: " + e.getMessage());
        }
    }
}
