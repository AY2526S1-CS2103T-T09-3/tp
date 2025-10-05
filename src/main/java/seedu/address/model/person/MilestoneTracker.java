package seedu.address.model.person;

import static seedu.address.model.person.Status.DONE;
import static seedu.address.model.person.Status.NOT_DONE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Generic tracker for milestones (e.g., labs, exercises, etc.).
 * Tracks the completion status for each milestone.
 */
public class MilestoneTracker<T extends Milestone> {

    private final ArrayList<Status> statuses;

    /**
     * Creates a milestone tracker where all milestones start as NOT_DONE.
     *
     * @param milestoneList the milestone list to track (e.g., MilestoneLists.getExerciseList()).
     */
    public MilestoneTracker(MilestoneList<T> milestoneList) {
        Objects.requireNonNull(milestoneList);
        this.statuses = new ArrayList<>(
                Collections.nCopies(milestoneList.size(), NOT_DONE)
        );
    }

    /**
     * Creates a milestone tracker from an existing list of statuses (e.g., loaded from storage).
     *
     * @param statuses the list of statuses to use.
     */
    public MilestoneTracker(List<Status> statuses) {
        Objects.requireNonNull(statuses);
        this.statuses = new ArrayList<>(statuses);
    }

    /** Marks the milestone at the given index as DONE. */
    public void mark(int index) {
        statuses.set(index, DONE);
    }

    /** Marks the milestone at the given index as NOT_DONE. */
    public void unmark(int index) {
        statuses.set(index, NOT_DONE);
    }

    /** Returns an unmodifiable view of the current statuses. */
    public List<Status> getStatuses() {
        return Collections.unmodifiableList(statuses);
    }

    @Override
    public String toString() {
        return "Milestone statuses: " + statuses;
    }
}
