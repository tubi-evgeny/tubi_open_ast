package ru.tubi.project.activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import ru.tubi.project.R;
import ru.tubi.project.adapters.ShopingBoxAdapter;
import ru.tubi.project.models.OrderModel;
import ru.tubi.project.models.ShopingBoxModel;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.utilites.GetColorShopingBox;
import ru.tubi.project.utilites.HelperDB;
import ru.tubi.project.utilites.InitialData;
import ru.tubi.project.utilites.InitialDataPOST;
import ru.tubi.project.utilites.OrderDataRecoveryUtil;
import ru.tubi.project.utilites.SearchOrder_id;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ru.tubi.project.utilites.Constant;
import ru.tubi.project.utilites.UserDataRecovery;

import static ru.tubi.project.free.AllText.DELIVERY_TO_ADDRESS;
import static ru.tubi.project.free.AllText.FOR_REGISTRATION_ORDER_NEED;
import static ru.tubi.project.free.AllText.GO_TO_DESIGN;
import static ru.tubi.project.free.AllText.JOINT_BUY_TEXT;
import static ru.tubi.project.free.AllText.LOAD_TEXT;
import static ru.tubi.project.free.AllText.MAXIMUM;
import static ru.tubi.project.free.AllText.ON;
import static ru.tubi.project.free.AllText.ONE_STEP_LEFT;
import static ru.tubi.project.free.AllText.ORDER;
import static ru.tubi.project.free.AllText.RECEIVING_FROM_WAREHOUSE;
import static ru.tubi.project.free.AllText.RUB;
import static ru.tubi.project.free.AllText.SHOPING_BOX;
import static ru.tubi.project.free.AllText.SHOPING_BOX_EMPTY;
import static ru.tubi.project.free.AllText.STOCK_OF_GOODS_REQUESTED_QUANTITY;
import static ru.tubi.project.free.VariablesHelpers.DELIVERY_TO_BUYER_STATUS;
import static ru.tubi.project.utilites.Constant.API;
import static ru.tubi.project.utilites.Constant.API_TEST;
import static ru.tubi.project.utilites.InitialDataPOST.getParamsString;

public class ShopingBox extends AppCompatActivity implements View.OnClickListener {


