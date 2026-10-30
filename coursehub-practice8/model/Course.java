package model;

import java.util.List;

public record Course(
        String code,
        String title,
        String level,
        int duration,
        List<Module> modules) {
    public Course {
        if (code == null || code.isBlank())
            throw new IllegalArgumentException("code");
        if (duration < 0)
            throw new IllegalArgumentException("duration<0");
        modules = modules == null ? List.of() : List.copyOf(modules);
    }
}
