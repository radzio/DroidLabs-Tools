package net.droidlabs.cache;

import java.lang.ref.SoftReference;
import java.util.WeakHashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class BitmapCache
{
	private  WeakHashMap<String, SoftReference<Bitmap>> mCache = new WeakHashMap<String, SoftReference<Bitmap>>();
	private String storage;

	
	public BitmapCache(String storageCatalog)
	{
		this.storage = storageCatalog;
	}
	
    public  Bitmap getBitmap(String key)
    {
        if (key == null)
        {
            return null;
        }
        if (mCache.containsKey(key))
        {
            SoftReference<Bitmap> reference = mCache.get(key);
            Bitmap bitmap = reference.get();
            if (bitmap != null)
            {
                return bitmap;
            }
            return decodeFile(key);
        }
        // the key does not exists so it could be that the
        // file is not downloaded or decoded yet...
        return decodeFile(key);

    }

    private  Bitmap decodeFile(String key)
    {
        Bitmap bitmap = BitmapFactory.decodeFile(storage + key);
        mCache.put(key, new SoftReference<Bitmap>(bitmap));
        return bitmap;
    }


    public  Drawable getImageDrawable(String file)
    {
        Bitmap bitmapImage = getBitmap(file);
        return new BitmapDrawable(bitmapImage);
    }
}
