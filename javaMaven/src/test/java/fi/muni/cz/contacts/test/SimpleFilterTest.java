package fi.muni.cz.contacts.test;

import fi.muni.cz.contacts.Email;
import fi.muni.cz.contacts.Phone;
import fi.muni.cz.contacts.AddressBuilder;
import fi.muni.cz.contacts.ContactManager;
import fi.muni.cz.contacts.SimpleFilter;
import fi.muni.cz.contacts.Address;
import fi.muni.cz.contacts.EmailBuilder;
import fi.muni.cz.contacts.PersonBuilder;
import fi.muni.cz.contacts.DBUtils;
import fi.muni.cz.contacts.InvalidFieldException;
import fi.muni.cz.contacts.ThisShouldNeverHappenException;
import fi.muni.cz.contacts.SimpleFilterBuilder;
import fi.muni.cz.contacts.ContactManagerImpl;
import fi.muni.cz.contacts.Person;
import fi.muni.cz.contacts.PhoneBuilder;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Vratislav Bendel
 * @version 3/27/17
 */
public class SimpleFilterTest {
    private ContactManager contactManager;
    private DataSource ds;

    static Person vratoB = samplePerson().name("Vrato").surname("Bendel").build();
    static Person pokornaT = samplePerson().name("Kristyna").surname("Pokorna").build();
    static Person vrato = samplePerson().name("Vrato").build();
    static Person pokorna = samplePerson().surname("Pokorna").build();

    private static PersonBuilder samplePerson() {
        return new PersonBuilder()
                .name("meno")
                .surname("priezvisko");
    }

    private AddressBuilder sampleAddress() {
        return new AddressBuilder()
                .street("ulica")
                .number("42")
                .city("mesto");
    }

    private EmailBuilder sampleEmail() {
        return new EmailBuilder()
                .name("name")
                .domain("domain.com");
    }

    private PhoneBuilder samplePhone() {
        return new PhoneBuilder().number(123456789);
    }


    /**
     * ############# SetUp #############
     */

    private static Collection<Person> mockAllPersons() {
        List<Person> all = new LinkedList<>();
        all.add(vrato);
        all.add(vratoB);
        all.add(pokorna);
        all.add(pokornaT);
        return Collections.unmodifiableCollection(all);
    }

    private static DataSource prepareDataSource() throws SQLException {
        EmbeddedDataSource ds = new EmbeddedDataSource();
        ds.setDatabaseName("memory:contactbook-test");
        ds.setCreateDatabase("create");
        return ds;
    }

    private void prepareDatabaseForTests() throws SQLException {

        Address sipova = sampleAddress().street("sipova").build();
        Address sipova3 = sampleAddress().street("sipova").number("3").build();
        Address brno = sampleAddress().city("brno").build();

        Email email1 = sampleEmail().name("bedo").domain("gmail.com").build();
        Email email2 = sampleEmail().name("teena").domain("seznam.cz").build();

        Phone phone1 = samplePhone().number(111222333).build();
        Phone phone2 = samplePhone().number(999888777).build();

        //SETUP
        try {


            //Vrato; sipova; bedo@gmail.com
            contactManager.addPerson(vrato);
            contactManager.selectPerson(vrato.getID()).updateAddress(sipova);
            contactManager.selectPerson(vrato.getID()).addEmail(email1);

            //Vrato Bendel; sipova 3; 111222333
            contactManager.addPerson(vratoB);
            contactManager.selectPerson(vratoB.getID()).updateAddress(sipova3);
            contactManager.selectPerson(vratoB.getID()).addPhone(phone1);

            //Pokorna; brno; teena@seznam.cz
            contactManager.addPerson(pokorna);
            contactManager.selectPerson(pokorna.getID()).updateAddress(brno);
            contactManager.selectPerson(pokorna.getID()).addEmail(email2);

            //Kristyna Pokorna; brno; 999888777
            contactManager.addPerson(pokornaT);
            contactManager.selectPerson(pokornaT.getID()).updateAddress(brno);
            contactManager.selectPerson(pokornaT.getID()).addPhone(phone2);
        }
        catch (SQLException ex) {
            System.err.print(ex.getMessage());
            throw ex;
        }
    }

    @Before
    public void setUp() throws SQLException {
        ds = prepareDataSource();

        DBUtils.executeSqlScript(ds, "/home/vbendel/School/java-project/sqlScripts/createTables.sql");

        contactManager = new ContactManagerImpl(ds);
        //contactManager.getAllPersons(); //initialize collection

        prepareDatabaseForTests();
    }

