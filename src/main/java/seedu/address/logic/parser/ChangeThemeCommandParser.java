
package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.model.ThemeColourUtil.getThemeHashMap;

import java.util.HashMap;

import seedu.address.logic.commands.ChangeThemeCommand;
import seedu.address.logic.parser.exceptions.ParseException;

//@@author johnnychanjx
/**
 * Parses input arguments and creates a new ChangeThemeCommand object
 */
public class ChangeThemeCommandParser implements Parser<ChangeThemeCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the ChangeThemeCommand
     * and returns a ThemeCommand object for execution.
     * @throws ParseException if the user input does not use the expected format
     */
    public ChangeThemeCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ChangeThemeCommand.MESSAGE_USAGE));
        }
        if (!isValidThemeColour(trimmedArgs)) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ChangeThemeCommand.MESSAGE_INVALID_THEME_COLOUR));

        }
        return new ChangeThemeCommand(trimmedArgs);
    }

    /**
     *
     * @param themeColour
     * @return
     */
    private boolean isValidThemeColour(String themeColour) {
        HashMap<String, String> themes = getThemeHashMap();
        if (themes.containsKey(themeColour.toLowerCase())) {
            return true;
        } else {
            return false;
        }

    }
}
//@@author

