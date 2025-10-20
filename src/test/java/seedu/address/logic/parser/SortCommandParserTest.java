package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.SortCommand;

public class SortCommandParserTest {

    private final SortCommandParser parser = new SortCommandParser();

    @Test
    public void parse_validArgs_success() {
        assertParseSuccess(parser, "asc/name",
                new SortCommand(SortCommand.Field.NAME, /* ascending */ true));
        assertParseSuccess(parser, "dsc/name",
                new SortCommand(SortCommand.Field.NAME, /* ascending */ false));
        assertParseSuccess(parser, "asc/address",
                new SortCommand(SortCommand.Field.ADDRESS, /* ascending */ true));
        assertParseSuccess(parser, "dsc/address",
                new SortCommand(SortCommand.Field.ADDRESS, /* ascending */ false));
    }

    @Test
    public void parse_invalidArgs_failure() {
        assertParseFailure(parser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "a/name",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "asc/phone",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "asc name",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "xyz/address",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
    }
}
