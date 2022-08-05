package ru.tubi.project.activity.company_my;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ru.tubi.project.R;
import ru.tubi.project.utilites.InitialData;

import java.util.ArrayList;

import ru.tubi.project.utilites.Constant;

import static ru.tubi.project.activity.Config.MY_ABBREVIATION;
import static ru.tubi.project.activity.Config.MY_COMPANY_TAXPAYER_ID;
import static ru.tubi.project.activity.Config.MY_NAME_COMPANY;
import static ru.tubi.project.activity.Config.MY_UID;
import static ru.tubi.project.free.AllCollor.RED;
import static ru.tubi.project.free.AllText.CREATE_WAREHOUSE_TEXT;
import static ru.tubi.project.free.AllText.ENTER_DETAILS;
import static ru.tubi.project.free.AllText.PLEASE_ENTER_YOUR_DETAILS;

public class WarehouseCreateActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvCounterparty,tvTaxId,tvCreateWarehouse;
    private EditText etRegion,etDistrict,etCity,etStreet,etHouse,
                        etBuilding,etSignboard;
    private ArrayList<EditText> etAll = new ArrayList<>();
    private Intent takeit;
    private boolean providerFlag,partnerFlag,createFlag, providerCheckInFlag=false, partnerCheckInFlag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse_create);
        setTitle(CREATE_WAREHOUSE_TEXT);//Создать склад

        tvCounterparty=findViewById(R.id.tvCounterparty);
        tvTaxId=findViewById(R.id.tvTaxId);
        tvCreateWarehouse=findViewById(R.id.tvCreateWarehouse);

        etRegion=findViewById(R.id.etRegion);
        etDistrict=findViewById(R.id.etDistrict);
        etCity=findViewById(R.id.etCity);
        etStreet=findViewById(R.id.etStreet);
        etHouse=findViewById(R.id.etHouse);
        etBuilding=findViewById(R.id.etBuilding);
        etSignboard=findViewById(R.id.etSignboard);

        //список с view сделать
        View[]viewList = {etRegion,etDistrict,etCity,etStreet,etHouse,
                etBuilding,etSignboard};
        //перебрать лист в edittext для поиска с помощью массива
        for(int i=0;i < viewList.length;i++){
            etAll.add((EditText)viewList[i]);
        }

        tvCreateWarehouse.setOnClickListener(this);

        tvCounterparty.setText(""+MY_ABBREVIATION+" "+MY_NAME_COMPANY);
        tvTaxId.setText(""+MY_COMPANY_TAXPAYER_ID);

       // takeit=getIntent();
        //providerFlag = takeit.getBooleanExtra("providerFlag",false);
       // partnerFlag = takeit.getBooleanExtra("partnerFlag",false);
    }
    private void createNewWarehouse(){
        String url= Constant.PROVIDER_OFFICE;
        url += "create_new_warehouse";
        url += "&"+"counterparty_tax_id="+MY_COMPANY_TAXPAYER_ID;
        url += "&"+"user_uid="+MY_UID;
        url += "&"+"region="+etAll.get(0).getText().toString();
        url += "&"+"district="+etAll.get(1).getText().toString();
        url += "&"+"city="+etAll.get(2).getText().toString();
        url += "&"+"street="+etAll.get(3).getText().toString();
        url += "&"+"house="+etAll.get(4).getText().toString();
        url += "&"+"building="+etAll.get(5).getText().toString();
        url += "&"+"signboard="+etAll.get(6).getText().toString();
        String whatQuestion = "create_new_warehouse";
        setInitialData(url,whatQuestion);

        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    private void setInitialData(String url_get, String whatQuestion) {
        InitialData task=new InitialData(){
            protected void onPostExecute(String result) {
                if(whatQuestion.equals("create_new_warehouse")){
                    //splitResult(result,whatQuestion);
                    //Toast.makeText(ProfileCompanyActivity.this, "resultDB: "+result, Toast.LENGTH_SHORT).show();
                }
            }
        };
        task.execute(url_get);
    }
    private void checkAllEditText(){
        createFlag=true;
        for(int i=0;i < etAll.size();i++){
                EditText et = etAll.get(i);
            if(!et.equals(etBuilding)) {
                if (et.getText().toString().isEmpty()) {
                    et.setHint("" + ENTER_DETAILS);
                    et.setHintTextColor(RED);
                    createFlag = false;
                }
            }
        }
    }
    @Override
    public void onClick(View v) {
        if(v.equals(tvCreateWarehouse)){
            checkAllEditText();
            if(createFlag){
                createNewWarehouse();
            }else{
                Toast.makeText(this, ""+PLEASE_ENTER_YOUR_DETAILS, Toast.LENGTH_SHORT).show();
            }
        }
    }
}