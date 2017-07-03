package fi.muni.cz.contacts;

/**
 * @author Vratislav Bendel
 * @version 3/14/17
 */
public class PhoneBuilder {
    private int number;

    public PhoneBuilder number(int number) {
        this.number = number;
        return this;
    }

    public Phone build() {
        PhoneImpl phone = new PhoneImpl();
        phone.setNumber(number);

        return phone;
    }

    //does the same, because setNumber() throws InvalidFieldException
    public Phone buildValid() {
        return new PhoneImpl(number);
    }
}
