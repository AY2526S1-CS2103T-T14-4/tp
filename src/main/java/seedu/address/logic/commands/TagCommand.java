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
            + "and t/TAGNAME \n"
            + "Examples:\n\t"
            + COMMAND_WORD + " i/1 t/friends\n\t"
            + COMMAND_WORD + " n/Alice p/12345678 t/vip\n\t";

    public static final String MESSAGE_TAG_PERSON_SUCCESS = "Tagged Person: %1$s with %2$s";
    public static final String MESSAGE_UNTAG_PERSON_SUCCESS = "Removed tag %2$s from Person: %1$s";
    public static final String MESSAGE_INVALID_IDENTIFIER = "Person not found";
    public static final String MESSAGE_TAG_NOT_FOUND = "Person does not have tag %s.";

    private final Index targetIndex;
    private final Name targetName;
    private final Phone targetPhone;
    //private final Address targetAddress;
    private final Tag tag;
    private final boolean isDelete;

    /**
     * Creates a TagCommand to tag the target entry based on index
     */
    public TagCommand(Index targetIndex, String tagName, boolean isDelete) {
        requireNonNull(targetIndex);
        requireNonNull(tagName);

        this.targetIndex = targetIndex;
        this.targetName = null;
        this.targetPhone = null;
        //this.targetAddress = null;
        this.tag = new Tag(tagName);
        //this.tag = new Tag(isDelete ? tagName.substring(0, tagName.lastIndexOf("--remove")).trim() : tagName);
        this.isDelete = isDelete;
    }

    /**
     * Creates a TagCommand to tag the person with the specified targetName and targetPhone
     */
    public TagCommand(Name targetName, Phone targetPhone, String tagName, boolean isDelete) {
        requireNonNull(targetName);
        requireNonNull(targetPhone);
        requireNonNull(tagName);

        this.targetIndex = null;
        this.targetName = targetName;
        this.targetPhone = targetPhone;
        //this.targetAddress = null;
        this.tag = new Tag(tagName);
        //this.tag = new Tag(isDelete ? tagName.substring(0, tagName.lastIndexOf("--remove")).trim() : tagName);
        this.isDelete = isDelete;
    }

//    /**
//     * Creates a TagCommand to tag the person with the specified targetName, targetPhone and targetAddress.
//     */
//    public TagCommand(Name targetName, Phone targetPhone, Address targetAddress, String tagName, boolean isDelete) {
//        requireNonNull(targetName);
//        requireNonNull(targetPhone);
//        requireNonNull(targetAddress);
//        requireNonNull(tagName);
//
//        this.targetIndex = null;
//        this.targetName = targetName;
//        this.targetPhone = targetPhone;
//        this.targetAddress = targetAddress;
//        this.tag = new Tag(tagName);
//        //this.tag = new Tag(isDelete ? tagName.substring(0, tagName.lastIndexOf("--remove")).trim() : tagName);
//        this.isDelete = isDelete;
//    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();
        Person targetPerson = null;

        if (targetIndex != null) {
            if (targetIndex.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
            }
            targetPerson = lastShownList.get(targetIndex.getZeroBased());
        } else if (targetName != null && targetPhone != null) {
            Optional<Person> match = lastShownList.stream()
                    .filter(p -> p.getName().fullName.equalsIgnoreCase(targetName.fullName)
                            && p.getPhone().equals(targetPhone))
                    .findFirst();
            targetPerson = match.orElse(null);
//        } else if (targetAddress != null) {
//            Optional<Person> match = lastShownList.stream()
//                    .filter(p -> p.getAddress().value.equalsIgnoreCase(targetAddress.value))
//                    .findFirst();
//            targetPerson = match.orElse(null);
        }

        if (targetPerson == null) {
            throw new CommandException(MESSAGE_INVALID_IDENTIFIER);
        }

        Person outputPerson;

        if (isDelete) {
            if (!targetPerson.getTags().contains(tag)) {
                throw new CommandException(String.format(MESSAGE_TAG_NOT_FOUND, tag.toString()));
            }
            outputPerson = targetPerson.removeTag(tag);
            model.setPerson(targetPerson, outputPerson);
            return new CommandResult(String.format(MESSAGE_UNTAG_PERSON_SUCCESS,
                    Messages.format(outputPerson), tag.toString()));
        } else {
            if (targetPerson.getTags().contains(tag)) {
                throw new CommandException("The person already has tag " + tag);
            }
            outputPerson = targetPerson.addTag(tag);
            model.setPerson(targetPerson, outputPerson);
            return new CommandResult(String.format(MESSAGE_TAG_PERSON_SUCCESS,
                    Messages.format(outputPerson), tag.toString()));
        }
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
        return isDelete == otherCommand.isDelete
                && ((targetIndex != null && targetIndex.equals(otherCommand.targetIndex))
                || (targetName != null && targetName.equals(otherCommand.targetName)
                && targetPhone != null && targetPhone.equals(otherCommand.targetPhone)));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("targetName", targetName)
                .add("targetPhone", targetPhone)
                //.add("targetAddress", targetAddress)
                .add("tag", tag)
                .add("isDelete", isDelete)
                .toString();
    }
}
