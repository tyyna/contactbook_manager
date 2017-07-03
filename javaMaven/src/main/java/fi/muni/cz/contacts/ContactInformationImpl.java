package fi.muni.cz.contacts;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by Vratislav Bendel on 11. 3. 2017.
 */
public class ContactInformationImpl implements ContactInformation {

    private DataSource ds;
    
    private Integer personId;
    private Integer addressId;

    public ContactInformationImpl(DataSource ds) {
        this.ds = ds;
    }

    public ContactInformationImpl(DataSource ds, Integer personId, Integer addressId) {
        this.ds = ds;
        this.personId = personId;
        this.addressId = addressId;
    }

    public Person getPerson() throws SQLException {
        Person person = null;
        Connection conn = null;
        try {
            conn = ds.getConnection();
            
            PreparedStatement st = conn.prepareStatement(
                    "SELECT PERSON_ID,FIRST_NAME,SURNAME FROM PEOPLE WHERE PERSON_ID = ?");
            st.setInt(1, personId);
            
            ResultSet rs = st.executeQuery();
            
            if (rs.next()) {
                if (personId != rs.getInt(1))
                    throw new SQLException("Query for person ID: " + personId + 
                            " returned person with different ID: " + rs.getInt(1));
                
                person = new PersonBuilder()
                        .id(rs.getInt(1))
                        .name(rs.getString(2))
                        .surname(rs.getString(3))
                        .buildValid();
                
                if(rs.next())
                    throw new SQLException("More than one persons with ID: " + personId + " found");
            }
            else {
                throw new SQLException("Person with ID: " + personId + " not found.");
            }
            
            rs.close();
            st.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(ContactInformationImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        finally {
            if (conn != null) {
                conn.close();
            }
        }
        return person;
    }

    public boolean updatePerson(Person person) throws SQLException {
        if (person == null) {
            throw new IllegalArgumentException("Cannot update null person.");
        }

        person.validatePerson(person);

        int resultSet;
        Connection conn = null;

        try {
            conn = ds.getConnection();
            //conn.setAutoCommit(false);

            PreparedStatement st = conn.prepareStatement(
                    "UPDATE PEOPLE SET FIRST_NAME= ? , SURNAME= ? WHERE PERSON_ID= ?");
            st.setString(1, person.getName());
            st.setString(2, person.getSurname());
            st.setInt(3, personId);

            resultSet = st.executeUpdate();
            st.close();

            conn.commit();

        } catch (SQLException e) {
            System.err.print(e.getMessage());
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.rollback();
                conn.close();
            }
        }

        if (resultSet != 1) {
            return false;
        }

        //this.person.updatePerson(person);
        return true;
    }

    public Address getAddress() throws SQLException {
        if (addressId == null) {
            return null;
        }
        Address address = null;
        Connection conn = null;
        try {
            conn = ds.getConnection();
            
            PreparedStatement st = conn.prepareStatement(
                    "SELECT ID,STREET,NUMBER,CITY FROM ADDRESS WHERE ADDRESS.ID = ?");
            st.setInt(1, addressId);
            
            ResultSet rs = st.executeQuery();
            
            if (rs.next()) {
                if (addressId != rs.getInt(1))
                    throw new SQLException("Query for address ID: " + addressId + 
                            " returned address with different ID: " + rs.getInt(1));
                
                address = new AddressBuilder()
                        .id(rs.getInt(1))
                        .street(rs.getString(2))
                        .number(rs.getString(3))
                        .city(rs.getString(4))
                        .buildValid();
                
                if(rs.next())
                    throw new SQLException("More than one addresses with ID: " + addressId + " found");
            }
            else {
                throw new SQLException("Address with ID: " + addressId + " not found.");
            }
            
            rs.close();
            st.close();
            
        } catch (SQLException ex) {
            Logger.getLogger(ContactInformationImpl.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        finally {
            if (conn != null) {
                conn.close();
            }
        }
        return address;
    }

    public boolean updateAddress(Address address) throws SQLException {
      if (address == null)
            throw new IllegalArgumentException("updateAddress: address was NULL");
                
        Integer addressId = null;
        Connection conn = null;
        
        try {
            conn = ds.getConnection();
            
            addressId = getIdIfAlreadyExists(conn, address);
        
            if (addressId == null) {
                addressId = addAddress(conn, address);
                if (addressId == null)
                    throw new SQLException("addAddress failed"); 
            }
            linkPersonToAddress(conn, personId, addressId);  
            this.addressId = addressId;
        }
        catch (SQLException e) {
            System.err.print(e.getMessage());
            throw e;
        }
        finally {
            if (conn != null) {
                conn.close();
            }
        }
        
        return this.addressId != null;
        
    }
    
    private Integer getIdIfAlreadyExists(Connection conn, Address address) throws SQLException {
        if (conn == null)
            throw new SQLException("getAddressId: connection was NULL");
        
        PreparedStatement st = conn.prepareStatement(
                "SELECT ID FROM ADDRESS WHERE STREET=? AND NUMBER=? AND CITY=?");
        
        st.setString(1, address.getStreet());
        st.setString(2, address.getNumber());
        st.setString(3, address.getCity());
        
        ResultSet rs = st.executeQuery();
        
        Integer id = null;
        if (rs.next()) {
            id = rs.getInt(1);
            if (rs.wasNull())
                id = null;
            if (rs.next())
                throw new InternalError("Same addresses with diferent ID");
        }
        rs.close();
        st.close();
        return id;
    }

    private void linkPersonToAddress(Connection conn, Integer personId, Integer addressId) throws SQLException {
        if (conn == null)
            throw new IllegalArgumentException("linkPerson: Connection was NULL");
        if (addressId == null)
            throw new IllegalArgumentException("linkPerson: New ID was NULL");


        PreparedStatement st = conn.prepareStatement("UPDATE PEOPLE SET ADDRESS_ID=? WHERE PERSON_ID=?");
        st.setInt(1, addressId);
        st.setInt(2, personId);

        int updateCount = st.executeUpdate();
        st.close();

        if (updateCount != 1)
            System.err.print("More than 1 person with the same ID");
    }
    
    private Integer addAddress(Connection conn, Address newAddress) throws SQLException {
        if (conn == null)
            throw new IllegalArgumentException("addAddress: Connection was NULL");
        if (newAddress == null)
            throw new IllegalArgumentException("addAddress: New Address was NULL");


        PreparedStatement st = conn.prepareStatement(
                "INSERT INTO ADDRESS (STREET, NUMBER, CITY) VALUES (?,?,?)",
                Statement.RETURN_GENERATED_KEYS);
        st.setString(1, newAddress.getStreet());
        st.setString(2, newAddress.getNumber());
        st.setString(3, newAddress.getCity());

        st.executeUpdate();
        ResultSet resultKeys = st.getGeneratedKeys();

        Integer newID = null;
        if (resultKeys.next())
            newID = resultKeys.getInt(1);

        resultKeys.close();
        st.close();

        return newID;
    }

    public boolean addEmail(Email email) throws SQLException {
        if (email == null) {
            throw new IllegalArgumentException("Invalid email.");
        }

        int resultSet;
        Connection conn = null;

        try {
            conn = ds.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement st = conn.prepareStatement(
                    "INSERT INTO EMAILS VALUES( ? , ? )");
            st.setInt(1, personId);
            st.setString(2, email.toString());

            resultSet = st.executeUpdate();
            st.close();

            conn.commit();

        } catch (SQLException e) {
            System.err.print(e.getMessage());
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.rollback();
                conn.close();
            }
        }

        if (resultSet != 1) {
            return false;
        }

        return true;
    }


    public boolean removeEmail(Email email) throws SQLException {
        if (email == null) {
            throw new IllegalArgumentException("Invalid email.");
        }

        int resultSet;
        Connection conn = null;

        try {
            conn = ds.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement st = conn.prepareStatement(
                    "DELETE FROM EMAILS WHERE EMAIL= ?");
            st.setString(1, email.toString());

            resultSet = st.executeUpdate();
            st.close();

            conn.commit();

        } catch (SQLException e) {
            System.err.print(e.getMessage());
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.rollback();
                conn.close();
            }
        }

        if (resultSet != 1) {
            return false;
        }

        return true;
    }

    public Collection<Email> getEmails() throws SQLException {
        Collection<Email> result = new LinkedList<>();
        Connection conn = null;

        try {
            conn = ds.getConnection();

            PreparedStatement st = conn.prepareStatement(
                    "SELECT EMAIL FROM EMAILS WHERE OWNER = ?");
            st.setInt(1, personId);

            ResultSet resultSet = st.executeQuery();

            while (resultSet.next()) {
                result.add(new EmailImpl(resultSet.getString(1)));
            }

            resultSet.close();
            st.close();


        } catch (SQLException e) {
            System.err.print(e.getMessage());
            throw e;
        }
        finally {
            if (conn != null) {
        //        conn.rollback();
                conn.close();
            }
        }
        
        return result; 
    }


    public boolean addPhone(Phone phone) throws SQLException {
        if (phone == null) {
            throw new IllegalArgumentException("Invalid phone.");
        }

       int resultSet;
        Connection conn = null;

        try {
            conn = ds.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement st = conn.prepareStatement(
                    "INSERT INTO PHONES VALUES( ? , ?)");
            st.setInt(1, personId);
            st.setInt(2, phone.getNumber());

            resultSet = st.executeUpdate();
            st.close();

            conn.commit();

        } catch (SQLException e) {
            System.err.print(e.getMessage());
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.rollback();
                conn.close();
            }
        }

        if (resultSet != 1) {
            return false;
        }

        return true;
    }


