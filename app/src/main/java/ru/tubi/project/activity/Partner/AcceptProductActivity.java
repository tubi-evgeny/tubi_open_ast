package ru.tubi.project.activity.Partner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import ru.tubi.project.R;
import ru.tubi.project.adapters.AcceptProductAdapter;
import ru.tubi.project.adapters.AcceptProductWarehouseAdapter;
import ru.tubi.project.models.CarrierPanelModel;
import ru.tubi.project.utilites.InitialData;

import java.util.ArrayList;
import java.util.Comparator;

import ru.tubi.project.utilites.Constant;

import static ru.tubi.project.Config.MY_COMPANY_TAXPAYER_ID;
import static ru.tubi.project.Config.MY_UID;
import static ru.tubi.project.free.AllCollor.TUBI_BLACK;
import static ru.tubi.project.free.AllCollor.TUBI_GREEN_600;
import static ru.tubi.project.free.AllCollor.TUBI_GREY_200;
import static ru.tubi.project.free.AllText.ACCEPT_PRODUCT;
import static ru.tubi.project.free.AllText.BUILDING;

public class AcceptProductActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView,rvWarehouseList;
    private LinearLayout llWarehouseInfo;
    private TextView tvWarehouseInfo, tvApply;
    //private ListView lvWarehouses;
    private ArrayList<CarrierPanelModel> warehousesInfoList=new ArrayList<>();
    private ArrayList<CarrierPanelModel> acceptProductList=new ArrayList<>();
    private ArrayList<CarrierPanelModel> fullAcceptProductList = new ArrayList<>();
    private ArrayList<String> warehouseStringList= new ArrayList<>();
    private AcceptProductAdapter adapter;
    private AcceptProductWarehouseAdapter adap_warehouse;
    private Intent takeit;
    private int warehouse_id;
    private String which_warehouse, document_name="товарная накладная";
    private ArrayList<Integer> checkedList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_product);
        setTitle(ACCEPT_PRODUCT);//Принять товар

        recyclerView=findViewById(R.id.rvList);
        llWarehouseInfo=findViewById(R.id.llWarehouseInfo);
        rvWarehouseList=findViewById(R.id.rvWarehouseList);
        //lvWarehouses=findViewById(R.id.lvWarehouses);
        tvWarehouseInfo=findViewById(R.id.tvWarehouseInfo);
        tvApply=findViewById(R.id.tvApply);

        tvWarehouseInfo.setOnClickListener(this);
        llWarehouseInfo.setOnClickListener(this);
        tvApply.setOnClickListener(this);

        tvApply.setClickable(false);

        takeit = getIntent();
        which_warehouse = takeit.getStringExtra("which_warehouse");

        startWarehousesList();

        AcceptProductWarehouseAdapter.RecyclerViewClickListener clickListener =
                new AcceptProductWarehouseAdapter.RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        tvWarehouseInfo.setText(""+warehouseStringList.get(position));
                        tvWarehouseInfo.setTextColor(TUBI_BLACK);
                        rvWarehouseList.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        warehouse_id=warehousesInfoList.get(position).getWarehouse_id();

                        acceptProductList.clear();
                        startList();
                        tvApply.setClickable(false);
                        tvApply.setBackgroundColor(TUBI_GREY_200);
                       // Toast.makeText(AcceptProductActivity.this,
                       //         "view: "+view+"\nposition: "+position, Toast.LENGTH_SHORT).show();
                    }
                };
        AcceptProductAdapter.OnCheckedChangeListener checkedChangeListener =
                new AcceptProductAdapter.OnCheckedChangeListener() {
                    @Override
                    public void isChecked(View view, boolean flag, int position) {
                        whatCheckClicked(flag, position);
                        //Toast.makeText(AcceptProductActivity.this,
                        //        "checked: "+flag+"\nposition : "+position, Toast.LENGTH_SHORT).show();
                    }
                };
        adap_warehouse=new AcceptProductWarehouseAdapter(
                this,warehousesInfoList,clickListener);
        rvWarehouseList.setAdapter(adap_warehouse);

        adapter=new AcceptProductAdapter(
                this,acceptProductList,checkedChangeListener);
        recyclerView.setAdapter(adapter);
    }
    private void whatCheckClicked(boolean flag, int position){
        int check=0;
        if (flag) check = 1;
        checkedList.set(position,check);

        tvApply.setBackgroundColor(TUBI_GREEN_600);
        tvApply.setClickable(true);

       // Toast.makeText(this, "check: "+checkedList.get(position), Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onClick(View v) {
        if (v.equals(llWarehouseInfo)) {//(v.equals(tvWarehouseInfo)) {
            //lvWarehouses.setVisibility(View.VISIBLE);
            rvWarehouseList.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            //adap_warehouse.notifyDataSetChanged();
           // Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
        } else if(v.equals(tvApply)){
            writeCheckToTable();
           // Toast.makeText(this, "tvApply", Toast.LENGTH_SHORT).show();
        }
    }
    //записать товары в таблицу t_warehouse_inventory_in_out и в acceptProductList
    private void writeCheckToTable() {

        for (int i = 0; i < acceptProductList.size(); i++) {
            if(checkedList.get(i) == 1 && acceptProductList.get(i).getChecked() == 0){
                //добавить собранный товар в warehouse_inventory_in_out
                addProductForMoving(i);
                acceptProductList.get(i).setChecked(1);
            }
        }
        tvApply.setBackgroundColor(TUBI_GREY_200);
        tvApply.setClickable(false);
        checkedList.clear();
       // acceptProductList.clear();
        startList();
    }
    //добавить собранный товар в warehouse_inventory_in_out и в t_logistic_product
    private void addProductForMoving(int position){
        int warehouseInventory_id = acceptProductList.get(position).getWarehouseInventory_id();
        int in_active = 1;//товар получен складом
        int logistic_product=acceptProductList.get(position).getLogistic_product();
        int give_out=acceptProductList.get(position).getColorDelivery();
        String url = Constant.PROVIDER_OFFICE;
        url += "update_in_active_to_table";
        url += "&"+"warehouseInventory_id="+warehouseInventory_id;
        url += "&"+"in_active="+in_active;
        url += "&"+"logistic_product="+logistic_product;
        url += "&"+"give_out="+give_out;
        url += "&"+"user_uid="+MY_UID;
        String whatQuestion = "update_in_active_to_table";
        setInitialData(url, whatQuestion);

    }
    //получить список складов контрагента
    private void startWarehousesList(){
        String url = Constant.PROVIDER_OFFICE;
        url += "receive_list_warehouse";
        url += "&"+"company_tax_id="+MY_COMPANY_TAXPAYER_ID;
        String whatQuestion = "receive_list_warehouse";
        setInitialData(url, whatQuestion);
    }
    //получить все доставленные warhouse_inventory_id но не принятые и показать их
    private void startList() {
        String url = Constant.PROVIDER_OFFICE;
        url += "receive_list_delivery_to_accept";//PROVIDER_OFFICE
        url += "&"+"warehouse_id="+warehouse_id;
        String whatQuestion = "receive_list_delivery_to_accept";
        setInitialData(url, whatQuestion);
    }
    private void setInitialData(String url_get, String whatQuestion) {
        // ProgressDialog asyncDialog = new ProgressDialog(this);

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
                if(whatQuestion.equals("receive_list_delivery_to_accept")){
                    splitResult(result);
                   // Toast.makeText(AcceptProductActivity.this, "res: "+result, Toast.LENGTH_SHORT).show();
                }else if(whatQuestion.equals("receive_list_warehouse")){
                    splitWarehouseResult(result);
                }
                //hide the dialog
                // asyncDialog.dismiss();
            }
        };
        task.execute(url_get);
    }
    //разобрать результат от сервера список складов
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void splitWarehouseResult(String result){
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
                    int Warehouse_id = Integer.parseInt(temp[0]);
                    String City = temp[1];
                    String Street = temp[2];
                    int House = Integer.parseInt(temp[3]);
                    String Building = "";
                    try {
                        Building = temp[4];
                    } catch (Exception ex) {      }
                    int warehouse_info_id=Integer.parseInt(temp[5]);
                    String warehouse_tipe = temp[6];
                    int checked=0;

                    if(which_warehouse.equals("partner_warehouse") &&
                            warehouse_tipe.equals("partner")){
                        CarrierPanelModel warehouse = new CarrierPanelModel(Warehouse_id, City,
                                Street,House, Building,warehouse_info_id,warehouse_tipe, checked);
                        warehousesInfoList.add(warehouse);
                    }else if(which_warehouse.equals("storage_provider_warehouse") &&
                            warehouse_tipe.equals("storage")){
                        CarrierPanelModel warehouse = new CarrierPanelModel(Warehouse_id, City,
                                Street,House, Building,warehouse_info_id,warehouse_tipe, checked);
                        warehousesInfoList.add(warehouse);
                    }else if(which_warehouse.equals("storage_provider_warehouse") &&
                            warehouse_tipe.equals("provider")){
                        CarrierPanelModel warehouse = new CarrierPanelModel(Warehouse_id, City,
                                Street,House, Building,warehouse_info_id,warehouse_tipe, checked);
                        warehousesInfoList.add(warehouse);
                    }
                }
            }
            //сортируем лист по 2 полям (logistic_product и car_or_warehouse_id)
            warehousesInfoList.sort(Comparator.comparing(CarrierPanelModel::getWarehouse_tipe)
                    .thenComparing(CarrierPanelModel::getWarehouse_info_id));

            //adap_warehouse.notifyDataSetChanged();
            //сделать список строк для показа в ListView
            makeStringListForWarehouse();

            if(warehousesInfoList.size() > 1){
                adap_warehouse.notifyDataSetChanged();
                //adapLvWarehouse.notifyDataSetChanged();
            }else{
                warehouse_id = warehousesInfoList.get(0).getWarehouse_id();
                tvWarehouseInfo.setText(warehouseStringList.get(0));
                tvWarehouseInfo.setTextColor(TUBI_BLACK);
               // tvWarehouseInfo.setClickable(false);
                llWarehouseInfo.setClickable(false);

                startList();
            }
        }catch (Exception ex){
            Toast.makeText(this, "Exception \n"+ex, Toast.LENGTH_SHORT).show();
        }
    }
    // разобрать результат с сервера, список продуктов которые собраны для отправки и их колличество
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void splitResult(String result){
        acceptProductList.clear();
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
                    //int warehouse_id=0;
                    int warehouseInventory_id = Integer.parseInt(temp[0]);
                    int productInventory_id = Integer.parseInt(temp[1]);
                    int logistic_product = Integer.parseInt(temp[2]);
                    int checked = Integer.parseInt(temp[3]);

                    String category = temp[4];
                    String brand = temp[5];
                    String characteristic = temp[6];
                    String unit_measure = temp[7];
                    int weight_volume = Integer.parseInt(temp[8]);
                    String image_url = temp[9];
                    double quantity = Double.parseDouble(temp[10]);
                    String typePackaging = temp[11];
                    int quantityPackage = Integer.parseInt(temp[12]);
                    int car_id = Integer.parseInt(temp[13]);
                    int outWarehouse_id = Integer.parseInt(temp[14]);
                    int colorDelivery = Integer.parseInt(temp[15]);
                    int document_num = Integer.parseInt(temp[16]);
                    String product_name = temp[17];

                    CarrierPanelModel delivery = new CarrierPanelModel(
                            warehouseInventory_id,productInventory_id,
                            logistic_product,checked,category,brand,characteristic,
                            unit_measure,weight_volume, image_url,quantity,typePackaging
                            ,quantityPackage, car_id,outWarehouse_id,colorDelivery
                            ,document_num, document_name, product_name);

                    acceptProductList.add(delivery);
                }
            }
          //  Toast.makeText(this, "hi: "+acceptProductList.get(0).getBrand()+
          //          "\nid: "+acceptProductList.get(0).getCar_or_warehouse_id(), Toast.LENGTH_SHORT).show();
        }catch(Exception ex){
             Toast.makeText(this, "Exception: "+ex, Toast.LENGTH_SHORT).show();
        }
        for(int i=0;i < acceptProductList.size();i++){
            checkedList.add(acceptProductList.get(i).getChecked());
        }
        //отсортировать есть доставка или нет и добавить данные доставщика(склад это или транспорт)
        //сортируем лист по 4 полям (logistic_product )
        acceptProductList.sort(Comparator.comparing(CarrierPanelModel::getLogistic_product)
                                .thenComparing(CarrierPanelModel::getCar_id)
                                .thenComparing(CarrierPanelModel::getOutWarehouse_id)
                                .thenComparing(CarrierPanelModel::getDocument_num));//getCar_or_warehouse_id

        adapter.notifyDataSetChanged();
    }
     //сделать список строк для показа в ListView
    private void makeStringListForWarehouse(){
        warehouseStringList.clear();
        for(int i=0;i < warehousesInfoList.size();i++){
            String warehouse_info = warehousesInfoList.get(i).getWarehouse_info_id()+"/"+
                    warehousesInfoList.get(i).getWarehouse_id()+" "
                    +warehousesInfoList.get(i).getCity()+" "+warehousesInfoList.get(i).getStreet()+" "
                    +warehousesInfoList.get(i).getHouse();
            if(!warehousesInfoList.get(i).getBuilding().equals("")){
                warehouse_info += " "+BUILDING+" "+warehousesInfoList.get(i).getBuilding();
            }
            warehouseStringList.add(warehouse_info);
        }
      /*  if(warehousesInfoList.size() > 1){
            //adap_warehouse.notifyDataSetChanged();
            //adapLvWarehouse.notifyDataSetChanged();
        }else{
            warehouse_id = warehousesInfoList.get(0).getWarehouse_id();
            tvWarehouseInfo.setText(warehouseStringList.get(0));
            tvWarehouseInfo.setTextColor(TUBI_BLACK);
            tvWarehouseInfo.setClickable(false);*/

           // startList();
       // }
    }

}