    @After
    public void destroy() throws SQLException {
        DBUtils.executeSqlScript(ds, "/home/vbendel/School/java-project/sqlScripts/dropTables.sql");
    }

    /**
     * ############ PERSON FILTER ##############
     */
/*
    @Test
    public void applyPersonFilterName() throws SQLException {
        SimpleFilter filter1 = new SimpleFilterBuilder()
                .name("Kristyna").build();

            assertThat(contactManager.applyFilter(filter1)).containsExactly(pokornaT);
    }

    @Test
    public void applyPersonFilterSurname() throws SQLException {
        SimpleFilter filter2 = new SimpleFilterBuilder().surname("Bendel").build();
            assertThat(contactManager.applyFilter(filter2)).containsExactly(vratoB);
    }

    @Test
    public void applyPersonFilterNameAndSurname() throws SQLException {
        SimpleFilter filter3 = new SimpleFilterBuilder().name("Vrato").surname("Bendel").build();

            assertThat(contactManager.applyFilter(filter3)).containsExactly(vratoB);
    }

    /**
     * ############## ADDRESS FILTER ###################
     */
/*
    @Test
    public void applyAddressFilterStreet() throws SQLException {
        SimpleFilter filter = new SimpleFilterBuilder().street("sipova").build();

            assertThat(contactManager.applyFilter(filter)).containsExactly(vratoB, vrato);
     }

    @Test
    public void applyAddressFilterStreetAndNumber() throws SQLException {
        SimpleFilter filter = new SimpleFilterBuilder().street("sipova").number("3").build();

            assertThat(contactManager.applyFilter(filter)).containsExactly(vratoB);
    }

    @Test
    public void applyAddressFilterNumberWithoutStreet() {
        SimpleFilterBuilder filter = new SimpleFilterBuilder().number("666");

        assertThatThrownBy(() -> contactManager.applyFilter(filter.build()))
                .isInstanceOf(InvalidFieldException.class);
    }

    @Test
    public void applyAddressFilterCity() throws SQLException {
        SimpleFilter filter = new SimpleFilterBuilder().city("brno").build();

            assertThat(contactManager.applyFilter(filter)).containsExactly(pokornaT, pokorna);
    }

    /**
     * ############## EMAIL FILTER ################
     */
/*
    @Test
    public void applyEmailFilter() throws SQLException {
        SimpleFilter filter = new SimpleFilterBuilder().email("bedo@gmail.com").build();

            assertThat(contactManager.applyFilter(filter)).containsExactly(vrato);
    }

    /**
     * ############## PHONE FILTER ################
     */
/*
    @Test
    public void applyPhoneFilter() throws SQLException {
        SimpleFilter filter = new SimpleFilterBuilder().phone(111222333).build();

            assertThat(contactManager.applyFilter(filter)).containsExactly(vratoB);
    }

    /**
     * ############# COMPLEX FILTERS ###############
     */
/*
    @Test
    public void applyComplexFilterSurnameAndStreet() throws SQLException {
        SimpleFilter filter = new SimpleFilterBuilder()
                .surname("Bendel")
                .street("sipova")
                .build();

            assertThat(contactManager.applyFilter(filter)).containsExactly(vratoB);
    }

    @Test
    public void applyComplexFilterNameAndCity() throws SQLException {
        SimpleFilter filter = new SimpleFilterBuilder()
                .name("Vrato")
                .city("brno")
                .build();

            assertThat(contactManager.applyFilter(filter)).isEmpty();
    }

    @Test
    public void applyComplexFilterEmailAndCity() throws SQLException {
        SimpleFilter filter = new SimpleFilterBuilder()
                .city("brno")
                .email("teena@seznam.cz")
                .build();

            assertThat(contactManager.applyFilter(filter)).containsExactly(pokorna);
    }

    /**
     * ############ SPECIAL CASES #################
     */
/*
    @Test
    public void applyEmptyFilter() throws ThisShouldNeverHappenException, SQLException {
        ContactManager allPersons = mock(ContactManager.class);
        try {
            when(allPersons.getAllPersons()).thenReturn(mockAllPersons());
        } catch (SQLException e) {
            throw new ThisShouldNeverHappenException("WTF?! A mock object thrown exception");
        }

            assertThat(contactManager.applyFilter(new SimpleFilterBuilder().build()))
                    .containsOnlyElementsOf(allPersons.getAllPersons());
    }

    @Test
    public void applyNullFilter() {
        assertThatThrownBy(() -> contactManager.applyFilter(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
    */
}
