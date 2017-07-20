package fi.muni.cz.contacts;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * @author tyyna
 */
public class ContactManagerImpl implements ContactManager {
    private DataSource ds;
    
    
    public ContactManagerImpl(DataSource ds) {

        this.ds = ds;
    }

    @Override
    public void setDataSource(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public boolean addPerson(Person person) throws SQLException {

        if (person == null)
            throw new IllegalArgumentException("Cannot add null person");

        person.validatePerson(person);

        Connection conn = null;
        try {
            conn = ds.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement st = conn.prepareStatement(
                    "INSERT INTO PEOPLE (FIRST_NAME, SURNAME) VALUES(?,?)",
                    Statement.RETURN_GENERATED_KEYS);
            st.setString(1, person.getName());
            st.setString(2, person.getSurname());

            st.executeUpdate();
            ResultSet resultSet = st.getGeneratedKeys();

            if (resultSet.next()) {
                person.setID(resultSet.getInt(1));
                if (resultSet.next())
                    System.err.print("More than 1 person with the same ID");
            }

            resultSet.close();
            st.close();
            conn.commit();
        }

        catch (SQLException e) {
            System.err.print(e.getMessage());
            throw e;
        }

        finally {
            if (conn!= null) {
                conn.setAutoCommit(true);
                conn.rollback();
                conn.close();
            }
        }
        if (person.getID() == null)
            return false;

        return true;
    }

    @Override
    public boolean removePerson(Person person) throws SQLException {

        if (person == null)
            throw new IllegalArgumentException("Cannot remove null person");

        if (person.getID() == null)
            throw new InvalidFieldException("RemovePerson: person with null id received");

        person.validatePerson(person);

        int resultSet;
        Connection conn = null;
        try {
            conn = ds.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement st =conn.prepareStatement("DELETE FROM EMAILS WHERE OWNER = ?");
            st.setInt(1, person.getID());
            st.executeUpdate();
                    
            st = conn.prepareStatement("DELETE FROM PHONES WHERE OWNER=?");
            st.setInt(1, person.getID());
            st.executeUpdate();
            
            st = conn.prepareStatement("DELETE FROM PEOPLE WHERE PERSON_ID = ?");
            st.setInt(1, person.getID());

            resultSet = st.executeUpdate();
            
            

            st.close();
            conn.commit();
        }

        catch (SQLException e) {
            System.err.print(e.getMessage());
            throw e;
        }

        finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.rollback();
                conn.close();
            }
        }
        if (resultSet != 1)
            return false;

