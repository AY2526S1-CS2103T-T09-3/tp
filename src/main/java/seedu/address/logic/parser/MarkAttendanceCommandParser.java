package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LAB_NUMBER;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.MarkAttendanceCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code MarkAttendanceCommand} object.
 */
public class MarkAttendanceCommandParser implements Parser<MarkAttendanceCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the {@code MarkAttendanceCommand}
     * and returns a {@code MarkAttendanceCommand} object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public MarkAttendanceCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args,
                PREFIX_LAB_NUMBER);

        Index index;
        Index labNumber;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
            labNumber = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_LAB_NUMBER).orElse(""));
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    MarkAttendanceCommand.MESSAGE_USAGE), ive);
        }

        return new MarkAttendanceCommand(index, labNumber);
    }
}
