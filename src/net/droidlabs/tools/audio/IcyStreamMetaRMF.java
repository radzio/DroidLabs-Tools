package net.droidlabs.tools.audio;

/**
 * Created with IntelliJ IDEA.
 * User: Radek
 * Date: 05.05.12
 * Time: 20:59
 * To change this template use File | Settings | File Templates.
 */

import java.net.URL;
import java.net.URLConnection;

/**
 * Get the Title of a stream using the Shoutcast Metadata Protocol From:
 * http://uniqueculture.net/2010/11/stream-metadata-plain-java/
 */


public class IcyStreamMetaRMF extends IcyStreamMetaBase
{
    public IcyStreamMetaRMF(URL streamUrl)
    {
        super(streamUrl);
    }

    @Override
    protected void setConnectionSettings(URLConnection con)
    {
        con.setRequestProperty("Icy-MetaData", "1");
        con.setRequestProperty("Connection", "close");
    }
}
