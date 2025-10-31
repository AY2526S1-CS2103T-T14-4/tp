package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMOVE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.TagCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

/**
 * Parses input arguments to create a new TagCommand object
 */
public class TagCommandParser implements Parser<TagCommand> {

    @Override
    public TagCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(
                args, PREFIX_INDEX, PREFIX_NAME, PREFIX_PHONE, PREFIX_TAG, PREFIX_REMOVE);
        boolean isDelete = argMultimap.getValue(PREFIX_REMOVE).isPresent();

        try {
            boolean indexIsPresent = arePrefixesPresent(argMultimap, PREFIX_INDEX);
            boolean nameAndPhoneArePresent = arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_PHONE);
            boolean tagIsPresent = arePrefixesPresent(argMultimap, PREFIX_TAG);

            if (!tagIsPresent) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
            }

            argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_INDEX, PREFIX_NAME, PREFIX_PHONE, PREFIX_ADDRESS,
                    PREFIX_TAG);

            String tagName = argMultimap.getValue(PREFIX_TAG).get().trim();

            if (tagName.isEmpty()) {
                throw new ParseException("Tag name cannot be empty.");
            }

            int identifierCount = 0;
            if (indexIsPresent) {
                identifierCount++;
            }
            if (nameAndPhoneArePresent) {
                identifierCount++;
            }

            if (identifierCount != 1) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
            }

            if (indexIsPresent) {
                Index index = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_INDEX).get());
                return new TagCommand(index, tagName, isDelete);
            } else if (nameAndPhoneArePresent) {
                Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
                Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
                return new TagCommand(name, phone, tagName, isDelete);
            } else {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
            }

        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE), pe);
        }
    }

    /**
     * Returns true if all the given prefixes have values in the ArgumentMultimap.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
