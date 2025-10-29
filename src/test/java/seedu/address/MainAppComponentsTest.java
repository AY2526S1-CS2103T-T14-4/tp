package seedu.address;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.logic.LogicManager;
import seedu.address.model.AddressBook;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.StorageManager;
import seedu.address.ui.UiManager;

/**
 * Tests individual components of the init method.
 */
public class MainAppComponentsTest {

    @TempDir
    public Path tempDir;

    @Test
    public void appParameters_canBeCreated() {
        assertDoesNotThrow(() -> {
            // Test that AppParameters can be instantiated and used
            // Since we can't easily create JavaFX Parameters in tests,
            // we'll test that the class exists and has the expected methods
            var appParametersClass = AppParameters.class;
            assertNotNull(appParametersClass);

            // Verify it has the parse method
            var parseMethod = appParametersClass.getMethod("parse", javafx.application.Application.Parameters.class);
            assertNotNull(parseMethod);
        });
    }

    @Test
    public void storageManager_canBeCreated() {
        assertDoesNotThrow(() -> {
            var addressBookStorage = new JsonAddressBookStorage(tempDir.resolve("addressbook.json"));
            var userPrefsStorage = new JsonUserPrefsStorage(tempDir.resolve("prefs.json"));

            var storage = new StorageManager(addressBookStorage, userPrefsStorage);
            assertNotNull(storage);
            assertNotNull(storage.getAddressBookFilePath());
            assertNotNull(storage.getUserPrefsFilePath());
        });
    }

    @Test
    public void logicManager_canBeCreated() {
        assertDoesNotThrow(() -> {
            var model = new ModelManager(new AddressBook(), new UserPrefs());
            var storage = new StorageManager(
                    new JsonAddressBookStorage(tempDir.resolve("addressbook.json")),
                    new JsonUserPrefsStorage(tempDir.resolve("prefs.json"))
            );

            var logic = new LogicManager(model, storage);
            assertNotNull(logic);
            assertNotNull(logic.getAddressBook());
        });
    }

    @Test
    public void uiManager_canBeCreated() {
        assertDoesNotThrow(() -> {
            var model = new ModelManager(new AddressBook(), new UserPrefs());
            var storage = new StorageManager(
                    new JsonAddressBookStorage(tempDir.resolve("addressbook.json")),
                    new JsonUserPrefsStorage(tempDir.resolve("prefs.json"))
            );
            var logic = new LogicManager(model, storage);

            var ui = new UiManager(logic);
            assertNotNull(ui);
        });
    }

    @Test
    public void initConfig_withVariousPaths_works() {
        MainApp mainApp = new MainApp();

        assertDoesNotThrow(() -> {
            // Test with null path (should use default)
            var config1 = mainApp.initConfig(null);
            assertNotNull(config1);

            // Test with custom path
            var config2 = mainApp.initConfig(tempDir.resolve("custom_config.json"));
            assertNotNull(config2);
        });
    }

    @Test
    public void initPrefs_withVariousStorage_works() {
        MainApp mainApp = new MainApp();

        assertDoesNotThrow(() -> {
            // Test with non-existent file (should create defaults)
            var storage1 = new JsonUserPrefsStorage(tempDir.resolve("non_existent_prefs.json"));
            var prefs1 = mainApp.initPrefs(storage1);
            assertNotNull(prefs1);

            // Test with existing file path
            var storage2 = new JsonUserPrefsStorage(tempDir.resolve("existing_prefs.json"));
            var prefs2 = mainApp.initPrefs(storage2);
            assertNotNull(prefs2);
        });
    }
}
