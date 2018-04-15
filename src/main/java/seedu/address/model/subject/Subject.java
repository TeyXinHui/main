package seedu.address.model.subject;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.Arrays;

//@@author TeyXinHui
/**
 * Represents a subject in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Subject {

    public static final String[] SUBJECT_NAME = new String[] {"English", "Chinese", "HChi", "CSP",
                                                              "ChiB", "French", "German", "Spanish", "Hindi",
                                                              "Urdu", "Gujarati", "Panjabi", "Bengali", "Burmese",
                                                              "Thai", "Jap", "BIndo", "Tamil",
                                                              "HTamil", "TamilB", "Malay", "HMalay", "MalayB",
                                                              "MSP", "EMath", "AMath", "Phy", "Chem", "Bio", "Sci",
                                                              "Hist", "Geog", "ComHum", "ELit", "CLit", "MLit", "TLit",
                                                              "Music", "HMusic", "Art", "HArt", "DnT", "Comp",
                                                              "FnN", "PoA", "Econs", "Drama", "PE", "Biz", "Biotech",
                                                              "Design"};
    public static final String MESSAGE_SUBJECT_NAME_CONSTRAINTS = "Subject names should be alphabetic and should be "
            + "one of the following: \n" + Arrays.toString(Arrays.copyOfRange(SUBJECT_NAME, 0, 9)) + "\n"
            + Arrays.toString(Arrays.copyOfRange(SUBJECT_NAME, 10, 19)) + "\n"
            + Arrays.toString(Arrays.copyOfRange(SUBJECT_NAME, 20, 29)) + "\n"
            + Arrays.toString(Arrays.copyOfRange(SUBJECT_NAME, 30, 39)) + "\n"
            + Arrays.toString(Arrays.copyOfRange(SUBJECT_NAME, 40, SUBJECT_NAME.length)) + ".";
    public static final String[] SUBJECT_GRADE = new String[] {"A1", "A2", "B3", "B4", "C5", "C6", "D7", "E8", "F9"};

    public static final String MESSAGE_SUBJECT_GRADE_CONSTRAINTS = "Subject grade should be alphanumeric and should be"
            + " one of the following: \n" + Arrays.deepToString(SUBJECT_GRADE) + ".";

    public static final String MESSAGE_DUPLICATE_SUBJECT = "There should not be duplicate subject(s) assigned "
            + "to student.";

    // Use for the calculation of the L1R5 subjects
    public static final String[] L1_SUBJECT = {"English", "HChi", "HTamil", "HMalay"};
    public static final String[] R1_SUBJECT = {"Hist", "Geog", "Com.Hum",  "ELit", "CLit", "MLit", "TLit", "HArt",
                                               "HMusic", "BIndo", "CSP", "MSP"};
    public static final String[] R2_SUBJECT = {"EMath", "AMath", "Phy", "Chem", "Bio", "Sci"};
    public static final String[] R3_SUBJECT = {"Hist", "Geog", "ComHum",  "ELit", "CLit", "MLit", "TLit", "HArt",
                                               "H.Music", "BIndo", "CSP", "MSP", "EMath", "AMath", "Phy", "Chem",
                                               "Bio", "Sci"};
    public static final String[] R4_R5_SUBJECT = {"English", "Chinese", "HChi", "CSP", "French", "German", "Spanish",
                                                  "Hindi", "Urdu", "Gujarati", "Panjabi", "Bengali", "Burmese",
                                                  "Thai", "Jap", "BIndo", "Tamil", "HTamil", "Malay", "HMalay",
                                                  "MSP", "EMath", "AMath", "Phy", "Chem", "Bio", "Sci", "Hist", "Geog",
                                                  "ComHum", "ELit", "CLit", "MLit", "TLit", "Music", "HMusic", "Art",
                                                  "HArt", "DnT", "Comp", "FnN", "PoA", "Econs", "Drama", "PE",
                                                  "Biz", "Biotech", "Design"};

    // Use for the calculation of the L1B4 subjects (A, B, C, D)
    public static final String[] R1A_SUBJECT = {"Art", "Biz", "ComHum", "Econs", "Geog", "HArt", "HMusic", "Hist",
                                                "ELit", "CLit", "MLit", "TLit", "Music"};
    public static final String[] R1BCD_SUBJECT = {"EMath", "AMath"};
    public static final String[] R2A_SUBJECT = {"AMath", "Art", "Biz", "Chinese", "ComHum", "DnT", "Design", "Econs",
                                                "EMath", "FnN", "Geog", "HArt", "HChi", "HMalay", "HTamil", "Hist",
                                                "ELit", "CLit", "MLit", "TLit", "Malay", "Music", "PoA", "Tamil"};
    public static final String[] R2B_SUBJECT = {"Art", "Biz", "ComHum", "Econs", "Geog", "HArt", "HMusic", "Hist",
                                                "ELit", "CLit", "TLit", "MLit", "Music", "PoA"};
    public static final String[] R2C_SUBJECT = {"Bio", "Biotech", "Chem", "Sci", "DnT", "FnN", "Phy", "Comp"};
    public static final String[] R2D_SUBJECT = {"Art", "Bio", "Biotech", "Chem", "Sci", "Comp", "DnT", "Design", "FnN",
                                                "HArt", "Phy", "Sci"};
    public static final String[] R3_R4_SUBJECT = R4_R5_SUBJECT;

    public final String subjectName;
    public final String subjectGrade;

    /**
     * Default constructor of Subject Object
     */
    public Subject() {
        this.subjectName = "";
        this.subjectGrade = "";
    }

    /**
     * Constructs a {@code Subject}.
     *
     * @param subjectName  A valid subject name.
     * @param subjectGrade A valid subject grade.
     */
    public Subject(String subjectName, String subjectGrade) {
        requireNonNull(subjectName);
        checkArgument(isValidSubjectName(subjectName), MESSAGE_SUBJECT_NAME_CONSTRAINTS);
        this.subjectName = subjectName;

        requireNonNull(subjectGrade);
        checkArgument(isValidSubjectGrade(subjectGrade), MESSAGE_SUBJECT_GRADE_CONSTRAINTS);
        this.subjectGrade = subjectGrade;
    }

    /**
     * Constructs a {@code Subject} by splitting the subject string into {@code subjectName}.
     *
     * @param subject A valid subject string.
     */
    public Subject(String subject) {
        requireNonNull(subject);
        String[] splitSubjectStr = subject.trim().split("\\s+");
        String subjectName = splitSubjectStr[0];
        String subjectGrade = splitSubjectStr[1];
        this.subjectName = subjectName;
        this.subjectGrade = subjectGrade;
    }

    /**
     * Returns true if a given string is a valid subject name.
     */
    public static boolean isValidSubjectName(String test) {
        for (String validSubjectName : SUBJECT_NAME) {
            if (test.equals(validSubjectName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if a given string is a valid subject grade.
     */

    public static boolean isValidSubjectGrade(String test) {
        for (String validSubjectGrade : SUBJECT_GRADE) {
            if (test.equals(validSubjectGrade)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (this == other) { //same object
            return true;
        }
        if (!(other instanceof Subject)) {
            return false;
        }
        Subject object = (Subject) other;
        return (this.subjectName.equals(object.subjectName) && this.subjectGrade.equals(object.subjectGrade));
    }

    @Override
    public int hashCode() {
        int hash = 17;
        hash = 37 * hash + subjectName.hashCode();
        hash = 37 * hash + subjectGrade.hashCode();
        return hash;
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + subjectName + ' ' + subjectGrade + ']';
    }
    //@@author

    //@@ author johnnychanjx
    public String nameToString() {
        return subjectName;
    }
    //@@author johnnychanjx
    public String gradeToString() {
        return subjectGrade;
    }

    /**
     * @@author johnnychanjx
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
    //@@author
}

