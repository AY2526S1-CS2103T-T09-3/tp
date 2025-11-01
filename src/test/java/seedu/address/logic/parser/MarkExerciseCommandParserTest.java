package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EXERCISE_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_EXERCISE;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.MultiIndex;
import seedu.address.logic.commands.MarkExerciseCommand;

public class MarkExerciseCommandParserTest {

    private final MarkExerciseCommandParser parser = new MarkExerciseCommandParser();

    @Test
    public void parse_validArgs_returnsMarkExerciseCommand() {
        // "1 ei/1 s/y"
        String userInput = INDEX_FIRST_PERSON.getOneBased() + " "
                + PREFIX_EXERCISE_INDEX + INDEX_FIRST_EXERCISE.getOneBased() + " "
                + PREFIX_STATUS + "y";

        MarkExerciseCommand expectedCommand = new MarkExerciseCommand(
                new MultiIndex(INDEX_FIRST_PERSON),
                INDEX_FIRST_EXERCISE,
                true
        );

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_missingCompulsoryField_failure() {
        String expectedMessage = String.format(
                MESSAGE_INVALID_COMMAND_FORMAT,
                MarkExerciseCommand.MESSAGE_USAGE
        );

        // Missing everything
        assertParseFailure(parser, MarkExerciseCommand.COMMAND_WORD, expectedMessage);

        // Missing student index
        assertParseFailure(parser, MarkExerciseCommand.COMMAND_WORD + " "
                + PREFIX_EXERCISE_INDEX + INDEX_FIRST_EXERCISE.getOneBased()
                + " " + PREFIX_STATUS + "y", expectedMessage);

        // Missing exercise index
        assertParseFailure(parser, MarkExerciseCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased()
                + " " + PREFIX_STATUS + "y", expectedMessage);

        // Missing status
        assertParseFailure(parser, MarkExerciseCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased()
                + " " + PREFIX_EXERCISE_INDEX + INDEX_FIRST_EXERCISE.getOneBased(), expectedMessage);
    }
}
