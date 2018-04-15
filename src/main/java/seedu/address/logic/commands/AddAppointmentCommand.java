package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_END_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INFO;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_START_TIME;

import java.io.IOException;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.appointment.exceptions.DuplicateAppointmentException;
import seedu.address.ui.CalendarDisplay;

//@@author kengsengg
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
//@@author
