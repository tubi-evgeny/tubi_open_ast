package ru.tubi.project.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import ru.tubi.project.R;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.utilites.CheckPhoneNumberInput;
import ru.tubi.project.utilites.FirstSimbolMakeBig;
import ru.tubi.project.utilites.HelperDB;
import ru.tubi.project.utilites.InitialData;

import ru.tubi.project.utilites.Constant;
import ru.tubi.project.utilites.UserDataRecovery;

import static ru.tubi.project.activity.Config.MY_NAME;
//import static com.example.tubi.Config.MY_TAXPAYER_ID;
import static ru.tubi.project.free.AllCollor.TUBI_BLACK;
import static ru.tubi.project.free.AllCollor.TUBI_GREY_200;
import static ru.tubi.project.free.AllText.CANCELLATION;
import static ru.tubi.project.free.AllText.CHECK_CONNECT_INTERNET;
import static ru.tubi.project.free.AllText.ENTER_NEW_NAME_TEXT;
//import static com.example.tubi.free.AllText.NEW_NAME_TEXT;
import static ru.tubi.project.free.AllText.ENTER_PHONE_NUM_ALL_TEXT;
import static ru.tubi.project.free.AllText.PHONE;
import static ru.tubi.project.free.AllText.PROFILE_USER;
import static ru.tubi.project.free.AllText.RECORD;
import static ru.tubi.project.free.AllText.YOUR_NAME_TEXT;

