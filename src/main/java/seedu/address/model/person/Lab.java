package seedu.address.model.person;

/**
 * Represents lab session
 */
public class Lab implements Milestone {
    private int week;
    private TimeSlot timeSlot;
    private int labNumber;
    /**
     * Constructor for lab
     *
     * @param timeSlot during which lab occurs
     */
    public Lab(TimeSlot timeSlot, int labNumber) {
        this.timeSlot = timeSlot;
        this.labNumber = labNumber;
    }
    @Override
    public String toString() {
        return String.format("Lab %d", labNumber);
    }
}
