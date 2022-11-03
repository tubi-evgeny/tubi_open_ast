package ru.tubi.project.activity.buyer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.tubi.project.R;
import ru.tubi.project.activity.ActivityProductCard;
import ru.tubi.project.activity.ActivtyAddProduct;
import ru.tubi.project.adapters.BuyGoodsTogetherAdapter;
import ru.tubi.project.adapters.ProductAdapter;
import ru.tubi.project.models.ProductCardModel;
import ru.tubi.project.models.ProductCardModel;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.utilites.DownloadImage;
import ru.tubi.project.utilites.InitialDataPOST;
import ru.tubi.project.utilites.MakeImageToSquare;
import ru.tubi.project.utilites.UserDataRecovery;

import static ru.tubi.project.activity.Config.ADMIN_PANEL_URL_IMAGES;
import static ru.tubi.project.free.AllText.BUY_TOGETHER_TEXT;
import static ru.tubi.project.free.AllText.DONE_BIG;
import static ru.tubi.project.free.AllText.DONT_SHOW_TEXT;
import static ru.tubi.project.free.AllText.LEAVE_A_COMMENT;
import static ru.tubi.project.free.AllText.LOAD_TEXT;
import static ru.tubi.project.free.AllText.MAKE_LESS;
import static ru.tubi.project.free.AllText.MES_28;
import static ru.tubi.project.free.AllText.ORDER_APPROVED;
import static ru.tubi.project.free.AllText.RETURN_BIG;
import static ru.tubi.project.free.AllText.SEARCH_PRODUCT_BEFORE_DOWNLOADING_DATA;
import static ru.tubi.project.free.VariablesHelpers.DIALOG_BUY_GOODS_TOGETHER;
import static ru.tubi.project.utilites.Constant.JOINT_BUY;
import static ru.tubi.project.utilites.InitialDataPOST.getParamsString;

public class BuyGoodsTogetherActivity extends AppCompatActivity {

    private Intent takeit, intent;
    private ProductCardModel productCard;
    final Context context = this;
    private String activityName;
    private int quantityProductOrderIn=0, partner_warehouse_id=14;// ИП цыганков склад=14
    private ArrayList<ProductCardModel> products =new ArrayList<ProductCardModel>();
    private Dialog dialog;
    private UserModel userDataModel;
    private RecyclerView recyclerView;
    private BuyGoodsTogetherAdapter adapter;

