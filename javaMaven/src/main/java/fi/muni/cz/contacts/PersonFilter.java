package fi.muni.cz.contacts;

/**
 * @author Vratislav Bendel
 * @version 3/14/17
 */
public class PersonFilter implements Filter {
    private Person person;

    public PersonFilter(Person person) {
        this.person = person;
    }

    public boolean isMatch(Object other) {
        if (other == null)
            return false;
        if (!(other instanceof Person))
            return false;

        Person check = (Person) other;

        boolean result = true;
        if (person.getName() != null)
            result &= person.getName().equals(check.getName());
        if (person.getSurname() != null)
            result &= person.getSurname().equals(check.getSurname());

        return result;
    }
}
