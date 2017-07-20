package fi.muni.cz.contacts;

/**
 * @author tyyna
 */
public interface Person {
    Integer getID();

    void setID(Integer id);
    
    Integer getAddress_Id();
    void setAddress_Id(Integer id);

    String getName();
    String getSurname();

    boolean updatePerson(Person person);
    void validatePerson(Person person);

    @Override
    String toString();
}
