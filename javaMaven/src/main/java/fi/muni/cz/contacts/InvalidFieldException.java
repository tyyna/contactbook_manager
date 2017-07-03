package fi.muni.cz.contacts;

/**
 * Created by teena on 14.3.17.
 */
public class InvalidFieldException extends IllegalArgumentException {

    public InvalidFieldException(String message) {
        super(message);
    }

    public InvalidFieldException() {}

    public InvalidFieldException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public InvalidFieldException(Throwable thrwbl) {
        super(thrwbl);
    }

    
}
