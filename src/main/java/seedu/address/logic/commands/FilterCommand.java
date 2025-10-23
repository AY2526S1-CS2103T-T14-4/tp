package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Filters and lists all elderly with the specified tag.
 */
public class FilterCommand extends Command {

    public static final String COMMAND_WORD = "filter";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": filters all persons with the specified tag.\n"
            + "Parameters: t/TAG\n"
            + "Example: " + COMMAND_WORD + " t/friend";

    private final Tag tag;

    /**
     * Creates a FilterCommand object to display entries with the specified tag
     * @param tagNames specified tags (criterion for filtering)
     */
    public FilterCommand(List<String> tagNames) {
        requireNonNull(tagNames);
        this.tag = tagNames.stream().map(Tag::new).toList();
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        Predicate<Person> predicate = person -> person.getTags().stream()
                .anyMatch(t -> t.tagName.equalsIgnoreCase(tag.tagName));

        model.updateFilteredPersonList(predicate);

        return new CommandResult(
                String.format("Elderly with tag: %s", tag.toString()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof FilterCommand
                && tag.equals(((FilterCommand) other).tag));
    }

    @Override
    public String toString() {
        return "Elderly with tag: " + tag;
    }
}
