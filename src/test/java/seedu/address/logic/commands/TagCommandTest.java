package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.person.Address;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.PersonBuilder;

public class TagCommandTest {

    @Test
    public void constructor_nullArgs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new TagCommand(null, "vip"));
        assertThrows(NullPointerException.class, () -> new TagCommand(null, new Phone("91234567"),
                "vip"));
        assertThrows(NullPointerException.class, () -> new TagCommand(new Name("Alex"), null,
                "vip"));
        assertThrows(NullPointerException.class, () -> new TagCommand(new Name("Alex"), new Phone("91234567"),
                null));
        assertThrows(NullPointerException.class, () -> new TagCommand(new Name("Alex"), new Phone("91234567"),
                new Address("Drury Lane"), null));
        assertThrows(NullPointerException.class, () -> new TagCommand(new Name("Alex"), new Phone("91234567"),
                null, "vip"));
    }

    @Test
    public void execute_indexBased_success() throws Exception {
        Person validPerson = new PersonBuilder().build();
        ModelStubAcceptingPersonTagged modelStub = new ModelStubAcceptingPersonTagged(validPerson);

        TagCommand tagCommand = new TagCommand(Index.fromZeroBased(0), "vip");
        tagCommand.execute(modelStub);

        assertTrue(modelStub.personsModified.get(0).getTags().contains(new Tag("vip")));
    }

    @Test
    public void execute_namePhoneBased_success() throws Exception {
        Person validPerson = new PersonBuilder().withName("Alex").withPhone("91234567").build();
        ModelStubAcceptingPersonTagged modelStub = new ModelStubAcceptingPersonTagged(validPerson);

        TagCommand tagCommand = new TagCommand(new Name("alex"), new Phone("91234567"), "vip");
        tagCommand.execute(modelStub);

        assertTrue(modelStub.personsModified.get(0).getTags().contains(new Tag("vip")));
    }

    @Test
    public void execute_addressBased_success() throws Exception {
        Person validPerson = new PersonBuilder()
                .withName("Alex")
                .withPhone("91234567")
                .withAddress("Drury Lane")
                .build();
        ModelStubAcceptingPersonTagged modelStub = new ModelStubAcceptingPersonTagged(validPerson);
        TagCommand tagCommand = new TagCommand(new Name("Alex"), new Phone("91234567"),
                new Address("drury lane"), "vip");
        tagCommand.execute(modelStub);

        assertTrue(modelStub.personsModified.get(0).getTags().contains(new Tag("vip")));
    }

    @Test
    public void execute_duplicateTag_throwsCommandException() throws Exception {
        Person validPerson = new PersonBuilder().build().addTag(new Tag("vip"));
        ModelStubAcceptingPersonTagged modelStub = new ModelStubAcceptingPersonTagged(validPerson);

        TagCommand tagCommand = new TagCommand(Index.fromZeroBased(0), "vip");

        assertThrows(CommandException.class,
                "The person already has tag [vip]", () -> tagCommand.execute(modelStub));
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() throws ParseException {
        Person validPerson = new PersonBuilder().build();
        ModelStubAcceptingPersonTagged modelStub = new ModelStubAcceptingPersonTagged(validPerson);

        TagCommand tagCommand = new TagCommand(Index.fromZeroBased(1), "vip");

        assertThrows(CommandException.class, () -> tagCommand.execute(modelStub));
    }

    @Test
    public void equals() throws ParseException {
        TagCommand tagIndex1 = new TagCommand(Index.fromZeroBased(0), "vip");
        TagCommand tagIndex2 = new TagCommand(Index.fromZeroBased(1), "vip");
        TagCommand tagNamePhone = new TagCommand(new Name("Alex"), new Phone("91234567"), "vip");

        assertTrue(tagIndex1.equals(tagIndex1));
        assertFalse(tagIndex1.equals(null));
        assertFalse(tagIndex1.equals(tagNamePhone));
        assertFalse(tagIndex1.equals(tagIndex2));
    }

    @Test
    public void toStringMethod() throws ParseException {
        TagCommand tagCommand = new TagCommand(Index.fromZeroBased(0), "vip");
        String expected = TagCommand.class.getCanonicalName() + "{targetIndex=0, targetName=null, targetPhone=null, "
                + "targetAddress=null, tag=vip}";
        assertTrue(tagCommand.toString().contains("vip"));
    }

    /**
     * A default model stub that has all of the methods failing
     */
    private class ModelStub implements Model {
        @Override
        public void setUserPrefs(ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public Path getAddressBookFilePath() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBookFilePath(Path addressBookFilePath) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deletePerson(Person target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredPersonList(Predicate<Person> predicate) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single person.
     */
    private class ModelStubWithPerson extends ModelStub {
        private final Person person;

        ModelStubWithPerson(Person person) {
            requireNonNull(person);
            this.person = person;
        }

        @Override
        public boolean hasPerson(Person person) {
            requireNonNull(person);
            return this.person.isSamePerson(person);
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return FXCollections.observableArrayList(person);
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            requireNonNull(target);
            requireNonNull(editedPerson);
        }
    }

    /**
     * A Model stub that accepts tagging to persons.
     */
    private class ModelStubAcceptingPersonTagged extends ModelStub {
        final List<Person> personsModified = new ArrayList<>();

        ModelStubAcceptingPersonTagged(Person... persons) {
            for (Person p : persons) {
                personsModified.add(p);
            }
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return FXCollections.observableArrayList(personsModified);
        }

        @Override
        public void setPerson(Person target, Person editedPerson) {
            requireNonNull(target);
            requireNonNull(editedPerson);
            personsModified.remove(target);
            personsModified.add(editedPerson);
        }
    }

}
