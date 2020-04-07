package it.polimi.tiw.authentication.security;

import org.junit.Test;

import static org.junit.Assert.*;

public class PBKDF2WithHmacSHA512Test
{
    @Test
    public void validatePasswordTest()
    {
        String password = "";
        PBKDF2WithHmacSHA512 pbkdf2WithHmacSHA512 = PBKDF2WithHmacSHA512.getInstance();

        String hashedPassword = pbkdf2WithHmacSHA512.hashPassword(password);

        boolean valid = pbkdf2WithHmacSHA512.validate(password, hashedPassword);
        boolean invalid = pbkdf2WithHmacSHA512.validate(password+"0", hashedPassword);
        assertTrue(valid && !invalid);
    }
}
