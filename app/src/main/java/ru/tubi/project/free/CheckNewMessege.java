package ru.tubi.project.free;

import android.app.ProgressDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ru.tubi.project.BuildConfig;
import ru.tubi.project.models.OrderModel;
import ru.tubi.project.utilites.InitialData;

import static ru.tubi.project.free.AllText.LOAD_TEXT;
import static ru.tubi.project.free.VariablesHelpers.MESSAGE_NUMBER;
import static ru.tubi.project.utilites.Constant.API;
import static ru.tubi.project.utilites.Constant.API_TEST;

public class CheckNewMessege {
    private ImageView imageView;
    int versionCode = BuildConfig.VERSION_CODE;
    String versionName = BuildConfig.VERSION_NAME;

    public CheckNewMessege(ImageView imageView) {
        this.imageView = imageView;

        String url= API;
        url += "&"+"check_new_messege";
        url += "&"+"message_number="+MESSAGE_NUMBER;
        setInitialData(url);
    }
    private void setInitialData(String url_get) {
        InitialData task=new InitialData(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }
            protected void onPostExecute(String result) {
                splitResult(result);
            }
        };
        task.execute(url_get);
    }
    private void splitResult(String result){
        try{
            if(result.equals("RESULT_OK")){
                imageView.setVisibility(View.VISIBLE);
            }else{
                imageView.setVisibility(View.GONE);
            }
        }catch (Exception ex){
        }
    }
}
