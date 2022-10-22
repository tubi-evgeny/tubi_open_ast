package ru.tubi.project.activity.company_my;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import ru.tubi.project.R;
import ru.tubi.project.models.CatalogProductProviderModel;
import ru.tubi.project.models.DecimalDigitsInputFilter;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.utilites.ConvertCyrilic;
import ru.tubi.project.utilites.GenerateImageName;
import ru.tubi.project.utilites.InitialDataPOST;
import ru.tubi.project.utilites.MakeImageOrientation;
import ru.tubi.project.utilites.MakeImageToSquare;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.tubi.project.utilites.Constant;
import ru.tubi.project.utilites.UploadImageData;
import ru.tubi.project.utilites.UserDataRecovery;

//import static com.example.tubi.Config.MY_TAXPAYER_ID;
import static ru.tubi.project.free.AllCollor.RED;
import static ru.tubi.project.free.AllCollor.TUBI_BLACK;
import static ru.tubi.project.free.AllCollor.TUBI_GREY_200;
import static ru.tubi.project.free.AllCollor.TUBI_GREY_600;
import static ru.tubi.project.free.AllText.BRAND_TEXT;
import static ru.tubi.project.free.AllText.BUILDING;
import static ru.tubi.project.free.AllText.CATEGORY_TEXT;
import static ru.tubi.project.free.AllText.CHARACTERISTIC_TEXT;
import static ru.tubi.project.free.AllText.CHECK_CONNECT_INTERNET;
import static ru.tubi.project.free.AllText.DESCRIPTION_PRODUCT;
import static ru.tubi.project.free.AllText.FILL_PRODUCT_CARD;
import static ru.tubi.project.free.AllText.IS_NO_DESCRIPTION;
import static ru.tubi.project.free.AllText.I_UNDERSTAND;
import static ru.tubi.project.free.AllText.LOAD_TEXT;
import static ru.tubi.project.free.AllText.PRICE_TEXT;
import static ru.tubi.project.free.AllText.PRODUCT_DATA_TEXT;
import static ru.tubi.project.free.AllText.PRODUCT_MUST_PASS_MODERATION_TEXT;
import static ru.tubi.project.free.AllText.PRODUCT_NAME_TEXT;
import static ru.tubi.project.free.AllText.PRODUCT_QUANTITY_TEXT;
import static ru.tubi.project.free.AllText.PRODUCT_UPLOAD_DB;
import static ru.tubi.project.free.AllText.QUANTITY_PACKAGE_TEXT;
import static ru.tubi.project.free.AllText.ST;
import static ru.tubi.project.free.AllText.STORAGE_CONDITIONS;
import static ru.tubi.project.free.AllText.TIPE_PACAGING_TEXT;
import static ru.tubi.project.free.AllText.UNIT_MEASURE_TEXT;
import static ru.tubi.project.free.AllText.WAREHOUSE;
import static ru.tubi.project.free.AllText.WEIHT_VOLUME_TEXT;
import static ru.tubi.project.utilites.Constant.PRODUCT_CARD_ADD;
import static ru.tubi.project.utilites.InitialDataPOST.getParamsString;

