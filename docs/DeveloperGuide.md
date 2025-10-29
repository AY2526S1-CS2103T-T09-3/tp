---
id: dev-guide
title: "Developer Guide"
pageNav: 3
---

# LambdaLab Developer Guide

<!-- * Table of Contents -->
<page-nav-print />

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

_{ list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well }_

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

### Architecture

<puml src="diagrams/ArchitectureDiagram.puml" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** (consisting of classes [`Main`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/Main.java) and [`MainApp`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/MainApp.java)) is in charge of the app launch and shut down.
* At app launch, it initializes the other components in the correct sequence, and connects them up with each other.
* At shut down, it shuts down the other components and invokes cleanup methods where necessary.

The bulk of the app's work is done by the following four components:

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<puml src="diagrams/ArchitectureSequenceDiagram.puml" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<puml src="diagrams/ComponentManagers.puml" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/Ui.java)

<puml src="diagrams/UiClassDiagram.puml" alt="Structure of the UI Component"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `PersonListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Person` object residing in the `Model`.

### Logic component

**API** : [`Logic.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<puml src="diagrams/LogicClassDiagram.puml" width="550"/>

The sequence diagram below illustrates the interactions within the `Logic` component, taking `execute("delete 1")` API call as an example.

<puml src="diagrams/DeleteSequenceDiagram.puml" alt="Interactions Inside the Logic Component for the `delete 1` Command" />

<box type="info" seamless>

**Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline continues till the end of diagram.
</box>

How the `Logic` component works:

1. When `Logic` is called upon to execute a command, it is passed to an `AddressBookParser` object which in turn creates a parser that matches the command (e.g., `DeleteCommandParser`) and uses it to parse the command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `DeleteCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to delete a person).<br>
   Note that although this is shown as a single step in the diagram above (for simplicity), in the code it can take several interactions (between the command object and the `Model`) to achieve.
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<puml src="diagrams/ParserClasses.puml" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `AddressBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### Model component
**API** : [`Model.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/model/Model.java)

<puml src="diagrams/ModelClassDiagram.puml" width="450" />


The `Model` component,

* stores the address book data i.e., all `Person` objects (which are contained in a `UniquePersonList` object).
* stores the currently 'selected' `Person` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Person>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, they should make sense on their own without depending on other components)

<box type="info" seamless>

**Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `AddressBook`, which `Person` references. This allows `AddressBook` to only require one `Tag` object per unique tag, instead of each `Person` needing their own `Tag` objects.<br>

<puml src="diagrams/BetterModelClassDiagram.puml" width="450" />

</box>


### Storage component