    public boolean removePhone(Phone phone) throws SQLException {
        if (phone == null) {
            throw new IllegalArgumentException("Invalid email.");
        }

        
        int resultSet;
        Connection conn = null;

        try {
            conn = ds.getConnection();
            conn.setAutoCommit(false);

            PreparedStatement st = conn.prepareStatement(
                    "DELETE FROM PHONES WHERE NUMBER= ?");
            st.setInt(1, phone.getNumber());

            resultSet = st.executeUpdate();
            st.close();

            conn.commit();
        } catch (SQLException e) {
            System.err.print(e.getMessage());
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.rollback();
                conn.close();
            }
        }

        if (resultSet != 1) {
            return false;
        }

        return true;
    }


    public Collection<Phone> getPhones() throws SQLException {
        Collection<Phone> result = new LinkedList<>();
        Connection conn = null;

        try {
            conn = ds.getConnection();

            PreparedStatement st = conn.prepareStatement(
                    "SELECT NUMBER FROM PHONES WHERE OWNER = ?");
            st.setInt(1, personId);

            ResultSet resultSet = st.executeQuery();

            while (resultSet.next()) {
                result.add(new PhoneImpl(resultSet.getInt(1)));
            }

            resultSet.close();
            st.close();

        } catch (SQLException e) {
            System.err.print(e.getMessage());
            throw e;
        }
        finally {
            if (conn != null) {
                conn.rollback();
                conn.close();
            }
        }
        return result;
    }

    
    //############# OBSOLETE #########################################
    /*
    public boolean initContactInformation(Person person, Integer addressID) throws SQLException {

        if (person == null)
            return false;

        this.person = person;

        if (addressID == null)
            address = null;
        else
            initAddress(addressID);

        initEmails(person.getID());
        initPhones(person.getID());

        return true;
    }

    private boolean initAddress(Integer addressID) throws SQLException {

        if (addressID == null)
            return false;

        Connection conn = null;

        try {
            conn = ds.getConnection();

            PreparedStatement st = conn.prepareStatement(
                    "SELECT STREET, NUMBER, CITY FROM ADDRESS WHERE ADDRESS.ID = ?");

            st.setInt(1, addressID);

            ResultSet resultSet = st.executeQuery();

            if (resultSet.next()) {
                address = new AddressBuilder()
                        .street(resultSet.getString(1))
                        .number(resultSet.getString(2))
                        .city(resultSet.getString(3))
                        .buildValid();
                if (resultSet.next())
                    System.err.print("More than one address with the same ID found.");
            }
            resultSet.close();
            st.close();
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

        return true;
    }

    private boolean initEmails(Integer personID) throws SQLException {
        if (personID == null)
            return false;

        Collection<Email> result = new LinkedList<>();
        Connection conn = null;

        try {
            conn = ds.getConnection();

            PreparedStatement st = conn.prepareStatement(
                    "SELECT EMAIL FROM EMAILS WHERE OWNER = ?");
            st.setInt(1, personID);

            ResultSet resultSet = st.executeQuery();

            while (resultSet.next()) {
                result.add(new EmailImpl(resultSet.getString(1)));
            }

            resultSet.close();
            st.close();


        } catch (SQLException e) {
            System.err.print(e.getMessage());
            throw e;
        }
        finally {
            if (conn != null) {
                conn.rollback();
                conn.close();
            }
        }
        emails = result;
        return true;
    }

    private boolean initPhones(Integer personID) throws SQLException {
        if (personID == null)
            return false;

        Collection<Phone> result = new LinkedList<>();
        Connection conn = null;

        try {
            conn = ds.getConnection();

            PreparedStatement st = conn.prepareStatement(
                    "SELECT NUMBER FROM PHONES WHERE OWNER = ?");
            st.setInt(1, personID);

            ResultSet resultSet = st.executeQuery();

            while (resultSet.next()) {
                result.add(new PhoneImpl(resultSet.getInt(1)));
            }

            resultSet.close();
            st.close();

        } catch (SQLException e) {
            System.err.print(e.getMessage());
            throw e;
        }
        finally {
            if (conn != null) {
                conn.rollback();
                conn.close();
            }
        }
        phones = result;
        return true;
    }
    */
}
