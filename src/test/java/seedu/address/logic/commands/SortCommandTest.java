package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.sortcriterion.NameSortCriterion;
import seedu.address.model.person.sortcriterion.SortCriterion;
import seedu.address.model.person.sortcriterion.StudentIdSortCriterion;

public class SortCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    private final SortCriterion nameSortCriterion = new NameSortCriterion();
    private final SortCriterion studentIdSortCriterion = new StudentIdSortCriterion();


    @Test
    public void execute() {
        // TODO
    }

    @Test
    public void equals() {
        final SortCommand standardCommand = new SortCommand(nameSortCriterion);

        // Same values
        SortCommand commandWithSameValues = new SortCommand(nameSortCriterion);
        assertEquals(standardCommand, commandWithSameValues);

        // Same object
        assertEquals(standardCommand, standardCommand);

        // Null
        assertNotEquals(standardCommand, null);

        // Different Command
        assertNotEquals(new ListCommand(), standardCommand);

        // Different criterion
        assertNotEquals(new SortCommand(studentIdSortCriterion), standardCommand);
    }
}
