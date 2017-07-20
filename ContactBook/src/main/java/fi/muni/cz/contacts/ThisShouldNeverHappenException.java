package fi.muni.cz.contacts;

/**
 * @author vbendel
 */
public class ThisShouldNeverHappenException extends Exception {
    //BOOM!!!

    public ThisShouldNeverHappenException(String message) {
        super(message);
    }
}
