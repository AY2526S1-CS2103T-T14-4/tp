package seedu.address.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Remark;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static final Remark EMPTY_REMARK = new Remark("");

    public static Person[] getSamplePersons() {
        return new Person[]{
            new Person(new Name("Alex Yeoh"), new Phone("87438807"), new Email("alexyeoh@example.com"),
                    new Address("Blk 30 Geylang Street 29, #06-40"), EMPTY_REMARK, getTagSet("friends")),
            new Person(new Name("Bernice Yu"), new Phone("99272758"), new Email("berniceyu@example.com"),
                    new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"), EMPTY_REMARK,
                    getTagSet("colleagues", "friends")),
            new Person(new Name("Charlotte Oliveiro"), new Phone("93210283"), new Email("charlotte@example.com"),
                    new Address("Blk 11 Ang Mo Kio Street 74, #11-04"), EMPTY_REMARK,
                    getTagSet("neighbours")),
            new Person(new Name("David Li"), new Phone("91031282"), new Email("lidavid@example.com"),
                    new Address("Blk 436 Serangoon Gardens Street 26, #16-43"), EMPTY_REMARK,
                    getTagSet("family")),
            new Person(new Name("Irfan Ibrahim"), new Phone("92492021"), new Email("irfan@example.com"),
                    new Address("Blk 47 Tampines Street 20, #17-35"), EMPTY_REMARK,
                    getTagSet("classmates")),
            new Person(new Name("Roy Balakrishnan"), new Phone("92624417"), new Email("royb@example.com"),
                    new Address("Blk 45 Aljunied Street 85, #11-31"), EMPTY_REMARK,
                    getTagSet("colleagues")),
            new Person(new Name("Wong Tai Man"), new Phone("63396638"), new Email("taimanwong@gmail.com"),
                    new Address("Blk 222 Clementi Avenue 4, #08-111"),
                    new Remark("Regular checkups needed for blood pressure monitoring."),
                    getTagSet("hypertension", "weekly_monitoring", "lives_alone", "elderly", "needs_groceries")),
            new Person(new Name("Kenneth Chew"), new Phone("63396638"), new Email(""),
                    new Address("Blk 222 Clementi Avenue 4, #08-111"),
                    new Remark("Lives with spouse"),
                    getTagSet("needs_groceries")),
            new Person(new Name("Candice Loh"), new Phone("83338455"), new Email("ilovekpop123@gmail.com"),
                    new Address("Blk 95 Whampoa Drive, #02-246"),
                    new Remark("Loves morning walks. Prefers low-sodium meals."),
                    getTagSet("healthy")),
            new Person(new Name("Christopher Lim"), new Phone("87654321"), new Email("limlimchris@gmail.com"),
                    new Address("401 HAVELOCK ROAD, #04-02"),
                    new Remark("Is aggressive to strangers, shouted at when prompted for information."),
                    getTagSet("aggressive")),
            new Person(new Name("Jordan Michael"), new Phone("96569930"), new Email("iamthebestballer@gmail.com"),
                    new Address("11 Tuas Bay Wlk #03-00"),
                    new Remark(""),
                    getTagSet("Friendly")),
            new Person(new Name("James Smith"), new Phone("92230136"), new Email("jamesbondwannabe@gmail.com"),
                    new Address("261 Yishun St 22 #01-137"),
                    new Remark(""),
                    getTagSet("diabetic")),
            new Person(new Name("Shermaine Tan"), new Phone("92846581"), new Email("shersher123@gmail.com"),
                    new Address("Lor 2 Toa Payoh #07-36"),
                    new Remark("Partially blind since 23 years old."),
                    getTagSet("visually_impaired"))
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

}
