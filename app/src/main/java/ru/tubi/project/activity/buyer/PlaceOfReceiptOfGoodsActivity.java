package ru.tubi.project.activity.buyer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ru.tubi.project.R;
import ru.tubi.project.models.DeliveryAddressModel;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.utilites.UserDataRecovery;

import static ru.tubi.project.Config.PARTNER_COMPANY_INFO_FOR_AGENT;
import static ru.tubi.project.free.AllCollor.TUBI_GREEN_300;
import static ru.tubi.project.free.AllCollor.TUBI_GREY_400;
import static ru.tubi.project.free.AllText.CHOOSE;
import static ru.tubi.project.free.AllText.FOR_YOUR_CITY_IS_NOT_DELIVERY;
import static ru.tubi.project.free.AllText.PLACE_OF_RECEIPT_OF_GOODS;
import static ru.tubi.project.free.VariablesHelpers.MY_CITY;
import static ru.tubi.project.free.VariablesHelpers.MY_REGION;
import static ru.tubi.project.utilites.Constant.API_TEST;

public class PlaceOfReceiptOfGoodsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvCompanyInfoForAgent, tvRegionDistrictCity, tvMessege, tvPlaceForReceiveOrder;
    private Button btnApply, btnDelivery, btnPickUpFromWarehouse;
    private String addressPartnerWarehouse, from_activity;
    private int partner_warehouse_id, addDeliveryKey;
    private DeliveryAddressModel addressForDelivery;
    public static int ENTER_FOR_DDELIVERY_ADDRESS_REQUEST = 7;
    public static int CHOOSE_DISTRIBUTION_WAREHOUSE_REQUEST = 8;
    private UserModel userDataModel;
    private UserDataRecovery userDataRecovery = new UserDataRecovery();
    private Intent takeit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_of_receipt_of_goods);
        setTitle(CHOOSE); //Выбор место получения товара
        getSupportActionBar().setSubtitle(""+PLACE_OF_RECEIPT_OF_GOODS);

        tvCompanyInfoForAgent = findViewById(R.id.tvCompanyInfoForAgent);
        tvRegionDistrictCity = findViewById(R.id.tvRegionDistrictCity);
        tvMessege = findViewById(R.id.tvMessege);
        tvPlaceForReceiveOrder = findViewById(R.id.tvPlaceForReceiveOrder);
        btnDelivery = findViewById(R.id.btnDelivery);
        btnPickUpFromWarehouse = findViewById(R.id.btnPickUpFromWarehouse);
        btnApply = findViewById(R.id.btnApply);

        btnApply.setOnClickListener(this);
        btnDelivery.setOnClickListener(this);
        btnPickUpFromWarehouse.setOnClickListener(this);

        btnApply.setClickable(false);

        takeit = getIntent();

        //from_activity = takeit.getStringExtra("from_activity");

        //получить из sqlLite данные пользователя
        userDataModel = userDataRecovery.getUserDataRecovery(this);
        if(userDataModel.getRole().equals("sales_agent")){
            tvCompanyInfoForAgent.setText(""+PARTNER_COMPANY_INFO_FOR_AGENT);
        }

        if(!MY_REGION.equals("Московская область") || MY_CITY.equals("Другой город")){
            tvRegionDistrictCity.setText(""+MY_REGION+" "+MY_CITY);
            tvMessege.setText(""+FOR_YOUR_CITY_IS_NOT_DELIVERY);
        }
        else{
            tvRegionDistrictCity.setText(""+MY_REGION+" "+MY_CITY);
        }
    }
    @Override
    public void onClick(View v) {
        if(v.equals(btnApply)){
            //если агент и пришел не из продуктАктивность то создать заказ и отправить в каталог
           // if(from_activity.equals("ChoosePartnerActivity")){
           //     addOrder();
           // }else{
                // поместите warehouse_id для передачи обратно в intent и закрыть это действие
                Intent intent = new Intent();

                intent.putExtra("addDeliveryKey", addDeliveryKey);
                intent.putExtra("warehouse_id", partner_warehouse_id);
                if(addDeliveryKey == 1){
                    intent.putExtra("addressForDelivery", addressForDelivery);
                }
                setResult(RESULT_OK, intent);
                finish();
            //}
        }
        else if(v.equals(btnPickUpFromWarehouse)){
            btnPickUpFromWarehouse.setBackgroundColor(TUBI_GREEN_300);
            btnDelivery.setBackgroundColor(TUBI_GREY_400);
            Intent intent = new Intent(this, ChooseDistributionWarehouseActivity.class);
            startActivityForResult(intent,CHOOSE_DISTRIBUTION_WAREHOUSE_REQUEST);
            //Toast.makeText(this, "Раздел находится в разработке", Toast.LENGTH_SHORT).show();
        }
        else if(v.equals(btnDelivery)){
            if(!MY_REGION.equals("Московская область")){
                Toast.makeText(this, ""+FOR_YOUR_CITY_IS_NOT_DELIVERY, Toast.LENGTH_SHORT).show();
                return;
            }
            if(MY_CITY.equals("Другой город")){
                Toast.makeText(this, ""+FOR_YOUR_CITY_IS_NOT_DELIVERY, Toast.LENGTH_SHORT).show();
                return;
            }
            btnPickUpFromWarehouse.setBackgroundColor(TUBI_GREY_400);
            btnDelivery.setBackgroundColor(TUBI_GREEN_300);
            Intent intent = new Intent(this, EnterForDeliveryAddressActivity.class);
            startActivityForResult(intent,ENTER_FOR_DDELIVERY_ADDRESS_REQUEST);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CHOOSE_DISTRIBUTION_WAREHOUSE_REQUEST
                && resultCode == RESULT_OK){
            addressPartnerWarehouse = data.getStringExtra("adressWarehouse");
            partner_warehouse_id = data.getIntExtra("warehouse_id",0);

            tvPlaceForReceiveOrder.setText(""+MY_REGION+" "+MY_CITY+"\n"+addressPartnerWarehouse);

            addDeliveryKey = 0;

            btnApply.setClickable(true);
            btnApply.setBackgroundColor(TUBI_GREEN_300);
            //Toast.makeText(this, "request 1", Toast.LENGTH_SHORT).show();
        }
        else if(requestCode == ENTER_FOR_DDELIVERY_ADDRESS_REQUEST
                && resultCode == RESULT_OK){
            partner_warehouse_id = data.getIntExtra("warehouse_id",0);
            addressForDelivery = (DeliveryAddressModel)data
                    .getSerializableExtra("addressForDelivery");

            tvPlaceForReceiveOrder.setText(""+MY_REGION+"\n"+addressForDelivery.toString());

            addDeliveryKey = 1;

            btnApply.setClickable(true);
            btnApply.setBackgroundColor(TUBI_GREEN_300);
            //Toast.makeText(this, "request 2", Toast.LENGTH_SHORT).show();
        }

    }
}