package fi.muni.cz.contacts;

/**
 * @author Vratislav Bendel
 * @version 3/14/17
 */
public interface Filter {
    boolean isMatch(Object other);
}
