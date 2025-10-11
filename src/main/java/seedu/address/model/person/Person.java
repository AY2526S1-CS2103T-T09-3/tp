package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final StudentId studentId;
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Address address;
    private final Set<Tag> tags = new HashSet<>();
    private final GithubUsername githubUsername;

    private ExerciseTracker exerciseTracker;

    /**
     * Every field must be present and not null.
     */
    public Person(StudentId studentId, Name name, Phone phone, Email email,
                  Address address, Set<Tag> tags, GithubUsername githubUsername) {
        requireAllNonNull(studentId, name, phone, email, address, tags, githubUsername);
        this.studentId = studentId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);
        this.exerciseTracker = new ExerciseTracker();
        this.githubUsername = githubUsername;
    }

    /**
     * Initialises a new person object, but with a specific list of exercise statuses
     */
    public Person(StudentId studentId, Name name, Phone phone, Email email, Address address, Set<Tag> tags,
                  GithubUsername githubUsername, ArrayList<Status> statuses) {
        requireAllNonNull(name, phone, email, address, tags);
        this.studentId = studentId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);
        this.exerciseTracker = new ExerciseTracker(statuses);
        this.githubUsername = githubUsername;
    }

    public StudentId getStudentId() {
        return studentId;
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    public GithubUsername getGithubUsername() {
        return githubUsername;
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getStudentId().equals(getStudentId());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;
        return studentId.equals(otherPerson.studentId)
                && name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && address.equals(otherPerson.address)
                && exerciseTracker.equals(otherPerson.exerciseTracker)
                && tags.equals(otherPerson.tags)
                && githubUsername.equals(otherPerson.githubUsername);
    }


    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(studentId, name, phone, email, address, tags, githubUsername, exerciseTracker);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getClass().getCanonicalName())
                .append("{studentId=").append(studentId)
                .append(", name=").append(name)
                .append(", phone=").append(phone)
                .append(", email=").append(email)
                .append(", address=").append(address)
                .append(", tags=").append(tags)
                .append(", github username=").append(githubUsername)
                .append(", exerciseStatuses=").append(exerciseTracker)
                .append("}");
        return builder.toString();
    }

    public ExerciseTracker getExerciseTracker() {
        return exerciseTracker;
    }
}
