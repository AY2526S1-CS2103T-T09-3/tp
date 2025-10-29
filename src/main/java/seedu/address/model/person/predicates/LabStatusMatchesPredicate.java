package seedu.address.model.person.predicates;

import java.util.function.Predicate;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.LabAttendance;
import seedu.address.model.person.LabList;
import seedu.address.model.person.Person;


/**
 * Tests that a {@code Person}'s {@code Exercise status} matches the status of the lab stated.
 */
public class LabStatusMatchesPredicate implements Predicate<Person> {
    private String status;
    private Index index;

    /**
     * Constructs a predicate that matches a {@code Person} if their {@code Lab} status
     * matches the status of the lab stated.
     *
     * @param index {@code Index} of the lab you are trying to filter for.
     * @param status {@code Status} of the lab chosen.
     */
    public LabStatusMatchesPredicate(Index index, String status) {
        this.index = index;
        this.status = status;
    }

    @Override
    public boolean test(Person person) {
        LabList labAttendanceList = (LabList) person.getLabAttendanceList();
        LabAttendance[] labAttendances = labAttendanceList.getLabs();
        int number = index.getZeroBased();
        return labAttendances[number].getStatus().equals(status);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof LabStatusMatchesPredicate)) {
            return false;
        }

        LabStatusMatchesPredicate otherPredicate = (LabStatusMatchesPredicate) other;
        return status == (otherPredicate.status)
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
