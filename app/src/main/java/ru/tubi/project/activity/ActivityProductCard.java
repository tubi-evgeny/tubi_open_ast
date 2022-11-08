package ru.tubi.project.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import ru.tubi.project.R;
import ru.tubi.project.activity.buyer.BuyGoodsTogetherActivity;
import ru.tubi.project.activity.buyer.PlaceOfReceiptOfGoodsActivity;
import ru.tubi.project.adapters.ProductCardAdapter;
import ru.tubi.project.models.DeliveryAddressModel;
import ru.tubi.project.models.OrderModel;
import ru.tubi.project.models.ProductModel;
import ru.tubi.project.models.ProductCardModel;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.utilites.CheckEqualsDateUtil;
import ru.tubi.project.utilites.DownloadImage;
import ru.tubi.project.utilites.GetColorShopingBox;
import ru.tubi.project.utilites.InitialData;
import ru.tubi.project.utilites.MakeImageToSquare;
import ru.tubi.project.utilites.OrderDataRecoveryUtil;
import ru.tubi.project.utilites.SearchOrder_id;

import java.util.ArrayList;

import ru.tubi.project.utilites.Constant;
import ru.tubi.project.utilites.UserDataRecovery;

import static ru.tubi.project.activity.Config.ADMIN_PANEL_URL_IMAGES;
import static ru.tubi.project.activity.Config.PARTNER_COMPANY_TAXPAYER_ID_FOR_AGENT;
import static ru.tubi.project.free.AllCollor.TUBI_GREY_200;
import static ru.tubi.project.free.AllCollor.alert_dialog_button_green_pressed;
import static ru.tubi.project.free.AllText.CHENGE_SMALL;
import static ru.tubi.project.free.AllText.LOAD_TEXT;
import static ru.tubi.project.free.AllText.MAXIMUM;
import static ru.tubi.project.free.AllText.MES_1_PROFILE;
import static ru.tubi.project.free.AllText.MES_22;
import static ru.tubi.project.free.AllText.MES_26;
import static ru.tubi.project.free.AllText.NO_DELIVERY;
import static ru.tubi.project.free.AllText.REPORT_A_BUG;
import static ru.tubi.project.free.AllText.RETURN_BIG;
import static ru.tubi.project.free.AllText.STOCK_OF_GOODS_REQUESTED_QUANTITY;
import static ru.tubi.project.free.VariablesHelpers.DELIVERY_TO_BUYER_STATUS;
import static ru.tubi.project.free.VariablesHelpers.MY_CITY;
import static ru.tubi.project.free.VariablesHelpers.MY_REGION;
import static ru.tubi.project.utilites.Constant.API_TEST;
//import static com.example.tubi.utilites.Constant.SEARCH_OPEN_ORDER;

