package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";
    private static final int MAX_NAME_LENGTH = 50;
    private static final int MAX_ADDRESS_LENGTH = 50;
    private static final int MAX_EMAIL_LENGTH = 50;
    private static final int MAX_TAG_LENGTH = 30;
    private static final int MAX_REMARK_LENGTH = 50;
    private static final int BEGINNING_INDEX = 0;

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;
    @FXML
    private Label remark;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        assert person != null : "Person should not be null";

        id.setText(displayedIndex + "");
        name.setText(truncate(person.getName().fullName, MAX_NAME_LENGTH));
        phone.setText(person.getPhone().value);
        address.setText(truncate(person.getAddress().value, MAX_ADDRESS_LENGTH));
        email.setText(truncate(person.getEmail().value, MAX_EMAIL_LENGTH));
        remark.setText(truncate(person.getRemark().value, MAX_REMARK_LENGTH));
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(
                        truncate(tag.tagName, MAX_TAG_LENGTH))));

        if (!person.getTags().isEmpty()) {
            tags.setStyle("-fx-padding: 7 0 0 0;");
        }

        setupExpandableFields();
    }

    private void setupExpandableFields() {
        setupExpandableField(name, person.getName().fullName, MAX_NAME_LENGTH);
        setupExpandableField(address, person.getAddress().value, MAX_ADDRESS_LENGTH);
        setupExpandableField(email, person.getEmail().value, MAX_EMAIL_LENGTH);
        setupExpandableField(remark, person.getRemark().value, MAX_REMARK_LENGTH);
    }

    private void setupExpandableField(Label field, String fullText, int maxLength) {
        field.setOnMouseClicked(event -> {
            if (field.getText().endsWith("...")) {
                field.setText(fullText);
            } else {
                field.setText(truncate(fullText, maxLength));
            }
        });
        field.setWrapText(true);
    }

    private String truncate(String text, int maxLength) {
        assert maxLength >= 0 : "Max length should be non-negative";
        assert text != null : "Text should not be null";

        if (text.length() > maxLength) {
            return text.substring(BEGINNING_INDEX, maxLength) + "...";
        }
        return text;
    }
}
