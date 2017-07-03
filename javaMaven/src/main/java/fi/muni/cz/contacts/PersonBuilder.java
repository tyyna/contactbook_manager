package fi.muni.cz.contacts;

/**
 * Created by teena on 14.3.17.
 */
public class PersonBuilder {

    private Integer id = null;
    private String name = null;
    private String surname = null;
    private Integer addressId = null;

    public PersonBuilder id(Integer id) {
        this.id = id;
        return this;
    }

    public PersonBuilder name(String name) {
        this.name = name;
        return this;
    }

    public PersonBuilder surname(String surname) {
        this.surname = surname;
        return this;
    }
    
    public PersonBuilder addressId(Integer id) {
        this.addressId = id;
        return this;
    }

    public Person build() {
        PersonImpl person = new PersonImpl();
        person.setID(id);
        person.setName(name);
        person.setSurname(surname);
        person.setAddress_Id(addressId);
        return person;
    }
    
    public Person buildValid() throws InvalidFieldException {
        PersonImpl person = new PersonImpl();
        if (name == null || surname == null)
            throw new InvalidFieldException("Person can't have null field");
        person.setID(id);
        person.setName(name);
        person.setSurname(surname);        
        person.setAddress_Id(addressId);
        return person;
    }

}
