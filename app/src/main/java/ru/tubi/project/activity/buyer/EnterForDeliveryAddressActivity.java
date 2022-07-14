package ru.tubi.project.activity.buyer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ru.tubi.project.R;
import ru.tubi.project.adapters.PartnerCollectProductAdapter;
import ru.tubi.project.models.DeliveryAddressModel;
import ru.tubi.project.utilites.Constant;
import ru.tubi.project.utilites.InitialData;

import static ru.tubi.project.free.AllCollor.TUBI_GREEN_600;
import static ru.tubi.project.free.AllText.BUILDING;
import static ru.tubi.project.free.AllText.ENTER_HAUSE_NUM_TEXT;
import static ru.tubi.project.free.AllText.ENTER_PHONE_NUM_TEXT;
import static ru.tubi.project.free.AllText.ENTER_STREET_NAME_TEXT;
import static ru.tubi.project.free.AllText.IN_YOUR_CITY_IS_NOT_DELIVERY;
import static ru.tubi.project.free.AllText.LOAD_TEXT;
import static ru.tubi.project.free.AllText.ST;
import static ru.tubi.project.free.VariablesHelpers.DELIVERY_ADDRESS_LAST;
import static ru.tubi.project.free.VariablesHelpers.MY_CITY;
import static ru.tubi.project.free.VariablesHelpers.MY_DICTRICT;
import static ru.tubi.project.free.VariablesHelpers.MY_REGION;

public class EnterForDeliveryAddressActivity extends AppCompatActivity 
                                implements View.OnClickListener {

    private TextView tvRegionDistrictCity;
    private EditText etStreet, etHause, etBuilding, etAdditionalInformation
            , etPhoneForContact;
    private Button btnApply;
    private int partner_warehouse_id = 0;
    ArrayList<Integer> warehouse_idList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_for_delivery_address);

        btnApply = findViewById(R.id.btnApply);
        tvRegionDistrictCity = findViewById(R.id.tvRegionDistrictCity);
        etStreet = findViewById(R.id.etStreet);
        etHause = findViewById(R.id.etHause);
        etBuilding = findViewById(R.id.etBuilding);
        etAdditionalInformation = findViewById(R.id.etAdditionalInformation);
        etPhoneForContact = findViewById(R.id.etPhoneForContact);

        btnApply.setOnClickListener(this);
        btnApply.setBackgroundColor(TUBI_GREEN_600);
        
        if(MY_CITY.equals("Другой город")){
            tvRegionDistrictCity.setText(""+IN_YOUR_CITY_IS_NOT_DELIVERY);
        }else{
            tvRegionDistrictCity.setText(""+MY_REGION+" "+MY_CITY);
            if(!DELIVERY_ADDRESS_LAST.getStreet().isEmpty()){
                etStreet.setText(""+DELIVERY_ADDRESS_LAST.getStreet());
                etHause.setText(""+DELIVERY_ADDRESS_LAST.getHause());
                etBuilding.setText(""+DELIVERY_ADDRESS_LAST.getBuilding());
                etAdditionalInformation.setText(""+DELIVERY_ADDRESS_LAST.getAdditionalInformation());
                etPhoneForContact.setText(""+DELIVERY_ADDRESS_LAST.getPhoneForContact());
            }
        }

        receivePartnerWarehouseList(MY_CITY);
    }

    @Override
    public void onClick(View v) {
        String street = etStreet.getText().toString().trim();
        int hause = 0;
        String building = etBuilding.getText().toString().trim();
        String additionalInformation = etAdditionalInformation.getText().toString().trim();
        String phoneForContact = etPhoneForContact.getText().toString().trim();
        
        if(street.isEmpty()){
            Toast.makeText(this, ""+ENTER_STREET_NAME_TEXT, Toast.LENGTH_SHORT).show();
            return;
        }
        if(etHause.getText().toString().trim().isEmpty()){
            Toast.makeText(this, ""+ENTER_HAUSE_NUM_TEXT, Toast.LENGTH_SHORT).show();
            return;
        }else{
            hause = Integer.parseInt(etHause.getText().toString().trim());
        }
        if(phoneForContact.isEmpty()){
            Toast.makeText(this, ""+ENTER_PHONE_NUM_TEXT, Toast.LENGTH_SHORT).show();
            return;
        }

        DELIVERY_ADDRESS_LAST = new DeliveryAddressModel(MY_REGION, MY_DICTRICT, MY_CITY, street, hause
                        , building, additionalInformation, phoneForContact);
        // поместите warehouse_id для передачи обратно в intent и закрыть это действие
        Intent intent = new Intent();
        intent.putExtra("addressForDelivery", DELIVERY_ADDRESS_LAST);
        intent.putExtra("warehouse_id", partner_warehouse_id);
        setResult(RESULT_OK, intent);
        finish();

    }


    private void receivePartnerWarehouseList(String city){
        String url = Constant.API;
        url += "receive_partner_warehouse_list";
        url += "&" + "city=" +MY_CITY;
        url += "&" + "region=" +MY_REGION;
        String whatQuestion = "receive_partner_warehouse_list";
        setInitialData(url,whatQuestion);
    }
    private void setInitialData(String url_get, String whatQuestion) {
        ProgressDialog asyncDialog = new ProgressDialog(this);

        InitialData task=new InitialData(){
            @Override
            protected void onPreExecute() {
                // if( whatQuestion.equals("get_product_and_quantity")){
                asyncDialog.setMessage(LOAD_TEXT);
                asyncDialog.show();
                //  }
                super.onPreExecute();
            }
            protected void onPostExecute(String result) {
                if(whatQuestion.equals("receive_partner_warehouse_list")){
                    splitResultPartnerWarehouseList(result);
                }
                //скрыть диалоговое окно
                asyncDialog.dismiss();
            }
        };
        task.execute(url_get);
    }
    private void splitResultPartnerWarehouseList(String result){
        Log.d("A111","ChooseDistributionWarehouseActivity " +
                "/ splitResultPartnerWarehouseList / result="+result);
        warehouse_idList.clear();
        try {
            String[] res = result.split("<br>");
            String[] temp = res[0].split("&nbsp");
            if (temp[0].equals("error") || temp[0].equals("messege")) {
                Toast.makeText(this, "" + temp[1], Toast.LENGTH_LONG).show();
            } else {
                for (int i = 0; i < res.length; i++) {
                    temp = res[i].split("&nbsp");
                    int warehouse_info_id= Integer.parseInt(temp[0]);
                    String street = temp[1];
                    String house = temp[2];
                    int warehouse_id= Integer.parseInt(temp[4]);

                    warehouse_idList.add(warehouse_id);
                }
            }
        }catch (Exception ex){
        }
        if(warehouse_idList.size() > 0){
            partner_warehouse_id = warehouse_idList.get(0);
        }
    }
}