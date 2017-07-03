package fi.muni.cz.contacts;

/**
 * Created by Vratislav Bendel on 6. 3. 2017.
 */
public class PersonImpl implements Person {
    private Integer id;
    private String name;
    private String surname;
    private Integer addressId;
    //spravime Gender, ked uz sa z toho robila taka sranda? :D

    public Integer getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public void setID(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public boolean updatePerson(Person person) {
        this.validatePerson(person);
        name = person.getName();
        surname = person.getSurname();
        return true;
    }

    public void validatePerson(Person person) {

        if (person == null)
            throw new IllegalArgumentException("UpdatePerson: null argument");

        if (person.getName() == null ||
                person.getSurname() == null)
            throw new InvalidFieldException("UpdatePerson: null field");

        if (person.getName().isEmpty() ||
                person.getSurname().isEmpty())
            throw new InvalidFieldException("UpdatePerson: empty field");
    }

    public String toString() {
        return name + " " + surname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonImpl person = (PersonImpl) o;

        if (id != null ? !id.equals(person.id) : person.id != null) return false;
        if (name != null ? !name.equals(person.name) : person.name != null) return false;
        return surname != null ? surname.equals(person.surname) : person.surname == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        return result;
    }

    @Override
    public Integer getAddress_Id() {
        return addressId;
    }

    @Override
    public void setAddress_Id(Integer id) {
        this.addressId = id;
    }
}
