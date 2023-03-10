package ru.tubi.project.activity.company_my;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import ru.tubi.project.R;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.utilites.DownloadImage;
import ru.tubi.project.utilites.GenerateImageName;
import ru.tubi.project.utilites.InitialData;
import ru.tubi.project.utilites.MakeImageOrientation;
import ru.tubi.project.utilites.MakeImageToSquare;
import ru.tubi.project.utilites.UploadImageData;
import ru.tubi.project.utilites.UserDataRecovery;

import static ru.tubi.project.activity.Config.ADMIN_PANEL_URL_IMAGES;
import static ru.tubi.project.free.AllCollor.alert_dialog_button_green_pressed;
import static ru.tubi.project.free.AllText.CHENGE;
import static ru.tubi.project.free.AllText.CLICK_AGAIN_TO_DOWNLOAD_IMAGE;
import static ru.tubi.project.free.AllText.DONT_SHOW_IT_ANYMORE;
import static ru.tubi.project.free.AllText.GO_CHENGE_IMAGE;
import static ru.tubi.project.free.AllText.IMAGE;
import static ru.tubi.project.free.AllText.IMAGE_DOWNLOAD;
import static ru.tubi.project.free.AllText.IMAGE_FOR_MODERATION_AND_COMMENT;
import static ru.tubi.project.free.AllText.IMAGE_UPLOADING_FAILED_TAXT;
import static ru.tubi.project.free.AllText.NETWORK_FAILED_TAXT;
import static ru.tubi.project.free.AllText.UNDERSTOOD_BIG;
import static ru.tubi.project.free.AllText.UPLOADING_IMAGE_TAXT;
import static ru.tubi.project.utilites.Constant.PROVIDER_OFFICE;

import static ru.tubi.project.free.VariablesHelpers.CHENGE_IMAGE_ALERT_SHOW;

public class ChengeImageActivity extends AppCompatActivity implements View.OnClickListener {

