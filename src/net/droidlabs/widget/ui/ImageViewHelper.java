package net.droidlabs.widget.ui;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.widget.ImageView;
import net.droidlabs.tools.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: Radek Piekarz
 * Date: 09.08.12
 * Time: 22:42
 */
public  class ImageViewHelper
{
    public static void setImageBitmapWithFade(final ImageView imageView, final Bitmap bitmap) {
        if(bitmap.isRecycled())
        {
            Logger.warn("ImageViewHelper", "Image");
            return;
        }
            Resources resources = imageView.getResources();

        BitmapDrawable bitmapDrawable = new BitmapDrawable(resources, bitmap);
        setImageDrawableWithFade(imageView, bitmapDrawable);
    }

    public static void setImageDrawableWithFade(final ImageView imageView, final Drawable drawable) {
        Drawable currentDrawable = imageView.getDrawable();
        if (currentDrawable != null)
        {
            Drawable [] arrayDrawable = new Drawable[2];
            arrayDrawable[0] = currentDrawable;
            arrayDrawable[1] = drawable;
            TransitionDrawable transitionDrawable = new TransitionDrawable(arrayDrawable);
            transitionDrawable.setCrossFadeEnabled(true);
            imageView.setImageDrawable(transitionDrawable);
            transitionDrawable.startTransition(250);
        }
        else
        {
            imageView.setImageDrawable(drawable);
        }
    }
}
