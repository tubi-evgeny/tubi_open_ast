package ru.tubi.project.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import ru.tubi.project.R;
import ru.tubi.project.adapters.ProductAdapter;
import ru.tubi.project.models.OrderModel;
import ru.tubi.project.models.ProductModel;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.utilites.CheckEqualsDateUtil;
import ru.tubi.project.utilites.InitialData;
import ru.tubi.project.utilites.OrderDataRecoveryUtil;
import ru.tubi.project.utilites.SearchOrder_id;

import java.util.ArrayList;

import ru.tubi.project.utilites.Constant;
import ru.tubi.project.utilites.UserDataRecovery;

import static ru.tubi.project.activity.ActivityCatalog.CATALOG_IS_MINE;
import static ru.tubi.project.activity.ActivityCategory.CATEGORY_ACTIVITY;
import static ru.tubi.project.free.AllText.LOAD_TEXT;
import static ru.tubi.project.free.AllText.MAXIMUM;
import static ru.tubi.project.free.AllText.MES_1_PROFILE;
import static ru.tubi.project.free.AllText.NO_DELIVERY;
import static ru.tubi.project.free.AllText.REPORT_A_BUG;
import static ru.tubi.project.utilites.Constant.API;
import static ru.tubi.project.utilites.Constant.API_TEST;
//import static com.example.tubi.utilites.Constant.GET_PRODUCT;


public class ActivityProduct extends AppCompatActivity {

    private Intent intent, takeit;
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private ArrayList<ProductModel> products =new ArrayList<ProductModel>();
    private ArrayList<String> categoryList = new ArrayList<>();
    private String url_get;
    private String category;
    private String whatQuestion;
    private int partner_warehouse_id, myPosition = -1;
    private double quantity, freeQuantityDB;
    private long dateOfSaleMillis;
    private static final int ADD_PRODUCT_ACTIVITY_REQUEST_CODE = 0;
    private static final int ADD_PRODUCT_TO_COMPANY_DATE_FORM_REQUEST_CODE = 12;
    private UserModel userDataModel;
    private SearchOrder_id searchOrder_id = new SearchOrder_id();
    private UserDataRecovery userDataRecovery = new UserDataRecovery();
    private ArrayList<OrderModel> orderDataModelList = new ArrayList<>();
    private OrderDataRecoveryUtil orderDataRecoveryUtil = new OrderDataRecoveryUtil();
    private CheckEqualsDateUtil checkEqualsDate = new CheckEqualsDateUtil();
    private Context context;

    private int key;
    private static final int SHOP_BOX_REQUEST_CODE = 2;
    private static final int CHOOSHE_WAREHOUSE_REQUEST_CODE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        setTitle(R.string.title_products);
        if (savedInstanceState != null) {
            finish();
        }
        //получить из sqlLite данные пользователя и компании
        userDataModel = userDataRecovery.getUserDataRecovery(this);
        //запуск метода обновить меню,
        // нужен для обновления цвета корзины если не пустая
        invalidateOptionsMenu();

        takeit=getIntent();
        key = takeit.getIntExtra("key",0);

        //если есть открытый заказ то получить его номер или получить 0 если заказа открытого нет
        searchOrder_id.searchStartedOrder(this);

        //получить список заказав с характеристиками
        orderDataModelList = orderDataRecoveryUtil.getOrderDataRecovery(this);
        //Toast.makeText(this, "test: "+orderDataModelList.get(0).getOrder_id(), Toast.LENGTH_SHORT).show();

        if(key == CATEGORY_ACTIVITY){
            category = takeit.getStringExtra("category");
            showProd();
        }//мой каталог, в нем товары которые не показаны в каталоге
        else if(key == CATALOG_IS_MINE){
            receiveCategoryListForMyCatalog();
        }


        recyclerView=(RecyclerView)findViewById(R.id.rvList);

