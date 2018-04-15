package seedu.address.logic.commands;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static seedu.address.logic.commands.ChangeThemeCommand.MESSAGE_SUCCESS;

import org.junit.Rule;
import org.junit.Test;

import seedu.address.commons.events.ui.ThemeSwitchRequestEvent;
import seedu.address.ui.testutil.EventsCollectorRule;


//@@author johnnychanjx

public class ChangeThemeCommandTest {

    private static final String VALID_THEME = "light";

    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    @Test
    public void execute_themeSwitch_success() {
        CommandResult result = new ChangeThemeCommand(VALID_THEME).execute();
        assertEquals(MESSAGE_SUCCESS, result.feedbackToUser);
        assertTrue(eventsCollectorRule.eventsCollector.getMostRecent() instanceof ThemeSwitchRequestEvent);
        assertTrue(eventsCollectorRule.eventsCollector.getSize() == 1);
    }

}




