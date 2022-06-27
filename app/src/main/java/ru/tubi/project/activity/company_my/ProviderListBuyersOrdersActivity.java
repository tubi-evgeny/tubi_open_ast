package ru.tubi.project.activity.company_my;

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
import ru.tubi.project.adapters.ProviderListBuyersOrdersAdapter;
import ru.tubi.project.models.CatalogProductProviderModel;
import ru.tubi.project.models.OrderModel;
import ru.tubi.project.utilites.InitialData;

import java.util.ArrayList;
import java.util.Comparator;

//import static com.example.tubi.Config.MY_TAXPAYER_ID;
import ru.tubi.project.utilites.Constant;

import static ru.tubi.project.Config.MY_COMPANY_TAXPAYER_ID;
import static ru.tubi.project.Config.MY_UID;
import static ru.tubi.project.free.AllText.BUILDING;
import static ru.tubi.project.free.AllText.BUYERS_ORDERS;
import static ru.tubi.project.free.AllText.ST;
import static ru.tubi.project.free.AllText.WAREHOUSE;

public class ProviderListBuyersOrdersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView tvProviderWarehouseInfo;
    private ProviderListBuyersOrdersAdapter adapter;
    private ArrayList<CatalogProductProviderModel> listProduct=new ArrayList<>();
    private ArrayList<CatalogProductProviderModel> list=new ArrayList<>();
    private ProviderListBuyersOrdersAdapter.OnCheckedChangeListener checked ;
    private Intent takeit;
    private OrderModel my_warehouse_info;
    private int partnerWarehouseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_list_buyers_orders);
        setTitle(BUYERS_ORDERS);//Заказы покупателей

        recyclerView=(RecyclerView)findViewById(R.id.rvList);
        tvProviderWarehouseInfo=findViewById(R.id.tvProviderWarehouseInfo);

        takeit = getIntent();
        my_warehouse_info = (OrderModel)takeit.getSerializableExtra("my_warehouse");
        partnerWarehouseId = my_warehouse_info.getPartner_warehouse_id();

        String myWarehouse = WAREHOUSE+" № "+my_warehouse_info.getPartner_warehouse_id()+" "
                +my_warehouse_info.getCity()+" "+ST+" "+my_warehouse_info.getStreet()+" "
                +my_warehouse_info.getHouse();
        if(!my_warehouse_info.getBuilding().isEmpty()){
            myWarehouse += " "+BUILDING+" "+my_warehouse_info.getBuilding();
        }
        tvProviderWarehouseInfo.setText(""+myWarehouse);

        startList();

        ProviderListBuyersOrdersAdapter.RecyclerViewClickListener clickListener=
                new ProviderListBuyersOrdersAdapter.RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        //WhatButtonClicked(view,position);
                         Toast.makeText(ProviderListBuyersOrdersActivity.this,
                                 "click position "+position+"\n"+"view "+view, Toast.LENGTH_SHORT).show();
                    }
                };
                checked = new ProviderListBuyersOrdersAdapter.OnCheckedChangeListener() {
            @Override
            public void isChecked(View view, boolean flag, int position) {
                WhatButtonClicked(view,flag,position);
                //Toast.makeText(ProviderListBuyersOrdersActivity.this, "view: "+view+"\nflag: "+flag+"\nposition: "+position, Toast.LENGTH_SHORT).show();
            }
        };
        adapter=new ProviderListBuyersOrdersAdapter(this,listProduct,clickListener,checked);
        recyclerView.setAdapter(adapter);
    }

    private void WhatButtonClicked(View view, boolean flag, int position){
        String processingCondition = "0";
        int processCondition =0;
        String st = String.valueOf(view);
        String temp[] = st.split("/");
        //меняем состояние выполнения всех заказов этого товара
        if(temp[temp.length-1].equals("checkBox2}")){
            //если это инвормационная строка
            if(listProduct.get(position).getOrder_product_id() == 900112200
                    && listProduct.get(position).getChecked() == 0){
                //в положении checked=0 делаем все позиции связанные с этой строкой
                // (собранный товар checked =1)
                listProduct.get(position).setChecked(1);
                for(int i = position+1;i < listProduct.size();i++){
                    if(listProduct.get(i).getOrder_product_id() != 900112200 ){
                        if(listProduct.get(i).getChecked() != 1){
                           // processingCondition = "provider_product_in_box";
                            processCondition = 1;
                            checkBoxConditionUpdate(processCondition, i);
                            //checkBoxConditionUpdate(processingCondition, i);
                            listProduct.get(i).setChecked(1);
                        }
                    }else{
                        i = listProduct.size();
                    }
                }
            }
            else if(listProduct.get(position).getOrder_product_id() == 900112200
                    && listProduct.get(position).getChecked() == 1){
                //нет необходимости удалять все галочки этого товара
               // Toast.makeText(this, "update: ", Toast.LENGTH_SHORT).show();
            }
            //меняем состояние выполнения одного заказа товара
            if(listProduct.get(position).getOrder_product_id() != 900112200
                    && listProduct.get(position).getChecked() == 0){
                //processingCondition = "provider_product_in_box";
                processCondition = 1;
                checkBoxConditionUpdate(processCondition, position);
                //checkBoxConditionUpdate(processingCondition, position);
                listProduct.get(position).setChecked(1);
            }
            else if(listProduct.get(position).getOrder_product_id() != 900112200
                    && listProduct.get(position).getChecked() == 1){
                //processingCondition = "null_provider_product_in_box";
                processCondition = 0;
                checkBoxConditionUpdate(processCondition, position);
                //checkBoxConditionUpdate(processingCondition, position);
                listProduct.get(position).setChecked(0);
                //после удаления галочки о выполнении сборки одной позиции
                //снимаем галочку со (головная строка этого товара сверху)
                for(int i = position;i >= 0;i--){
                    if(listProduct.get(i).getOrder_product_id() == 900112200){
                        listProduct.get(i).setChecked(0);
                        i=0;
                    }
                }
            }
        }
        adapter.notifyDataSetChanged();        //adapter.notifyItemChanged(position);
    }
    private void checkBoxConditionUpdate(int processingCondition, int position){
        String url = Constant.PROVIDER_OFFICE;
        url += "check_box_condition_update_or_insert";
        url += "&"+"order_product_id="+listProduct.get(position).getOrder_product_id();
        url += "&"+"processingCondition="+processingCondition;
        url += "&"+"user_uid="+MY_UID;
        String whatQuestion = "check_box_condition_update_or_insert";
        setInitialData(url,whatQuestion);
    }
        //получить все заказы покупателей для сборки товара для агента
    private void startList() {
        String url = Constant.PROVIDER_OFFICE;
        url += "receive_list_buyers_orders_product";
        url += "&"+"taxpayer_id="+MY_COMPANY_TAXPAYER_ID;
        String whatQuestion = "receive_list_buyers_orders_product";
        setInitialData(url, whatQuestion);
    }

    private void setInitialData(String url_get, String whatQuestion) {
       // ProgressDialog asyncDialog = new ProgressDialog(this);

        InitialData task=new InitialData(){
          /*  @Override
            protected void onPreExecute() {
                //set message of the dialog
                asyncDialog.setMessage(LOAD_TEXT);
                //show dialog
                asyncDialog.show();
                super.onPreExecute();
            }*/

            @RequiresApi(api = Build.VERSION_CODES.N)
            protected void onPostExecute(String result) {
                //Do your thing
                if(whatQuestion.equals("receive_list_buyers_orders_product")){
                    splitResult(result);
                    //Toast.makeText(CatalogProviderActivity.this, ""+result, Toast.LENGTH_SHORT).show();
                }
                //hide the dialog
               // asyncDialog.dismiss();
            }
        };
        task.execute(url_get);
    }

    // разобрать результат с сервера список продуктов и колличество
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void splitResult(String result){
        listProduct.clear();
        String [] res=result.split("<br>");
        String[]one_temp = res[0].split("&nbsp");
        if(one_temp[0].equals("error") || one_temp[0].equals("messege")){
           // Toast.makeText(this, ""+one_temp[1], Toast.LENGTH_LONG).show();
            return;
        }else{
            for(int i=0;i<res.length;i++){
                String[]temp = res[i].split("&nbsp");

                int order_product_id= Integer.parseInt(temp[0]);
                int product_id = Integer.parseInt(temp[1]);
                int product_inventory_id = Integer.parseInt(temp[2]);
                String category = temp[3];
                String brand = temp[4];
                String characteristic = temp[5];
                String type_packaging = temp[6];
                String unit_measure = temp[7];
                int weight_volume = Integer.parseInt(temp[8]);
                double price = Double.parseDouble(temp[9]);
                int quantity_package = Integer.parseInt(temp[10]);
                String image_url = temp[11];
                double quantity = Double.parseDouble(temp[12]);
                int checked = Integer.parseInt(temp[13]);
                int order_id = Integer.parseInt(temp[14]);

                CatalogProductProviderModel products=new CatalogProductProviderModel(order_product_id,product_id,
                        product_inventory_id, category, brand, characteristic, type_packaging, unit_measure,
                        weight_volume, price, quantity_package, image_url, quantity,checked,order_id);
                listProduct.add(products);
            }
           // receiveOrderProcessing();
            sortStartList();
        }
    }

    //рассортировать лист по (Product_id и Product_inventory_id) далее в начало нового продукта
    // вставить строку с характеристикой и общим колличеством продукта в последующем списке
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sortStartList() {
        //сортируем лист по 2 полям (Product_id и Product_inventory_id)
        listProduct.sort(Comparator.comparing(CatalogProductProviderModel::getProduct_id)
                .thenComparing(CatalogProductProviderModel::getProduct_inventory_id));

        list.addAll(listProduct);
        listProduct.clear();
        CatalogProductProviderModel product ;
        int id = 0;
        for (int i = 0; i < list.size(); i++) {
            //ищем место смены продукта
            if (id != list.get(i).getProduct_inventory_id()) {
                    //вставляем в начало перечня нового товара строку с характеристикой
                    // последующего перечисляемого товара
                    product = new CatalogProductProviderModel(900112200,0,
                            0,list.get(i).getCategory(),list.get(i).getBrand(),
                            list.get(i).getCharacteristic(),list.get(i).getType_packaging(),
                            list.get(i).getUnit_measure(),list.get(i).getWeight_volume(),
                            0,list.get(i).getQuantity_package(),list.get(i).getImage_url(),
                            0,0,0);

                listProduct.add(product);
                listProduct.add(list.get(i));
                id = list.get(i).getProduct_inventory_id();
            } else {
                listProduct.add(list.get(i));
            }
        }
        //теперь считаем колличество товара в следующих позициях этого товара, и вставляем
        //их в описание (головная строка этого товара сверху)
        int position = 0;
        double generalQuantity=0;
        int checkedCount = 0, count=0, s=1;
        for(int i=1;i < listProduct.size();i++){
            if(listProduct.get(i).getOrder_product_id() == 900112200){
                listProduct.get(position).setQuantity(generalQuantity);
                //проверяем все позиции товара на готовность если галочки стоят везде
                // ставим галочку в (головная строка этого товара сверху)
                if(count == checkedCount){
                    listProduct.get(position).setChecked(s);
                }
                count=0;
                checkedCount=0;
                position = i;
                generalQuantity=0;
            }else{
                generalQuantity += listProduct.get(i).getQuantity();
                if(listProduct.get(i).getChecked() == 1){
                    checkedCount++;
                }
                count++;
            }
            if(i == listProduct.size()-1){
                if(count == checkedCount){
                    listProduct.get(position).setChecked(s);
                }
            }
        }
        listProduct.get(position).setQuantity(generalQuantity);
        adapter.notifyDataSetChanged();

    }

}
//получить состояние выполнения заказа '1'='provider_product_in_box'; '0'=false
  /*  @RequiresApi(api = Build.VERSION_CODES.N)
    private void receiveOrderProcessing(){
        int order_product_id;
        String url = API;
        url += "provider_product_in_box";

        for(int i=0;i < listProduct.size();i++){
            j=i;

            order_product_id = listProduct.get(i).getOrder_product_id();
            url += "&"+"order_product_id=" + order_product_id;
            InitialData task = new InitialData(){
                @Override
                protected void onPostExecute(String result) {
                    Toast.makeText(ProviderListBuyersOrdersActivity.this, "res: "+result, Toast.LENGTH_SHORT).show();
                    orderProcessing=Integer.parseInt(result);
                    listProduct.get(j).setChecked(orderProcessing);
                }
            };
            task.equals(API + "provider_product_in_box"
                    +"&"+"order_product_id=" + order_product_id);
        }
        sortStartList();

        //orderProcessing=0;
        InitialData task = new InitialData() {
            @Override
            protected void onPostExecute(String result) {
                orderProcessing=Integer.parseInt(result);
            }
        task.execute(API + "provider_product_in_box"
                +"&"+"order_product_id=" + order_product_id
        };);
        return orderProcessing;
    }*/
