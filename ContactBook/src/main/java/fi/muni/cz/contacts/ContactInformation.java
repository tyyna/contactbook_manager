package fi.muni.cz.contacts;

import java.sql.SQLException;
import java.util.Collection;

/**
 * @author vbendel
 */
public interface ContactInformation {
    Person getPerson() throws SQLException;
    boolean updatePerson(Person person) throws SQLException;

    Address getAddress() throws SQLException;
    boolean updateAddress(Address address) throws SQLException;

    boolean addEmail(Email email) throws SQLException;
    boolean removeEmail(Email email) throws SQLException;
    Collection<Email> getEmails() throws SQLException;

    boolean addPhone(Phone phone) throws SQLException;
    boolean removePhone(Phone phone) throws SQLException;
    Collection<Phone> getPhones() throws SQLException;


}
