/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author satish
 */
/*
 * Copyright (C) 2011 www.itcsolutions.eu
 *
 * This file is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 2.1, or (at your
 * option) any later version.
 *
 * This file is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 *
 */
/**
 *
 * @author Catalin - www.itcsolutions.eu
 * @version june 2011
 *
 */
package com.mswipetech.sdk.network.util;


import java.security.GeneralSecurityException;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;


public class DESede_BC {


    public static int MAX_KEY_LENGTH = DESedeKeySpec.DES_EDE_KEY_LEN;
    private static String ENCRYPTION_KEY_TYPE = "DESede";
    private static String ENCRYPTION_ALGORITHM = "DESEDE/ECB/PKCS7Padding";
    private SecretKeySpec keySpec = null;

    public DESede_BC(byte[] keyBytes)
    {
        //keyBytes = padKeyToLength(keyBytes, MAX_KEY_LENGTH);
        keySpec = new SecretKeySpec(keyBytes, ENCRYPTION_KEY_TYPE);
    }

    // !!! - see post below
    private byte[] padKeyToLength(byte[] key, int len) {
        byte[] newKey = new byte[len];
        System.arraycopy(key, 0, newKey, 0, Math.min(key.length, len));
        return newKey;
    }

    // standard stuff
    public byte[] encrypt(byte[] unencrypted) throws GeneralSecurityException {
        return doCipher(unencrypted, Cipher.ENCRYPT_MODE);
    }

    public byte[] decrypt(byte[] encrypted) throws GeneralSecurityException {
        return doCipher(encrypted, Cipher.DECRYPT_MODE);
    }

    private byte[] doCipher(byte[] original, int mode) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        // IV = 0 is yet another issue, we'll ignore it here
        //IvParameterSpec iv = new IvParameterSpec(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 });
        cipher.init(mode, keySpec);
        return cipher.doFinal(original);
    }


    public static byte[] getBytes() {
        byte[] bkey = null;
        long seed = System.currentTimeMillis();
        Random rand = new Random(seed);
        int ictr = 0;
        StringBuffer sb = new StringBuffer();
        do {
            int key = rand.nextInt(126);
            if (key > 32) {
                // newKey[ictr] = new String(ictr + "").getBytes()[0] ;
                sb.append((char) key);
                ictr++;
            }
        }
        while (ictr < 24);

        try {
            bkey = sb.toString().getBytes("UTF-8");
        } catch (Exception ex) {
        }
        sb = null;


        return bkey;
    }



    public String urlEncode(String sUrl) {
        StringBuffer urlOK = new StringBuffer();
        for (int i = 0; i < sUrl.length(); i++) {
            char ch = sUrl.charAt(i);
            switch (ch) {
                case '<':
                    urlOK.append("%3C");
                    break;
                case '>':
                    urlOK.append("%3E");
                    break;
                case '/':
                    urlOK.append("%2F");
                    break;
                case ' ':
                    urlOK.append("%20");
                    break;
                case ':':
                    urlOK.append("%3A");
                    break;
                case '-':
                    urlOK.append("%2D");
                    break;
                case '+':
                    urlOK.append("%2B");
                    break;
                case '=':
                    urlOK.append("%3D");
                    break;
                default:
                    urlOK.append(ch);
                    break;
            }
        }
        return urlOK.toString();
    }


    public static String randomKey() {
        Random rand = new Random();
        StringBuffer randomChars = new StringBuffer();
        //StringBuffer randomInt = new StringBuffer();
        int noOfChars = 20;
        int ictr = 0;
        while (ictr < noOfChars) {
            int numNoRange = rand.nextInt(126);
            if ((numNoRange >= 48 && numNoRange <= 57) || (numNoRange >= 65 && numNoRange <= 90) || (numNoRange >= 97 && numNoRange <= 122)) {
                randomChars.append((char) numNoRange);
                //randomInt.append("-" + numNoRange);
                ictr++;

            }

        }
        return randomChars.toString();
    }

    public static String randomKey(int len) {
        Random rand = new Random();
        StringBuffer randomChars = new StringBuffer();
        //StringBuffer randomInt = new StringBuffer();
        int noOfChars = 24;
        int ictr = 0;
        while (ictr < noOfChars) {
            int numNoRange = rand.nextInt(126);
            if ((numNoRange >= 48 && numNoRange <= 57) || (numNoRange >= 65 && numNoRange <= 90) || (numNoRange >= 97 && numNoRange <= 122)) {
                randomChars.append((char) numNoRange);
                //randomInt.append("-" + numNoRange);
                ictr++;

            }

        }
        return randomChars.toString();

    }





}


