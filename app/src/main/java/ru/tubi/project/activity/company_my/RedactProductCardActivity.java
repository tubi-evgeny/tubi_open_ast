package ru.tubi.project.activity.company_my;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ru.tubi.project.R;
import ru.tubi.project.models.DecimalDigitsInputFilter;
import ru.tubi.project.models.ProductModel;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.utilites.Constant;
import ru.tubi.project.utilites.DownloadImage;
import ru.tubi.project.utilites.InitialData;
import ru.tubi.project.utilites.MakeImageToSquare;
import ru.tubi.project.utilites.UserDataRecovery;

import static ru.tubi.project.activity.Config.ADMIN_PANEL_URL_IMAGES;
import static ru.tubi.project.free.AllCollor.TUBI_GREY_200;
import static ru.tubi.project.free.AllCollor.alert_dialog_button_green_pressed;
import static ru.tubi.project.free.AllCollor.alert_dialog_button_yellow_pressed;
import static ru.tubi.project.free.AllText.DESCRIPTION_MODERATION;
import static ru.tubi.project.free.AllText.ENTER_DETAILS;
import static ru.tubi.project.free.AllText.LOAD_TEXT;
import static ru.tubi.project.free.AllText.MIN_SELL_TEXT;
import static ru.tubi.project.free.AllText.MULTIPLE_OF_TEXT;
import static ru.tubi.project.free.AllText.PRICE_TEXT;
import static ru.tubi.project.free.AllText.PRODUCT_CARD;
import static ru.tubi.project.free.AllText.PRODUCT_QUANTITY_TEXT;
import static ru.tubi.project.free.AllText.QUANTITY_PACKAGE_TEXT;
import static ru.tubi.project.free.AllText.REDACT;
import static ru.tubi.project.free.AllText.RETURN_BIG;
import static ru.tubi.project.free.AllText.SAVE_BIG;
import static ru.tubi.project.free.AllText.STORAGE_CONDITIONS;
import static ru.tubi.project.free.AllText.WAREHOUSE;
import static ru.tubi.project.free.VariablesHelpers.STORAGE_CONDITIONS_LIST;
import static ru.tubi.project.utilites.Constant.PRODUCT_CARD_ADD;

