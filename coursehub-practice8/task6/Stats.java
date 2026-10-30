package task6;

public final class Stats {
    private final long count;
    private final long sum;
    private final int min;
    private final int max;

    private Stats(long count, long sum, int min, int max) {
        this.count = count;
        this.sum = sum;
        this.min = min;
        this.max = max;
    }

    public static final class Acc {
        long count;
        long sum;
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
    }

    public static Stats from(Acc a) {
        return new Stats(a.count, a.sum,
                a.count == 0 ? 0 : a.min,
                a.count == 0 ? 0 : a.max);
    }

    public long count() {
        return count;
    }

    public long sum() {
        return sum;
    }

    public int min() {
        return min;
    }

    public int max() {
        return max;
    }

    public double avg() {
        return count == 0 ? 0.0 : (double) sum / count;
    }

    @Override
    public String toString() {
        return "Stats{count=%d, sum=%d, min=%d, max=%d, avg=%.2f}"
                .formatted(count, sum, min, max, avg());
    }
}
