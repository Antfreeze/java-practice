package coursehub.practice3;

public final class Task02 {
    private Task02() {
    }

    public static void run() {
        NotificationSender first = new EmailSender();
        first.send("Новое сообщение");

        EmailSender second = new EmailSender();
        second.send("Строковая перегрузка");

        NotificationSender third = new ConsoleSender();
        third.send("Сообщение в консоль");
    }

    public static class NotificationSender {
        public void send(Object message) {
            System.out.println("Базовая отправка: " + message);
        }
    }

    public static final class EmailSender extends NotificationSender {
        @Override
        public void send(Object message) {
            System.out.println("EmailSender.send(Object): " + message);
        }

        public void send(String message) {
            System.out.println("EmailSender.send(String): " + message);
        }
    }

    public static final class ConsoleSender extends NotificationSender {
        @Override
        public void send(Object message) {
            System.out.println("ConsoleSender.send(Object): " + message);
        }
    }
}
