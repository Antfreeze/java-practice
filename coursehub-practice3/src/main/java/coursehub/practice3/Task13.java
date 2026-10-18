package coursehub.practice3;

public final class Task13 {
    private Task13() {
    }

    public static void run() {
        NotificationChannel[] channels = {
                new EmailChannel("student@example.com"),
                new SmsChannel("+44000000000"),
                new ConsoleChannel()
        };

        for (NotificationChannel channel : channels) {
            channel.send("Курс скоро начнётся");
        }
    }

    public interface NotificationChannel {
        void send(String message);
    }

    public static final class EmailChannel implements NotificationChannel {
        private final String address;

        public EmailChannel(String address) {
            this.address = address;
        }

        @Override
        public void send(String message) {
            System.out.println("Email на " + address + ": " + message);
        }
    }

    public static final class SmsChannel implements NotificationChannel {
        private final String phone;

        public SmsChannel(String phone) {
            this.phone = phone;
        }

        @Override
        public void send(String message) {
            System.out.println("SMS на " + phone + ": " + message);
        }
    }

    public static final class ConsoleChannel implements NotificationChannel {
        @Override
        public void send(String message) {
            System.out.println("Консоль: " + message);
        }
    }
}
