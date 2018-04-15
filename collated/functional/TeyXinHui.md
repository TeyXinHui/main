# TeyXinHui
###### \java\seedu\address\logic\commands\AddSubjectCommand.java
``` java
/**
 * Edits the subject details of the student at a specified index.
 */
public class AddSubjectCommand extends UndoableCommand {
    public static final String COMMAND_WORD = "addsub";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds on the new subjects to the student's "
            + "subjects identified by the index number used in the last student listing. Duplicate subject input "
            + "will not alter the original subject in the subject list.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_SUBJECT + "SUBJECT SUBJECT_GRADE...]...\n"
            + "Example: " + COMMAND_WORD + " 1 sub/Jap A1";

    public static final String MESSAGE_ADD_SUBJECT_SUCCESS = "Edited Person: ";
    public static final String MESSAGE_NEW_SUBJECTS = ". Updated Subjects: ";
    public static final String MESSAGE_NOT_EDITED = "At least one field to add must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;

    private Person personToEdit;
    private Person editedPerson;

    /**
     * @param index of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public AddSubjectCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException, IOException {
        StringBuilder result = new StringBuilder();
        try {
            model.updatePerson(personToEdit, editedPerson);
            model.deletePage(personToEdit);
            model.addPage(editedPerson);
        } catch (DuplicatePersonException dpe) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        } catch (PersonNotFoundException pnfe) {
            throw new AssertionError("The target person cannot be missing");
        }
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(result.append(MESSAGE_ADD_SUBJECT_SUCCESS).append(editedPerson.getName())
                .append(MESSAGE_NEW_SUBJECTS).append(editedPerson.getSubjects()).toString());
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        personToEdit = lastShownList.get(index.getZeroBased());
        editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);
    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert personToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Nric updatedNric = editPersonDescriptor.getNric().orElse(personToEdit.getNric());
        Set<Tag> updatedTags = editPersonDescriptor.getTags().orElse(personToEdit.getTags());
        Set<Subject> updatedSubjects = new HashSet<>(personToEdit.getSubjects());
        Set<Subject> newSubjects = new HashSet<>(editPersonDescriptor.getSubjectsAsSet());
        checkIfSubjectExists(newSubjects, updatedSubjects);
        Remark updatedRemark = editPersonDescriptor.getRemark().orElse(personToEdit.getRemark());
        Cca updatedCca = editPersonDescriptor.getCca().orElse(personToEdit.getCca());
        InjuriesHistory updatedInjuriesHistory = editPersonDescriptor.getInjuriesHistory()
                .orElse(personToEdit.getInjuriesHistory());
        NextOfKin updatedNextOfKin = editPersonDescriptor.getNextOfKin().orElse(personToEdit.getNextOfKin());
        return new Person(updatedName, updatedNric, updatedTags, updatedSubjects, updatedRemark, updatedCca,
                updatedInjuriesHistory, updatedNextOfKin);
    }

    /**
     * Checks if the new subjects to be added exist in original subject list.
     * If the subject exists, the subject will not be added to the list. Else, it will be added.
     */
    public static void checkIfSubjectExists(Set<Subject> newSubjects, Set<Subject> subjectList) {
        boolean isPresent = false;
        for (Subject subToAdd : newSubjects) {
            for (Subject sub : subjectList) {
                if (subToAdd.subjectName.equals(sub.subjectName)) {
                    isPresent = true;
                }
            }
            if (!isPresent) {
                subjectList.add(subToAdd);
            }
            isPresent = false;
        }
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddSubjectCommand)) {
            return false;
        }

        // state check
        AddSubjectCommand e = (AddSubjectCommand) other;
        return index.equals(e.index)
                && editPersonDescriptor.equals(e.editPersonDescriptor)
                && Objects.equals(personToEdit, e.personToEdit);
    }
```
###### \java\seedu\address\logic\commands\EditPersonDescriptor.java
``` java
    /**
     * Returns an unmodifiable subject set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     * Returns {@code Optional#empty()} if {@code subjects} is null.
     */
    public Set<Subject> getSubjectsAsSet() {
        return (subjects != null) ? Collections.unmodifiableSet(subjects) : Collections.emptySet();
    }
```
###### \java\seedu\address\logic\commands\StreamCommand.java
``` java
/**
 * Finds student at the specified index and returns a streaming score based on the streaming type provided.
 */
public class StreamCommand extends Command {

    public static final String COMMAND_WORD = "stream";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds student at the specified index given and "
            + "returns a streaming score based on the streaming type provided.\n"
            + "Parameters: [INDEX] [STREAMING_TYPE]\n"
            + "Example: " + COMMAND_WORD + " 1 1";

    public static final String MESSAGE_SELECT_STUDENT_SUCCESS = "Student: %1$s";
    public static final String MESSAGE_L1R5_SUCCESS = ". L1R5 Score: %1$s";
    public static final String MESSAGE_L1B4A_SUCCESS = ". L1B4A Score: %1$s";
    public static final String MESSAGE_L1B4B_SUCCESS = ". L1B4B Score: %1$s";
    public static final String MESSAGE_L1B4C_SUCCESS = ". L1B4C Score: %1$s";
    public static final String MESSAGE_L1B4D_SUCCESS = ". L1B4D Score: %1$s";

    private final Index targetIndex;
    private int type;

    public StreamCommand(Index index, int type) {
        this.targetIndex = index;
        this.type = type;
    }

    @Override
    public CommandResult execute() throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();
        String message;

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person selectedPerson = lastShownList.get(targetIndex.getZeroBased());
        EventsCenter.getInstance().post(new JumpToListRequestEvent(targetIndex));

        message = scoreCalculation(selectedPerson, type);
        return new CommandResult(message);
    }

    /**
     * Returns a {@String result} message according to the subject combination
     * of {@Person selectedPerson} and {@int type}.
     * @return message to user
     */
    public static String scoreCalculation(Person selectedPerson, int type) {
        StringBuilder result = new StringBuilder();
        String message = "";
        int score = 0;

        switch (type) {
        case(1):
            score = selectedPerson.calculateL1R5();
            message = MESSAGE_L1R5_SUCCESS;
            break;
        case(2):
            score = selectedPerson.calculateL1B4A();
            message = MESSAGE_L1B4A_SUCCESS;
            break;
        case(3):
            score = selectedPerson.calculateL1B4B();
            message = MESSAGE_L1B4B_SUCCESS;
            break;
        case(4):
            score = selectedPerson.calculateL1B4C();
            message = MESSAGE_L1B4C_SUCCESS;
            break;
        case(5):
            score = selectedPerson.calculateL1B4D();
            message = MESSAGE_L1B4D_SUCCESS;
            break;
        default:
            break;
        }
        return result.append(String.format(MESSAGE_SELECT_STUDENT_SUCCESS, selectedPerson.getName()))
                .append(String.format(message, score)).toString();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof StreamCommand // instanceof handles nulls
                && this.targetIndex.equals(((StreamCommand) other).targetIndex)
                && (this.type == (((StreamCommand) other).type))); // state check
    }

}
```
###### \java\seedu\address\logic\commands\TagDeleteCommand.java
``` java
/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class TagDeleteCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "tagdelete";
    public static final String COMMAND_ALIAS = "td";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Removes specified tag from everyone in the "
            + "in the address book.\n"
            + "Parameters: Tag \n"
            + "Example: " + COMMAND_WORD + " 1A\n"
            + "Example: " + COMMAND_ALIAS + " 1A";

    public static final String MESSAGE_DELETE_TAG_SUCCESS = "Deleted Tag: %1$s";

    private Tag tagToDelete;

    public TagDeleteCommand(Tag tagToDelete) {
        this.tagToDelete = tagToDelete;
    }

    @Override
    public CommandResult executeUndoableCommand() {
        requireNonNull(tagToDelete);
        try {
            model.deleteTag(tagToDelete);
        } catch (TagNotFoundException error) {
            throw new AssertionError("The target tag cannot be missing");
        }
        return new CommandResult(String.format(MESSAGE_DELETE_TAG_SUCCESS, tagToDelete));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        ReadOnlyAddressBook addressBook = model.getAddressBook();
        List<Tag> lastShownList = addressBook.getTagList();

        if (!lastShownList.contains(tagToDelete)) {
            throw new CommandException(Messages.MESSAGE_INVALID_TAG_ENTERED);
        }

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TagDeleteCommand // instanceof handles nulls
                && this.tagToDelete.equals(((TagDeleteCommand) other).tagToDelete)); // state check
    }
}
```
###### \java\seedu\address\logic\parser\AddSubjectCommandParser.java
``` java
/**
 * Parses input arguments and creates a new AddSubjectCommand object
 */
public class AddSubjectCommandParser implements Parser<AddSubjectCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditSubjectCommand
     * and returns an EditSubjectCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddSubjectCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_SUBJECT);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddSubjectCommand.MESSAGE_USAGE));
        }

        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        try {
            parseSubjectsForEdit(argMultimap.getAllValues(PREFIX_SUBJECT)).ifPresent(editPersonDescriptor::setSubjects);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }

        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new ParseException(AddSubjectCommand.MESSAGE_NOT_EDITED);
        }

        return new AddSubjectCommand(index, editPersonDescriptor);
    }

    /**
     * Parses {@code Collection<String> subjects} into a {@code Set<Subject>} if {@code subjects} is non-empty.
     * If {@code subjects} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Subject>} containing zero subjects.
     */
    private Optional<Set<Subject>> parseSubjectsForEdit(Collection<String> subjects) throws IllegalValueException {
        assert subjects != null;

        if (subjects.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> subjectSet = subjects.size() == 1 && subjects.contains("")
                ? Collections.emptySet() : subjects;
        return Optional.of(ParserUtil.parseSubjects(subjectSet));
    }
```
###### \java\seedu\address\logic\parser\EditCommandParser.java
``` java
    /**
     * Parses {@code Collection<String> subjects} into a {@code Set<Subject>} if {@code subjects} is non-empty.
     * If {@code subjects} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Subject>} containing zero subjects.
     */
    private Optional<Set<Subject>> parseSubjectsForEdit(Collection<String> subjects) throws IllegalValueException {
        assert subjects != null;

        if (subjects.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> subjectSet = subjects.size() == 1 && subjects.contains("")
                ? Collections.emptySet() : subjects;
        return Optional.of(ParserUtil.parseSubjects(subjectSet));
    }
```
###### \java\seedu\address\logic\parser\ParserUtil.java
``` java
    /**
     * Splits a {@code String subjects} into {@code String subjectName} and {@code String subjectGrade}
     * Parses {@code String subjectName} and {@code String subjectGrade}into a {@code Subject}.
     * Add {@code Subject} into a {@code Set<Subject> subjectSet}.
     * @throws IllegalValueException if the given {@code subjects} is invalid
     */
    public static void parseSubject(Collection<String> subjects, Set<Subject> subjectSet) throws IllegalValueException {
        requireNonNull(subjects);
        String subjectsStr = Iterables.get(subjects, 0);
        String[] splitSubjectStr = subjectsStr.trim().split("\\s+");
        Subject subjectToAdd;
        for (int i = 0; i < splitSubjectStr.length; i++) {
            String subjectName = splitSubjectStr[i];
            if (!Subject.isValidSubjectName(subjectName)) {
                throw new IllegalValueException(Subject.MESSAGE_SUBJECT_NAME_CONSTRAINTS);
            }
            i += 1;
            String subjectGrade;
            if (i >= splitSubjectStr.length) {
                throw new IllegalValueException(Subject.MESSAGE_SUBJECT_GRADE_CONSTRAINTS);
            } else {
                subjectGrade = splitSubjectStr[i];
            }
            if (!Subject.isValidSubjectGrade(subjectGrade)) {
                throw new IllegalValueException(Subject.MESSAGE_SUBJECT_GRADE_CONSTRAINTS);
            }
            subjectToAdd = new Subject(subjectName, subjectGrade);
            for (Subject s : subjectSet) {
                if (s.subjectName.equals(subjectToAdd.subjectName)) {
                    throw new IllegalValueException(Subject.MESSAGE_DUPLICATE_SUBJECT);
                }
            }
            subjectSet.add(new Subject(subjectName, subjectGrade));
        }
    }

    /**
     * Parses {@code Collection<String> subjects} into a {@code Set<Subject}.
     */
    public static Set<Subject> parseSubjects(Collection<String> subjects) throws IllegalValueException {
        requireNonNull(subjects);
        final Set<Subject> subjectSet = new HashSet<>();
        if (subjects.size() == 1) {
            parseSubject(subjects, subjectSet);
        }
        return subjectSet;
    }

```
###### \java\seedu\address\logic\parser\StreamCommandParser.java
``` java
/**
 * Parses input arguments and creates a new StreamCommand object
 */
public class StreamCommandParser implements Parser<StreamCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns an FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public StreamCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        Index targetIndex;

        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, StreamCommand.MESSAGE_USAGE));
        }

        String[] nameKeywords = trimmedArgs.split("\\s+");
        try {
            targetIndex = ParserUtil.parseIndex(nameKeywords[0]);
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, StreamCommand.MESSAGE_USAGE));
        }
        if (nameKeywords.length != 2 || !nameKeywords[1].matches("-?\\d+")) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, StreamCommand.MESSAGE_USAGE));
        }
        int type = Integer.parseInt(nameKeywords[1]);

        return new StreamCommand(targetIndex, type);
    }

}
```
###### \java\seedu\address\logic\parser\TagDeleteCommandParser.java
``` java
/**
 * Parses input arguments and creates a new FindCommand object
 */
public class TagDeleteCommandParser implements Parser<TagDeleteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the TagDeleteCommand
     * and returns an TagDeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public TagDeleteCommand parse(String args) throws ParseException {
        try {
            Tag tag = ParserUtil.parseTag(args.trim());
            return new TagDeleteCommand(tag);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagDeleteCommand.MESSAGE_USAGE));
        }
    }

}
```
###### \java\seedu\address\model\AddressBook.java
``` java
    public void addSubject(Subject s) throws UniqueSubjectList.DuplicateSubjectException {
        subjects.add(s);
    }

    /**
     * Calls removeTagFromPerson method when tag is found in tags.
     * @param t
     * @throws TagNotFoundException
     */
    public void removeTag(Tag t) throws TagNotFoundException {
        if (tags.contains(t)) {
            for (Person person : persons) {
                removeTagFromPerson(t, person);
            }
        } else {
            throw new TagNotFoundException("Specific tag is not used in the address book.");
        }
    }

```
###### \java\seedu\address\model\AddressBook.java
``` java
    /**
     * Removes a specific tag from an individual person and updates the person's information.
     * Person needs to have the specific tag in his/her tag list.
     * @param tag
     * @param person
     */
    public void removeTagFromPerson(Tag tag, Person person) {
        Set<Tag> tagList = new HashSet<>(person.getTags());
        if (tagList.remove(tag)) {
            Person newPerson = new Person(person.getName(), person.getNric(), tagList, person.getSubjects(),
                                        person.getRemark(), person.getCca(), person.getInjuriesHistory(),
                                        person.getNextOfKin());
            try {
                updatePerson(person, newPerson);
            } catch (DuplicatePersonException error1) {
                throw new AssertionError("Updating person after removing tag should not have duplicate persons.");
            } catch (PersonNotFoundException error2) {
                throw new AssertionError("Person should exist in the address book.");
            }
        }

    }

```
###### \java\seedu\address\model\Model.java
``` java
    /**
     * Removes a specific tag from everyone in the address book.
     * @param tag
     * @throws TagNotFoundException
     */
    void deleteTag(Tag tag) throws TagNotFoundException;
```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public void deleteTag(Tag tag) {
        try {
            addressBook.removeTag(tag);
        } catch (TagNotFoundException error) {
            throw new AssertionError();
        }
    }

