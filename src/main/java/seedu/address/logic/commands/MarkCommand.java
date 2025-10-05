package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Marks a lab or exercise as DONE for the specified person.
 */
public class MarkCommand extends Command {

    public static final String COMMAND_WORD = "mark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks a specific lab or exercise as done for the person identified "
            + "by the index number in the displayed person list.\n"
            + "Parameters: PERSON_INDEX (must be a positive integer) "
            + "[lab|ex] MILESTONE_INDEX (must be a positive integer)\n"
            + "Examples: \n"
            + "  " + COMMAND_WORD + " 1 lab 2\n"
            + "  " + COMMAND_WORD + " 3 ex 5";

    public static final String MESSAGE_MARK_SUCCESS = "Marked %1$s #%2$d as DONE for: %3$s";

    private final Index personIndex;
    private final boolean isLab;
    private final int milestoneIndex; // zero-based internally

    /**
     * @param personIndex index of person to mark
     * @param isLab lab number of to mark
     * @param milestoneIndex
     */
    public MarkCommand(Index personIndex, boolean isLab, int milestoneIndex) {
        this.personIndex = personIndex;
        this.isLab = isLab;
        this.milestoneIndex = milestoneIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (personIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToMark = lastShownList.get(personIndex.getZeroBased());

        if (isLab) {
            if (milestoneIndex < 0 || milestoneIndex >= personToMark.getLabTracker().getStatuses().size()) {
                throw new CommandException("Invalid lab index.");
            }
            personToMark.getLabTracker().mark(milestoneIndex);
        } else {
            if (milestoneIndex < 0 || milestoneIndex >= personToMark.getExerciseTracker().getStatuses().size()) {
                throw new CommandException("Invalid exercise index.");
            }
            personToMark.getExerciseTracker().mark(milestoneIndex);
        }

        model.setPerson(personToMark, personToMark);

        String type = isLab ? "Lab" : "Exercise";
        return new CommandResult(String.format(MESSAGE_MARK_SUCCESS, type, milestoneIndex + 1,
                Messages.format(personToMark)));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof MarkCommand
                && personIndex.equals(((MarkCommand) other).personIndex)
                && milestoneIndex == ((MarkCommand) other).milestoneIndex
                && isLab == ((MarkCommand) other).isLab);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("personIndex", personIndex)
                .add("isLab", isLab)
                .add("milestoneIndex", milestoneIndex)
                .toString();
    }
}
