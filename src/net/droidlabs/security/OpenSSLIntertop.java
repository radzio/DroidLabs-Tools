package net.droidlabs.security;

/**
 * Created with IntelliJ IDEA.
 * User: Radek
 * Date: 27.05.12
 * Time: 11:13
 * To change this template use File | Settings | File Templates.
 */
import android.util.Log;

import java.io.File;import java.io.FileInputStream;import java.io.FileReader;
import java.security.*;
import java.security.cert.Certificate;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class OpenSSLIntertop
{
    private static boolean bcInitialized = false;


    public static String decrypt(String cipherText, String cipherKey, PrivateKey privateKey)	throws Exception
    {
        return decrypt(cipherText, cipherKey, privateKey, "UTF-8");
    }

    public static String decrypt(String cipherText, String cipherKey, PrivateKey privateKey, String charsetName)	throws Exception
    {
        byte[] plainText = null;
        byte [] plainKey = StringCodec.rsaDecrypt(Base64Coder.decode(cipherKey), privateKey);
        try
        {
            plainText = decryptRC4(plainKey, Base64Coder.decode(cipherText));
        }
        catch (NoSuchAlgorithmException e)
        {
            RC4Cipher rc4 = new RC4Cipher(plainKey);
            byte[] a = Base64Coder.decode(cipherText);
            plainText = new byte[a.length];
            for(int i = 0; i<a.length; i++)
            {
                plainText[i] = rc4.decrypt(a[i]);
            }
        }

        return new String(plainText);//, charsetName);
    }


    private static byte[] decryptRSA(byte[] cipherKey, PrivateKey privateKey) throws Exception
    {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(cipherKey);
    }

    private static byte[] decryptRC4(byte[] plainKey, byte[] cipherText)	throws Exception
    {
        SecretKey skeySpec = new SecretKeySpec(plainKey, "RC4");
        Cipher cipher = Cipher.getInstance("RC4");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        return cipher.doFinal(cipherText);
    }

}