```
###### \java\seedu\address\model\person\Person.java
``` java
    /**
     * Calculates the lowest possible score from the grades of the subjects of the selected person.
     * @return L1R5 score
     */
    public int calculateL1R5() {
        int score = 0;
        Set<Subject> subjects = new HashSet<>(this.getSubjects());
        Set<Subject> subjectsToCheck = new HashSet<>();
        for (int i = 0; i < 6; i++) {
            for (Subject subject: subjects) {
                switch (i) {
                // Check if the subject is a L1 subject
                case 0:
                    if (Arrays.asList(Subject.L1_SUBJECT).contains(subject.subjectName)) {
                        subjectsToCheck.add(subject);
                    }
                    break;
                // Check if the subject is a R1 subject
                case 1:
                    if (Arrays.asList(Subject.R1_SUBJECT).contains(subject.subjectName)) {
                        subjectsToCheck.add(subject);
                    }
                    break;
                // Check if the subject is a R2 subject
                case 2:
                    if (Arrays.asList(Subject.R2_SUBJECT).contains(subject.subjectName)) {
                        subjectsToCheck.add(subject);
                    }
                    break;
                // Check if the subject is a R3 subject
                case 3:
                    if (Arrays.asList(Subject.R3_SUBJECT).contains(subject.subjectName)) {
                        subjectsToCheck.add(subject);
                    }
                    break;
                // Check if the subject is a R4 or R5 subject
                case 4:
                case 5:
                    if (Arrays.asList(Subject.R4_R5_SUBJECT).contains(subject.subjectName)) {
                        subjectsToCheck.add(subject);
                    }
                    break;
                default:
                    break;
                }
            }
            // Check that if the student has at least one subject in each L1R5 category, else return error message
            if (checkLowest(subjectsToCheck, subjects) == 10) {
                return 0;
            } else {
                score += checkLowest(subjectsToCheck, subjects);
            }
            subjectsToCheck.clear();
        }
        return score;
    }

