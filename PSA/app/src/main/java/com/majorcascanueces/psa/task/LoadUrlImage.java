package com.majorcascanueces.psa.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.majorcascanueces.psa.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class LoadUrlImage extends AsyncTask<String, Void, Bitmap> {
    private final ImageView imageView;

    public LoadUrlImage(ImageView imageView) {
        this.imageView = imageView;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        String url = urls[0];
        Bitmap bitmap = null;
        try {
            InputStream inputStream = new URL(url).openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        if (result != null) {
            imageView.setImageBitmap(result);
        } else {
            imageView.setImageResource(R.mipmap.ic_launcher_round);
        }
    }
}