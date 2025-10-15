package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Person;

/**
 * An Immutable AddressBook that is serializable to JSON format.
 */
@JsonRootName(value = "addressbook")
class JsonSerializableAddressBook {

    //public static final String MESSAGE_DUPLICATE_PERSON = "Persons list contains duplicate person(s).";
    public static final String MESSAGE_DUPLICATE_PERSON = "Duplicate entries found based on PHONE + NAME combination. "
            + "Entries will be automatically cleaned up.";

    private static final Logger logger = LogsCenter.getLogger(JsonSerializableAddressBook.class);

    private final List<JsonAdaptedPerson> persons = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableAddressBook} with the given persons.
     */
    @JsonCreator
    public JsonSerializableAddressBook(@JsonProperty("persons") List<JsonAdaptedPerson> persons) {
        this.persons.addAll(persons);
    }

    /**
     * Converts a given {@code ReadOnlyAddressBook} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableAddressBook}.
     */
    public JsonSerializableAddressBook(ReadOnlyAddressBook source) {
        persons.addAll(source.getPersonList().stream().map(JsonAdaptedPerson::new).collect(Collectors.toList()));
    }

    /**
     * Converts this address book into the model's {@code AddressBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public AddressBook toModelType() throws IllegalValueException {
        AddressBook addressBook = new AddressBook();
        int duplicateCount = 0;

        for (JsonAdaptedPerson jsonAdaptedPerson : persons) {
            Person person = jsonAdaptedPerson.toModelType();

            if (hasDuplicatePhoneName(addressBook, person)) {
                duplicateCount++;
                logger.warning("Duplicate entry skipped - Name: " + person.getName()
                        + ", Phone: " + person.getPhone());
                continue; // Skip this duplicate entry
            }

            addressBook.addPerson(person);
        }

        if (duplicateCount > 0) {
            logger.warning(duplicateCount + " duplicate entries were skipped during loading.");
            // Note: We don't throw an exception, we just log and continue
        }

        return addressBook;
    }

    /**
     * Checks if the address book already contains a person with the same PHONE + NAME combination.
     * Other fields (address, email, tags) can be different.
     */
    private boolean hasDuplicatePhoneName(AddressBook addressBook, Person person) {
        return addressBook.getPersonList().stream()
                .anyMatch(existingPerson ->
                        existingPerson.getPhone().equals(person.getPhone())
                                && existingPerson.getName().fullName.equalsIgnoreCase(person.getName().fullName));
    }

    //    public AddressBook toModelType() throws IllegalValueException {
    //        AddressBook addressBook = new AddressBook();
    //        for (JsonAdaptedPerson jsonAdaptedPerson : persons) {
    //            Person person = jsonAdaptedPerson.toModelType();
    //            if (addressBook.hasPerson(person)) {
    //                throw new IllegalValueException(MESSAGE_DUPLICATE_PERSON);
    //            }
    //            addressBook.addPerson(person);
    //        }
    //        return addressBook;
    //    }

}
