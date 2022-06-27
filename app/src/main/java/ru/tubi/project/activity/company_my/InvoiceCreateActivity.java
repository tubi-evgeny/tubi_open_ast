package ru.tubi.project.activity.company_my;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import ru.tubi.project.R;
import ru.tubi.project.activity.MessageOrderActivity;
import ru.tubi.project.activity.OrderFinishedActivity;
import ru.tubi.project.utilites.Constant;
import ru.tubi.project.utilites.InitialData;

import static ru.tubi.project.Config.MY_UID;
import static ru.tubi.project.Config.ORDER_ID;
import static ru.tubi.project.free.AllCollor.TUBI_GREEN_600;
import static ru.tubi.project.free.AllCollor.TUBI_GREY_200;
import static ru.tubi.project.free.AllText.AFTER;
import static ru.tubi.project.free.AllText.BUILDING;
import static ru.tubi.project.free.AllText.C;
import static ru.tubi.project.free.AllText.CHECK_CONNECT_INTERNET;
import static ru.tubi.project.free.AllText.CREATURE;
import static ru.tubi.project.free.AllText.DATE_ABOUT_COMPANY_MISSING;
import static ru.tubi.project.free.AllText.DECORATED;
import static ru.tubi.project.free.AllText.GENERAL_VEIGHT;
import static ru.tubi.project.free.AllText.HOURS;
import static ru.tubi.project.free.AllText.HOUR_CHAR;
import static ru.tubi.project.free.AllText.INVOICE;
import static ru.tubi.project.free.AllText.KILOGRAM;
import static ru.tubi.project.free.AllText.MAKING_ORDER;
import static ru.tubi.project.free.AllText.ON;
import static ru.tubi.project.free.AllText.ORDER;
import static ru.tubi.project.free.AllText.ORDER_APPROVED;
import static ru.tubi.project.free.AllText.ORDER_IS_RECORDER;
import static ru.tubi.project.free.AllText.POSITION;
import static ru.tubi.project.free.AllText.RETURN_BIG;
import static ru.tubi.project.free.AllText.RUB;
import static ru.tubi.project.free.AllText.SELECT_WAREHOUSE;
import static ru.tubi.project.free.AllText.SMOLENSCK;
import static ru.tubi.project.free.AllText.ST;
import static ru.tubi.project.free.AllText.SUGGESTIONS;
import static ru.tubi.project.free.AllText.YEAR_CHAR;
import static ru.tubi.project.free.AllText.YOUR_ORDER;
import static ru.tubi.project.free.VariablesHelpers.MESSAGE_FROM_ORDER_ACTIVITY;

