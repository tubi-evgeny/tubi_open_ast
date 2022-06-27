package ru.tubi.project.activity.logistics;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;

import ru.tubi.project.R;
import ru.tubi.project.adapters.HandOverProductAdapter;
import ru.tubi.project.models.CarModel;
import ru.tubi.project.models.CarrierPanelModel;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.utilites.Constant;
import ru.tubi.project.utilites.InitialData;
import ru.tubi.project.utilites.UserDataRecovery;

import static ru.tubi.project.free.AllCollor.TUBI_BLACK;
import static ru.tubi.project.free.AllCollor.TUBI_GREEN_600;
import static ru.tubi.project.free.AllCollor.TUBI_GREY_200;
import static ru.tubi.project.free.AllText.HAND_OVER_PRODUCT;
import static ru.tubi.project.free.AllText.LOAD_TEXT;

public class HandOverProductActivity extends AppCompatActivity
        implements View.OnClickListener {

    private RecyclerView recyclerView;
    private TextView tvCarInfo,  tvApply;
    private ListView lvCars;
    private ArrayList<CarModel> carModelList = new ArrayList<>();
    //private ArrayList<CarrierPanelModel> deliveryList = new ArrayList<>();
    private ArrayList<CarrierPanelModel> sortProdList = new ArrayList<>();
    private ArrayList<String> carListString = new ArrayList<>();
    private ArrayAdapter adapLvCars;
    private HandOverProductAdapter adapter;
    private int car_id;

    private UserModel userDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hand_over_product);
        setTitle(HAND_OVER_PRODUCT);//Сдать товар

        recyclerView = (RecyclerView) findViewById(R.id.rvList);
        lvCars = findViewById(R.id.lvCars);
        tvCarInfo = findViewById(R.id.tvCarInfo);
        tvApply = findViewById(R.id.tvApply);

        tvCarInfo.setOnClickListener(this);
        tvApply.setOnClickListener(this);

        tvApply.setClickable(false);

        //получить из sqlLite данные пользователя и компании
        UserDataRecovery userDataRecovery = new UserDataRecovery();
        userDataModel = userDataRecovery.getUserDataRecovery(this);

        startCarList();

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
                sortProdList.clear();
                adapter.notifyDataSetChanged();
                startList();
                tvApply.setClickable(false);
                tvApply.setBackgroundColor(TUBI_GREY_200);
            }
        });
        HandOverProductAdapter.OnCheckedChangeListener checkedChangeListener =
                new HandOverProductAdapter.OnCheckedChangeListener() {
                    @Override
                    public void isChecked(View view, boolean flag, int position) {
                        whatCheckClicked(flag, position);
                    }
                };
        adapLvCars = new ArrayAdapter(this, android.R.layout.simple_list_item_1, carListString);
        lvCars.setAdapter(adapLvCars);
        adapter = new HandOverProductAdapter(this, sortProdList, checkedChangeListener);
        recyclerView.setAdapter(adapter);

    }
    private void whatCheckClicked(boolean flag, int position) {
        int check = 0;
        if (flag) check = 1;
        sortProdList.get(position).setChecked(check);
        tvApply.setBackgroundColor(TUBI_GREEN_600);
        tvApply.setClickable(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        if (v.equals(tvCarInfo)) {
            lvCars.setVisibility(View.VISIBLE);
        }
        else if (v.equals(tvApply)) {
            writeCheckToTable();
            tvApply.setClickable(false);
            tvApply.setBackgroundColor(TUBI_GREY_200);
        }
    }
    //записать галочки товаров в таблицу DB и в deliveryList
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void writeCheckToTable() {
        for (int i = 0; i < sortProdList.size(); i++) {
            int check = sortProdList.get(i).getChecked();
            if (check == 1) {
                int warehouse_inventory_id = sortProdList.get(i).getWarehouseInventory_id();

                writeCheckToLogisticTable(warehouse_inventory_id, check);
            }
        }
        adapter.notifyDataSetChanged();
    }
    //записать галочку в таблицу логистики и склада
    private void writeCheckToLogisticTable(int warehouse_inventory_id, int check){
        String url = Constant.CARRIER_OFFICE;
        String whatQuestion="";
            url += "write_check_give_out_to_logistic_table";
            url += "&" + "warehouse_inventory_id=" + warehouse_inventory_id;
            url += "&" + "check=" + check;
            whatQuestion = "write_check_give_out_to_logistic_table";
        setInitialData(url, whatQuestion);
    }
    //получить список автомобилей user
    private void startCarList(){
        String url = Constant.CARRIER_OFFICE;
        url += "receive_list_cars_user";
        url += "&"+"taxpayer_id="+userDataModel.getCompany_tax_id();
        String whatQuestion = "receive_list_cars_user";
        setInitialData(url, whatQuestion);
    }
    //получить все товары которые загружены в авто и готовы к выдачи на склад
    private void startList() {
        String url = Constant.CARRIER_OFFICE;
        url += "receive_list_hand_over_product";
        url += "&"+"car_id="+car_id;
        String whatQuestion = "receive_list_hand_over_product";
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
                }else if(whatQuestion.equals("receive_list_hand_over_product")){
                    splitResult(result);
                }
                //hide the dialog
                asyncDialog.dismiss();
            }
        };
        task.execute(url_get);
    }
    //разобрать список авто user
    private void splitCarResult(String result){
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
    }
    // разобрать результат с сервера, список продуктов которые собраны для
    // загрузки-разгрузки(отправки) и их колличество
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void splitResult(String result){
        sortProdList.clear();
        try{
            String[] res = result.split("<br>");
            String[] one_temp = res[0].split("&nbsp");
            if (one_temp[0].equals("error") || one_temp[0].equals("messege")) {
                Toast.makeText(this, "" + one_temp[1], Toast.LENGTH_LONG).show();
                return;
            } else {
                for (int i = 0; i < res.length; i++) {
                    String[] temp = res[i].split("&nbsp");
                    int inWarehouse_id=Integer.parseInt(temp[0]);
                    String inCity=temp[1];
                    String inStreet=temp[2];
                    int inHouse=Integer.parseInt(temp[3]);
                    String inBuilding="";
                    try { inBuilding=temp[4]; }catch (Exception ex){     }

                    int warehouseInventory_id = Integer.parseInt(temp[5]);
                    String category = temp[6];
                    String brand = temp[7];
                    String characteristic = temp[8];
                    String unit_measure = temp[9];
                    int weight_volume = Integer.parseInt(temp[10]);
                    String image_url = temp[11];
                    double quantity = Double.parseDouble(temp[12]);
                    String typePackaging = temp[13];
                    int quantityPackage = Integer.parseInt(temp[14]);
                    int check_give_out=Integer.parseInt(temp[15]);
                    int inWarehouse_info_id=Integer.parseInt(temp[16]);
                    int document_num=Integer.parseInt(temp[17]);
                    int document_closed=Integer.parseInt(temp[18]);
                    //int document_save=Integer.parseInt(temp[19]);
                    int invoice_key_id=Integer.parseInt(temp[19]);

                    CarrierPanelModel delivery = new CarrierPanelModel(
                            inWarehouse_id, inCity, inStreet, inHouse, inBuilding,
                            warehouseInventory_id, category, brand, characteristic, unit_measure,
                            weight_volume, typePackaging, quantityPackage, image_url,
                            quantity, check_give_out,inWarehouse_info_id
                            ,document_num,document_closed,invoice_key_id);

                    sortProdList.add(delivery);
                    //сортируем лист по 1 полю(warehouse_id )
                    sortProdList.sort(Comparator.comparing(CarrierPanelModel::getWarehouse_id)
                            .thenComparing(CarrierPanelModel::getDocument_num));
                    adapter.notifyDataSetChanged();
                }
            }
            /*
            //сортируем лист по 2 полям (logistic_product и car_or_warehouse_id)
            deliveryList.sort(Comparator.comparing(CarrierPanelModel::getDocument_num)
                    .thenComparing(CarrierPanelModel::getCounterparty));
             */
        }catch(Exception ex){
            Toast.makeText(this, "Exception: "+ex, Toast.LENGTH_SHORT).show();
        }
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