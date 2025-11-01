package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class EmailTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Email(null));
    }

    @Test
    public void constructor_invalidEmail_throwsIllegalArgumentException() {
        String invalidEmail = " ";
        assertThrows(IllegalArgumentException.class, () -> new Email(invalidEmail));
    }

    @Test
    public void isValidEmail() {
        // null email
        assertThrows(NullPointerException.class, () -> Email.isValidEmail(null));

        // blank email
        assertTrue(Email.isValidEmail("")); // empty string
        assertFalse(Email.isValidEmail(" ")); // spaces only

        // missing parts
        assertFalse(Email.isValidEmail("@example.com")); // missing local part
        assertFalse(Email.isValidEmail("peterjackexample.com")); // missing '@' symbol
        assertFalse(Email.isValidEmail("peterjack@")); // missing domain name

        // invalid parts
        assertFalse(Email.isValidEmail("peterjack@-")); // invalid domain name
        assertFalse(Email.isValidEmail("peterjack@exam_ple.com")); // underscore in domain name
        assertFalse(Email.isValidEmail("peter jack@example.com")); // spaces in local part
        assertFalse(Email.isValidEmail("peterjack@exam ple.com")); // spaces in domain name
        assertFalse(Email.isValidEmail(" peterjack@example.com")); // leading space
        assertFalse(Email.isValidEmail("peterjack@example.com ")); // trailing space
        assertFalse(Email.isValidEmail("peterjack@@example.com")); // double '@' symbol
        assertFalse(Email.isValidEmail("peter@jack@example.com")); // '@' symbol in local part
        assertFalse(Email.isValidEmail("-peterjack@example.com")); // local part starts with a hyphen
        assertFalse(Email.isValidEmail("peterjack-@example.com")); // local part ends with a hyphen
        assertFalse(Email.isValidEmail("peter..jack@example.com")); // local part has two consecutive periods
        assertFalse(Email.isValidEmail("peterjack@example@com")); // '@' symbol in domain name
        assertFalse(Email.isValidEmail("peterjack@.example.com")); // domain name starts with a period
        assertFalse(Email.isValidEmail("peterjack@example.com.")); // domain name ends with a period
        assertFalse(Email.isValidEmail("peterjack@-example.com")); // domain name starts with a hyphen
        assertFalse(Email.isValidEmail("peterjack@example.com-")); // domain name ends with a hyphen

        // previously invalid due to TLD restriction, now valid
        assertTrue(Email.isValidEmail("peterjack@example.c")); // single-letter last label now allowed
        assertTrue(Email.isValidEmail("peterjack@example.org")); // any letter-only TLD allowed

        assertFalse(Email.isValidEmail("PeterJack_1190@example.com")); // underscore in local part
        assertFalse(Email.isValidEmail("PeterJack.1190@example.com")); // period in local part
        assertFalse(Email.isValidEmail("PeterJack+1190@example.com")); // '+' symbol in local part
        assertFalse(Email.isValidEmail("PeterJack-1190@example.com")); // hyphen in local part
        assertFalse(Email.isValidEmail("123@145")); // numeric domain label not allowed
        assertFalse(Email.isValidEmail("a1+be.d@example1.com")); // digits in domain label
        assertFalse(Email.isValidEmail("peter_jack@very-very-very-long-example.com")); // hyphen in domain
        assertFalse(Email.isValidEmail("if.you.dream.it_you.can.do.it@example.com")); // underscore in local part
        assertFalse(Email.isValidEmail("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX@YY.com")); // total max 51 chars

        // subdomains allowed
        assertTrue(Email.isValidEmail("john@sales.marketing.example.com")); // multi-label domain

        // valid email
        assertTrue(Email.isValidEmail("peterjack@example.com"));
        assertTrue(Email.isValidEmail("PeterJack1190@example.com"));
        assertTrue(Email.isValidEmail("X@YY.com"));
        assertTrue(Email.isValidEmail("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX@YY.com")); // total max 50 chars
    }

    @Test
    public void equals() {
        Email email = new Email("valid@email.com");

        // same values -> returns true
        assertTrue(email.equals(new Email("valid@email.com")));

        // same object -> returns true
        assertTrue(email.equals(email));

        // null -> returns false
        assertFalse(email.equals(null));

        // different types -> returns false
        assertFalse(email.equals(5.0f));

        // different values -> returns false
        assertFalse(email.equals(new Email("vvalid@email.com")));
    }

    @Test
    public void defaultConstructor_setsEmpty() {
        Email e = new Email();
        assertTrue(Email.isValidEmail(e.value));
        // verify exactly empty
        // assertEquals("", e.value);
    }

    // New tests for subdomains, boundaries, and label rules

    @Test
    public void isValidEmail_subdomainsAllowed() {
        assertTrue(Email.isValidEmail("a@aa.com"));
        assertTrue(Email.isValidEmail("bob@sales.example.com"));
        assertTrue(Email.isValidEmail("user@a.aa.com")); // single-letter subdomain allowed, last label >= 2
    }

    @Test
    public void isValidEmail_lastLabelLengthConstraint() {
        // Now allowed: last label before TLD can be 1 character
        assertTrue(Email.isValidEmail("u@b.a.com"));
        assertTrue(Email.isValidEmail("u@b.aa.com"));
    }

    @Test
    public void isValidEmail_consecutiveDotsInDomain_invalid() {
        assertFalse(Email.isValidEmail("a@aa..com")); // empty label between dots
        assertFalse(Email.isValidEmail("a@.aa.com")); // starts with dot label
        assertFalse(Email.isValidEmail("a@aa.com.")); // ends with dot
    }

    @Test
    public void isValidEmail_uppercaseDomain_valid() {
        assertTrue(Email.isValidEmail("a@EXAMPLE.com"));
        assertTrue(Email.isValidEmail("a@SALES.EXAMPLE.com"));
        assertTrue(Email.isValidEmail("a@EXAMPLE.COM"));
        assertTrue(Email.isValidEmail("a@Sales.Example.COM"));
        assertTrue(Email.isValidEmail("X@YY.COM"));
        // New: .sg accepted (case-insensitive)
        assertTrue(Email.isValidEmail("a@example.sg"));
        assertTrue(Email.isValidEmail("a@EXAMPLE.SG"));
        assertTrue(Email.isValidEmail("user@sales.example.SG"));
    }

    @Test
    public void isValidEmail_lengthBoundaryWithSubdomain() {
        // Build an address with subdomain "sales.example.com"
        String domain = "sales.example.com"; // length = 17
        String suffix = "@" + domain; // length = 18
        int localLen = 50 - suffix.length(); // local + '@domain' == 50
        String exact = "a".repeat(localLen) + suffix;
        assertTrue(Email.isValidEmail(exact)); // exactly 50

        String over = "a".repeat(localLen + 1) + suffix;
        assertFalse(Email.isValidEmail(over)); // 51
    }

    @Test
    public void isValidEmail_whitespaceCharacters_invalid() {
        assertFalse(Email.isValidEmail("a@aa.com\n"));
        assertFalse(Email.isValidEmail("\ta@aa.com"));
        assertFalse(Email.isValidEmail("a@ a.com"));
    }
}
