package coursehub.practice6.numbers;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public final class SampleStats {
    private final int min, max;
    private final BigDecimal average;
    public SampleStats(int min, int max, BigDecimal average) {
        this.min = min; this.max = max; this.average = average;
    }
    public static SampleStats calculate(List<Integer> values) {
        if (values == null || values.isEmpty()) throw new IllegalArgumentException("Выборка пуста");
        int min = values.get(0), max = values.get(0); long sum = 0;
        for (int value : values) { min = Math.min(min, value); max = Math.max(max, value); sum += value; }
        BigDecimal avg = BigDecimal.valueOf(sum).divide(BigDecimal.valueOf(values.size()), 2, RoundingMode.HALF_UP);
        return new SampleStats(min, max, avg);
    }
    public int getMin() { return min; }
    public int getMax() { return max; }
    public BigDecimal getAverage() { return average; }
    @Override public String toString() { return "SampleStats{min=" + min + ", max=" + max + ", average=" + average + "}"; }
}
