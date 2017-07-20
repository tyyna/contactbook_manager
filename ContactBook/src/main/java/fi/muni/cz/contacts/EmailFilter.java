package fi.muni.cz.contacts;

/**
 * @author vbendel
 * @version 3/14/17
 */
public class EmailFilter implements Filter {
    private Email email;

    public EmailFilter(Email email) {
        this.email = email;
    }

    public boolean isMatch(Object other) {
        return email.equals(other);
    }
}
