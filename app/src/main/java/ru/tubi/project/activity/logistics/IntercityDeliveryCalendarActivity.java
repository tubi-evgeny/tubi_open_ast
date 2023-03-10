package ru.tubi.project.activity.logistics;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import ru.tubi.project.R;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.utilites.Constant;
import ru.tubi.project.utilites.InitialData;
import ru.tubi.project.utilites.UserDataRecovery;

import static ru.tubi.project.free.AllCollor.RED_600;
import static ru.tubi.project.free.AllCollor.TUBI_BLACK;
import static ru.tubi.project.free.AllCollor.TUBI_GREEN_600;
import static ru.tubi.project.free.AllCollor.TUBI_GREY_200;
import static ru.tubi.project.free.AllCollor.TUBI_GREY_400;
import static ru.tubi.project.free.AllCollor.TUBI_GREY_600;
import static ru.tubi.project.free.AllText.CALENDAR_TEXT;
import static ru.tubi.project.free.AllText.DELIVERY_CANNOD_BE_DELETED;
import static ru.tubi.project.free.AllText.INTERCITY_DELIVERY_TEXT;
import static ru.tubi.project.free.AllText.MONTH_NAMES_LIST;
import static ru.tubi.project.free.AllText.PASS_CANNOD_BE_CHENGED;
import static ru.tubi.project.free.AllText.TO_EXIT_PRESS_TWICE;
import static ru.tubi.project.free.AllText.WEEK_NAMES_CHAR_LIST;
import static ru.tubi.project.free.AllText.YEAR_CHAR;
import static ru.tubi.project.free.AllText.YOUR_DID_NOT_SAVE;

public class IntercityDeliveryCalendarActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout llCalendar;
    private TextView tvMonthYear, tvCalendarInfo, tvSave;
    private ImageView ivBack, ivForward;
    private ArrayList<Long> intercityDeliveryList = new ArrayList<>();
    private ArrayList<Long> startIntercityDeliveryList = new ArrayList<>();
    private ArrayList<Long> writeIntercityDeliveryList = new ArrayList<>();
    private ArrayList<Long> deleteIntercityDeliveryList = new ArrayList<>();
    private long dayToOrderMillis=0, todayMillis;
    private boolean flagCiti=false, flagDay=false, onBackFlag = true;
    private int dayToOrder = 0, chengeColor = 0, outCity_id, inCity_id, today;
    private String outCity, inCity;
    GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance();
    int day = calendar.get(Calendar.DAY_OF_MONTH);
    int month = calendar.get(Calendar.MONTH);
    int year = calendar.get(Calendar.YEAR);
    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
    int endDay = calendar.getActualMaximum( Calendar.DATE );

    private static final int TIME_INTERVAL = 2000;
    private long backPressedMillis;

    //?????????????? ????????
    int selectYear;
    int selectMonth;
    int selectDay;
    private UserModel userDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intercity_delivery_calendar);
        setTitle(CALENDAR_TEXT);
        getSupportActionBar().setSubtitle(INTERCITY_DELIVERY_TEXT);

        llCalendar=findViewById(R.id.llCalendar);
        tvMonthYear=findViewById(R.id.tvMonthYear);
        tvCalendarInfo=findViewById(R.id.tvCalendarInfo);
        tvSave=findViewById(R.id.tvSave);
        ivBack=findViewById(R.id.ivBack);
        ivForward=findViewById(R.id.ivForward);

        tvSave.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        ivForward.setOnClickListener(this);

        tvSave.setClickable(false);

        //???????????????? ???? sqlLite ???????????? ???????????????????????? ?? ????????????????
        UserDataRecovery userDataRecovery = new UserDataRecovery();
        userDataModel = userDataRecovery.getUserDataRecovery(this);

        outCity = "????????????";
        inCity = "????????????????";
        tvCalendarInfo.setText(""+outCity+" - "+inCity);

        if(outCity.equals("????????????"))outCity_id = 1;
        if(inCity.equals("????????????????"))inCity_id = 2;

        //???????????? ??????????
        selectYear = year;
        selectMonth = month;
        selectDay = 1;

        //receiveWeekendList();
        receiveIntercityDeliveryList();
    }

    @Override
    public void onClick(View v) {
        if(v.equals(tvSave)){
            checkAndWriteWeekend();
        }else if(v.equals(ivBack)){
            makeAndShowNewCalndar(-1);
            //Toast.makeText(this, "ivBack", Toast.LENGTH_SHORT).show();
        }else if(v.equals(ivForward)){
            makeAndShowNewCalndar(1);
            // Toast.makeText(this, "ivForward", Toast.LENGTH_SHORT).show();
        }
    }
    private void makeAndShowNewCalndar(int count){
        //???????? ???????? ???????????????? ??????????
        if(count > 0){
            // ?????????? ?????????????? ?????????????? ???? ??????????????
            if(selectMonth != 11){
                selectMonth +=1;
            }else{
                selectMonth = 0;
                selectYear +=1;
            }
        }//???????? ???????? ???????????? ??????????
        else {
            //?? ?????????? ?????????????? ?????????????? ???? ????????????
            if(selectMonth !=0){
                selectMonth -=1;
            }else{
                selectMonth = 11;
                selectYear -=1;
            }
        }
        //receiveWeekendList();
        receiveIntercityDeliveryList();
    }
    private void makeCalendarTable( ){
        llCalendar.removeAllViews();

        TableLayout table = new TableLayout(this);

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT );
        table.setLayoutParams(lp);
        table.setStretchAllColumns(true);

        TableLayout.LayoutParams rowLp = new TableLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                1.0f);
        rowLp.setMargins(50,0,50,0);
        TableRow.LayoutParams cellLp = new TableRow.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                1.0f);
        cellLp.setMargins(2,2,2,2);

        TableLayout.LayoutParams tvPar = new TableLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        tvPar.setMargins(0,30,0,30);

        calendar.set(selectYear, selectMonth, selectDay,00, 00,00);
        //?????????????? ?????????? ?? ?????? ?????????????? ????????????
        tvMonthYear.setText(MONTH_NAMES_LIST[calendar.get(Calendar.MONTH)]+" "+calendar.get(Calendar.YEAR)+YEAR_CHAR+".");

        //???????????????? ?????????? ?????????????? ?????? ???????????? ???????????? ?????????? ???????????? ?????? ???????????? ?? ???????????? ???????????? ??????????????????
        Date curentDate = calendar.getTime();
        calendar.setTime(curentDate);
        calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());

        long startDayMillis = calendar.getTimeInMillis();
        long nextDayMillis = startDayMillis;
        calendar.set(selectYear, selectMonth, selectDay,00, 00,00);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        long finishDayMillis = calendar.getTimeInMillis();
        // startWeekendList.add(finishDayMillis -(2 * 24 * 60 * 60 * 1000));

        boolean maxSizeMonth = false, weekCharFlag = true;;
        int count =1;
        while (maxSizeMonth == false){
            TableRow row = new TableRow(this);
            //?????????? ???????????? ?????????????? ????????????
            if(weekCharFlag){
                TableRow rowChar = new TableRow(this);
                for(int i=0;i < 7;i++){
                    LinearLayout llChar = new LinearLayout(this);
                    TextView textChar = new TextView(this);
                    llChar.setGravity(Gravity.CENTER);

                    if(i < 5){
                        textChar.setTextColor(TUBI_GREY_600);
                    }else textChar.setTextColor(RED_600);
                    textChar.setText(""+WEEK_NAMES_CHAR_LIST[i]);
                    textChar.setTextSize(14);
                    textChar.setGravity(Gravity.CENTER);
                    llChar.addView(textChar,tvPar);

                    rowChar.addView(llChar, cellLp);
                }
                table.addView(rowChar, rowLp);
                weekCharFlag =false;
            }
            //?????????? ?????????????? ??????
            for (int y = 1; y <= 7; ++y){
                LinearLayout ll = new LinearLayout(this);
                TextView text = new TextView(this);
                TextView textGrey = new TextView(this);
                ll.setGravity(Gravity.CENTER);
                text.setOnClickListener(this);


                calendar.setTimeInMillis(nextDayMillis);
                int writeDay = calendar.get(Calendar.DAY_OF_MONTH);
                int writeMonth = calendar.get(Calendar.MONTH);
                if(selectMonth != writeMonth){
                    textGrey.setTextColor(TUBI_GREY_400);
                    textGrey.setText(""+writeDay);//
                    textGrey.setTextSize(16);
                    textGrey.setGravity(Gravity.CENTER);
                    ll.addView(textGrey,tvPar);
                }else{
                    for(int i=0;i < intercityDeliveryList.size();i++){
                        calendar.setTimeInMillis(intercityDeliveryList.get(i));
                        int myDay = calendar.get(Calendar.DAY_OF_MONTH);
                        //Log.d("A111","myDay = "+myDay);
                        if(myDay == writeDay){
                            text.setTextColor(RED_600);
                            i = intercityDeliveryList.size();
                        }else{
                            text.setTextColor(TUBI_BLACK);
                        }
                    }
                    text.setText(""+writeDay);//
                    text.setTextSize(16);
                    text.setGravity(Gravity.CENTER);
                    ll.addView(text,tvPar);
                }
                if(day == writeDay && month == writeMonth){
                    ll.setBackgroundResource(R.drawable.round_backgraund_gray_200);
                }
                if(nextDayMillis > finishDayMillis){
                    maxSizeMonth = true;
                    count++;
                }

                row.addView(ll, cellLp);
                nextDayMillis +=  1 * 24 * 60 * 60 * 1000;

                text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        calendar.set(selectYear, selectMonth, Integer.parseInt(text.getText().toString()),00, 00,00);
                        //?????????????????? ?????????????? ???????????????? ???? ?????????????????? ???????? ???? ???????????????? ???????????????? ?????????? 6 ????????
                        if(System.currentTimeMillis()+(6*24*60*60*1000) >  calendar.getTimeInMillis()
                                && System.currentTimeMillis() <  calendar.getTimeInMillis()){
                            //Toast.makeText(IntercityDeliveryCalendarActivity.this, "long", Toast.LENGTH_SHORT).show();
                            for(int i=0;i < startIntercityDeliveryList.size();i++){//intercityDeliveryList
                                calendar.setTimeInMillis(startIntercityDeliveryList.get(i));//intercityDeliveryList
                                int myDay = calendar.get(Calendar.DAY_OF_MONTH);
                                int selectDay = Integer.parseInt(text.getText().toString());
                                if(selectDay == myDay){
                                    Toast.makeText(IntercityDeliveryCalendarActivity.this,
                                            ""+DELIVERY_CANNOD_BE_DELETED, Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        }
                        calendar.set(selectYear, selectMonth, Integer.parseInt(text.getText().toString()),00, 00,00);
                        //?????????????????? ???????????????? ???????? ?????????????? ?????? ?? ??????????????
                        if(System.currentTimeMillis() > calendar.getTimeInMillis()){
                            Toast.makeText(IntercityDeliveryCalendarActivity.this, ""+PASS_CANNOD_BE_CHENGED, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        boolean writeMillis = true;
                        calendar.set(selectYear, selectMonth, Integer.parseInt(text.getText().toString()),00, 00,00);
                        int d = calendar.get(Calendar.DAY_OF_MONTH);
                        for(int i=0;i < intercityDeliveryList.size();i++){
                            calendar.setTimeInMillis(intercityDeliveryList.get(i));
                            int d2 = calendar.get(Calendar.DAY_OF_MONTH);
                            if(d == d2){
                                intercityDeliveryList.remove(i);
                                writeMillis = false;
                                i = intercityDeliveryList.size();
                            }
                        }
                        if(writeMillis){
                            calendar.set(selectYear, selectMonth, Integer.parseInt(text.getText().toString()),00, 00,00);
                            intercityDeliveryList.add(calendar.getTimeInMillis());
                           // Log.d("A111","year-"+selectYear+" month-"+selectMonth+" day-"+Integer.parseInt(text.getText().toString()));
                            //Log.d("A111","year-"+calendar.get(Calendar.YEAR)+" month-"+calendar.get(Calendar.MONTH)+" day-"+calendar.get(Calendar.DAY_OF_MONTH)+" millis-"+calendar.getTimeInMillis());
                           // Log.d("A111","calendar = "+calendar.toString());

                        }
                        makeCalendarTable();
                        tvSave.setClickable(true);
                        tvSave.setBackgroundColor(TUBI_GREEN_600);
                        onBackFlag = false;
                    }
                });
            }
            table.addView(row, rowLp);
        }
        llCalendar.addView(table);
    }
    private void checkAndWriteWeekend(){
        writeIntercityDeliveryList.clear();
        deleteIntercityDeliveryList.clear();
        // Toast.makeText(this, "tvSave", Toast.LENGTH_SHORT).show();
        //???????? ?? t_weekend ???????????????? ???? ???????? ???????????????? ???? ?????????? ?????? ?? ??????????????
        if(startIntercityDeliveryList.size() == 0){
            for(int i=0;i < intercityDeliveryList.size();i++){
                writeIntercityDeliveryToTable(String.valueOf(intercityDeliveryList.get(i)));
                // Toast.makeText(this, "test1: "+String.valueOf(weekendList.get(i)), Toast.LENGTH_SHORT).show();
            }
        }else {//???????? ???????????????? ???????? ?? ???????????????????? ??????
            for (int i = 0; i < intercityDeliveryList.size(); i++) {
                boolean writeFlag =true;
                for (int j = 0; j < startIntercityDeliveryList.size(); j++) {
                    if (intercityDeliveryList.get(i) == startIntercityDeliveryList.get(j)) {
                        writeFlag = false;
                    }
                }
                if(writeFlag){
                    writeIntercityDeliveryList.add(intercityDeliveryList.get(i));
                }
            }//?????? ???????? ?????????????? ???????????????????????????? ????
            for(int i=0;i < startIntercityDeliveryList.size();i++){
                boolean deleteFlag = true;
                for(int j=0;j < intercityDeliveryList.size();j++){
                    if(startIntercityDeliveryList.get(i) == intercityDeliveryList.get(j)){
                        deleteFlag =false;
                    }
                }
                if(deleteFlag){
                    deleteIntercityDeliveryList.add(startIntercityDeliveryList.get(i));
                }
            }
        }
        for(int i=0;i < writeIntercityDeliveryList.size();i++){
            writeIntercityDeliveryToTable(String.valueOf(writeIntercityDeliveryList.get(i)));
            //Toast.makeText(this, "write: "+String.valueOf(writeWeekendList.get(i)), Toast.LENGTH_SHORT).show();
        }
        for(int i=0;i < deleteIntercityDeliveryList.size();i++){
            //deleteWeekendFromTable(String.valueOf(deleteIntercityDeliveryList.get(i)));
            deleteIntercityDeliveryFromTable(String.valueOf(deleteIntercityDeliveryList.get(i)));
            // Toast.makeText(this, "delete: "+String.valueOf(deleteWeekendList.get(i)), Toast.LENGTH_SHORT).show();
        }
        tvSave.setBackgroundColor(TUBI_GREY_200);
        tvSave.setClickable(false);
        onBackFlag=true;
    }
    //???????????????? ???????? ???????????????? ?? ????????????
    private void receiveIntercityDeliveryList(){
        String url = Constant.API;
        url += "receive_delivery_list";
        url += "&" + "outCity_id="+outCity_id;
        url += "&" + "inCity_id="+inCity_id;
        url += "&" + "month="+(selectMonth+1);
        url += "&" + "year="+selectYear;
        String whatQuestion = "receive_delivery_list";
        setInitialData(url,whatQuestion);
    }
    //???????????? ???????? ???????????????? ?? ???????????? ?? ??????????????
    private void writeIntercityDeliveryToTable(String dateMillis){
        String url = Constant.API;
        url += "write_intercity_delivery_to_table";
        url += "&" + "outCity_id="+outCity_id;
        url += "&" + "inCity_id="+inCity_id;
        url += "&" + "dateMillis="+dateMillis;
        url += "&" + "user_uid="+userDataModel.getUid();//MY_UID;
        url += "&" + "taxpayer_id="+userDataModel.getCompany_tax_id();//MY_COMPANY_TAXPAYER_ID;
        String whatQuestion = "write_intercity_delivery_to_table";
        setInitialData(url,whatQuestion);
    }
    //?????????????? ???????? ???????????????? ?? ???????????? ???? ??????????????
    private void deleteIntercityDeliveryFromTable(String dateMillis){
        String url = Constant.API;
        url += "delete_intercity_delivery_from_table";
        url += "&" + "dateMillis="+dateMillis;
        url += "&" + "user_uid="+userDataModel.getUid();//MY_UID;
        url += "&" + "taxpayer_id="+userDataModel.getCompany_tax_id();//MY_COMPANY_TAXPAYER_ID;
        String whatQuestion = "delete_intercity_delivery_from_table";
        setInitialData(url,whatQuestion);
    }
    //?????????????? ???????????????? ???? ??????????????
    private void deleteWeekendFromTable(String dateMillis){
        String url = Constant.API;
        url += "delete_weekend_from_table";
        url += "&" + "dateMillis="+dateMillis;
        url += "&" + "user_uid="+userDataModel.getUid();//MY_UID;
        url += "&" + "taxpayer_id="+userDataModel.getCompany_tax_id();//MY_COMPANY_TAXPAYER_ID;
        String whatQuestion = "delete_weekend_from_table";
        //setInitialData(url,whatQuestion);
    }

    private void setInitialData(String url_get, String whatQuestion) {

        InitialData task=new InitialData(){
            protected void onPostExecute(String result) {
              /*  if(whatQuestion.equals("receive_weekend_list")){
                    splitWeekendList(result);
                }else if(whatQuestion.equals("write_weekend_to_table")){
                    // splitWeekendList(result);
                }*/
                if(whatQuestion.equals("receive_delivery_list")){
                    splitWeekendList(result);
                }
            }
        };
        task.execute(url_get);
    }
    private void splitWeekendList(String result){
        // Toast.makeText(this, "res: "+result, Toast.LENGTH_SHORT).show();
        intercityDeliveryList.clear();
        startIntercityDeliveryList.clear();
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

                    startIntercityDeliveryList.add(weekendMillis);
                }
            }
        }catch (Exception ex){
            // Toast.makeText(this, "Exception: "+ex, Toast.LENGTH_SHORT).show();
        }
        intercityDeliveryList.addAll(startIntercityDeliveryList);
        makeCalendarTable();
    }

    @Override
    public void onBackPressed() {
        if(onBackFlag){
            super.onBackPressed();
        }else{
            if (backPressedMillis + TIME_INTERVAL > System.currentTimeMillis()) {
                super.onBackPressed();
            }
            else {
                Toast.makeText(this, ""+YOUR_DID_NOT_SAVE+"\n"+TO_EXIT_PRESS_TWICE, Toast.LENGTH_LONG).show();
            }
            backPressedMillis = System.currentTimeMillis();
        }
    }
}