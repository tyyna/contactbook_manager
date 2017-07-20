package fi.muni.cz.contacts;

/**
 * @author vbendel
 */
public class DatabaseFilterImpl implements DatabaseFilter {

    private Filter addressFilter;
    private Filter personFilter;
    private Filter emailFilter;
    private Filter phoneFilter;

    public Filter getAddressFilter() {
        return addressFilter;
    }

    public void setAddressFilter(AddressFilter addressFilter) {
        this.addressFilter = addressFilter;
    }

    public Filter getPersonFilter() {
        return personFilter;
    }

    public void setPersonFilter(PersonFilter personFilter) {
        this.personFilter = personFilter;
    }

    public Filter getEmailFilter() {
        return emailFilter;
    }

    public void setEmailFilter(EmailFilter emailFilter) {
        this.emailFilter = emailFilter;
    }

    public Filter getPhoneFilter() {
        return phoneFilter;
    }

    public void setPhoneFilter(PhoneFilter phoneFilter) { this.phoneFilter = phoneFilter; }

}
