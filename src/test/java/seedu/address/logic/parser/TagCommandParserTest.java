package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.TagCommand;
import seedu.address.model.person.Address;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;

public class TagCommandParserTest {

    private final TagCommandParser parser = new TagCommandParser();

    @Test
    public void parse_indexBased_success() {
        TagCommand expected = new TagCommand(Index.fromOneBased(1), "friend");
        assertParseSuccess(parser, " i/1 t/friend", expected);
    }

    @Test
    public void parse_namePhoneBased_success() {
        TagCommand expected = new TagCommand(new Name("Bob Choo"), new Phone("88888888"), "friend");
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + TAG_DESC_FRIEND, expected);
    }

    @Test
    public void parse_namePhoneAddressBased_success() {
        TagCommand expected = new TagCommand(new Name("Bob Choo"), new Phone("88888888"),
                new Address("Block 123, Bobby Street 3"), "friend");
        assertParseSuccess(parser, NAME_DESC_BOB + PHONE_DESC_BOB + ADDRESS_DESC_BOB + TAG_DESC_FRIEND,
                expected);
    }

    @Test
    public void parse_missingTag_failure() {
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_multipleIdentifiers_failure() {
        // too many identifiers
        assertParseFailure(parser, " i/1 " + NAME_DESC_BOB + PHONE_DESC_BOB + TAG_DESC_FRIEND,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));

        // missing tag
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + ADDRESS_DESC_BOB,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_whitespacePreamble_success() {
        TagCommand expected = new TagCommand(new Name("Bob Choo"), new Phone("88888888"), "friend");
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_BOB + PHONE_DESC_BOB + TAG_DESC_FRIEND,
                expected);
    }
}
