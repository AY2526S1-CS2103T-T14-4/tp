// EditCommandParser.java (full file replacement)
package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new EditCommand object
 */
public class EditCommandParser implements Parser<EditCommand> {

    @Override
    public EditCommand parse(String args) throws ParseException {
        requireNonNull(args);

        // Now we require i/INDEX and no preamble
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_INDEX, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TAG);

        // disallow any preamble (old style "edit 1 ..." becomes invalid)
        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }

        // parse index from i/
        final String rawIndex = argMultimap.getValue(PREFIX_INDEX)
                .orElseThrow(() -> new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE)));

        Index index;
        try {
            index = ParserUtil.parseIndex(rawIndex);
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE), pe);
        }

        // prevent duplicate scalar prefixes
        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_INDEX, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS);

        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();

        argMultimap.getValue(PREFIX_NAME).ifPresent(v -> {
            try {
                editPersonDescriptor.setName(ParserUtil.parseName(v));
            } catch (ParseException e) {
                throw new RuntimeParseException(e); // wrap to rethrow after
            }
        });

        argMultimap.getValue(PREFIX_PHONE).ifPresent(v -> {
            try {
                editPersonDescriptor.setPhone(ParserUtil.parsePhone(v));
            } catch (ParseException e) {
                throw new RuntimeParseException(e);
            }
        });

        argMultimap.getValue(PREFIX_EMAIL).ifPresent(v -> {
            try {
                editPersonDescriptor.setEmail(ParserUtil.parseEmail(v));
            } catch (ParseException e) {
                throw new RuntimeParseException(e);
            }
        });

        argMultimap.getValue(PREFIX_ADDRESS).ifPresent(v -> {
            try {
                editPersonDescriptor.setAddress(ParserUtil.parseAddress(v));
            } catch (ParseException e) {
                throw new RuntimeParseException(e);
            }
        });

        try {
            parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editPersonDescriptor::setTags);
        } catch (ParseException e) {
            throw new ParseException(e.getMessage(), e);
        } catch (RuntimeParseException rpe) {
            throw (ParseException) rpe.getCause();
        }

        if (!editPersonDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }

        return new EditCommand(index, editPersonDescriptor);
    }

    /** Parses tags helper (unchanged). */
    private Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws ParseException {
        assert tags != null;
        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }

    /** Local unchecked wrapper so we can use lambdas cleanly and still emit ParseException. */
    private static class RuntimeParseException extends RuntimeException {
        RuntimeParseException(ParseException cause) { super(cause); }
    }
}
