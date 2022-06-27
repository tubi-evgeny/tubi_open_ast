package ru.tubi.project.activity.company_my;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;

import ru.tubi.project.R;
import ru.tubi.project.adapters.ShipmentProduct_partners_Adapter;
import ru.tubi.project.adapters.ShipmentProduct_warehouse_Adapter;
import ru.tubi.project.models.CarrierPanelModel;
import ru.tubi.project.models.OrderForCollectModel;
import ru.tubi.project.models.ShipmentProductModel;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.utilites.Constant;
import ru.tubi.project.utilites.InitialData;
import ru.tubi.project.utilites.UserDataRecovery;

import static ru.tubi.project.Config.MY_COMPANY_TAXPAYER_ID;
import static ru.tubi.project.free.AllCollor.TUBI_BLACK;
import static ru.tubi.project.free.AllText.BUILDING;
import static ru.tubi.project.free.AllText.LOAD_TEXT;
import static ru.tubi.project.free.AllText.WAREHOUSE;

public class ShipmentProductActivity extends AppCompatActivity
        implements View.OnClickListener{

    private RecyclerView recyclerView,rvWarehouseList;
    private LinearLayout llWarehouseInfo;
    private TextView tvWarehouseInfo, tvApply;
    private ArrayList<CarrierPanelModel> warehousesInfoList=new ArrayList<>();
    private ArrayList<ShipmentProductModel> shipmentList = new ArrayList<>();
    private ShipmentProduct_warehouse_Adapter adapter_warehouse;
    private ShipmentProduct_partners_Adapter adapter_buyer;
    private int warehouse_id;
    private String myWarehousInfo;
    private AlertDialog.Builder adb;
    private AlertDialog ad;
    private UserModel userDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_shipment);
        setTitle(R.string.shipment_product);//Открузка товара

        recyclerView=findViewById(R.id.rvList);
        llWarehouseInfo=findViewById(R.id.llWarehouseInfo);
        rvWarehouseList=findViewById(R.id.rvWarehouseList);
        tvWarehouseInfo=findViewById(R.id.tvWarehouseInfo);

        llWarehouseInfo.setOnClickListener(this);

        //получить из sqlLite данные пользователя и компании
        UserDataRecovery userDataRecovery = new UserDataRecovery();
        userDataModel = userDataRecovery.getUserDataRecovery(this);

        startWarehousesList();

        ShipmentProduct_warehouse_Adapter.RecyclerViewClickListener clickListener =
                new ShipmentProduct_warehouse_Adapter.RecyclerViewClickListener() {
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
        ShipmentProduct_partners_Adapter.RecyclerViewClickListener orderClick=
                new ShipmentProduct_partners_Adapter.RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        whatGiveAwayShow(position);
                        //Toast.makeText(ShipmentProductActivity.this, "whatBuyerShowOrder\n"+position, Toast.LENGTH_SHORT).show();
                    }
                };

        adapter_warehouse = new ShipmentProduct_warehouse_Adapter(this,
                warehousesInfoList,clickListener);
        rvWarehouseList.setAdapter(adapter_warehouse);

        adapter_buyer = new ShipmentProduct_partners_Adapter(this,
                shipmentList,orderClick);
        recyclerView.setAdapter(adapter_buyer);
    }
    @Override
    public void onClick(View v) {

    }
    private void whatGiveAwayShow(int position){
        Intent intent = new Intent(this,GiveAwayProductActivity.class);
        intent.putExtra("out_warehouse_id",warehouse_id);
        intent.putExtra("in_warehouse_id",shipmentList.get(position).getIn_warehouse_id());
        intent.putExtra("car_id",shipmentList.get(position).getCar_id());
        intent.putExtra("logistic_product",shipmentList.get(position).getLogistic_product());
        intent.putExtra("myWarehousInfo",myWarehousInfo);
        intent.putExtra("in_WarehousInfo",shipmentList.get(position).getWarehouseInfoString());
        intent.putExtra("car_info_string",shipmentList.get(position).getCar_info_string());
        startActivity(intent);
    }
    //получить список покупателей для выдачи товара
    private void buyersCompanyList(){
        String url = Constant.PROVIDER_OFFICE;
        url += "receive_list_company_for_shipment";
        url += "&"+"warehouse_id="+warehouse_id;
        String whatQuestion = "receive_list_company_for_shipment";
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
                else if(whatQuestion.equals("receive_list_company_for_shipment")){
                    splitBuyersCompanyResult(result);
                }

                //скрыть диалоговое окно
                asyncDialog.dismiss();
            }
        };
        task.execute(url_get);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void splitBuyersCompanyResult(String result){
        //Toast.makeText(this, "res:\n"+result, Toast.LENGTH_SHORT).show();
        shipmentList.clear();
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
                    int counterparty_id=Integer.parseInt(temp[0]);
                    String companyInfoString=temp[1];
                    int in_warehouse_id=Integer.parseInt(temp[2]);
                    String warehouseInfoString = temp[3];
                    int logistic_product = Integer.parseInt(temp[4]);
                    int car_id = Integer.parseInt(temp[5]);
                    String car_info_string= temp[6];
                    int out_active = Integer.parseInt(temp[7]);

                    ShipmentProductModel shipment = new ShipmentProductModel(counterparty_id
                            , companyInfoString, in_warehouse_id, warehouseInfoString
                            , logistic_product, car_id, car_info_string, out_active);
                    shipmentList.add(shipment);
                }
                //сортируем лист по 2 полям (logistic_product и car_or_warehouse_id)
              /*  buyersCompanyList.sort(Comparator.comparing(OrderForCollectModel::getAbbreviation)
                        .thenComparing(OrderForCollectModel::getCounterparty));*/
            }
        }catch (Exception ex){
            Toast.makeText(this, "ex: OrdersShipmentActivity / splitBuyersCompanyResult\n"
                    +ex, Toast.LENGTH_SHORT).show();
        }
        adapter_buyer.notifyDataSetChanged();
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