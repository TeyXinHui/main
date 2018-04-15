package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.util.List;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.appointment.exceptions.AppointmentNotFoundException;
import seedu.address.ui.CalendarDisplay;

//@@author kengsengg
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
//@@author
