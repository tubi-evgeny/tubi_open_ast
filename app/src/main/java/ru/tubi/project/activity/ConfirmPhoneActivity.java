package ru.tubi.project.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import ru.tubi.project.R;
import ru.tubi.project.adapters.PartnerCollectProductAdapter;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.utilites.InitialDataPOST;

import static ru.tubi.project.free.AllText.CODE_IS_FALSE;
import static ru.tubi.project.free.AllText.LOAD_TEXT;
import static ru.tubi.project.utilites.Constant.AUTHORIZATION_USER;
import static ru.tubi.project.utilites.InitialDataPOST.getParamsString;

public class ConfirmPhoneActivity extends AppCompatActivity implements View.OnClickListener {

    private Intent takeit, intent;
    private String stPhone, visualPhone;
    private Button btnGoConfirmPhone;
    private String trueCode;
    private EditText etInputCode;
    private TextView tvPhone;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_phone);

        tvPhone=findViewById(R.id.tvPhone);
        btnGoConfirmPhone=findViewById(R.id.btnGoConfirmPhone);
        etInputCode=findViewById(R.id.etInputCode);

        btnGoConfirmPhone.setOnClickListener(this);

        takeit = getIntent();
        stPhone = takeit.getStringExtra("phone");
        visualPhone = takeit.getStringExtra("visualPhone");

        tvPhone.setText(""+visualPhone);

        //запрос для звонка на номер пользователя для получения кода
        startAuthorizationToCall();

    }
    @Override
    public void onClick(View v) {
        if (v.equals(btnGoConfirmPhone)) {
            String userCode = etInputCode.getText().toString().trim();
            //код правильный
            if(userCode.equals(trueCode)){
                intent = new Intent();
                intent.putExtra("result", "OPEN");
                setResult(RESULT_OK, intent);
                finish();
            }else{
                Toast.makeText(this, ""+CODE_IS_FALSE, Toast.LENGTH_SHORT).show();
            }
        }
    }
    //запрос для звонка на номер пользователя для получения кода
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void startAuthorizationToCall() {
        final Map<String, String> parameters = new HashMap<>();
        parameters.put("authorization_to_call","");
        parameters.put("phone",stPhone);
        String whatQuestion = "authorization_to_call";
        setInitialDataPOST(AUTHORIZATION_USER, parameters, whatQuestion);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setInitialDataPOST(String url, Map<String, String> param, String whatQuestion){
        ProgressDialog asyncDialog = new ProgressDialog(this);

        InitialDataPOST task = new InitialDataPOST(){
            @Override
            protected void onPreExecute() {
                asyncDialog.setMessage(LOAD_TEXT);
                asyncDialog.show();
                super.onPreExecute();
            }
            @Override
            protected void onPostExecute(String s) {
                splitUserAuthorizationAnsver(s);

                //скрыть диалоговое окно
                asyncDialog.dismiss();
            }
        };
        task.execute(url, getParamsString(param));
    }
    private void splitUserAuthorizationAnsver(String result){  // разобрать результат с сервера
        trueCode = result;
        Log.d("A111","ConfirmPhoneActivity / splitUserAuthorizationAnsver / code="+trueCode);


    }
}