package ru.tubi.project.activity.company_my;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ru.tubi.project.R;
import ru.tubi.project.models.CounterpartyModel;
import ru.tubi.project.models.WarehouseModel;
import ru.tubi.project.utilites.Constant;
import ru.tubi.project.utilites.InitialData;

import static android.provider.ContactsContract.CommonDataKinds.StructuredPostal.STREET;
import static ru.tubi.project.Config.MY_COMPANY_TAXPAYER_ID;
import static ru.tubi.project.Config.MY_UID;
import static ru.tubi.project.free.AllCollor.TUBI_BLACK;
import static ru.tubi.project.free.AllCollor.TUBI_GREY_200;
import static ru.tubi.project.free.AllText.ADD;
import static ru.tubi.project.free.AllText.BUILDING;
import static ru.tubi.project.free.AllText.C;
import static ru.tubi.project.free.AllText.COLLECT_PRODUCT_FOR;
import static ru.tubi.project.free.AllText.DATE_ABOUT_COMPANY_MISSING;
import static ru.tubi.project.free.AllText.ENTER_CITY;
import static ru.tubi.project.free.AllText.PHONE_SHORT;
import static ru.tubi.project.free.AllText.RETURN_BIG;
import static ru.tubi.project.free.AllText.ST;
import static ru.tubi.project.free.AllText.TAX_ID;
import static ru.tubi.project.free.AllText.TAX_ID_SMALL;
import static ru.tubi.project.free.AllText.TO_FIND;
import static ru.tubi.project.free.AllText.WAREHOUSE;
import static ru.tubi.project.utilites.Constant.API;

