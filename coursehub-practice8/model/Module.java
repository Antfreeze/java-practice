package model;

import java.util.List;

public record Module(String name, List<String> topics) {
    public Module {
        topics = topics == null ? List.of() : List.copyOf(topics);
    }
}
