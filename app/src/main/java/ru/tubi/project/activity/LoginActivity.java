package ru.tubi.project.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
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

import static ru.tubi.project.activity.Config.MY_ABBREVIATION;
import static ru.tubi.project.activity.Config.MY_COMPANY_TAXPAYER_ID;
import static ru.tubi.project.activity.Config.MY_NAME;
import static ru.tubi.project.activity.Config.MY_NAME_COMPANY;
//import static com.example.tubi.Config.MY_TAXPAYER_ID;
import static ru.tubi.project.activity.Config.MY_UID;
import static ru.tubi.project.activity.Config.ROLE;
import static ru.tubi.project.free.AllText.ENTER_YOUR_LOGIN_AND_PASSWORD_AND_PRESS_LOGINBUTTON;
import static ru.tubi.project.free.AllText.LOAD_TEXT;
import static ru.tubi.project.free.AllText.REGISTRATION;
import static ru.tubi.project.utilites.Constant.URL_LOGIN;
import static ru.tubi.project.utilites.InitialDataPOST.getParamsString;

public class LoginActivity extends AppCompatActivity{

    private Button btnLogin;
    private Button btnLinkToRegisterScreen;
    private EditText etPhone;
    private EditText etPassword;
    private ImageView imageView;
    private ProgressDialog pDialog;
    private SQLiteDatabase sqdb;
    private HelperDB my_db;
    private String whatQuestion;
    private String url_get;
    private boolean isHidePwd = true;
    private Activity activity = (Activity) this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        etPhone = findViewById(R.id.etPhone);
        imageView = findViewById(R.id.imageView);
        btnLinkToRegisterScreen=findViewById(R.id.btnLinkToRegisterScreen);
        etPassword = findViewById(R.id.etPassword);
        etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

