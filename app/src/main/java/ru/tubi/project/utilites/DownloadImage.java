package ru.tubi.project.utilites;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;

import ru.tubi.project.R;

public class DownloadImage  extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;


    public DownloadImage(ImageView bmImage) {
        this.bmImage = bmImage;
    }
    public DownloadImage() {
    }


    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {

            Log.e("Ошибка передачи",e.getMessage());//"Ошибка передачи изображения"
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {

            try {
                int check = result.getWidth();
                // bitmap не пустой изображение есть
                    bmImage.setImageBitmap(result);

            } catch (Exception w) {
                //bitmap пустой image не найден
                bmImage.setImageResource(R.drawable.tubi_logo_no_image_300ps);
            }

    }

}
