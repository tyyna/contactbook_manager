package fi.muni.cz.contacts.test;


//import com.sun.javaws.exceptions.InvalidArgumentException;
import fi.muni.cz.contacts.EmailBuilder;
import fi.muni.cz.contacts.Email;
import fi.muni.cz.contacts.InvalidFieldException;
import fi.muni.cz.contacts.EmailImpl;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Created by vbendel on 3/14/17.
 */
public class EmailImplTest {

    private Email email;

    @Before
    public void setUp() {
        email = new EmailImpl();
    }

    @Test
    public void parseEmailValid() {
        String good = "meno@domena.com";
        assertThat(email.parseEmailFromString(good)).isTrue();

        Email checkEmail = new EmailBuilder().name("meno").domain("domena.com").build();
        assertThat(email).isEqualToComparingFieldByField(checkEmail);
    }

    @Test
    public void parseEmailNoName() {
        String badNoName = "@domena.com";
        assertThatThrownBy(() -> email.parseEmailFromString(badNoName))
                .isInstanceOf(InvalidFieldException.class);
    }

    @Test
    public void parseEmailNoDomain() {
        String badNoDomain = "meno@";
        assertThatThrownBy(() -> email.parseEmailFromString(badNoDomain))
                .isInstanceOf(InvalidFieldException.class);
    }

    @Test
    public void parseEmailNoAt() {
        String badNoAt = "bezzavinacu";
        assertThatThrownBy(() -> email.parseEmailFromString(badNoAt))
                .isInstanceOf(InvalidFieldException.class);
    }

    @Test
    public void parseEmailEmpty() {
        String badEmpty = "";
        assertThatThrownBy(() -> email.parseEmailFromString(badEmpty))
                .isInstanceOf(InvalidFieldException.class);
    }

}
