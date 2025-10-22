package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input specified tag to create a new FilterCommand object
 */
public class FilterCommandParser implements Parser<FilterCommand> {

    @Override
    public FilterCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, CliSyntax.PREFIX_TAG);

        if (!argMultimap.getValue(CliSyntax.PREFIX_TAG).isPresent()
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    FilterCommand.MESSAGE_USAGE));
        }

        String tagName = argMultimap.getValue(CliSyntax.PREFIX_TAG).get();
        return new FilterCommand(tagName);
    }
}
