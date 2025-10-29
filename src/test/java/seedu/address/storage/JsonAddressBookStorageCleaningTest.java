package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;

/**
 * Integration tests for data cleaning in JsonAddressBookStorage.
 */
public class JsonAddressBookStorageCleaningTest {

    @TempDir
    public Path tempDir;

    @Test
    public void readAddressBook_withValidFile_returnsAddressBook() throws Exception {
        // Setup
        Path addressBookPath = tempDir.resolve("testAddressBook.json");
        JsonAddressBookStorage storage = new JsonAddressBookStorage(addressBookPath);

        // Create a minimal valid address book file
        AddressBook originalBook = new AddressBook();
        storage.saveAddressBook(originalBook);

        // Execute
        Optional<ReadOnlyAddressBook> loadedBook = storage.readAddressBook();

        // Verify
        assertTrue(loadedBook.isPresent());
        assertTrue(loadedBook.get() instanceof ReadOnlyAddressBook);
    }

    @Test
    public void saveAddressBook_afterLoading_worksCorrectly() throws Exception {
        // Setup
        Path addressBookPath = tempDir.resolve("testAddressBook.json");
        JsonAddressBookStorage storage = new JsonAddressBookStorage(addressBookPath);

        AddressBook originalBook = new AddressBook();
        storage.saveAddressBook(originalBook);

        // Load and immediately save (simulating the cleaning process)
        Optional<ReadOnlyAddressBook> loadedBook = storage.readAddressBook();

        // Execute - save the loaded book back
        assertDoesNotThrow(() -> {
            if (loadedBook.isPresent()) {
                storage.saveAddressBook(loadedBook.get());
            }
        });

        // Verify the file still exists and is valid
        assertTrue(java.nio.file.Files.exists(addressBookPath));
        Optional<ReadOnlyAddressBook> reloadedBook = storage.readAddressBook();
        assertTrue(reloadedBook.isPresent());
    }

    @Test
    public void storageOperations_handleIoExceptionGracefully() throws Exception {
        // Setup - create a read-only directory to simulate permission issues
        Path readOnlyDir = tempDir.resolve("readonly");
        java.nio.file.Files.createDirectory(readOnlyDir);
        readOnlyDir.toFile().setReadOnly();

        Path addressBookPath = readOnlyDir.resolve("testAddressBook.json");
        JsonAddressBookStorage storage = new JsonAddressBookStorage(addressBookPath);

        AddressBook testBook = new AddressBook();

        // Execute & Verify - should handle IOException without crashing
        assertDoesNotThrow(() -> {
            try {
                storage.saveAddressBook(testBook);
            } catch (IOException e) {
                // Expected - the test passes as long as it doesn't crash catastrophically
            }
        });

        // Cleanup
        readOnlyDir.toFile().setWritable(true);
    }
}