**API** : [`Storage.java`](https://github.com/se-edu/addressbook-level3/tree/master/src/main/java/seedu/address/storage/Storage.java)

<puml src="diagrams/StorageClassDiagram.puml" width="550" />

The `Storage` component,
* can save address book data, user preferences, and timeslot data in JSON format and read them back into model objects.
* inherits from `AddressBookStorage`, `UserPrefsStorage`, and `TimeslotsStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the Model component (because the Storage component's job is to save/retrieve objects that belong to the Model)
* delegates JSON conversion to classes such as `JsonSerializableAddressBook` / `JsonAdaptedPerson` for the address book and `JsonSerializableTimeslots` / `JsonAdaptedTimeslot` for timeslots.
* provides concrete implementations like `JsonAddressBookStorage`, `JsonUserPrefsStorage`, and `JsonTimeslotsStorage` which read/write files on disk.

The `StorageManager` class,
* composes the individual storage providers (address book, prefs, timeslots)
* exposes unified operations so `MainApp`, `Logic`, and other components access persistence through a single entry point

The timeslots file location is configurable via `UserPrefs` and the application will create or populate the file with sample timeslots when none is present.

### Common classes

Classes used by multiple components are in the `seedu.address.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.
### Undo feature

#### Implementation

The undo mechanism is facilitated by `ModelManager`. It stores a single previous state of the address book and
timeslots, stored internally as `previousAddressBookState` and `previousTimeslotsState`. Additionally, it implements
the following operations:

* `Model#saveAddressBook()` — Saves the current address book and timeslots state before modification.
* `Model#undoAddressBook()` — Restores the previous address book and timeslots state.
* `Model#canUndoAddressBook()` — Checks if there is a previous state available to undo to.

These operations are exposed in the `Model` interface as `Model#saveAddressBook()`, `Model#undoAddressBook()` and
`Model#canUndoAddressBook()` respectively.

Given below is an example usage scenario and how the undo mechanism behaves at each step.

**Step 1.** The user launches the application for the first time. The `ModelManager` will be initialized with the
initial address book and timeslots state, with `previousAddressBookState` and `previousTimeslotsState` set to `null`
(no previous state to undo to).

<puml src="diagrams/UndoCommand/UndoState0.puml" with="574" />

**Step 2.** The user executes `delete 5` command to delete the 5th person in the address book. The `delete` command
calls `Model#saveAddressBook()` before deleting, saving the current state. After the deletion, the current state is
modified but the previous state preserves the state before deletion.

<puml src="diagrams/UndoCommand/UndoState1.puml" with="574" />

<box type="info" seamless>

**Note:** If a command fails its execution, it will not call `Model#saveAddressBook()`, so the previous state will
not be updated.

</box>

**Step 3.** The user executes `add n/John Doe …​` to add a new student. The `add` command also calls
`Model#saveAddressBook()` before adding, which **replaces** the previous state with the current state (ab1),
then adds the new student.

<puml src="diagrams/UndoCommand/UndoState2.puml" with="574" />

**Step 4.** The user now decides that adding the student was a mistake, and decides to undo that action by
executing the `undo` command. The `undo` command will call `Model#undoAddressBook()`, which restores the address book
to the previous state (ab1) and sets both `previousAddressBookState` and `previousTimeslotsState` to `null`.

<puml src="diagrams/UndoCommand/UndoState3.puml" with="574" />

<box type="info" seamless>

**Note:** If `previousAddressBookState` is `null`, then there is no previous state to restore. The `undo` command
uses `Model#canUndoAddressBook()` to check if this is the case. If so, it will return an error to the user rather than
attempting to perform the undo.

</box>

<box type="warning" seamless>

**Important:** This implementation only supports undoing **one command** at a time. After undoing once, you must
execute another modifying command before you can undo again. There is no redo functionality.

</box>

The following sequence diagram shows how an undo operation goes through the `Logic` component:

<puml src="diagrams/UndoCommand/UndoSequenceDiagram-Logic" with="574" />

<box type="info" seamless>

**Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML,
the lifeline reaches the end of diagram.

</box>

Similarly, how an undo operation goes through the `Model` component is shown below:

<puml src="diagrams/UndoCommand/UndoSequenceDiagram-Model" with="574" />

**Step 5.** The user then decides to execute the command `list`. Commands that do not modify the address book,
such as `list`, `find`, or `get-timeslots`, will not call `Model#saveAddressBook()`. Thus, the previous state remains `null`.

<puml src="diagrams/UndoCommand/UndoState4.puml" with="574" />

**Step 6.** The user executes `clear`, which calls `Model#saveAddressBook()` before clearing. The current state (ab1)
is saved as the previous state, then all persons are deleted, creating a new current state.

<puml src="diagrams/UndoCommand/UndoState5.puml" with="574" />

The following activity diagram summarises what happens when a user executes a new command:

<puml src="diagrams/UndoCommand/SaveActivityDiagram.puml" with="574" />

#### Commands that support undo

The following commands call `Model#saveAddressBook()` and thus support undo:
- `add` - Adds a student
- `delete` - Deletes a student
- `edit` - Edits a student
- `clear` - Clears all students
- `marke` - Marks an exercise
- `marka` - Marks attendance
- `grade` - Marks grade of an assessment
- `block-timeslot` - Adds a timeslot
- `unblock-timeslot` - Unblock a timeslot
- `clear-timeslots` - Clears all timeslots
- `add-consultation` - Adds a consultation
- `set-week` - Sets current week

The following commands do NOT support undo (read-only commands):
- `list` - Lists all students
- `find` - Finds students
- `filter` - Filters students
- `sort` - Sorts students base on some criteria
- `get-timeslots` - Displays timeslots
- `get-consultations` - Gets consultation schedule
- `help` - Shows help
- `exit` - Exits the application

#### Design considerations

**Aspect: How undo executes:**

* **Alternative 1 (current choice):** Saves only one previous state (address book + timeslots).
    * Pros:
        * Simple to implement and understand.
        * Minimal memory usage (only doubles the data size at most).
        * No complex state management or pointer tracking.
    * Cons:
        * Cannot undo multiple commands in sequence.
        * Cannot undo a command once a new data-modifying command is executed.
        * No redo functionality.

* **Alternative 2:** Save entire history in a list with pointer.
    * Pros:
        * Can undo multiple commands in sequence.
        * Supports redo functionality.
    * Cons:
        * More complex to implement and maintain.
        * Higher memory usage (stores multiple states in a list).
        * Need to carefully manage state pointer and list boundaries.

* **Alternative 3:** Individual command knows how to undo itself (Command Pattern with undo).
    * Pros:
        * Memory efficient (only store minimal data needed to reverse each command).
        * Can undo multiple commands.
        * Each command encapsulates its own undo logic.
    * Cons:
        * Much more complex - every command must implement its own undo logic.
        * Must ensure correctness of each command's undo implementation.
        * Harder to maintain and test.
        * Increased development time for new commands.

**Rationale for Alternative 1:** For the scope of this project, a simple single-level undo is sufficient for most use
cases. Users typically need to undo only their most recent action, and the simplicity of implementation outweighs the
benefits of a full undo/redo stack. The minimal memory overhead and straightforward logic make this approach ideal for
a student project with limited development time.

#### Future enhancements

* **Multiple undo levels:** Implement a full history stack (Alternative 2) to support undoing multiple commands in
  sequence. This would involve storing a list of states and maintaining a current state pointer.

* **Redo functionality:** Allow users to redo commands that were undone. This would require preserving the "future"
  states after an undo operation until a new modifying command is executed.

* **Selective undo:** Allow undoing specific commands in history rather than just the most recent one. This would
  require implementing Alternative 3 with command-specific undo logic.

* **Undo command confirmation:** For destructive commands like `clear`, prompt the user to confirm before executing,
  reducing the need for undo in the first place.


### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_

### Find Feature:

<puml src="diagrams/findCommand/find.puml" width="250" />


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:

* Undergraduate Teaching Assistants of CS2030S
* need to manage a significant number of students
* prefer desktop apps for reliability during labs/consultations
* can type fast and are comfortable with keyboard shortcuts (Vim)
* prefer typing over mouse interactions
* comfortable using CLI apps

**Value proposition**: manage students, submissions, attendance, and resources faster and more efficiently than traditional spreadsheets or GUI-based systems


### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …​                                   | I want to …​                                                                              | So that I can…​                                                                                        |
|----------|-------------------------------------------|-------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------|
| `* * *`  | CS2030S TA                                | add a GitHub username to the student                                                      | track their exercises easily (auto-link)                                                               |
| `* * *`  | Grader                                    | mark student's exercise as graded after grading it                                        | know which students' exercises are graded / not graded yet                                             |
| `* * *`  | New user                                  | receive help from the app                                                                 | learn how to use it quickly                                                                            |
| `* * *`  | TA                                        | search for students based on their name                                                   | easily find the student im looking for                                                                 |
| `* * *`  | TA                                        | delete student's information                                                              | remove false information                                                                               |
| `* * *`  | TA conducting labs                        | mark students attendance                                                                  | know which students attended the lab and which students didnt                                          |
| `* * *`  | TA with many students                     | add, update students data                                                                 | have their accurate information in LambdaLabs                                                          |
| `* *`    | Grader                                    | tag my student based on their exercise performance                                        | know how much effort I would need to help each student                                                 |
| `* *`    | New user                                  | input student data quickly                                                                | focus on teaching                                                                                      |
| `* *`    | New user                                  | undo my mistakes                                                                          | recover from them quickly                                                                              |
| `* *`    | TA                                        | review statistics regarding performance                                                   | see if the class has room for improvement                                                              |
| `* *`    | TA                                        | search for students based on their student ID                                             | easily find the student im looking for                                                                 |
| `* *`    | TA                                        | I can visualise the students' performance through charts and graph                        | see which part students are doing well/lacking at and put a sufficient amount of effort for that topic |
| `* *`    | TA                                        | sort based on alphabetical order                                                          | easily look for a student and his/her data by his/her name                                             |
| `* *`    | TA                                        | I can sort based on students grades                                                       | see who is underperforming and needs help                                                              |
| `* *`    | TA                                        | sort students based on assignment submitted/ graded/ not submitted                        | better visualise the class's progress on current assignment                                            |
| `* *`    | TA                                        | I can sort based on students attendance rate                                              | see who is missing the most classes                                                                    |
| `* *`    | TA                                        | I can add a tag to signal I need to follow up with a student                              | ensure all students are well taught                                                                    |
| `* *`    | TA                                        | I can filter based on specific assignment submissions                                     | check who did which assignment                                                                         |
| `* *`    | TA accepting consultations                | get my available time slots                                                               | schedule consultations easily by allowing students to choose from all my free time                     |
| `* *`    | TA accepting consultations                | block out timeslots by inputting manually                                                 | use the scheduling feature                                                                             |
| `* *`    | TA marking for attendance                 | filter students based on attendance                                                       | accurately grade my students' attendance                                                               |
| `*`      | Experienced user                          | quickly access my students data                                                           | save time                                                                                              |
| `*`      | Experienced user                          | add aliases to commonly used commands                                                     | easily call frequently used commands                                                                   |
| `*`      | Grader                                    | be notified if any new students have submitted the assignment since I last opened the app | grade their exercises promptly                                                                         |
| `*`      | TA                                        | receive notifications if I have class/consultations the next day                          | not miss any classes/consultations                                                                     |
| `*`      | TA who is making my own slides            | add my slides as a link or filepath                                                       | easily retrieve my slides                                                                              |
| `*`      | TA accepting consultations                | block out timeslots by importing the .ics file from NUSMods                               | use the scheduling feature wiithout much setup                                                         |
| `*`      | TA who is teaching for multiple semesters | archive my student data from previous semesters                                           | focus on the current students                                                                          |
| `*`      | TA who is teaching for multiple semesters | unarchive my past student data                                                            | find something that happened in previous semester if I need that                                       |
| `*`      | TA who is teaching multiple semesters     | I can archive my timetable data                                                           | schedule consultations based on current semester's timetable                                           |


### Use cases

(For all use cases below, the **System** is the `LambdaLab` and the **Actor** is the `user`, unless specified otherwise)

**Use case: Grade an exercise**

**Precondition: A student has submitted their programming exercise**

**MSS**

1.  User receives notification that a student has submitted the exercise
2.  User navigates to student's submission on GitHub via notification
3.  User returns after grading student's submission on GitHub
4.  User marks exercise as graded in LambdaLab
5.  LambdaLab updates statistics
    Use case ends.

**Extensions**

* 1a. User doesn't want to grade student's exercise now
    * 1a1. User dismisses the notification
      Use case ends.
* 1b. User accidentally dismisses notification
    * 1b1. User goes to student's profile
    * 1b2. User navigates to student's submission on GitHub via link in student's profile
      Use case resumes at Step 3


**Use case: Mark student attendance**

**MSS**

1.  User wants to mark attendance, enters student name and lab number using the command format
2.  LambdaLab validates the student name and lab number
3.  LambdaLab marks the student’s attendance for the specified lab
4.  LambdaLab confirms: “Attendance for <studentName> marked for lab number <labNumber>”
    Use case ends.

**Extensions**

* 1a. User provides an empty name or a name with invalid characters
    * 1a1. LambdaLab displays error message: “Invalid name”
    * 1a2. User re-enters a valid name
      Use case resumes at Step 2
* 1b. User provides an invalid lab number (non-numeric, zero, negative, or out-of-range)
    * 1b1. System displays error message: “Invalid lab number”
    * 1b2. User re-enters a valid lab number
      Use case resumes at Step 2
* 3a. Attendance for the student in that lab number has already been marked
    * 3a1. LambdaLab displays: “Attendance already marked for <studentName> in lab number <labNumber>”
      Use case ends.


**Use case: Schedule a consultation**

**Precondition: User has uploaded his schedule**

**Actor:User**

**MSS**
1. User views all the periods of available time he has
2. User inputs a desired consultation time slot
3. Time slot is saved into his schedule
4. A day before the consultation, the user will be reminded of it

**Extensions**
* 2a. User inputs an invalid consultation slot
    * 1a1. User is prompted to enter a valid consultation slot
      Use case resumes at step 2.
* 3a.
    * 3a1. User requests to reschedule or delete the consultation.
    * 3a2. System allows modification or cancellation.
      Use case ends.

*{More to be added}*

### Non-Functional Requirements

1.  Should work on any _mainstream OS_ as long as it has Java `17` or above installed.
2.  Should be able to hold up to 1000 persons without a noticeable sluggishness in performance for typical usage.
3.  A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4.  The system should respond to any command within 1 second.
5.  Core functionalities should be covered by automated tests to ensure that future changes do not break the existing features
6.  Users should be able to run the application simply by executing a JAR file, without needing to run an installer.
7.  Should be able to function fully offline.
8.  Date persistence should not depend on an external database system. Storage should be file-based and embedded.
9.  User data should not be lost due to unexpected situations (e.g., unexpected shutdowns).
10. Should be able to support multiple screen resolutions (e.g., 1280×720 and above) without layout issues.


### Glossary

* **Assignment**: Weekly coding homework that is submitted through GitHub
* **Auto-link**: Automatically add a link to the students GitHub repo
* **Consultation**: Scheduled meeting between a TA and student
* **Exercise**: Weekly coding homework that is submitted through GitHub
* **Mainstream OS**: Windows, Linux, Unix, MacOS
* **Students' performance**: Grades that students receive for their weekly exercises and labs
* **TA**: Teaching Assistant


--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<box type="info" seamless>

**Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</box>

### Launch and shutdown

1. Initial launch

    1. Download the jar file and copy into an empty folder

    2. Open the application using `java -jar LambdaLab.jar`. <br>
       Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

2. Saving window preferences

    1. Resize the window to an optimum size. Move the window to a different location. Close the window.

    2. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

### Adding a student

1. Adding a student with valid data

   1. Test case: `add i/A0309024L n/Shawn Lee p/98765432 e/shawn@gmail.com g/shawnlee2 t/modelStudent` <br>
      Expected: Shawn lee is added to the end of the student list with the specified details.
   
   2. Test case: `add n/Kai Hong i/A0309024L p/99983721 e/kh@gmail.com g/kaihong551 t/consultation t/struggling` <br>
      Expected: Kai Hong is added with multiple tags.

2. Adding a student with missing required fields

   1. Test case:`add n/Shawn Lee p/98765432 e/shawn@gmail.com g/shawnlee2 t/modelStudent` - Student Id missing. <br>
      Expected: Error message indicating invalid command format.
   
   2. Test case:`add i/A0309024L n/Shawn Lee e/shawn@gmail.com g/shawnlee2 t/modelStudent` - Phone number missing. <br>
      Expected: Error message indicating invalid command format.
   
3. Adding a student with invalid data fields

    1. Test case:`add i/A0309021 n/Shawn Lee p/98765432 e/shawn@gmail.com g/shawnlee2 t/modelStudent` <br>
       Expected: Error message indicating invalid `Student Id`.

    2. Test case:`add i/A0309024L n/Shawn Lee p/98765432 e/shawngmail.com g/shawnlee2 t/modelStudent` <br>
       Expected: Error message indicating invalid `Email`.

4. Add a duplicate person

    1. Prerequisite: Student named Alex Yeoh with Student Id: A1231234B is already added.
   
    2. Test case:`add i/A1231234B n/Alex Yeoh p/98765432 e/alexyeoh@example.com g/AlexYeoh` <br>
       Expected: Error message indicating that this student already exists.

### Editing a student

1. Editing a student with valid data

    1. Prerequisites: List all students using the `list` command. Multiple students in the list.

    2. Test case: `edit 2 p/91234567 e/newmail@gmail.com` <br>
       Expected: In the displayed student list, the second student's data is updated to new details.

    3. Test case: `edit 1:3 t/outstanding` <br>
       Expected: In the displayed student list, the first 3 students' tags are replaced with a new tag.

2. Editing a student with invalid fields

    1. Prerequisites: List all persons using the `list` command. Multiple persons in the list.

    2. Test case: `edit 2 g/-shawner` <br>
       Expected: Error message indicating invalid `Github Username`.

    3. Test case: `edit 0 p/98123456` <br>
       Expected: Error message indicating invalid `Student Index`.
   
3. Editing with no fields specified

    1. Test case: `edit 1`<br>
       Expected: Error message indicating invalid command format.


### Deleting a student

1. Deleting student(s)

    1. Prerequisites: List all student using the `list` command. Multiple students in the list.

    2. Test case: `delete 1`<br>
       Expected: First student is deleted from the list. Details of the deleted student shown in the status message.
    3. Test case: `delete 1:3`<br>
       Expected: First three students are deleted from the list. Details of the deleted student shown in the status message. 

2. Deleting with invalid index

    1. Test case: `delete 0`<br>
       Expected: Error message indicating invalid `Student Index`.

    2. Test case: `delete 0:3`<br>
       Expected: Error message indicating invalid `Student Index`.

    3. Test case: `delete x`<br>
       Expected: Error message indicating invalid `Student Index`.
   
3. Deleting with no index specified

    1. Test case: `delete`<br>
       Expected: Error message indicating invalid command format.

### Setting the current week
1. Setting a valid week number

    1. Prerequisites: App is running with default or any current week.

    2. Test case: `set-week 5`<br>
       Expected: Status bar footer updates to display "Week 5". Lab attendance and Exercises will be updated accordingly. (labs 1-2 show red for absent/green for attended and exercises 0-2 show red for overdue/green for done)
   
    3. Test case: `set-week 0`<br>
       Expected: Status bar shows "Week 0". Lab attendance and Exercises updated.
   
2. Setting an invalid week number

    1. Test case: `set-week -1`<br>
       Expected: Error message indicating `Week number`.

    2. Test case: `set-week 14`<br>
       Expected: Error message indicating `Week number`.
   
3. Setting week with invalid format

    1. Test case: `set-week abc`<br>
       Expected: Error message indicating invalid command format.

    2. Test case: `set-week`<br>
       Expected: Error message indicating invalid command format.

    3. Test case: `set-week 5.5`<br>
       Expected: Error message indicating invalid command format.

### Marking lab attendance

1. Marking attendance for student(s))

      1. Prerequisites: List all students. Multiple persons in the list.

      2. Test case: `marka 1 l/1 s/y`<br>
         Expected: Lab 1 marked as attended (green) for student 1. Success message shows student name and lab number.

      3. Test case: `marka 1 l/1 s/n`<br>
         Expected: Lab 1 marked as not attended (grey/red depending on the week number) for student 2.
   
      4. Test case: `marka 3:5 l/2 s/y`<br>
         Expected: Lab 2 marked as attended (green) for students 3, 4, and 5. 
   
