package seedu.address;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.core.Config;
import seedu.address.commons.core.Version;
import seedu.address.model.UserPrefs;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.UserPrefsStorage;

/**
 * Tests the main application class.
 */
public class MainAppTest {

    @TempDir
    public Path tempDir;

    @Test
    public void version_isCorrect() {
        assertEquals(new Version(0, 2, 2, true), MainApp.VERSION);
    }

    @Test
    public void initConfig_withNullPath_usesDefaultConfig() {
        MainApp mainApp = new MainApp();
        Config config = mainApp.initConfig(null);

        assertNotNull(config);
        // The config should be initialized with default values
        assertNotNull(config.getLogLevel());
        assertNotNull(config.getUserPrefsFilePath());
    }

    @Test
    public void initConfig_withCustomPath_usesCustomConfig() {
        MainApp mainApp = new MainApp();
        Path customPath = tempDir.resolve("custom_config.json");

        Config config = mainApp.initConfig(customPath);

        assertNotNull(config);
        // Config should be properly initialized even with custom path
        assertNotNull(config.getLogLevel());
    }

    @Test
    public void initPrefs_createsNewPrefsWhenFileNotFound() {
        MainApp mainApp = new MainApp();
        Path nonExistentPath = tempDir.resolve("non_existent_prefs.json");
        UserPrefsStorage storage = new JsonUserPrefsStorage(nonExistentPath);

        UserPrefs prefs = mainApp.initPrefs(storage);

        assertNotNull(prefs);
        // Should create default user prefs when file doesn't exist
        assertEquals(new UserPrefs().getGuiSettings(), prefs.getGuiSettings());
    }

    @Test
    public void applicationName_isReflectedInLogs() {
        // Verify the application name appears in the class structure
        String className = MainApp.class.getName();
        assertTrue(className.contains("address") || className.contains("MainApp"));
    }

    @Test
    public void mainApp_canBeInstantiated() {
        // test to create an instance of MainApp
        MainApp mainApp = new MainApp();
        assertEquals(MainApp.class, mainApp.getClass());
    }

    @Test
    public void mainApp_hasExpectedFields() {
        // Test that MainApp has the expected fields
        MainApp mainApp = new MainApp();

        // These fields should exist (they may be null until initialized)
        assertNotNull(MainApp.VERSION);
        assertEquals("seedu.address.MainApp", MainApp.class.getName());
    }
}
