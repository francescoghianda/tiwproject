package it.polimi.tiw.authentication.security;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class PBKDF2WithHmacSHA512
{
    private static PBKDF2WithHmacSHA512 instance = new PBKDF2WithHmacSHA512();

    private static final int ITERATION = 1000;
    private static final int HASH_BYTE_LENGTH = 64;
    private static final int SALT_BYTE_LENGTH = 16;

    private SecretKeyFactory skt;
    private SecureRandom secureRandom;

    private PBKDF2WithHmacSHA512()
    {
        try
        {
            this.skt = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
            secureRandom = SecureRandom.getInstance("SHA1PRNG");
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
    }

    public static PBKDF2WithHmacSHA512 getInstance()
    {
        return instance;
    }

    public String hashPassword(String password)
    {
        byte[] salt = new byte[SALT_BYTE_LENGTH];

        secureRandom.nextBytes(salt);

        byte[] hash = generateHash(password, HASH_BYTE_LENGTH, salt, ITERATION);

        String saltString = DatatypeConverter.printHexBinary(salt);
        String hashString = DatatypeConverter.printHexBinary(hash);

        return ITERATION+":"+saltString+":"+hashString;
    }

    public boolean validate(String password, String hashedPassword)
    {
        String[] hashArray = hashedPassword.split(":");
        if(hashArray.length != 3)throw new IllegalArgumentException("Invalid hash! ("+hashedPassword+")");
        int iteration = Integer.parseInt(hashArray[0]);
        byte[] salt = DatatypeConverter.parseHexBinary(hashArray[1]);
        byte[] hash = DatatypeConverter.parseHexBinary(hashArray[2]);

        byte[] newHash = generateHash(password, hash.length, salt, iteration);

        return compare(hash, newHash);
    }

    private boolean compare(byte[] hash1, byte[] hash2)
    {
        int diff = hash1.length ^ hash2.length;
        for(int i = 0; i < hash1.length && i < hash2.length; i++) diff |= hash1[i] ^ hash2[i];
        return diff == 0;
    }

    private byte[] generateHash(String password, int hashByteLength, byte[] salt, int iteration)
    {
        try
        {
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iteration, hashByteLength*8);
            return skt.generateSecret(spec).getEncoded();
        }
        catch (InvalidKeySpecException e)
        {
            e.printStackTrace();
            throw new RuntimeException("Error during password hashing");
        }
    }


}


