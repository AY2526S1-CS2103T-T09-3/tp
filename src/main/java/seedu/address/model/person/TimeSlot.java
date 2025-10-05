package seedu.address.model.person;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a time slot with a start and end date-time.
 */
public class TimeSlot {
    private final LocalDateTime start;
    private final LocalDateTime end;

    /**
     * Constructs a TimeSlot with the given start and end times.
     *
     * @param start the start date and time
     * @param end   the end date and time (must be after start)
     */
    public TimeSlot(LocalDateTime start, LocalDateTime end) {
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("End time must be after start time.");
        }
        this.start = start;
        this.end = end;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * Returns the duration of the time slot.
     */
    public Duration getDuration() {
        return Duration.between(start, end);
    }

    /**
     * Returns true if this time slot overlaps with another.
     */
    public boolean overlapsWith(TimeSlot other) {
        return !end.isBefore(other.start) && !start.isAfter(other.end);
    }

    @Override
    public String toString() {
        return String.format("TimeSlot from %s to %s", start, end);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof TimeSlot)) {
            return false;
        }
        TimeSlot other = (TimeSlot) o;
        return start.equals(other.start) && end.equals(other.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}