public class ProfileUserActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvUserName,tvUserGeneralInfo;
    private ImageView ivEditUser, ivEditUserPhone;
    private SQLiteDatabase sqdb;
    private HelperDB my_db;
    private AlertDialog.Builder adb;
    private AlertDialog ad;
    private String user_name,user_phone,abbreviation,counterparty,taxpayer_id;
    private String newPhone, newName;
    String url , url_get;
    private UserModel userDataModel;

    private static final int RETURN_COMPANY_DATE = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);
        setTitle(PROFILE_USER);//ВАШ ПРОФИЛЬ

        tvUserName=findViewById(R.id.tvUserName);
        tvUserGeneralInfo=findViewById(R.id.tvUserGeneralInfo);
        ivEditUser=findViewById(R.id.ivEditUser);
        ivEditUserPhone=findViewById(R.id.ivEditUserPhone);

        //получить из sqlLite данные пользователя и компании
        UserDataRecovery userDataRecovery = new UserDataRecovery();
        userDataModel = userDataRecovery.getUserDataRecovery(this);

        ivEditUser.setOnClickListener(this);
        ivEditUserPhone.setOnClickListener(this);

        //получить информацию о пользователе
        receiveUserGeneralInfo();

    }
    //заполнить activity
    private void fillActivity() {
        tvUserName.setText(""+user_name);
        //показать номер для пользователя, вернуть со скобками
        String numberStr =  new CheckPhoneNumberInput().PhoneNumWhithBrackets(user_phone);
        tvUserGeneralInfo.setText(""+numberStr);
    }
    //получить информацию о пользователе
    private void receiveUserGeneralInfo() {
        my_db = new HelperDB(this);
        sqdb = my_db.getWritableDatabase();

        Cursor c = sqdb.query(HelperDB.TABLE_NAME, null, null,
                null, null, null, null);
        int col0 = c.getColumnIndex(HelperDB.USER_NAME);
        int col1 = c.getColumnIndex(HelperDB.USER_PHONE);
        int col2 = c.getColumnIndex(HelperDB.ABBREVIATION);
        int col3 = c.getColumnIndex(HelperDB.COUNTERPARTY);
        int col4 = c.getColumnIndex(HelperDB.TAXPAYER_ID);

        c.moveToFirst();
        user_name = c.getString(col0);
        user_phone = c.getString(col1);
        abbreviation = c.getString(col2);
        counterparty = c.getString(col3);
        taxpayer_id = c.getString(col4);
        sqdb.close();

        user_name = new FirstSimbolMakeBig().firstSimbolMakeBig(user_name);
        //заполнить activity
        fillActivity();
    }

    @Override
    public void onClick(View v) {
        if (v.equals(ivEditUser)) {
            editUserName();
        }else if(v.equals(ivEditUserPhone)){
            editUserPhone();
        }
    }
    private void editUserName(){
        adb = new AlertDialog.Builder(this);
        final EditText etName = new EditText(this);
        etName.setHint(user_name);
        etName.setHintTextColor(TUBI_GREY_200);

        String st1 = YOUR_NAME_TEXT+" "+user_name;
        String st2 = ENTER_NEW_NAME_TEXT;
        adb.setTitle(st1);
        adb.setMessage(st2);
        adb.setView(etName);

        adb.setNegativeButton(CANCELLATION, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ad.cancel();
            }
        });
        adb.setPositiveButton(RECORD, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newName = etName.getText().toString().trim().toLowerCase();
                updateUserName();
            }
        });
        ad=adb.create();
        ad.show();
    }

    private void editUserPhone() {
        adb = new AlertDialog.Builder(this);
        Activity activity = (Activity) this;
        EditText etPhone = new EditText(this);
        etPhone.setHint(user_phone);
        etPhone.setHintTextColor(TUBI_BLACK);
        //показать номер для пользователя, вернуть со скобками
        String numberStr =  new CheckPhoneNumberInput().PhoneNumWhithBrackets(user_phone);
        etPhone.append(numberStr);//user_phone);
        etPhone.setRawInputType(InputType.TYPE_CLASS_NUMBER);

        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //заполнить номер телефона скобками и тире
                new CheckPhoneNumberInput(activity, etPhone
                                        , s, start, before, count);
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        String st1 = PHONE;
        adb.setView(etPhone);
        adb.setTitle(st1);

        adb.setNegativeButton(CANCELLATION, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ad.cancel();
            }
        });
        adb.setPositiveButton(RECORD, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newPhone = etPhone.getText().toString();

                //очистить номер от скобок и тире
                CheckPhoneNumberInput num = new CheckPhoneNumberInput();
                String phoneNumStr = num.clearPhoneNumber(etPhone);
                if(phoneNumStr.length() != 11 ){
                    Toast.makeText(activity, ""+newPhone+"\n"+ENTER_PHONE_NUM_ALL_TEXT, Toast.LENGTH_LONG).show();
                    return;
                }
                newPhone=phoneNumStr;
                updateUserPhone();
            }
        });
        ad=adb.create();
        ad.show();
    }
    //изменить имя пользователя в DB
    private void updateUserName() {
        url = Constant.USER_OFFICE;
        url_get = url;
        url_get += "chenge_user_name";
        url_get += "&" + "user_uid="+userDataModel.getUid();//MY_UID;
        url_get += "&" + "user_name="+ newName;
        String whatQuestion = "chenge_user_name";
        setInitialData(url_get,whatQuestion);
    }
    //изменить номер телефона в DB
    private void updateUserPhone() {
        url = Constant.USER_OFFICE;
        url_get = url;
        url_get += "chenge_user_phone";
        url_get += "&" + "user_uid="+userDataModel.getUid();//MY_UID;
        url_get += "&" + "user_phone="+ newPhone;
        String whatQuestion = "chenge_user_phone";
        setInitialData(url_get,whatQuestion);
    }

    private void setInitialData(String url_get, String whatQuestion) {
       // ProgressDialog asyncDialog = new ProgressDialog(this);

        InitialData task=new InitialData(){
           /* @Override
            protected void onPreExecute() {
                //set message of the dialog
                asyncDialog.setMessage(LOAD_TEXT);
                //show dialog
                asyncDialog.show();
                super.onPreExecute();
            }*/

            protected void onPostExecute(String result) {
                //Do your thing
                if(whatQuestion.equals("chenge_user_name")){
                    splitResult(result,whatQuestion);
                    //Toast.makeText(ShopingBox.this, ""+result, Toast.LENGTH_SHORT).show();
                }else if(whatQuestion.equals("chenge_user_phone")){
                    splitResult(result,whatQuestion);
                }
                //hide the dialog
              //  asyncDialog.dismiss();
            }
        };
        task.execute(url_get);
    }
    // разобрать ответ с сервера
    private void splitResult(String result, String whatQuestion){
        String [] res=result.split("<br>");
        String[]one_temp = res[0].split("&nbsp");
        if(one_temp[0].equals("RESULT_OK")) {
            // получаем положительный ответ о записи в DB и записываем в SQLlite
            if(whatQuestion.equals("chenge_user_name")){
                chengeInfoForUser("name");
            }else if(whatQuestion.equals("chenge_user_phone")){
                chengeInfoForUser("phone");
            }
        }else if(one_temp[0].equals("error") || one_temp[0].equals("messege")){
            Toast.makeText(this, ""+one_temp[1], Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, ""+CHECK_CONNECT_INTERNET, Toast.LENGTH_SHORT).show();
        }
    }
    // получаем положительный ответ о записи в DB и записываем в SQLlite
    private void chengeInfoForUser(String st){
        my_db=new HelperDB(this);
        sqdb=my_db.getWritableDatabase();

        ContentValues values= new ContentValues();

        if(st.equals("name")){
            //записываем в  configuration
            MY_NAME=newName;
            tvUserName.setText(""+new FirstSimbolMakeBig().firstSimbolMakeBig(newName));
            values.put(HelperDB.USER_NAME, newName);
        }else if(st.equals("phone")){
            user_phone=newPhone;
            //показать номер для пользователя, вернуть со скобками
            String numberStr =  new CheckPhoneNumberInput().PhoneNumWhithBrackets(newPhone);
            tvUserGeneralInfo.setText(""+numberStr);
            values.put(HelperDB.USER_PHONE, newPhone);
        }

        sqdb.update(HelperDB.TABLE_NAME, values,  HelperDB.USER_UID + "=?",
                new String[]{userDataModel.getUid()});//MY_UID
        sqdb.close();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,MenuActivity.class);
        startActivity(intent);
        finish();
    }

}