        return true;
    }

    @Override
    public ContactInformation selectPerson(Integer id) throws SQLException {
        if (id == null)
            throw new IllegalArgumentException("SelectPerson: NULL ID received");

        ContactInformation result = null;
        Connection conn = null;
        try {
            conn = ds.getConnection();

            PreparedStatement st = conn.prepareStatement(
                    "SELECT PERSON_ID,ADDRESS_ID FROM PEOPLE WHERE PERSON_ID = ?" );
            st.setInt(1, id);

            ResultSet resultSet = st.executeQuery();

            if (resultSet.next()) {
                Integer personId = resultSet.getInt(1);
                Integer addressId = resultSet.getInt(2);
                if (resultSet.wasNull())
                    addressId = null;

                result = new ContactInformationImpl(ds, personId, addressId);

                if (resultSet.next())
                    System.err.print("More than 1 person with the same ID");
            }
            else
                throw new IllegalArgumentException("SelectPerson: selected ID not in database");

            resultSet.close();
            st.close();
        }

        catch (SQLException e) {
            System.err.print(e.getMessage());
            throw e;
        }

        finally {
            if (conn != null) {
                //conn.rollback();
                conn.close();
            }
        }
        
        return result;
    }
    
    @Override
    public Collection<Person> getAllPersons() throws SQLException {
        Collection<Person> cache = null;

        Connection conn = null;
        try {
            conn = ds.getConnection();
            PreparedStatement st = conn.prepareStatement("SELECT PERSON_ID, FIRST_NAME, SURNAME, ADDRESS_ID FROM PEOPLE");
            ResultSet resultSet = st.executeQuery();
            cache = parsePersonsFromResultSet(resultSet);
            resultSet.close();
            st.close();

        }

        catch (SQLException ex) {
            System.err.print(ex.getMessage());
            throw ex;
        }

        finally {
            if (conn != null) {
                conn.rollback();
                conn.close();
            }
        }
        return cache;
    }

    private Collection<Person> parsePersonsFromResultSet(ResultSet resultSet) throws SQLException {
        Set<Person> personList = new HashSet<>();
        while(resultSet.next()) {
            personList.add(new PersonBuilder()
                    .id(resultSet.getInt(1))
                    .name(resultSet.getString(2))
                    .surname(resultSet.getString(3))
                    .addressId(resultSet.getInt(4))
                    .buildValid());
        }
        return personList;
    }

    public Collection<Person> applyFilter(SimpleFilter filter) throws SQLException {
        if (filter == null)
            throw new IllegalArgumentException("applyFilter: NULL filter received");

        if (filter.isEmpty())
            return getAllPersons();

        Collection<Person> found = new HashSet<>(getAllPersons());


        //statement = composeFilterString(filter, composeEmailPhoneFilterSqlString(filter));

        Connection conn = null;

        try {
            conn = ds.getConnection();

            if (filter.getEmail() != null)
                found.retainAll(filterEmails(conn, filter));

            if (filter.getPhone() != null)
                found.retainAll(filterPhones(conn, filter));

            if (countCommas(filter) >= 0)
                found.retainAll(filterPersonAddress(conn, filter));

        }

        catch (SQLException e) {
            System.err.print(e.getMessage());
            throw e;
        }

        finally {
            if (conn != null) {
                conn.rollback();
                conn.close();
            }
        }

        return Collections.unmodifiableCollection(found);
    }

    private Collection<Person> filterPersonAddress(Connection conn, SimpleFilter filter) throws SQLException {
        if (conn == null)
            throw new IllegalArgumentException("filterEmails: NULL connection received");

        PreparedStatement st = conn.prepareStatement(composeFilterString(filter));
        ResultSet resultSet = st.executeQuery();

        Collection<Person> found = parsePersonsFromResultSet(resultSet);
        resultSet.close();
        st.close();

        return found;
    }

    private Collection<Person> filterEmails(Connection conn, SimpleFilter filter) throws SQLException {
        if (conn == null)
            throw new IllegalArgumentException("filterEmails: NULL connection received");
        if (filter.getEmail() == null)
            throw new InternalError("filterEmail called with NULL email filter set");

        PreparedStatement st = conn.prepareStatement(
                "SELECT PEOPLE.ID, FIRST_NAME, SURNAME FROM PEOPLE INNER JOIN EMAILS ON PEOPLE.ID=EMAILS.OWNER WHERE EMAIL=?");
        st.setString(1, filter.getEmail());
        ResultSet resultSet = st.executeQuery();

        Collection<Person> found = parsePersonsFromResultSet(resultSet);
        resultSet.close();
        st.close();

        return found;
    }

    private Collection<Person> filterPhones(Connection conn, SimpleFilter filter) throws SQLException {
        if (conn == null)
            throw new IllegalArgumentException("filterPhones: NULL connection received");
        if (filter.getPhone() == null)
            throw new InternalError("filterPhones called with NULL phone filter set");

        PreparedStatement st = conn.prepareStatement(
                "SELECT PEOPLE.ID, FIRST_NAME, SURNAME FROM PEOPLE INNER JOIN PHONES ON PEOPLE.ID=PHONES.OWNER WHERE NUMBER=?");
        st.setInt(1, filter.getPhone());
        ResultSet resultSet = st.executeQuery();

        Collection<Person> found = parsePersonsFromResultSet(resultSet);
        resultSet.close();
        st.close();

        return found;
    }


    private String composeFilterString(SimpleFilter filter) {

        StringBuilder from = new StringBuilder().append(" FROM ");
        StringBuilder where = new StringBuilder().append(" WHERE ");

        from.append("PEOPLE");
        if (filter.hasAddress())
            from.append(" INNER JOIN ADDRESS ON PEOPLE.ADDRESS_ID=ADDRESS.ID");


        //Now construct the WHERE part according to given fields
        //To get the commas right, we count the filtered attributes and append only exact number of commas
        int numCommas = countCommas(filter);
        if (numCommas < 0)
            throw new InternalError("filterPersonAddress called when countCommas is negative");

        if (filter.getName() != null) {
            where.append("FIRST_NAME='").append(filter.getName()).append("'");
            if (--numCommas >= 0)
                where.append(" AND ");
        }
        if (filter.getSurname() != null) {
            where.append("SURNAME='").append(filter.getSurname()).append("'");
            if (--numCommas >= 0)
                where.append(" AND ");
        }
        if (filter.getStreet() != null) {
            where.append("STREET='").append(filter.getStreet()).append("'");
            if (--numCommas >= 0)
                where.append(" AND ");
        }
        if (filter.getNumber() != null) {
            where.append("ADDRESS.NUMBER='").append(filter.getNumber()).append("'");
            if (--numCommas >= 0)
                where.append(" AND ");
        }
        if (filter.getCity() != null)
            where.append("CITY='").append(filter.getCity()).append("'");


        return new StringBuilder().append("SELECT PEOPLE.ID, FIRST_NAME, SURNAME")
                .append(from).append(where).toString();

    }

    private int countCommas(SimpleFilter filter) {
        //if we have only one attribute to filter by, we need 0 commas
        int result = -1;
        if (filter.getName() != null)
            result++;
        if (filter.getSurname() != null)
            result++;
        if (filter.getStreet() != null)
            result++;
        if (filter.getNumber() != null)
            result++;
        if (filter.getCity() != null)
            result++;
        return result;
    }

    private String composeEmailPhoneFilterSqlString(SimpleFilter filter) {

        if (filter.hasEmailOrPhone()) {
            StringBuilder from = new StringBuilder().append(" FROM PEOPLE, ");
            StringBuilder where = new StringBuilder().append(" WHERE ");
            if (filter.getEmail() != null) {
                from.append("EMAILS");
                where.append("EMAIL='").append(filter.getEmail()).append("'");
                if (filter.getPhone() != null) {
                    from.append(", ");
                    where.append(" AND ");
                }
            }
            if (filter.getPhone() != null) {
                from.append("PHONES");
                where.append("PHONE.NUMBER=").append(filter.getPhone());
            }
            return new StringBuilder().append("SELECT ID, FIRST_NAME, SURNAME, ADDRESS_ID")
                    .append(from).append(where).toString();
        }
        else
            return null;

    }
}
