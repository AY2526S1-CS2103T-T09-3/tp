package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_SORT_CRITERION;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.sortcriterion.SortCriterion;

/**
 * Sorts the students in the address book by a specified criterion.
 */
public class SortCommand extends Command {
    public static final String COMMAND_WORD = "sort";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sorts the students in the display list "
            + "by the specified criterion\n"
            + "Parameters: " + PREFIX_SORT_CRITERION + "SORTCRITERION (must be one of 'name', 'id')\n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_SORT_CRITERION + "name";

    public static final String MESSAGE_SUCCESS = "Sorted all students by %s";

    private final SortCriterion sortCriterion;

    /**
     * @param sortCriterion the criterion used to sort the students in the address book
     */
    public SortCommand(SortCriterion sortCriterion) {
        requireNonNull(sortCriterion);

        this.sortCriterion = sortCriterion;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        model.sortPersonList(sortCriterion.getComparator());
        return new CommandResult(
                String.format(MESSAGE_SUCCESS, sortCriterion.getDisplayString()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof SortCommand)) {
            return false;
        }

        SortCommand otherCommand = (SortCommand) other;
        return this.sortCriterion.equals(otherCommand.sortCriterion);
    }
}
