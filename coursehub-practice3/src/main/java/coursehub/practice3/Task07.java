package coursehub.practice3;

public final class Task07 {
    private Task07() {
    }

    public static void run() {
        MetricsDecorator firstMetrics = new MetricsDecorator(
                new LoggingDecorator(new RetryDecorator(new FlakySender(2), 3)));
        firstMetrics.send("Первый порядок");
        System.out.println("Внешний счётчик: " + firstMetrics.calls());

        MetricsDecorator secondMetrics = new MetricsDecorator(new LoggingDecorator(new FlakySender(2)));
        NotificationSender second = new RetryDecorator(secondMetrics, 3);
        second.send("Второй порядок");
        System.out.println("Внутренний счётчик: " + secondMetrics.calls());
    }

    public interface NotificationSender {
        void send(String message);
    }

    public static final class FlakySender implements NotificationSender {
        private final int failuresBeforeSuccess;
        private int attempts;

        public FlakySender(int failuresBeforeSuccess) {
            this.failuresBeforeSuccess = failuresBeforeSuccess;
        }

        @Override
        public void send(String message) {
            attempts++;
            if (attempts <= failuresBeforeSuccess) {
                System.out.println("Попытка " + attempts + " не удалась");
                throw new IllegalStateException("Временная ошибка");
            }
            System.out.println("Отправлено: " + message);
        }
    }

    public static final class LoggingDecorator implements NotificationSender {
        private final NotificationSender next;

        public LoggingDecorator(NotificationSender next) {
            this.next = next;
        }

        @Override
        public void send(String message) {
            System.out.println("Лог: начало отправки");
            next.send(message);
            System.out.println("Лог: отправка завершена");
        }
    }

    public static final class MetricsDecorator implements NotificationSender {
        private final NotificationSender next;
        private int calls;

        public MetricsDecorator(NotificationSender next) {
            this.next = next;
        }

        @Override
        public void send(String message) {
            calls++;
            next.send(message);
        }

        public int calls() {
            return calls;
        }
    }

    public static final class RetryDecorator implements NotificationSender {
        private final NotificationSender next;
        private final int maxAttempts;

        public RetryDecorator(NotificationSender next, int maxAttempts) {
            this.next = next;
            this.maxAttempts = maxAttempts;
        }

        @Override
        public void send(String message) {
            RuntimeException lastError = null;
            for (int attempt = 1; attempt <= maxAttempts; attempt++) {
                try {
                    next.send(message);
                    return;
                } catch (RuntimeException exception) {
                    lastError = exception;
                }
            }
            throw lastError;
        }
    }
}
