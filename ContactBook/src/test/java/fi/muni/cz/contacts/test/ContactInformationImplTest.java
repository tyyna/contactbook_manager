package fi.muni.cz.contacts.test;


import fi.muni.cz.contacts.ContactInformation;
import fi.muni.cz.contacts.EmailBuilder;
import fi.muni.cz.contacts.PersonBuilder;
import fi.muni.cz.contacts.Email;
import fi.muni.cz.contacts.DBUtils;
import fi.muni.cz.contacts.Phone;
import fi.muni.cz.contacts.AddressBuilder;
import fi.muni.cz.contacts.ContactManager;
import fi.muni.cz.contacts.Person;
import fi.muni.cz.contacts.ContactManagerImpl;
import fi.muni.cz.contacts.Address;
import fi.muni.cz.contacts.PhoneBuilder;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.junit.*;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.*;


/**
 * @author vbendel
 * @version 3/14/17
 */
public class ContactInformationImplTest {

    ContactInformation contactInformation;
    DataSource ds;

//################## SAMPLES #########################

    private PersonBuilder samplePersonVrato() {
        return new PersonBuilder()
                .name("Vratislav")
                .surname("Bendel");
    }

    private PersonBuilder samplePersonTeena() {
        return new PersonBuilder()
                .name("Kristyna")
                .surname("Pokorna");
    }

    private AddressBuilder sampleAddressBotanicka() {
        return new AddressBuilder()
                .street("Botanicka")
                .number("68a")
                .city("Brno");
    }

    public AddressBuilder sampleAddressSipova() {
        return new AddressBuilder()
                .street("Sipova")
                .number("3c")
                .city("Bratislava");
    }

    private EmailBuilder sampleEmailVrato() {
        return new EmailBuilder()
                .name("vrato")
                .domain("gmail.com");
    }

    private EmailBuilder sampleEmailTeena() {
        return new EmailBuilder()
                .name("teena")
                .domain("seznam.cz");
    }

    private PhoneBuilder samplePhoneRandom1() {
        return new PhoneBuilder().number(123456789);
    }

    private PhoneBuilder samplePhoneRandom2() {
        return new PhoneBuilder().number(987654321);
    }

//################## PREPARATION #########################

    @Before
    public void setUp() throws SQLException {

        ds = prepareDataSource();

        DBUtils.executeSqlScript(ds, ContactManager.class.getResource("createTables.sql"));

        ContactManager contactManager = new ContactManagerImpl(ds);
        Person vrato = samplePersonVrato().build();
        //contactManager.getAllPersons(); //initialize list
        contactManager.addPerson(vrato); //add a person
        contactInformation = contactManager.selectPerson(vrato.getID()); //get that persons contact info
    }

    @After
    public void destroy() throws SQLException {
        DBUtils.executeSqlScript(ds, ContactManager.class.getResource("dropTables.sql"));
    }

    private static DataSource prepareDataSource() throws SQLException {
        EmbeddedDataSource ds = new EmbeddedDataSource();
        ds.setDatabaseName("memory:contactbook-test");
        ds.setCreateDatabase("create");
        return ds;
    }

    private void prepareContactInformationWithAddress() throws SQLException {
        contactInformation.updateAddress(sampleAddressSipova().buildValid());
    }


//################## PERSON TESTS #########################

    //Note: Person.updatePerson() is tested in PersonImplTest
    @Test
    public void updatePerson() throws SQLException {
        Person person = samplePersonTeena().build();
        int originalID = contactInformation.getPerson().getID();

        assertThat(contactInformation.updatePerson(person)).isTrue();

        person = samplePersonTeena().id(originalID).build();

        assertThat(contactInformation.getPerson()).isEqualToComparingFieldByField(person);
    }

//################## ADDRESS TESTS #########################

    //Note: Address.updateAddress() is tested in AddressImplTest
    @Test
    public void updateAddressFirstTime() throws SQLException {
        Address firstAddress = sampleAddressSipova().id(null).build();

        assertThat(contactInformation.getAddress()).isNull();

        assertThat(contactInformation.updateAddress(firstAddress)).isTrue();
        assertThat(contactInformation.getAddress().getID()).isNotNull();

        int id = contactInformation.getAddress().getID();
        firstAddress = sampleAddressSipova().id(id).build();

        assertThat(contactInformation.getAddress()).isEqualToComparingFieldByField(firstAddress);
    }

    @Test
    public void updateAddress() throws SQLException {
        prepareContactInformationWithAddress();
        Address newAddress = sampleAddressBotanicka().build();

        assertThat(contactInformation.getAddress()).isNotNull();

        assertThat(contactInformation.updateAddress(newAddress)).isTrue();

        Integer id = contactInformation.getAddress().getID();
        
        newAddress = sampleAddressBotanicka().id(id).build();
        assertThat(contactInformation.getAddress()).isEqualToComparingFieldByField(newAddress);
    }

//################## EMAIL TESTS #########################

    @Test
    public void getEmailsEmpty() throws SQLException {

        assertThat(contactInformation.getEmails()).isEmpty();
    }

