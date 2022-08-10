package ru.tubi.project.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import ru.tubi.project.R;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.utilites.InitialData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

//import static com.example.tubi.Config.MY_TAXPAYER_ID;
import ru.tubi.project.utilites.Constant;
import ru.tubi.project.utilites.UserDataRecovery;

import static ru.tubi.project.free.AllCollor.TUBI_GREEN_600;
import static ru.tubi.project.free.AllText.BUILDING;
import static ru.tubi.project.free.AllText.DELIVERY_TO_ADDRESS;
import static ru.tubi.project.free.AllText.GENERAL_VEIGHT;
import static ru.tubi.project.free.AllText.HOUR_CHAR;
import static ru.tubi.project.free.AllText.KILOGRAM;
import static ru.tubi.project.free.AllText.LOAD_TEXT;
import static ru.tubi.project.free.AllText.MES_25;
import static ru.tubi.project.free.AllText.POSITION;
import static ru.tubi.project.free.AllText.RECEIVING_FROM_WAREHOUSE;
import static ru.tubi.project.free.AllText.RUB;
import static ru.tubi.project.free.AllText.SMOLENSCK;
import static ru.tubi.project.free.AllText.ST;
import static ru.tubi.project.free.AllText.YEAR_CHAR;
import static ru.tubi.project.free.AllText.YOUR_ORDER;
import static ru.tubi.project.free.VariablesHelpers.DELIVERY_TO_BUYER_STATUS;
import static ru.tubi.project.free.VariablesHelpers.MESSAGE_FROM_ORDER_ACTIVITY;
import static ru.tubi.project.free.AllText.MAKING_ORDER;
import static ru.tubi.project.free.AllText.SUGGESTIONS;

