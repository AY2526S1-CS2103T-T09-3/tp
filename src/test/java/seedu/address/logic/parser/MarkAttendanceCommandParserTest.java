package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LAB_NUMBER;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_LAB;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.MarkAttendanceCommand;

// TODO: Refactor the test cases later
public class MarkAttendanceCommandParserTest {
    private MarkAttendanceCommandParser parser = new MarkAttendanceCommandParser();
    private final Index validLabNumber = INDEX_FIRST_LAB;

    @Test
    public void parse_indexAndLabSpecified_success() {
        String userInput = INDEX_FIRST_PERSON.getOneBased() + " " + PREFIX_LAB_NUMBER + validLabNumber.getOneBased();
        MarkAttendanceCommand expectedCommand = new MarkAttendanceCommand(INDEX_FIRST_PERSON, validLabNumber);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_missingCompulsoryField_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, MarkAttendanceCommand.MESSAGE_USAGE);

        // No Parameters
        assertParseFailure(parser, MarkAttendanceCommand.COMMAND_WORD, expectedMessage);

        // No index
        assertParseFailure(parser, MarkAttendanceCommand.COMMAND_WORD
                + " " + PREFIX_LAB_NUMBER + validLabNumber.getOneBased(), expectedMessage);

        //No Lab Number
        assertParseFailure(parser, MarkAttendanceCommand.COMMAND_WORD
                + " " + INDEX_FIRST_PERSON + " " + PREFIX_LAB_NUMBER + validLabNumber.getOneBased(), expectedMessage);
    }
}
