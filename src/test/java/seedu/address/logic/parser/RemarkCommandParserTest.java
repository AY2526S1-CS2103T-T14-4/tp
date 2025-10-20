package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.RemarkCommand;
import seedu.address.model.person.Remark;

public class RemarkCommandParserTest {
    private static final String NON_EMPTY_REMARK = "Some remark.";
    private final RemarkCommandParser parser = new RemarkCommandParser();

    // ========== Success cases ==========

    @Test
    public void parse_addWithIndexPrefix_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = " i/" + targetIndex.getOneBased() + " " + PREFIX_REMARK + NON_EMPTY_REMARK;
        RemarkCommand expected = new RemarkCommand(targetIndex, new Remark(NON_EMPTY_REMARK));
        assertParseSuccess(parser, userInput, expected);
    }

    @Test
    public void parse_removeFlag_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = " i/" + targetIndex.getOneBased() + " --remove";
        RemarkCommand expected = new RemarkCommand(targetIndex, new Remark(""));
        assertParseSuccess(parser, userInput, expected);
    }

    // ========== Failure cases ==========

    @Test
    public void parse_missingEverything_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE);
        assertParseFailure(parser, RemarkCommand.COMMAND_WORD, expectedMessage);
    }

    @Test
    public void parse_missingIndex_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE);
        assertParseFailure(parser, RemarkCommand.COMMAND_WORD
            + " " + PREFIX_REMARK + NON_EMPTY_REMARK, expectedMessage);
        assertParseFailure(parser, RemarkCommand.COMMAND_WORD + " --remove", expectedMessage);
    }

    @Test
    public void parse_indexInPreambleNowInvalid_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE);
        // Old style: "1 r/..." should now fail
        assertParseFailure(parser, "1 " + PREFIX_REMARK + NON_EMPTY_REMARK, expectedMessage);
        // Any preamble (even if i/ exists) should fail
        assertParseFailure(parser, "1 i/1 " + PREFIX_REMARK + NON_EMPTY_REMARK, expectedMessage);
    }

    @Test
    public void parse_missingActionNeitherRemarkNorRemove_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE);
        assertParseFailure(parser, " i/1", expectedMessage);
    }

    @Test
    public void parse_bothRemarkAndRemoveProvided_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE);
        assertParseFailure(parser, " i/1 " + PREFIX_REMARK + NON_EMPTY_REMARK + " --remove", expectedMessage);
    }

    @Test
    public void parse_emptyRemarkString_failure() {
        // Parser enforces non-empty remark; instructs to use --remove otherwise
        String expectedMessage = "Remark cannot be empty. To remove a remark, use '--remove'.";
        assertParseFailure(parser, " i/1 " + PREFIX_REMARK, expectedMessage);
        assertParseFailure(parser, " i/1 " + PREFIX_REMARK + "   ", expectedMessage);
        assertParseFailure(parser, " i/1 " + PREFIX_REMARK + "\t", expectedMessage);
    }

    @Test
    public void parse_invalidIndex_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE);
        assertParseFailure(parser, " i/0 " + PREFIX_REMARK + NON_EMPTY_REMARK, expectedMessage);
        assertParseFailure(parser, " i/-3 " + PREFIX_REMARK + NON_EMPTY_REMARK, expectedMessage);
        assertParseFailure(parser, " i/abc " + PREFIX_REMARK + NON_EMPTY_REMARK, expectedMessage);
    }

    @Test
    public void parse_withTrailingOrWeirdSpacing_success() {
        // Whitespace around tokens should be fine
        String userInput = "   i/1   " + PREFIX_REMARK + "  hello world  ";
        RemarkCommand expected = new RemarkCommand(Index.fromOneBased(1), new Remark("hello world"));
        assertParseSuccess(parser, userInput, expected);

        String userInput2 = "\t i/1 \t --remove  ";
        RemarkCommand expected2 = new RemarkCommand(Index.fromOneBased(1), new Remark(""));
        assertParseSuccess(parser, userInput2, expected2);
    }
}
