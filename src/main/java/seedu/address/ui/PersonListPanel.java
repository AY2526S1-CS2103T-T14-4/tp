package seedu.address.ui;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import seedu.address.model.person.Person;

/**
 * Panel containing the list of persons.
 */
public class PersonListPanel extends UiPart<Region> {
    private static final String FXML = "PersonListPanel.fxml";

    @FXML
    private FlowPane cardContainer;

    /**
     * Creates a {@code PersonListPanel} with the given {@code ObservableList}.
     */
    public PersonListPanel(ObservableList<Person> personList) {
        super(FXML);
        populateFlow(personList);

        // Add listener to update the FlowPane when the personList changes
        personList.addListener((ListChangeListener<Person>) change -> {
            while (change.next()) {
                populateFlow(personList);
            }
        });
    }

    /**
     * Populates the FlowPane with Person cards.
     */
    private void populateFlow(ObservableList<Person> personList) {
        cardContainer.getChildren().clear();

        for (int i = 0; i < personList.size(); i++) {
            Person person = personList.get(i);
            PersonCard personCard = new PersonCard(person, i + 1);
            cardContainer.getChildren().add(personCard.getRoot());
        }
    }
}
