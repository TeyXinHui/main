# TeyXinHui
###### \java\seedu\address\logic\commands\AddSubjectCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model, UndoCommand and RedoCommand) and unit tests
 * for AddSubjectCommand.
 */
public class AddSubjectCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() throws Exception {
        Person editedPerson = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedPerson).build();
        AddSubjectCommand addSubjectCommand = prepareCommand(INDEX_FIRST_PERSON, descriptor);
        StringBuilder result = new StringBuilder();
        String expectedMessage = result.append(AddSubjectCommand.MESSAGE_ADD_SUBJECT_SUCCESS)
                .append(editedPerson.getName()).append(AddSubjectCommand.MESSAGE_NEW_SUBJECTS)
                .append(editedPerson.getSubjects()).toString();

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updatePerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(addSubjectCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_fieldsSpecifiedUnfilteredList_success() throws Exception {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredPersonList().size());
        Person lastPerson = model.getFilteredPersonList().get(indexLastPerson.getZeroBased());

        PersonBuilder personInList = new PersonBuilder(lastPerson);
        Person editedPerson = personInList.withName(VALID_NAME_BOB).withNric(VALID_NRIC_BOB)
                .withTags(VALID_TAG_HUSBAND).withRemark(VALID_REMARK).withSubjects(VALID_SUBJECT_ENGLISH).build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
                .withNric(VALID_NRIC_BOB).withTags(VALID_TAG_HUSBAND).withRemark(VALID_REMARK)
                .withSubjects(VALID_SUBJECT_ENGLISH).build();
        AddSubjectCommand addSubjectCommand = prepareCommand(indexLastPerson, descriptor);
        StringBuilder result = new StringBuilder();

        String expectedMessage = result.append(AddSubjectCommand.MESSAGE_ADD_SUBJECT_SUCCESS)
                .append(editedPerson.getName()).append(AddSubjectCommand.MESSAGE_NEW_SUBJECTS)
                .append(editedPerson.getSubjects()).toString();

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updatePerson(lastPerson, editedPerson);

        //assertCommandSuccess(addSubjectCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() throws IOException {
        AddSubjectCommand addSubjectCommand = prepareCommand(INDEX_FIRST_PERSON, new EditPersonDescriptor());
        Person editedPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        StringBuilder result = new StringBuilder();

        String expectedMessage = result.append(AddSubjectCommand.MESSAGE_ADD_SUBJECT_SUCCESS)
                .append(editedPerson.getName()).append(AddSubjectCommand.MESSAGE_NEW_SUBJECTS)
                .append(editedPerson.getSubjects()).toString();

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(addSubjectCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() throws Exception {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(personInFilteredList).withName(VALID_NAME_BOB)
                .withSubjects(VALID_SUBJECT_ENGLISH).build();
        AddSubjectCommand addSubjectCommand = prepareCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).withSubjects(VALID_SUBJECT_ENGLISH).build());

        StringBuilder result = new StringBuilder();
        String expectedMessage = result.append(AddSubjectCommand.MESSAGE_ADD_SUBJECT_SUCCESS)
                .append(editedPerson.getName()).append(AddSubjectCommand.MESSAGE_NEW_SUBJECTS)
                .append(editedPerson.getSubjects()).toString();

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updatePerson(model.getFilteredPersonList().get(0), editedPerson);

        //assertCommandSuccess(addSubjectCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() throws IOException {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(firstPerson).build();
        AddSubjectCommand addSubjectCommand = prepareCommand(INDEX_SECOND_PERSON, descriptor);

        assertCommandFailure(addSubjectCommand, model, AddSubjectCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicatePersonFilteredList_failure() throws IOException {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        // edit person in filtered list into a duplicate in address book
        Person personInList = model.getAddressBook().getPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        AddSubjectCommand addSubjectCommand = prepareCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder(personInList).build());

        assertCommandFailure(addSubjectCommand, model, AddSubjectCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() throws IOException {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build();
        AddSubjectCommand addSubjectCommand = prepareCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(addSubjectCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() throws IOException {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        AddSubjectCommand addSubjectCommand = prepareCommand(outOfBoundIndex,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(addSubjectCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build();
        AddSubjectCommand addSubjectCommand = prepareCommand(outOfBoundIndex, descriptor);

        // execution failed -> addSubjectCommand not pushed into undoRedoStack

        try {
            assertCommandFailure(addSubjectCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        } catch (IOException e) {
            fail("The expected CommandException was not thrown.");
        }

        // no commands in undoRedoStack -> undoCommand and redoCommand fail
        try {
            assertCommandFailure(undoCommand, model, UndoCommand.MESSAGE_FAILURE);
        } catch (IOException e) {
            fail("The expected CommandException was not thrown.");
        }
        try {
            assertCommandFailure(redoCommand, model, RedoCommand.MESSAGE_FAILURE);
        } catch (IOException e) {
            fail("The expected CommandException was not thrown.");
        }

    }

    @Test
    public void equals() throws Exception {
        final AddSubjectCommand standardCommand = prepareCommand(INDEX_FIRST_PERSON, DESC_AMY);

        // same values -> returns true
        EditPersonDescriptor copyDescriptor = new EditPersonDescriptor(DESC_AMY);
        AddSubjectCommand commandWithSameValues = prepareCommand(INDEX_FIRST_PERSON, copyDescriptor);
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
        assertFalse(standardCommand.equals(new AddSubjectCommand(INDEX_SECOND_PERSON, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new AddSubjectCommand(INDEX_FIRST_PERSON, DESC_BOB)));
    }

    /**
     * Returns an {@code AddSubjectCommand} with parameters {@code index} and {@code descriptor}
     */
    private AddSubjectCommand prepareCommand(Index index, EditPersonDescriptor descriptor) {
        AddSubjectCommand addSubjectCommand = new AddSubjectCommand(index, descriptor);
        addSubjectCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return addSubjectCommand;
    }
```
###### \java\seedu\address\logic\commands\CcaCommandTest.java
``` java
public class CcaCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void constructor_nullIndexAndPerson_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new CcaCommand(null, null);

        thrown.expect(NullPointerException.class);
        new CcaCommand(INDEX_FIRST_PERSON, null);

        thrown.expect(NullPointerException.class);
        new CcaCommand(null, new EditPersonDescriptor());
    }

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() throws Exception {
        Person editedPerson = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedPerson).build();
        CcaCommand ccaCommand = prepareCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(CcaCommand.MESSAGE_REMARK_PERSON_SUCCESS, editedPerson.getCca(),
                editedPerson.getName());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updatePerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(ccaCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() throws Exception {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredPersonList().size());
        Person lastPerson = model.getFilteredPersonList().get(indexLastPerson.getZeroBased());

        PersonBuilder personInList = new PersonBuilder(lastPerson);
        Person editedPerson = personInList.build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withCca("Basketball", "Member").build();
        CcaCommand ccaCommand = prepareCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(CcaCommand.MESSAGE_REMARK_PERSON_SUCCESS, editedPerson.getCca(),
                editedPerson.getName());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updatePerson(lastPerson, editedPerson);
    }

    @Test
    public void execute_filteredList_success() throws Exception {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(personInFilteredList).build();
        CcaCommand ccaCommand = prepareCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder().withCca("Basketball", "Member").build());

        String expectedMessage = String.format(CcaCommand.MESSAGE_REMARK_PERSON_SUCCESS, editedPerson.getCca(),
                editedPerson.getName());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updatePerson(model.getFilteredPersonList().get(0), editedPerson);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() throws IOException {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(firstPerson).build();
        CcaCommand ccaCommand = prepareCommand(INDEX_SECOND_PERSON, descriptor);

        assertCommandFailure(ccaCommand, model, CcaCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicatePersonFilteredList_failure() throws IOException {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        // edit person in filtered list into a duplicate in address book
        Person personInList = model.getAddressBook().getPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        CcaCommand ccaCommand = prepareCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder(personInList).build());

        assertCommandFailure(ccaCommand, model, CcaCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() throws IOException {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withCca("Basketball", "Member").build();
        CcaCommand ccaCommand = prepareCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(ccaCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() throws IOException {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        Assert.assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        CcaCommand ccaCommand = prepareCommand(outOfBoundIndex,
                new EditPersonDescriptorBuilder().withCca("Basketball", "Member").build());

        assertCommandFailure(ccaCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withCca("Basketball", "Member").build();
        CcaCommand ccaCommand = prepareCommand(outOfBoundIndex, descriptor);

        // execution failed -> ccaCommand not pushed into undoRedoStack

        try {
            assertCommandFailure(ccaCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        } catch (IOException e) {
            Assert.fail("The expected CommandException was not thrown.");
        }

        // no commands in undoRedoStack -> undoCommand and redoCommand fail
        try {
            assertCommandFailure(undoCommand, model, UndoCommand.MESSAGE_FAILURE);
        } catch (IOException e) {
            Assert.fail("The expected CommandException was not thrown.");
        }
        try {
            assertCommandFailure(redoCommand, model, RedoCommand.MESSAGE_FAILURE);
        } catch (IOException e) {
            Assert.fail("The expected CommandException was not thrown.");
        }

    }

    @Test
    public void equals() throws Exception {
        final CcaCommand standardCommand = prepareCommand(INDEX_FIRST_PERSON, DESC_AMY);

        // same values -> returns true
        EditPersonDescriptor copyDescriptor = new EditPersonDescriptor(DESC_AMY);
        CcaCommand commandWithSameValues = prepareCommand(INDEX_FIRST_PERSON, copyDescriptor);
        Assert.assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        Assert.assertTrue(standardCommand.equals(standardCommand));

        // one command preprocessed when previously equal -> returns false
        commandWithSameValues.preprocessUndoableCommand();
        assertFalse(standardCommand.equals(commandWithSameValues));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new CcaCommand(INDEX_SECOND_PERSON, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new CcaCommand(INDEX_FIRST_PERSON, DESC_BOB)));
    }

    /**
     * Returns an {@code CcaCommand} with parameters {@code index} and {@code descriptor}
     */
    private CcaCommand prepareCommand(Index index, EditPersonDescriptor descriptor) {
        CcaCommand ccaCommand = new CcaCommand(index, descriptor);
        ccaCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return ccaCommand;
    }
```
###### \java\seedu\address\logic\commands\NextOfKinCommandTest.java
``` java
public class NextOfKinCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void constructor_nullIndexAndPerson_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new NextOfKinCommand(null, null);

        thrown.expect(NullPointerException.class);
        new NextOfKinCommand(INDEX_FIRST_PERSON, null);

        thrown.expect(NullPointerException.class);
        new NextOfKinCommand(null, new EditPersonDescriptor());
    }

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() throws Exception {
        Person editedPerson = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(editedPerson).build();
        NextOfKinCommand nextOfKinCommand = prepareCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(NextOfKinCommand.MESSAGE_ADD_NOK_SUCCESS, editedPerson.getNextOfKin(),
                editedPerson.getName());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updatePerson(model.getFilteredPersonList().get(0), editedPerson);

        assertCommandSuccess(nextOfKinCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() throws Exception {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredPersonList().size());
        Person lastPerson = model.getFilteredPersonList().get(indexLastPerson.getZeroBased());

        PersonBuilder personInList = new PersonBuilder(lastPerson);
        Person editedPerson = personInList.build();

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withNextOfKin("John 98765432 john@gmail.com Father").build();
        NextOfKinCommand nextOfKinCommand = prepareCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(NextOfKinCommand.MESSAGE_ADD_NOK_SUCCESS, editedPerson.getNextOfKin(),
                editedPerson.getName());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updatePerson(lastPerson, editedPerson);

        //assertCommandSuccess(nextOfKinCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_noFieldSpecifiedUnfilteredList_success() throws IOException {
        NextOfKinCommand nextOfKinCommand = prepareCommand(INDEX_FIRST_PERSON, new EditPersonDescriptor());
        Person editedPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        String expectedMessage = String.format(NextOfKinCommand.MESSAGE_ADD_NOK_SUCCESS, editedPerson.getNextOfKin(),
                editedPerson.getName());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());

        assertCommandSuccess(nextOfKinCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() throws Exception {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(personInFilteredList).build();
        NextOfKinCommand nextOfKinCommand = prepareCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder().withNextOfKin("John 98765432 john@gmail.com Father").build());

        String expectedMessage = String.format(NextOfKinCommand.MESSAGE_ADD_NOK_SUCCESS, editedPerson.getNextOfKin(),
                editedPerson.getName());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.updatePerson(model.getFilteredPersonList().get(0), editedPerson);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() throws IOException {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(firstPerson).build();
        NextOfKinCommand nextOfKinCommand = prepareCommand(INDEX_SECOND_PERSON, descriptor);

        assertCommandFailure(nextOfKinCommand, model, NextOfKinCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicatePersonFilteredList_failure() throws IOException {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        // edit person in filtered list into a duplicate in address book
        Person personInList = model.getAddressBook().getPersonList().get(INDEX_SECOND_PERSON.getZeroBased());
        NextOfKinCommand nextOfKinCommand = prepareCommand(INDEX_FIRST_PERSON,
                new EditPersonDescriptorBuilder(personInList).build());

        assertCommandFailure(nextOfKinCommand, model, NextOfKinCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() throws IOException {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withNextOfKin("John 98765432 john@gmail.com Father").build();
        NextOfKinCommand nextOfKinCommand = prepareCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(nextOfKinCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() throws IOException {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        Assert.assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        NextOfKinCommand nextOfKinCommand = prepareCommand(outOfBoundIndex,
                new EditPersonDescriptorBuilder().withNextOfKin("John 98765432 john@gmail.com Father").build());

        assertCommandFailure(nextOfKinCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void executeUndoRedo_invalidIndexUnfilteredList_failure() {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withNextOfKin("John 98765432 john@gmail.com Father").build();
        NextOfKinCommand nextOfKinCommand = prepareCommand(outOfBoundIndex, descriptor);

        // execution failed -> nextOfKinCommand not pushed into undoRedoStack

        try {
            assertCommandFailure(nextOfKinCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        } catch (IOException e) {
            Assert.fail("The expected CommandException was not thrown.");
        }

        // no commands in undoRedoStack -> undoCommand and redoCommand fail
        try {
            assertCommandFailure(undoCommand, model, UndoCommand.MESSAGE_FAILURE);
        } catch (IOException e) {
            Assert.fail("The expected CommandException was not thrown.");
        }
        try {
            assertCommandFailure(redoCommand, model, RedoCommand.MESSAGE_FAILURE);
        } catch (IOException e) {
            Assert.fail("The expected CommandException was not thrown.");
        }

    }

    @Test
    public void equals() throws Exception {
        final NextOfKinCommand standardCommand = prepareCommand(INDEX_FIRST_PERSON, DESC_AMY);

        // same values -> returns true
        EditPersonDescriptor copyDescriptor = new EditPersonDescriptor(DESC_AMY);
        NextOfKinCommand commandWithSameValues = prepareCommand(INDEX_FIRST_PERSON, copyDescriptor);
        Assert.assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        Assert.assertTrue(standardCommand.equals(standardCommand));

        // one command preprocessed when previously equal -> returns false
        commandWithSameValues.preprocessUndoableCommand();
        assertFalse(standardCommand.equals(commandWithSameValues));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new NextOfKinCommand(INDEX_SECOND_PERSON, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new NextOfKinCommand(INDEX_FIRST_PERSON, DESC_BOB)));
    }

    /**
     * Returns an {@code NextOfKinCommand} with parameters {@code index} and {@code descriptor}
     */
    private NextOfKinCommand prepareCommand(Index index, EditPersonDescriptor descriptor) {
        NextOfKinCommand nextOfKinCommand = new NextOfKinCommand(index, descriptor);
        nextOfKinCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return nextOfKinCommand;
    }
```
###### \java\seedu\address\logic\commands\StreamCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) for {@code StreamCommand}.
 */
public class StreamCommandTest {
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_indexOutOfBounds_throwCommandException() {
        Index outOfBoundsIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        int type = 1;
        assertExecutionFailure(outOfBoundsIndex, type, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validInput_success() {
        Index lastPersonIndex = Index.fromOneBased(model.getFilteredPersonList().size());
        assertExecutionSuccess(INDEX_FIRST_PERSON, 1);
        assertExecutionSuccess(INDEX_THIRD_PERSON, 2);
        assertExecutionSuccess(INDEX_FIRST_PERSON, 3);
        assertExecutionSuccess(INDEX_FIRST_PERSON, 4);
        assertExecutionSuccess(lastPersonIndex, 5);
    }

    @Test
    public void equals() {
        StreamCommand streamFirstCommand = new StreamCommand(INDEX_FIRST_PERSON, 1);
        StreamCommand streamSecondCommand = new StreamCommand(INDEX_SECOND_PERSON, 1);


        // same object -> returns true
        assertTrue(streamFirstCommand.equals(streamFirstCommand));

        // same values -> returns true
        StreamCommand streamFirstCommandCopy = new StreamCommand(INDEX_FIRST_PERSON, 1);
        assertTrue(streamFirstCommand.equals(streamFirstCommandCopy));

        // different types -> returns false
        assertFalse(streamFirstCommand.equals(1));

        // null -> returns false
        assertFalse(streamFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(streamFirstCommand.equals(streamSecondCommand));
    }

    /**
     * Executes a {@code SelectCommand} with the given {@code index}, and checks that {@code JumpToListRequestEvent}
     * is raised with the correct index.
     */
    private void assertExecutionSuccess (Index index, int type) {
        StreamCommand streamCommand = prepareCommand(index, type);
        List<Person> lastShownList = model.getFilteredPersonList();
        Person selectedPerson = lastShownList.get(index.getZeroBased());
        StringBuilder result = new StringBuilder();
        String message = "";
        int score = 5;
        switch(type) {
        case(1):
            message = MESSAGE_L1R5_SUCCESS;
            score = 6;
            break;
        case(2):
            message = MESSAGE_L1B4A_SUCCESS;
            break;
        case(3):
            message = MESSAGE_L1B4B_SUCCESS;
            break;
        case(4):
            message = MESSAGE_L1B4C_SUCCESS;
            break;
        case(5):
            message = MESSAGE_L1B4D_SUCCESS;
            break;
        default:
            break;
        }

        try {
            CommandResult commandResult = streamCommand.execute();
            assertEquals(result.append(String.format(MESSAGE_SELECT_STUDENT_SUCCESS, selectedPerson.getName()))
                    .append(String.format(message, score)).toString(),
                    commandResult.feedbackToUser);
        } catch (CommandException ce) {
            throw new IllegalArgumentException("Execution of command should not fail.", ce);
        }

        JumpToListRequestEvent lastEvent = (JumpToListRequestEvent) eventsCollectorRule.eventsCollector.getMostRecent();
        assertEquals(index, Index.fromZeroBased(lastEvent.targetIndex));
    }
    /**
     *
     * Executes a {@code StreamCommand} with the given {@code index} and {@code type},
     * and checks that a {@code CommandException}
     * is thrown with the {@code expectedMessage}.
     */
    private void assertExecutionFailure(Index index, int type, String expectedMessage) {
        StreamCommand streamCommand = prepareCommand(index, type);
        try {
            streamCommand.execute();
            fail("The expected CommandException was not thrown.");
        } catch (CommandException ce) {
            assertEquals(expectedMessage, ce.getMessage());
            assertTrue(eventsCollectorRule.eventsCollector.isEmpty());
        }
    }

    /**
     * Returns a {@code SelectCommand} with parameters {@code index}.
     */
    private StreamCommand prepareCommand(Index index, int type) {
        StreamCommand streamCommand = new StreamCommand(index, type);
        streamCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return streamCommand;
    }
}
```
###### \java\seedu\address\logic\commands\TagDeleteCommandTest.java
``` java
public class TagDeleteCommandTest {
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Tag tagToDelete = new Tag("removeTag");

    @Test
    public void execute_validTagToRemoveEntered_success() throws Exception {
        ReadOnlyAddressBook addressBook = model.getAddressBook();
        List<Tag> tagList = addressBook.getTagList();
        Tag removeTag = tagList.get(0);
        TagDeleteCommand tagDeleteCommand = prepareCommand(removeTag);

        String expectedMessage = String.format(TagDeleteCommand.MESSAGE_DELETE_TAG_SUCCESS, removeTag);

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.deleteTag(removeTag);

        assertCommandSuccess(tagDeleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidTagToRemoveEntered_throwsCommandException() throws IOException {
        TagDeleteCommand tagDeleteCommand = prepareCommand(tagToDelete);
        assertCommandFailure(tagDeleteCommand, model, Messages.MESSAGE_INVALID_TAG_ENTERED);
    }

    @Test
    public void executeUndoRedo_invalidTagToRemoveEntered_failure() throws IOException {
        UndoRedoStack undoRedoStack = new UndoRedoStack();
        UndoCommand undoCommand = prepareUndoCommand(model, undoRedoStack);
        RedoCommand redoCommand = prepareRedoCommand(model, undoRedoStack);
        TagDeleteCommand tagDeleteCommand = prepareCommand(tagToDelete);

        // execution failed -> deleteCommand not pushed into undoRedoStack
        assertCommandFailure(tagDeleteCommand, model, Messages.MESSAGE_INVALID_TAG_ENTERED);

        // no commands in undoRedoStack -> undoCommand and redoCommand fail
        assertCommandFailure(undoCommand, model, UndoCommand.MESSAGE_FAILURE);
        assertCommandFailure(redoCommand, model, RedoCommand.MESSAGE_FAILURE);
    }

    /**
     * Returns a {@code DeleteCommand} with the parameter {@code index}.
     */
    private TagDeleteCommand prepareCommand(Tag removeTag) {
        TagDeleteCommand tagDeleteCommand = new TagDeleteCommand(removeTag);
        tagDeleteCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return tagDeleteCommand;
    }

}
```
###### \java\seedu\address\logic\parser\AddressBookParserTest.java
``` java
    @Test
    public void parseCommand_stream() throws Exception {
        StreamCommand command = (StreamCommand) parser.parseCommand(
                StreamCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased() + " 1");
        assertEquals(new StreamCommand(INDEX_FIRST_PERSON, 1), command);
    }
```
###### \java\seedu\address\logic\parser\AddSubjectCommandParserTest.java
``` java
public class AddSubjectCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddSubjectCommand.MESSAGE_USAGE);

    private AddSubjectCommandParser parser = new AddSubjectCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_SUBJECT_BIOLOGY, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", AddSubjectCommand.MESSAGE_NOT_EDITED);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + SUBJECT_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + SUBJECT_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        assertParseFailure(parser, "1" + INVALID_SUBJECT_NAME_DESC,
                Subject.MESSAGE_SUBJECT_NAME_CONSTRAINTS); // invalid subject name
        assertParseFailure(parser, "1" + INVALID_SUBJECT_GRADE_DESC,
                Subject.MESSAGE_SUBJECT_GRADE_CONSTRAINTS); // invalid subject grade

        // valid subject followed by invalid subject. The test case for invalid subject followed by valid subject
        // is tested at {@code parse_invalidValueFollowedByValidValue_success()}
        assertParseFailure(parser, "1 " + PREFIX_SUBJECT + VALID_SUBJECT_MATHEMATICS + " "
                        + "wqfqwf A1", Subject.MESSAGE_SUBJECT_NAME_CONSTRAINTS);
        assertParseFailure(parser, "1 " + PREFIX_SUBJECT + VALID_SUBJECT_MATHEMATICS + " "
                        + "English aswfwef", Subject.MESSAGE_SUBJECT_GRADE_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_PERSON;
        String userInput = targetIndex.getOneBased() + " " + PREFIX_SUBJECT + VALID_SUBJECT_HISTORY;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withSubjects(VALID_SUBJECT_HISTORY).build();
        AddSubjectCommand expectedCommand = new AddSubjectCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_addDuplicateSubject_success() {
        Index targetIndex = INDEX_THIRD_PERSON;
        String userInput = targetIndex.getOneBased() + " " + PREFIX_SUBJECT + VALID_SUBJECT_MATHEMATICS;
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withSubjects(VALID_SUBJECT_MATHEMATICS)
                .build();
        AddSubjectCommand expectedCommand = new AddSubjectCommand(targetIndex, descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_duplicateSubjectsInInput_throwsException() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + " " + PREFIX_SUBJECT + VALID_SUBJECT_MATHEMATICS + " "
                + VALID_SUBJECT_MATHEMATICS;

        String expectedMessage = "There should not be duplicate subject(s) assigned to student.";

        assertParseFailure(parser, userInput, expectedMessage);
    }

    @Test
    public void parse_noSubjectInput_success() {
        Index targetIndex = INDEX_THIRD_PERSON;
        String userInput = targetIndex.getOneBased() + " " + PREFIX_SUBJECT;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withSubjects().build();
        AddSubjectCommand expectedCommand = new AddSubjectCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
```
###### \java\seedu\address\logic\parser\CcaCommandParserTest.java
``` java
public class CcaCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, CcaCommand.MESSAGE_USAGE);

    private CcaCommandParser parser = new CcaCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_CCA_DESC, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", MESSAGE_INVALID_FORMAT);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + VALID_CCA_DESC, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + VALID_CCA_DESC, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        //empty cca
        assertParseFailure(parser, "1" + EMPTY_CCA_DESC, MESSAGE_INVALID_FORMAT);
        //empty cca position
        assertParseFailure(parser, "1" + EMPTY_CCA_POSITION_DESC, MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_oneFieldSpecified_failure() {
        //Cca position field not stated
        Index targetIndex = INDEX_SECOND_PERSON;
        String userInput = targetIndex.getOneBased() + NO_CCA_POSITION_STATED;
        assertParseFailure(parser, userInput, MESSAGE_INVALID_FORMAT);

        //Cca field not stated
        Index index = INDEX_SECOND_PERSON;
        String nextUserInput = index.getOneBased() + NO_CCA_STATED;
        assertParseFailure(parser, nextUserInput, MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_PERSON;
        String userInput = targetIndex.getOneBased() + VALID_CCA_DESC;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withCca(VALID_CCA, VALID_CCA_POSITION).build();
        CcaCommand expectedCommand = new CcaCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
```
###### \java\seedu\address\logic\parser\NextOfKinCommandParserTest.java
``` java
public class NextOfKinCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, NextOfKinCommand.MESSAGE_USAGE);

    private NextOfKinCommandParser parser = new NextOfKinCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, VALID_NOK_DESC, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", MESSAGE_INVALID_FORMAT);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + NAME_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        //invalid name
        assertParseFailure(parser, "1" + INVALID_NOK_NAME_DESC, MESSAGE_NAME_CONSTRAINTS);
        //invalid phone
        assertParseFailure(parser, "1" + INVALID_NOK_PHONE_DESC, MESSAGE_PHONE_CONSTRAINTS);
        //invalid email
        assertParseFailure(parser, "1" + INVALID_NOK_EMAIL_DESC, MESSAGE_EMAIL_CONSTRAINTS);
        //invalid remark
        assertParseFailure(parser, "1" + INVALID_NOK_REMARK_DESC, MESSAGE_REMARK_CONSTRAINTS);

    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_PERSON;
        String userInput = targetIndex.getOneBased() + VALID_NOK_DESC;

        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder()
                .withNextOfKin("Bob 98765433 email@gmail.com Father").build();
        NextOfKinCommand expectedCommand = new NextOfKinCommand(targetIndex, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
```
###### \java\seedu\address\logic\parser\ParserUtilTest.java
``` java
    @Test
    public void parseSubject_null_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        ParserUtil.parseSubject(null, null);
    }

    @Test
    public void parseSubject_invalidSubjectName_throwsIllegalValueException() throws Exception {
        thrown.expect(IllegalValueException.class);
        ParserUtil.parseSubject(INVALID_SUBJECT_NAME_SET, null);
    }

    @Test
    public void parseSubject_invalidSubjectGrade_throwsIllegalValueException() throws Exception {
        thrown.expect(IllegalValueException.class);
        ParserUtil.parseSubject(INVALID_SUBJECT_GRADE_SET, null);
    }

    @Test
    public void parseSubjects_emptyCollection_returnsEmptySet() throws Exception {
        assertTrue(ParserUtil.parseSubjects(Collections.emptyList()).isEmpty());
    }
```
###### \java\seedu\address\logic\parser\StreamCommandParserTest.java
``` java
public class StreamCommandParserTest {

    private StreamCommandParser parser = new StreamCommandParser();

    @Test
    public void parse_validInput_success() {
        assertParseSuccess(parser, " 1 1", new StreamCommand(INDEX_FIRST_PERSON, 1));
    }

    @Test
    public void parse_emptyArgs_failure() {
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, StreamCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidArgs_failure() {
        //invalid Index input
        assertParseFailure(parser, " A 1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, StreamCommand.MESSAGE_USAGE));

        //invalid type input
        assertParseFailure(parser, " 1 A",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, StreamCommand.MESSAGE_USAGE));

        //insufficient input
        assertParseFailure(parser, " 1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, StreamCommand.MESSAGE_USAGE));

        //too much unnecessary input
        assertParseFailure(parser, " 1 1 2 3",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, StreamCommand.MESSAGE_USAGE));
    }
}
```
###### \java\seedu\address\model\AddressBookTest.java
``` java
    @Test
    public void removeTag_tagNotFound_throwsTagNotFoundException() {
        AddressBook testCase = new AddressBookBuilder().withPerson(BOB).build();
        AddressBook expectedAddressBook = new AddressBookBuilder().withPerson(BOB).build();
        try {
            testCase.removeTag(new Tag(VALID_TAG_REMOVE));
        } catch (TagNotFoundException error) {
            assertEquals(error.getMessage(), "Specific tag is not used in the address book.");
        }
        assertEquals(expectedAddressBook, testCase);
    }

    @Test
    public void removeTag_tagFoundOnMultiplePersons_tagRemoved() {
        AddressBook testCase = new AddressBookBuilder().withPerson(BOB).withPerson(AMY).build();
        try {
            testCase.removeTag(new Tag(VALID_TAG_FRIEND));
        } catch (TagNotFoundException error) {
            thrown.expect(TagNotFoundException.class);
        }
        Person amyWithoutFriendTag = new PersonBuilder(AMY).withTags().build();
        Person bobWithoutFriendTag = new PersonBuilder(BOB).withTags(VALID_TAG_HUSBAND).build();
        AddressBook expectedAddressBook = new AddressBookBuilder().withPerson(bobWithoutFriendTag)
                .withPerson(amyWithoutFriendTag).build();
        assertEquals(expectedAddressBook, testCase);
    }
```
###### \java\seedu\address\model\subject\UniqueSubjectListTest.java
``` java
public class UniqueSubjectListTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        UniqueSubjectList uniqueSubjectList = new UniqueSubjectList();
        thrown.expect(UnsupportedOperationException.class);
        uniqueSubjectList.asObservableList().remove(0);
    }

    @Test
    public void testEquals_similarObject_returnTrue() {
        UniqueSubjectList uniqueSubjectList = new UniqueSubjectList();
        UniqueSubjectList uniqueSubjectList1 = new UniqueSubjectList();
        assertEquals(uniqueSubjectList, uniqueSubjectList1);
    }

    @Test
    public void hashCode_checkForHashCode_sameHashCode() {
        UniqueSubjectList uniqueSubjectList = new UniqueSubjectList();
        UniqueSubjectList uniqueSubjectList1 = new UniqueSubjectList();
        assertEquals(uniqueSubjectList.hashCode(), uniqueSubjectList1.hashCode());
    }

}
```
