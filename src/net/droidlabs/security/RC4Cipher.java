package net.droidlabs.security;

/// The RC4 encryption method.
// <P>
// This may or may not be the real RC4 cipher.  It's based on
// code which showed up anonymously posted in sci.crypt.
// Here's the start of the article:
// <BLOCKQUOTE><PRE>
// Path: dog.ee.lbl.gov!overload.lbl.gov!lll-winken.llnl.gov!seismo!rsg1.er.usgs.gov!jobone!newsxfer.itd.umich.edu!europa.eng.gtefsd.com!howland.reston.ans.net!EU.net!sun4nl!hacktic!usenet
// From: nobody@vox.xs4all.nl (An0nYm0Us UsEr)
// Newsgroups: sci.crypt
// Subject: RC4 ?
// Date: 13 Sep 1994 21:30:36 GMT
// Organization: Global Anonymous Remail Services Ltd.
// Lines: 83
// Message-ID: &lt;3555ls$fsv@news.xs4all.nl&gt;
// NNTP-Posting-Host: xs1.xs4all.nl
// X-Comment: This message did not originate from the above address.
// X-Comment: It was automatically remailed by an anonymous mailservice.
// X-Comment: Info: usura@xs4all.nl, Subject: remailer-help
// X-Comment: Please report inappropriate use to &lt;admin@vox.xs4all.nl&gt;
//
// SUBJECT:  RC4 Source Code
//
// I've tested this.  It is compatible with the RC4 object module
// that comes in the various RSA toolkits.
// </PRE></BLOCKQUOTE>
// <P>
// It's surprisingly fast, for pure Java.  On a SPARC 20, wrapped
// in Acme.Crypto.EncryptedOutputStream or Acme.Crypto.EncryptedInputStream,
// it does around 13000 bytes/second.
// <P>
// <A HREF="/resources/classes/Acme/Crypto/Rc4Cipher.java">Fetch the software.</A><BR>
// <A HREF="/resources/classes/Acme.tar.gz">Fetch the entire Acme package.</A>
// <P>
// @see EncryptedOutputStream
// @see EncryptedInputStream

public class RC4Cipher// extends StreamCipher
{

    // Constructor, string key.
//    public RC4Cipher( String keyStr )
//    {
//        //super( 256 );	// (typically, not all key bits are used)
//        setKey( keyStr );
//    }

    // Constructor, byte-array key.
    public RC4Cipher( byte[] key )
    {
        //super( 256 );	// (typically, not all key bits are used)
        setKey( key );
    }

    // Key routines.

    private byte[] state = new byte[256];
    private int x, y;

    /// Set the key.
    public void setKey( byte[] key )
    {
        int index1;
        int index2;
        int counter;
        byte temp;

        for ( counter = 0; counter < 256; ++counter )
            state[counter] = (byte) counter;
        x = 0;
        y = 0;
        index1 = 0;
        index2 = 0;
        for ( counter = 0; counter < 256; ++counter )
        {
            index2 = ( key[index1] + state[counter] + index2 ) & 0xff;
            temp = state[counter];
            state[counter] = state[index2];
            state[index2] = temp;
            index1 = ( index1 + 1 ) % key.length;
        }
    }

    /// Encrypt a byte.
    public byte encrypt( byte clearText )
    {
        return (byte) ( clearText ^ state[nextState()] );
    }

    /// Decrypt a byte.
    public byte decrypt( byte cipherText )
    {
        return (byte) ( cipherText ^ state[nextState()] );
    }

    /// Encrypt some bytes.
    public void encrypt( byte[] clearText, int clearOff, byte[] cipherText, int cipherOff, int len )
    {
        for ( int i = 0; i < len; ++i )
            cipherText[cipherOff + i] =
                    (byte) ( clearText[clearOff + i] ^ state[nextState()] );
    }

    /// Decrypt some bytes.
    public void decrypt( byte[] cipherText, int cipherOff, byte[] clearText, int clearOff, int len )
    {
        for ( int i = 0; i < len; ++i )
            clearText[clearOff + i] =
                    (byte) ( cipherText[cipherOff + i] ^ state[nextState()] );
    }

    private int nextState()
    {
        byte temp;

        x = ( x + 1 ) & 0xff;
        y = ( y + state[x] ) & 0xff;
        temp = state[x];
        state[x] = state[y];
        state[y] = temp;
        return ( state[x] + state[y] ) & 0xff;
    }

}