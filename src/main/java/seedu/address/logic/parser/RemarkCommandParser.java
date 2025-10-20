package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMOVE;

import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.commands.RemarkCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Remark;
/**
 * Parses input arguments and creates a new {@code RemarkCommand} object
 */
public class RemarkCommandParser implements Parser<RemarkCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the {@code RemarkCommand}
     * and returns a {@code RemarkCommand} object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public RemarkCommand parse(String args) throws ParseException {
        requireNonNull(args);

        // Accept i/, r/, and --remove. Index must now be provided via i/.
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_INDEX, PREFIX_REMARK, PREFIX_REMOVE);

        // Disallow stray text before prefixes (index is not in preamble anymore)
        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE));
        }

        // Parse index from i/
        Index index;
        try {
            String indexRaw = argMultimap.getValue(PREFIX_INDEX)
                    .orElseThrow(() -> new ParseException(
                            String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE)));
            index = ParserUtil.parseIndex(indexRaw);
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE), ive);
        }

        // Mutually exclusive: either r/REMARK or --remove
        boolean hasRemove = argMultimap.getValue(PREFIX_REMOVE).isPresent();
        Optional<String> remarkOpt = argMultimap.getValue(PREFIX_REMARK);

        if (hasRemove == remarkOpt.isPresent()) {
            // either both present or both absent
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE));
        }

        if (hasRemove) {
            // canonicalize remove to empty remark
            return new RemarkCommand(index, new Remark(""));
        }

        // r/ path: require non-empty after trim
        String remark = remarkOpt.get().trim();
        if (remark.isEmpty()) {
            throw new ParseException("Remark cannot be empty. To remove a remark, use '--remove'.");
        }

        return new RemarkCommand(index, new Remark(remark));
    }
}
