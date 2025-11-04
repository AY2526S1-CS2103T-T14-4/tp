package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;

public class FilterCommandTest {

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private final Model expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void equals() {
        List<String> firstTagList = Collections.singletonList("vip");
        List<String> secondTagList = Collections.singletonList("friend");

        FilterCommand filterFirstCommand = new FilterCommand(firstTagList);
        FilterCommand filterSecondCommand = new FilterCommand(secondTagList);

        assertTrue(filterFirstCommand.equals(filterFirstCommand));

        FilterCommand filterFirstCommandCopy = new FilterCommand(firstTagList);
        assertTrue(filterFirstCommand.equals(filterFirstCommandCopy));

        assertFalse(filterFirstCommand.equals(1));

        assertFalse(filterFirstCommand.equals(null));

        assertFalse(filterFirstCommand.equals(filterSecondCommand));
    }

    @Test
    public void execute_noMatch_noPersonFound() {
        String expectedMessage = "Elderly with tag(s): [nonexistent]";
        FilterCommand command = new FilterCommand(Collections.singletonList("nonexistent"));
        expectedModel.updateFilteredPersonList(p -> false);
        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_multipleTags_multiplePersonsFound() {
        String expectedMessage = "Elderly with tag(s): [vip], [friend]";
        List<String> tagsToFilter = Arrays.asList("vip", "friend");
        FilterCommand command = new FilterCommand(tagsToFilter);

        expectedModel.updateFilteredPersonList(person ->
                person.getTags().stream()
                        .anyMatch(personTag ->
                                tagsToFilter.stream()
                                        .anyMatch(t -> t.equalsIgnoreCase(personTag.tagName))));

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
        assertEquals(Arrays.asList(CARL, ELLE, FIONA), model.getFilteredPersonList());
    }

    @Test
    public void toStringMethod() {
        List<String> tags = Arrays.asList("vip", "friend");
        FilterCommand filterCommand = new FilterCommand(tags);
        String result = filterCommand.toString();

        assertTrue(result.contains("vip"));
        assertTrue(result.contains("friend"));
        assertTrue(result.startsWith("Listed all seniors with tag(s):"));
    }
}
