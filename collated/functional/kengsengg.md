# kengsengg
###### \java\seedu\address\commons\events\ui\DisplayCalendarRequestEvent.java
``` java
/**
 * Indicates a request to display Calendar
 */
public class DisplayCalendarRequestEvent extends BaseEvent {

    private String parameter;

    public DisplayCalendarRequestEvent(String parameter) {
        this.parameter = parameter;
    }
    @Override
    public String toString() {
        return this.parameter;
    }
}
```
###### \java\seedu\address\logic\commands\AddAppointmentCommand.java
``` java
/**
 * Creates an appointment for the student at the specified index.
 */
public class AddAppointmentCommand extends Command {

    public static final String COMMAND_WORD = "addappointment";
    public static final String COMMAND_ALIAS = "addappt";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Creates an appointment for the student "
            + "at the specified index.\n"
            + "Parameters: "
            + PREFIX_NAME + "NAME "
            + PREFIX_INFO + "INFO "
            + PREFIX_DATE + "DATE "
            + PREFIX_START_TIME + "START TIME "
            + PREFIX_END_TIME + "END TIME \n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NAME + "Alex Yeoh "
            + PREFIX_INFO + "Consultation "
            + PREFIX_DATE + "28042018 "
            + PREFIX_START_TIME + "1500 "
            + PREFIX_END_TIME + "1600 \n"
            + "Example: " + COMMAND_ALIAS + " "
            + PREFIX_NAME + "Alex Yeoh "
            + PREFIX_INFO + "Consultation "
            + PREFIX_DATE + "28042018 "
            + PREFIX_START_TIME + "1500 "
            + PREFIX_END_TIME + "1600 ";

    public static final String MESSAGE_SUCCESS = "New appointment added: %1$s";
    public static final String MESSAGE_DUPLICATE_APPOINTMENT = "This appointment already exists in EduBuddy or "
            + "there is an overlap in appointments";

    private Appointment toAdd;
    private CalendarDisplay calendarDisplay = new CalendarDisplay();

    /**
     * Creates an AddAppointmentCommand to add the specified {@code Appointment}
     */
    public AddAppointmentCommand(Appointment appointment) {
        requireNonNull(appointment);
        this.toAdd = appointment;
    }

    @Override
    public CommandResult execute() throws CommandException, IOException {
        requireNonNull(model);
        try {
            model.addAppointment(toAdd);
            getDetails();
            addEventOnCalendar();
            return new CommandResult(String.format(MESSAGE_SUCCESS, getDetails()));
        } catch (DuplicateAppointmentException e) {
            throw new CommandException(MESSAGE_DUPLICATE_APPOINTMENT);
        }
    }

    private String getDetails() {
        return toAdd.getInfo() + ": " + toAdd.getStartTime() + " to " + toAdd.getEndTime() + " on " + toAdd.getDate()
                + " with " + toAdd.getName();
    }

    /**
     * Generates an unique ID for each event and adds the event on calendar
     */
    private void addEventOnCalendar() throws IOException {
        String id = toAdd.getDate() + toAdd.getStartTime() +  toAdd.getEndTime();
        calendarDisplay.createEvent(toAdd, id);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddAppointmentCommand // instanceof handles nulls
                && toAdd.equals(((AddAppointmentCommand) other).toAdd));
    }
}
```
###### \java\seedu\address\logic\commands\DeleteAppointmentCommand.java
``` java
/**
 * Removes an appointment at the specified index.
 */
public class DeleteAppointmentCommand extends Command {

    public static final String COMMAND_WORD = "deleteappointment";
    public static final String COMMAND_ALIAS = "deleteappt";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Removes the appointment identified by the index number used in the last appointment listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1\n"
            + "Example: " + COMMAND_ALIAS + " 1\n";

    public static final String MESSAGE_DELETE_APPOINTMENT_SUCCESS = "Appointment removed: %1$s";

    private final Index targetIndex;

    private Appointment toDelete;
    private CalendarDisplay calendarDisplay = new CalendarDisplay();

    /**
     * Creates a DeleteAppointmentCommand to delete the specified {@code Appointment}
     */
    public DeleteAppointmentCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() throws CommandException, IOException {
        List<Appointment> lastShownList = model.getFilteredAppointmentList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_APPOINTMENT_DISPLAYED_INDEX);
        }

        toDelete = lastShownList.get(targetIndex.getZeroBased());
        requireNonNull(toDelete);

        try {
            model.deleteAppointment(toDelete);
            getDetails();
            deleteEventOnCalendar();
            return new CommandResult(String.format(MESSAGE_DELETE_APPOINTMENT_SUCCESS, getDetails()));
        } catch (AppointmentNotFoundException e) {
            throw new AssertionError("The target appointment cannot be missing");
        }
    }

    private String getDetails() {
        return toDelete.getInfo() + ": " + toDelete.getStartTime() + " to " + toDelete.getEndTime() + " on "
                + toDelete.getDate();
    }

    private void deleteEventOnCalendar() throws IOException {
        String id = toDelete.getDate() + toDelete.getStartTime() +  toDelete.getEndTime();
        calendarDisplay.removeEvent(id);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteAppointmentCommand // instanceof handles nulls
                && toDelete.equals(((DeleteAppointmentCommand) other).toDelete));
    }
}
```
###### \java\seedu\address\logic\commands\SortCommand.java
``` java
/**
 * Sorts all the people in the list by their names in alphabetical order (case insensitive)
 */
public class SortCommand extends Command {

    public static final String COMMAND_WORD = "sort";

    public static final String MESSAGE_SUCCESS_SORT_BY_NAME = "Sorted all persons by name in alphabetical order";
    public static final String MESSAGE_SUCCESS_SORT_BY_TAG = "Sorted all persons by tag in alphabetical order";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sorts the student list by the parameter provided "
            + "by the user.\n"
            + "Parameters: KEYWORD (valid keyword: name, tag)\n"
            + "Example: " + COMMAND_WORD + " name\n";

    private final String parameter;

    public SortCommand(String parameter) {
        this.parameter = parameter;
    }

    @Override
    public CommandResult execute() {
        model.sortPersonList(parameter);
        if (("name").equals(parameter)) {
            return new CommandResult(MESSAGE_SUCCESS_SORT_BY_NAME);
        }
        return new CommandResult(MESSAGE_SUCCESS_SORT_BY_TAG);
    }
}
```
###### \java\seedu\address\logic\commands\SortCommand.java
``` java

```
###### \java\seedu\address\logic\commands\ViewCommand.java
``` java
/**
 * Shows the calendar display
 */
public class ViewCommand extends Command {

    public static final String COMMAND_WORD = "view";

    public static final String MESSAGE_VIEW_SUCCESS = "Calendar view displayed";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Shows the Google Calendar page of the gmail ID keyed in by the user.\n"
            + "Parameters: GMAIL_ID \n"
            + "Example: " + COMMAND_WORD + " edubuddytest\n";

    private final String parameter;

    public ViewCommand(String parameter) {
        this.parameter = parameter;
    }

    @Override
    public CommandResult execute() {
        EventsCenter.getInstance().post(new DisplayCalendarRequestEvent(this.parameter));
        return new CommandResult(MESSAGE_VIEW_SUCCESS);
    }
}
```
###### \java\seedu\address\logic\parser\AddAppointmentCommandParser.java
``` java
/**
 * Parses input arguments and creates a new AppointmentCommand object
 */
public class AddAppointmentCommandParser implements Parser<AddAppointmentCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AppointmentCommand
     * and returns an AppointmentCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddAppointmentCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_INFO, PREFIX_DATE, PREFIX_START_TIME,
                        PREFIX_END_TIME);

        if (!(argMultimap.arePrefixesPresent(PREFIX_NAME, PREFIX_INFO, PREFIX_DATE, PREFIX_START_TIME,
                PREFIX_END_TIME)) || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddAppointmentCommand.MESSAGE_USAGE));
        }

        try {
            String name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME)).get().toString();
            String info = ParserUtil.parseInfo(argMultimap.getValue(PREFIX_INFO)).get();
            String date = ParserUtil.parseDate(argMultimap.getValue(PREFIX_DATE)).get();
            String startTime = ParserUtil.parseStartTime(argMultimap.getValue(PREFIX_START_TIME)).get();
            String endTime = ParserUtil.parseEndTime(argMultimap.getValue(PREFIX_END_TIME)).get();
            Appointment appointment = new Appointment(name, info, date, startTime, endTime);

            return new AddAppointmentCommand(appointment);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
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
###### \java\seedu\address\logic\parser\DeleteAppointmentCommandParser.java
``` java
/**
 * Parses input arguments and creates a new DeleteAppointmentCommand object
 */
