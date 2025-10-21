package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Address;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Tags a person in the address book identified by index or by entry details
 */
public class TagCommand extends Command {

    public static final String COMMAND_WORD = "tag";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Tags the specified person in the address book.\n"
            + "Parameters: "
            + "i/INDEX (must be a positive integer) "
            + "or n/NAME p/PHONE "
            + "or a/ADDRESS "
            + "and t/TAGNAME (alphanumeric only)\n"
            + "Examples:\n\t"
            + COMMAND_WORD + " i/1 t/friends\n\t"
            + COMMAND_WORD + " n/Alice p/12345678 t/vip\n\t"
            + COMMAND_WORD + " a/123 Clementi Ave t/home";

    public static final String MESSAGE_TAG_PERSON_SUCCESS = "Tagged Person: %1$s with %2$s";
    public static final String MESSAGE_INVALID_IDENTIFIER = "No such entry exists.";
    //public static final String MESSAGE_INVALID_TAG = "Tag name must be alphanumeric.";

    private final Index targetIndex;
    private final Name targetName;
    private final Phone targetPhone;
    private final Address targetAddress;
    private final Tag tag;

    /**
     * Creates a TagCommand to tag the target entry based on index
     */
    public TagCommand(Index targetIndex, String tagName) {
        requireNonNull(targetIndex);
        requireNonNull(tagName);

        this.targetIndex = targetIndex;
        this.targetName = null;
        this.targetPhone = null;
        this.targetAddress = null;
        this.tag = new Tag(tagName);
    }

    /**
     * Creates a TagCommand to tag the person with the specified targetName and targetPhone
     */
    public TagCommand(Name targetName, Phone targetPhone, String tagName) {
        requireNonNull(targetName);
        requireNonNull(targetPhone);
        requireNonNull(tagName);

        this.targetIndex = null;
        this.targetName = targetName;
        this.targetPhone = targetPhone;
        this.targetAddress = null;
        this.tag = new Tag(tagName);
    }

    /**
     * Creates a TagCommand to tag the person with the specified targetName, targetPhone and targetAddress.
     */
    public TagCommand(Name targetName, Phone targetPhone, Address targetAddress, String tagName) {
        requireNonNull(targetName);
        requireNonNull(targetPhone);
        requireNonNull(targetAddress);
        requireNonNull(tagName);

        this.targetIndex = null;
        this.targetName = targetName;
        this.targetPhone = targetPhone;
        this.targetAddress = targetAddress;
        this.tag = new Tag(tagName);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();
        Person personToTag = null;

        if (targetIndex != null) {
            if (targetIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
            personToTag = lastShownList.get(targetIndex.getZeroBased());
        } else if (targetName != null && targetPhone != null) {
            Optional<Person> match = lastShownList.stream()
                    .filter(p -> p.getName().fullName.equalsIgnoreCase(targetName.fullName)
                            && p.getPhone().equals(targetPhone))
                    .findFirst();
            personToTag = match.orElse(null);
        } else if (targetAddress != null) {
            Optional<Person> match = lastShownList.stream()
                    .filter(p -> p.getAddress().value.equalsIgnoreCase(targetAddress.value))
                    .findFirst();
            personToTag = match.orElse(null);
        }

        if (personToTag == null) {
            throw new CommandException(MESSAGE_INVALID_IDENTIFIER);
        }

        if (personToTag.getTags().contains(tag)) {
            throw new CommandException("The person already has tag " + tag);
        }

        Person taggedPerson = personToTag.addTag(tag);
        model.setPerson(personToTag, taggedPerson);

        return new CommandResult(String.format(
                MESSAGE_TAG_PERSON_SUCCESS,
                Messages.format(taggedPerson),
                tag.toString()
        ));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof TagCommand)) {
            return false;
        }

        TagCommand otherCommand = (TagCommand) other;
        return (targetIndex != null && targetIndex.equals(otherCommand.targetIndex))
                || (targetName != null && targetName.equals(otherCommand.targetName)
                && targetPhone != null && targetPhone.equals(otherCommand.targetPhone))
                || (targetAddress != null && targetAddress.equals(otherCommand.targetAddress));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("targetName", targetName)
                .add("targetPhone", targetPhone)
                .add("targetAddress", targetAddress)
                .add("tag", tag)
                .toString();
    }
}
