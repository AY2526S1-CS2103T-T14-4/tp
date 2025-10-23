package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static seedu.address.testutil.Assert.assertThrows;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.UserPrefs;

public class JsonUserPrefsStorageTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonUserPrefsStorageTest");

    @TempDir
    public Path testFolder;

    @Test
    public void readUserPrefs_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> readUserPrefs(null));
    }

    private Optional<UserPrefs> readUserPrefs(String userPrefsFileInTestDataFolder) throws DataLoadingException {
        Path prefsFilePath = addToTestDataPathIfNotNull(userPrefsFileInTestDataFolder);
        return new JsonUserPrefsStorage(prefsFilePath).readUserPrefs(prefsFilePath);
    }

    @Test
    public void readUserPrefs_missingFile_emptyResult() throws DataLoadingException {
        assertFalse(readUserPrefs("NonExistentFile.json").isPresent());
    }

    @Test
    public void readUserPrefs_notJsonFormat_exceptionThrown() {
        assertThrows(DataLoadingException.class, () -> readUserPrefs("NotJsonFormatUserPrefs.json"));
    }

    private Path addToTestDataPathIfNotNull(String userPrefsFileInTestDataFolder) {
        return userPrefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(userPrefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void readUserPrefs_fileInOrder_successfullyRead() throws DataLoadingException {
        UserPrefs expected = getTypicalUserPrefs();
        UserPrefs actual = readUserPrefs("TypicalUserPref.json").get();
        assertEquals(expected, actual);
    }

    @Test
    public void readUserPrefs_valuesMissingFromFile_defaultValuesUsed() throws DataLoadingException {
        UserPrefs actual = readUserPrefs("EmptyUserPrefs.json").get();
        assertEquals(new UserPrefs(), actual);
    }

    @Test
    public void readUserPrefs_extraValuesInFile_extraValuesIgnored() throws DataLoadingException {
        UserPrefs expected = getTypicalUserPrefs();
        UserPrefs actual = readUserPrefs("ExtraValuesUserPref.json").get();

        assertEquals(expected, actual);
    }

    private UserPrefs getTypicalUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setGuiSettings(new GuiSettings(1000, 500, 300, 100));
        userPrefs.setAddressBookFilePath(Paths.get("addressbook.json"));
        return userPrefs;
    }

    @Test
    public void savePrefs_nullPrefs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveUserPrefs(null, "SomeFile.json"));
    }

    @Test
    public void saveUserPrefs_nullFilePath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> saveUserPrefs(new UserPrefs(), null));
    }

    /**
     * Saves {@code userPrefs} at the specified {@code prefsFileInTestDataFolder} filepath.
     */
    private void saveUserPrefs(UserPrefs userPrefs, String prefsFileInTestDataFolder) {
        try {
            new JsonUserPrefsStorage(addToTestDataPathIfNotNull(prefsFileInTestDataFolder))
                    .saveUserPrefs(userPrefs);
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file", ioe);
        }
    }

    @Test
    public void saveUserPrefs_allInOrder_success() throws DataLoadingException, IOException {

        UserPrefs original = new UserPrefs();
        original.setGuiSettings(new GuiSettings(1200, 200, 0, 2));

        Path pefsFilePath = testFolder.resolve("TempPrefs.json");
        JsonUserPrefsStorage jsonUserPrefsStorage = new JsonUserPrefsStorage(pefsFilePath);

        //Try writing when the file doesn't exist
        jsonUserPrefsStorage.saveUserPrefs(original);
        UserPrefs readBack = jsonUserPrefsStorage.readUserPrefs().get();
        assertEquals(original, readBack);

        //Try saving when the file exists
        original.setGuiSettings(new GuiSettings(5, 5, 5, 5));
        jsonUserPrefsStorage.saveUserPrefs(original);
        readBack = jsonUserPrefsStorage.readUserPrefs().get();
        assertEquals(original, readBack);
    }

    @Test
    public void getUserPrefsFilePath_returnsCorrectPath() {
        Path expectedPath = Paths.get("test/path/userPrefs.json");
        JsonUserPrefsStorage storage = new JsonUserPrefsStorage(expectedPath);

        assertEquals(expectedPath, storage.getUserPrefsFilePath());
    }

    @Test
    public void getUserPrefsFilePath_withTempDir_returnsCorrectPath() {
        Path tempPath = testFolder.resolve("testPrefs.json");
        JsonUserPrefsStorage storage = new JsonUserPrefsStorage(tempPath);

        assertEquals(tempPath, storage.getUserPrefsFilePath());
    }

    @Test
    public void getUserPrefsFilePath_afterConstruction_unchanged() {
        Path initialPath = Paths.get("initial/path.json");
        JsonUserPrefsStorage storage = new JsonUserPrefsStorage(initialPath);

        // Call multiple times to ensure consistency
        assertEquals(initialPath, storage.getUserPrefsFilePath());
        assertEquals(initialPath, storage.getUserPrefsFilePath());
        assertEquals(initialPath, storage.getUserPrefsFilePath());
    }

    @Test
    public void readUserPrefs_usesCorrectFilePath() throws DataLoadingException {
        Path testPath = testFolder.resolve("testReadPrefs.json");
        JsonUserPrefsStorage storage = new JsonUserPrefsStorage(testPath);

        // Verify the file path matches what was passed to constructor
        assertEquals(testPath, storage.getUserPrefsFilePath());

        // Create a test file at that path and verify it can be read
        UserPrefs testPrefs = new UserPrefs();
        testPrefs.setGuiSettings(new GuiSettings(800, 600, 10, 20));
        testPrefs.setAddressBookFilePath(Paths.get("testAddressBook.json"));

        try {
            storage.saveUserPrefs(testPrefs);
            Optional<UserPrefs> readPrefs = storage.readUserPrefs();
            assertTrue(readPrefs.isPresent());
            assertEquals(testPrefs, readPrefs.get());
        } catch (IOException e) {
            fail("Should not throw IOException during save: " + e.getMessage());
        }
    }

    @Test
    public void getUserPrefsFilePath_nullPath_throwsNullPointerException() {
        // Verify that constructor rejects null paths
        assertThrows(NullPointerException.class, () -> new JsonUserPrefsStorage(null));
    }
}
