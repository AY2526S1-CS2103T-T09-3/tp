package seedu.address.model.person;

/**
 * Represents a student's attendance status for a single lab session.
 */
public class LabAttendance implements Lab {
    private boolean isAttended;

    /**
     * Constructs an {@code LabAttendance} with attendance initially set to false.
     */
    public LabAttendance() {
        this.isAttended = false;
    }

    @Override
    public void markAsAttended() throws IllegalStateException {
        if (isAttended) {
            throw new IllegalStateException("Lab Attendance has already been marked");
        }
        isAttended = true;
    }

    @Override
    public boolean isAttended() {
        return isAttended;
    }

}
