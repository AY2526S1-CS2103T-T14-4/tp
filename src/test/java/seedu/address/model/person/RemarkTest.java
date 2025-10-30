package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class RemarkTest {

    @Test
    public void equals() {
        Remark remark = new Remark("Hello");

        // same object -> returns true
        assertTrue(remark.equals(remark));

        // same values -> returns true
        Remark remarkCopy = new Remark(remark.value);
        assertTrue(remark.equals(remarkCopy));

        // different types -> returns false
        assertFalse(remark.equals(1));

        // null -> returns false
        assertFalse(remark.equals(null));

        // different remark -> returns false
        Remark differentRemark = new Remark("Bye");
        assertFalse(remark.equals(differentRemark));
    }

    @Test
    public void constructor_maxLength_success() {
        String max = "a".repeat(Remark.MAX_LENGTH);
        new Remark(max); // no exception
    }

    @Test
    public void constructor_overLimitThrowsIllegalArgumentException() {
        String over = "a".repeat(Remark.MAX_LENGTH + 1);
        assertThrows(IllegalArgumentException.class, () -> new Remark(over));
    }
}
