package ru.tubi.project.activity.company_my;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import ru.tubi.project.R;

import ru.tubi.project.adapters.DistributionOrdersProviderPartnersAdapter;
import ru.tubi.project.models.OrderModel;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.utilites.InitialData;

import java.util.ArrayList;
import java.util.Comparator;

import ru.tubi.project.utilites.Constant;
import ru.tubi.project.utilites.UserDataRecovery;

import static ru.tubi.project.Config.MY_COMPANY_TAXPAYER_ID;
import static ru.tubi.project.free.AllText.BUILDING;
import static ru.tubi.project.free.AllText.ST;
import static ru.tubi.project.free.AllText.WAREHOUSE;

public class DistributionOrdersProviderPartnersActivity extends AppCompatActivity {

    private TextView tvProviderWarehouseInfo;
    private RecyclerView recyclerView;
    private DistributionOrdersProviderPartnersAdapter adapter;
    private ArrayList<OrderModel> ordersList = new ArrayList<>();
    private ArrayList<OrderModel> list = new ArrayList<>();
    private ArrayList<OrderModel> listIntent = new ArrayList<>();
    private Intent takeit;
    private OrderModel my_warehouse_info;
    private int x;
    private String myWarehouse;
    private int COLLECT_PRODUCT_BACK =1;

