package ru.tubi.project.activity.company_my;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ru.tubi.project.R;
import ru.tubi.project.adapters.CollectProductForAdapter;
import ru.tubi.project.models.CollectProductModel;
import ru.tubi.project.utilites.InitialData;

import static ru.tubi.project.activity.Config.MY_COMPANY_TAXPAYER_ID;
import static ru.tubi.project.free.AllText.COLLECT_PRODUCT_FOR;
import static ru.tubi.project.free.AllText.TAX_ID;
import static ru.tubi.project.free.AllText.TO_FIND;
import static ru.tubi.project.utilites.Constant.API;

public class CollectProductForActivity extends AppCompatActivity
        implements View.OnClickListener ,TextWatcher{

    private RecyclerView recyclerView;
    private TextView tvCountryId,tvAdd, tvToFind, tvBuyerInfo;
    private EditText etPhone;
    public int textLength = 0, buyer_user_id;
    private long buyerCompTaxId, buyer_counterparty_id;
    private String stFullPhone="", fullPhoneNum="";
    private Intent takeit;

    private ArrayList<CollectProductModel> startOpenOrdersList = new ArrayList<>();
    private ArrayList<CollectProductModel> openOrdersList = new ArrayList<>();
    private CollectProductForAdapter adap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_product_for);
        setTitle(COLLECT_PRODUCT_FOR);//Собрать товар для
        getSupportActionBar().setSubtitle(TO_FIND);//найти

        recyclerView=findViewById(R.id.rvList);
        tvCountryId = findViewById(R.id.tvCountryId);
        tvAdd = findViewById(R.id.tvAdd);
        tvToFind = findViewById(R.id.tvToFind);
        tvBuyerInfo = findViewById(R.id.tvBuyerInfo);
        etPhone = findViewById(R.id.etPhone);

        tvAdd.setOnClickListener(this);
        tvToFind.setOnClickListener(this);
        etPhone.addTextChangedListener(this);

        try{
            takeit = getIntent();
            stFullPhone = takeit.getStringExtra("buyer_phone");
            if(!stFullPhone.isEmpty()) etPhone.setText(""+stFullPhone);
        }catch (Exception ex){}

        fullPhoneNum += tvCountryId.getText().toString();

        receiveAllWriteOffOrders();

        CollectProductForAdapter.RecyclerViewClickListener clickListener =
                new CollectProductForAdapter.RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        whatClickedPosition(position);
                    }
                };

        adap = new CollectProductForAdapter(this, openOrdersList, clickListener);
        recyclerView.setAdapter(adap);
    }

    private void whatClickedPosition(int position){
        int buyer_order_id = openOrdersList.get(position).getOrder_id();
        String buyer_comp_info = openOrdersList.get(position).getAbbreviation()+" "+
                openOrdersList.get(position).getCounterparty();
        int partner_warehouse_id = openOrdersList.get(position).getWarehouse_id();
        goNextActivity(buyer_order_id, buyer_comp_info , partner_warehouse_id);
    }
    @Override
    public void onClick(View v) {
        if(v.equals(tvAdd)){
            Intent intent = new Intent(this,CollectProductForAddActivity.class);
            intent.putExtra("buyer_phone", etPhone.getText().toString());
            startActivity(intent);
        }else if(v.equals(tvToFind)){
            tvBuyerInfo.setText("");
            getPhoneNumber();
            receiveBuyerInfo();
        }
    }
    //найти в списке открытые заказы для данного покупателя
    private void searchOpenOrder(){
        ArrayList<CollectProductModel> tempList = new ArrayList<>();
        for(int i=0;i < startOpenOrdersList.size();i++){
            if(startOpenOrdersList.get(i).getTaxpayer_id() == buyerCompTaxId){
                tempList.add(startOpenOrdersList.get(i));
            }
        }
        openOrdersList.clear();
        openOrdersList.addAll(tempList);
        adap.notifyDataSetChanged();
    }
    //получить номер телефона (очищенный) из tv
    private void getPhoneNumber(){
        etPhone.setText(stFullPhone);
        char[] strToArray = stFullPhone.toCharArray();
        fullPhoneNum = "";
        for(int j=0;j < strToArray.length;j++){
            if(j == 0 || j == 4 || j == 5 || j == 9){

            }else{
                fullPhoneNum += strToArray[j];
            }
        }
    }
    private void goNextActivity(int buyer_order_id, String buyer_comp_info,int partner_warehouse_id){
        Intent intent = new Intent(this,WriteOutProductActivity.class);
        intent.putExtra("buyer_order_id",buyer_order_id );
        intent.putExtra("partner_warehouse_id", partner_warehouse_id);
        intent.putExtra("info",buyer_comp_info);
        startActivity(intent);
    }
    private void showAllListOpenOrders(){
        openOrdersList.clear();
        openOrdersList.addAll(startOpenOrdersList);
        adap.notifyDataSetChanged();
    }
    //получить данные покупателя и его компании
    private void receiveBuyerInfo() {
        String url = API;
        url += "receive_buyer_info";
        url += "&"+"phoneNum="+fullPhoneNum;
        String whatQuestion = "receive_buyer_info";
        setInitialData(url, whatQuestion);
    }
    //получить все заказы которые выписывает компания
    private void receiveAllWriteOffOrders() {
        String url = API;
        url += "receive_all_write_off_orders";
        url += "&" + "taxpayer_id=" + MY_COMPANY_TAXPAYER_ID;
        String whatQuestion = "receive_all_write_off_orders";
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
                }
                /*else if (whatQuestion.equals("add_order_id_for_buyer")) {
                    buyer_order_id= Integer.parseInt(result);
                     goNextActivity();
                }*/
                else if (whatQuestion.equals("receive_all_write_off_orders")) {
                    splitAllWriteOffOrders(result);
                }//
            }
        };
        task.execute(url_get);
    }
    // разобрать результат с сервера, все заказы с которые выписывает компания
    private void splitAllWriteOffOrders(String result){
        startOpenOrdersList.clear();
        try{
            String[] res = result.split("<br>");
            String[] one_temp = res[0].split("&nbsp");
            if (one_temp[0].equals("error") || one_temp[0].equals("messege")) {
                Toast.makeText(this, "" + one_temp[1], Toast.LENGTH_LONG).show();
                return;
            } else {
                for (int i = 0; i < res.length; i++) {
                    String[] temp = res[i].split("&nbsp");
                    String buyer_name = temp[0];
                    String buyer_phone = temp[1];
                    String abbreviation = temp[2];
                    String counterparty = temp[3];
                    long taxpayer_id = Long.parseLong(temp[4]);

                    int warehouse_info_id = Integer.parseInt(temp[5]);
                    int warehouse_id = Integer.parseInt(temp[6]);
                    String city = temp[7];
                    String street = temp[8];
                    int house = Integer.parseInt(temp[9]);
                    String building="";
                    try { building=temp[10]; }catch (Exception ex){     }
                    int order_id = Integer.parseInt(temp[11]);

                    CollectProductModel openOrders = new CollectProductModel(buyer_name,
                            buyer_phone, abbreviation, counterparty, taxpayer_id,
                            warehouse_info_id, warehouse_id, city, street, house,
                            building, order_id);

                    startOpenOrdersList.add(openOrders);
                }
                showAllListOpenOrders();
            }
        }catch(Exception ex){
            Toast.makeText(this, "Ex: "+ex, Toast.LENGTH_SHORT).show();
        }
    }
    // разобрать результат с сервера, информация о покупателе и еге компании
    private void splitBuyerResult(String result){
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
                    buyer_counterparty_id = Long.parseLong(temp[5]);

                buyerCompTaxId=taxpayer_id_number;
                tvBuyerInfo.setText(name+"\n"
                        +abbreviation+" "+counterparty+"\n"+TAX_ID+" "+taxpayer_id_number);
            }
        }catch(Exception ex){
            Toast.makeText(this, "Exception: "+ex, Toast.LENGTH_SHORT).show();
        }
       
            searchOpenOrder();
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
      if(etPhone.length() < 2){
          showAllListOpenOrders();
          Toast.makeText(this, "lenght: 0", Toast.LENGTH_SHORT).show();
      }
    }


}