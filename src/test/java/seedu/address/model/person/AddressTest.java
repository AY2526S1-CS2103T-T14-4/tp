package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class AddressTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Address(null));
    }

    @Test
    public void constructor_invalidBlank_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Address(""));
        assertThrows(IllegalArgumentException.class, () -> new Address("   "));
    }

    @Test
    public void constructor_overLimit_throwsIllegalArgumentException() {
        String over = "X".repeat(Address.MAX_LENGTH + 1);
        assertThrows(IllegalArgumentException.class, () -> new Address(over));
    }

    @Test
    public void constructor_exactLimit_success() {
        String exact = "Y".repeat(Address.MAX_LENGTH);
        new Address(exact); // no exception
    }

    @Test
    public void isValidAddress() {
        // null -> NPE
        assertThrows(NullPointerException.class, () -> Address.isValidAddress(null));

        // invalid
        assertFalse(Address.isValidAddress("")); // empty
        assertFalse(Address.isValidAddress("   ")); // spaces only
        assertFalse(Address.isValidAddress("Z".repeat(Address.MAX_LENGTH + 1))); // too long

        // valid
        assertTrue(Address.isValidAddress("Blk 41 Telok Blangah Way #07-436"));
        assertTrue(Address.isValidAddress("A".repeat(Address.MAX_LENGTH))); // boundary
    }

    @Test
    public void equals() {
        Address address = new Address("Valid Address");

        // same values -> returns true
        assertTrue(address.equals(new Address("Valid Address")));

        // same object -> returns true
        assertTrue(address.equals(address));

        // null -> returns false
        assertFalse(address.equals(null));

        // different types -> returns false
        assertFalse(address.equals(5.0f));

        // different values -> returns false
        assertFalse(address.equals(new Address("Other Valid Address")));
    }
}
