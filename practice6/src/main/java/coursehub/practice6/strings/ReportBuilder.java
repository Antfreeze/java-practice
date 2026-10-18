package coursehub.practice6.strings;

public class ReportBuilder {
    public static String buildWithPlus(int count) {
        String result = "";
        for (int i = 1; i <= count; i++) result = result + "Строка " + i + "\n";
        return result;
    }
    public static String buildWithBuilder(int count) {
        StringBuilder result = new StringBuilder();
        for (int i = 1; i <= count; i++) result.append("Строка ").append(i).append('\n');
        return result.toString();
    }
    public static void main(String[] args) {
        // Учебный замер, не JMH. String неизменяем, поэтому + создаёт новые строки.
        long start = System.nanoTime();
        String a = buildWithPlus(10_000);
        long plus = System.nanoTime() - start;
        start = System.nanoTime();
        String b = buildWithBuilder(10_000);
        long builder = System.nanoTime() - start;
        System.out.println(a.equals(b));
        System.out.println("+: " + plus + " нс; StringBuilder: " + builder + " нс");
    }
}
