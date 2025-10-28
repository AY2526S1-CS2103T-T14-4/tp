package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Remark;
import seedu.address.model.tag.Tag;

/**
 * Deletes a senior identified using it's displayed index from the address book.
 */
public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the senior identified by the index number, or the name and phone number "
            + "used in the displayed senior list.\n"
            + "Parameters: i/INDEX (must be a positive integer) OR n/NAME p/PHONE\n"
            + "Examples:\n\t" + COMMAND_WORD + " i/1\n\t" + COMMAND_WORD + " n/Alice p/12345678";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Senior: %1$s";

    private final Index targetIndex;
    private final Phone targetPhone;
    private final Name targetName;

    /**
     * Creates a DeleteCommand to delete the person at the specified
     * {@code targetIndex}
     */
    public DeleteCommand(Index targetIndex) {
        requireNonNull(targetIndex);
        this.targetIndex = targetIndex;
        this.targetPhone = null;
        this.targetName = null;
    }

    /**
     * Creates a DeleteCommand to delete the person with the specified
     * {@code targetName} and {@code targetPhone}
     */
    public DeleteCommand(Name targetName, Phone targetPhone) {
        requireNonNull(targetName);
        requireNonNull(targetPhone);
        this.targetIndex = null;
        this.targetName = targetName;
        this.targetPhone = targetPhone;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex == null && (targetName == null || targetPhone == null)) {
            throw new CommandException(String.format(MESSAGE_USAGE));
        }

        if (targetIndex != null) {
            return deleteByIndex(model, lastShownList);
        }

        Person personToDelete = getPersonByPhoneAndName(lastShownList);
        if (personToDelete != null) {
            return deleteByPhoneAndName(model, personToDelete);
        }

        throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_NAME_PHONE);
    }

    private CommandResult deleteByPhoneAndName(Model model, Person personToDelete) {
        model.deletePerson(personToDelete);
        return new CommandResult(
                String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(personToDelete)));
    }

    private Person getPersonByPhoneAndName(List<Person> lastShownList) {
        Person partialPerson = new Person(targetName, targetPhone, new Email(), new Address("address"),
                new Remark(""), new HashSet<Tag>());
        Person personToDelete = lastShownList.stream()
                .filter(person -> person.isSamePerson(partialPerson))
                .findFirst()
                .orElse(null);
        return personToDelete;
    }

    private CommandResult deleteByIndex(Model model, List<Person> lastShownList) throws CommandException {
        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToDelete = lastShownList.get(targetIndex.getZeroBased());
        return deleteByPhoneAndName(model, personToDelete);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        DeleteCommand otherDeleteCommand = (DeleteCommand) other;
        if (targetIndex == null) {
            return otherDeleteCommand.targetIndex == null
                    && targetName.equals(otherDeleteCommand.targetName)
                    && targetPhone.equals(otherDeleteCommand.targetPhone);
        }
        return targetIndex.equals(otherDeleteCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
