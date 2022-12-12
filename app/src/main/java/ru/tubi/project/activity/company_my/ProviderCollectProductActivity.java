package ru.tubi.project.activity.company_my;

import androidx.annotation.RequiresApi;
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
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;

import ru.tubi.project.R;
import ru.tubi.project.activity.Provider.CorrectedQuantityFromDeletedOrdersActivity;
import ru.tubi.project.adapters.ProviderCollectProductAdapter;
import ru.tubi.project.adapters.ProviderCollectProductDealAdapter;
import ru.tubi.project.models.AcceptProductListProvidersModel;
import ru.tubi.project.models.ProviderCollectProductModel;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.utilites.Constant;
import ru.tubi.project.utilites.DownloadImage;
import ru.tubi.project.utilites.InitialData;
import ru.tubi.project.utilites.MakeImageToSquare;
import ru.tubi.project.utilites.UserDataRecovery;

import static ru.tubi.project.activity.Config.ADMIN_PANEL_URL_PREVIEW_IMAGES;
import static ru.tubi.project.free.AllCollor.TUBI_GREY_200;
import static ru.tubi.project.free.AllCollor.alert_dialog_button_green_pressed;
import static ru.tubi.project.free.AllText.CANCEL_BIG;
import static ru.tubi.project.free.AllText.COLLECTED;
import static ru.tubi.project.free.AllText.ERROR_BIG;
import static ru.tubi.project.free.AllText.FOR_COLLECT;
import static ru.tubi.project.free.AllText.IGNORE_BIG;
import static ru.tubi.project.free.AllText.LIST_PRODUCT;
import static ru.tubi.project.free.AllText.LOAD_TEXT;
import static ru.tubi.project.free.AllText.MES_33;
import static ru.tubi.project.free.AllText.MES_34;
import static ru.tubi.project.free.AllText.PERFORM;
import static ru.tubi.project.free.AllText.REJECTION_BIG;
import static ru.tubi.project.free.AllText.RESERVE;
import static ru.tubi.project.free.AllText.RESERVE_IN_WAAREHOUSE;
import static ru.tubi.project.free.AllText.RESERVE_TO_WAREHOUSE_WILBEE_CORRECT;
import static ru.tubi.project.free.AllText.RETURN_BIG;
import static ru.tubi.project.free.AllText.SHOW_BIG;
import static ru.tubi.project.free.AllText.SPECIFY_QUANTITY_OF_COLLECTED_GOODS;
import static ru.tubi.project.free.AllText.THER_AR_DELETED_GOODS;
import static ru.tubi.project.free.AllText.UNDERSTOOD_BIG;

