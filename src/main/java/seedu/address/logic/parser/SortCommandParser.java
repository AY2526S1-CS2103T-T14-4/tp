package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Locale;

import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.commands.SortCommand.Field;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses arguments for SortCommand.
 * Accepts: "a/name", "d/name", "a/address"
 */
public class SortCommandParser implements Parser<SortCommand> {
    @Override
    public SortCommand parse(String args) throws ParseException {
        String trimmed = args == null ? "" : args.trim();
        if (trimmed.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }

        // Expect "<dir>/<field>" where dir in {a, d}, field in {name, address}
        String[] parts = trimmed.split("/", 2);
        if (parts.length != 2) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }

        String dir = parts[0].trim().toLowerCase(Locale.ROOT);
        String fld = parts[1].trim().toLowerCase(Locale.ROOT);

        boolean ascending;
        if (dir.equals("asc")) {
            ascending = true;
        } else if (dir.equals("dsc")) {
            ascending = false;
        } else {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }

        Field field;
        switch (fld) {
        case "name":
            field = Field.NAME;
            break;
        case "address":
            field = Field.ADDRESS;
            break;
        default:
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        }

        return new SortCommand(field, ascending);
    }
}
