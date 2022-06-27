package ru.tubi.project.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ru.tubi.project.R;
import ru.tubi.project.utilites.InitialData;

import ru.tubi.project.utilites.Constant;

import static ru.tubi.project.Config.MY_COMPANY_TAXPAYER_ID;
import static ru.tubi.project.Config.MY_UID;
import static ru.tubi.project.free.AllText.CHECK_CONNECT_INTERNET;
import static ru.tubi.project.free.AllText.CONTRACT_BIG;
import static ru.tubi.project.free.AllText.CONTRACT_SENT;
import static ru.tubi.project.free.AllText.I_UNDERSTAND;
import static ru.tubi.project.free.AllText.LOAD_TEXT;
import static ru.tubi.project.free.AllText.MES_3;
import static ru.tubi.project.free.AllText.PARTNER_WAREHOUSE;
import static ru.tubi.project.free.AllText.SUPPLIER;

public class ContractFormActivity extends AppCompatActivity implements View.OnClickListener {

    private Intent takeit;
    private String role = "", role_for_allert="", contractSampleName="", show_contract="";
    private int contractSampleId;
    private CheckBox checkBox;
    private Button btnSendContract;
    private EditText etComent;
    private TextView tvContract;
    private AlertDialog ad;
    private AlertDialog.Builder adb;
    private ActionBar actionBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contract_form);
        setTitle(CONTRACT_BIG);//ДОГОВОР
        actionBar = getSupportActionBar();

        checkBox = findViewById(R.id.checkBox);
        etComent = findViewById(R.id.etComent);
        tvContract = findViewById(R.id.tvContract);
        btnSendContract = findViewById(R.id.btnSendContract);
        btnSendContract.setOnClickListener(this);
        btnSendContract.setEnabled(false);


        takeit = getIntent();
        show_contract = takeit.getStringExtra("show_contract");
        whatShowContract();

       // receiveContractFromDB();

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    btnSendContract.setEnabled(true);
                }else {
                    btnSendContract.setEnabled(false);
                }
            }
        });
    }
    private void whatShowContract(){

        if(show_contract.equals("provider")) {
            contractSampleName = "агентский договор";
        }
        else if(show_contract.equals("partnerWarehouse")){
            contractSampleName = "партнер склада договор";
        }
        actionBar.setSubtitle(contractSampleName);
        receiveContractFromDB(contractSampleName);
    }

    private void receiveContractFromDB(String contractSampleName) {
        //String contractSampleName = "агентский договор";
        String url = Constant.ADMIN_OFFICE;
        //String url = PROVIDER_OFFICE;
        String url_get = url;
        url_get += "receive_sample_contract";
        url_get += "&" + "contract_sample_name="+contractSampleName;
        String whatQuestion = "receive_sample_contract";
        setInitialData(url_get, whatQuestion);
    }

    @Override
    public void onClick(View v) {
        //отправить запрос на присвоение поставщику роли
        //передать сообщение "коментарий"
        transferRequestNewProvider(etComent.getText().toString());
    }
        //отправить запрос на присвоение partner роли
    private void transferRequestNewProvider(String comment) {
        if(show_contract.equals("provider")) {
            role = "provider_business";
            role_for_allert=SUPPLIER;
        }
        //присвоение партнеру new роли
        else if(show_contract.equals("partnerWarehouse")){
            role = "partner_warehouse";
            role_for_allert=PARTNER_WAREHOUSE;
        }
        String url = Constant.ADMIN_OFFICE;
        url += "transfer_request_partner_role_new";//присвоение партнеру new роли
        url += "&" + "user_uid="+MY_UID;
        url += "&" + "role="+role;
        url += "&" + "comment="+comment;
        url += "&"+"counterparty_tax_id="+MY_COMPANY_TAXPAYER_ID;
        url += "&"+"contract_sample_id="+contractSampleId;
        String whatQuestion = "transfer_request_partner_role_new";
        setInitialData(url, whatQuestion);
    }
    private void setInitialData(String url_get, String whatQuestion) {
        ProgressDialog asyncDialog = new ProgressDialog(this);

        InitialData task=new InitialData(){
            @Override
            protected void onPreExecute() {
                asyncDialog.setMessage(LOAD_TEXT);
                asyncDialog.show();
                super.onPreExecute();
            }

            protected void onPostExecute(String result) {
                //Do your thing
                if(whatQuestion.equals("transfer_request_new_provider")){
                    splitResult(result);
                   // Toast.makeText(ContractProviderActivity.this, "resultDB: "+result, Toast.LENGTH_SHORT).show();
                }else if(whatQuestion.equals("receive_sample_contract")){
                    splitResultContract(result);
                   // Toast.makeText(ContractProviderActivity.this, "A: "+result, Toast.LENGTH_SHORT).show();
                }else if(whatQuestion.equals("transfer_request_partner_role_new")){
                    splitResult(result);
                    // Toast.makeText(ContractFormActivity.this, "A: "+result, Toast.LENGTH_SHORT).show();
                }
                //скрыть диалоговое окно
                asyncDialog.dismiss();
            }
        };
        task.execute(url_get);
    }
    // разобрать результат с сервера
    private void splitResultContract(String result) {
        try {

        String [] res=result.split("<br>");
        String[]temp = res[0].split("&nbsp");
          //  contractSampleId = Integer.parseInt(temp[0]);

        if(temp[0].equals("error")){
            Toast.makeText(this, ""+temp[1], Toast.LENGTH_LONG).show();
        }else{
            StringBuilder sb = new StringBuilder();
            for(int i=1; i<temp.length;i++){
                sb.append(temp[i]+"\n");
            }
            tvContract.setText(""+sb);
            contractSampleId = Integer.parseInt(temp[0]);
        }
        }catch (Exception ex){
            Toast.makeText(this, "Exception: "+ex, Toast.LENGTH_SHORT).show();
        }
        //Toast.makeText(this, "id: "+contractSampleId, Toast.LENGTH_SHORT).show();
    }
    // разобрать результат с сервера
    private void splitResult(String result){
        String [] res=result.split("<br>");
        String[]temp = res[0].split("&nbsp");
        if(temp[0].equals("RESULT_OK")){
            //ваш договор успешно доставлен сообщение
            alertDialogShow();
            //Toast.makeText(this, "RESULT_OK", Toast.LENGTH_SHORT).show();
        }else if(temp[0].equals("error")){
            Toast.makeText(this, ""+temp[1], Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, ""+CHECK_CONNECT_INTERNET, Toast.LENGTH_LONG).show();
        }

    }

    //ваш договор успешно доставлен сообщение
    private void alertDialogShow() {
        adb = new AlertDialog.Builder(this);
        Intent intent = new Intent(this,MenuActivity.class);
        String st1 = CONTRACT_SENT;
        String st2 = MES_3 +"\n"+role_for_allert;
        adb.setTitle(st1);
        adb.setMessage(st2);
        adb.setNeutralButton(I_UNDERSTAND, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

               /* if(show_contract.equals("provider")) {
                    //CHECK_PROVIDER_ROLE = "RESULT_OK";
                }
                else if(show_contract.equals("partnerWarehouse")){
                   // CHECK_PARTNER_WAREHOUSE_ROLE = "RESULT_OK";
                }*/
                //возврат в профиль пользователя
                startActivity(intent);
                finish();
            }
        });
        ad=adb.create();
        ad.setCanceledOnTouchOutside(false);
        ad.setCancelable(false);
        ad.show();
    }
}