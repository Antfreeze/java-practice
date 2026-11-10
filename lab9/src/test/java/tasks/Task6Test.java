package tasks;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Task6Test {

    @Test
    @Timeout(10)
    void synchronizedCourseHasExactlyCapacitySuccesses() throws Exception {
        Task6.Course course = new Task6.Course(10);
        int successes = Task6.runEnroll(course::enroll, 100);

        assertEquals(10, successes);
        assertEquals(10, course.enrolledCount());
        assertEquals(10, course.size());
    }

    @Test
    @Timeout(10)
    void atomicCourseHasExactlyCapacitySuccesses() throws Exception {
        Task6.AtomicCourse course = new Task6.AtomicCourse(10);
        int successes = Task6.runEnroll(course::enroll, 100);

        assertEquals(10, successes);
        assertEquals(10, course.enrolledCount());
        assertEquals(10, course.size());
    }

    @Test
    void duplicateStudentIsRejected() {
        Task6.Course course = new Task6.Course(10);
        assertTrue(course.enroll("student-1"));
        assertFalse(course.enroll("student-1"));
        assertEquals(1, course.enrolledCount());
        assertEquals(1, course.size());

        Task6.AtomicCourse atomic = new Task6.AtomicCourse(10);
        assertTrue(atomic.enroll("student-1"));
        assertFalse(atomic.enroll("student-1"));
        assertEquals(1, atomic.enrolledCount());
        assertEquals(1, atomic.size());
    }

    @Test
    @Timeout(10)
    void sameStudentCanEnrollOnlyOnceUnderConcurrency() throws Exception {
        Task6.Course course = new Task6.Course(10);
        int successes = Task6.runEnrollSameId(course::enroll, 50, "student-1");
        assertEquals(1, successes);
        assertEquals(1, course.enrolledCount());
        assertEquals(1, course.size());

        Task6.AtomicCourse atomic = new Task6.AtomicCourse(10);
        int atomicSuccesses = Task6.runEnrollSameId(atomic::enroll, 50, "student-1");
        assertEquals(1, atomicSuccesses);
        assertEquals(1, atomic.enrolledCount());
        assertEquals(1, atomic.size());
    }

    @Test
    void rejectedEnrollDoesNotChangeState() {
        Task6.Course course = new Task6.Course(1);
        assertTrue(course.enroll("a"));
        assertFalse(course.enroll("b"));
        assertEquals(1, course.enrolledCount());
        assertEquals(1, course.size());
        assertFalse(course.contains("b"));

        Task6.AtomicCourse atomic = new Task6.AtomicCourse(1);
        assertTrue(atomic.enroll("a"));
        assertFalse(atomic.enroll("b"));
        assertEquals(1, atomic.enrolledCount());
        assertEquals(1, atomic.size());
        assertFalse(atomic.contains("b"));
    }
}
