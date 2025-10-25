package seedu.address.logic.parser;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STATUS;
import static seedu.address.logic.parser.MarkExerciseCommandParser.INVALID_STATUS_FORMAT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javafx.util.Pair;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.core.index.MultiIndex;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Email;
import seedu.address.model.person.Examination;
import seedu.address.model.person.ExerciseTracker;
import seedu.address.model.person.GithubUsername;
import seedu.address.model.person.GradeMap;
import seedu.address.model.person.Lab;
import seedu.address.model.person.LabAttendance;
import seedu.address.model.person.LabAttendanceList;
import seedu.address.model.person.LabList;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Status;
import seedu.address.model.person.StudentId;
import seedu.address.model.person.exceptions.InvalidScoreException;
import seedu.address.model.person.sortcriterion.ExerciseSortCriterion;
import seedu.address.model.person.sortcriterion.LabSortCriterion;
import seedu.address.model.person.sortcriterion.NameSortCriterion;
import seedu.address.model.person.sortcriterion.SortCriterion;
import seedu.address.model.person.sortcriterion.StudentIdSortCriterion;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";
    /**
     * @param input a string that is either in the "X:Y" or "X" form
     * @return a MultiIndex instance
     * @throws ParseException if the input is invalid
     */
    public static MultiIndex parseMultiIndex(String input) throws ParseException {
        if (input.contains(":")) {
            return parseRange(input);
        } else {
            return new MultiIndex(parseIndex(input));
        }
    }
    /**
     * Parses a range input like "2:5" into a MultiIndex.
     * */
    private static MultiIndex parseRange(String input) throws ParseException {
        String[] parts = input.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid range format: " + input);
        }

        Index lower = ParserUtil.parseIndex(parts[0].trim());
        Index upper = ParserUtil.parseIndex(parts[1].trim());

        if (lower.getZeroBased() > upper.getZeroBased()) {
            throw new IllegalArgumentException("Lower bound cannot be greater than upper bound: " + input);
        }

        return new MultiIndex(lower, upper);
    }
    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }
    /**
     * Parses {@code zeroBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseZeroBasedIndex(String zeroBasedIndex) throws ParseException {
        String trimmedIndex = zeroBasedIndex.trim();
        if (!StringUtil.isNonNegativeUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromZeroBased(Integer.parseInt(trimmedIndex));
    }
    /**
     * Parses a {@code String studentId} into a {@code StudentId}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code studentId} is invalid.
     */
    public static StudentId parseStudentId(String studentId) throws ParseException {
        requireNonNull(studentId);
        String trimmedStudentId = studentId.trim();
        if (!StudentId.isValidStudentId(trimmedStudentId)) {
            throw new ParseException(StudentId.MESSAGE_CONSTRAINTS);
        }
        return new StudentId(trimmedStudentId);
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }

    /**
     * Parses a {@code String githubUsername} into an {@code GithubUsername}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code githubUsername} is invalid.
     */
    public static GithubUsername parseGithubUsername(String githubUsername) throws ParseException {
        requireNonNull(githubUsername);
        String trimmedGithubUsername = githubUsername.trim();
        if (!GithubUsername.isValidGithubUsername(trimmedGithubUsername)) {
            throw new ParseException(GithubUsername.MESSAGE_CONSTRAINTS);
        }
        return new GithubUsername(trimmedGithubUsername);
    }

    /**
     * Converts first letter of status to status object
     * @param code first letter of status
     * @return status represented by code
     */
    public static Status parseStatus(String code) throws ParseException {
        switch (code.toUpperCase()) {
        case "D": return Status.DONE;
        case "N": return Status.NOT_DONE;
        case "O": return Status.OVERDUE;
        default:
            throw new ParseException(INVALID_STATUS_FORMAT + Arrays.toString(Status.values()));
        }
    }

    /**
     * Parses a {@code String labStatus} into a boolean value.
     *
     * @param labStatus The status string to parse.
     * @return true if the status is "y", false if the status is "n".
     * @throws ParseException if the given {@code labStatus} is invalid.
     */
    public static boolean parseLabStatus(String labStatus) throws ParseException {
        requireNonNull(labStatus);
        String trimmed = labStatus.trim();
        switch (trimmed.toLowerCase()) {
        case "y": return true;
        case "n": return false;
        default:
            throw new ParseException(Lab.MESSAGE_CONSTRAINTS);
        }
    }

    /**
     * Parses a {@code String labAttendanceListString} into an {@code LabAttendanceList}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code labAttendanceListString} is invalid.
     */
    public static LabAttendanceList parseLabAttendanceList(String labAttendanceList, int currentWeek) throws ParseException {
        requireNonNull(labAttendanceList);
        String trimmed = labAttendanceList.trim();
        if (!LabList.isValidLabList(trimmed)) {
            throw new ParseException(LabList.MESSAGE_CONSTRAINTS);
        }

        LabAttendance[] labs = new LabAttendance[LabList.NUMBER_OF_LABS];
        String[] parts = trimmed.split("\\s+");

        for (int i = 0; i < labs.length; i++) {
            String status = parts[i * 2 + 1];

            labs[i] = new Lab(i + 1, currentWeek);
            if (status.equals("Y")) {
                labs[i].markAsAttended();
            }
        }
        return new LabList(labs);
    }
    /**
     * Parses a {@code String exerciseTrackerString} into an {@code ExerciseTracker}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code exerciseTrackerString} is invalid.
     */
    public static ExerciseTracker parseExerciseTracker(String exerciseTrackerString) throws ParseException {
        requireNonNull(exerciseTrackerString);
        String trimmed = exerciseTrackerString.trim();

        if (!ExerciseTracker.isValidExerciseTracker(trimmed)) {
            throw new ParseException(ExerciseTracker.MESSAGE_CONSTRAINTS);
        }

        Status[] statuses = new Status[ExerciseTracker.NUMBER_OF_EXERCISES];
        String[] parts = trimmed.split("\\s+");

        // Now we have 3 tokens per exercise: "ex", "0:", "N"
        for (int i = 0; i < ExerciseTracker.NUMBER_OF_EXERCISES; i++) {
            String statusString = parts[i * 3 + 2];
            statuses[i] = parseStatus(statusString);
        }

        return new ExerciseTracker(new ArrayList<>(Arrays.asList(statuses)));
    }

    /**
     * parses a {@code gradeMapString} into a {@code gradeMap}
     * @throws ParseException if the given {@code exerciseTrackerString} is invalid
     */
    public static GradeMap parseGradeMap(String input) throws ParseException {
        GradeMap gradeMap = new GradeMap();

        for (String entry : input.split(",")) {
            String[] parts = entry.trim().split(":");
            if (parts.length != 2) {
                continue;
            }
            String name = parts[0].trim().toLowerCase();
            String scoreStr = parts[1].trim();
            Examination exam = new Examination(name);
            if (!scoreStr.equalsIgnoreCase("NA")) {
                try {
                    exam.setPercentageScore(Double.parseDouble(scoreStr));
                } catch (InvalidScoreException e) {
                    throw new ParseException(e.getMessage());
                }
            }
            gradeMap.putExam(name, exam);
        }

        return gradeMap;
    }

    /**
     * Parses a {@code String criterionString} into a {@code SortCriterion}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code criterionString} is not a valid criterion.
     */
    public static SortCriterion parseSortCriterion(String criterionString) throws ParseException {
        requireNonNull(criterionString);
        String trimmedCriterion = criterionString.trim().toLowerCase();

        switch (trimmedCriterion) {
        case NameSortCriterion.CRITERION_KEYWORD:
            return new NameSortCriterion();

        case StudentIdSortCriterion.CRITERION_KEYWORD:
            return new StudentIdSortCriterion();

        case LabSortCriterion.CRITERION_KEYWORD:
            return new LabSortCriterion();

        case ExerciseSortCriterion.CRITERION_KEYWORD:
            return new ExerciseSortCriterion();

        default:
            throw new ParseException(SortCriterion.MESSAGE_CONSTRAINTS);
        }
    }

    /**
     * Parses a {@code String} exerciseIndexString into a {@code Pair} of exercise index
     * and exercise status.
     *
     * @param exerciseIndexString a string containing both the index and status as a combined string
     * @throws ParseException if the given {@code String} does not include a status.
     */
    public static Pair<String, Status> parseExerciseIndexStatus(String exerciseIndexString) throws ParseException {
        ArgumentMultimap exerciseMultimap =
                ArgumentTokenizer.tokenize(exerciseIndexString, PREFIX_STATUS);
        Optional<String> status = exerciseMultimap.getValue(PREFIX_STATUS);
        String exercise = exerciseMultimap.getPreamble();
        if (exercise.isEmpty()) {
            throw new ParseException("Need exercise");
        }
        if (status.isEmpty()) {
            throw new ParseException("Need status");
        }
        String statusString = status.get().toUpperCase();
        switch (statusString) {
        case "D":
            return new Pair<>(exercise, Status.DONE);
        case "N":
            return new Pair<>(exercise, Status.NOT_DONE);
        case "O":
            return new Pair<>(exercise, Status.OVERDUE);
        default:
            throw new ParseException("Need D N O");
        }
    }

    /**
     * Parses a {@code String} labNumberString into a {@code Pair} of
     * lab index and lab attendance status.
     *
     * @param labNumberString a string containing both the index and status as a combined string
     * @throws ParseException if the given {@code String} does not include a status.
     */
    public static Pair<String, Boolean> parseLabNumberStatus(String labNumberString) throws ParseException {
        ArgumentMultimap exerciseMultimap =
                ArgumentTokenizer.tokenize(labNumberString, PREFIX_STATUS);
        Optional<String> status = exerciseMultimap.getValue(PREFIX_STATUS);
        String labNumber = exerciseMultimap.getPreamble();
        if (labNumber.isEmpty()) {
            throw new ParseException("Need lab number");
        }
        if (status.isEmpty()) {
            throw new ParseException("Need status");
        }
        String statusString = status.get().toUpperCase();
        switch (statusString) {
        case "Y":
            return new Pair<>(labNumber, TRUE);
        case "N":
            return new Pair<>(labNumber, FALSE);
        default:
            throw new ParseException("Need Y N");
        }
    }
}
