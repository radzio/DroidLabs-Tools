package net.droidlabs.tools.audio;

import java.net.URL;
import java.net.URLConnection;

/**
 * Get the Title of a stream using the Shoutcast Metadata Protocol From:
 * http://uniqueculture.net/2010/11/stream-metadata-plain-java/
 */


public class IcyStreamMeta extends IcyStreamMetaBase
{
    public IcyStreamMeta(URL streamUrl)
    {
        super(streamUrl);
    }

    @Override
    protected void setConnectionSettings(URLConnection con)
    {
        con.setRequestProperty("Icy-MetaData", "1");
        con.setRequestProperty("Connection", "close");
        con.setRequestProperty("Accept", null);
    }
}