public class CollectProductForAddActivity extends AppCompatActivity
        implements View.OnClickListener , TextWatcher{

    private TextView tvCountryId,tvAdd, tvToFind, tvBuyerInfo,tvCityOrWarehouse,tvNext;
    private EditText etPhone;
    private LinearLayout  llCity, llWarehouse;
    public int textLength = 0, buyer_user_id, buyer_counterparty_id, partner_warehouse_id,
            buyer_order_id;
    private long buyerCompTaxId=0;
    private String stFullPhone="",fullPhoneNum="", city, buyer_comp_info="",
            partner_warehouse_info="";
   // private String receiveBuyerInfoKey = "";
    private Intent takeit;
    private ListView  lvCity,lvWarehouse;
    private ArrayList<WarehouseModel> warehouseList = new ArrayList<>();
    private ArrayList<String> stWarehouseList = new ArrayList<>();
    private ArrayList<CounterpartyModel> allWriteOffOrderslist = new ArrayList<>();

    private ArrayList<String> stAllWriteOffOrdersList = new ArrayList<>();
    private ArrayList<String> startStAllWriteOffOrdersList = new ArrayList<>();

    private ArrayList<String> cityList = new ArrayList<>();
   // private ArrayAdapter<String> adapAllOrders;
    private ArrayAdapter<String> adapCity;
    private ArrayAdapter<String> adapWarehouse;
    private AlertDialog ad;
    private AlertDialog.Builder adb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_product_for_add);
        setTitle(COLLECT_PRODUCT_FOR);//Собрать товар для
        getSupportActionBar().setSubtitle(ADD);//добавить

        tvCountryId = findViewById(R.id.tvCountryId);
        tvAdd = findViewById(R.id.tvAdd);
        tvToFind = findViewById(R.id.tvToFind);
        tvBuyerInfo = findViewById(R.id.tvBuyerInfo);
        tvCityOrWarehouse = findViewById(R.id.tvCityOrWarehouse);
        tvNext = findViewById(R.id.tvNext);
        etPhone = findViewById(R.id.etPhone);
       // lvAllOrdersInfo = findViewById(R.id.lvAllOrdersInfo);
        lvCity = findViewById(R.id.lvCity);
        lvWarehouse = findViewById(R.id.lvWarehouse);
       // llAllOrdersInfo = findViewById(R.id.llAllOrdersInfo);
        llCity = findViewById(R.id.llCity);
        llWarehouse = findViewById(R.id.llWarehouse);

        tvAdd.setOnClickListener(this);
        tvToFind.setOnClickListener(this);
        tvCityOrWarehouse.setOnClickListener(this);
        tvNext.setOnClickListener(this);
        etPhone.addTextChangedListener(this);

       // lvAllOrdersInfo.setOnItemClickListener(this);

        tvNext.setClickable(false);

        takeit = getIntent();
        stFullPhone = takeit.getStringExtra("buyer_phone");
        etPhone.setText(""+stFullPhone);

        fullPhoneNum += tvCountryId.getText().toString();

    }

    @Override
    public void onClick(View v) {
        if(v.equals(tvAdd)){
           // receiveBuyerInfoKey = "add";
            tvBuyerInfo.setText("");
            getPhoneNumber();
           // llAllOrdersInfo.setVisibility(View.GONE);
            receiveBuyerInfo();
        }else if(v.equals(tvToFind)){
            Intent intent = new Intent(this,CollectProductForActivity.class);
            intent.putExtra("buyer_phone", etPhone.getText().toString());
            startActivity(intent);
        }else if(v.equals(tvCityOrWarehouse)){
            tvCityOrWarehouse.setHint(ENTER_CITY);
            tvNext.setBackgroundResource(R.drawable.round_background_grey_200_black);
            tvNext.setClickable(false);
            receiveCity();
        }else if(v.equals(tvNext)){
            // создать новый заказ для покупателя(оформление поставщиком)
         /*   if(buyer_user_id == 0 || buyer_counterparty_id == 0 || partner_warehouse_id ==0){
                Toast.makeText(this, "tvNext данных не достаточно", Toast.LENGTH_SHORT).show();
            }else{*/
                addOrderForBuyer();
           // }
        }
    }
    //найти в списке открытые заказы для данного покупателя
   /* private void searchOpenOrder(){
        ArrayList<String> tempList = new ArrayList<>();
        for(int i=0;i < allWriteOffOrderslist.size();i++){
            if(allWriteOffOrderslist.get(i).getTaxpayer_id() == buyerCompTaxId){
                tempList.add(stAllWriteOffOrdersList.get(i));
            }
        }
        stAllWriteOffOrdersList.clear();
        stAllWriteOffOrdersList.addAll(tempList);
        //adapAllOrders.notifyDataSetChanged();
    }*/
    //получить номер телефона (очищенный) из tv
    private void getPhoneNumber(){
        etPhone.setText(stFullPhone);
        char[] strToArray = stFullPhone.toCharArray();
        fullPhoneNum = "";
        // fullPhoneNum = tvCountryId.getText().toString();
        for(int j=0;j < strToArray.length;j++){
            if(j == 0 || j == 4 || j == 5 || j == 9){

            }else{
                fullPhoneNum += strToArray[j];
            }
        }
    }
    private void goNextActivity(){
        //buyer_comp_info =
        Intent intent = new Intent(this,WriteOutProductActivity.class);
        intent.putExtra("buyer_order_id",buyer_order_id );
        intent.putExtra("info",buyer_comp_info);
        intent.putExtra("partner_warehouse_info",partner_warehouse_info);
        startActivity(intent);
    }
    private void showWarehouse(){
        llWarehouse.setVisibility(View.VISIBLE);
        adapWarehouse= new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,stWarehouseList);
        lvWarehouse.setAdapter(adapWarehouse);
        lvWarehouse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tvCityOrWarehouse.setText(stWarehouseList.get(position));
                partner_warehouse_id = warehouseList.get(position).getWarehouse_id();

                llWarehouse.setVisibility(View.GONE);
                tvNext.setBackgroundResource(R.drawable.round_corners_green_600_and_black);
                tvNext.setClickable(true);
                //Toast.makeText(CollectProductForActivity.this, "position: "+position, Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showCity(){
        llCity.setVisibility(View.VISIBLE);
        adapCity = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,cityList);
        lvCity.setAdapter(adapCity);
        if(cityList.size() == 1){
            city = cityList.get(0);
            tvCityOrWarehouse.setText(city);
            tvCityOrWarehouse.setTextColor(TUBI_BLACK);
            llCity.setVisibility(View.GONE);
            receivePartnerWarehouse();
        }
    }
    // создать новый заказ для покупателя(оформление поставщиком)
    private void addOrderForBuyer(){
        String url = Constant.API;
        url += "add_order_id_for_buyer";
        url += "&" + "creator_user_uid=" + MY_UID;
        url += "&" + "taxpayer_id=" + MY_COMPANY_TAXPAYER_ID;
        url += "&" + "buyer_user_id=" + buyer_user_id;
        url += "&" + "buyer_counterparty_id=" + buyer_counterparty_id;
        url += "&" + "partner_warehouse_id=" + partner_warehouse_id;
        String whatQuestion= "add_order_id_for_buyer";
        setInitialData(url, whatQuestion);//buyer_user_id  ,partner_warehouse_id
    }
    //получить список городов присутствия компании
    private void receiveCity(){
        if(buyer_counterparty_id != 0 ){
            String url = API;
            url += "receive_city_list";
            String whatQuestion = "receive_city_list";
            setInitialData(url, whatQuestion);
        }else{
            allertDialog();
            //lvAllOrdersInfo.setVisibility(View.VISIBLE);
        }
    }
    //получить список складов партнера с которых можно забрать товар
    private void receivePartnerWarehouse(){
        String url = API;
        url += "receive_partner_warehouse_info";
        url += "&"+"city="+city;
        String whatQuestion = "receive_partner_warehouse_info";
        setInitialData(url, whatQuestion);
    }
    //получить данные покупателя и его компании
    private void receiveBuyerInfo() {
        String url = API;
        url += "receive_buyer_info";
        url += "&"+"phoneNum="+fullPhoneNum;
        String whatQuestion = "receive_buyer_info";
        setInitialData(url, whatQuestion);
    }

    private void setInitialData(String url_get, String whatQuestion) {
        InitialData task=new InitialData(){
          /*  @Override
            protected void onPreExecute() {
                //set message of the dialog
                asyncDialog.setMessage(LOAD_TEXT);
                //show dialog
                asyncDialog.show();
                super.onPreExecute();
            }*/

            @RequiresApi(api = Build.VERSION_CODES.N)
            protected void onPostExecute(String result) {
                //Do your thing
                if(whatQuestion.equals("receive_buyer_info")){
                    splitBuyerResult(result);
                }else if(whatQuestion.equals("receive_city_list")){
                    splitCityResult(result);
                }else if(whatQuestion.equals("receive_partner_warehouse_info")){
                    splitPartnerWarehouseResult(result);
                }else if (whatQuestion.equals("add_order_id_for_buyer")) {
                    buyer_order_id= Integer.parseInt(result);
                    goNextActivity();
                }/*else if (whatQuestion.equals("receive_all_write_off_orders")) {
                    splitAllWriteOffOrders(result);
                }*/
            }
        };
        task.execute(url_get);
    }

    // разобрать результат с сервера, список складов в запрошенном городе
    private void splitPartnerWarehouseResult(String result){
        //Toast.makeText(this, "res: \n"+result, Toast.LENGTH_SHORT).show();
        warehouseList.clear();
        stWarehouseList.clear();
        try{
            String[] res = result.split("<br>");
            String[] one_temp = res[0].split("&nbsp");
            if (one_temp[0].equals("error") || one_temp[0].equals("messege")) {
                Toast.makeText(this, "" + one_temp[1], Toast.LENGTH_LONG).show();
                return;
            } else {
                for (int i = 0; i < res.length; i++) {
                    String[] temp = res[i].split("&nbsp");
                    int warehouse_info_id = Integer.parseInt(temp[0]);
                    int warehouse_id = Integer.parseInt(temp[1]);
                    String city = temp[2];
                    String street = temp[3];
                    int house = Integer.parseInt(temp[4]);
                    String building="";
                    try { building=temp[5]; }catch (Exception ex){     }

                    WarehouseModel warehouse = new WarehouseModel(warehouse_id,city,street,
                            house, building, warehouse_info_id);

                    String warehouse_info = WAREHOUSE+" № "+warehouse_info_id+"/"+
                            warehouse_id+" "+city+
                            " "+ST+" "+street+" "+house;
                    if(!building.isEmpty()){
                        warehouse_info += " "+C+". "+building;
                    }
                    warehouseList.add(warehouse);
                    stWarehouseList.add(warehouse_info);
                }
            }
        }catch(Exception ex){
            Toast.makeText(this, "Exception: "+ex, Toast.LENGTH_SHORT).show();
        }
        showWarehouse();
    }
    // разобрать результат с сервера, список городов присутствия компании
    private void splitCityResult(String result){
        // Toast.makeText(this, "res: \n"+result, Toast.LENGTH_SHORT).show();
        cityList.clear();
        try{
            String[] res = result.split("<br>");
            String[] one_temp = res[0].split("&nbsp");
            if (one_temp[0].equals("error") || one_temp[0].equals("messege")) {
                Toast.makeText(this, "" + one_temp[1], Toast.LENGTH_LONG).show();
                return;
            } else {
                for (int i = 0; i < res.length; i++) {
                    String[] temp = res[i].split("&nbsp");
                    String city = temp[0];

                    cityList.add(city);
                }
            }
        }catch(Exception ex){
            Toast.makeText(this, "Exception: "+ex, Toast.LENGTH_SHORT).show();
        }
        showCity();
    }
    // разобрать результат с сервера, информация о покупателе и еге компании
    private void splitBuyerResult(String result){
        //Toast.makeText(this, "res: \n"+result, Toast.LENGTH_SHORT).show();
        try{
            String[] res = result.split("<br>");
            String[] one_temp = res[0].split("&nbsp");
            if (one_temp[0].equals("error") || one_temp[0].equals("messege")) {
                Toast.makeText(this, "" + one_temp[1], Toast.LENGTH_LONG).show();
                return;
            } else {
                String[] temp = res[0].split("&nbsp");
                buyer_user_id = Integer.parseInt(temp[0]);
                String name = temp[1];
                String abbreviation = temp[2];
                String counterparty = temp[3];
                long taxpayer_id_number = Long.parseLong(temp[4]);
                buyer_counterparty_id = Integer.parseInt(temp[5]);

                // Toast.makeText(this, "id: "+buyer_counterparty_id, Toast.LENGTH_SHORT).show();
                buyerCompTaxId=taxpayer_id_number;
                buyer_comp_info = abbreviation+" "+counterparty;
                tvBuyerInfo.setText(name+"\n"
                        +abbreviation+" "+counterparty+"\n"+TAX_ID+" "+taxpayer_id_number);
            }
        }catch(Exception ex){
            Toast.makeText(this, "Exception: splitBuyerResult / "+ex, Toast.LENGTH_SHORT).show();
        }
            receiveCity();
    }

    private void allertDialog() {
        adb = new AlertDialog.Builder(this);
        String st1 = DATE_ABOUT_COMPANY_MISSING;
        adb.setTitle(st1);

        adb.setNeutralButton(RETURN_BIG, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ad.cancel();
            }
        });

        ad=adb.create();
        ad.setCanceledOnTouchOutside(false);
        ad.setCancelable(false);
        ad.show();

        Button buttonbackground2 = ad.getButton(DialogInterface.BUTTON_NEUTRAL);
        buttonbackground2.setBackgroundColor(TUBI_GREY_200);
        buttonbackground2.setTextColor(Color.WHITE);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String phoneText = etPhone.getText().toString();
        textLength = etPhone.getText().length();

        if (phoneText.endsWith("-") || phoneText.endsWith(" "))//|| phoneText.endsWith(" ")
            return;
        if(textLength < 15) {
            stFullPhone = phoneText;
            if (textLength == 1) {
                if (!phoneText.contains("(")) {
                    etPhone.setText(new StringBuilder(phoneText).insert(phoneText.length() - 1, "(").toString());
                    etPhone.setSelection(etPhone.getText().length());
                }

            } else if (textLength == 5) {

                if (!phoneText.contains(")")) {
                    etPhone.setText(new StringBuilder(phoneText).insert(phoneText.length() - 1, ")").toString());
                    etPhone.setSelection(etPhone.getText().length());
                }

            } else if (textLength == 6) {
                etPhone.setText(new StringBuilder(phoneText).insert(phoneText.length() - 1, " ").toString());
                etPhone.setSelection(etPhone.getText().length());

            } else if (textLength == 10) {
                if (!phoneText.contains("-")) {
                    etPhone.setText(new StringBuilder(phoneText).insert(phoneText.length() - 1, "-").toString());
                    etPhone.setSelection(etPhone.getText().length());
                }
            }
        }else {
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


}