package task6;

import java.util.stream.Collector;

public final class StatsCollector {
    private StatsCollector() {
    }

    public static Collector<Integer, ?, Stats> toStats() {
        return Collector.of(
                Stats.Acc::new,
                (acc, v) -> {
                    acc.count++;
                    acc.sum += v;
                    if (v < acc.min)
                        acc.min = v;
                    if (v > acc.max)
                        acc.max = v;
                },
                (a, b) -> {
                    if (b.count == 0)
                        return a;
                    if (a.count == 0)
                        return b;
                    a.count += b.count;
                    a.sum += b.sum;
                    if (b.min < a.min)
                        a.min = b.min;
                    if (b.max > a.max)
                        a.max = b.max;
                    return a;
                },
                Stats::from,
                Collector.Characteristics.UNORDERED);
    }

    public static void main(String[] args) {
        var data = java.util.List.of(5, 3, 9, 1, 7);
        System.out.println("custom      : " + data.stream().collect(toStats()));
        System.out.println("summarizing : " + data.stream()
                .collect(java.util.stream.Collectors.summarizingInt(Integer::intValue)));

        System.out.println("parallel    : " + java.util.stream.IntStream.rangeClosed(1, 1_000_000)
                .boxed().parallel().collect(toStats()));
    }
}
