package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;

//@@author kengsengg
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
//@@author
