package ru.tubi.project.activity.agent;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import ru.tubi.project.R;
import ru.tubi.project.adapters.PartnerCollectProductAdapter;
import ru.tubi.project.models.CodeModel;
import ru.tubi.project.models.OrderModel;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.utilites.Constant;
import ru.tubi.project.utilites.InitialData;
import ru.tubi.project.utilites.UserDataRecovery;

import static ru.tubi.project.free.AllText.CODE_GENERATOR;
import static ru.tubi.project.free.AllText.FREE_TEXT;
import static ru.tubi.project.free.AllText.LOAD_TEXT;
import static ru.tubi.project.free.AllText.TRANSFERED;
import static ru.tubi.project.free.AllText.USED_TEXT;

public class CodeGeneratorActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvCode, tvGenerateCode, tvActivateCode;
    private ListView lvCodeList;
    private UserModel userDataModel;
    UserDataRecovery userDataRecovery = new UserDataRecovery();
    private ArrayList<CodeModel> codeList = new ArrayList<>();
    private ArrayList<String> codeListStr = new ArrayList<>();
    ArrayAdapter adapter;

    private boolean allCodeActivatedFlag = true;
    private int freeCode_id=0;
    private String freeCode ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_generator);
        setTitle(""+CODE_GENERATOR);//Генератор кода

        lvCodeList=findViewById(R.id.lvCodeList);
        tvCode=findViewById(R.id.tvCode);
        tvGenerateCode=findViewById(R.id.tvGenerateCode);
        tvActivateCode=findViewById(R.id.tvActivateCode);

        tvGenerateCode.setOnClickListener(this);
        tvActivateCode.setOnClickListener(this);

        tvActivateCode.setClickable(false);

        //получить из sqlLite данные пользователя и компании
        userDataModel = userDataRecovery.getUserDataRecovery(this);

        receiveCodeList();

        adapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1,codeListStr);
        lvCodeList.setAdapter(adapter);
    }
    @Override
    public void onClick(View v) {
        if(v.equals(tvGenerateCode)){
            goGenerateNewCode();
        }else if(v.equals(tvActivateCode)) {
            goActivateCode();
        }
    }
    private void createCodeStringList(){
        codeListStr.clear();
        for(int i=0;i < codeList.size();i++){
            String activatedInfo = ""+FREE_TEXT;
            if(codeList.get(i).getCode_used() == 1){
                activatedInfo = ""+TRANSFERED+"      ";//USED_TEXT;
            }
            long creationTime = codeList.get(i).getCreation_time();
            long activation_millis = codeList.get(i).getActivation_millis();
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yy");
            String creationTimeStr = formatter.format(new Date(creationTime));

            String activationTimeStr = "________";
            if(activation_millis != 0){
                formatter = new SimpleDateFormat("dd.MM.yy");
                activationTimeStr = formatter.format(new Date(activation_millis));
            }

            String str = ""+codeList.get(i).getCode()+"   "+creationTimeStr+"    "
                    +activatedInfo+"  "+activationTimeStr;
            codeListStr.add(str);
        }
        adapter.notifyDataSetChanged();
    }
    //активировать код
    private void goActivateCode() {
        String url = Constant.AGENT_OFFICE;
        url += "activate_code";
        url += "&"+"code_id="+freeCode_id;
        String whatQuestion = "activate_code";
        setInitialData(url, whatQuestion);

        allCodeActivatedFlag=true;
        freeCode_id=0;

        //получить список сгенерированных кодов этого агента
        receiveCodeList();
    }
    //проверить инфу кнопки и окна с кодом и испарвить если надо
    private void writeTrueInfoToView(){
        if(allCodeActivatedFlag == false){
            tvCode.setText(""+freeCode);
            tvGenerateCode.setVisibility(View.GONE);
            tvGenerateCode.setClickable(false);

            tvActivateCode.setVisibility(View.VISIBLE);
            tvActivateCode.setClickable(true);
        }else{
            freeCode = "0000";
            tvCode.setText(""+freeCode);
            tvGenerateCode.setVisibility(View.VISIBLE);
            tvGenerateCode.setClickable(true);

            tvActivateCode.setVisibility(View.GONE);
            tvActivateCode.setClickable(false);
        }
    }
    //сгенрировать новый код
    private void goGenerateNewCode() {
        String url = Constant.AGENT_OFFICE;
        url += "generate_new_code";
        url += "&"+"agent_user_uid="+userDataModel.getUid();
        String whatQuestion = "generate_new_code";
        setInitialData(url, whatQuestion);
    }

    //получить список сгенерированных кодов этого агента
    private void receiveCodeList() {
        String url = Constant.AGENT_OFFICE;
        url += "receive_code_list";
        url += "&"+"agent_user_uid="+userDataModel.getUid();
        String whatQuestion = "receive_code_list";
        setInitialData(url, whatQuestion);
        Log.d("A111","CodeGeneratorActivity / receiveCodeList / url="+url);
    }
    private void setInitialData(String url_get, String whatQuestion) {
        ProgressDialog asyncDialog = new ProgressDialog(this);
        //проверка соединения интернета
        if ( !isOnline() ){
            Toast.makeText(getApplicationContext(),
                    "Нет соединения с интернетом!",Toast.LENGTH_LONG).show();
            return;
        }
        InitialData task=new InitialData(){
            @Override
            protected void onPreExecute() {
                asyncDialog.setMessage(LOAD_TEXT);
                asyncDialog.show();
                super.onPreExecute();
            }

            protected void onPostExecute(String result) {
                if(whatQuestion.equals("receive_code_list")){
                    splitCodeArray(result);
                }
                else if(whatQuestion.equals("generate_new_code")){
                    splitNewCode(result);
                }

                //скрыть диалоговое окно
                asyncDialog.dismiss();
            }
        };
        task.execute(url_get);
    }
    private void splitNewCode(String result){
        try{
            if(result.equals("RESULT_OK")){
                receiveCodeList();
            }
        }catch(Exception ex){

        }
    }
    // разобрать результат с сервера, список выданных кодов
    private void splitCodeArray(String result){
        codeList.clear();
        try{
            String[] res = result.split("<br>");
            String[] one_temp = res[0].split("&nbsp");
            if (one_temp[0].equals("error") || one_temp[0].equals("messege")) {
                Toast.makeText(this, "" + one_temp[1], Toast.LENGTH_LONG).show();
                return;
            } else {
                for (int i = 0; i < res.length; i++) {
                    String[] temp = res[i].split("&nbsp");

                    int code_id = Integer.parseInt(temp[0]);
                    String code = temp[1];
                    int used_user_id = Integer.parseInt(temp[2]);
                    long creation_time = Long.parseLong(temp[3]);
                    long activation_time = Long.parseLong(temp[4]);
                    int code_used = Integer.parseInt(temp[5]);

                    CodeModel codeInfo = new CodeModel(code_id, code, used_user_id
                            , creation_time,activation_time,code_used);

                    codeList.add(codeInfo);
                    //если код не активирован показать его для активации
                    if(code_used == 0){
                        freeCode = code;
                        freeCode_id = code_id;
                        allCodeActivatedFlag = false;
                    }
                }
            }
            writeTrueInfoToView();

            Log.d("A111","CodeGeneratorActivity / splitCodeArray / result="+result);
            createCodeStringList();
        }catch(Exception ex){
            Log.d("A111","CodeGeneratorActivity / splitCodeArray / Exception="+ex);
        }
    }


    //проверка соединения интернета
    protected boolean isOnline() {
        String cs = Context.CONNECTIVITY_SERVICE;
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(cs);
        if (cm.getActiveNetworkInfo() == null) {
            return false;
        } else {
            return true;
        }
    }


}