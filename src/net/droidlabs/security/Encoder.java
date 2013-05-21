package net.droidlabs.security;

/**
 * Created with IntelliJ IDEA.
 * User: Radek
 * Date: 27.05.12
 * Time: 11:50
 * To change this template use File | Settings | File Templates.
 */
import java.io.IOException;
import java.io.OutputStream;

/**
 * Encode and decode byte arrays (typically from binary to 7-bit ASCII
 * encodings).
 */
public interface Encoder
{
    int encode(byte[] data, int off, int length, OutputStream out) throws IOException;

    int decode(byte[] data, int off, int length, OutputStream out) throws IOException;

    int decode(String data, OutputStream out) throws IOException;
}
