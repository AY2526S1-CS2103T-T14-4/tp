package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FilterCommand;

public class FilterCommandParserTest {

    private final FilterCommandParser parser = new FilterCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingPrefix_throwsParseException() {
        assertParseFailure(parser, "vip",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFilterCommand() {
        FilterCommand expectedCommand = new FilterCommand("vip");
        assertParseSuccess(parser, " t/vip", expectedCommand);

        assertParseSuccess(parser, " \n t/vip \t", expectedCommand);
    }

    @Test
    public void parse_multiplePrefixes_throwsParseException() {
        assertParseFailure(parser, " t/vip t/friend",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_preamblePresent_throwsParseException() {
        assertParseFailure(parser, "someText t/vip",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FilterCommand.MESSAGE_USAGE));
    }
}
