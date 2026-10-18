package coursehub.practice6.strings;

public class StringIdentityDemo {
    public static boolean sameText(String a, String b) {
        if (a == null || b == null) throw new IllegalArgumentException("Строка не должна быть null");
        return a.equals(b);
    }
    public static void main(String[] args) {
        String a = "JAVA", b = "JAVA", c = new String("JAVA");
        String compileTime = "JA" + "VA";
        String suffix = "VA";
        String runtime = "JA" + suffix;
        System.out.println(a == b);
        System.out.println(a.equals(c));
        System.out.println(a == c);
        System.out.println(a == compileTime);
        System.out.println(a == runtime);
        System.out.println(a == c.intern());
    }
}
