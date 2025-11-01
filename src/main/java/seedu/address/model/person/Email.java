package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's email in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidEmail(String)}
 */
public class Email {

    public static final String MESSAGE_CONSTRAINTS =
            "Emails should be of the format local-part@domain and adhere to the following constraints:\n"
          + "1. The email may be empty (\"\").\n"
          + "2. If provided, the entire email must be at most 50 characters long.\n"
          + "3. The local-part must contain only letters and digits (A–Z, a–z, 0–9) and be at least 1 character long.\n"
          + "4. The domain may be composed of dot-separated labels containing only letters (A–Z, a–z).\n"
          + "The domain must:\n"
          + "    - end with a label of at least 1 letter\n"
          + "    - have each domain label start and end with a letter\n"
          + "    - contain only letters in each label (no digits, underscores, hyphens, or other symbols).";

    // Length limit lookahead for the non-empty alternative
    private static final String LENGTH_LIMIT_LOOKAHEAD = "(?=.{1,50}$)";

    // Local part: letters OR digits, at least 1
    private static final String ALPHANUMERIC_LOCAL = "[A-Za-z0-9]+";
    private static final String LOCAL_PART_REGEX = ALPHANUMERIC_LOCAL;

    private static final String DOMAIN_REGEX = "(?:[A-Za-z]+\\.)*[A-Za-z]+";

    public static final String VALIDATION_REGEX =
            "^(?:$|" + LENGTH_LIMIT_LOOKAHEAD + LOCAL_PART_REGEX + "@" + DOMAIN_REGEX + ")$";

    public final String value;

    /**
     * Constructs an {@code Email}.
     *
     * @param email A valid email address.
     */
    public Email(String email) {
        requireNonNull(email);
        checkArgument(isValidEmail(email.toLowerCase()), MESSAGE_CONSTRAINTS);
        value = email.toLowerCase();
    }

    /**
     * Constructs an empty {@code Email}.
     * This represents a person with no email address specified.
     */
    public Email() {
        value = "";
    }

    /**
     * Returns if a given string is a valid email.
     */
    public static boolean isValidEmail(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Email)) {
            return false;
        }

        Email otherEmail = (Email) other;
        return value.equals(otherEmail.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
