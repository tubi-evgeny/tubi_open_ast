package ru.tubi.project.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import ru.tubi.project.R;
import ru.tubi.project.adapters.ProductCardAdapter;
import ru.tubi.project.models.OrderModel;
import ru.tubi.project.models.ProductModel;
import ru.tubi.project.models.ProductCardModel;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.utilites.CheckEqualsDateUtil;
import ru.tubi.project.utilites.GetColorShopingBox;
import ru.tubi.project.utilites.InitialData;
import ru.tubi.project.utilites.OrderDataRecoveryUtil;
import ru.tubi.project.utilites.SearchOrder_id;

import java.util.ArrayList;

import ru.tubi.project.utilites.Constant;
import ru.tubi.project.utilites.UserDataRecovery;

import static ru.tubi.project.free.AllText.LOAD_TEXT;
import static ru.tubi.project.free.AllText.MAXIMUM;
import static ru.tubi.project.free.AllText.MES_1_PROFILE;
import static ru.tubi.project.free.AllText.NO_DELIVERY;
import static ru.tubi.project.free.AllText.REPORT_A_BUG;
import static ru.tubi.project.free.AllText.STOCK_OF_GOODS_REQUESTED_QUANTITY;
import static ru.tubi.project.free.VariablesHelpers.MY_CITY;
import static ru.tubi.project.utilites.Constant.API_TEST;
//import static com.example.tubi.utilites.Constant.SEARCH_OPEN_ORDER;

public class ActivityProductCard extends AppCompatActivity {

    private Intent intent, takeit;
    private int product_id, myPosition = -1,order_id,partner_warehouse_id,a;
    private ProductModel product;
    private ArrayList<ProductCardModel> allPrice =new ArrayList<ProductCardModel>();
    private ProductCardAdapter adapter;
    private RecyclerView recyclerViewProdCard;
    private String url_get;
    private double quantity,freeQuantityDB;
    private long dateOfSaleMillis;
    private String whatQuestion;
    private AlertDialog.Builder adb;
    private AlertDialog ad;
    private SearchOrder_id searchOrder_id = new SearchOrder_id();
    private UserDataRecovery userDataRecovery = new UserDataRecovery();
    private Context context;
    private UserModel userDataModel;
    private ArrayList<OrderModel> orderDataModelList = new ArrayList<>();
    private OrderDataRecoveryUtil orderDataRecoveryUtil = new OrderDataRecoveryUtil();
    private CheckEqualsDateUtil checkEqualsDate = new CheckEqualsDateUtil();

