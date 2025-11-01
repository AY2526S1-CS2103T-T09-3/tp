package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_HUNDRED_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.MultiIndex;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.GradeTracker;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

/**
 * Unit tests for {@link GradeCommand}.
 */
public class GradeCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    // -------------------------------------------------------------------------
    // Success case
    // -------------------------------------------------------------------------
    @Test
    public void execute_markExamPassed_success() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        GradeTracker gradeMap = firstPerson.getGradeTracker().copy();
        gradeMap.markExamPassed("midterm");
        Person gradedPerson = new PersonBuilder(firstPerson)
                .withGradeMap(gradeMap.toString())
                .build();

        GradeCommand command = new GradeCommand(
                new MultiIndex(INDEX_FIRST_PERSON),
                "midterm",
                true
        );

        String expectedMessage = String.format(
                GradeCommand.MESSAGE_GRADE_SUCCESS,
                "midterm",
                "passed",
                firstPerson.getNameAndID()
        );

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, gradedPerson);

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    // -------------------------------------------------------------------------
    // Invalid person index
    // -------------------------------------------------------------------------
    @Test
    public void execute_invalidPersonIndex_throwsCommandException() {
        GradeCommand command = new GradeCommand(
                new MultiIndex(INDEX_HUNDRED_PERSON),
                "midterm",
                true
        );

        assertThrows(CommandException.class, () -> command.execute(model));
    }

    // -------------------------------------------------------------------------
    // Invalid exam name
    // -------------------------------------------------------------------------
    @Test
    public void execute_invalidExamName_throwsCommandException() {
        GradeCommand command = new GradeCommand(
                new MultiIndex(INDEX_FIRST_PERSON),
                "notrealexam",
                true
        );

        assertThrows(CommandException.class, () -> command.execute(model));
    }

    // -------------------------------------------------------------------------
    // Equality
    // -------------------------------------------------------------------------
    @Test
    public void equals() {
        final GradeCommand standardCommand =
                new GradeCommand(new MultiIndex(INDEX_FIRST_PERSON), "midterm", true);

        // same values -> true
        GradeCommand commandWithSameValues =
                new GradeCommand(new MultiIndex(INDEX_FIRST_PERSON), "midterm", true);
        assertEquals(standardCommand, commandWithSameValues);

        // same object -> true
        assertEquals(standardCommand, standardCommand);

        // null -> false
        assertNotEquals(null, standardCommand);

        // different type -> false
        assertNotEquals(new ClearCommand(), standardCommand);

        // different student index -> false
        assertNotEquals(
                new GradeCommand(new MultiIndex(INDEX_SECOND_PERSON), "midterm", true),
                standardCommand);

        // different exam name -> false
        assertNotEquals(
                new GradeCommand(new MultiIndex(INDEX_FIRST_PERSON), "pe1", true),
                standardCommand);

        // different pass/fail state -> false
        assertNotEquals(
                new GradeCommand(new MultiIndex(INDEX_FIRST_PERSON), "midterm", false),
                standardCommand);
    }
}
