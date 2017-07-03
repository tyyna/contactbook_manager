package fi.muni.cz.contacts.test;


import fi.muni.cz.contacts.ContactInformation;
import fi.muni.cz.contacts.PersonBuilder;
import fi.muni.cz.contacts.DBUtils;
import fi.muni.cz.contacts.InvalidFieldException;
import fi.muni.cz.contacts.ContactManager;
import fi.muni.cz.contacts.SimpleFilterBuilder;
import fi.muni.cz.contacts.Person;
import fi.muni.cz.contacts.ContactManagerImpl;
import org.junit.*;

import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.derby.jdbc.EmbeddedDataSource;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

/**
 * Created by vbendel on 3/14/17.
 */
public class ContactManagerImplTest {

    private ContactManager contactManager;
    private DataSource ds;
    private DataSource failingDataSource;

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

    private static DataSource prepareDataSource() throws SQLException {
        EmbeddedDataSource ds = new EmbeddedDataSource();
        ds.setDatabaseName("memory:contactbook-test");
        ds.setCreateDatabase("create");
        return ds;
    }

    @Before
    public void setUp() throws SQLException {
        ds = prepareDataSource();
        //ContactManager.class.getResource("createTables.sql");
        DBUtils.executeSqlScript(ds, ContactManager.class.getResource("createTables.sql"));

        contactManager = new ContactManagerImpl(ds);
        //contactManager.initializePersonsCache();
        failingDataSource = mock(DataSource.class);
        when(failingDataSource.getConnection()).thenThrow(new SQLException());

    }

    @After
    public void destroy() throws SQLException {
        DBUtils.executeSqlScript(ds, ContactManager.class.getResource("dropTables.sql"));
    }

    //---------------------------------------------------------------

    @Test
    public void addPerson() throws SQLException {
        Person person = sampleTeenaPersonBuilder().build();
        contactManager.addPerson(person);

        int personID = person.getID();
        assertThat(personID).isNotNull();

        assertThat(contactManager.selectPerson(personID).getPerson())
                .isNotSameAs(person)
                .isEqualToComparingFieldByField(person);
    }

