package ru.tubi.project.activity.AdminPanel;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ru.tubi.project.R;
import ru.tubi.project.activity.ActivtyAddProduct;
import ru.tubi.project.adapters.AddProductsCheckAdapter;
import ru.tubi.project.models.AddProduct;
import ru.tubi.project.utilites.InitialData;

import java.util.ArrayList;

import ru.tubi.project.utilites.Constant;

import static ru.tubi.project.activity.ActivtyAddProduct.ADD_BRAND;
import static ru.tubi.project.activity.ActivtyAddProduct.ADD_CATEGORY;
import static ru.tubi.project.activity.ActivtyAddProduct.ADD_CHARACTERISTIC;
import static ru.tubi.project.activity.ActivtyAddProduct.ADD_PRODUCT_NAME;
import static ru.tubi.project.activity.ActivtyAddProduct.ADD_TIPE_PACAGING;
import static ru.tubi.project.activity.ActivtyAddProduct.ADD_UNIT_MEASURE;
import static ru.tubi.project.activity.ActivtyAddProduct.CHENGE_BRAND;
import static ru.tubi.project.activity.ActivtyAddProduct.CHENGE_CATALOG;
import static ru.tubi.project.activity.ActivtyAddProduct.CHENGE_CATEGORY;
import static ru.tubi.project.activity.ActivtyAddProduct.CHENGE_CHARACTERISTIC;
import static ru.tubi.project.activity.ActivtyAddProduct.CHENGE_PRODUCT_NAME;
import static ru.tubi.project.activity.ActivtyAddProduct.CHENGE_PROVIDER;
import static ru.tubi.project.activity.ActivtyAddProduct.CHENGE_TYPE_PACKAGING;
import static ru.tubi.project.activity.ActivtyAddProduct.CHENGE_UNIT_MEASURE;
import static ru.tubi.project.free.AllCollor.TUBI_GREY_200;
import static ru.tubi.project.free.AllCollor.alert_dialog_button_green_pressed;
import static ru.tubi.project.free.AllCollor.alert_dialog_button_yellow_pressed;
import static ru.tubi.project.free.AllText.BRAND;
import static ru.tubi.project.free.AllText.CATALOG;
import static ru.tubi.project.free.AllText.CATEGORY;
import static ru.tubi.project.free.AllText.CHARACTERISTIC;
//import static com.example.tubi.free.AllText.LIST_PRODUCT;
import static ru.tubi.project.free.AllText.DESCRIPTION_EDIT;
import static ru.tubi.project.free.AllText.DESCRIPTION_MODERATION;
import static ru.tubi.project.free.AllText.LIST_PRODUCT_EMPTY;
import static ru.tubi.project.free.AllText.LOAD_TEXT;
import static ru.tubi.project.free.AllText.PRODUCT_NAME;
import static ru.tubi.project.free.AllText.PROVIDER;
import static ru.tubi.project.free.AllText.RETURN_BIG;
import static ru.tubi.project.free.AllText.SAVE_BIG;
import static ru.tubi.project.free.AllText.SEARCH_PRODUCT_BEFORE_DOWNLOADING_DATA;
import static ru.tubi.project.free.AllText.SELECT_CATALOG;
import static ru.tubi.project.free.AllText.TAX_ID;
import static ru.tubi.project.free.AllText.TIPE_PACAGING;
import static ru.tubi.project.free.AllText.TO_CHENGE_DESCRIPTION_BIG;
import static ru.tubi.project.free.AllText.UNIT_MEASURE;
import static ru.tubi.project.free.AllText.UPLOADING_DATABASE;

