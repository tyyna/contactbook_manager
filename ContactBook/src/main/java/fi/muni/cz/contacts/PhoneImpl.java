package fi.muni.cz.contacts;

/**
 * @author vbendel
 */
public class PhoneImpl implements Phone {
    private int number;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        if (Integer.toString(number).length() != 9)
            throw new IllegalArgumentException("Wrong number of digits for Phone");
        this.number = number;
    }

    public PhoneImpl() {}

    public PhoneImpl(int number) {
        setNumber(number);
    }

    public String toString() {
        return Integer.toString(number);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PhoneImpl phone = (PhoneImpl) o;

        return number == phone.number;
    }

    @Override
    public int hashCode() {
        return number;
    }
}