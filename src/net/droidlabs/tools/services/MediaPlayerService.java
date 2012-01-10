package net.droidlabs.tools.services;

import java.io.File;
import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

public class MediaPlayerService extends Service implements OnPreparedListener, OnCompletionListener, OnBufferingUpdateListener
{
	private static final String TAG = "MediaPlayerService";
	MediaPlayer player;

	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}

	@Override
	public void onCreate()
	{
		player = new MediaPlayer();
		player.setLooping(false); // Set looping
	}

	@Override
	public void onDestroy()
	{
		player.stop();
		player.reset();
	}

	@Override
	public void onStart(Intent intent, int startid)
	{
		Toast.makeText(this, "My Service Started", Toast.LENGTH_LONG).show();
		Log.d(TAG, "onStart");

		player.setAudioStreamType(AudioManager.STREAM_MUSIC);
		player.setOnPreparedListener(this);
		player.setOnBufferingUpdateListener(this);
		player.setOnCompletionListener(this);
		player.setOnPreparedListener(this);
		player.setWakeMode(getBaseContext(), PowerManager.PARTIAL_WAKE_LOCK);
		try
		{
			//player.setDataSource("http://217.74.72.3:8000/rmf_90s");
			String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
	        File outputSource = new File(extStorageDirectory, "er.mp3");
	        player.setDataSource(outputSource.toString());
			//rtsp://stream85.polskieradio.pl/live/pr3.sdp
			//http://217.74.72.3:8000/rmf_90s
		}
		catch (IllegalArgumentException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IllegalStateException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try
		{
			player.prepareAsync();
		}
		catch (IllegalStateException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void onPrepared(MediaPlayer mpa) 
	{
	    synchronized(this)
	    {
	    	mpa.start();
	    }
	}
	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) 
	{
		// TODO Auto-generated method stub
		Log.i(TAG, "% " + percent);
		
	}
	@Override
	public void onCompletion(MediaPlayer arg0) 
	{
		// TODO Auto-generated method stub
		
		
	}
	
}