        ProductAdapter.OnProductClickListener productClickListener=
                new ProductAdapter.OnProductClickListener() {
                    @Override
                    public void onProductClick(ProductModel product, int position) {
                         goActivityProductCard(position);
                    }
                };
        ProductAdapter.RecyclerViewClickListener clickListener=
                new ProductAdapter.RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        WhatButtonClicked(view,position);
                    }
                };

        adapter=new ProductAdapter(this, products,productClickListener,clickListener);

        recyclerView.setAdapter(adapter);
    }
    //какая кнопка нажата
    public void WhatButtonClicked(View view,int position){
        //если изменено колличество в другом товаре то записываем в БД
        if(myPosition >= 0 && position != myPosition){
            addOrderProduct(products.get(myPosition).getQuantity(), myPosition);
        }
        myPosition = position;
        String string=String.valueOf(view);
        String str[]=string.split("/");
        quantity = products.get(position).getQuantity();
        dateOfSaleMillis = products.get(position).getDate_of_sale_millis();
        //если дата доставки не указана вызвать возврат
        if(dateOfSaleMillis == 0){
            Toast.makeText(this, ""+REPORT_A_BUG+" "+NO_DELIVERY, Toast.LENGTH_SHORT).show();
            return;
        }
        if(str[1].equals("llPlus}")) {
            //нет данных о компании
            if(userDataModel.getCompany_tax_id() == 0){
                Intent intent= new Intent(this,CompanyDateFormActivity.class);
                intent.putExtra("message",MES_1_PROFILE);
                startActivityForResult(intent,ADD_PRODUCT_TO_COMPANY_DATE_FORM_REQUEST_CODE);
                return;
            }
            //проверить есть ли открытый заказ для данного товара (на нужную дату)
            boolean openOrderThisDate = false;
            for(int i=0;i < orderDataModelList.size();i++){
                long dateOfOrderMillis = orderDataModelList.get(i).getDate_millis();

                boolean flag = checkEqualsDate.check(dateOfOrderMillis, dateOfSaleMillis);
                if(flag){
                    openOrderThisDate=true;
                    products.get(position).setQuantity(quantity+1);
                }
            }
            //если открытый заказ на нужную дату не найден то открыть заказ
            if(openOrderThisDate == false){
                Intent intent = new Intent(this,ChooseDistributionWarehouseActivity.class);
                startActivityForResult(intent,CHOOSHE_WAREHOUSE_REQUEST_CODE);
            }
        }else if(str[1].equals("llPlusTen}")) {
                products.get(position).setQuantity(quantity+10);
        }
        else if(str[1].equals("llMinus}")){//  btnMinus
            if(quantity <=0){
                quantity=0;
            }else {
                products.get(position).setQuantity(quantity - 1);
            }
        }else if(str[1].equals("llMinusTen}")){//  btnMinus
            if(quantity <=10){
                quantity=0;
                products.get(position).setQuantity(quantity);
            }else {
                products.get(position).setQuantity(quantity - 10);
            }
        }
        adapter.notifyItemChanged(position);
    }
    // создать новый заказ
    private void addOrder(){
        String myCategory = "все";
        if(category.equals("сигареты")){
            myCategory = category;
        }
        url_get = API_TEST;
        url_get += "add_my_order";
        url_get += "&" + "user_uid=" + userDataModel.getUid();//MY_UID;
        url_get += "&" + "company_tax_id=" + userDataModel.getCompany_tax_id();//MY_COMPANY_TAXPAYER_ID;
        url_get += "&" + "warehouse_id=" + partner_warehouse_id;
        url_get += "&" + "dateOfSaleMillis=" + dateOfSaleMillis;
        url_get += "&" + "category=" + myCategory;//category;
        whatQuestion= "get_my_order_id";
        setInitialData(url_get, whatQuestion);
    }
    //получить список продуктов и их колличество
    private void showProd(){
        products.clear();
        String myCategory="";
        String order_id_string = "";
        for(int i=0;i < orderDataModelList.size();i++){
            order_id_string += orderDataModelList.get(i).getOrder_id();
            if(i != orderDataModelList.size()-1){
                order_id_string += ";";
            }
        }
        if(key == CATEGORY_ACTIVITY) {
            myCategory = category;
            url_get = API_TEST;//api_test.php?show_product_and_quantity
            url_get += "show_product_and_quantity";
            url_get += "&" + "order_id=" + order_id_string;//userDataModel.getOrder_id();
            url_get += "&" + "category=" + myCategory;
            url_get += "&" + "city_id=" + 2;
            whatQuestion = "get_product_and_quantity";
            setInitialData(url_get, whatQuestion);
        }else if(key == CATALOG_IS_MINE){
            for(int i=0;i < categoryList.size();i++){
                myCategory = categoryList.get(i);
                url_get = Constant.GET_PRODUCT_AND_QUANTITY;
                url_get += "&" + "order_id=" + order_id_string;//userDataModel.getOrder_id();
                url_get += "&" + "category=" + myCategory;
                url_get += "&" + "city_id=" + 2;
                whatQuestion = "get_product_and_quantity";
                setInitialData(url_get, whatQuestion);
            }
        }
    }
    private void receiveCategoryListForMyCatalog(){
        String url = API;
        url += "receive_categori_list_for_my_catalog";
        url += "&" + "taxpayer_id=" + userDataModel.getCompany_tax_id();//MY_COMPANY_TAXPAYER_ID;
        String whatQuestion = "receive_categori_list_for_my_catalog";
        setInitialData(url,whatQuestion);
    }
    //добавить продукт и колличество в заказ
    private void addOrderProduct(double myQuantity, int position){
        //выбрать заказ(order_id) для этого товара
        int order_id = 0;
        for(int i=0;i < orderDataModelList.size();i++){
            if(checkEqualsDate.check(orderDataModelList.get(i).getDate_millis()
                    ,products.get(position).getDate_of_sale_millis())){
                order_id = orderDataModelList.get(i).getOrder_id();
                //Toast.makeText(this, "order_id: "+order_id, Toast.LENGTH_SHORT).show();
            }
        }

        url_get= API_TEST;//api_test.php?add_order_product
        url_get += "&"+"add_order_product";
        url_get += "&"+"order_id="+order_id;
        url_get += "&"+"product_inventory_id=" + products.get(position).getProduct_inventory_id();
        url_get += "&"+"provider_warehouse_id=" + products.get(position).getProvider_warehouse_id();
        url_get += "&"+"quantity="+myQuantity;
        url_get += "&"+"process_price="+products.get(position).getProcess_price();
        whatQuestion = "add_order_product";
        setInitialData(url_get, whatQuestion);
    }

    private void setInitialData(String url_get, String whatQuestion) {
        ProgressDialog asyncDialog = new ProgressDialog(this);

        InitialData task=new InitialData(){
            @Override
            protected void onPreExecute() {
               // if( whatQuestion.equals("get_product_and_quantity")){
                    asyncDialog.setMessage(LOAD_TEXT);
                    asyncDialog.show();
              //  }
                super.onPreExecute();
            }

            protected void onPostExecute(String result) {
                //Do your thing
                /*if(whatQuestion.equals("my_started_order")){
                    ORDER_ID = Integer.parseInt(result);
                    searchOrder_id.checkOrUpdataOrderId(result);
                    userDataModel = userDataRecovery.getUserDataRecovery(this);
                    showProd();
                }else*/
                    if(whatQuestion.equals("get_product_and_quantity")){
                    splitResultProductArray(result);
                }
                 // создать новый заказ
                 else if(whatQuestion.equals("get_my_order_id")){

                    addFirstProductInOrder(result);

                }else if(whatQuestion.equals("add_order_product")){
                    showMessege(result);
                }else if(whatQuestion.equals("receive_categori_list_for_my_catalog")){
                    splitCategoryList(result);
                     //Toast.makeText(ActivityProduct.this, "res: "+result, Toast.LENGTH_SHORT).show();
                }//
                //скрыть диалоговое окно
                asyncDialog.dismiss();
            }
        };
        task.execute(url_get);
    }
    private void addFirstProductInOrder(String result){
        //Toast.makeText(this, "res:\n"+result, Toast.LENGTH_SHORT).show();
        try{
            String [] res=result.split("<br>");
            String[] temp = res[0].split("&nbsp");
            int myOrder_id = Integer.parseInt(temp[0]);
            long myDateOfSaleMillis  = Long.parseLong(temp[1]);
            String myCategory = temp[2];

            userDataModel.setOrder_id(myOrder_id);

            OrderModel orderDataModel = new OrderModel
                    (myOrder_id,myDateOfSaleMillis,myCategory);
            orderDataModelList.add(orderDataModel);

            products.get(myPosition).setQuantity(quantity+1);
            addOrderProduct(products.get(myPosition).getQuantity(), myPosition);
            adapter.notifyItemChanged(myPosition);

            invalidateOptionsMenu();
        }catch (Exception ex){
            Toast.makeText(this, "ex: "+ex, Toast.LENGTH_SHORT).show();
        }
    }
    //разобрать список категорий для мой каталог
    private void splitCategoryList(String result){
        try{
            categoryList.clear();
            String [] res=result.split("<br>");
            for(int i=0;i<res.length;i++){
                String category = res[0];
                categoryList.add(category);
            }
        }catch (Exception ex){
            Toast.makeText(this, "ex: "+ex, Toast.LENGTH_SHORT).show();
        }
        showProd();
    }
    // разобрать результат с сервера список продуктов и колличество
    private void splitResultProductArray(String result){
        try{
            String [] res=result.split("<br>");
            for(int i=0;i<res.length;i++){
                String[]temp = res[i].split("&nbsp");
                int product_id=Integer.parseInt(temp[0]);
                int product_inventory_id=Integer.parseInt(temp[1]);
                String category= temp[2];
                String brand= temp[3];
                String characteristic= temp[4];
                String unit_measure = temp[5];
                int weight_volume=Integer.parseInt(temp[6]);
                double price=Double.parseDouble(temp[7]);
                String image_url = temp[8];
                String abbreviation = temp[9];
                String counterparty = temp[10];
                double quantity =Double.parseDouble(temp[11]);
                int count_product_provider=Integer.parseInt(temp[12]);

                int quantity_package = Integer.parseInt(temp[13]);
                String product_name = temp[14];
                long date_of_sale_millis = Long.parseLong(temp[15]);
                double process_price = Double.parseDouble(temp[16]);
                int provider_warehouse_id = Integer.parseInt(temp[17]);

                ProductModel product = new ProductModel(product_id,product_inventory_id
                        , category, product_name, brand, characteristic,unit_measure
                        , weight_volume, price, process_price, image_url, abbreviation, counterparty
                        , quantity,count_product_provider, quantity_package
                        , date_of_sale_millis, provider_warehouse_id);
                products.add(product);
            }
        }catch (Exception ex){
            Log.d("A111","ActivityProduct / splitResultProductArray " +
                    "/ ex: "+ex+"\nres = "+result);
            Toast.makeText(this, "ex: "+ex+"\n"+result, Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();
    }

    private void showMessege(String result){
        //Toast.makeText(this, "res plus or minus:\n "+result, Toast.LENGTH_SHORT).show();
        try {
            String[] res = result.split("<br>");
            // Toast.makeText(this, "res: "+res[0], Toast.LENGTH_SHORT).show();
            for (int i = 0; i < res.length; i++) {
                String[] temp = res[i].split("&nbsp");
                if (temp[0].equals("messege")) {
                    Toast.makeText(this, "" + temp[1], Toast.LENGTH_SHORT).show();
                } else if (temp[0].equals("error")) {
                    Toast.makeText(this, "" + temp[1], Toast.LENGTH_SHORT).show();
                } else if (temp[0].equals("RETURN_QUANTITY")) {
                    freeQuantityDB = Double.parseDouble(temp[1]);
                    products.get(myPosition).setQuantity(freeQuantityDB);
                    adapter.notifyItemChanged(myPosition);
                    Toast.makeText(getApplicationContext(), "" + temp[2] + " \n" + MAXIMUM + ": " + freeQuantityDB, Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception ex){

        }
    }
    // переити в новую активность
    private void goActivityProductCard(int position){
        if(myPosition >= 0 ) {
            addOrderProduct(products.get(myPosition).getQuantity(), myPosition);
        }
        ProductModel product = products.get(position);
        intent = new Intent(this,ActivityProductCard.class);
        intent.putExtra("product",product);
        intent.putExtra("order_id",userDataModel.getOrder_id());//ORDER_ID
        //startActivity(intent);
        startActivityForResult(intent,ADD_PRODUCT_ACTIVITY_REQUEST_CODE);
        //finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SHOP_BOX_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                showProd();
            }
        }else if (requestCode == ADD_PRODUCT_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                showProd();
            }
        }else if(requestCode == ADD_PRODUCT_TO_COMPANY_DATE_FORM_REQUEST_CODE
                && resultCode == RESULT_OK){
            //обновить/получить из sqlLite данные пользователя и компании
            userDataModel = userDataRecovery.getUserDataRecovery(this);
            //обновить/получить список заказав с характеристиками
            orderDataModelList = orderDataRecoveryUtil.getOrderDataRecovery(this);
        }
        else if(requestCode == CHOOSHE_WAREHOUSE_REQUEST_CODE
                && resultCode == RESULT_OK){
            //получить id склада для создания заказа
            partner_warehouse_id = data.getIntExtra("warehouse_id",0);
            addOrder();
            //Toast.makeText(this, "id "+warehouse_id, Toast.LENGTH_SHORT).show();
        }
        //
        //showProd();
    }
    //слушатель возврата по методу Back(); из предыдущей активности
    //нужен для обновления необходимой информации
    @Override
    protected void onRestart() {
        super.onRestart();
        //запуск метода обновить меню,
        // нужен для обновления цвета корзины если не пустая
        invalidateOptionsMenu();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) { //----invalidateOptionsMenu();
        searchOrder_id.searchStartedOrder(this);
        if(userDataModel.getOrder_id() != 0){//ORDER_ID
            menu.findItem(R.id.shoping_box).setIcon(R.drawable.soping_box_green_60ps);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int itemID=item.getItemId();
        if(itemID==R.id.menu){
            intent=new Intent(this,MenuActivity.class);
            startActivity(intent);
        }
        if(itemID==R.id.main){
            intent=new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        if(itemID==R.id.category){
            intent=new Intent(this, ActivityCatalog.class);
            startActivity(intent);
        }
        if(itemID==R.id.shoping_box){
            if(myPosition >= 0 ) {
                addOrderProduct(products.get(myPosition).getQuantity(), myPosition);
            }
            intent=new Intent(this, ShopingBox.class);
            startActivityForResult(intent,SHOP_BOX_REQUEST_CODE );
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(myPosition >= 0 ) {
            addOrderProduct(products.get(myPosition).getQuantity(), myPosition);
        }
        finish();
        super.onBackPressed();
    }
}