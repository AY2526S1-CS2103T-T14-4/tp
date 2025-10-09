package seedu.address.storage;

import static java.util.Objects.requireNonNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.DataLoadingException;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.FileUtil;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * A class to access AddressBook data stored as a text file on the hard disk.
 */
public class TextAddressBookStorage implements AddressBookStorage {

    private static final Logger logger = LogsCenter.getLogger(TextAddressBookStorage.class);
    private static final String FIELD_SEPARATOR = "\\|";
    private static final String LINE_SEPARATOR = "|";
    private static final String TAG_SEPARATOR = ",";

    // Error messages as specified in requirements
    private static final String SUCCESS_MESSAGE = "[+] Data loaded!";
    private static final String ERROR_INVALID_DATA = "[-] Error! Ensure that the data file is valid.";
    private static final String ERROR_ACCESS_DENIED = "[-] Unable to access data file. Ensure read and write "
            + "permissions are granted.";
    private static final String ERROR_DUPLICATE_PERSON = "[-] Error! Duplicate entries found in data file.";

    private Path filePath;

    public TextAddressBookStorage(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public Path getAddressBookFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyAddressBook> readAddressBook() throws DataLoadingException {
        return readAddressBook(filePath);
    }

    @Override
    public Optional<ReadOnlyAddressBook> readAddressBook(Path filePath) throws DataLoadingException {
        requireNonNull(filePath);

        if (!Files.exists(filePath)) {
            logger.info("Text data file not found: " + filePath);
            return Optional.empty();
        }

        List<Person> persons = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();

                // Skip empty lines
                if (line.isEmpty()) {
                    continue;
                }

                try {
                    Person person = parsePerson(line, lineNumber);
                    if (person != null) {
                        persons.add(person);
                    }
                } catch (IllegalValueException e) {
                    logger.warning("Invalid data format in " + filePath + " at line " + lineNumber + ": "
                            + e.getMessage());
                    logger.severe(ERROR_INVALID_DATA);
                    throw new DataLoadingException(new IllegalValueException(ERROR_INVALID_DATA));
                }
            }

            // Check for duplicates as specified in requirements
            if (hasDuplicates(persons)) {
                logger.severe(ERROR_DUPLICATE_PERSON);
                throw new DataLoadingException(new IllegalValueException(ERROR_DUPLICATE_PERSON));
            }

            AddressBook addressBook = new AddressBook();
            for (Person person : persons) {
                addressBook.addPerson(person);
            }

            logger.info(SUCCESS_MESSAGE);
            return Optional.of(addressBook);

        } catch (IOException e) {
            logger.warning("Error reading from text file: " + filePath);
            logger.severe(ERROR_ACCESS_DENIED);
            throw new DataLoadingException(new IOException(ERROR_ACCESS_DENIED));
        }
    }

    /**
     * Parses a line from the text file into a Person object.
     * Format: NAME|PHONE|ADDRESS|EMAIL_ADDRESS|TAG1,TAG2,TAG3
     */
    private Person parsePerson(String line, int lineNumber) throws IllegalValueException {
        String[] fields = line.split(FIELD_SEPARATOR, -1); // -1 to include trailing empty fields

        // Now expect 5 fields (4 required + 1 optional for tags)
        if (fields.length < 4 || fields.length > 5) {
            throw new IllegalValueException("Expected 4-5 fields but found " + fields.length + " in line: " + line);
        }

        String name = fields[0].trim();
        String phone = fields[1].trim();
        String address = fields[2].trim();
        String email = fields[3].trim();
        String tagsField = fields.length > 4 ? fields[4].trim() : "";

        // Validate required fields
        if (name.isEmpty()) {
            throw new IllegalValueException("Name field is missing at line " + lineNumber);
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException("Invalid name format at line " + lineNumber);
        }

        if (phone.isEmpty()) {
            throw new IllegalValueException("Phone field is missing at line " + lineNumber);
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException("Invalid phone format at line " + lineNumber);
        }

        if (address.isEmpty()) {
            throw new IllegalValueException("Address field is missing at line " + lineNumber);
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException("Invalid address format at line " + lineNumber);
        }

        if (email.isEmpty()) {
            throw new IllegalValueException("Email field is missing at line " + lineNumber);
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException("Invalid email format at line " + lineNumber);
        }

        // Parse tags
        Set<Tag> tags = parseTags(tagsField, lineNumber);

        return new Person(
                new Name(name),
                new Phone(phone),
                new Email(email),
                new Address(address),
                tags
        );
    }

    /**
     * Parses tags from a comma-separated string.
     * Format: "tag1,tag2,tag3" -> Set of Tag objects
     */
    private Set<Tag> parseTags(String tagsField, int lineNumber) throws IllegalValueException {
        Set<Tag> tags = new HashSet<>();

        if (tagsField == null || tagsField.isEmpty()) {
            return tags; // Return empty set if no tags
        }

        // Split by comma and process each tag
        String[] tagNames = tagsField.split(",");

        for (String tagName : tagNames) {
            String trimmedTagName = tagName.trim();
            if (!trimmedTagName.isEmpty()) {
                if (!Tag.isValidTagName(trimmedTagName)) {
                    throw new IllegalValueException("Invalid tag name '" + trimmedTagName + "' at line " + lineNumber);
                }
                tags.add(new Tag(trimmedTagName));
            }
        }

        return tags;
    }

    /**
     * Checks if there are duplicate persons in the list based on PHONE + NAME combination.
     * As specified in requirements: "If there are duplicate entries in the data file during import,
     * abandon the import operation and throw an error."
     * <p>
     * Definition of duplicate: Same PHONE + NAME combination
     * Rationale: PHONE + NAME is used as the unique identifier.
     */
    private boolean hasDuplicates(List<Person> persons) {
        for (int i = 0; i < persons.size(); i++) {
            for (int j = i + 1; j < persons.size(); j++) {
                Person person1 = persons.get(i);
                Person person2 = persons.get(j);

                // Check if both PHONE and NAME are the same (case-insensitive for name)
                boolean samePhone = person1.getPhone().value.equals(person2.getPhone().value);
                boolean sameName = person1.getName().fullName.equalsIgnoreCase(person2.getName().fullName);

                if (samePhone && sameName) {
                    logger.warning("Duplicate found (PHONE + NAME): "
                            + person1.getName() + " (" + person1.getPhone() + ") and "
                            + person2.getName() + " (" + person2.getPhone() + ")");
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void saveAddressBook(ReadOnlyAddressBook addressBook) throws IOException {
        saveAddressBook(addressBook, filePath);
    }

    @Override
    public void saveAddressBook(ReadOnlyAddressBook addressBook, Path filePath) throws IOException {
        requireNonNull(addressBook);
        requireNonNull(filePath);

        FileUtil.createIfMissing(filePath);

        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            for (Person person : addressBook.getPersonList()) {
                String line = formatPerson(person);
                writer.write(line);
                writer.newLine();
            }
        }

        logger.fine("Successfully saved data to: " + filePath);
    }

    /**
     * Formats a Person object into the text file format.
     * Format: NAME|PHONE|ADDRESS|EMAIL_ADDRESS|TAG1,TAG2,TAG3
     * If no tags, the tags field will be empty.
     */
    private String formatPerson(Person person) {
        StringBuilder line = new StringBuilder();

        // Add required fields
        line.append(person.getName().fullName)
                .append(LINE_SEPARATOR)
                .append(person.getPhone().value)
                .append(LINE_SEPARATOR)
                .append(person.getAddress().value)
                .append(LINE_SEPARATOR)
                .append(person.getEmail().value);

        // Add tags if present
        Set<Tag> tags = person.getTags();
        if (!tags.isEmpty()) {
            String tagsString = tags.stream()
                    .map(tag -> tag.tagName)
                    .collect(Collectors.joining(","));
            line.append(LINE_SEPARATOR).append(tagsString);
        }

        return line.toString();
    }
}
