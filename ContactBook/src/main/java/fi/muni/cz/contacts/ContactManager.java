package fi.muni.cz.contacts;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collection;

/**
 * @author tyyna
 */
public interface ContactManager {
    void setDataSource(DataSource ds);

    boolean addPerson(Person person) throws SQLException;
    boolean removePerson(Person person) throws SQLException;

    ContactInformation selectPerson(Integer id) throws SQLException;

    Collection<Person> getAllPersons() throws SQLException;

    Collection<Person> applyFilter(SimpleFilter filter) throws SQLException;
}
