package fi.muni.cz.contacts;

/**
 * @author vbendel
 */
public interface DatabaseFilter {
    //treba vyriesit ako bude pouzivat ContactManger ten filter, set metody vs. nejaka ina logika?
    void setEmailFilter(EmailFilter emailFilter);
    void setPhoneFilter(PhoneFilter phoneFilter);
    void setPersonFilter(PersonFilter personFilter);
    void setAddressFilter(AddressFilter addressFilter);
}
