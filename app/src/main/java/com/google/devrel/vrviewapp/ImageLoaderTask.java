package com.google.devrel.vrviewapp;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.google.vr.sdk.widgets.pano.VrPanoramaView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

public class ImageLoaderTask extends AsyncTask<AssetManager, Void, Bitmap> {

    private static final String TAG = "ImageLoaderTask";
    private final String assetName;
    private final WeakReference<VrPanoramaView> viewReference;
    private final VrPanoramaView.Options viewOptions;

    private static WeakReference<Bitmap> lastBitmap = new WeakReference<>(null);
    private static String lastAssetName;

    public ImageLoaderTask(VrPanoramaView view, VrPanoramaView.Options viewOptions, String assetName) {
        viewReference = new WeakReference<>(view);
        this.viewOptions = viewOptions;
        this.assetName = assetName;
    }

    public Bitmap doInBackground (AssetManager ...params) {
        AssetManager assetManager = params[0];

        if (assetName.equals(lastAssetName) && lastBitmap.get() != null) {
            return lastBitmap.get();
        }

        try (InputStream istr = assetManager.open(assetName)) {
            Bitmap bitmap = BitmapFactory.decodeStream(istr);
            lastBitmap = new WeakReference<>(bitmap);
            lastAssetName = assetName;
            return bitmap;
        } catch (IOException e) {
            Log.e(TAG, "Could not decode default bitmap: " + e);
            return null;
        }
    }

    @Override
    public void onPostExecute (Bitmap bitmap) {
        final VrPanoramaView vw = viewReference.get();
        if (vw != null && bitmap != null) {
            vw.loadImageFromBitmap(bitmap, viewOptions);
        }
    }
}
