package fi.muni.cz.contacts;

/**
 * @author vbendel
 * @version 3/14/17
 */
public class AddressFilter implements Filter {
    private Address address;

    public AddressFilter(Address address) throws InvalidFieldException {
        if (address.getStreet() == null && address.getNumber() != null)
            throw new InvalidFieldException("AddressFilter: can't filter by number without street");
        this.address = address;
    }

    public boolean isMatch(Object other) {
        if (other == null)
            return false;
        if (!(other instanceof Address))
            return false;

        Address check = (Address) other;

        boolean result = true;
        if (address.getStreet() != null) {
            result &= address.getStreet().equals(check.getStreet());

            if (address.getNumber() != null)
                result &= address.getNumber().equals(check.getNumber());
        }
        if (address.getCity() != null)
            result &= address.getCity().equals(check.getCity());

        return result;
    }
}
