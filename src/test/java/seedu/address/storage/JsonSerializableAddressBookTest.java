package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.AddressBook;
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

    //    @Test
    //    public void toModelType_duplicatePersons_throwsIllegalValueException() throws Exception {
    //        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(DUPLICATE_PERSON_FILE,
    //                JsonSerializableAddressBook.class).get();
    //        assertThrows(IllegalValueException.class, JsonSerializableAddressBook.MESSAGE_DUPLICATE_PERSON,
    //                dataFromFile::toModelType);
    //    }
}
