package ru.tubi.project.activity.company_my;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import ru.tubi.project.R;
import ru.tubi.project.adapters.CatalogStocksAdapter;
import ru.tubi.project.models.CatalogProductProviderModel;
import ru.tubi.project.models.DecimalDigitsInputFilter;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.models.WarehouseModel;
import ru.tubi.project.utilites.DownloadImage;
import ru.tubi.project.utilites.InitialData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

import ru.tubi.project.utilites.Constant;
import ru.tubi.project.utilites.MakeImageToSquare;
import ru.tubi.project.utilites.UserDataRecovery;

import static ru.tubi.project.activity.Config.ADMIN_PANEL_URL_IMAGES;
import static ru.tubi.project.activity.Config.ADMIN_PANEL_URL_PREVIEW_IMAGES;
//import static com.example.tubi.Config.MY_TAXPAYER_ID;
import static ru.tubi.project.free.AllCollor.TUBI_BLACK;
import static ru.tubi.project.free.AllCollor.TUBI_GREY_200;
import static ru.tubi.project.free.AllCollor.alert_dialog_button_green_pressed;
import static ru.tubi.project.free.AllText.BUILDING;
import static ru.tubi.project.free.AllText.CATALOG_CTOCKS_BIG;
import static ru.tubi.project.free.AllText.CHECK_CONNECT_INTERNET;
import static ru.tubi.project.free.AllText.CHENGE_SMALL;
import static ru.tubi.project.free.AllText.COPY_TEXT;
import static ru.tubi.project.free.AllText.DONE_BIG;
import static ru.tubi.project.free.AllText.EDIT_BIG;
import static ru.tubi.project.free.AllText.LOAD_TEXT;
import static ru.tubi.project.free.AllText.MAKE_COPY_PRODUCT_CARD_TEXT;
import static ru.tubi.project.free.AllText.PRICE_NEW;
import static ru.tubi.project.free.AllText.PRICE_NEW_IS_NOT;
import static ru.tubi.project.free.AllText.PRICE_OLD;
import static ru.tubi.project.free.AllText.QUANTITY_ADD;
import static ru.tubi.project.free.AllText.QUANTITY_FREE;
import static ru.tubi.project.free.AllText.QUANTITY_NEW_IS_NOT;
import static ru.tubi.project.free.AllText.RETURN_BIG;
import static ru.tubi.project.free.AllText.WAREHOUSE;
import static ru.tubi.project.free.AllText.WAREHOUSE_PARTNER;
import static ru.tubi.project.free.AllText.WAREHOUSE_PROVIDER;
import static ru.tubi.project.free.AllText.WAREHOUSE_STORAGE;
import static ru.tubi.project.free.AllText.YOUR_CEN_REDACT_PRODUCT_CARD_TEXT;

public class CatalogStocksActivity extends AppCompatActivity implements View.OnClickListener {

    private int row;
    private Intent takeit;
    private TextView tvWarehouse_info, tvWarehouse_info_short;
    private ImageView ivAddProductCard, ivSearch;
    private EditText etSearchTextInList;
    private LinearLayout llAddProductActivity;
    private RecyclerView recyclerView;
    private CatalogStocksAdapter adapter;
    private ArrayList<CatalogProductProviderModel> startListProduct = new ArrayList<>();
    private ArrayList<CatalogProductProviderModel> listProduct = new ArrayList<>();
    private ArrayList<CatalogProductProviderModel> parseListProduct = new ArrayList<>();
    private String url, url_get;
    private AlertDialog.Builder adb;
    private AlertDialog ad;
    private double newPrice, addQuantity;
    private int myPosition;
    private WarehouseModel warehouseModel;
    private UserModel userDataModel;
    //private ProgressDialog asyncDialog = new ProgressDialog(this);