        btnLinkToRegisterScreen.setText(REGISTRATION);
        // Проверьте, вошел ли пользователь уже в систему или нет
        my_db = new HelperDB(this);
        String uid;
        goReadUid();

        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //заполнить номер телефона скобками и тире
                new CheckPhoneNumberInput(activity,etPhone,s,start,before,count);
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
    }
    // вход в систему по логин парроль
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void goLogin(View view) {


        String password = etPassword.getText().toString().trim();

        //очистить номер от скобок и тире
        CheckPhoneNumberInput num = new CheckPhoneNumberInput();
        String phone = num.clearPhoneNumber(etPhone);
       // String phone = etPhone.getText().toString().trim();

        // Проверьте наличие пустых данных в форме
        if (!phone.isEmpty() && !password.isEmpty()) {
            // пользователь для входа в систему
            checkLogin(phone, password);
        } else {
            // Попросите пользователя ввести учетные данные
            Toast.makeText(getApplicationContext(),
                    "Пожалуйста, введите учетные данные!", Toast.LENGTH_LONG)
                    .show();
        }
    }


    @Override
    public void onBackPressed() {
            Toast.makeText(this, ""+
                    ENTER_YOUR_LOGIN_AND_PASSWORD_AND_PRESS_LOGINBUTTON, Toast.LENGTH_LONG).show();
    }

    // Проверьте, вошел ли пользователь уже в систему или нет
    private void goReadUid(){

        Cursor yourCursor = my_db.getYourTableContents();
            //проверить колличество записей в таблице
        int i = 0;
        try {
            while (yourCursor.moveToNext()) {
                i += 1;
            }
            my_db.close();
        }catch (Exception ex){
            Log.d("A111", "Ошибка курсора: " + ex.toString());
        }

        if(i != 0) {
            //получить его данные
            sqdb = my_db.getWritableDatabase();

            Cursor c = sqdb.query(HelperDB.TABLE_NAME_MY_USER, null, null,
                    null, null, null, null);
            int col0 = c.getColumnIndex(HelperDB.USER_UID);
            int col1 = c.getColumnIndex(HelperDB.USER_NAME);
            int col2 = c.getColumnIndex(HelperDB.ABBREVIATION);
            int col3 = c.getColumnIndex(HelperDB.COUNTERPARTY);
            int col4 = c.getColumnIndex(HelperDB.TAXPAYER_ID);
            int col5 = c.getColumnIndex(HelperDB.USER_ROLE);

            c.moveToFirst();
            String uid = c.getString(col0);
            String myName = c.getString(col1);
            sqdb.close();
            // если пользователь в системе то отправить его в MainActivity
            if (!uid.isEmpty()) {
                MY_UID = uid;
                MY_NAME = myName;
                MY_ABBREVIATION=c.getString(col2);
                MY_NAME_COMPANY=c.getString(col3);
                MY_COMPANY_TAXPAYER_ID=c.getString(col4);
                ROLE=c.getString(col5);

                //перейти дальше
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }


                // переход на активность регистрации
    public void goRegisterActivity(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

                 // функция проверки учетных данных в базе данных mysql
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void checkLogin(final String phone, final String password){

                        // Тег, используемый для отмены запроса
        String tag_string_req = "req_login";

       // pDialog.setMessage("Logging in ...");
        //showDialog();

        // запрос на получение данных пользователя
       /* url_get = URL_LOGIN;
        url_get += "&" + "phone=" + phone;
        url_get += "&" + "password=" + password;
        whatQuestion = "login";
        setInitialData(url_get,whatQuestion);*/
        final Map<String, String> parameters = new HashMap<>();
        parameters.put("phone",phone);
        parameters.put("password",password);
        setInitialDataPOST(URL_LOGIN, parameters);

    }
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
                //  Пользователь успешно найден в MySQL
                splitUserLogResult(s);

                //скрыть диалоговое окно
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
                if(whatQuestion.equals("login")){
                    //  Пользователь успешно найден в MySQL
                    splitUserLogResult(result);
                }
                asyncDialog.dismiss();
            }
        };
        task.execute(url_get);
    }*/
    private void splitUserLogResult(String result){  // разобрать результат с сервера
        try {
            String[] temp;
            String[] res = result.split("<br>");
            for (int i = 0; i < res.length; i++) {
                temp = res[i].split("&nbsp");

                if (temp[0].equals("error") || temp[0].equals("message")) {
                    Toast.makeText(this, " " + temp[1], Toast.LENGTH_LONG).show();
                } else {
                    UserModel user = new UserModel(temp[0], temp[1], temp[2], temp[3], temp[4],
                            temp[5], temp[6], temp[7], temp[8]);

                    saveInfoTableDB(user);   // Теперь сохраните пользователя в sqlite
                }
            }
        }catch(Exception ex){
            Log.d("A111","LoginActivity / splitUserLogResult / exception = "+ex);
        }
    }
    // Теперь сохраните пользователя в sqlite
    private void saveInfoTableDB(UserModel user){
        String order_id = "0";
        String partner_role ="";

        ContentValues cv = new ContentValues();

        cv.put(my_db.USER_NAME,user.getName());
        cv.put(my_db.USER_PHONE,user.getPhone());
        cv.put(my_db.USER_UID,user.getUid());
        cv.put(my_db.ABBREVIATION,user.getAbbreviation());
        cv.put(my_db.COUNTERPARTY,user.getCounterparty());
        cv.put(my_db.TAXPAYER_ID,user.getTaxpayer_id());
        cv.put(my_db.USER_ROLE,user.getRole());
        cv.put(my_db.ORDER_ID,order_id);
        cv.put(my_db.PARTNER_ROLE,partner_role);
        cv.put(my_db.USER_CREATED_AT,user.getCreate_at());
        cv.put(my_db.USER_UPDATED_AT,user.getUpdated_at());


        sqdb = my_db.getWritableDatabase();
        sqdb.insert(my_db.TABLE_NAME,null,cv);
        sqdb.close();
            //Запуск основной деятельности
        //goMainActivity();
        //проверьте заново есть ли пользователь в системе
        goReadUid();
    }
    /*private void goMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }*/

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    public void showPassword(View view) {
        if (isHidePwd) {
            imageView.setImageResource(R.drawable.eye_is_open_50ps);
            //etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            etPassword.setTransformationMethod(new HideReturnsTransformationMethod());
            etPassword.setSelection(etPassword.getText().length());
            isHidePwd = false;
        } else {

            imageView.setImageResource(R.drawable.eye_is_closed_50ps);
            //etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            etPassword.setTransformationMethod(new PasswordTransformationMethod());
            etPassword.setSelection(etPassword.getText().length());
            isHidePwd = true;
        }
        //etPassword.setSelection(R.drawable.shoping_box_60ps);
    }
}