    private Intent takeit;
    private String category,productName,brand,characteristic,image_url="",currentPhotoPath;
    private String imageName;
    private int product_inventory_id;
    private long  apply_time, download_time;
    private TextView tvPhotoCamera, tvChoosePhoto, tvApply;
    private ImageView ivItWas, ivWillBe;
    private LinearLayout llImageTable,llItWas,llWillBe;
    private Bitmap bitmap, newBitmap;
    static final int IMG_REQUEST = 11;
    private AlertDialog.Builder adb;
    private AlertDialog ad;
    private UserModel userDataModel;
    private ProgressDialog asyncDialog;
    private UserDataRecovery userDataRecovery = new UserDataRecovery();
    static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int REQUEST_UPLOAD_IMAGE = 12;
    public static int REQUEST_UPLOADED_IMAGE_INFO = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chenge_image);
        setTitle(IMAGE);//???????? ????????????
        getSupportActionBar().setSubtitle(CHENGE);

        tvPhotoCamera = findViewById(R.id.tvPhotoCamera);
        tvChoosePhoto = findViewById(R.id.tvChoosePhoto);
        tvApply = findViewById(R.id.tvApply);
        ivItWas = findViewById(R.id.ivItWas);
        ivWillBe = findViewById(R.id.ivWillBe);
        llImageTable = findViewById(R.id.llImageTable);
        //llItWas = findViewById(R.id.llItWas);
       // llWillBe = findViewById(R.id.llWillBe);

        tvPhotoCamera.setOnClickListener(this);
        tvChoosePhoto.setOnClickListener(this);
        tvApply.setOnClickListener(this);
        //llWillBe.setOnClickListener(this);

        //????????????????????
        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.CAMERA}, PackageManager.PERMISSION_GRANTED);

        //???????????????? ???? sqlLite ???????????? ???????????????????????? ?? ????????????????
        userDataModel = userDataRecovery.getUserDataRecovery(this);

        tvApply.setText(""+GO_CHENGE_IMAGE);
        tvApply.setClickable(false);

        takeit = getIntent();
        category = takeit.getStringExtra("category");
        productName = takeit.getStringExtra("productName");
        brand  = takeit.getStringExtra("brand");
        characteristic = takeit.getStringExtra("characteristic");
        image_url = takeit.getStringExtra("image_url");
        product_inventory_id = takeit.getIntExtra("product_inventory_id",0);

        asyncDialog = new ProgressDialog(this);

        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = (int)(display.getHeight() * 0.87);

        llImageTable.setLayoutParams(new LinearLayout.LayoutParams(width,height));

        if(!image_url.equals("")) {
            Log.d("A111","ChengeImageActivity / onCreate / image_url = "+image_url);
            setDownloadImage(ADMIN_PANEL_URL_IMAGES + image_url);
        }else ivItWas.setImageResource(R.drawable.tubi_logo_no_image_300ps);

    }
    private void setDownloadImage(String url){
        DownloadImage bmp = new DownloadImage(){
            protected void onPostExecute(Bitmap bitmap){
                showImage(bitmap);
            }
        };
        bmp.execute(url);
       // Log.d("A111","test");
    }

    private void showImage(Bitmap bitmap){
        //???????????? ???????????? ???????? ?????? ????????????????
        new MakeImageToSquare(this,bitmap,ivItWas);
    }

    @Override
    public void onClick(View v) {
        if(v.equals(tvChoosePhoto)){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent,IMG_REQUEST);
           // Toast.makeText(this, "llWillBe", Toast.LENGTH_SHORT).show();
        }
        else if(v.equals(tvPhotoCamera)){
            //?????? ???????????????? ?????????? ?????? ????????????????????, ???????????? ???? ???????????? ?????????????????? ?? ???????????????? Intent
            cretePictureIntent();
        }
        else if(v.equals(tvApply)){
           // if(apply_time  + 2000 > System.currentTimeMillis()){
                //?????????????? ???????????? ???? ???????????????? ?? ??????????
                tvApply.setClickable(false);
                tvApply.setBackgroundResource(R.drawable.round_background_grey_200_black);
                //???????????????? ?????? ????????????????
                GenerateImageName image = new GenerateImageName();
                imageName = image.generateImageName(category,productName,characteristic,brand);

                //Log.d("A111","ChengeImageActivity / onClick / ok");
                //?????????????????? ???????????????? ?? ????
                UploadImageData uploadImage = new UploadImageData();
                String messege = uploadImage.uploadImageData(newBitmap,imageName, REQUEST_UPLOAD_IMAGE);

                TimerTask timerTask = new TimerTask();
                timerTask.execute();

           /* }else{
                Toast.makeText(this, ""+CLICK_AGAIN_TO_DOWNLOAD_IMAGE,
                        Toast.LENGTH_SHORT).show();
            }
            apply_time = System.currentTimeMillis();*/
        }
    }
    class TimerTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            asyncDialog.setMessage(UPLOADING_IMAGE_TAXT);
            asyncDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                for (int i = 0; i < 30; i++) {
                    TimeUnit.MILLISECONDS.sleep(500);
                    if(REQUEST_UPLOADED_IMAGE_INFO != 0){
                        i = 30;
                    }
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //???????????? ???????????????????? ????????
            asyncDialog.dismiss();
            //?????????????????? ?????????????????? ???????????????? ??????????????????????
            checkUploadImageResult();
        }
    }
    private void goBackActivity(String imageName){
        Intent intent = new Intent();
        intent.putExtra("product_inventory_id",product_inventory_id);
        intent.putExtra("imageName",imageName);
        setResult(RESULT_OK,intent);
        finish();
    }
    //?????????????????? ?????????????????? ???????????????? ??????????????????????
    private void checkUploadImageResult(){
        //?????????????????????? ?????????????? ??????????????????
        if(REQUEST_UPLOADED_IMAGE_INFO == 1){
            REQUEST_UPLOADED_IMAGE_INFO = 0;
            // ???????????? ???????????????? ?? t_product_inventory
            downloadImageNameToTable(imageName);

            //???????? ???????????????? alert ?????????????? ???? ?????????? ???? ????????????????
            if(CHENGE_IMAGE_ALERT_SHOW == 0){
                //make allert dialog for ansver (image download)
                alertDialogImageDownload(imageName);
            }else{
                goBackActivity(imageName);
            }
        }
        //???? ?????????????? ?????????????????? ??????????????????????
        else if(REQUEST_UPLOADED_IMAGE_INFO == 2){
            //?????????????? ???????????? ???????????????? ?? ??????????????
            tvApply.setClickable(true);
            tvApply.setBackgroundResource(R.drawable.round_corners_green_600_and_black);
            REQUEST_UPLOADED_IMAGE_INFO = 0;
            Toast.makeText(this, ""+IMAGE_UPLOADING_FAILED_TAXT, Toast.LENGTH_LONG).show();
        }
        //???????????? ????????????????????
        else if(REQUEST_UPLOADED_IMAGE_INFO == 3){
            //?????????????? ???????????? ???????????????? ?? ??????????????
            tvApply.setClickable(true);
            tvApply.setBackgroundResource(R.drawable.round_corners_green_600_and_black);
            REQUEST_UPLOADED_IMAGE_INFO = 0;
            Toast.makeText(this, ""+NETWORK_FAILED_TAXT, Toast.LENGTH_LONG).show();
        }else if(REQUEST_UPLOADED_IMAGE_INFO == 0){
            Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
        }

    }

    //?????? ???????????????? ?????????? ?????? ????????????????????, ???????????? ???? ???????????? ?????????????????? ?? ???????????????? Intent
    private void cretePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            if (photoFile != null) {
                Log.d(ChengeImageActivity.class.getSimpleName(),"test ");
                //Uri photoURI = Uri.fromFile(photoFile);
                Uri photoURI = FileProvider.getUriForFile(this,
                        "ru.tubi.project.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }
    // ?????????????? ???????????????????? ?? ?????????????????????????? ?????? ??????????.
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        Log.d(ChengeImageActivity.class.getSimpleName(),"File image; "+image.toString());
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    //?????????????????? ?????? ???????????????? ?? t_image_moderation (??????????????)
    private void downloadImageNameToTable(String imageName){
        String url_get = PROVIDER_OFFICE;
        url_get += "&" + "write_image_name_to_table";
        url_get += "&" + "product_inventory_id=" + product_inventory_id;
        url_get += "&" + "imageName=" + imageName;
        url_get += "&" + "user_uid=" + userDataModel.getUid();//MY_UID;
        String whatQuestion = "write_image_name_to_table";
        setInitialData(url_get, whatQuestion);
    }
    private void setInitialData(String url_get, String whatQuestion) {

        InitialData task = new InitialData() {

            protected void onPostExecute(String result) {
                if (whatQuestion.equals("write_image_name_to_table")) {
                    //splitResult(result);
                    //product_in_partner_warehouse_array
                }
            }
        };
        task.execute(url_get);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null ){
            try {
                Uri patch = data.getData();

                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),patch);

                //?????????? ???????? ???? 1536 ??????????????
                int newHeight = (int) ( bitmap.getHeight() * (2064.0 / bitmap.getWidth()) );
                bitmap = Bitmap.createScaledBitmap(bitmap, 2064, newHeight, true);

                Log.d("a111","ChengeImageActivity / onActivityResult " +
                        "/ bmt height="+bitmap.getHeight()+" bmt width="+bitmap.getWidth());
                //?????????????????? ????????????????
                newBitmap = new MakeImageOrientation()
                        .makeImageOrientation(this,bitmap,patch);

                //???????????? ???????????? ???????? ?????? ???????????????? ?? ????????????????????
                new MakeImageToSquare(this,newBitmap,ivWillBe);

                tvApply.setClickable(true);
                tvApply.setBackgroundResource(R.drawable.round_corners_green_600_and_black);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){

            galleryAddPic();
        }
    }
    //???????????? ?????????????????????? ??????????????
    //???????????????? ???????? ???????????????????? ?? ???????? ???????????? ???????????????????? ????????????????????
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);

        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),contentUri);
            //?????????? ???????? ???? 1536 ??????????????
            int newHeight = (int) ( bitmap.getHeight() * (2064.0 / bitmap.getWidth()) );
            bitmap = Bitmap.createScaledBitmap(bitmap, 2064, newHeight, true);
            Log.d("a111","ChengeImageActivity / galleryAddPic " +
                    "/ bmt height="+bitmap.getHeight()+" bmt width="+bitmap.getWidth());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //?????????????????? ????????????????
        newBitmap = new MakeImageOrientation()
                .makeImageOrientation(this,bitmap,contentUri);
        //???????????? ???????????? ???????? ?????? ???????????????? ?? ????????????????????
        new MakeImageToSquare(this,newBitmap,ivWillBe);

        tvApply.setClickable(true);
        tvApply.setBackgroundResource(R.drawable.round_corners_green_600_and_black);
    }

    private void alertDialogImageDownload(String imageName){
        adb = new AlertDialog.Builder(this);
        LinearLayout ll = new LinearLayout(this);
        CheckBox chengeImageCheckBox = new CheckBox(this);
        chengeImageCheckBox.setText(""+DONT_SHOW_IT_ANYMORE);
        ll.addView(chengeImageCheckBox);

        String st1 = IMAGE_DOWNLOAD;
        String st2 = IMAGE_FOR_MODERATION_AND_COMMENT;
        adb.setTitle(st1);
        adb.setMessage(st2);
        adb.setView(ll);
        adb.setPositiveButton(UNDERSTOOD_BIG, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(chengeImageCheckBox.isChecked()){
                    CHENGE_IMAGE_ALERT_SHOW = 1;
                }
                goBackActivity(imageName);
               /* Intent intent = new Intent();
                intent.putExtra("product_inventory_id",product_inventory_id);
                intent.putExtra("imageName",imageName);
                setResult(RESULT_OK,intent);
                finish();*/
                ad.cancel();
            }
        });


        ad=adb.create();
        ad.setCanceledOnTouchOutside(false);
        ad.setCancelable(false);
        ad.show();

        Button buttonbackground1 = ad.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonbackground1.setBackgroundColor(alert_dialog_button_green_pressed);
        buttonbackground1.setTextColor(Color.WHITE);
    }

}