package seedu.address.model.person.predicates;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.ExerciseTracker;
import seedu.address.model.person.Person;
import seedu.address.model.person.Status;


/**
 * Tests that a {@code Person}'s {@code Exercise status} matches the status of the exercise stated.
 */
public class ExerciseStatusMatchesPredicate implements Predicate<Person> {
    private Status status;
    private Index index;

    /**
     * Constructs a predicate that matches a {@code Person} if their {@code Exericse} status
     * matches the status of the exercise stated.
     *
     * @param index {@code Index} of the exercise you are trying to filter for.
     * @param status {@code Status} of the exercise chosen.
     */
    public ExerciseStatusMatchesPredicate(Index index, Status status) {
        this.index = index;
        this.status = status;
    }

    @Override
    public boolean test(Person person) {
        ExerciseTracker exerciseTracker = person.getExerciseTracker();
        List<Status> exercises = exerciseTracker.getStatuses();
        int number = index.getZeroBased();
        return exercises.get(number).equals(status);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ExerciseStatusMatchesPredicate)) {
            return false;
        }

        ExerciseStatusMatchesPredicate otherPredicate = (ExerciseStatusMatchesPredicate) other;
        return status.equals(otherPredicate.status)
                && index.equals(otherPredicate.index);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("Status", status)
                .add("index", index)
                .toString();
    }
}