2. Invalid lab numbers

    1. Test case: `marka 1 l/0 s/y`<br>
       Expected: Error message indicating invalid `Lab index`.

    2. Test case: `marka 1 l/11 s/y`<br>
       Expected: Error message indicating invalid `Lab index`.

3. Invalid attendance status

    1. Test case: `marka 1 l/1 s/x`<br>
       Expected: Error message indicating invalid `Status`.

    2. Test case: `marka 1 l/1 s/`<br>
       Expected: Error message indicating invalid `Status`.

4. Missing required fields

    1. Test case: `marka 1 l/1`<br>
       Expected: Error message indicating invalid command format.

    2. Test case: `marka 1 s/y`<br>
       Expected: Error message indicating invalid command format.

5. Already marked attendance

    1. Prerequisites: Student 1's lab 1 already marked as attended.

    2. Test case: `marka 1 l/1 s/y`<br>
       Expected: Error message indicating lab already marked as attended.

### Marking exercises
1. Marking an exercise for a single student

    1. Prerequisites: List all students. Multiple persons in the list.

    2. Test case: `marke 1 ei/0 s/y`<br>
       Expected: Exercise 0 marked as done (green) for student 1. Success message shows student name and exercise number.

    3. Test case: `marke 1 ei/0 s/n`<br>
       Expected: Exercise 3 marked as not done for student 2.

    4. Test case: `marke 3:5 ei/1 s/y`<br>
       Expected: Exercise 1 marked as done for students 3, 4, and 5.

