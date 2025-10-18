package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's student ID in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidStudentId(String)}
 */
public class StudentId implements Comparable<StudentId> {

    public static final String MESSAGE_CONSTRAINTS =
            "Student IDs should be in the format: 'A' followed by 7 digits and one capital letter "
                    + "(e.g. A1234567X). The input is case-insensitive but will be stored in uppercase.";

    // Regex:
    // A or a (case-insensitive), followed by exactly 7 digits, followed by one alphabet letter.
    public static final String VALIDATION_REGEX = "(?i)^A\\d{7}[A-Z]$";

    public final String value;

    /**
     * Constructs a {@code StudentId}.
     *
     * @param studentId A valid student ID.
     */
    public StudentId(String studentId) {
        requireNonNull(studentId);
        checkArgument(isValidStudentId(studentId), MESSAGE_CONSTRAINTS);
        // Normalize to uppercase before storing
        value = studentId.toUpperCase();
    }

    /**
     * Returns true if a given string is a valid student ID.
     */
    public static boolean isValidStudentId(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof StudentId
                && value.equals(((StudentId) other).value));
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public int compareTo(StudentId otherStudentId) {
        return this.value.compareTo(otherStudentId.value);
    }
}
