package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Exercise;
import seedu.address.model.person.Lab;
import seedu.address.model.person.MilestoneLists;
import seedu.address.model.person.MilestoneTracker;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Status;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final String name;
    private final String phone;
    private final String email;
    private final String address;

    private final List<JsonAdaptedTag> tags = new ArrayList<>();
    private final List<String> labTracker = MilestoneLists.getLabList()
                    .getMilestones().stream().map(x -> "NOT_DONE").toList();
    private final List<String> exerciseTracker = MilestoneLists.getExerciseList()
            .getMilestones().stream().map(x -> "NOT_DONE").toList();

    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name,
                             @JsonProperty("phone") String phone,
                             @JsonProperty("email") String email,
                             @JsonProperty("address") String address,
                             @JsonProperty("tags") List<JsonAdaptedTag> tags,
                             @JsonProperty("labTracker") List<String> labTracker,
                             @JsonProperty("exerciseTracker") List<String> exerciseTracker) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        if (tags != null) {
            this.tags.addAll(tags);
        }
        if (labTracker != null) {
            this.labTracker.addAll(labTracker);
        }
        if (exerciseTracker != null) {
            this.exerciseTracker.addAll(exerciseTracker);
        }
    }
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("name") String name,
                             @JsonProperty("phone") String phone,
                             @JsonProperty("email") String email,
                             @JsonProperty("address") String address,
                             @JsonProperty("tags") List<JsonAdaptedTag> tags) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        if (tags != null) {
            this.tags.addAll(tags);
        }
    }
    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;

        // Tags
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));

        // Lab tracker
        labTracker.addAll(source.getLabTracker().getStatuses().stream()
                .map(Enum::name)
                .collect(Collectors.toList()));

        // Exercise tracker
        exerciseTracker.addAll(source.getExerciseTracker().getStatuses().stream()
                .map(Enum::name)
                .collect(Collectors.toList()));
    }


    /**
     * Converts this Jackson-friendly adapted person object into the model's {@code Person} object.
     */
    public Person toModelType() throws IllegalValueException {
        // Convert tags
        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            personTags.add(tag.toModelType());
        }

        // Validate core fields
        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        };
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        final Set<Tag> modelTags = new HashSet<>(personTags);

        // Convert tracker strings → Status enums
        List<Status> labStatuses = new ArrayList<>();
        for (String s : labTracker) {
            labStatuses.add(Status.valueOf(s));
        }

        List<Status> exerciseStatuses = new ArrayList<>();
        for (String s : exerciseTracker) {
            exerciseStatuses.add(Status.valueOf(s));
        }

        MilestoneTracker<Lab> modelLabTracker = labStatuses.isEmpty()
                ? new MilestoneTracker<>(MilestoneLists.getLabList())
                : new MilestoneTracker<>(labStatuses);

        MilestoneTracker<Exercise> modelExerciseTracker = exerciseStatuses.isEmpty()
                ? new MilestoneTracker<>(MilestoneLists.getExerciseList())
                : new MilestoneTracker<>(exerciseStatuses);

        return new Person(modelName, modelPhone, modelEmail, modelAddress,
                modelTags, modelLabTracker, modelExerciseTracker);
    }
}
