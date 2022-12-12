package ru.tubi.project.activity.company_my;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;

import ru.tubi.project.R;
import ru.tubi.project.activity.Provider.CorrectedQuantityFromDeletedOrdersActivity;
import ru.tubi.project.adapters.OrderForCollect_partners_Adapter;
import ru.tubi.project.adapters.OrderForCollect_warehouse_Adapter;
import ru.tubi.project.models.CarrierPanelModel;
import ru.tubi.project.models.OrderForCollectModel;
import ru.tubi.project.models.ProviderCollectProductModel;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.utilites.Constant;
import ru.tubi.project.utilites.InitialData;
import ru.tubi.project.utilites.UserDataRecovery;

import static ru.tubi.project.activity.Config.MY_COMPANY_TAXPAYER_ID;
import static ru.tubi.project.free.AllCollor.TUBI_BLACK;
import static ru.tubi.project.free.AllCollor.TUBI_GREY_200;
import static ru.tubi.project.free.AllCollor.alert_dialog_button_green_pressed;
import static ru.tubi.project.free.AllText.ACTIVATE_BIG;
import static ru.tubi.project.free.AllText.BUILDING;
import static ru.tubi.project.free.AllText.LOAD_TEXT;
import static ru.tubi.project.free.AllText.MES_18;
import static ru.tubi.project.free.AllText.ORDER_IS_NOT_ACTIVE;
import static ru.tubi.project.free.AllText.SEE_BIG;
import static ru.tubi.project.free.AllText.TAX_ID_SMALL;
import static ru.tubi.project.free.AllText.WAREHOUSE;

