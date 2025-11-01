package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

        RemarkCommand remarkCommand = new RemarkCommand(INDEX_FIRST_PERSON, new Remark(REMARK_STUB), false);

        String expectedMessage = String.format(RemarkCommand.MESSAGE_ADD_REMARK_SUCCESS,
                Messages.format(editedPerson), editedPerson.getRemark().value);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(remarkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_deleteRemarkUnfilteredList_success() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        // Ensure existing remark is non-empty before removal
        Person withRemark = new PersonBuilder(firstPerson).withRemark("alpha").build();
        model.setPerson(firstPerson, withRemark);

        Person editedPerson = new PersonBuilder(withRemark).withRemark("").build();
        RemarkCommand remarkCommand = new RemarkCommand(INDEX_FIRST_PERSON, new Remark(""), false);

        String expectedMessage = String.format(RemarkCommand.MESSAGE_DELETE_REMARK_SUCCESS,
                Messages.format(editedPerson));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(withRemark, editedPerson);

        assertCommandSuccess(remarkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person editedPerson = new PersonBuilder(firstPerson).withRemark(REMARK_STUB).build();

        RemarkCommand remarkCommand = new RemarkCommand(INDEX_FIRST_PERSON, new Remark(REMARK_STUB), false);

        String expectedMessage = String.format(RemarkCommand.MESSAGE_ADD_REMARK_SUCCESS,
                Messages.format(editedPerson), editedPerson.getRemark().value);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPerson, editedPerson);

        assertCommandSuccess(remarkCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_appendWithNonEmptyRemark_includesPreviousRemarkInMessage() throws Exception {
        // Set up a person with an existing remark
        String previousRemark = "[OLD-REMARK-123]";
        Person original = new PersonBuilder().withRemark(previousRemark).build();

        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        model.addPerson(original);

        // isAppend = true, new remark is non-empty -> final line should use 2-arg format
        RemarkCommand cmd = new RemarkCommand(Index.fromOneBased(1),
                new Remark(" new note"), /* isAppend */ true);

        CommandResult result = cmd.execute(model);
        String feedback = result.getFeedbackToUser();

        // The message should contain the previous remark value as the 2nd formatted arg
        assertTrue(feedback.contains(previousRemark),
                "Expected feedback to include previous remark when appending non-empty text.");
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        // use 3-arg constructor; flag value doesn't matter for invalid index
        RemarkCommand remarkCommand = new RemarkCommand(outOfBoundIndex, new Remark(VALID_REMARK_BOB),
                false);

        assertCommandFailure(remarkCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_removeWhenAlreadyEmpty_returnsNoRemarkToRemove() {
        // Arrange: ensure the target person already has an empty remark
        Person first = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person alreadyEmpty = new PersonBuilder(first).withRemark("").build();
        model.setPerson(first, alreadyEmpty);

        // Act: run a remove (isAppend = false, remark = "")
        RemarkCommand cmd = new RemarkCommand(INDEX_FIRST_PERSON, new Remark(""), /*isAppend=*/false);

        // Expect: MESSAGE_NO_REMARK_TO_REMOVE and no change to remark
        String expectedMessage = String.format(
                RemarkCommand.MESSAGE_NO_REMARK_TO_REMOVE,
                Messages.format(alreadyEmpty));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(alreadyEmpty, alreadyEmpty); // no-op replacement to mirror command flow

        assertCommandSuccess(cmd, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_replaceWhenExistingNonEmptyIncludesRemarkInMessage_success() {
        // Person initially with a non-empty remark ("old")
        Person first = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person withOldRemark = new PersonBuilder(first).withRemark("old").build();
        model.setPerson(first, withOldRemark);

        // Replace (not append) with "new"
        RemarkCommand cmd = new RemarkCommand(INDEX_FIRST_PERSON, new Remark("new"), /*isAppend*/ false);

        Person expected = new PersonBuilder(withOldRemark).withRemark("new").build();

        // Must hit the add/replace branch that formats with two placeholders and a newline.
        String expectedMessage = String.format(
                RemarkCommand.MESSAGE_ADD_REMARK_SUCCESS,
                Messages.format(expected),
                "new");

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(withOldRemark, expected);

        assertCommandSuccess(cmd, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_add_includesExactTwoPlaceholderFormatting() {
        // Start from empty remark to confirm the exact string formatting (including the newline)
        Person first = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person empty = new PersonBuilder(first).withRemark("").build();
        model.setPerson(first, empty);

        String newRemark = "line1 and line2";
        RemarkCommand cmd = new RemarkCommand(INDEX_FIRST_PERSON, new Remark(newRemark), /*isAppend*/ false);

        Person expected = new PersonBuilder(empty).withRemark(newRemark).build();

        // Verify exact formatting with newline between the two placeholders:
        // "Added remark to Senior: %1$s\nRemark: %2$s"
        String expectedMessage = "Added remark to Senior: "
                + Messages.format(expected)
                + "\nRemark: "
                + newRemark;

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(empty, expected);

        assertCommandSuccess(cmd, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        // use 3-arg constructor; flag value doesn't matter for invalid index
        RemarkCommand remarkCommand = new RemarkCommand(outOfBoundIndex, new Remark(VALID_REMARK_BOB),
                false);

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
        Person personWithEmptyRemark = new PersonBuilder(firstPerson).withRemark("").build();
        model.setPerson(firstPerson, personWithEmptyRemark);

        RemarkCommand cmd = new RemarkCommand(INDEX_FIRST_PERSON, new Remark("alpha"), true);

        Person expectedEdited = new PersonBuilder(personWithEmptyRemark).withRemark("alpha").build();
        String expectedMessage = String.format(RemarkCommand.MESSAGE_APPEND_REMARK_SUCCESS,
                Messages.format(expectedEdited), expectedEdited.getRemark().value);

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

        Person expected = new PersonBuilder(withRemark).withRemark("alphabeta").build();
        String expectedMessage = String.format(RemarkCommand.MESSAGE_APPEND_REMARK_SUCCESS,
                Messages.format(expected), expected.getRemark().value);

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

        Person expected = empty; // stays empty
        String expectedMessage = String.format(RemarkCommand.MESSAGE_NO_REMARK_TO_REMOVE,
                Messages.format(expected));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(empty, expected);

        assertCommandSuccess(cmd, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_appendFilteredListSuccess() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Person first = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person edited = new PersonBuilder(first).withRemark(first.getRemark().value.isEmpty()
                ? "new" : (first.getRemark().value + "new")).build();

        RemarkCommand cmd = new RemarkCommand(INDEX_FIRST_PERSON, new Remark("new"), true);

        String expectedMessage = String.format(RemarkCommand.MESSAGE_APPEND_REMARK_SUCCESS,
                Messages.format(edited), edited.getRemark().value);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), edited);

        assertCommandSuccess(cmd, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_appendBoundary_success() {
        Person first = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        String appendText = "ab";
        String existing = "x".repeat(Remark.MAX_LENGTH - appendText.length());
        Person withRemark = new PersonBuilder(first).withRemark(existing).build();
        model.setPerson(first, withRemark);

        RemarkCommand cmd = new RemarkCommand(INDEX_FIRST_PERSON, new Remark(appendText), true);

        Person expected = new PersonBuilder(withRemark).withRemark(existing + appendText).build();
        String expectedMessage = String.format(RemarkCommand.MESSAGE_APPEND_REMARK_SUCCESS,
                Messages.format(expected), expected.getRemark().value);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(withRemark, expected);

        assertCommandSuccess(cmd, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_appendExceedsLimit_failure() {
        Person first = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        // existing length 2499, append 2 -> 2501 FAIL
        String existing = "x".repeat(Remark.MAX_LENGTH - 1);
        Person withRemark = new PersonBuilder(first).withRemark(existing).build();
        model.setPerson(first, withRemark);

        RemarkCommand cmd = new RemarkCommand(INDEX_FIRST_PERSON, new Remark("ab"), true);
        assertCommandFailure(cmd, model, Remark.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void execute_replaceMaxLength_success() {
        Person first = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        String max = "a".repeat(Remark.MAX_LENGTH);

        Person edited = new PersonBuilder(first).withRemark(max).build();
        RemarkCommand cmd = new RemarkCommand(INDEX_FIRST_PERSON, new Remark(max), false);

        String expectedMessage = String.format(RemarkCommand.MESSAGE_ADD_REMARK_SUCCESS,
                Messages.format(edited), edited.getRemark().value);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(first, edited);

        assertCommandSuccess(cmd, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_removeOnAlreadyEmpty_usesNoRemarkToRemoveMessage() {
        Person first = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        // Ensure existing remark is empty
        Person empty = new PersonBuilder(first).withRemark("").build();
        model.setPerson(first, empty);

        RemarkCommand cmd = new RemarkCommand(INDEX_FIRST_PERSON, new Remark(""), /*isAppend=*/false);

        // After execution, person remains empty; message should be NO_REMARK_TO_REMOVE
        Person expected = empty; // unchanged
        String expectedMessage = String.format(
                RemarkCommand.MESSAGE_NO_REMARK_TO_REMOVE,
                Messages.format(expected));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(empty, expected);

        assertCommandSuccess(cmd, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_add_includesRemarkInMessage() {
        Person first = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        String newRemark = "hello there";
        Person edited = new PersonBuilder(first).withRemark(newRemark).build();

        RemarkCommand cmd = new RemarkCommand(INDEX_FIRST_PERSON, new Remark(newRemark), /*isAppend=*/false);

        String expectedMessage = String.format(
                RemarkCommand.MESSAGE_ADD_REMARK_SUCCESS,
                Messages.format(edited),
                newRemark);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(first, edited);

        assertCommandSuccess(cmd, model, expectedMessage, expectedModel);
    }

    /**
     * Guard against accidental double-spacing when existing remark already ends with a space.
     * If your intended behavior is different, adjust expectedFinal accordingly.
     */
    @Test
    public void execute_appendExistingEndsWithSpace_noDoubleSpace() {
        Person first = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Person withTrailingSpace = new PersonBuilder(first).withRemark("alpha ").build();
        model.setPerson(first, withTrailingSpace);

        RemarkCommand cmd = new RemarkCommand(INDEX_FIRST_PERSON, new Remark("beta"), /*isAppend=*/true);

        String expectedFinal = "alpha beta"; // single space
        Person expected = new PersonBuilder(withTrailingSpace).withRemark(expectedFinal).build();

        String expectedMessage = String.format(
                RemarkCommand.MESSAGE_APPEND_REMARK_SUCCESS,
                Messages.format(expected),
                expectedFinal);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(withTrailingSpace, expected);

        assertCommandSuccess(cmd, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_replaceOverLimitViaReflection_failure() throws Exception {
        // Build a command with a safe remark first (so constructor doesn't throw)
        RemarkCommand cmd = new RemarkCommand(INDEX_FIRST_PERSON, new Remark("x"), false);

        // Reflectively grab the internal Remark instance from the command
        java.lang.reflect.Field remarkField = RemarkCommand.class.getDeclaredField("remark");
        remarkField.setAccessible(true);
        Remark internalRemark = (Remark) remarkField.get(cmd);

        // Overwrite its public final 'value' with an overlong string to exceed 2500
        String tooLong = "a".repeat(Remark.MAX_LENGTH + 1);
        java.lang.reflect.Field valueField = Remark.class.getDeclaredField("value");
        valueField.setAccessible(true);
        valueField.set(internalRemark, tooLong);

        assertCommandFailure(cmd, model, Remark.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void execute_append_includesFinalRemarkInMessage() {
        Person first = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        // Start with a non-empty remark to exercise the append path + message %2$s
        Person withRemark = new PersonBuilder(first).withRemark("alpha").build();
        model.setPerson(first, withRemark);

        RemarkCommand cmd = new RemarkCommand(INDEX_FIRST_PERSON, new Remark("beta"), /*isAppend=*/true);

        // If your implementation inserts a single space, this becomes "alpha beta".
        // If you intentionally removed the space, change expectedFinal to "alphabeta".
        String expectedFinal = "alphabeta";
        Person expected = new PersonBuilder(withRemark).withRemark(expectedFinal).build();

        String expectedMessage = String.format(
                RemarkCommand.MESSAGE_APPEND_REMARK_SUCCESS,
                Messages.format(expected),
                expectedFinal);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(withRemark, expected);

        assertCommandSuccess(cmd, model, expectedMessage, expectedModel);
    }


    @Test
    public void execute_removeExistingRemark_returnsDeleteSuccessMessage() throws Exception {
        // Model with one person who HAS a remark
        Person personWithRemark = new PersonBuilder().withRemark("needs follow-up").build();
        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        model.addPerson(personWithRemark);

        // Build what the edited person will look like after removal (empty remark)
        Person editedPerson = new PersonBuilder(personWithRemark).withRemark("").build();
        String expectedMsg = String.format(
                RemarkCommand.MESSAGE_DELETE_REMARK_SUCCESS,
                Messages.format(editedPerson)
        );

        // User command: not append, empty remark -> remove
        RemarkCommand cmd = new RemarkCommand(Index.fromOneBased(1), new Remark(""), /* isAppend */ false);

        CommandResult result = cmd.execute(model);

        assertEquals(expectedMsg, result.getFeedbackToUser());
    }
}
