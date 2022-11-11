package ru.tubi.project.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import ru.tubi.project.R;
import ru.tubi.project.activity.company_my.ShowMyOrderActivity;
import ru.tubi.project.adapters.OrderHistoryAdapter;
import ru.tubi.project.models.OrderHistoryFinishModel;
import ru.tubi.project.models.OrderHistoryModel;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.utilites.InitialData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import ru.tubi.project.utilites.Constant;
import ru.tubi.project.utilites.UserDataRecovery;

import static ru.tubi.project.free.AllText.FOR_YOU_DONT_ORDER;
import static ru.tubi.project.free.AllText.LOAD_TEXT;
import static ru.tubi.project.free.AllText.ORDER_HISTORY_BIG;

public class OrderHistoryActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private TextView tvNewOrder;
    private OrderHistoryAdapter adapter;
    private ArrayList<String> list = new ArrayList<>();
    private ArrayList<OrderHistoryModel> listOrders=new ArrayList<>();
    private ArrayList<OrderHistoryFinishModel> listFinishOrderHistory = new ArrayList<>();
    private String[] strResult;
    private String url;
    private UserModel userDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        setTitle(ORDER_HISTORY_BIG);//ИСТОРИЯ ЗАКАЗОВ

        tvNewOrder=findViewById(R.id.tvNewOrder);
        tvNewOrder.setOnClickListener(this);

        //получить из sqlLite данные пользователя и компании
        UserDataRecovery userDataRecovery = new UserDataRecovery();
        userDataModel = userDataRecovery.getUserDataRecovery(this);


        //получить историю заказов от DB
        receiveOrderHistoryFromDB();

        recyclerView=(RecyclerView)findViewById(R.id.rvList);

        OrderHistoryAdapter.RecyclerViewClickListener clickListener=
                new OrderHistoryAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                whatClicked(position);
               // Toast.makeText(OrderHistoryActivity.this,
              //          "Position: "+position, Toast.LENGTH_SHORT).show();
            }
        };

        adapter=new OrderHistoryAdapter(this, listFinishOrderHistory,clickListener);

        recyclerView.setAdapter(adapter);

    }
    private void whatClicked(int position){
        Intent intent = new Intent(this, ShowMyOrderActivity.class);
        intent.putExtra("order_id",listFinishOrderHistory.get(position).getOrder_id());
        intent.putExtra("executed",listFinishOrderHistory.get(position).getExecuted());
        intent.putExtra("delivery",listFinishOrderHistory.get(position).getDelivery());
        intent.putExtra("joint_buy",listFinishOrderHistory.get(position).getJoint_buy());
        intent.putExtra("order_deleted",listFinishOrderHistory.get(position).getOrder_deleted());
        startActivity(intent);
    }
    @Override
    public void onClick(View v) {
        if (v.equals(tvNewOrder)) {
            Intent intent = new Intent(this, ActivityCatalog.class);
            startActivity(intent);
        }
    }
    //получить историю заказов от DB
    private void receiveOrderHistoryFromDB() {
        String limit = "20";
        url = Constant.RECEIVE_MY_ORDER_HISTORY;
        url += "&" + "user_uid="+ userDataModel.getUid();//MY_UID;
        url += "&" + "company_tax_id="+ userDataModel.getCompany_tax_id();
        url += "&" + "limit="+limit;
        String whatQuestion = "receive_my_order_history";
        setInitialData(url, whatQuestion);
        //receive_my_order_history.php?receive&user_uid=60f59717234e4&limit=20
    }

    private void setInitialData(String url_get, String whatQuestion) {
        ProgressDialog asyncDialog = new ProgressDialog(this);
        InitialData task=new InitialData(){
            @Override
            protected void onPreExecute() {
                asyncDialog.setMessage(LOAD_TEXT);
                asyncDialog.show();
                super.onPreExecute();
            }
            @RequiresApi(api = Build.VERSION_CODES.N)
            protected void onPostExecute(String result) {
                //Do your thing
                if(whatQuestion.equals("receive_my_order_history")){
                    splitOrderHistoryResult(result);

                    //скрыть диалоговое окно
                    asyncDialog.dismiss();
                }
            }
        };
        task.execute(url_get);
    }
    // разобрать результат с сервера список продуктов и колличество
    private void splitOrderHistoryResult(String result){
        Log.d("A111",getClass()+" / splitOrderHistoryResult / res="+result);
        listOrders.clear();
        String [] res=result.split("<br>");
        //Toast.makeText(this, ""+res[0], Toast.LENGTH_SHORT).show();
        String[]one_temp = res[0].split("&nbsp");
        if(one_temp[0].equals("NO_ORDER")){
            Toast.makeText(this, ""+FOR_YOU_DONT_ORDER, Toast.LENGTH_LONG).show();
            return;
        }else if(one_temp[0].equals("error") || one_temp[0].equals("message")){
            Toast.makeText(this, ""+one_temp[1], Toast.LENGTH_LONG).show();
            return;
        }else{
            for(int i=0;i<res.length;i++){
                String[]temp = res[i].split("&nbsp");
                int order_id=Integer.parseInt(temp[0]);
                String category=temp[1];
                String brand=temp[2];
                String characteristic=temp[3];
                int weight_volume=Integer.parseInt(temp[4]);
                double price=Double.parseDouble(temp[5]);
                double quantity=Double.parseDouble(temp[6]);
                int executed=Integer.parseInt(temp[7]);
                String date=temp[8];
                String get_date=temp[9];
                int order_deleted=Integer.parseInt(temp[10]);
                double price_process=Double.parseDouble(temp[11]);
                int delivery=Integer.parseInt(temp[12]);
                int joint_buy=Integer.parseInt(temp[13]);

                GregorianCalendar calendar = (GregorianCalendar) GregorianCalendar.getInstance();
                calendar.setTimeInMillis(Long.parseLong(get_date));
                get_date = calendar.get(Calendar.DAY_OF_MONTH)+"."+(calendar.get(Calendar.MONTH)+1)+"."+calendar.get(Calendar.YEAR);

                OrderHistoryModel orderHistory = new OrderHistoryModel(order_id,category,
                        brand, characteristic, weight_volume, price, quantity,
                        executed, date, get_date, order_deleted, price_process
                        , delivery, joint_buy);

                listOrders.add(orderHistory);
            }

        }
        makeOrderHistoryList();
    }
    private void makeOrderHistoryList(){
        listFinishOrderHistory.clear();
        int order_id;
         int positionCount = 0;
         String descriptionFirst = null;
         String descriptionSecond = "....";
         String date = null;
        String get_date = null;
         int executed = 0;
         double summ = 0;
         int listSize = listOrders.size();
        int order_deleted;
        int delivery;
        int joint_buy;

         if(listOrders.size() > 0) {
             order_id = listOrders.get(0).getOrder_id();
             order_deleted=listOrders.get(0).getOrder_deleted();
             delivery = listOrders.get(0).getDelivery();
             joint_buy = listOrders.get(0).getJoint_buy();

             for(int i=0;i<listOrders.size();i++){
                 int a = i+1;
                 //номер заказов совпадает начинаем формировать отчет
                 if(order_id == listOrders.get(i).getOrder_id()){
                        //заказ, 1 позиция собираем данные
                     if(positionCount == 0){
                        // order_id = listOrders.get(i).getOrder_id();
                         positionCount ++;
                         descriptionFirst = listOrders.get(i).getCategory();
                         descriptionFirst += " " +listOrders.get(i).getBrand();
                         descriptionFirst += " " +listOrders.get(i).getCharacteristic();
                         date = listOrders.get(i).getDate();
                         get_date = listOrders.get(i).getGet_date();
                         executed = listOrders.get(i).getExecuted();
                         summ += (listOrders.get(i).getPrice() + listOrders.get(i).getPrice_process() )
                                 * listOrders.get(i).getQuantity();

                    //заказ, 2 позиция собираем данные
                     }else if( positionCount == 1){
                         positionCount++;
                         descriptionSecond = "";
                         descriptionSecond = listOrders.get(i).getCategory();
                         descriptionSecond += " " +listOrders.get(i).getBrand();
                         descriptionSecond += " ....";
                         summ += (listOrders.get(i).getPrice() + listOrders.get(i).getPrice_process() )
                                 * listOrders.get(i).getQuantity();
                         //заказ остальные позиции собираем данные
                     }else {
                         positionCount++;
                         summ += (listOrders.get(i).getPrice() + listOrders.get(i).getPrice_process() )
                                 * listOrders.get(i).getQuantity();
                     }
                     if(a < listSize){
                         // если следующий номер заказа отличается то записываем в лист собранные данные
                         // и меняем номер заказа на следующий в случае если он есть
                         if(order_id != listOrders.get(a).getOrder_id()){
                                //записываем в лист собранные данные
                             OrderHistoryFinishModel finishOrder = new OrderHistoryFinishModel(
                                     order_id,positionCount,descriptionFirst,descriptionSecond,
                                     date,get_date,executed,summ, order_deleted, delivery, joint_buy);
                             listFinishOrderHistory.add(finishOrder);

                             order_id = listOrders.get(a).getOrder_id();
                             order_deleted=listOrders.get(a).getOrder_deleted();
                             delivery=listOrders.get(a).getDelivery();
                             joint_buy=listOrders.get(a).getJoint_buy();
                             positionCount = 0;
                             descriptionFirst = null;
                             descriptionSecond = "....";
                             date = null;
                             executed = 0;
                             summ = 0;
                         }
                     }
                 }
             }
             //записываем последние собранные данные в лист
             OrderHistoryFinishModel finishOrder = new OrderHistoryFinishModel(
                     order_id,positionCount,descriptionFirst,descriptionSecond,
                     date,get_date,executed,summ, order_deleted, delivery, joint_buy);
             listFinishOrderHistory.add(finishOrder);
         }
        adapter.notifyDataSetChanged();


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //получить историю заказов от DB
        receiveOrderHistoryFromDB();
    }
}