2. Invalid exercise numbers

    1. Test case: `marke 1 ei/-1 s/y`<br>
       Expected: Error message indicating invalid `Exercise index`.

    2. Test case: `marke 1 ei/13 s/y`<br>
       Expected: Error message indicating invalid `Exercise index`.

3. Invalid exercise status

    1. Test case: `marke 1 ei/0 s/x`<br>
       Expected: Error message indicating invalid `Status`.
   
4. Missing required fields

    1. Test case: `marke 1 ei/0`<br>
       Expected: Error message indicating invalid command format.

    2. Test case: `marke 1 s/y`<br>
       Expected: Error message indicating invalid command format.

5. Already marked exercise

    1. Prerequisites: Student 1's exercise 0 already marked as done.

    2. Test case: `marke 1 ei/0 s/y`<br>
       Expected: Error message indicating exercise already marked as done.

### Recording grades
1. Grading an exam with valid data

    1. Prerequisites: List all students. At least 3 students in the list.

    2. Test case: `grade 1 en/midterm s/y`<br>
       Expected: Midterm exam marked as passed (green) for student 1.

    3. Test case: `grade 2 en/pe1 s/n`<br>
       Expected: PE1 marked as failed (red) for student 2.

    4. Test case: `grade 1:3 en/final s/y`<br>
       Expected: Final exam marked as passed (green) for students 1, 2, and 3.

