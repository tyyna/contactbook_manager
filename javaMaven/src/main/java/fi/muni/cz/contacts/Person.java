package fi.muni.cz.contacts;

/**
 * Created by Vratislav Bendel on 6. 3. 2017.
 * TODO: napsat kontrakty
 */
public interface Person {
    Integer getID();

    void setID(Integer id);
    
    Integer getAddress_Id();
    void setAddress_Id(Integer id);

    String getName();
    String getSurname();
    //Set => rovnaka poznamka ako u Phone

    boolean updatePerson(Person person);
    void validatePerson(Person person);

    @Override
    String toString();
}
