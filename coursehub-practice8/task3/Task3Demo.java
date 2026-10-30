package task3;

import java.util.List;

public class Task3Demo {

    public static DurationSum reduceCorrect(List<Integer> durations) {
        return durations.stream()
                .reduce(DurationSum.ZERO,
                        (acc, d) -> acc.plus(new DurationSum(d, 1)),
                        DurationSum::plus);
    }

    public static DurationSum reduceWrong(List<Integer> durations) {
        DurationSum wrongIdentity = new DurationSum(0, 1);
        return durations.stream()
                .reduce(wrongIdentity,
                        (acc, d) -> acc.plus(new DurationSum(d, 1)),
                        DurationSum::plus);
    }

    public static void main(String[] args) {
        System.out.println("empty correct -> " + reduceCorrect(List.of()));
        System.out.println("empty wrong   -> " + reduceWrong(List.of()));
        System.out.println("[10,20]       -> " + reduceCorrect(List.of(10, 20)));
    }
}