public class OrderForCollectActivity extends AppCompatActivity
            implements View.OnClickListener {

    private RecyclerView recyclerView,rvWarehouseList;
    private LinearLayout llWarehouseInfo;
    private TextView tvWarehouseInfo, tvApply;
    private ArrayList<CarrierPanelModel> warehousesInfoList=new ArrayList<>();
    private ArrayList<OrderForCollectModel> buyersCompanyList = new ArrayList<>();
    private OrderForCollect_warehouse_Adapter adapter_warehouse;
    private OrderForCollect_partners_Adapter adapter_buyer;
    private int warehouse_id;
    private ArrayList <Integer> corrected_order_partner_id_arr= new ArrayList<>();
    private String myWarehousInfo;
    private AlertDialog.Builder adb;
    private AlertDialog ad;
    private UserModel userDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_for_collect);
        setTitle(R.string.order_for_collect);//Заказы для сборки

        recyclerView=findViewById(R.id.rvList);
        llWarehouseInfo=findViewById(R.id.llWarehouseInfo);
        rvWarehouseList=findViewById(R.id.rvWarehouseList);
        tvWarehouseInfo=findViewById(R.id.tvWarehouseInfo);

        llWarehouseInfo.setOnClickListener(this);

        //получить из sqlLite данные пользователя и компании
        UserDataRecovery userDataRecovery = new UserDataRecovery();
        userDataModel = userDataRecovery.getUserDataRecovery(this);

        startWarehousesList();

        OrderForCollect_warehouse_Adapter.RecyclerViewClickListener clickListener =
                new OrderForCollect_warehouse_Adapter.RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        warehouse_id = warehousesInfoList.get(position).getWarehouse_id();
                        myWarehousInfo = WAREHOUSE+" № "
                                +warehousesInfoList.get(position).getWarehouse_info_id()+"/"
                                +warehousesInfoList.get(position).getWarehouse_id()+" "
                                +warehousesInfoList.get(position).getCity()+" "
                                +warehousesInfoList.get(position).getStreet()+" "
                                +warehousesInfoList.get(position).getHouse();
                        try{
                            myWarehousInfo += BUILDING+" "+warehousesInfoList.get(position).getBuilding();
                        }catch (Exception ex){}
                        tvWarehouseInfo.setText(""+myWarehousInfo);
                        tvWarehouseInfo.setTextColor(TUBI_BLACK);

                        buyersCompanyList();
                        rvWarehouseList.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                };
        OrderForCollect_partners_Adapter.RecyclerViewClickListener orderClick =
                new OrderForCollect_partners_Adapter.RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        if(buyersCompanyList.get(position).getOrder_active() == 1){
                            whatBuyerShowOrder(position);
                        }else{
                            adActivateOrder(position);
                        }
                    }
                };

        adapter_warehouse = new OrderForCollect_warehouse_Adapter(this,
                warehousesInfoList,clickListener);
        rvWarehouseList.setAdapter(adapter_warehouse);

        adapter_buyer = new OrderForCollect_partners_Adapter(this,
                buyersCompanyList,orderClick);
        recyclerView.setAdapter(adapter_buyer);
    }

    @Override
    public void onClick(View v) {
        if(v.equals(llWarehouseInfo)){
            rvWarehouseList.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }
    //перейти в список товаров в заказе покупателя
    private void whatBuyerShowOrder(int position){
        //если этот заказ удален то аернуться
       /* if(buyersCompanyList.get(position).getOrder_deleted() == 1
                && buyersCompanyList.get(position).getCollect_product_for_delete() == 0){
            return;
        }*/
        String stBuyersCompany = buyersCompanyList.get(position).getAbbreviation()+" "
                +buyersCompanyList.get(position).getCounterparty()+" "+TAX_ID_SMALL+" "
                +buyersCompanyList.get(position).getTaxpayer_id();

        int order_partner_id = buyersCompanyList.get(position).getOrder_partner_id();
        int order_active = buyersCompanyList.get(position).getOrder_active();
        Intent intent = new Intent(this, ProviderCollectProductActivity.class);
        intent.putExtra("order_partner_id",order_partner_id);
        intent.putExtra("myWarehousInfo",myWarehousInfo);
        intent.putExtra("stBuyersCompany",stBuyersCompany);
        intent.putExtra("warehouse_id",warehouse_id);
        intent.putExtra("order_active",order_active);
        startActivity(intent);
        // Toast.makeText(this, "order_id: "+order_id, Toast.LENGTH_SHORT).show();
    }

    //получить ответ, есть удаленные товары для обработки
    private void getInfoToDeletedGoodsList(){
        String allOrders = "";
        for(int i=0; i < buyersCompanyList.size();i++){
            allOrders += buyersCompanyList.get(i).getOrder_partner_id()+";";
        }
        String url = Constant.WAREHOUSE_OFFICE;
        url += "get_all_orders_deleted_goods";
        url += "&"+"all_order_partner_id_str="+allOrders;
        String whatQuestion = "get_all_orders_deleted_goods";
        setInitialData(url, whatQuestion);
        Log.d("A111",getClass()+" / getInfoToDeletedGoodsList / url = "+url);
    }
    //активировать заказ для начала оформления документов
    //объединить и внести все товары в t_warehouse_inventory
    private void goOrderPartnerActivation(int position){
        String transaction_name = "sale";//продажа
        String url = Constant.PROVIDER_OFFICE;
        url += "go_order_partner_activation";
        url += "&"+"order_partner_id="+buyersCompanyList.get(position).getOrder_partner_id();
        url += "&"+"transaction_name="+transaction_name;
        url += "&"+"user_uid="+userDataModel.getUid();
        String whatQuestion = "go_order_partner_activation";
        setInitialData(url, whatQuestion);
    }
    //получить список покупателей для сборки товара
    private void buyersCompanyList(){
        String url = Constant.PROVIDER_OFFICE;
        url += "receive_list_company_for_collect";
        url += "&"+"warehouse_id="+warehouse_id;
        String whatQuestion = "receive_list_company_for_collect";
        setInitialData(url, whatQuestion);
    }
    //получить список складов поставщика
    private void startWarehousesList(){
        String url = Constant.PROVIDER_OFFICE;
        url += "receive_list_provider_warehouse";
        url += "&"+"company_tax_id="+MY_COMPANY_TAXPAYER_ID;
        String whatQuestion = "receive_list_provider_warehouse";
        setInitialData(url, whatQuestion);
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
                asyncDialog.setMessage(LOAD_TEXT);
                asyncDialog.show();
                super.onPreExecute();
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            protected void onPostExecute(String result) {
                if(whatQuestion.equals("receive_list_provider_warehouse")){
                    splitWarehouseResult(result);
                }
                else if(whatQuestion.equals("receive_list_company_for_collect")){
                    splitBuyersCompanyResult(result);
                }
                else if(whatQuestion.equals("go_order_partner_activation")){
                    splitOrderPartnerActivationResult(result);
                }
                else if(whatQuestion.equals("get_all_orders_deleted_goods")){
                    splitOrderPartnerForCorrectedResult(result);
                }
                //скрыть диалоговое окно
                asyncDialog.dismiss();
            }
        };
        task.execute(url_get);
    }

    //список заказов в которых есть товары которые удаленазчиком
    private void splitOrderPartnerForCorrectedResult(String result){
        corrected_order_partner_id_arr.clear();
        Log.d("A111",getClass()+" / splitOrderPartnerForCorrectedResult / result = "+result);
        try {
            String[] res = result.split("<br>");

            for (int i = 0; i < res.length; i++) {
                corrected_order_partner_id_arr.add(Integer.parseInt(res[i]));
            }
            if (corrected_order_partner_id_arr.size() > 0) {
                adOrdersForCorrectedList();
            }
        }catch (Exception ex){

        }
    }
    private void splitOrderPartnerActivationResult(String result){
        //Toast.makeText(this, "res\n"+result, Toast.LENGTH_SHORT).show();
        try{
            String[] res = result.split("<br>");
            String[] one_temp = res[0].split("&nbsp");
            if (one_temp[0].equals("error") || one_temp[0].equals("messege")) {
                Toast.makeText(this, "" + one_temp[1], Toast.LENGTH_LONG).show();
                // adapter_buyer.notifyDataSetChanged();
                // return;
            } else if(one_temp[0].equals("RESULT_OK")){
                //получить список покупателей
                buyersCompanyList();
            }
        }catch (Exception ex){
            Toast.makeText(this, "ex: OrdersShipmentActivity / splitOrderPartnerActivationResult\n"
                    +ex, Toast.LENGTH_SHORT).show();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void splitBuyersCompanyResult(String result){
        //Toast.makeText(this, "res:\n"+result, Toast.LENGTH_SHORT).show();
        buyersCompanyList.clear();
        try{
            String[] res = result.split("<br>");
            String[] one_temp = res[0].split("&nbsp");
            if (one_temp[0].equals("error") || one_temp[0].equals("messege")) {
                Toast.makeText(this, "" + one_temp[1], Toast.LENGTH_LONG).show();
                // adapter_buyer.notifyDataSetChanged();
                // return;
            } else {
                for (int i = 0; i < res.length; i++) {
                    String[] temp = res[i].split("&nbsp");
                    int $order_partner_id=Integer.parseInt(temp[0]);
                    String abbreviation=temp[1];
                    String counterparty=temp[2];
                    long taxpayer_id = Long.parseLong(temp[3]);
                    int order_active = Integer.parseInt(temp[4]);
                    int collected = Integer.parseInt(temp[5]);
                    long get_order_date_millis= Long.parseLong(temp[6]);

                    OrderForCollectModel buyer = new OrderForCollectModel($order_partner_id, abbreviation
                            , counterparty, taxpayer_id, order_active
                            , collected, get_order_date_millis);
                    buyersCompanyList.add(buyer);
                }
                //сортируем лист по 2 полям (logistic_product и car_or_warehouse_id)
                buyersCompanyList.sort(Comparator.comparing(OrderForCollectModel::getAbbreviation)
                        .thenComparing(OrderForCollectModel::getCounterparty));
            }
        }catch (Exception ex){
            Toast.makeText(this, "ex: OrdersShipmentActivity / splitBuyersCompanyResult\n"
                    +ex, Toast.LENGTH_SHORT).show();
        }
        adapter_buyer.notifyDataSetChanged();
        //получить ответ, есть удаленные товары для обработки
        getInfoToDeletedGoodsList();
    }
    //разобрать результат от сервера список складов
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void splitWarehouseResult(String result){
        //Toast.makeText(this, "res\n"+result, Toast.LENGTH_SHORT).show();
        warehousesInfoList.clear();
        try{
            String[] res = result.split("<br>");
            String[] one_temp = res[0].split("&nbsp");
            if (one_temp[0].equals("error") || one_temp[0].equals("messege")) {
                Toast.makeText(this, "" + one_temp[1], Toast.LENGTH_LONG).show();
                return;
            } else {
                for (int i = 0; i < res.length; i++) {
                    String[] temp = res[i].split("&nbsp");
                    int warehouse_info_id=Integer.parseInt(temp[0]);
                    int Warehouse_id = Integer.parseInt(temp[1]);
                    String City = temp[2];
                    String Street = temp[3];
                    int House = Integer.parseInt(temp[4]);
                    String Building = "";
                    try {
                        Building = temp[5];
                    } catch (Exception ex) {      }


                    CarrierPanelModel warehouse = new CarrierPanelModel(warehouse_info_id,
                            Warehouse_id, City, Street,House, Building);
                    warehousesInfoList.add(warehouse);
                }
            }
            //сортируем лист по 1 полям (Warehouse_info_id )
            warehousesInfoList.sort(Comparator.comparing(CarrierPanelModel::getWarehouse_info_id));

            //сделать список строк для показа в ListView
            //makeStringListForWarehouse();

            if(warehousesInfoList.size() > 1){
                adapter_warehouse.notifyDataSetChanged();
                //adapLvWarehouse.notifyDataSetChanged();
            }else{
                warehouse_id = warehousesInfoList.get(0).getWarehouse_id();
                myWarehousInfo = WAREHOUSE+" № "+warehousesInfoList.get(0).getWarehouse_info_id()+"/"
                        +warehousesInfoList.get(0).getWarehouse_id()+" "+warehousesInfoList.get(0).getCity()+" "
                        +warehousesInfoList.get(0).getStreet()+" "+warehousesInfoList.get(0).getHouse();
                try{
                    myWarehousInfo += BUILDING+" "+warehousesInfoList.get(0).getBuilding();
                }catch (Exception ex){}
                tvWarehouseInfo.setText(""+myWarehousInfo);
                tvWarehouseInfo.setTextColor(TUBI_BLACK);

                buyersCompanyList();
                rvWarehouseList.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                // tvWarehouseInfo.setClickable(false);
                llWarehouseInfo.setClickable(false);

                //startList();
            }
        }catch (Exception ex){

        }
    }
    // список заказов для коректировки удаленных заказов
    private void adOrdersForCorrectedList(){
        adb = new AlertDialog.Builder(this);
        ListView lv = new ListView(this);
        ArrayAdapter adap;
        ArrayList ordersCorrectedList = new ArrayList();
        for(int i=0; i < corrected_order_partner_id_arr.size();i++){
            String str = "Заказ № "+corrected_order_partner_id_arr.get(i);
            ordersCorrectedList.add(str);
        }
        adap = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ordersCorrectedList);
        lv.setAdapter(adap);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("A111",getClass()+" / adCorrectProductsQuantity / i = "+i);
                Intent intent = new Intent(getApplication()
                        , CorrectedQuantityFromDeletedOrdersActivity.class);
                intent.putExtra("order_partner_id",corrected_order_partner_id_arr.get(i));
                startActivity(intent);
                ad.cancel();
            }
        });
        String st1 = "Список заказов с удаленными товарами";
        adb.setTitle(st1);
        adb.setView(lv);
        ad=adb.create();
        ad.show();
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
    private void adActivateOrder(int position) {
        adb = new AlertDialog.Builder(this);
        String st1 = ORDER_IS_NOT_ACTIVE;
        String description = MES_18;

        adb.setTitle(st1);
        adb.setMessage(description);

        adb.setPositiveButton(SEE_BIG, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                whatBuyerShowOrder(position);
            }
        });
        adb.setNeutralButton(ACTIVATE_BIG, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //активировать заказ для начала оформления документов
                //объединить и внести все товары в t_warehouse_inventory
                goOrderPartnerActivation(position);
                Toast.makeText(OrderForCollectActivity.this, "activation", Toast.LENGTH_SHORT).show();
            }
        });

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //получить список покупателей для сборки товара
        buyersCompanyList();
    }
}