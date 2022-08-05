package ru.tubi.project.activity.company_my;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import ru.tubi.project.R;
import ru.tubi.project.adapters.CatalogInWarehouseAdapter;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.models.WarehouseModel;
import ru.tubi.project.utilites.InitialData;

import java.util.ArrayList;
import java.util.Comparator;

import ru.tubi.project.utilites.Constant;
import ru.tubi.project.utilites.UserDataRecovery;

import static ru.tubi.project.free.AllText.IN_WAREHOUSES;
import static ru.tubi.project.free.AllText.LOAD_TEXT;
import static ru.tubi.project.free.AllText.MY_CATALOG_BIG;

public class CatalogInWarehouseActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private LinearLayout llMyProductInPartner;
    private ArrayList<WarehouseModel> warehousesList = new ArrayList<>();
    private CatalogInWarehouseAdapter adap;

    private UserModel userDataModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog_in_warehouse);
        setTitle(MY_CATALOG_BIG);//МОЙ КАТАЛОГ на складах
        getSupportActionBar().setSubtitle(IN_WAREHOUSES);

        recyclerView=findViewById(R.id.rvList);
        llMyProductInPartner=findViewById(R.id.llMyProductInPartner);

        //получить из sqlLite данные пользователя и компании
        UserDataRecovery userDataRecovery = new UserDataRecovery();
        userDataModel = userDataRecovery.getUserDataRecovery(this);

        llMyProductInPartner.setOnClickListener(this);
        llMyProductInPartner.setClickable(false);
        startList();

        CatalogInWarehouseAdapter.RecyclerViewClickListener clickListener=
                new CatalogInWarehouseAdapter.RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        WhatButtonClicked(view,position);
                        // Toast.makeText(CatalogInWarehouseActivity.this,
                          //      "test: "+view+"\n"+position, Toast.LENGTH_SHORT).show();
                    }
                };

        adap=new CatalogInWarehouseAdapter(this,warehousesList,clickListener);
        recyclerView.setAdapter(adap);
        //если у поставщика только один склад то сразу переходим к следующей активности
        if(warehousesList.size() == 1){
            Intent intent=new Intent(this, CatalogStocksActivity.class);
            intent.putExtra("my_warehouse",warehousesList.get(0));
            startActivity(intent);
        }
    }
    @Override
    public void onClick(View v) {
        if(v.equals(llMyProductInPartner)){
            Intent intent = new Intent(this, MyProductInPartnerWarehouseActivity.class);
            startActivity(intent);
            //Toast.makeText(this, "llMyProductInPartner", Toast.LENGTH_SHORT).show();
        }
    }
    private void WhatButtonClicked(View view,int position){
        String string=String.valueOf(view);
        String str[]=string.split("/");
        if(str[1].equals("llWarehouse}")) {
            Intent intent=new Intent(this, CatalogStocksActivity.class);
            intent.putExtra("my_warehouse",warehousesList.get(position));
            startActivity(intent);
        }
    }
    private void startList(){
        String url = Constant.PROVIDER_OFFICE;
        url += "my_provider_warehouses_count";
        url += "&"+"counterparty_tax_id="+userDataModel.getCompany_tax_id();//MY_COMPANY_TAXPAYER_ID;
        String whatQuestion = "my_provider_warehouses_count";
        setInitialData(url,whatQuestion);
        //найти мои товары на складах моих партнеров
        url = Constant.PROVIDER_OFFICE;
        url += "search_my_product_int_partner_warehouses";
        url += "&"+"counterparty_tax_id="+userDataModel.getCompany_tax_id();//MY_COMPANY_TAXPAYER_ID;
        whatQuestion = "search_my_product_int_partner_warehouses";
        setInitialData(url,whatQuestion);
    }
    private void setInitialData(String url_get, String whatQuestion) {
        ProgressDialog asyncDialog = new ProgressDialog(this);

        InitialData task=new InitialData(){
            @Override
            protected void onPreExecute() {
                asyncDialog.setMessage(LOAD_TEXT);
                asyncDialog.show();
                super.onPreExecute();
            }
            @RequiresApi(api = Build.VERSION_CODES.N)
            protected void onPostExecute(String result) {
                if(whatQuestion.equals("my_provider_warehouses_count")){
                    splitResult(result);
                }else if(whatQuestion.equals("search_my_product_int_partner_warehouses")){
                    splitResultPartnerStock(result);

                }
                //скрыть диалоговое окно
                asyncDialog.dismiss();
            }
        };
        task.execute(url_get);
    }
    private void splitResultPartnerStock(String result) {
        String[] res = result.split("<br>");
        String[] one_temp = res[0].split("&nbsp");
        if (one_temp[0].equals("RESULT_OK")) {
            llMyProductInPartner.setBackgroundResource(R.drawable.round_corners_green);
            llMyProductInPartner.setClickable(true);
        }
    }
    // разобрать результат с сервера список складов и общая информация о заказах
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void splitResult(String result){
        warehousesList.clear();
        try {
            String[] res = result.split("<br>");
            String[] one_temp = res[0].split("&nbsp");
            if (one_temp[0].equals("error") || one_temp[0].equals("messege")) {
                Toast.makeText(this, "" + one_temp[1], Toast.LENGTH_LONG).show();
                return;
            } else {
                for (int i = 0; i < res.length; i++) {
                    String[] temp = res[i].split("&nbsp");

                    int warehouse_info_id = Integer.parseInt(temp[0]);
                    String city = temp[1];
                    String street = temp[2];
                    int house = Integer.parseInt(temp[3]);
                    String building = "";
                    int warehouse_id = Integer.parseInt(temp[5]);
                    String warehouse_type = temp[6];
                    try {
                        building = temp[4];
                    } catch (Exception ex) {
                    }
                    WarehouseModel warehouse = new WarehouseModel(warehouse_info_id, city, street, house,
                            building, warehouse_id, warehouse_type);

                    warehousesList.add(warehouse);
                }
                //сортируем лист по 2 полям (getWarehouse_type и getWarehouse_id)
                warehousesList.sort(Comparator.comparing(WarehouseModel::getWarehouse_type)
                        .thenComparing(WarehouseModel::getWarehouse_id));

                adap.notifyDataSetChanged();
            }
        }catch(Exception ex){
            Log.d("A111","CatalogInWarehouseActivity / splitResult result="+result);
        }
    }


}