public class ActivityProductCard extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private Intent intent, takeit;
    private int product_id, myPosition = -1,order_id,partner_warehouse_id,a;
    private int addDeliveryKey;
    private ProductModel product;
    private ArrayList<ProductCardModel> allPrice =new ArrayList<ProductCardModel>();
    private ProductCardAdapter adapter;
    private RecyclerView recyclerViewProdCard;
    private String url_get, searchText;
    private double quantity,freeQuantityDB;
    private long dateOfSaleMillis;
    private String whatQuestion;
    private AlertDialog.Builder adb;
    private AlertDialog ad, ad2;
    private SearchOrder_id searchOrder_id = new SearchOrder_id();
    private UserDataRecovery userDataRecovery = new UserDataRecovery();
    private Context context;
    private UserModel userDataModel;
    private DeliveryAddressModel addressForDelivery;
    private ArrayList<OrderModel> orderDataModelList = new ArrayList<>();
    private OrderDataRecoveryUtil orderDataRecoveryUtil = new OrderDataRecoveryUtil();
    private CheckEqualsDateUtil checkEqualsDate = new CheckEqualsDateUtil();
    private ProgressDialog asyncDialog;

    private static final int ADD_PRODUCT_CARD_ACTIVITY_REQUEST_CODE = 6;
    private static final int ADD_PRODUCT_CARD_TO_COMPANY_DATE_FORM_REQUEST_CODE = 14;
    private static final int CHOOSHE_WAREHOUSE_REQUEST_CODE = 15;
    private static final int PLACE_OF_RECEIPT_OF_GOODS_REQUEST_CODE = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_card);
        setTitle(R.string.product_card);//Карточка товара
        if (savedInstanceState != null) {
            finish();
        }

        recyclerViewProdCard=(RecyclerView)findViewById(R.id.recyclerViewProdCard);
        asyncDialog = new ProgressDialog(this);

        //получить из sqlLite данные пользователя и компании
        userDataModel = userDataRecovery.getUserDataRecovery(this);
        //если есть открытый заказ то получить его номер или 0 если заказа открытого нет
        searchOrder_id.searchStartedOrder(this);
        //получить список заказав с характеристиками
        orderDataModelList = orderDataRecoveryUtil.getOrderDataRecovery(this);

        takeit=getIntent();
        product = (ProductModel) takeit.getSerializableExtra("product");
        searchText=takeit.getStringExtra("searchText");
        try {
            //product = (ProductModel) takeit.getSerializableExtra("product");
            product_id = product.getProduct_id();
            //если есть открытый заказ то получить его номер или 0 если заказа открытого нет
           // searchOrder_id.searchStartedOrder(this);
            //получить список заказав с характеристиками
           // orderDataModelList = orderDataRecoveryUtil.getOrderDataRecovery(this);
            showProd();
        }catch (Exception ex){
            //отправить запрос на поиск товаров по введенному тексту в стоку поиска
            //searchProductShow(searchText);
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
            //если это агент и нет company_tax_id партнера
            if(userDataModel.getRole().equals("sales_agent")
                    && PARTNER_COMPANY_TAXPAYER_ID_FOR_AGENT == 0){
                Log.d("A111","ActivityProductCard / WhatButtonClicked / if (sales_agent)");
                Toast.makeText(this, ""+MES_22, Toast.LENGTH_LONG).show();
                return;
            }
            //если это не агент и нет данных о компании
            else if(!userDataModel.getRole().equals("sales_agent")
                    && userDataModel.getCompany_tax_id() == 0){
                Intent intent= new Intent(this,CompanyDateFormActivity.class);
                intent.putExtra("message",MES_1_PROFILE);
                startActivityForResult(intent
                        ,ADD_PRODUCT_CARD_TO_COMPANY_DATE_FORM_REQUEST_CODE);
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
                //Intent intent = new Intent(this,ChooseDistributionWarehouseActivity.class);
                Intent intent = new Intent(this, PlaceOfReceiptOfGoodsActivity.class);
                startActivityForResult(intent,PLACE_OF_RECEIPT_OF_GOODS_REQUEST_CODE);
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
        else if(str[1].equals("tvQuantity}")){
            adSelectQuantity(position);
        }
       /* else if(str[1].equals("llProviderIfo}")){
            adProviderIfo(position);
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
        url += "&" + "my_region=" + MY_REGION;
        url += "&" + "delivery=" + DELIVERY_TO_BUYER_STATUS;
        whatQuestion = "show_product_price_all_provider";
        setInitialData(url,whatQuestion);
        Log.d("A111","ActivityProductCard / showProd / url="+url);
    }
    //отправить запрос на поиск товаров по введенному тексту в стоку поиска
    private void searchProductShow(String searchText){
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
        url += "search_product_by_text";
        url += "&" + "text_for_search=" + searchText;
        url += "&" + "order_id=" + order_id_string;
        url += "&" + "city_id=" + 2;
        url += "&" + "my_city=" + MY_CITY;
        url += "&" + "my_region=" + MY_REGION;
        url += "&" + "delivery=" + DELIVERY_TO_BUYER_STATUS;
        whatQuestion = "search_product_by_text";
        setInitialData(url,whatQuestion);
        Log.d("A111","ActivityProductCard / searchProductShow / url="+url);
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
        long company_tax_id = userDataModel.getCompany_tax_id();
        //проверить заказ создает агент продаж
        if(userDataModel.getRole().equals("sales_agent")){
            company_tax_id = PARTNER_COMPANY_TAXPAYER_ID_FOR_AGENT;
        }
        String myCategory = "все";
        if(product.getCategory().equals("сигареты")){
            myCategory = product.getCategory();
        }
        url_get = API_TEST;
        url_get += "add_my_order";//url_get = Constant.ADD_MY_ORDER;
        url_get += "&" + "user_uid=" + userDataModel.getUid();//MY_UID;
        url_get += "&" + "company_tax_id=" + company_tax_id;//userDataModel.getCompany_tax_id();
        url_get += "&" + "warehouse_id=" + partner_warehouse_id;
        url_get += "&" + "dateOfSaleMillis=" + dateOfSaleMillis;
        url_get += "&" + "category=" + myCategory;
        url_get += "&" + "delivery=" + addDeliveryKey;
        whatQuestion= "get_my_order_id";
        setInitialData(url_get, whatQuestion);

        //получить заказа (заказов) номер
        searchOrder_id.searchStartedOrder(this);
    }
    //добавить доставку заказа в таблицу БД
    private void addDeliveryToTableDB(int order_id){
        url_get= API_TEST;//api_test.php?add_order_product
        url_get += "&"+"add_delivery_to_table";
        url_get += "&"+"order_id="+order_id;
        url_get += "&"+"addressForDelivery="+addressForDelivery.toString();
        whatQuestion = "add_delivery_to_table";
        setInitialData(url_get, whatQuestion);
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
                }
                else if(whatQuestion.equals("search_product_by_text")){
                    splitResult(result);
                    Log.d("A111","result = "+result);
                }
                else if(whatQuestion.equals("get_my_order_id")){
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

            //если есть доставка покупателю то записать (адрес, телефон) данные в БД
            if(addDeliveryKey == 1){
                addDeliveryToTableDB(myOrder_id);
            }
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
                int guarante_there_is_goods = Integer.parseInt(temp[21]);
                int percent_no_goods = Integer.parseInt(temp[22]);

                ProductCardModel prodInfo = new ProductCardModel(product_id, product_inventory_id
                        , category, product_name, brand, characteristic, unit_measure
                        , weight_volume, price, process_price, image_url, description_prod
                        , counterparty, quantity, quantity_package, date_of_sale_millis
                        , provider_warehouse_id, min_sell, multiple_of, free_inventory, product_info
                        , guarante_there_is_goods, percent_no_goods);
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
        else if(requestCode == PLACE_OF_RECEIPT_OF_GOODS_REQUEST_CODE
                && resultCode == RESULT_OK){
            //Получить ключ, это доставка=1 или самовывоз=0
            addDeliveryKey = data.getIntExtra("addDeliveryKey",0);
            //получить id склада для создания заказа
            partner_warehouse_id = data.getIntExtra("warehouse_id",0);

            if(addDeliveryKey == 1){
                addressForDelivery=(DeliveryAddressModel)data
                        .getSerializableExtra("addressForDelivery");
            }
            addOrder();
            //Toast.makeText(this, "id "+warehouse_id, Toast.LENGTH_SHORT).show();
        }
    }
    private void adSelectQuantity(int position){
        LinearLayout ll = new LinearLayout(this);
        LinearLayout layout = new LinearLayout(this);
        ListView lv = new ListView(this);
        RelativeLayout.LayoutParams mParam = new RelativeLayout.LayoutParams((int)(200),(int)(600));
        layout.setLayoutParams(mParam);
        ll.setGravity(Gravity.CENTER_HORIZONTAL);

        double free_quantity = allPrice.get(position).getFree_inventory();
        ArrayList <Double> quantity_arr = new ArrayList();
        double myQuantity = allPrice.get(position).getMin_sell();
        //показать собрать список какое можно выбрать колликество кратно
        while(free_quantity > myQuantity){
            if(myQuantity <= free_quantity){
                quantity_arr.add(myQuantity);
            }
            myQuantity += allPrice.get(position).getMultiple_of();
        }
        ArrayAdapter adap = new ArrayAdapter(this, android.R.layout.simple_list_item_1, quantity_arr);
        lv.setAdapter(adap);
        ll.addView(layout);
        layout.addView(lv);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pn, long id) {
                double quantity = quantity_arr.get(pn);
                allPrice.get(position).setQuantity(quantity);
                adapter.notifyItemChanged(position);
                ad.cancel();
            }
        });

        adb = new AlertDialog.Builder(this);
        adb.setView(ll);
        ad = adb.create();
        ad.show();
    }
    private void alertDialogShow(ProductCardModel productCard, int position){
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        Button btnJointBayer = new Button(this);
        Button btnShowBigImage = new Button(this);
        Button btnDescription = new Button(this);
        Button btnProviderInfo = new Button(this);
        btnJointBayer.setText("Купить этот товар совместно");
        btnShowBigImage.setText("Увеличить картинку");
        btnDescription.setText("Описание товара");
        btnProviderInfo.setText("О Поставщике");
        btnJointBayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateOfSaleMillis = allPrice.get(position).getDate_of_sale_millis();
                //если дата доставки не указана вызвать возврат
                if(dateOfSaleMillis == 0){
                    Toast.makeText(ActivityProductCard.this, ""
                            +REPORT_A_BUG+" "+NO_DELIVERY, Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.d("A111","btn test 1");
                ProductCardModel productCard = allPrice.get(position);
                Intent intent=new Intent(ActivityProductCard.this
                        , BuyGoodsTogetherActivity.class);
                intent.putExtra("productCard",productCard);
                intent.putExtra("activity","productCard");
                startActivity(intent);
                ad2.cancel();
            }
        });
        btnShowBigImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adShowBigImage(position);
                ad2.cancel();
            }
        });
        btnDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adDescriptionProduct(productCard, position);
                ad2.cancel();
            }
        });
        btnProviderInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adProviderIfo(position);
                ad2.cancel();
            }
        });
        ll.addView(btnJointBayer);
        ll.addView(btnShowBigImage);
        ll.addView(btnDescription);
        ll.addView(btnProviderInfo);
        adb = new AlertDialog.Builder(this);
        adb.setView(ll);
        ad2 = adb.create();
        ad2.show();
    }
    private void adShowBigImage(int position){
        ImageView ivImage = new ImageView(this);
        LinearLayout linearLayout = new LinearLayout(this);

        linearLayout.addView(ivImage, new LinearLayout.LayoutParams(1000,1000));
        linearLayout.setGravity(Gravity.CENTER);

        String image_url = allPrice.get(position).getImage_url();
        if(!image_url.equals("null")) {
            //проверка соединения интернета
            if ( !isOnline() ){
                Toast.makeText(getApplicationContext(),
                        "Нет соединения с интернетом!",Toast.LENGTH_LONG).show();
                return;
            }
            new DownloadImage(){
                  @Override
                  protected void onPreExecute() {
                      //asyncDialog.setMessage(LOAD_TEXT);
                      //asyncDialog.show();
                      asyncDialogShow();
                      super.onPreExecute();
                  }
                @Override
                protected void onPostExecute(Bitmap result) {
                    try {
                        int check = result.getWidth();
                        new MakeImageToSquare(result, ivImage);
                    }catch (Exception ex){
                        ivImage.setImageResource(R.drawable.tubi_logo_no_image_300ps);
                    }
                    asyncDialogDismiss();//asyncDialog.dismiss();
                }
            }
                    .execute(ADMIN_PANEL_URL_IMAGES+image_url);

        }else ivImage.setImageResource(R.drawable.tubi_logo_no_image_300ps);

        adb = new AlertDialog.Builder(this);
        adb.setView(linearLayout);
        ad=adb.create();
        ad.show();
    }
    private void asyncDialogShow(){
        asyncDialog.setMessage(LOAD_TEXT);
        asyncDialog.show();
    }
    private void asyncDialogDismiss(){
        asyncDialog.dismiss();
    }
    private void adDescriptionProduct(ProductCardModel productCard, int position){

        adb = new AlertDialog.Builder(this);
        String str1 = allPrice.get(position).toString();
        String str2 = allPrice.get(position).getDescription_prod();
        adb.setTitle(str1);
        adb.setMessage(str2);
        ad = adb.create();
        ad.show();
    }

    private void adProviderIfo(int position){
        adb = new AlertDialog.Builder(this);
        //String str1 = allPrice.get(position).toString();
        String str2 = MES_26;
        //adb.setTitle(str1);
        adb.setMessage(str2);
        ad = adb.create();
        ad.show();
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
        getMenuInflater().inflate(R.menu.menu_and_search,menu);

        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        try{
            if(searchText != null){
                searchView.setIconified(false);
                searchView.setQuery(searchText,true);
                //Log.d("A111","onCreateOptionsMenu / OK");
            }
        }catch (Exception ex){
            Log.d("A111","onCreateOptionsMenu / ex="+ex);
        }
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
    public void onBackPressed() {
        if(myPosition >= 0 ) {
            addOrderProduct(allPrice.get(myPosition).getQuantity(), myPosition);
        }
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
        super.onBackPressed();
    }

    @Override  //поисковая строка
    public boolean onQueryTextSubmit(String query) {
        searchProductShow(query.trim());
        Log.d("A111","search true / query="+query);
        return false;
    }
    @Override   //поисковая строка
    public boolean onQueryTextChange(String newText) {
        if(newText.toCharArray().length > 2){
            searchProductShow(newText.trim());
        }
        Log.d("A111","search new text / newText="+newText
                            +" length="+newText.toCharArray().length);
        return false;
    }
}