```
###### \java\seedu\address\model\person\Person.java
``` java
    /**
     * Calculates the lowest possible score from the grades of the subjects of the selected person.
     * @return L1B4-A score
     */
    public int calculateL1B4A() {
        int score = 0;
        Set<Subject> subjects = new HashSet<>(this.getSubjects());
        Set<Subject> subjectsToCheck = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            for (Subject subject: subjects) {
                switch (i) {
                // Check if the subject is a L1 subject
                case 0:
                    if (Arrays.asList(Subject.L1_SUBJECT).contains(subject.subjectName)) {
                        subjectsToCheck.add(subject);
                    }
                    break;
                // Check if the subject is a R1 subject
                case 1:
                    if (Arrays.asList(Subject.R1A_SUBJECT).contains(subject.subjectName)) {
                        subjectsToCheck.add(subject);
                    }
                    break;
                // Check if the subject is a R2 subject
                case 2:
                    if (Arrays.asList(Subject.R2A_SUBJECT).contains(subject.subjectName)) {
                        subjectsToCheck.add(subject);
                    }
                    break;
                // Check if the subject is a R3 or R4 subject
                case 3:
                case 4:
                    if (Arrays.asList(Subject.R3_R4_SUBJECT).contains(subject.subjectName)) {
                        subjectsToCheck.add(subject);
                    }
                    break;
                default:
                    break;
                }
            }
            // Check that if the student has at least one subject in each L1R5 category, else return error message
            if (checkLowest(subjectsToCheck, subjects) == 10) {
                return 0;
            } else {
                score += checkLowest(subjectsToCheck, subjects);
            }
            subjectsToCheck.clear();
        }
        return score;
    }
