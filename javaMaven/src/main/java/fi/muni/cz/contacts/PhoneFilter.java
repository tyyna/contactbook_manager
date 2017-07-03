package fi.muni.cz.contacts;

/**
 * @author Vratislav Bendel
 * @version 3/14/17
 */
public class PhoneFilter implements Filter {
    private Phone phone;

    public PhoneFilter(Phone phone) {
        this.phone = phone;
    }

    public boolean isMatch(Object other) {
        return phone.equals(other);
    }
}
