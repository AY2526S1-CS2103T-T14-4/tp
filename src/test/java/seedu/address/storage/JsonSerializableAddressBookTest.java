package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static seedu.address.testutil.Assert.assertThrows;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.AddressBook;
import seedu.address.model.person.Person;
import seedu.address.testutil.TypicalPersons;

public class JsonSerializableAddressBookTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableAddressBookTest");
    private static final Path TYPICAL_PERSONS_FILE = TEST_DATA_FOLDER.resolve("typicalPersonsAddressBook.json");
    private static final Path INVALID_PERSON_FILE = TEST_DATA_FOLDER.resolve("invalidPersonAddressBook.json");
    private static final Path DUPLICATE_PERSON_FILE = TEST_DATA_FOLDER.resolve("duplicatePersonAddressBook.json");
    private static final Path MULTIPLE_DUPLICATE_PERSON_FILE = TEST_DATA_FOLDER
            .resolve("multipleDuplicatePersonAddressBook.json");
    private static final Path SAME_NAME_DIFFERENT_PHONE_FILE = TEST_DATA_FOLDER
            .resolve("sameNameDifferentPhoneAddressBook.json");
    private static final Path SAME_PHONE_DIFFERENT_NAME_FILE = TEST_DATA_FOLDER
            .resolve("samePhoneDifferentNameAddressBook.json");
    private static final Path MIXED_VALID_DUPLICATE_FILE = TEST_DATA_FOLDER
            .resolve("mixedValidAndDuplicateAddressBook.json");
    private static final Path MISSING_REQUIRED_FIELDS_FILE = TEST_DATA_FOLDER
            .resolve("missingRequiredFieldsAddressBook.json");
    private static final Path MISSING_OPTIONAL_FIELDS_FILE = TEST_DATA_FOLDER
            .resolve("missingOptionalFieldsAddressBook.json");
    private static final Path INVALID_EMAIL_FILE = TEST_DATA_FOLDER
            .resolve("invalidEmailAddressBook.json");
    private static final Path MIXED_VALID_INVALID_FILE = TEST_DATA_FOLDER
            .resolve("mixedValidInvalidAddressBook.json");
    private static final Path ALL_INVALID_ENTRIES_FILE = TEST_DATA_FOLDER
            .resolve("allInvalidEntriesAddressBook.json");
    private static final Path WHITESPACE_FIELDS_FILE = TEST_DATA_FOLDER
            .resolve("whitespaceFieldsAddressBook.json");
    private static final Path EMPTY_ADDRESS_BOOK_FILE = TEST_DATA_FOLDER
            .resolve("emptyAddressBook.json");


    @Test
    public void toModelType_typicalPersonsFile_success() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(TYPICAL_PERSONS_FILE,
                JsonSerializableAddressBook.class).get();
        AddressBook addressBookFromFile = dataFromFile.toModelType();
        AddressBook typicalPersonsAddressBook = TypicalPersons.getTypicalAddressBook();
        assertEquals(addressBookFromFile, typicalPersonsAddressBook);
    }

    @Test
    public void toModelType_invalidPersonFile_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(INVALID_PERSON_FILE,
                JsonSerializableAddressBook.class).get();
        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_duplicatePersons_removedAutomatically() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(DUPLICATE_PERSON_FILE,
                JsonSerializableAddressBook.class).get();

        AddressBook addressBook = dataFromFile.toModelType();

        // Verify only one person remains after duplicate removal
        assertEquals(1, addressBook.getPersonList().size());

        // verify the remaining person's details
        assertEquals("Alice Pauline", addressBook.getPersonList().get(0).getName().toString());
    }

    @Test
    public void toModelType_multipleDuplicatePersons_removedAutomatically() throws Exception {
        // Create JSON file with 3+ duplicate entries (same name + phone)
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(
                MULTIPLE_DUPLICATE_PERSON_FILE, JsonSerializableAddressBook.class).get();
        AddressBook addressBook = dataFromFile.toModelType();

        // Verify only one person remains
        assertEquals(1, addressBook.getPersonList().size());
    }

    @Test
    public void toModelType_sameNameDifferentPhone_bothKept() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(
                SAME_NAME_DIFFERENT_PHONE_FILE, JsonSerializableAddressBook.class).get();
        AddressBook addressBook = dataFromFile.toModelType();

        // Both should be kept since phone numbers differ
        assertEquals(2, addressBook.getPersonList().size());
    }

    @Test
    public void toModelType_samePhoneDifferentName_bothKept() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(
                SAME_PHONE_DIFFERENT_NAME_FILE, JsonSerializableAddressBook.class).get();
        AddressBook addressBook = dataFromFile.toModelType();

        // Both should be kept since names differ
        assertEquals(2, addressBook.getPersonList().size());
    }

    @Test
    public void toModelType_mixedValidAndDuplicate_duplicatesRemoved() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(
                MIXED_VALID_DUPLICATE_FILE, JsonSerializableAddressBook.class).get();
        AddressBook addressBook = dataFromFile.toModelType();

        // Should have 2 unique persons (Bob and one Alice)
        assertEquals(2, addressBook.getPersonList().size());
    }

    @Test
    public void toModelType_missingRequiredFields_skipsEntry() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(
                MISSING_REQUIRED_FIELDS_FILE, JsonSerializableAddressBook.class).get();
        AddressBook model = dataFromFile.toModelType();

        // Should only have 2 valid persons (missing name skipped)
        assertEquals(2, model.getPersonList().size());
    }

    @Test
    public void toModelType_missingOptionalFields_usesDefaults() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(
                MISSING_OPTIONAL_FIELDS_FILE, JsonSerializableAddressBook.class).get();
        AddressBook model = dataFromFile.toModelType();

        // Should have person with default email and remark
        assertEquals(1, model.getPersonList().size());
        Person loadedPerson = model.getPersonList().get(0);
        assertEquals("", loadedPerson.getEmail().value);
        assertEquals("", loadedPerson.getRemark().value);
    }

    @Test
    public void toModelType_invalidEmail_usesEmptyEmail() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(
                INVALID_EMAIL_FILE, JsonSerializableAddressBook.class).get();
        AddressBook model = dataFromFile.toModelType();

        // Should have person with empty email
        assertEquals(1, model.getPersonList().size());
        assertEquals("", model.getPersonList().get(0).getEmail().value);
    }

    @Test
    public void toModelType_mixedValidAndInvalid_preservesValidOnes() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(
                MIXED_VALID_INVALID_FILE, JsonSerializableAddressBook.class).get();
        AddressBook model = dataFromFile.toModelType();

        // Should only have 2 valid persons (1 and 3)
        assertEquals(2, model.getPersonList().size());
        assertEquals("Valid Person One", model.getPersonList().get(0).getName().toString());
        assertEquals("Valid Person Three", model.getPersonList().get(1).getName().toString());
    }

    @Test
    public void toModelType_allEntriesInvalid_throwsException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(
                ALL_INVALID_ENTRIES_FILE, JsonSerializableAddressBook.class).get();

        assertThrows(IllegalValueException.class, dataFromFile::toModelType);
    }

    @Test
    public void toModelType_whitespaceFields_handlesAppropriately() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(
                WHITESPACE_FIELDS_FILE, JsonSerializableAddressBook.class).get();
        AddressBook model = dataFromFile.toModelType();

        // Should only have 1 valid person (the one with whitespace email/remark but valid name/phone/address)
        assertEquals(1, model.getPersonList().size());
        Person loadedPerson = model.getPersonList().get(0);
        assertEquals("Another Valid", loadedPerson.getName().toString());
        assertEquals("", loadedPerson.getEmail().value); // Whitespace email becomes empty
        assertEquals("", loadedPerson.getRemark().value); // Whitespace remark becomes empty
    }

    @Test
    public void toModelType_allEntriesInvalid_throwsExceptionWithMessage() {
        try {
            JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(
                    ALL_INVALID_ENTRIES_FILE, JsonSerializableAddressBook.class).get();

            dataFromFile.toModelType();
            fail("Expected IllegalValueException to be thrown");
        } catch (IllegalValueException e) {
            assertTrue(e.getMessage().contains("No valid persons found in data file"));
            assertTrue(e.getMessage().contains("All entries were skipped due to errors"));
        } catch (DataLoadingException e) {
            fail("Unexpected DataLoadingException: " + e.getMessage());
        } catch (Exception e) {
            fail("Expected IllegalValueException, but got: " + e.getClass().getSimpleName());
        }
    }

    @Test
    public void toModelType_onlyInvalidEntries_appropriateLogging() {
        try {
            JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(
                    ALL_INVALID_ENTRIES_FILE, JsonSerializableAddressBook.class).get();

            dataFromFile.toModelType();
            fail("Expected IllegalValueException to be thrown");
        } catch (IllegalValueException e) {
            // Expected - test passes
        } catch (DataLoadingException e) {
            fail("Unexpected DataLoadingException: " + e.getMessage());
        } catch (Exception e) {
            fail("Expected IllegalValueException, but got: " + e.getClass().getSimpleName());
        }
    }

    @Test
    public void toModelType_comprehensiveMixed_handlesCorrectly() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(
                MIXED_VALID_INVALID_FILE, JsonSerializableAddressBook.class).get();
        AddressBook model = dataFromFile.toModelType();

        // Should have 2 unique valid persons after removing duplicates and invalid entries
        assertEquals(2, model.getPersonList().size());
    }

    @Test
    public void toModelType_emptyAddressBook_returnsEmpty() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(
                EMPTY_ADDRESS_BOOK_FILE, JsonSerializableAddressBook.class).get();
        AddressBook model = dataFromFile.toModelType();

        assertEquals(0, model.getPersonList().size());
    }

    // Test the error extraction methods using reflection
    @Test
    public void extractPersonInfoFromError_withPersonInfo_returnsName() throws Exception {
        JsonSerializableAddressBook addressBook = new JsonSerializableAddressBook(new ArrayList<>());

        Method method = JsonSerializableAddressBook.class.getDeclaredMethod("extractPersonInfoFromError", String.class);
        method.setAccessible(true);

        String result = (String) method.invoke(addressBook, "Invalid phone for person: John Doe");
        assertEquals("John Doe", result);
    }

    @Test
    public void extractPersonInfoFromError_withoutPersonInfo_returnsUnknown() throws Exception {
        JsonSerializableAddressBook addressBook = new JsonSerializableAddressBook(new ArrayList<>());

        Method method = JsonSerializableAddressBook.class.getDeclaredMethod("extractPersonInfoFromError", String.class);
        method.setAccessible(true);

        String result = (String) method.invoke(addressBook, "Generic error message");
        assertEquals("Unknown", result);
    }

    @Test
    public void extractReasonFromError_variousErrorTypes_returnsCleanReasons() throws Exception {
        JsonSerializableAddressBook addressBook = new JsonSerializableAddressBook(new ArrayList<>());

        Method method = JsonSerializableAddressBook.class.getDeclaredMethod("extractReasonFromError", String.class);
        method.setAccessible(true);

        // Test various error patterns
        String result = (String) method.invoke(addressBook, "Missing required field: name");
        assertEquals("Missing name field", result);

        result = (String) method.invoke(addressBook, "Missing required field: phone");
        assertEquals("Missing phone field", result);

        result = (String) method.invoke(addressBook, "Missing required field: address");
        assertEquals("Missing address field", result);

        result = (String) method.invoke(addressBook, "Invalid name format");
        assertEquals("Invalid name format", result);

        result = (String) method.invoke(addressBook, "Invalid phone format");
        assertEquals("Invalid phone number", result);

        result = (String) method.invoke(addressBook, "Invalid address format");
        assertEquals("Invalid address format", result);

        result = (String) method.invoke(addressBook, "Invalid email format");
        assertEquals("Invalid email format", result);

        result = (String) method.invoke(addressBook, "Some other error");
        assertEquals("Some other error", result);
    }

}
