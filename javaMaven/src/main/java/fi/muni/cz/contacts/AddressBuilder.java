package fi.muni.cz.contacts;

/**
 * @author Vratislav Bendel
 * @version 3/14/17
 */
public class AddressBuilder {


    private Integer id = null;
    private String street = null;
    private String number = null;
    private String city = null;

    public AddressBuilder id(Integer id) {
        this.id = id;
        return this;
    }

    public AddressBuilder street(String street) {
        this.street = street;
        return this;
    }

    public AddressBuilder number(String number) {
        this.number = number;
        return this;
    }

    public AddressBuilder city(String city) {
        this.city = city;
        return this;
    }

    public Address build() {
        AddressImpl address = new AddressImpl();
        address.setID(id);
        address.setStreet(street);
        address.setNumber(number);
        address.setCity(city);
        return address;
    }

    public Address buildValid() throws IllegalArgumentException {
        Address address = this.build();
        address.validateAddress(address);
        return address;
    }
}