```
###### \java\seedu\address\model\person\Person.java
``` java
    /**
     * Calculates the lowest possible score from the grades of the subjects of the selected person.
     * @return L1B4-B score
     */
    public int calculateL1B4B() {
        int score = 0;
        Set<Subject> subjects = new HashSet<>(this.getSubjects());
        Set<Subject> subjectsToCheck = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            for (Subject subject: subjects) {
                switch (i) {
                // Check if the subject is a L1 subject
                case 0:
                    if (Arrays.asList(Subject.L1_SUBJECT).contains(subject.subjectName)) {
                        subjectsToCheck.add(subject);
                    }
                    break;
                // Check if the subject is a R1 subject
                case 1:
                    if (Arrays.asList(Subject.R1BCD_SUBJECT).contains(subject.subjectName)) {
                        subjectsToCheck.add(subject);
                    }
                    break;
                // Check if the subject is a R2 subject
                case 2:
                    if (Arrays.asList(Subject.R2B_SUBJECT).contains(subject.subjectName)) {
                        subjectsToCheck.add(subject);
                    }
                    break;
                // Check if the subject is a R3 or R4 subject
                case 3:
                case 4:
                    if (Arrays.asList(Subject.R3_R4_SUBJECT).contains(subject.subjectName)) {
                        subjectsToCheck.add(subject);
                    }
                    break;
                default:
                    break;
                }
            }
            // Check that if the student has at least one subject in each L1R5 category, else return error message
            if (checkLowest(subjectsToCheck, subjects) == 10) {
                return 0;
            } else {
                score += checkLowest(subjectsToCheck, subjects);
            }
            subjectsToCheck.clear();
        }
        return score;
    }
