package ru.tubi.project.activity.Partner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import ru.tubi.project.R;
import ru.tubi.project.adapters.PartnerCollectProductAdapter;
import ru.tubi.project.models.AcceptProductListProvidersModel;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.utilites.DownloadImage;
import ru.tubi.project.utilites.InitialData;

import java.util.ArrayList;
import java.util.Comparator;

import ru.tubi.project.utilites.Constant;
import ru.tubi.project.utilites.MakeImageToSquare;
import ru.tubi.project.utilites.UserDataRecovery;

import static ru.tubi.project.Config.ADMIN_PANEL_URL_PREVIEW_IMAGES;
import static ru.tubi.project.Config.MY_UID;
import static ru.tubi.project.free.AllCollor.TUBI_GREEN_600;
import static ru.tubi.project.free.AllCollor.TUBI_GREY_200;
import static ru.tubi.project.free.AllText.FOR_COLLECT;
import static ru.tubi.project.free.AllText.I_UNDERSTAND_SMOL;
import static ru.tubi.project.free.AllText.LIST_PRODUCT;

public class PartnerCollectProductActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvWarehouseInfo,tvBuyerInfo,tvApply,tvDeleteApply;
    private LinearLayout llBuyerInfo;
    private RecyclerView rvList;
    private Intent takeit;
    private PartnerCollectProductAdapter adapter;
    private ArrayList<AcceptProductListProvidersModel> productList = new ArrayList<>();
    private ArrayList<Integer> checkedList = new ArrayList<>();
    private int providerWarehouse_id,order_id, deliveryKey, x=0 ;
    private String stBuyersCompany, address_for_delivery;
    private UserModel userDataModel;
    private AlertDialog.Builder adb;
    private AlertDialog ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_collect_product);
        setTitle(LIST_PRODUCT);
        getSupportActionBar().setSubtitle(FOR_COLLECT);
        //Список товара для сборки партнер

        rvList=findViewById(R.id.rvList);
        llBuyerInfo=findViewById(R.id.llBuyerInfo);
        tvWarehouseInfo=findViewById(R.id.tvWarehouseInfo);
        tvBuyerInfo=findViewById(R.id.tvBuyerInfo);
        tvApply=findViewById(R.id.tvApply);
        tvDeleteApply=findViewById(R.id.tvDeleteApply);

        llBuyerInfo.setOnClickListener(this);
        tvApply.setOnClickListener(this);
        tvDeleteApply.setOnClickListener(this);

        takeit = getIntent();
        order_id = takeit.getIntExtra("order_id",0);
        deliveryKey = takeit.getIntExtra("deliveryKey",0);
        providerWarehouse_id = takeit.getIntExtra("warehouse_id",0);
        String myWarehousInfo = takeit.getStringExtra("myWarehousInfo");
        stBuyersCompany= takeit.getStringExtra("stBuyersCompany");
        int order_deleted=takeit.getIntExtra("order_deleted",0);

        //получить из sqlLite данные пользователя и компании
        UserDataRecovery userDataRecovery = new UserDataRecovery();
        userDataModel = userDataRecovery.getUserDataRecovery(this);

        tvWarehouseInfo.setText(myWarehousInfo);
        tvBuyerInfo.setText(stBuyersCompany);
        if(order_deleted == 0){
            tvDeleteApply.setVisibility(View.GONE);
            tvDeleteApply.setBackgroundColor(TUBI_GREEN_600);
        }else{
            tvApply.setVisibility(View.GONE);
        }

        //получить список продуктов для комплектации этому покупателю
        startProductToOrderList();

        if(deliveryKey == 1){
            receiveDeliveryaddress();
        }

        PartnerCollectProductAdapter.OnCheckedChangeListener checked =
                new PartnerCollectProductAdapter.OnCheckedChangeListener() {
                    @Override
                    public void isChecked(View view, boolean flag, int position) {
                        whatCheckClicked(flag, position);
                    }
                };
        PartnerCollectProductAdapter.OnClickListener click =
                new PartnerCollectProductAdapter.OnClickListener() {
                    @Override
                    public void isClicked(View v, int position) {
                        adShowBigImage(position);
                    }
                };
        adapter = new PartnerCollectProductAdapter(this,productList,checkedList,checked,click);
        rvList.setAdapter(adapter);
    }
    private void whatCheckClicked(boolean flag, int position){
        if(productList.get(position).getPartner_stock_quantity() <
                productList.get(position).getQuantity_to_order()){
            adapter.notifyItemChanged(position);
        }else{
            int check=0;
            if (flag) check = 1;
            checkedList.set(position,check);

            adapter.notifyItemChanged(position);

            //tvApply.setBackgroundColor(TUBI_GREEN_600);
            tvApply.setBackgroundResource(R.drawable.round_corners_green_600_and_black);
            tvApply.setClickable(true);
        }
    }
    @Override
    public void onClick(View v) {
        if(v.equals(tvApply)){
            writeCheckToTable();
        }
        else if(v.equals(tvDeleteApply)){
            clearAllProductCollect();
        }
        else if(v.equals(llBuyerInfo)){
            adShowDeliveriAddress();
        }
    }
    //записать товары в таблицу t_warehouse_inventory_in_out и в listProduct
    private void writeCheckToTable() {

        for (int i = 0; i < productList.size(); i++) {
            //if(checkedList.get(i) != productList.get(i).getChecked()){
            if(checkedList.get(i) == 1 && productList.get(i).getChecked() == 0){
                //добавить собранный товар в warehouse_inventory_in_out
                //найти ключ документа invoice_key_id
                int invoice_key_id = checkInvoiceKeyThisOrder();
                //или создать ключ документа и товарный чек
                addProductForMoving(i, invoice_key_id);
                productList.get(i).setChecked(1);
                productList.get(i).setPartner_stock_quantity(
                        productList.get(i).getPartner_stock_quantity() -
                                productList.get(i).getQuantity_to_order());
            }
        }
        tvApply.setBackgroundColor(TUBI_GREY_200);
        tvApply.setClickable(false);

         adapter.notifyDataSetChanged();
    }
    //найти ключ документа invoice_key_id
    private int checkInvoiceKeyThisOrder(){
        int invoice_key_id=0;
      //  boolean keyFlag = false;
        for(int i=0;i < productList.size();i++){
            if(productList.get(i).getInvoice_key_id() != 0){
               // keyFlag = true;
                invoice_key_id=productList.get(i).getInvoice_key_id();
                i = productList.size();
            }
        }
        return invoice_key_id;
    }
    //очистить(удалить) все записи о сборке товара
    private void clearAllProductCollect(){
        String url = Constant.PARTNER_OFFICE;
        url += "delete_all_product_collect_this_order";
        url += "&"+"order_id="+order_id;
        String whatQuestion = "delete_all_product_collect_this_order";
        setInitialData(url, whatQuestion);
        finish();
    }
    //сделать запись в (warehouse_inventory_in_out) о том что товар собран
    //или создать ключ документа и товарный чек
    private void addProductForMoving(int position,  int invoice_key_id){
        double quantity = productList.get(position).getQuantity_to_order();
        int product_inventory_id = productList.get(position).getProduct_inventory_id();
        String transaction_name = "sale";//продажа
        int collected = 1;//1=yes

        String url = Constant.PARTNER_OFFICE;
        url += "write_collect_product_for_sale";
        url += "&"+"warehouse_id="+providerWarehouse_id;
        url += "&"+"order_id="+order_id;
        url += "&"+"product_inventory_id="+product_inventory_id;
        url += "&"+"quantity="+quantity;
        url += "&"+"transaction_name="+transaction_name;
        url += "&"+"collected="+collected;
        url += "&"+"user_uid="+userDataModel.getUid();//MY_UID;
        url += "&"+"invoice_key_id="+invoice_key_id;
        url += "&"+"counterparty_tax_id="+userDataModel.getCompany_tax_id();
        url += "&"+"order_product_id="+productList.get(position).getOrder_product_id();
        String whatQuestion = "write_collect_product_for_sale";
        setInitialData(url, whatQuestion);
    }
    //получить список продуктов для комплектации этому покупателю
    private void startProductToOrderList(){
        String url = Constant.PARTNER_OFFICE;
        url += "receive_list_order_product";
        url += "&"+"order_id="+order_id;
        //url += "&"+"deliveryKey="+deliveryKey;
        url += "&"+"providerWarehouse_id="+providerWarehouse_id;
        String whatQuestion = "receive_list_order_product";
        setInitialData(url, whatQuestion);
        //Log.d("A111","PartnerCollectProductActivity / startProductToOrderList / url="+url);
    }
    //получить адрес доставки заказа
    private void receiveDeliveryaddress(){
        String url = Constant.PARTNER_OFFICE;
        url += "receive_delivery_address";
        url += "&"+"order_id="+order_id;
        String whatQuestion = "receive_delivery_address";
        setInitialData(url, whatQuestion);
        //Log.d("A111","PartnerCollectProductActivity / startProductToOrderList / url="+url);
    }
    private void setInitialData(String url_get, String whatQuestion) {
        InitialData task=new InitialData(){

            @RequiresApi(api = Build.VERSION_CODES.N)
            protected void onPostExecute(String result) {
                if(whatQuestion.equals("receive_list_order_product")){
                    splitResult(result);

                }else if(whatQuestion.equals("receive_delivery_address")){
                    splitDeliveryaddressResult(result);

                }
                //hide the dialog
                // asyncDialog.dismiss();
            }
        };
        task.execute(url_get);
    }
    private void splitDeliveryaddressResult(String result){
        address_for_delivery = result;

        tvBuyerInfo.setText(stBuyersCompany+"\n"+address_for_delivery);
        //Toast.makeText(this, "res = "+result, Toast.LENGTH_SHORT).show();
    }
    // разобрать результат с сервера, список продуктов которые собраны для отправки и их колличество
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void splitResult(String result){
        productList.clear();
        checkedList.clear();
        //Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
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
                    int invoice_key_id = Integer.parseInt(temp[19]);
                    int out_active = Integer.parseInt(temp[20]);
                    String description = temp[21];

                    AcceptProductListProvidersModel delivery = new AcceptProductListProvidersModel(
                            product_id,productInventory_id, category,brand,characteristic,
                            type_packaging, unit_measure,weight_volume,quantity_package,
                            image_url,order_product_id,quantity_to_order,partner_stock_quantity,
                            counterparty_id, abbreviation, counterparty, storage_conditions,
                            checked, quantity_of_colected, invoice_key_id, out_active, description, 0);

                    productList.add(delivery);

                }
            }
        }catch(Exception ex){
            Log.d("A111"
                    ,"PartnerCollectProductActivity / splitResult / ex: "+ex+" res: "+result);
            Toast.makeText(this, "Exception: "+ex, Toast.LENGTH_SHORT).show();
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
    private void adShowBigImage(int position){
        ImageView iv = new ImageView(this);
        String imageUrl = productList.get(position).getImage_url();
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
    private void adShowDeliveriAddress(){
        adb = new AlertDialog.Builder(this);
        adb.setTitle("");
        adb.setMessage(stBuyersCompany+"\n"+ address_for_delivery);

        ad=adb.create();
        ad.show();
    }

}