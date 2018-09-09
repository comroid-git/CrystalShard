package de.kaleidox.crystalshard.util;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

public class BiTimestamp {
    private final Instant start;
    private final Instant end;
    private final Duration duration;

    public BiTimestamp(JsonNode data) {
        this.start = data.has("start") ?
                Instant.ofEpochMilli(data.path("start").asLong()) : null;
        this.end = data.has("end") ?
                Instant.ofEpochMilli(data.path("end").asLong()) : null;

        if (Objects.nonNull(start) && Objects.nonNull(end)) {
            this.duration = Duration.between(start, end);
        } else {
            this.duration = null;
        }
    }

    public Optional<Instant> getStart() {
        return Optional.ofNullable(start);
    }

    public Optional<Instant> getEnd() {
        return Optional.ofNullable(end);
    }

    public Optional<Duration> getDuration() {
        return Optional.ofNullable(duration);
    }
}
