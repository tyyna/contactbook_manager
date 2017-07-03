package fi.muni.cz.contacts.test;

import fi.muni.cz.contacts.PersonBuilder;
import fi.muni.cz.contacts.InvalidFieldException;
import fi.muni.cz.contacts.PersonImpl;
import fi.muni.cz.contacts.Person;
import org.junit.Before;
import org.junit.Test;
import static org.assertj.core.api.Assertions.*;

/**
 * Created by vbendel on 3/14/17.
 * //TODO teena
 */
public class PersonImplTest {

    private Person person;

    private PersonBuilder sampleTeenaPersonBuilder() {
        return new PersonBuilder()
                .name("Pristýna")
                .surname("Kokorná");
    }

    private PersonBuilder sampleVratoPersonBuilder() {
        return new PersonBuilder()
                .name("Bratislav")
                .surname("Vendel");
    }

    @Before
    public void setUp() {
        person = new PersonImpl();
    }

    @Test
    public void updatePersonValid() {
        Person newPerson = sampleTeenaPersonBuilder().build();
        person.updatePerson(newPerson);

        assertThat(person).isEqualToComparingFieldByField(newPerson);
    }

    @Test
    public void updatePersonNull() {
        assertThatThrownBy(() -> person.updatePerson(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void updatePersonNullName() {
        Person newPerson = sampleTeenaPersonBuilder().name(null).build();
        assertThatThrownBy(() -> person.updatePerson(newPerson))
                .isInstanceOf(InvalidFieldException.class);
    }

    @Test
    public void updatePersonNullSurname() {
        Person newPerson = sampleTeenaPersonBuilder().surname(null).build();
        assertThatThrownBy(() -> person.updatePerson(newPerson))
                .isInstanceOf(InvalidFieldException.class);
    }

    @Test
    public void updatePersonEmptyName() {
        Person newPerson = sampleTeenaPersonBuilder().name("").build();
        assertThatThrownBy(() -> person.updatePerson(newPerson))
                .isInstanceOf(InvalidFieldException.class);
    }

    @Test
    public void updatePersonEmptySurname() {
        Person newPerson = sampleTeenaPersonBuilder().surname("").build();
        assertThatThrownBy(() -> person.updatePerson(newPerson))
                .isInstanceOf(InvalidFieldException.class);
    }
}
