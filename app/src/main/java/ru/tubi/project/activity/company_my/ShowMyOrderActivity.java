package ru.tubi.project.activity.company_my;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import ru.tubi.project.R;
import ru.tubi.project.adapters.ShowMyOrderAdapter;
import ru.tubi.project.models.OrderModel;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.utilites.DownloadImage;
import ru.tubi.project.utilites.InitialData;

import java.util.ArrayList;
import java.util.Comparator;

import ru.tubi.project.utilites.Constant;
import ru.tubi.project.utilites.MakeImageToSquare;
import ru.tubi.project.utilites.UserDataRecovery;

import static ru.tubi.project.activity.Config.ADMIN_PANEL_URL_IMAGES;
import static ru.tubi.project.activity.Config.ADMIN_PANEL_URL_PREVIEW_IMAGES;
import static ru.tubi.project.free.AllCollor.TUBI_GREY_200;
import static ru.tubi.project.free.AllCollor.alert_dialog_button_green_pressed;
import static ru.tubi.project.free.AllText.BUILDING;
import static ru.tubi.project.free.AllText.CANOT_DELETE_ORDER_TEXT;
import static ru.tubi.project.free.AllText.DELIVERY_TEXT;
import static ru.tubi.project.free.AllText.JOINT_BUY_CHAR_TEXT;
import static ru.tubi.project.free.AllText.JOINT_BUY_SHORT_TEXT;
import static ru.tubi.project.free.AllText.MES_30;
import static ru.tubi.project.free.AllText.NO_BACK;
import static ru.tubi.project.free.AllText.ORDER;
import static ru.tubi.project.free.AllText.PERFORM;
import static ru.tubi.project.free.AllText.SHOW;
import static ru.tubi.project.free.AllText.ST;
import static ru.tubi.project.free.AllText.WAREHOUSE;
import static ru.tubi.project.free.AllText.YOUR_ORDER;
import static ru.tubi.project.free.AllText.YOU_REALLY_WANT_TO_DELETE_ORDER;

