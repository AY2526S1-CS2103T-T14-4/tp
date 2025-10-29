package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindCommand;
import seedu.address.model.person.NameContainsKeywordsPredicate;

public class FindCommandParserTest {

    private FindCommandParser parser = new FindCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ", String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        // no leading and trailing whitespaces
        FindCommand expectedFindCommand =
                new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob")));
        assertParseSuccess(parser, "Alice Bob", expectedFindCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n Alice \n \t Bob  \t", expectedFindCommand);
    }

    @Test
    public void parse_keywordWithNumbers_throwsParseException() {
        String expectedErrorMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                "Find command only accepts alphabetical characters. Numbers and special characters are not allowed.");

        // Single keyword with numbers
        assertParseFailure(parser, "Alice123", expectedErrorMessage);

        // Multiple keywords with numbers mixed
        assertParseFailure(parser, "Alice Bob123", expectedErrorMessage);

        // Only numbers
        assertParseFailure(parser, "123", expectedErrorMessage);

        // Numbers at the beginning
        assertParseFailure(parser, "123Alice", expectedErrorMessage);

        // Numbers in the middle
        assertParseFailure(parser, "Ali123ce", expectedErrorMessage);
    }

    @Test
    public void parse_keywordWithSpecialCharacters_throwsParseException() {
        String expectedErrorMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                "Find command only accepts alphabetical characters. Numbers and special characters are not allowed.");

        // Special characters only
        assertParseFailure(parser, "@lice", expectedErrorMessage);

        // Hyphen in name
        assertParseFailure(parser, "Anne-Marie", expectedErrorMessage);

        // Apostrophe in name
        assertParseFailure(parser, "O'Connor", expectedErrorMessage);

        // Multiple special characters
        assertParseFailure(parser, "John@Doe", expectedErrorMessage);

        // Mixed special characters and numbers
        assertParseFailure(parser, "User123!", expectedErrorMessage);
    }

    @Test
    public void parse_keywordWithOnlyLettersMixedCase_returnsFindCommand() {
        // Test various valid cases with mixed capitalization
        FindCommand expectedFindCommand =
                new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList("ALICE", "bob", "CharLie")));
        assertParseSuccess(parser, "ALICE bob CharLie", expectedFindCommand);

        // All uppercase
        expectedFindCommand =
                new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList("JOHN", "DOE")));
        assertParseSuccess(parser, "JOHN DOE", expectedFindCommand);

        // All lowercase
        expectedFindCommand =
                new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList("john", "doe")));
        assertParseSuccess(parser, "john doe", expectedFindCommand);

        // With multiple spaces that get filtered out
        expectedFindCommand =
                new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob")));
        assertParseSuccess(parser, "Alice   Bob", expectedFindCommand);
    }

    @Test
    public void parse_onlySpacesBetweenKeywords_returnsFindCommand() {
        // Even with many spaces, the empty strings get filtered out
        FindCommand expectedFindCommand =
                new FindCommand(new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Bob")));
        assertParseSuccess(parser, "Alice     Bob", expectedFindCommand);
    }

    @Test
    public void parse_mixedValidAndInvalidKeywords_throwsParseException() {
        String expectedErrorMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                "Find command only accepts alphabetical characters. Numbers and special characters are not allowed.");

        // First keyword valid, second invalid
        assertParseFailure(parser, "Alice Bob123", expectedErrorMessage);

        // First keyword invalid, second valid
        assertParseFailure(parser, "Alice123 Bob", expectedErrorMessage);
    }

}
