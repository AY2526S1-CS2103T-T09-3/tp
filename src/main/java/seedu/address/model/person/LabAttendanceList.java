package seedu.address.model.person;

/**
 * Represents a collection of lab attendance records for a student across all lab sessions.
 */
public class LabAttendanceList implements LabList {
    // Might need to shift to person class or other classes
    public static final int NUMBER_OF_LABS = 10;
    private final Lab[] labs;

    /**
     * Constructs a {@code LabAttendanceList} with the specified lab attendance records.
     *
     * @param labs an array of {@code Lab} objects representing the lab attendance records
     */
    public LabAttendanceList(Lab[] labs) {
        assert(labs.length == NUMBER_OF_LABS);
        this.labs = labs;
    }

    /**
     * Marks the specified lab session as attended.
     * @param index the zero-based index of the lab session.
     */
    @Override
    public void markLabAsAttended(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= NUMBER_OF_LABS) {
            throw new IndexOutOfBoundsException("Index should be between 0 and " + (NUMBER_OF_LABS - 1));
        }
        labs[index].markAsAttended();
    }
}
