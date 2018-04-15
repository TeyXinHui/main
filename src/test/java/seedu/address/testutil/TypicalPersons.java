package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_INJURIES_HISTORY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NRIC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NRIC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_REMARK;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SUBJECT_BIOLOGY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SUBJECT_ENGLISH;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SUBJECT_HISTORY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SUBJECT_MALAY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SUBJECT_MATHEMATICS;
import static seedu.address.logic.commands.CommandTestUtil.VALID_SUBJECT_PHYSICS;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.appointment.Appointment;
import seedu.address.model.appointment.exceptions.DuplicateAppointmentException;
import seedu.address.model.person.Person;
import seedu.address.model.person.exceptions.DuplicatePersonException;

/**
 * A utility class containing a list of {@code Person} objects to be used in tests.
 */
public class TypicalPersons {

    public static final Person ALICE = new PersonBuilder().withName("Alice Pauline").withNric("S8535525Z")
            .withTags("friends").withSubjects("English A1", "EMath A1", "Hist A1", "HTamil A1", "Chem A1", "Phy A1")
            .withRemark(VALID_REMARK).withCca("", "").withInjuriesHistory(VALID_INJURIES_HISTORY)
            .withNameOfKin("Betty", "98763451", "betty@gmail.com", "Mother").build();
    public static final Person BENSON = new PersonBuilder().withName("Benson Meier").withNric("S9123456X")
            .withTags("owesMoney", "friends").withSubjects("English A1", "EMath A1", "Hist A1", "HTamil A1",
                    "Chem A1", "Phy A2").withRemark(VALID_REMARK).withCca("", "")
            .withInjuriesHistory(VALID_INJURIES_HISTORY)
            .withNameOfKin("Ben", "90875674", "ben@gmail.com", "Father").build();
    public static final Person CARL = new PersonBuilder().withName("Carl Kurz").withNric("S9535256J")
            .withSubjects("English A1", "EMath A1", "Hist A1", "HTamil A1", "Chem A1", "Phy A1")
            .withRemark(VALID_REMARK).withCca("",  "").withInjuriesHistory("").build();
    public static final Person DANIEL = new PersonBuilder().withName("Daniel Meier").withNric("S8765253I")
            .withSubjects("English A1", "EMath A1", "Hist A1", "HTamil A1", "Chem A1", "Phy A1")
            .withRemark(VALID_REMARK).withCca("", "").withInjuriesHistory("").build();
    public static final Person ELLE = new PersonBuilder().withName("Elle Meyer").withNric("S9482224A")
            .withSubjects("English A1", "EMath A1", "Hist A1", "HTamil A1", "Chem A1", "Phy A1")
            .withRemark(VALID_REMARK).withCca("", "").withInjuriesHistory("").build();
    public static final Person FIONA = new PersonBuilder().withName("Fiona Kunz").withNric("S9482427J")
            .withSubjects("English A1", "EMath A1", "Hist A1", "HTamil A1", "Chem A1", "Phy A1")
            .withRemark(VALID_REMARK).withCca("", "").withInjuriesHistory("").build();
    public static final Person GEORGE = new PersonBuilder().withName("George Best").withNric("S9482442U")
            .withSubjects("English A1", "EMath A1", "Hist A1", "HTamil A1", "Chem A1", "Phy A1")
            .withRemark(VALID_REMARK).withCca("", "").withInjuriesHistory(VALID_INJURIES_HISTORY).build();

    // Manually added
    public static final Person HOON = new PersonBuilder().withName("Hoon Meier").withNric("S8482424Z")
            .withSubjects("English A1", "EMath A1", "Hist A1", "HTamil A1", "Chem A1", "Phy A1")
            .withRemark(VALID_REMARK).withCca("", "").withInjuriesHistory("").build();
    public static final Person IDA = new PersonBuilder().withName("Ida Mueller").withNric("S8482131K")
            .withSubjects("English A1", "EMath A1", "Hist A1", "HTamil A1", "Chem A1", "Phy A1")
            .withRemark(VALID_REMARK).withCca("", "").withInjuriesHistory("").build();

    // Manually added - Person's details found in {@code CommandTestUtil}
    public static final Person AMY = new PersonBuilder().withName(VALID_NAME_AMY).withNric(VALID_NRIC_AMY)
            .withTags(VALID_TAG_FRIEND).withSubjects(VALID_SUBJECT_MATHEMATICS, VALID_SUBJECT_PHYSICS,
                      VALID_SUBJECT_ENGLISH, VALID_SUBJECT_MALAY , VALID_SUBJECT_BIOLOGY, VALID_SUBJECT_HISTORY)
            .withRemark(VALID_REMARK).withCca(VALID_REMARK, VALID_REMARK).withInjuriesHistory(VALID_REMARK).build();
    public static final Person BOB = new PersonBuilder().withName(VALID_NAME_BOB).withNric(VALID_NRIC_BOB)
            .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).withSubjects(VALID_SUBJECT_MATHEMATICS,
                      VALID_SUBJECT_PHYSICS, VALID_SUBJECT_ENGLISH, VALID_SUBJECT_MALAY , VALID_SUBJECT_BIOLOGY,
                      VALID_SUBJECT_HISTORY).withRemark("").withCca(VALID_REMARK, VALID_REMARK)
                    .withInjuriesHistory(VALID_REMARK).build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    //Appointments
    public static final Appointment A1 = new Appointment("Alex Yeoh", "Consultation", "04042018",
            "1200", "1300");
    public static final Appointment A2 = new Appointment("David Li", "Remedial", "05052018",
            "1400", "1600");
    public static final Appointment A3 = new Appointment("Bon", "Remedial", "01052018",
            "0800", "1000");
    public static final Appointment A4 = new Appointment("Carl", "Consultation", "18042018",
            "1400", "1500");
    public static final Appointment A5 = new Appointment("Emily", "Remedial", "01042018",
            "0900", "1000");

    private TypicalPersons() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical persons.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Person person : getTypicalPersons()) {
            try {
                ab.addPerson(person);
            } catch (DuplicatePersonException e) {
                throw new AssertionError("not possible");
            }
        }
        for (Appointment appointment : getTypicalAppointments()) {
            try {
                ab.addAppointment(appointment);
            } catch (DuplicateAppointmentException e) {
                throw new AssertionError("not possible");
            }
        }
        return ab;
    }

    public static List<Person> getTypicalPersons() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }

    public static List<Appointment> getTypicalAppointments() {
        return new ArrayList<>(Arrays.asList(A1, A2, A3, A4, A5));
    }
}
