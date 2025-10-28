package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

/**
 * Parses input arguments and creates a new DeleteCommand object
 */
public class DeleteCommandParser implements Parser<DeleteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteCommand
     * and returns a DeleteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteCommand parse(String args) throws ParseException {
        try {
            ArgumentMultimap argMultimap =
                    ArgumentTokenizer.tokenize(args, PREFIX_INDEX, PREFIX_NAME, PREFIX_PHONE);

            boolean indexIsPresent = arePrefixesPresent(argMultimap, PREFIX_INDEX);
            boolean nameIsPresent = arePrefixesPresent(argMultimap, PREFIX_NAME);
            boolean phoneIsPresent = arePrefixesPresent(argMultimap, PREFIX_PHONE);

            if (!isValidDeletionCriteria(indexIsPresent, nameIsPresent, phoneIsPresent)) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
            }

            argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_INDEX, PREFIX_NAME, PREFIX_PHONE);

            return getDeleteCommand(argMultimap, indexIsPresent, nameIsPresent, phoneIsPresent);

        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE), pe);
        }
    }

    private DeleteCommand getDeleteCommand(ArgumentMultimap argMultimap, boolean indexIsPresent, boolean nameIsPresent,
            boolean phoneIsPresent) throws ParseException {
        if (indexIsPresent) {
            Index index = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_INDEX).get());
            return new DeleteCommand(index);
        } else if (nameIsPresent && phoneIsPresent) {
            Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
            Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
            return new DeleteCommand(name, phone);
        } else {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        }
    }

    private boolean isValidDeletionCriteria(boolean indexIsPresent, boolean nameIsPresent, boolean phoneIsPresent) {
        return (indexIsPresent && !nameIsPresent && !phoneIsPresent)
                || (!indexIsPresent && nameIsPresent && phoneIsPresent);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
