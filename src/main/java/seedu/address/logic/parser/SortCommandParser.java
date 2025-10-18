package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SORT_CRITERION;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.sortcriterion.SortCriterion;

/**
 * Parses input arguments and creates a new {@code SortCommand} object.
 */
public class SortCommandParser implements Parser<SortCommand> {

    /**
     * Parse the given {@code String} of arguments in the context of the {@code SortCommand}
     * and return a {@code SortCommand} object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    @Override
    public SortCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_SORT_CRITERION);

        SortCriterion sortCriterion;
        try {
            sortCriterion = ParserUtil.parseSortCriterion(argMultimap.getValue(PREFIX_SORT_CRITERION).orElse(""));
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE), ive);
        }

        return new SortCommand(sortCriterion);
    }
}
