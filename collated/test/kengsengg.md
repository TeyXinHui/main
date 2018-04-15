# kengsengg
###### \java\guitests\guihandles\PersonCardHandle.java
``` java
    public List<String> getTagStyles(String tag) {
        return tagLabels
                .stream()
                .filter(label -> label.getText().equals(tag))
                .map(Label::getStyleClass)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No such tag."));
    }
```
###### \java\seedu\address\logic\commands\AddAppointmentCommandTest.java
``` java
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
```
###### \java\seedu\address\logic\commands\SortCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) and unit tests for SortCommand.
 */

public class SortCommandTest {

    private Model model;
    private Model expectedModel;
    private SortCommand sortCommand;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        String parameter = "name";

        sortCommand = new SortCommand(parameter);
        sortCommand.setData(model, new CommandHistory(), new UndoRedoStack());
    }

    @Test
    public void showsSortedList() throws IOException {
        assertCommandSuccess(sortCommand, model, SortCommand.MESSAGE_SUCCESS_SORT_BY_NAME, expectedModel);
    }
}
```
###### \java\seedu\address\ui\PersonCardTest.java
``` java
    /**
     * Asserts that {@code personCard} matches the tag details and color of {@code expectedPerson} correctly
     */
    private static void assertTagsMatching(Person expectedPerson, PersonCardHandle personCard) {
        List<String> expectedTags = expectedPerson.getTags().stream()
                .map(tag -> tag.tagName).collect(Collectors.toList());
        assertEquals(expectedTags, personCard.getTags());
    }
```
