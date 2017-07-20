package fi.muni.cz.contacts;

/**
 * @author vbendel
 */

public interface Email {
    //int getId();
    boolean parseEmailFromString(String rawEmail);

    String toString();
}
