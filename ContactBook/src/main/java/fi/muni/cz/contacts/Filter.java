package fi.muni.cz.contacts;

/**
 * @author vbendel
 * @version 3/14/17
 */
public interface Filter {
    boolean isMatch(Object other);
}
