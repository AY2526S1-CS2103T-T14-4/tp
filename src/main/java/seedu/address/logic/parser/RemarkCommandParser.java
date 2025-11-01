package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_APPEND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMOVE;

import java.util.Optional;

import seedu.address.commons.core.index.Index;
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

        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args,
            PREFIX_INDEX, PREFIX_REMARK, PREFIX_REMOVE, PREFIX_APPEND);

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_INDEX, PREFIX_REMARK, PREFIX_REMOVE, PREFIX_APPEND);

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
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE), pe);
        }

        boolean hasRemove = argMultimap.getValue(PREFIX_REMOVE).isPresent();
        Optional<String> remarkOpt = argMultimap.getValue(PREFIX_REMARK);
        Optional<String> appendOpt = argMultimap.getValue(PREFIX_APPEND);

        // Check mutual exclusivity
        if ((hasRemove ? 1 : 0) + (remarkOpt.isPresent() ? 1 : 0) + (appendOpt.isPresent() ? 1 : 0) != 1) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE));
        }

        if (hasRemove) {
            return new RemarkCommand(index, new Remark(""), false);
        }

        if (remarkOpt.isPresent()) {
            String remark = remarkOpt.get().trim();
            if (remark.isEmpty()) {
                throw new ParseException("Remark cannot be empty. To remove a remark, use '--remove'.");
            }
            try {
                return new RemarkCommand(index, new Remark(remark), false);
            } catch (IllegalArgumentException ex) {
                throw new ParseException(Remark.MESSAGE_CONSTRAINTS, ex);
            }
        }

        // Handle append
        String appendText = appendOpt.get().trim();
        if (appendText.isEmpty()) {
            throw new ParseException("Appended text cannot be empty.");
        }
        try {
            // Note: this validates the append segment length (<= 2500). The final combined
            // length is validated in RemarkCommand#execute.
            return new RemarkCommand(index, new Remark(appendText), true);
        } catch (IllegalArgumentException ex) {
            throw new ParseException(Remark.MESSAGE_CONSTRAINTS, ex);
        }
    }
}