public class MakingOrderActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvSuggestions, tvYourSuggestions, tvProductGeneralInfo,
            tvSumm, tvScore, tvWarehouseInfo, tvGiveDate;
    private LinearLayout llSuggestions;//,llWarehouseList;
    private Button btnGoBuy;
    //private ListView lvWarehouseList;
    private String [] cityList= {SMOLENSCK, "выберите город"};//,"MOSCOW"
    private ArrayList <String> adressWarehouseList = new ArrayList<>();
    private ArrayAdapter <String> adapter;
    private ArrayAdapter <String> adapWarehouse;
    private Intent intent;
    private String city, dateGiveOrder, timeReceiveOrder, address_for_delivery;
    private String warehouseInfo;
    private int order_id, warehouse_id, count, veight, deliveryKey, x=0;//, buttonColor = 0
    private double priceSumm, z;
    private AlertDialog ad;
    private AlertDialog.Builder adb;
    private boolean flagCiti=false, flagDay=false;
    private ArrayList<Integer> dayNumsList ;
    ArrayList<Long> weekendListMillis;

    private LinearLayout llayout;
    private int dayToOrder = 0, today;
    private long dayToOrderMillis=0, todayMillis, getOrderMillis;
    private GregorianCalendar cal;
    private int year, month, day, hour;

    private UserModel userDataModel;

    // Имя запроса для возврвта информации из следующей активности
    private static final int MESSAGE_ORDER_REQUEST_CODE = 0;
    private static final int RETURN_COMPANY_DATE_FORM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_making_order);
        setTitle(MAKING_ORDER);//ОФОРМЛЕНИЕ ЗАКАЗА

        tvSuggestions = findViewById(R.id.tvSuggestions);
        tvYourSuggestions = findViewById(R.id.tvYourSuggestions);
        tvProductGeneralInfo = findViewById(R.id.tvProductGeneralInfo);
        tvSumm = findViewById(R.id.tvSumm);
        tvScore = findViewById(R.id.tvScore);
        tvGiveDate = findViewById(R.id.tvGiveDate);
        tvWarehouseInfo=findViewById(R.id.tvWarehouseInfo);
        btnGoBuy = findViewById(R.id.btnGoBuy);

        llSuggestions = findViewById(R.id.llSuggestions);
        //llWarehouseList = findViewById(R.id.llWarehouseList);
        llSuggestions.setBackgroundResource(R.drawable.krugliye_ugli);
        //llWarehouseList.setBackgroundResource(R.drawable.krugliye_ugli);
        btnGoBuy.setBackgroundColor(TUBI_GREEN_600);
        //spCity = findViewById(R.id.spCity);
        //lvWarehouseList = findViewById(R.id.lvWarehouseList);

        tvSuggestions.setText(SUGGESTIONS);
        llSuggestions.setOnClickListener(this);

        tvYourSuggestions.setText(MESSAGE_FROM_ORDER_ACTIVITY);

        //получить из sqlLite данные пользователя и компании
        UserDataRecovery userDataRecovery = new UserDataRecovery();
        userDataModel = userDataRecovery.getUserDataRecovery(this);

       /* adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                cityList);
        spCity.setAdapter(adapter);

        adapWarehouse = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1,adressWarehouseList);
        lvWarehouseList.setAdapter(adapWarehouse);*/

        intent = getIntent();
        count=intent.getIntExtra("count",0);
        veight=intent.getIntExtra("veight",0);
        order_id=intent.getIntExtra("order_id",0);
        deliveryKey=intent.getIntExtra("deliveryKey",0);
        timeReceiveOrder=intent.getStringExtra("timeReceiveOrder");
        try{
            int a = veight/1000;
            int b = veight%1000;
            String stVeight = a+"."+b;
            priceSumm=intent.getDoubleExtra("priceSumm",z);
            String stPriceSumm = String.format("%.2f", priceSumm);
            tvProductGeneralInfo.setText(""+count+" "+POSITION+" "+" "+GENERAL_VEIGHT+" "+stVeight+" "+KILOGRAM);
            tvSumm.setText(""+stPriceSumm+" "+RUB);

            tvScore.setText(""+stPriceSumm+" "+RUB);

        }catch (Exception ex){
            Toast.makeText(this, "Ex: "+ex, Toast.LENGTH_SHORT).show();
        }
        //Toast.makeText(this, "hi\n"+timeReceiveOrder, Toast.LENGTH_SHORT).show();
        tvGiveDate.setText(""+timeReceiveOrder);

        //получить день сегодня
        cal = (GregorianCalendar) GregorianCalendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        hour = cal.get(Calendar.HOUR_OF_DAY);
        todayMillis = cal.getTimeInMillis();

        //выяснить есть доставка или нет
        checkDelliveryKey();


    }


    @Override
    public void onClick(View v) {//написать пожелания textView
        if(v.equals(llSuggestions)) {
            String messege = tvYourSuggestions.getText().toString();
            Intent intent = new Intent(this, MessageOrderActivity.class);
            intent.putExtra("message", messege);
            startActivityForResult(intent, MESSAGE_ORDER_REQUEST_CODE);
        }
    }
    //ЗАКАЗАТЬ
    //создать всем товарам в заказе invoice_info_id
    public void goBuy(View view) {
        if(userDataModel.getRole().equals("GoogleTester")){
            Toast.makeText(this, ""+MES_25, Toast.LENGTH_LONG).show();
            return;
        }
        String url_get = Constant.API;
        url_get += "&" + "chenge_order_active";//api.php?chenge_order_active
        url_get += "&" + "order_id=" +order_id;
        url_get += "&" + "warehouse_id=" +warehouse_id;
        url_get += "&" + "company_tax_id=" +userDataModel.getCompany_tax_id();
        String whatQuestion = "chenge_order_active";
        setInitialData(url_get,whatQuestion);

        //записать пожелания  в t_message_order
        if(!tvYourSuggestions.getText().toString().isEmpty()) {
            url_get = Constant.WRITE_MESSAGE_FROM_ORDER;
            url_get += "&" + "user_uid=" + userDataModel.getUid();//MY_UID;
            url_get += "&" + "order_id=" + order_id;//ORDER_ID;
            url_get += "&" + "message=" + tvYourSuggestions.getText().toString();
            whatQuestion = "write_message_from_order";
            setInitialData(url_get, whatQuestion);
        }
        MESSAGE_FROM_ORDER_ACTIVITY = "";
    }
    //выяснить есть доставка или нет
    private void checkDelliveryKey() {
        if(deliveryKey == 1){
            //если есть то получит адрес доставки
            receiveDeliveryAddress();
            //и данные склада
            receivePartnerWarehouse_id();
        }else{
            //если нет то получить данные склада
            receivePartnerWarehouse_id();
        }
    }
    private void showPlaceReceivingOrder(){
        if(deliveryKey == 1){
            tvWarehouseInfo.setText(""+YOUR_ORDER+" № "+order_id+" \n"
                    +DELIVERY_TO_ADDRESS+" \n"+address_for_delivery);

        }else{
            tvWarehouseInfo.setText(""+YOUR_ORDER+" № "+order_id+" \n"
                    +RECEIVING_FROM_WAREHOUSE+" \n"+warehouseInfo);
        }
    }
    //получить id склада партнера на который отправить товар для выдачи покупателю
    private void receivePartnerWarehouse_id(){
        String url = Constant.USER_OFFICE;
        url += "receive_partner_warehouse";
        url += "&" + "order_id=" +order_id;
        String whatQuestion = "receive_partner_warehouse";
        setInitialData(url,whatQuestion);
    }
    //получить адресс доставки
    private void receiveDeliveryAddress(){
        String url = Constant.API_TEST;
        url += "receive_delivery_address";
        url += "&" + "order_id=" +order_id;
        String whatQuestion = "receive_delivery_address";
        setInitialData(url,whatQuestion);
    }
    private void receiveWeekendListMillis(){
        String url = Constant.API;
        url += "receive_weekend_list";
        url += "&" + "month="+(month+1);
        url += "&" + "year="+year;
        String whatQuestion = "receive_weekend_list";
        setInitialData(url,whatQuestion);
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
                //if(whatQuestion.equals("show_products")){
                asyncDialog.setMessage(LOAD_TEXT);
                asyncDialog.show();
                //  }
                super.onPreExecute();
            }
            protected void onPostExecute(String result) {
                if(whatQuestion.equals("receive_weekend_list")){
                    splitWeekendList(result);
                }else if(whatQuestion.equals("chenge_order_active")){
                    splitResultChengeOrder(result);
                }else if(whatQuestion.equals("receive_partner_warehouse")){
                    splitResultPartnerWarehouse(result);
                }else if(whatQuestion.equals("receive_delivery_address")){
                    splitAddressDelivery(result);
                }
                //скрыть диалоговое окно
                asyncDialog.dismiss();
            }
        };
        task.execute(url_get);
    }
    //разобрать ответ адрес доставки
    private void splitAddressDelivery(String result){
        address_for_delivery = result;

        showPlaceReceivingOrder();
    }
    private void splitResultPartnerWarehouse(String result){
        try {
            String[] res = result.split("<br>");
            String[] temp = res[0].split("&nbsp");
            if (temp[0].equals("error") || temp[0].equals("messege")) {
                Toast.makeText(this, "" + temp[1], Toast.LENGTH_LONG).show();
            } else {
                String warehouse_info_id = temp[0];
                String myWarehouse_id=temp[1];
                String city = temp[2];
                String street = temp[3];
                String house = temp[4];

                String st = "№ "+warehouse_info_id+"/"+myWarehouse_id+" "+city+" "
                                +ST+" "+street+" "+house;
                try {
                    String building = temp[5];
                    st += " "+BUILDING+" "+building;
                }catch (Exception ex){};

                warehouseInfo = st;
                warehouse_id = Integer.parseInt(myWarehouse_id);
                Log.d("A111","MakingOrderActivity / splitResultPartnerWarehouse / warehouse_id ="+warehouse_id);

                showPlaceReceivingOrder();
               // tvWarehouseInfo.setText(warehouseInfo);
            }
        }catch (Exception ex){
        }
    }
    private void splitWeekendList(String result){
        weekendListMillis = new ArrayList<>();
        try {
            String[] res = result.split("<br>");
            String[] temp = res[0].split("&nbsp");
            if (temp[0].equals("error") || temp[0].equals("messege")) {
                Toast.makeText(this, "" + temp[1], Toast.LENGTH_LONG).show();
            } else {
                for (int i = 0; i < res.length; i++) {
                    temp = res[i].split("&nbsp");
                    int weekend_id = Integer.parseInt(temp[0]);
                    long weekendMillis = Long.parseLong(temp[1]);

                    weekendListMillis.add(weekendMillis);
                }
            }
        }catch (Exception ex){        }
        receiveDateIssueOrder();
    }
    private void splitResultChengeOrder(String result){  // разобрать результат
        try {
            String[] res = result.split("<br>");
            String[] temp = res[0].split("&nbsp");
            if (temp[0].equals("RESULT_OK")) {
                DELIVERY_TO_BUYER_STATUS = 0;
                goOrderFinished();
            } else if (temp[0].equals("error") || temp[0].equals("messege")) {
                Toast.makeText(this, "" + temp[1], Toast.LENGTH_LONG).show();
            }/* else {
                Toast.makeText(this, "" + CHECK_CONNECT_INTERNET, Toast.LENGTH_LONG).show();
            }*/
        }catch (Exception ex){       }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // убедитесь, что это возврат с хорошим результатом
        if (requestCode == MESSAGE_ORDER_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // получение строки из Intent
                MESSAGE_FROM_ORDER_ACTIVITY = data.getStringExtra("message");
                tvYourSuggestions.setText(""+MESSAGE_FROM_ORDER_ACTIVITY);
                if(!MESSAGE_FROM_ORDER_ACTIVITY.isEmpty())tvSuggestions.setVisibility(View.GONE);
                else tvSuggestions.setVisibility(View.VISIBLE);
            }
        }
    }

    private void goOrderFinished(){
        //ЗАКАЗ ОФОРМЛЕН
        Intent intent = new Intent(this,OrderFinishedActivity.class);
        intent.putExtra("day",dayToOrder);
        intent.putExtra("order_id",order_id);
        intent.putExtra("deliveryKey", deliveryKey);
        intent.putExtra("warehouseInfo",warehouseInfo);
        intent.putExtra("address_for_delivery",address_for_delivery);
        intent.putExtra("dateGiveOrder",timeReceiveOrder);//dateGiveOrder);
        startActivity(intent);
    }

    //выяснить и показать дату выдачи товара
    private void receiveDateIssueOrder(){
        int giveYear=0, giveMonth=0, giveDay=0, giveHour=0;

        boolean flagIssue = true;
        boolean weekendFlag = false; int a=0;//, step = 0;
        long nextDayMillis = todayMillis +(1 * 24 * 60 * 60 * 1000);
        //проверить следующий день после заказа не является выходным
        while(flagIssue){
            // long nextDayMillis = todayMillis +(1 * 24 * 60 * 60 * 1000);
            cal.setTimeInMillis(nextDayMillis);
            int nextYear = cal.get(Calendar.YEAR);
            int nextMonth = cal.get(Calendar.MONTH);
            int nextDay = cal.get(Calendar.DAY_OF_MONTH);

            for(int i=0;i < weekendListMillis.size();i++){
                cal.setTimeInMillis(weekendListMillis.get(i));
                int weekendYear = cal.get(Calendar.YEAR);
                int weekendMonth = cal.get(Calendar.MONTH);
                int weekendDay = cal.get(Calendar.DAY_OF_MONTH);
                //если является выходным то перейти на один день вперед
                if(nextYear == weekendYear && nextMonth == weekendMonth && nextDay == weekendDay){
                    nextDayMillis += 1 * 24 * 60 * 60 * 1000;
                    weekendFlag=true;
                    i = weekendListMillis.size();
                    // step++;
                }else {
                    weekendFlag = false;
                }
            }
            //если найденный  день не выходной то получаем данные дня для выполнения заказа
            if(weekendFlag == false){
                cal.setTimeInMillis(nextDayMillis);
                giveYear = cal.get(Calendar.YEAR);
                giveMonth = cal.get(Calendar.MONTH);
                giveDay = cal.get(Calendar.DAY_OF_MONTH);
                //giveHour = cal.get(Calendar.HOUR_OF_DAY);
                flagIssue = false;
            }
            if(a++ == 10){
                flagIssue = false;
            }
        }
        //заказ оформлен до 16 часов
        if(hour < 16){
            cal.set(giveYear, giveMonth, giveDay,8, 00,00);
        }//и заказ оформлен после 16 часов
        else{
            cal.set(giveYear, giveMonth, giveDay,12, 00,00);
        }

        SimpleDateFormat  dateFormat =
                new SimpleDateFormat ("dd.MM.yy"+YEAR_CHAR+". "+" HH:mm"+HOUR_CHAR);//"yyyy-MM-dd HH:mm:ss"
        tvGiveDate.setText("test 2 "+dateFormat.format(cal.getTime()));
        dateFormat =
                new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        dateGiveOrder = dateFormat.format(cal.getTime());
        getOrderMillis = cal.getTimeInMillis();
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