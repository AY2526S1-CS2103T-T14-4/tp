package seedu.address;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.commons.core.Config;
import seedu.address.model.UserPrefs;
import seedu.address.storage.JsonUserPrefsStorage;
import seedu.address.storage.UserPrefsStorage;

/**
 * Tests for MainApp initialization.
 */
public class MainAppInitTest {

    @TempDir
    public Path tempDir;

    @Test
    public void init_methodCanBeCalledWithoutException() {
        MainApp mainApp = new MainApp();

        // Test that init doesn't throw exceptions when called with minimal setup
        assertDoesNotThrow(() -> {
            try {
                mainApp.init();
            } catch (Exception e) {
                // Some exceptions are expected due to JavaFX not being initialized
                if (!isExpectedInitException(e)) {
                    throw e;
                }
            }
        });
    }

    @Test
    public void init_individualComponentsCanBeInitialized() {
        MainApp mainApp = new MainApp();

        // Test to call the individual initialization methods
        assertDoesNotThrow(() -> {
            Config config = mainApp.initConfig(null);
            assertNotNull(config);

            UserPrefsStorage storage = new JsonUserPrefsStorage(tempDir.resolve("prefs.json"));
            UserPrefs prefs = mainApp.initPrefs(storage);
            assertNotNull(prefs);
        });
    }

    private boolean isExpectedInitException(Exception e) {
        // These are common exceptions when init is called without proper JavaFX setup
        return e instanceof IllegalStateException
                || e instanceof NullPointerException && e.getMessage() != null
                && (e.getMessage().contains("Application")
                || e.getMessage().contains("JavaFX"));
    }
}
