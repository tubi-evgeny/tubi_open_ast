package ru.tubi.project.utilites;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import ru.tubi.project.R;

public class MakeImageToSquare {
    private Context context;
    private ImageView imageView;
    //задаем размер фото для квадрата и показываем
    public MakeImageToSquare(Context context, Bitmap bitmap, ImageView imageView){
        this.context=context;
        this.imageView=imageView;
        try{
            int bmt_height  = bitmap.getHeight();

            Bitmap resizedbitmap = makeBitmap(bitmap);

            this.imageView.setImageBitmap(resizedbitmap);
        }catch(Exception ex){
            this.imageView.setImageResource(R.drawable.tubi_logo_no_image_300ps);
            Log.d("A111","MakeImageToSquare / exception = "+ex);
            Log.d("A111","MakeImageToSquare / ERROR  bitmap isEmpty");
        }

    }
    //задаем размер фото для квадрата и показываем
    public MakeImageToSquare(Bitmap bitmap, ImageView imageView){
        this.imageView=imageView;
        try{
            int bmt_height  = bitmap.getHeight();

            Bitmap resizedbitmap = makeBitmap(bitmap);

            this.imageView.setImageBitmap(resizedbitmap);
        }catch(Exception ex){
            this.imageView.setImageResource(R.drawable.tubi_logo_no_image_300ps);
            Log.d("A111","MakeImageToSquare 2 / exception = "+ex);
            Log.d("A111","MakeImageToSquare 2 / ERROR  bitmap isEmpty");
        }
    }

    private Bitmap makeBitmap(Bitmap bitmap){
        int width = (int) (bitmap.getWidth());
        int height = bitmap.getHeight();
        int finish_size;
        int x = 0 , y = 0;
        if(width > height){
            finish_size=height;
            x = (width - finish_size)/2;
        }else {
            finish_size = width;
            y= (height - finish_size)/2;
        }
        //новый bitmap обрезаем в квадрат
        Bitmap resizedbitmap=Bitmap.createBitmap(
                bitmap,x,y, finish_size, finish_size);
        return resizedbitmap;
    }
}
