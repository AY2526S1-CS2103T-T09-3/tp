package seedu.address.model.person.sortcriterion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class SortCriterionTest {

    @Test
    public void equals() {
        SortCriterion name1 = new NameSortCriterion();
        SortCriterion studentId1 = new StudentIdSortCriterion();
        SortCriterion name2 = new NameSortCriterion();
        SortCriterion studentId2 = new StudentIdSortCriterion();

        assertTrue(name1.equals(name1));

        assertFalse(name1.equals(studentId1));

        assertTrue(name1.equals(name2));
        assertTrue(studentId1.equals(studentId2));

        assertFalse(name1.equals(null));
    }

    @Test
    public void toString_default_success() {
        SortCriterion name = new NameSortCriterion();
        SortCriterion studentId = new StudentIdSortCriterion();

        assertEquals("name", name.toString());
        assertEquals("id", studentId.toString());
    }

    @Test
    public void getDisplayString() {
        SortCriterion name = new NameSortCriterion();
        SortCriterion studentId = new StudentIdSortCriterion();

        assertEquals("name", name.getDisplayString());
        assertEquals("student id", studentId.getDisplayString());
    }
}
