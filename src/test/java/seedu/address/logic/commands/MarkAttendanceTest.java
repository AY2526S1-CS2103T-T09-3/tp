package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.MarkAttendanceCommand.MESSAGE_ARGUMENTS;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_LAB;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_LAB;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class MarkAttendanceTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute() {
        assertCommandFailure(new MarkAttendanceCommand(INDEX_FIRST_PERSON, INDEX_FIRST_LAB), model,
                String.format(MESSAGE_ARGUMENTS, INDEX_FIRST_PERSON.getOneBased(), INDEX_FIRST_LAB.getOneBased()));
    }

    @Test
    public void equals() {
        final MarkAttendanceCommand standardCommand = new MarkAttendanceCommand(INDEX_FIRST_PERSON, INDEX_FIRST_LAB);

        // same values -> returns true
        MarkAttendanceCommand commandWithSameValues = new MarkAttendanceCommand(INDEX_FIRST_PERSON, INDEX_FIRST_LAB);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new MarkAttendanceCommand(INDEX_SECOND_PERSON, INDEX_FIRST_LAB)));

        // different remark -> returns false
        assertFalse(standardCommand.equals(new MarkAttendanceCommand(INDEX_FIRST_PERSON, INDEX_SECOND_LAB)));
    }

}
