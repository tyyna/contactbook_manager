package fi.muni.cz.contacts;

/**
 * @author vbendel
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
