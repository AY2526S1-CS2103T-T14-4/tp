package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_REMARK_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_REMARK_BOB;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.Remark;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for RemarkCommand.
 */
public class RemarkCommandTest {

    private static final String REMARK_STUB = "Some remark";

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_addRemarkUnfilteredList_success() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(firstPerson).withRemark(REMARK_STUB).build();

        // pass the remark value and isAppend = false
        RemarkCommand remarkCommand = new RemarkCommand(INDEX_FIRST_PERSON, new Remark(REMARK_STUB),
        /*isAppend=*/false);

        String expectedMessage = String.format(RemarkCommand.MESSAGE_ADD_REMARK_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(remarkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_deleteRemarkUnfilteredList_success() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(firstPerson).withRemark("").build();

        // use 3-arg constructor with isAppend = false
        RemarkCommand remarkCommand = new RemarkCommand(INDEX_FIRST_PERSON, new Remark(""), /*isAppend=*/false);

        String expectedMessage = String.format(RemarkCommand.MESSAGE_DELETE_REMARK_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(remarkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()))
                .withRemark(REMARK_STUB).build();

        RemarkCommand remarkCommand = new RemarkCommand(INDEX_FIRST_PERSON, new Remark(REMARK_STUB),
        /*isAppend=*/false);

        String expectedMessage = String.format(RemarkCommand.MESSAGE_ADD_REMARK_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(remarkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        // use 3-arg constructor; flag value doesn't matter for invalid index
        RemarkCommand remarkCommand = new RemarkCommand(outOfBoundIndex, new Remark(VALID_REMARK_BOB),
        /*isAppend=*/false);

        assertCommandFailure(remarkCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        // use 3-arg constructor; flag value doesn't matter for invalid index
        RemarkCommand remarkCommand = new RemarkCommand(outOfBoundIndex, new Remark(VALID_REMARK_BOB),
        /*isAppend=*/false);

        assertCommandFailure(remarkCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        final RemarkCommand standardCommand =
                new RemarkCommand(INDEX_FIRST_PERSON, new Remark(VALID_REMARK_AMY), false);

        // same values -> returns true
        RemarkCommand commandWithSameValues =
                new RemarkCommand(INDEX_FIRST_PERSON, new Remark(VALID_REMARK_AMY), false);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(
                new RemarkCommand(INDEX_SECOND_PERSON, new Remark(VALID_REMARK_AMY), false)));

        // different remark -> returns false
        assertFalse(standardCommand.equals(new RemarkCommand(INDEX_FIRST_PERSON,
                new Remark(VALID_REMARK_BOB), false)));

        // different append flag -> returns false
        assertFalse(standardCommand.equals(
                new RemarkCommand(INDEX_FIRST_PERSON, new Remark(VALID_REMARK_AMY), true)));
    }

    @Test
    public void execute_appendWhenExistingEmptySuccess() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        // Ensure existing remark is empty
        Person personWithEmptyRemark = new PersonBuilder(firstPerson).withRemark("").build();
        model.setPerson(firstPerson, personWithEmptyRemark);

        RemarkCommand cmd = new RemarkCommand(INDEX_FIRST_PERSON, new Remark("alpha"), true);

        Person expectedEdited = new PersonBuilder(personWithEmptyRemark).withRemark("alpha").build();
        String expectedMessage = String.format(RemarkCommand.MESSAGE_APPEND_REMARK_SUCCESS,
                Messages.format(expectedEdited));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(personWithEmptyRemark, expectedEdited);

        assertCommandSuccess(cmd, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_appendWhenExistingNonEmptyInsertSingleSpaceSuccess() {
        Person first = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person withRemark = new PersonBuilder(first).withRemark("alpha").build();
        model.setPerson(first, withRemark);

        RemarkCommand cmd = new RemarkCommand(INDEX_FIRST_PERSON, new Remark("beta"), true);

        Person expected = new PersonBuilder(withRemark).withRemark("alpha beta").build();
        String expectedMessage = String.format(RemarkCommand.MESSAGE_APPEND_REMARK_SUCCESS,
                Messages.format(expected));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(withRemark, expected);

        assertCommandSuccess(cmd, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_removeOnAlreadyEmptySuccessAndMessage() {
        Person first = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person empty = new PersonBuilder(first).withRemark("").build();
        model.setPerson(first, empty);

        RemarkCommand cmd = new RemarkCommand(INDEX_FIRST_PERSON, new Remark(""), false);

        // Deleting from empty still counts as remove
        Person expected = empty; // stays empty
        String expectedMessage = String.format(RemarkCommand.MESSAGE_DELETE_REMARK_SUCCESS,
                Messages.format(expected));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        // setPerson still called (no-op replacement) to keep behavior consistent
        expectedModel.setPerson(empty, expected);

        assertCommandSuccess(cmd, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_appendFilteredListSuccess() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Person first = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person edited = new PersonBuilder(first).withRemark(first.getRemark().value.isEmpty()
                ? "new" : (first.getRemark().value + " new")).build();

        RemarkCommand cmd = new RemarkCommand(INDEX_FIRST_PERSON, new Remark("new"), true);

        String expectedMessage = String.format(RemarkCommand.MESSAGE_APPEND_REMARK_SUCCESS,
                Messages.format(edited));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), edited);

        assertCommandSuccess(cmd, model, expectedMessage, expectedModel);
    }
}
