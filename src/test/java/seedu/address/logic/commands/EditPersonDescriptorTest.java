package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.model.person.Name;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.EditPersonDescriptorBuilder;

public class EditPersonDescriptorTest {

    @Test
    public void equals() {
        // same values -> returns true
        EditPersonDescriptor descriptorWithSameValues = new EditPersonDescriptor(DESC_AMY);
        assertTrue(DESC_AMY.equals(descriptorWithSameValues));

        // same object -> returns true
        assertTrue(DESC_AMY.equals(DESC_AMY));

        // null -> returns false
        assertFalse(DESC_AMY.equals(null));

        // different types -> returns false
        assertFalse(DESC_AMY.equals(5));

        // different values -> returns false
        assertFalse(DESC_AMY.equals(DESC_BOB));

        // different name -> returns false
        EditPersonDescriptor editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withName(VALID_NAME_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different phone -> returns false
        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withPhone(VALID_PHONE_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different email -> returns false
        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withEmail(VALID_EMAIL_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different address -> returns false
        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));

        // different tags -> returns false
        editedAmy = new EditPersonDescriptorBuilder(DESC_AMY).withTags(VALID_TAG_HUSBAND).build();
        assertFalse(DESC_AMY.equals(editedAmy));
    }

    @Test
    public void toStringMethod() {
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        String expected = EditPersonDescriptor.class.getCanonicalName() + "{name="
                + editPersonDescriptor.getName().orElse(null) + ", phone="
                + editPersonDescriptor.getPhone().orElse(null) + ", email="
                + editPersonDescriptor.getEmail().orElse(null) + ", address="
                + editPersonDescriptor.getAddress().orElse(null) + ", tags="
                + editPersonDescriptor.getTags().orElse(null) + "}";
        assertEquals(expected, editPersonDescriptor.toString());
    }

    @Test
    public void isAnyFieldEdited_empty_false() {
        EditCommand.EditPersonDescriptor d = new EditCommand.EditPersonDescriptor();
        assertFalse(d.isAnyFieldEdited());
    }

    @Test
    public void isAnyFieldEdited_name_true() {
        EditCommand.EditPersonDescriptor d = new EditCommand.EditPersonDescriptor();
        d.setName(new Name("Alice"));
        assertTrue(d.isAnyFieldEdited());
    }

    @Test
    public void isAnyFieldEdited_phone_true() {
        EditCommand.EditPersonDescriptor d = new EditCommand.EditPersonDescriptor();
        d.setPhone(new Phone("91234567"));
        assertTrue(d.isAnyFieldEdited());
    }

    @Test
    public void isAnyFieldEdited_address_true() {
        EditCommand.EditPersonDescriptor d = new EditCommand.EditPersonDescriptor();
        d.setAddress(new Address("Blk 123, Some St"));
        assertTrue(d.isAnyFieldEdited());
    }

    @Test
    public void isAnyFieldEdited_tags_true() {
        EditCommand.EditPersonDescriptor d = new EditCommand.EditPersonDescriptor();
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("friend"));
        d.setTags(tags);
        assertTrue(d.isAnyFieldEdited());
    }

    @Test
    public void setTags_nullThenEmptyGet_optionalEmptyAndThenPresent() {
        EditCommand.EditPersonDescriptor d = new EditCommand.EditPersonDescriptor();

        // Initially optional is empty
        assertFalse(d.getTags().isPresent());

        // Set to empty set (simulates reset)
        d.setTags(Collections.emptySet());
        assertTrue(d.getTags().isPresent());
        assertTrue(d.getTags().get().isEmpty());
    }

    @Test
    public void defensiveCopy_tagsImmutableFromOutside() {
        EditCommand.EditPersonDescriptor d = new EditCommand.EditPersonDescriptor();
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("x"));
        d.setTags(tags);

        // Mutate original set should not affect descriptor (defensive copy)
        tags.add(new Tag("y"));
        assertTrue(d.getTags().isPresent());
        assertTrue(d.getTags().get().stream().anyMatch(t -> t.tagName.equals("x")));
        assertFalse(d.getTags().get().stream().anyMatch(t -> t.tagName.equals("y")));
    }
}
