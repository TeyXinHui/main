package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.ObservableList;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.UserPrefs;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.appointment.exceptions.DuplicateAppointmentException;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.exceptions.TagNotFoundException;

//@@author kengsengg
/**
 * Contains integration tests and unit tests for AddAppointmentCommand.
 */
public class AddAppointmentCommandTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    private String name;
    private String info;
    private String date;
    private String startTime;
    private String endTime;

    @Test
    public void constructor_nullAppointment_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddAppointmentCommand(null);

    }

    @Test
    public void execute_duplicateAppointment_throwsCommandException() throws Exception {
        AddAppointmentCommandTest.ModelStub modelStub = new AddAppointmentCommandTest
                .ModelStubThrowingDuplicateAppointmentException();
        Appointment validAppointment = new Appointment(name, info, date, startTime, endTime);

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddAppointmentCommand.MESSAGE_DUPLICATE_APPOINTMENT);

        getAddAppointmentCommand(validAppointment, modelStub).execute();
    }

    @Test
    public void equals() {
        Appointment a1 = new Appointment("Alex Yeoh", "Consultation", "04042018", "1200",
                "1300");
        Appointment a2 = new Appointment("David Li", "Remedial", "05052018", "1400",
                "1600");
        AddAppointmentCommand adda1Command = new AddAppointmentCommand(a1);
        AddAppointmentCommand adda2Command = new AddAppointmentCommand(a2);

        // same appointment -> returns true
        assertEquals(adda1Command, adda1Command);

        // different appointment -> returns false
        assertFalse(adda1Command.equals(adda2Command));

        // same values -> returns true
        AddAppointmentCommand adda1CommandCopy = new AddAppointmentCommand(a1);
        assertTrue(adda1Command.equals(adda1CommandCopy));

        // different types -> returns false
        assertFalse(adda1Command.equals(1));
        assertFalse(adda1Command.equals("abc"));

        // null -> returns false
        assertFalse(adda1Command.equals(null));
    }

    /**
     * Generates a new AppointmentCommand with the details of the given person.
     */
    private AddAppointmentCommand getAddAppointmentCommand(Appointment appointment, Model model) {
        AddAppointmentCommand command = new AddAppointmentCommand(appointment);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }

    /**
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {

        @Override
        public void replaceTag(List<Tag> tagList) {
            fail("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) throws DuplicatePersonException {
            fail("This method should not be called.");
        }

        @Override
        public void addPage(Person person) throws IOException {}

        public void deletePage(Person person) {}

        @Override
        public void resetData(ReadOnlyAddressBook newData) {
            fail("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void deletePerson(Person target) throws PersonNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public void updatePerson(Person target, Person editedPerson)
                throws DuplicatePersonException {
            fail("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            fail("This method should not be called.");
        }

        @Override
        public void deleteTag(Tag tag) throws TagNotFoundException {
            fail("This method should not be called.");
        }

        @Override
        public void sortPersonList(String parameter) {
            fail("This method should not be called");
        }

        @Override
        public void addAppointment(Appointment appointment) throws DuplicateAppointmentException {
            fail("This method should not be called");
        }

        @Override
        public void deleteAppointment(Appointment appointment) {
            fail("This method should not be called");
        }

        @Override
        public ObservableList<Appointment> getFilteredAppointmentList() {
            fail("This method should not be called.");
            return null;
        }

        @Override
        public void updateFilteredAppointmentList(Predicate<Appointment> predicate) {
            fail("This method should not be called.");
        }
    }

    /**
     * A Model stub that always throw a DuplicatePersonException when trying to add a person.
     */
    private class ModelStubThrowingDuplicateAppointmentException extends AddAppointmentCommandTest.ModelStub {
        @Override
        public void addAppointment(Appointment appointment) throws DuplicateAppointmentException {
            throw new DuplicateAppointmentException();
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

    /**
     * A Model stub that always accept the person being added.
     */
    private class ModelStubAcceptingAppointmentAdded extends AddAppointmentCommandTest.ModelStub {
        final ArrayList<Appointment> appointmentsAdded = new ArrayList<>();

        @Override
        public void addAppointment(Appointment appointment) throws DuplicateAppointmentException {
            requireNonNull(appointment);
            appointmentsAdded.add(appointment);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

}
//@@author
