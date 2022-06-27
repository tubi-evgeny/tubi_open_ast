package ru.tubi.project.utilites;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.io.InputStream;

public class MakeImageOrientation {
    private Bitmap bitmap;
    private Context context;

    //повернуть картинку
    @RequiresApi(api = Build.VERSION_CODES.N)
    public Bitmap makeImageOrientation(Context context,Bitmap bitmap, Uri uri){
        this.context=context;
        Matrix matrix = new Matrix();
        //получаем ориентацию картинки портрет или альбом
        int exifRotation = getExifOrientation(uri);
        //переворачивает картинку
        int orientatyon = exifToDegrees(exifRotation);
        matrix.setRotate(orientatyon);

        //размер фото
        int width=(int)(bitmap.getWidth());
        int height=bitmap.getHeight();

        //переворачиваем картинку
        Bitmap resizedbitmap=Bitmap.createBitmap(
                bitmap,0,0, width, height,matrix, true);

        return resizedbitmap;
    }
    //получаем ориентацию картинки портрет или альбом
    @RequiresApi(api = Build.VERSION_CODES.N)
    private int getExifOrientation(Uri uri) {

        ExifInterface exif;
        int orientation = 0;
        //Uri uri = data.getData();
        InputStream in;
        try {
            in = context.getContentResolver().openInputStream(uri);
            exif = new ExifInterface(in);

            orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 1 );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        return orientation;
    }
    //переворачивает картинку
    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }
        return 0;
    }
}