public class ActivityAddProductsCheck extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AddProductsCheckAdapter adapter;
    private ArrayList<AddProduct> addProducts= new ArrayList<>();
    private String url, url_get, newDescription;
    private int limit_num = 0, count_show = 2, x;
    private String whatQuestion = "", name;
    private Intent intent;
    private static final int ADD_PRODUCT_ACTIVITY_REQUEST_CODE = 0;
    AddProductsCheckAdapter.RecyclerViewClickListener clickListener;
    private AlertDialog.Builder adb;
    private AlertDialog ad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products_check);
        setTitle(R.string.product_from_provider);
        //Поставка товара, Проверка товара перед загрузкой в базу
        getSupportActionBar().setSubtitle(SEARCH_PRODUCT_BEFORE_DOWNLOADING_DATA);

        recyclerView = (RecyclerView) findViewById(R.id.rvList);

        ShowList();

        clickListener=
                new AddProductsCheckAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                WhatButtonClicked(view,position);
                //Toast.makeText(ActivityAddProductsCheck.this,
                //        "Click: "+position+" view: "+view, Toast.LENGTH_SHORT).show();
            }
        };

        adapter = new AddProductsCheckAdapter(this, addProducts, clickListener);
        recyclerView.setAdapter(adapter);


    }

    private void ShowList(){
        whatQuestion = "";
        String url = Constant.INPUT;
        url += "input_product";
        url += "&" + "limit_num=" + limit_num;
        url += "&" + "count_show=" + count_show;
        setInitialData(url,whatQuestion);
        /*
        whatQuestion = "";
        url = Constant.ADD_INPUT_PRODUCT;
        url_get = url;
        url_get += "&" + "limit_num=" + limit_num;
        url_get += "&" + "count_show=" + count_show;
        setInitialData(url_get,whatQuestion);
         */
    }

    private void WhatButtonClicked(View view,int position){
        String string=String.valueOf(view);
        String str[]=string.split("/");
        //Toast.makeText(this, "view: "+str[1], Toast.LENGTH_SHORT).show();
        intent=new Intent(this, ActivtyAddProduct.class);
        if(str[1].equals("tvCatalog}")){
            name = CATALOG;
            intent.putExtra("textView",name);
            intent.putExtra("nameBrand",addProducts.get(position).getCatalog());
            intent.putExtra("input_product_id", addProducts.get(position).getId());
            intent.putExtra("position", position);
            startActivityForResult(intent,ADD_PRODUCT_ACTIVITY_REQUEST_CODE);
        }else if(str[1].equals("tvCategory}")){
            if(addProducts.get(position).getCatalog().equals("0")){
                Toast.makeText(this, ""+SELECT_CATALOG, Toast.LENGTH_SHORT).show();
                return;
            }else {
                name = CATEGORY;
               // if (addProducts.get(position).getCatalog().equals("")){}
                intent.putExtra("textView", name);
                intent.putExtra("nameBrand", addProducts.get(position).getCategory());
                intent.putExtra("input_product_id", addProducts.get(position).getId());
                intent.putExtra("catalog_name", addProducts.get(position).getCatalog());
                intent.putExtra("position", position);
                startActivityForResult(intent, ADD_PRODUCT_ACTIVITY_REQUEST_CODE);
            }
        }
        //--------------------------
        else if(str[1].equals("tvProductName}")){
            name = PRODUCT_NAME;
            intent.putExtra("textView",name);
            intent.putExtra("nameBrand",addProducts.get(position).getProduct_name());
            intent.putExtra("input_product_id", addProducts.get(position).getId());
            intent.putExtra("position", position);
            startActivityForResult(intent,ADD_PRODUCT_ACTIVITY_REQUEST_CODE);
        }
        //--------------------------
        else if(str[1].equals("tvBrand}")){
            name = BRAND;
            intent.putExtra("textView",name);
            intent.putExtra("nameBrand",addProducts.get(position).getBrand());
            intent.putExtra("input_product_id", addProducts.get(position).getId());
            intent.putExtra("position", position);
            startActivityForResult(intent,ADD_PRODUCT_ACTIVITY_REQUEST_CODE);
        }else if(str[1].equals("tvCharacteristic}")){
            name = CHARACTERISTIC;
            intent.putExtra("textView",name);
            intent.putExtra("nameBrand",addProducts.get(position).getCharacteristic());
            intent.putExtra("input_product_id", addProducts.get(position).getId());
            intent.putExtra("position", position);
            startActivityForResult(intent,ADD_PRODUCT_ACTIVITY_REQUEST_CODE);
        }else if(str[1].equals("tvTipePacaging}")){
            name = TIPE_PACAGING;
            intent.putExtra("textView",name);
            intent.putExtra("nameBrand",addProducts.get(position).getType_packaging());
            intent.putExtra("input_product_id", addProducts.get(position).getId());
            intent.putExtra("position", position);
            startActivityForResult(intent,ADD_PRODUCT_ACTIVITY_REQUEST_CODE);
        }else if(str[1].equals("tvUnitMeasure}")){
            name = UNIT_MEASURE;
            intent.putExtra("textView",name);
            intent.putExtra("nameBrand",addProducts.get(position).getUnit_measure());
            intent.putExtra("input_product_id", addProducts.get(position).getId());
            intent.putExtra("position", position);
            startActivityForResult(intent,ADD_PRODUCT_ACTIVITY_REQUEST_CODE);
        }else if(str[1].equals("tvCounterparty}")){
            name = PROVIDER;
            intent.putExtra("textView",name);
            String stProvider = addProducts.get(position).getAbbreviation()+" "+addProducts.get(position).getCounterparty()
                    +" "+TAX_ID+": "+addProducts.get(position).getTaxpayer_id();
            intent.putExtra("nameBrand",stProvider);
            //intent.putExtra("nameBrand",addProducts.get(position).getCounterparty());
            intent.putExtra("input_product_id", addProducts.get(position).getId());
            intent.putExtra("position", position);
            intent.putExtra("taxpayer_id",addProducts.get(position).getTaxpayer_id());
            startActivityForResult(intent,ADD_PRODUCT_ACTIVITY_REQUEST_CODE);
        }else if(str[1].equals("tvDescription}")){
            alertDialogDescription(position);
            //Toast.makeText(this, "tvDescription", Toast.LENGTH_SHORT).show();
        }else if(str[1].equals("tvBtnShowMore}")){
            whatQuestion = "";
            count_show += 2;
            String url = Constant.INPUT;
            url += "input_product";
            url_get = url;
            url_get += "&" + "limit_num=" + limit_num;
            url_get += "&" + "count_show=" + count_show;
            setInitialData(url_get,whatQuestion);
        }else if(str[1].equals("tvBtnDownloadData}")){
            whatQuestion = "tvBtnDownloadData";
            url_get = Constant.INPUT;
            url_get += "insert_inventory";
            url_get += "&" + "limit_num=" + limit_num;
            url_get += "&" + "count_show=" + count_show;
            setInitialData(url_get,whatQuestion);
            Toast.makeText(this, ""+UPLOADING_DATABASE, Toast.LENGTH_LONG).show();
            ShowList();
        }
    }
    private void setInitialData(String url_get, String whatQuestion) {
        ProgressDialog asyncDialog = new ProgressDialog(this);

        InitialData task=new InitialData(){
            @Override
            protected void onPreExecute() {
                asyncDialog.setMessage(LOAD_TEXT);
                asyncDialog.show();
                super.onPreExecute();
            }

            protected void onPostExecute(String result) {
                //Do your thing
                if(whatQuestion.equals("")) {
                    if(!result.isEmpty()){
                        splitResultAddProduct(result);
                    }else {
                        Toast.makeText(ActivityAddProductsCheck.this,
                                "" + LIST_PRODUCT_EMPTY, Toast.LENGTH_SHORT).show();
                    }
                }
                //скрыть диалоговое окно
                asyncDialog.dismiss();
            }
        };
        task.execute(url_get);
    }
    private void splitResultAddProduct(String result){  // разобрать результат с сервера список продуктов и колличество
        addProducts.clear();
        try {
            String[] res = result.split("<br>");

            for (int i = 0; i < res.length; i++) {
                String[] temp = res[i].split("&nbsp");
                int id = Integer.parseInt(temp[0]);
                String catalog = temp[1];
                int catalog_flag = Integer.parseInt(temp[2]);
                String category = temp[3];
                int category_flag = Integer.parseInt(temp[4]);
                String product_name = temp[5];
                int product_name_flag = Integer.parseInt(temp[6]);
                String brand = temp[7];
                int brand_flag = Integer.parseInt(temp[8]);
                String characteristic = temp[9];
                int characteristic_flag = Integer.parseInt(temp[10]);
                String type_packaging = temp[11];
                int type_packaging_flag = Integer.parseInt(temp[12]);
                String unit_measure = temp[13];
                int unit_measure_flag = Integer.parseInt(temp[14]);
                int weight_volume = Integer.parseInt(temp[15]);
                double price = Double.parseDouble(temp[16]);
                int quantity = Integer.parseInt(temp[17]);
                int quantity_package = Integer.parseInt(temp[18]);
                String image = temp[19];
                int image_flag = Integer.parseInt(temp[20]);
                String description = temp[21];
                String abbreviation = temp[22];
                String counterparty = temp[23];
                int counterparty_flag = Integer.parseInt(temp[24]);
                long taxpayer_id = Long.parseLong(temp[25]);
                //int description_flag

                AddProduct addProduct = new AddProduct(id, catalog, catalog_flag,
                        category, category_flag, product_name, product_name_flag, brand,
                        brand_flag, characteristic, characteristic_flag, type_packaging,
                        type_packaging_flag, unit_measure, unit_measure_flag, weight_volume,
                        price, quantity, quantity_package, image, image_flag, description,
                        abbreviation, counterparty, counterparty_flag, taxpayer_id, 1);
                addProducts.add(addProduct);
                    /*
                     AddProduct addProduct = new AddProduct(Integer.parseInt(temp[0]), temp[1], Integer.parseInt(temp[2]),
                            temp[3], Integer.parseInt(temp[4]), temp[5], Integer.parseInt(temp[6]), temp[7],
                            Integer.parseInt(temp[8]), temp[9],
                            Integer.parseInt(temp[10]), temp[11], Integer.parseInt(temp[12]), Integer.parseInt(temp[13]),
                            Double.parseDouble(temp[14]), Integer.parseInt(temp[15]), Integer.parseInt(temp[16]), temp[17],
                            Integer.parseInt(temp[18]), temp[19], temp[20], temp[21], Integer.parseInt(temp[22])
                            , Integer.parseInt(temp[23]),0);
                    addProducts.add(addProduct);
                     */
            }
            adapter.notifyDataSetChanged();
        }catch (Exception ex){
            Toast.makeText(this,
                    "ActivityAddProductsCheck\nsplitResultAddProduct\nex: "+ex, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int recuestCode, int resultCode, Intent data) {
        super.onActivityResult(recuestCode, resultCode, data);

        if(recuestCode == ADD_PRODUCT_ACTIVITY_REQUEST_CODE && data != null){
            int myPosition = data.getIntExtra("position_return",x);

            if(resultCode == CHENGE_CATALOG){
                addProducts.get(myPosition).setCatalog_flag(1);
                String newCatalog = data.getStringExtra("name_for_chenge");
                addProducts.get(myPosition).setCatalog(newCatalog);
                adapter.notifyItemChanged(myPosition);
            }
            else if(resultCode == ADD_CATEGORY){
                    addProducts.get(myPosition).setCategory_flag(1);
                    adapter.notifyItemChanged(myPosition);
               /* String category = addProducts.get(myPosition).getCategory();
                for(int i=0;i<addProducts.size();i++){
                    if(addProducts.get(i).getCategory().equals(category)) {
                        addProducts.get(i).setCategory_flag(1);
                        adapter.notifyItemChanged(i);
                    }
                }*/
            }else if(resultCode == CHENGE_CATEGORY){
                addProducts.get(myPosition).setCategory_flag(1);
                String newCategory = data.getStringExtra("name_for_chenge");
                addProducts.get(myPosition).setCategory(newCategory);
                adapter.notifyItemChanged(myPosition);
            }
            //-------------------------------------------->>
            else if(resultCode == ADD_PRODUCT_NAME){
                String product_name = addProducts.get(myPosition).getProduct_name();
                for(int i=0;i<addProducts.size();i++){
                    if(addProducts.get(i).getProduct_name().equals(product_name)) {
                        addProducts.get(i).setProduct_name_flag(1);
                        adapter.notifyItemChanged(i);
                    }
                }
            }else if(resultCode == CHENGE_PRODUCT_NAME){
                addProducts.get(myPosition).setProduct_name_flag(1);
                String newProduct_name = data.getStringExtra("name_for_chenge");
                addProducts.get(myPosition).setProduct_name(newProduct_name);
                adapter.notifyItemChanged(myPosition);
            }
            //---------------------------------------------<<
            else if(resultCode == ADD_BRAND){
                String brand = addProducts.get(myPosition).getBrand();
                for(int i=0;i<addProducts.size();i++){
                    if(addProducts.get(i).getBrand().equals(brand)) {
                        addProducts.get(i).setBrand_flag(1);
                        adapter.notifyItemChanged(i);
                    }
                }
            }else if(resultCode == CHENGE_BRAND){
                addProducts.get(myPosition).setBrand_flag(1);
                String newBrand = data.getStringExtra("name_for_chenge");
                addProducts.get(myPosition).setBrand(newBrand);
                adapter.notifyItemChanged(myPosition);
            }
            else if(resultCode == ADD_CHARACTERISTIC){
                String tCharacteristic = addProducts.get(myPosition).getCharacteristic();
                for(int i=0;i<addProducts.size();i++){
                    if(addProducts.get(i).getCharacteristic().equals(tCharacteristic)) {
                        addProducts.get(i).setCharacteristic_flag(1);
                        adapter.notifyItemChanged(i);
                    }
                }
            }else if(resultCode == CHENGE_CHARACTERISTIC){
                addProducts.get(myPosition).setCharacteristic_flag(1);
                String newCharacteristic = data.getStringExtra("name_for_chenge");
                addProducts.get(myPosition).setCharacteristic(newCharacteristic);
                adapter.notifyItemChanged(myPosition);
            }else if(resultCode == ADD_TIPE_PACAGING){
                String tPacaging = addProducts.get(myPosition).getType_packaging();
                for(int i=0;i<addProducts.size();i++){
                    if(addProducts.get(i).getType_packaging().equals(tPacaging)) {
                        addProducts.get(i).setType_packaging_flag(1);
                        adapter.notifyItemChanged(i);
                    }
                }
            }else if(resultCode == CHENGE_TYPE_PACKAGING){
                addProducts.get(myPosition).setType_packaging_flag(1);
                String newTypePackaging = data.getStringExtra("name_for_chenge");
                addProducts.get(myPosition).setType_packaging(newTypePackaging);
                adapter.notifyItemChanged(myPosition);
            }else if(resultCode == ADD_UNIT_MEASURE) {
                String unMeasure = addProducts.get(myPosition).getUnit_measure();
                for(int i=0;i<addProducts.size();i++){
                    if(addProducts.get(i).getUnit_measure().equals(unMeasure)){
                        addProducts.get(i).setUnit_measure_flag(1);
                        adapter.notifyItemChanged(i);
                    }
                }
            }else if(resultCode == CHENGE_UNIT_MEASURE) {
                addProducts.get(myPosition).setUnit_measure_flag(1);
                String newUnitMeasure = data.getStringExtra("name_for_chenge");
                addProducts.get(myPosition).setUnit_measure(newUnitMeasure);
                adapter.notifyItemChanged(myPosition);

            }else if(resultCode == CHENGE_PROVIDER) {
                addProducts.get(myPosition).setCounterparty_flag(1);
                String newAbbreviation = data.getStringExtra("abbreviation_for_chenge");//abbreviation_for_chenge
                String newProvider = data.getStringExtra("name_for_chenge");
                int tax_id = Integer.parseInt(data.getStringExtra("tax_id"));
                addProducts.get(myPosition).setAbbreviation(newAbbreviation);
                addProducts.get(myPosition).setCounterparty(newProvider);
                addProducts.get(myPosition).setTaxpayer_id(tax_id);
                adapter.notifyItemChanged(myPosition);
            }
        }
    }
    private void alertDialogDescription(int position){
        adb = new AlertDialog.Builder(this);
        String st1 = DESCRIPTION_MODERATION;
        String st2 = addProducts.get(position).getDescription();
        adb.setTitle(st1);
        adb.setMessage(st2);
        adb.setPositiveButton(SAVE_BIG, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addProducts.get(position).setDescription_flag(1);
                adapter.notifyItemChanged(position);
            }
        });
        adb.setNegativeButton(TO_CHENGE_DESCRIPTION_BIG, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialogUpdateDescription(position);
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
        Button buttonbackground3 = ad.getButton(DialogInterface.BUTTON_NEGATIVE);
        buttonbackground3.setBackgroundColor(alert_dialog_button_yellow_pressed);
        buttonbackground3.setTextColor(Color.BLACK);
        Button buttonbackground2 = ad.getButton(DialogInterface.BUTTON_NEUTRAL);
        buttonbackground2.setBackgroundColor(TUBI_GREY_200);
        buttonbackground2.setTextColor(Color.WHITE);
    }
    private void alertDialogUpdateDescription(int position){
        adb = new AlertDialog.Builder(this);
        EditText etDescription = new EditText(this);
        String st1 = DESCRIPTION_EDIT;
        String st2 = addProducts.get(position).getDescription();
        etDescription.setText(st2);
        adb.setTitle(st1);
        adb.setView(etDescription);
        adb.setPositiveButton(SAVE_BIG, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addProducts.get(position).setDescription(etDescription.getText().toString());
                addProducts.get(position).setDescription_flag(1);
                adapter.notifyItemChanged(position);
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
}