package net.droidlabs.security;

import android.content.Context;
import net.droidlabs.tools.R;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created with IntelliJ IDEA.
 * User: Radek
 * Date: 27.05.12
 * Time: 10:12
 * To change this template use File | Settings | File Templates.
 */
public class RsaHelper
{
    private Context context;

    public RsaHelper(Context context)
    {
        this.context = context;
    }

    public String encrypt(String source)
    {
        try
        {
            return new String(Base64Coder.encode(StringCodec.rsaEncrypt(source.getBytes(), this.getPublicKey())));
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
            byte [] c = new byte[0];
            c = StringCodec.rsaDecrypt(Base64Coder.decode(source), this.getPrivateKey());
            return new String(c);
        }
        catch (Exception e)
        {
            return "";
        }

    }

    public PublicKey getPublicKey() throws Exception
    {
        InputStream instream = context.getResources().openRawResource(R.raw.pk);

        byte[] encodedKey = new byte[instream.available()];
        instream.read(encodedKey);
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey pkPublic = kf.generatePublic(publicKeySpec);
        return pkPublic;

    }

    public PrivateKey getPrivateKey() throws Exception
    {
        InputStream instream = context.getResources().openRawResource(R.raw.prk);

        byte[] encodedKey = new byte[instream.available()];
        instream.read(encodedKey);
        PKCS8EncodedKeySpec publicKeySpec = new PKCS8EncodedKeySpec(encodedKey);

        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey pkPublic = kf.generatePrivate(publicKeySpec);
        return pkPublic;

    }

}
