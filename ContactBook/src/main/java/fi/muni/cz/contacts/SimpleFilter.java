package fi.muni.cz.contacts;

/**
 * @author vbendel
 * @version 3/26/17
 */
public class SimpleFilter {
    private String name = null;
    private String surname = null;
    private String street = null;
    private String number = null;
    private String city = null;
    private Email email = null;
    private Phone phone = null;

    public boolean isEmpty() {
        return name == null &&
                surname == null &&
                street == null &&
                number == null &&
                city == null &&
                email == null &&
                phone == null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public boolean hasPerson() {
        return name != null || surname != null;
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
        if (number != null && street == null)
            throw new InvalidFieldException("Cannot filter by number without street.");
        this.number = number;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean hasAddress() {
        return street != null || number != null || city != null;
    }

    public String getEmail() {
        return email == null ? null : email.toString();
    }

    public void setEmail(String email) {
        if (email == null)
            return;
        this.email = new EmailImpl(email);
    }

    public Integer getPhone() {
        return phone == null ? null : phone.getNumber();
    }

    public void setPhone(Integer phone) {
        if (phone == null)
            return;
        this.phone = new PhoneImpl(phone);
    }

    public boolean hasEmailOrPhone() {
        return email != null || phone != null;
    }
}
