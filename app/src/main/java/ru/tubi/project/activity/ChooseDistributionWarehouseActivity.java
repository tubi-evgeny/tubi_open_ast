package ru.tubi.project.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import ru.tubi.project.R;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.utilites.Constant;
import ru.tubi.project.utilites.InitialData;
import ru.tubi.project.utilites.UserDataRecovery;

import static ru.tubi.project.free.AllCollor.TUBI_GREEN_600;
import static ru.tubi.project.free.AllCollor.TUBI_GREY_200;
import static ru.tubi.project.free.AllText.BUILDING;
import static ru.tubi.project.free.AllText.CHOOSE;
import static ru.tubi.project.free.AllText.DISTRIBUTION_WAREHOUSE;
import static ru.tubi.project.free.AllText.SELECT_WAREHOUSE;
import static ru.tubi.project.free.AllText.SMOLENSCK;
import static ru.tubi.project.free.AllText.ST;

public class ChooseDistributionWarehouseActivity extends AppCompatActivity
implements View.OnClickListener {

    private LinearLayout llWarehouseList;
    private Spinner spCity;
    private ListView lvWarehouseList;
    private Button btnApply;
    private String city;
    private ArrayAdapter <String> adapter;
    private ArrayAdapter <String> adapWarehouse;
    private String [] cityList= {SMOLENSCK, "выберите город"};//,"MOSCOW"
    private ArrayList<String> adressWarehouseList = new ArrayList<>();
    private boolean flagCiti=false;

    private UserModel userDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_distribution_warehouse);
        setTitle(CHOOSE); //Выбор распределительного склада
        getSupportActionBar().setSubtitle(DISTRIBUTION_WAREHOUSE);

        llWarehouseList = findViewById(R.id.llWarehouseList);
        spCity = findViewById(R.id.spCity);
        lvWarehouseList = findViewById(R.id.lvWarehouseList);
        btnApply = findViewById(R.id.btnApply);

        btnApply.setOnClickListener(this);
        //получить из sqlLite данные пользователя и компании
        UserDataRecovery userDataRecovery = new UserDataRecovery();
        userDataModel = userDataRecovery.getUserDataRecovery(this);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                cityList);
        spCity.setAdapter(adapter);
        adapWarehouse = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1,adressWarehouseList);
        lvWarehouseList.setAdapter(adapWarehouse);
        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                city = cityList[position];
                receivePartnerWarehouseList(city);
                flagCiti=false;
                makeButtonColor();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {            }
        });

        lvWarehouseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(adressWarehouseList.size() == 1){
                    receivePartnerWarehouseList(city);
                    flagCiti=false;
                }else {
                    yourChoiceRartnerWarehouse(adressWarehouseList.get(position));
                    flagCiti=true;
                }
                makeButtonColor();
            }
        });
    }
    @Override
    public void onClick(View v) {
        if(v.equals(btnApply)){
            if(flagCiti == false) {
                Toast.makeText(this, ""+SELECT_WAREHOUSE, Toast.LENGTH_SHORT).show();
                return;
            }else{
                int warehouse_id = receiveWarehouse_id();
                // поместите warehouse_id для передачи обратно в intent и закрыть это действие
                Intent intent = new Intent();
                intent.putExtra("warehouse_id", warehouse_id);
                setResult(RESULT_OK, intent);
                finish();
               // Toast.makeText(this, "hi "+warehouse_id, Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void yourChoiceRartnerWarehouse(String adressWarehouse){
        adressWarehouseList.clear();
        adressWarehouseList.add(adressWarehouse);
        adapWarehouse.notifyDataSetChanged();
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
        url += "&" + "city=" +city;
        String whatQuestion = "receive_partner_warehouse_list";
        setInitialData(url,whatQuestion);
    }
    private void setInitialData(String url_get, String whatQuestion) {

        InitialData task=new InitialData(){
            protected void onPostExecute(String result) {
                if(whatQuestion.equals("receive_weekend_list")){
                   // splitWeekendList(result);
                }else if(whatQuestion.equals("chenge_order_active")){
                   // splitResultChengeOrder(result);
                }else if(whatQuestion.equals("receive_partner_warehouse_list")){
                    splitResultPartnerWarehouseList(result);
                }
            }
        };
        task.execute(url_get);
    }
    private void splitResultPartnerWarehouseList(String result){
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
                        st += " "+BUILDING+" "+building;
                    }catch (Exception ex){};

                    adressWarehouseList.add(st);
                }
            }
        }catch (Exception ex){
        }
        adapWarehouse.notifyDataSetChanged();
        if(adressWarehouseList.size() == 1){
            flagCiti=true;
            //makeButtonColor();
        }
    }
    private int receiveWarehouse_id(){
        String [] step = adressWarehouseList.get(0).split(" ");
        String [] step_two =step[1].split("/");
        int warehous_id = Integer.parseInt(step_two[1]);
        return warehous_id;
    }

}