```
###### \java\seedu\address\model\person\Person.java
``` java
    /**
     * Calculates the lowest possible score from the grades of the subjects of the selected person.
     * @return L1B4-C score
     */
    public int calculateL1B4C() {
        int score = 0;
        Set<Subject> subjects = new HashSet<>(this.getSubjects());
        Set<Subject> subjectsToCheck = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            for (Subject subject: subjects) {
                switch (i) {
                // Check if the subject is a L1 subject
                case 0:
                    if (Arrays.asList(Subject.L1_SUBJECT).contains(subject.subjectName)) {
                        subjectsToCheck.add(subject);
                    }
                    break;
                // Check if the subject is a R1 subject
                case 1:
                    if (Arrays.asList(Subject.R1BCD_SUBJECT).contains(subject.subjectName)) {
                        subjectsToCheck.add(subject);
                    }
                    break;
                // Check if the subject is a R2 subject
                case 2:
                    if (Arrays.asList(Subject.R2C_SUBJECT).contains(subject.subjectName)) {
                        subjectsToCheck.add(subject);
                    }
                    break;
                // Check if the subject is a R3 or R4 subject
                case 3:
                case 4:
                    if (Arrays.asList(Subject.R3_R4_SUBJECT).contains(subject.subjectName)) {
                        subjectsToCheck.add(subject);
                    }
                    break;
                default:
                    break;
                }
            }
            // Check that if the student has at least one subject in each L1R5 category, else return 0
            if (checkLowest(subjectsToCheck, subjects) == 10) {
                return 0;
            } else {
                score += checkLowest(subjectsToCheck, subjects);
            }
            subjectsToCheck.clear();
        }
        return score;
    }
```
###### \java\seedu\address\model\person\Person.java
``` java
    /**
     * Calculates the lowest possible score from the grades of the subjects of the selected person.
     * @return L1B4-D score
     */
    public int calculateL1B4D() {
        int score = 0;
        Set<Subject> subjects = new HashSet<>(this.getSubjects());
        Set<Subject> subjectsToCheck = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            for (Subject subject: subjects) {
                switch (i) {
                // Check if the subject is a L1 subject
                case 0:
                    if (Arrays.asList(Subject.L1_SUBJECT).contains(subject.subjectName)) {
                        subjectsToCheck.add(subject);
                    }
                    break;
                // Check if the subject is a R1 subject
                case 1:
                    if (Arrays.asList(Subject.R1BCD_SUBJECT).contains(subject.subjectName)) {
                        subjectsToCheck.add(subject);
                    }
                    break;
                // Check if the subject is a R2 subject
                case 2:
                    if (Arrays.asList(Subject.R2D_SUBJECT).contains(subject.subjectName)) {
                        subjectsToCheck.add(subject);
                    }
                    break;
                // Check if the subject is a R3 or R4 subject
                case 3:
                case 4:
                    if (Arrays.asList(Subject.R3_R4_SUBJECT).contains(subject.subjectName)) {
                        subjectsToCheck.add(subject);
                    }
                    break;
                default:
                    break;
                }
            }
            // Check that if the student has at least one subject in each L1R5 category, else return error message
            if (checkLowest(subjectsToCheck, subjects) == 10) {
                return 0;
            } else {
                score += checkLowest(subjectsToCheck, subjects);
            }
            subjectsToCheck.clear();
        }
        return score;
    }
