package seedu.address.ui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import javafx.scene.web.WebView;
import seedu.address.MainApp;
import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.events.ui.DisplayCalendarRequestEvent;
import seedu.address.commons.events.ui.PersonPanelSelectionChangedEvent;
import seedu.address.logic.commands.ViewCommand;
import seedu.address.model.person.Person;

/**
 * The Browser Panel of the App.
 */
public class BrowserPanel extends UiPart<Region> {

    public static final String DEFAULT_PAGE = "default.html";
    public static final String SEARCH_PAGE_URL = "https"
           + "://se-edu.github.io/addressbook-level4/DummySearchPage.html?name=";
    public static final String PERSON_PAGE = ".html";
    public static final String PROFILE_DIRECTORY = "/StudentPage/";
    private static final String FXML = "BrowserPanel.fxml";

    private final Logger logger = LogsCenter.getLogger(this.getClass());

    private ViewCommand viewCommand;

    @FXML
    private WebView browser;

    public BrowserPanel() {
        super(FXML);

        // To prevent triggering events for typing inside the loaded Web page.
        getRoot().setOnKeyPressed(Event::consume);
        loadDefaultPage();
        registerAsAnEventHandler(this);
    }

    /**
     * @@author johnnychanjx
    * To load person page according to person name
    */
    public void loadPersonPage(Person person) throws IOException {
        URL personPage = MainApp.class.getResource(File.separator + "StudentPage" + File.separator
                + "template.html");
        loadPage("file:" + System.getProperty("user.home") + File.separator + "StudentPage"
                + File.separator + person.getName() + ".html");
    }
    //@@author johnnychanjx
    public void loadPage(String url) {
        Platform.runLater(() -> browser.getEngine().load(url));
    }

    /**
     * Loads a default HTML file with a background that matches the general theme.
     */
    public void loadDefaultPage() {
        URL defaultPage = MainApp.class.getResource(FXML_FILE_FOLDER + DEFAULT_PAGE);
        loadPage(defaultPage.toExternalForm());
    }
    //@@author

    /**
     * Loads the Google Calendar page
     */
    public void loadCalendarPage(String parameter) {
        loadPage("https://calendar.google.com/calendar/embed?src=" + parameter
                + "%40gmail.com&ctz=Asia%2FSingapore");
    }

    /**
     * Frees resources allocated to the browser.
     */
    public void freeResources() {
        browser = null;
    }

    @Subscribe
    private void handlePersonPanelSelectionChangedEvent(PersonPanelSelectionChangedEvent event) throws IOException {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadPersonPage(event.getNewSelection().person);
    }

    @Subscribe
    private void handleDisplayCalendarEvent(DisplayCalendarRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        loadCalendarPage(event.toString());
    }
}