    private AlertDialog.Builder adb;
    private AlertDialog ad;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_goods_together);
        setTitle("Каталог");
        getSupportActionBar().setSubtitle("совместных закупок");

        recyclerView=(RecyclerView)findViewById(R.id.rvList);

        //получить из sqlLite данные пользователя и компании
        UserDataRecovery userDataRecovery = new UserDataRecovery();
        userDataModel = userDataRecovery.getUserDataRecovery(this);

        takeit = getIntent();
        activityName = takeit.getStringExtra("activity");
        showThisWarehouseJointBuy();
        try{
            productCard = (ProductCardModel) takeit.getSerializableExtra("productCard");
            //adProductBuy(productCard);
        }catch (Exception ex){
            //пришли из главной активности
            if(DIALOG_BUY_GOODS_TOGETHER == 0) adInfoBuyTogether();
        }

        BuyGoodsTogetherAdapter.RecyclerViewClickListener clickListener=
                new BuyGoodsTogetherAdapter.RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        whathButtonClicked(position);
                        //quantityProductOrderIn = products.get(position).getQuantity_joint();
                        Log.d("A111",getClass()+" / BuyGoodsTogetherAdapter / position="+position);
                    }
                };
        adapter=new BuyGoodsTogetherAdapter(this, products,clickListener);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(adapter);
    }
    private void whathButtonClicked(int position) {
        quantityProductOrderIn = products.get(position).getQuantity_joint();

        productCard = products.get(position);
        adProductBuy(productCard);
    }
    private void showAlertDialog(){
        //если товар есть в списке товаров к совместной закупке то показать товар из списка
        if(activityName.equals("productCard")){
            for(int i=0; i < products.size();i++){
                if(products.get(i).getProduct_inventory_id() == productCard.getProduct_inventory_id()){
                    productCard = products.get(i);
                }
            }
            quantityProductOrderIn = productCard.getQuantity_joint();
            adProductBuy(productCard);
        }

    }

    private void adProductBuy(ProductCardModel productCard){
        //final Dialog
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.custon_ad_goods_together);

        ImageView ivImageProduct = dialog.findViewById(R.id.ivImageProduct);
        TextView productInfo = dialog.findViewById(R.id.tvProductInfo);
        TextView tvFreeStock = dialog.findViewById(R.id.tvFreeStock);
        TextView tvPrice = dialog.findViewById(R.id.tvPrice);
        EditText tvQuantityToOrder = dialog.findViewById(R.id.tvQuantityToOrder);
        Button btnOrder = dialog.findViewById(R.id.btnOrder);
        ListView lvBuyerInfo = dialog.findViewById(R.id.lvBuyerInfo);


        ArrayList <String>buyerList = new ArrayList<>();
        try {
            JSONArray joint_buy_list = new JSONArray(productCard.getJoint_buy_list());
            Log.d("A111",getClass()+" / adProductBuy / json length="+joint_buy_list.length());
            for(int i = 0;i < joint_buy_list.length();i++){
                JSONObject detail = joint_buy_list.getJSONObject(i);
                String buyerInfo = detail.getString("companyInfoString_short");
                buyerInfo += " кол-"+detail.getString("quantity_joint")+"шт.";
                buyerList.add(buyerInfo);
              //  Log.d("A111",getClass()+" / adProductBuy / json detail="
             //           +detail.getString("companyInfoString_short")
             //           +" кол-"+detail.getString("quantity_joint")+" i="+i);

            }
           // Log.d("A111",getClass()+" / adProductBuy / json string="
          //          +buyerList.toString());
        }catch(JSONException ex){
            Log.d("A111",getClass()+" / adProductBuy / json ex="+ex);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, buyerList);
        lvBuyerInfo.setAdapter(adapter);

        double freeStock = productCard.getMin_sell() - quantityProductOrderIn;
        productInfo.setText(""+productCard.getProduct_info());
        showImage(ivImageProduct, productCard.getImage_url());
        tvFreeStock.setText(""+freeStock);
        tvPrice.setText(""+String.format("%.2f",(productCard.getPrice() + productCard.getProcess_price())));

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                if(!tvQuantityToOrder.getText().toString().trim().isEmpty()) {
                    int quantityToOrder = Integer.parseInt(tvQuantityToOrder.getText().toString().trim());
                    //колличество входит в перделы свободного к заказу
                    if (quantityToOrder <= freeStock) {
                        createNewJointBuy(productCard.getProduct_inventory_id(), quantityToOrder);
                        activityName = "";
                       // Log.d("A111", "" + getClass() + " / quantity = " + tvQuantityToOrder.getText().toString());
                    } else {
                        Toast.makeText(BuyGoodsTogetherActivity.this, "" + MAKE_LESS, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        dialog.show();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showThisWarehouseJointBuy() {
        final Map<String, String> parameters = new HashMap<>();
        parameters.put("show_this_warehouse_joint_buy", "");
        parameters.put("partner_warehouse_id", ""+partner_warehouse_id);
        String whatQuestion = "show_this_warehouse_joint_buy";
        setInitialDataPOST(JOINT_BUY, parameters, whatQuestion);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void createNewJointBuy(int product_inventory_id, int quantityToOrder){
        final Map<String, String> parameters = new HashMap<>();
        parameters.put("create_new_joint_buy", "");
        parameters.put("partner_warehouse_id", ""+partner_warehouse_id);
        parameters.put("product_inventory_id", ""+product_inventory_id);
        parameters.put("quantityToOrder", String.valueOf(quantityToOrder));
        parameters.put("user_uid", userDataModel.getUid());
        String whatQuestion = "create_new_joint_buy";
        setInitialDataPOST(JOINT_BUY, parameters, whatQuestion);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setInitialDataPOST(String url, Map<String, String> param, String whatQuestion){
        ProgressDialog asyncDialog = new ProgressDialog(this);

        InitialDataPOST task = new InitialDataPOST(){
            @Override
            protected void onPreExecute() {
                asyncDialog.setMessage(LOAD_TEXT);
                asyncDialog.show();
                super.onPreExecute();
            }
            @Override
            protected void onPostExecute(String result) {
                if(whatQuestion.equals("create_new_joint_buy") ) {
                    splitResultJointBuy(result);
                }
                else if(whatQuestion.equals("show_this_warehouse_joint_buy") ) {
                    splitResultThisWarehouseJointBuy(result);
                }
                //скрыть диалоговое окно
                asyncDialog.dismiss();
            }
        };
        task.execute(url, getParamsString(param));
    }

    private void showImage(ImageView ivImageProduct, String image_url) {
        if(!image_url.equals("null")) {
            new DownloadImage(){
                @Override
                protected void onPostExecute(Bitmap result) {
                    try {
                        int check = result.getWidth();
                        new MakeImageToSquare(result, ivImageProduct);
                    }catch (Exception ex){
                        ivImageProduct.setImageResource(R.drawable.tubi_logo_no_image_300ps);
                    }
                }
            }.execute(ADMIN_PANEL_URL_IMAGES+image_url);
        }else ivImageProduct.setImageResource(R.drawable.tubi_logo_no_image_300ps);
    }
    private void splitResultThisWarehouseJointBuy(String result){
        products.clear();
        Log.d("A111",getClass()+" / splitResultThisWarehouseJointBuy / result="+result);
        try {
            String[] res = result.split("<br>");
            for (int i = 0; i < res.length; i++) {
                String[] temp = res[i].split("&nbsp");
                int product_id=Integer.parseInt(temp[0]);
                int product_inventory_id=Integer.parseInt(temp[1]);
                String image_url=temp[2];
                double price=Double.parseDouble(temp[3]);
                double process_price = Double.parseDouble(temp[4]);
                int min_sell = Integer.parseInt(temp[5]);
                int quantity_joint = Integer.parseInt(temp[6]);
                String product_info=temp[7];
                String joint_buy_list=temp[8];
                ProductCardModel prodInfo = new ProductCardModel(product_id,product_inventory_id
                        ,image_url, price, process_price, min_sell, quantity_joint,product_info
                        ,joint_buy_list);
                products.add(prodInfo);
            }
        }catch (Exception ex){
            Log.d("A111",getClass()+" / splitResultThisWarehouseJointBuy / ex="+ex);
            //Toast.makeText(this, "ex: splitResult\n"+ex, Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();
        showAlertDialog();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void splitResultJointBuy(String result){
        if(result.equals("RESULT_OK")){
            showThisWarehouseJointBuy();
            dialog.dismiss();
            Toast.makeText(this, "Все отлично, товар теперь в совместных закупках", Toast.LENGTH_SHORT).show();
        }else if(result.equals("NO_RESULT")){
            Toast.makeText(this, "Не получилось сохранить, попробуйте еще раз", Toast.LENGTH_SHORT).show();
        }
       // Log.d("A111",""+getClass()+" / result="+result);
    }
    private void adInfoBuyTogether(){
        adb = new AlertDialog.Builder(this);
        String st1 = BUY_TOGETHER_TEXT;
        String st2 = MES_28;
        adb.setTitle(st1);
        adb.setMessage(st2);

        adb.setNeutralButton(""+DONT_SHOW_TEXT, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DIALOG_BUY_GOODS_TOGETHER=1;
                ad.cancel();
            }
        });

        ad=adb.create();
        //ad.getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        ad.show();
    }

}