public class DeleteAppointmentCommandParser implements Parser<DeleteAppointmentCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteAppointmentCommand
     * and returns an DeleteAppointmentCommand object for execution.
     * @throws ParseException if the user input does not conform with the expected format
     */
    public DeleteAppointmentCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new DeleteAppointmentCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteAppointmentCommand.MESSAGE_USAGE));
        }
    }
}
```
###### \java\seedu\address\logic\parser\SortCommandParser.java
``` java
/**
 * Parses input arguments and creates a new SortCommand object
 */
public class SortCommandParser implements Parser<SortCommand> {

    private static final String[] ALLOWED_PARAMETERS = {"name", "tag"};
    private int arrayCounter = 0;
    private boolean validParameter = false;

    /**
     * Parses the given {@code String} of arguments in the context of the SortCommand
     * and returns an SortCommand object for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public SortCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }

        while (arrayCounter < ALLOWED_PARAMETERS.length) {
            if (trimmedArgs.equals(ALLOWED_PARAMETERS[arrayCounter++])) {
                validParameter = true;
                break;
            }
        }

        if (!validParameter) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }

        return new SortCommand(trimmedArgs);
    }
}
```
###### \java\seedu\address\logic\parser\ViewCommandParser.java
``` java
/**
 * Parses the given {@code String} of arguments in the context of the ViewCommand
 * and returns an ViewCommand object for execution.
 * @throws ParseException if the user input does not conform the expected format
 */
public class ViewCommandParser implements Parser<ViewCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns an DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ViewCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ViewCommand.MESSAGE_USAGE));
        }

        return new ViewCommand(trimmedArgs);
    }

}
```
###### \java\seedu\address\model\AddressBook.java
``` java
    public void addAppointment(Appointment appointment) throws DuplicateAppointmentException {
        appointments.add(appointment);
    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * @throws AppointmentNotFoundException if the {@code key} is not in this {@code AddressBook}.
     */
    public boolean removeAppointment(Appointment key) throws AppointmentNotFoundException {
        if (appointments.remove(key)) {
            return true;
        } else {
            throw new AppointmentNotFoundException();
        }
    }

    //@author TeyXinHui
    /**
     * Removes all {@code Tag}s that are not used by any {@code Person} in this {@code AddressBook}.
     */
    private void removeUnusedTags() {
        Set<Tag> tagsInPersons = persons.asObservableList().stream()
                .map(Person::getTags)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
        tags.setTags(tagsInPersons);
    }
```
###### \java\seedu\address\model\AddressBook.java
``` java
    /**
     * Chooses the sorting method based on the parameter given
     */
    public void sort(String parameter) {
        if (("name").equals(parameter)) {
            persons.sortNames();
        }
        if (("tag").equals(parameter)) {
            persons.sortTags();
        }
    }
```
###### \java\seedu\address\model\appointment\Appointment.java
``` java
/**
 * Represents an appointment in EduBuddy.
 */
public class Appointment {

    public static final String MESSAGE_APPOINTMENT_DATE_CONSTRAINTS = "Appointment date should be in DDMMYYYY format";
    public static final String MESSAGE_APPOINTMENT_START_TIME_CONSTRAINTS = "Appointment start time should be in "
            + "24 hour format";
    public static final String MESSAGE_APPOINTMENT_END_TIME_CONSTRAINTS = "Appointment end time should be in "
            + "24 hour format and later than the start time";
    public static final String APPOINTMENT_DATE_VALIDATION_REGEX = "^[0-9]{8}$";
    public static final String APPOINTMENT_START_TIME_VALIDATION_REGEX = "^[0-9]{4}$";
    public static final String APPOINTMENT_END_TIME_VALIDATION_REGEX = "^[0-9]{4}$";

    public final String name;
    public final String info;
    public final String date;
    public final String startTime;
    public final String endTime;


    public Appointment(String name, String info, String date, String startTime, String endTime) {
        this.name = name;
        this.info = info;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    public String getDate() {
        return date;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    /**
     * Returns true if a given string is a valid appointment date.
     */
    public static boolean isValidAppointmentDate(String date) {
        requireNonNull(date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(date.trim());
        } catch (ParseException pe) {
            return false;
        }

        return date.matches(APPOINTMENT_DATE_VALIDATION_REGEX);
    }

    /**
     * Returns true if a given string is a valid appointment start time.
     */
    public static boolean isValidAppointmentStartTime(String startTime) {
        requireNonNull(startTime);
        return ((0 < Integer.valueOf(startTime)) && (Integer.valueOf(startTime) <= 2359)
                && startTime.matches(APPOINTMENT_START_TIME_VALIDATION_REGEX));
    }

    /**
     * Returns true if a given string is a valid appointment end time.
     */
    public static boolean isValidAppointmentEndTime(String endTime) {
        requireNonNull(endTime);
        return ((0 < Integer.valueOf(endTime)) && (Integer.valueOf(endTime) <= 2359)
                && endTime.matches(APPOINTMENT_END_TIME_VALIDATION_REGEX));
    }
}
```
###### \java\seedu\address\model\appointment\exceptions\DuplicateAppointmentException.java
``` java
/**
 * Signals that the operation will result in duplicate Appointment objects.
 */
public class DuplicateAppointmentException extends DuplicateDataException {
    public DuplicateAppointmentException() {
        super("Operation would result in duplicate appointments");
    }
}
```
###### \java\seedu\address\model\appointment\UniqueAppointmentList.java
``` java
/**
 * A list of appointments that enforces uniqueness between its elements and does not allow nulls.
 *
 * Supports a minimal set of list operations.
 *
 * @see Appointment#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniqueAppointmentList implements Iterable<Appointment> {

    private final ObservableList<Appointment> internalList = FXCollections.observableArrayList();

    /**
     * Constructs empty AppointmentList.
     */
    public UniqueAppointmentList() {}

    public UniqueAppointmentList(Set<Appointment> appointments) {
        requireAllNonNull(appointments);
        internalList.addAll(appointments);

        assert CollectionUtil.elementsAreUnique(internalList);
    }

    @Override
    public Iterator<Appointment> iterator() {
        assert CollectionUtil.elementsAreUnique(internalList);
        return internalList.iterator();
    }

    /**
     * Adds an Appointment to the list.
     *
     * @throws DuplicateAppointmentException if the Appointment to add is a duplicate of an existing Appointment
     * in the list.
     */
    public void add(Appointment toAdd) throws DuplicateAppointmentException {
        requireNonNull(toAdd);
        if (isAppointmentOverlapped(toAdd)) {
            throw new DuplicateAppointmentException();
        }
        internalList.add(toAdd);

        assert CollectionUtil.elementsAreUnique(internalList);
    }

    /**
     * Removes the equivalent appointment from the list.
     *
     * @throws AppointmentNotFoundException if no such appointment could be found in the list.
     */
    public boolean remove(Appointment toRemove) throws AppointmentNotFoundException {
        final boolean appointmentFoundAndDeleted = internalList.remove(toRemove);
        if (!appointmentFoundAndDeleted) {
            throw new AppointmentNotFoundException();
        }
        return appointmentFoundAndDeleted;
    }

    public void setAppointments(UniqueAppointmentList replacement) {
        this.internalList.setAll(replacement.internalList);
    }

    public void setAppointments(List<Appointment> appointments) throws DuplicateAppointmentException {
        requireAllNonNull(appointments);
        final UniqueAppointmentList replacement = new UniqueAppointmentList();
        for (final Appointment appointment : appointments) {
            replacement.add(appointment);
        }
        setAppointments(replacement);
    }

    /**
     * Returns true if the list contains an equivalent Appointment as the given argument or there is an overlap
     * in appointments
     */
    public boolean isAppointmentOverlapped(Appointment toAdd) {
        for (Appointment appointment : internalList) {
            if (toAdd.getDate().equals(appointment.getDate())) {
                if ((Integer.valueOf(toAdd.getStartTime()) > Integer.valueOf(appointment.getStartTime()))
                    && (Integer.valueOf(toAdd.getStartTime()) < Integer.valueOf(appointment.getEndTime()))) {
                    return true;
                }
                if ((Integer.valueOf(toAdd.getEndTime()) > Integer.valueOf(appointment.getStartTime()))
                    && (Integer.valueOf(toAdd.getEndTime()) < Integer.valueOf(appointment.getEndTime()))) {
                    return true;
                }
                if ((Integer.valueOf(toAdd.getStartTime()) <= Integer.valueOf(appointment.getStartTime()))
                    && (Integer.valueOf(toAdd.getEndTime()) >= Integer.valueOf(appointment.getEndTime()))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<Appointment> asObservableList() {
        return FXCollections.unmodifiableObservableList(internalList);
    }
}
```
###### \java\seedu\address\model\ModelManager.java
``` java
    /** Adds the given appointment */
    public synchronized void addAppointment(Appointment appointment) throws DuplicateAppointmentException {
        addressBook.addAppointment(appointment);
        updateFilteredAppointmentList(PREDICATE_SHOW_ALL_APPOINTMENTS);
        indicateAddressBookChanged();
    }

    @Override
    public synchronized void deleteAppointment(Appointment target) throws AppointmentNotFoundException {
        addressBook.removeAppointment(target);
        indicateAddressBookChanged();
    }
```
###### \java\seedu\address\model\ModelManager.java
``` java
    @Override
    public void sortPersonList(String parameter) {
        addressBook.sort(parameter);
    }

    @Override
    public ObservableList<Appointment> getFilteredAppointmentList() {
        return FXCollections.unmodifiableObservableList(filteredAppointments);
    }

```
###### \java\seedu\address\storage\XmlAdaptedAppointment.java
``` java
/**
 * JAXB-friendly version of the Person.
 */
public class XmlAdaptedAppointment {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Appointment's %s field is missing!";

    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private String info;
    @XmlElement(required = true)
    private String date;
    @XmlElement(required = true)
    private String startTime;
    @XmlElement(required = true)
    private String endTime;

    /**
     * Constructs an XmlAdaptedPerson.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedAppointment() {}

    /**
     * Constructs an {@code XmlAdaptedPerson} with the given appointment details.
     */
    public XmlAdaptedAppointment(String name, String info, String date, String startTime, String endTime) {
        this.name = name;
        this.info = info;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Converts a given Appointment into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedPerson
     */
    public XmlAdaptedAppointment(Appointment source) {
        name = source.getName();
        info = source.getInfo();
        date = source.getDate();
        startTime = source.getStartTime();
        endTime = source.getEndTime();
    }

    /**
     * Converts this jaxb-friendly adapted appointment object into the model's Appointment object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted appointment
     */
    public Appointment toModelType() throws IllegalValueException {
        if (!Appointment.isValidAppointmentDate(this.date)) {
            throw new IllegalValueException(Appointment.MESSAGE_APPOINTMENT_DATE_CONSTRAINTS);
        }

        if (!Appointment.isValidAppointmentStartTime(this.startTime)) {
            throw new IllegalValueException(Appointment.MESSAGE_APPOINTMENT_START_TIME_CONSTRAINTS);
        }

        if (!Appointment.isValidAppointmentEndTime(this.endTime)) {
            throw new IllegalValueException(Appointment.MESSAGE_APPOINTMENT_END_TIME_CONSTRAINTS);
        }

        return new Appointment(name, info, date, startTime, endTime);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedAppointment)) {
            return false;
        }

        XmlAdaptedAppointment otherAppointment = (XmlAdaptedAppointment) other;
        return Objects.equals(info, otherAppointment.info)
                && Objects.equals(date, otherAppointment.date)
                && Objects.equals(startTime, otherAppointment.startTime)
                && Objects.equals(endTime, otherAppointment.endTime);
    }
}
```
###### \java\seedu\address\ui\AppointmentCard.java
``` java
/**
 * An UI component that displays information of an {@code Appointment}.
 */
public class AppointmentCard extends UiPart<Region> {

    private static final String FXML = "AppointmentListCard.fxml";

    public final Appointment appointment;

    @FXML
    private HBox cardPane;
    @FXML
    private Label id;
    @FXML
    private Label name;
    @FXML
    private Label info;
    @FXML
    private Label date;
    @FXML
    private Label startTime;
    @FXML
    private Label endTime;

    public AppointmentCard(Appointment appointment, int displayedIndex) {
        super(FXML);
        this.appointment = appointment;
        id.setText(displayedIndex + ". ");
        info.setText(appointment.getInfo());
        name.setText("Name: " + appointment.getName());
        date.setText("Date: " + appointment.getDate());
        startTime.setText("Start Time: " + appointment.getStartTime());
        endTime.setText("End Time:  " + appointment.getEndTime());
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AppointmentCard)) {
            return false;
        }

        // state check
        AppointmentCard card = (AppointmentCard) other;
        return id.getText().equals(card.id.getText())
                && appointment.equals(card.appointment);
    }
}
```
###### \java\seedu\address\ui\AppointmentListPanel.java
``` java
/**
 * Panel containing the list of appointments
 */
public class AppointmentListPanel extends UiPart<Region> {
    private static final String FXML = "AppointmentListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(AppointmentListPanel.class);

    @FXML
    private ListView<AppointmentCard> appointmentListView;

    public AppointmentListPanel(ObservableList<Appointment> appointmentList) {
        super(FXML);
        setConnections(appointmentList);
        registerAsAnEventHandler(this);
    }

    private void setConnections(ObservableList<Appointment> appointmentList) {
        ObservableList<AppointmentCard> mappedList = EasyBind.map(
                appointmentList, (appointment) -> new AppointmentCard(
                        appointment, appointmentList.indexOf(appointment) + 1));
        appointmentListView.setItems(mappedList);
        appointmentListView.setCellFactory(listView -> new AppointmentListViewCell());
        setEventHandlerForSelectionChangeEvent();
    }

    private void setEventHandlerForSelectionChangeEvent() {
        appointmentListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        logger.fine("Selection in appointment list panel changed to : '" + newValue + "'");
                        raise(new AppointmentPanelSelectionChangedEvent(newValue));
                    }
                });
    }

    /**
     * Scrolls to the {@code AppointmentCard} at the {@code index} and selects it.
     */
    private void scrollTo(int index) {
        Platform.runLater(() -> {
            appointmentListView.scrollTo(index);
            appointmentListView.getSelectionModel().clearAndSelect(index);
        });
    }

    @Subscribe
    private void handleJumpToListRequestEvent(JumpToListRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        scrollTo(event.targetIndex);
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code AppointmentCard}.
     */
    class AppointmentListViewCell extends ListCell<AppointmentCard> {

        @Override
        protected void updateItem(AppointmentCard appointment, boolean empty) {
            super.updateItem(appointment, empty);

            if (empty || appointment == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(appointment.getRoot());
            }
        }
    }
}
```
###### \java\seedu\address\ui\CalendarDisplay.java
``` java
/**
 * Calendar of the App
 */
