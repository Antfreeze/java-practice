package coursehub.practice3;

public final class Task04 {
    private Task04() {
    }

    public static void run() {
        AbstractNotificationSender email = new EmailSender();
        email.send("student@example.com", "Материалы курса готовы");

        AbstractNotificationSender console = new ConsoleSender();
        console.send("студент", "Напоминание о занятии");

        try {
            console.send(" ", "Сообщение без адресата");
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public abstract static class AbstractNotificationSender {
        public final void send(String recipient, String message) {
            if (recipient == null || recipient.isBlank()) {
                throw new IllegalArgumentException("Адресат не должен быть пустым");
            }
            doSend(recipient, message);
        }

        protected abstract void doSend(String recipient, String message);
    }

    public static final class EmailSender extends AbstractNotificationSender {
        @Override
        protected void doSend(String recipient, String message) {
            System.out.println("Email для " + recipient + ": " + message);
        }
    }

    public static final class ConsoleSender extends AbstractNotificationSender {
        @Override
        protected void doSend(String recipient, String message) {
            System.out.println("Сообщение для " + recipient + ": " + message);
        }
    }
}
