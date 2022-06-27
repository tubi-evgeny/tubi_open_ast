package ru.tubi.project.activity.company_my;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ru.tubi.project.R;
import ru.tubi.project.activity.ActivityCatalog;
import ru.tubi.project.activity.CompanyDateFormActivity;
import ru.tubi.project.activity.MakingOrderActivity;
import ru.tubi.project.adapters.ProductInvoiceCreateAdapter;
import ru.tubi.project.adapters.ShopingBoxAdapter;
import ru.tubi.project.models.ShopingBoxModel;
import ru.tubi.project.utilites.Constant;
import ru.tubi.project.utilites.HelperDB;
import ru.tubi.project.utilites.InitialData;
import ru.tubi.project.utilites.SearchOrder_id;

import static ru.tubi.project.Config.ORDER_ID;
import static ru.tubi.project.free.AllText.CREATE;
import static ru.tubi.project.free.AllText.FOR_REGISTRATION_ORDER_NEED;
import static ru.tubi.project.free.AllText.GO_TO_DESIGN;
import static ru.tubi.project.free.AllText.INVOICE_PRODUCT;
import static ru.tubi.project.free.AllText.LOAD_TEXT;
import static ru.tubi.project.free.AllText.MAXIMUM;
import static ru.tubi.project.free.AllText.MES_1;
import static ru.tubi.project.free.AllText.ONE_STEP_LEFT;
import static ru.tubi.project.free.AllText.RUB;
import static ru.tubi.project.free.AllText.SHOPING_BOX;
import static ru.tubi.project.free.AllText.SHOPING_BOX_EMPTY;

