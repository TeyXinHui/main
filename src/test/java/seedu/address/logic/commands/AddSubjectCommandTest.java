package seedu.address.logic.commands;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NRIC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_REMARK;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SUBJECT_ENGLISH;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.prepareRedoCommand;
import static seedu.address.logic.commands.CommandTestUtil.prepareUndoCommand;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.io.IOException;

import org.junit.Test;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;

//@@author TeyXinHui
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
    //@@author
}
