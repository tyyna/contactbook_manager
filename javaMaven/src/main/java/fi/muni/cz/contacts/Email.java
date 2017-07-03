package fi.muni.cz.contacts;

/**
 * Created by Vratislav Bendel on 6. 3. 2017.
 */

public interface Email {
    //int getId();
    boolean parseEmailFromString(String rawEmail);

    String toString();
}