public class ProductInvoiceCreateActivity extends AppCompatActivity
        implements View.OnClickListener{

    TextView tvWarehouse_info, tvBtnOrder, tvAddProduct;
    Intent intent, takeit;
    SearchOrder_id searchOrder_id = new SearchOrder_id();
    private RecyclerView recyclerView;
    private ProductInvoiceCreateAdapter adapter;
    private String url_get;
    private String whatQuestion;
    private int myPosition;
    private ArrayList<ShopingBoxModel> shopinBoxArray = new ArrayList<>();
    double quantity, summ, freeQuantityDB;
    private AlertDialog.Builder adb;
    private AlertDialog ad;
    private String counterparty,taxpayerID;
    SQLiteDatabase sqdb;
    private HelperDB my_db;
    private String st1 = FOR_REGISTRATION_ORDER_NEED;//Для оформления заказа нам потребуется
    private String stNextAlert = ONE_STEP_LEFT;//Остался один шаг!
    private static final int COMPANY_DATE_FORM_REQUEST_CODE = 0;

    private int invoice_order_id, partner_warehouse_id;
    private String buyer_comp_info="", partner_warehouse_info="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_invoice_create);
        setTitle(CREATE);//создание накладная товара
        getSupportActionBar().setSubtitle(INVOICE_PRODUCT);

        recyclerView=(RecyclerView)findViewById(R.id.rvList);
        tvWarehouse_info = findViewById(R.id.tvWarehouse_info);
        tvBtnOrder = findViewById(R.id.tvBtnOrder);
        tvAddProduct = findViewById(R.id.tvAddProduct);

        tvBtnOrder.setOnClickListener(this);
        tvAddProduct.setOnClickListener(this);

        takeit = getIntent();
        invoice_order_id = takeit.getIntExtra("buyer_order_id",0);
        buyer_comp_info = takeit.getStringExtra("buyer_comp_info");
        partner_warehouse_id=takeit.getIntExtra("partner_warehouse_id",0);
        partner_warehouse_info=takeit.getStringExtra("partner_warehouse_info");

        tvWarehouse_info.setText("№"+invoice_order_id+" "+buyer_comp_info);
        tvBtnOrder.setText(""+SHOPING_BOX_EMPTY);

      /*  ProductInvoiceCreateAdapter.OnShopingBoxClickListener shopingBoxClickListener =
                new ProductInvoiceCreateAdapter.OnShopingBoxClickListener() {
                    @Override
                    public void onShopingBoxClick(ShopingBoxModel shopingBox, int position) {

                    }
                };*/

        ProductInvoiceCreateAdapter.RecyclerViewClickListener clickListener =
                new ProductInvoiceCreateAdapter.RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        WhatButtonClicked(view,position);
                    }
                };

            showShopingBox();// показать товары в корзине
            adapter=new ProductInvoiceCreateAdapter(this, shopinBoxArray,clickListener);

            recyclerView.setAdapter(adapter);
    }
    public void WhatButtonClicked(View view,int position){    //какая кнопка нажата
        String string=String.valueOf(view);
        String str[]=string.split("/");
        int x=str.length;
        quantity = shopinBoxArray.get(position).getQuantity();
        myPosition = position;
        if(str[1].equals("btnPlus}")) {
            shopinBoxArray.get(position).setQuantity(quantity+1);
            addOrderProduct(shopinBoxArray.get(position).getQuantity(), position);
        }
        if(str[1].equals("btnMinus}")){
            //отнять один товар из заказа)
            quantity--;
            if(quantity > 0){
                shopinBoxArray.get(position).setQuantity(quantity);
                addOrderProduct(shopinBoxArray.get(position).getQuantity(), position);
            }
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
        //добавить товар
        if(v.equals(tvAddProduct)){
            onBackPressed();
        }//ОФОРМЛЕНИЕ ЗАКАЗА
        else if(v.equals(tvBtnOrder)){
            makingOrder();
        }
    }
    // данные есть оформляем заказ
    private void makingOrder(){
        int count=0, veight = 0;
        double priceSumm=0;
        for(int i = 0;i<shopinBoxArray.size();i++){

            double quantity=shopinBoxArray.get(i).getQuantity();
            veight +=quantity * shopinBoxArray.get(i).getWeight_volume();

            priceSumm += quantity * shopinBoxArray.get(i).getPrice();
            count = i+1;
        }
        Intent intent = new Intent(this, InvoiceCreateActivity.class);
        intent.putExtra("invoice_order_id",invoice_order_id);
        intent.putExtra("count",count);
        intent.putExtra("veight",veight);
        intent.putExtra("priceSumm", priceSumm);
        startActivity(intent);
       // finish();
    }
    //получить список продуктов в корзине и их колличество
    private void showShopingBox(){
        url_get = Constant.SHOW_SHOPING_BOX;
        url_get += "show_products";
        url_get += "&"+"order_id="+invoice_order_id;//ORDER_ID;
        whatQuestion = "show_products";
        setInitialData(url_get,whatQuestion);

    }
    private void deleteOrderProduct(int order_product_id){
        whatQuestion = "delete_order_product";
        url_get = Constant.DELETE_ORDER_PRODUCT;
        url_get += "&" + "order_product_id="+order_product_id;
        setInitialData(url_get, whatQuestion);
    }
    //добавить продукт и колличество в заказ
    private void addOrderProduct(double myQuantity, int position){
        whatQuestion = "add_order_product";
        url_get= Constant.ADD_ORDER_PRODUCT;
        url_get += "&"+"order_id="+invoice_order_id;//ORDER_ID;
        url_get += "&"+"product_inventory_id=" + shopinBoxArray.get(position).getProduct_inventory_id();
        url_get += "&"+"quantity="+myQuantity;
        setInitialData(url_get, whatQuestion);
    }

    private void setInitialData(String url_get, String whatQuestion) {
        ProgressDialog asyncDialog = new ProgressDialog(this);

        InitialData task=new InitialData(){
            @Override
            protected void onPreExecute() {
                //if(whatQuestion.equals("show_products")){
                    asyncDialog.setMessage(LOAD_TEXT);
                    asyncDialog.show();
                //}
                super.onPreExecute();
            }

            protected void onPostExecute(String result) {
                //Do your thing
                if(whatQuestion.equals("show_products")){
                    splitResultShoringBoxArray(result);
                }else if(whatQuestion.equals("delete_order_product")){
                    //-----проверить order_id (в заказе может не осталось товара)
                    if(result.equals("order_id=0") ){

                        recyclerView.setVisibility(View.GONE);
                    }else{
                        showShopingBox();
                    }
                }else if(whatQuestion.equals("add_order_product")){
                    showMessege(result);
                }
                //скрыть диалоговое окно
                asyncDialog.dismiss();
            }
        };
        task.execute(url_get);
    }
    private void showMessege(String result){
        try {
            String[] res = result.split("<br>");
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
    // разобрать результат с сервера список продуктов и колличество
    private void splitResultShoringBoxArray(String result){
        try {
            Log.d("A111","ProductInvoiceCreateActivity / splitResultShoringBoxArray\n"
                    +"result\n"+result);
            shopinBoxArray.clear();
            String[] res = result.split("<br>");
            String[] one_temp = res[0].split("&nbsp");
            if (one_temp[0].equals("NO_PRODUCT")) {
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
                    String description = temp[10];
                    String counterparty = temp[11];
                    double quantity = Double.parseDouble(temp[12]);

                    ShopingBoxModel shopingBox = new ShopingBoxModel(order_product_id, product_id,
                            product_inventory_id, category, brand, characteristic, unit_measure, weight_volume,
                            price, image_url, description, counterparty, quantity);
                    if (quantity > 0) {
                        shopinBoxArray.add(shopingBox);
                    } else {
                        deleteOrderProduct(order_product_id);
                    }
                }
                allSumm();
                //adapter.notifyDataSetChanged();
            }
        }catch (Exception ex){
            Log.d("A111","error. \nProductInvoiceCreateActivity / splitResultShoringBoxArray\n"
                    +ex+"\nresult\n"+result);
            //Toast.makeText(this, "Ex: "+ex, Toast.LENGTH_SHORT).show();
        }
    }
    //стоимость товаров в корзине
    private void allSumm(){
        summ = 0;
        for(int i=0;i < shopinBoxArray.size(); i++){
            summ += shopinBoxArray.get(i).getPrice() * shopinBoxArray.get(i).getQuantity();
        }
        tvBtnOrder.setText(String.format("%.2f", +summ)+" "+RUB+"    "+ GO_TO_DESIGN);
    }
    // данные получены то пойдем оформлять заказ
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // убедитесь, что это возврат с хорошим результатом
        if (requestCode == COMPANY_DATE_FORM_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
               makingOrder();
            }
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}