public class ProviderCollectProductActivity extends AppCompatActivity
                implements View.OnClickListener {

    private TextView tvWarehouseInfo,tvBuyerInfo,tvApply,tvDeleteApply;
    private RecyclerView rvList;
    private Switch switchOnOffDelivery;
    private boolean deliveryFlag = true;
    private Intent takeit;
    private ProviderCollectProductAdapter adapter;
    private ProviderCollectProductDealAdapter adapDeal;
    private ArrayList<AcceptProductListProvidersModel> productList = new ArrayList<>();
    private ArrayList<ProviderCollectProductModel> productDealList = new ArrayList<>();
    private ArrayList<ProviderCollectProductModel> productDeletedList = new ArrayList<>();
    private ArrayList<Integer> checkedList = new ArrayList<>();
    private int providerWarehouse_id,order_partner_id, order_active,x=0, count=0;
    private AlertDialog.Builder adb;
    private AlertDialog ad;
    private UserModel userDataModel;
    private Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_collect_product);
        setTitle(LIST_PRODUCT);
        getSupportActionBar().setSubtitle(FOR_COLLECT);
        //Список товара для сборки поставщик

        rvList=findViewById(R.id.rvList);
        switchOnOffDelivery=findViewById(R.id.switchOnOffDelivery);
        tvWarehouseInfo=findViewById(R.id.tvWarehouseInfo);
        tvBuyerInfo=findViewById(R.id.tvBuyerInfo);
        tvApply=findViewById(R.id.tvApply);
        //tvDeleteApply=findViewById(R.id.tvDeleteApply);

        tvApply.setOnClickListener(this);
        //tvDeleteApply.setOnClickListener(this);

        takeit = getIntent();
        order_partner_id = takeit.getIntExtra("order_partner_id",x);
        order_active = takeit.getIntExtra("order_active",x);
        providerWarehouse_id = takeit.getIntExtra("warehouse_id",x);
        String myWarehousInfo = takeit.getStringExtra("myWarehousInfo");
        String stBuyersCompany= takeit.getStringExtra("stBuyersCompany");

        //получить из sqlLite данные пользователя и компании
        UserDataRecovery userDataRecovery = new UserDataRecovery();
        userDataModel = userDataRecovery.getUserDataRecovery(this);

        tvWarehouseInfo.setText(myWarehousInfo);
        tvBuyerInfo.setText(stBuyersCompany);

        //бесконечный цикл, для запроса изменений?
        int sleepSecond = 200;
        TimerTask t = new TimerTask() {
            @Override
            public void run() {
                Log.d("A111",getClass()+" / TimerTask / run");
                //getInfoToDeletedGoods();
                TimerMethod();
            }
        };
        timer.scheduleAtFixedRate(t,1000,sleepSecond *1000);

        switchOnOffDelivery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int logistic=0;
                if(isChecked){
                    switchOnOffDelivery.setText(R.string.delivery_is_needed);
                    deliveryFlag=true;
                    logistic=1;
                }else{
                    switchOnOffDelivery.setText(R.string.without_delivery);
                    deliveryFlag=false;
                    logistic=0;
                }
                //записать статус доставки
                writeAllLogisticToDB( logistic);
                //editLogisticProduct();

                //проверить доствка нужна и записать in DB
                //checkLogistic();
            }
        });

        ProviderCollectProductAdapter.OnCheckedChangeListener checked =
                new ProviderCollectProductAdapter.OnCheckedChangeListener() {
                    @Override
                    public void isChecked(View view, boolean flag, int position) {
                        //для показа заказа который не активен
                    }
                };
        adapter = new ProviderCollectProductAdapter(this,productList,checked);
        rvList.setAdapter(adapter);

        ProviderCollectProductDealAdapter.OnCheckedChangeListener check
                = new ProviderCollectProductDealAdapter.OnCheckedChangeListener() {
            @Override
            public void isChecked(View view, boolean flag, int position) {
                //Toast.makeText(ProviderCollectProductActivity.this, "position: "+position, Toast.LENGTH_SHORT).show();
               Log.d("A111","ProviderCollectProductActivity " +
                       "/ isChecked / flag="+flag+"; position="+position);
                whatCheckClicked(flag, position);
            }
        };
        ProviderCollectProductDealAdapter.OnClickListener click
                = new ProviderCollectProductDealAdapter.OnClickListener() {
            @Override
            public void isClicked(View v, int position) {
                //Toast.makeText(ProviderCollectProductActivity.this
                //       , "click = "+v+" position = "+position, Toast.LENGTH_SHORT).show();
                whatClicked(v, position);
                //adChengeDataInTheCollect(position);
            }
        };
        adapDeal = new ProviderCollectProductDealAdapter(
                this,productDealList,checkedList,check,click);
        rvList.setAdapter(adapDeal);

        if(order_active == 0){
            //заказ не активен = 0, показать список из заказа
            switchOnOffDelivery.setVisibility(View.GONE);
            tvApply.setVisibility(View.GONE);
            startProductToOrderList();
            rvList.setAdapter(adapter);
        }else {
            //заказ активен = 1, показать список из warehouse_inventory
            switchOnOffDelivery.setVisibility(View.VISIBLE);
            tvApply.setVisibility(View.VISIBLE);
            showProductForWorkToCollect();
            rvList.setAdapter(adapDeal);
        }

    }
    private void TimerMethod(){
        this.runOnUiThread(Timer_Tick);
    }
    private Runnable Timer_Tick = new Runnable() {
        public void run() {
            getInfoToDeletedGoods();
        }
    };

    private void whatClicked(View v, int position){
        String string=String.valueOf(v);
        String str[]=string.split("/");

        if(str[1].equals("tvQuantityColected}")) {
            adChengeDataInTheCollect(position);
        }else if(str[1].equals("ivImageProduct}")){
            adShowBigImage(position);
        }
    }
    private void whatCheckClicked(boolean flag, int position){
        if(productDealList.get(position).getProvider_stock_quantity() <
                productDealList.get(position).getQuantity_to_deal()){
        }else{
            int check=0;
            if (flag) check = 1;
            checkedList.set(position,check);

            tvApply.setBackgroundResource(R.drawable.round_corners_green_600_and_black);
            tvApply.setClickable(true);
        }
        adapter.notifyItemChanged(position);
    }
    @Override
    public void onClick(View v) {
        if(v.equals(tvApply)){
            //записать в таблицу собранные товары
            writeCheckToTable();
            //перепроверить доствка нужна и записать
            checkLogistic();
        }
    }
    //перепроверить доствка нужна и записать
    private void checkLogistic(){
        if(switchOnOffDelivery.isChecked()){
            int logistic=1;
            writeAllLogisticToDB(logistic);
        }
    }
    //записать товары в таблицу t_warehouse_inventory_in_out и в listProduct
    private void writeCheckToTable() {
        for (int i = 0; i < productDealList.size(); i++) {
            //if(checkedList.get(i) != productList.get(i).getChecked()){
            if(checkedList.get(i) == 1 && productDealList.get(i).getCollected_check() == 0){

                //сделать запись (collected) собранный товар в warehouse_inventory_in_out
                addProductForMoving(i);

                productDealList.get(i).setCollected_check(1);
                productDealList.get(i).setProvider_stock_quantity(
                        productDealList.get(i).getProvider_stock_quantity()
                      - productDealList.get(i).getQuantity_to_deal());
            }
        }
        tvApply.setBackgroundColor(TUBI_GREY_200);
        tvApply.setClickable(false);

        adapter.notifyDataSetChanged();
    }
    //откоректировать количество товара из-за удаления товара
    private void goCorrectedProductQuantityForCollect
                    (ProviderCollectProductModel product, int correctStatus){
        String url = Constant.PROVIDER_OFFICE;
        url += "corrected_product_quantity_for_collect";
        url += "&"+"deleted_goods_id="+product.getDeleted_goods_id();
        url += "&"+"order_partner_id="+order_partner_id;
        url += "&"+"product_inventory_id="+product.getProductInventory_id();
        url += "&"+"warehouse_inventory_id="+product.getWarehouse_inventory_id();
        url += "&"+"quantity_deleted_product="+product.getQuantity_deleted_product();
        url += "&"+"correct_status="+correctStatus;
        String whatQuestion = "corrected_product_quantity_for_collect";
        setInitialData(url, whatQuestion);
        Log.d("A111",getClass()+" / goCorrectedProductQuantityForCollect / url = "+url);
        //Обновить старт список
        //showProductForWorkToCollect();
    }
    //получить список товаров которые удалены в заказе
    private void showDeletedGoods(){
        String url = Constant.WAREHOUSE_OFFICE;
        url += "get_deleted_goods_list";
        url += "&"+"order_partner_id="+order_partner_id;
        String whatQuestion = "get_deleted_goods_list";
        setInitialData(url, whatQuestion);
        Log.d("A111",getClass()+" / showDeletedGoods / url = "+url);
    }
    //получить ответ, есть удаленные товары для обработки
    private void getInfoToDeletedGoods(){
        String url = Constant.WAREHOUSE_OFFICE;
        url += "get_deleted_goods_flag";
        url += "&"+"order_partner_id="+order_partner_id;
        String whatQuestion = "get_deleted_goods_flag";
        setInitialData(url, whatQuestion);
        Log.d("A111",getClass()+" / getInfoToDeletedGoods / url = "+url);
    }
    //поставщик откорректировал остаток в заказе(уменьшил)
    private void corectStockToWarehouse(double quantity_to_deal, double quantity_collect, int warehouse_inventory_id){
        String url = Constant.WAREHOUSE_OFFICE;
        url += "correct_stock_to_warehouse_to_order";
        url += "&"+"provider_warehouse_id="+providerWarehouse_id;//
        url += "&"+"warehouse_inventory_id="+warehouse_inventory_id;
        url += "&"+"quantity_to_deal="+quantity_to_deal;
        url += "&"+"quantity_collect="+quantity_collect;
        url += "&"+"user_uid="+userDataModel.getUid();
        String whatQuestion = "correct_stock_to_warehouse_to_order";
        setInitialData(url, whatQuestion);

        showProductForWorkToCollect();
    }
    //записать статус доставки
    private void writeAllLogisticToDB(int logistic){
        String url = Constant.PROVIDER_OFFICE;
        url += "write_this_deal_delivery_status";
        url += "&"+"order_partner_id="+order_partner_id;
        url += "&"+"logistic="+logistic;
        String whatQuestion = "write_this_deal_delivery_status";
        setInitialData(url, whatQuestion);

        //Обновить старт список
        showProductForWorkToCollect();
    }
    //запись в (t_warehouse_inventory_in_out and t_order_product_part) о том что товар собран(collected)
    private void addProductForMoving(int position){
        int warehouse_inventory_id = productDealList.get(position).getWarehouse_inventory_id();
        int product_inventory_id = productDealList.get(position).getProductInventory_id();
        int collected = 1;//1=yes

        String url = Constant.PROVIDER_OFFICE;
        url += "write_provider_collect_product_for_sale";
        url += "&"+"warehouse_inventory_id="+warehouse_inventory_id;
        url += "&"+"order_partner_id="+order_partner_id;
        url += "&"+"collected="+collected;
        String whatQuestion = "write_provider_collect_product_for_sale";
        setInitialData(url, whatQuestion);
    }

    //показать список из warehouse_inventory для сборки товара
    private void showProductForWorkToCollect(){
        String url = Constant.PROVIDER_OFFICE;
        url += "receive_list_product_for_collect";
        url += "&"+"order_partner_id="+order_partner_id;
        url += "&"+"providerWarehouse_id="+providerWarehouse_id;
        String whatQuestion = "receive_list_product_for_collect";
        setInitialData(url, whatQuestion);
    }
    //получить список продуктов для комплектации этому покупателю
    private void startProductToOrderList(){
        String url = Constant.PROVIDER_OFFICE;
        url += "receive_list_order_product";
        url += "&"+"order_partner_id="+order_partner_id;
        url += "&"+"providerWarehouse_id="+providerWarehouse_id;
        String whatQuestion = "receive_list_order_product";
        setInitialData(url, whatQuestion);
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
                asyncDialog.show();
                super.onPreExecute();
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            protected void onPostExecute(String result) {
                if(whatQuestion.equals("receive_list_order_product")){
                    splitResult(result);
                }
                else if(whatQuestion.equals("receive_list_product_for_collect")){
                    splitProductForCollectResult(result);
                }
                else if(whatQuestion.equals("correct_stock_to_warehouse_to_order")){
                    splitAnswerFromCorrectResult(result);
                }
                else if(whatQuestion.equals("get_deleted_goods_flag")){
                    splitAnswerFromDeletedGoodsResult(result);
                }
                else if(whatQuestion.equals("get_deleted_goods_list")){
                    //splitDeletedGoodsListResult(result);
                }
                //hide the dialog
                asyncDialog.dismiss();
            }
        };
        task.execute(url_get);
    }
    private void splitDeletedGoodsListResult(String result){
        Log.d("A111",getClass()+" / splitDeletedGoodsListResult / result = "+result);
        productDeletedList.clear();
        try{
            String[] res = result.split("<br>");
            String[] one_temp = res[0].split("&nbsp");
            if (one_temp[0].equals("error") || one_temp[0].equals("messege")) {
                Toast.makeText(this, "" + one_temp[1], Toast.LENGTH_LONG).show();
                adapDeal.notifyDataSetChanged();
                return;
            } else {
                for (int i = 0; i < res.length; i++) {
                    String[] temp = res[i].split("&nbsp");
                    int order_product_part_id = Integer.parseInt(temp[0]);
                    int productInventory_id = Integer.parseInt(temp[1]);
                    int warehouse_inventory_id = Integer.parseInt(temp[2]);
                    double quantity_full_orders = Double.parseDouble(temp[3]);
                    double quantity_deleted_product = Double.parseDouble(temp[4]);
                    int status_collect_provider = Integer.parseInt(temp[5]);
                    String product_info  = ""+temp[6];
                    int deleted_goods_id = Integer.parseInt(temp[7]);

                    ProviderCollectProductModel product
                            = new ProviderCollectProductModel(order_product_part_id
                            , productInventory_id, warehouse_inventory_id
                            , quantity_full_orders, quantity_deleted_product
                            , status_collect_provider, product_info, deleted_goods_id);
                    productDeletedList.add(product);
                }
            }
            adCorrectProductsQuantity();
        }catch(Exception ex){
            Log.d("A111","error. ProviderCollectProductActivity " +
                    "/ splitDeletedGoodsListResult \n"+ex.toString()+"\n"+result);
        }
    }
    private void splitAnswerFromDeletedGoodsResult(String result){
        Log.d("A111",getClass()+" / splitAnswerFromDeletedGoodsResult / result = "+result);
        int deletedGoods = Integer.parseInt(result);
        //если товары которые удалил покупатель есть и они еще не обработаны показать сообщение
        if(deletedGoods == 1){
            timer.cancel();
            adWhatDoToDeletedGoods();
        }
    }
    //проверить ответ сервера если не ок то обновить данные
    private void splitAnswerFromCorrectResult(String result){
        String[] res = result.split("<br>");
        String[] one_temp = res[0].split("&nbsp");
        if (one_temp[0].equals("command")) {
            if (one_temp[1].equals("NO_RESULT")) {
                adUpdateProductList();
            }
        }
    }
    // разобрать результат с сервера, список продуктов которые собраны для отправки и их колличество
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void splitProductForCollectResult(String result){
        productDealList.clear();
        checkedList.clear();
        int logisticKey = 1;
        boolean collectedFlag = true;
        //Toast.makeText(this, "res\n"+result, Toast.LENGTH_LONG).show();
        try{
            String[] res = result.split("<br>");
            String[] one_temp = res[0].split("&nbsp");
            if (one_temp[0].equals("error") || one_temp[0].equals("messege")) {
                Toast.makeText(this, "" + one_temp[1], Toast.LENGTH_LONG).show();
                adapDeal.notifyDataSetChanged();
                return;
            } else {
                for (int i = 0; i < res.length; i++) {
                    String[] temp = res[i].split("&nbsp");
                    int product_id = Integer.parseInt(temp[0]);
                    int productInventory_id = Integer.parseInt(temp[1]);
                    String category = temp[2];
                    String brand = temp[3];
                    String characteristic = temp[4];
                    String type_packaging = temp[5];
                    String unit_measure = temp[6];
                    int weight_volume = Integer.parseInt(temp[7]);
                    int quantity_package = Integer.parseInt(temp[8]);
                    String image_url = temp[9];
                    String storage_conditions=temp[10];

                    int warehouse_inventory_id = Integer.parseInt(temp[11]);
                    double quantity_to_deal = Double.parseDouble(temp[12]);
                    int logistic_product = Integer.parseInt(temp[13]);
                    int car_id = Integer.parseInt(temp[14]);

                    double provider_stock_quantity = Double.parseDouble(temp[15]);
                    int collected_check = Integer.parseInt(temp[16]);
                    long get_order_date_millis = Long.parseLong(temp[17]);
                    String product_name = temp[18];
                    int corrected = Integer.parseInt(temp[19]);
                    String description  = temp[20];
                    String product_name_from_provider  = temp[21];

                    ProviderCollectProductModel product_info
                            = new ProviderCollectProductModel(product_id, productInventory_id
                            , category, brand, characteristic, type_packaging , unit_measure
                            , weight_volume, quantity_package, image_url, storage_conditions
                            , warehouse_inventory_id, quantity_to_deal, logistic_product
                            , car_id, provider_stock_quantity, collected_check
                            , get_order_date_millis, product_name, corrected, description
                            ,product_name_from_provider);

                    productDealList.add(product_info);

                    //если товар без доставки склад склад
                    if(logistic_product == 0){
                        logisticKey = 0;
                    }
                    //все ли товары собраны
                    if(collected_check == 0){
                        collectedFlag = false;
                    }
                }
            }
        }catch(Exception ex){
            Log.d("A111","error. ProviderCollectProductActivity " +
                    "/ splitProductForCollectResult \n"+ex.toString()+"\n"+result);

            Toast.makeText(this, "Exception: ProviderCollectProductActivity " +
                    "/ splitProductForCollectResult\n"+ex, Toast.LENGTH_SHORT).show();
        }

        //отсортировать есть доставка или нет и добавить данные доставщика(склад это или транспорт)
        //сортируем лист по 2 полям (logistic_product и car_or_warehouse_id)
        productDealList.sort(Comparator.comparing(ProviderCollectProductModel::getCategory)
                .thenComparing(ProviderCollectProductModel::getCharacteristic));

        for(int i=0;i < productDealList.size();i++){
            checkedList.add(productDealList.get(i).getCollected_check());
        }
        //проверить доставку склад склад
        if(logisticKey == 1){
            switchOnOffDelivery.setChecked(true);
        }
        //если все товары собраны запретить менять доставку
        if(collectedFlag){
            switchOnOffDelivery.setClickable(false);
        }

        adapDeal.notifyDataSetChanged();
    }
    // разобрать результат с сервера, список продуктов посмотреть без обработки
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void splitResult(String result){
        productList.clear();
        checkedList.clear();
        //Toast.makeText(this, "hello\n"+result, Toast.LENGTH_SHORT).show();
        try{
            String[] res = result.split("<br>");
            String[] one_temp = res[0].split("&nbsp");
            if (one_temp[0].equals("error") || one_temp[0].equals("messege")) {
                Toast.makeText(this, "" + one_temp[1], Toast.LENGTH_LONG).show();
                adapter.notifyDataSetChanged();
                return;
            } else {
                for (int i = 0; i < res.length; i++) {
                    String[] temp = res[i].split("&nbsp");
                    int product_id = Integer.parseInt(temp[0]);
                    int productInventory_id = Integer.parseInt(temp[1]);
                    String category = temp[2];
                    String brand = temp[3];
                    String characteristic = temp[4];
                    String type_packaging = temp[5];
                    String unit_measure = temp[6];
                    int weight_volume = Integer.parseInt(temp[7]);
                    int quantity_package = Integer.parseInt(temp[8]);
                    String image_url = temp[9];

                    int order_product_id = Integer.parseInt(temp[10]);
                    double quantity_to_order = Double.parseDouble(temp[11]);
                    double partner_stock_quantity = Double.parseDouble(temp[12]);

                    int counterparty_id = Integer.parseInt(temp[13]);
                    String abbreviation = temp[14];
                    String counterparty = temp[15];
                    String storage_conditions=temp[16];
                    int checked = Integer.parseInt(temp[17]);
                    double quantity_of_colected = Double.parseDouble(temp[18]);

                    AcceptProductListProvidersModel delivery = new AcceptProductListProvidersModel(
                            product_id,productInventory_id, category,brand,characteristic,
                            type_packaging, unit_measure,weight_volume,quantity_package,
                            image_url,order_product_id,quantity_to_order,partner_stock_quantity,
                            counterparty_id, abbreviation, counterparty, storage_conditions,
                            checked, quantity_of_colected, order_active);

                    productList.add(delivery);

                }
            }
        }catch(Exception ex){
            Toast.makeText(this, "Exception: ProviderCollectProductActivity / splitResult\n"
                     +ex, Toast.LENGTH_SHORT).show();
        }
        //отсортировать есть доставка или нет и добавить данные доставщика(склад это или транспорт)
        //сортируем лист по 2 полям (logistic_product и car_or_warehouse_id)
        productList.sort(Comparator.comparing(AcceptProductListProvidersModel::getCategory)
                .thenComparing(AcceptProductListProvidersModel::getCharacteristic));

        for(int i=0;i < productList.size();i++){
            checkedList.add(productList.get(i).getChecked());
        }

        adapter.notifyDataSetChanged();
    }
    private void adMessegeToCorrectStockToWarehouse(double quantity_collect, int position){

        adb = new AlertDialog.Builder(this);
        String st1 = RESERVE_TO_WAREHOUSE_WILBEE_CORRECT;
        adb.setTitle(st1);
        adb.setMessage(""+RESERVE+" = "+quantity_collect);
        adb.setPositiveButton(PERFORM, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int warehouse_inventory_id = productDealList.get(position).getWarehouse_inventory_id();
                double quantity_to_deal = productDealList.get(position).getQuantity_to_deal();
                //поставщик откорректировал остаток в заказе(уменьшил).
                corectStockToWarehouse(quantity_to_deal, quantity_collect, warehouse_inventory_id);
            }
        });
        adb.setNeutralButton(CANCEL_BIG, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ad.cancel();
            }
        });

        ad=adb.create();
        ad.setCanceledOnTouchOutside(false);
        ad.setCancelable(false);
        ad.show();

        Button buttonbackground1 = ad.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonbackground1.setBackgroundColor(alert_dialog_button_green_pressed);
        buttonbackground1.setTextColor(Color.WHITE);
        Button buttonbackground2 = ad.getButton(DialogInterface.BUTTON_NEUTRAL);
        buttonbackground2.setBackgroundColor(TUBI_GREY_200);
        buttonbackground2.setTextColor(Color.WHITE);
    }
    //сообщить что данные о заказе этой позиции изменились
    private void adUpdateProductList(){
        adb = new AlertDialog.Builder(this);
        String st1 = ""+MES_34;
        String st2 = ""+MES_33;
        adb.setTitle(st1);
        adb.setMessage(st2);
        adb.setPositiveButton(UNDERSTOOD_BIG, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Обновить старт список
                showProductForWorkToCollect();
                ad.cancel();
            }
        });
        ad=adb.create();
        ad.setCanceledOnTouchOutside(false);
        ad.setCancelable(false);
        ad.show();
        Button buttonbackground1 = ad.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonbackground1.setBackgroundColor(alert_dialog_button_green_pressed);
        buttonbackground1.setTextColor(Color.WHITE);
    }
    private void adCorrectProductsQuantity(){
        adb = new AlertDialog.Builder(this);
        ListView lv = new ListView(this);
        ArrayAdapter adap;
        ArrayList productsDeletedList = new ArrayList();
        for(int i=0; i < productDeletedList.size();i++){
            ProviderCollectProductModel prm = productDeletedList.get(i);
            String str = prm.getProduct_info()+" \nв заказе-"+prm.getQuantity_full_orders()
                    +" удалить-"+prm.getQuantity_deleted_product();
            productsDeletedList.add(str);
        }
        adap = new ArrayAdapter(this, android.R.layout.simple_list_item_1, productsDeletedList);
        lv.setAdapter(adap);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("A111",getClass()+" / adCorrectProductsQuantity / i = "+i);
                //adCorrectedDeletedProduct(i);
            }
        });
        String st1 = "Список удаленных товаров";
        adb.setTitle(st1);
        adb.setView(lv);
        ad=adb.create();
        ad.show();
    }
    //окно коррекции количества товра если удален заказ
   /* private void adCorrectedDeletedProduct(int position){
        ProviderCollectProductModel product = productDeletedList.get(position);
        String str = product.getProduct_info()+" \nв заказе-"+product.getQuantity_full_orders()
                +" удалить-"+product.getQuantity_deleted_product();
        adb = new AlertDialog.Builder(this);
        String st1 = "Отредактировать количество товара";
        adb.setTitle(st1);
        adb.setMessage(str);
        adb.setPositiveButton(PERFORM, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int correctStatus = 1;//внести изменения=1;
                //goCorrectedProductQuantityForCollect(product, correctStatus);
                //Обновить старт список
                showProductForWorkToCollect();
                ad.cancel();
            }
        });
        adb.setNegativeButton(REJECTION_BIG, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int correctStatus = 2;//отказать в изменениях=2;
                //goCorrectedProductQuantityForCollect(product, correctStatus);
                //Обновить старт список
                showProductForWorkToCollect();
                ad.cancel();
            }
        });
        ad=adb.create();
        ad.setCanceledOnTouchOutside(false);
        ad.setCancelable(false);
        ad.show();

        Button buttonbackground1 = ad.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonbackground1.setBackgroundColor(alert_dialog_button_green_pressed);
        buttonbackground1.setTextColor(Color.WHITE);
        Button buttonbackground2 = ad.getButton(DialogInterface.BUTTON_NEGATIVE);
        buttonbackground2.setBackgroundColor(TUBI_GREY_200);
        buttonbackground2.setTextColor(Color.WHITE);

    }*/
    private void adShowBigImage(int position){
        ImageView iv = new ImageView(this);
        String imageUrl = productDealList.get(position).getImage_url();
        if(!imageUrl.equals("null")) {
            new DownloadImage(){
                @Override
                protected void onPostExecute(Bitmap result) {
                    new MakeImageToSquare(result,iv);
                }
            }
                    .execute(ADMIN_PANEL_URL_PREVIEW_IMAGES + imageUrl);
        }else iv.setImageResource(R.drawable.tubi_logo_no_image_300ps);
        adb = new AlertDialog.Builder(this);
        adb.setView(iv);
        ad=adb.create();
        ad.show();
    }
    //сообщить что есть в заказе удаленные клиентом товары
    private void adWhatDoToDeletedGoods(){
        adb = new AlertDialog.Builder(this);
        String st1 = THER_AR_DELETED_GOODS;
        adb.setTitle(st1);
        adb.setPositiveButton(SHOW_BIG, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getApplication()
                        , CorrectedQuantityFromDeletedOrdersActivity.class);
                intent.putExtra("order_partner_id",order_partner_id);
                startActivity(intent);
                //showDeletedGoods();
                timer.cancel();
                ad.cancel();
            }
        });
        adb.setNeutralButton(IGNORE_BIG, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                timer.cancel();
                ad.cancel();
            }
        });

        ad=adb.create();
        ad.setCanceledOnTouchOutside(false);
        ad.setCancelable(false);
        ad.show();

        Button buttonbackground1 = ad.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonbackground1.setBackgroundColor(alert_dialog_button_green_pressed);
        buttonbackground1.setTextColor(Color.WHITE);
        Button buttonbackground2 = ad.getButton(DialogInterface.BUTTON_NEUTRAL);
        buttonbackground2.setBackgroundColor(TUBI_GREY_200);
        buttonbackground2.setTextColor(Color.WHITE);
    }
    private void adChengeDataInTheCollect(int position){
        double stockOfGoods = productDealList.get(position).getProvider_stock_quantity();
        double quantityToDeal = productDealList.get(position).getQuantity_to_deal();

        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setGravity(Gravity.CENTER);
        ll.setPadding(0,50,0,0);

        LinearLayout llTwo = new LinearLayout(this);
        llTwo.setOrientation(LinearLayout.HORIZONTAL);
        llTwo.setGravity(Gravity.CENTER);
        llTwo.setPadding(0,20,0,0);

        TextView tvStock = new TextView(this);
        tvStock.setTextSize(20);
        tvStock.setTextColor(Color.BLACK);
        tvStock.setWidth(200);
        tvStock.setGravity(Gravity.CENTER);
        tvStock.setText(""+RESERVE_IN_WAAREHOUSE+"\n"+stockOfGoods);

        TextView tvMess = new TextView(this);
        tvMess.setTextSize(20);
        tvMess.setTextColor(Color.BLACK);
        tvMess.setText(""+COLLECTED);
        tvMess.setPadding(0,0,30,0);

        EditText etQuantity = new EditText(this);
        etQuantity.setLayoutParams(new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        etQuantity.setMinimumWidth(100);
        etQuantity.setInputType(InputType.TYPE_CLASS_NUMBER);

        llTwo.addView(tvMess);
        llTwo.addView(etQuantity);

        ll.addView(tvStock);
        ll.addView(llTwo);

        adb = new AlertDialog.Builder(this);
        String st1 = SPECIFY_QUANTITY_OF_COLLECTED_GOODS;
        adb.setTitle(st1);
        adb.setView(ll);
        //adb.setMessage(st2);
        adb.setPositiveButton(PERFORM, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!etQuantity.getText().toString().isEmpty()) {
                    double quantityToCollected = Double.parseDouble(etQuantity.getText().toString().trim());
                    if(quantityToCollected < quantityToDeal){
                        adMessegeToCorrectStockToWarehouse(quantityToCollected, position);
                    }else{
                        Toast.makeText(ProviderCollectProductActivity.this
                                , ""+ERROR_BIG, Toast.LENGTH_SHORT).show();
                        ad.cancel();
                    }
                }else{
                    ad.cancel();
                }
            }
        });
        adb.setNeutralButton(RETURN_BIG, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ad.cancel();
            }
        });

        ad=adb.create();
        ad.setCanceledOnTouchOutside(false);
        ad.setCancelable(false);
        ad.show();

        Button buttonbackground1 = ad.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonbackground1.setBackgroundColor(alert_dialog_button_green_pressed);
        buttonbackground1.setTextColor(Color.WHITE);
        Button buttonbackground2 = ad.getButton(DialogInterface.BUTTON_NEUTRAL);
        buttonbackground2.setBackgroundColor(TUBI_GREY_200);
        buttonbackground2.setTextColor(Color.WHITE);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        timer.cancel();
    }
}