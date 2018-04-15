
package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.EventsCenter;
import seedu.address.commons.events.ui.ThemeSwitchRequestEvent;

//@@author johnnychanjx
/**
 * Changes the EduBuddy colour theme
 */
public class ChangeThemeCommand extends Command {
    public static final String COMMAND_WORD = "theme";

    public static final String MESSAGE_SUCCESS = "Theme changed!";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Changes the theme to the theme word entered.\n"
            + "Parameters: COLOUR THEME\n"
            + "(Available Themes: dark, light)\n"
            + "Example: " + COMMAND_WORD + " dark\n";
    public static final String MESSAGE_INVALID_THEME_COLOUR = "Invalid theme color. \n"
            + "(Themes: dark, light)\n";
    private final String themeToChangeTo;

    /**
     * Creates a ThemeCommand based on the specified themeColour.
     */
    public ChangeThemeCommand (String themeToChangeTo) {
        requireNonNull(themeToChangeTo);
        this.themeToChangeTo = themeToChangeTo;
    }

    @Override
    public CommandResult execute() {

        EventsCenter.getInstance().post(new ThemeSwitchRequestEvent(themeToChangeTo));
        return new CommandResult(String.format(MESSAGE_SUCCESS, themeToChangeTo));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ChangeThemeCommand // instanceof handles nulls
                && themeToChangeTo.equals(((ChangeThemeCommand) other).themeToChangeTo));
    }
    //@@author
}