public class RedactProductCardActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener,  View.OnClickListener,
        View.OnFocusChangeListener , TextView.OnEditorActionListener{

    private TextView tvCatalog, tvCategory,tvUnitMeasureText,tvUnitMeasure,
            tvWeightVolume,tvTipePacagingText,tvTipePacaging,tvPrice,
            tvPackingQuantityText,tvProductDataText,tvProductData,tvDescriptionText;//tvStorageConditionsText,tvStorageConditions
    private ImageButton ibCatalog,ibCategory,ibProductNameRevert,ibBrandRevert,ibCharacteristicRevert,ibTipePacaging;
    private Button btnRedactData;
    private EditText etWeightVolume,etProductName,etBrand,etCharacteristic,etPrice,etPackingQuantity
            ,etMinSell,etMultipleOf,etDescription;
    private ImageView ivImage;
    private ArrayList<Integer> catalog_id_list = new ArrayList<>();
    private ArrayList<String> catalog_name_list = new ArrayList<>();
    private ArrayList<String> category_name_list = new ArrayList<>();
    private ArrayList<String> unit_measure_list = new ArrayList<>();
    private ArrayList<String> type_pacaging_list = new ArrayList<>();
    // private Spinner spinnerStorageConditions;
    private ArrayList<String> alCategory=new ArrayList<>();
    private String catalog,category,productName,brand,characteristic,weightVolume,unitMeasure,tipePacaging,
            price,productQuantity,pacagingQuantity,minSell,multipleOf,description, productLong;
    private String product,whatQuestion;
    private String url_get,storageWarehouse,storageConditions;
    private int product_inventory_id, all_category_list =0;
    private String url = PRODUCT_CARD_ADD;
    static final int IMG_REQUEST = 21;
    static final int REQUEST_CATEGORY = 11;
    static final int REQUEST_PRODUCT_NAME = 18;
    static final int REQUEST_BRAND = 12;
    static final int REQUEST_CHARACTERISTIC = 13;
    static final int REQUEST_UNIT_MEASURE = 14;
    static final int REQUEST_TIPE_PACAGING = 16;
    // static final int REQUEST_STORAGE_CONDITIONS = 17;
    private Bitmap bitmap, imageBitmap;
    private Intent intent;
    private AlertDialog ad;
    private AlertDialog.Builder adb;
    private Spinner spStorageWarehouse,spinnerStorageConditions;
    private ArrayList<String> storageWarehouseList = new ArrayList<>();
    //private ArrayList<String> storageConditionsList = new ArrayList<>("мороз","холод","обычное");
    private String []  storageConditionsList = {"обычное","холод","мороз"};
    private ArrayAdapter<String> adapWarehouse;
    private ArrayAdapter<String>adapStorageConditions;
    private UserModel userDataModel;
    private ProductModel productModel;
    private ProductModel startProductModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redact_product_card);
        setTitle(REDACT);//Редактировать карточку товара
        getSupportActionBar().setSubtitle(PRODUCT_CARD);

        tvCatalog=findViewById(R.id.tvCatalog);
        tvCategory=findViewById(R.id.tvCategory);
        //tvProductName=findViewById(R.id.tvProductName);
        //tvBrand=findViewById(R.id.tvBrand);
        //tvCharacteristic=findViewById(R.id.tvCharacteristic);
        tvUnitMeasureText=findViewById(R.id.tvUnitMeasureText);
        tvUnitMeasure=findViewById(R.id.tvUnitMeasure);
        tvWeightVolume=findViewById(R.id.tvWeightVolume);
        //tvTipePacagingText=findViewById(R.id.tvTipePacagingText);
        tvTipePacaging=findViewById(R.id.tvTipePacaging);
        tvPrice=findViewById(R.id.tvPrice);
        tvPackingQuantityText=findViewById(R.id.tvPackingQuantityText);
        tvProductDataText=findViewById(R.id.tvProductDataText);
        tvProductData=findViewById(R.id.tvProductData);
        //tvStorageConditionsText=findViewById(R.id.tvStorageConditionsText);
        //tvStorageConditions=findViewById(R.id.tvStorageConditions);
        tvDescriptionText=findViewById(R.id.tvDescriptionText);

        spinnerStorageConditions=findViewById(R.id.spinnerStorageConditions);
        spStorageWarehouse=findViewById(R.id.spStorageWarehouse);

        ibCatalog=findViewById(R.id.ibCatalog);
        ibCategory=findViewById(R.id.ibCategory);
        ibProductNameRevert=findViewById(R.id.ibProductNameRevert);
        ibBrandRevert=findViewById(R.id.ibBrandRevert);
        ibCharacteristicRevert=findViewById(R.id.ibCharacteristicRevert);

        btnRedactData=findViewById(R.id.btnRedactData);

        etProductName=findViewById(R.id.etProductName);
        etBrand=findViewById(R.id.etBrand);
        etCharacteristic=findViewById(R.id.etCharacteristic);
        etWeightVolume=findViewById(R.id.etWeightVolume);
        etPrice=findViewById(R.id.etPrice);
        etPackingQuantity=findViewById(R.id.etPackingQuantity);
        etMinSell=findViewById(R.id.etMinSell);
        etMultipleOf=findViewById(R.id.etMultipleOf);
        etDescription=findViewById(R.id.etDescription);

        ivImage=findViewById(R.id.ivImage);


        //курсор в конец текста
       // etProductName.setSelection(etProductName.getText().length());
       // etBrand.setSelection(etBrand.getText().length());
       // etCharacteristic.setSelection(etCharacteristic.getText().length());

        //получить из sqlLite данные пользователя и компании
        UserDataRecovery userDataRecovery = new UserDataRecovery();
        userDataModel = userDataRecovery.getUserDataRecovery(this);

        ibCatalog.setOnClickListener(this);
        ibCategory.setOnClickListener(this);
        ibProductNameRevert.setOnClickListener(this);
        ibBrandRevert.setOnClickListener(this);
        ibCharacteristicRevert.setOnClickListener(this);
        tvUnitMeasure.setOnClickListener(this);
        tvTipePacaging.setOnClickListener(this);

        btnRedactData.setOnClickListener(this);

        etProductName.setOnFocusChangeListener(this);
        etBrand.setOnFocusChangeListener(this);
        etCharacteristic.setOnFocusChangeListener(this);
        etWeightVolume.setOnFocusChangeListener(this);
        etPrice.setOnFocusChangeListener(this);
        etPackingQuantity.setOnFocusChangeListener(this);
        etMinSell.setOnFocusChangeListener(this);
        etMultipleOf.setOnFocusChangeListener(this);
        etDescription.setOnFocusChangeListener(this);

        etWeightVolume.setOnEditorActionListener(this);
        etPrice.setOnEditorActionListener(this);
        etPackingQuantity.setOnEditorActionListener(this);
        etDescription.setOnEditorActionListener(this);

        etPrice.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(8,2)});

        intent = getIntent();
        product_inventory_id = intent.getIntExtra("product_inventory_id",0);

        adapStorageConditions = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1,STORAGE_CONDITIONS_LIST);
        spinnerStorageConditions.setAdapter(adapStorageConditions);

        startProduct();
    }

    @Override
    public void onClick(View view) {
        if(view.equals(ibCatalog)){
            getCatalogNamesList();
        }
        else if (view.equals(ibCategory)) {
            getCategoryNamesList();
        }
        else if(view.equals(ibProductNameRevert)){
            etProductName.setText(startProductModel.getProduct_name());
            etProductName.setSelection(etProductName.getText().length());
            productStringBuider();
        }
        else if(view.equals(ibBrandRevert)){
            etBrand.setText(startProductModel.getBrand());
            etBrand.setSelection(etBrand.getText().length());
            productStringBuider();
        }
        else if(view.equals(ibCharacteristicRevert)){
            etCharacteristic.setText(""+startProductModel.getCharacteristic());
            etCharacteristic.setSelection(etCharacteristic.getText().length());
            productStringBuider();
        }
        else if(view.equals(tvUnitMeasure)){
            getUnitMeasuryList();
        }
        else if(view.equals(tvTipePacaging)){
            getTypePacagingList();
        }
        else if(view.equals(btnRedactData)){
            Log.d("A111",getClass()+" / onClick / btnRedactData");
            productStringBuider();
            if(checkFullInfoAllVariables()){
                adCheckNewProductInfo();
            }else{
                Toast.makeText(this, ""+ENTER_DETAILS, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        Log.d("A111",getClass()+" / onFocusChange / boolean = "+b);
        if(!b) productStringBuider();
        /*if(view.equals(etProductName)){
            if(!b) productStringBuider();
        }
        else if(view.equals(etBrand)){
            if(!b) productStringBuider();
        }
        else if(view.equals(etCharacteristic)){
            if(!b) productStringBuider();
        }
        else if(view.equals(etWeightVolume)){
            if(!b) productStringBuider();
        }
        else if(view.equals(etPrice)){
            if(!b) productStringBuider();
        }*/
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if(textView.equals(etWeightVolume)){
            Log.d("A111",getClass()+" / onEditorAction / int = "+i+" / KeyEvent = "+keyEvent);
        }
        return false;
    }
    //проверить все переменные не пустые
    private boolean checkFullInfoAllVariables(){
        boolean writeData_flag = true;
        if(productModel.getCatalog().isEmpty() || productModel.getCategory().isEmpty()
            || productModel.getProduct_name().isEmpty() || productModel.getBrand().isEmpty()
            || productModel.getCharacteristic().isEmpty() || productModel.getType_packaging().isEmpty()
            || productModel.getUnit_measure().isEmpty() || productModel.getWeight_volume() == 0
            || productModel.getQuantity_package() == 0 || productModel.getStorage_conditions().isEmpty()
            || productModel.getPrice() == 0 || productModel.getMin_sell() == 0
            ||  productModel.getMultiple_of() == 0 || productModel.getDescription().isEmpty()){
            writeData_flag = false;
        }
        return writeData_flag;
    }
    //Запустить редактирование данных о продукте
    private void goRedactData(){
        String url = Constant.PROVIDER_OFFICE;
        url += "&" + "go_redact_data";
        url += "&" + "product_id=" + productModel.getProduct_id();
        url += "&" + "product_inventory_id=" + productModel.getProduct_inventory_id();
        url += "&" + "catalog=" + productModel.getCatalog();
        url += "&" + "category=" + productModel.getCategory();
        url += "&" + "product_name=" + productModel.getProduct_name();
        url += "&" + "brand=" + productModel.getBrand();
        url += "&" + "characteristic=" + productModel.getCharacteristic();
        url += "&" + "type_packaging=" + productModel.getType_packaging();
        url += "&" + "unit_measure=" + productModel.getUnit_measure();
        url += "&" + "weight_volume=" + productModel.getWeight_volume();
        url += "&" + "quantity_package=" + productModel.getQuantity_package();
        url += "&" + "storage_conditions=" + productModel.getStorage_conditions();
        url += "&" + "price=" + productModel.getPrice();
        url += "&" + "min_sell=" + productModel.getMin_sell();
        url += "&" + "multiple_of=" + productModel.getMultiple_of();
        url += "&" + "description=" + productModel.getDescription();
        String whatQuestion = "go_redact_data";
        setInitialData(url, whatQuestion);
        Log.d("A111",getClass()+" / goRedactData / url="+url);

        onBackPressed();
    }
    //получить список типов упаковки товара
    private void getTypePacagingList(){
        String url = Constant.PROVIDER_OFFICE;
        url += "&" + "get_tipe_pacaging_list";
        String whatQuestion = "get_tipe_pacaging_list";
        setInitialData(url, whatQuestion);
        Log.d("A111",getClass()+" / getTipePacagingList / url="+url);
    }
    //получить список вариантов меры веса
    private void getUnitMeasuryList(){
        String url = Constant.PROVIDER_OFFICE;
        url += "&" + "get_unit_measure_list";
        String whatQuestion = "get_unit_measure_list";
        setInitialData(url, whatQuestion);
        Log.d("A111",getClass()+" / getUnitMeasuryList / url="+url);
    }
    //получить список имен категорий
    private void getCategoryNamesList(){
        String url = Constant.PROVIDER_OFFICE;
        url += "&" + "get_category_name_list";
        //url += "&" + "catalog_id=" + catalog_id;
        url += "&" + "catalog_name=" + productModel.getCatalog();
        url += "&" + "all_category_list=" + all_category_list;
        String whatQuestion = "get_category_name_list";
        setInitialData(url, whatQuestion);
        Log.d("A111",getClass()+" / getCategoryNamesList / url="+url);
    }
    //получить список имен каталогов
    private void getCatalogNamesList(){
        String url = Constant.PROVIDER_OFFICE;
        url += "&" + "get_catalog_name_list";
        String whatQuestion = "get_catalog_name_list";
        setInitialData(url, whatQuestion);
        Log.d("A111",getClass()+" / getCatalogNamesList / url="+url);
    }
    //получить товар для редактирования
    private void startProduct(){
        String url = Constant.PROVIDER_OFFICE;
        url += "&" + "get_product_for_redacted";
        url += "&" + "product_inventory_id=" + product_inventory_id;
        String whatQuestion = "get_product_for_redacted";
        setInitialData(url, whatQuestion);
        Log.d("A111",getClass()+" / startProduct / url="+url);
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
                if (whatQuestion.equals("get_product_for_redacted")) {
                    splitResult(result);
                }
                else if (whatQuestion.equals("get_catalog_name_list")) {
                    splitCatalogNamesResult(result);
                }
                else if (whatQuestion.equals("get_category_name_list")) {
                    splitCategoryNamesResult(result);
                }
                else if (whatQuestion.equals("get_unit_measure_list")) {
                    splitUnitMeasureResult(result);
                }
                else if (whatQuestion.equals("get_tipe_pacaging_list")) {
                    splitTipePacagingResult(result);
                }
                asyncDialog.dismiss();
            }
        };
        task.execute(url_get);
    }
    private void downloadImage(){
        if(!productModel.getImage_url().isEmpty()) {
            new DownloadImage(){
                @Override
                protected void onPostExecute(Bitmap result) {
                    try {
                        int check = result.getWidth();
                        new MakeImageToSquare(result, ivImage);
                    }catch (Exception w) {
                        //bitmap пустой image не найден
                        ivImage.setImageResource(R.drawable.tubi_logo_no_image_300ps);
                    }
                }
            }
                    .execute(ADMIN_PANEL_URL_IMAGES+productModel.getImage_url());
        }else ivImage.setImageResource(R.drawable.tubi_logo_no_image_300ps);
    }

    private void splitCategoryNamesResult(String result){
        Log.d("A111",getClass()+" / splitCategoryNamesResult / result="+result);
        category_name_list.clear();
        try{
            String[] res = result.split("<br>");
            for (int i = 0; i < res.length; i++) {
                String[] temp = res[i].split("&nbsp");
                int category_id = Integer.parseInt(temp[0]);
                String category= ""+temp[1];

                category_name_list.add(category);
            }
            adShowCategoryNamesAndChenge();
        }catch(Exception ex){
            Log.d("A111",getClass()+" / splitCategoryNamesResult / ex="+ex);
        }
    }
    private void splitTipePacagingResult(String result){
        Log.d("A111",getClass()+" / splitTipePacagingResult / result="+result);
        type_pacaging_list.clear();
        try{
            String[] res = result.split("<br>");
            for (int i = 0; i < res.length; i++) {
                String[] temp = res[i].split("&nbsp");
                int type_packaging_id = Integer.parseInt(temp[0]);
                String type_packaging = ""+temp[1];

                type_pacaging_list.add(type_packaging);
            }
            adShowTypePacagingAndChenge();
        }catch(Exception ex){
            Log.d("A111",getClass()+" / splitTipePacagingResult / ex="+ex);
        }
    }
    private void splitUnitMeasureResult(String result){
        Log.d("A111",getClass()+" / splitUnitMeasureResult / result="+result);
        unit_measure_list.clear();
        try{
            String[] res = result.split("<br>");
            for (int i = 0; i < res.length; i++) {
                String[] temp = res[i].split("&nbsp");
                int unit_measure_id = Integer.parseInt(temp[0]);
                String unit_measure = ""+temp[1];

                unit_measure_list.add(unit_measure);
            }
            adShowUnitMeasureAndChenge();
        }catch(Exception ex){
            Log.d("A111",getClass()+" / splitCatalogNamesResult / ex="+ex);
        }
    }
    private void splitCatalogNamesResult(String result){
        catalog_name_list.clear();
        catalog_id_list.clear();
        try{
            String[] res = result.split("<br>");
            for (int i = 0; i < res.length; i++) {
                String[] temp = res[i].split("&nbsp");
                int catalog_id = Integer.parseInt(temp[0]);
                String catalog = ""+temp[1];

                catalog_id_list.add(catalog_id);
                catalog_name_list.add(catalog);
            }
            adShowCatalogNamesAndChenge();
        }catch(Exception ex){
            Log.d("A111",getClass()+" / splitCatalogNamesResult / ex="+ex);
        }
    }
    private void splitResult(String result) {
        try {
            String[] res = result.split("<br>");
            String[] temp = res[0].split("&nbsp");
            int product_id = Integer.parseInt(temp[0]);
            int product_inventory_id = Integer.parseInt(temp[1]);
            String category = ""+temp[2];
            String product_name = ""+temp[3];
            String brand = ""+temp[4];
            String characteristic = ""+temp[5];
            String type_packaging = ""+temp[6];
            String unit_measure = ""+temp[7];
            int weight_volume = Integer.parseInt(temp[8]);
            int quantity_package = Integer.parseInt(temp[9]);
            String image_url = "" + temp[10];
            String storage_conditions = ""+temp[11];
            double price = Double.parseDouble(temp[12]);
            String in_product_name = ""+temp[13];
            int min_sell = Integer.parseInt(temp[14]);
            int multiple_of = Integer.parseInt(temp[15]);
            String description_prod = ""+temp[16];
            String product_info = ""+temp[17];
            String catalog = ""+temp[18];

            startProductModel = new ProductModel(product_id, product_inventory_id, category
                    , product_name, brand, characteristic, type_packaging
                    , unit_measure, weight_volume, quantity_package
            , image_url, storage_conditions, price, in_product_name
            , min_sell, multiple_of, description_prod, product_info, catalog);

            productModel = new ProductModel(product_id, product_inventory_id, category
                    , product_name, brand, characteristic, type_packaging
                    , unit_measure, weight_volume, quantity_package
                    , image_url, storage_conditions, price, in_product_name
                    , min_sell, multiple_of, description_prod, product_info, catalog);

            writeDateToActivity();
        }catch (Exception ex){
            Log.d("A111",getClass()+" / splitResult / ex="+ex);
        }
    }
    private void writeDateToActivity(){
        tvCatalog.setText(""+productModel.getCatalog());
        tvCategory.setText(""+productModel.getCategory());
        //tvProductName.setText(""+productModel.getProduct_name());
        etProductName.setText(""+productModel.getProduct_name());
        //tvBrand.setText(""+productModel.getBrand());
        etBrand.setText(""+productModel.getBrand());
        //tvCharacteristic.setText(""+productModel.getCharacteristic());
        etCharacteristic.setText(""+productModel.getCharacteristic());
        etWeightVolume.setText(""+productModel.getWeight_volume());
        tvUnitMeasure.setText(""+productModel.getUnit_measure());
        tvTipePacaging.setText(""+productModel.getType_packaging());
        etPrice.setText(""+productModel.getPrice());
        etPackingQuantity.setText(""+productModel.getQuantity_package());
        etMinSell.setText(""+productModel.getMin_sell());
        etMultipleOf.setText(""+productModel.getMultiple_of());
        etDescription.setText(""+productModel.getDescription());
        spinnerStorageConditions.setSelection(
                adapStorageConditions.getPosition(productModel.getStorage_conditions()));
        storageConditions = productModel.getStorage_conditions();
        //spinnerStorageConditions.setSelection(position);

        //курсор в конец текста
        etProductName.setSelection(etProductName.getText().length());
        etBrand.setSelection(etBrand.getText().length());
        etCharacteristic.setSelection(etCharacteristic.getText().length());

        downloadImage();
    }
    private void productStringBuider(){
        catalog = tvCatalog.getText().toString().trim();
        category = tvCategory.getText().toString().trim();
        productName=etProductName.getText().toString().trim();
        brand = etBrand.getText().toString().trim();
        characteristic = etCharacteristic.getText().toString().trim();
        weightVolume = etWeightVolume.getText().toString().trim();
        unitMeasure = tvUnitMeasure.getText().toString().trim();
        tipePacaging = tvTipePacaging.getText().toString().trim();
        price = etPrice.getText().toString().trim();
        pacagingQuantity = etPackingQuantity.getText().toString().trim();
        minSell=etMinSell.getText().toString().trim();
        multipleOf=etMultipleOf.getText().toString().trim();
        description=etDescription.getText().toString().trim();
        storageWarehouse = spinnerStorageConditions.getSelectedItem().toString();

        if(weightVolume.equals("")) weightVolume="0";
        if(price.equals("")) price="0.0";
        if(pacagingQuantity.equals("")) pacagingQuantity="0";
        if(minSell.equals("")) minSell="0";
        if(multipleOf.equals("")) multipleOf="0";
        double dbPrice = Double.parseDouble(price);

        product = catalog+ "<br>"+category+
                "<br>"+productName+"<br>"+brand+"<br>"+characteristic+
                "<br>"+weightVolume+" "+unitMeasure+" "+tipePacaging+
                "<br>"+"<i>"+PRICE_TEXT+"</i>:"+" "+dbPrice+
                //"<br><i>"+PRODUCT_QUANTITY_TEXT+"</i>:"+" "+productQuantity+
                "<br><i>"+QUANTITY_PACKAGE_TEXT+"</i>:"+" "+pacagingQuantity+
                "<br><i>"+MIN_SELL_TEXT+"</i>:"+" "+minSell+
                "<br><i>"+MULTIPLE_OF_TEXT+"</i>:"+" "+multipleOf+
                "<br><i>"+STORAGE_CONDITIONS+"</i>:"+" "+storageConditions;
                //+"<br><i>"+WAREHOUSE+"</i>"+": "+storageWarehouse;

        productLong = product+"<br>"+description;

        tvProductData.setText(Html.fromHtml(productLong));

        productModel.setCatalog(""+catalog);
        productModel.setCategory(""+category);
        productModel.setProduct_name(""+productName);
        productModel.setBrand(""+brand);
        productModel.setCharacteristic(""+characteristic);
        productModel.setUnit_measure(""+unitMeasure);
        productModel.setWeight_volume(Integer.parseInt(weightVolume));
        productModel.setType_packaging(""+tipePacaging);
        productModel.setPrice(Double.parseDouble(price));
        productModel.setQuantity_package(Integer.parseInt(pacagingQuantity));
        productModel.setMin_sell(Integer.parseInt(minSell));
        productModel.setMultiple_of(Integer.parseInt(multipleOf));
        productModel.setStorage_conditions(storageConditions);
        productModel.setDescription(description);
    }
    private void adShowCategoryNamesAndChenge(){
        LinearLayout ll = new LinearLayout(this);
        ListView lv = new ListView(this);
        ll.setGravity(Gravity.CENTER_HORIZONTAL);
        ArrayAdapter adap = new ArrayAdapter(this
                , android.R.layout.simple_list_item_1, category_name_list);
        lv.setAdapter(adap);
        ll.addView(lv);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pn, long id) {
                String newCategory = category_name_list.get(pn);
                tvCategory.setText(newCategory);
                productStringBuider();
                ad.cancel();
            }
        });
        adb = new AlertDialog.Builder(this);
        adb.setTitle(productModel.getCategory());
        adb.setView(ll);
        ad = adb.create();
        ad.show();
    }
    private void adShowTypePacagingAndChenge(){
        LinearLayout ll = new LinearLayout(this);
        ListView lv = new ListView(this);
        ll.setGravity(Gravity.CENTER_HORIZONTAL);
        ArrayAdapter adap = new ArrayAdapter(this
                , android.R.layout.simple_list_item_1, type_pacaging_list);
        lv.setAdapter(adap);
        ll.addView(lv);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pn, long id) {
                String newTypePacaging = type_pacaging_list.get(pn);
                tvTipePacaging.setText(newTypePacaging);
                productStringBuider();
                ad.cancel();
            }
        });
        adb = new AlertDialog.Builder(this);
        adb.setTitle(productModel.getType_packaging());
        adb.setView(ll);
        ad = adb.create();
        ad.show();
    }
    private void adShowUnitMeasureAndChenge(){
        LinearLayout ll = new LinearLayout(this);
        ListView lv = new ListView(this);
        ll.setGravity(Gravity.CENTER_HORIZONTAL);
        ArrayAdapter adap = new ArrayAdapter(this
                , android.R.layout.simple_list_item_1, unit_measure_list);
        lv.setAdapter(adap);
        ll.addView(lv);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pn, long id) {
                String newUnitMeasure = unit_measure_list.get(pn);
                tvUnitMeasure.setText(newUnitMeasure);
                productStringBuider();
                ad.cancel();
            }
        });
        adb = new AlertDialog.Builder(this);
        adb.setTitle(productModel.getUnit_measure());
        adb.setView(ll);
        ad = adb.create();
        ad.show();
    }
    private void adShowCatalogNamesAndChenge(){
        LinearLayout ll = new LinearLayout(this);
        ListView lv = new ListView(this);
        ll.setGravity(Gravity.CENTER_HORIZONTAL);
        ArrayAdapter adap = new ArrayAdapter(this
                , android.R.layout.simple_list_item_1, catalog_name_list);
        lv.setAdapter(adap);
        ll.addView(lv);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pn, long id) {
                String newCatalog = catalog_name_list.get(pn);
                tvCatalog.setText(newCatalog);
                //очитить категорию (категория привязана к каталогу)
                tvCategory.setText("");
                productStringBuider();
                ad.cancel();
            }
        });
        adb = new AlertDialog.Builder(this);
        adb.setTitle(productModel.getCatalog());
        adb.setView(ll);
        ad = adb.create();
        ad.show();
    }
    //проверить новые данные товара перед записью
    private void adCheckNewProductInfo(){
        adb = new AlertDialog.Builder(this);
        adb.setPositiveButton(SAVE_BIG, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                goRedactData();
            }
        });
        adb.setNeutralButton(RETURN_BIG, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ad.cancel();
            }
        });

        adb.setTitle(DESCRIPTION_MODERATION);
        adb.setMessage(Html.fromHtml(productLong));
        ad = adb.create();
        ad.show();

        Button buttonbackground1 = ad.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonbackground1.setBackgroundColor(alert_dialog_button_green_pressed);
        buttonbackground1.setTextColor(Color.WHITE);
        Button buttonbackground2 = ad.getButton(DialogInterface.BUTTON_NEUTRAL);
        buttonbackground2.setBackgroundColor(TUBI_GREY_200);
        buttonbackground2.setTextColor(Color.WHITE);
    }
}