package net.droidlabs.tools.audio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import android.os.Environment;
import android.util.Log;

public class StreamRecorder
{
	private String extStorageDirectory;
	private boolean continueRecording;
	private String fileName;
	private URL url;
	private static final String TAG = "StreamRecorder";
	public StreamRecorder()
	{
		extStorageDirectory = Environment.getExternalStorageDirectory().toString();
		continueRecording = true;
	}

	public void startRecording(URL url, String fileName)
	{
		this.url = url;
		this.fileName = fileName;
		Log.d(TAG, "startRecording");
		new Thread("recordingThread")
		{
			public void run()
			{
				try
				{
					InputStream inputStream = StreamRecorder.this.url.openStream();
					File outputSource = new File(extStorageDirectory, StreamRecorder.this.fileName);
					FileOutputStream fileOutputStream = new FileOutputStream(outputSource);

					int count;
					int bytesRead = 0;

					while ((count = inputStream.read()) != -1 && continueRecording)
					{
						fileOutputStream.write(count);
						bytesRead++;
					}
					fileOutputStream.close();
				}
				catch (IOException ex)
				{

				}
			}
		}.start();
	}

	public void stopRecording()
	{
		Log.d(TAG, "stopRecording");
		this.continueRecording = false;
	}
}
