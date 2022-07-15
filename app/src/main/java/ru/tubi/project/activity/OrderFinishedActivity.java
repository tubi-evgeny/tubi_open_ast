package ru.tubi.project.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import ru.tubi.project.R;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.utilites.Constant;
import ru.tubi.project.utilites.InitialData;
import ru.tubi.project.utilites.SearchOrder_id;
import ru.tubi.project.utilites.UserDataRecovery;

import static ru.tubi.project.Config.ORDER_ID;
import static ru.tubi.project.free.AllText.DECORATED;
import static ru.tubi.project.free.AllText.DELIVERY_TO_ADDRESS;
import static ru.tubi.project.free.AllText.FINISHED_ORDER;
import static ru.tubi.project.free.AllText.LOAD_TEXT;
import static ru.tubi.project.free.AllText.MES_20;
import static ru.tubi.project.free.AllText.ON;
import static ru.tubi.project.free.AllText.POINT_OF_ISSUE;
import static ru.tubi.project.free.AllText.RECEIVING_FROM_WAREHOUSE;
import static ru.tubi.project.free.AllText.YOUR_ORDER;
import static ru.tubi.project.free.VariablesHelpers.ORDER_FINISHED_ACTIVITY_MESSEGE;

public class OrderFinishedActivity extends AppCompatActivity {

    LinearLayout lLayoutOrderNumber;
    private TextView tvOrderNumberInfo, tvMessege;
    private int deliveryKey, order_id;
    private String address_for_delivery, date, warehouseInfo;
    private Intent intent, takeit;
    private UserModel userDataModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_finished);
        setTitle(FINISHED_ORDER);//ЗАКАЗ ОФОРМЛЕН

        tvOrderNumberInfo = findViewById(R.id.tvOrderNumberInfo);
        tvMessege = findViewById(R.id.tvMessege);

        lLayoutOrderNumber = findViewById(R.id.lLayoutOrderNumber);
        lLayoutOrderNumber.setBackgroundResource(R.drawable.krugliye_ugli);

        //получить из sqlLite данные пользователя и компании
        UserDataRecovery userDataRecovery = new UserDataRecovery();
        userDataModel = userDataRecovery.getUserDataRecovery(this);

        takeit = getIntent();
        int dayToOrder = takeit.getIntExtra("day",0);
        order_id = takeit.getIntExtra("order_id",0);
        deliveryKey=takeit.getIntExtra("deliveryKey",0);
        date = takeit.getStringExtra("dateGiveOrder");
        warehouseInfo = takeit.getStringExtra("warehouseInfo");
        address_for_delivery = takeit.getStringExtra("address_for_delivery");

        //checkDelliveryKey();

           // tvOrderNumberInfo.setText(""+YOUR_ORDER+" № "+order_id+" "
            //        +DECORATED+"\n" +ON+" "+date+" \n"+POINT_OF_ISSUE+" \n"+warehouseInfo);


        if(deliveryKey == 1){
            tvOrderNumberInfo.setText(""
                    +YOUR_ORDER+" № "+order_id+" \n"
                    +DECORATED+"\n" +ON+" "+date+" \n"
                    +DELIVERY_TO_ADDRESS+" \n"+address_for_delivery);
        }else{
            tvOrderNumberInfo.setText(""
                    +YOUR_ORDER+" № "+order_id+" \n"
                    +DECORATED+"\n" +ON+" "+date+" \n"
                    +RECEIVING_FROM_WAREHOUSE+" \n"+warehouseInfo);
        }


        SearchOrder_id searchOrder_id = new SearchOrder_id();
        searchOrder_id.searchStartedOrder(this);
        ORDER_ID=0;

        if(ORDER_FINISHED_ACTIVITY_MESSEGE < 5){
            tvMessege.setText(""+MES_20);
            tvMessege.setVisibility(View.VISIBLE);
        }
        ORDER_FINISHED_ACTIVITY_MESSEGE++;
    }

   /* private void checkDelliveryKey() {
        if(deliveryKey == 1){
            receiveDeliveryAddress();
        }else{
            showPlaceReceivingOrder();
        }
    }*/
    /*private void showPlaceReceivingOrder(){
        if(deliveryKey == 1){

        }else{
            tvOrderNumberInfo.setText(""+YOUR_ORDER+" № "+order_id+" "
                    +DECORATED+"\n" +ON+" "+date+" \n"+POINT_OF_ISSUE+" \n"+warehouseInfo);

        }
    }*/
    //получить адресс доставки
   /* private void receiveDeliveryAddress(){
        String url = Constant.API_TEST;
        url += "receive_delivery_address";
        url += "&" + "order_id=" +order_id;
        String whatQuestion = "receive_delivery_address";
        setInitialData(url,whatQuestion);
    }*/
   /* private void setInitialData(String url_get, String whatQuestion) {
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
                //if(whatQuestion.equals("show_products")){
                asyncDialog.setMessage(LOAD_TEXT);
                asyncDialog.show();
                //  }
                super.onPreExecute();
            }
            protected void onPostExecute(String result) {
                if(whatQuestion.equals("receive_delivery_address")){
                    splitAddressDelivery(result);
                }
                //скрыть диалоговое окно
                asyncDialog.dismiss();
            }
        };
        task.execute(url_get);
    }*/
    //разобрать ответ адрес доставки
   /* private void splitAddressDelivery(String result){
        address_for_delivery = result;

        showPlaceReceivingOrder();
    }*/

    @Override
    public void onBackPressed() {
        intent = new Intent(this,ActivityCatalog.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    public boolean onOptionsItemSelected(MenuItem item){
        int itemID=item.getItemId();
        if(itemID==R.id.menu){
            intent=new Intent(this, MenuActivity.class);
            startActivity(intent);
        }
        if(itemID==R.id.main){
            intent=new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        if(itemID==R.id.category){
            intent=new Intent(this, ActivityCatalog.class);
            startActivity(intent);
        }
        if(itemID==R.id.shoping_box){
            intent=new Intent(this, ShopingBox.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
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