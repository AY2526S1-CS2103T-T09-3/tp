package seedu.address.model.person.predicates;

import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.helpers.Comparison;
import seedu.address.model.person.LabAttendanceList;
import seedu.address.model.person.Person;


/**
 * Tests that a {@code Person}'s {@code Github Username} matches any of the keywords given.
 */
public class LabAttendanceMatchesPredicate implements Predicate<Person> {
    private final double value;
    private final Comparison comparison;

    /**
     * Creates a predicate that compares a person's lab-attendance percentage against a target.
     *
     * @param value      target attendance percentage in the range 0–100
     * @param comparison comparison operator to apply (EQ, GE, LE, GT, LT)
     */
    public LabAttendanceMatchesPredicate(double value, Comparison comparison) {
        this.value = value;
        this.comparison = comparison;
    }

    @Override
    public boolean test(Person person) {
        LabAttendanceList attendaceList = person.getLabAttendanceList();
        double labAttendance = attendaceList.calculateLabAttendance();
        switch (comparison) {
        case EQ:
            return labAttendance == value;
        case GE:
            return labAttendance >= value;
        case LE:
            return labAttendance <= value;
        case GT:
            return labAttendance > value;
        case LT:
            return labAttendance < value;
        default:
            return false;
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof LabAttendanceMatchesPredicate)) {
            return false;
        }

        LabAttendanceMatchesPredicate otherPredicate = (LabAttendanceMatchesPredicate) other;
        return comparison.equals(otherPredicate.comparison)
                && value == otherPredicate.value;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("value", value)
                .add("comparison", comparison)
                .toString();
    }
}
