# johnnychanjx
###### \java\seedu\address\logic\commands\ChangeThemeCommandParserTest.java
``` java

public class ChangeThemeCommandParserTest {
    private ChangeThemeCommandParser parser = new ChangeThemeCommandParser();

    @Test
    public void parse_validArgs_returnsThemeCommand() {
        assertParseSuccess(parser, "dark", new ChangeThemeCommand("dark"));
    }

    @Test
    public void parser_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "invalid", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ChangeThemeCommand.MESSAGE_INVALID_THEME_COLOUR));

        assertParseFailure(parser, "", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ChangeThemeCommand.MESSAGE_USAGE));
    }
}

```
###### \java\seedu\address\logic\commands\ChangeThemeCommandTest.java
``` java

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




```
