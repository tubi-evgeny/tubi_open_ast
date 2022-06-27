package ru.tubi.project.activity.logistics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import ru.tubi.project.R;
import ru.tubi.project.adapters.CarrierPanelFilterAdapter;
import ru.tubi.project.models.CarrierPanelModel;

import java.util.ArrayList;

import static ru.tubi.project.free.AllCollor.TUBI_GREY_100;
import static ru.tubi.project.free.AllCollor.TUBI_WHITE;

public class CarrierPanelFilterActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvFromWhereClick, tvWhereClick, tvTemperatTransportClick,
            tvFromWhere,  tvWhere, tvTempTrans, tvApply;
    private CheckBox cbStandart, cbCold, cbFrost;
    private ImageView ivBack;

    private ArrayList<CarrierPanelModel> outWarehouseList;//=new ArrayList<>();
    private ArrayList<CarrierPanelModel> inWarehouseList=new ArrayList<>();
    private CarrierPanelFilterAdapter outWarehouseAdap;
    private CarrierPanelFilterAdapter inWarehouseAdap;
    private LinearLayout llTempTrans;
    private ScrollView scroll;
    private Intent takeit, intent;
    private RecyclerView rvListFromWhere, rvListWhere;
    private boolean temperStandart, temperCold, temperFrost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrier_panel_filter);
        //Фильтры

        rvListFromWhere=(RecyclerView)findViewById(R.id.rvListFromWhere);
        rvListWhere=(RecyclerView)findViewById(R.id.rvListWhere);
        scroll=findViewById(R.id.scroll);
        ivBack=findViewById(R.id.ivBack);
        llTempTrans=findViewById(R.id.llTempTrans);
        tvFromWhereClick=findViewById(R.id.tvFromWhereClick);
        tvWhereClick=findViewById(R.id.tvWhereClick);
        tvTemperatTransportClick=findViewById(R.id.tvTemperatTransportClick);
        tvFromWhere=findViewById(R.id.tvFromWhere);
        tvWhere=findViewById(R.id.tvWhere);
        tvTempTrans=findViewById(R.id.tvTempTrans);
        tvApply=findViewById(R.id.tvApply);

        cbStandart=findViewById(R.id.cbStandart);
        cbCold=findViewById(R.id.cbCold);
        cbFrost=findViewById(R.id.cbFrost);

        ivBack.setOnClickListener(this);
        tvFromWhereClick.setOnClickListener(this);
        tvWhereClick.setOnClickListener(this);
        tvTemperatTransportClick.setOnClickListener(this);
        cbStandart.setOnClickListener(this);
        cbCold.setOnClickListener(this);
        cbFrost.setOnClickListener(this);
        tvApply.setOnClickListener(this);

        takeit = getIntent();

        outWarehouseList=new ArrayList<>();
        outWarehouseList = (ArrayList<CarrierPanelModel>)
                takeit.getSerializableExtra("outWarehouseList");
        inWarehouseList = (ArrayList<CarrierPanelModel>)
                takeit.getSerializableExtra("inWarehouseList");
        temperStandart=takeit.getBooleanExtra("temperStandart",false);
        temperCold=takeit.getBooleanExtra("temperCold",false);
        temperFrost=takeit.getBooleanExtra("temperFrost",false);

        cbStandart.setChecked(temperStandart);
        cbCold.setChecked(temperCold);
        cbFrost.setChecked(temperFrost);

        CarrierPanelFilterAdapter.OnCheckedChangeListener checkedOutWarehouse =
                new CarrierPanelFilterAdapter.OnCheckedChangeListener() {
                    @Override
                    public void isChecked(View view, boolean flag, int position) {
                        clickedOutWarehouse(flag, position);
                        Toast.makeText(CarrierPanelFilterActivity.this, "check Out: "+position, Toast.LENGTH_SHORT).show();
                    }
                };
        CarrierPanelFilterAdapter.OnCheckedChangeListener checkedInWarehouse = new CarrierPanelFilterAdapter.OnCheckedChangeListener() {
            @Override
            public void isChecked(View view, boolean flag, int position) {
                clickedInWarehouse(flag, position);
                Toast.makeText(CarrierPanelFilterActivity.this, "check In: "+position, Toast.LENGTH_SHORT).show();
            }
        };
        outWarehouseAdap = new CarrierPanelFilterAdapter(this,outWarehouseList,checkedOutWarehouse);
        rvListFromWhere.setAdapter(outWarehouseAdap);
        inWarehouseAdap = new CarrierPanelFilterAdapter(this,inWarehouseList,checkedInWarehouse);
        rvListWhere.setAdapter(inWarehouseAdap);
    }
    private void clickedInWarehouse(boolean flag, int position){
        int check;
        if(flag){ check = 1;
        }else check =0;
        inWarehouseList.get(position).setChecked(check);
    }
    private void clickedOutWarehouse(boolean flag, int position){
        int check;
        if(flag){ check = 1;
        }else check =0;
        outWarehouseList.get(position).setChecked(check);
    }
    private void  goReturnData(){
        // поместите строку для передачи обратно в intent и закрыть это действие
        intent = new Intent();
        intent.putExtra("outWarehouseList",outWarehouseList);
        intent.putExtra("inWarehouseList",inWarehouseList);
        intent.putExtra("temperStandart",temperStandart);
        intent.putExtra("temperCold",temperCold);
        intent.putExtra("temperFrost",temperFrost);

        setResult(RESULT_OK, intent);
        finish();
    }
    public void goWriteData(View view) {
       /* String res = request;
        if(!etRequestData.getText().toString().equals("")) {
            res = etRequestData.getText().toString();
        }
        // поместите строку для передачи обратно в intent и закрыть это действие
        intent = new Intent();
        intent.putExtra("request", res);
        setResult(RESULT_OK, intent);
        finish();*/
    }

    @Override
    public void onClick(View v) {
        //ivBack
        if(v.equals(ivBack)){
            onBackPressed();
        }
        else if(v.equals(tvFromWhereClick)){
            tvFromWhere.getParent().requestChildFocus(tvFromWhere,tvFromWhere);
        }
        else if(v.equals(tvWhereClick)){
            tvWhere.getParent().requestChildFocus(tvWhere,tvWhere);
        }
        else if(v.equals(tvTemperatTransportClick)){
            llTempTrans.getParent().requestChildFocus(llTempTrans,llTempTrans);
        }
        else if(v.equals(tvApply)){
            goReturnData();
        }
        else if(v.equals(cbStandart)){
            if(cbStandart.isChecked()){
                temperStandart = true;
            }else temperStandart = false;
        }
        else if(v.equals(cbCold)){
            if(cbCold.isChecked()){
                temperCold = true;
            }else temperCold = false;
        }
        else if(v.equals(cbFrost)){
            if(cbFrost.isChecked()){
                temperFrost = true;
            }else temperFrost = false;
            Toast.makeText(this, "frost: "+temperFrost, Toast.LENGTH_SHORT).show();
        }

        chengeBackgroundButton(v, tvFromWhereClick, tvWhereClick, tvTemperatTransportClick);
    }
    private void chengeBackgroundButton(View v, TextView... textViews) {
        for (int i = 0; i < textViews.length; i++) {
            if(v.equals(textViews[i])){
                textViews[i].setBackgroundColor(TUBI_WHITE);
            }else{
                textViews[i].setBackgroundColor(TUBI_GREY_100);
            }
        }
    }
     /* private void makeStartList(){
        ArrayList <Integer> outTempo = new ArrayList<>();
        ArrayList <Integer> outList = new ArrayList<>();
        //Set set = new HashSet();
        for(int i=0;i < deliveryList.size();i++){
            outTempo.add(deliveryList.get(i).getOutWarehouse_id());
        }
        outList = removeDuplicates(outTempo);
        for(int j=0;j<outList.size();j++){
            for(int z=0;z<deliveryList.size();z++){
                if(outList.get(j) == deliveryList.get(z).getOutWarehouse_id()){
                    outWarehouseList.add(deliveryList.get(z).getOutWarehouse_id(),
                            deliveryList.get(z).getOutCity() ,deliveryList.get(z).getOutStreet(),
                            deliveryList.get(z).getOutHouse(),
                            deliveryList.get(z).getOutBuilding(), 0);
                }
            }
        }

    }*/

  /*  private ArrayList removeDuplicates(ArrayList<?> list){
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
    }*/
}