2. Invalid exam names

    1. Test case: `grade 1 en/quiz s/y`<br>
       Expected: Error message indicating invalid `Exam name`.

    2. Test case: `grade 1 en/ s/y`<br>
       Expected: Error message indicating invalid `Exam name`.

3. Invalid status

    1. Test case: `grade 1 en/midterm s/x`<br>
       Expected: Error message indicating invalid `Status`.

4. Missing required fields

    1. Test case: `grade 1 en/midterm`<br>
       Expected: Error message indicating invalid `Status`.

    2. Test case: `grade 1 s/y`<br>
       Expected: Error message indicating invalid command format.

### Finding students

1. Finding students with valid keywords

    1. Test case: `find Alex`<br>
       Expected: All students with "Alex" (case-insensitive) in any field (`name, ID, email, etc.`) are displayed.

    2. Test case: `find A123 i/`<br>
       Expected: All students with "A123" (case-insensitive) in `Student Id` field are displayed.

    3. Test case: `find alex irfan`<br>
       Expected: Students with "alex" OR "irfan" in any field are displayed.

    4. Test case: `find irf g/ n/`<br>
       Expected: All students with "irf" in `Github Username` or `Name`field are displayed.

2. Finding with no results

    1. Test case: `find nonexistentname`<br>
       Expected: No students displayed. Message indicates 0 students listed.