public class InvoiceCreateActivity extends AppCompatActivity
        implements View.OnClickListener{

    private TextView tvSuggestions, tvYourSuggestions, tvProductGeneralInfo,
            tvSumm, tvScore, tvGiveDate, tvWarehouse_info;
    private LinearLayout llSuggestions,llWarehouseList;
    private ArrayList<String> adressWarehouseList = new ArrayList<>();
    private Intent intent, takeit;
    private String city, dateGiveOrder, warehouse_info="";
    private int count, veight, x;//, buttonColor = 0
    private double priceSumm, z;
    private AlertDialog ad;
    private AlertDialog.Builder adb;
    private Button btnGoBuy;
    ArrayList<Long> weekendListMillis;

    private long todayMillis, getOrderMillis;
    private GregorianCalendar cal;
    private int invoice_order_id, partner_varehouse_id, year, month, day, hour;

    // Имя запроса для возврвта информации из следующей активности
    private static final int INVOICE_CREATE_REQUEST_CODE = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_create);
        setTitle(CREATURE);//Оформление
        getSupportActionBar().setSubtitle(INVOICE);//накладной

        tvSuggestions = findViewById(R.id.tvSuggestions);
        tvYourSuggestions = findViewById(R.id.tvYourSuggestions);
        tvProductGeneralInfo = findViewById(R.id.tvProductGeneralInfo);
        tvSumm = findViewById(R.id.tvSumm);
        tvScore = findViewById(R.id.tvScore);
        tvGiveDate = findViewById(R.id.tvGiveDate);
        tvWarehouse_info = findViewById(R.id.tvWarehouse_info);
        btnGoBuy = findViewById(R.id.btnGoBuy);

        llSuggestions = findViewById(R.id.llSuggestions);
        llWarehouseList = findViewById(R.id.llWarehouseList);
        llSuggestions.setBackgroundResource(R.drawable.krugliye_ugli);
        llWarehouseList.setBackgroundResource(R.drawable.krugliye_ugli);

        tvSuggestions.setText(SUGGESTIONS);
        llSuggestions.setOnClickListener(this);
        btnGoBuy.setOnClickListener(this);

        tvYourSuggestions.setText(MESSAGE_FROM_ORDER_ACTIVITY);

        takeit = getIntent();
        invoice_order_id=takeit.getIntExtra("invoice_order_id",0);
        Toast.makeText(this, "order: "+invoice_order_id, Toast.LENGTH_SHORT).show();

        intent = getIntent();
        count=intent.getIntExtra("count",x);
        veight=intent.getIntExtra("veight",x);
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

        //получить день сегодня
        cal = (GregorianCalendar) GregorianCalendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);
        hour = cal.get(Calendar.HOUR_OF_DAY);
        todayMillis = cal.getTimeInMillis();

        receiveWeekendListMillis();
        receiveWarehouseInfo();
    }

    @Override
    public void onClick(View v) {
        //написать пожелания textView
        if(v.equals(llSuggestions)) {
            String messege = tvYourSuggestions.getText().toString();
            Intent intent = new Intent(this, MessageOrderActivity.class);
            intent.putExtra("message", messege);
            startActivityForResult(intent, INVOICE_CREATE_REQUEST_CODE);
        }
        else if(v.equals(btnGoBuy)){
           writeOrderToActive();
        }
    }
    private void goReturnActivity(){
        Intent intent = new Intent(this,CollectProductForActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void writeOrderToActive(){
        String url_get = Constant.API;
        url_get += "chenge_order_active_execute";
        url_get += "&" + "order_id=" +invoice_order_id;//ORDER_ID;
        url_get += "&" + "warehouse_id=" +partner_varehouse_id;//warehouse_id;
        url_get += "&" + "getOrderMillis="+getOrderMillis;
        String whatQuestion = "chenge_order_active_execute";
        setInitialData(url_get,whatQuestion);

        //записать пожелания  в t_message_order
        if(!tvYourSuggestions.getText().toString().isEmpty()) {
            url_get = Constant.WRITE_MESSAGE_FROM_ORDER;
            url_get += "&" + "user_uid=" + MY_UID;
            url_get += "&" + "order_id=" + invoice_order_id;//ORDER_ID;
            url_get += "&" + "message=" + tvYourSuggestions.getText().toString();
            whatQuestion = "write_message_from_order";
            setInitialData(url_get, whatQuestion);
        }
        MESSAGE_FROM_ORDER_ACTIVITY = "";
    }
    //получить данные склад
    private void receiveWarehouseInfo(){
        String url = Constant.API;
        url += "receive_warehouse_info";
        url += "&" + "order_id="+invoice_order_id;
        String whatQuestion = "receive_warehouse_info";
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

        InitialData task=new InitialData(){
            protected void onPostExecute(String result) {
                if(whatQuestion.equals("receive_weekend_list")){
                    splitWeekendList(result);
                }else if(whatQuestion.equals("chenge_order_active_execute")){
                    splitResultChengeOrder(result);
                }
                /*else if(whatQuestion.equals("receive_partner_warehouse_list")){
                    splitResultPartnerWarehouseList(result);
                }*/

                else if(whatQuestion.equals("receive_warehouse_info")){
                    splitResultWarehouseInfo(result);
                }//
            }
        };
        task.execute(url_get);
    }
    private void splitResultWarehouseInfo(String result){
        try {
            String[] res = result.split("<br>");
            String[] temp = res[0].split("&nbsp");
            if (temp[0].equals("error") || temp[0].equals("messege")) {
                Toast.makeText(this, "" + temp[1], Toast.LENGTH_LONG).show();
            } else {
               // for (int i = 0; i < res.length; i++) {
                temp = res[0].split("&nbsp");
                int warehouse_info_id = Integer.parseInt(temp[0]);
                int warehouse_id=Integer.parseInt(temp[1]);
                String city = temp[2];
                String street = temp[3];
                String house = temp[4];
                String building="";

                warehouse_info = "№ "+warehouse_info_id+"/"+warehouse_id+" "+
                        city+" "+ST+" "+street+" "+house;
                try {
                    building = temp[5];
                    warehouse_info += " "+C+". "+building;
                }catch (Exception ex){};
                partner_varehouse_id=warehouse_id;
            }
           // }
        }catch (Exception ex){
        }
        tvWarehouse_info.setText(""+warehouse_info);
    }
   /* private void splitResultPartnerWarehouseList(String result){
        adressWarehouseList.clear();
        try {
            String[] res = result.split("<br>");
            String[] temp = res[0].split("&nbsp");
            if (temp[0].equals("error") || temp[0].equals("messege")) {
                Toast.makeText(this, "" + temp[1], Toast.LENGTH_LONG).show();
            } else {
                for (int i = 0; i < res.length; i++) {
                    temp = res[i].split("&nbsp");
                    String warehouse_info_id = temp[0], street = temp[1], house = temp[2],
                            warehouse_id=temp[4];
                    String st = "№ "+warehouse_info_id+"/"+warehouse_id+" "+ST+" "+street+" "+house;
                    try {
                        String building = temp[3];
                        st += " "+BUILDING+" "+building;
                    }catch (Exception ex){};

                    adressWarehouseList.add(st);
                }
            }
        }catch (Exception ex){
        }
    }*/
    private void splitResultChengeOrder(String result){  // разобрать результат
        try {
            String[] res = result.split("<br>");
            String[] temp = res[0].split("&nbsp");
            if (temp[0].equals("RESULT_OK")) {
                //ЗАКАЗ ОФОРМЛЕН
                allertDialog();
              //  goOrderFinished();
            } else if (temp[0].equals("error") || temp[0].equals("messege")) {
                Toast.makeText(this, "" + temp[1], Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "" + CHECK_CONNECT_INTERNET, Toast.LENGTH_LONG).show();
            }
        }catch (Exception ex){       }
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

        SimpleDateFormat dateFormat =
                new SimpleDateFormat ("dd.MM.yy"+YEAR_CHAR+". "+" HH:mm"+HOUR_CHAR);//"yyyy-MM-dd HH:mm:ss"
        tvGiveDate.setText(""+dateFormat.format(cal.getTime()));//giveDay+"."+(giveMonth+1)+"."+giveYear+" "+giveHour+""
        dateFormat =
                new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        dateGiveOrder = dateFormat.format(cal.getTime());
        getOrderMillis = cal.getTimeInMillis();
    }

    private void allertDialog() {
        adb = new AlertDialog.Builder(this);
        String st1 = ORDER_IS_RECORDER;
        String st2 = ORDER+" №"+invoice_order_id+" "+DECORATED+"\n"
                +ON+" "+dateGiveOrder;
        adb.setTitle(st1);
        adb.setMessage(st2);

        adb.setNeutralButton(RETURN_BIG, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                goReturnActivity();
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // убедитесь, что это возврат с хорошим результатом
        if (requestCode == INVOICE_CREATE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // получение строки из Intent
                MESSAGE_FROM_ORDER_ACTIVITY = data.getStringExtra("message");
                tvYourSuggestions.setText(""+MESSAGE_FROM_ORDER_ACTIVITY);
                if(!MESSAGE_FROM_ORDER_ACTIVITY.isEmpty())tvSuggestions.setVisibility(View.GONE);
                else tvSuggestions.setVisibility(View.VISIBLE);
            }
        }
    }
}