package task4;

public record LevelSummary(long count, double avgDuration) {
    @Override
    public String toString() {
        return "LevelSummary{count=%d, avg=%.2f}".formatted(count, avgDuration);
    }
}
