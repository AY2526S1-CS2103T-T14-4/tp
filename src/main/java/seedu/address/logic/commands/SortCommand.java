package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.Model;

/**
 * Sorts the details of all elderly in the address book by name in ascending order.
 */
public class SortCommand extends Command {

    public static final String COMMAND_WORD = "sort";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sorts all elderly by name from A->Z.\n"
            + "Example: " + COMMAND_WORD + " by NAME";

    public static final String MESSAGE_SUCCESS = "Sorted all elderly by name.";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.sortPersonsByName(/* ascending = */ true);
        // Ensure the UI shows the full, freshly-sorted list
        model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
