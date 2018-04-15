# chuakunhong
###### \java\seedu\address\logic\commands\AddInjuriesCommandTest.java
``` java

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for EditCommand.
 */
public class AddInjuriesCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() throws Exception {
        Person editedPerson = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedPerson).build();
        AddInjuriesHistoryCommand addInjuriesHistoryCommand = prepareCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(addInjuriesHistoryCommand.MESSAGE_REMARK_PERSON_SUCCESS,
                                                editedPerson.getInjuriesHistory(), editedPerson.getName());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updatePerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(addInjuriesHistoryCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() throws Exception {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredPersonList().size());
        Person lastPerson = model.getFilteredPersonList().get(indexLastPerson.getZeroBased());

        PersonBuilder personInList = new PersonBuilder(lastPerson);
        Person editedPerson = personInList.withName(GEORGE.getName().toString()).withNric(VALID_NRIC_BOB)
                .withTags(VALID_TAG_HUSBAND).withInjuriesHistory(VALID_INJURIES_HISTORY)
                .build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(GEORGE.getName().toString())
                .withNric(VALID_NRIC_BOB).withTags(VALID_TAG_HUSBAND).withInjuriesHistory("").build();
        AddInjuriesHistoryCommand addInjuriesHistoryCommand = prepareCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(AddInjuriesHistoryCommand.MESSAGE_REMARK_PERSON_SUCCESS,
                descriptor.getInjuriesHistory().get(), editedPerson.getName());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updatePerson(lastPerson, editedPerson);

        assertCommandSuccess(addInjuriesHistoryCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() throws IOException {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(firstPerson).build();
        AddInjuriesHistoryCommand addInjuriesHistoryCommand = prepareCommand(INDEX_SECOND_PERSON, descriptor);

        assertCommandFailure(addInjuriesHistoryCommand, model, addInjuriesHistoryCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() throws IOException {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build();
        AddInjuriesHistoryCommand addInjuriesHistoryCommand = prepareCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(addInjuriesHistoryCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() throws Exception {
        final AddInjuriesHistoryCommand standardCommand = prepareCommand(INDEX_FIRST_PERSON, DESC_AMY);

        // same values -> returns true
        EditPersonDescriptor copyDescriptor = new EditPersonDescriptor(DESC_AMY);
        AddInjuriesHistoryCommand commandWithSameValues = prepareCommand(INDEX_FIRST_PERSON, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // one command preprocessed when previously equal -> returns false
        commandWithSameValues.preprocessUndoableCommand();
        assertFalse(standardCommand.equals(commandWithSameValues));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new AddInjuriesHistoryCommand(INDEX_SECOND_PERSON, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new AddInjuriesHistoryCommand(INDEX_FIRST_PERSON, DESC_BOB)));
    }

    /**
     * Returns an {@code AddInjuriesHistoryCommand} with parameters {@code index} and {@code descriptor}
     */
    private AddInjuriesHistoryCommand prepareCommand(Index index, EditPersonDescriptor descriptor) {
        AddInjuriesHistoryCommand addInjuriesHistoryCommand = new AddInjuriesHistoryCommand(index, descriptor);
        addInjuriesHistoryCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return addInjuriesHistoryCommand;
    }

    /**
     * A utility class to help with building EditPersonDescriptor objects.
     */
    public class EditPersonDescriptorBuilder {

        private EditPersonDescriptor descriptor;

        public EditPersonDescriptorBuilder() {
            descriptor = new EditPersonDescriptor();
        }

        public EditPersonDescriptorBuilder(EditPersonDescriptor descriptor) {
            this.descriptor = new EditPersonDescriptor();
        }

        /**
         * Returns an {@code EditPersonDescriptor} with fields containing {@code person}'s details
         */
        public EditPersonDescriptorBuilder(Person person) {
            descriptor = new EditPersonDescriptor();
            descriptor.setName(person.getName());
            descriptor.setNric(person.getNric());
            descriptor.setTags(person.getTags());
            descriptor.setSubjects(person.getSubjects());
            descriptor.setRemark(person.getRemark());
            descriptor.setInjuriesHistory(person.getInjuriesHistory());
        }

        /**
         * Sets the {@code Name} of the {@code EditPersonDescriptor} that we are building.
         */
        public EditPersonDescriptorBuilder withName(String name) {
            descriptor.setName(new Name(name));
            return this;
        }

        /**
         * Sets the {@code Nric} of the {@code EditPersonDescriptor} that we are building.
         */
        public EditPersonDescriptorBuilder withNric(String nric) {
            descriptor.setNric(new Nric(nric));
            return this;
        }

        /**
         * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code EditPersonDescriptor}
         * that we are building.
         */
        public EditPersonDescriptorBuilder withTags(String... tags) {
            Set<Tag> tagSet = Stream.of(tags).map(Tag::new).collect(Collectors.toSet());
            descriptor.setTags(tagSet);
            return this;
        }

        /**
         * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code EditPersonDescriptor}
         * that we are building.
         */
        public EditPersonDescriptorBuilder withSubjects(String... subjects) {
            Set<Subject> subjectSet = Stream.of(subjects).map(Subject::new).collect(Collectors.toSet());
            descriptor.setSubjects(subjectSet);
            return this;
        }

        /**
         * Sets the {@code Remark} of the {@code EditPersonDescriptor} that we are building.
         */
        public EditPersonDescriptorBuilder withRemark(String remark) {
            descriptor.setRemark(new Remark(remark));
            return this;
        }

        /**
         * Sets the {@code InjuriesHistory} of the {@code EditPersonDescriptor} that we are building.
         */
        public EditPersonDescriptorBuilder withInjuriesHistory(String injuriesHistory) {
            descriptor.setInjuriesHistory(new InjuriesHistory(injuriesHistory));
            return this;
        }

        public EditPersonDescriptor build() {
            return descriptor;
        }
    }
```
###### \java\seedu\address\logic\commands\AddRemarkCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for EditCommand.
 */
