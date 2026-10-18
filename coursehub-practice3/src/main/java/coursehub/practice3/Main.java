package coursehub.practice3;

public final class Main {
    private Main() {
    }

    public static void main(String[] args) {
        runTask("Задание 1", Task01::run);
        runTask("Задание 2", Task02::run);
        runTask("Задание 3", Task03::run);
        runTask("Задание 4", Task04::run);
        runTask("Задание 5", Task05::run);
        runTask("Задание 6", Task06::run);
        runTask("Задание 7", Task07::run);
        runTask("Задание 8", Task08::run);
        runTask("Задание 9", Task09::run);
        runTask("Задание 10", Task10::run);
        runTask("Задание 11", Task11::run);
        runTask("Задание 12", Task12::run);
        runTask("Задание 13", Task13::run);
        runTask("Задание 14", Task14::run);
        runTask("Задание 15", Task15::run);
    }

    private static void runTask(String title, Runnable task) {
        System.out.println();
        System.out.println("========== " + title + " ==========");
        task.run();
    }
}
