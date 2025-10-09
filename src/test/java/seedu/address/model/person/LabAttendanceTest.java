package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class LabAttendanceTest {

    @Test
    public void constructor_default_hasAttendedFalse() {
        LabAttendance labAttendance = new LabAttendance();
        assertFalse(labAttendance.isAttended());
    }

    @Test
    public void markAsAttended_unmarkedLab_hasAttendedTrue() {
        LabAttendance labAttendance = new LabAttendance();

        labAttendance.markAsAttended();
        assertTrue(labAttendance.isAttended());
    }

    @Test
    public void markAsAttended_markedLab_throwIllegalStateException() {
        LabAttendance labAttendance = new LabAttendance();

        labAttendance.markAsAttended();
        assertTrue(labAttendance.isAttended());

        assertThrows(IllegalStateException.class, labAttendance::markAsAttended);
    }
}
