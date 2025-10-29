package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindCommand object
 */
public class FindCommandParser implements Parser<FindCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindCommand
     * and returns a FindCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        // Split by whitespace and filter out empty strings
        List<String> nameKeywords = Arrays.stream(trimmedArgs.split("\\s+"))
                .filter(keyword -> !keyword.isEmpty())
                .collect(Collectors.toList());

        // Check if we have any valid keywords after filtering
        if (nameKeywords.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
        }

        // NEW VALIDATION: Check if all keywords contain only letters
        for (String keyword : nameKeywords) {
            if (!keyword.matches("[a-zA-Z]+")) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                                "Find command only accepts alphabetical characters. "
                                        + "Numbers and special characters are not allowed."));
            }
        }

        return new FindCommand(new NameContainsKeywordsPredicate(nameKeywords));
    }

}
