package fi.muni.cz.contacts;

/**
 * Created by Vratislav Bendel on 6. 3. 2017.
 */
public class AddressImpl implements Address {

    private Integer id; //obsolete
    private String street;
    private String number;
    private String city;

    public boolean updateAddress(Address address) throws IllegalArgumentException {

        this.validateAddress(address);
        this.street = address.getStreet();
        this.number = address.getNumber();
        this.city = address.getCity();
        return true;
    }

    public void validateAddress(Address address) {

        if (address == null)
            throw new IllegalArgumentException("UpdateAddres: null argument");

        if (address.getStreet() == null ||
                address.getNumber() == null ||
                address.getCity() == null)
            throw new InvalidFieldException("UpdateAddress: null field");

        if (address.getStreet().isEmpty() ||
                address.getNumber().isEmpty() ||
                address.getCity().isEmpty())
            throw new InvalidFieldException("UpdateAddress: empty field");
    }

    public void setID(Integer id) {
        this.id = id;
    }

    public Integer getID() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AddressImpl address = (AddressImpl) o;

        if (id != null ? !id.equals(address.id) : address.id != null) return false;
        if (street != null ? !street.equals(address.street) : address.street != null) return false;
        if (number != null ? !number.equals(address.number) : address.number != null) return false;
        return city != null ? city.equals(address.city) : address.city == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (street != null ? street.hashCode() : 0);
        result = 31 * result + (number != null ? number.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        return result;
    }
}
