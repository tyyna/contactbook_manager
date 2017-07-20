package fi.muni.cz.contacts;

/**
 * @author vbendel
 */
public class DatabaseFilterBuilder {
    PersonFilter personFilter = null;
    AddressFilter addressFilter = null;
    EmailFilter emailFilter = null;
    PhoneFilter phoneFilter = null;

    public DatabaseFilterBuilder person(Person person) {
        this.personFilter = new PersonFilter(person);
        return this;
    }

    public DatabaseFilterBuilder address(Address address) {
        this.addressFilter = new AddressFilter(address);
        return this;
    }

    public DatabaseFilterBuilder email(Email email) {
        this.emailFilter = new EmailFilter(email);
        return this;
    }

    public DatabaseFilterBuilder phone(Phone phone) {
        this.phoneFilter = new PhoneFilter(phone);
        return this;
    }

    public DatabaseFilter build() {
        DatabaseFilter dbf = new DatabaseFilterImpl();
        dbf.setPersonFilter(personFilter);
        dbf.setAddressFilter(addressFilter);
        dbf.setEmailFilter(emailFilter);
        dbf.setPhoneFilter(phoneFilter);
        return dbf;
    }
}