    private TextView tvBtnOrder, tvAddProduct,tvOrder;
    private ListView lvOrders;
    private LinearLayout llButton, llRecycler, llOrder;
    private ArrayList<String> ordersList = new ArrayList();
    private Intent intent, takeit;
    private SearchOrder_id searchOrder_id = new SearchOrder_id();
    private RecyclerView recyclerView;
    private ShopingBoxAdapter adapter;
    private ArrayAdapter orderAdap;
    private String url_get;
    private String whatQuestion;
    private int order_id = 0,myPosition = -1, deliveryKey;
    private ArrayList<ShopingBoxModel> shopinBoxArray = new ArrayList<>();
    private double quantity, orderSumm, freeQuantityDB, orderSummMax=0, orderSummMin=0;
    private AlertDialog.Builder adb;
    private AlertDialog ad;
    private String timeReceiveOrder, counterparty,taxpayerID;
    private SQLiteDatabase sqdb;
    private HelperDB my_db;
    private String st1 = FOR_REGISTRATION_ORDER_NEED;//Для оформления заказа нам потребуется
    private String stNextAlert = ONE_STEP_LEFT;//Остался один шаг!
    private static final int COMPANY_DATE_FORM_REQUEST_CODE = 0;
    private UserModel userDataModel;
    private UserDataRecovery userDataRecovery = new UserDataRecovery();
    private ArrayList<OrderModel> orderDataModelList = new ArrayList<>();
    private OrderDataRecoveryUtil orderDataRecoveryUtil = new OrderDataRecoveryUtil();
    private Context context;
    public static int JOIN_BUY_MEANING = 0;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoping_box);
        setTitle(SHOPING_BOX);//КОРЗИНА ТОВАРОВ
        if (savedInstanceState != null) {
            finish();
        }

        llButton = findViewById(R.id.llButton);
        llRecycler = findViewById(R.id.llRecycler);
        llOrder = findViewById(R.id.llOrder);
        recyclerView=(RecyclerView)findViewById(R.id.rvList);
        lvOrders = findViewById(R.id.lvOrders);
        tvBtnOrder = findViewById(R.id.tvBtnOrder);
        tvAddProduct = findViewById(R.id.tvAddProduct);
        tvOrder = findViewById(R.id.tvOrder);


        tvOrder.setOnClickListener(this);
        tvBtnOrder.setOnClickListener(this);
        tvAddProduct.setOnClickListener(this);
        tvBtnOrder.setText(""+SHOPING_BOX_EMPTY);

        tvOrder.setVisibility(View.GONE);
        llButton.setVisibility(View.GONE);
        llRecycler.setVisibility(View.GONE);

        //внести в sqlLite данные пользователя и компании
        searchOrder_id.searchStartedOrder(this);
        //получить из sqlLite данные пользователя и компании
        userDataModel = userDataRecovery.getUserDataRecovery(this);
        //order_id = userDataModel.getOrder_id();
        //получить список заказав с характеристиками
        orderDataModelList = orderDataRecoveryUtil.getOrderDataRecovery(this);
        //сортируем лист по 1 полям (getDate_millis )
        orderDataModelList.sort(Comparator.comparing(OrderModel::getDate_millis));

        ShopingBoxAdapter.OnShopingBoxClickListener shopingBoxClickListener =
                new ShopingBoxAdapter.OnShopingBoxClickListener() {
                    @Override
                    public void onShopingBoxClick(ShopingBoxModel shopingBox, int position) {

                    }
                };

        ShopingBoxAdapter.RecyclerViewClickListener clickListener=
                new ShopingBoxAdapter.RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        WhatButtonClicked(view,position);
                    }
                };
        lvOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                whatPositionClicked(position);
               // Toast.makeText(ShopingBox.this, "position "+position, Toast.LENGTH_SHORT).show();
            }
        });

        invalidateOptionsMenu();
        my_db = new HelperDB(this);

        //сделай список заказов в строках для адаптера
        makeOrdersListToString();
        //показать кнопки если заказов нет
        if(ordersList.size() == 0){
            llOrder.setVisibility(View.GONE);
            llButton.setVisibility(View.VISIBLE);
        }

        orderAdap = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ordersList);
        lvOrders.setAdapter(orderAdap);

        showShopingBox();// показать товары в корзине
        receiveOrderSummMin();//получить минимальную сумму одного заказа
        receiveOrderSummMax();//получить максимальную сумму одного заказа
        adapter=new ShopingBoxAdapter(this
                , shopinBoxArray,shopingBoxClickListener,clickListener);//,1);

        recyclerView.setAdapter(adapter);
    }
    //какая позиция списка выдранна
    private void whatPositionClicked(int position){
        tvOrder.setVisibility(View.VISIBLE);

        tvOrder.setText(""+ordersList.get(position));
        order_id  = receiveOrder_id(position);
        deliveryKey = receiveDeliveryKey(order_id);
        timeReceiveOrder = timeReceiveOrder(position);
        JOIN_BUY_MEANING = orderDataModelList.get(position).getJoint_buy();

       // Log.d("A111",getClass()+" / whatPositionClicked / joint_buy="+joint_buy);
        //adapter.notifyDataSetChanged();
        showShopingBox();

        llOrder.setVisibility(View.GONE);
        llButton.setVisibility(View.VISIBLE);
        llRecycler.setVisibility(View.VISIBLE);
    }
    //какая кнопка нажата
    public void WhatButtonClicked(View view,int position){
        //если изменено колличество в другом товаре то записываем в БД
        if(myPosition >= 0 && position != myPosition){
            addOrderProduct(shopinBoxArray.get(myPosition).getQuantity(), myPosition);
        }
        myPosition = position;
        String string=String.valueOf(view);
        String str[]=string.split("/");
        quantity = shopinBoxArray.get(position).getQuantity();
        if(str[1].equals("llPlus}")) {
            //проверить запас товара + (кратно)
            if((quantity + shopinBoxArray.get(position).getMultiple_of())
                    <= shopinBoxArray.get(position).getFree_inventory()){

                //добавить к колиичеству (кратно)
                shopinBoxArray.get(position).setQuantity(quantity + shopinBoxArray
                        .get(position).getMultiple_of());
            }else{
                //добавить остаток
                shopinBoxArray.get(position).setQuantity
                        (quantity + (shopinBoxArray.get(position).getFree_inventory() - quantity));

                Toast.makeText(this, ""
                        +STOCK_OF_GOODS_REQUESTED_QUANTITY, Toast.LENGTH_LONG).show();
            }
            //shopinBoxArray.get(position).setQuantity(++quantity);
        }
        if(str[1].equals("llMinus}")){
            //если заказ меньше мин.заказ
            if(quantity < shopinBoxArray.get(position).getMin_sell()){
                quantity = 0;
                shopinBoxArray.get(position).setQuantity(quantity);
            }
            //если заказ равен мин.заказ
            else if(quantity == shopinBoxArray.get(position).getMin_sell()){
                quantity = 0;
                shopinBoxArray.get(position).setQuantity(quantity);
            }
            //если заказ больше мин.заказ
            else{
                //заказ равен мин.заказ + (кратно)
                if (((quantity - shopinBoxArray.get(position).getMin_sell())
                        % shopinBoxArray.get(position).getMultiple_of()) == 0){
                    //то отнять только (кратно)
                    quantity -= shopinBoxArray.get(position).getMultiple_of();
                    shopinBoxArray.get(position).setQuantity(quantity);
                }
                //отнять остаток от (кратно)
                else{
                    quantity = quantity - ((quantity - shopinBoxArray.get(position).getMin_sell())
                            % shopinBoxArray.get(position).getMultiple_of());
                    shopinBoxArray.get(position).setQuantity(quantity);
                }
            }
            //отнять один товар из заказа)
           /* if(quantity > 0){
                shopinBoxArray.get(position).setQuantity(--quantity);
            }*/

            if(quantity ==0){
                int order_product_id = shopinBoxArray.get(position).getOrder_product_id();
                deleteOrderProduct(order_product_id);
            }
        }
        allSumm();
        adapter.notifyItemChanged(position);
    }
    @Override
    public void onClick(View v) {
        if(v.equals(tvOrder)){
            llOrder.setVisibility(View.VISIBLE);
            llButton.setVisibility(View.GONE);
            llRecycler.setVisibility(View.GONE);
            if(myPosition >= 0 ){
                addOrderProduct(shopinBoxArray.get(myPosition).getQuantity(), myPosition);
            }
        }
        else if(v.equals(tvAddProduct)){
            if(myPosition >= 0 ){
                addOrderProduct(shopinBoxArray.get(myPosition).getQuantity(), myPosition);
            }
            Intent intent = new Intent(this,ActivityCatalog.class);
            startActivity(intent);
            finish();
        }
        //ПЕРЕЙТИ К ОФОРМЛЕНИЮ кнопка
        else if(v.equals(tvBtnOrder)){
            if(tvBtnOrder.getText().equals(SHOPING_BOX_EMPTY)){
                Intent intent = new Intent(this,ActivityCatalog.class);
                startActivity(intent);
                finish();
            }else {
                //ОФОРМЛЕНИЕ ЗАКАЗА
                if(myPosition >= 0 ){
                    addOrderProduct(shopinBoxArray.get(myPosition).getQuantity(), myPosition);
                }
                if(orderSumm <= orderSummMax) {
                    if(DELIVERY_TO_BUYER_STATUS == 0){
                        // данные о компании есть пойдем оформлять заказ
                        makingOrder();
                    }else{
                        if(orderSumm < orderSummMin){
                            //Сообщение сумма заказа меньше чем допускается
                            adOrderSummMin();
                        }else{
                            // данные о компании есть пойдем оформлять заказ
                            makingOrder();
                        }
                    }

                }else if(orderSumm > orderSummMax ){
                    //Сообщение сумма заказа превышенна
                    adOrderSummMax();
                }
            }
        }
    }
    /*
    if(orderSumm <= orderSummMax && orderSumm >= orderSummMin) {
                    // данные о компании есть пойдем оформлять заказ
                    makingOrder();
                }else if(orderSumm >= orderSummMax ){
                    //Сообщение сумма заказа превышенна
                    adOrderSummMax();
                }else if(orderSumm <= orderSummMin){
                    //Сообщение сумма заказа меньше чем допускается
                    adOrderSummMin();
                }
     */
    //очистить данные и выйти со страницы
    private void clearActivityAndBack(String result){
        invalidateOptionsMenu();
        tvBtnOrder.setText(""+SHOPING_BOX_EMPTY);

        recyclerView.setVisibility(View.GONE);
    }
    //узнать есть доставка или нет
    private int receiveDeliveryKey(int order_id){
        int my_deliveryKey=0;
        for(int i=0;i < orderDataModelList.size();i++){
            if(orderDataModelList.get(i).getOrder_id() == order_id){
                my_deliveryKey = orderDataModelList.get(i).getDelivery();
                break;
            }
        }
        return my_deliveryKey;
    }
    //получить максимальную сумму одного заказа
    private void receiveOrderSummMax(){
        whatQuestion = "receive_order_summ_max";
        url_get= API;
        url_get += "receive_order_summ_max";
        setInitialData(url_get, whatQuestion);
       // Log.d("A111","ShopingBox / receiveOrderSummMax / url = "+url_get);
    }
    private void deleteOrderProduct(int order_product_id){
        whatQuestion = "delete_order_product";
        url_get= API_TEST;
        url_get += "delete_order_product";
        url_get += "&" + "order_product_id="+order_product_id;
        setInitialData(url_get, whatQuestion);
    }
    //добавить продукт и колличество в заказ
    private void addOrderProduct(double myQuantity, int position){
        whatQuestion = "add_order_product";
        //url_get= Constant.ADD_ORDER_PRODUCT;
        url_get= API_TEST;
        url_get += "add_order_product";
        url_get += "&"+"order_id="+order_id;//userDataModel.getOrder_id();
        url_get += "&"+"product_inventory_id="+shopinBoxArray.get(position).getProduct_inventory_id();
        url_get += "&"+"provider_warehouse_id=" + shopinBoxArray.get(position).getProvider_warehouse_id();
        url_get += "&"+"quantity="+myQuantity;
        url_get += "&"+"process_price="+shopinBoxArray.get(position).getPrice_process();
        setInitialData(url_get, whatQuestion);
    }
    //получить список продуктов в корзине и их колличество
    private void showShopingBox(){
        if(order_id != 0){

            url_get = Constant.SHOPING_BOX;
            url_get += "show_products";
            url_get += "&"+"order_id="+order_id;//userDataModel.getOrder_id();
            url_get += "&" + "city_id=" + 2;
            whatQuestion = "show_products";
            setInitialData(url_get,whatQuestion);
        }
    }
    //получить минимальную сумму одного заказа
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void receiveOrderSummMin(){
        final Map<String, String> parameters = new HashMap<>();
        parameters.put("receive_order_summ_min","");
        String whatQuestion = "receive_order_summ_min";
        setInitialDataPOST(API, parameters, whatQuestion);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setInitialDataPOST(String url, Map<String, String> param, String whatQuestion){
        //проверка соединения интернета
        if ( !isOnline() ){
            Toast.makeText(getApplicationContext(),
                    "Нет соединения с интернетом!",Toast.LENGTH_LONG).show();
            return;
        }
        ProgressDialog asyncDialog = new ProgressDialog(this);

        InitialDataPOST task = new InitialDataPOST(){
            @Override
            protected void onPreExecute() {
                asyncDialog.setMessage(LOAD_TEXT);
                asyncDialog.show();
                super.onPreExecute();
            }
            @Override
            protected void onPostExecute(String s) {
                if(whatQuestion.equals("receive_order_summ_min")) {
                    splitOrderSummMin(s);
                }
                //скрыть диалоговое окно
                asyncDialog.dismiss();
            }
        };
        task.execute(url, getParamsString(param));
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
                //if(whatQuestion.equals("show_products")){
                    asyncDialog.setMessage(LOAD_TEXT);
                    asyncDialog.show();
              //  }
                super.onPreExecute();
            }

            protected void onPostExecute(String result) {
                //Do your thing
                if(whatQuestion.equals("show_products")){
                    splitResultShoringBoxArray(result);
                }else if(whatQuestion.equals("delete_order_product")){
                    //-----проверить order_id (в заказе может не осталось товара)
                    if(result.equals("order_id=0") ){
                        clearActivityAndBack(result);
                       // recyclerView.setVisibility(View.GONE);
                    }else{
                        showShopingBox();
                    }
                }else if(whatQuestion.equals("add_order_product")){
                    showMessege(result);
                }else if(whatQuestion.equals("receive_order_summ_max")){
                    splitOrderSummMax(result);
                }
                //скрыть диалоговое окно
                asyncDialog.dismiss();
            }
        };
        task.execute(url_get);
    }
    private void splitOrderSummMin(String result){
        try{
            orderSummMin = Double.parseDouble(result);
        }catch (Exception ex){
            Log.d("A111","ShopingBox / splitOrderSummMin / result="+result+" / ex="+ex);
        }
    }
    private void splitOrderSummMax(String result){
        orderSummMax = Double.parseDouble(result);
        Log.d("A111","ShopingBox / splitOrderSummMax / orderSummMax = "+orderSummMax);
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
                    shopinBoxArray.get(myPosition).setQuantity(freeQuantityDB);
                    adapter.notifyItemChanged(myPosition);
                    Toast.makeText(this, "" + temp[2] + " \n" + MAXIMUM + ": " + freeQuantityDB, Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception ex){

        }
    }
    private void splitResultShoringBoxArray(String result){  // разобрать результат с сервера список продуктов и колличество

        try {
            shopinBoxArray.clear();
            String[] res = result.split("<br>");
            //Toast.makeText(this, ""+res[0], Toast.LENGTH_SHORT).show();
            String[] one_temp = res[0].split("&nbsp");
            if (one_temp[0].equals("NO_PRODUCT")) {
                //Toast.makeText(this, "return", Toast.LENGTH_SHORT).show();
                return;
            } else {
                for (int i = 0; i < res.length; i++) {
                    String[] temp = res[i].split("&nbsp");
                    int order_product_id = Integer.parseInt(temp[0]);
                    int product_id = Integer.parseInt(temp[1]);
                    int product_inventory_id = Integer.parseInt(temp[2]);
                    String category = temp[3];
                    String brand = temp[4];
                    String characteristic = temp[5];
                    String unit_measure = temp[6];
                    int weight_volume = Integer.parseInt(temp[7]);
                    double price = Double.parseDouble(temp[8]);
                    String image_url = temp[9];
                    String description_prod = temp[10];
                    String counterparty = temp[11];
                    double quantity = Double.parseDouble(temp[12]);
                    String product_name=temp[13];
                    int quantity_package = Integer.parseInt(temp[14]);
                    double price_process = Double.parseDouble(temp[15]);
                    int provider_warehouse_id = Integer.parseInt(temp[16]);

                    int min_sell = Integer.parseInt(temp[17]);
                    int multiple_of = Integer.parseInt(temp[18]);
                    String product_info = temp[19];
                    double free_inventory = Double.parseDouble(temp[20]);

                    ShopingBoxModel shopingBox = new ShopingBoxModel(order_product_id, product_id
                            ,product_inventory_id, category, product_name, brand, characteristic
                            ,unit_measure, weight_volume,price,price_process, image_url
                            ,description_prod, counterparty, quantity,quantity_package
                            ,provider_warehouse_id
                            ,min_sell, multiple_of, free_inventory, product_info);
                    if (quantity > 0) {
                        shopinBoxArray.add(shopingBox);
                    } else {
                        deleteOrderProduct(order_product_id);
                    }
                }
                allSumm();

                adapter.notifyDataSetChanged();
            }
        }catch (Exception ex){
            Toast.makeText(this, "Ex: "+ex, Toast.LENGTH_SHORT).show();
        }
    }

    private void allSumm(){ //-----стоимость товаров в корзине
        orderSumm = 0;
        for(int i=0;i < shopinBoxArray.size(); i++){
            orderSumm += (shopinBoxArray.get(i).getPrice() + shopinBoxArray.get(i).getPrice_process())
                    * shopinBoxArray.get(i).getQuantity();
        }
        tvBtnOrder.setText(String.format("%.2f", +orderSumm)+" "+RUB+"    "+ GO_TO_DESIGN);
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
    // данные получены то пойдем оформлять заказ
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // убедитесь, что это возврат с хорошим результатом
        if (requestCode == COMPANY_DATE_FORM_REQUEST_CODE) {
            if (resultCode == RESULT_OK) { // Activity.RESULT_OK
                //Toast.makeText(this, "return test: ", Toast.LENGTH_SHORT).show();
                makingOrder();
            }
        }
    }

    private void makingOrder(){
        int count=0, veight = 0;
        double priceSumm=0;
        for(int i = 0;i<shopinBoxArray.size();i++){

            double quantity=shopinBoxArray.get(i).getQuantity();
            veight +=quantity * shopinBoxArray.get(i).getWeight_volume();

            priceSumm += quantity * (shopinBoxArray.get(i).getPrice()
                    +shopinBoxArray.get(i).getPrice_process());
            count = i+1;
        }
        Intent intent = new Intent(this,MakingOrderActivity.class);
        intent.putExtra("count",count);
        intent.putExtra("veight",veight);
        intent.putExtra("priceSumm", priceSumm);
        intent.putExtra("order_id", order_id);
        intent.putExtra("deliveryKey", deliveryKey);
        intent.putExtra("timeReceiveOrder", timeReceiveOrder);
        startActivity(intent);
    }
    //сделай список заказов в строках для адаптера
    private void makeOrdersListToString(){
        ordersList.clear();
        if(orderDataModelList.size() == 1 && orderDataModelList.get(0).getOrder_id() == 0){
            return;
        }
        for(int i=0; i < orderDataModelList.size();i++){
            int myOrder_id = orderDataModelList.get(i).getOrder_id();
            long myMillis = orderDataModelList.get(i).getDate_millis();
            int deliveryKey = orderDataModelList.get(i).getDelivery();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            String myDate = dateFormat.format(new Date(myMillis));
            String st = ""+ORDER+" "+"№ "+myOrder_id+" "+ON+" "+myDate;
            int joint_buy = orderDataModelList.get(i).getJoint_buy();
            //если это совместная закупка
            if(joint_buy == 1){
                st += "\n   "+JOINT_BUY_TEXT;
            }
            //если есть доставка
            if(deliveryKey == 1){
                st += "\n          "+DELIVERY_TO_ADDRESS;
            }else{
                st += "\n          "+RECEIVING_FROM_WAREHOUSE;
            }

            ordersList.add(st);
           // Toast.makeText(this, "test: \n"+st, Toast.LENGTH_SHORT).show();
        }
       // orderAdap.notifyDataSetChanged();
    }
    //разобрать строку заказа
    private int receiveOrder_id(int position){
        String [] step = ordersList.get(position).split(" ");
        int myOrder_id = Integer.parseInt(step[2]);
        return myOrder_id;
    }
    private String timeReceiveOrder(int position){
        String [] step = ordersList.get(position).split(" ");
        String timeReceiveOrder = (step[4]);
        return timeReceiveOrder;
    }
    //слушатель возврата по методу Back(); из предыдущей активности
    //нужен для обновления необходимой информации
    @Override
    protected void onRestart() {
        super.onRestart();
        SearchOrder_id searchOrder_id = new SearchOrder_id();
        searchOrder_id.searchStartedOrder(this);
        //запуск метода обновить меню,
        // нужен для обновления цвета корзины если не пустая
        invalidateOptionsMenu();
        showShopingBox();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) { //----invalidateOptionsMenu();
        GetColorShopingBox gc = new GetColorShopingBox();
        menu = gc.color(this, menu);

        /*  searchOrder_id.searchStartedOrder(this);
        if(userDataModel.getOrder_id() != 0){//ORDER_ID
            menu.findItem(R.id.shoping_box).setIcon(R.drawable.soping_box_green_60ps);
        }*/
        return super.onPrepareOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int itemID=item.getItemId();
        if(itemID==R.id.menu){
            intent=new Intent(this, MenuActivity.class);
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
            intent=new Intent(this, ShopingBox.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(myPosition >= 0 ){
            addOrderProduct(shopinBoxArray.get(myPosition).getQuantity(), myPosition);
        }
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }
    //Сообщение сумма заказа превышенна
    private void adOrderSummMax(){
        adb = new AlertDialog.Builder(this);

        String st1 = "Превышение суммы заказа";
        String st2 = "Сумма заказа временно ограниченна\nмаксимум "+orderSummMax+" руб.\nпожалуйста уменьшите колличество товара в заказе";
        adb.setTitle(st1);
        adb.setMessage(st2);

        ad=adb.create();
        ad.show();
    }
    //Сообщение сумма заказа меньше чем допускается
    private void adOrderSummMin(){
        adb = new AlertDialog.Builder(this);

        String st1 = "Сумма заказа недостаточна";
        String st2 = "Сумма заказа при доставке\nминимум "+orderSummMin+" руб.\nпожалуйста добавьте колличество товара в заказ";
        adb.setTitle(st1);
        adb.setMessage(st2);

        ad=adb.create();
        ad.show();
    }
}