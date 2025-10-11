package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;

public class ExerciseTrackerTest {

    private ExerciseTracker tracker;

    @BeforeEach
    public void setUp() {
        tracker = new ExerciseTracker();
    }

    @Test
    public void constructor_default_initializesAllToNotDone() {
        ArrayList<Status> statuses = tracker.getStatuses();
        assertEquals(10, statuses.size());
        assertTrue(statuses.stream().allMatch(s -> s == Status.NOT_DONE));
    }

    @Test
    public void constructor_customList_initializesCorrectly() {
        ArrayList<Status> custom = new ArrayList<>(Collections.nCopies(5, Status.DONE));
        ExerciseTracker customTracker = new ExerciseTracker(custom);
        assertEquals(custom, customTracker.getStatuses());
    }

    @Test
    public void mark_validIndex_updatesStatus() {
        Index index = Index.fromZeroBased(3);
        tracker.mark(index, Status.DONE);
        assertEquals(Status.DONE, tracker.getStatuses().get(3));
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        assertTrue(tracker.equals(tracker));
    }

    @Test
    public void equals_sameStatuses_returnsTrue() {
        ExerciseTracker other = new ExerciseTracker(new ArrayList<>(tracker.getStatuses()));
        assertTrue(tracker.equals(other));
        assertEquals(tracker.hashCode(), other.hashCode());
    }

    @Test
    public void equals_differentStatuses_returnsFalse() {
        ExerciseTracker other = new ExerciseTracker();
        other.mark(Index.fromZeroBased(1), Status.DONE);
        assertFalse(tracker.equals(other));
    }

    @Test
    public void toString_containsExpectedFormat() {
        String result = tracker.toString();
        assertTrue(result.contains("ex 0: N"));
        assertTrue(result.contains("ex 9: N"));
    }
}