public class CalendarDisplay {

    private static final String APPLICATION_NAME =  "Google Calendar API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static FileDataStoreFactory DATA_STORE_FACTORY;
    private static HttpTransport HTTP_TRANSPORT;

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("user.home"), ".credentials/calendar-java-quickstart");

    /** Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/calendar-java-quickstart
     */
    private static final List<String> SCOPES = Arrays.asList(CalendarScopes.CALENDAR);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     * @return an authorized Credential object.
     * @throws IOException
     */
    private Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in = Calendar.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets =  GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("offline")
                .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");
        return credential;
    }

    /**
     * Build and return an authorized Calendar client service.
     * @return an authorized Calendar client service
     * @throws IOException
     */
    private Calendar getCalendarService() throws IOException {
        Credential credential = authorize();
        return new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /**
     * Creates an event on the Google Calendar
     * @throws IOException
     */
    public void createEvent(Appointment toAdd, String id) throws IOException {
        Calendar service = getCalendarService();

        Event event = new Event().setSummary(toAdd.getName());

        event.setId(String.valueOf(id));

        DateTime startDateTime = new DateTime(formattedStartDateTime(toAdd));
        EventDateTime start = new EventDateTime().setDateTime(startDateTime);
        event.setStart(start);

        DateTime endDateTime = new DateTime(formattedEndDateTime(toAdd));
        EventDateTime end = new EventDateTime().setDateTime(endDateTime);
        event.setEnd(end);

        service.events().insert("primary", event).execute();
    }

    /**
     * Removes an event on the Google Calendar
     * @throws IOException
     */
    public void removeEvent(String id) throws IOException {
        Calendar service = getCalendarService();

        service.events().delete("primary", id).execute();
    }

    /**
     * Sets the required format for start time
     * @param toAdd appointment details provided by the user
     * @return the start time in the required format
     */
    private String formattedStartDateTime(Appointment toAdd) {
        String date = toAdd.getDate();
        String startTime = toAdd.getStartTime();
        return (date.substring(4, 8)).concat("-").concat(date.substring(2, 4)).concat("-")
                .concat(date.substring(0, 2)).concat("T").concat(startTime.substring(0, 2))
                .concat(":").concat(startTime.substring(2, 4)).concat(":00+08:00");
    }

    /**
     * Sets the required format for end time
     * @param toAdd appointment details provided by the user
     * @return the end time in the required format
     */
    private String formattedEndDateTime(Appointment toAdd) {
        String date = toAdd.getDate();
        String endTime = toAdd.getEndTime();
        return (date.substring(4, 8)).concat("-").concat(date.substring(2, 4)).concat("-")
                .concat(date.substring(0, 2)).concat("T").concat(endTime.substring(0, 2))
                .concat(":").concat(endTime.substring(2, 4)).concat(":00+08:00");
    }
}
```
###### \java\seedu\address\ui\PersonCard.java
``` java
    /**
     * Generates different colors for different tag labels
     *
     * @param tagName
     * @return a color style that is listed under tagColors array
     */
    private String generateTagColor(String tagName) {
        return tagColors[Math.abs(tagName.hashCode()) % tagColors.length];
    }

    /**
     * Creates the tag labels for {@code person} with different colors assigned to different tag names
     */
    private void setTags(Person person) {
        person.getTags().forEach(tag -> {
            Label tagLabel = new Label(tag.tagName);
            tagLabel.getStyleClass().add(generateTagColor(tag.tagName));
            tags.getChildren().add(tagLabel);
        });
    }