    @Test
    public void addPersonNull() {
        assertThatThrownBy(() -> contactManager.addPerson(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void addPersonNullName() {
        final Person person = sampleTeenaPersonBuilder().name(null).build();

        assertThatThrownBy(() -> contactManager.addPerson(person))
                .isInstanceOf(InvalidFieldException.class);
    }

    @Test
    public void addPersonNullSurname() {
        final Person person = sampleTeenaPersonBuilder().surname(null).build();

        assertThatThrownBy(() -> contactManager.addPerson(person))
                .isInstanceOf(InvalidFieldException.class);
    }


    @Test
    public void removePerson() throws SQLException {
        Person teena = sampleTeenaPersonBuilder().build();
        Person vrato = sampleVratoPersonBuilder().build();
        contactManager.addPerson(teena);
        contactManager.addPerson(vrato);

        assertThat(contactManager.removePerson(teena)).isTrue();

        assertThat(contactManager.getAllPersons())
                .containsOnly(vrato);
    }

    @Test
    public void removePersonNull() {
        assertThatThrownBy(() -> contactManager.removePerson(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void removePersonNullId() throws SQLException {
        Person person = sampleTeenaPersonBuilder().id(null).build();
        assertThatThrownBy(() -> contactManager.removePerson(person))
                .isInstanceOf(InvalidFieldException.class);
    }

    @Test
    public void removePersonNotInCollection() throws SQLException {
        Person teena = sampleTeenaPersonBuilder().build();
        contactManager.addPerson(teena);

        Person vrato = sampleVratoPersonBuilder().id(teena.getID()+5).build();


        assertThat(contactManager.removePerson(vrato)).isFalse();

        assertThat(contactManager.getAllPersons())
                .containsOnly(teena);
    }

    @Test
    public void getAllPersons() throws SQLException {
        assertThat(contactManager.getAllPersons())
                .isEmpty();

        Person teena = sampleTeenaPersonBuilder().build();
        Person vrato = sampleVratoPersonBuilder().build();

        contactManager.addPerson(teena);
        contactManager.addPerson(vrato);

        assertThat(contactManager.getAllPersons())
                .containsExactly(teena, vrato);
    }

    @Test
    public void selectPerson() throws SQLException {
        Person vrato = sampleVratoPersonBuilder().build();
        Person teena = sampleTeenaPersonBuilder().build();

        contactManager.addPerson(vrato);
        contactManager.addPerson(teena);

        ContactInformation info = contactManager.selectPerson(vrato.getID());
        assertThat(info.getPerson()).isEqualToComparingFieldByField(vrato);

        info = contactManager.selectPerson(teena.getID());
        assertThat(info.getPerson()).isEqualToComparingFieldByField(teena);
    }
    @Test
    public void selectPersonNullId() {
        Person person = sampleTeenaPersonBuilder().id(null).build();
        assertThatThrownBy(() -> contactManager.selectPerson(person.getID()))
                .isInstanceOf(IllegalArgumentException.class);
    }
    /*
    @Test
    public void selectPersonNull() {
        assertThatThrownBy(() -> contactManager.selectPerson(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
    */
    @Test
    public void selectPersonNotInCollection() throws SQLException {
        Person person = sampleTeenaPersonBuilder().build();
        contactManager.addPerson(person);

        assertThatThrownBy(() -> contactManager.selectPerson(person.getID() + 1))
                .isInstanceOf(IllegalArgumentException.class);
    }
/*
    @Test
    public void flushData() {
        Person vrato = sampleVratoPersonBuilder().build();
        contactManager.addPerson(vrato);

        //Prepare ContactInformationMock
        ContactInformation info = mock(ContactInformation.class);
        when(info.getPerson()).thenReturn(vrato);
        when(info.getAddress()).thenReturn(new AddressBuilder()
                .street("Sipova").number("3C").city("Bratislava").build());


        List<Email> emails = new ArrayList<>();
        emails.add(new EmailBuilder().name("bedo282").domain("gmail.com").build());
        when(info.getEmails()).thenReturn(emails);

        List<Phone> phones = new ArrayList<>();
        phones.add(new PhoneBuilder().number(123456789).build());
        when(info.getPhones()).thenReturn(phones);

        assertThat(contactManager.flushData(info)).isTrue();

        //Bude toto fungovat?
        assertThat(contactManager.selectPerson(vrato.getID()))
                .isEqualToComparingFieldByField(info);

        //Ak nebude fungovat hento, tak pouzijeme tieto asserty:
        ContactInformation actual = contactManager.selectPerson(vrato.getID());
        assertThat(actual.getPerson()).isEqualToComparingFieldByField(info.getPerson());
        assertThat(actual.getAddress()).isEqualToComparingFieldByField(info.getAddress());
        assertThat(actual.getEmails()).containsOnlyElementsOf(info.getEmails());
        assertThat(actual.getPhones()).containsOnlyElementsOf(info.getPhones());
    }
*/
    /**
     * SQLException testing
     */

    @Test
    public void addPersonWithSqlExceptionThrown() throws SQLException {
        contactManager.setDataSource(failingDataSource);
        assertThatThrownBy(() -> contactManager.addPerson(sampleTeenaPersonBuilder().build()))
                .isInstanceOf(SQLException.class);
    }
    @Test
    public void removePersonWithSqlExceptionThrown() throws SQLException {
        contactManager.setDataSource(failingDataSource);
        assertThatThrownBy(() -> contactManager.removePerson(sampleTeenaPersonBuilder().id(3).build()))
                .isInstanceOf(SQLException.class);
    }
    @Test
    public void selectPersonWithSqlExceptionThrown() throws SQLException {
        contactManager.setDataSource(failingDataSource);
        assertThatThrownBy(() -> contactManager.selectPerson(42))
                .isInstanceOf(SQLException.class);
    }
 /*   @Test
    public void getAllPersonsWithSqlExceptionThrown() throws SQLException {
        contactManager.setDataSource(failingDataSource);
        assertThatThrownBy(() -> contactManager.getAllPersons())
                .isInstanceOf(SQLException.class);
    }*/
    @Test
    public void applyFilterPersonWithSqlExceptionThrown() throws SQLException {
        contactManager.setDataSource(failingDataSource);
        assertThatThrownBy(() -> contactManager.applyFilter(new SimpleFilterBuilder().name("neco").build()))
                .isInstanceOf(SQLException.class);
    }
}