    private static final int ADD_PRODUCT_CARD_ACTIVITY_REQUEST_CODE = 6;
    private static final int ADD_PRODUCT_CARD_TO_COMPANY_DATE_FORM_REQUEST_CODE = 14;
    private static final int CHOOSHE_WAREHOUSE_REQUEST_CODE = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_card);
        setTitle(R.string.product_card);//Карточка товара
        if (savedInstanceState != null) {
            finish();
        }

        recyclerViewProdCard=(RecyclerView)findViewById(R.id.recyclerViewProdCard);

        //получить из sqlLite данные пользователя и компании
        userDataModel = userDataRecovery.getUserDataRecovery(this);

        takeit=getIntent();
        try {
            product = (ProductModel) takeit.getSerializableExtra("product");
            product_id = product.getProduct_id();

            //если есть открытый заказ то получить его номер или 0 если заказа открытого нет
            searchOrder_id.searchStartedOrder(this);

            //получить список заказав с характеристиками
            orderDataModelList = orderDataRecoveryUtil.getOrderDataRecovery(this);

            showProd();
        }catch (Exception ex){

        }

        //запуск метода обновить меню,
        // нужен для обновления цвета корзины если не пустая
        invalidateOptionsMenu();

        ProductCardAdapter.OnProductClickListener productCardClickListener =
                new ProductCardAdapter.OnProductClickListener() {
                    @Override
                    public void onProductCardClick(ProductCardModel productCard, int position) {
                        alertDialogShow( productCard,  position);
                    }
                };

        ProductCardAdapter.RecyclerViewClickListener clickListener=
                new ProductCardAdapter.RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        WhatButtonClicked(view,position);
                        // Toast.makeText(ActivityProductCard.this, "Button: "+view, Toast.LENGTH_SHORT).show();
                    }
                };

        adapter=new ProductCardAdapter(this,allPrice,
                                        productCardClickListener, clickListener);
        recyclerViewProdCard.setAdapter(adapter);

    }


    private void alertDialogShow(ProductCardModel productCard, int position){

        adb = new AlertDialog.Builder(this);
        String str1 = allPrice.get(position).toString();
        String str2 = allPrice.get(position).getDescription_prod();
        adb.setTitle(str1);
        adb.setMessage(str2);
        ad = adb.create();
        ad.show();
    }
    //какая кнопка нажата
    public void WhatButtonClicked(View view,int position){
        //если изменено колличество в другом товаре то записываем в БД
        if(myPosition >= 0 && position != myPosition){
            addOrderProduct(allPrice.get(myPosition).getQuantity(), myPosition);
        }
        myPosition = position;
        String string=String.valueOf(view);
        String str[]=string.split("/");
        quantity = allPrice.get(position).getQuantity();
        dateOfSaleMillis = allPrice.get(position).getDate_of_sale_millis();
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
                startActivityForResult(
                        intent,ADD_PRODUCT_CARD_TO_COMPANY_DATE_FORM_REQUEST_CODE);
                return;
            }
            //проверить есть ли открытый заказ для данного товара (на нужную дату)
            boolean openOrderThisDate = false;
            for(int i=0;i < orderDataModelList.size();i++){
                long dateOfOrderMillis = orderDataModelList.get(i).getDate_millis();

                boolean flag = checkEqualsDate.check(dateOfOrderMillis, dateOfSaleMillis);
                if(flag){
                    openOrderThisDate=true;
                    //Если колличество товара =0 то добавить мин.заказ
                    if(quantity == 0){
                        //получить колличество товар которое надо добавить в заказ
                        double myQuantity = getQuantityOfProductToAdd(position);

                        allPrice.get(position).setQuantity(myQuantity);

                    }else{
                        //проверить запас товара + (кратно)
                        if((quantity + allPrice.get(position).getMultiple_of())
                                <= allPrice.get(position).getFree_inventory()){

                            //добавить к колиичеству (кратно)
                            allPrice.get(position).setQuantity(quantity + allPrice
                                    .get(position).getMultiple_of());
                        }else{
                            //добавить остаток
                            allPrice.get(position).setQuantity
                                    (quantity + (allPrice.get(position).getFree_inventory() - quantity));

                            Toast.makeText(this, ""
                                    +STOCK_OF_GOODS_REQUESTED_QUANTITY, Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
            //если открытый заказ на нужную дату не найден то открыть заказ
            if(openOrderThisDate == false){
                Intent intent = new Intent(this,ChooseDistributionWarehouseActivity.class);
                startActivityForResult(intent,CHOOSHE_WAREHOUSE_REQUEST_CODE);
            }

        }
       /* else if(str[1].equals("llPlusTen}")) {
            allPrice.get(position).setQuantity(quantity+10);
        }*/
        else if(str[1].equals("llMinus}")){
            //если заказ меньше мин.заказ
            if(quantity < allPrice.get(position).getMin_sell()){
                allPrice.get(position).setQuantity(0);
            }
            //если заказ равен мин.заказ
            else if(quantity == allPrice.get(position).getMin_sell()){
                allPrice.get(position).setQuantity(0);
            }
            //если заказ больше мин.заказ
            else{
                //заказ равен мин.заказ + (кратно)
                if (((quantity - allPrice.get(position).getMin_sell())
                        % allPrice.get(position).getMultiple_of()) == 0){
                    //то отнять только (кратно)
                    allPrice.get(position).setQuantity(quantity - allPrice.get(position).getMultiple_of());
                }
                //отнять остаток от (кратно)
                else{
                    allPrice.get(position).setQuantity(quantity - ((quantity - allPrice.get(position).getMin_sell())
                            % allPrice.get(position).getMultiple_of()));
                }
            }
        }
        /*else if(str[1].equals("llMinusTen}")){//  btnMinus
            if(quantity <=10){
                quantity=0;
                allPrice.get(position).setQuantity(quantity);
            }else {
                allPrice.get(position).setQuantity(quantity - 10);
            }
        }*/
        adapter.notifyItemChanged(position);
    }
    //получить колличество товар которое надо добавить в заказ
    private double getQuantityOfProductToAdd(int position){
        double myQuantity = 0;
        //проверить запас товара
        if(allPrice.get(position).getFree_inventory()
                >= allPrice.get(position).getMin_sell()){
            //добавить мин.заказ
            myQuantity = allPrice.get(position).getMin_sell();
        }else{
            //добавить остаток а складе
            myQuantity = allPrice.get(position).getFree_inventory();
        }
        return  myQuantity;
    }
    private void showProd(){
        //получить список заказав с характеристиками
        orderDataModelList.clear();
        orderDataModelList = orderDataRecoveryUtil.getOrderDataRecovery(this);

        String order_id_string = "";
        for(int i=0;i < orderDataModelList.size();i++){
            order_id_string += orderDataModelList.get(i).getOrder_id();
            if(i != orderDataModelList.size()-1){
                order_id_string += ";";
            }
        }
        String url = Constant.API_TEST;
        url += "show_product_price_all_provider";
        url += "&" + "product_id=" + product_id;
        url += "&" + "order_id=" + order_id_string;
        url += "&" + "city_id=" + 2;
        url += "&" + "my_city=" + MY_CITY;
        whatQuestion = "show_product_price_all_provider";
        setInitialData(url,whatQuestion);
        Log.d("A111","ActivityProductCard / showProd / url="+url);
    }
    //добавить продукт и колличество в заказ
    private void addOrderProduct(double myQuantity, int position){
        //выбрать заказ(order_id) для этого товара
        int order_id = 0;
        for(int i=0;i < orderDataModelList.size();i++){
            if(checkEqualsDate.check(orderDataModelList.get(i).getDate_millis()
                    ,allPrice.get(position).getDate_of_sale_millis())){
                order_id = orderDataModelList.get(i).getOrder_id();
                //Toast.makeText(this, "order_id: "+order_id, Toast.LENGTH_SHORT).show();
            }
        }
        whatQuestion = "add_order_product";
        url_get= Constant.ADD_ORDER_PRODUCT;
        url_get += "&"+"order_id="+order_id;//userDataModel.getOrder_id();
        url_get += "&"+"product_inventory_id=" + allPrice.get(position).getProduct_inventory_id();
        url_get += "&"+"provider_warehouse_id=" + allPrice.get(position).getProvider_warehouse_id();
        url_get += "&"+"quantity="+myQuantity;
        url_get += "&"+"process_price="+allPrice.get(position).getProcess_price();
        setInitialData(url_get, whatQuestion);
    }
    // создать новый заказ
    private void addOrder(){
        String myCategory = "все";
        if(product.getCategory().equals("сигареты")){
            myCategory = product.getCategory();
        }
        url_get = API_TEST;
        url_get += "add_my_order";//url_get = Constant.ADD_MY_ORDER;
        url_get += "&" + "user_uid=" + userDataModel.getUid();//MY_UID;
        url_get += "&" + "company_tax_id=" + userDataModel.getCompany_tax_id();//MY_COMPANY_TAXPAYER_ID;
        url_get += "&" + "warehouse_id=" + partner_warehouse_id;
        url_get += "&" + "dateOfSaleMillis=" + dateOfSaleMillis;
        url_get += "&" + "category=" + myCategory;
        whatQuestion= "get_my_order_id";
        setInitialData(url_get, whatQuestion);

        //получить заказа (заказов) номер
        searchOrder_id.searchStartedOrder(this);
    }

    private void setInitialData(String url_get, String whatQuestion) {
        ProgressDialog asyncDialog = new ProgressDialog(this);
        //проверка соединения интернета
        if ( !isOnline() ){
            Toast.makeText(getApplicationContext(),
                    "Нет соединения с интернетом!",Toast.LENGTH_LONG).show();
            return;
        }
        InitialData task=new InitialData(){
            @Override
            protected void onPreExecute() {
                asyncDialog.setMessage(LOAD_TEXT);
               // asyncDialog.setInverseBackgroundForced();
                asyncDialog.show();
                super.onPreExecute();
            }

            protected void onPostExecute(String result) {
                if(whatQuestion.equals("show_product_price_all_provider")) {
                    splitResult(result);
                }else if(whatQuestion.equals("get_my_order_id")){
                    addFirstProductInOrder(result);
                }
               else if(whatQuestion.equals("add_order_product")) {
                    showMessege(result);
                }
                //скрыть диалоговое окно
                asyncDialog.dismiss();
            }
        };
        task.execute(url_get);

    }
    private void addFirstProductInOrder(String result){
        try {
            String [] res=result.split("<br>");
            String[] temp = res[0].split("&nbsp");
            int myOrder_id = Integer.parseInt(temp[0]);
            long myDateOfSaleMillis  = Long.parseLong(temp[1]);
            String myCategory = temp[2];

            userDataModel.setOrder_id(myOrder_id);

            OrderModel orderDataModel = new OrderModel
                    (myOrder_id, myDateOfSaleMillis, myCategory);
            orderDataModelList.add(orderDataModel);

            //получить колличество товар которое надо добавить в заказ
            double myQuantity = getQuantityOfProductToAdd(myPosition);

            allPrice.get(myPosition).setQuantity(myQuantity);
            //allPrice.get(myPosition).setQuantity(quantity + 1);
            addOrderProduct(allPrice.get(myPosition).getQuantity(), myPosition);
            adapter.notifyItemChanged(myPosition);

            invalidateOptionsMenu();
        }catch (Exception ex){
            Toast.makeText(this, "ex: addFirstProductInOrder\n"+ex, Toast.LENGTH_SHORT).show();
        }
    }
    private void splitResult(String result){
        allPrice.clear();
        try {
            String[] res = result.split("<br>");
            for (int i = 0; i < res.length; i++) {
                String[] temp = res[i].split("&nbsp");
                int product_id=Integer.parseInt(temp[0]);
                int product_inventory_id=Integer.parseInt(temp[1]);
                String category=temp[2];
                String brand=temp[3];
                String characteristic=temp[4];
                String unit_measure=temp[5];
                int weight_volume=Integer.parseInt(temp[6]);
                double price=Double.parseDouble(temp[7]);
                String image_url=temp[8];
                String description_prod=temp[9];
                String counterparty=temp[10];

                double quantity = Double.parseDouble(temp[11]);
                int quantity_package = Integer.parseInt(temp[12]);
                String product_name = temp[13];
                long date_of_sale_millis = Long.parseLong(temp[14]);
                double process_price = Double.parseDouble(temp[15]);
                int provider_warehouse_id = Integer.parseInt(temp[16]);

                int min_sell = Integer.parseInt(temp[17]);
                int multiple_of = Integer.parseInt(temp[18]);
                String product_info = temp[19];
                double free_inventory = Double.parseDouble(temp[20]);

                ProductCardModel prodInfo = new ProductCardModel(product_id, product_inventory_id
                        , category, product_name, brand, characteristic, unit_measure
                        , weight_volume, price, process_price, image_url, description_prod
                        , counterparty, quantity, quantity_package, date_of_sale_millis
                        , provider_warehouse_id
                        , min_sell, multiple_of, free_inventory, product_info);
                allPrice.add(prodInfo);
            }
        }catch (Exception ex){
            Toast.makeText(this, "ex: splitResult\n"+ex, Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();
    }

    private void showMessege(String result){
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
                    allPrice.get(myPosition).setQuantity(freeQuantityDB);
                    adapter.notifyItemChanged(myPosition);
                    Toast.makeText(this, "" + temp[2] + " \n" + MAXIMUM + ": " + freeQuantityDB, Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception ex){

        }
    }
    //проверка соединения интернета
    protected boolean isOnline() {
        String cs = Context.CONNECTIVITY_SERVICE;
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(cs);
        if (cm.getActiveNetworkInfo() == null) {
            return false;
        } else {
            return true;
        }
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
        GetColorShopingBox gc = new GetColorShopingBox();
        menu = gc.color(this, menu);
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
                addOrderProduct(allPrice.get(myPosition).getQuantity(), myPosition);
            }
            intent=new Intent(this, ShopingBox.class);
            startActivityForResult(intent,ADD_PRODUCT_CARD_ACTIVITY_REQUEST_CODE);
            //startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_PRODUCT_CARD_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                showProd();
            }
        }
        else if(requestCode == ADD_PRODUCT_CARD_TO_COMPANY_DATE_FORM_REQUEST_CODE
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
    }

    @Override
    public void onBackPressed() {
        if(myPosition >= 0 ) {
            addOrderProduct(allPrice.get(myPosition).getQuantity(), myPosition);
        }
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }
}