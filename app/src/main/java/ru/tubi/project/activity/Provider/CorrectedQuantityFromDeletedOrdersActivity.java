package ru.tubi.project.activity.Provider;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import ru.tubi.project.R;
import ru.tubi.project.adapters.CollectProductAdapter;
import ru.tubi.project.adapters.CorrectedQuantityFromDeletedOrdersAdapter;
import ru.tubi.project.models.ProviderCollectProductModel;
import ru.tubi.project.utilites.Constant;
import ru.tubi.project.utilites.InitialData;

import static ru.tubi.project.free.AllCollor.TUBI_GREY_200;
import static ru.tubi.project.free.AllCollor.alert_dialog_button_green_pressed;
import static ru.tubi.project.free.AllText.LOAD_TEXT;
import static ru.tubi.project.free.AllText.PERFORM;
import static ru.tubi.project.free.AllText.REJECTION_BIG;

public class CorrectedQuantityFromDeletedOrdersActivity extends AppCompatActivity {

    private Intent takeit;
    private int order_partner_id;

    private RecyclerView recyclerView;
    private ArrayList<ProviderCollectProductModel> productDeletedList = new ArrayList<>();
    private AlertDialog.Builder adb;
    private AlertDialog ad;
    private CorrectedQuantityFromDeletedOrdersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corrected_quantity_from_deleted_orders);
        setTitle("Список удаленных товаров");

        recyclerView=(RecyclerView)findViewById(R.id.rvList);

        takeit = getIntent();
        order_partner_id = takeit.getIntExtra("order_partner_id",0);
        if(order_partner_id != 0){
            showDeletedGoods();
        }

        CorrectedQuantityFromDeletedOrdersAdapter.RecyclerViewClickListener clickListener =
                new CorrectedQuantityFromDeletedOrdersAdapter.RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        whatClicked( view, position);
                        Log.d("A111",CorrectedQuantityFromDeletedOrdersActivity.this.getClass()
                                +" / RecyclerView / view = "+view+" / position = "+position);
                    }
                };
        adapter=new CorrectedQuantityFromDeletedOrdersAdapter
                (this,productDeletedList, clickListener);
        recyclerView.setAdapter(adapter);

    }
    private void whatClicked(View v, int position){
        ProviderCollectProductModel product = productDeletedList.get(position);
        String string=String.valueOf(v);
        String str[]=string.split("/");

        if(str[1].equals("btnRejection}")) {
            int correctStatus = 2;//отказать в изменениях=2;
            goCorrectedProductQuantityForCollect(product, correctStatus);
        }else if(str[1].equals("btnPerform}")){
            int correctStatus = 1;//внести изменения=1;
            goCorrectedProductQuantityForCollect(product, correctStatus);
        }
        productDeletedList.remove(position);
        if(productDeletedList.size() == 0){
            Toast.makeText(this, "Все выполнено", Toast.LENGTH_LONG).show();
        }
        adapter.notifyDataSetChanged();
    }
    //откоректировать количество товара из-за удаления товара
    private void goCorrectedProductQuantityForCollect
    (ProviderCollectProductModel product, int correctStatus){
        String url = Constant.WAREHOUSE_OFFICE;
        url += "corrected_product_quantity_for_collect";
        url += "&"+"deleted_goods_id="+product.getDeleted_goods_id();
        url += "&"+"order_partner_id="+order_partner_id;
        url += "&"+"product_inventory_id="+product.getProductInventory_id();
        url += "&"+"warehouse_inventory_id="+product.getWarehouse_inventory_id();
        url += "&"+"quantity_deleted_product="+product.getQuantity_deleted_product();
        url += "&"+"order_product_part_id="+product.getOrder_product_part_id();
        url += "&"+"correct_status="+correctStatus;
        String whatQuestion = "corrected_product_quantity_for_collect";
        setInitialData(url, whatQuestion);
        Log.d("A111",getClass()+" / goCorrectedProductQuantityForCollect / url = "+url);

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
                if(whatQuestion.equals("get_deleted_goods_list")){
                    splitDeletedGoodsListResult(result);
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
            adapter.notifyDataSetChanged();
            //adCorrectProductsQuantity();
        }catch(Exception ex){
            Log.d("A111","error. " +getClass()+
                    " / splitDeletedGoodsListResult \n"+ex.toString()+"\n"+result);
        }
    }
   /* private void adCorrectProductsQuantity(){
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
                adCorrectedDeletedProduct(i);
            }
        });
        String st1 = "Список удаленных товаров";
        adb.setTitle(st1);
        adb.setView(lv);
        ad=adb.create();
        ad.show();
    }*/
    //окно коррекции количества товра если удален заказ
  /*  private void adCorrectedDeletedProduct(int position){
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
                goCorrectedProductQuantityForCollect(product, correctStatus);


                ad.cancel();
            }
        });
        adb.setNegativeButton(REJECTION_BIG, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int correctStatus = 2;//отказать в изменениях=2;
                goCorrectedProductQuantityForCollect(product, correctStatus);


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

}