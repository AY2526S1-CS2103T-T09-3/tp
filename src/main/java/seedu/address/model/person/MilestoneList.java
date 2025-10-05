package seedu.address.model.person;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Generic list of weekly milestones in a semester (e.g., Labs, Exercises).
 * Each milestone occurs weekly, with an optional skip after week 6 (recess week).
 *
 * @param <T> the type of milestone (e.g., Exercise, Lab)
 */
public class MilestoneList<T extends Milestone> {
    private final ArrayList<T> milestones = new ArrayList<>();

    /**
     * Constructs a MilestoneList with weekly milestones.
     *
     * @param firstWeek   the week number of the first milestone (e.g., 2)
     * @param numItems    total number of milestones
     * @param firstDate   date and time of the first milestone
     * @param factory     a factory interface to create milestone instances
     */
    public MilestoneList(int firstWeek, int numItems, LocalDateTime firstDate, MilestoneFactory<T> factory) {
        int week = firstWeek;
        for (int i = 0; i < numItems; i++) {
            LocalDateTime date = week > 6
                    ? firstDate.plusWeeks(i + 1) // skip recess week
                    : firstDate.plusWeeks(i);

            T milestone = factory.create(date, i + 1);
            milestones.add(milestone);
            week++;
        }
    }

    /** Returns the number of milestones. */
    public int size() {
        return milestones.size();
    }

    /** Returns an unmodifiable copy of the milestone list. */
    public List<T> getMilestones() {
        return List.copyOf(milestones);
    }

    @Override
    public String toString() {
        return milestones.toString();
    }

    /**
     * Functional interface for milestone creation.
     * Allows different milestone types (Lab, Exercise, etc.) to be instantiated.
     */
    @FunctionalInterface
    public interface MilestoneFactory<T extends Milestone> {
        T create(LocalDateTime date, int index);
    }
}
