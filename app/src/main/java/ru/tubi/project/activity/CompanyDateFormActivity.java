package ru.tubi.project.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import ru.tubi.project.R;

import ru.tubi.project.activity.company_my.CatalogStocksActivity;
import ru.tubi.project.models.CatalogProductProviderModel;
import ru.tubi.project.models.DecimalDigitsInputFilter;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.utilites.FirstSimbolMakeBig;
import ru.tubi.project.utilites.HelperDB;
import ru.tubi.project.utilites.InitialData;

import ru.tubi.project.utilites.Constant;
import ru.tubi.project.utilites.UserDataRecovery;

import static ru.tubi.project.Config.MY_COMPANY_TAXPAYER_ID;
import static ru.tubi.project.Config.MY_NAME;
import static ru.tubi.project.Config.MY_UID;
import static ru.tubi.project.free.AllCollor.RED;
import static ru.tubi.project.free.AllCollor.TUBI_BLACK;
import static ru.tubi.project.free.AllCollor.TUBI_GREY_200;
import static ru.tubi.project.free.AllCollor.TUBI_WHITE;
import static ru.tubi.project.free.AllCollor.TUBI_YELLOW_200;
import static ru.tubi.project.free.AllCollor.alert_dialog_button_green_pressed;
import static ru.tubi.project.free.AllText.AGENT_BIG;
import static ru.tubi.project.free.AllText.CHECK_TAX_ID;
import static ru.tubi.project.free.AllText.CHOOSE_ABBREVIATION;
import static ru.tubi.project.free.AllText.DATE_OR_COMPANY;
import static ru.tubi.project.free.AllText.DONE_BIG;
import static ru.tubi.project.free.AllText.DO_IT_LATHER;
import static ru.tubi.project.free.AllText.EDIT_BIG;
import static ru.tubi.project.free.AllText.ENTER_COMPANY_NAME;
import static ru.tubi.project.free.AllText.ENTER_TAXPAYER_ID;
import static ru.tubi.project.free.AllText.ERROR_BIG;
import static ru.tubi.project.free.AllText.HELLO;
import static ru.tubi.project.free.AllText.IMPORTANT;
import static ru.tubi.project.free.AllText.IP;
import static ru.tubi.project.free.AllText.IP_SMOL;
import static ru.tubi.project.free.AllText.MES_1_PROFILE;
import static ru.tubi.project.free.AllText.MES_2;
import static ru.tubi.project.free.AllText.MES_21;
import static ru.tubi.project.free.AllText.MY_COMPANY_NAME;
import static ru.tubi.project.free.AllText.NAME_COMPANY;
import static ru.tubi.project.free.AllText.OOO;
import static ru.tubi.project.free.AllText.OOO_SMOL;
import static ru.tubi.project.free.AllText.PRICE_NEW;
import static ru.tubi.project.free.AllText.PRICE_NEW_IS_NOT;
import static ru.tubi.project.free.AllText.PRICE_OLD;
import static ru.tubi.project.free.AllText.RETURN_BIG;
import static ru.tubi.project.free.AllText.TAX_ID;
import static ru.tubi.project.free.AllText.TAX_ID_NUM;
import static ru.tubi.project.free.AllText.WRITE_NOW;
import static ru.tubi.project.Config.MY_ABBREVIATION;
import static ru.tubi.project.Config.MY_NAME_COMPANY;
//import static com.example.tubi.Config.MY_TAXPAYER_ID;


