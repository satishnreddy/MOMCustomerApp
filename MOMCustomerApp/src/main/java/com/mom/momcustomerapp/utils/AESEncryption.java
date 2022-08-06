package com.mom.momcustomerapp.utils;

import android.util.Base64;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author shailesh.lobo at 09-06-2020 16:13
 */
public class AESEncryption {

    private static final int pswdIterations = 10;
    private static final int keySize = 128;

    private static final String cypherInstance = "AES/CBC/PKCS5Padding";
    private static final String secretKeyInstance = "PBKDF2WithHmacSHA1";
    private static final String plainText = "sampleText";
    private static final String AESSalt = "m_O_s_Mbasket";
    private static final String initializationVector = "8119745113154120";

//    private static String cypherInstance = "";
//    private static String secretKeyInstance = "";
//    private static String plainText = "";
//    private static String AESSalt = "";
//    private static String initializationVector = "";

    private static final String UTF_8 = "UTF-8";

    private static void decodedata(){

//        cypherInstance = new String(Base64.decode(("QUVTL0NCQy9QS0NTNVBhZGRpbmc=").getBytes(), Base64.DEFAULT));
//        secretKeyInstance = new String(Base64.decode(("UEJLREYyV2l0aEhtYWNTSEEx").getBytes(), Base64.DEFAULT));
//        plainText = new String(Base64.decode(("c2FtcGxlVGV4dA==").getBytes(), Base64.DEFAULT));
//        AESSalt = new String(Base64.decode(("bV9PX3NfTWJhc2tldA==").getBytes(), Base64.DEFAULT));
//        initializationVector = new String(Base64.decode(("ODExOTc0NTExMzE1NDEyMA==").getBytes(), Base64.DEFAULT));

    }

    public static String encrypt(String textToEncrypt) throws Exception {

        decodedata();

        SecretKeySpec skeySpec = new SecretKeySpec(getRaw(plainText, AESSalt), "AES");
        Cipher cipher = Cipher.getInstance(cypherInstance);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(initializationVector.getBytes()));
        byte[] encrypted = cipher.doFinal(textToEncrypt.getBytes());
        return Base64.encodeToString(encrypted, Base64.DEFAULT);
    }

    public static String decrypt(String textToDecrypt) throws Exception {

        decodedata();

        byte[] encryted_bytes = Base64.decode(textToDecrypt, Base64.DEFAULT);
        SecretKeySpec skeySpec = new SecretKeySpec(getRaw(plainText, AESSalt), "AES");
        Cipher cipher = Cipher.getInstance(cypherInstance);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(initializationVector.getBytes()));
        byte[] decrypted = cipher.doFinal(encryted_bytes);
        return new String(decrypted, UTF_8);
    }

    private static byte[] getRaw(String plainText, String salt) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(secretKeyInstance);
            KeySpec spec = new PBEKeySpec(plainText.toCharArray(), salt.getBytes(), pswdIterations, keySize);
            return factory.generateSecret(spec).getEncoded();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}
