package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class SortCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = freshModelWith(
                new PersonBuilder().withName("Charlie").withAddress("zzz Street").build(),
                new PersonBuilder().withName("alice").withAddress("AAA Road").build(),
                new PersonBuilder().withName("Bob").withAddress("mmm Ave").build()
        );
    }

    private Model freshModelWith(Person... persons) {
        AddressBook ab = new AddressBook();
        for (Person p : persons) {
            ab.addPerson(p);
        }
        return new ModelManager(ab, new UserPrefs());
    }

    @Test
    public void execute_sortByNameAscending_success() {
        SortCommand cmd = new SortCommand(SortCommand.Field.NAME, /* ascending */ true);
        CommandResult result = cmd.execute(model);

        assertEquals(SortCommand.MESSAGE_SUCCESS_NAME_ASC, result.getFeedbackToUser());

        List<String> names = model.getFilteredPersonList().stream()
                .map(p -> p.getName().toString())
                .collect(Collectors.toList());
        List<String> expected = names.stream()
                .sorted(String::compareToIgnoreCase)
                .collect(Collectors.toList());
        assertEquals(expected, names);
    }

    @Test
    public void execute_sortByNameDescending_success() {
        SortCommand cmd = new SortCommand(SortCommand.Field.NAME, /* ascending */ false);
        CommandResult result = cmd.execute(model);

        assertEquals(SortCommand.MESSAGE_SUCCESS_NAME_DESC, result.getFeedbackToUser());

        List<String> names = model.getFilteredPersonList().stream()
                .map(p -> p.getName().toString())
                .collect(Collectors.toList());
        List<String> expected = names.stream()
                .sorted((a, b) -> b.compareToIgnoreCase(a))
                .collect(Collectors.toList());
        assertEquals(expected, names);
    }

    @Test
    public void execute_sortByAddressAscending_success() {
        SortCommand cmd = new SortCommand(SortCommand.Field.ADDRESS, /* ascending */ true);
        CommandResult result = cmd.execute(model);

        assertEquals(SortCommand.MESSAGE_SUCCESS_ADDR_ASC, result.getFeedbackToUser());

        List<String> addrs = model.getFilteredPersonList().stream()
                .map(p -> p.getAddress().toString()) // use .value if that's your convention
                .collect(Collectors.toList());
        List<String> expected = addrs.stream()
                .sorted(String::compareToIgnoreCase)
                .collect(Collectors.toList());
        assertEquals(expected, addrs);
    }

    @Test
    public void equals_onlyUsesFieldAndDirection() {
        SortCommand a1 = new SortCommand(SortCommand.Field.NAME, true);
        SortCommand a2 = new SortCommand(SortCommand.Field.NAME, true);
        SortCommand d1 = new SortCommand(SortCommand.Field.NAME, false);
        SortCommand addr = new SortCommand(SortCommand.Field.ADDRESS, true);

        assertEquals(a1, a2); // same values
        assertNotEquals(a1, d1); // different direction
        assertNotEquals(a1, addr); // different field
        assertNotEquals(a1, null); // sanity
        assertNotEquals(a1, new ListCommand());
    }

    @Test
    public void execute_sort_doesNotChangeCount() {
        int before = model.getFilteredPersonList().size();

        // sort by name asc
        new SortCommand(SortCommand.Field.NAME, true).execute(model);
        assertEquals(before, model.getFilteredPersonList().size());

        // sort by name desc
        new SortCommand(SortCommand.Field.NAME, false).execute(model);
        assertEquals(before, model.getFilteredPersonList().size());

        // sort by address asc
        new SortCommand(SortCommand.Field.ADDRESS, true).execute(model);
        assertEquals(before, model.getFilteredPersonList().size());

        // sort by address desc
        new SortCommand(SortCommand.Field.ADDRESS, false).execute(model);
        assertEquals(before, model.getFilteredPersonList().size());
    }
}