public class AddRemarkCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() throws Exception {
        Person editedPerson = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedPerson).build();
        AddRemarkCommand addRemarkCommand = prepareCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(addRemarkCommand.MESSAGE_REMARK_PERSON_SUCCESS, editedPerson.getRemark(),
                                                editedPerson.getName());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updatePerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(addRemarkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() throws Exception {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredPersonList().size());
        Person lastPerson = model.getFilteredPersonList().get(indexLastPerson.getZeroBased());

        PersonBuilder personInList = new PersonBuilder(lastPerson);
        Person editedPerson = personInList.withName(GEORGE.getName().toString()).withNric(VALID_NRIC_BOB)
                .withTags(VALID_TAG_HUSBAND).withRemark(VALID_REMARK).build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(GEORGE.getName().toString())
                .withNric(VALID_NRIC_BOB).withTags(VALID_TAG_HUSBAND).withRemark("").build();
        AddRemarkCommand addRemarkCommand = prepareCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(AddRemarkCommand.MESSAGE_REMARK_PERSON_SUCCESS,
                descriptor.getRemark().get(), editedPerson.getName());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updatePerson(lastPerson, editedPerson);

        assertCommandSuccess(addRemarkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() throws IOException {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(firstPerson).build();
        AddRemarkCommand addRemarkCommand = prepareCommand(INDEX_SECOND_PERSON, descriptor);

        assertCommandFailure(addRemarkCommand, model, addRemarkCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() throws IOException {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build();
        AddRemarkCommand addRemarkCommand = prepareCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(addRemarkCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() throws Exception {
        final AddRemarkCommand standardCommand = prepareCommand(INDEX_FIRST_PERSON, DESC_AMY);

        // same values -> returns true
        EditPersonDescriptor copyDescriptor = new EditPersonDescriptor(DESC_AMY);
        AddRemarkCommand commandWithSameValues = prepareCommand(INDEX_FIRST_PERSON, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // one command preprocessed when previously equal -> returns false
        commandWithSameValues.preprocessUndoableCommand();
        assertFalse(standardCommand.equals(commandWithSameValues));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new AddRemarkCommand(INDEX_SECOND_PERSON, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new AddRemarkCommand(INDEX_FIRST_PERSON, DESC_BOB)));
    }

    /**
     * Returns an {@code EditCommand} with parameters {@code index} and {@code descriptor}
     */
    private AddRemarkCommand prepareCommand(Index index, EditPersonDescriptor descriptor) {
        AddRemarkCommand addRemarkCommand = new AddRemarkCommand(index, descriptor);
        addRemarkCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return addRemarkCommand;
    }

    /**
     * A utility class to help with building EditPersonDescriptor objects.
     */
    public class EditPersonDescriptorBuilder {

        private EditPersonDescriptor descriptor;

        public EditPersonDescriptorBuilder() {
            descriptor = new seedu.address.logic.commands.EditPersonDescriptor();
        }

        public EditPersonDescriptorBuilder(EditPersonDescriptor descriptor) {
            this.descriptor = new seedu.address.logic.commands.EditPersonDescriptor();
        }

        /**
         * Returns an {@code EditPersonDescriptor} with fields containing {@code person}'s details
         */
        public EditPersonDescriptorBuilder(Person person) {
            descriptor = new seedu.address.logic.commands.EditPersonDescriptor();
            descriptor.setName(person.getName());
            descriptor.setNric(person.getNric());
            descriptor.setTags(person.getTags());
            descriptor.setSubjects(person.getSubjects());
            descriptor.setRemark(person.getRemark());
        }

        /**
         * Sets the {@code Name} of the {@code EditPersonDescriptor} that we are building.
         */
        public EditPersonDescriptorBuilder withName(String name) {
            descriptor.setName(new Name(name));
            return this;
        }

        /**
         * Sets the {@code Nric} of the {@code EditPersonDescriptor} that we are building.
         */
        public EditPersonDescriptorBuilder withNric(String nric) {
            descriptor.setNric(new Nric(nric));
            return this;
        }

        /**
         * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code EditPersonDescriptor}
         * that we are building.
         */
        public EditPersonDescriptorBuilder withTags(String... tags) {
            Set<Tag> tagSet = Stream.of(tags).map(Tag::new).collect(Collectors.toSet());
            descriptor.setTags(tagSet);
            return this;
        }

        /**
         * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code EditPersonDescriptor}
         * that we are building.
         */
        public EditPersonDescriptorBuilder withSubjects(String... subjects) {
            Set<Subject> subjectSet = Stream.of(subjects).map(Subject::new).collect(Collectors.toSet());
            descriptor.setSubjects(subjectSet);
            return this;
        }

        /**
         * Sets the {@code Remark} of the {@code EditPersonDescriptor} that we are building.
         */
        public EditPersonDescriptorBuilder withRemark(String remark) {
            descriptor.setRemark(new Remark(remark));
            return this;
        }

        public EditPersonDescriptor build() {
            return descriptor;
        }
    }
```
###### \java\seedu\address\logic\commands\DeleteInjuriesCommandTest.java
``` java

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * DeleteRemarkCommand.
 */
public class DeleteInjuriesCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() throws Exception {
        Person editedPerson = new PersonBuilder().withInjuriesHistory(VALID_INJURIES_HISTORY).build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedPerson).build();
        DeleteInjuriesHistoryCommand deleteInjuriesHistoryCommand = prepareCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(deleteInjuriesHistoryCommand.MESSAGE_REMARK_PERSON_SUCCESS,
                                                editedPerson.getInjuriesHistory(), editedPerson.getName());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updatePerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(deleteInjuriesHistoryCommand, model, expectedMessage, expectedModel);
    }



    @Test


    public void execute_someFieldsSpecifiedUnfilteredList_success() throws Exception {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredPersonList().size());
        Person lastPerson = model.getFilteredPersonList().get(indexLastPerson.getZeroBased());

        PersonBuilder personInList = new PersonBuilder(lastPerson);
        Person editedPerson = personInList.withName(GEORGE.getName().toString()).withNric(VALID_NRIC_BOB)
                .withTags(VALID_TAG_HUSBAND).withInjuriesHistory(VALID_INJURIES_HISTORY).build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(GEORGE.getName().toString())
                .withNric(VALID_NRIC_BOB).withTags(VALID_TAG_HUSBAND).withInjuriesHistory(" ").build();
        DeleteInjuriesHistoryCommand deleteInjuriesCommand = prepareCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(DeleteInjuriesHistoryCommand.MESSAGE_REMARK_PERSON_SUCCESS,
                editedPerson.getInjuriesHistory(), editedPerson.getName());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updatePerson(lastPerson, editedPerson);

        assertCommandSuccess(deleteInjuriesCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() throws IOException {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(firstPerson).build();
        DeleteInjuriesHistoryCommand deleteInjuriesHistoryCommand = prepareCommand(INDEX_SECOND_PERSON, descriptor);

        assertCommandFailure(deleteInjuriesHistoryCommand, model,
                deleteInjuriesHistoryCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() throws IOException {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build();
        DeleteInjuriesHistoryCommand deleteInjuriesHistoryCommand = prepareCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(deleteInjuriesHistoryCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() throws Exception {
        final DeleteInjuriesHistoryCommand standardCommand = prepareCommand(INDEX_FIRST_PERSON, DESC_AMY);

        // same values -> returns true
        EditPersonDescriptor copyDescriptor = new EditPersonDescriptor(DESC_AMY);
        DeleteInjuriesHistoryCommand commandWithSameValues = prepareCommand(INDEX_FIRST_PERSON, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

    }

    /**
     * Returns an {@code DeleteRemarkCommand} with parameters {@code index} and {@code descriptor}
     */
    private DeleteInjuriesHistoryCommand prepareCommand(Index index, EditPersonDescriptor descriptor) {
        DeleteInjuriesHistoryCommand deleteInjuriesHistoryCommand =
                new DeleteInjuriesHistoryCommand(index, descriptor);
        deleteInjuriesHistoryCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return deleteInjuriesHistoryCommand;
    }

    /**
     * A utility class to help with building EditPersonDescriptor objects.
     */
    public class EditPersonDescriptorBuilder {

        private EditPersonDescriptor descriptor;

        public EditPersonDescriptorBuilder() {
            descriptor = new EditPersonDescriptor();
        }

        public EditPersonDescriptorBuilder(EditPersonDescriptor descriptor) {
            this.descriptor = new EditPersonDescriptor();
        }

        /**
         * Returns an {@code EditPersonDescriptor} with fields containing {@code person}'s details
         */
        public EditPersonDescriptorBuilder(Person person) {
            descriptor = new EditPersonDescriptor();
            descriptor.setName(person.getName());
            descriptor.setNric(person.getNric());
            descriptor.setTags(person.getTags());
            descriptor.setSubjects(person.getSubjects());
            descriptor.setRemark(person.getRemark());
            descriptor.setInjuriesHistory(person.getInjuriesHistory());
        }

        /**
         * Sets the {@code Name} of the {@code EditPersonDescriptor} that we are building.
         */
        public EditPersonDescriptorBuilder withName(String name) {
            descriptor.setName(new Name(name));
            return this;
        }

        /**
         * Sets the {@code Nric} of the {@code EditPersonDescriptor} that we are building.
         */
        public EditPersonDescriptorBuilder withNric(String nric) {
            descriptor.setNric(new Nric(nric));
            return this;
        }

        /**
         * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code EditPersonDescriptor}
         * that we are building.
         */
        public EditPersonDescriptorBuilder withTags(String... tags) {
            Set<Tag> tagSet = Stream.of(tags).map(Tag::new).collect(Collectors.toSet());
            descriptor.setTags(tagSet);
            return this;
        }

        /**
         * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code EditPersonDescriptor}
         * that we are building.
         */
        public EditPersonDescriptorBuilder withSubjects(String... subjects) {
            Set<Subject> subjectSet = Stream.of(subjects).map(Subject::new).collect(Collectors.toSet());
            descriptor.setSubjects(subjectSet);
            return this;
        }

        /**
         * Sets the {@code Remark} of the {@code EditPersonDescriptor} that we are building.
         */
        public EditPersonDescriptorBuilder withRemark(String remark) {
            descriptor.setRemark(new Remark(remark));
            return this;
        }

        /**
         * Sets the {@code InjuriesHistory} of the {@code EditPersonDescriptor} that we are building.
         */
        public EditPersonDescriptorBuilder withInjuriesHistory(String injuriesHistory) {
            descriptor.setInjuriesHistory(new InjuriesHistory(injuriesHistory));
            return this;
        }

        public EditPersonDescriptor build() {
            return descriptor;
        }
    }
```
###### \java\seedu\address\logic\commands\DeleteRemarkCommandTest.java
``` java

/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests for
 * DeleteRemarkCommand.
 */
public class DeleteRemarkCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() throws Exception {
        Person editedPerson = new PersonBuilder().withRemark(VALID_REMARK).build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedPerson).build();
        DeleteRemarkCommand deleteRemarkCommand = prepareCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(deleteRemarkCommand.MESSAGE_REMARK_PERSON_SUCCESS,
                                                editedPerson.getRemark(), editedPerson.getName());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updatePerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(deleteRemarkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() throws Exception {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredPersonList().size());
        Person lastPerson = model.getFilteredPersonList().get(indexLastPerson.getZeroBased());

        PersonBuilder personInList = new PersonBuilder(lastPerson);
        Person editedPerson = personInList.withName(GEORGE.getName().toString()).withNric(VALID_NRIC_BOB)
                .withTags(VALID_TAG_HUSBAND).withRemark(VALID_REMARK).build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(GEORGE.getName().toString())
                .withNric(VALID_NRIC_BOB).withTags(VALID_TAG_HUSBAND).withRemark("").build();
        DeleteRemarkCommand deleteRemarkCommand = prepareCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(DeleteRemarkCommand.MESSAGE_REMARK_PERSON_SUCCESS,
                editedPerson.getRemark(), editedPerson.getName());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updatePerson(lastPerson, editedPerson);

        assertCommandSuccess(deleteRemarkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() throws IOException {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(firstPerson).build();
        DeleteRemarkCommand deleteRemarkCommand = prepareCommand(INDEX_SECOND_PERSON, descriptor);

        assertCommandFailure(deleteRemarkCommand, model, deleteRemarkCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() throws IOException {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build();
        DeleteRemarkCommand deleteRemarkCommand = prepareCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(deleteRemarkCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() throws Exception {
        final DeleteRemarkCommand standardCommand = prepareCommand(INDEX_FIRST_PERSON, DESC_AMY);

        // same values -> returns true
        EditPersonDescriptor copyDescriptor = new EditPersonDescriptor(DESC_AMY);
        DeleteRemarkCommand commandWithSameValues = prepareCommand(INDEX_FIRST_PERSON, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

    }

    /**
     * Returns an {@code DeleteRemarkCommand} with parameters {@code index} and {@code descriptor}
     */
    private DeleteRemarkCommand prepareCommand(Index index, EditPersonDescriptor descriptor) {
        DeleteRemarkCommand deleteRemarkCommand = new DeleteRemarkCommand(index, descriptor);
        deleteRemarkCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return deleteRemarkCommand;
    }

    /**
     * A utility class to help with building EditPersonDescriptor objects.
     */
    public class EditPersonDescriptorBuilder {

        private EditPersonDescriptor descriptor;

        public EditPersonDescriptorBuilder() {
            descriptor = new EditPersonDescriptor();
        }

        public EditPersonDescriptorBuilder(EditPersonDescriptor descriptor) {
            this.descriptor = new EditPersonDescriptor();
        }

        /**
         * Returns an {@code EditPersonDescriptor} with fields containing {@code person}'s details
         */
        public EditPersonDescriptorBuilder(Person person) {
            descriptor = new EditPersonDescriptor();
            descriptor.setName(person.getName());
            descriptor.setNric(person.getNric());
            descriptor.setTags(person.getTags());
            descriptor.setSubjects(person.getSubjects());
            descriptor.setRemark(person.getRemark());
        }

        /**
         * Sets the {@code Name} of the {@code EditPersonDescriptor} that we are building.
         */
        public EditPersonDescriptorBuilder withName(String name) {
            descriptor.setName(new Name(name));
            return this;
        }

        /**
         * Sets the {@code Nric} of the {@code EditPersonDescriptor} that we are building.
         */
        public EditPersonDescriptorBuilder withNric(String nric) {
            descriptor.setNric(new Nric(nric));
            return this;
        }

        /**
         * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code EditPersonDescriptor}
         * that we are building.
         */
        public EditPersonDescriptorBuilder withTags(String... tags) {
            Set<Tag> tagSet = Stream.of(tags).map(Tag::new).collect(Collectors.toSet());
            descriptor.setTags(tagSet);
            return this;
        }

        /**
         * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code EditPersonDescriptor}
         * that we are building.
         */
        public EditPersonDescriptorBuilder withSubjects(String... subjects) {
            Set<Subject> subjectSet = Stream.of(subjects).map(Subject::new).collect(Collectors.toSet());
            descriptor.setSubjects(subjectSet);
            return this;
        }

        /**
         * Sets the {@code Remark} of the {@code EditPersonDescriptor} that we are building.
         */
        public EditPersonDescriptorBuilder withRemark(String remark) {
            descriptor.setRemark(new Remark(remark));
            return this;
        }

        public EditPersonDescriptor build() {
            return descriptor;
        }
    }
```
###### \java\seedu\address\logic\commands\TagReplaceCommandTest.java
``` java
public class TagReplaceCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Tag tagToDelete = new Tag("removeTag");
    private Tag tagToBePlace = new Tag("replaceTag");

    @Test
    public void execute_validTagToReplaceEntered_success() throws Exception {
        List<Tag> tagList = model.getAddressBook().getTagList();
        TagReplaceCommand tagReplaceCommand = prepareCommand(tagList);

        String expectedMessage = String.format(TagReplaceCommand.MESSAGE_REPLACE_TAG_SUCCESS, tagList.get(0),
                                                tagList.get(1));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.replaceTag(tagList);

        assertCommandSuccess(tagReplaceCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidTagToRemoveEntered_throwsCommandException() throws IOException {
        List<Tag> tagList = new ArrayList<>();
        tagList.add(tagToDelete);
        tagList.add(tagToBePlace);
        TagReplaceCommand tagReplaceCommand = prepareCommand(tagList);
        assertCommandFailure(tagReplaceCommand, model, Messages.MESSAGE_INVALID_TAG_ENTERED);
    }

    @Test
    public void executeUndoRedo_invalidTagToReplaceEntered_failure() throws IOException {
        List<Tag> tagList = new ArrayList<>();
        tagList.add(tagToDelete);
        tagList.add(tagToBePlace);
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        TagReplaceCommand tagReplaceCommand = prepareCommand(tagList);

        // execution failed -> replaceCommand not pushed into undoRedoStack
        assertCommandFailure(tagReplaceCommand, model, Messages.MESSAGE_INVALID_TAG_ENTERED);

        // no commands in undoRedoStack -> undoCommand and redoCommand fail
        assertCommandFailure(undoCommand, model, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(redoCommand, model, RedoCommand.MESSAGE_FAILURE);
    }

    /**
     * Returns a {@code DeleteCommand} with the parameter {@code index}.
     */
    private TagReplaceCommand prepareCommand(List<Tag> tagList) {
        TagReplaceCommand tagReplaceCommand = new TagReplaceCommand(tagList);
        tagReplaceCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return tagReplaceCommand;
    }
```
###### \java\seedu\address\logic\parser\AddInjuriesCommandParserTest.java
``` java
public class AddInjuriesCommandParserTest {


    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddInjuriesHistoryCommand.MESSAGE_USAGE);

    private AddInjuriesHistoryCommandParser parser = new AddInjuriesHistoryCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, INJURIES_HISTORY_DESC, MESSAGE_INVALID_FORMAT);

        // empty remark
        assertParseFailure(parser, "1" + INVALID_INJURIES_HISTORY_DESC, MESSAGE_INVALID_FORMAT);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + INJURIES_HISTORY_DESC, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + INJURIES_HISTORY_DESC, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + INJURIES_HISTORY_DESC;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withRemark(INJURIES_HISTORY_DESC).build();
        AddInjuriesHistoryCommand expectedCommand = new AddInjuriesHistoryCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
```
###### \java\seedu\address\logic\parser\AddRemarkCommandParserTest.java
``` java
public class AddRemarkCommandParserTest {


    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddRemarkCommand.MESSAGE_USAGE);

    private AddRemarkCommandParser parser = new AddRemarkCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, REMARK_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // empty remark
        assertParseFailure(parser, "1" + INVALID_REMARK_DESC, MESSAGE_INVALID_FORMAT);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + REMARK_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + REMARK_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + REMARK_DESC_BOB;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withRemark(REMARK_DESC_BOB).build();
        AddRemarkCommand expectedCommand = new AddRemarkCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
```
###### \java\seedu\address\logic\parser\AddressBookParserTest.java
``` java
    @Test
    public void parseCommand_addAlias() throws Exception {
        Person person = new PersonBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(AddCommand.COMMAND_ALIAS + " "
                + PersonUtil.getPersonDetails(person));
        assertEquals(new AddCommand(person), command);
    }

```
###### \java\seedu\address\logic\parser\AddressBookParserTest.java
``` java
    @Test
    public void parseCommand_deleteAlias() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_ALIAS + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_PERSON), command);
    }

```
###### \java\seedu\address\logic\parser\AddressBookParserTest.java
``` java
    @Test
    public void parseCommand_editAlias() throws Exception {
        Person person = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_ALIAS + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + PersonUtil.getPersonDetails(person));
        //assertEquals(new EditCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

```
###### \java\seedu\address\logic\parser\AddressBookParserTest.java
``` java
    @Test
    public void parseCommand_findAlias() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_ALIAS + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

```
###### \java\seedu\address\logic\parser\AddressBookParserTest.java
``` java
    @Test
    public void parseCommand_historyAlias() throws Exception {
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_ALIAS) instanceof HistoryCommand);
        assertTrue(parser.parseCommand(HistoryCommand.COMMAND_ALIAS + " 3") instanceof HistoryCommand);

        try {
            parser.parseCommand("histories");
            fail("The expected ParseException was not thrown.");
        } catch (ParseException pe) {
            assertEquals(MESSAGE_UNKNOWN_COMMAND, pe.getMessage());
        }
    }

```
###### \java\seedu\address\logic\parser\AddressBookParserTest.java
``` java
    @Test
    public void parseCommand_listAlias() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_ALIAS) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_ALIAS + " 3") instanceof ListCommand);
    }

```
###### \java\seedu\address\logic\parser\AddressBookParserTest.java
``` java
    @Test
    public void parseCommand_selectAlias() throws Exception {
        SelectCommand command = (SelectCommand) parser.parseCommand(
                SelectCommand.COMMAND_ALIAS + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new SelectCommand(INDEX_FIRST_PERSON), command);
    }

```
###### \java\seedu\address\logic\parser\AddressBookParserTest.java
``` java
    @Test
    public void parseCommand_redoCommandAlias_returnsRedoCommand() throws Exception {
        assertTrue(parser.parseCommand(RedoCommand.COMMAND_ALIAS) instanceof RedoCommand);
        assertTrue(parser.parseCommand("redo 1") instanceof RedoCommand);
    }

```
###### \java\seedu\address\logic\parser\AddressBookParserTest.java
``` java
    @Test
    public void parseCommand_undoCommandAlias_returnsUndoCommand() throws Exception {
        assertTrue(parser.parseCommand(UndoCommand.COMMAND_ALIAS) instanceof UndoCommand);
        assertTrue(parser.parseCommand("undo 3") instanceof UndoCommand);
    }

```
###### \java\seedu\address\logic\parser\DeleteInjuriesCommandParserTest.java
``` java
public class DeleteInjuriesCommandParserTest {


    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteInjuriesHistoryCommand.MESSAGE_USAGE);

    private DeleteInjuriesHistoryCommandParser parser = new DeleteInjuriesHistoryCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, INJURIES_HISTORY_DESC, MESSAGE_INVALID_FORMAT);

        // empty remark
        assertParseFailure(parser, "1" + INVALID_INJURIES_HISTORY_DESC, MESSAGE_INVALID_FORMAT);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + INJURIES_HISTORY_DESC, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + INJURIES_HISTORY_DESC, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + INJURIES_HISTORY_DESC;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withRemark(INJURIES_HISTORY_DESC).build();
        DeleteInjuriesHistoryCommand expectedCommand = new DeleteInjuriesHistoryCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
```
###### \java\seedu\address\logic\parser\DeleteRemarkCommandParserTest.java
``` java
public class DeleteRemarkCommandParserTest {


    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteRemarkCommand.MESSAGE_USAGE);

    private DeleteRemarkCommandParser parser = new DeleteRemarkCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, REMARK_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // empty remark
        assertParseFailure(parser, "1" + INVALID_REMARK_DESC, MESSAGE_INVALID_FORMAT);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + REMARK_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + REMARK_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_someFieldsSpecified_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + REMARK_DESC_BOB;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withRemark(REMARK_DESC_BOB).build();
        DeleteRemarkCommand expectedCommand = new DeleteRemarkCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
```
###### \java\seedu\address\logic\parser\TagReplaceCommandParserTest.java
``` java
public class TagReplaceCommandParserTest {

    private TagReplaceCommandParser parser = new TagReplaceCommandParser();


    @Test


    public void parse_validArgs_returnsTagReplaceCommand() {
        List<Tag> tagList = new ArrayList<Tag>(){};
        tagList.add(new Tag("friend"));
        tagList.add(new Tag("husband"));
        assertParseFailure(parser, "t/ ", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                            TagReplaceCommand.MESSAGE_USAGE));
        //assertParseFailure(parser, "t/", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
        //                   TagReplaceCommand.MESSAGE_USAGE));
        //assertParseSuccess(parser, "t/friend t/husband", new TagReplaceCommand(tagList));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                                                                TagReplaceCommand.MESSAGE_USAGE));
    }
```
###### \java\seedu\address\model\person\NricTest.java
``` java
public class NricTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Nric(null));
    }

    @Test
    public void constructor_invalidNric_throwsIllegalArgumentException() {
        String invalidNric = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Nric(invalidNric));
    }

    @Test
    public void isValidNric() {
        // null nric number
        Assert.assertThrows(NullPointerException.class, () -> Nric.isValidNric(null));

        // invalid nric numbers
        assertFalse(Nric.isValidNric("")); // empty string
        assertFalse(Nric.isValidNric(" ")); // spaces only
        assertFalse(Nric.isValidNric("91")); // less than 3 numbers
        assertFalse(Nric.isValidNric("ic")); // non-numeric
        assertFalse(Nric.isValidNric("9011p041")); // alphabets within digits
        assertFalse(Nric.isValidNric("S9312 153A")); // spaces within digits

        // valid nric number
        assertTrue(Nric.isValidNric("S9312154Z"));
    }
}
```
