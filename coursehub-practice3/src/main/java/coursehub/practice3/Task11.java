package coursehub.practice3;

import java.util.Objects;

public final class Task11 {
    private Task11() {
    }

    public static void run() {
        Point point = new Point(2, 3);
        ColoredPoint coloredPoint = new ColoredPoint(2, 3, "blue");
        System.out.println("Нарушение симметрии: " + point.equals(coloredPoint) + " / " + coloredPoint.equals(point));

        StrictPoint strictPoint = new StrictPoint(2, 3);
        StrictColoredPoint strictColoredPoint = new StrictColoredPoint(2, 3, "blue");
        System.out.println("Строгое равенство: " + strictPoint.equals(strictColoredPoint) + " / " + strictColoredPoint.equals(strictPoint));

        ComposedPoint first = new ComposedPoint(new Position(2, 3), "blue");
        ComposedPoint second = new ComposedPoint(new Position(2, 3), "red");
        System.out.println("Композиция: " + first.equals(second));
    }

    public static class Point {
        protected final int x;
        protected final int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object object) {
            if (!(object instanceof Point other)) {
                return false;
            }
            return x == other.x && y == other.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    public static final class ColoredPoint extends Point {
        private final String color;

        public ColoredPoint(int x, int y, String color) {
            super(x, y);
            this.color = color;
        }

        @Override
        public boolean equals(Object object) {
            if (!(object instanceof ColoredPoint other)) {
                return false;
            }
            return super.equals(other) && color.equals(other.color);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), color);
        }
    }

    public static class StrictPoint {
        protected final int x;
        protected final int y;

        public StrictPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object object) {
            if (object == null || getClass() != object.getClass()) {
                return false;
            }
            StrictPoint other = (StrictPoint) object;
            return x == other.x && y == other.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    public static final class StrictColoredPoint extends StrictPoint {
        private final String color;

        public StrictColoredPoint(int x, int y, String color) {
            super(x, y);
            this.color = color;
        }

        @Override
        public boolean equals(Object object) {
            if (object == null || getClass() != object.getClass()) {
                return false;
            }
            StrictColoredPoint other = (StrictColoredPoint) object;
            return super.equals(other) && color.equals(other.color);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), color);
        }
    }

    public record Position(int x, int y) {
    }

    public record ComposedPoint(Position position, String color) {
    }
}
