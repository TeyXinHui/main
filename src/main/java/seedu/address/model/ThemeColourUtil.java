
package seedu.address.model;

import java.util.HashMap;

/**
 * //@@author johnnychanjx
 * Util for Theme Selection
 */
public class ThemeColourUtil {

    private static final HashMap<String, String> themes;


    static {
        themes = new HashMap<>();
        themes.put("light", "view/LightTheme.css");
        themes.put("dark", "view/DarkTheme.css");
    }

    public static HashMap<String, String> getThemeHashMap() {
        return themes;
    }
}