public class CompanyDateFormActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvHello, tvUserName, tvMessage, tvInfo, tvInfo2;
    private RadioGroup rbGroup;
    private RadioButton rbIP, rbOOO;
    private EditText etCompanyName, etTaxpayerID;
    private SQLiteDatabase sqdb;
    private HelperDB my_db;
    private AlertDialog.Builder adb;
    private AlertDialog ad;
    private Intent takeit;
    private UserModel userDataModel;

    private boolean rb_ip=false, rb_ooo=false;
    private String abbreviation, counterparty, taxpayer_id ;
    private int agentKey, registrationCode = 0;
    //private  FirstSimbolMakeBig simbolMakeBig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_date_form);
        setTitle(DATE_OR_COMPANY);//ДАННЫЕ КОМПАНИИ

        tvHello=findViewById(R.id.tvHello);
        tvUserName=findViewById(R.id.tvUserName);
        tvMessage=findViewById(R.id.tvMessage);
        rbGroup=findViewById(R.id.rbGroup);
        tvInfo=findViewById(R.id.tvInfo);
        tvInfo2=findViewById(R.id.tvInfo2);
        rbIP=findViewById(R.id.rbIP);
        rbOOO=findViewById(R.id.rbOOO);
        etCompanyName=findViewById(R.id.etCompanyName);
        etTaxpayerID=findViewById(R.id.etTaxpayerID);

        takeit = getIntent();
        //данные вводит агент о компании партнере, не сохранять в память приложения
        agentKey = takeit.getIntExtra("agentKey",0);

        //получить из sqlLite данные пользователя и компании
        UserDataRecovery userDataRecovery = new UserDataRecovery();
        userDataModel = userDataRecovery.getUserDataRecovery(this);

        if(agentKey == 1){
            tvHello.setText(""+HELLO+"\n"+AGENT_BIG);
        }else{
            tvHello.setText(""+HELLO);
        }

        tvUserName.setText(""+new FirstSimbolMakeBig().firstSimbolMakeBig(userDataModel.getName()));//MY_NAME);
        if(agentKey == 1) {
            tvMessage.setText(MES_21);
        }else{
            tvMessage.setText(MES_2);
        }
        //проверить есть код для создания компании
        //это не агент и сомпания не создана то без кода не пускать
        if(!userDataModel.getRole().equals("sales_agent")
                && userDataModel.getCompany_tax_id() == 0) {
            adInputRegistrationCode();
        }
        rbIP.setText(IP);
        rbOOO.setText(OOO);
        tvInfo.setText(new FirstSimbolMakeBig().firstSimbolMakeBig(userDataModel.getCounterparty()));//NAME_COMPANY);
        etCompanyName.setHint(MY_COMPANY_NAME);
        tvInfo2.setText(""+TAX_ID);//ИНН текст
        etTaxpayerID.setHint(TAX_ID_NUM);

        rbIP.setOnClickListener(this);
        rbOOO.setOnClickListener(this);
        etCompanyName.setOnClickListener(this);
        etTaxpayerID.setOnClickListener(this);



        my_db = new HelperDB(this);

        sqdb = my_db.getWritableDatabase();

        Cursor c = sqdb.query(HelperDB.TABLE_NAME, null, null,
                null, null, null, null);
        int col0 = c.getColumnIndex(HelperDB.ABBREVIATION);
        int col1 = c.getColumnIndex(HelperDB.COUNTERPARTY);
        int col2 = c.getColumnIndex(HelperDB.TAXPAYER_ID);


        c.moveToFirst();
        abbreviation = c.getString(col0);
        counterparty = c.getString(col1);
        taxpayer_id = c.getString(col2);
        sqdb.close();

        if(abbreviation != null){
            if(abbreviation.equals(IP)){
                rbIP.setChecked(true);
            }else{
                rbOOO.setChecked(true);
            }
        }
        if( counterparty != null){
            etCompanyName.setText(""+new FirstSimbolMakeBig().firstSimbolMakeBig(counterparty));
        }
        if( taxpayer_id != null){
            etTaxpayerID.setText(""+taxpayer_id);
        }

        etCompanyName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {    }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(start >= 0){
                    etCompanyName.setBackgroundColor(TUBI_WHITE);
                    etCompanyName.setTextColor(TUBI_BLACK);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {            }
        });
        etTaxpayerID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(start >= 0){
                    etTaxpayerID.setBackgroundColor(TUBI_WHITE);
                    etTaxpayerID.setHintTextColor(TUBI_BLACK);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }
    //записать данные о компании
    public void goRecord(View view) {
        int tax_id_length = 0;
        //одна из радиокнопок нажата или нет
        if(rbIP.isChecked()){
            //rb_ip=true;
            tax_id_length = 12;
            abbreviation = IP_SMOL;
            //Toast.makeText(this, "IP", Toast.LENGTH_SHORT).show();
        }else if(rbOOO.isChecked()){
            //rb_ooo=true;
            tax_id_length = 10;
            abbreviation = OOO_SMOL;
           // Toast.makeText(this, "OOO", Toast.LENGTH_SHORT).show();
        }else {
            rbGroup.setBackgroundColor(TUBI_YELLOW_200);
            rbOOO.setTextColor(RED);
            rbIP.setTextColor(RED);
            Toast.makeText(this, ""+CHOOSE_ABBREVIATION, Toast.LENGTH_LONG).show();
            return;
        }
        //проверить наличие записи при отсутствии попросить заполнить
        if(etCompanyName.getText().toString().trim().isEmpty()){
            etCompanyName.setBackgroundColor(TUBI_YELLOW_200);
            etCompanyName.setHintTextColor(RED);
            Toast.makeText(this, ""+ENTER_COMPANY_NAME, Toast.LENGTH_LONG).show();
            return;
        }else{
            counterparty = etCompanyName.getText().toString().trim().toLowerCase();
        }
        //проверить наличие записи при отсутствии попросить заполнить
        if(etTaxpayerID.getText().toString().isEmpty()){
            etTaxpayerID.setBackgroundColor(TUBI_YELLOW_200);
            etTaxpayerID.setHintTextColor(RED);
            Toast.makeText(this, ""+ENTER_TAXPAYER_ID, Toast.LENGTH_LONG).show();
            return;
        }else if(etTaxpayerID.getText().toString().length() != tax_id_length) {
            Toast.makeText(this, ""+CHECK_TAX_ID, Toast.LENGTH_LONG).show();
            return;
        }else{
                taxpayer_id = etTaxpayerID.getText().toString();
            //Toast.makeText(this, "id: "+taxpayer_id, Toast.LENGTH_LONG).show();
        }

        createMyCompany();
        //записываем(изменяем) полученные данные в DB
       /* String url_get = Constant.INPUT;
        url_get += "&" + "new_counterparty";
        url_get += "&" + "unique_id=" + userDataModel.getUid();//MY_UID;
        url_get += "&" + "abbreviation=" + abbreviation;
        url_get += "&" + "counterparty=" + counterparty;
        url_get += "&" + "taxpayer_id=" + taxpayer_id;
        String whatQuestion = "new_counterparty";
        setInitialData(url_get,whatQuestion);*/

    }
    @Override
    public void onClick(View v) {
        if(rbIP.isChecked() || rbOOO.isChecked()){
            rbGroup.setBackgroundColor(TUBI_GREY_200);
            rbOOO.setTextColor(TUBI_BLACK);
            rbIP.setTextColor(TUBI_BLACK);
            //Toast.makeText(this, "IP or OOO", Toast.LENGTH_SHORT).show();
        }else if(etCompanyName.isClickable()){
            Toast.makeText(this, "click Company name", Toast.LENGTH_SHORT).show();
        }else if(etTaxpayerID.isClickable()){
            Toast.makeText(this, "click Taxpayer", Toast.LENGTH_SHORT).show();
        }
    }
    private void createMyCompany(){
        //записываем(изменяем) полученные данные в DB
        String url_get = Constant.INPUT;
        url_get += "&" + "new_counterparty";
        url_get += "&" + "unique_id=" + userDataModel.getUid();//MY_UID;
        url_get += "&" + "abbreviation=" + abbreviation;
        url_get += "&" + "counterparty=" + counterparty;
        url_get += "&" + "taxpayer_id=" + taxpayer_id;
        url_get += "&" + "agentKey=" + agentKey;//agentKey
        String whatQuestion = "new_counterparty";
        setInitialData(url_get,whatQuestion);
    }
    //закрыть код регистрации (использован)
    private void closeRegistrationCode(){
        String url_get = Constant.AGENT_OFFICE;
        url_get += "&" + "close_code";
        url_get += "&" + "code=" + registrationCode;
        url_get += "&" + "user_uid=" + userDataModel.getUid();
        String whatQuestion = "close_code";
        setInitialData(url_get,whatQuestion);
    }
    //проверить введенный код существует, свободен
    private void checkCodeToDB(int code){
        String url_get = Constant.AGENT_OFFICE;
        url_get += "&" + "check_code";
        url_get += "&" + "code=" + code;
        String whatQuestion = "check_code";
        setInitialData(url_get,whatQuestion);
    }
    private void setInitialData(String url_get, String whatQuestion) {
        Log.d("A111","CompanyDateFormActivity / setInitialData / url="+url_get);
        InitialData task=new InitialData(){
            protected void onPostExecute(String result) {
                if(whatQuestion.equals("new_counterparty")){
                    //ответ о записи в sql
                    splitCreateNewConterparty(result);
                }
                else if(whatQuestion.equals("check_code")){
                    //ответ о записи в sql
                    splitCheckCodeAnsver(result);
                }

            }
        };
        task.execute(url_get);
    }
    private void splitCheckCodeAnsver(String result){
        Log.d("A111","CompanyDateFormActivity / splitCheckCodeAnsver / result="+result);
        if(result.equals("RESULT_OK")){
            ad.cancel();
        }
        else{
            Toast.makeText(this, ""+ERROR_BIG, Toast.LENGTH_LONG).show();
        }
    }
    private void splitCreateNewConterparty(String result){  // разобрать результат с сервера
        String[]temp;
        String [] res=result.split("<br>");
        for(int i=0;i<res.length;i++){
            temp = res[i].split("&nbsp");

            if(temp[0].equals("error") || temp[0].equals("message")){
                Toast.makeText(this, " " +temp[1], Toast.LENGTH_LONG).show();
            }else if(temp[0].equals("RESULT_OK")){

                if(agentKey == 1){
                    //если данные вводил агент то записывать их в память приложения не надо
                    Intent intent = new Intent();
                    long partner_taxpayer_id = Long.parseLong(taxpayer_id);
                    intent.putExtra("partner_taxpayer_id", partner_taxpayer_id);
                    setResult(RESULT_OK, intent);
                    finish();
                }else{
                    //закрыть код регистрации (использован)
                    closeRegistrationCode();
                    // Теперь сохраните данные компании в sqlite
                    createCompanyDate();
                }

            }
        }
    }
    private void createCompanyDate(){
        // получаем положительный ответ о записи в DB и записываем в SQLlite
        //записываем в  configuration
        MY_ABBREVIATION=abbreviation;
        MY_NAME_COMPANY=counterparty;
        MY_COMPANY_TAXPAYER_ID=taxpayer_id;

        my_db=new HelperDB(this);
        sqdb=my_db.getWritableDatabase();

        ContentValues values= new ContentValues();
        values.put(HelperDB.ABBREVIATION, abbreviation);
        values.put(HelperDB.COUNTERPARTY, counterparty);
        values.put(HelperDB.TAXPAYER_ID, taxpayer_id);

        sqdb.update(HelperDB.TABLE_NAME, values,  HelperDB.USER_UID + "=?",
                new String[]{MY_UID});
        sqdb.close();
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
        //сделаю позже
    public void doItLather(View view) {
        //сообщение данные обязательны для продолжения заказа
        adb = new AlertDialog.Builder(this);
        String st1 = IMPORTANT;//важно
        //String st2 = receiveMessage();
        String st2 = MES_1_PROFILE;//takeit.getStringExtra("message");

        adb.setTitle(st1);
        adb.setMessage(st2);
        adb.setNeutralButton(DO_IT_LATHER, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        adb.setPositiveButton(WRITE_NOW, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ad.cancel();
            }
        });
        ad=adb.create();
        ad.show();
    }

    private void adInputRegistrationCode() {
        int maxLength = 4;
        EditText editText = new EditText(this);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
       /* LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT
                        , LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_VERTICAL;*/

        editText.setGravity(Gravity.CENTER);


        adb = new AlertDialog.Builder(this);
        String st1 = EDIT_BIG;
        adb.setTitle("Input your code");
        adb.setView(editText);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {   }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Log.d("A111","CompanyDateFormActivity / adInputRegistrationCode / start="+start);
                //Log.d("A111","CompanyDateFormActivity / adInputRegistrationCode / s="+s);
                if(start == 3){
                    int code = Integer.parseInt(editText.getText().toString().trim());
                    registrationCode = code;
                    checkCodeToDB(code);
                    //Toast.makeText(CompanyDateFormActivity.this, "go check", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {  }
        });


        adb.setNeutralButton(RETURN_BIG, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ad.cancel();
                onBackPressed();
            }
        });

        ad=adb.create();
        ad.setCanceledOnTouchOutside(false);
        ad.setCancelable(false);
        ad.show();

        Button buttonbackground1 = ad.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonbackground1.setBackgroundColor(alert_dialog_button_green_pressed);
        buttonbackground1.setTextColor(Color.WHITE);
        Button buttonbackground2 = ad.getButton(DialogInterface.BUTTON_NEUTRAL);
        buttonbackground2.setBackgroundColor(TUBI_GREY_200);
        buttonbackground2.setTextColor(Color.WHITE);
    }
}