package seedu.address.model.person;

import java.time.LocalDateTime;

/**
 * Class representing milestone
 */
public class Exercise implements Milestone {
    private int number;
    private LocalDateTime dueDate;
    /**
     * Constructor for milestone
     *
     * @param dueDate date and time on which exercise is due
     */
    public Exercise(LocalDateTime dueDate, int number) {
        this.dueDate = dueDate;
        this.number = number;
    }
    @Override
    public String toString() {
        return String.format("Exercise %d", number);
    }
}
