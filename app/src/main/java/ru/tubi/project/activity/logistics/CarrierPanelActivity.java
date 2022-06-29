package ru.tubi.project.activity.logistics;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import ru.tubi.project.R;
import ru.tubi.project.adapters.CarrierPanelAdapter;
import ru.tubi.project.models.CarModel;
import ru.tubi.project.models.CarrierPanelModel;
import ru.tubi.project.utilites.InitialData;

import java.util.ArrayList;
import java.util.Comparator;

import ru.tubi.project.utilites.Constant;

import static ru.tubi.project.Config.MY_UID;
import static ru.tubi.project.free.AllCollor.TUBI_GREEN_600;
import static ru.tubi.project.free.AllCollor.TUBI_GREY_200;
import static ru.tubi.project.free.AllText.ALL_GOODS_TEXT;
import static ru.tubi.project.free.AllText.CARGO_WEGHT;
import static ru.tubi.project.free.AllText.CHOICE_CAR;
import static ru.tubi.project.free.AllText.DATA_RECORDER;
import static ru.tubi.project.free.AllText.KG;
import static ru.tubi.project.free.AllText.LOAD_TEXT;

public class CarrierPanelActivity extends AppCompatActivity implements View.OnClickListener  {

    private Intent intent, takeit;
    private ImageView ivFilter;
    private RecyclerView recyclerView;
    private Spinner spinnerTransport;
    private TextView tvGeneralWeght,tvApply, tvTotalWeight;
    private CarrierPanelAdapter adapter;
    private ArrayList<CarrierPanelModel> deliveryList=new ArrayList<>();
    private ArrayList<CarrierPanelModel> fullDeliveryList=new ArrayList<>();
    private ArrayList<CarrierPanelModel> outWarehouseList=new ArrayList<>();
    private ArrayList<CarrierPanelModel> inWarehouseList=new ArrayList<>();
    private ArrayList<CarrierPanelModel> carDeliveryList=new ArrayList<>();
    private ArrayList<String> carList = new ArrayList<>();
    private ArrayList<CarModel> carModelList = new ArrayList<>();
    private ArrayList<Integer> checkMakeNullList = new ArrayList<>();
    private ArrayAdapter adapSpiner;
    private int car_id=0;
    double generalWeght=0;
    private boolean temperStandart =false, temperCold= false,
            temperFrost=false, carSelectFlag=false;

