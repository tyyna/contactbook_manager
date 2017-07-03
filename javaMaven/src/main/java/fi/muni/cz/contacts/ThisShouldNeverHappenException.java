package fi.muni.cz.contacts;

/**
 * Created by vbendel on 3/14/17.
 */
public class ThisShouldNeverHappenException extends Exception {
    //BOOM!!!

    public ThisShouldNeverHappenException(String message) {
        super(message);
    }
}
