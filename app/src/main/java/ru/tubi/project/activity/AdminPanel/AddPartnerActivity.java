package ru.tubi.project.activity.AdminPanel;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import ru.tubi.project.R;
import ru.tubi.project.models.ListPartnerForModerationAdapterModel;
import ru.tubi.project.utilites.HelperDB;
import ru.tubi.project.utilites.InitialData;

import ru.tubi.project.utilites.Constant;

import static ru.tubi.project.activity.Config.MY_UID;
import static ru.tubi.project.free.AllCollor.TUBI_BLACK;
import static ru.tubi.project.free.AllCollor.TUBI_GREEN_600;
import static ru.tubi.project.free.AllCollor.TUBI_GREY_200;
import static ru.tubi.project.free.AllCollor.TUBI_GREY_400;
import static ru.tubi.project.free.AllCollor.TUBI_YELLOW_200;
import static ru.tubi.project.free.AllCollor.alert_dialog_button_green_pressed;
import static ru.tubi.project.free.AllText.ADDED_PARTNER_GO_BACK_BIG;
import static ru.tubi.project.free.AllText.ADD_TEXT;
import static ru.tubi.project.free.AllText.ALL_RIGHT_TEXT;
import static ru.tubi.project.free.AllText.ALL_RIGHT_TEXT_BIG;
import static ru.tubi.project.free.AllText.CHECK_CONNECT_INTERNET;
import static ru.tubi.project.free.AllText.CONTRACT_DO_NOT_SIGTATURE_TEXT;
import static ru.tubi.project.free.AllText.PHONE_NUMBER_TEXT;
import static ru.tubi.project.free.AllText.RESPONSIBLE_PERSON_TEXT;
import static ru.tubi.project.free.AllText.RETURN_BIG;
import static ru.tubi.project.free.AllText.TAX_ID;
import static ru.tubi.project.free.AllText.TO_PARTNER_WAREHOUSE;
import static ru.tubi.project.free.AllText.TO_SUPPLIERS_PRODUCT;

