package seedu.address.model.person;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * List of weekly exercises in a semester.
 */
public class ExerciseList {
    private ArrayList<Exercise> exercises = new ArrayList<>();
    /**
     * Constructs an ExerciseList based on an array of exercise numbers,
     * starting from the given firstDate and spaced one week apart.
     *
     * @param numExercises number of exercises
     * @param firstWeek number of week when first exercise is issued
     * @param firstDate the date and time of the first exercise
     */
    public ExerciseList(int firstWeek, int numExercises, LocalDateTime firstDate) {
        int weekNumber = firstWeek;
        for (int i = 0; i < numExercises; i++) {
            LocalDateTime date = weekNumber > 6
                    ? firstDate.plusWeeks(i + 1)
                    : firstDate.plusWeeks(i);
            Exercise exercise = new Exercise(date, i);
            weekNumber++;
            exercises.set(i + 1, exercise);
        }
    }
    public int size() {
        return exercises.size();
    }
    public List<Exercise> getExercises() {
        return List.copyOf(exercises);
    }
}
