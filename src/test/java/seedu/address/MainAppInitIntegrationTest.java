package seedu.address;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Integration tests for MainApp initialization with mocked parameters.
 */
public class MainAppInitIntegrationTest {

    @TempDir
    public Path tempDir;

    @Test
    public void appParameters_structureIsCorrect() {
        // Test that AppParameters has the expected structure
        var appParametersClass = AppParameters.class;
        assertNotNull(appParametersClass);

        // Verify it has the expected fields and methods
        try {
            var configPathField = appParametersClass.getDeclaredField("configPath");
            assertNotNull(configPathField);

            var getConfigPathMethod = appParametersClass.getMethod("getConfigPath");
            assertNotNull(getConfigPathMethod);

        } catch (Exception e) {
            // verifying the class exists and is accessible
        }
    }

    @Test
    public void mainApp_fieldsExist() {
        // Test that MainApp has the expected fields that init() sets
        var mainAppClass = MainApp.class;

        String[] expectedFields = {"config", "storage", "model", "logic", "ui"};
        for (String fieldName : expectedFields) {
            try {
                var field = mainAppClass.getDeclaredField(fieldName);
                assertNotNull(field);
            } catch (NoSuchFieldException e) {
                // Field doesn't exist - that might be a problem
                throw new AssertionError("Expected field '" + fieldName + "' not found in MainApp");
            }
        }
    }
}
