package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_LAB_NUMBER;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Marks the specified lab as attended of an existing person in the address book.
 */
public class MarkAttendanceCommand extends Command {
    public static final String COMMAND_WORD = "markA";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Marks the specific lab of the person identified "
            + "by the index number used in the last person listing.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_LAB_NUMBER + "[LAB NUMBER]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_LAB_NUMBER + "1";

    public static final String MESSAGE_NOT_IMPLEMENTED_YET = "Remark command not implemented yet";

    public static final String MESSAGE_ARGUMENTS = "Index: %1$d, Lab Number: %2$d";

    private final Index index;
    private final Index labNumber;

    /**
     * @param index of the person in the filtered person list to mark their lab attendance
     * @param index of the lab number to be marked as attended
     */
    public MarkAttendanceCommand(Index index, Index labNumber) {
        requireAllNonNull(index, labNumber);

        this.index = index;
        this.labNumber = labNumber;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        throw new CommandException(
                String.format(MESSAGE_ARGUMENTS, index.getOneBased(), labNumber.getOneBased()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof MarkAttendanceCommand)) {
            return false;
        }

        MarkAttendanceCommand otherCommand = (MarkAttendanceCommand) other;
        return index.equals(otherCommand.index)
                && labNumber.equals(otherCommand.labNumber);
    }


}
