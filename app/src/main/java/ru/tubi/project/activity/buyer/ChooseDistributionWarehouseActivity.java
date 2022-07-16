package ru.tubi.project.activity.buyer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import static ru.tubi.project.free.AllCollor.alert_dialog_button_green_pressed;
import static ru.tubi.project.free.AllText.BUILDING;
import static ru.tubi.project.free.AllText.CHOOSE;
import static ru.tubi.project.free.AllText.COPY_TEXT;
import static ru.tubi.project.free.AllText.DISTRIBUTION_WAREHOUSE;
import static ru.tubi.project.free.AllText.IN_YOUR_CITY_IS_NOT_DELIVERY;
import static ru.tubi.project.free.AllText.MAKE_COPY_PRODUCT_CARD_TEXT;
import static ru.tubi.project.free.AllText.RETURN_BIG;
import static ru.tubi.project.free.AllText.SELECT_WAREHOUSE;
import static ru.tubi.project.free.AllText.SMOLENSCK;
import static ru.tubi.project.free.AllText.ST;
import static ru.tubi.project.free.AllText.TRY_TO_SELECT_DELIVERY_SMALL;
import static ru.tubi.project.free.AllText.YOUR_CEN_REDACT_PRODUCT_CARD_TEXT;
import static ru.tubi.project.free.AllText.YOUR_REGION_IS_NOT_WAREHOUSES;
import static ru.tubi.project.free.VariablesHelpers.MY_CITY;
import static ru.tubi.project.free.VariablesHelpers.MY_REGION;

public class ChooseDistributionWarehouseActivity extends AppCompatActivity
implements View.OnClickListener {

    private TextView tvRegionDistrictCity;
    private LinearLayout llWarehouseList, llChoiceWarehouse;
    private ListView lvWarehouseList;
    private Button btnApply;
    private int help_warehouse;
    private ArrayAdapter <String> adapWarehouse;
    private ArrayList<String> adressWarehouseList = new ArrayList<>();
    //private boolean flagCiti=false;
    private AlertDialog.Builder adb;
    private AlertDialog ad;

    private UserModel userDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_distribution_warehouse);
        setTitle(CHOOSE); //Выбор распределительного склада
        getSupportActionBar().setSubtitle(DISTRIBUTION_WAREHOUSE);

        llWarehouseList= findViewById(R.id.llWarehouseList);
        tvRegionDistrictCity = findViewById(R.id.tvRegionDistrictCity);
        llChoiceWarehouse = findViewById(R.id.llChoiceWarehouse);
        lvWarehouseList = findViewById(R.id.lvWarehouseList);
        btnApply = findViewById(R.id.btnApply);

        btnApply.setOnClickListener(this);

        btnApply.setClickable(false);
        //получить из sqlLite данные пользователя и компании
        UserDataRecovery userDataRecovery = new UserDataRecovery();
        userDataModel = userDataRecovery.getUserDataRecovery(this);

        if(MY_CITY.equals("Другой город")){
            tvRegionDistrictCity.setText(""+IN_YOUR_CITY_IS_NOT_DELIVERY);
        }else{

            tvRegionDistrictCity.setText(""+MY_REGION+" "+MY_CITY);
        }

        receivePartnerWarehouseList(MY_CITY);

        lvWarehouseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(adressWarehouseList.size() == 1){
                    //обновить список если было несколько складо но произошла ошибка в выборе
                    receivePartnerWarehouseList(MY_CITY);
                    btnApply.setClickable(false);
                    btnApply.setBackgroundColor(TUBI_GREY_200);
                }else {
                    yourChoiceRartnerWarehouse(adressWarehouseList.get(position));
                }
                //flagCiti=true;
            }
        });
        adapWarehouse = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1,adressWarehouseList);
        lvWarehouseList.setAdapter(adapWarehouse);
    }
    @Override
    public void onClick(View v) {
        if(v.equals(btnApply)){
                //получить склад id из строки
                int warehouse_id = receiveWarehouse_id();
                // поместите warehouse_id для передачи обратно в intent и закрыть это действие
                Intent intent = new Intent();
                intent.putExtra("adressWarehouse", adressWarehouseList.get(0));
                intent.putExtra("warehouse_id", warehouse_id);
                setResult(RESULT_OK, intent);
                finish();
        }

    }
    private void yourChoiceRartnerWarehouse(String adressWarehouse){
        adressWarehouseList.clear();
        adressWarehouseList.add(adressWarehouse);
        adapWarehouse.notifyDataSetChanged();
        btnApply.setClickable(true);
        btnApply.setBackgroundColor(TUBI_GREEN_600);
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

                    int warehouse_info_id = Integer.parseInt(temp[0]);
                    String street = temp[1];
                    String house = temp[2];
                    int warehouse_id=Integer.parseInt(temp[4]);
                    help_warehouse=Integer.parseInt(temp[5]);

                    //если склад не фиктивный то пишем его
                    if(help_warehouse != 1){
                        String st = "№ "+warehouse_info_id+"/"+warehouse_id+" "+ST+" "+street+" "+house;
                        try {
                            String building = temp[3];
                            if(!building.isEmpty()){
                                st += " "+BUILDING+" "+building;
                            }
                        }catch (Exception ex){};

                        adressWarehouseList.add(st);
                    }else{
                        Log.d("A111","ChooseDistributionWarehouseActivity " +
                                "/ splitResultPartnerWarehouseList " +
                                "/ получен фиктивный склад и в список не записан");
                    }
                }
            }
        }catch (Exception ex){
        }
        adapWarehouse.notifyDataSetChanged();
        if(adressWarehouseList.size() == 1){
            //flagCiti=true;
            btnApply.setClickable(true);
            btnApply.setBackgroundColor(TUBI_GREEN_600);
        }else if(adressWarehouseList.size() == 0){
            alertDialogMessege();
        }
    }
    //получить склад id из строки
    private int receiveWarehouse_id(){
        String [] step = adressWarehouseList.get(0).split(" ");
        String [] step_two =step[1].split("/");
        int warehous_id = Integer.parseInt(step_two[1]);
        return warehous_id;
    }
    private void alertDialogMessege(){
        adb = new AlertDialog.Builder(this);
        String st1 = YOUR_REGION_IS_NOT_WAREHOUSES;
        String st2 = TRY_TO_SELECT_DELIVERY_SMALL;
        adb.setTitle(st1);
        adb.setMessage(st2);

        ad=adb.create();
        ad.show();
    }
}