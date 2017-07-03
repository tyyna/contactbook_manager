package fi.muni.cz.contacts;

/**
 * Created by Vratislav Bendel on 11. 3. 2017.
 */
public interface DatabaseFilter {
    //treba vyriesit ako bude pouzivat ContactManger ten filter, set metody vs. nejaka ina logika?
    void setEmailFilter(EmailFilter emailFilter);
    void setPhoneFilter(PhoneFilter phoneFilter);
    void setPersonFilter(PersonFilter personFilter);
    void setAddressFilter(AddressFilter addressFilter);
}
