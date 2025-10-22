package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.core.GuiSettings;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.UserPrefs;

public class StorageManagerTest {

    @TempDir
    public Path testFolder;

    private StorageManager storageManager;

    @BeforeEach
    public void setUp() {
        JsonAddressBookStorage addressBookStorage = new JsonAddressBookStorage(getTempFilePath("ab"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs"));
        storageManager = new StorageManager(addressBookStorage, userPrefsStorage);
    }

    private Path getTempFilePath(String fileName) {
        return testFolder.resolve(fileName);
    }

    @Test
    public void prefsReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonUserPrefsStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonUserPrefsStorageTest} class.
         */
        UserPrefs original = new UserPrefs();
        original.setGuiSettings(new GuiSettings(300, 600, 4, 6));
        storageManager.saveUserPrefs(original);
        UserPrefs retrieved = storageManager.readUserPrefs().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void addressBookReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonAddressBookStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonAddressBookStorageTest} class.
         */
        AddressBook original = getTypicalAddressBook();
        storageManager.saveAddressBook(original);
        ReadOnlyAddressBook retrieved = storageManager.readAddressBook().get();
        assertEquals(original, new AddressBook(retrieved));
    }

    @Test
    public void getAddressBookFilePath() {
        assertNotNull(storageManager.getAddressBookFilePath());
    }

    @Test
    public void getUserPrefsFilePath_returnsUserPrefsStoragePath() throws Exception {
        // First, save some user prefs to ensure the file exists
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setGuiSettings(new GuiSettings(300, 600, 4, 6));
        userPrefs.setAddressBookFilePath(Paths.get("addressbook.json"));
        storageManager.saveUserPrefs(userPrefs);

        // Now read should work
        Path expectedPath = storageManager.getUserPrefsFilePath();
        assertNotNull(expectedPath);

        // Verify it has the expected file name
        assertEquals("prefs", expectedPath.getFileName().toString());
    }

    @Test
    public void getUserPrefsFilePath_consistentWithUnderlyingStorage() {
        // Create a specific path for testing
        Path testPath = testFolder.resolve("testUserPrefs.json");
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(testPath);
        JsonAddressBookStorage addressBookStorage = new JsonAddressBookStorage(getTempFilePath("ab"));
        StorageManager testStorageManager = new StorageManager(addressBookStorage, userPrefsStorage);

        // Verify the path matches what was set in the UserPrefsStorage
        assertEquals(testPath, testStorageManager.getUserPrefsFilePath());
        assertEquals(userPrefsStorage.getUserPrefsFilePath(), testStorageManager.getUserPrefsFilePath());
    }

    @Test
    public void getUserPrefsFilePath_delegatesToUserPrefsStorage() {
        // Create mock-like behavior using real objects
        Path specificPrefsPath = testFolder.resolve("specificPrefs.json");
        JsonUserPrefsStorage specificUserPrefsStorage = new JsonUserPrefsStorage(specificPrefsPath);
        JsonAddressBookStorage addressBookStorage = new JsonAddressBookStorage(getTempFilePath("ab"));

        StorageManager specificStorageManager = new StorageManager(addressBookStorage, specificUserPrefsStorage);

        // The StorageManager should return exactly what the underlying UserPrefsStorage returns
        assertEquals(specificUserPrefsStorage.getUserPrefsFilePath(), specificStorageManager.getUserPrefsFilePath());
    }

    @Test
    public void getUserPrefsFilePath_differentPaths_returnsCorrectly() {
        // Test with various path configurations
        Path[] testPaths = {
                testFolder.resolve("prefs1.json"),
                testFolder.resolve("deep/nested/prefs.json"),
                testFolder.resolve("..relative/path.json"),
                Paths.get("simple/path.json")
        };

        for (Path testPath : testPaths) {
            JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(testPath);
            JsonAddressBookStorage addressBookStorage = new JsonAddressBookStorage(getTempFilePath("ab"));
            StorageManager testStorageManager = new StorageManager(addressBookStorage, userPrefsStorage);

            assertEquals(testPath, testStorageManager.getUserPrefsFilePath());
        }
    }
}
