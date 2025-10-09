package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.UserPrefs;

/**
 * Manages storage of AddressBook data in both JSON and text formats.
 */
public class DualStorageManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(DualStorageManager.class);
    private final JsonAddressBookStorage jsonStorage;
    private final TextAddressBookStorage textStorage;
    private final UserPrefsStorage userPrefsStorage;

    /**
     * Creates a {@code DualStorageManager} with both JSON and text storage.
     */
    public DualStorageManager(Path jsonFilePath, Path textFilePath, UserPrefsStorage userPrefsStorage) {
        this.jsonStorage = new JsonAddressBookStorage(jsonFilePath);
        this.textStorage = new TextAddressBookStorage(textFilePath);
        this.userPrefsStorage = userPrefsStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataLoadingException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }

    // ================ AddressBook methods ==============================

    @Override
    public Path getAddressBookFilePath() {
        // Primary storage is JSON for full feature support
        return jsonStorage.getAddressBookFilePath();
    }

    @Override
    public Optional<ReadOnlyAddressBook> readAddressBook() throws DataLoadingException {
        // Try to read from JSON first, fall back to text if JSON doesn't exist
        Optional<ReadOnlyAddressBook> jsonAddressBook = jsonStorage.readAddressBook();
        if (jsonAddressBook.isPresent()) {
            // If JSON exists, also save to text format to keep them synchronized
            try {
                textStorage.saveAddressBook(jsonAddressBook.get());
            } catch (IOException e) {
                logger.warning("Failed to synchronize JSON data to text format: " + e.getMessage());
            }
            return jsonAddressBook;
        }

        // If JSON doesn't exist, try reading from text
        Optional<ReadOnlyAddressBook> textAddressBook = textStorage.readAddressBook();
        if (textAddressBook.isPresent()) {
            // If text exists, save to JSON format to keep them synchronized
            try {
                jsonStorage.saveAddressBook(textAddressBook.get());
            } catch (IOException e) {
                logger.warning("Failed to synchronize text data to JSON format: " + e.getMessage());
            }
            return textAddressBook;
        }

        return Optional.empty();
    }

    @Override
    public Optional<ReadOnlyAddressBook> readAddressBook(Path filePath) throws DataLoadingException {
        // For specific file path reading, use JSON storage
        return jsonStorage.readAddressBook(filePath);
    }

    @Override
    public void saveAddressBook(ReadOnlyAddressBook addressBook) throws IOException {
        // Save to both formats
        jsonStorage.saveAddressBook(addressBook);
        textStorage.saveAddressBook(addressBook);
        logger.info("Data saved to both JSON and text formats");
    }

    @Override
    public void saveAddressBook(ReadOnlyAddressBook addressBook, Path filePath) throws IOException {
        // Save to specific file path in JSON, and also update text format
        jsonStorage.saveAddressBook(addressBook, filePath);
        textStorage.saveAddressBook(addressBook);
    }
}
