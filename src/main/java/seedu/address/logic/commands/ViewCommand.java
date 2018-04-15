package seedu.address.logic.commands;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.DisplayCalendarRequestEvent;

//@@author kengsengg
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
//@@author
