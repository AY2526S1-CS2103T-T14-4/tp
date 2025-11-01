package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.Model;

/**
 * Sorts senior entries by their name or address in ascending/descending order.
 */
public class SortCommand extends Command {

    public static final String COMMAND_WORD = "sort";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Sorts seniors by a field.\n"
            + "Parameters: Sort (ASC | DSC)/(NAME | ADDRESS)\n"
            + "Examples: \n\t"
            + "  " + COMMAND_WORD + " asc/name\n\t"
            + "  " + COMMAND_WORD + " dsc/name\n\t"
            + "  " + COMMAND_WORD + " asc/address\n\t"
            + "  " + COMMAND_WORD + " dsc/address";

    public static final String MESSAGE_SUCCESS_NAME_ASC = "Sorted all seniors by name (A→Z).";
    public static final String MESSAGE_SUCCESS_NAME_DESC = "Sorted all seniors by name (Z→A).";
    public static final String MESSAGE_SUCCESS_ADDR_ASC = "Sorted all seniors by address (A→Z).";
    public static final String MESSAGE_SUCCESS_ADDR_DESC = "Sorted all seniors by address (Z→A).";

    /**
     * Field contains attributes seniors can be sorted by
     * Can only be sorted by Name or Address
     */
    public enum Field { NAME, ADDRESS }

    private final Field field;
    private final boolean ascending;

    /**
     * Create a SortCommand to sort the seniors.
     */
    public SortCommand(Field field, boolean ascending) {
        this.field = requireNonNull(field);
        this.ascending = ascending;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        switch (field) {
        case NAME:
            model.sortPersonsByName(ascending);
            //model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
            return new CommandResult(ascending ? MESSAGE_SUCCESS_NAME_ASC : MESSAGE_SUCCESS_NAME_DESC);

        case ADDRESS:
            model.sortPersonsByAddress(ascending);
            //model.updateFilteredPersonList(Model.PREDICATE_SHOW_ALL_PERSONS);
            return new CommandResult(ascending ? MESSAGE_SUCCESS_ADDR_ASC : MESSAGE_SUCCESS_ADDR_DESC);

        default:
            // Should never happen
            throw new AssertionError("Unknown sort field: " + field);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof SortCommand)) {
            return false;
        }
        SortCommand o = (SortCommand) other;
        return this.field == o.field && this.ascending == o.ascending;
    }
}
