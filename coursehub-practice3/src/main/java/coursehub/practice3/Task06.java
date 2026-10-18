package coursehub.practice3;

public final class Task06 {
    private Task06() {
    }

    public static void run() {
        Handler handler = new AccidentalHandler();
        System.out.println(handler.handle("hello"));

        AccidentalHandler accidental = new AccidentalHandler();
        System.out.println(accidental.handle((Object) "hello"));

        Handler correct = new CorrectHandler();
        System.out.println(correct.handle("hello"));
    }

    public static class Handler {
        public String handle(String value) {
            return "Родительский обработчик: " + value;
        }
    }

    public static final class AccidentalHandler extends Handler {
        public String handle(Object value) {
            return "Перегруженный метод: " + value;
        }
    }

    public static final class CorrectHandler extends Handler {
        @Override
        public String handle(String value) {
            return "Переопределённый метод: " + value;
        }
    }
}
