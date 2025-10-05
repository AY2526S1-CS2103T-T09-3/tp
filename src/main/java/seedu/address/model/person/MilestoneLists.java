package seedu.address.model.person;

import java.time.LocalDateTime;

/**
 * Holds static milestone lists for labs and exercises across the semester.
 */
public class MilestoneLists {
    private static final MilestoneList<Exercise> exerciseList;
    private static final MilestoneList<Lab> labList;
    private MilestoneLists() {
    }
    static {
        exerciseList = new MilestoneList<>(
                1, // first week
                10, // number of exercises
                LocalDateTime.parse("2025-01-14T10:00"),
                Exercise::new // (date, index) -> new Exercise(date, index)
        );

        labList = new MilestoneList<>(
                1, // first week
                10, // number of labs
                LocalDateTime.parse("2025-01-14T10:00"),
                (date, index) -> new Lab(
                        new TimeSlot(date, date.plusHours(2)), index)
        );
    }

    public static MilestoneList<Lab> getLabList() {
        return labList;
    }

    public static MilestoneList<Exercise> getExerciseList() {
        return exerciseList;
    }
}
