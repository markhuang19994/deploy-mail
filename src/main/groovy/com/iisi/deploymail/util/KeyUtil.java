package com.iisi.deploymail.util;

import javax.crypto.Cipher;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;
import java.security.Key;

public class KeyUtil {
    public static byte[] encrypt(byte[] source, Key publicKey, String algorithm) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(source);
    }

    public static byte[] decrypt(byte[] encrypt, Key privateKey, String algorithm) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(encrypt);
    }

    public static Key getKey(File keyFile) throws Exception {
        return getKey(new FileInputStream(keyFile));
    }

    public static Key getKey(InputStream ips) throws Exception {
        Key key;
        try (ObjectInputStream ois = new ObjectInputStream(ips)) {
            key = (Key) ois.readObject();
        }
        return key;
    }
}
