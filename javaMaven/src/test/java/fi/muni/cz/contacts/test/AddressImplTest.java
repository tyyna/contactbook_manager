package fi.muni.cz.contacts.test;


import fi.muni.cz.contacts.InvalidFieldException;
import fi.muni.cz.contacts.AddressBuilder;
import fi.muni.cz.contacts.AddressImpl;
import fi.muni.cz.contacts.Address;
import org.junit.*;

import static org.assertj.core.api.Assertions.*;
/**
 * Created by vbendel on 3/14/17.
 * TODO teena
 */
public class AddressImplTest {

    private Address address;

    private AddressBuilder sampleAddressFull() {
        return new AddressBuilder()
                .street("Botanicka")
                .number("68a")
                .city("Brno");
    }

    @Before
    public void setUp() {
        address = new AddressImpl();
    }

    @Test
    public void updateAddressValid() {
        Address newAddress = sampleAddressFull().build();
        address.updateAddress(newAddress);

        assertThat(address).isEqualToComparingFieldByField(newAddress);
    }

    @Test
    public void updateAddressNull() {
        assertThatThrownBy(() -> address.updateAddress(null)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void updateAddressNullStreet() {
        Address badNullStreet = sampleAddressFull().street(null).build();

        assertThatThrownBy(() -> address.updateAddress(badNullStreet))
                .isInstanceOf(InvalidFieldException.class);
    }

    @Test
    public void updateAddressNullNumber() {
        Address badNullNumber = sampleAddressFull().number(null).build();

        assertThatThrownBy(() -> address.updateAddress(badNullNumber))
                .isInstanceOf(InvalidFieldException.class);
    }

    @Test
    public void updateAddressNullCity() {
        Address badNullCity = sampleAddressFull().city(null).build();

        assertThatThrownBy(() -> address.updateAddress(badNullCity))
                .isInstanceOf(InvalidFieldException.class);
    }

    @Test
    public void updateAddressEmptyStreet() {
        Address badNullStreet = sampleAddressFull().street("").build();

        assertThatThrownBy(() -> address.updateAddress(badNullStreet))
                .isInstanceOf(InvalidFieldException.class);
    }

    @Test
    public void updateAddressEmptyNumber() {
        Address badNullNumber = sampleAddressFull().number("").build();

        assertThatThrownBy(() -> address.updateAddress(badNullNumber))
                .isInstanceOf(InvalidFieldException.class);
    }

    @Test
    public void updateAddressEmptyCity() {
        Address badEmptyCity = sampleAddressFull().city("").build();

        assertThatThrownBy(() -> address.updateAddress(badEmptyCity))
                .isInstanceOf(InvalidFieldException.class);
    }
}
