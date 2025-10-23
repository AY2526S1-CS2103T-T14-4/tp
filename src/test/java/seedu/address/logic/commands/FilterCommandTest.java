package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class FilterCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        FilterCommand filterVipCommand = new FilterCommand("vip");
        FilterCommand filterFriendCommand = new FilterCommand("friend");

        assertTrue(filterVipCommand.equals(filterVipCommand));

        FilterCommand filterVipCommandCopy = new FilterCommand("vip");
        assertTrue(filterVipCommand.equals(filterVipCommandCopy));

        assertFalse(filterVipCommand.equals(1));

        assertFalse(filterVipCommand.equals(null));

        assertFalse(filterVipCommand.equals(filterFriendCommand));
    }

    @Test
    public void execute_noMatch_noPersonFound() {
        String expectedMessage = "Elderly with tag: [nonexistent]";
        FilterCommand command = new FilterCommand("nonexistent");
        expectedModel.updateFilteredPersonList(p -> false);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_tagMatch_multiplePersonsFound() {
        String expectedMessage = "Elderly with tag: [friends]";
        FilterCommand command = new FilterCommand("friends");

        expectedModel.updateFilteredPersonList(person ->
                person.getTags().stream()
                        .anyMatch(tag -> tag.tagName.equalsIgnoreCase("friends")));

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(ALICE, BENSON, DANIEL), model.getFilteredPersonList());
    }

    @Test
    public void toStringMethod() {
        FilterCommand filterCommand = new FilterCommand("vip");
        String expected = "Elderly with tag: [vip]";
        assertEquals(expected, filterCommand.toString());
    }
}
