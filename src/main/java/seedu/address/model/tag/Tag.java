package seedu.address.model.tag;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Tag in the address book.
 * Guarantees: immutable
 */
public class Tag {

    public static final String MESSAGE_CONSTRAINTS = "Tags cannot be empty (i.e. whitespace only).\n"
            + "Tags cannot be longer than 30 characters.";
    //public static final String VALIDATION_REGEX = "\\p{Alnum}+";

    public final String tagName;

    /**
     * Constructs a {@code Tag}.
     *
     * @param tagName A valid tag name.
     */
    public Tag(String tagName) {
        requireNonNull(tagName);
        this.tagName = tagName.trim().toLowerCase(); //keeps all tags as lowercase
    }

    public static boolean isValidTagContent(String tagName) {
        requireNonNull(tagName);
        String trimmed = tagName.trim();
        if (trimmed.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isValidTagLength(String tagName) {
        requireNonNull(tagName);
        String trimmed = tagName.trim();
        if (trimmed.length() > 30) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Tag)) {
            return false;
        }

        Tag otherTag = (Tag) other;
        return tagName.equals(otherTag.tagName);
    }

    @Override
    public int hashCode() {
        return tagName.hashCode();
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return '[' + tagName + ']';
    }

}
