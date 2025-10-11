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
          + "0. The email may be empty (\"\").\n"
          + "1. If provided, the entire email must be at most 50 characters long.\n"
          + "2. The local-part must contain only letters and digits (A–Z, a–z, 0–9) and be at least 1 character long.\n"
          + "3. The domain is composed of dot-separated labels containing only letters (A–Z, a–z).\n"
          + "The domain must:\n"
          + "    - end with a label of at least 2 letters followed by '.com'\n"
          + "    - have each domain label start and end with a letter\n"
          + "    - contain only letters in each label (no digits, underscores, hyphens, or other symbols)."
          + "3. The domain must be a single label of letters (A–Z, a–z) of length ≥ 2, followed by '.com' "
          + "(e.g., example.com). Subdomains like 'sales.example.com' are not allowed.";
    // ===== Regex parts (minimal changes, same overall structure) =====

    // Length limit lookahead for the non-empty alternative
    private static final String LENGTH_LIMIT_LOOKAHEAD = "(?=.{1,50}$)";

    // Local part: letters OR digits, at least 1
    private static final String ALPHANUMERIC_LOCAL = "[A-Za-z0-9]+";
    private static final String LOCAL_PART_REGEX = ALPHANUMERIC_LOCAL;
    
    // Domain: a single label of letters (at least 2), then ".com" (no subdomains)
    private static final String DOMAIN_REGEX = "[A-Za-z]{2,}\\.com";
    
    // Full validation: allow empty string OR enforce length and local@domain(.com)
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
        checkArgument(isValidEmail(email), MESSAGE_CONSTRAINTS);
        value = email;
    }

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

        // instanceof handles nulls
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
