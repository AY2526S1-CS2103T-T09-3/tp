package seedu.address.model.person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import seedu.address.commons.core.index.Index;

/**
 * Represents a Person's address in the address book.
 */
public class ExerciseTracker {

    public static final String MESSAGE_CONSTRAINTS = "Exercise tracker takes in statuses";
    public static final int NUMBER_OF_EXERCISES = 10;
    public final ArrayList<Status> statuses;

    /**
     * Initialises statuses to all be not done
     */
    public ExerciseTracker() {
        this.statuses = new ArrayList<>(
                Collections.nCopies(NUMBER_OF_EXERCISES, Status.NOT_DONE)
        );
    }
    /**
     * Initialises statuses to an input arraylist
     */
    public ExerciseTracker(ArrayList<Status> statuses) {
        this.statuses = statuses;
    }

    @Override
    public String toString() {
        return IntStream.range(0, statuses.size())
                .mapToObj(x -> String.format("ex %d: %s ", x, statuses.get(x)))
                .collect(Collectors.joining());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ExerciseTracker)) {
            return false;
        }
        ExerciseTracker otherTracker = (ExerciseTracker) other;
        return statuses.equals(otherTracker.statuses);
    }

    @Override
    public int hashCode() {
        return statuses.hashCode();
    }
    public ArrayList<Status> getStatuses() {
        return statuses;
    }

    public void mark(Index index, Status status) {
        statuses.set(index.getZeroBased(), status);
    }
}

