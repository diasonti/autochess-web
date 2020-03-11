package fun.diasonti.autochessweb.data.form;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserAccountFormTest {

    @Test
    void equalsAndHashCodeTest() {
        final Long idOne = 1L;
        final String usernameOne = "one.username";
        final String passwordOne = "one.password";

        final Long idTwo = 2L;
        final String usernameTwo = "two.username";
        final String passwordTwo = "two.password";


        final UserAccountForm form1 = new UserAccountForm();
        final UserAccountForm form2 = new UserAccountForm();

        // True cases
        assertTrue(form1.equals(form2), "Equals is broken");
        assertTrue(form2.equals(form1), "Equals is broken");
        assertTrue(form1.hashCode() == form2.hashCode(), "HashCode is broken");

        form1.setId(idOne);
        form1.setUsername(usernameOne);
        form1.setPassword(passwordOne);
        form2.setId(idOne);
        form2.setUsername(usernameOne);
        form2.setPassword(passwordOne);
        assertTrue(form1.equals(form2), "Equals is broken");
        assertTrue(form2.equals(form1), "Equals is broken");
        assertTrue(form1.hashCode() == form2.hashCode(), "HashCode is broken");

        form2.setId(idOne);
        form2.setUsername(usernameOne);
        form2.setPassword(passwordTwo);
        assertTrue(form1.equals(form2), "Equals is broken");
        assertTrue(form2.equals(form1), "Equals is broken");
        assertTrue(form1.hashCode() == form2.hashCode(), "HashCode is broken");

        form2.setId(idOne);
        form2.setUsername(usernameOne);
        form2.setPassword(null);
        assertTrue(form1.equals(form2), "Equals is broken");
        assertTrue(form2.equals(form1), "Equals is broken");
        assertTrue(form1.hashCode() == form2.hashCode(), "HashCode is broken");


        // False cases
        form2.setId(idTwo);
        form2.setUsername(usernameOne);
        form2.setPassword(passwordOne);
        assertFalse(form1.equals(form2), "Equals is broken");
        assertFalse(form2.equals(form1), "Equals is broken");
        assertFalse(form1.hashCode() == form2.hashCode(), "HashCode is broken");

        form2.setId(idOne);
        form2.setUsername(usernameTwo);
        form2.setPassword(passwordOne);
        assertFalse(form1.equals(form2), "Equals is broken");
        assertFalse(form2.equals(form1), "Equals is broken");
        assertFalse(form1.hashCode() == form2.hashCode(), "HashCode is broken");

        form2.setId(idTwo);
        form2.setUsername(usernameTwo);
        form2.setPassword(passwordOne);
        assertFalse(form1.equals(form2), "Equals is broken");
        assertFalse(form2.equals(form1), "Equals is broken");
        assertFalse(form1.hashCode() == form2.hashCode(), "HashCode is broken");

        form2.setId(idOne);
        form2.setUsername(usernameTwo);
        form2.setPassword(passwordTwo);
        assertFalse(form1.equals(form2), "Equals is broken");
        assertFalse(form2.equals(form1), "Equals is broken");
        assertFalse(form1.hashCode() == form2.hashCode(), "HashCode is broken");

        form2.setId(idTwo);
        form2.setUsername(usernameOne);
        form2.setPassword(passwordTwo);
        assertFalse(form1.equals(form2), "Equals is broken");
        assertFalse(form2.equals(form1), "Equals is broken");
        assertFalse(form1.hashCode() == form2.hashCode(), "HashCode is broken");

        form2.setId(null);
        form2.setUsername(usernameOne);
        form2.setPassword(passwordOne);
        assertFalse(form1.equals(form2), "Equals is broken");
        assertFalse(form2.equals(form1), "Equals is broken");
        assertFalse(form1.hashCode() == form2.hashCode(), "HashCode is broken");

        form2.setId(idOne);
        form2.setUsername(null);
        form2.setPassword(passwordOne);
        assertFalse(form1.equals(form2), "Equals is broken");
        assertFalse(form2.equals(form1), "Equals is broken");
        assertFalse(form1.hashCode() == form2.hashCode(), "HashCode is broken");

        form2.setId(null);
        form2.setUsername(null);
        form2.setPassword(passwordOne);
        assertFalse(form1.equals(form2), "Equals is broken");
        assertFalse(form2.equals(form1), "Equals is broken");
        assertFalse(form1.hashCode() == form2.hashCode(), "HashCode is broken");

        form2.setId(idOne);
        form2.setUsername(null);
        form2.setPassword(null);
        assertFalse(form1.equals(form2), "Equals is broken");
        assertFalse(form2.equals(form1), "Equals is broken");
        assertFalse(form1.hashCode() == form2.hashCode(), "HashCode is broken");

        form2.setId(null);
        form2.setUsername(usernameOne);
        form2.setPassword(null);
        assertFalse(form1.equals(form2), "Equals is broken");
        assertFalse(form2.equals(form1), "Equals is broken");
        assertFalse(form1.hashCode() == form2.hashCode(), "HashCode is broken");

        form2.setId(null);
        form2.setUsername(null);
        form2.setPassword(null);
        assertFalse(form1.equals(form2), "Equals is broken");
        assertFalse(form2.equals(form1), "Equals is broken");
        assertFalse(form1.hashCode() == form2.hashCode(), "HashCode is broken");
    }
}
