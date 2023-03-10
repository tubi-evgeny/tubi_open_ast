package ru.tubi.project.activity.buyer;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import ru.tubi.project.R;
import ru.tubi.project.activity.ActivityCatalog;
import ru.tubi.project.models.Catalog;
import ru.tubi.project.models.DeliveryAddressModel;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.utilites.InitialDataPOST;
import ru.tubi.project.utilites.UserDataRecovery;

import static ru.tubi.project.activity.Config.PARTNER_COMPANY_INFO_FOR_AGENT;
import static ru.tubi.project.free.AllCollor.TUBI_GREEN_300;
import static ru.tubi.project.free.AllCollor.TUBI_GREY_200;
import static ru.tubi.project.free.AllCollor.TUBI_GREY_400;
import static ru.tubi.project.free.AllCollor.alert_dialog_button_green_pressed;
import static ru.tubi.project.free.AllText.CHECK_CONNECT_INTERNET;
import static ru.tubi.project.free.AllText.CHOOSE;
import static ru.tubi.project.free.AllText.CONTINUE_TEXT;
import static ru.tubi.project.free.AllText.FOR_YOUR_CITY_IS_NOT_DELIVERY;
import static ru.tubi.project.free.AllText.LOAD_TEXT;
import static ru.tubi.project.free.AllText.MES_23;
import static ru.tubi.project.free.AllText.MES_24;
import static ru.tubi.project.free.AllText.MES_27;
import static ru.tubi.project.free.AllText.PLACE_OF_RECEIPT_OF_GOODS;
import static ru.tubi.project.free.AllText.PRICES_WILL_BE_CHANGED;
import static ru.tubi.project.free.AllText.RUB;
import static ru.tubi.project.free.AllText.VIEW_PRICES_TEXT;
import static ru.tubi.project.free.VariablesHelpers.DELIVERY_OPEN;
import static ru.tubi.project.free.VariablesHelpers.DELIVERY_TO_BUYER_STATUS;
import static ru.tubi.project.free.VariablesHelpers.MY_CITY;
import static ru.tubi.project.free.VariablesHelpers.MY_REGION;
import static ru.tubi.project.utilites.Constant.API;
import static ru.tubi.project.utilites.Constant.GET_CATALOG;
import static ru.tubi.project.utilites.InitialDataPOST.getParamsString;

public class PlaceOfReceiptOfGoodsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvCompanyInfoForAgent, tvRegionDistrictCity, tvMessege, tvPlaceForReceiveOrder;
    private Button btnApply, btnDelivery, btnPickUpFromWarehouse;
    private String addressPartnerWarehouse, from_activity;
    private int partner_warehouse_id, addDeliveryKey;
    private double orderSummMin=0;
    private DeliveryAddressModel addressForDelivery;
    public static int ENTER_FOR_DDELIVERY_ADDRESS_REQUEST = 7;
    public static int CHOOSE_DISTRIBUTION_WAREHOUSE_REQUEST = 8;
    private UserModel userDataModel;
    private UserDataRecovery userDataRecovery = new UserDataRecovery();
    private Intent takeit;
    private AlertDialog.Builder adb;
    private AlertDialog ad;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_of_receipt_of_goods);
        setTitle(CHOOSE); //?????????? ?????????? ?????????????????? ????????????
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

        //???????????????? ???? sqlLite ???????????? ????????????????????????
        userDataModel = userDataRecovery.getUserDataRecovery(this);
        if(userDataModel.getRole().equals("sales_agent")){
            tvCompanyInfoForAgent.setText(""+PARTNER_COMPANY_INFO_FOR_AGENT);
        }
        //???????????? ???????????????? ??????????????
        if (DELIVERY_OPEN == 1){
            btnDelivery.setVisibility(View.VISIBLE);
        }else{
            btnDelivery.setVisibility(View.GONE);
        }
        checkDeliveryOpen();
        receiveOrderSummMin();
        if(!MY_REGION.equals("???????????????????? ??????????????") || MY_CITY.equals("???????????? ??????????")){
            tvRegionDistrictCity.setText(""+MY_REGION+" "+MY_CITY);
            tvMessege.setText(""+FOR_YOUR_CITY_IS_NOT_DELIVERY);
        }
        else{
            tvRegionDistrictCity.setText(""+MY_REGION+" "+MY_CITY);
        }
        if(DELIVERY_TO_BUYER_STATUS == 0){
            btnPickUpFromWarehouse.setBackgroundColor(TUBI_GREEN_300);
            btnDelivery.setBackgroundColor(TUBI_GREY_400);
        }
        else if(DELIVERY_TO_BUYER_STATUS == 1){
            btnPickUpFromWarehouse.setBackgroundColor(TUBI_GREY_400);
            btnDelivery.setBackgroundColor(TUBI_GREEN_300);
        }
    }
    @Override
    public void onClick(View v) {
        if(v.equals(btnApply)){
            if(addDeliveryKey == 1){
                //????????????????=1
                DELIVERY_TO_BUYER_STATUS = 1;
            }else{
                //??????????????????=0
                DELIVERY_TO_BUYER_STATUS = 0;
            }
                // ?????????????????? warehouse_id ?????? ???????????????? ?????????????? ?? intent ?? ?????????????? ?????? ????????????????
                Intent intent = new Intent();

                intent.putExtra("addDeliveryKey", addDeliveryKey);
                intent.putExtra("warehouse_id", partner_warehouse_id);
                if(addDeliveryKey == 1){
                    intent.putExtra("addressForDelivery", addressForDelivery);
                }
                setResult(RESULT_OK, intent);
                finish();
        }
        else if(v.equals(btnPickUpFromWarehouse)) {
            if (DELIVERY_TO_BUYER_STATUS == 0) {
                //???????????????????? ?????????? ????????????
                chooseDistributionWarehouse();
               /* btnPickUpFromWarehouse.setBackgroundColor(TUBI_GREEN_300);
                btnDelivery.setBackgroundColor(TUBI_GREY_400);
                Intent intent = new Intent(this, ChooseDistributionWarehouseActivity.class);
                startActivityForResult(intent, CHOOSE_DISTRIBUTION_WAREHOUSE_REQUEST);*/
            }else{
                //???????????????? ???????? ???? ???????????? ?????? ?????????????????
                adPriceWithoutDelivery();
            }
        }
        else if(v.equals(btnDelivery)){
            if(!MY_REGION.equals("???????????????????? ??????????????")){
                Toast.makeText(this, ""+FOR_YOUR_CITY_IS_NOT_DELIVERY, Toast.LENGTH_SHORT).show();
                return;
            }
            if(MY_CITY.equals("???????????? ??????????")){
                Toast.makeText(this, ""+FOR_YOUR_CITY_IS_NOT_DELIVERY, Toast.LENGTH_SHORT).show();
                return;
            }
            if (DELIVERY_TO_BUYER_STATUS == 1) {
                //???????????????????? ???????? ???????????? ??????????????
                enterForDeliveryAddress();
            }else{
                //???????????????? ???????? ???? ???????????? ???????? ?????????????????
                adPricePlusDelivery();
            }
           /* btnPickUpFromWarehouse.setBackgroundColor(TUBI_GREY_400);
            btnDelivery.setBackgroundColor(TUBI_GREEN_300);
            Intent intent = new Intent(this, EnterForDeliveryAddressActivity.class);
            startActivityForResult(intent,ENTER_FOR_DDELIVERY_ADDRESS_REQUEST);*/
        }
    }
    //???????????????????? ???????? ???????????? ??????????????
    private void enterForDeliveryAddress(){
        btnPickUpFromWarehouse.setBackgroundColor(TUBI_GREY_400);
        btnDelivery.setBackgroundColor(TUBI_GREEN_300);
        Intent intent = new Intent(this, EnterForDeliveryAddressActivity.class);
        startActivityForResult(intent,ENTER_FOR_DDELIVERY_ADDRESS_REQUEST);
    }
    //???????????????????? ?????????? ????????????
    private void chooseDistributionWarehouse(){
        btnPickUpFromWarehouse.setBackgroundColor(TUBI_GREEN_300);
        btnDelivery.setBackgroundColor(TUBI_GREY_400);
        Intent intent = new Intent(this, ChooseDistributionWarehouseActivity.class);
        startActivityForResult(intent, CHOOSE_DISTRIBUTION_WAREHOUSE_REQUEST);
    }
    //?????????????????? ???????????????? ?????? ???????????????? ??????????????
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void checkDeliveryOpen(){
        final Map<String, String> parameters = new HashMap<>();
        parameters.put("check_delivery_open","");
        String whatQuestion = "check_delivery_open";
        setInitialDataPOST(API, parameters, whatQuestion);
    }
    //?????????????????? ?????????????????????? ?????????? ???????????? ????????????
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void receiveOrderSummMin(){
        final Map<String, String> parameters = new HashMap<>();
        parameters.put("receive_order_summ_min","");
        String whatQuestion = "receive_order_summ_min";
        setInitialDataPOST(API, parameters, whatQuestion);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setInitialDataPOST(String url, Map<String, String> param, String whatQuestion){
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
                if(whatQuestion.equals("check_delivery_open")) {
                    splitResult(s);
                }
                else if(whatQuestion.equals("receive_order_summ_min")) {
                    splitOrderSummMin(s);
                }
                //???????????? ???????????????????? ????????
                asyncDialog.dismiss();
            }
        };
        task.execute(url, getParamsString(param));
    }
    private void splitOrderSummMin(String result){
        try{
            orderSummMin = Double.parseDouble(result);
        }catch (Exception ex){
            Log.d("A111","PlaceOfReceiptOfGoodsActivity / splitOrderSummMin / result="+result+" / ex="+ex);
        }
    }
    private void splitResult(String result){
        try{
            int x = Integer.parseInt(result);
            DELIVERY_OPEN = x;
        }catch(Exception ex){

        }
        //???????????? ???????????????? ??????????????
        if (DELIVERY_OPEN == 1){
            btnDelivery.setVisibility(View.VISIBLE);
        }else{
            btnDelivery.setVisibility(View.GONE);
        }
        Log.d("A111","PlaceOfReceiptOfGoodsActivity / splitResult / result = "+result);
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
    //???????????????? ???????? ???? ???????????? ???????? ?????????????????
    private void adPricePlusDelivery(){
        adb = new AlertDialog.Builder(this);
        String st1 = MES_27+" "+orderSummMin+" "+RUB;
        String st2 = MES_24;

        adb.setPositiveButton(CONTINUE_TEXT, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //???????????????????? ???????? ???????????? ??????????????
                enterForDeliveryAddress();
            }
        });
        adb.setNeutralButton(VIEW_PRICES_TEXT, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DELIVERY_TO_BUYER_STATUS = 1;//????????????????=1
                Intent intent = new Intent(getApplication(), ActivityCatalog.class);
                startActivity(intent);
            }
        });
        adb.setTitle(st1);
        adb.setMessage(st2);

        ad=adb.create();
        //ad.setCanceledOnTouchOutside(false);
        //ad.setCancelable(false);
        ad.show();

        Button buttonbackground1 = ad.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonbackground1.setBackgroundColor(alert_dialog_button_green_pressed);
        buttonbackground1.setTextColor(Color.WHITE);
        Button buttonbackground2 = ad.getButton(DialogInterface.BUTTON_NEUTRAL);
        buttonbackground2.setBackgroundColor(TUBI_GREY_200);
        buttonbackground2.setTextColor(Color.WHITE);
    }
    //???????????????? ???????? ???? ???????????? ?????? ?????????????????
    private void adPriceWithoutDelivery( ){
        adb = new AlertDialog.Builder(this);
        String st1 = PRICES_WILL_BE_CHANGED;
        String st2 = MES_23;

        adb.setPositiveButton(CONTINUE_TEXT, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //???????????????????? ?????????? ????????????
                chooseDistributionWarehouse();
            }
        });
        adb.setNeutralButton(VIEW_PRICES_TEXT, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DELIVERY_TO_BUYER_STATUS = 0;//??????????????????=0
                Intent intent = new Intent(getApplication(), ActivityCatalog.class);
                startActivity(intent);
            }
        });
        adb.setTitle(st1);
        adb.setMessage(st2);

        ad=adb.create();
        //ad.setCanceledOnTouchOutside(false);
        //ad.setCancelable(false);
        ad.show();

        Button buttonbackground1 = ad.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonbackground1.setBackgroundColor(alert_dialog_button_green_pressed);
        buttonbackground1.setTextColor(Color.WHITE);
        Button buttonbackground2 = ad.getButton(DialogInterface.BUTTON_NEUTRAL);
        buttonbackground2.setBackgroundColor(TUBI_GREY_200);
        buttonbackground2.setTextColor(Color.WHITE);
    }
}