```
###### \java\seedu\address\model\person\Person.java
``` java
    /**
     * Takes in a set of subjects under the category of L1 or R5 and find the smallest grade score.
     * Removes the best subject from the full list of subjects of the student to prevent the same subject being
     * considered in the L1R5 score more than once.
     * @param subjectsToCheck
     * @return lowest grade score
     */
    public static int checkLowest(Set<Subject> subjectsToCheck, Set<Subject> subjects) {
        int lowest = 10;
        Subject bestSubject = new Subject();
        for (Subject subject: subjectsToCheck) {
            if (Character.getNumericValue(subject.subjectGrade.charAt(1)) < lowest) {
                lowest = Character.getNumericValue(subject.subjectGrade.charAt(1));
                bestSubject = subject;
            }
        }
        subjects.remove(bestSubject);
        return lowest;
    }

```
###### \java\seedu\address\model\subject\Subject.java
``` java
/**
 * Represents a subject in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Subject {

    public static final String[] SUBJECT_NAME = new String[] {"English", "Chinese", "HChi", "CSP",
                                                              "ChiB", "French", "German", "Spanish", "Hindi",
                                                              "Urdu", "Gujarati", "Panjabi", "Bengali", "Burmese",
                                                              "Thai", "Jap", "BIndo", "Tamil",
                                                              "HTamil", "TamilB", "Malay", "HMalay", "MalayB",
                                                              "MSP", "EMath", "AMath", "Phy", "Chem", "Bio", "Sci",
                                                              "Hist", "Geog", "ComHum", "ELit", "CLit", "MLit", "TLit",
                                                              "Music", "HMusic", "Art", "HArt", "DnT", "Comp",
                                                              "FnN", "PoA", "Econs", "Drama", "PE", "Biz", "Biotech",
                                                              "Design"};
    public static final String MESSAGE_SUBJECT_NAME_CONSTRAINTS = "Subject names should be alphabetic and should be "
            + "one of the following: \n" + Arrays.toString(Arrays.copyOfRange(SUBJECT_NAME, 0, 9)) + "\n"
            + Arrays.toString(Arrays.copyOfRange(SUBJECT_NAME, 10, 19)) + "\n"
            + Arrays.toString(Arrays.copyOfRange(SUBJECT_NAME, 20, 29)) + "\n"
            + Arrays.toString(Arrays.copyOfRange(SUBJECT_NAME, 30, 39)) + "\n"
            + Arrays.toString(Arrays.copyOfRange(SUBJECT_NAME, 40, SUBJECT_NAME.length)) + ".";
    public static final String[] SUBJECT_GRADE = new String[] {"A1", "A2", "B3", "B4", "C5", "C6", "D7", "E8", "F9"};

    public static final String MESSAGE_SUBJECT_GRADE_CONSTRAINTS = "Subject grade should be alphanumeric and should be"
            + " one of the following: \n" + Arrays.deepToString(SUBJECT_GRADE) + ".";

    public static final String MESSAGE_DUPLICATE_SUBJECT = "There should not be duplicate subject(s) assigned "
            + "to student.";

    // Use for the calculation of the L1R5 subjects
    public static final String[] L1_SUBJECT = {"English", "HChi", "HTamil", "HMalay"};
    public static final String[] R1_SUBJECT = {"Hist", "Geog", "Com.Hum",  "ELit", "CLit", "MLit", "TLit", "HArt",
                                               "HMusic", "BIndo", "CSP", "MSP"};
    public static final String[] R2_SUBJECT = {"EMath", "AMath", "Phy", "Chem", "Bio", "Sci"};
    public static final String[] R3_SUBJECT = {"Hist", "Geog", "ComHum",  "ELit", "CLit", "MLit", "TLit", "HArt",
                                               "H.Music", "BIndo", "CSP", "MSP", "EMath", "AMath", "Phy", "Chem",
                                               "Bio", "Sci"};
    public static final String[] R4_R5_SUBJECT = {"English", "Chinese", "HChi", "CSP", "French", "German", "Spanish",
                                                  "Hindi", "Urdu", "Gujarati", "Panjabi", "Bengali", "Burmese",
                                                  "Thai", "Jap", "BIndo", "Tamil", "HTamil", "Malay", "HMalay",
                                                  "MSP", "EMath", "AMath", "Phy", "Chem", "Bio", "Sci", "Hist", "Geog",
                                                  "ComHum", "ELit", "CLit", "MLit", "TLit", "Music", "HMusic", "Art",
                                                  "HArt", "DnT", "Comp", "FnN", "PoA", "Econs", "Drama", "PE",
                                                  "Biz", "Biotech", "Design"};

    // Use for the calculation of the L1B4 subjects (A, B, C, D)
    public static final String[] R1A_SUBJECT = {"Art", "Biz", "ComHum", "Econs", "Geog", "HArt", "HMusic", "Hist",
                                                "ELit", "CLit", "MLit", "TLit", "Music"};
    public static final String[] R1BCD_SUBJECT = {"EMath", "AMath"};
    public static final String[] R2A_SUBJECT = {"AMath", "Art", "Biz", "Chinese", "ComHum", "DnT", "Design", "Econs",
                                                "EMath", "FnN", "Geog", "HArt", "HChi", "HMalay", "HTamil", "Hist",
                                                "ELit", "CLit", "MLit", "TLit", "Malay", "Music", "PoA", "Tamil"};
    public static final String[] R2B_SUBJECT = {"Art", "Biz", "ComHum", "Econs", "Geog", "HArt", "HMusic", "Hist",
                                                "ELit", "CLit", "TLit", "MLit", "Music", "PoA"};
    public static final String[] R2C_SUBJECT = {"Bio", "Biotech", "Chem", "Sci", "DnT", "FnN", "Phy", "Comp"};
    public static final String[] R2D_SUBJECT = {"Art", "Bio", "Biotech", "Chem", "Sci", "Comp", "DnT", "Design", "FnN",
                                                "HArt", "Phy", "Sci"};
    public static final String[] R3_R4_SUBJECT = R4_R5_SUBJECT;

    public final String subjectName;
    public final String subjectGrade;

    /**
     * Default constructor of Subject Object
     */
    public Subject() {
        this.subjectName = "";
        this.subjectGrade = "";
    }

    /**
     * Constructs a {@code Subject}.
     *
     * @param subjectName  A valid subject name.
     * @param subjectGrade A valid subject grade.
     */
    public Subject(String subjectName, String subjectGrade) {
        requireNonNull(subjectName);
        checkArgument(isValidSubjectName(subjectName), MESSAGE_SUBJECT_NAME_CONSTRAINTS);
        this.subjectName = subjectName;

        requireNonNull(subjectGrade);
        checkArgument(isValidSubjectGrade(subjectGrade), MESSAGE_SUBJECT_GRADE_CONSTRAINTS);
        this.subjectGrade = subjectGrade;
    }

    /**
     * Constructs a {@code Subject} by splitting the subject string into {@code subjectName}.
     *
     * @param subject A valid subject string.
     */
    public Subject(String subject) {
        requireNonNull(subject);
        String[] splitSubjectStr = subject.trim().split("\\s+");
        String subjectName = splitSubjectStr[0];
        String subjectGrade = splitSubjectStr[1];
        this.subjectName = subjectName;
        this.subjectGrade = subjectGrade;
    }

    /**
     * Returns true if a given string is a valid subject name.
     */
    public static boolean isValidSubjectName(String test) {
        for (String validSubjectName : SUBJECT_NAME) {
            if (test.equals(validSubjectName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if a given string is a valid subject grade.
     */

    public static boolean isValidSubjectGrade(String test) {
        for (String validSubjectGrade : SUBJECT_GRADE) {
            if (test.equals(validSubjectGrade)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (this == other) { //same object
            return true;
        }
        if (!(other instanceof Subject)) {
            return false;
        }
        Subject object = (Subject) other;
        return (this.subjectName.equals(object.subjectName) && this.subjectGrade.equals(object.subjectGrade));
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = 37 * hash + subjectName.hashCode();
        hash = 37 * hash + subjectGrade.hashCode();
        return hash;
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + subjectName + ' ' + subjectGrade + ']';
    }
```
###### \java\seedu\address\model\subject\UniqueSubjectList.java
``` java
/**
 * A list of subjects that enforces no nulls and uniqueness between its elements.
 *
 * Supports minimal set of list operations for the app's features.
 *
 * @see Subject#equals(Object)
 */
public class UniqueSubjectList implements Iterable<Subject> {

    private final ObservableList<Subject> internalList = FXCollections.observableArrayList();

    /**
     * Constructs empty Subject List.
     */
    public UniqueSubjectList() {}

    /**
     * Creates a UniqueSubjectList using given subjects.
     * Enforces no nulls.
     */
    public UniqueSubjectList(Set<Subject> subjects) {
        requireAllNonNull(subjects);
        internalList.addAll(subjects);

        assert CollectionUtil.elementsAreUnique(internalList);
    }

    /**
     * Returns all Subjects in this list as a Set.
     * This set is mutable and change-insulated against the internal list.
     */
    public Set<Subject> toSet() {
        assert CollectionUtil.elementsAreUnique(internalList);
        return new HashSet<>(internalList);
    }

    /**
     * Replaces the Subjects in this list with those in the argument subject list.
     */
    public void setSubjects(Set<Subject> subjects) {
        requireAllNonNull(subjects);
        internalList.setAll(subjects);
        assert CollectionUtil.elementsAreUnique(internalList);
    }

    /**
     * Ensures every subject in the argument list exists in this object.
     */
    public void mergeFrom(UniqueSubjectList from) {
        final Set<Subject> alreadyInside = this.toSet();
        from.internalList.stream()
                .filter(subject -> !alreadyInside.contains(subject))
                .forEach(internalList::add);

        assert CollectionUtil.elementsAreUnique(internalList);
    }

    /**
     * Returns true if the list contains an equivalent Subject as the given argument.
     */
    public boolean contains(Subject toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Adds a Subject to the list.
     *
     * @throws DuplicateSubjectException if the Tag to add is a duplicate of an existing Subject in the list.
     */
    public void add(Subject toAdd) throws DuplicateSubjectException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateSubjectException();
        }
        internalList.add(toAdd);

        assert CollectionUtil.elementsAreUnique(internalList);
    }

    @Override
    public Iterator<Subject> iterator() {
        assert CollectionUtil.elementsAreUnique(internalList);
        return internalList.iterator();
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Subject> asObservableList() {
        assert CollectionUtil.elementsAreUnique(internalList);
        return FXCollections.unmodifiableObservableList(internalList);
    }

    @Override
    public boolean equals(Object other) {
        assert CollectionUtil.elementsAreUnique(internalList);
        return other == this // short circuit if same object
                || (other instanceof UniqueSubjectList // instanceof handles nulls
                && this.internalList.equals(((UniqueSubjectList) other).internalList));
    }

    /**
     * Returns true if the element in this list is equal to the elements in {@code other}.
     * The elements do not have to be in the same order.
     */
    public boolean equalsOrderInsensitive(UniqueSubjectList other) {
        assert CollectionUtil.elementsAreUnique(internalList);
        assert CollectionUtil.elementsAreUnique(other.internalList);
        return this == other || new HashSet<>(this.internalList).equals(new HashSet<>(other.internalList));
    }

    @Override
    public int hashCode() {
        assert CollectionUtil.elementsAreUnique(internalList);
        return internalList.hashCode();
    }

    /**
     * Signals that an operation would have violated the 'no duplicates' property of the list.
     */
    public static class DuplicateSubjectException extends DuplicateDataException {
        protected DuplicateSubjectException() {
            super("Operation would result in duplicate subject");
        }
    }

}
```
###### \java\seedu\address\model\tag\exceptions\TagNotFoundException.java
``` java
/**
 * Signals that the operation is unable to find the specified tag.
 */
public class TagNotFoundException extends Exception {
    public TagNotFoundException(String message) {
        super(message);
    }

}
```
###### \java\seedu\address\model\util\SampleDataUtil.java
``` java
    /**
     * Returns a subject set containing the list of strings given.
     */
    public static Set<Subject> getSubjectSet(String... strings) {
        HashSet<Subject> subjects = new HashSet<>();
        for (String s : strings) {
            subjects.add(new Subject(s));
        }

        return subjects;
    }
```
###### \java\seedu\address\storage\XmlAdaptedSubject.java
``` java
/**
 *
 */
public class XmlAdaptedSubject {

    @XmlElement
    private String subjectName;
    @XmlElement
    private String subjectGrade;

    /**
     * Constructs an XmlAdaptedSubject.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedSubject() {}

    /**
     * Constructs a {@code XmlAdaptedSubject} with the given {@code subject}.
     */
    public XmlAdaptedSubject(String subject) {
        String[] splitSubjectStr = subject.trim().split("\\s+");
        String subjectName = splitSubjectStr[0];
        String subjectGrade = splitSubjectStr[1];
        this.subjectName = subjectName;
        this.subjectGrade = subjectGrade;
    }

    /**
     * Converts a given Subject into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created
     */
    public XmlAdaptedSubject(Subject source) {
        subjectName = source.subjectName;
        subjectGrade = source.subjectGrade;
    }

    /**
     * Converts this jaxb-friendly adapted subject object into the model's subject object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted person
     */
    public Subject toModelType() throws IllegalValueException {
        if (!Subject.isValidSubjectName(subjectName)) {
            throw new IllegalValueException(Subject.MESSAGE_SUBJECT_NAME_CONSTRAINTS);
        }
        if (!Subject.isValidSubjectGrade(subjectGrade)) {
            throw new IllegalValueException(Subject.MESSAGE_SUBJECT_GRADE_CONSTRAINTS);
        }
        return new Subject(subjectName, subjectGrade);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (this == other) { //same object
            return true;
        }
        if (!(other instanceof XmlAdaptedSubject)) {
            return false;
        }
        XmlAdaptedSubject object = (XmlAdaptedSubject) other;
        return this.subjectName == object.subjectName && this.subjectGrade == object.subjectGrade;
    }
}
```