public class ShowMyOrderActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvOrderInfo,tvTotalSumm;
    private ImageView ivDeleteOrder, ivEditOrder;
    private LinearLayout llEditDelete, llOrderInfo;
    private RecyclerView rvList;
    private Intent takeit;
    private ArrayList<OrderModel> productList = new ArrayList<>();
    private ShowMyOrderAdapter adapter;
    private int order_id, executed, order_deleted, deliveryKey, joint_buy, x=0 ;
    private String address_for_delivery, placeDeliveryOrderInfo, joint_buy_str="";
    private AlertDialog.Builder adb;
    private AlertDialog ad;
    private UserModel userDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_my_order);
        setTitle(SHOW);//Показываю Ваш заказ
        getSupportActionBar().setSubtitle(YOUR_ORDER);

        rvList=findViewById(R.id.rvList);
        llEditDelete=findViewById(R.id.llEditDelete);
        llOrderInfo=findViewById(R.id.llOrderInfo);
        tvOrderInfo=findViewById(R.id.tvOrderInfo);
        tvTotalSumm=findViewById(R.id.tvTotalSumm);
        ivDeleteOrder=findViewById(R.id.ivDeleteOrder);
        ivEditOrder=findViewById(R.id.ivEditOrder);

        //получить из sqlLite данные пользователя и компании
        UserDataRecovery userDataRecovery = new UserDataRecovery();
        userDataModel = userDataRecovery.getUserDataRecovery(this);

        ivDeleteOrder.setOnClickListener(this);
        ivEditOrder.setOnClickListener(this);
        llOrderInfo.setOnClickListener(this);

        takeit = getIntent();
        order_id = takeit.getIntExtra("order_id",0);
        executed = takeit.getIntExtra("executed",0);
        deliveryKey = takeit.getIntExtra("delivery",0);
        joint_buy = takeit.getIntExtra("joint_buy",0);
        order_deleted = takeit.getIntExtra("order_deleted",0);

        if(joint_buy == 1) {
            joint_buy_str = JOINT_BUY_SHORT_TEXT;
        }
        tvOrderInfo.setText(""+ORDER+" № "+order_id+" "+joint_buy_str);

        if(executed == 1 || order_deleted == 1){
            llEditDelete.setVisibility(View.GONE);
        }
        if(deliveryKey == 1){
            receiveDeliveryAddress();
        }else{
            receiveParetnerWarehouseInfo();
        }

        startShowProductToOrderList();

        ShowMyOrderAdapter.RecyclerViewClickListener clickListener =
                new ShowMyOrderAdapter.RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        WhatButtonClicked(view, position);
                       // Toast.makeText(ShowMyOrderActivity.this,
                       //         "view: "+view+"\nposition: "+position, Toast.LENGTH_SHORT).show();
                    }
                };
        adapter = new ShowMyOrderAdapter(this,productList,clickListener);
        rvList.setAdapter(adapter);
    }
    @Override
    public void onClick(View v) {
        if (v.equals(ivEditOrder)) {
            Toast.makeText(this,
                    "Опция редактирования заказа находится в разработке", Toast.LENGTH_LONG).show();

        }
        else if (v.equals(ivDeleteOrder)) {
            if(joint_buy == 1) {
                //если совместный заказ удалять нельзя
                adCanotDelete();
            }else{
                adDeleteOrder();
            }
        }
        else if(v.equals(llOrderInfo)){
            adPlaceDeliveryInfo();
        }
    }
    private void WhatButtonClicked(View view, int position){
        String st = String.valueOf(view);
        String [] clickName = st.split("/");
        if(clickName[1].equals("ivImage}")){
            adShowImage(position);
        }

    }
    private void showPlaceReceivingOrder(String placeDeliveryOrderInfo){

        tvOrderInfo.setText(""+ORDER+" № "+order_id+" "+joint_buy_str+"  "+placeDeliveryOrderInfo);
    }
    //получить адресс доставки
    private void receiveDeliveryAddress(){
        String url = Constant.API_TEST;
        url += "receive_delivery_address";
        url += "&" + "order_id=" +order_id;
        String whatQuestion = "receive_delivery_address";
        setInitialData(url,whatQuestion);
    }
    //получить данные о складе выдачи товара
    private void receiveParetnerWarehouseInfo(){
        String url = Constant.API;
        url += "receive_paretner_warehouse_info";
        url += "&"+"order_id="+order_id;
        String whatQuestion = "receive_paretner_warehouse_info";
        setInitialData(url, whatQuestion);
        Log.d("A111","ShowMyOrderActivity / receiveParetnerWarehouseInfo / url="+url);
    }
    //удалить заказ из БД
    private void deleteOrderFromDataBase(){
        String url = Constant.USER_OFFICE;
        url += "delelte_order_from_database";
        url += "&"+"user_uid="+userDataModel.getUid();
        url += "&"+"order_id="+order_id;
        String whatQuestion = "delelte_order_from_database";
        setInitialData(url, whatQuestion);
        Log.d("A111",getClass()+" / deleteOrderFromDataBase / url = "+url);

        onBackPressed();
    }
    //получить список продуктов из этого заказа
    private void startShowProductToOrderList(){
        String url = Constant.API;
        url += "receive_list_products_to_order";
        url += "&"+"order_id="+order_id;
        String whatQuestion = "receive_list_products_to_order";
        setInitialData(url, whatQuestion);
    }
    private void setInitialData(String url_get, String whatQuestion) {
        InitialData task=new InitialData(){

            @RequiresApi(api = Build.VERSION_CODES.N)
            protected void onPostExecute(String result) {
                if(whatQuestion.equals("receive_list_products_to_order")){
                    splitResult(result);
                     //Toast.makeText(ShowMyOrderActivity.this,
                      //  "res: "+result, Toast.LENGTH_SHORT).show();
                }else if(whatQuestion.equals("receive_paretner_warehouse_info")){
                    splitWarehouseResult(result);
                }else if(whatQuestion.equals("receive_delivery_address")){
                    splitAddressDelivery(result);
                }
                //hide the dialog
                // asyncDialog.dismiss();
            }
        };
        task.execute(url_get);
    }
    //разобрать ответ адрес доставки
    private void splitAddressDelivery(String result){
        address_for_delivery = result;
        placeDeliveryOrderInfo = DELIVERY_TEXT+" "+address_for_delivery;
        showPlaceReceivingOrder(placeDeliveryOrderInfo);
    }
    //разобрать данные о складе выдачи товара
    private void splitWarehouseResult(String result){
        Log.d("A111","ShowMyOrderActivity / splitWarehouseResult / result="+result);
        try{
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

                String st1 = "№ "+warehouse_info_id+"/"+myWarehouse_id;
                String st2 = city+" "
                        +ST+" "+street+" "+house;
                try {
                    String building = temp[5];
                    st2 += " "+BUILDING+" "+building;
                }catch (Exception ex){};

                placeDeliveryOrderInfo = WAREHOUSE+" "+st1+"\n"+st2;
                showPlaceReceivingOrder(placeDeliveryOrderInfo);
                //tvOrderInfo.setText(""+ORDER+" № "+order_id+"  "+WAREHOUSE+" "+st1+"\n"+st2);
            }
        }catch(Exception ex){
            Log.d("A111","ShowMyOrderActivity / splitWarehouseResult / Exception="+ex);
            Toast.makeText(this, "Exception: "+ex, Toast.LENGTH_SHORT).show();
        }
    }
    // разобрать результат с сервера, список продуктов которые собраны для отправки и их колличество
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void splitResult(String result){
        productList.clear();
        double totalSumm = 0;
        //Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
        try{
            String[] res = result.split("<br>");
            String[] one_temp = res[0].split("&nbsp");
            if (one_temp[0].equals("error") || one_temp[0].equals("messege")) {
                Toast.makeText(this, "" + one_temp[1], Toast.LENGTH_LONG).show();
                adapter.notifyDataSetChanged();
                return;
            } else {
                for (int i = 0; i < res.length; i++) {
                    String[] temp = res[i].split("&nbsp");
                    int product_id = Integer.parseInt(temp[0]);
                    int productInventory_id = Integer.parseInt(temp[1]);
                    String category = temp[2];
                    String brand = temp[3];
                    String characteristic = temp[4];
                    String type_packaging = temp[5];
                    String unit_measure = temp[6];
                    int weight_volume = Integer.parseInt(temp[7]);
                    int quantity_package = Integer.parseInt(temp[8]);
                    String image_url = temp[9];
                    double price = Double.parseDouble(temp[10]);
                    String storage_conditions=temp[11];

                    double quantity_to_order = Double.parseDouble(temp[12]);

                    int counterparty_id = Integer.parseInt(temp[13]);
                    String abbreviation = temp[14];
                    String counterparty = temp[15];
                    String product_name=temp[16];
                    double price_process = Double.parseDouble(temp[17]);
                    int corrected = Integer.parseInt(temp[18]);

                    //int checked = Integer.parseInt(temp[17]);

                    OrderModel delivery = new OrderModel(
                            product_id,productInventory_id, category, product_name,brand,
                            characteristic,
                            type_packaging, unit_measure,weight_volume,quantity_package,
                            image_url, price, price_process, storage_conditions ,quantity_to_order
                            ,counterparty_id, abbreviation, counterparty, corrected);

                    productList.add(delivery);

                    totalSumm += quantity_to_order * (price + price_process);
                }
            }
        }catch(Exception ex){
            Toast.makeText(this, "Exception: "+ex, Toast.LENGTH_SHORT).show();
        }
         //отсортировать есть доставка или нет и добавить данные доставщика(склад это или транспорт)
        //сортируем лист по 2 полям (logistic_product и car_or_warehouse_id)
        productList.sort(Comparator.comparing(OrderModel::getCategory)
                .thenComparing(OrderModel::getCharacteristic));

        adapter.notifyDataSetChanged();
        tvTotalSumm.setText(String.format("%.2f",totalSumm));
    }
    private void adCanotDelete(){
        adb = new AlertDialog.Builder(this);
        String st1 = CANOT_DELETE_ORDER_TEXT;
        String st2 = MES_30;
        adb.setTitle(st1);
        adb.setMessage(st2);

        ad=adb.create();
        ad.show();
    }
    private void adDeleteOrder(){
        adb = new AlertDialog.Builder(this);
        String st1 = YOU_REALLY_WANT_TO_DELETE_ORDER;
        adb.setTitle(st1);
        adb.setPositiveButton(PERFORM, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //удалить заказ из БД
                deleteOrderFromDataBase();
            }
        });
        adb.setNeutralButton(NO_BACK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ad.cancel();
            }
        });
        ad=adb.create();
        ad.setCanceledOnTouchOutside(false);
        ad.setCancelable(false);
        ad.show();

        Button buttonbackground1 = ad.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonbackground1.setBackgroundColor(alert_dialog_button_green_pressed);
        buttonbackground1.setTextColor(Color.WHITE);
        Button buttonbackground2 = ad.getButton(DialogInterface.BUTTON_NEUTRAL);
        buttonbackground2.setBackgroundColor(TUBI_GREY_200);
        buttonbackground2.setTextColor(Color.WHITE);
    }
    private void adShowImage(int position){
        String image_url = productList.get(position).getImage_url();
        adb = new AlertDialog.Builder(this);
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        ImageView imageView = new ImageView(this);

        if(!image_url.equals("null")) {
            new DownloadImage(){
                @Override
                protected void onPostExecute(Bitmap result) {
                    new MakeImageToSquare(result,imageView);
                }
            }
                    .execute(ADMIN_PANEL_URL_IMAGES+image_url);
        }else imageView.setImageResource(R.drawable.tubi_logo_no_image_300ps);

        adb.setView(imageView);
        ad=adb.create();
        ad.show();
        ad.getWindow().setLayout(width, width);
    }
    private void adPlaceDeliveryInfo(){
        AlertDialog.Builder adb= new AlertDialog.Builder(this);
        String st1 = placeDeliveryOrderInfo;
        adb.setTitle("");
        adb.setMessage(st1);
        ad=adb.create();
        ad.show();
    }

}