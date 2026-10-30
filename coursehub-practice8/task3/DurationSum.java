package task3;

public record DurationSum(long total, int count) {
    public static final DurationSum ZERO = new DurationSum(0, 0);

    public DurationSum plus(DurationSum o) {
        return new DurationSum(total + o.total, count + o.count);
    }

    public double average() {
        return count == 0 ? 0.0 : (double) total / count;
    }
}
