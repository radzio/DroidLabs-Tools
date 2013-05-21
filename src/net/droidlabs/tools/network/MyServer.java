package net.droidlabs.tools.network;

import java.io.IOException;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: Radek Piekarz
 * Date: 06.09.12
 * Time: 21:44
 */
public class MyServer extends NanoHTTPD
{
    public MyServer() throws IOException
    {
        super(8080, null);
    }

    public Response serve( String uri, String method, Properties header, Properties parms, Properties files )
    {
        System.out.println( method + " '" + uri + "' " );
        String msg = "<html><body><h1>Hello server</h1>\n";
        if ( parms.getProperty("username") == null )
            msg +=
                    "<form action='?' method='get'>\n" +
                            "  <p>Your name: <input type='text' name='username'></p>\n" +
                            "</form>\n";
        else
            msg += "<p>Hello, " + parms.getProperty("username") + "!</p>";

        msg += "</body></html>\n";
        return new NanoHTTPD.Response( HTTP_OK, MIME_HTML, msg );
    }
}