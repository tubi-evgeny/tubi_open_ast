package ru.tubi.project.activity.company_my;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

import ru.tubi.project.R;
import ru.tubi.project.adapters.WriteOutProductAdapter;
import ru.tubi.project.models.CatalogProductProviderModel;
import ru.tubi.project.models.DecimalDigitsInputFilter;
import ru.tubi.project.models.ShopingBoxModel;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.models.WarehouseModel;
import ru.tubi.project.utilites.Constant;
import ru.tubi.project.utilites.InitialData;
import ru.tubi.project.utilites.UserDataRecovery;

import static ru.tubi.project.free.AllCollor.TUBI_BLACK;
import static ru.tubi.project.free.AllCollor.TUBI_GREY_200;
import static ru.tubi.project.free.AllCollor.alert_dialog_button_green_pressed;
import static ru.tubi.project.free.AllText.DONE_BIG;
import static ru.tubi.project.free.AllText.MAXIMUM;
import static ru.tubi.project.free.AllText.NO_PRODUCT_HAS_BEEN_SELECTED;
import static ru.tubi.project.free.AllText.QUANTITY_ADD;
import static ru.tubi.project.free.AllText.QUANTITY_FREE;
import static ru.tubi.project.free.AllText.QUANTITY_IS_NOT;
import static ru.tubi.project.free.AllText.RETURN_BIG;
import static ru.tubi.project.free.AllText.TO_ORDER;
import static ru.tubi.project.free.AllText.WRITE_OUT;
import static ru.tubi.project.free.AllText.WRITE_OUT_PRODUCT;
import static ru.tubi.project.utilites.Constant.PROVIDER_OFFICE;

