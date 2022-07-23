package ru.tubi.project.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import ru.tubi.project.R;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.utilites.HelperDB;
import ru.tubi.project.utilites.InitialData;
import ru.tubi.project.utilites.UserRoleReceive;

import ru.tubi.project.utilites.Constant;

import static ru.tubi.project.Config.MY_NAME;
import static ru.tubi.project.Config.MY_UID;
import static ru.tubi.project.Config.ROLE;
import static ru.tubi.project.free.AllText.CHECK_PASSWORD_TO_COPY;
import static ru.tubi.project.free.AllText.LOAD_TEXT;
import static ru.tubi.project.free.AllText.PLEASE_ENTER_YOUR_DETAILS;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etName, etPhone,etPassword, etPasswordChek;
    private ImageView ivPassEye, ivPassEyeChek;
    private String url_get;
    private String whatQuestion;
    private SQLiteDatabase sqdb;
    private HelperDB my_db;
    private UserModel user;
    Intent intent;
    private boolean isHidePwd = true, isHidePwdCheck = true;

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

        ivPassEye.setOnClickListener(this);
        ivPassEyeChek.setOnClickListener(this);

        my_db = new HelperDB(this);
        sqdb = my_db.getWritableDatabase();
        sqdb.close();

    }

    @Override
    public void onClick(View v) {
        if (v.equals(ivPassEye)) {
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
        }else if (v.equals(ivPassEyeChek)) {
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
    public void goRegister(View view) {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String passwordCheck = etPasswordChek.getText().toString().trim();

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
   /* public void showPass(View view) {
        if (isHidePwd) {
            ivPassEye.setImageResource(R.drawable.eye_is_open_50ps);
            //etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            etPassword.setTransformationMethod(new HideReturnsTransformationMethod());
            etPassword.setSelection(etPassword.getText().length());
            isHidePwd = false;
        } else {

            ivPassEye.setImageResource(R.drawable.eye_is_closed_50ps);
            //etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            etPassword.setTransformationMethod(new PasswordTransformationMethod());
            etPassword.setSelection(etPassword.getText().length());
            isHidePwd = true;
        }
        //etPassword.setSelection(R.drawable.shoping_box_60ps);
    }*/

    public void goLinkToLoginScreen(View view) {
        intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void registerUser(final String name, final String phone,
                              final String password){

                              //Тег, используемый для отмены запроса

                                //запрос на регистрацию в БД
        url_get = Constant.URL_REGISTER;
        url_get += "&" + "name="+name;
        url_get += "&" + "phone=" + phone;
        url_get += "&" + "password=" + password;
        whatQuestion = "registration";
        setInitialData(url_get,whatQuestion);


                                //Вставка строки в таблицу пользователей


                                //Запуск действия входа в систему

                           //Произошла ошибка при регистрации. Получить ошибку
                            // сообщение

    }
    private void setInitialData(String url_get, String whatQuestion) {
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
                                            //  Пользователь успешно сохранен в MySQL
                    splitUserRegResult(result);
                }
                asyncDialog.dismiss();
            }
        };
        task.execute(url_get);
    }
    private void splitUserRegResult(String result){  // разобрать результат с сервера
        String[]temp;
        String [] res=result.split("<br>");
        for(int i=0;i<res.length;i++){
            temp = res[i].split("&nbsp");

            if(temp[0].equals("error") || temp[0].equals("message")){
                Toast.makeText(this, " " +temp[1], Toast.LENGTH_LONG).show();
            }else{
                user = new UserModel(temp[0],temp[1],temp[2],temp[3],temp[4]);
               // UserModel user = new UserModel(temp[0],temp[1],temp[2],temp[3],temp[4],
                //        temp[5],temp[6],temp[7]);
                saveInfoTableDB(this.user);   // Теперь сохраните пользователя в sqlite
            }

        }

    }
    // Теперь сохраните пользователя в sqlite
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
        //получить роль пользователя
        UserRoleReceive userRoleReceive = new UserRoleReceive();
        userRoleReceive.RoleReceive(this);
        // перейти в MainActivity
        goMainActivity();
    }
    private void goMainActivity(){
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onBackPressed() {
        finish();
    }


}