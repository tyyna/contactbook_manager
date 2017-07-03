package fi.muni.cz.contacts;

/**
 * Created by Vratislav Bendel on 6. 3. 2017.
 */
public interface Address {
    Integer getID();
    void setID(Integer id);

    String getStreet();

    String getNumber();

    String getCity();

    //Set => rovnaka poznamka ako u Phone

    boolean updateAddress(Address address) throws IllegalArgumentException;

    void validateAddress(Address address) throws InvalidFieldException;

    @Override
    String toString();
}
