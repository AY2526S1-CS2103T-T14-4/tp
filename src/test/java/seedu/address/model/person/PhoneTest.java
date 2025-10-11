package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class PhoneTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Phone(null));
    }

    @Test
    public void constructor_invalidPhone_throwsIllegalArgumentException() {
        String invalidPhone = "";
        assertThrows(IllegalArgumentException.class, () -> new Phone(invalidPhone));
    }

    @Test
    public void isValidPhone() {
        // null phone number
        assertThrows(NullPointerException.class, () -> Phone.isValidPhone(null));

        // invalid phone numbers
        assertFalse(Phone.isValidPhone("")); // empty string
        assertFalse(Phone.isValidPhone(" ")); // spaces only
        assertFalse(Phone.isValidPhone("91")); // less than 3 numbers
        assertFalse(Phone.isValidPhone("phone")); // non-numeric
        assertFalse(Phone.isValidPhone("9011p041")); // alphabets within digits
        assertFalse(Phone.isValidPhone("9312 1534")); // spaces within digits
        assertFalse(Phone.isValidPhone("124293842033123")); // long phone numbers
        assertFalse(Phone.isValidPhone("911")); // exactly 3 numbers

        assertFalse(Phone.isValidPhone("51234567")); // wrong starting digit (5)
        assertFalse(Phone.isValidPhone("71234567")); // wrong starting digit (7)
        assertFalse(Phone.isValidPhone("01234567")); // wrong starting digit (0)
        assertFalse(Phone.isValidPhone("6123456"));  // 7 digits (too short)
        assertFalse(Phone.isValidPhone("612345678")); // 9 digits (too long)

        // valid phone numbers (exactly 8 digits; start with 9/8/6)
        assertTrue(Phone.isValidPhone("93121534")); // starts with 9
        assertTrue(Phone.isValidPhone("81234123")); // starts with 8
        assertTrue(Phone.isValidPhone("61234123")); // starts with 6

        // boundary-valids: minimal patterns with correct first digit
        assertTrue(Phone.isValidPhone("90000000"));
        assertTrue(Phone.isValidPhone("80000000"));
        assertTrue(Phone.isValidPhone("60000000"));
    }

    @Test
    public void equals() {
        Phone phone = new Phone("99999999");

        // same values -> returns true
        assertTrue(phone.equals(new Phone("99999999")));

        // same object -> returns true
        assertTrue(phone.equals(phone));

        // null -> returns false
        assertFalse(phone.equals(null));

        // different types -> returns false
        assertFalse(phone.equals(5.0f));

        // different values -> returns false
        assertFalse(phone.equals(new Phone("99999998")));
    }
    
}
