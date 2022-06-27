package ru.tubi.project.utilites;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.tubi.project.activity.company_my.ChengeImageActivity;
import ru.tubi.project.activity.company_my.ProductCardFillActivity;

import static ru.tubi.project.activity.company_my.ChengeImageActivity.REQUEST_UPLOADED_IMAGE_INFO;
import static ru.tubi.project.activity.company_my.ChengeImageActivity.REQUEST_UPLOAD_IMAGE;

public class UploadImageData {
    String messege = "";
    private Bitmap bitmap;
    private String imageName;
    private int request_code;
    private int product_inventory_id;
    private boolean status;

    public String uploadImageData(Bitmap bitmap,String imageName){
        this.bitmap=bitmap;
        this.imageName=imageName;

        goUpload();

        return messege;
    }

    public String uploadImageData(Bitmap bitmap,String imageName, int request_code){
        this.bitmap=bitmap;
        this.imageName=imageName;
        this.request_code=request_code;
        this.product_inventory_id=product_inventory_id;

        goUpload();


        return messege;
    }
    private void goUpload(){
        status = false;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,75,byteArrayOutputStream);
        byte[] imageInByte = byteArrayOutputStream.toByteArray();

        String encodedImage = Base64.encodeToString(imageInByte,Base64.DEFAULT);
        //String imageName = "my_image_1";

        Call<ResponsePOJO> call = RetroClient
                .getInstance()
                .getApi()
                .uploadImage(encodedImage, imageName);
        call.enqueue(new Callback<ResponsePOJO>() {
            @Override
            public void onResponse(Call<ResponsePOJO> call, Response<ResponsePOJO> response) {
                messege = "1: "+response.body().getRemarks();
                //Toast.makeText(ProductCardFillActivity.this, "1: "+response.body().getRemarks(), Toast.LENGTH_LONG).show();

                if(response.body().isStatus()){
                    status = true;
                    messege=response.body().getRemarks();

                    Log.d("A111","UploadImageData / onResponse / status=true; remarks - "+response.body().getRemarks());

                    if(request_code == REQUEST_UPLOAD_IMAGE){
                        //Log.d("A111","UploadImageData / uploadImageData / test 3");
                        REQUEST_UPLOADED_IMAGE_INFO = 1;
                    }
                }else{
                    messege=response.body().getRemarks();
                    Log.d("A111","UploadImageData / onResponse / status=false; remarks - "+response.body().getRemarks());

                    if(request_code == REQUEST_UPLOAD_IMAGE){
                        REQUEST_UPLOADED_IMAGE_INFO = 2;
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponsePOJO> call, Throwable t) {

                Log.d("A111","UploadImageData / onFailure / ошибка соединения / "+t.getMessage());
                messege="Ошибка соединения";//Network Faield
                if(request_code == REQUEST_UPLOAD_IMAGE){
                    REQUEST_UPLOADED_IMAGE_INFO = 3;
                }
                //Toast.makeText(ProductCardFillActivity.this, "Network Faield", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
