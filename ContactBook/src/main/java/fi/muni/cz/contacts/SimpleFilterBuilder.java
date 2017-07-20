package fi.muni.cz.contacts;

/**
 * @author vbendel
 * @version 3/27/17
 */
public class SimpleFilterBuilder {
    private String name = null;
    private String surname = null;
    private String street = null;
    private String number = null;
    private String city = null;
    private String email = null;
    private Integer phone = null;

    public SimpleFilterBuilder name(String name) {
        this.name = name;
        return this;
    }

    public SimpleFilterBuilder surname(String surname) {
        this.surname = surname;
        return this;
    }

    public SimpleFilterBuilder street(String street) {
        this.street = street;
        return this;
    }

    public SimpleFilterBuilder number(String number) {
        this.number = number;
        return this;
    }

    public SimpleFilterBuilder city(String city) {
        this.city = city;
        return this;
    }

    public SimpleFilterBuilder email(String email) {
        this.email = email;
        return this;
    }

    public SimpleFilterBuilder phone(Integer phone) {
        this.phone = phone;
        return this;
    }

    public SimpleFilter build() {
        SimpleFilter filter = new SimpleFilter();
        filter.setName(name);
        filter.setSurname(surname);
        filter.setStreet(street);
        filter.setNumber(number);
        filter.setCity(city);
        filter.setEmail(email);
        filter.setPhone(phone);
        return filter;
    }

    public SimpleFilter buildValid() {
        return build();
    }
}
