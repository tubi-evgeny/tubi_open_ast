package ru.tubi.project.activity.logistics;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import ru.tubi.project.R;
import ru.tubi.project.adapters.DeliveryToReceiveGoodsAdapter;
import ru.tubi.project.models.CarModel;
import ru.tubi.project.models.CarrierPanelModel;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.utilites.InitialData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import ru.tubi.project.utilites.Constant;
import ru.tubi.project.utilites.UserDataRecovery;

import static ru.tubi.project.free.AllCollor.TUBI_BLACK;
import static ru.tubi.project.free.AllCollor.TUBI_GREEN_600;
import static ru.tubi.project.free.AllCollor.TUBI_GREY_200;
import static ru.tubi.project.free.AllCollor.TUBI_GREY_400;
import static ru.tubi.project.free.AllCollor.TUBI_WHITE;
import static ru.tubi.project.free.AllText.DELIVERY_TO_WAREHOUSE;
import static ru.tubi.project.free.AllText.LOAD_TEXT;

public class DeliveryToReceiveGoodsActivity extends AppCompatActivity
        implements View.OnClickListener {

    private RecyclerView recyclerView;
    private TextView tvCarInfo,  tvApply;//tvAcceptProd, tvHandOverProd,
    private ListView lvCars;
    private ArrayList<CarModel> carModelList = new ArrayList<>();
    private ArrayList<CarrierPanelModel> deliveryList = new ArrayList<>();
    private ArrayList<CarrierPanelModel> sortProdList = new ArrayList<>();
    private ArrayList<CarrierPanelModel> handOverProdList = new ArrayList<>();
    private ArrayList<String> carListString = new ArrayList<>();
    private ArrayAdapter adapLvCars;
    private DeliveryToReceiveGoodsAdapter adapter;
    private int car_id;
    private boolean accept, applyFlag=false;

    private UserModel userDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_to_receive_goods);
        setTitle(DELIVERY_TO_WAREHOUSE);//Доставка склад-склад

        recyclerView = (RecyclerView) findViewById(R.id.rvList);
        lvCars = findViewById(R.id.lvCars);
        tvCarInfo = findViewById(R.id.tvCarInfo);
       // tvAcceptProd = findViewById(R.id.tvAcceptProd);
       // tvHandOverProd = findViewById(R.id.tvHandOverProd);
        tvApply = findViewById(R.id.tvApply);

        tvCarInfo.setOnClickListener(this);
        //tvAcceptProd.setOnClickListener(this);
       // tvHandOverProd.setOnClickListener(this);
        tvApply.setOnClickListener(this);

        tvApply.setClickable(false);

        //--------------------
       // tvHandOverProd.setClickable(false); // кнопку удалить и все методы и классы тоже
                                            // (есть рабочая копия)
        //----------------

        //получить из sqlLite данные пользователя и компании
        UserDataRecovery userDataRecovery = new UserDataRecovery();
        userDataModel = userDataRecovery.getUserDataRecovery(this);

        startCarList();
        //startList();


        lvCars.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tvCarInfo.setText(carModelList.get(position).getCar_brand() + " " +
                        carModelList.get(position).getCar_model() + " "
                        + carModelList.get(position).getRegistration_num());
                tvCarInfo.setTextColor(TUBI_BLACK);
                lvCars.setVisibility(View.GONE);
                car_id = carModelList.get(position).getCar_id();

                writeCheckToTable();
                deliveryList.clear();
                sortProdList.clear();
                adapter.notifyDataSetChanged();
               // tvAcceptProd.setBackgroundColor(TUBI_WHITE);
               // tvHandOverProd.setBackgroundColor(TUBI_WHITE);
                startList();
                tvApply.setClickable(false);
                tvApply.setBackgroundColor(TUBI_GREY_200);
            }
        });
        DeliveryToReceiveGoodsAdapter.OnCheckedChangeListener checkedChangeListener =
                new DeliveryToReceiveGoodsAdapter.OnCheckedChangeListener() {
                    @Override
                    public void isChecked(View view, boolean flag, int position) {
                        whatCheckClicked(flag, position);
                        // Toast.makeText(DeliveryToWarehouseActivity.this,
                        //        "hi: "+position+"\n"+flag, Toast.LENGTH_SHORT).show();
                    }
                };
        adapLvCars = new ArrayAdapter(this, android.R.layout.simple_list_item_1, carListString);
        lvCars.setAdapter(adapLvCars);
        adapter = new DeliveryToReceiveGoodsAdapter(this, sortProdList, checkedChangeListener);
        recyclerView.setAdapter(adapter);
    }

    private void whatCheckClicked(boolean flag, int position) {
        int check = 0;
        if (flag) check = 1;
        sortProdList.get(position).setChecked(check);
        tvApply.setBackgroundColor(TUBI_GREEN_600);
        tvApply.setClickable(true);
        //Toast.makeText(this, "check: \n" + check, Toast.LENGTH_SHORT).show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        if (v.equals(tvCarInfo)) {
            lvCars.setVisibility(View.VISIBLE);
        } /*else if (v.equals(tvAcceptProd)) {
            writeCheckToTable();
            accept = true;
            makeAcceptOrHandOverList("accept");
            tvAcceptProd.setBackgroundColor(TUBI_GREEN_600);
            tvHandOverProd.setBackgroundColor(TUBI_GREY_400);
            tvApply.setClickable(false);
            tvApply.setBackgroundColor(TUBI_GREY_200);
        } else if (v.equals(tvHandOverProd)) {
            writeCheckToTable();
            accept = false;
            makeAcceptOrHandOverList("hand_over");
            tvAcceptProd.setBackgroundColor(TUBI_GREY_400);
            tvHandOverProd.setBackgroundColor(TUBI_GREEN_600);
            tvApply.setClickable(false);
            tvApply.setBackgroundColor(TUBI_GREY_200);
        } */
        else if (v.equals(tvApply)) {
            writeCheckToTable();
            tvApply.setClickable(false);
            tvApply.setBackgroundColor(TUBI_GREY_200);
            //Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show();
        }

    }

    //собрать список в соответствии с запросом или (принять товар) или (сдать товар)
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void makeAcceptOrHandOverList() {
        sortProdList.clear();
        //if (string.equals("accept")) {
            for (int i = 0; i < deliveryList.size(); i++) {
                if (deliveryList.get(i).getCheck_give_out() == 0) {
                    //Toast.makeText(this, "give: ", Toast.LENGTH_SHORT).show();
               // }
                    CarrierPanelModel delivery = new CarrierPanelModel(
                        deliveryList.get(i).getOutWarehouse_id(), deliveryList.get(i).getOutCity(),
                        deliveryList.get(i).getOutStreet(), deliveryList.get(i).getOutHouse(),
                        deliveryList.get(i).getOutBuilding(),
                        deliveryList.get(i).getWarehouseInventory_id(),
                        deliveryList.get(i).getCategory(), deliveryList.get(i).getBrand(),
                        deliveryList.get(i).getCharacteristic(), deliveryList.get(i).getUnit_measure(),
                        deliveryList.get(i).getWeight_volume(), deliveryList.get(i).getTypePackaging(),
                        deliveryList.get(i).getQuantityPackage(), deliveryList.get(i).getImage_url(),
                        deliveryList.get(i).getQuantity()
                            , deliveryList.get(i).getCheck_take_in(),
                        deliveryList.get(i).getCheck_out_active(),
                        deliveryList.get(i).getOutWarehouse_info_id()
                            ,deliveryList.get(i).getDocument_num()
                            ,deliveryList.get(i).getDocument_closed()
                            ,deliveryList.get(i).getDocument_save()
                            ,deliveryList.get(i).getInvoice_key_id());

                    sortProdList.add(delivery);
                }
            }
       // }
       /* if (string.equals("hand_over")) {
            for (int i = 0; i < deliveryList.size(); i++) {
                if (deliveryList.get(i).getCheck_take_in() == 1) {

                    CarrierPanelModel delivery = new CarrierPanelModel(
                            deliveryList.get(i).getInWarehouse_id(), deliveryList.get(i).getInCity(),
                            deliveryList.get(i).getInStreet(), deliveryList.get(i).getInHouse(),
                            deliveryList.get(i).getInBuilding(),
                            deliveryList.get(i).getWarehouseInventory_id(),
                            deliveryList.get(i).getCategory(), deliveryList.get(i).getBrand(),
                            deliveryList.get(i).getCharacteristic(), deliveryList.get(i).getUnit_measure(),
                            deliveryList.get(i).getWeight_volume(), deliveryList.get(i).getTypePackaging(),
                            deliveryList.get(i).getQuantityPackage(), deliveryList.get(i).getImage_url(),
                            deliveryList.get(i).getQuantity(), deliveryList.get(i).getCheck_give_out(),
                            0,deliveryList.get(i).getInWarehouse_info_id()
                            ,deliveryList.get(i).getDocument_num()
                            ,deliveryList.get(i).getDocument_closed()
                            ,deliveryList.get(i).getDocument_save()
                            ,deliveryList.get(i).getInvoice_key_id());//deliveryList.get(i).getCheck_out_active()

                    sortProdList.add(delivery);
                }
            }
        }*/
        Log.d("A111","sortProdList size="+sortProdList.size());
        //сортируем лист по 2 полям(warehouse_id, getDocument_num )
        sortProdList.sort(Comparator.comparing(CarrierPanelModel::getWarehouse_id)
                .thenComparing(CarrierPanelModel::getDocument_num));
        adapter.notifyDataSetChanged();
    }

    //записать галочки товаров в таблицу DB и в deliveryList
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void writeCheckToTable() {
       // if (accept){
            writeCheckToDeliveryList();

            for (int i = 0; i < sortProdList.size(); i++) {
                int check = sortProdList.get(i).getChecked();
                if (check == 1) {
                    int warehouse_inventory_id = sortProdList.get(i).getWarehouseInventory_id();

                    writeCheckToLogisticTable(warehouse_inventory_id, check);
                }
            }
        adapter.notifyDataSetChanged();
        //получить ключи документов проверить все товары получены,
        // закрыть ключ к документам и обновить адаптер
        checkAllInvoiceKeyToClose();
    }

    //записать галочки товаров в deliveryList
    private void writeCheckToDeliveryList() {
        if (accept) {
            for (int i = 0; i < sortProdList.size(); i++) {
                if (sortProdList.get(i).getChecked() == 1) {
                    for (int j = 0; j < deliveryList.size(); j++) {
                        if (sortProdList.get(i).getWarehouseInventory_id() ==
                                deliveryList.get(j).getWarehouseInventory_id()) {
                            deliveryList.get(j).setCheck_take_in(1);
                        }
                    }
                }
            }
        }else if(accept == false){
            for (int i = 0; i < sortProdList.size(); i++) {
                if (sortProdList.get(i).getChecked() == 1) {
                    for (int j = 0; j < deliveryList.size(); j++) {
                        if (sortProdList.get(i).getWarehouseInventory_id() ==
                                deliveryList.get(j).getWarehouseInventory_id()) {
                            deliveryList.get(j).setCheck_give_out(1);
                        }
                        //Toast.makeText(this, "test: "+deliveryList.get(j).getCheck_give_out(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

    }
    //получить ключи документов проверить все ли товары получены,
    //если все то закрыть ключ к документам и обновить адаптер
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void checkAllInvoiceKeyToClose(){
        ArrayList<Integer> keyList = new ArrayList<>();
        for(int i=0;i < deliveryList.size();i++){
            keyList.add(deliveryList.get(i).getInvoice_key_id());
        }
        //убрать дубликаты
        Set<Integer> set = new HashSet<>(keyList);
        keyList.clear();
        keyList.addAll(set);
        //String test = "test \n";
        for(int i=0;i < keyList.size();i++){
            String url = Constant.CARRIER_OFFICE;
            url += "check_invoice_key_to_write_close";//CARRIER_OFFICE
            url += "&"+"invoice_key_id="+keyList.get(i);
            String whatQuestion = "check_invoice_key_to_write_close";
            setInitialData(url, whatQuestion);
            //test += url+" \n ";
        }
       // Toast.makeText(this, "hello\n"+test, Toast.LENGTH_LONG).show();
        //обновить данные
        applyFlag = true;
        startList();

    }
    //записать галочку в таблицу логистики и склада
    private void writeCheckToLogisticTable(int warehouse_inventory_id, int check){
        String url = Constant.CARRIER_OFFICE;
        String whatQuestion="";
        if (accept) {
            url += "write_check_to_logistic_table";
            url += "&" + "warehouse_inventory_id=" + warehouse_inventory_id;
            url += "&" + "check=" + check;
            whatQuestion = "write_check_to_logistic_table";
        }else if(accept == false){
            url += "write_check_give_out_to_logistic_table";
            url += "&" + "warehouse_inventory_id=" + warehouse_inventory_id;
            url += "&" + "check=" + check;
            whatQuestion = "write_check_give_out_to_logistic_table";
        }

        setInitialData(url, whatQuestion);

    }
    //получить список автомобилей user
    private void startCarList(){
        String url = Constant.CARRIER_OFFICE;
        url += "receive_list_cars_user";
        //url += "&"+"user_uid="+MY_UID;//
        url += "&"+"taxpayer_id="+userDataModel.getCompany_tax_id();//MY_UID;
        String whatQuestion = "receive_list_cars_user";
       // Toast.makeText(this, "tax id: "+url, Toast.LENGTH_SHORT).show();
        setInitialData(url, whatQuestion);
    }
    //получить все склады и собранные товары warhouse_inventory_id дла погрузки и доставки
    private void startList() {
        String url = Constant.CARRIER_OFFICE;
        url += "receive_list_warehouse_and_product_for_delivery";
        url += "&"+"car_id="+car_id;
        String whatQuestion = "receive_list_warehouse_and_product_for_delivery";
        setInitialData(url, whatQuestion);
    }
    private void setInitialData(String url_get, String whatQuestion) {
         ProgressDialog asyncDialog = new ProgressDialog(this);

        InitialData task=new InitialData(){
            @Override
            protected void onPreExecute() {
                //set message of the dialog
                asyncDialog.setMessage(LOAD_TEXT);
                //show dialog
                asyncDialog.show();
                super.onPreExecute();
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            protected void onPostExecute(String result) {
                //Do your thing
                if(whatQuestion.equals("receive_list_cars_user")){
                    splitCarResult(result);
                }else if(whatQuestion.equals("receive_list_warehouse_and_product_for_delivery")){
                    splitResult(result);
                   // Toast.makeText(DeliveryToWarehouseActivity.this,
                   //         "res: "+result, Toast.LENGTH_SHORT).show();
                }
                //hide the dialog
                 asyncDialog.dismiss();
            }
        };
        task.execute(url_get);
    }
    //разобрать список авто user
    private void splitCarResult(String result){
        //carModelList.clear();
        try{
            String[] res = result.split("<br>");
            String[] one_temp = res[0].split("&nbsp");
            if (one_temp[0].equals("error") || one_temp[0].equals("messege")) {
                Toast.makeText(this, "" + one_temp[1], Toast.LENGTH_LONG).show();
                return;
            } else {
                for (int i = 0; i < res.length; i++) {
                    String[] temp = res[i].split("&nbsp");
                    int car_id=Integer.parseInt(temp[0]);
                    String car_brand=temp[1];
                    String car_model=temp[2];
                    String registration_num=temp[3];
                    CarModel car = new CarModel(car_id, car_brand, car_model, registration_num);

                    carModelList.add(car);
                }
            }
            //сделать список строк для показа в ListView
            makeStringListForCars();
        }catch(Exception ex){
            Toast.makeText(this, "cars is not\nException: "+ex, Toast.LENGTH_SHORT).show();
        }
        //сделать список строк для показа в ListView
       // makeStringListForCars();
    }
    // разобрать результат с сервера, список продуктов которые собраны для
    // загрузки-разгрузки(отправки) и их колличество
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void splitResult(String result){
        deliveryList.clear();
        String out_active="";
        try{
            String[] res = result.split("<br>");
            String[] one_temp = res[0].split("&nbsp");
            if (one_temp[0].equals("error") || one_temp[0].equals("messege")) {
                Toast.makeText(this, "" + one_temp[1], Toast.LENGTH_LONG).show();
                return;
            } else {
                for (int i = 0; i < res.length; i++) {
                    String[] temp = res[i].split("&nbsp");
                    int outWarehouse_id=Integer.parseInt(temp[0]);
                    String outCity=temp[1];
                    String outStreet=temp[2];
                    int outHouse=Integer.parseInt(temp[3]);
                    String outBuilding="";
                    try { outBuilding=temp[4]; }catch (Exception ex){     }

                    int inWarehouse_id=Integer.parseInt(temp[5]);
                    String inCity=temp[6];
                    String inStreet=temp[7];
                    int inHouse=Integer.parseInt(temp[8]);
                    String inBuilding="";
                    try { inBuilding=temp[9]; }catch (Exception ex){     }

                    int warehouseInventory_id = Integer.parseInt(temp[10]);
                    String category = temp[11];
                    String brand = temp[12];
                    String characteristic = temp[13];
                    String unit_measure = temp[14];
                    int weight_volume = Integer.parseInt(temp[15]);
                    String image_url = temp[16];
                    double quantity = Double.parseDouble(temp[17]);
                    String typePackaging = temp[18];
                    int quantityPackage = Integer.parseInt(temp[19]);
                    int check_take_in=Integer.parseInt(temp[20]);
                    int check_give_out=Integer.parseInt(temp[21]);
                    int check_out_active=Integer.parseInt(temp[22]);
                    int outWarehouse_info_id=Integer.parseInt(temp[23]);
                    int inWarehouse_info_id=Integer.parseInt(temp[24]);
                    int document_num=Integer.parseInt(temp[25]);
                    int document_closed=Integer.parseInt(temp[26]);
                    int document_save=Integer.parseInt(temp[27]);
                    int invoice_key_id=Integer.parseInt(temp[28]);

                    CarrierPanelModel delivery = new CarrierPanelModel(outWarehouse_id,outCity,outStreet,
                            outHouse,outBuilding,inWarehouse_id,inCity,inStreet,inHouse,inBuilding,
                            warehouseInventory_id,category,brand,characteristic,unit_measure,
                            weight_volume,image_url,quantity,typePackaging,quantityPackage,
                            check_take_in, check_give_out,check_out_active,outWarehouse_info_id,
                            inWarehouse_info_id,document_num, document_closed, document_save
                            ,invoice_key_id);
                    //$outWarehouse_info_id."&nbsp".$inWarehouse_info_id.

                    out_active += " "+delivery.getCheck_out_active()+"\n";

                    deliveryList.add(delivery);
                }
            }
            //обновить данные
           /* if(applyFlag){
                if(accept){
                    makeAcceptOrHandOverList("accept");
                }else{
                    makeAcceptOrHandOverList("hand_over");
                }
            }
            applyFlag = false;*/
            makeAcceptOrHandOverList();
            /*
            //сортируем лист по 2 полям (logistic_product и car_or_warehouse_id)
            deliveryList.sort(Comparator.comparing(CarrierPanelModel::getDocument_num)
                    .thenComparing(CarrierPanelModel::getCounterparty));
             */
        }catch(Exception ex){
             Toast.makeText(this, "Exception: "+ex, Toast.LENGTH_SHORT).show();
        }
        Log.d("A111","DeliveryToReceiveGoodsActivity / splitResult res="+result);
        //Toast.makeText(this, "out: "+out_active, Toast.LENGTH_SHORT).show();
    }
    //сделать список строк для показа в ListView
    private void makeStringListForCars(){
        for (int i = 0; i < carModelList.size(); i++) {
            carListString.add(carModelList.get(i).getCar_brand() + " " + carModelList.get(i).getCar_model()
                     + "  " + carModelList.get(i).getRegistration_num());
        }
        if(carModelList.size() > 1) {
            adapLvCars.notifyDataSetChanged();
        }else{
            car_id = carModelList.get(0).getCar_id();
            tvCarInfo.setText(carListString.get(0));
            tvCarInfo.setTextColor(TUBI_BLACK);
            tvCarInfo.setClickable(false);

            startList();
        }
    }
}