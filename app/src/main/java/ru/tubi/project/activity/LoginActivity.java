package ru.tubi.project.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import ru.tubi.project.R;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.utilites.HelperDB;
import ru.tubi.project.utilites.InitialData;

import ru.tubi.project.utilites.Constant;

import static android.content.ContentValues.TAG;

import static ru.tubi.project.Config.MY_ABBREVIATION;
import static ru.tubi.project.Config.MY_COMPANY_TAXPAYER_ID;
import static ru.tubi.project.Config.MY_NAME;
import static ru.tubi.project.Config.MY_NAME_COMPANY;
//import static com.example.tubi.Config.MY_TAXPAYER_ID;
import static ru.tubi.project.Config.MY_UID;
import static ru.tubi.project.Config.ROLE;
import static ru.tubi.project.free.AllText.ENTER_YOUR_LOGIN_AND_PASSWORD_AND_PRESS_LOGINBUTTON;
import static ru.tubi.project.free.AllText.LOAD_TEXT;
import static ru.tubi.project.free.AllText.REGISTRATION;
import static ru.tubi.project.utilites.HelperDB.TABLE_NAME_MY_USER;

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
        // Progress dialog
       // pDialog = new ProgressDialog(this);
        //pDialog.setCancelable(false);

        // Проверьте, вошел ли пользователь уже в систему или нет
        my_db = new HelperDB(this);
        String uid;
        goReadUid();
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

            // вход в систему по логин парроль
    public void goLogin(View view) {

        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

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

                // переход на активность регистрации
    public void goRegisterActivity(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

                 // функция проверки учетных данных в базе данных mysql
    private void checkLogin(final String phone, final String password){

                        // Тег, используемый для отмены запроса
        String tag_string_req = "req_login";

       // pDialog.setMessage("Logging in ...");
        //showDialog();

        // запрос на получение данных пользователя
        url_get = Constant.URL_LOGIN;
        url_get += "&" + "phone=" + phone;
        url_get += "&" + "password=" + password;
        whatQuestion = "login";
        setInitialData(url_get,whatQuestion);

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
                if(whatQuestion.equals("login")){
                    //  Пользователь успешно найден в MySQL
                    splitUserLogResult(result);
                }
                asyncDialog.dismiss();
            }
        };
        task.execute(url_get);
    }
    private void splitUserLogResult(String result){  // разобрать результат с сервера
        String[]temp;
        String [] res=result.split("<br>");
        for(int i=0;i<res.length;i++){
            temp = res[i].split("&nbsp");

            if(temp[0].equals("error") || temp[0].equals("message")){
                Toast.makeText(this, " " +temp[1], Toast.LENGTH_LONG).show();
            }else{
                UserModel user = new UserModel(temp[0],temp[1],temp[2],temp[3],temp[4],
                                                temp[5],temp[6],temp[7],temp[8]);

                saveInfoTableDB(user);   // Теперь сохраните пользователя в sqlite
            }
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