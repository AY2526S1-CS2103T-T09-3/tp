package seedu.address.model.person;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the list of lab sessions in a semester.
 */
public class LabList {
    private ArrayList<Lab> labs = new ArrayList<>();
    /**
     * Constructs a LabList with labs scheduled weekly starting from a given start date.
     * @param firstWeek first week with a lab session
     * @param numLabs   number of labs in the semester
     * @param startDate start date and time of the first lab
     */
    public LabList(int firstWeek, int numLabs, LocalDateTime startDate) {
        int week = firstWeek;
        for (int i = 0; i < numLabs; i++) {
            LocalDateTime labStart = week > 6
                ? startDate.plusWeeks(week)
                : startDate.plusWeeks(week - 1);
            LocalDateTime labEnd = labStart.plusHours(2);
            TimeSlot slot = new TimeSlot(labStart, labEnd);
            Lab lab = new Lab(slot, i + 1);
            week++;
            labs.set(i + 1, lab);
        }
    }
    public int size() {
        return labs.size();
    }
    public List<Lab> getLabs() {
        return List.copyOf(labs);
    }
}
