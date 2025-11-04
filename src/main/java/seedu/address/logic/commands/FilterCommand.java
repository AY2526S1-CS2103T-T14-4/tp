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
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": filters all seniors with the specified tag(s).\n"
            + "Parameters: t/TAG\n"
            + "Example: " + COMMAND_WORD + " t/friend";

    private final List<Tag> tags;

    /**
     * Creates a FilterCommand object to display entries with the specified tag
     * @param tagNames specified tags (criterion for filtering)
     */
    public FilterCommand(List<String> tagNames) {
        requireNonNull(tagNames);
        this.tags = tagNames.stream().map(Tag::new).toList();
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        Predicate<Person> predicate = person -> person.getTags().stream()
                .anyMatch(personTag -> tags.stream()
                        .anyMatch(filterTag -> filterTag.tagName.equalsIgnoreCase(personTag.tagName)));

        model.updateFilteredPersonList(predicate);

        String joinedTags = tags.stream().map(Tag::toString).reduce((a, b) -> a + ", " + b).orElse("");
        return new CommandResult(
                String.format("Listed all seniors with tag(s): %s", joinedTags));
    }

    @Override
    public boolean equals(Object other) {
        return other == this
                || (other instanceof FilterCommand
                && tags.equals(((FilterCommand) other).tags));
    }

    @Override
    public String toString() {
        String joinedTags = tags.stream().map(Tag::toString).reduce((a, b) -> a + ", " + b).orElse("");
        return "Listed all seniors with tag(s): " + joinedTags;
    }
}