3. Invalid find command format

    1. Test case: `find`<br>
       Expected: Error message indicating invalid command format.

### Filtering students

1. Filtering by exercise status

    1. Prerequisites: List all students. Multiple students in the list with various exercise statuses.

    2. Test case: `filter ei/1 s/Y`<br>
       Expected: All students with exercise 1 marked as done are displayed. Message shows number of students listed.

    3. Test case: `filter ei/0 s/N`<br>
       Expected: All students with exercise 0 marked as not done are displayed.

    4. Test case: `filter ei/2 s/O`<br>
       Expected: All students with exercise 2 marked as overdue are displayed.

2. Filtering by lab attendance

    1. Test case: `filter l/1 s/Y`<br>
       Expected: All students who attended lab 1 are displayed.

    2. Test case: `filter l/2 s/N`<br>
       Expected: All students who did not attend lab 2 are displayed.

3. Filtering with multiple criteria

    1. Test case: `filter ei/1 s/Y l/2 s/Y`<br>
       Expected: All students who completed exercise 1 AND attended lab 2 are displayed.

4. Invalid filter commands

    1. Test case: `filter`<br>
       Expected: Error message indicating invalid command format.

    2. Test case: `filter ei/1`<br>
       Expected: Error message indicating exercise index must be followed by status.

    3. Test case: `filter s/Y`<br>
       Expected: Error message indicating invalid command format.

