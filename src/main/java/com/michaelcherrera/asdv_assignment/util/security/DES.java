package com.michaelcherrera.asdv_assignment.util.security;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;

public class DES {

    private int interval = 1; //space between chars of password
    //in the 4096 bytes

    //added to all users before encryption 
    private final String _888 = "ΙΗΣΟΥΣ ΧΡΙΣΤΟΣ ΝΙΚΑ";

    /**
     * Encrypts the parameter data using the DES key parameter.
     *
     * @param data   to be encrypted
     * @param desKey key to be used for encryption
     * @return encrypted string
     * @throws InvalidKeyException       if DESKeySpec is invalid
     * @throws NoSuchAlgorithmException  if Cipher.getInstance("DES") is invalid
     * @throws InvalidKeySpecException   SecretKey exception
     * @throws NoSuchPaddingException    Cipher exception
     * @throws IllegalBlockSizeException Base64 exception
     * @throws BadPaddingException       Base64 exception
     */
    public String encrypt(String data, byte[] desKey)
            throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, IllegalBlockSizeException,
            NoSuchPaddingException, BadPaddingException {

        String encryptedData;

        SecureRandom sr = new SecureRandom();
        DESKeySpec deskey = new DESKeySpec(desKey);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(deskey);

        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, key, sr);
        encryptedData = Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));

        return encryptedData;
    }

    /**
     * Decrypts the cryptData using the DesKey
     *
     * @param cryptData - data to be decrypted
     * @param desKey    - DES key
     * @return the decryption of the data cryptData
     * @throws InvalidKeyException       if DESKeySpec is invalid
     * @throws NoSuchAlgorithmException  if Cipher.getInstance("DES") is invalid
     * @throws InvalidKeySpecException   SecretKey exception
     * @throws NoSuchPaddingException    Cipher exception
     * @throws IllegalBlockSizeException Base64 exception
     * @throws BadPaddingException       Base64 exception
     */
    public String decryptDes(String cryptData, byte[] desKey)
            throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException {

        String decryptedData;

        SecureRandom sr = new SecureRandom();
        DESKeySpec deskey = new DESKeySpec(desKey);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(deskey);
        Cipher cipher = Cipher.getInstance("DES");

        cipher.init(Cipher.DECRYPT_MODE, key, sr);
        decryptedData = new String(cipher.doFinal(Base64.getDecoder().decode(cryptData)));

        return decryptedData;
    }

    /**
     * Creates a byte array out of hash codes of user name and password, after xOring and rotating different bits
     *
     * @param userName use name
     * @param password password
     * @return byte array of username and password
     */
    private byte[] salt(String userName, String password) {

        final int SIZE = userName.length() + password.length(); byte[] key = new byte[SIZE];

        int hcUserName = userName.hashCode(); hcUserName = Integer.rotateRight(SIZE, hcUserName);

        int hcPassword = password.hashCode(); hcPassword = Integer.rotateRight(SIZE, hcPassword);

        for (int i = 0; i < userName.length(); ++i) {
            int x = (hcUserName ^ (hcPassword * (i + 23) * 88)); if (x < 128 && x >= -128) key[i] = (byte) x;
            else key[i] = (byte) (x % 0xff);
        }

        for (int i = userName.length() - 1; i < SIZE; ++i) {
            int x = (hcPassword ^ (hcPassword * i * 8)); if (x < 128 && x >= -128) key[i] = (byte) x;
            else key[i] = (byte) (x % 0xff);
        }

        for (int i = 0; i < key.length; ++i) key[i] = (byte) (key[i] ^ 0b10101010);

        return key;
    }

    /**
     * A manipulates the hash code of String instance var _888 and converts it to bytes array.
     *
     * @return instance var _888 manipulated in byte[] form
     */
    private byte[] cross() {

        final int SIZE = _888.length();
        byte[] key = new byte[SIZE];

        int hc_888 = _888.hashCode();

        for (int i = 0; i < _888.length(); ++i) {
            int x = (hc_888 ^ (hc_888 * (i + 8) * 88));
            if (x < 128 && x >= -128)
                key[i] = (byte) x;
            else
                key[i] = (byte) (x % 0xff);
        } return key;
    }

    /**
     * The password is embedded inside a char[] of size 4096. It can be called multiple times for the same password.
     *
     * @param password the password to be embedded into the byte[]
     * @return the byte[] with the embedded password
     */
    public char[] passwordTo4096(String password) {

        int size = 4096; char[] pass4096 = new char[size];
        //>create random array with random values -128 to 127
        for (int i = 0; i < size; ++i)
            pass4096[i] = (char) (-128 + ((int) (Math.random() * 256)));
        //password length
        int length = password.length();

        //>set an interval to use for jumping accross the 4096 array
        //The interval varies depending on the length of the password.
        if (length > 128)//password length
            interval = 64;
        else if (length > 64)
            interval = 8;
        else if (length > 32)
            interval = 4;
        else interval = 1;

        //>store the length of the password at position 4095
        pass4096[size - 1] = (char) length;
        int v = pass4096[size - 1];
        //place each char of the password in the char[] at different positions
        for (int i = 0; i < length; ++i)
            pass4096[i * (length / interval)] = password.charAt(i);

        return pass4096;
    }

    /**
     * Retrieves the embedded password from the into a 4096 char[] The instance var interval was set in method
     * passwordTo4096 during encryption.
     *
     * @param password4096     the array the has the password embedded in it.
     * @param lengthOfPassword the length of the password.
     */
    public String _4096toPassword(char[] password4096, int lengthOfPassword) {

        StringBuilder s = new StringBuilder();

        for (int i = 0; i < lengthOfPassword; ++i)
            s.append(password4096[i * (lengthOfPassword / interval)]);

        return s.toString();
    }

    /**
     * Encrypt the password using DES algorithm,salt and pepper.
     */
    public String encryptDES(String userName, String password)
            throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, IllegalBlockSizeException,
            NoSuchPaddingException, BadPaddingException {

        //>generate the cross that is used in all users
        byte[] cross = cross();//pepper

        //>generate the salt that is individual for each user 
        //and depends on the user password
        byte[] salt = salt(userName, password);
        byte[] saltAndCross = new byte[salt.length + cross.length];

        //> put salt and pepper into array
        System.arraycopy(salt, 0, saltAndCross, 0, salt.length);
        for (int i = salt.length, j = 0;
             i < salt.length + cross.length; ++i) saltAndCross[i] = cross[j++];

        //>Generate a DES key using salt,pepper
        byte[] desKey = Arrays.copyOf(saltAndCross, saltAndCross.length);
        //> encrypt the salt, pepper  and password
        String encryptedPassword = encrypt(password, desKey);
        return encryptedPassword;
    }

    /**
     * Generates a DES key which is specific to the userName and password The caller of the method decryptDES must call
     * this method and use this DES key to decrypt.
     *
     * @param userName user name
     * @param password password
     * @return the DES key
     */
    public byte[] getDESkeyForUserNameAndPassword(String userName, String password) {

        byte[] cross = cross(); byte[] salt = salt(userName, password);
        byte[] saltAndCross = new byte[salt.length + cross.length];

        System.arraycopy(salt, 0, saltAndCross, 0, salt.length);
        for (int i = salt.length, j = 0; i < salt.length + cross.length; ++i)
            saltAndCross[i] = cross[j++];

        byte[] desKey = Arrays.copyOf(saltAndCross, saltAndCross.length);
        return desKey;
    }
}