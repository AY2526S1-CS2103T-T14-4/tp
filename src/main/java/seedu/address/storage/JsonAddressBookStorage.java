package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.ReadOnlyAddressBook;

/**
 * A class to access AddressBook data stored as a json file on the hard disk.
 */
public class JsonAddressBookStorage implements AddressBookStorage {

    private static final Logger logger = LogsCenter.getLogger(JsonAddressBookStorage.class);
    private static final int MAX_PERSONS_LIMIT = 250;

    private Path filePath;

    public JsonAddressBookStorage(Path filePath) {
        this.filePath = filePath;
    }

    public Path getAddressBookFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyAddressBook> readAddressBook() throws DataLoadingException {
        return readAddressBook(filePath);
    }

    /**
     * Similar to {@link #readAddressBook()}.
     *
     * @param filePath location of the data. Cannot be null.
     * @throws DataLoadingException if loading the data from storage failed.
     */
    @Override
    public Optional<ReadOnlyAddressBook> readAddressBook(Path filePath) throws DataLoadingException {
        requireNonNull(filePath);

        Optional<JsonSerializableAddressBook> jsonAddressBook = JsonUtil.readJsonFile(
                filePath, JsonSerializableAddressBook.class);
        if (!jsonAddressBook.isPresent()) {
            return Optional.empty();
        }

        try {
            ReadOnlyAddressBook addressBook = jsonAddressBook.get().toModelType();

            // Log success message with cleanup info
            logger.info("[+] Data loaded! Duplicate entries were automatically removed.");

            // Check if original data exceeded limit and add warning
            JsonSerializableAddressBook jsonBook = jsonAddressBook.get();

            // Log if limit was checked during load
            if (jsonBook.getOriginalPersonCount() > MAX_PERSONS_LIMIT) {
                logger.warning("Original JSON file contained " + jsonBook.getOriginalPersonCount()
                        + " entries. Only first " + MAX_PERSONS_LIMIT + " were loaded.");
            }

            return Optional.of(addressBook);

        } catch (IllegalValueException ive) {
            logger.info("Illegal values found in " + filePath + ": " + ive.getMessage());
            throw new DataLoadingException(ive);
        }
    }

    @Override
    public void saveAddressBook(ReadOnlyAddressBook addressBook) throws IOException {
        saveAddressBook(addressBook, filePath);
    }

    /**
     * Similar to {@link #saveAddressBook(ReadOnlyAddressBook)}.
     *
     * @param filePath location of the data. Cannot be null.
     */
    public void saveAddressBook(ReadOnlyAddressBook addressBook, Path filePath) throws IOException {
        requireNonNull(addressBook);
        requireNonNull(filePath);

        // Check and truncate accordingly
        int personCount = addressBook.getPersonList().size();

        if (personCount > MAX_PERSONS_LIMIT) {
            // Log warning about truncation
            int excessCount = personCount - MAX_PERSONS_LIMIT;
            logger.warning("Address book contains " + personCount + " entries, which exceeds the limit of "
                    + MAX_PERSONS_LIMIT + ". The last " + excessCount + " entries will be removed.");

            // Create a new AddressBook with only the first MAX_PERSONS_LIMIT entries
            seedu.address.model.AddressBook truncatedBook = new seedu.address.model.AddressBook();
            addressBook.getPersonList().stream()
                    .limit(MAX_PERSONS_LIMIT)
                    .forEach(truncatedBook::addPerson);

            FileUtil.createIfMissing(filePath);
            JsonUtil.saveJsonFile(new JsonSerializableAddressBook(truncatedBook), filePath);

            logger.info("Successfully saved " + MAX_PERSONS_LIMIT + " entries. "
                    + excessCount + " excess entries were deleted.");
        } else {
            // Normal save
            FileUtil.createIfMissing(filePath);
            JsonUtil.saveJsonFile(new JsonSerializableAddressBook(addressBook), filePath);
        }
    }

}