public class AddPartnerActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private TextView tvProvider,tvGeneralInfo,tvComent,tvAdd;
    private CheckBox cbContractPrepared,cbContractSentForSignature,cbContractSignature;
    private Intent takeit, intent;
    private ListPartnerForModerationAdapterModel partner_info;
    private boolean addProviderFlag = false, backFlag = false;
    private AlertDialog ad;
    private String url, contractInfo, condidateForm="";
    private SQLiteDatabase sqdb;
    private HelperDB my_db;
    private int cbCountClicked=0;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_partner);
        setTitle(ADD_TEXT);//Добавить
        actionBar = getSupportActionBar();

        tvProvider=findViewById(R.id.tvProvider);
        tvGeneralInfo=findViewById(R.id.tvGeneralInfo);
        tvComent=findViewById(R.id.tvComent);
        tvAdd=findViewById(R.id.tvAdd);

        cbContractPrepared=findViewById(R.id.cbContractPrepared);
        cbContractSentForSignature=findViewById(R.id.cbContractSentForSignature);
        cbContractSignature=findViewById(R.id.cbContractSignature);

        cbContractPrepared.setOnCheckedChangeListener(this);
        cbContractSentForSignature.setOnCheckedChangeListener(this);
        cbContractSignature.setOnCheckedChangeListener(this);
        tvAdd.setOnClickListener(this);

        cbContractSentForSignature.setClickable(false);
        cbContractSentForSignature.setTextColor(TUBI_GREY_400);
        cbContractSignature.setClickable(false);
        cbContractSignature.setTextColor(TUBI_GREY_400);


        takeit = getIntent();
        partner_info = (ListPartnerForModerationAdapterModel)
                takeit.getSerializableExtra("provider_info");
        contractInfo = takeit.getStringExtra("contractInfo");

        makeSubtitle();

        tvProvider.setText(""+partner_info.getAbbreviation()+" "
                                        +partner_info.getCounterparty());
        tvGeneralInfo.setText(""+TAX_ID+" "+partner_info.getTaxpayer_id()+"\n"
                                +RESPONSIBLE_PERSON_TEXT+":\n"
                                +partner_info.getUser()+"\n"
                                +PHONE_NUMBER_TEXT+":\n"
                                +partner_info.getPhone());
        //получить коментарии к договору
        receiveComentForContract();
        //получить выполненные пункты необходимые для добвления в поставщики
        receiveContractStep();

    }
    private void makeSubtitle() {

        if(contractInfo.equals("condidate_for_provider")){
            condidateForm = TO_SUPPLIERS_PRODUCT;//в поставщики товаров
        }
        else if(contractInfo.equals("candidate_for_partner_warehouse")){
            condidateForm = TO_PARTNER_WAREHOUSE;//в партнеры склада
        }
        //candidate_for_partner_warehouse
        actionBar.setSubtitle(condidateForm);
        // makeStartList();
    }
    //получить выполненные пункты необходимые для добвления в поставщики
    private void receiveContractStep() {
        String url = Constant.ADMIN_OFFICE;
        url += "receive_contract_step";
        url += "&"+"role_partner_id="+partner_info.getRole_partner_id();
        String whatQuestion = "receive_contract_step";
        setInitialData(url,whatQuestion);
    }

    private void receiveComentForContract() {
        String url = Constant.ADMIN_OFFICE;
        url += "receive_coment_for_contract";
        url += "&"+"role_partner_id="+partner_info.getRole_partner_id();
        String whatQuestion = "receive_coment_for_contract";
        setInitialData(url,whatQuestion);
    }
    //добавить в поставщики
    private void addProvider(){
        String url = Constant.ADMIN_OFFICE;
        url += "add_provider";
        url += "&"+"role_partner_id="+partner_info.getRole_partner_id();
        url += "&"+"moderator_uid="+MY_UID;
        url += "&"+"user_id="+partner_info.getUser_id();
        String whatQuestion = "add_provider";
        setInitialData(url,whatQuestion);
    }
    private void setInitialData(String url_get, String whatQuestion) {

        InitialData task=new InitialData(){
            protected void onPostExecute(String result) {
                //Do your thing
                if(whatQuestion.equals("receive_coment_for_contract")){
                    splitResult(result);
                }else if(whatQuestion.equals("receive_contract_step")){
                    splitResContractStep(result);
                }else if(whatQuestion.equals("add_provider")){
                    splitResAddProvider(result);
                }
            }
        };
        task.execute(url_get);
    }
    private void splitResAddProvider(String result){
        String [] res=result.split("<br>");
        String[]one_temp = res[0].split("&nbsp");
        if(one_temp[0].equals("error") || one_temp[0].equals("messege") ){
            Toast.makeText(this, ""+one_temp[1], Toast.LENGTH_LONG).show();
            return;
        }else if(one_temp[0].equals("FAILURE") ){
            //ниже обновление страницы без картинок
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
        }else if(one_temp[0].equals("RESULT_OK") ){
            tvAdd.setBackgroundColor(TUBI_YELLOW_200);
            tvAdd.setText(""+ADDED_PARTNER_GO_BACK_BIG);
            backFlag=true;
            cbCountClicked=0;
            //Toast.makeText(this, "RESULT_OK", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, ""+CHECK_CONNECT_INTERNET, Toast.LENGTH_SHORT).show();
        }
    }
    private void splitResContractStep(String result){
        String [] res=result.split("<br>");
        String[]one_temp = res[0].split("&nbsp");
        if(one_temp[0].equals("error") || one_temp[0].equals("messege")){
            Toast.makeText(this, ""+one_temp[1], Toast.LENGTH_LONG).show();
            return;
        }else if(one_temp[0].equals("RESULT_OK") ){
            //tvComent.setText(""+one_temp[1]);
            showMySteps(one_temp[1]);
        }else{
            Toast.makeText(this, ""+CHECK_CONNECT_INTERNET, Toast.LENGTH_SHORT).show();
        }
    }
    private void splitResult(String result){  // разобрать результат с сервера список продуктов и колличество
        String [] res=result.split("<br>");
      //  Toast.makeText(this, ""+res[0], Toast.LENGTH_SHORT).show();
        String[]one_temp = res[0].split("&nbsp");
        if(one_temp[0].equals("error") ){
            Toast.makeText(this, ""+one_temp[1], Toast.LENGTH_LONG).show();
            return;
        }else if(one_temp[0].equals("RESULT_OK") || one_temp[0].equals("messege")){
            tvComent.setText(""+one_temp[1]);
        }else{
            Toast.makeText(this, ""+CHECK_CONNECT_INTERNET, Toast.LENGTH_SHORT).show();
        }

    }
    private void showMySteps(String s){
        int step = Integer.parseInt(s);
        if(step == 0){
            cbContractPrepared.setChecked(false);
            cbContractSentForSignature.setChecked(false);
            cbContractSignature.setChecked(false);
        }
        if(step == 1){
            cbCountClicked = 1;
            cbContractPrepared.setEnabled(false);
            //
            cbContractSentForSignature.setTextColor(TUBI_BLACK);
            cbContractSentForSignature.setClickable(true);
            cbContractSentForSignature.setChecked(false);
            //
            cbContractSignature.setChecked(false);
        }
        if(step == 2){
            cbCountClicked = 2;
            cbContractPrepared.setEnabled(false);
            cbContractSentForSignature.setEnabled(false);

            cbContractSignature.setTextColor(TUBI_BLACK);
            cbContractSignature.setClickable(true);
            cbContractSignature.setChecked(false);
        }
        if(step == 3){
            cbCountClicked = 3;
            cbContractPrepared.setEnabled(false);
            cbContractSentForSignature.setEnabled(false);
            cbContractSignature.setEnabled(false);

            tvAdd.setBackgroundColor(TUBI_GREEN_600);
            tvAdd.setTextColor(TUBI_BLACK);
        }
    }
    private void transferRequestToDB(View view){//String string
        cbCountClicked++;
        String whatQuestion = "allert_dialog";
        if(view.equals(cbContractPrepared)){//Договор подготовлен
            url = Constant.ADMIN_OFFICE;
            url += "provider_contract_step";
            url += "&"+"role_partner_id="+partner_info.getRole_partner_id();
            //url += "&"+"role_for_moderation_id="+provider_info.getRole_for_moderation_id();
            url += "&"+"contract_step="+getResources().getString(R.string.contract_prepared);
            url += "&"+"uid="+MY_UID;
            setInitialData(url,whatQuestion);
        }else if(view.equals(cbContractSentForSignature)){
            url = Constant.ADMIN_OFFICE;
            url += "provider_contract_step";
            url += "&"+"role_partner_id="+partner_info.getRole_partner_id();
            //url += "&"+"role_for_moderation_id="+provider_info.getRole_for_moderation_id();
            url += "&"+"contract_step="+getResources().getString(R.string.contract_sent_for_signature);
            url += "&"+"uid="+MY_UID;
            setInitialData(url,whatQuestion);
           // Toast.makeText(this, "cbContractSentForSignature", Toast.LENGTH_SHORT).show();
        }else if(view.equals(cbContractSignature)){
            url = Constant.ADMIN_OFFICE;
            url += "provider_contract_step";
            url += "&"+"role_partner_id="+partner_info.getRole_partner_id();
            //url += "&"+"role_for_moderation_id="+provider_info.getRole_for_moderation_id();
            url += "&"+"contract_step="+getResources().getString(R.string.contract_is_signet);
            url += "&"+"uid="+MY_UID;
            setInitialData(url,whatQuestion);
           // Toast.makeText(this, "cbContractSignature", Toast.LENGTH_SHORT).show();
        }
        if(cbCountClicked == 3){
            tvAdd.setBackgroundColor(TUBI_GREEN_600);
            tvAdd.setTextColor(TUBI_BLACK);
        }
    }
    private void allertDialog(View view, String string){

        AlertDialog.Builder adb= new AlertDialog.Builder(this);
        String st1 = string;
        String st2 = ALL_RIGHT_TEXT;
        adb.setTitle(st1);
        adb.setMessage(st2);
        adb.setPositiveButton(ALL_RIGHT_TEXT_BIG, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(view.equals(cbContractPrepared)) {//"Договор подготовлен"
                    //cbContractPrepared.setTextColor(TUBI_BLACK);
                    cbContractPrepared.setEnabled(false);

                    cbContractSentForSignature.setClickable(true);
                    cbContractSentForSignature.setTextColor(TUBI_BLACK);
                }else if(view.equals(cbContractSentForSignature)){
                    cbContractSentForSignature.setEnabled(false);
                    cbContractSentForSignature.setTextColor(TUBI_GREY_400);

                    cbContractSignature.setClickable(true);
                    cbContractSignature.setTextColor(TUBI_BLACK);
                }else if(view.equals(cbContractSignature)){
                    cbContractSignature.setEnabled(false);
                    cbContractSignature.setTextColor(TUBI_GREY_400);
                }
                //transferRequestToDB(string);
                transferRequestToDB(view);
            }
        });
        adb.setNeutralButton(RETURN_BIG, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(view.equals(cbContractPrepared)) {//Договор подготовлен
                    cbContractPrepared.setChecked(false);
                }else if(view.equals(cbContractSentForSignature)){
                    cbContractSentForSignature.setChecked(false);
                }else if(view.equals(cbContractSignature)){
                    cbContractSignature.setChecked(false);
                }
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
        Button buttonbackground2 = ad.getButton(DialogInterface.BUTTON_NEUTRAL);
        buttonbackground2.setBackgroundColor(TUBI_GREY_200);
        buttonbackground2.setTextColor(Color.WHITE);
    }

    @Override
    public void onClick(View v) {
        if(v.equals(tvAdd)){
            if(cbCountClicked==3){
                addProvider();
                //Toast.makeText(this, "your metod", Toast.LENGTH_SHORT).show();
            }else if(backFlag){
                onBackPressed();
            }else {
                Toast.makeText(this, ""+CONTRACT_DO_NOT_SIGTATURE_TEXT, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(buttonView.equals(cbContractPrepared)){
            if(cbContractPrepared.isChecked()){
                allertDialog(cbContractPrepared, cbContractPrepared.getText().toString());
            }
        }else if(buttonView.equals(cbContractSentForSignature)){
            if(cbContractSentForSignature.isChecked()){
                allertDialog(cbContractSentForSignature, cbContractSentForSignature.getText().toString());
            }
        }else if(buttonView.equals(cbContractSignature)){
            if(cbContractSignature.isChecked()){
                allertDialog(cbContractSignature, cbContractSignature.getText().toString());
            }
        }
    }
}