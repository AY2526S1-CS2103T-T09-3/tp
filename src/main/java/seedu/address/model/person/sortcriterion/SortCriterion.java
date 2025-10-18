package seedu.address.model.person.sortcriterion;

import java.util.Comparator;

import seedu.address.model.person.Person;

/**
 * Represents an abstract sorting criterion for Person objects.
 * Subclasses define specific sorting behaviors.
 */
public abstract class SortCriterion {
    public static final String MESSAGE_CONSTRAINTS = "Sort Criterion should be one of: name, id";

    /**
     * Returns the comparator for sorting Person objects according to this criterion.
     */
    public abstract Comparator<Person> getComparator();

    /**
     * Returns a user-friendly display string for this criterion.
     */
    public abstract String getDisplayString();


    @Override
    public abstract String toString();

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // Two criteria are equal if they're of the same class
        return other != null && this.getClass().equals(other.getClass());
    }
}
