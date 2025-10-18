package seedu.address.model.person.sortcriterion;

import java.util.Comparator;

import seedu.address.model.person.Person;

/**
 * Sorts persons by their name in ascending alphabetical order (case-insensitive).
 */
public class NameSortCriterion extends SortCriterion {
    public static final String CRITERION_KEYWORD = "name";

    @Override
    public Comparator<Person> getComparator() {
        return Comparator.comparing(Person::getName);
    }

    @Override
    public String getDisplayString() {
        return "name";
    }

    @Override
    public String toString() {
        return CRITERION_KEYWORD;
    }
}
