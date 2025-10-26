package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.model.person.ExerciseTracker.NUMBER_OF_EXERCISES;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.core.index.MultiIndex;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.ExerciseTracker;
import seedu.address.model.person.Person;

/**
 * Marks a specific exercise as a given status for one or more students in the address book.
 */
public class MarkExerciseCommand extends MultiIndexCommand {

    public static final String COMMAND_WORD = "marke";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Marks the exercise status of one or more persons "
            + "identified by their index numbers in the last person listing.\n"
            + "Parameters: INDEX (must be a positive integer or range X:Y) "
            + "ei/EXERCISEINDEX s/STATUS\n"
            + "Example: " + COMMAND_WORD + " 1:3 ei/1 s/y";

    public static final String MESSAGE_MARK_EXERCISE_SUCCESS =
            "Exercise %1$d marked as %2$s for: %3$s";
    public static final String MESSAGE_FAILURE_ALREADY_MARKED =
            "Exercise %1$d already marked as %2$s for %3$s";
    public static final String MESSAGE_INDEX_OUT_OF_BOUNDS =
            "Exercise index out of bounds! Index should be between 0 and %d.";

    private static final int HIGHEST_INDEX = NUMBER_OF_EXERCISES - 1;

    private final MultiIndex studentIndex;
    private final Index exerciseIndex;
    private final boolean isDone;
    private final List<Person> alreadyMarkedPersons = new ArrayList<>();
    private String action;

    /**
     * @param studentIndex indices of students to be marked
     * @param exerciseIndex exercise number to mark
     * @param isDone status to mark the exercise with
     */
    public MarkExerciseCommand(MultiIndex studentIndex, Index exerciseIndex, boolean isDone) {
        super(studentIndex);
        requireAllNonNull(studentIndex, exerciseIndex, isDone);
        this.studentIndex = studentIndex;
        this.exerciseIndex = exerciseIndex;
        this.isDone = isDone;
        this.action = isDone ? "done" : "not done";
    }

    @Override
    protected Person applyActionToPerson(Model model, Person personToEdit) throws CommandException {
        ExerciseTracker updatedExerciseTracker = personToEdit.getExerciseTracker().copy();
        try {
            updatedExerciseTracker.markExercise(exerciseIndex, isDone);
        } catch (IndexOutOfBoundsException e) {
            throw new CommandException(String.format(MESSAGE_INDEX_OUT_OF_BOUNDS, HIGHEST_INDEX));
        } catch (IllegalStateException e) {
            alreadyMarkedPersons.add(personToEdit);
            return null;
        }

        Person updatedPerson = new Person(
                personToEdit.getStudentId(),
                personToEdit.getName(),
                personToEdit.getPhone(),
                personToEdit.getEmail(),
                personToEdit.getTags(),
                personToEdit.getGithubUsername(),
                updatedExerciseTracker,
                personToEdit.getLabAttendanceList(),
                personToEdit.getGradeMap()
        );

        model.setPerson(personToEdit, updatedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return updatedPerson;
    }

    @Override
    protected CommandResult buildResult(List<Person> updatedPersons) {
        return new CommandResult(generateResponseMessage(alreadyMarkedPersons, updatedPersons));
    }

    private String generateResponseMessage(List<Person> alreadyMarkedPersons, List<Person> personsEdited) {
        String editedNames = personsEdited.stream()
                .map(person -> person.getName().fullName)
                .collect(Collectors.joining(", "));

        String alreadyMarkedMessage = compileAlreadyMarkedMessage(alreadyMarkedPersons);
        StringBuilder message = new StringBuilder();

        if (!alreadyMarkedMessage.isEmpty()) {
            message.append(alreadyMarkedMessage).append("\n");
        }

        if (!personsEdited.isEmpty()) {
            String successMessage = String.format(MESSAGE_MARK_EXERCISE_SUCCESS,
                    exerciseIndex.getZeroBased(), action, editedNames);
            message.append(successMessage);
        }

        return message.toString().trim();
    }

    private String compileAlreadyMarkedMessage(List<Person> alreadyMarkedPersons) {
        if (alreadyMarkedPersons.isEmpty()) {
            return "";
        }

        String names = alreadyMarkedPersons.stream()
                .map(person -> person.getName().fullName)
                .collect(Collectors.joining(", "));

        return String.format(MESSAGE_FAILURE_ALREADY_MARKED,
                exerciseIndex.getZeroBased(), action, names);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof MarkExerciseCommand)) {
            return false;
        }

        MarkExerciseCommand otherCommand = (MarkExerciseCommand) other;
        return studentIndex.equals(otherCommand.studentIndex)
                && exerciseIndex.equals(otherCommand.exerciseIndex)
                && isDone == otherCommand.isDone;
    }
}
