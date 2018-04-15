# chuakunhong
###### \java\seedu\address\logic\commands\AddInjuriesHistoryCommand.java
``` java

/**
 * Adds the injuries history to an existing person in the address book.
 */
public class AddInjuriesHistoryCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "addinjuries";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a injuries history to the student that you want. "
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_INJURIES_HISTORY + "INJURYHISTORY\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_INJURIES_HISTORY + "Torn ligament" + "\n";

    public static final String MESSAGE_REMARK_PERSON_SUCCESS = "Injuries History added: %1$s\nPerson: %2$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;

    private Person personToEdit;
    private Person editedPerson;

    /**
     * @param index of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public AddInjuriesHistoryCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException, IOException {
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
        return new CommandResult(String.format(MESSAGE_REMARK_PERSON_SUCCESS, editPersonDescriptor.getInjuriesHistory()
                        .get(), personToEdit.getName()));
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
        Set<Subject> updatedSubjects = editPersonDescriptor.getSubjects().orElse(personToEdit.getSubjects());
        Remark updatedRemark = editPersonDescriptor.getRemark().orElse(personToEdit.getRemark());
        Cca updatedCca = editPersonDescriptor.getCca().orElse(personToEdit.getCca());
        InjuriesHistory updatedInjuriesHistory = ParserUtil.parseInjuriesHistory(personToEdit
                .getInjuriesHistory().toString() + "\n" + editPersonDescriptor.getInjuriesHistory().get().toString());
        NextOfKin updatedNextOfKin = editPersonDescriptor.getNextOfKin().orElse(personToEdit.getNextOfKin());

        return new Person(updatedName, updatedNric, updatedTags, updatedSubjects, updatedRemark, updatedCca,
                        updatedInjuriesHistory, updatedNextOfKin);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddInjuriesHistoryCommand)) {
            return false;
        }

        // state check
        AddInjuriesHistoryCommand e = (AddInjuriesHistoryCommand) other;
        return index.equals(e.index)
                && editPersonDescriptor.equals(e.editPersonDescriptor)
                && Objects.equals(personToEdit, e.personToEdit);
    }
```
###### \java\seedu\address\logic\commands\AddRemarkCommand.java
``` java
/**
 * Adds a remark to an existing person in the address book.
 */
public class AddRemarkCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "addremark";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds remarks to the student that you want. "
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_REMARK + "REMARKS...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_REMARK + "Need help" + "\n";

    public static final String MESSAGE_REMARK_PERSON_SUCCESS = "Remark added: %1$s\nPerson: %2$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;

    private Person personToEdit;
    private Person editedPerson;

    /**
     * @param index of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public AddRemarkCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = editPersonDescriptor;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException, IOException {
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
        return new CommandResult(String.format(MESSAGE_REMARK_PERSON_SUCCESS, editPersonDescriptor.getRemark().get(),
                                                personToEdit.getName()));
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
        Set<Subject> updatedSubjects = editPersonDescriptor.getSubjects().orElse(personToEdit.getSubjects());
        Remark updatedRemark = parseRemark(((personToEdit.getRemark()).toString() + "\n"
                                            + editPersonDescriptor.getRemark().get().toString()));
        Cca updatedCca = editPersonDescriptor.getCca().orElse(personToEdit.getCca());
        InjuriesHistory updatedInjuriesHistory = editPersonDescriptor.getInjuriesHistory()
                                                .orElse(personToEdit.getInjuriesHistory());
        NextOfKin updatedNextOfKin = editPersonDescriptor.getNextOfKin().orElse(personToEdit.getNextOfKin());

        return new Person(updatedName, updatedNric, updatedTags, updatedSubjects, updatedRemark, updatedCca,
                            updatedInjuriesHistory, updatedNextOfKin);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddRemarkCommand)) {
            return false;
        }

        // state check
        AddRemarkCommand e = (AddRemarkCommand) other;
        return index.equals(e.index)
                && editPersonDescriptor.equals(e.editPersonDescriptor)
                && Objects.equals(personToEdit, e.personToEdit);
    }
```
###### \java\seedu\address\logic\commands\CcaCommand.java
``` java

/**
 * Edits the cca details of an existing person in the address book.
 */
public class CcaCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "cca";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds the CCA and the position "
            + "to the student that you want. "
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_CCA + "CCA " + PREFIX_CCA_POSITION + "POSITION\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_CCA + "Basketball " + PREFIX_CCA_POSITION + "Member\n";

    public static final String MESSAGE_REMARK_PERSON_SUCCESS = "CCA added: %1$s\nPerson: %2$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;

    private Person personToEdit;
    private Person editedPerson;

    /**
     * @param index of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public CcaCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException, IOException {
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
        return new CommandResult(String.format(MESSAGE_REMARK_PERSON_SUCCESS, editPersonDescriptor.getCca().get(),
                personToEdit.getName()));
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
        Set<Subject> updatedSubjects = editPersonDescriptor.getSubjects().orElse(personToEdit.getSubjects());
        Remark updatedRemark = editPersonDescriptor.getRemark().orElse(personToEdit.getRemark());
        Cca updatedCca = editPersonDescriptor.getCca().orElse(personToEdit.getCca());
        InjuriesHistory updatedInjuriesHistory = editPersonDescriptor.getInjuriesHistory()
                .orElse(personToEdit.getInjuriesHistory());
        NextOfKin updatedNextOfKin = editPersonDescriptor.getNextOfKin()
                .orElse(personToEdit.getNextOfKin());

        return new Person(updatedName, updatedNric, updatedTags, updatedSubjects, updatedRemark, updatedCca,
                updatedInjuriesHistory, updatedNextOfKin);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CcaCommand)) {
            return false;
        }

        // state check
        CcaCommand e = (CcaCommand) other;
        return index.equals(e.index)
                && editPersonDescriptor.equals(e.editPersonDescriptor)
                && Objects.equals(personToEdit, e.personToEdit);
    }

```
###### \java\seedu\address\logic\commands\DeleteInjuriesHistoryCommand.java
``` java

/**
 * Edits the details of an existing person in the address book.
 */
public class DeleteInjuriesHistoryCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "deleteinjuries";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Delete injuries history from the "
            + "student that you want. "
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_INJURIES_HISTORY + "INJURIES_HISTORY...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_INJURIES_HISTORY + "Torn Ligament" + "\n";

    public static final String MESSAGE_REMARK_PERSON_SUCCESS = "Injuries Deleted: %1$s\nPerson: %2$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;

    private Person personToEdit;
    private Person editedPerson;

    /**
     * @param index of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public DeleteInjuriesHistoryCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = editPersonDescriptor;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException, IOException {
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
        return new CommandResult(String.format(MESSAGE_REMARK_PERSON_SUCCESS, editPersonDescriptor.getInjuriesHistory()
                        .get(), personToEdit.getName()));
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
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor)
            throws CommandException {
        assert personToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Nric updatedNric = editPersonDescriptor.getNric().orElse(personToEdit.getNric());
        Set<Tag> updatedTags = editPersonDescriptor.getTags().orElse(personToEdit.getTags());
        Set<Subject> updatedSubjects = editPersonDescriptor.getSubjects().orElse(personToEdit.getSubjects());
        Remark updatedRemark = editPersonDescriptor.getRemark().orElse((personToEdit.getRemark()));
        Cca updatedCca = editPersonDescriptor.getCca().orElse(personToEdit.getCca());
        String[] injuriesHistoryArray = personToEdit.getInjuriesHistory().toString().split("\n");
        String updateInjuriesHistory = "";
        NextOfKin updatedNextOfKin = editPersonDescriptor.getNextOfKin().orElse(personToEdit.getNextOfKin());
        boolean injuriesHistoryIsFound = false;
        for (String injuriesHistory : injuriesHistoryArray) {
            if (!injuriesHistory.contains(editPersonDescriptor.getInjuriesHistory().get().toString())) {
                updateInjuriesHistory = updateInjuriesHistory + injuriesHistory + "\n";
            } else {
                editPersonDescriptor.setInjuriesHistory(parseInjuriesHistory(injuriesHistory));
                injuriesHistoryIsFound = true;
            }
        }
        if (injuriesHistoryIsFound) {
            InjuriesHistory updatedInjuriesHistory = parseInjuriesHistory(updateInjuriesHistory);
            return new Person(updatedName, updatedNric, updatedTags, updatedSubjects, updatedRemark, updatedCca,
                    updatedInjuriesHistory, updatedNextOfKin);
        } else {
            throw new CommandException("The target injuriesHistory cannot be missing.");
        }
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteInjuriesHistoryCommand)) {
            return false;
        }

        // state check
        DeleteInjuriesHistoryCommand e = (DeleteInjuriesHistoryCommand) other;
        return index.equals(e.index)
                && editPersonDescriptor.equals(e.editPersonDescriptor)
                && Objects.equals(personToEdit, e.personToEdit);
    }
```
###### \java\seedu\address\logic\commands\DeleteRemarkCommand.java
``` java
/**
 * Edits the details of an existing person in the address book.
 */
public class DeleteRemarkCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "deleteremark";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": delete remarks from the student that you want. "
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_REMARK + "REMARKS...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_REMARK + "Need help" + "\n";

    public static final String MESSAGE_REMARK_PERSON_SUCCESS = "Remark deleted: %1$s\nPerson: %2$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;

    private Person personToEdit;
    private Person editedPerson;

    /**
     * @param index of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public DeleteRemarkCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException, IOException {
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
        return new CommandResult(String.format(MESSAGE_REMARK_PERSON_SUCCESS, editPersonDescriptor.getRemark().get(),
                                                personToEdit.getName()));
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
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor)
        throws CommandException {
        assert personToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Nric updatedNric = editPersonDescriptor.getNric().orElse(personToEdit.getNric());
        Set<Tag> updatedTags = editPersonDescriptor.getTags().orElse(personToEdit.getTags());
        Set<Subject> updatedSubjects = editPersonDescriptor.getSubjects().orElse(personToEdit.getSubjects());
        String[] remarkArray = personToEdit.getRemark().toString().split("\n");
        String updateRemark = "";
        NextOfKin updatedNextOfKin = editPersonDescriptor.getNextOfKin().orElse(personToEdit.getNextOfKin());
        Cca updatedCca = editPersonDescriptor.getCca().orElse(personToEdit.getCca());
        InjuriesHistory updatedInjuriesHistory = editPersonDescriptor.getInjuriesHistory()
                .orElse(personToEdit.getInjuriesHistory());
        boolean remarkIsFound = false;
        for (String remark: remarkArray) {
            if (!remark.contains(editPersonDescriptor.getRemark().get().toString())) {
                updateRemark = updateRemark + remark + "\n";
            } else {
                editPersonDescriptor.setRemark(parseRemark(remark));
                remarkIsFound = true;
            }
        }
        if (remarkIsFound) {
            Remark updatedRemark = parseRemark(updateRemark);

            return new Person(updatedName, updatedNric, updatedTags, updatedSubjects, updatedRemark, updatedCca,
                    updatedInjuriesHistory, updatedNextOfKin);
        } else {
            throw new CommandException("The target remark cannot be missing.");
        }
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteRemarkCommand)) {
            return false;
        }

        // state check
        DeleteRemarkCommand e = (DeleteRemarkCommand) other;
        return index.equals(e.index)
                && editPersonDescriptor.equals(e.editPersonDescriptor)
                && Objects.equals(personToEdit, e.personToEdit);
    }
```
###### \java\seedu\address\logic\commands\NextOfKinCommand.java
``` java

public class NextOfKinCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "nok";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds the Next of Kin details of the person identified"
            + " "
            + "by the index number used in the last person listing. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + PREFIX_REMARK + "RELATIONSHIP\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_NAME + "John "
            + PREFIX_PHONE + "96784213 "
            + PREFIX_REMARK + "Father";

    public static final String MESSAGE_ADD_NOK_SUCCESS = "Next of Kin: %1$s\nPerson: %2$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;

    private Person personToEdit;
    private Person editedPerson;

    /**
     * @param index of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public NextOfKinCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException, IOException {
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
        return new CommandResult(String.format(MESSAGE_ADD_NOK_SUCCESS, editedPerson.getNextOfKin(),
                                                editedPerson.getName()));
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
        Set<Subject> updatedSubjects = editPersonDescriptor.getSubjects().orElse(personToEdit.getSubjects());
        Remark updatedRemark = editPersonDescriptor.getRemark().orElse(personToEdit.getRemark());
        Cca updatedCca = editPersonDescriptor.getCca().orElse(personToEdit.getCca());
        InjuriesHistory updatedInjuriesHistory = editPersonDescriptor.getInjuriesHistory()
                .orElse(personToEdit.getInjuriesHistory());
        NextOfKin updatedNextOfKin = editPersonDescriptor.getNextOfKin().orElse(personToEdit.getNextOfKin());
        return new Person(updatedName, updatedNric, updatedTags, updatedSubjects, updatedRemark, updatedCca,
                            updatedInjuriesHistory, updatedNextOfKin);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof NextOfKinCommand)) {
            return false;
        }

        // state check
        NextOfKinCommand e = (NextOfKinCommand) other;
        return index.equals(e.index)
                && editPersonDescriptor.equals(e.editPersonDescriptor)
                && Objects.equals(personToEdit, e.personToEdit);
    }
```
###### \java\seedu\address\logic\commands\TagReplaceCommand.java
``` java
/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 *
 *
 */
public class TagReplaceCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "tagreplace";
    public static final String COMMAND_ALIAS = "tr";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Replace specified tag from everyone in the "
            + "address book with the intended tag." + "Parameters: Tag \n"
            + "Example: " + COMMAND_WORD + " " + PREFIX_TAG + "1A " + PREFIX_TAG + "2A\n"
            + "Example: " + COMMAND_ALIAS + " " + PREFIX_TAG + "1A " + PREFIX_TAG + "2A";

    public static final String MESSAGE_REPLACE_TAG_SUCCESS = "Replaced Tag: From %1$s to %2$s";

    private List<Tag> tagSet = new LinkedList<>();
    private Tag[] tagArray = new Tag[2];

    public TagReplaceCommand(List<Tag> tagSet) {
        requireNonNull(tagSet);
        this.tagSet = tagSet;
    }

    @Override
    public CommandResult executeUndoableCommand() {
        requireNonNull(tagSet);
        model.replaceTag(tagSet);
        return new CommandResult(String.format(MESSAGE_REPLACE_TAG_SUCCESS, tagArray[0], tagArray[1]));
    }

    @Override
    protected void preprocessUndoableCommand() throws CommandException {
        ReadOnlyAddressBook addressBook = model.getAddressBook();
        List<Tag> lastShownList = addressBook.getTagList();
        tagSet.toArray(tagArray);
        if (!lastShownList.contains(tagArray[0]) && !tagSet.isEmpty()) {
            throw new CommandException(Messages.MESSAGE_INVALID_TAG_ENTERED);
        }

    }
}
```
###### \java\seedu\address\logic\parser\AddInjuriesHistoryCommandParser.java
``` java

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class AddInjuriesHistoryCommandParser implements Parser<AddInjuriesHistoryCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddInjuriesHistoryCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_INJURIES_HISTORY);

        if (!argMultimap.arePrefixesPresent(PREFIX_INJURIES_HISTORY)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                                    AddInjuriesHistoryCommand.MESSAGE_USAGE));
        }
        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                                    AddInjuriesHistoryCommand.MESSAGE_USAGE));
        }

        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        if (ParserUtil.parseInjuriesHistory(argMultimap.getValue(PREFIX_INJURIES_HISTORY)).get().toString().isEmpty()) {
            throw new ParseException((String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                                        AddInjuriesHistoryCommand.MESSAGE_USAGE)));
        } else {
            ParserUtil.parseInjuriesHistory(argMultimap.getValue(PREFIX_INJURIES_HISTORY))
                    .ifPresent(editPersonDescriptor::setInjuriesHistory);
        }
        return new AddInjuriesHistoryCommand(index, editPersonDescriptor);
    }
```
###### \java\seedu\address\logic\parser\AddRemarkCommandParser.java
``` java
/**
 * Parses input arguments and creates a new EditCommand object
 */
public class AddRemarkCommandParser implements Parser<AddRemarkCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddRemarkCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_REMARK);

        if (!argMultimap.arePrefixesPresent(PREFIX_REMARK)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddRemarkCommand.MESSAGE_USAGE));
        }
        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddRemarkCommand.MESSAGE_USAGE));
        }

        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        if (ParserUtil.parseRemark(argMultimap.getValue(PREFIX_REMARK)).get().toString().isEmpty()) {
            throw new ParseException((String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddRemarkCommand.MESSAGE_USAGE)));
        } else {
            ParserUtil.parseRemark(argMultimap.getValue(PREFIX_REMARK)).ifPresent(editPersonDescriptor::setRemark);
        }
        return new AddRemarkCommand(index, editPersonDescriptor);
    }
```
###### \java\seedu\address\logic\parser\ArgumentMultimap.java
``` java
    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    public boolean arePrefixesPresent(Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> this.getValue(prefix).isPresent());
    }
```
###### \java\seedu\address\logic\parser\CcaCommandParser.java
``` java

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class CcaCommandParser implements Parser<CcaCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public CcaCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_CCA, PREFIX_CCA_POSITION);

        if (!argMultimap.arePrefixesPresent(PREFIX_CCA, PREFIX_CCA_POSITION)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, CcaCommand.MESSAGE_USAGE));
        }
        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, CcaCommand.MESSAGE_USAGE));
        }

        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        if (argMultimap.getValue(PREFIX_CCA).get().isEmpty()
                || argMultimap.getValue(PREFIX_CCA_POSITION).get().isEmpty()) {
            throw new ParseException((String.format(MESSAGE_INVALID_COMMAND_FORMAT, CcaCommand.MESSAGE_USAGE)));
        } else {
            ParserUtil.parseCca(argMultimap.getValue(PREFIX_CCA), argMultimap.getValue(PREFIX_CCA_POSITION))
                    .ifPresent(editPersonDescriptor::setCca);
        }
        return new CcaCommand(index, editPersonDescriptor);
    }
```
###### \java\seedu\address\logic\parser\DeleteInjuriesHistoryCommandParser.java
``` java

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class DeleteInjuriesHistoryCommandParser implements Parser<DeleteInjuriesHistoryCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteInjuriesHistoryCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_INJURIES_HISTORY);

        if (!argMultimap.arePrefixesPresent(PREFIX_INJURIES_HISTORY)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                                    DeleteInjuriesHistoryCommand.MESSAGE_USAGE));
        }
        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                                    DeleteInjuriesHistoryCommand.MESSAGE_USAGE));
        }

        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        if (ParserUtil.parseInjuriesHistory(argMultimap.getValue(PREFIX_INJURIES_HISTORY)).get().toString().isEmpty()) {
            throw new ParseException((String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                                                    DeleteInjuriesHistoryCommand.MESSAGE_USAGE)));
        } else {
            ParserUtil.parseInjuriesHistory(argMultimap.getValue(PREFIX_INJURIES_HISTORY))
                    .ifPresent(editPersonDescriptor::setInjuriesHistory);
        }
        return new DeleteInjuriesHistoryCommand(index, editPersonDescriptor);
    }
```
###### \java\seedu\address\logic\parser\DeleteRemarkCommandParser.java
``` java
/**
 * Parses input arguments and creates a new EditCommand object
 */
public class DeleteRemarkCommandParser implements Parser<DeleteRemarkCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteRemarkCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_REMARK);

        if (!argMultimap.arePrefixesPresent(PREFIX_REMARK)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteRemarkCommand.MESSAGE_USAGE));
        }
        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteRemarkCommand.MESSAGE_USAGE));
        }

        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        if (ParserUtil.parseRemark(argMultimap.getValue(PREFIX_REMARK)).get().toString().isEmpty()) {
            throw new ParseException((String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                                                    DeleteRemarkCommand.MESSAGE_USAGE)));
        } else {
            ParserUtil.parseRemark(argMultimap.getValue(PREFIX_REMARK)).ifPresent(editPersonDescriptor::setRemark);
        }
        return new DeleteRemarkCommand(index, editPersonDescriptor);
    }
```
###### \java\seedu\address\logic\parser\NextOfKinCommandParser.java
``` java
public class NextOfKinCommandParser implements Parser<NextOfKinCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditCommand
     * and returns an EditCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public NextOfKinCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_REMARK);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, NextOfKinCommand.MESSAGE_USAGE));
        }

        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        if (!(argMultimap.arePrefixesPresent(PREFIX_NAME, PREFIX_PHONE, PREFIX_REMARK))) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, NextOfKinCommand.MESSAGE_USAGE));
        }
        try {
            ParserUtil.parseNextOfKin(argMultimap.getValue(PREFIX_NAME),
                        argMultimap.getValue(PREFIX_PHONE),
                        argMultimap.getValue(PREFIX_EMAIL),
                        argMultimap.getValue(PREFIX_REMARK)).ifPresent(editPersonDescriptor::setNextOfKin);

        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }

        if (argMultimap.getValue(PREFIX_NAME).get().isEmpty() && argMultimap.getValue(PREFIX_PHONE).get().isEmpty()
                && argMultimap.getValue(PREFIX_NAME).get().isEmpty()) {
            throw new ParseException(NextOfKinCommand.MESSAGE_NOT_EDITED);
        }

        if (!isValidName(argMultimap.getValue(PREFIX_NAME).get())) {
            throw new ParseException(MESSAGE_NAME_CONSTRAINTS);
        }

        if (!isValidPhone(argMultimap.getValue(PREFIX_PHONE).get())) {
            throw new ParseException(MESSAGE_PHONE_CONSTRAINTS);
        }

        if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            if (!isValidEmail(argMultimap.getValue(PREFIX_EMAIL).get())) {
                throw new ParseException(MESSAGE_EMAIL_CONSTRAINTS);
            }
        }

        if (!isValidRemark(argMultimap.getValue(PREFIX_REMARK).get())) {
            throw new ParseException(MESSAGE_REMARK_CONSTRAINTS);
        }


        return new NextOfKinCommand(index, editPersonDescriptor);
    }
```
###### \java\seedu\address\logic\parser\ParserUtil.java
``` java
    /**
     * Parses a {@code String nric} into a {@code Nric}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws IllegalValueException if the given {@code nric} is invalid.
     */
    public static Nric parseNric(String nric) throws IllegalValueException {
        requireNonNull(nric);
        String trimmedNric = nric.trim();
        if (!Nric.isValidNric(trimmedNric)) {
            throw new IllegalValueException(Nric.MESSAGE_NRIC_CONSTRAINTS);
        }
        return new Nric(trimmedNric);
    }

    /**
     * Parses a {@code Optional<String> nric} into an {@code Optional<Nric>} if {@code nric} is present.
     * See header comment of this class regarding the use of {@code Optional} parameters.
     */
    public static Optional<Nric> parseNric(Optional<String> nric) throws IllegalValueException {
        requireNonNull(nric);
        return nric.isPresent() ? Optional.of(parseNric(nric.get())) : Optional.empty();
    }

```
###### \java\seedu\address\logic\parser\TagReplaceCommandParser.java
``` java
/**
 * Parses input arguments and creates a new FindCommand object
 */
public class TagReplaceCommandParser implements Parser<TagReplaceCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the TagReplaceCommand
     * and returns an TagReplaceCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public TagReplaceCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TAG);

        if (!arePrefixesPresent(argMultimap, PREFIX_TAG)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagReplaceCommand.MESSAGE_USAGE));
        }

        try {
            List<Tag> tagSet = ParserUtil.parseTagsForReplacement(argMultimap.getAllValues(PREFIX_TAG));
            return new TagReplaceCommand(tagSet);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagReplaceCommand.MESSAGE_USAGE));
        }
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
```
###### \java\seedu\address\model\AddressBook.java
``` java
    /**
     * Calls replaceTagForPerson method when tag is found in tags.
     * @param tagSet
     * @throws TagNotFoundException
     */
    public void replaceTag(List<Tag> tagSet) {
        Tag[] tagArray = new Tag[2];
        tagSet.toArray(tagArray);
        Tag tagToBeReplaced = tagArray[0];
        Tag tagToBePlaced = tagArray[1];
        if (tags.contains(tagToBeReplaced)) {
            for (Person person : persons) {
                replaceTagForPerson(tagToBeReplaced, tagToBePlaced, person);
            }
        } else {
        }
    }

```
###### \java\seedu\address\model\AddressBook.java
``` java
    /**
     * Removes a specific tag from an individual person and updates the person's information.
     * @param tagToBeReplaced
     * @param tagToBePlaced
     * @param person
     */
    public void replaceTagForPerson(Tag tagToBeReplaced, Tag tagToBePlaced, Person person) {
        Set<Tag> tagList = new HashSet<>(person.getTags());
        if (tagList.remove(tagToBeReplaced)) {
            tagList.add(tagToBePlaced);
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

    //// util methods

```
###### \java\seedu\address\model\Model.java
``` java
    /**
     * Replaces a specific tag for everyone in the address book.
     * @param tagSet
     */
    void replaceTag(List<Tag> tagSet);

}
```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public void replaceTag(List<Tag> tagSet) {
        Tag[] tagArray = new Tag[2];
        tagSet.toArray(tagArray);
        addressBook.replaceTag(tagSet);
        indicateAddressBookChanged();
    }

