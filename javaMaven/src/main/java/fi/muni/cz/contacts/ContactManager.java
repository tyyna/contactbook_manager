package fi.muni.cz.contacts;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collection;

/**
 * Created by Vratislav Bendel on 11. 3. 2017.
 */
public interface ContactManager {
    void setDataSource(DataSource ds);

    boolean addPerson(Person person) throws SQLException;
    boolean removePerson(Person person) throws SQLException;

    ContactInformation selectPerson(Integer id) throws SQLException;

    Collection<Person> getAllPersons() throws SQLException;

    Collection<Person> applyFilter(SimpleFilter filter) throws SQLException;

    //boolean flushData(ContactInformation information);
}
