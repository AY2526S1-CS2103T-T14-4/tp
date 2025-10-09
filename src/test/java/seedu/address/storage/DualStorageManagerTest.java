package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.UserPrefs;

public class DualStorageManagerTest {

    @TempDir
    public Path testFolder;

    private DualStorageManager dualStorageManager;

    @BeforeEach
    public void setUp() {
        Path jsonFilePath = testFolder.resolve("addressbook.json");
        Path textFilePath = testFolder.resolve("addressbook.txt");
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(testFolder.resolve("prefs.json"));
        dualStorageManager = new DualStorageManager(jsonFilePath, textFilePath, userPrefsStorage);
    }

    @Test
    public void saveAddressBook_savesToBothFormats() throws Exception {
        AddressBook original = getTypicalAddressBook();
        dualStorageManager.saveAddressBook(original);

        ReadOnlyAddressBook retrieved = dualStorageManager.readAddressBook().get();
        assertEquals(original, new AddressBook(retrieved));
    }

    @Test
    public void getAddressBookFilePath() {
        assertNotNull(dualStorageManager.getAddressBookFilePath());
    }
}