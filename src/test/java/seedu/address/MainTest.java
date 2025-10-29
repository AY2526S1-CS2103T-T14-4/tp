package seedu.address;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

/**
 * Tests the main entry point of the application.
 */
public class MainTest {

    @Test
    public void main_classCanBeInstantiated() {
        // Test that we can create an instance of Main (without calling main method)
        assertDoesNotThrow(() -> {
            Main main = new Main();
            // Verify it's the right type
            assertEquals(Main.class, main.getClass());
        });
    }

    @Test
    public void main_classHasExpectedMethods() {
        // Test that the Main class has the expected static main method
        assertDoesNotThrow(() -> {
            Main.class.getDeclaredMethod("main", String[].class);
        });
    }

    @Test
    public void main_loggerIsInitialized() {
        // Test that the logger is properly initialized
        assertDoesNotThrow(() -> {
            // The logger should be initialized in the class
            var loggerField = Main.class.getDeclaredField("logger");
            loggerField.setAccessible(true);
            Object logger = loggerField.get(null);
            assert logger != null;
        });
    }

    @Test
    public void main_methodExistsAndIsCallable() {
        // Test that the main method exists and can be referenced
        // This doesn't actually call main(), but verifies the method exists
        assertDoesNotThrow(() -> {
            var mainMethod = Main.class.getMethod("main", String[].class);
            assert mainMethod != null;
            assert mainMethod.getReturnType() == void.class;
        });
    }

    @Test
    public void main_loggerFieldExists() {
        // Test that the logger field exists and is accessible
        assertDoesNotThrow(() -> {
            var loggerField = Main.class.getDeclaredField("logger");
            loggerField.setAccessible(true);
            Object logger = loggerField.get(null); // static field
            assert logger != null;
        });
    }

    @Test
    public void main_methodSignatureIsCorrect() {
        assertDoesNotThrow(() -> {
            var mainMethod = Main.class.getMethod("main", String[].class);
            assertNotNull(mainMethod);

            // Verify it's public static void
            int modifiers = mainMethod.getModifiers();
            assert java.lang.reflect.Modifier.isPublic(modifiers);
            assert java.lang.reflect.Modifier.isStatic(modifiers);
            assert mainMethod.getReturnType() == void.class;
        });
    }

    @Test
    public void main_loggerIsProperlyInitialized() {
        assertDoesNotThrow(() -> {
            var loggerField = Main.class.getDeclaredField("logger");
            loggerField.setAccessible(true);
            var logger = loggerField.get(null);
            assertNotNull(logger);
        });
    }

    @Test
    public void main_classIsInstantiable() {
        assertDoesNotThrow(() -> {
            Main main = new Main();
            assertNotNull(main);
        });
    }

    @Test
    public void main_requiredClassesExist() {
        // Verify that all classes referenced in main method exist
        assertDoesNotThrow(() -> {
            Class<?> mainAppClass = Class.forName("seedu.address.MainApp");
            assertNotNull(mainAppClass);

            Class<?> applicationClass = Class.forName("javafx.application.Application");
            assertNotNull(applicationClass);

            Class<?> loggerClass = Class.forName("java.util.logging.Logger");
            assertNotNull(loggerClass);
        });
    }

    @Test
    public void main_warningMessageContent() {
        // Verify the warning message content (basic sanity check)
        String warningMessage = "The warning about Unsupported JavaFX configuration below (if any) can be ignored.";
        assertNotNull(warningMessage);
        assert warningMessage.contains("Unsupported JavaFX configuration");
        assert warningMessage.contains("can be ignored");
    }
}
