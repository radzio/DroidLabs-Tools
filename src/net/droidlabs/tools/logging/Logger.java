package net.droidlabs.tools.logging;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Radek Piekarz
 * Date: 05.09.12
 * Time: 17:10
 */
public class Logger
{
    public static final int ERROR = 5;
    public static final int WARN = 4;
    public static final int INFO = 3;
    public static final int DEBUG = 2;
    public static final int VERBOSE = 1;
    public static final int DISABLED = 0;


    private static final String TAG = Logger.class.getSimpleName();

    private static int CURRENT_LEVEL = DISABLED;
    private static boolean LOG_EXTERNALLY = false;
    private static int VERSION_CODE = 0;
    private static String VERSION_NAME;
    private static String MODEL;
    private static String RELEASE;
    private static String REPORT_ADDRESS;


    public static void init(Context context, String reportingAddress)
    {

        PackageManager manager = context.getPackageManager();
        try
        {
            PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), 0);
            VERSION_CODE = packageInfo.versionCode;
            VERSION_NAME = packageInfo.versionName;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            VERSION_CODE = 0;
            VERSION_NAME = "no-set";
        }


        REPORT_ADDRESS = reportingAddress;

        // Device model
        MODEL = android.os.Build.MODEL;

        // Android version
        RELEASE = android.os.Build.VERSION.RELEASE;
    }

    public static void setLogLevel(int currentLevel)
    {
        CURRENT_LEVEL = currentLevel;
    }

    public static void setLogExternally(boolean logExternally)
    {
        LOG_EXTERNALLY = logExternally;
    }

    public static void error(String TAG, String message)
    {
        if(CURRENT_LEVEL >= ERROR)
            Log.e(TAG, message);

        if(LOG_EXTERNALLY)
            sendReport(message);
    }

    public static void error(String TAG, String message, Throwable throwable)
    {
        if(CURRENT_LEVEL >= ERROR)
            Log.e(TAG, message, throwable);

        if(LOG_EXTERNALLY)
            sendReport(message, throwable);
    }

    public static void warn(String TAG, String message)
    {
        if(CURRENT_LEVEL >= WARN)
            Log.w(TAG, message);

        if(LOG_EXTERNALLY)
            sendReport(message);
    }

    public static void warn(String TAG, String message, Throwable throwable)
    {
        if(CURRENT_LEVEL >= WARN)
            Log.w(TAG, message, throwable);

        if(LOG_EXTERNALLY)
            sendReport(message, throwable);
    }

    public static void info(String TAG, String message)
    {
        if(CURRENT_LEVEL >= INFO)
            Log.i(TAG, message);
    }

    public static void info(String TAG, String message, Throwable throwable)
    {
        if(CURRENT_LEVEL >= INFO)
            Log.i(TAG, message, throwable);
    }

    public static void debug(String TAG, String message)
    {
        if(CURRENT_LEVEL >= DEBUG)
            Log.d(TAG, message);
    }

    public static void debug(String TAG, String message, Throwable throwable)
    {
        if(CURRENT_LEVEL >= DEBUG)
            Log.d(TAG, message, throwable);
    }

    public static void verbose(String TAG, String message)
    {
        if(CURRENT_LEVEL >= VERBOSE)
            Log.v(TAG, message);
    }

    public static void verbose(String TAG, String message, Throwable throwable)
    {
        if(CURRENT_LEVEL >= VERBOSE)
            Log.v(TAG, message, throwable);
    }



    private static void sendReport(String message)
    {
        sendReport(message, null);
    }

    private static void sendReport(Throwable throwable)
    {
        sendReport(null, throwable);
    }

    private static void sendReport(final String message, final Throwable throwable)
    {

        final String stackTrace;
        if(throwable != null)
        {
            stackTrace = getStackTrace(throwable);
        }
        else
        {
            stackTrace = "";
        }

        new Thread()
        {

            public void run()
            {

                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(REPORT_ADDRESS);

                List<BasicNameValuePair> results = new ArrayList<BasicNameValuePair>();
                results.add(new BasicNameValuePair("entry.0.single", String.valueOf(VERSION_CODE)));
                results.add(new BasicNameValuePair("entry.1.single", VERSION_NAME));
                results.add(new BasicNameValuePair("entry.2.single", MODEL));
                results.add(new BasicNameValuePair("entry.3.single", RELEASE));

                if(message == null)
                {
                    results.add(new BasicNameValuePair("entry.4.single", ""));
                }
                else
                {
                    results.add(new BasicNameValuePair("entry.4.single", message));
                }


                results.add(new BasicNameValuePair("entry.5.single", stackTrace));


                try
                {
                    post.setEntity(new UrlEncodedFormEntity(results));
                }
                catch (UnsupportedEncodingException e)
                {
                    // Auto-generated catch block
                    Log.e(TAG, "An error has occurred", e);
                }
                try
                {
                    client.execute(post);
                }
                catch (ClientProtocolException e)
                {
                    // Auto-generated catch block
                    Log.e(TAG, "client protocol exception", e);
                }
                catch (IOException e)
                {
                    // Auto-generated catch block
                    Log.e(TAG, "io exception", e);
                }
            }
        }.start();

    }

    private static String getStackTrace(Throwable throwable)
    {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        throwable.printStackTrace(printWriter);
        return writer.toString();
    }
}
