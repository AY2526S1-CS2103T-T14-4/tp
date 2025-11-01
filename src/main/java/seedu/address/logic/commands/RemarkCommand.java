package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.Remark;

/**
 * Changes the remark of an existing senior in the address book.
 */
public class RemarkCommand extends Command {

    public static final String COMMAND_WORD = "remark";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits, appends to, or removes the remark of the senior identified by the index.\n"
            + "Parameters: i/INDEX (must be a positive integer) "
            + "[ r/REMARK | ap/APPEND_TEXT | --remove ]\n"
            + "Examples:\n"
            + "  " + COMMAND_WORD + " i/1 r/Likes to swim.\n"
            + "  " + COMMAND_WORD + " i/1 ap/Has a cat.\n"
            + "  " + COMMAND_WORD + " i/1 --remove";

    public static final String MESSAGE_ADD_REMARK_SUCCESS = "Added remark to Senior: %1$s";
    public static final String MESSAGE_DELETE_REMARK_SUCCESS = "Removed remark from Senior: %1$s";
    public static final String MESSAGE_APPEND_REMARK_SUCCESS = "Appended to remark for Senior: %1$s";
    public static final String MESSAGE_NO_REMARK_TO_REMOVE = "No remark to remove for Senior: %1$s";
    public static final String MESSAGE_EXCLUSIVE_ACTIONS =
            "Specify exactly one of r/REMARK, ap/APPEND_TEXT, or --remove.";

    private final Index index;
    private final Remark remark;
    private final boolean isAppend;

    /**
     * Creates a RemarkCommand to update the remark of the specified senior.
     * @param index The index of the senior in the filtered senior list to modify
     * @param remark The new remark for the senior
     */
    public RemarkCommand(Index index, Remark remark, boolean isAppend) {
        requireAllNonNull(index, remark);
        this.index = index;
        this.remark = remark;
        this.isAppend = isAppend;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof RemarkCommand)) {
            return false;
        }
        RemarkCommand e = (RemarkCommand) other;
        return index.equals(e.index)
            && remark.equals(e.remark)
            && isAppend == e.isAppend;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Person> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToEdit = lastShownList.get(index.getZeroBased());

        Remark newRemark;
        if (isAppend) {
            String currentRemark = personToEdit.getRemark().value;
            String appendedRemark = currentRemark.isEmpty() ? remark.value
                : currentRemark + remark.value;
            try {
                newRemark = new Remark(appendedRemark);
            } catch (IllegalArgumentException ex) {
                throw new CommandException(Remark.MESSAGE_CONSTRAINTS, ex);
            }
        } else {
            // Replacement/remove path already validated at parse time, but be defensive
            try {
                newRemark = new Remark(remark.value);
            } catch (IllegalArgumentException ex) {
                throw new CommandException(Remark.MESSAGE_CONSTRAINTS, ex);
            }
        }

        Person editedPerson = new Person(
                personToEdit.getName(), personToEdit.getPhone(), personToEdit.getEmail(),
                personToEdit.getAddress(), newRemark, personToEdit.getTags());

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        if (!isAppend && remark.value.isEmpty()) {
            String msg = String.format(
                    personToEdit.getRemark().value.isEmpty() ? MESSAGE_NO_REMARK_TO_REMOVE
                            : MESSAGE_DELETE_REMARK_SUCCESS, Messages.format(editedPerson));
            return new CommandResult(msg);
        }
        return new CommandResult(generateSuccessMessage(editedPerson));
    }

    private String generateSuccessMessage(Person personToEdit) {
        if (isAppend) {
            return String.format(MESSAGE_APPEND_REMARK_SUCCESS, Messages.format(personToEdit));
        }
        String message = !remark.value.isEmpty() ? MESSAGE_ADD_REMARK_SUCCESS : MESSAGE_DELETE_REMARK_SUCCESS;
        return String.format(message, Messages.format(personToEdit));
    }
}