### Sorting students

1. Sorting by valid criteria

    1. Prerequisites: Multiple students in the address book.

    2. Test case: `sort c/name`<br>
       Expected: Students sorted alphabetically by name. Success messages shows the specified sort criterion.

    3. Test case: `sort c/id`<br>
       Expected: Students sorted by student ID. 

    4. Test case: `sort c/lab`<br>
       Expected: Students sorted by lab attendance.

    5. Test case: `sort c/ex`<br>
       Expected: Students sorted by exercise completion.

2. Invalid sort commands

    1. Test case: `sort`<br>
       Expected: Error message indicating invalid command format.

    2. Test case: `sort c/strength`<br>
       Expected: Error message indicating invalid command format.

    3. Test case: `sort name`<br>
       Expected: Error message indicating invalid command format.

### Blocking a timeslot

1. Blocking a valid timeslot

    1. Prerequisites: No existing timeslots or no overlapping timeslots for the time range.

    2. Test case: `block-timeslot ts/1 Jan 2025, 10:00 te/1 Jan 2025, 11:00`<br>
       Expected: Timeslot blocked successfully. Success message shows the blocked timeslot details.

    3. Test case: `block-timeslot ts/2025-10-04T10:00:00 te/2025-10-04T13:00:00`<br>
       Expected: Timeslot blocked using ISO format. Success message displayed.

2. Blocking overlapping timeslots

    1. Prerequisites: A timeslot already exists from 10:00 to 11:00 on 1 Jan 2025.

    2. Test case: `block-timeslot ts/1 Jan 2025, 10:30 te/1 Jan 2025, 11:30`<br>
       Expected: Error message indicating timeslot already exists.

3. Invalid timeslot commands

    1. Test case: `block-timeslot ts/1 Jan 2025, 11:00 te/1 Jan 2025, 10:00`<br>
       Expected: Error message indicating invalid `timeslot range`.

    2. Test case: `block-timeslot ts/1 Jan 2025, 10:00`<br>
       Expected: Error message indicating invalid command format.

    3. Test case: `block-timeslot ts/invalid te/1 Jan 2025, 11:00`<br>
       Expected: Error message indicating invalid `timeslot datetime`.

### Unblocking a timeslot

1. Unblocking an existing timeslot

    1. Prerequisites: A timeslot exists from 10:00 to 13:00 on 4 Oct 2025.

    2. Test case: `unblock-timeslot ts/2025-10-04T10:00:00 te/2025-10-04T13:00:00`<br>
       Expected: Entire timeslot removed. Success message shows the changes (in words).

    3. Test case: `unblock-timeslot ts/4 Oct 2025, 11:00 te/4 Oct 2025, 12:00`<br>
       Expected: Middle portion removed, timeslot split into two parts.

2. Unblocking partial overlap

    1. Prerequisites: A timeslot exists from 10:00 to 13:00 on 4 Oct 2025.

    2. Test case: `unblock-timeslot ts/4 Oct 2025, 12:00 te/4 Oct 2025, 14:00`<br>
       Expected: Right portion removed.

3. Unblocking non-existent timeslot

    1. Test case: `unblock-timeslot ts/1 Jan 2026, 10:00 te/1 Jan 2026, 11:00`<br>
       Expected: Error message indicating no stored timeslot overlaps the given range.

4. Invalid unblock commands

    1. Test case: `unblock-timeslot ts/4 Oct 2025, 13:00 te/4 Oct 2025, 10:00`<br>
       Expected: Error message indicating invalid timeslot range (end must be after start).

### Adding a consultation

1. Adding a valid consultation

    1. Prerequisites: No existing timeslots or consultations at the specified time.

    2. Test case: `add-consultation ts/2025-10-04T10:00:00 te/2025-10-04T11:00:00 n/John Doe`<br>
       Expected: Consultation added successfully. Success message shows timeslot and student name.

    3. Test case: `add-consultation ts/5 Oct 2025, 14:00 te/5 Oct 2025, 15:00 n/Alice Tan`<br>
       Expected: Consultation added using human-friendly format. Success message displayed.