```
###### \java\seedu\address\model\person\Cca.java
``` java

/**
 * Represents a remarks of the person in the address book.
 */
public class Cca {

    public final String value;
    public final String pos;

    /**
     * Constructs a {@code Cca}.
     *
     * @param cca A valid cca.
     * @param pos A valid position.
     */
    public Cca(String cca, String pos) {
        requireNonNull(cca);
        this.value = cca;
        requireNonNull(pos);
        this.pos = pos;

    }

    @Override
    public String toString() {
        if (value != "" && pos != "") {
            return value + ", " + pos;
        } else {
            return "";
        }
    }

    public String getValue() {
        return value;
    }

    public String getPos() {
        return pos;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Cca // instanceof handles nulls
                && this.value.equals(((Cca) other).value)); // state check
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = 37 * hash + value.hashCode();
        hash = 37 * hash + pos.hashCode();
        return hash;
    }

}
```
###### \java\seedu\address\model\person\InjuriesHistory.java
``` java

/**
 * Represents a remarks of the person in the address book.
 */
public class InjuriesHistory {

    public final String value;

    /**
     * Constructs a {@code InjuriesHistory}.
     *
     * @param injurieshistory A valid injurieshistory.
     */
    public InjuriesHistory(String injurieshistory) {
        requireNonNull(injurieshistory);
        this.value = injurieshistory;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof InjuriesHistory // instanceof handles nulls
                && this.value.equals(((InjuriesHistory) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
```
###### \java\seedu\address\model\person\NextOfKin.java
``` java
public class NextOfKin {

    public static final String MESSAGE_PHONE_CONSTRAINTS =
            "Phone numbers can only contain numbers, and should be at least 3 digits long";

    public static final String PHONE_VALIDATION_REGEX = "\\d{3,}";

    public static final String[] REMARK_VALIDATION_REGEX = new String[] {"Father", "Mother", "Guardian"};

    private static  final String SPECIAL_CHARACTERS = "!#$%&'*+/=?`{|}~^.-";
    public static final String MESSAGE_EMAIL_CONSTRAINTS = "Person emails should be of the format local-part@domain "
            + "and adhere to the following constraints:\n"
            + "1. The local-part should only contain alphanumeric characters and these special characters, excluding "
            + "the parentheses, (" + SPECIAL_CHARACTERS + ") .\n"
            + "2. This is followed by a '@' and then a domain name. "
            + "The domain name must:\n"
            + "    - be at least 2 characters long\n"
            + "    - start and end with alphanumeric characters\n"
            + "    - consist of alphanumeric characters, a period or a hyphen for the characters in between, if any.";
    // alphanumeric and special characters

    private static final String LOCAL_PART_REGEX = "^[\\w" + SPECIAL_CHARACTERS + "]+";
    private static final String DOMAIN_FIRST_CHARACTER_REGEX = "[^\\W_]"; // alphanumeric characters except underscore
    private static final String DOMAIN_MIDDLE_REGEX = "[a-zA-Z0-9.-]*"; // alphanumeric, period and hyphen
    private static final String DOMAIN_LAST_CHARACTER_REGEX = "[^\\W_]$";
    public static final String EMAIL_VALIDATION_REGEX = LOCAL_PART_REGEX + "@"
            + DOMAIN_FIRST_CHARACTER_REGEX + DOMAIN_MIDDLE_REGEX + DOMAIN_LAST_CHARACTER_REGEX;

    public static final String MESSAGE_REMARK_CONSTRAINTS =
            "Remarks for the Next of Kin should be one of the following: "
                    + (Arrays.deepToString(REMARK_VALIDATION_REGEX) + ".");


    public final String fullName;
    public final String phone;
    public final String email;
    public final String remark;

    /**
     * Constructs a {@code Name}.
     *
     * @param name A valid name.
     */



    public NextOfKin(String name, String phone, String email, String remark) {
        requireNonNull(name);
        this.fullName = name;

        requireNonNull(email);
        this.email = email;

        requireNonNull(phone);
        this.phone = phone;

        requireNonNull(remark);
        this.remark = remark;
    }

    /**
     * Returns true if a given string is a valid person name.
     */
    public static boolean isValidName(String test) {
        return test.matches(NAME_VALIDATION_REGEX);
    }

    /**
     * Returns true if a given string is a valid email.
     */
    public static boolean isValidEmail(String test) {
        return test.matches(EMAIL_VALIDATION_REGEX);
    }

    /**
     * Returns true if a given string is a valid phone number.
     */
    public static boolean isValidPhone(String test) {
        return test.matches(PHONE_VALIDATION_REGEX);
    }


    /**
     * Returns true if a given string is a valid remark.
     */
    public static boolean isValidRemark(String test) {
        for (String name : REMARK_VALIDATION_REGEX) {
            if (name.equals(test)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return (fullName + " Phone: " + phone + " Email: " + email + " Remark: " + remark);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof NextOfKin // instanceof handles nulls
                && this.fullName.equals(((NextOfKin) other).fullName)); // state check
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }
```
###### \java\seedu\address\model\person\Nric.java
``` java
/**
 * Represents a Person's nric number in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidNric(String)}
 */
public class Nric {


    public static final String MESSAGE_NRIC_CONSTRAINTS =
            "Nric should start with a S, should be at 7 digits long"
                    + " and should end with a capital letter.";
    public static final String NRIC_VALIDATION_REGEX = "^[STFG]\\d{7}[A-Z]$";
    public final String value;

    /**
     * Constructs a {@code Nric}.
     *
     * @param nric A valid nric number.
     */
    public Nric(String nric) {
        requireNonNull(nric);
        checkArgument(isValidNric(nric), MESSAGE_NRIC_CONSTRAINTS);
        this.value = nric;
    }

    /**
     * Returns true if a given string is a valid person nric number.
     */
    public static boolean isValidNric(String test) {
        return test.matches(NRIC_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Nric // instanceof handles nulls
                && this.value.equals(((Nric) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
```
###### \java\seedu\address\model\person\Remark.java
``` java
/**
 * Represents a remarks of the person in the address book.
 */
public class Remark {


    public static final String MESSAGE_REMARK_CONSTRAINTS = "Remark can contain anything that you want.";
    public final String value;

    /**
     * Constructs a {@code Remark}.
     *
     * @param remark A valid remark.
     */
    public Remark(String remark) {
        requireNonNull(remark);
        this.value = remark;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Remark // instanceof handles nulls
                && this.value.equals(((Remark) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
```
