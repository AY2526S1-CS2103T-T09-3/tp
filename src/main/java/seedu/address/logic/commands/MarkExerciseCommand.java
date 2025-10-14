package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.Status;


/**
 * Command used to mark an exercise
 */
public class MarkExerciseCommand extends Command {
    public static final String COMMAND_WORD = "marke";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Marks the exercise status of the person identified "
            + "by the index number used in the last person listing. "
            + "Existing exercise status will be updated based on the input.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "Example: " + COMMAND_WORD + " 1 "
            + "ei/ [EXERCISE INDEX]\n"
            + "s/ [EXERCISE STATUS]\n";

    public static final String MESSAGE_NOT_IMPLEMENTED_YET =
            "MarkExercise command not implemented yet";
    public static final String MESSAGE_MARK_EXERCISE =
            "Exercise %d marked as %s for student %d (%s)";
    private final Status status;
    private final Index studentIndex;
    private final Index exerciseIndex;

    /**
     * @param studentIndex of the student to be marked
     * @param exerciseIndex exercise number to mark
     * @param status status to mark the exercise with
     */
    public MarkExerciseCommand(Status status, Index studentIndex, Index exerciseIndex) {
        requireAllNonNull(studentIndex, status, exerciseIndex);
        this.studentIndex = studentIndex;
        this.exerciseIndex = exerciseIndex;
        this.status = status;
    }
    public Index getExerciseIndex() {
        return exerciseIndex;
    }

    public Index getStudentIndex() {
        return studentIndex;
    }

    public Status getStatus() {
        return status;
    }
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        System.out.println(status);
        System.out.println(studentIndex);
        System.out.println(exerciseIndex);
        List<Person> lastShownList = model.getFilteredPersonList();
        if (studentIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
        Person student = lastShownList.get(studentIndex.getZeroBased());
        student.getExerciseTracker().mark(exerciseIndex, status);
        model.setPerson(student, student);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_MARK_EXERCISE,
                exerciseIndex.getZeroBased(),
                status,
                studentIndex.getOneBased(),
                student.getName()));
    }
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof MarkExerciseCommand)) {
            return false;
        }

        MarkExerciseCommand e = (MarkExerciseCommand) other;
        return studentIndex.equals(e.getStudentIndex())
                && status.equals(e.getStatus())
                && exerciseIndex == e.getExerciseIndex();
    }
}


