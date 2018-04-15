# johnnychanjx
###### \java\seedu\address\commons\events\ui\ThemeSwitchRequestEvent.java
``` java
/**
 * Indicates that a theme switch is called.
 */
public class ThemeSwitchRequestEvent extends BaseEvent {
    public final String themeToChangeTo;

    public ThemeSwitchRequestEvent(String themeToChangeTo) {
        this.themeToChangeTo = themeToChangeTo;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
}

```
###### \java\seedu\address\logic\commands\ChangeThemeCommand.java
``` java
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
```
###### \java\seedu\address\logic\parser\ChangeThemeCommandParser.java
``` java
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
```
###### \java\seedu\address\model\Model.java
``` java
    /** Deletes the Person's page */
    void deletePage(Person target);
```
###### \java\seedu\address\model\Model.java
``` java
    /** Adds the given person's page*/
    void addPage(Person person) throws IOException;
```
###### \java\seedu\address\model\ModelManager.java
``` java
     * @param person
     * @throws IOException
     * Adds a BrowserPanel html Page into StudentPage
     *
     */

    public void addPage(Person person) throws IOException {

        String userHome = System.getProperty("user.home") + File.separator + "StudentPage";
        String locatie = userHome;
        File folder = new File(locatie);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        URL personPage = MainApp.class.getResource(PROFILE_DIRECTORY + "template.html");

        String htmlString = Resources.toString(personPage, Charsets.UTF_8);

        File f = new File(System.getProperty("user.home") + File.separator + "StudentPage"
                + File.separator + person.getName() + ".html");
        if (!f.exists()) {
            f.createNewFile();
        }

        Name titleName = person.getName();
        String title = titleName.toString();
        htmlString = htmlString.replace("$title", title);

        Nric identityNumberClass = person.getNric();
        String identityNumber = identityNumberClass.toString();
        htmlString = htmlString.replace("$identityNumber", identityNumber);

        List<Tag> tagList = person.getTagArray();
        int taglistSize = tagList.size();
        if (taglistSize != 0) {
            htmlString = htmlString.replace("Class Not Specified", tagList.get(0).tagForBrowser());
        }

        List<Subject> subjectList = person.getSubjectArray();
        int listSize = subjectList.size();
        System.out.println(person.getSubjects());
        int i = 0;
        while (i < listSize) {
            String iString = Integer.toString(i + 1);
            htmlString = htmlString.replace("Subject " + iString, subjectList.get(i).nameToString());
            htmlString = htmlString.replace("$percent" + iString, subjectList.get(i).gradeToPercent());
            htmlString = htmlString.replace("Grade " + iString, subjectList.get(i).gradeToString());
            i++;
        }

        // ADD L1R5

        int score = person.calculateL1R5();
        String scoreString = "-";
        if (score == 0) {
            scoreString = "-";
        } else {
            scoreString = Integer.toString(score);
        }
        htmlString = htmlString.replace("STUDENTS SCORE", scoreString);

        // L1B4A
        int scoreL1B4A = person.calculateL1B4A();
        String scoreL1B4AString = "-";
        if (scoreL1B4A == 0) {
            scoreL1B4AString = "-";
        } else {
            scoreL1B4AString = Integer.toString(scoreL1B4A);
        }
        htmlString = htmlString.replace("STUDENTS L1B4A", scoreL1B4AString);

        //L1B4B

        int scoreL1B4B = person.calculateL1B4B();
        String scoreL1B4BString = "-";
        if (scoreL1B4B == 0) {
            scoreL1B4BString = "-";
        } else {
            scoreL1B4BString = Integer.toString(scoreL1B4B);
        }
        htmlString = htmlString.replace("STUDENTS L1B4B", scoreL1B4BString);

        //L1B4C
        int scoreL1B4C = person.calculateL1B4C();
        String scoreL1B4CString = "-";
        if (scoreL1B4C == 0) {
            scoreL1B4CString = "-";
        } else {
            scoreL1B4CString = Integer.toString(scoreL1B4C);
        }
        htmlString = htmlString.replace("STUDENTS L1B4C", scoreL1B4CString);

        //L1B4D
        int scoreL1B4D = person.calculateL1B4D();
        String scoreL1B4DString = "-";
        if (scoreL1B4D == 0) {
            scoreL1B4DString = "-";
        } else {
            scoreL1B4DString = Integer.toString(scoreL1B4D);
        }
        htmlString = htmlString.replace("STUDENTS L1B4D", scoreL1B4DString);

        // ADD CCA
        String ccaString = person.getCca().getValue();
        htmlString = htmlString.replace("CCA", ccaString);

        //ADD CCA Rank

        String ccaRank = person.getCca().getPos();
        htmlString = htmlString.replace("STUDENT RANK", ccaRank);

        // ADD REMARK

        String remark = person.getRemark().toString();
        htmlString = htmlString.replace("Remarks to facilitate teaching should be included here.", remark);

        //ADD INJURY
        String injury = person.getInjuriesHistory().toString();
        htmlString = htmlString.replace("Insert injury history here", injury);

        // NOK Details

        String nokName = person.getNextOfKin().fullName;
        htmlString = htmlString.replace("NOK Name", nokName);
        String nokGender = person.getNextOfKin().remark;
        htmlString = htmlString.replace("NOK Gender", nokGender);
        String nokEmail = person.getNextOfKin().email;
        htmlString = htmlString.replace("NOK Email", nokEmail);
        String nokPhone = person.getNextOfKin().phone;
        htmlString = htmlString.replace("NOK Phone", nokPhone);

        BufferedWriter bw = new BufferedWriter(new FileWriter(f));
        bw.write(htmlString);
        bw.close();

    }
```
###### \java\seedu\address\model\ModelManager.java
``` java
     * Deletes BrowserPanel html
     */
    public void deletePage(Person person) {

        File f = new File(System.getProperty("user.home") + File.separator + "StudentPage"
                + File.separator + person.getName() + ".html");
        boolean bool = f.delete();
    }
```
###### \java\seedu\address\model\subject\Subject.java
``` java
    public String gradeToString() {
        return subjectGrade;
    }

    /**
```
###### \java\seedu\address\model\subject\Subject.java
``` java
     * Returns grade in number form for html bar
     */
    public String gradeToPercent() {
        int percent;

        switch (subjectGrade) {
        case "A1": percent = 100;
                break;
        case "A2": percent = 90;
                break;
        case "B3": percent = 80;
                break;
        case "B4": percent = 70;
                break;
        case "C5": percent = 60;
                break;
        case "C6": percent = 50;
                break;
        case "D7": percent = 40;
                break;
        case "E8": percent = 30;
                break;
        case "F9": percent = 10;
                break;
        default: percent = 0;
                break;
        }

        String percentString = Integer.toString(percent);
        return percentString;
    }
```
###### \java\seedu\address\model\tag\Tag.java
``` java
     * Returns tag name without [] for Browser Panel
     * @return
     */
    public String tagForBrowser() {
        return tagName;
    }
}
```
###### \java\seedu\address\model\ThemeColourUtil.java
``` java
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

```
###### \java\seedu\address\storage\AddressBookStorage.java
``` java
    void backupAddressBook(ReadOnlyAddressBook addressBook) throws IOException;
```
###### \java\seedu\address\storage\StorageManager.java
``` java
    public void backupAddressBook(ReadOnlyAddressBook addressBook) throws IOException {
        addressBookStorage.backupAddressBook(addressBook);
    }

    //@author
    @Override
    @Subscribe
    public void handleAddressBookChangedEvent(AddressBookChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
            saveAddressBook(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }

}
```
###### \java\seedu\address\storage\XmlAddressBookStorage.java
``` java
    @Override
    public void backupAddressBook(ReadOnlyAddressBook addressBook) throws IOException {
        saveAddressBook(addressBook, filePath + ".backup");

    }
```
###### \java\seedu\address\ui\BrowserPanel.java
``` java
    * To load person page according to person name
    */
    public void loadPersonPage(Person person) throws IOException {
        URL personPage = MainApp.class.getResource(File.separator + "StudentPage" + File.separator
                + "template.html");
        loadPage("file:" + System.getProperty("user.home") + File.separator + "StudentPage"
                + File.separator + person.getName() + ".html");
    }
```
###### \java\seedu\address\ui\BrowserPanel.java
``` java
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
```
###### \resources\StudentPage\Caleb.html
``` html
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel='stylesheet' href='https://fonts.googleapis.com/css?family=Helvetica'>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">


<style>
html,body,h1,h2,h3,h4,h5,h6 {font-family: "Helvetica", sans-serif}
</style>
<body class="w3-light-grey">

<!-- Page Container -->
<div class="w3-content w3-margin-top" style="max-width:1400px;">

    <!-- The Grid -->
    <div class="w3-row-padding">

        <!-- Left Column -->
        <div class="w3-third">

            <div class="w3-white w3-text-grey w3-card-4">
                <div class="w3-display-container">
                    <img src="../docs/images/defaultface.png" style="width:100%" alt="Avatar">

                </div>
                <div class="w3-container">
                    <p><i class="fa fa-address-book fa-fw w3-margin-right w3-large w3-text-teal"></i>Caleb</p>
                    <p><i class="fa fa-folder-o fa-fw w3-margin-right w3-large w3-text-teal"></i>S9867456H</p>
                    <p><i class="fa fa-file  fa-fw w3-margin-right w3-large w3-text-teal"></i>[[1A]]</p>
                    <p><i class="fa fa-drivers-license-o fa-fw w3-margin-right w3-large w3-text-teal"></i>No Result Information</p>
                    <hr>



                    <p class="w3-large"><b><i class="fa fa-asterisk fa-fw w3-margin-right w3-text-teal"></i>Current Subjects</b></p>
                    <p>Subject 1</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent1%">Grade 1</div>
                    </div>
                    <p>Subject 2</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent2%">
                            <div class="w3-center w3-text-white">Grade 2</div>
                        </div>
                    </div>
                    <p>Subject 3</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent3%">Grade 3</div>
                    </div>
                    <p>Subject 4</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent4%">Grade 4</div>
                    </div>
                    <p>Subject 5</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent5%">Grade 5</div>
                    </div>
                    <p>Subject 6</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent6%">Grade 6</div>
                    </div>

                    <hr>
                </div>
            </div><br>

            <!-- End Left Column -->
        </div>

        <!-- Right Column -->
        <div class="w3-twothird">

            <div class="w3-container w3-card w3-white w3-margin-bottom">



                <h2 class="w3-text-grey w3-padding-16"><i class="fa fa-address-card-o fa-fw w3-margin-right w3-xxlarge w3-text-teal"></i>Next of Kin Details</h2>
                <div class="w3-container">
                    <p><i class="fa fa-address-book fa-fw w3-margin-right w3-large w3-text-teal"></i>Jonathan Doe</p>
                    <p><i class="fa fa-genderless fa-fw w3-margin-right w3-large w3-text-teal"></i>Father</p>
                    <p><i class="fa fa-envelope fa-fw w3-margin-right w3-large w3-text-teal"></i>jonathandoe@company.com.sg</p>
                    <p><i class="fa fa-phone fa-fw w3-margin-right w3-large w3-text-teal"></i>+65 96993220</p>

                </div>







                <h2 class="w3-text-grey w3-padding-16"><i class="fa fa-suitcase fa-fw w3-margin-right w3-xxlarge w3-text-teal"></i>Co Curricular Activities</h2>
                <div class="w3-container">
                    <h5 class="w3-opacity"><b>Basketball: Member</b></h5>
                    <p>Head</p>
                </div>
                <br>
                <br>


                <div class="w3-container w3-card w3-white">
                    <h2 class="w3-text-grey w3-padding-16"><i class="fa fa-certificate fa-fw w3-margin-right w3-xxlarge w3-text-teal"></i>Teacher's Remarks</h2>
                    <div class="w3-container">
                        <p></p>
                    </div>
                    <br>
                </div>
                <br>



                <div class="w3-container w3-card w3-white">
                    <h2 class="w3-text-grey w3-padding-16"><i class="fa fa-certificate fa-fw w3-margin-right w3-xxlarge w3-text-teal"></i>Injury History</h2>
                    <div class="w3-container">
                        <p></p>
                    </div>
                   <br>
                </div>
                <br>


                <iframe src="https://calendar.google.com/calendar/embed?src=edubuddytest%40gmail.com&ctz=Asia%2FSingapore'
" style="border-width:0" width="600" height="600" frameborder="0" scrolling="no"></iframe>

                <!-- End Right Column -->

            </div>

            <!-- End Grid -->
        </div>

        <!-- End Page Container -->
    </div>

    <footer>
        <p>EduBuddy, Powered by<a href="https://www.w3schools.com/w3css/default.asp" target="_blank">w3.css</a></p>
    </footer>

</body>
</html>
```
###### \resources\StudentPage\Deb.html
``` html
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel='stylesheet' href='https://fonts.googleapis.com/css?family=Helvetica'>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">


<style>
html,body,h1,h2,h3,h4,h5,h6 {font-family: "Helvetica", sans-serif}
</style>
<body class="w3-light-grey">

<!-- Page Container -->
<div class="w3-content w3-margin-top" style="max-width:1400px;">

    <!-- The Grid -->
    <div class="w3-row-padding">

        <!-- Left Column -->
        <div class="w3-third">

            <div class="w3-white w3-text-grey w3-card-4">
                <div class="w3-display-container">
                    <img src="../docs/images/defaultface.png" style="width:100%" alt="Avatar">

                </div>
                <div class="w3-container">
                    <p><i class="fa fa-address-book fa-fw w3-margin-right w3-large w3-text-teal"></i>Deb</p>
                    <p><i class="fa fa-folder-o fa-fw w3-margin-right w3-large w3-text-teal"></i>S9856345Z</p>
                    <p><i class="fa fa-file  fa-fw w3-margin-right w3-large w3-text-teal"></i>[[1A]]</p>
                    <p><i class="fa fa-drivers-license-o fa-fw w3-margin-right w3-large w3-text-teal"></i>No Result Information</p>
                    <hr>



                    <p class="w3-large"><b><i class="fa fa-asterisk fa-fw w3-margin-right w3-text-teal"></i>Current Subjects</b></p>
                    <p>Subject 1</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent1%">Grade 1</div>
                    </div>
                    <p>Subject 2</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent2%">
                            <div class="w3-center w3-text-white">Grade 2</div>
                        </div>
                    </div>
                    <p>Subject 3</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent3%">Grade 3</div>
                    </div>
                    <p>Subject 4</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent4%">Grade 4</div>
                    </div>
                    <p>Subject 5</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent5%">Grade 5</div>
                    </div>
                    <p>Subject 6</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent6%">Grade 6</div>
                    </div>

                    <hr>
                </div>
            </div><br>

            <!-- End Left Column -->
        </div>

        <!-- Right Column -->
        <div class="w3-twothird">

            <div class="w3-container w3-card w3-white w3-margin-bottom">



                <h2 class="w3-text-grey w3-padding-16"><i class="fa fa-address-card-o fa-fw w3-margin-right w3-xxlarge w3-text-teal"></i>Next of Kin Details</h2>
                <div class="w3-container">
                    <p><i class="fa fa-address-book fa-fw w3-margin-right w3-large w3-text-teal"></i>Jonathan Doe</p>
                    <p><i class="fa fa-genderless fa-fw w3-margin-right w3-large w3-text-teal"></i>Father</p>
                    <p><i class="fa fa-envelope fa-fw w3-margin-right w3-large w3-text-teal"></i>jonathandoe@company.com.sg</p>
                    <p><i class="fa fa-phone fa-fw w3-margin-right w3-large w3-text-teal"></i>+65 96993220</p>

                </div>







                <h2 class="w3-text-grey w3-padding-16"><i class="fa fa-suitcase fa-fw w3-margin-right w3-xxlarge w3-text-teal"></i>Co Curricular Activities</h2>
                <div class="w3-container">
                    <h5 class="w3-opacity"><b>: </b></h5>
                    <p>Head</p>
                </div>
                <br>
                <br>


                <div class="w3-container w3-card w3-white">
                    <h2 class="w3-text-grey w3-padding-16"><i class="fa fa-certificate fa-fw w3-margin-right w3-xxlarge w3-text-teal"></i>Teacher's Remarks</h2>
                    <div class="w3-container">
                        <p></p>
                    </div>
                    <br>
                </div>
                <br>



                <div class="w3-container w3-card w3-white">
                    <h2 class="w3-text-grey w3-padding-16"><i class="fa fa-certificate fa-fw w3-margin-right w3-xxlarge w3-text-teal"></i>Injury History</h2>
                    <div class="w3-container">
                        <p></p>
                    </div>
                   <br>
                </div>
                <br>


                <iframe src="https://calendar.google.com/calendar/embed?src=edubuddytest%40gmail.com&ctz=Asia%2FSingapore'
" style="border-width:0" width="600" height="600" frameborder="0" scrolling="no"></iframe>

                <!-- End Right Column -->

            </div>

            <!-- End Grid -->
        </div>

        <!-- End Page Container -->
    </div>

    <footer>
        <p>EduBuddy, Powered by<a href="https://www.w3schools.com/w3css/default.asp" target="_blank">w3.css</a></p>
    </footer>

</body>
</html>
```
###### \resources\StudentPage\John.html
``` html
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel='stylesheet' href='https://fonts.googleapis.com/css?family=Helvetica'>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">


<style>
html,body,h1,h2,h3,h4,h5,h6 {font-family: "Helvetica", sans-serif}
</style>
<body class="w3-light-grey">

<!-- Page Container -->
<div class="w3-content w3-margin-top" style="max-width:1400px;">

    <!-- The Grid -->
    <div class="w3-row-padding">

        <!-- Left Column -->
        <div class="w3-third">

            <div class="w3-white w3-text-grey w3-card-4">
                <div class="w3-display-container">
                    <img src="../docs/images/defaultface.png" style="width:100%" alt="Avatar">

                </div>
                <div class="w3-container">
                    <p><i class="fa fa-address-book fa-fw w3-margin-right w3-large w3-text-teal"></i>John</p>
                    <p><i class="fa fa-folder-o fa-fw w3-margin-right w3-large w3-text-teal"></i>S9786456H</p>
                    <p><i class="fa fa-file  fa-fw w3-margin-right w3-large w3-text-teal"></i>[[1A]]</p>
                    <p><i class="fa fa-drivers-license-o fa-fw w3-margin-right w3-large w3-text-teal"></i>No Result Information</p>
                    <hr>



                    <p class="w3-large"><b><i class="fa fa-asterisk fa-fw w3-margin-right w3-text-teal"></i>Current Subjects</b></p>
                    <p>Subject 1</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent1%">Grade 1</div>
                    </div>
                    <p>Subject 2</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent2%">
                            <div class="w3-center w3-text-white">Grade 2</div>
                        </div>
                    </div>
                    <p>Subject 3</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent3%">Grade 3</div>
                    </div>
                    <p>Subject 4</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent4%">Grade 4</div>
                    </div>
                    <p>Subject 5</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent5%">Grade 5</div>
                    </div>
                    <p>Subject 6</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent6%">Grade 6</div>
                    </div>

                    <hr>
                </div>
            </div><br>

            <!-- End Left Column -->
        </div>

        <!-- Right Column -->
        <div class="w3-twothird">

            <div class="w3-container w3-card w3-white w3-margin-bottom">



                <h2 class="w3-text-grey w3-padding-16"><i class="fa fa-address-card-o fa-fw w3-margin-right w3-xxlarge w3-text-teal"></i>Next of Kin Details</h2>
                <div class="w3-container">
                    <p><i class="fa fa-address-book fa-fw w3-margin-right w3-large w3-text-teal"></i>Jonathan Doe</p>
                    <p><i class="fa fa-genderless fa-fw w3-margin-right w3-large w3-text-teal"></i>Father</p>
                    <p><i class="fa fa-envelope fa-fw w3-margin-right w3-large w3-text-teal"></i>jonathandoe@company.com.sg</p>
                    <p><i class="fa fa-phone fa-fw w3-margin-right w3-large w3-text-teal"></i>+65 96993220</p>

                </div>







                <h2 class="w3-text-grey w3-padding-16"><i class="fa fa-suitcase fa-fw w3-margin-right w3-xxlarge w3-text-teal"></i>Co Curricular Activities</h2>
                <div class="w3-container">
                    <h5 class="w3-opacity"><b>: </b></h5>
                    <p>Head</p>
                </div>
                <br>
                <br>


                <div class="w3-container w3-card w3-white">
                    <h2 class="w3-text-grey w3-padding-16"><i class="fa fa-certificate fa-fw w3-margin-right w3-xxlarge w3-text-teal"></i>Teacher's Remarks</h2>
                    <div class="w3-container">
                        <p></p>
                    </div>
                    <br>
                </div>
                <br>



                <div class="w3-container w3-card w3-white">
                    <h2 class="w3-text-grey w3-padding-16"><i class="fa fa-certificate fa-fw w3-margin-right w3-xxlarge w3-text-teal"></i>Injury History</h2>
                    <div class="w3-container">
                        <p></p>
                    </div>
                   <br>
                </div>
                <br>


                <iframe src="https://calendar.google.com/calendar/embed?src=edubuddytest%40gmail.com&ctz=Asia%2FSingapore'
" style="border-width:0" width="600" height="600" frameborder="0" scrolling="no"></iframe>

                <!-- End Right Column -->

            </div>

            <!-- End Grid -->
        </div>

        <!-- End Page Container -->
    </div>

    <footer>
        <p>EduBuddy, Powered by<a href="https://www.w3schools.com/w3css/default.asp" target="_blank">w3.css</a></p>
    </footer>

</body>
</html>
```
###### \resources\StudentPage\Johnny.html
``` html
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel='stylesheet' href='https://fonts.googleapis.com/css?family=Helvetica'>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">


<style>
html,body,h1,h2,h3,h4,h5,h6 {font-family: "Helvetica", sans-serif}
</style>
<body class="w3-light-grey">

<!-- Page Container -->
<div class="w3-content w3-margin-top" style="max-width:1400px;">

    <!-- The Grid -->
    <div class="w3-row-padding">

        <!-- Left Column -->
        <div class="w3-third">

            <div class="w3-white w3-text-grey w3-card-4">
                <div class="w3-display-container">
                    <img src="../docs/images/defaultface.png" style="width:100%" alt="Avatar">

                </div>
                <div class="w3-container">
                    <p><i class="fa fa-address-book fa-fw w3-margin-right w3-large w3-text-teal"></i>Johnny</p>
                    <p><i class="fa fa-folder-o fa-fw w3-margin-right w3-large w3-text-teal"></i>S9086453H</p>
                    <p><i class="fa fa-file  fa-fw w3-margin-right w3-large w3-text-teal"></i>[[1A]]</p>
                    <p><i class="fa fa-drivers-license-o fa-fw w3-margin-right w3-large w3-text-teal"></i>No Result Information</p>
                    <hr>



                    <p class="w3-large"><b><i class="fa fa-asterisk fa-fw w3-margin-right w3-text-teal"></i>Current Subjects</b></p>
                    <p>Subject 1</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent1%">Grade 1</div>
                    </div>
                    <p>Subject 2</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent2%">
                            <div class="w3-center w3-text-white">Grade 2</div>
                        </div>
                    </div>
                    <p>Subject 3</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent3%">Grade 3</div>
                    </div>
                    <p>Subject 4</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent4%">Grade 4</div>
                    </div>
                    <p>Subject 5</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent5%">Grade 5</div>
                    </div>
                    <p>Subject 6</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent6%">Grade 6</div>
                    </div>

                    <hr>
                </div>
            </div><br>

            <!-- End Left Column -->
        </div>

        <!-- Right Column -->
        <div class="w3-twothird">

            <div class="w3-container w3-card w3-white w3-margin-bottom">



                <h2 class="w3-text-grey w3-padding-16"><i class="fa fa-address-card-o fa-fw w3-margin-right w3-xxlarge w3-text-teal"></i>Next of Kin Details</h2>
                <div class="w3-container">
                    <p><i class="fa fa-address-book fa-fw w3-margin-right w3-large w3-text-teal"></i>Jonathan Doe</p>
                    <p><i class="fa fa-genderless fa-fw w3-margin-right w3-large w3-text-teal"></i>Father</p>
                    <p><i class="fa fa-envelope fa-fw w3-margin-right w3-large w3-text-teal"></i>jonathandoe@company.com.sg</p>
                    <p><i class="fa fa-phone fa-fw w3-margin-right w3-large w3-text-teal"></i>+65 96993220</p>

                </div>







                <h2 class="w3-text-grey w3-padding-16"><i class="fa fa-suitcase fa-fw w3-margin-right w3-xxlarge w3-text-teal"></i>Co Curricular Activities</h2>
                <div class="w3-container">
                    <h5 class="w3-opacity"><b>: </b></h5>
                    <p>Head</p>
                </div>
                <br>
                <br>


                <div class="w3-container w3-card w3-white">
                    <h2 class="w3-text-grey w3-padding-16"><i class="fa fa-certificate fa-fw w3-margin-right w3-xxlarge w3-text-teal"></i>Teacher's Remarks</h2>
                    <div class="w3-container">
                        <p></p>
                    </div>
                    <br>
                </div>
                <br>



                <div class="w3-container w3-card w3-white">
                    <h2 class="w3-text-grey w3-padding-16"><i class="fa fa-certificate fa-fw w3-margin-right w3-xxlarge w3-text-teal"></i>Injury History</h2>
                    <div class="w3-container">
                        <p></p>
                    </div>
                   <br>
                </div>
                <br>


                <iframe src="https://calendar.google.com/calendar/embed?src=edubuddytest%40gmail.com&ctz=Asia%2FSingapore'
" style="border-width:0" width="600" height="600" frameborder="0" scrolling="no"></iframe>

                <!-- End Right Column -->

            </div>

            <!-- End Grid -->
        </div>

        <!-- End Page Container -->
    </div>

    <footer>
        <p>EduBuddy, Powered by<a href="https://www.w3schools.com/w3css/default.asp" target="_blank">w3.css</a></p>
    </footer>

</body>
</html>
```
###### \resources\StudentPage\Joy.html
``` html
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel='stylesheet' href='https://fonts.googleapis.com/css?family=Helvetica'>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">


<style>
html,body,h1,h2,h3,h4,h5,h6 {font-family: "Helvetica", sans-serif}
</style>
<body class="w3-light-grey">

<!-- Page Container -->
<div class="w3-content w3-margin-top" style="max-width:1400px;">

    <!-- The Grid -->
    <div class="w3-row-padding">

        <!-- Left Column -->
        <div class="w3-third">

            <div class="w3-white w3-text-grey w3-card-4">
                <div class="w3-display-container">
                    <img src="../docs/images/defaultface.png" style="width:100%" alt="Avatar">

                </div>
                <div class="w3-container">
                    <p><i class="fa fa-address-book fa-fw w3-margin-right w3-large w3-text-teal"></i>Joy</p>
                    <p><i class="fa fa-folder-o fa-fw w3-margin-right w3-large w3-text-teal"></i>S9876456H</p>
                    <p><i class="fa fa-file  fa-fw w3-margin-right w3-large w3-text-teal"></i>[[1A]]</p>
                    <p><i class="fa fa-drivers-license-o fa-fw w3-margin-right w3-large w3-text-teal"></i>No Result Information</p>
                    <hr>



                    <p class="w3-large"><b><i class="fa fa-asterisk fa-fw w3-margin-right w3-text-teal"></i>Current Subjects</b></p>
                    <p>Subject 1</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent1%">Grade 1</div>
                    </div>
                    <p>Subject 2</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent2%">
                            <div class="w3-center w3-text-white">Grade 2</div>
                        </div>
                    </div>
                    <p>Subject 3</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent3%">Grade 3</div>
                    </div>
                    <p>Subject 4</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent4%">Grade 4</div>
                    </div>
                    <p>Subject 5</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent5%">Grade 5</div>
                    </div>
                    <p>Subject 6</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent6%">Grade 6</div>
                    </div>

                    <hr>
                </div>
            </div><br>

            <!-- End Left Column -->
        </div>

        <!-- Right Column -->
        <div class="w3-twothird">

            <div class="w3-container w3-card w3-white w3-margin-bottom">



                <h2 class="w3-text-grey w3-padding-16"><i class="fa fa-address-card-o fa-fw w3-margin-right w3-xxlarge w3-text-teal"></i>Next of Kin Details</h2>
                <div class="w3-container">
                    <p><i class="fa fa-address-book fa-fw w3-margin-right w3-large w3-text-teal"></i>Jonathan Doe</p>
                    <p><i class="fa fa-genderless fa-fw w3-margin-right w3-large w3-text-teal"></i>Father</p>
                    <p><i class="fa fa-envelope fa-fw w3-margin-right w3-large w3-text-teal"></i>jonathandoe@company.com.sg</p>
                    <p><i class="fa fa-phone fa-fw w3-margin-right w3-large w3-text-teal"></i>+65 96993220</p>

                </div>







                <h2 class="w3-text-grey w3-padding-16"><i class="fa fa-suitcase fa-fw w3-margin-right w3-xxlarge w3-text-teal"></i>Co Curricular Activities</h2>
                <div class="w3-container">
                    <h5 class="w3-opacity"><b>Choir member</b></h5>
                    <p>Head</p>
                </div>
                <br>
                <br>


                <div class="w3-container w3-card w3-white">
                    <h2 class="w3-text-grey w3-padding-16"><i class="fa fa-certificate fa-fw w3-margin-right w3-xxlarge w3-text-teal"></i>Teacher's Remarks</h2>
                    <div class="w3-container">
                        <p></p>
                    </div>
                    <br>
                </div>
                <br>



                <div class="w3-container w3-card w3-white">
                    <h2 class="w3-text-grey w3-padding-16"><i class="fa fa-certificate fa-fw w3-margin-right w3-xxlarge w3-text-teal"></i>Injury History</h2>
                    <div class="w3-container">
                        <p></p>
                    </div>
                   <br>
                </div>
                <br>


                <iframe src="https://calendar.google.com/calendar/embed?src=edubuddytest%40gmail.com&ctz=Asia%2FSingapore'
" style="border-width:0" width="600" height="600" frameborder="0" scrolling="no"></iframe>

                <!-- End Right Column -->

            </div>

            <!-- End Grid -->
        </div>

        <!-- End Page Container -->
    </div>

    <footer>
        <p>EduBuddy, Powered by<a href="https://www.w3schools.com/w3css/default.asp" target="_blank">w3.css</a></p>
    </footer>

</body>
</html>
```
###### \resources\StudentPage\Keng Seng.html
``` html
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel='stylesheet' href='https://fonts.googleapis.com/css?family=Helvetica'>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">


<style>
html,body,h1,h2,h3,h4,h5,h6 {font-family: "Helvetica", sans-serif}
</style>
<body class="w3-light-grey">

<!-- Page Container -->
<div class="w3-content w3-margin-top" style="max-width:1400px;">

    <!-- The Grid -->
    <div class="w3-row-padding">

        <!-- Left Column -->
        <div class="w3-third">

            <div class="w3-white w3-text-grey w3-card-4">
                <div class="w3-display-container">
                    <img src="../docs/images/defaultface.png" style="width:100%" alt="Avatar">

                </div>
                <div class="w3-container">
                    <p><i class="fa fa-address-book fa-fw w3-margin-right w3-large w3-text-teal"></i>Keng Seng</p>
                    <p><i class="fa fa-folder-o fa-fw w3-margin-right w3-large w3-text-teal"></i>S9867444H</p>
                    <p><i class="fa fa-file  fa-fw w3-margin-right w3-large w3-text-teal"></i>[[1A]]</p>
                    <p><i class="fa fa-drivers-license-o fa-fw w3-margin-right w3-large w3-text-teal"></i>No Result Information</p>
                    <hr>



                    <p class="w3-large"><b><i class="fa fa-asterisk fa-fw w3-margin-right w3-text-teal"></i>Current Subjects</b></p>
                    <p>Subject 1</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent1%">Grade 1</div>
                    </div>
                    <p>Subject 2</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent2%">
                            <div class="w3-center w3-text-white">Grade 2</div>
                        </div>
                    </div>
                    <p>Subject 3</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent3%">Grade 3</div>
                    </div>
                    <p>Subject 4</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent4%">Grade 4</div>
                    </div>
                    <p>Subject 5</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent5%">Grade 5</div>
                    </div>
                    <p>Subject 6</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent6%">Grade 6</div>
                    </div>

                    <hr>
                </div>
            </div><br>

            <!-- End Left Column -->
        </div>

        <!-- Right Column -->
        <div class="w3-twothird">

            <div class="w3-container w3-card w3-white w3-margin-bottom">



                <h2 class="w3-text-grey w3-padding-16"><i class="fa fa-address-card-o fa-fw w3-margin-right w3-xxlarge w3-text-teal"></i>Next of Kin Details</h2>
                <div class="w3-container">
                    <p><i class="fa fa-address-book fa-fw w3-margin-right w3-large w3-text-teal"></i>Jonathan Doe</p>
                    <p><i class="fa fa-genderless fa-fw w3-margin-right w3-large w3-text-teal"></i>Father</p>
                    <p><i class="fa fa-envelope fa-fw w3-margin-right w3-large w3-text-teal"></i>jonathandoe@company.com.sg</p>
                    <p><i class="fa fa-phone fa-fw w3-margin-right w3-large w3-text-teal"></i>+65 96993220</p>

                </div>







                <h2 class="w3-text-grey w3-padding-16"><i class="fa fa-suitcase fa-fw w3-margin-right w3-xxlarge w3-text-teal"></i>Co Curricular Activities</h2>
                <div class="w3-container">
                    <h5 class="w3-opacity"><b>: </b></h5>
                    <p>Head</p>
                </div>
                <br>
                <br>


                <div class="w3-container w3-card w3-white">
                    <h2 class="w3-text-grey w3-padding-16"><i class="fa fa-certificate fa-fw w3-margin-right w3-xxlarge w3-text-teal"></i>Teacher's Remarks</h2>
                    <div class="w3-container">
                        <p></p>
                    </div>
                    <br>
                </div>
                <br>



                <div class="w3-container w3-card w3-white">
                    <h2 class="w3-text-grey w3-padding-16"><i class="fa fa-certificate fa-fw w3-margin-right w3-xxlarge w3-text-teal"></i>Injury History</h2>
                    <div class="w3-container">
                        <p></p>
                    </div>
                   <br>
                </div>
                <br>


                <iframe src="https://calendar.google.com/calendar/embed?src=edubuddytest%40gmail.com&ctz=Asia%2FSingapore'
" style="border-width:0" width="600" height="600" frameborder="0" scrolling="no"></iframe>

                <!-- End Right Column -->

            </div>

            <!-- End Grid -->
        </div>

        <!-- End Page Container -->
    </div>

    <footer>
        <p>EduBuddy, Powered by<a href="https://www.w3schools.com/w3css/default.asp" target="_blank">w3.css</a></p>
    </footer>

</body>
</html>
```
###### \resources\StudentPage\Sunny.html
``` html
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel='stylesheet' href='https://fonts.googleapis.com/css?family=Helvetica'>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">


<style>
html,body,h1,h2,h3,h4,h5,h6 {font-family: "Helvetica", sans-serif}
</style>
<body class="w3-light-grey">

<!-- Page Container -->
<div class="w3-content w3-margin-top" style="max-width:1400px;">

    <!-- The Grid -->
    <div class="w3-row-padding">

        <!-- Left Column -->
        <div class="w3-third">

            <div class="w3-white w3-text-grey w3-card-4">
                <div class="w3-display-container">
                    <img src="../docs/images/defaultface.png" style="width:100%" alt="Avatar">

                </div>
                <div class="w3-container">
                    <p><i class="fa fa-address-book fa-fw w3-margin-right w3-large w3-text-teal"></i>Sunny</p>
                    <p><i class="fa fa-folder-o fa-fw w3-margin-right w3-large w3-text-teal"></i>S9086456H</p>
                    <p><i class="fa fa-file  fa-fw w3-margin-right w3-large w3-text-teal"></i>[[3A]]</p>
                    <p><i class="fa fa-drivers-license-o fa-fw w3-margin-right w3-large w3-text-teal"></i>No Result Information</p>
                    <hr>



                    <p class="w3-large"><b><i class="fa fa-asterisk fa-fw w3-margin-right w3-text-teal"></i>Current Subjects</b></p>
                    <p>Subject 1</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent1%">Grade 1</div>
                    </div>
                    <p>Subject 2</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent2%">
                            <div class="w3-center w3-text-white">Grade 2</div>
                        </div>
                    </div>
                    <p>Subject 3</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent3%">Grade 3</div>
                    </div>
                    <p>Subject 4</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent4%">Grade 4</div>
                    </div>
                    <p>Subject 5</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent5%">Grade 5</div>
                    </div>
                    <p>Subject 6</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent6%">Grade 6</div>
                    </div>

                    <hr>
                </div>
            </div><br>

            <!-- End Left Column -->
        </div>

        <!-- Right Column -->
        <div class="w3-twothird">

            <div class="w3-container w3-card w3-white w3-margin-bottom">



                <h2 class="w3-text-grey w3-padding-16"><i class="fa fa-address-card-o fa-fw w3-margin-right w3-xxlarge w3-text-teal"></i>Next of Kin Details</h2>
                <div class="w3-container">
                    <p><i class="fa fa-address-book fa-fw w3-margin-right w3-large w3-text-teal"></i>Jonathan Doe</p>
                    <p><i class="fa fa-genderless fa-fw w3-margin-right w3-large w3-text-teal"></i>Father</p>
                    <p><i class="fa fa-envelope fa-fw w3-margin-right w3-large w3-text-teal"></i>jonathandoe@company.com.sg</p>
                    <p><i class="fa fa-phone fa-fw w3-margin-right w3-large w3-text-teal"></i>+65 96993220</p>

                </div>







                <h2 class="w3-text-grey w3-padding-16"><i class="fa fa-suitcase fa-fw w3-margin-right w3-xxlarge w3-text-teal"></i>Co Curricular Activities</h2>
                <div class="w3-container">
                    <h5 class="w3-opacity"><b>: </b></h5>
                    <p>Head</p>
                </div>
                <br>
                <br>


                <div class="w3-container w3-card w3-white">
                    <h2 class="w3-text-grey w3-padding-16"><i class="fa fa-certificate fa-fw w3-margin-right w3-xxlarge w3-text-teal"></i>Teacher's Remarks</h2>
                    <div class="w3-container">
                        <p></p>
                    </div>
                    <br>
                </div>
                <br>



                <div class="w3-container w3-card w3-white">
                    <h2 class="w3-text-grey w3-padding-16"><i class="fa fa-certificate fa-fw w3-margin-right w3-xxlarge w3-text-teal"></i>Injury History</h2>
                    <div class="w3-container">
                        <p></p>
                    </div>
                   <br>
                </div>
                <br>


                <iframe src="https://calendar.google.com/calendar/embed?src=edubuddytest%40gmail.com&ctz=Asia%2FSingapore'
" style="border-width:0" width="600" height="600" frameborder="0" scrolling="no"></iframe>

                <!-- End Right Column -->

            </div>

            <!-- End Grid -->
        </div>

        <!-- End Page Container -->
    </div>

    <footer>
        <p>EduBuddy, Powered by<a href="https://www.w3schools.com/w3css/default.asp" target="_blank">w3.css</a></p>
    </footer>

</body>
</html>
```
###### \resources\StudentPage\template.html
``` html
<meta charset="UTF-8">

<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel='stylesheet' href='https://fonts.googleapis.com/css?family=Helvetica'>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">


<style>
html,body,h1,h2,h3,h4,h5,h6 {font-family: "Helvetica", sans-serif}
</style>
<body class="w3-light-grey">

<!-- Page Container -->
<div class="w3-content w3-margin-top" style="max-width:1400px;">

    <!-- The Grid -->
    <div class="w3-row-padding">

        <!-- Left Column -->
        <div class="w3-third">

            <div class="w3-white w3-text-grey w3-card-4">
                <div class="w3-display-container">
                    <img src="https://s31.postimg.cc/4n089680p/defaultface.png?dl=1" style="width:100%" alt="Avatar">

                </div>
                <div class="w3-container">
                    <p><i class="fa fa-address-book fa-fw w3-margin-right w3-large w3-text-teal"></i>Name: $title</p>
                    <p><i class="fa fa-folder-o fa-fw w3-margin-right w3-large w3-text-teal"></i>IC: $identityNumber</p>
                    <p><i class="fa fa-file  fa-fw w3-margin-right w3-large w3-text-teal"></i>Class: Class Not Specified</p>
                    <p><i class="fa fa-file  fa-fw w3-margin-right w3-large w3-text-teal"></i>L1R5: STUDENTS SCORE</p>
                    <p><i class="fa fa-file  fa-fw w3-margin-right w3-large w3-text-teal"></i>L1B4A: STUDENTS L1B4A</p>
                    <p><i class="fa fa-file  fa-fw w3-margin-right w3-large w3-text-teal"></i>L1B4B: STUDENTS L1B4B</p>
                    <p><i class="fa fa-file  fa-fw w3-margin-right w3-large w3-text-teal"></i>L1B4C: STUDENTS L1B4C</p>
                    <p><i class="fa fa-file  fa-fw w3-margin-right w3-large w3-text-teal"></i>L1B4D: STUDENTS L1B4D</p>
                    <hr>



                    <p class="w3-large"><b><i class="fa fa-asterisk fa-fw w3-margin-right w3-text-teal"></i>Current Subjects</b></p>
                    <p>Subject 1</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent1%">Grade 1</div>
                    </div>
                    <p>Subject 2</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent2%">
                            <div class="w3-center w3-text-white">Grade 2</div>
                        </div>
                    </div>
                    <p>Subject 3</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent3%">Grade 3</div>
                    </div>
                    <p>Subject 4</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent4%">Grade 4</div>
                    </div>
                    <p>Subject 5</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent5%">Grade 5</div>
                    </div>
                    <p>Subject 6</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent6%">Grade 6</div>
                    </div>

                    <hr>
                </div>
            </div><br>

            <!-- End Left Column -->
        </div>

        <!-- Right Column -->
        <div class="w3-twothird">

            <div class="w3-container w3-card w3-white w3-margin-bottom">



                <h2 class="w3-text-grey w3-padding-16"><i class="fa fa-address-card-o fa-fw w3-margin-right w3-xxlarge w3-text-teal"></i>Next of Kin Details</h2>
                <div class="w3-container">
                    <p><i class="fa fa-address-book fa-fw w3-margin-right w3-large w3-text-teal"></i>Name: NOK Name</p>
                    <p><i class="fa fa-genderless fa-fw w3-margin-right w3-large w3-text-teal"></i>Relationship: NOK Gender</p>
                    <p><i class="fa fa-envelope fa-fw w3-margin-right w3-large w3-text-teal"></i>Email: NOK Email</p>
                    <p><i class="fa fa-phone fa-fw w3-margin-right w3-large w3-text-teal"></i>Phone: NOK Phone</p>

                </div>




                <h2 class="w3-text-grey w3-padding-16"><i class="fa fa-suitcase fa-fw w3-margin-right w3-xxlarge w3-text-teal"></i>Co Curricular Activities</h2>
                <div class="w3-container">
                    <h5 class="w3-opacity"><b>CCA</b></h5>
                    <p>STUDENT RANK</p>
                </div>
                <br>
                <br>


                <div class="w3-container w3-card w3-white">
                    <h2 class="w3-text-grey w3-padding-16"><i class="fa fa-certificate fa-fw w3-margin-right w3-xxlarge w3-text-teal"></i>Teacher's Remarks</h2>
                    <div class="w3-container">
                        <p>Remarks to facilitate teaching should be included here.</p>
                    </div>
                    <br>
                </div>
                <br>



                <div class="w3-container w3-card w3-white">
                    <h2 class="w3-text-grey w3-padding-16"><i class="fa fa-certificate fa-fw w3-margin-right w3-xxlarge w3-text-teal"></i>Injury History</h2>
                    <div class="w3-container">
                        <p>Insert injury history here</p>
                    </div>
                   <br>
                </div>
                <br>

                <!-- End Right Column -->

            </div>

            <!-- End Grid -->
        </div>

        <!-- End Page Container -->
    </div>

    <footer>
        <p>EduBuddy, Powered by<a href="https://www.w3schools.com/w3css/default.asp" target="_blank">w3.css</a></p>
    </footer>

</body>
</html>
```
###### \resources\StudentPage\Xin.html
``` html
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel='stylesheet' href='https://fonts.googleapis.com/css?family=Helvetica'>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">


<style>
html,body,h1,h2,h3,h4,h5,h6 {font-family: "Helvetica", sans-serif}
</style>
<body class="w3-light-grey">

<!-- Page Container -->
<div class="w3-content w3-margin-top" style="max-width:1400px;">

    <!-- The Grid -->
    <div class="w3-row-padding">

        <!-- Left Column -->
        <div class="w3-third">

            <div class="w3-white w3-text-grey w3-card-4">
                <div class="w3-display-container">
                    <img src="../docs/images/defaultface.png" style="width:100%" alt="Avatar">

                </div>
                <div class="w3-container">
                    <p><i class="fa fa-address-book fa-fw w3-margin-right w3-large w3-text-teal"></i>Xin</p>
                    <p><i class="fa fa-folder-o fa-fw w3-margin-right w3-large w3-text-teal"></i>S9084561H</p>
                    <p><i class="fa fa-file  fa-fw w3-margin-right w3-large w3-text-teal"></i>[[1A]]</p>
                    <p><i class="fa fa-drivers-license-o fa-fw w3-margin-right w3-large w3-text-teal"></i>No Result Information</p>
                    <hr>



                    <p class="w3-large"><b><i class="fa fa-asterisk fa-fw w3-margin-right w3-text-teal"></i>Current Subjects</b></p>
                    <p>Subject 1</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent1%">Grade 1</div>
                    </div>
                    <p>Subject 2</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent2%">
                            <div class="w3-center w3-text-white">Grade 2</div>
                        </div>
                    </div>
                    <p>Subject 3</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent3%">Grade 3</div>
                    </div>
                    <p>Subject 4</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent4%">Grade 4</div>
                    </div>
                    <p>Subject 5</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent5%">Grade 5</div>
                    </div>
                    <p>Subject 6</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent6%">Grade 6</div>
                    </div>

                    <hr>
                </div>
            </div><br>

            <!-- End Left Column -->
        </div>

        <!-- Right Column -->
        <div class="w3-twothird">

            <div class="w3-container w3-card w3-white w3-margin-bottom">



                <h2 class="w3-text-grey w3-padding-16"><i class="fa fa-address-card-o fa-fw w3-margin-right w3-xxlarge w3-text-teal"></i>Next of Kin Details</h2>
                <div class="w3-container">
                    <p><i class="fa fa-address-book fa-fw w3-margin-right w3-large w3-text-teal"></i>Jonathan Doe</p>
                    <p><i class="fa fa-genderless fa-fw w3-margin-right w3-large w3-text-teal"></i>Father</p>
                    <p><i class="fa fa-envelope fa-fw w3-margin-right w3-large w3-text-teal"></i>jonathandoe@company.com.sg</p>
                    <p><i class="fa fa-phone fa-fw w3-margin-right w3-large w3-text-teal"></i>+65 96993220</p>

                </div>







                <h2 class="w3-text-grey w3-padding-16"><i class="fa fa-suitcase fa-fw w3-margin-right w3-xxlarge w3-text-teal"></i>Co Curricular Activities</h2>
                <div class="w3-container">
                    <h5 class="w3-opacity"><b>: </b></h5>
                    <p>Head</p>
                </div>
                <br>
                <br>


                <div class="w3-container w3-card w3-white">
                    <h2 class="w3-text-grey w3-padding-16"><i class="fa fa-certificate fa-fw w3-margin-right w3-xxlarge w3-text-teal"></i>Teacher's Remarks</h2>
                    <div class="w3-container">
                        <p></p>
                    </div>
                    <br>
                </div>
                <br>



                <div class="w3-container w3-card w3-white">
                    <h2 class="w3-text-grey w3-padding-16"><i class="fa fa-certificate fa-fw w3-margin-right w3-xxlarge w3-text-teal"></i>Injury History</h2>
                    <div class="w3-container">
                        <p></p>
                    </div>
                   <br>
                </div>
                <br>


                <iframe src="https://calendar.google.com/calendar/embed?src=edubuddytest%40gmail.com&ctz=Asia%2FSingapore'
" style="border-width:0" width="600" height="600" frameborder="0" scrolling="no"></iframe>

                <!-- End Right Column -->

            </div>

            <!-- End Grid -->
        </div>

        <!-- End Page Container -->
    </div>

    <footer>
        <p>EduBuddy, Powered by<a href="https://www.w3schools.com/w3css/default.asp" target="_blank">w3.css</a></p>
    </footer>

</body>
</html>
```
###### \resources\StudentPage\Yi ren.html
``` html
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel='stylesheet' href='https://fonts.googleapis.com/css?family=Helvetica'>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">


<style>
html,body,h1,h2,h3,h4,h5,h6 {font-family: "Helvetica", sans-serif}
</style>
<body class="w3-light-grey">

<!-- Page Container -->
<div class="w3-content w3-margin-top" style="max-width:1400px;">

    <!-- The Grid -->
    <div class="w3-row-padding">

        <!-- Left Column -->
        <div class="w3-third">

            <div class="w3-white w3-text-grey w3-card-4">
                <div class="w3-display-container">
                    <img src="../docs/images/defaultface.png" style="width:100%" alt="Avatar">

                </div>
                <div class="w3-container">
                    <p><i class="fa fa-address-book fa-fw w3-margin-right w3-large w3-text-teal"></i>Yi ren</p>
                    <p><i class="fa fa-folder-o fa-fw w3-margin-right w3-large w3-text-teal"></i>S9867456H</p>
                    <p><i class="fa fa-file  fa-fw w3-margin-right w3-large w3-text-teal"></i>[[3A]]</p>
                    <p><i class="fa fa-drivers-license-o fa-fw w3-margin-right w3-large w3-text-teal"></i>No Result Information</p>
                    <hr>



                    <p class="w3-large"><b><i class="fa fa-asterisk fa-fw w3-margin-right w3-text-teal"></i>Current Subjects</b></p>
                    <p>Subject 1</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent1%">Grade 1</div>
                    </div>
                    <p>Subject 2</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent2%">
                            <div class="w3-center w3-text-white">Grade 2</div>
                        </div>
                    </div>
                    <p>Subject 3</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent3%">Grade 3</div>
                    </div>
                    <p>Subject 4</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent4%">Grade 4</div>
                    </div>
                    <p>Subject 5</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent5%">Grade 5</div>
                    </div>
                    <p>Subject 6</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent6%">Grade 6</div>
                    </div>

                    <hr>
                </div>
            </div><br>

            <!-- End Left Column -->
        </div>

        <!-- Right Column -->
        <div class="w3-twothird">

            <div class="w3-container w3-card w3-white w3-margin-bottom">



                <h2 class="w3-text-grey w3-padding-16"><i class="fa fa-address-card-o fa-fw w3-margin-right w3-xxlarge w3-text-teal"></i>Next of Kin Details</h2>
                <div class="w3-container">
                    <p><i class="fa fa-address-book fa-fw w3-margin-right w3-large w3-text-teal"></i>Jonathan Doe</p>
                    <p><i class="fa fa-genderless fa-fw w3-margin-right w3-large w3-text-teal"></i>Father</p>
                    <p><i class="fa fa-envelope fa-fw w3-margin-right w3-large w3-text-teal"></i>jonathandoe@company.com.sg</p>
                    <p><i class="fa fa-phone fa-fw w3-margin-right w3-large w3-text-teal"></i>+65 96993220</p>

                </div>







                <h2 class="w3-text-grey w3-padding-16"><i class="fa fa-suitcase fa-fw w3-margin-right w3-xxlarge w3-text-teal"></i>Co Curricular Activities</h2>
                <div class="w3-container">
                    <h5 class="w3-opacity"><b>Basketball: Member</b></h5>
                    <p>Head</p>
                </div>
                <br>
                <br>


                <div class="w3-container w3-card w3-white">
                    <h2 class="w3-text-grey w3-padding-16"><i class="fa fa-certificate fa-fw w3-margin-right w3-xxlarge w3-text-teal"></i>Teacher's Remarks</h2>
                    <div class="w3-container">
                        <p></p>
                    </div>
                    <br>
                </div>
                <br>



                <div class="w3-container w3-card w3-white">
                    <h2 class="w3-text-grey w3-padding-16"><i class="fa fa-certificate fa-fw w3-margin-right w3-xxlarge w3-text-teal"></i>Injury History</h2>
                    <div class="w3-container">
                        <p>Torn Ligament</p>
                    </div>
                   <br>
                </div>
                <br>


                <iframe src="https://calendar.google.com/calendar/embed?src=edubuddytest%40gmail.com&ctz=Asia%2FSingapore'
" style="border-width:0" width="600" height="600" frameborder="0" scrolling="no"></iframe>

                <!-- End Right Column -->

            </div>

            <!-- End Grid -->
        </div>

        <!-- End Page Container -->
    </div>

    <footer>
        <p>EduBuddy, Powered by<a href="https://www.w3schools.com/w3css/default.asp" target="_blank">w3.css</a></p>
    </footer>

</body>
</html>
```
###### \resources\view\default.html
``` html
<!DOCTYPE html>
<html>
<title>Edubuddy</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet" href="https://www.w3schools.com/lib/w3-theme-black.css">
<link rel='stylesheet' href='https://fonts.googleapis.com/css?family=Helvetica'>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<body id="myPage">

<!-- Image Header -->
<div class="w3-display-container w3-animate-opacity">
    <img src="edubuddyhead.png" alt="logobuddy" style="width:100%;min-height:350px;max-height:500px;">
</div>

<!-- Team Container -->
<div class="w3-container w3-padding-32 w3-center" id="team">
    <h2>EduBuddy</h2>
    <p>A secondary school teacher's best companion.</p>


    </div>
</div>
```
###### \resources\view\LightTheme.css
``` css
.background {
    -fx-background-color: derive(#ffffff, 20%);
    background-color: #f5f5f5;
}

.label {
    -fx-font-size: 11pt;
    -fx-font-family: "Segoe UI Semibold";
    -fx-text-fill: #cecece;
    -fx-opacity: 0.9;
}

.label-bright {
    -fx-font-size: 11pt;
    -fx-font-family: "Segoe UI Semibold";
    -fx-text-fill: black;
    -fx-opacity: 1;
}

.label-header {
    -fx-font-size: 32pt;
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: black;
    -fx-opacity: 1;
}

.text-field {
    -fx-font-size: 12pt;
    -fx-font-family: "Segoe UI Semibold";
}

.tab-pane {
    -fx-padding: 0 0 0 1;
}

.tab-pane .tab-header-area {
    -fx-padding: 0 0 0 0;
    -fx-min-height: 0;
    -fx-max-height: 0;
}

.table-view {
    -fx-base: #ffffff;
    -fx-control-inner-background: #ffffff;
    -fx-background-color: #ffffff;
    -fx-table-cell-border-color: transparent;
    -fx-table-header-border-color: transparent;
    -fx-padding: 5;
}

.table-view .column-header-background {
    -fx-background-color: transparent;
}

.table-view .column-header, .table-view .filler {
    -fx-size: 35;
    -fx-border-width: 0 0 1 0;
    -fx-background-color: transparent;
    -fx-border-color:
        transparent
        transparent
        derive(-fx-base, 80%)
        transparent;
    -fx-border-insets: 0 10 1 0;
}

.table-view .column-header .label {
    -fx-font-size: 20pt;
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: black;
    -fx-alignment: center-left;
    -fx-opacity: 1;
}

.table-view:focused .table-row-cell:filled:focused:selected {
    -fx-background-color: -fx-focus-color;
}

.split-pane:horizontal .split-pane-divider {
    -fx-background-color: derive(#ffffff, 20%);
    -fx-border-color: transparent transparent transparent #4d4d4d;
}

.split-pane {
    -fx-border-radius: 1;
    -fx-border-width: 1;
    -fx-background-color: derive(#ffffff, 20%);
}

.list-view {
    -fx-background-insets: 0;
    -fx-padding: 0;
    -fx-background-color: derive(#ffffff, 20%);
}

.list-cell {
    -fx-label-padding: 0 0 0 0;
    -fx-graphic-text-gap : 0;
    -fx-padding: 0 0 0 0;
}

.list-cell:filled:even {
    -fx-background-color: #b2d8d8;
}

.list-cell:filled:odd {
    -fx-background-color: #66b2b2;
}

.list-cell:filled:selected {
    -fx-background-color: #fcf7ff;
}

.list-cell:filled:selected #cardPane {
    -fx-border-color: #3e7b91;
    -fx-border-width: 1;
}

.list-cell .label {
    -fx-text-fill: black;
}

.cell_big_label {
    -fx-font-family: "Segoe UI Semibold";
    -fx-font-size: 16px;
    -fx-text-fill: #010504;
}

.cell_small_label {
    -fx-font-family: "Segoe UI";
    -fx-font-size: 13px;
    -fx-text-fill: #010504;
}

.anchor-pane {
     -fx-background-color: derive(#ffffff, 20%);
}

.pane-with-border {
     -fx-background-color: derive(#ffffff, 20%);
     -fx-border-color: derive(#ffffff, 10%);
     -fx-border-top-width: 1px;
}

.status-bar {
    -fx-background-color: derive(#ffffff, 20%);
    -fx-text-fill: black;
}

.result-display {
    -fx-background-color: transparent;
    -fx-font-family: "Segoe UI Light";
    -fx-font-size: 13pt;
    -fx-text-fill: black;
}

.result-display .label {
    -fx-text-fill: black !important;
}

.status-bar .label {
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: black;
}

.status-bar-with-border {
    -fx-background-color: derive(#ffffff, 30%);
    -fx-border-color: derive(#ffffff, 25%);
    -fx-border-width: 1px;
}

.status-bar-with-border .label {
    -fx-text-fill: black;
}

.grid-pane {
    -fx-background-color: derive(#ffffff, 30%);
    -fx-border-color: derive(#ffffff, 30%);
    -fx-border-width: 1px;
}

.grid-pane .anchor-pane {
    -fx-background-color: derive(#ffffff, 30%);
}

.context-menu {
    -fx-background-color: derive(#ffffff, 50%);
}

.context-menu .label {
    -fx-text-fill: black;
}

.menu-bar {
    -fx-background-color: derive(#ffffff, 20%);
}

.menu-bar .label {
    -fx-font-size: 14pt;
    -fx-font-family: "Segoe UI Light";
    -fx-text-fill: black;
    -fx-opacity: 0.9;
}
/*a*/
.menu .left-container {
    -fx-background-color: black;
}

/*
 * Metro style Push Button
 * Author: Pedro Duque Vieira
 * http://pixelduke.wordpress.com/2012/10/23/jmetro-windows-8-controls-on-java/
 */
.button {
    -fx-padding: 5 22 5 22;
    -fx-border-color: #ffffff;
    -fx-border-width: 2;
    -fx-background-radius: 0;
    -fx-background-color: #ffffff;
    -fx-font-family: "Segoe UI", Helvetica, Arial, sans-serif;
    -fx-font-size: 11pt;
    -fx-text-fill: #d8d8d8;
    -fx-background-insets: 0 0 0 0, 0, 1, 2;
}

.button:hover {
    -fx-background-color: #ebebeb;
}

.button:pressed, .button:default:hover:pressed {
  -fx-background-color: black;
  -fx-text-fill: #ffffff;
}

.button:focused {
    -fx-border-color: black, black;
    -fx-border-width: 1, 1;
    -fx-border-style: solid, segments(1, 1);
    -fx-border-radius: 0, 0;
    -fx-border-insets: 1 1 1 1, 0;
}

.button:disabled, .button:default:disabled {
    -fx-opacity: 0.4;
    -fx-background-color: #ffffff;
    -fx-text-fill: black;
}

.button:default {
    -fx-background-color: -fx-focus-color;
    -fx-text-fill: #ffffff;
}

.button:default:hover {
    -fx-background-color: derive(-fx-focus-color, 30%);
}

.dialog-pane {
    -fx-background-color: #ffffff;
}

.dialog-pane > *.button-bar > *.container {
    -fx-background-color: #ffffff;
}

.dialog-pane > *.label.content {
    -fx-font-size: 14px;
    -fx-font-weight: bold;
    -fx-text-fill: black;
}

.dialog-pane:header *.header-panel {
    -fx-background-color: derive(#ffffff, 25%);
}

.dialog-pane:header *.header-panel *.label {
    -fx-font-size: 18px;
    -fx-font-style: italic;
    -fx-fill: black;
    -fx-text-fill: black;
}

.scroll-bar {
    -fx-background-color: derive(teal, 20%);
}

.scroll-bar .thumb {
    -fx-background-color: derive(#ffffff, 50%);
    -fx-background-insets: 3;
}

.scroll-bar .increment-button, .scroll-bar .decrement-button {
    -fx-background-color: transparent;
    -fx-padding: 0 0 0 0;
}

.scroll-bar .increment-arrow, .scroll-bar .decrement-arrow {
    -fx-shape: " ";
}

.scroll-bar:vertical .increment-arrow, .scroll-bar:vertical .decrement-arrow {
    -fx-padding: 1 8 1 8;
}

.scroll-bar:horizontal .increment-arrow, .scroll-bar:horizontal .decrement-arrow {
    -fx-padding: 8 1 8 1;
}

#cardPane {
    -fx-background-color: transparent;
    -fx-border-width: 0;
}

#commandTypeLabel {
    -fx-font-size: 11px;
    -fx-text-fill: #F70D1A;
}

#commandTextField {
    -fx-background-color: transparent #f5f5f5 transparent #f5f5f5;
    -fx-background-insets: 0;
    -fx-border-color: #f5f5f5 #f5f5f5 #ffffff #f5f5f5;
    -fx-border-insets: 0;
    -fx-border-width: 1;
    -fx-font-family: "Segoe UI Light";
    -fx-font-size: 13pt;
    -fx-text-fill: black;
}

#filterField, #personListPanel, #personWebpage {
    -fx-effect: innershadow(gaussian, white, 10, 0, 0, 0);
}

#resultDisplay .content {
    -fx-background-color: transparent, #f5f5f5, transparent, #f5f5f5;
    -fx-background-radius: 0;
}

#tags {
    -fx-hgap: 7;
    -fx-vgap: 3;
}

#tags .label {
    -fx-padding: 1 3 1 3;
    -fx-border-radius: 2;
    -fx-background-radius: 2;
    -fx-font-size: 11;
}


#tags .teal {
    -fx-text-fill: black;
    -fx-background-color: #ffffff;
}

#tags .red {
    -fx-text-fill: black;
    -fx-background-color: #ff7675;
}

#tags .yellow {
    -fx-background-color: #ffeaa7;
    -fx-text-fill: black;
}

#tags .blue {
    -fx-text-fill: black;
    -fx-background-color: #48dbfb;
}

#tags .orange {
    -fx-text-fill: black;
    -fx-background-color: #ffa502;
}

#tags .brown {
    -fx-text-fill: black;
    -fx-background-color: #D7ACAC;
}

#tags .green {
    -fx-text-fill: black;
    -fx-background-color: #55efc4;
}

#tags .pink {
    -fx-text-fill: black;
    -fx-background-color: #fd79a8;
}

#tags .black {
    -fx-text-fill: white;
    -fx-background-color: black;
}

#tags .purple {
    -fx-text-fill: black;
    -fx-background-color: #a29bfe;
}

```
###### \resources\view\template.html
``` html
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel='stylesheet' href='https://fonts.googleapis.com/css?family=Helvetica'>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">


<style>
html,body,h1,h2,h3,h4,h5,h6 {font-family: "Helvetica", sans-serif}
</style>
<body class="w3-light-grey">

<!-- Page Container -->
<div class="w3-content w3-margin-top" style="max-width:1400px;">

    <!-- The Grid -->
    <div class="w3-row-padding">

        <!-- Left Column -->
        <div class="w3-third">

            <div class="w3-white w3-text-grey w3-card-4">
                <div class="w3-display-container">
                    <img src="../docs/images/johnnychanjx.png" style="width:100%" alt="Avatar">

                </div>
                <div class="w3-container">
                    <p><i class="fa fa-address-book fa-fw w3-margin-right w3-large w3-text-teal"></i>$title</p>
                    <p><i class="fa fa-folder-o fa-fw w3-margin-right w3-large w3-text-teal"></i>$identityNumber</p>
                    <p><i class="fa fa-home fa-fw w3-margin-right w3-large w3-text-teal"></i>10 Tampines North Street 3 Singapore 461110 </p>
                    <p><i class="fa fa-phone fa-fw w3-margin-right w3-large w3-text-teal"></i>+65 96992220</p>
                    <hr>



                    <p class="w3-large"><b><i class="fa fa-asterisk fa-fw w3-margin-right w3-text-teal"></i>Current Subjects</b></p>
                    <p>$subject1</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent1%">$grade1</div>
                    </div>
                    <p>$subject2</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent2%">
                            <div class="w3-center w3-text-white">$grade2</div>
                        </div>
                    </div>
                    <p>$subject3</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent3%">$grade3</div>
                    </div>
                    <p>$subject4</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent4%">$grade4</div>
                    </div>
                    <p>$subject5</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent5%">$grade5</div>
                    </div>
                    <p>$subject6</p>
                    <div class="w3-light-grey w3-round-xlarge w3-small">
                        <div class="w3-container w3-center w3-round-xlarge w3-teal" style="width:$percent6%">$grade6</div>
                    </div>

                    <hr>
                </div>
            </div><br>

            <!-- End Left Column -->
        </div>

        <!-- Right Column -->
        <div class="w3-twothird">

            <div class="w3-container w3-card w3-white w3-margin-bottom">



                <h2 class="w3-text-grey w3-padding-16"><i class="fa fa-address-card-o fa-fw w3-margin-right w3-xxlarge w3-text-teal"></i>Next of Kin Details</h2>
                <div class="w3-container">
                    <p><i class="fa fa-address-book fa-fw w3-margin-right w3-large w3-text-teal"></i>Jonathan Doe</p>
                    <p><i class="fa fa-genderless fa-fw w3-margin-right w3-large w3-text-teal"></i>Father</p>
                    <p><i class="fa fa-envelope fa-fw w3-margin-right w3-large w3-text-teal"></i>jonathandoe@company.com.sg</p>
                    <p><i class="fa fa-phone fa-fw w3-margin-right w3-large w3-text-teal"></i>+65 96993220</p>

                </div>







                <h2 class="w3-text-grey w3-padding-16"><i class="fa fa-suitcase fa-fw w3-margin-right w3-xxlarge w3-text-teal"></i>Co Curricular Activities</h2>
                <div class="w3-container">
                    <h5 class="w3-opacity"><b>Boys Brigade</b></h5>
                    <p>Head</p>
                </div>
                <div class="w3-container">
                    <h5 class="w3-opacity"><b>Life Sciences Club</b></h5>
                    <p>Member</p>
                    <hr>
                </div>


                <div class="w3-container w3-card w3-white">
                    <h2 class="w3-text-grey w3-padding-16"><i class="fa fa-certificate fa-fw w3-margin-right w3-xxlarge w3-text-teal"></i>Teacher's Remarks</h2>
                    <div class="w3-container">
                        <p>Hardworking Student, soft spoken and shy</p>
                    </div>
                    <div class="w3-container">
                        <p>History of anxiety disorder, refrain from putting student on the spot</p>
                    </div>
                </div>



                <div class="w3-container w3-card w3-white">
                    <h2 class="w3-text-grey w3-padding-16"><i class="fa fa-certificate fa-fw w3-margin-right w3-xxlarge w3-text-teal"></i>Injury History</h2>
                    <div class="w3-container">
                        <p>Chronic Bronchitis</p>
                    </div>
                    <div class="w3-container">
                        <p>Medial Collateral Ligament (MCL) injury history</p>
                        <hr>
                        <br>
                    </div>
                </div>

                <br>
                <!-- End Right Column -->
            </div>

            <!-- End Grid -->
        </div>

        <!-- End Page Container -->
    </div>

    <footer>
        <p>EduBuddy, Powered by<a href="https://www.w3schools.com/w3css/default.asp" target="_blank">w3.css</a></p>
    </footer>

</body>
</html>
```