    static final int REQUEST_FILTER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrier_panel);
        setTitle(getResources().getString(R.string.carrier_panel));//Панель перевозчика

        recyclerView=(RecyclerView)findViewById(R.id.rvList);
        ivFilter = findViewById(R.id.ivFilter);
        spinnerTransport = findViewById(R.id.spinnerTransport);
        tvGeneralWeght = findViewById(R.id.tvGeneralWeght);
        tvApply = findViewById(R.id.tvApply);
        tvTotalWeight = findViewById(R.id.tvTotalWeight);

        ivFilter.setOnClickListener(this);
        tvApply.setOnClickListener(this);


        startList();
        carsList();
        spinnerTransport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.RED);
                    tvApply.setClickable(false);
                    tvApply.setBackgroundColor(TUBI_GREY_200);
                    carSelectFlag=false;
                }else{
                    ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
                    carSelectFlag=true;
                    //tvApply.setClickable(true);
                   // tvApply.setBackgroundColor(TUBI_GREEN_600);
                }
                if(position != 0){
                    car_id = carModelList.get(position-1).getCar_id();
                    Log.d("A111","CarrierPanelActivity / onCreate car_id="+car_id);
                    //обновить (получить) все собранные warhouse_inventory_id но не отправленные и показать их
                    startList();
                    //получить список товаров для транспорта которые добавлены в доставку t_logistic_product
                    receiveDeliveryListForCar(car_id);
                    //получить вес товаров для транспорта которые уже добавлены в доставку t_logistic_product
                    receiveProductWeigthForCar(car_id);
                }else{
                    //обновить (получить) все собранные warhouse_inventory_id но не отправленные и показать их
                    startList();
                    tvGeneralWeght.setText(""+CARGO_WEGHT);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {            }
        });
        CarrierPanelAdapter.OnCheckedChangeListener checkedChangeListener =
                new CarrierPanelAdapter.OnCheckedChangeListener() {
                    @Override
                    public void isChecked(View view, boolean flag, int position) {
                        whatCheckedClicked(flag, position);
                        if(carSelectFlag){
                            tvApply.setClickable(true);
                            tvApply.setBackgroundColor(TUBI_GREEN_600);
                        }
                    }
                };
        adapter=new CarrierPanelAdapter(this,deliveryList, checkedChangeListener);
        recyclerView.setAdapter(adapter);
        adapSpiner=new ArrayAdapter(this,
                android.R.layout.simple_spinner_item,carList);
        spinnerTransport.setAdapter(adapSpiner);
    }
    @Override
    public void onClick(View v) {

        if(v.equals(ivFilter)){
            // makeWarehouseListForNextActivity();
            intent = new Intent(this,CarrierPanelFilterActivity.class);
            //intent.putExtra("deliveryList",deliveryList);
            intent.putExtra("outWarehouseList",outWarehouseList);
            intent.putExtra("inWarehouseList",inWarehouseList);
            intent.putExtra("temperStandart",temperStandart);
            intent.putExtra("temperCold",temperCold);
            intent.putExtra("temperFrost",temperFrost);
            startActivityForResult(intent,REQUEST_FILTER);
        }
        else if (v.equals(tvApply)) {//применить
            //изменить статус outActive=1, warehouse_inventory_in_out
            //и добавить запись о товаре в доставку t_logistic_product
            for(int i=0;i < deliveryList.size();i++){
                if(deliveryList.get(i).getChecked() == 1){
                    CarrierPanelModel checkInfo = deliveryList.get(i);
                    //записать товары и машину в таблицу доставки t_logistic_product
                    recordGoodsAndCarForDelivery(checkInfo);
                }
            }
            //проверить и удалить все исправленные на check=0; брони товаров из БД
            checkMakeNullList = removeDuplicates(checkMakeNullList);
            for(int i=0;i < deliveryList.size();i++){
                for(int j=0;j < checkMakeNullList.size();j++){
                    if(deliveryList.get(i).getWarehouseInventory_id() == checkMakeNullList.get(j)){
                        if(deliveryList.get(i).getChecked() == 0){
                            int warehouseInventory_id=checkMakeNullList.get(j);
                            //удалить все исправленные на check=0; брони товаров из БД
                            checkAndUpdateLogisticProduct(warehouseInventory_id);
                        }
                    }
                }
            }
            tvApply.setClickable(false);
            tvApply.setBackgroundColor(TUBI_GREY_200);
            Toast.makeText(this, ""+DATA_RECORDER, Toast.LENGTH_SHORT).show();
        }
        //inList = removeDuplicates(inTempo);
    }
    //
    private void whatCheckedClicked(boolean flag, int position){
        double weght=(deliveryList.get(position).getProductWeight()
                * deliveryList.get(position).getQuantity()) / 1000;
        //добавить вес к весу из textView tvGeneralWeght
        //изменить статус check=1 or =0
        if(flag){
            generalWeght= generalWeght + weght;
            deliveryList.get(position).setChecked(1);
        }else{
            generalWeght= generalWeght - weght;
            if(generalWeght < 0){  generalWeght = 0;  }
            deliveryList.get(position).setChecked(0);
            checkMakeNullList.add(deliveryList.get(position).getWarehouseInventory_id());
        }
        tvGeneralWeght.setText(""+CARGO_WEGHT+"\n"+String.format("%.2f",generalWeght));

        //Toast.makeText(this, "check:\n"+deliveryList.get(position).getChecked(), Toast.LENGTH_SHORT).show();
    }
    //сложить вес товаров и показать
    private void  addUpTotalWeight(ArrayList<CarrierPanelModel> list){
        double totalWeight = 0;
        for(int i=0;i < list.size();i++){
            totalWeight += (list.get(i).getProductWeight() * list.get(i).getQuantity()) / 1000;
        }
        tvTotalWeight.setText(ALL_GOODS_TEXT+" "+String.format("%.3f",totalWeight)+" "+KG);
    }
    //удалить все исправленные на check=0; брони товаров из БД
    private void checkAndUpdateLogisticProduct(int warehouseInventory_id){
        String url = Constant.CARRIER_OFFICE;
        url += "update_and_delete_logistic_product";
        url += "&"+"warehouseInventory_id="+warehouseInventory_id;
        String whatQuestion = "update_and_delete_logistic_product";
        setInitialData(url, whatQuestion);
    }
    //получить вес товаров для транспорта которые уже добавлены в доставку t_logistic_product
    private void receiveProductWeigthForCar(int car_id){
        String url = Constant.CARRIER_OFFICE;
        url += "receive_product_weight_for_car";
        url += "&"+"car_id="+car_id;
        String whatQuestion = "receive_product_weight_for_car";
        setInitialData(url, whatQuestion);
    }
    //получить список товаров для транспорта которые добавлены в доставку t_logistic_product
    private void receiveDeliveryListForCar(int car_id){
        String url = Constant.CARRIER_OFFICE;
        url += "receive_list_delivery_for_car";
        url += "&"+"car_id="+car_id;
        String whatQuestion = "receive_list_delivery_for_car";
        setInitialData(url, whatQuestion);
    }
    //записать товары и машину в таблицу доставки t_logistic_product
    private void recordGoodsAndCarForDelivery(CarrierPanelModel checkInfo){
        int logistic_product_id = checkInfo.getWarehouseInventory_id();
        String url = Constant.CARRIER_OFFICE;
        url += "record_goods_and_car_for_delivery";
        url += "&"+"user_uid="+MY_UID;
        url += "&"+"car_id="+car_id;
        url += "&"+"logistic_product_id="+logistic_product_id;
        String whatQuestion = "record_goods_and_car_for_delivery";
        setInitialData(url, whatQuestion);
    }
    //получить все собранные warhouse_inventory_id но не отправленные и показать их
    private void startList() {
        String url = Constant.CARRIER_OFFICE;
        url += "receive_list_delivery";
        String whatQuestion = "receive_list_delivery";
        setInitialData(url, whatQuestion);
    }
    //получить все авто
    private void carsList() {
        String url = Constant.CARRIER_OFFICE;
        url += "receive_list_cars";
        String whatQuestion = "receive_list_cars";
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
                if(whatQuestion.equals("receive_list_delivery")){
                    splitResult(result);
                }else if(whatQuestion.equals("receive_list_delivery_for_car")){
                    splitResultDliveryForCars(result);
                   // Toast.makeText(CarrierPanelActivity.this,
                       //     "res: "+result, Toast.LENGTH_SHORT).show();
                }else if(whatQuestion.equals("receive_list_cars")){
                    splitResultCars(result);
                   //  Toast.makeText(CarrierPanelActivity.this,
                     //     "res: "+result, Toast.LENGTH_SHORT).show();
                }else if(whatQuestion.equals("receive_product_weight_for_car")){
                    splitProductWeightForCarResult(result);
                }
                //hide the dialog
                 asyncDialog.dismiss();
            }
        };
        task.execute(url_get);
    }
    //разобрать вес товара который уже распределен в авто
    private void splitProductWeightForCarResult(String result){
        generalWeght=Double.parseDouble(result);
        tvGeneralWeght.setText(""+CARGO_WEGHT+"\n"+String.format("%.2f",generalWeght));
        Toast.makeText(this, "res\n"+result, Toast.LENGTH_SHORT).show();
    }
    private void splitResultCars(String result){
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
                    int max_cargo_weght=Integer.parseInt(temp[4]);
                    CarModel car = new CarModel(car_id, car_brand, car_model, registration_num,
                            max_cargo_weght);

                    carModelList.add(car);
                }
            }
        }catch(Exception ex){
            Toast.makeText(this, "Exception: "+ex, Toast.LENGTH_SHORT).show();
        }
        //сделать список строк для показа в spinner
        makeStringListForCars();
    }
    // разобрать результат с сервера, список продуктов для которых забронированна машина
    // и их колличество
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void splitResultDliveryForCars(String result){
        carDeliveryList.clear();
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

                    double productVolume=Double.parseDouble(temp[10]);
                    double productWeight=Double.parseDouble(temp[11]);
                    String storageTemperature=temp[12];
                    int warehouseInventory_id = Integer.parseInt(temp[13]);
                    int checked=1;
                    CarrierPanelModel delivery = new CarrierPanelModel(outWarehouse_id,outCity,outStreet,
                            outHouse,outBuilding,inWarehouse_id,inCity,inStreet,inHouse,inBuilding,
                            productVolume,productWeight,storageTemperature,warehouseInventory_id,checked);

                    carDeliveryList.add(delivery);
                }
            }
        }catch(Exception ex){
           // Toast.makeText(this, "Exception: "+ex, Toast.LENGTH_SHORT).show();
        }
        //сортируем лист по 2 полям (partner_warehouse_id и Product_inventory_id)
        carDeliveryList.sort(Comparator.comparing(CarrierPanelModel::getOutWarehouse_id)
                .thenComparing(CarrierPanelModel::getInWarehouse_id));
        //соединяем список товаров забронированных в авто и свободных к брони
        makePutAndFreeProductCar();
    }
    // разобрать результат с сервера, список продуктов которые собраны для отправки и их колличество
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void splitResult(String result){
        //Log.d("A111","CarrierPanelActivity / splitResult \n"+result);
        deliveryList.clear();
        fullDeliveryList.clear();
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

                    double productVolume=Double.parseDouble(temp[10]);
                     double productWeight=Double.parseDouble(temp[11]);
                     String storageTemperature=temp[12];
                    int warehouseInventory_id = Integer.parseInt(temp[13]);
                    int outWarehouse_info_id = Integer.parseInt(temp[14]);
                    int inWarehouse_info_id = Integer.parseInt(temp[15]);
                     int checked=0;
                    CarrierPanelModel delivery = new CarrierPanelModel(outWarehouse_id,outCity,outStreet,
                            outHouse,outBuilding,inWarehouse_id,inCity,inStreet,inHouse,inBuilding,
                            productVolume,productWeight,storageTemperature,warehouseInventory_id,
                            outWarehouse_info_id,inWarehouse_info_id,checked);
                   deliveryList.add(delivery);
                }
            }
        }catch(Exception ex){
            Log.d("A111","error CarrierPanelActivity / splitResult \n"
                    +ex.toString()+"\n"+result);
           // Toast.makeText(this, "Exception: "+ex, Toast.LENGTH_SHORT).show();
        }
       // Toast.makeText(this, "count: "+deliveryList.size(), Toast.LENGTH_SHORT).show();
        sortStartList();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sortStartList(){
        //сортируем лист по 2 полям (partner_warehouse_id и Product_inventory_id)
        deliveryList.sort(Comparator.comparing(CarrierPanelModel::getOutWarehouse_id)
                .thenComparing(CarrierPanelModel::getInWarehouse_id));

        fullDeliveryList.addAll(deliveryList);

        //list for filter
        makeWarehouseListForNextActivity();

        //сложить вес товаров и показать
        addUpTotalWeight(deliveryList);

        adapter.notifyDataSetChanged();
    }
    private void makeStringListForCars(){
        carList.add(CHOICE_CAR);
        for(int i=0;i<carModelList.size();i++){
            carList.add(carModelList.get(i).getCar_brand()+" "+carModelList.get(i).getCar_model()
                    +" \n"+carModelList.get(i).getRegistration_num()+" "+carModelList.get(i).getMax_cargo_weght());
        }
        adapSpiner.notifyDataSetChanged();
    }
    //соединяем список товаров забронированных в авто и свободных к брони
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void makePutAndFreeProductCar(){
           deliveryList.clear();
           deliveryList.addAll(carDeliveryList);
           deliveryList.addAll(fullDeliveryList);
        //сортируем лист по 2 полям (partner_warehouse_id и Product_inventory_id)
        deliveryList.sort(Comparator.comparing(CarrierPanelModel::getOutWarehouse_id)
                .thenComparing(CarrierPanelModel::getInWarehouse_id));
        adapter.notifyDataSetChanged();
        //посчитать вес забронированного товара и вывести в tvGeneralWeght
        calculateWeghtCheckedProduct();

        //сложить вес товаров и показать
        addUpTotalWeight(deliveryList);
    }
    //посчитать вес забронированного товара и вывести в tvGeneralWeght
    private void calculateWeghtCheckedProduct(){
        generalWeght = 0;
        for(int i=0;i < deliveryList.size();i++){
            if(deliveryList.get(i).getChecked() == 1){
                generalWeght += (deliveryList.get(i).getProductWeight() *
                        deliveryList.get(i).getQuantity()) / 1000;
            }
        }
        tvGeneralWeght.setText(""+CARGO_WEGHT+"\n"+String.format("%.2f",generalWeght));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //ArrayList<CarrierPanelModel> oneList = new ArrayList<>();
        if(requestCode == REQUEST_FILTER && resultCode == RESULT_OK  && data != null){//&& data != null
            outWarehouseList.clear();
            outWarehouseList = (ArrayList<CarrierPanelModel>)
                    data.getSerializableExtra("outWarehouseList");
            inWarehouseList.clear();
            inWarehouseList = (ArrayList<CarrierPanelModel>)
                    data.getSerializableExtra("inWarehouseList");
            temperStandart=data.getBooleanExtra("temperStandart",false);
            temperCold=data.getBooleanExtra("temperCold",false);
            temperFrost=data.getBooleanExtra("temperFrost",false);

            filterWarehouseList();
        }
    }
    //фильтруем список по условиям из фильтра
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void filterWarehouseList(){
        boolean outFlag=false, inFlag=false, temperFlag = false;
        for(int i=0;i < outWarehouseList.size();i++){
            if(outWarehouseList.get(i).getChecked() == 1){
                outFlag=true;
                i = outWarehouseList.size();
            }
        }
        for(int i=0;i < inWarehouseList.size();i++){
            if(inWarehouseList.get(i).getChecked() == 1){
                inFlag=true;
               // Toast.makeText(this, "in: "+i, Toast.LENGTH_SHORT).show();
                i = inWarehouseList.size();
            }
        }
        if (temperStandart || temperCold || temperFrost){
            temperFlag = true;
        }

        filterWarehouseList(outFlag, inFlag, temperFlag);
       //filterOutWarehouseList();
    }
    //фильтруем список по условиям из фильтра
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void filterWarehouseList(boolean outFlag, boolean inFlag, boolean temperFlag){
        deliveryList.clear();
        deliveryList.addAll(fullDeliveryList);
        ArrayList<CarrierPanelModel> list=new ArrayList<>();
        if(outFlag){
            for(int i=0;i < outWarehouseList.size();i++){
                if(outWarehouseList.get(i).getChecked() == 1){
                    for(int j=0;j < deliveryList.size();j++){
                        if(outWarehouseList.get(i).getWarehouse_id() ==
                                deliveryList.get(j).getOutWarehouse_id()){
                            list.add(deliveryList.get(j));
                        }
                    }
                }
            }
            deliveryList.clear();
            deliveryList.addAll(list);
            list.clear();
        }
        if(inFlag){
            for(int i=0;i < inWarehouseList.size();i++){
                if(inWarehouseList.get(i).getChecked() == 1){
                    for(int j=0;j < deliveryList.size();j++){
                        if(inWarehouseList.get(i).getWarehouse_id() ==
                                deliveryList.get(j).getInWarehouse_id()){
                            list.add(deliveryList.get(j));
                        }
                    }
                }
            }
            deliveryList.clear();
            deliveryList.addAll(list);
        }
        if(temperFlag){
            list.clear();
            for(int i=0;i < deliveryList.size();i++){
                String temperature = deliveryList.get(i).getStorageTemperature();
                if(temperStandart && temperature.equals("обычное")){
                    list.add(deliveryList.get(i));
                }
                if(temperCold && temperature.equals("холод")){
                    list.add(deliveryList.get(i));
                }
                if(temperFrost && temperature.equals("мороз")){
                    list.add(deliveryList.get(i));
                }
            }
            deliveryList.clear();
            deliveryList.addAll(list);
        }
        deliveryList.addAll(carDeliveryList);
        //сортируем лист по 2 полям (partner_warehouse_id и Product_inventory_id)
        deliveryList.sort(Comparator.comparing(CarrierPanelModel::getOutWarehouse_id)
                .thenComparing(CarrierPanelModel::getInWarehouse_id));
        adapter.notifyDataSetChanged();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void makeWarehouseListForNextActivity(){
        outWarehouseList.clear();
        inWarehouseList.clear();
        ArrayList <Integer> outTempo = new ArrayList<>();
        ArrayList <Integer> outList = new ArrayList<>();
        ArrayList <Integer> inTempo = new ArrayList<>();
        ArrayList <Integer> inList = new ArrayList<>();
        for(int i=0;i < deliveryList.size();i++){
            outTempo.add(deliveryList.get(i).getOutWarehouse_id());
            inTempo.add(deliveryList.get(i).getInWarehouse_id());
        }
        outList = removeDuplicates(outTempo);
       // inList = removeDuplicates(inTempo);
        for(int j=0;j<outList.size();j++){
            for(int z=0;z<deliveryList.size();z++){
                if(outList.get(j) == deliveryList.get(z).getOutWarehouse_id()){
                    CarrierPanelModel warehouse = new CarrierPanelModel(
                            deliveryList.get(z).getOutWarehouse_id(),
                            deliveryList.get(z).getOutCity() ,deliveryList.get(z).getOutStreet(),
                            deliveryList.get(z).getOutHouse(),
                            deliveryList.get(z).getOutBuilding(),
                            deliveryList.get(z).getOutWarehouse_info_id(),0);
                    outWarehouseList.add(warehouse);
                    z=deliveryList.size();
                }
            }
        }
        inList = removeDuplicates(inTempo);
        for(int j=0;j<inList.size();j++){
            for(int z=0;z<deliveryList.size();z++){
                if(inList.get(j) == deliveryList.get(z).getInWarehouse_id()){
                    CarrierPanelModel warehouse = new CarrierPanelModel(
                            deliveryList.get(z).getInWarehouse_id(),
                            deliveryList.get(z).getInCity() ,deliveryList.get(z).getInStreet(),
                            deliveryList.get(z).getInHouse(),
                            deliveryList.get(z).getInBuilding(),
                            deliveryList.get(z).getInWarehouse_info_id(), 0);
                    inWarehouseList.add(warehouse);
                    z=deliveryList.size();
                }
            }
        }
        //сортируем лист по 2 полям (partner_warehouse_id и Product_inventory_id)
        inWarehouseList.sort(Comparator.comparing(CarrierPanelModel::getWarehouse_id));
    }
    private ArrayList removeDuplicates(ArrayList<?> list){
        int count = list.size();
        for (int i = 0; i < count; i++)
        {
            for (int j = i + 1; j < count; j++)
            {
                if (list.get(i).equals(list.get(j)))
                {
                    list.remove(j--);
                    count--;
                }
            }
        }
        return list;
    }


}