    private UserModel userDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distribution_orders_provider_partners);
        //Распределение заказов поставщик партнеры

        recyclerView=(RecyclerView)findViewById(R.id.rvList);
        tvProviderWarehouseInfo=findViewById(R.id.tvProviderWarehouseInfo);

        //получить из sqlLite данные пользователя и компании
        UserDataRecovery userDataRecovery = new UserDataRecovery();
        userDataModel = userDataRecovery.getUserDataRecovery(this);


        takeit = getIntent();
        //int  warehouse_id = takeit.getIntExtra("my_warehouse_id",x);
        my_warehouse_info = (OrderModel)takeit.getSerializableExtra("my_warehouse");
        int warehouse_id = my_warehouse_info.getWarehouse_id();

        myWarehouse = WAREHOUSE+" № "+my_warehouse_info.getWarehouse_info_id()+"/"+
                my_warehouse_info.getWarehouse_id()+" "
                +my_warehouse_info.getCity()+" "+ST+" "+my_warehouse_info.getStreet()+" "
                +my_warehouse_info.getHouse();
        if(!my_warehouse_info.getBuilding().isEmpty()){
            myWarehouse += " "+BUILDING+" "+my_warehouse_info.getBuilding();
        }
        tvProviderWarehouseInfo.setText(""+myWarehouse);

        startList(warehouse_id);

        DistributionOrdersProviderPartnersAdapter.RecyclerViewClickListener clickListener=
                new DistributionOrdersProviderPartnersAdapter.RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        WhatButtonClicked(view,position);
                        // Toast.makeText(DistributionOrdersProviderPartnersActivity.this,
                         //        "test: "+view+"\n"+position, Toast.LENGTH_SHORT).show();
                    }
                };
        adapter=new DistributionOrdersProviderPartnersAdapter(this,ordersList,clickListener);
        recyclerView.setAdapter(adapter);

    }
    private void WhatButtonClicked(View view,int position){
        String string=String.valueOf(view);
        String str[]=string.split("/");
        if(str[1].equals("tvOrderProductInfo}")) {
            if(ordersList.get(position).getWarehouse_id() == 000){
                receiveListProduct(position);
                Intent intent=new Intent(this, CollectProductActivity.class);
                //Intent intent=new Intent(this, ProviderListBuyersOrdersActivity.class);
                intent.putExtra("my_warehouse",myWarehouse);
                intent.putExtra("listProduct",listIntent);
                startActivityForResult(intent,COLLECT_PRODUCT_BACK);
            }
        }
    }
    //собрать список товаров и для сборки и колличество которое надо добавить
    private void receiveListProduct(int position){
        //получить список товаров и колличество которое надо добавить
        // следующих позиций до позиции с warehouse_id = '000'
        listIntent.clear();
        for(int i = position + 1;i < ordersList.size()
                && ordersList.get(i).getWarehouse_id() != 000 ;i++){
            listIntent.add(ordersList.get(i));
        }

    }
    private void startList(int warehouse_id){
        String url = Constant.PROVIDER_OFFICE;
        url += "distribution_orders_provider_to_partners";
        url += "&"+"counterparty_tax_id="+userDataModel.getCompany_tax_id();//MY_COMPANY_TAXPAYER_ID;
        url += "&"+"my_warehouse_id="+warehouse_id;
        String whatQuestion = "distribution_orders_provider_to_partners";
        setInitialData(url,whatQuestion);
    }
    private void setInitialData(String url_get, String whatQuestion) {
        InitialData task=new InitialData(){

            @RequiresApi(api = Build.VERSION_CODES.N)
            protected void onPostExecute(String result) {
                if(whatQuestion.equals("distribution_orders_provider_to_partners")){
                    splitResult(result);
                   // Toast.makeText(DistributionOrdersProviderPartnersActivity.this,
                     //       "res: "+result, Toast.LENGTH_SHORT).show();
                }
            }
        };
        task.execute(url_get);
    }
    // разобрать результат с сервера список складов и общая информация о заказах
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void splitResult(String result) {
        ordersList.clear();
         try{
        String[] res = result.split("<br>");
        String[] one_temp = res[0].split("&nbsp");
        if (one_temp[0].equals("error") || one_temp[0].equals("messege")) {
            Toast.makeText(this, "" + one_temp[1], Toast.LENGTH_LONG).show();
            return;
        } else {
            for (int i = 0; i < res.length; i++) {
                String[] temp = res[i].split("&nbsp");

                int warehouse_id = Integer.parseInt(temp[0]);
                int product_id = Integer.parseInt(temp[1]);
                int product_inventory_id = Integer.parseInt(temp[2]);
                String category = temp[3];
                String brand = temp[4];
                String characteristic = temp[5];
                String type_packaging = temp[6];
                String unit_measure = temp[7];
                int weight_volume = Integer.parseInt(temp[8]);
                int quantity_package = Integer.parseInt(temp[9]);
                double provider_stock_quantity = Double.parseDouble(temp[10]);
                double partner_stock_quantity = Double.parseDouble(temp[11]);
                double quantity_to_order = Double.parseDouble(temp[12]);
                int partner_warehouse_id = Integer.parseInt(temp[13]);
                String city = temp[14];
                String street = temp[15];
                int house = Integer.parseInt(temp[16]);
                String building = "";
                double quantityToCollect = Double.parseDouble(temp[18]);
                double quantity_give_away_bad_do_not_receive=Double.parseDouble(temp[19]);
                int partner_warehouse_info_id=Integer.parseInt(temp[20]);
                String product_name=temp[21];

                OrderModel order;
                try {
                    building = temp[17];
                } catch (Exception ex) {
                    //Toast.makeText(this, "Ex: "+ex, Toast.LENGTH_SHORT).show();
                }

                order = new OrderModel(warehouse_id, product_id, product_inventory_id, category,
                        product_name, brand,
                        characteristic, type_packaging, unit_measure, weight_volume, quantity_package,
                        provider_stock_quantity, partner_stock_quantity, quantity_to_order, partner_warehouse_id,
                        city, street, house, building,quantityToCollect,
                        quantity_give_away_bad_do_not_receive,partner_warehouse_info_id);

                ordersList.add(order);
            }
            //Toast.makeText(this, "res: "+ordersList.get(2).getQuantityToCollect(), Toast.LENGTH_SHORT).show();
            //adapter.notifyDataSetChanged();
        }
    }catch(Exception ex){
             Toast.makeText(this, "Exception: "+ex, Toast.LENGTH_SHORT).show();
         }
        sortStartList();
    }
    //рассортировать лист по (partner_warehouse_id и Product_inventory_id) далее в начало нового склада
    // вставить строку с характеристикой и общим колличеством продукта в последующем списке
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sortStartList(){
        list.clear();
        //сортируем лист по 2 полям (partner_warehouse_id и Product_inventory_id)
        ordersList.sort(Comparator.comparing(OrderModel::getPartner_warehouse_id)
                .thenComparing(OrderModel::getProduct_inventory_id));
        list.addAll(ordersList);
        ordersList.clear();
        OrderModel orderSelect;
        int id = 0;
        for (int i = 0; i < list.size(); i++) {
            //ищем место смены складаПартнера
            if (id != list.get(i).getPartner_warehouse_id()) {
                //вставляем в начало перечня нового складаПартнера строку с информацией
                // последующего перечисляемого складаПартнера
                orderSelect = new OrderModel(000, list.get(i).getProduct_id(),
                        list.get(i).getProduct_inventory_id(), list.get(i).getCategory(),
                        list.get(i).getBrand(), list.get(i).getCharacteristic(),
                        list.get(i).getType_packaging(), list.get(i).getUnit_measure(),
                        list.get(i).getWeight_volume(), list.get(i).getQuantity_package(),
                        list.get(i).getProvider_stock_quantity(), list.get(i).getPartner_stock_quantity(),
                        list.get(i).getQuantity_to_order(),
                        list.get(i).getPartner_warehouse_id(),
                        list.get(i).getCity(), list.get(i).getStreet(), list.get(i).getHouse(),
                        list.get(i).getBuilding(),0,
                        list.get(i).getWarehouse_info_id());
                ordersList.add(orderSelect);
                ordersList.add(list.get(i));
                id = list.get(i).getPartner_warehouse_id();
            } else {
                ordersList.add(list.get(i));
            }
        }
        //Toast.makeText(this, "test "+ordersList.get(0).getPartner_warehouse_id(), Toast.LENGTH_SHORT).show();
        //теперь считаем колличество товара в следующих позициях этого товара, и вставляем
        //их в описание (головная строка этого товара сверху)
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // убедитесь, что это возврат с хорошим результатом
        if (requestCode == COLLECT_PRODUCT_BACK) {
            if (resultCode == RESULT_OK) { // Activity.RESULT_OK
                //Toast.makeText(this, "test: back", Toast.LENGTH_SHORT).show();
                int warehouse_id = my_warehouse_info.getWarehouse_id();
                startList(warehouse_id);
            }
        }
    }
}