    @Test
    public void addEmail() throws SQLException {

        assertThat(contactInformation.getEmails()).isEmpty();

        Email vrato = sampleEmailVrato().build();
        Email teena = sampleEmailTeena().build();

        contactInformation.addEmail(vrato);
        contactInformation.addEmail(teena);

        assertThat(contactInformation.getEmails()).containsOnly(vrato, teena);
    }

    @Test
    public void addEmailNull() {
        assertThatThrownBy(() -> contactInformation.addEmail(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
    /* Test omitted, because nothing can create invalid intance of Email

        @Test
        public void addEmailNullField() {
            Email noName = sampleEmailTeena().name(null).build();
            Email noDomain = sampleEmailVrato().domain(null).build();

            assertThatThrownBy(() -> contactInformation.addEmail(noName))
                    .isInstanceOf(InvalidFieldException.class);

            assertThatThrownBy(() -> contactInformation.addEmail(noDomain))
                    .isInstanceOf(InvalidFieldException.class);
        }

        @Test
        public void addEmailEmptyName() {
            Email emptyName = sampleEmailTeena().name("").build();

            assertThatThrownBy(() -> contactInformation.addEmail(emptyName))
                    .isInstanceOf(InvalidFieldException.class);

        }


        @Test
        public void addEmailEmptyDomain() {
            Email emptyDomain = sampleEmailVrato().domain("").build();

            assertThatThrownBy(() -> contactInformation.addEmail(emptyDomain))
                    .isInstanceOf(InvalidFieldException.class);
        }
    */
    @Test
    public void removeEmail() throws SQLException {
        Email vrato = sampleEmailVrato().build();
        Email teena = sampleEmailTeena().build();

        contactInformation.addEmail(vrato);
        contactInformation.addEmail(teena);

        assertThat(contactInformation.removeEmail(vrato)).isTrue();

        assertThat(contactInformation.getEmails()).containsOnly(teena);

        contactInformation.removeEmail(teena);

        assertThat(contactInformation.getEmails()).isEmpty();
    }

    @Test
    public void removeEmailNull() {
        assertThatThrownBy(() -> contactInformation.removeEmail(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void removeEmailNotInCollection() throws SQLException {
        Email vrato = sampleEmailVrato().build();
//       Email teena = sampleEmailTeena().build();

        contactInformation.addEmail(vrato);
/*
        assertThatThrownBy(() -> contactInformation.removeEmail(teena))
                .isInstanceOf(IllegalArgumentException.class);
*/
        assertThat(contactInformation.getEmails()).containsOnly(vrato);
    }

//################## PHONE TESTS #########################

    @Test
    public void getPhonesEmpty() throws SQLException {
        assertThat(contactInformation.getPhones()).isEmpty();
    }

    @Test
    public void addPhone() throws SQLException {
        assertThat(contactInformation.getEmails()).isEmpty();

        Phone phone1 = samplePhoneRandom1().build();
        Phone phone2 = samplePhoneRandom2().build();

        contactInformation.addPhone(phone1);
        contactInformation.addPhone(phone2);

        assertThat(contactInformation.getPhones()).containsOnly(phone1, phone2);
    }

    @Test
    public void addPhoneNull() {
        assertThatThrownBy(() -> contactInformation.addPhone(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
/*
    @Test
    public void addPhoneZero() {
        Phone zero = samplePhoneRandom1().number(0).build();

        assertThatThrownBy(() -> contactInformation.addPhone(zero))
                .isInstanceOf(InvalidFieldException.class);
    }

    @Test
    public void addPhoneTooShort() {
        Phone eight = samplePhoneRandom1().number(12345678).build();

        assertThatThrownBy(() -> contactInformation.addPhone(eight))
                .isInstanceOf(InvalidFieldException.class);
    }

    @Test
    public void addPhoneTooLong() {
        Phone ten = samplePhoneRandom1().number(1234567890).build();

        assertThatThrownBy(() -> contactInformation.addPhone(ten))
                .isInstanceOf(InvalidFieldException.class);
    }
*/
    @Test
    public void removePhone() throws SQLException {
        Phone phone1 = samplePhoneRandom1().build();
        Phone phone2 = samplePhoneRandom2().build();

        contactInformation.addPhone(phone1);
        contactInformation.addPhone(phone2);

        assertThat(contactInformation.removePhone(phone1)).isTrue();
        assertThat(contactInformation.getPhones()).containsOnly(phone2);

        contactInformation.removePhone(phone2);
        assertThat(contactInformation.getPhones()).isEmpty();

    }

    @Test
    public void removePhoneNull() {
        assertThatThrownBy(() -> contactInformation.removePhone(null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void removePhoneNotInCollection() throws SQLException {
        Phone phone1 = samplePhoneRandom1().build();
//        Phone phone2 = samplePhoneRandom2().build();

        contactInformation.addPhone(phone1);
//        assertThatThrownBy(() -> contactInformation.removePhone(phone2))
 //               .isInstanceOf(IllegalArgumentException.class);

        assertThat(contactInformation.getPhones()).containsOnly(phone1);
    }
}
