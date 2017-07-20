package fi.muni.cz.contacts;

/**
 * @author tyyna
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
