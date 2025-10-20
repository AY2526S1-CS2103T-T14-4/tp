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

        address.setOnMouseClicked(event -> {
            if (address.getText().endsWith("...")) {
                address.setText(person.getAddress().value);
            } else {
                address.setText(truncate(person.getAddress().value, MAX_ADDRESS_LENGTH));
            }
        });
        address.setWrapText(true);

        email.setOnMouseClicked(event -> {
            if (email.getText().endsWith("...")) {
                email.setText(person.getEmail().value);
            } else {
                email.setText(truncate(person.getEmail().value, MAX_EMAIL_LENGTH));
            }
        });
        email.setWrapText(true);
    }

    private String truncate(String text, int maxLength) {
        if (text.length() > maxLength) {
            return text.substring(BEGINNING_INDEX, maxLength) + "...";
        }
        return text;
    }
}
