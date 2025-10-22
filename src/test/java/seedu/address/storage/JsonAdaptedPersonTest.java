package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.storage.JsonAdaptedPerson.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;

public class JsonAdaptedPersonTest {
    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final String VALID_EMAIL = BENSON.getEmail().toString();
    private static final String VALID_ADDRESS = BENSON.getAddress().toString();
    private static final String VALID_REMARK = BENSON.getRemark().toString();
    private static final List<JsonAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validPersonDetails_returnsPerson() throws Exception {
        JsonAdaptedPerson person = new JsonAdaptedPerson(BENSON);
        assertEquals(BENSON, person.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(INVALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_REMARK, VALID_TAGS);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(null, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_REMARK, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, INVALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_REMARK, VALID_TAGS);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, null, VALID_EMAIL, VALID_ADDRESS, VALID_REMARK, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_returnsPersonWithEmptyEmail() throws Exception {
        // Email is now optional - invalid email should result in empty email, not throw exception
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, INVALID_EMAIL, VALID_ADDRESS, VALID_REMARK, VALID_TAGS);

        // Should not throw exception, should create person with empty email
        Person modelPerson = person.toModelType();
        assertEquals("", modelPerson.getEmail().value); // Email should be empty string
    }

    @Test
    public void toModelType_nullEmail_returnsPersonWithEmptyEmail() throws Exception {
        // Email is now optional - null email should result in empty email, not throw exception
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, null, VALID_ADDRESS, VALID_REMARK, VALID_TAGS);

        // Should not throw exception, should create person with empty email
        Person modelPerson = person.toModelType();
        assertEquals("", modelPerson.getEmail().value); // Email should be empty string
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, INVALID_ADDRESS, VALID_REMARK, VALID_TAGS);
        String expectedMessage = Address.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, null, VALID_REMARK, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, person::toModelType);
    }

    // Optional: Test that empty email string works
    @Test
    public void toModelType_emptyEmail_returnsPersonWithEmptyEmail() throws Exception {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, "", VALID_ADDRESS, VALID_REMARK, VALID_TAGS);

        Person modelPerson = person.toModelType();
        assertEquals("", modelPerson.getEmail().value);
    }

    @Test
    public void toModelType_whitespaceOnlyEmail_returnsPersonWithEmptyEmail() throws Exception {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, "   ", VALID_ADDRESS, VALID_REMARK, VALID_TAGS);

        Person modelPerson = person.toModelType();
        assertEquals("", modelPerson.getEmail().value);
    }

    @Test
    public void toModelType_nullRemark_returnsPersonWithEmptyRemark() throws Exception {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, null, VALID_TAGS);

        Person modelPerson = person.toModelType();
        assertEquals("", modelPerson.getRemark().value);
    }

    @Test
    public void toModelType_emptyRemark_returnsPersonWithEmptyRemark() throws Exception {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, "", VALID_TAGS);

        Person modelPerson = person.toModelType();
        assertEquals("", modelPerson.getRemark().value);
    }

    @Test
    public void toModelType_whitespaceOnlyRemark_returnsPersonWithEmptyRemark() throws Exception {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, "   ", VALID_TAGS);

        Person modelPerson = person.toModelType();
        assertEquals("", modelPerson.getRemark().value);
    }

    @Test
    public void toModelType_emptyName_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson("", VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_REMARK, VALID_TAGS);

        assertThrows(IllegalValueException.class, person::toModelType);
    }

    @Test
    public void toModelType_whitespaceOnlyName_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson("   ", VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_REMARK, VALID_TAGS);

        assertThrows(IllegalValueException.class, person::toModelType);
    }

    @Test
    public void toModelType_emptyPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, "", VALID_EMAIL, VALID_ADDRESS, VALID_REMARK, VALID_TAGS);

        assertThrows(IllegalValueException.class, person::toModelType);
    }

    @Test
    public void toModelType_whitespaceOnlyPhone_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, "   ", VALID_EMAIL, VALID_ADDRESS, VALID_REMARK, VALID_TAGS);

        assertThrows(IllegalValueException.class, person::toModelType);
    }

    @Test
    public void toModelType_emptyAddress_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, "", VALID_REMARK, VALID_TAGS);

        assertThrows(IllegalValueException.class, person::toModelType);
    }

    @Test
    public void toModelType_whitespaceOnlyAddress_throwsIllegalValueException() {
        JsonAdaptedPerson person =
                new JsonAdaptedPerson(VALID_NAME, VALID_PHONE, VALID_EMAIL, "   ", VALID_REMARK, VALID_TAGS);

        assertThrows(IllegalValueException.class, person::toModelType);
    }
}