```
###### \resources\view\AppointmentListCard.fxml
``` fxml
<HBox id="cardPane" fx:id="cardPane" xmlns="http://javafx.com/javafx/8" xmlns:fx="http://javafx.com/fxml/1">
    <GridPane HBox.hgrow="ALWAYS">
        <columnConstraints>
            <ColumnConstraints hgrow="SOMETIMES" minWidth="10" prefWidth="150" />
        </columnConstraints>
        <VBox alignment="CENTER_LEFT" minHeight="60" GridPane.columnIndex="0">
            <padding>
                <Insets top="5" right="5" bottom="5" left="15" />
            </padding>
            <HBox spacing="5" alignment="CENTER_LEFT">
                <Label fx:id="id" styleClass="cell_big_label">
                    <minWidth>
                        <!-- Ensures that the label text is never truncated -->
                        <Region fx:constant="USE_PREF_SIZE" />
                    </minWidth>
                </Label>
                <Label fx:id="info" text="\$first" styleClass="cell_big_label" />
            </HBox>
            <Label fx:id="name" text="\$name" styleClass="cell_small_label"  />
            <Label fx:id="date" text="\$date" styleClass="cell_small_label"  />
            <Label fx:id="startTime" text="\$startTime" styleClass="cell_small_label"  />
            <Label fx:id="endTime" text="\$endTime" styleClass="cell_small_label" />
        </VBox>
    </GridPane>
</HBox>
```
###### \resources\view\AppointmentListPanel.fxml
``` fxml
<VBox xmlns="http://javafx.com/javafx/8" xmlns:fx="http://javafx.com/fxml/1">
    <ListView fx:id="appointmentListView" VBox.vgrow="ALWAYS"/>
</VBox>
```
###### \resources\view\CalendarDisplay.html
``` html
<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="stylesheet" href="LightTheme.css">
    <meta http-equiv="refresh" content="1;url=https://calendar.google.com/calendar/embed?src=edubuddytest%40gmail.com&ctz=Asia%2FSingapore">
</head>
<body class="background">
</body>
</html>
```