    private int REQUEST_CHENGE_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog_stocks);
        setTitle(CATALOG_CTOCKS_BIG);//КАТАЛОГ/ЗАПАСЫ

        llAddProductActivity = findViewById(R.id.llAddProductActivity);
        tvWarehouse_info = findViewById(R.id.tvWarehouse_info);
        tvWarehouse_info_short = findViewById(R.id.tvWarehouse_info_short);
        ivAddProductCard = findViewById(R.id.ivAddProductCard);
        ivSearch = findViewById(R.id.ivSearch);
        etSearchTextInList = findViewById(R.id.etSearchTextInList);

        ivAddProductCard.setOnClickListener(this);
        ivSearch.setOnClickListener(this);

        //получить из sqlLite данные пользователя и компании
        UserDataRecovery userDataRecovery = new UserDataRecovery();
        userDataModel = userDataRecovery.getUserDataRecovery(this);

        takeit = getIntent();
        warehouseModel = (WarehouseModel) takeit.getSerializableExtra("my_warehouse");
        String warehouseType = warehouseModel.getWarehouse_type();
        if (warehouseType.equals("partner")) {
            warehouseType = WAREHOUSE_PARTNER;
        } else if (warehouseType.equals("provider")) {
            warehouseType = WAREHOUSE_PROVIDER;
        } else if (warehouseType.equals("storage")) {
            warehouseType = WAREHOUSE_STORAGE;
        }
        String warehouse = warehouseType + " №" + warehouseModel.getWarehouse_info_id() + "/" +
                warehouseModel.getWarehouse_id() + " \n" + warehouseModel.getCity() + " " +
                warehouseModel.getStreet() + " " + warehouseModel.getHouse();//"\n" + WAREHOUSE +
        if (!warehouseModel.getBuilding().isEmpty()) {
            warehouse += BUILDING + " " + warehouseModel.getBuilding();
        }
        String warehouse_short = WAREHOUSE + " №" + warehouseModel.getWarehouse_info_id() + "/" +
                warehouseModel.getWarehouse_id();
        tvWarehouse_info.setText("" + warehouse);
        tvWarehouse_info_short.setText("" + warehouse_short);

        if (warehouseModel.getWarehouse_type().equals("partner")) {
            llAddProductActivity.setVisibility(View.GONE);
        }

        startList();

        recyclerView = (RecyclerView) findViewById(R.id.rvList);

        CatalogStocksAdapter.RecyclerViewClickListener clickListener =
                new CatalogStocksAdapter.RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        WhatButtonClicked(view, position);
                        // Toast.makeText(CatalogProviderActivity.this,
                        //         "click position "+position+"\n"+"view "+view, Toast.LENGTH_SHORT).show();
                    }
                };
        adapter = new CatalogStocksAdapter(this, listProduct, clickListener);

        recyclerView.setAdapter(adapter);

        etSearchTextInList.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {            }
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getNeedProduct();
            }
            @Override
            public void afterTextChanged(Editable s) {            }
        });

        KeyboardVisibilityEvent.setEventListener(this, new KeyboardVisibilityEventListener() {
            @Override
            public void onVisibilityChanged(boolean b) {
                if (b) {
                    tvWarehouse_info_short.setVisibility(View.VISIBLE);
                    tvWarehouse_info.setVisibility(View.GONE);
                } else {
                    tvWarehouse_info_short.setVisibility(View.GONE);
                    tvWarehouse_info.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    //какая кнопка нажата
    public void WhatButtonClicked(View view, int position) {
        myPosition = position;
        String string = String.valueOf(view);
        String str[] = string.split("/");

        if (str[1].equals("ivImageProduct}")) {
            alertDialogChengeImage(position);
            //Toast.makeText(this, "ivImageProduct", Toast.LENGTH_SHORT).show();
        } else if (str[1].equals("tvDescription}")) {
            alertDialogReallyMakeCopy();
            Log.d("A111","CatalogStocksActivity / WhatButtonClicked / product_id="+listProduct.get(myPosition).getProduct_id()
                    +" / product_inventory_id="+listProduct.get(myPosition).getProduct_inventory_id());
        } else if (!warehouseModel.getWarehouse_type().equals("partner")) {
            if (str[1].equals("tvPrice}")) {
                alertDialogPriceShow(position);
            } else if (str[1].equals("tvQuantity}")) {
                alertDialogQuantityShow(position);
            }
        }
    }

    //подбор товаров по запросу из editText
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getNeedProduct() {
        Log.d("A111","CatalogStocksActivity / getNeedProduct / click");
        listProduct.clear();
        parseListProduct.clear();
        parseListProduct.addAll(startListProduct);

        //меняем регистр букв на нижний
        String searchSimbol = etSearchTextInList.getText().toString().trim();
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
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void checkEtSearchTextLength(){
        if(!etSearchTextInList.getText().toString().trim().isEmpty()){
            getNeedProduct();
        }
    }
    @Override
    public void onClick(View v) {
        if (v.equals(ivAddProductCard)) {
            Intent intent = new Intent(this, ProductCardFillActivity.class);
            startActivity(intent);
        }
    }
    //получить bitmap фоток
   /* @RequiresApi(api = Build.VERSION_CODES.N)
    private void receiveBmt(){
        for(int i=0;i < startListProduct.size();i++){
            row=i;
            new DownloadImage() {
                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    try {
                        int check = bitmap.getWidth();
                        startListProduct.get(row).setBmt(bitmap);
                        //Log.d("A111","CatalogStocksActivity / receiveBmt");
                        //добавить в listProduct bitmap битмап
                        //addListProductBmt(startListProduct.get(row).getProduct_inventory_id(), bitmap);
                    } catch (Exception ex) {
                        //bitmap пустой image не найден
                    }
                }
            }.execute(ADMIN_PANEL_URL_PREVIEW_IMAGES + startListProduct.get(i).getImage_url());
        }
        checkEtSearchTextLength();
    }
    //добавить в listProduct bitmap битмап
    private void addListProductBmt(int product_inv_id, Bitmap bitmap){
        for(int i=0;i < listProduct.size();i++){
            if(listProduct.get(i).getProduct_inventory_id() == product_inv_id){
                listProduct.get(i).setBmt(bitmap);
                //adapter.notifyItemRangeInserted(1,listProduct.size());
            }
        }
    }*/
    //добавить колличество товара поставщика в БД
    private void chengeQuantityInDB() {
        url_get = "";
        url_get = Constant.ADD_INPUT_CHECK;
        url_get += "chenge_quantity_in_inventory";
        url_get += "&" + "product_inventory_id=" + listProduct.get(myPosition).getProduct_inventory_id();
        url_get += "&" + "quantity=" + addQuantity;
        url_get += "&" + "transaction_name=" + "delivery";
        url_get += "&" + "warehouse_id=" + warehouseModel.getWarehouse_id();
        url_get += "&" + "user_uid=" + userDataModel.getUid();//MY_UID;
        String whatQuestion = "chenge_quantity_in_inventory";
        setInitialData(url_get, whatQuestion);
    }

    //изменить цену товара поставщика в БД
    private void chengePriceInDB() {
        url_get = "";
        url_get = Constant.ADD_INPUT_CHECK;
        url_get += "&" + "chenge_price_in_inventory";
        url_get += "&" + "product_inventory_id=" + listProduct.get(myPosition).getProduct_inventory_id();
        url_get += "&" + "price=" + newPrice;
        String whatQuestion = "chenge_price_in_inventory";
        setInitialData(url_get, whatQuestion);

        // Toast.makeText(this, "new Price "+stNewPrice, Toast.LENGTH_SHORT).show();
    }

    private void startList() {
        url = Constant.PROVIDER_OFFICE;
        if (warehouseModel.getWarehouse_type().equals("partner")) {
            url_get = url;
            url_get += "&" + "product_in_partner_warehouse_array";
            url_get += "&" + "tax_id=" + userDataModel.getCompany_tax_id();//MY_COMPANY_TAXPAYER_ID;
            url_get += "&" + "warehouse_id=" + warehouseModel.getWarehouse_id();
            String whatQuestion = "product_in_partner_warehouse_array";
            setInitialData(url_get, whatQuestion);
            //Toast.makeText(this, "partner warehouse", Toast.LENGTH_SHORT).show();
        } else {
            url_get = url;
            url_get += "&" + "product_provider_array";
            url_get += "&" + "tax_id=" + userDataModel.getCompany_tax_id();//MY_COMPANY_TAXPAYER_ID;
            url_get += "&" + "warehouse_id=" + warehouseModel.getWarehouse_id();
            String whatQuestion = "product_provider_array";
            setInitialData(url_get, whatQuestion);
            // Toast.makeText(this, "Warehouses", Toast.LENGTH_SHORT).show();
            Log.d("A111","CatalogStocksActivity / startList / url: "+url_get);
        }
    }

    private void setInitialData(String url_get, String whatQuestion) {
        ProgressDialog asyncDialog = new ProgressDialog(this);

        InitialData task = new InitialData() {
            @Override
            protected void onPreExecute() {
                asyncDialog.setMessage(LOAD_TEXT);
                asyncDialog.show();
                super.onPreExecute();
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            protected void onPostExecute(String result) {
                if (whatQuestion.equals("product_provider_array")) {
                    splitResult(result);
                    //product_in_partner_warehouse_array
                } else if (whatQuestion.equals("product_in_partner_warehouse_array")) {
                    splitResult(result);
                    // Toast.makeText(CatalogProviderActivity.this, "res: "+result, Toast.LENGTH_SHORT).show();
                } else if (whatQuestion.equals("chenge_price_in_inventory")) {
                    splitResultUpdate(result);
                    // Toast.makeText(CatalogProviderActivity.this, "res: "+result, Toast.LENGTH_SHORT).show();
                } else if (whatQuestion.equals("chenge_quantity_in_inventory")) {
                    splitResultUpdateQuantity(result);
                    // Toast.makeText(CatalogProviderActivity.this, "chenge_quantity: "+result, Toast.LENGTH_SHORT).show();
                }
                asyncDialog.dismiss();
            }
        };
        task.execute(url_get);
    }
    private void splitResultUpdateQuantity(String result) {
        String[] res = result.split("<br>");
        String[] one_temp = res[0].split("&nbsp");
        if (one_temp[0].equals("RESULT_OK")) {
            double quantityResult = addQuantity + listProduct.get(myPosition).getFree_balance();
            listProduct.get(myPosition).setFree_balance(quantityResult);
            //Toast.makeText(this, "test: "+one_temp[0]
            //         +"\nquantityResult: "+listProduct.get(myPosition).getFree_balance(), Toast.LENGTH_SHORT).show();
        } else if (one_temp[0].equals("error") || one_temp[0].equals("messege")) {
            Toast.makeText(this, "" + one_temp[1], Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "" + CHECK_CONNECT_INTERNET, Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();
    }

    // разобрать ответ с сервера
    private void splitResultUpdate(String result) {
        String[] res = result.split("<br>");
        String[] one_temp = res[0].split("&nbsp");
        if (one_temp[0].equals("RESULT_OK")) {
            listProduct.get(myPosition).setPrice(newPrice);
        } else if (one_temp[0].equals("error") || one_temp[0].equals("messege")) {
            Toast.makeText(this, "" + one_temp[1], Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "" + CHECK_CONNECT_INTERNET, Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();
    }

    // разобрать результат с сервера список продуктов и колличество
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void splitResult(String result) {
        listProduct.clear();
        startListProduct.clear();
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
                    String product_info = temp[14];
                    String product_name_from_provider = temp[15];
                    Bitmap bmt = null;

                    CatalogProductProviderModel products =
                            new CatalogProductProviderModel(product_id, product_inventory_id
                            , category, product_name, brand, characteristic, type_packaging
                            , unit_measure, weight_volume, total_quantity, price, quantity_package
                            , image_url, description, total_sale_quantity, free_balance
                            ,product_info, product_name_from_provider, bmt);
                    if (product_id == 0) {
                        startListProduct.add(products);
                    } else {
                        listProduct.add(products);
                    }
                }
                //сортируем лист по 4 полям (getCategory, getProduct_name, getCharacteristic,getBrand )
                //здесь собраны товары которые находятся на модерации
                startListProduct.sort(Comparator.comparing(CatalogProductProviderModel::getCategory)
                        .thenComparing(CatalogProductProviderModel::getProduct_name)
                        .thenComparing(CatalogProductProviderModel::getCharacteristic)
                        .thenComparing(CatalogProductProviderModel::getBrand));

                listProduct.sort(Comparator.comparing(CatalogProductProviderModel::getCategory)
                        .thenComparing(CatalogProductProviderModel::getProduct_name)
                        .thenComparing(CatalogProductProviderModel::getCharacteristic)
                        .thenComparing(CatalogProductProviderModel::getBrand));
                startListProduct.addAll(listProduct);
                listProduct.clear();
                listProduct.addAll(startListProduct);
                adapter.notifyDataSetChanged();
            }
        }catch (Exception ex){
            Toast.makeText(this, "ex: "+ex, Toast.LENGTH_SHORT).show();
        }
        checkEtSearchTextLength();
        //получить bitmap фоток
        //receiveBmt();
    }

    private void copyProductcard() {
        //Bitmap не сериализуется, выдает ошибку, приложение падает
        try {
            int product_id = listProduct.get(myPosition).getProduct_id();
            int product_inventory_id = listProduct.get(myPosition).getProduct_inventory_id();
            String category = listProduct.get(myPosition).getCategory();
            String brand = listProduct.get(myPosition).getBrand();
            String characteristic = listProduct.get(myPosition).getCharacteristic();
            String type_packaging = listProduct.get(myPosition).getType_packaging();
            String unit_measure = listProduct.get(myPosition).getUnit_measure();
            int weight_volume = listProduct.get(myPosition).getWeight_volume();
            double total_quantity = listProduct.get(myPosition).getTotal_quantity();
            double price = listProduct.get(myPosition).getPrice();
            int quantity_package = listProduct.get(myPosition).getQuantity_package();
            String image_url = listProduct.get(myPosition).getImage_url();
            String description = listProduct.get(myPosition).getDescription();
            double total_sale_quantity = listProduct.get(myPosition).getTotal_sale_quantity();
            double free_balance = listProduct.get(myPosition).getFree_balance();
            String product_name = listProduct.get(myPosition).getProduct_name();
            String product_info = listProduct.get(myPosition).getProduct_info();
            String product_name_from_provider = listProduct.get(myPosition).getProduct_name_from_provider();
            Intent intent = new Intent(this, ProductCardFillActivity.class);
            CatalogProductProviderModel product_card_info=new CatalogProductProviderModel(product_id
                    , product_inventory_id, category, product_name, brand, characteristic
                    , type_packaging, unit_measure, weight_volume, total_quantity, price
                    , quantity_package, image_url, description, total_sale_quantity
                    , free_balance,product_info, product_name_from_provider);
            intent.putExtra("product_card_info", product_card_info);
            startActivity(intent);
        }catch(Exception ex) {
            Log.d("A111","CatalogStocksActivity / copyProductcard / ex = "+ex);
        }
            Log.d("A111","CatalogStocksActivity / copyProductcard / intent = "+listProduct.get(myPosition).toString());

    }

    private void chengeImage(int position){
        String category,productName,characteristic,brand,image_url;
        int product_inventory_id;
        category = listProduct.get(position).getCategory();
        productName = listProduct.get(position).getProduct_name();
        brand = listProduct.get(position).getBrand();
        characteristic = listProduct.get(position).getCharacteristic();
        image_url = listProduct.get(position).getImage_url();
        product_inventory_id = listProduct.get(position).getProduct_inventory_id();

        Intent intent = new Intent(this, ChengeImageActivity.class);
        intent.putExtra("category",category);
        intent.putExtra("productName",productName);
        intent.putExtra("brand",brand);
        intent.putExtra("characteristic",characteristic);
        intent.putExtra("image_url",image_url);
        intent.putExtra("product_inventory_id",product_inventory_id);
        startActivityForResult(intent, REQUEST_CHENGE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CHENGE_IMAGE && resultCode == RESULT_OK ) {
            int my_product_inventory_id = data.getIntExtra("product_inventory_id",0);
            String my_imageName = data.getStringExtra("imageName");

            //меняем в списке урл фото и обновляем адаптер
            for(int i=0;i < listProduct.size();i++){
                if(listProduct.get(i).getProduct_inventory_id() == my_product_inventory_id){
                    listProduct.get(i).setImage_url(my_imageName);
                    adapter.notifyItemChanged(i);
                }
            } //startList();
        }
    }

    private void alertDialogReallyMakeCopy(){
        adb = new AlertDialog.Builder(this);
        String st1 = MAKE_COPY_PRODUCT_CARD_TEXT;
        String st2 = YOUR_CEN_REDACT_PRODUCT_CARD_TEXT;
        adb.setTitle(st1);
        adb.setMessage(st2);
        adb.setPositiveButton(COPY_TEXT, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                copyProductcard();
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
    private void alertDialogChengeImage(int position){
        ImageView ivImage = new ImageView(this);
        LinearLayout linearLayout = new LinearLayout(this);

        linearLayout.addView(ivImage, new LinearLayout.LayoutParams(1000,1000));
        linearLayout.setGravity(Gravity.CENTER);

        String image_url = listProduct.get(position).getImage_url();
        if(!image_url.equals("null")) {
          /*  new DownloadImage(ivImage)
                    .execute(ADMIN_PANEL_URL_IMAGES + image_url);*/
            new DownloadImage(){
              /*  @Override
                protected void onPreExecute() {
                    asyncDialog.setMessage(LOAD_TEXT);
                    asyncDialog.show();
                    super.onPreExecute();
                }*/
                @Override
                protected void onPostExecute(Bitmap result) {
                    try {
                        int check = result.getWidth();
                        new MakeImageToSquare(result, ivImage);
                    }catch (Exception ex){
                        ivImage.setImageResource(R.drawable.tubi_logo_no_image_300ps);
                    }
                   // asyncDialog.dismiss();
                }
            }
            .execute(ADMIN_PANEL_URL_IMAGES+image_url);

        }else ivImage.setImageResource(R.drawable.tubi_logo_no_image_300ps);

        adb = new AlertDialog.Builder(this);

        adb.setPositiveButton(CHENGE_SMALL, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                chengeImage(position);
                ad.cancel();
                //Toast.makeText(CatalogStocksActivity.this, "chenge image", Toast.LENGTH_SHORT).show();
            }
        });
        adb.setNeutralButton(RETURN_BIG, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ad.cancel();
            }
        });

        adb.setView(linearLayout);
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
    private void alertDialogQuantityShow(int position){
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

        tvQuantity.setText(""+QUANTITY_ADD);
        etQuantityAdd.setHint("0.00");
        etQuantityAdd.setTextSize(20);
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
        String st1 = EDIT_BIG;
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
                    addQuantity = Double.parseDouble(etQuantityAdd.getText().toString());
                    chengeQuantityInDB();
                }else {
                    Toast.makeText(CatalogStocksActivity.this,
                            ""+QUANTITY_NEW_IS_NOT, Toast.LENGTH_SHORT).show();
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

    private void alertDialogPriceShow(int position) {
        GridLayout gridLayout = new GridLayout(this);
        TextView tvPriceOld = new TextView(this);
        TextView tvPriceOldSum = new TextView(this);
        EditText etPrice = new EditText(this);
        TextView tvPrice = new TextView(this);

        tvPriceOld.setText(""+PRICE_OLD);
        tvPriceOld.setTextColor(TUBI_BLACK);
        tvPriceOld.setTextSize(20);

        tvPriceOldSum.setText(""+listProduct.get(myPosition).getPrice());
        tvPriceOldSum.setTextColor(TUBI_BLACK);
        tvPriceOldSum.setTextSize(20);

        tvPrice.setText(""+PRICE_NEW);
        etPrice.setHint("0.00");
        etPrice.setTextSize(20);
        etPrice.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(8,2)});
        etPrice.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        tvPrice.setTextColor(TUBI_BLACK);
        tvPrice.setTextSize(20);

        gridLayout.setColumnCount(2);
        gridLayout.setRowCount(2);

        gridLayout.addView(tvPriceOld, new GridLayout.LayoutParams(
                GridLayout.spec(0, GridLayout.CENTER),
                GridLayout.spec(0, GridLayout.RIGHT)));
        gridLayout.addView(tvPriceOldSum, new GridLayout.LayoutParams(
                GridLayout.spec(0, GridLayout.CENTER),
                GridLayout.spec(1, GridLayout.CENTER)));
        gridLayout.addView(tvPrice, new GridLayout.LayoutParams(
                GridLayout.spec(1, GridLayout.CENTER),
                GridLayout.spec(0, GridLayout.RIGHT)));
        gridLayout.addView(etPrice, new GridLayout.LayoutParams(
                GridLayout.spec(1, GridLayout.CENTER),
                GridLayout.spec(1, GridLayout.CENTER)));

        adb = new AlertDialog.Builder(this);
        String st1 = EDIT_BIG;
        CatalogProductProviderModel product = listProduct.get(myPosition);
        String description = product.getCategory()+" "+product.getBrand()+" "+product.getCharacteristic()+" "
                +product.getType_packaging()+" "+product.getWeight_volume()+" "+product.getUnit_measure();

        adb.setTitle(st1);
        adb.setMessage(description);
        adb.setView(gridLayout);

        adb.setPositiveButton(DONE_BIG, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(etPrice.getText().length() != 0){
                     newPrice = Double.parseDouble(etPrice.getText().toString());
                    chengePriceInDB();
                }else {
                    Toast.makeText(CatalogStocksActivity.this,
                            ""+PRICE_NEW_IS_NOT, Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onRestart() {
        super.onRestart();
        startList();
    }
}
