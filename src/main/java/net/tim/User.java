package net.tim;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

public class User {
    private final String username;
    private final byte[] salt;
    private final byte[] iv;
    private final String hashedPassword;

    public User(String username, String hashedPassword, byte[] salt, byte[] iv) {
        this.username = username;
        this.salt = salt;
        this.iv = iv;
        this.hashedPassword = hashedPassword;
    }

    public User(String username,  String password) {
        this.username = username;
        this.salt = EncryptionService.getSalt().getBytes();
        this.iv = EncryptionService.generateIv().getIV();
        try {
            this.hashedPassword = EncryptionService.getHash(password, Arrays.toString(salt));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUsername() {
        return username;
    }

    public byte[] getSalt() {
        return salt;
    }

    public byte[] getIv() {
        return iv;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }
}
