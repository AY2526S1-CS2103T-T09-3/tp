package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Address address;
    private final Set<Tag> tags = new HashSet<>();
    private MilestoneTracker<Exercise> exerciseTracker =
            new MilestoneTracker<>(MilestoneLists.getExerciseList());

    private MilestoneTracker<Lab> labTracker =
            new MilestoneTracker<>(MilestoneLists.getLabList());
    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        requireAllNonNull(name, phone, email, address, tags);
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);
    }

    /**
        alternate constructor
     */
    public Person(Name modelName,
                               Phone modelPhone,
                               Email modelEmail,
                               Address modelAddress,
                               Set<Tag> modelTags,
                               MilestoneTracker<Lab> modelLabTracker,
                               MilestoneTracker<Exercise> modelExerciseTracker) {
        requireAllNonNull(modelName,
                 modelPhone,
                 modelEmail,
                 modelAddress,
                 modelTags,
                 modelLabTracker,
                 modelExerciseTracker);
        this.name = modelName;
        this.phone = modelPhone;
        this.email = modelEmail;
        this.address = modelAddress;
        this.tags.addAll(modelTags);
        this.exerciseTracker = modelExerciseTracker;
        this.labTracker = modelLabTracker;
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

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName());
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
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && address.equals(otherPerson.address)
                && tags.equals(otherPerson.tags);
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, email, address, tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("address", address)
                .add("tags", tags)
                .toString();
    }
    public void setLabAttendance(boolean isAttended, int index) {
        if (isAttended) {
            labTracker.mark(index);
        } else {
            labTracker.unmark(index);
        }
    }
    public void setExerciseCompletion(boolean isDone, int index) {
        if (isDone) {
            exerciseTracker.mark(index);
        } else {
            exerciseTracker.unmark(index);
        }
    }

    public MilestoneTracker<Exercise> getExerciseTracker() {
        return exerciseTracker;
    }
    public MilestoneTracker<Lab> getLabTracker() {
        return labTracker;
    }
}
