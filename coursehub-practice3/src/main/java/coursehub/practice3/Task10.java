package coursehub.practice3;

public final class Task10 {
    private Task10() {
    }

    public static void run() {
        BothCapabilities sender = new BothCapabilities();
        System.out.println(sender.channel());
    }

    public interface EmailCapability {
        default String channel() {
            return "email";
        }
    }

    public interface ConsoleCapability {
        default String channel() {
            return "console";
        }
    }

    public static final class BothCapabilities implements EmailCapability, ConsoleCapability {
        @Override
        public String channel() {
            return EmailCapability.super.channel() + " + " + ConsoleCapability.super.channel();
        }
    }
}
