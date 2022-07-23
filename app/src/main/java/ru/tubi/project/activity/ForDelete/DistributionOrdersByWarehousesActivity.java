package ru.tubi.project.activity.ForDelete;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import ru.tubi.project.R;
import ru.tubi.project.activity.company_my.DistributionOrdersProviderPartnersActivity;
import ru.tubi.project.activity.company_my.TransferProductActivity;
import ru.tubi.project.adapters.DistributionOrdersByWarehousesAdapter;
import ru.tubi.project.models.OrderModel;
import ru.tubi.project.utilites.InitialData;

import java.util.ArrayList;

import ru.tubi.project.utilites.Constant;

import static ru.tubi.project.Config.MY_COMPANY_TAXPAYER_ID;
import static ru.tubi.project.free.AllCollor.TUBI_GREEN_600;
import static ru.tubi.project.free.AllCollor.TUBI_WHITE;
import static ru.tubi.project.free.AllText.DISTRIBUTION;
//import static com.example.tubi.free.AllText.DISTRIBUTION_ORDERS_BY_WAREHOUSE;
import static ru.tubi.project.free.AllText.EMPTY;
import static ru.tubi.project.free.AllText.ORDERS_BY_WAREHOUSE;

public class DistributionOrdersByWarehousesActivity extends AppCompatActivity
                                            implements View.OnClickListener {

    private RecyclerView recyclerView;
    private DistributionOrdersByWarehousesAdapter adapter;
    private TextView tvCollectProduct, tvTransferProduct;
    private ArrayList<OrderModel>ordersList = new ArrayList<>();
    private Intent myIntent;

    String testIntent = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distribution_orders_by_warehouses);
        setTitle(DISTRIBUTION);
        getSupportActionBar().setSubtitle(ORDERS_BY_WAREHOUSE);
        //Распределение заказов по складам

        recyclerView=(RecyclerView)findViewById(R.id.rvList);
        tvCollectProduct=findViewById(R.id.tvCollectProduct);
        tvTransferProduct=findViewById(R.id.tvTransferProduct);

        tvCollectProduct.setOnClickListener(this);
        tvTransferProduct.setOnClickListener(this);

        startList();

        myIntent=new Intent(this, DistributionOrdersProviderPartnersActivity.class);
        tvCollectProduct.setBackgroundColor(TUBI_GREEN_600);

        DistributionOrdersByWarehousesAdapter.RecyclerViewClickListener clickListener=
                new DistributionOrdersByWarehousesAdapter.RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        WhatButtonClicked(view,position);
                       // Toast.makeText(DistributionOrdersByWarehousesActivity.this,
                       //         "test: "+view+"\n"+position, Toast.LENGTH_SHORT).show();
                    }
                };
    adapter=new DistributionOrdersByWarehousesAdapter(this,ordersList,clickListener);
    recyclerView.setAdapter(adapter);
    }
    @Override
    public void onClick(View v) {
        if (v.equals(tvCollectProduct)) {
            myIntent=new Intent(this, DistributionOrdersProviderPartnersActivity.class);
            tvCollectProduct.setBackgroundColor(TUBI_GREEN_600);
            tvTransferProduct.setBackgroundColor(TUBI_WHITE);
        }
        else if(v.equals(tvTransferProduct)){
            myIntent=new Intent(this, TransferProductActivity.class);
            tvCollectProduct.setBackgroundColor(TUBI_WHITE);
            tvTransferProduct.setBackgroundColor(TUBI_GREEN_600);
        }
    }

    private void WhatButtonClicked(View view,int position){
        String string=String.valueOf(view);
        String str[]=string.split("/");
        if(str[1].equals("tvMyWarehouseInfo}")) {
                     myIntent.putExtra("my_warehouse",ordersList.get(position));
                     startActivity(myIntent);
        }
    }
    //список складов и общая информация о заказах
    private void startList(){
        String url = Constant.PROVIDER_OFFICE;
        url += "distribution_orders_by_warehouses";
        url += "&"+"counterparty_tax_id="+MY_COMPANY_TAXPAYER_ID;
        String whatQuestion = "distribution_orders_by_warehouses";
        setInitialData(url,whatQuestion);
    }
    private void setInitialData(String url_get, String whatQuestion) {
        InitialData task=new InitialData(){

            protected void onPostExecute(String result) {
                if(whatQuestion.equals("distribution_orders_by_warehouses")){
                    splitResult(result);
                   // Toast.makeText(DistributionOrdersByWarehousesActivity.this,
                      //      "res: "+result, Toast.LENGTH_SHORT).show();
                }
            }
        };
        task.execute(url_get);
    }
    // разобрать результат с сервера список складов и общая информация о заказах
    private void splitResult(String result){
        ordersList.clear();
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
                    int Warehouse_id = Integer.parseInt(temp[5]);

                    OrderModel order;//=new OrderModel(Warehouse_id, city, street, house,"");
                    try {
                        String building = temp[4];
                        order = new OrderModel(warehouse_info_id, city, street, house, building,Warehouse_id);
                    } catch (Exception ex) {
                        order = new OrderModel(warehouse_info_id, city, street, house, "",Warehouse_id);
                    }

                    ordersList.add(order);
                }
                adapter.notifyDataSetChanged();
            }
        }catch (Exception ex){
            Toast.makeText(this, ""+EMPTY, Toast.LENGTH_SHORT).show();
        }

    }


}