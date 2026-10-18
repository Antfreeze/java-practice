package coursehub.practice6.domain;

import java.util.Objects;

public final class CourseOffering {
    private final String courseId;
    private final CourseTitle title;
    private final Money price;
    private final WebinarSeriesPlan plan;

    public CourseOffering(String courseId, CourseTitle title, Money price, WebinarSeriesPlan plan) {
        this.courseId = Objects.requireNonNull(courseId);
        this.title = Objects.requireNonNull(title);
        this.price = Objects.requireNonNull(price);
        this.plan = Objects.requireNonNull(plan);
        if (courseId.isBlank())
            throw new IllegalArgumentException("courseId не должен быть пустым");
    }

    public String getCourseId() {
        return courseId;
    }

    public CourseTitle getTitle() {
        return title;
    }

    public Money getPrice() {
        return price;
    }

    public WebinarSeriesPlan getPlan() {
        return plan;
    }
}
