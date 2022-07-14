package ru.tubi.project.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ru.tubi.project.R;
import ru.tubi.project.activity.buyer.EnterForDeliveryAddressActivity;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.utilites.Constant;
import ru.tubi.project.utilites.InitialData;
import ru.tubi.project.utilites.UserDataRecovery;

import static ru.tubi.project.free.AllCollor.TUBI_GREEN_300;
import static ru.tubi.project.free.AllCollor.TUBI_GREEN_600;
import static ru.tubi.project.free.AllCollor.TUBI_GREY_200;
import static ru.tubi.project.free.AllText.BUILDING;
import static ru.tubi.project.free.AllText.CHOOSE;
import static ru.tubi.project.free.AllText.DISTRIBUTION_WAREHOUSE;
import static ru.tubi.project.free.AllText.IN_YOUR_CITY_IS_NOT_DELIVERY;
import static ru.tubi.project.free.AllText.SELECT_WAREHOUSE;
import static ru.tubi.project.free.AllText.SMOLENSCK;
import static ru.tubi.project.free.AllText.ST;
import static ru.tubi.project.free.VariablesHelpers.MY_CITY;
import static ru.tubi.project.free.VariablesHelpers.MY_REGION;

public class ChooseDistributionWarehouseActivity extends AppCompatActivity
implements View.OnClickListener {

    private TextView tvRegionDistrictCity;
    //private EditText etStreet, etHause, etBuilding;
    //private Button btnDelivery, btnPickUpFromWarehouse;
    private LinearLayout llWarehouseList, llChoiceWarehouse;//, llEnterAddress;
    //private Spinner spCity;
    private ListView lvWarehouseList;
    private Button btnApply;
    private String city;
    //private ArrayAdapter <String> adapter;
    private ArrayAdapter <String> adapWarehouse;
    private String [] cityList= {SMOLENSCK, "выберите город"};//,"MOSCOW"
    private ArrayList<String> adressWarehouseList = new ArrayList<>();
    private boolean flagCiti=false;
    public static int ENTER_FOR_DDELIVERY_ADDRESS = 7;

    private UserModel userDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_distribution_warehouse);
        setTitle(CHOOSE); //Выбор распределительного склада
        getSupportActionBar().setSubtitle(DISTRIBUTION_WAREHOUSE);

        llWarehouseList= findViewById(R.id.llWarehouseList);
        //llEnterAddress= findViewById(R.id.llEnterAddress);
        tvRegionDistrictCity = findViewById(R.id.tvRegionDistrictCity);
        //etStreet = findViewById(R.id.etStreet);
       // etHause = findViewById(R.id.etHause);
        //etBuilding = findViewById(R.id.etBuilding);
        //btnDelivery = findViewById(R.id.btnDelivery);
        //btnPickUpFromWarehouse = findViewById(R.id.btnPickUpFromWarehouse);
        llChoiceWarehouse = findViewById(R.id.llChoiceWarehouse);
       // spCity = findViewById(R.id.spCity);
        lvWarehouseList = findViewById(R.id.lvWarehouseList);
        btnApply = findViewById(R.id.btnApply);

        btnApply.setOnClickListener(this);
        //btnDelivery.setOnClickListener(this);
       // btnPickUpFromWarehouse.setOnClickListener(this);

        btnApply.setClickable(false);
        //llChoiceWarehouse.setVisibility(View.GONE);
        //llEnterAddress.setVisibility(View.GONE);
        //получить из sqlLite данные пользователя и компании
        UserDataRecovery userDataRecovery = new UserDataRecovery();
        userDataModel = userDataRecovery.getUserDataRecovery(this);

        if(MY_CITY.equals("Другой город")){
            tvRegionDistrictCity.setText(""+IN_YOUR_CITY_IS_NOT_DELIVERY);
        }else{

            tvRegionDistrictCity.setText(""+MY_REGION+" "+MY_CITY);
        }

        receivePartnerWarehouseList(MY_CITY);

       /* spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                city = cityList[position];
                //receivePartnerWarehouseList(city);
                flagCiti=false;
                makeButtonColor();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {            }
        });*/

        lvWarehouseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(adressWarehouseList.size() == 1){
                    //обновить список если было несколько складо но произошла ошибка в выборе
                    receivePartnerWarehouseList(MY_CITY);
                    btnApply.setClickable(false);
                    btnApply.setBackgroundColor(TUBI_GREY_200);
                    //flagCiti=false;
                }else {
                    yourChoiceRartnerWarehouse(adressWarehouseList.get(position));
                   // flagCiti=true;
                }
                flagCiti=true;
               // makeButtonColor();
            }
        });
        //adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,cityList);
        //spCity.setAdapter(adapter);
        adapWarehouse = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1,adressWarehouseList);
        lvWarehouseList.setAdapter(adapWarehouse);
    }
    @Override
    public void onClick(View v) {
        if(v.equals(btnApply)){
           /* if(flagCiti == false) {
                Toast.makeText(this, ""+SELECT_WAREHOUSE, Toast.LENGTH_SHORT).show();
                return;
            }else{*/
                //получить склад id из строки
                int warehouse_id = receiveWarehouse_id();
                // поместите warehouse_id для передачи обратно в intent и закрыть это действие
                Intent intent = new Intent();
                intent.putExtra("adressWarehouse", adressWarehouseList.get(0));
                intent.putExtra("warehouse_id", warehouse_id);
                setResult(RESULT_OK, intent);
                finish();
           // }
        }
       /* else if(v.equals(btnPickUpFromWarehouse)){
           // llWarehouseList.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Раздел находится в разработке", Toast.LENGTH_SHORT).show();
        }
        else if(v.equals(btnDelivery)){
            if(MY_CITY.equals("Другой город")){
                return;
            }
            //llEnterAddress.setVisibility(View.VISIBLE);
            btnDelivery.setBackgroundColor(TUBI_GREEN_300);
            Intent intent = new Intent(this, EnterForDeliveryAddressActivity.class);
            startActivityForResult(intent,ENTER_FOR_DDELIVERY_ADDRESS);
        }*/
    }
    private void yourChoiceRartnerWarehouse(String adressWarehouse){
        adressWarehouseList.clear();
        adressWarehouseList.add(adressWarehouse);
        adapWarehouse.notifyDataSetChanged();
        btnApply.setClickable(true);
        btnApply.setBackgroundColor(TUBI_GREEN_600);
    }
    private void makeButtonColor(){
        if(flagCiti ){//&& flagDay
            btnApply.setBackgroundColor(TUBI_GREEN_600);
        }else{
            btnApply.setBackgroundColor(TUBI_GREY_200);
        }
    }
    private void receivePartnerWarehouseList(String city){
        String url = Constant.API;
        url += "receive_partner_warehouse_list";
        url += "&" + "city=" +MY_CITY;
        url += "&" + "region=" +MY_REGION;
        String whatQuestion = "receive_partner_warehouse_list";
        setInitialData(url,whatQuestion);
    }
    private void setInitialData(String url_get, String whatQuestion) {

        InitialData task=new InitialData(){
            protected void onPostExecute(String result) {
                if(whatQuestion.equals("receive_partner_warehouse_list")){
                    splitResultPartnerWarehouseList(result);
                }
            }
        };
        task.execute(url_get);
    }
    private void splitResultPartnerWarehouseList(String result){
        Log.d("A111","ChooseDistributionWarehouseActivity " +
                "/ splitResultPartnerWarehouseList / result="+result);
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
                        if(!building.isEmpty()){
                            st += " "+BUILDING+" "+building;
                        }
                    }catch (Exception ex){};

                    adressWarehouseList.add(st);
                }
            }
        }catch (Exception ex){
        }
        adapWarehouse.notifyDataSetChanged();
        if(adressWarehouseList.size() == 1){
            flagCiti=true;
            btnApply.setClickable(true);
            btnApply.setBackgroundColor(TUBI_GREEN_600);
            //makeButtonColor();
        }
    }
    //получить склад id из строки
    private int receiveWarehouse_id(){
        String [] step = adressWarehouseList.get(0).split(" ");
        String [] step_two =step[1].split("/");
        int warehous_id = Integer.parseInt(step_two[1]);
        return warehous_id;
    }

}