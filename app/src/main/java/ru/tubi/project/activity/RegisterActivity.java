package ru.tubi.project.activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import ru.tubi.project.R;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.utilites.CheckPhoneNumberInput;
import ru.tubi.project.utilites.HelperDB;
import ru.tubi.project.utilites.InitialDataPOST;
import ru.tubi.project.utilites.UserRoleReceive;

import static ru.tubi.project.activity.Config.MY_NAME;
import static ru.tubi.project.activity.Config.MY_UID;
import static ru.tubi.project.activity.Config.ROLE;
import static ru.tubi.project.free.AllText.CHECK_PASSWORD_TO_COPY;
import static ru.tubi.project.free.AllText.CONFIRM_PHONE;
import static ru.tubi.project.free.AllText.ENTER_PHONE_NUM_ALL_TEXT;
import static ru.tubi.project.free.AllText.LOAD_TEXT;
import static ru.tubi.project.free.AllText.PLEASE_ENTER_YOUR_DETAILS;
import static ru.tubi.project.utilites.Constant.URL_REGISTER;
import static ru.tubi.project.utilites.InitialDataPOST.getParamsString;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etName, etPhone,etPassword, etPasswordChek;
    private ImageView ivPassEye, ivPassEyeChek, ivPhoneTrue;
    private Button btnConfirmPhoneNumber;
    private String url_get;
    private String whatQuestion, phone;
    private SQLiteDatabase sqdb;
    private HelperDB my_db;
    private UserModel user;
    private Intent intent;
    private boolean isHidePwd = true, isHidePwdCheck = true, phoneVerified = false;
    private Activity activity = (Activity) this;
    private int CONFIRM_PHONE_REQUEST=28;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etPasswordChek = findViewById(R.id.etPasswordChek);
        ivPassEye = findViewById(R.id.ivPassEye);
        ivPassEyeChek = findViewById(R.id.ivPassEyeChek);
        btnConfirmPhoneNumber = findViewById(R.id.btnConfirmPhoneNumber);
        ivPhoneTrue = findViewById(R.id.ivPhoneTrue);

        ivPassEye.setOnClickListener(this);
        ivPassEyeChek.setOnClickListener(this);
        btnConfirmPhoneNumber.setOnClickListener(this);

        my_db = new HelperDB(this);
        sqdb = my_db.getWritableDatabase();
        sqdb.close();

        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //?????????????????? ?????????? ???????????????? ???????????????? ?? ????????
                new CheckPhoneNumberInput(activity, etPhone
                                        , s, start, before, count);
                phoneVerified=false;
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        if (v.equals(btnConfirmPhoneNumber)) {
            confirmPhoneNumber();
        }
        else if (v.equals(ivPassEye)) {
            if (isHidePwd) {
                ivPassEye.setImageResource(R.drawable.eye_is_open_50ps);
                etPassword.setTransformationMethod(new HideReturnsTransformationMethod());
                etPassword.setSelection(etPassword.getText().length());
                isHidePwd = false;
            }
            else {
                ivPassEye.setImageResource(R.drawable.eye_is_closed_50ps);
                etPassword.setTransformationMethod(new PasswordTransformationMethod());
                etPassword.setSelection(etPassword.getText().length());
                isHidePwd = true;
            }
        }
        else if (v.equals(ivPassEyeChek)) {
            if (isHidePwdCheck) {
                ivPassEyeChek.setImageResource(R.drawable.eye_is_open_50ps);
                etPasswordChek.setTransformationMethod(new HideReturnsTransformationMethod());
                etPasswordChek.setSelection(etPasswordChek.getText().length());
                isHidePwdCheck = false;
            }
            else {
                ivPassEyeChek.setImageResource(R.drawable.eye_is_closed_50ps);
                etPasswordChek.setTransformationMethod(new PasswordTransformationMethod());
                etPasswordChek.setSelection(etPasswordChek.getText().length());
                isHidePwdCheck = true;
            }

        }

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void goRegister(View view) {
        String name = etName.getText().toString().trim();
        //String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String passwordCheck = etPasswordChek.getText().toString().trim();

        //???????????????? ?????????? ???? ???????????? ?? ????????
       /* CheckPhoneNumberInput num = new CheckPhoneNumberInput();
        phone = num.clearPhoneNumber(etPhone);
        if(phone.length() != 11 ){
            Toast.makeText(this, ""+ENTER_PHONE_NUM_ALL_TEXT, Toast.LENGTH_SHORT).show();
            return;
        }*/
        if(!phoneVerified){
            Toast.makeText(this, ""+CONFIRM_PHONE, Toast.LENGTH_SHORT).show();
            btnConfirmPhoneNumber.setTextColor(Color.RED);
            return;
        }
        if (!name.isEmpty() && !phone.isEmpty()
                && !password.isEmpty() && !passwordCheck.isEmpty()) {
            if(password.equals(passwordCheck)){
                registerUser(name, phone, password);
            }
            else{
                Toast.makeText(this, ""+CHECK_PASSWORD_TO_COPY, Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getApplicationContext(),
                    "" + PLEASE_ENTER_YOUR_DETAILS, Toast.LENGTH_LONG)
                    .show();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void confirmPhoneNumber(){
        //???????????????? ?????????? ???? ???????????? ?? ????????
        CheckPhoneNumberInput num = new CheckPhoneNumberInput();
        phone = num.clearPhoneNumber(etPhone);
        if(phone.length() != 11 ){
            Toast.makeText(this, ""+ENTER_PHONE_NUM_ALL_TEXT, Toast.LENGTH_SHORT).show();
        }
        else{
            intent = new Intent(this, ConfirmPhoneActivity.class);
            intent.putExtra("phone",phone);
            intent.putExtra("visualPhone",etPhone.getText().toString());
            startActivityForResult(intent, CONFIRM_PHONE_REQUEST);
           /* final Map<String, String> parameters = new HashMap<>();
            parameters.put("authorization_to_call","");
            parameters.put("phone",phone);
            setInitialDataPOST(URL_REGISTER, parameters);*/
        }
    }

    public void goLinkToLoginScreen(View view) {
        intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void registerUser(final String name, final String phone,
                              final String password){
                                //???????????? ???? ?????????????????????? ?? ????
       /* url_get = URL_REGISTER;
        url_get += "&" + "name="+name;
        url_get += "&" + "phone=" + phone;
        url_get += "&" + "password=" + password;
        whatQuestion = "registration";
        setInitialData(url_get,whatQuestion);*/
        final Map<String, String> parameters = new HashMap<>();
        parameters.put("name",name);
        parameters.put("phone",phone);
        parameters.put("password",password);
        setInitialDataPOST(URL_REGISTER, parameters);
    }
    //???????????????? ???????????? ???? ?????????????? ??/??
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setInitialDataPOST(String url, Map<String, String> param){
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
                splitUserRegResult(s);

                //???????????? ???????????????????? ????????
                asyncDialog.dismiss();
            }
        };
        task.execute(url, getParamsString(param));
    }
  /*  private void setInitialData(String url_get, String whatQuestion) {
        ProgressDialog asyncDialog = new ProgressDialog(this);

        InitialData task=new InitialData(){
            @Override
            protected void onPreExecute() {
                asyncDialog.setMessage(LOAD_TEXT);
                // asyncDialog.setInverseBackgroundForced();
                asyncDialog.show();
                super.onPreExecute();
            }

            protected void onPostExecute(String result) {
                //Do your thing
                if(whatQuestion.equals("registration")){
                                            //  ???????????????????????? ?????????????? ???????????????? ?? MySQL
                    splitUserRegResult(result);
                }
                asyncDialog.dismiss();
            }
        };
        task.execute(url_get);
    }*/
    private void splitUserRegResult(String result){  // ?????????????????? ?????????????????? ?? ??????????????
        String[]temp;
        String [] res=result.split("<br>");
        for(int i=0;i<res.length;i++){
            temp = res[i].split("&nbsp");

            if(temp[0].equals("error") || temp[0].equals("message")){
                Toast.makeText(this, " " +temp[1], Toast.LENGTH_LONG).show();
            }else{
                user = new UserModel(temp[0],temp[1],temp[2],temp[3],temp[4]);
                saveInfoTableDB(this.user);   // ???????????? ?????????????????? ???????????????????????? ?? sqlite
            }

        }

    }
    // ???????????? ?????????????????? ???????????????????????? ?? sqlite
    private void saveInfoTableDB(UserModel user){
        String abbreviation="";
        String counterparty="";
        String taxpayer_id="";
        /*String abbreviation=null;
        String counterparty=null;
        String taxpayer_id=null;*/
        String role = "user";
        String order_id = "0";
        String partner_role ="";

        ContentValues cv = new ContentValues();

        cv.put(my_db.USER_NAME,this.user.getName());
        cv.put(my_db.USER_PHONE,this.user.getPhone());
        cv.put(my_db.USER_UID,this.user.getUid());
        cv.put(my_db.ABBREVIATION,abbreviation);
        cv.put(my_db.COUNTERPARTY,counterparty);
        cv.put(my_db.TAXPAYER_ID,taxpayer_id);
        cv.put(my_db.USER_ROLE,role);
        cv.put(my_db.ORDER_ID,order_id);
        cv.put(my_db.PARTNER_ROLE,partner_role);
        cv.put(my_db.USER_CREATED_AT,this.user.getCreate_at());
        cv.put(my_db.USER_UPDATED_AT,this.user.getUpdated_at());


        sqdb = my_db.getWritableDatabase();
       // my_db.TABLE_NAME = my_db.TABLE_NAME_MY_USER;
        sqdb.insert(my_db.TABLE_NAME_MY_USER,null,cv);
        sqdb.close();

        MY_NAME = this.user.getName();
        MY_UID = this.user.getUid();
        ROLE=role;
        //???????????????? ???????? ????????????????????????
        UserRoleReceive userRoleReceive = new UserRoleReceive();
        userRoleReceive.RoleReceive(this);
        // ?????????????? ?? MainActivity
        goMainActivity();
    }
    private void goMainActivity(){
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CONFIRM_PHONE_REQUEST && resultCode==RESULT_OK && data != null){
            if(data.getStringExtra("result").equals("OPEN")){
                ivPhoneTrue.setImageResource(R.drawable.checkmark_green_140ps);
                btnConfirmPhoneNumber.setTextColor(Color.WHITE);
                phoneVerified = true;
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }


}