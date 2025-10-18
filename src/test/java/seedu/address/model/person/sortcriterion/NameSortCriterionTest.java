package seedu.address.model.person.sortcriterion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Comparator;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class NameSortCriterionTest {

    @Test
    public void getComparator() {
        NameSortCriterion nameSortCriterion = new NameSortCriterion();
        Comparator<Person> comparator = nameSortCriterion.getComparator();

        Person alice = new PersonBuilder().withName("alice").build();
        Person alice2 = new PersonBuilder().withName("alice").build();
        Person aliceWithSurname = new PersonBuilder().withName("alice yeo").build();
        Person bob = new PersonBuilder().withName("bob").build();
        Person bob2 = new PersonBuilder().withName("Bob").build();

        // Sort Alphabetically
        assertTrue(comparator.compare(alice, bob) < 0);
        assertTrue(comparator.compare(bob, alice) > 0);

        // Same Name
        assertEquals(0, comparator.compare(alice, alice2));

        // Case Insensitive
        assertEquals(0, comparator.compare(bob, bob2));
        assertTrue(comparator.compare(alice, bob2) < 0);
        assertTrue(comparator.compare(bob2, alice) > 0);

        // Space
        assertTrue(comparator.compare(aliceWithSurname, bob) < 0);
        assertTrue(comparator.compare(bob, aliceWithSurname) > 0);
        assertTrue(comparator.compare(alice, aliceWithSurname) < 0);
    }
}
