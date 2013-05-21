package net.droidlabs.security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created with IntelliJ IDEA.
 * User: Radek Piekarz
 * Date: 08.08.12
 * Time: 19:20
 */
public class AesHelper
{

    private byte[] rawKey;

    public AesHelper(String key)
    {
          this(key.getBytes());
    }

    public AesHelper(byte[] key)
    {
       this.rawKey = key;
    }

    public String encrypt(String source)
    {
        try
        {
            SecretKeySpec skeySpec = new SecretKeySpec(this.rawKey, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted;
            encrypted = cipher.doFinal(source.getBytes());
            return new String(Base64Coder.encode(encrypted));
        }
        catch (Exception e)
        {
            return null;
        }

    }

    public String decrypt(String source)
    {

        try
        {
            SecretKeySpec skeySpec = new SecretKeySpec(this.rawKey, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] decrypted;
            decrypted = cipher.doFinal(Base64Coder.decode(source));
            return new String(decrypted);
        }
        catch (Exception e)
        {
            return null;
        }

    }
}