2. Adding duplicate or overlapping consultation

    1. Prerequisites: A consultation exists with John Doe from 10:00 to 11:00 on 4 Oct 2025.

    2. Test case: `add-consultation ts/2025-10-04T10:00:00 te/2025-10-04T11:00:00 n/John Doe`<br>
       Expected: Error message indicating consultation already exists.

    3. Test case: `add-consultation ts/2025-10-04T10:30:00 te/2025-10-04T11:30:00 n/Alice Tan`<br>
       Expected: Error message indicating timeslot already exists.

3. Invalid consultation commands

    1. Test case: `add-consultation ts/2025-10-04T10:00:00 te/2025-10-04T11:00:00`<br>
       Expected: Error message indicating invalid command format.

    2. Test case: `add-consultation ts/2025-10-04T11:00:00 te/2025-10-04T10:00:00 n/John Doe`<br>
       Expected: Error message indicating end datetime must be after start datetime.

    3. Test case: `add-consultation n/John Doe`<br>
       Expected: Error message indicating invalid command format.

### Getting timeslots

1. Retrieving blocked timeslots

    1. Prerequisites: Several blocked timeslots exist in the system.

    2. Test case: `get-timeslots`<br>
       Expected: All blocked timeslots displayed in a new window as merged ranges in chronological order. Success message shows "Blocked Timeslots:" followed by the list.

2. Getting timeslots when none exist

    1. Prerequisites: No blocked timeslots in the system.

    2. Test case: `get-timeslots`<br>
       Expected: Message shows "No timeslots found."

3. Getting timeslots with overlapping ranges

    1. Prerequisites: Multiple overlapping blocked timeslots exist.

    2. Test case: `get-timeslots`<br>
       Expected: Overlapping timeslots are merged and displayed as continuous ranges.

4. Schedule window behaviour

    1. Prerequisites: Schedule window should already be opened

    2. Test case: `get-timeslots`<br>
       Expected: Existing schedule window comes to focus, no duplicate window created.

### Getting consultations

1. Retrieving consultations

    1. Prerequisites: Several consultations exist in the system.

    2. Test case: `get-consultations`<br>
       Expected: All consultation timeslots displayed as merged ranges in chronological order. Message shows "Consultations:" followed by the list.

2. Getting consultations when none exist

    1. Prerequisites: No consultations scheduled in the system.

    2. Test case: `get-consultations`<br>
       Expected: Error message shows "No consultations found."

3. Getting consultations with mixed timeslots

    1. Prerequisites: Both blocked timeslots and consultations exist.

    2. Test case: `get-consultations`<br>
       Expected: Only consultation timeslots are displayed, blocked timeslots are excluded.

4. Schedule window behaviour

    1. Prerequisites: Schedule window should already be opened

    2. Test case: `get-consultations`<br>
       Expected: Existing schedule window comes to focus, no duplicate window created.


### Getting help

1. Opening the help window

    1. Test case: `help`<br>
       Expected: Help window opens.

    2. Test case: Press `F1` key<br>
       Expected: Help window opens.
   
    3. Test case: `help x`<br>
       Expected: Help window opens.

2. Help window behavior

    1. Prerequisites: Help window should already be opened

    2. Test case: `help` <br>
       Expected: Existing help window comes to focus, no duplicate window created.
   

### Saving data

1. Data persistence

    1. Test case: Perform various `add` / `edit` / `delete` / `marka` commands. Then close and then reopen the app.
       Expected: Data should save automatically and persist after closing and reopening the app.

2. Simulate a missing data file:
    1. Test case: Remove `addressbook.json` in `/data` folder. Launch the app
       Expected: The app initializes with default data, showing error or warning messages about missing or corrupted data.

3. Simulate a corrupted data file:
    1. Test case: Remove some lines from `addressbook.json` in `/data` folder. Launch the app.
       Expected: The app initializes with no data, showing error messages about corrupted data in the terminal.

### Extra Testing
Testers are encouraged to create their own test scenarios by combining the commands covered in previous sections. Here are some examples of workflows to explore:

- **Filter + Mark Attendance**: Filter students by a criteria, then mark their attendance. Verify the filter updates correctly.
- **Set Week + Mark Attendance**: Set different week numbers and observe how lab colors change (grey for future, red for absent past labs, green for attended).
- **Add + Filter + Sort**: Add new students, filter by tags or status, then sort the results by different criteria.
- **Multi-Index Operations**: Use range indices (e.g., `1:5`) with edit (for tagging), delete, marka, marke, and grade commands, then verify results.
- **Scheduling Workflows**: Block timeslots, add consultations, unblock portions, and use `get-timeslots`/`get-consultations` to verify.
- **Undo After Operations**: Perform various data-modifying commands, then use `undo` to verify state restoration.
- **Filter Persistence**: Apply filters, execute commands like marka or marke, verify filters remain active and update correctly.


