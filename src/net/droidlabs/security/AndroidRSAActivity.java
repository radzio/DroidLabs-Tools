package net.droidlabs.security;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class AndroidRSAActivity extends Activity
{
	
	TextView tv ;
	TextView tv2 ;
	EditText tv3 ; 
	/** Called when the activity is first created. */
	@Override

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
	}

//	public void send(View view)
//	{
//		try
//		{
//			String aa = new String(Base64Coder.encode(StringCodec.rsaEncrypt(tv3.getText().toString().getBytes(), this.getPublicKey())));
//
//
//
//			HttpClient httpclient = new DefaultHttpClient();
//    	    HttpPost httppost = new HttpPost("http://droidlabs.net/api/index.php");
//
//    	    try
//    	    {
//    	        // Add your data
//    	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//    	        nameValuePairs.add(new BasicNameValuePair("a", aa));
//    	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//    	        // Execute HTTP Post Request
//    	        HttpResponse response = httpclient.execute(httppost);
//    	        InputStream ips  = response.getEntity().getContent();
//    	        BufferedReader buf = new BufferedReader(new InputStreamReader(ips));
//    	        if(response.getStatusLine().getStatusCode()!=HttpStatus.SC_OK)
//    	        {
//    	            throw new Exception(response.getStatusLine().getReasonPhrase());
//    	        }
//    	        StringBuilder sb = new StringBuilder();
//    	        String s;
//    	        while(true )
//    	        {
//    	            s = buf.readLine();
//    	            if(s==null || s.length()==0)
//    	                break;
//    	            sb.append(s);
//
//    	        }
//    	        buf.close();
//    	        ips.close();
//
//    	        byte [] c = StringCodec.rsaDecrypt(Base64Coder.decode(sb.toString()), this.getPrivateKey());
//    			tv2.setText(new String(c));
//    			Log.e("RSA", "Send  = " + aa);
//    			Log.e("RSA", "Received  = " + sb.toString());
//    			Log.e("RSA", new String(c));
//
//    	    }
//    	    catch (ClientProtocolException e)
//    	    {
//    	        // TODO Auto-generated catch block
//    	    }
//    	    catch (IOException e)
//    	    {
//    	        // TODO Auto-generated catch block
//    	    }
//
//
//
//
//		}
//		catch (Exception e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	private PublicKey getPublicKey() throws Exception
//	{
//		InputStream instream = getResources().openRawResource(R.raw.public_key);
//
//		byte[] encodedKey = new byte[instream.available()];
//		instream.read(encodedKey);
//		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedKey);
//		KeyFactory kf = KeyFactory.getInstance("RSA");
//		PublicKey pkPublic = kf.generatePublic(publicKeySpec);
//		return pkPublic;
//
//	}
//
//	private PrivateKey getPrivateKey() throws Exception
//	{
//		InputStream instream = getResources().openRawResource(R.raw.private_key);
//
//		byte[] encodedKey = new byte[instream.available()];
//		instream.read(encodedKey);
//		PKCS8EncodedKeySpec publicKeySpec = new PKCS8EncodedKeySpec(encodedKey);
//
//		KeyFactory kf = KeyFactory.getInstance("RSA");
//		PrivateKey pkPublic = kf.generatePrivate(publicKeySpec);
//		return pkPublic;
//
//	}

	

}