public class ProductCardFillActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,  View.OnClickListener,
        View.OnFocusChangeListener , TextView.OnEditorActionListener {

    private TextView tvCategory,tvProductName,tvBrand,tvCharacteristic,tvUnitMeasureText,tvUnitMeasure,
            tvWeightVolume,tvTipePacagingText,tvTipePacaging,tvPrice,tvProductQuantityText,
             tvPackingQuantityText,tvProductDataText,tvProductData,tvDescriptionText;//tvStorageConditionsText,tvStorageConditions
    private ImageButton ibCategory,ibProductName,ibBrand,ibCharacteristic,ibTipePacaging;
    private Button btnSelectImage;
    private EditText etWeightVolume,etQuantityPackage,etPrice,etProductQuantity,etPackingQuantity,etDescription;
    private ImageView ivImage;
   // private Spinner spinnerStorageConditions;
    private ArrayList<String> alCategory=new ArrayList<>();
    private String category,productName,brand,characteristic,weightVolume,unitMeasure,tipePacaging,
    price,productQuantity,pacagingQuantity,description;
    private String product,whatQuestion;
    private String url_get,storageWarehouse,storageConditions;
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
    private ArrayAdapter<String>adapWarehouse;
    private ArrayAdapter<String>adapStorageConditions;
    private UserModel userDataModel;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_card_fill);
        setTitle(FILL_PRODUCT_CARD);//ЗАПОЛНИТЬ КАРТОЧКУ ТОВАРА

        tvCategory=findViewById(R.id.tvCategory);
        tvProductName=findViewById(R.id.tvProductName);
        tvBrand=findViewById(R.id.tvBrand);
        tvCharacteristic=findViewById(R.id.tvCharacteristic);
        tvUnitMeasureText=findViewById(R.id.tvUnitMeasureText);
        tvUnitMeasure=findViewById(R.id.tvUnitMeasure);
        tvWeightVolume=findViewById(R.id.tvWeightVolume);
        tvTipePacagingText=findViewById(R.id.tvTipePacagingText);
        tvTipePacaging=findViewById(R.id.tvTipePacaging);
        tvPrice=findViewById(R.id.tvPrice);
        tvProductQuantityText=findViewById(R.id.tvProductQuantityText);
        tvPackingQuantityText=findViewById(R.id.tvPackingQuantityText);
        tvProductDataText=findViewById(R.id.tvProductDataText);
        tvProductData=findViewById(R.id.tvProductData);
        //tvStorageConditionsText=findViewById(R.id.tvStorageConditionsText);
        //tvStorageConditions=findViewById(R.id.tvStorageConditions);
        tvDescriptionText=findViewById(R.id.tvDescriptionText);

        spinnerStorageConditions=findViewById(R.id.spinnerStorageConditions);
        spStorageWarehouse=findViewById(R.id.spStorageWarehouse);

        ibCategory=findViewById(R.id.ibCategory);
        ibProductName=findViewById(R.id.ibProductName);
        ibBrand=findViewById(R.id.ibBrand);
        ibCharacteristic=findViewById(R.id.ibCharacteristic);

        btnSelectImage=findViewById(R.id.btnSelectImage);

        etWeightVolume=findViewById(R.id.etWeightVolume);
        etPrice=findViewById(R.id.etPrice);
        etProductQuantity=findViewById(R.id.etProductQuantity);
        etPackingQuantity=findViewById(R.id.etPackingQuantity);
        etDescription=findViewById(R.id.etDescription);

        ivImage=findViewById(R.id.ivImage);

        //получить из sqlLite данные пользователя и компании
       UserDataRecovery userDataRecovery = new UserDataRecovery();
        userDataModel = userDataRecovery.getUserDataRecovery(this);

         tvCategory.setText(CATEGORY_TEXT);
        tvProductName.setText(PRODUCT_NAME_TEXT);
        tvBrand.setText(BRAND_TEXT);
        tvCharacteristic.setText(CHARACTERISTIC_TEXT);
        tvUnitMeasureText.setText(UNIT_MEASURE_TEXT);
        tvWeightVolume.setText(WEIHT_VOLUME_TEXT);
        tvTipePacagingText.setText(TIPE_PACAGING_TEXT);
        tvPrice.setText(PRICE_TEXT);
        tvProductQuantityText.setText(PRODUCT_QUANTITY_TEXT);
        tvPackingQuantityText.setText(QUANTITY_PACKAGE_TEXT);
        tvProductDataText.setText(PRODUCT_DATA_TEXT);
        tvDescriptionText.setText(DESCRIPTION_PRODUCT);

        ibCategory.setOnClickListener(this);
        ibProductName.setOnClickListener(this);
        ibBrand.setOnClickListener(this);
        ibCharacteristic.setOnClickListener(this);
        tvUnitMeasure.setOnClickListener(this);
        tvTipePacaging.setOnClickListener(this);
        //tvStorageConditions.setOnClickListener(this);

        etWeightVolume.setOnFocusChangeListener(this);
        etPrice.setOnFocusChangeListener(this);
        etProductQuantity.setOnFocusChangeListener(this);
        etPackingQuantity.setOnFocusChangeListener(this);
        etDescription.setOnFocusChangeListener(this);

        etWeightVolume.setOnEditorActionListener(this);
        etPrice.setOnEditorActionListener(this);
        etProductQuantity.setOnEditorActionListener(this);
        etPackingQuantity.setOnEditorActionListener(this);
        etDescription.setOnEditorActionListener(this);

        etPrice.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(8,2)});


        receiveMyWarehouse();

        adapWarehouse = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,storageWarehouseList);
        spStorageWarehouse.setAdapter(adapWarehouse);

        adapStorageConditions = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1,storageConditionsList);
        spinnerStorageConditions.setAdapter(adapStorageConditions);

        intent = getIntent();
        CatalogProductProviderModel product = (CatalogProductProviderModel)
                intent.getSerializableExtra("product_card_info");
        if(product != null){
            tvCategory.setText(""+product.getCategory());
            tvProductName.setText(""+product.getProduct_name());
            tvBrand.setText(""+product.getBrand());
            tvCharacteristic.setText(""+product.getCharacteristic());
            tvUnitMeasure.setText(""+product.getUnit_measure());
            tvTipePacaging.setText(""+product.getType_packaging());
            etDescription.setText(""+product.getDescription());
        }
        spinnerStorageConditions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               // ((TextView) parent.getChildAt(0)).setTextColor(Color.BLUE);
                ((TextView) parent.getChildAt(0)).setTextSize(18);
                storageConditions=storageConditionsList[position];
                productStringBuider();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {            }
        });
        spStorageWarehouse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextSize(18);
                storageWarehouse = storageWarehouseList.get(position);
                productStringBuider();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {            }
        });
        productStringBuider();
    }
    //записывааем в БД
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void goWriteData(View view) {
        //проверить наличие всех данных в textView
        boolean result = checkEveryDataDontEmpty();
        if(result == true){
            String category,productName,brand,characteristic;
            category = tvCategory.getText().toString().trim();
            productName = tvProductName.getText().toString().trim();
            brand = tvBrand.getText().toString().trim();
            characteristic = tvCharacteristic.getText().toString().trim();
            //получить имя картинки
            GenerateImageName image = new GenerateImageName();
            String imageName = image.generateImageName(category,productName,characteristic,brand);

            //загружаем картинку в БД
            UploadImageData uploadImage = new UploadImageData();
            String messege = uploadImage.uploadImageData(imageBitmap,imageName);
            Toast.makeText(this, ""+messege, Toast.LENGTH_SHORT).show();
            //загружаем данные товара в БД
            uploadProductCardData(imageName);
        }
    }

    //слушатель нажатия на ОК клавиатуры
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            // обрабатываем нажатие кнопки DONE
            productStringBuider();
            checkEveryDataDontEmpty();
           // Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
    //слушатель фокуса editText
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(!hasFocus){
            productStringBuider();
            checkEveryDataDontEmpty();
        }
    }

    private void productStringBuider(){
         category = tvCategory.getText().toString().trim();
         productName=tvProductName.getText().toString().trim();
         brand = tvBrand.getText().toString().trim();
         characteristic = tvCharacteristic.getText().toString().trim();
         weightVolume = etWeightVolume.getText().toString().trim();
         unitMeasure = tvUnitMeasure.getText().toString().trim();
         tipePacaging = tvTipePacaging.getText().toString().trim();
         price = etPrice.getText().toString().trim();
         productQuantity = etProductQuantity.getText().toString().trim();
         pacagingQuantity = etPackingQuantity.getText().toString().trim();
         description=etDescription.getText().toString().trim();

        if(price.equals("")){      price="0.0";        }
        double dbPrice = Double.parseDouble(price);

        product = category+" "+productName+" "+characteristic+" "+brand+
                "<br>"+weightVolume+" "+unitMeasure+" "+tipePacaging+
                "<br>"+"<i>"+PRICE_TEXT+"</i>:"+" "+dbPrice+
                "<br><i>"+PRODUCT_QUANTITY_TEXT+"</i>:"+" "+productQuantity+
                "<br><i>"+QUANTITY_PACKAGE_TEXT+"</i>:"+" "+pacagingQuantity+
                "<br><i>"+STORAGE_CONDITIONS+"</i>:"+" "+storageConditions+
                 "<br><i>"+WAREHOUSE+"</i>"+": "+storageWarehouse;

        String productLong = product+"<br>"+description;

        tvProductData.setText(Html.fromHtml(productLong));

    }




    //выбрать фото
    public void selectImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMG_REQUEST);
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(this, InfoForProductCardFillActivity.class);
        if(v.equals(ibCategory)){
            url_get = "";
            url_get = url;
            url_get += "select_category";
            intent.putExtra("url",url_get);
            if(CATEGORY_TEXT.equals(tvCategory.getText().toString())){
                intent.putExtra("product",product);
                intent.putExtra("request",CATEGORY_TEXT);
                intent.putExtra("request_name","");
            }else {
                intent.putExtra("product",product);
                intent.putExtra("request",CATEGORY_TEXT);
                intent.putExtra("request_name",  tvCategory.getText().toString());
            }
            startActivityForResult(intent,REQUEST_CATEGORY);
        }else if(v.equals(ibProductName)){
            url_get = "";
            url_get = url;
            url_get += "select_product_name";
            intent.putExtra("url",url_get);
            if(PRODUCT_NAME_TEXT.equals(tvProductName.getText().toString().trim())){
                intent.putExtra("product",product);
                intent.putExtra("request",PRODUCT_NAME_TEXT);
                intent.putExtra("request_name","");
            }else {
                intent.putExtra("product",product);
                intent.putExtra("request",PRODUCT_NAME_TEXT);
                intent.putExtra("request_name", tvProductName.getText().toString().trim());
            }
            startActivityForResult(intent,REQUEST_PRODUCT_NAME);
        }else if(v.equals(ibBrand)){
            url_get = "";
            url_get = url;
            url_get += "select_brand";
            intent.putExtra("url",url_get);
            if(BRAND_TEXT.equals(tvBrand.getText().toString())){
                intent.putExtra("product",product);
                intent.putExtra("request",BRAND_TEXT);
                intent.putExtra("request_name","");
            }else {
                intent.putExtra("product",product);
                intent.putExtra("request",BRAND_TEXT);
                intent.putExtra("request_name", tvBrand.getText().toString());
            }
            startActivityForResult(intent,REQUEST_BRAND);
        }else if(v.equals(ibCharacteristic)){
            url_get = "";
            url_get = url;
            url_get += "select_characteristic";
            intent.putExtra("url",url_get);
            if(CHARACTERISTIC_TEXT.equals(tvCharacteristic.getText().toString())){
                intent.putExtra("product",product);
                intent.putExtra("request",CHARACTERISTIC_TEXT);
                intent.putExtra("request_name","");
            }else {
                intent.putExtra("product",product);
                intent.putExtra("request", CHARACTERISTIC_TEXT );
                intent.putExtra("request_name",tvCharacteristic.getText().toString());
            }
            startActivityForResult(intent,REQUEST_CHARACTERISTIC);
        }else if(v.equals(tvUnitMeasure)){
            url_get = "";
            url_get = url;
            url_get += "select_unit_measure";
            intent.putExtra("url",url_get);
            intent.putExtra("product",product);
            intent.putExtra("request", UNIT_MEASURE_TEXT );
            intent.putExtra("request_name",tvUnitMeasure.getText().toString());

            startActivityForResult(intent,REQUEST_UNIT_MEASURE);
        }else if(v.equals(tvTipePacaging)){
            url_get = "";
            url_get = url;
            url_get += "select_tipe_pacaging";
            intent.putExtra("url",url_get);
            intent.putExtra("product",product);
            intent.putExtra("request", TIPE_PACAGING_TEXT );
            intent.putExtra("request_name",tvTipePacaging.getText().toString());

            startActivityForResult(intent,REQUEST_TIPE_PACAGING);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null ){
            Toast.makeText(this, "test", Toast.LENGTH_SHORT).show();
            Uri patch = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),patch);
                //получаем ориентацию картинки портрет или альбом
                MakeImageOrientation orientation = new MakeImageOrientation();
                imageBitmap = orientation.makeImageOrientation(this,bitmap,patch);
                //задаем размер фото для квадрата и показываем
                new MakeImageToSquare(this,imageBitmap,ivImage);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(requestCode == REQUEST_CATEGORY && resultCode == RESULT_OK && data != null){
            String st = data.getStringExtra("request");
            tvCategory.clearComposingText();
            tvCategory.setText(st);
        }else if(requestCode == REQUEST_PRODUCT_NAME && resultCode == RESULT_OK && data != null){
            String st = data.getStringExtra("request");
            tvProductName.setText(st);
        }else if(requestCode == REQUEST_BRAND && resultCode == RESULT_OK && data != null){
            String st = data.getStringExtra("request");
            tvBrand.setText(st);
        }else if(requestCode == REQUEST_CHARACTERISTIC && resultCode == RESULT_OK && data != null){
            String st = data.getStringExtra("request");
            tvCharacteristic.setText(st);
        }else if(requestCode == REQUEST_UNIT_MEASURE && resultCode == RESULT_OK && data != null){
            String st = data.getStringExtra("request");
            if(!UNIT_MEASURE_TEXT.equals(st)){
                tvUnitMeasure.setText(st);
            }
        }else if(requestCode == REQUEST_TIPE_PACAGING && resultCode == RESULT_OK && data != null){
            String st = data.getStringExtra("request");
            if(!TIPE_PACAGING_TEXT.equals(st)){
                tvTipePacaging.setText(st);
            }
           // tvTipePacagingShow.setText(st);
        }
        productStringBuider();
        checkEveryDataDontEmpty();
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
    //загружаем данные товара в БД
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void uploadProductCardData(String imageName) {
        //String st = "№ "+warehouse_info_id+"/"+warehouse_id+" "+city+" "+ST+". "+street+" "+house;
        String tempo[] = storageWarehouse.split(" ");
        String twoStep[] = tempo[1].split("/");
        String warehouse_id = twoStep[1];
        if (description.isEmpty())description = IS_NO_DESCRIPTION;
        String in_product_name= category+" "+productName+" "+characteristic+" "+brand+" "
                +weightVolume+" "+unitMeasure+" "+tipePacaging;
        //Toast.makeText(this, "warehouse_id: "+warehouse_id, Toast.LENGTH_SHORT).show();
       /* url_get="";
        url_get=url;
        url_get += "upload_product_card";
        url_get += "&"+"in_product_name="+in_product_name;
        url_get += "&"+"category="+category;
        url_get += "&"+"productName="+productName;//productName
        url_get += "&"+"brand="+brand;
        url_get += "&"+"characteristic="+characteristic;
        url_get += "&"+"weightVolume="+weightVolume;
        url_get += "&"+"unitMeasure="+unitMeasure;
        url_get += "&"+"tipePacaging="+tipePacaging;
        url_get += "&"+"price="+price;
        url_get += "&"+"productQuantity="+productQuantity;
        url_get += "&"+"pacagingQuantity="+pacagingQuantity;
        url_get += "&"+"imageName="+imageName+".jpg";
        url_get += "&"+"abbreviation="+userDataModel.getAbbreviation();//MY_ABBREVIATION;
        url_get += "&"+"company_name="+userDataModel.getCounterparty();//MY_NAME_COMPANY;
        url_get += "&"+"companyTaxId="+userDataModel.getCompany_tax_id();//MY_COMPANY_TAXPAYER_ID;
        url_get += "&"+"description="+description;
        url_get += "&"+"warehouse_id="+warehouse_id;
        url_get += "&"+"user_uid="+userDataModel.getUid();//MY_UID;
        url_get += "&"+"storageConditions="+storageConditions;
        whatQuestion = "upload_product_card";*/
        //setInitialData(url_get,whatQuestion);

        final Map<String, String> parameters = new HashMap<>();
        parameters.put("upload_product_card","");
        parameters.put("in_product_name", in_product_name);
        parameters.put("category", category);
        parameters.put("productName", productName);
        parameters.put("brand", brand);
        parameters.put("characteristic", characteristic);
        parameters.put("weightVolume", weightVolume);
        parameters.put("unitMeasure", unitMeasure);
        parameters.put("tipePacaging", tipePacaging);
        parameters.put("price", price);
        parameters.put("productQuantity", productQuantity);
        parameters.put("pacagingQuantity", pacagingQuantity);
        parameters.put("imageName", imageName+".jpg");
        parameters.put("abbreviation", userDataModel.getAbbreviation());
        parameters.put("company_name", userDataModel.getCounterparty());
        parameters.put("companyTaxId", String.valueOf(userDataModel.getCompany_tax_id()));
        parameters.put("description", description);
        parameters.put("warehouse_id", warehouse_id);
        parameters.put("user_uid", userDataModel.getUid());
        parameters.put("storageConditions", storageConditions);
        whatQuestion = "upload_product_card";

        setInitialDataPOST(PRODUCT_CARD_ADD, parameters, whatQuestion);
        Log.d("A111","ProductCardFillActivity / uploadProductCardData / url="+PRODUCT_CARD_ADD+parameters);

        clearEditText();
    }
    //получить список складов в которые можно складировать товар
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void receiveMyWarehouse(){
        String whatQuestion = "";
       /* String url = Constant.API;
        url += "receive_my_warehouse_list";
        url += "&" + "counterparty_tax_id=" +MY_COMPANY_TAXPAYER_ID;
        whatQuestion = "receive_my_warehouse_list";*/
       // setInitialData(url,whatQuestion);

        final Map<String, String> parameters = new HashMap<>();
        parameters.put("receive_my_warehouse_list","");
        parameters.put("counterparty_tax_id", String.valueOf(userDataModel.getCompany_tax_id()));
        whatQuestion = "receive_my_warehouse_list";
        setInitialDataPOST(Constant.API, parameters, whatQuestion);
        Log.d("A111","ProductCardFillActivity / receiveMyWarehouse / url="+Constant.API+parameters);
    }

    //получаем данные из сервера б/д
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
            protected void onPostExecute(String s) {
                if(whatQuestion.equals("upload_product_card")){
                    splitResult(s);
                }
                else if(whatQuestion.equals("receive_my_warehouse_list")){
                    splitResultMyWarehouse(s);
                }

                //скрыть диалоговое окно
                asyncDialog.dismiss();
            }
        };
        task.execute(url, getParamsString(param));
    }

  /*  private void setInitialData(String url_get, String whatQuestion) {
        ProgressDialog asyncDialog = new ProgressDialog(this);

        InitialData task=new InitialData(){
            @Override
            protected void onPreExecute() {
                asyncDialog.setMessage(LOAD_TEXT);
                asyncDialog.show();
                super.onPreExecute();
            }

            protected void onPostExecute(String result) {
                if(whatQuestion.equals("upload_product_card")){
                    splitResult(result);
                }
                else if(whatQuestion.equals("receive_my_warehouse_list")){
                    splitResultMyWarehouse(result);
                }
                //скрыть диалоговое окно
                asyncDialog.dismiss();
               // Toast.makeText(ProductCardFillActivity.this, "res: "+result, Toast.LENGTH_LONG).show();

            }
        };
        task.execute(url_get);
    }*/
    private void splitResultMyWarehouse(String result){
        //Toast.makeText(this, "result: "+result, Toast.LENGTH_SHORT).show();
        storageWarehouseList.clear();
        if(result.equals("")){
            storageWarehouseList.add("warehouse is not");
        }
        try{
            String[] res = result.split("<br>");
            String[] temp = res[0].split("&nbsp");
            if (temp[0].equals("error") || temp[0].equals("messege")) {
                Toast.makeText(this, "" + temp[1], Toast.LENGTH_LONG).show();
            } else {
                for (int i = 0; i < res.length; i++) {
                    temp = res[i].split("&nbsp");
                    String warehouse_info_id = temp[0], city = temp[1], street = temp[2],
                            house = temp[3], warehouse_id = temp[5];
                    String st = "№ "+warehouse_info_id+"/"+warehouse_id+" "+city+" "+ST+". "+street+" "+house;
                    try {
                        String building = temp[4];
                        st += " "+BUILDING+" "+building;
                    }catch (Exception ex){};

                    storageWarehouseList.add(st);
                }
            }
        }catch (Exception ex){
            Toast.makeText(this, "Exception: "+ex, Toast.LENGTH_SHORT).show();
        }
        adapWarehouse.notifyDataSetChanged();
    }
    // разобрать результат с сервера категории
    private void splitResult(String result){  // разобрать результат
        String [] res=result.split("<br>");
        String[]temp = res[0].split("&nbsp");

        if(temp[0].equals("RESULT_OK")){
            alertDialogUploadedProduct();
           // Toast.makeText(this, ""+PRODUCT_UPLOAD_DB, Toast.LENGTH_SHORT).show();
        }else if(temp[0].equals("error") || temp[0].equals("messege")){
            Toast.makeText(this, ""+temp[1], Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, ""+CHECK_CONNECT_INTERNET, Toast.LENGTH_LONG).show();
        }
    }
    private void clearEditText(){
        etPackingQuantity.setText("");
        etProductQuantity.setText("");
        etPrice.setText("");
    }
    private void alertDialogUploadedProduct(){
        adb = new AlertDialog.Builder(this);
        String st1 = PRODUCT_UPLOAD_DB;
        String st2 =PRODUCT_MUST_PASS_MODERATION_TEXT;
        adb.setTitle(st1);
        adb.setMessage(st2);
        adb.setNeutralButton(I_UNDERSTAND, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ad.cancel();
            }
        });
        ad=adb.create();
        ad.setCanceledOnTouchOutside(false);
        ad.setCancelable(false);
        ad.show();
        Button buttonbackground2 = ad.getButton(DialogInterface.BUTTON_NEUTRAL);
        buttonbackground2.setBackgroundColor(TUBI_GREY_200);
        buttonbackground2.setTextColor(Color.WHITE);
    }
    //проверить все строки и картинку на заполнино
    private boolean checkEveryDataDontEmpty(){
        boolean result=true;
        if (bitmap == null) {
            btnSelectImage.setTextColor(RED);
            result=false;
        }else{
            btnSelectImage.setTextColor(TUBI_BLACK);
        }
        if(tvCategory.getText().toString().equals(CATEGORY_TEXT)){
            tvCategory.setTextColor(RED);
            result=false;
        }else{
            tvCategory.setTextColor(TUBI_BLACK);
        }
        if(tvProductName.getText().toString().equals(PRODUCT_NAME_TEXT)){
            tvProductName.setTextColor(RED);
            result=false;
        }else{
            tvProductName.setTextColor(TUBI_BLACK);
        }
        if(tvBrand.getText().toString().equals(BRAND_TEXT)){
            tvBrand.setTextColor(RED);
            result=false;
        }else{
            tvBrand.setTextColor(TUBI_BLACK);
        }
        if(tvCharacteristic.getText().toString().equals(CHARACTERISTIC_TEXT)){
            tvCharacteristic.setTextColor(RED);
            result=false;
        }else{
            tvCharacteristic.setTextColor(TUBI_BLACK);
        }
        if(etWeightVolume.getText().toString().equals("")){
            tvWeightVolume.setTextColor(RED);
            result=false;
        }else{
            tvWeightVolume.setTextColor(TUBI_GREY_600);
        }
        if(tvUnitMeasure.getText().toString().equals("")){
            tvUnitMeasureText.setTextColor(RED);
            result=false;
        }else{
            tvUnitMeasureText.setTextColor(TUBI_GREY_600);
        }
        if(tvTipePacaging.getText().toString().equals("")){
            tvTipePacagingText.setTextColor(RED);
            result=false;
        }else{
            tvTipePacagingText.setTextColor(TUBI_GREY_600);
        }
        if(etPrice.getText().toString().equals("")){
            tvPrice.setTextColor(RED);
            result=false;
        }else{
            tvPrice.setTextColor(TUBI_GREY_600);
        }
        if(etProductQuantity.getText().toString().equals("")){
            tvProductQuantityText.setTextColor(RED);
            result=false;
        }else{
            tvProductQuantityText.setTextColor(TUBI_GREY_600);
        }
        if(etPackingQuantity.getText().toString().equals("")){
            tvPackingQuantityText.setTextColor(RED);
            result=false;
        }else{
            tvPackingQuantityText.setTextColor(TUBI_GREY_600);
        }

        return result;
    }
   /* private String generateImageName(String category,String brand,String characteristic) {
        //удалить из строки все символы кроме букв и цифр
        String tempCategory = deleteBadSimbolFromString(category);
        String tempBrand = deleteBadSimbolFromString(brand);
        String tempCharacteristic = deleteBadSimbolFromString(characteristic);

        String imageName=tempCategory + tempBrand+tempCharacteristic+ System.currentTimeMillis();
        //String imageName=tempCategory + tempBrand+tempCharacteristic+ System.currentTimeMillis() + ".jpg";

        return imageName;
    }*/
    //удалить из строки все символы кроме букв и цифр
    private String deleteBadSimbolFromString(String str) {

        String[]string = str.split("[' '|\\-]+");//"[-.]+"
        StringBuilder builder = new StringBuilder();
        for(int i=0; i < string.length; i++){
            //делаем все буквы строчными
            String st = string[i].toLowerCase();
            //удалить из слов все символы кроме букв и цифр в массив
            String strClear = st.replaceAll("[^\\p{L}\\p{N}]+", "");
            //заменить всю кирилицу на латиницу

            ConvertCyrilic convertCyrilic = new ConvertCyrilic();
            String strLatin = convertCyrilic.ConvertCyrilic(strClear);
            // String strLatin = convertCyrilic(strClear);
            //собрать строку с подчеркиванием "_"
            builder.append(strLatin+"_");

            //удалить из строки все символы кроме букв и цифр
            // builder.append(st.replaceAll("[^\\p{L}\\p{N}]+", "")+"_");

        }

        String myStrLatin = builder.toString();
        return myStrLatin;
    }
  /*  public static String convertCyrilic(String message){
        //char[] abcCyr =   {' ','а','б','в','г','д','е','ё', 'ж','з','ѕ','и','ј','к','л','љ','м','н','њ','о','п','р','с','т', 'ќ','у', 'ф','х','ц','ч','џ','ш', 'А','Б','В','Г','Д','Ѓ','Е', 'Ж','З','Ѕ','И','Ј','К','Л','Љ','М','Н','Њ','О','П','Р','С','Т', 'Ќ', 'У','Ф', 'Х','Ц','Ч','Џ','Ш','a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','1','2','3','4','5','6','7','8','9','/','-'};
        //String[] abcLat = {" ","a","b","v","g","d","e","e","zh","z","y","i","j","k","l","q","m","n","w","o","p","r","s","t","'","u","f","h", "c",";", "x","{","A","B","V","G","D","}","E","Zh","Z","Y","I","J","K","L","Q","M","N","W","O","P","R","S","T","KJ","U","F","H", "C",":", "X","{", "a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z","1","2","3","4","5","6","7","8","9","/","-"};
        char[] abcCyr = {' ','А','Б','В','Г','Д','Е','Ё', 'Ж', 'З','И','Й','К','Л','М','Н','О','П','Р','С','Т','У','Ф','Х','Ч', 'Ц','Ш', 'Щ', 'Э','Ю', 'Я', 'Ы','Ъ', 'Ь', 'а','б','в','г','д','е','ё', 'ж', 'з','и','й','к','л','м','н','о','п','р','с','т','у','ф','х','ч', 'ц','ш',  'щ',  'э', 'ю','я', 'ы','ъ','ь','-'};
        String[] abcLat = {" ","A","B","V","G","D","E","JO","ZH","Z","I","J","K","L","M","N","O","P","R","S","T","U","F","H","CH","C","SH","CSH","E","JU","JA","Y","", "", "a","b","v","g","d","e","jo","zh","z","i","j","k","l","m","n","o","p","r","s","t","u","f","h","ch","c","sh","csh","e","ju","ja","y", "", "","_"};

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            for (int x = 0; x < abcCyr.length; x++ ) {
                if (message.charAt(i) == abcCyr[x]) {
                    builder.append(abcLat[x]);
                }
            }
        }
        return builder.toString();
    }*/



   /* public void uploadImageData(String imageName) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,75,byteArrayOutputStream);
        byte[] imageInByte = byteArrayOutputStream.toByteArray();

        String encodedImage = Base64.encodeToString(imageInByte,Base64.DEFAULT);
        //String imageName = "my_image_1";

        Call<ResponsePOJO> call = RetroClient.getInstance().getApi().uploadImage(encodedImage, imageName);
        call.enqueue(new Callback<ResponsePOJO>() {
            @Override
            public void onResponse(Call<ResponsePOJO> call, Response<ResponsePOJO> response) {
                Toast.makeText(ProductCardFillActivity.this, "1: "+response.body().getRemarks(), Toast.LENGTH_LONG).show();

                if(response.body().isStatus()){

                }else{

                }
            }

            @Override
            public void onFailure(Call<ResponsePOJO> call, Throwable t) {

                Toast.makeText(ProductCardFillActivity.this, "Network Faield", Toast.LENGTH_SHORT).show();

            }
        });
    }*/

}