public class WriteOutProductActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private EditText etSearchTextInList;
    private LinearLayout llChengeList, llGoShopBox;
    private TextView tvWarehouse_info, tvQuantityToShopingBox;
    private Intent intent, takeit;
    double  freeQuantityDB;
    private int buyer_user_id, buyer_order_id, myPosition,partner_warehouse_id;
    private String buyer_comp_info="", partner_warehouse_info="";
    private ArrayList<Integer> warehouseIdList;
    private ArrayList<WarehouseModel> warehousesList;
    private ArrayList<CatalogProductProviderModel> startListProduct = new ArrayList<>();
    private ArrayList<CatalogProductProviderModel> listProduct = new ArrayList<>();
    private ArrayList<CatalogProductProviderModel> parseListProduct = new ArrayList<>();
    private ArrayList<CatalogProductProviderModel> writeOutProductList = new ArrayList<>();
    private ArrayList<ShopingBoxModel> shopinBoxArray = new ArrayList<>();
    private AlertDialog.Builder adb;
    private AlertDialog ad;
    private WriteOutProductAdapter adapter;
    private boolean startListShowFlag = true;
    private UserModel userDataModel;

    public static final int WRITE_OUT_PRODUCT_ACTIVITY = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_out_product);
        setTitle(WRITE_OUT_PRODUCT);//Выписать товар

        recyclerView = (RecyclerView) findViewById(R.id.rvList);
        llGoShopBox = findViewById(R.id.llGoShopBox);
        etSearchTextInList = findViewById(R.id.etSearchTextInList);
        tvWarehouse_info = findViewById(R.id.tvWarehouse_info);
        tvQuantityToShopingBox = findViewById(R.id.tvQuantityToShopingBox);

        llGoShopBox.setOnClickListener(this);

        //получить из sqlLite данные пользователя и компании
        UserDataRecovery userDataRecovery = new UserDataRecovery();
        userDataModel = userDataRecovery.getUserDataRecovery(this);


        takeit = getIntent();
        buyer_order_id = takeit.getIntExtra("buyer_order_id",0 );
        partner_warehouse_id = takeit.getIntExtra("partner_warehouse_id",0);
        buyer_comp_info = takeit.getStringExtra("info");
        partner_warehouse_info=takeit.getStringExtra("partner_warehouse_info");
        tvWarehouse_info.setText("№"+buyer_order_id+" "+buyer_comp_info);


        WriteOutProductAdapter.RecyclerViewClickListener clickListener =
                new WriteOutProductAdapter.RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        WhatButtonClicked(view, position);
                       // Toast.makeText(WriteOutProductActivity.this, "view: "+view
                        //        +"\nposition: "+position, Toast.LENGTH_SHORT).show();
                    }
                };
        etSearchTextInList.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {      }
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getNeedProduct();
            }
            @Override
            public void afterTextChanged(Editable s) {            }
        });
        adapter = new WriteOutProductAdapter(this, listProduct, clickListener);
        recyclerView.setAdapter(adapter);

        receiveMyWarehouse();//получаем склады а затем товары на всех складах receiveProducts();
        receiveQuantityPositionToOrder();
    }
    @Override
    public void onClick(View v) {
        if(v.equals(llGoShopBox)){
            if(Integer.parseInt(tvQuantityToShopingBox.getText().toString()) == 0){
                Toast.makeText(this, ""+NO_PRODUCT_HAS_BEEN_SELECTED, Toast.LENGTH_LONG).show();
            }else {
                intent = new Intent(this, ProductInvoiceCreateActivity.class);
                intent.putExtra("buyer_order_id", buyer_order_id);
                intent.putExtra("key", WRITE_OUT_PRODUCT_ACTIVITY);
                intent.putExtra("buyer_comp_info", buyer_comp_info);
                intent.putExtra("partner_warehouse_id", partner_warehouse_id);
                intent.putExtra("partner_warehouse_info",partner_warehouse_info);
                startActivity(intent);
            }
        }
    }
    public void WhatButtonClicked(View view, int position) {    //какая кнопка нажата
        myPosition = position;
        String string = String.valueOf(view);
        String str[] = string.split("/");

        if (str[1].equals("llProdInfo}")) {
            alertDialogWriteOutProduct(position);
            //Toast.makeText(this, "ivImageProduct", Toast.LENGTH_SHORT).show();
        }
    }
    //подбор товаров по запросу из editText
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getNeedProduct() {
        //Toast.makeText(this, "click edit: ", Toast.LENGTH_SHORT).show();
        listProduct.clear();
        parseListProduct.clear();
        parseListProduct.addAll(startListProduct);

        //меняем регистр букв на нижний
        String searchSimbol = etSearchTextInList.getText().toString();
        //Toast.makeText(this, "searchSimbol: " + searchSimbol, Toast.LENGTH_SHORT).show();
        for (int i = 0; i < parseListProduct.size(); i++) {

            ArrayList<String> forParse = new ArrayList<>();
            //берем строку и меняем регистр букв на нижний
            forParse.add(parseListProduct.get(i).toString().toLowerCase());
            ArrayList<String> res;
            //ведем поиск строки по запросу (ищем совпадения)
            res = (ArrayList<String>) forParse.stream().filter(lang -> lang.contains(searchSimbol)).collect(Collectors.toList());
            if (res.size() != 0) {
                //пишем в новый каталог
                listProduct.add(parseListProduct.get(i));
            }
        }
        adapter.notifyDataSetChanged();
    }
    //показать колличество товаров в корзине
    private void outputQuantityPositionToShopBox(int count){
        if(count > 0){
            llGoShopBox.setBackgroundResource(R.drawable.soping_box_green_61ps);
        }else{
            llGoShopBox.setBackgroundResource(R.drawable.shoping_box_60ps);
        }
        tvQuantityToShopingBox.setText(""+count);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void receiveProducts(){
        listProduct.clear();
        for(int i=0;i < warehousesList.size();i++){
            if(warehousesList.get(i).getWarehouse_type().equals("provider")) {
                String url = PROVIDER_OFFICE;
                url += "&" + "product_provider_array";
                url += "&" + "tax_id=" + userDataModel.getCompany_tax_id();//MY_COMPANY_TAXPAYER_ID;
                url += "&" + "warehouse_id=" + warehousesList.get(i).getWarehouse_id();
                String whatQuestion = "product_provider_array";
                setInitialData(url, whatQuestion);
            }
        }
    }

    //добавить продукт и колличество в заказ
    private void addOrderProduct(double myQuantity, int position){
        String whatQuestion = "add_order_product";
        String url_get= Constant.ADD_ORDER_PRODUCT;
        url_get += "&"+"order_id="+buyer_order_id;
        url_get += "&"+"product_inventory_id=" + listProduct.get(position).getProduct_inventory_id();
        url_get += "&"+"quantity="+myQuantity;
        setInitialData(url_get, whatQuestion);
    }
    //получить список складов хранения своего товара
    private void receiveMyWarehouse(){
        //Toast.makeText(this, "tax_id: "+MY_COMPANY_TAXPAYER_ID, Toast.LENGTH_SHORT).show();
        String url = PROVIDER_OFFICE;
        url += "&" + "receive_my_warehouse";
        url += "&" + "counterparty_tax_id=" + userDataModel.getCompany_tax_id();//MY_COMPANY_TAXPAYER_ID;
        String whatQuestion = "receive_my_warehouse";
        setInitialData(url, whatQuestion);
    }
    //получить колличество позиций товара в заказе(для кнопки в коркину)
    private void receiveQuantityPositionToOrder(){
        String url = PROVIDER_OFFICE;
        url += "&" + "receive_quantity_position_to_order";
        url += "&" + "order_id=" + buyer_order_id;
        String whatQuestion = "receive_quantity_position_to_order";
        setInitialData(url, whatQuestion);
    }
    private void setInitialData(String url_get, String whatQuestion) {
        InitialData task = new InitialData() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            protected void onPostExecute(String result) {
                if (whatQuestion.equals("receive_my_warehouse")) {
                    splitWarehouseResult(result);
                }else if (whatQuestion.equals("product_provider_array")) {
                    splitResult(result);
                }else if(whatQuestion.equals("add_order_product")){
                    showMessege(result);
                    receiveQuantityPositionToOrder();
                }else if (whatQuestion.equals("receive_quantity_position_to_order")) {
                    splitQuantityPositionToOrderResult(result);
                }
            }
        };
        task.execute(url_get);
    }
    //разобрать колличество позиций товара в заказе(для кнопки в коркину)
    private void splitQuantityPositionToOrderResult(String result){
        //Toast.makeText(this, "res: "+result, Toast.LENGTH_SHORT).show();
        try {
             outputQuantityPositionToShopBox(Integer.parseInt(result));
        }catch (Exception ex){       }
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
                    //products.get(myPosition).setQuantity(freeQuantityDB);
                    //adapter.notifyItemChanged(myPosition);
                    Toast.makeText(this, "" + temp[2] + " \n" + MAXIMUM + ": " + freeQuantityDB, Toast.LENGTH_SHORT).show();
                }
            }
        }catch (Exception ex){

        }
    }
    // разобрать результат с сервера список продуктов и колличество
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void splitResult(String result) {
        try {
            String[] res = result.split("<br>");
            String[] one_temp = res[0].split("&nbsp");
            if (one_temp[0].equals("error") || one_temp[0].equals("messege")) {
                Toast.makeText(this, "" + one_temp[1], Toast.LENGTH_LONG).show();
                return;
            } else {
                for (int i = 0; i < res.length; i++) {
                    String[] temp = res[i].split("&nbsp");

                    int product_id = Integer.parseInt(temp[0]);
                    int product_inventory_id = Integer.parseInt(temp[1]);
                    String category = temp[2];
                    String brand = temp[3];
                    String characteristic = temp[4];
                    String type_packaging = temp[5];
                    String unit_measure = temp[6];
                    int weight_volume = Integer.parseInt(temp[7]);
                    double total_quantity = 0;
                    double price = Double.parseDouble(temp[8]);
                    int quantity_package = Integer.parseInt(temp[9]);
                    String image_url = temp[10];
                    String description = temp[11];
                    double total_sale_quantity = 0;
                    double free_balance = Double.parseDouble(temp[12]);
                    String product_name = temp[13];

                    if (product_id != 0) {
                        CatalogProductProviderModel products = new CatalogProductProviderModel(product_id,
                                product_inventory_id, category, product_name, brand, characteristic,
                                type_packaging, unit_measure,
                                weight_volume, total_quantity, price, quantity_package, image_url, description,
                                total_sale_quantity, free_balance);
                        //Toast.makeText(this, ""+temp[0], Toast.LENGTH_SHORT).show();
                        listProduct.add(products);
                    }
                }
                //сортируем лист по 4 полям (getCategory , getProduct_name, getCharacteristic, getBrand )
                listProduct.sort(Comparator.comparing(CatalogProductProviderModel::getCategory)
                        .thenComparing(CatalogProductProviderModel::getProduct_name)
                        .thenComparing(CatalogProductProviderModel::getCharacteristic)
                        .thenComparing(CatalogProductProviderModel::getBrand));
                startListProduct.addAll(listProduct);
                adapter.notifyDataSetChanged();
            }
        }catch (Exception ex){}
    }
    // разобрать результат с сервера список складов
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void splitWarehouseResult(String result) {
        //Toast.makeText(this, "res:\n"+result, Toast.LENGTH_SHORT).show();
        warehouseIdList= new ArrayList<>();
        warehousesList = new ArrayList<>();
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
                    int warehouse_id = Integer.parseInt(temp[1]);
                    String warehouse_type = temp[2];
                    WarehouseModel warehouse = new WarehouseModel(warehouse_info_id,warehouse_id
                                                                    ,warehouse_type);

                    warehousesList.add(warehouse);
                    warehouseIdList.add(warehouse_id);
                }
                receiveProducts();
            }
        }catch (Exception ex){
            Toast.makeText(this, "ex: "+ex, Toast.LENGTH_SHORT).show();
        }
    }
    private void alertDialogWriteOutProduct(int position){
        GridLayout gridLayout = new GridLayout(this);
        TextView tvQuantityFree = new TextView(this);
        TextView tvQuantityFreeSum = new TextView(this);
        EditText etQuantityAdd = new EditText(this);
        TextView tvQuantity = new TextView(this);

        tvQuantityFree.setText(""+QUANTITY_FREE);
        tvQuantityFree.setTextColor(TUBI_BLACK);
        tvQuantityFree.setTextSize(20);

        tvQuantityFreeSum.setText(""+listProduct.get(myPosition).getFree_balance());
        tvQuantityFreeSum.setTextColor(TUBI_BLACK);
        tvQuantityFreeSum.setTextSize(20);

        tvQuantity.setText(""+QUANTITY_ADD+"\n"+TO_ORDER);
        etQuantityAdd.setHint("0.00");
        etQuantityAdd.setTextSize(20);
        //etQuantityAdd.setGravity(Gravity.RIGHT);
        etQuantityAdd.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(8,2)});
        etQuantityAdd.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        tvQuantity.setTextColor(TUBI_BLACK);
        tvQuantity.setTextSize(20);

        gridLayout.setColumnCount(2);
        gridLayout.setRowCount(2);

        gridLayout.addView(tvQuantityFree, new GridLayout.LayoutParams(
                GridLayout.spec(0, GridLayout.CENTER),
                GridLayout.spec(0, GridLayout.RIGHT)));
        gridLayout.addView(tvQuantityFreeSum, new GridLayout.LayoutParams(
                GridLayout.spec(0, GridLayout.CENTER),
                GridLayout.spec(1, GridLayout.CENTER)));
        gridLayout.addView(tvQuantity, new GridLayout.LayoutParams(
                GridLayout.spec(1, GridLayout.CENTER),
                GridLayout.spec(0, GridLayout.RIGHT)));
        gridLayout.addView(etQuantityAdd, new GridLayout.LayoutParams(
                GridLayout.spec(1, GridLayout.CENTER),
                GridLayout.spec(1, GridLayout.CENTER)));

        adb = new AlertDialog.Builder(this);
        String st1 = WRITE_OUT;
        CatalogProductProviderModel product = listProduct.get(myPosition);
        String description = product.getCategory()+" "+product.getBrand()+" "+product.getCharacteristic()+" "
                +product.getType_packaging()+" "+product.getWeight_volume()+" "+product.getUnit_measure();

        adb.setTitle(st1);
        adb.setMessage(description);
        adb.setView(gridLayout);

        adb.setPositiveButton(DONE_BIG, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(etQuantityAdd.getText().length() != 0){
                    double myQuantity = Double.parseDouble(etQuantityAdd.getText().toString());
                    addOrderProduct(myQuantity, position);
                }else {
                    Toast.makeText(WriteOutProductActivity.this,
                            ""+QUANTITY_IS_NOT, Toast.LENGTH_SHORT).show();
                    //ad.cancel();
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

    @Override
    protected void onRestart() {
        super.onRestart();
        receiveQuantityPositionToOrder();
    }
}