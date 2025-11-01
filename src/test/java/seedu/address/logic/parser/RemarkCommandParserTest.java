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
    private static final String NON_EMPTY_REMARK = "Needs wheelchair access";
    private final RemarkCommandParser parser = new RemarkCommandParser();

    // ========== Success cases ==========

    @Test
    public void parse_addWithIndexPrefix_success() {
        String userInput = " i/" + INDEX_FIRST_PERSON.getOneBased() + " r/" + NON_EMPTY_REMARK;
        RemarkCommand expected = new RemarkCommand(INDEX_FIRST_PERSON, new Remark(NON_EMPTY_REMARK), false);
        assertParseSuccess(parser, userInput, expected);
    }

    @Test
    public void parse_appendWithIndexPrefix_success() {
        String appendText = "Has a cat";
        String userInput = " i/" + INDEX_FIRST_PERSON.getOneBased() + " ap/" + appendText;
        RemarkCommand expected = new RemarkCommand(INDEX_FIRST_PERSON, new Remark(appendText), true);
        assertParseSuccess(parser, userInput, expected);
    }

    // ========== Failure cases ==========

    @Test
    public void parse_missingEverything_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "", expectedMessage);
        assertParseFailure(parser, RemarkCommand.COMMAND_WORD, expectedMessage);
    }

    @Test
    public void parse_missingIndex_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE);
        assertParseFailure(parser, PREFIX_REMARK + NON_EMPTY_REMARK, expectedMessage);
        assertParseFailure(parser, "--remove", expectedMessage);
    }

    @Test
    public void parse_indexInPreambleNowInvalid_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "1 " + PREFIX_REMARK + NON_EMPTY_REMARK, expectedMessage);
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
        String userInput = "   i/1   " + PREFIX_REMARK + "  hello world  ";
        RemarkCommand expected = new RemarkCommand(Index.fromOneBased(1), new Remark("hello world"), false);
        assertParseSuccess(parser, userInput, expected);

        String userInput2 = "\t i/1 \t --remove  ";
        RemarkCommand expected2 = new RemarkCommand(Index.fromOneBased(1), new Remark(""), false);
        assertParseSuccess(parser, userInput2, expected2);
    }

    @Test
    public void parse_append_success() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = " i/" + targetIndex.getOneBased() + " ap/" + "extra note";
        RemarkCommand expected = new RemarkCommand(targetIndex, new Remark("extra note"), true);
        assertParseSuccess(parser, userInput, expected);
    }

    @Test
    public void parse_appendTrimsWhitespace_success() {
        String userInput = " i/1 ap/   extra   note   ";
        RemarkCommand expected = new RemarkCommand(Index.fromOneBased(1), new Remark("extra   note"), true);
        assertParseSuccess(parser, userInput, expected);
    }

    @Test
    public void parse_appendEmpty_failure() {
        assertParseFailure(parser, " i/1 ap/", "Appended text cannot be empty.");
        assertParseFailure(parser, " i/1 ap/    ", "Appended text cannot be empty.");
        assertParseFailure(parser, " i/1 ap/\t", "Appended text cannot be empty.");
    }

    @Test
    public void parse_mutuallyExclusiveRemarkAndAppend_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE);
        assertParseFailure(parser, " i/1 r/hey ap/there", expectedMessage);
    }

    @Test
    public void parse_mutuallyExclusiveAppendAndRemove_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE);
        assertParseFailure(parser, " i/1 ap/hello --remove", expectedMessage);
    }

    @Test
    public void parse_allThreeProvided_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE);
        assertParseFailure(parser, " i/1 r/x ap/y --remove", expectedMessage);
    }

    @Test
    public void parse_removeFlagPresent_successEvenWithoutValue() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = " i/" + targetIndex.getOneBased() + " --remove";
        RemarkCommand expected = new RemarkCommand(targetIndex, new Remark(""), false);
        assertParseSuccess(parser, userInput, expected);
    }

    @Test
    public void parse_preambleNotAllowed_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE);
        assertParseFailure(parser, " random r/hey", expectedMessage);
    }

    @Test
    public void parse_remarkTrimsWhitespace_success() {
        String userInput = " i/1 r/   hello   world   ";
        RemarkCommand expected = new RemarkCommand(Index.fromOneBased(1), new Remark("hello   world"), false);
        assertParseSuccess(parser, userInput, expected);
    }

    @Test
    public void parse_addRemarkMaxLength_success() {
        String max = "a".repeat(Remark.MAX_LENGTH);
        String userInput = " i/1 r/" + max;
        RemarkCommand expected = new RemarkCommand(Index.fromOneBased(1), new Remark(max), false);
        assertParseSuccess(parser, userInput, expected);
    }

    @Test
    public void parse_addRemarkOverLimit_failure() {
        String over = "a".repeat(Remark.MAX_LENGTH + 1);
        String userInput = " i/1 r/" + over;
        assertParseFailure(parser, userInput, Remark.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_appendSegmentOverLimit_failure() {
        String over = "a".repeat(Remark.MAX_LENGTH + 1);
        String userInput = " i/1 ap/" + over;
        assertParseFailure(parser, userInput, Remark.MESSAGE_CONSTRAINTS);
    }

}
