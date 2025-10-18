package seedu.address.model.person.sortcriterion;

import java.util.Comparator;

import seedu.address.model.person.Person;

/**
 * Sorts persons by their student ID in ascending order.
 */
public class StudentIdSortCriterion extends SortCriterion {
    public static final String CRITERION_KEYWORD = "id";

    @Override
    public Comparator<Person> getComparator() {
        return Comparator.comparing(Person::getStudentId);
    }

    @Override
    public String getDisplayString() {
        return "student id";
    }

    @Override
    public String toString() {
        return CRITERION_KEYWORD;
    }
}
