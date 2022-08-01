package ru.tubi.project.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import ru.tubi.project.R;
import ru.tubi.project.free.AllText;
import ru.tubi.project.models.CounterpartyModel;
import ru.tubi.project.utilites.InitialData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.tubi.project.utilites.Constant;
import ru.tubi.project.utilites.InitialDataPOST;

import static ru.tubi.project.free.AllText.BRAND;
import static ru.tubi.project.free.AllText.CATALOG;
import static ru.tubi.project.free.AllText.CATEGORY;
import static ru.tubi.project.free.AllText.CHARACTERISTIC;
import static ru.tubi.project.free.AllText.LOAD_TEXT;
import static ru.tubi.project.free.AllText.PRODUCT_NAME;
import static ru.tubi.project.free.AllText.PROVIDER;
import static ru.tubi.project.free.AllText.TIPE_PACAGING;
import static ru.tubi.project.free.AllText.UNIT_MEASURE;
import static ru.tubi.project.free.VariablesHelpers.MY_REGION;
import static ru.tubi.project.utilites.Constant.GET_CATALOG;
import static ru.tubi.project.utilites.Constant.INPUT;
import static ru.tubi.project.utilites.InitialDataPOST.getParamsString;

public class ActivtyAddProduct extends AppCompatActivity
                        implements AdapterView.OnItemClickListener {

    public static final int ADD_BRAND = 1;
    public static final int CHENGE_BRAND = 2;
    public static final int ADD_TIPE_PACAGING = 3;
    public static final int CHENGE_TYPE_PACKAGING = 4;
    public static final int ADD_UNIT_MEASURE = 5;
    public static final int CHENGE_UNIT_MEASURE = 6;
    public static final int CHENGE_PROVIDER = 8;
    public static final int ADD_CHARACTERISTIC = 9;
    public static final int CHENGE_CHARACTERISTIC = 10;
    public static final int ADD_CATEGORY = 11;
    public static final int CHENGE_CATEGORY =12;
    public static final int CHENGE_CATALOG =13;
    public static final int ADD_PRODUCT_NAME = 21;
    public static final int CHENGE_PRODUCT_NAME = 22;
    private Button btnAdd,btnChenge;
    private Intent takeit, intent;
    private TextView tvIntentInfo,tvBrandName,tvChengeFor,tvNameForChenge ;
    private ArrayList<String> alBrands = new ArrayList<>();
    private ArrayList<CounterpartyModel> alBrandsModel = new ArrayList<>();
    //private ArrayAdapter<CounterpartyModel>adapter;
    private ArrayAdapter<String>adapter;
    private ListView lvList;
    private String url_get,activityName;
    private String whatQuestion="";
    private AlertDialog.Builder adb;
    private AlertDialog ad;
    private String url;
    private int input_product_id ,textPosition ,x;
    AllText text;
    final Map<String, String> parameters = new HashMap<>();
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        setTitle(R.string.edit_product);//Редактор товара

        tvIntentInfo=findViewById(R.id.tvIntentInfo);
        tvBrandName=findViewById(R.id.tvBrandName);
        tvChengeFor=findViewById(R.id.tvChengeFor);
        tvNameForChenge=findViewById(R.id.tvNameForChenge);
        btnAdd=findViewById(R.id.btnAdd);
        btnChenge = findViewById(R.id.btnChenge);

        tvChengeFor.setVisibility(View.GONE);
        tvNameForChenge.setVisibility(View.GONE);

        lvList=findViewById(R.id.lvList);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,alBrands);

        takeit = getIntent();
        activityName = takeit.getStringExtra("textView");
        input_product_id = takeit.getIntExtra("input_product_id",x);
        tvBrandName.setText(takeit.getStringExtra("nameBrand"));
        tvIntentInfo.setText(activityName);

        if(activityName.equals(CATALOG)){
            btnAdd.setVisibility(View.GONE);
        }

        url= INPUT;
        //url_get = urlBuilder(url);
        urlBuilder();
        //setInitialData(url_get, whatQuestion);

        lvList.setAdapter(adapter);
        lvList.setOnItemClickListener(this);

        if(activityName.equals("ПОСТАВЩИК")){btnAdd.setVisibility(View.GONE);}
        setResult(000,new Intent());

    }
   /* private String urlBuilder(String url){
        String url_get = url;
        if(activityName.equals(CATALOG)){
            whatQuestion = "catalog_array";
            url_get += "catalog_array";
        }else if(activityName.equals(CATEGORY)){
            whatQuestion = "array";
            url_get += "category_array";
        }
        //----------------------
        else if(activityName.equals(PRODUCT_NAME)){
            whatQuestion = "product_name_array";
            url_get += "product_name_array";
        }
        //------------------------
        else if(activityName.equals(BRAND)){
            whatQuestion = "brand_array";
            url_get += "brand_array";
        }else if(activityName.equals(CHARACTERISTIC)){
            whatQuestion = "characteristic_array";
            url_get += "characteristic_array";
        }else if(activityName.equals(TIPE_PACAGING)){
            whatQuestion = "tipe_pacaging_array";
            url_get += "tipe_pacaging_array";
        }else if(activityName.equals(UNIT_MEASURE)){
            whatQuestion = "unit_measure_array";
            url_get += "unit_measure_array";
        }else if(activityName.equals(PROVIDER)){
            whatQuestion = "provider_array";
            url_get += "provider_array";
            url_get += "&"+"taxpayer_id="+takeit.getLongExtra("taxpayer_id",x);
        }
        return url_get;
    }*/
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void urlBuilder(){
        //String url_get = url;
        String whatQuestion = "";
        final Map<String, String> parameters = new HashMap<>();
        if(activityName.equals(CATALOG)){
            whatQuestion = "catalog_array";
            //url_get += "catalog_array";
            parameters.put("catalog_array","");
        }else if(activityName.equals(CATEGORY)){
            whatQuestion = "array";
            //url_get += "category_array";
            parameters.put("category_array","");
        }
        //----------------------
        else if(activityName.equals(PRODUCT_NAME)){
            whatQuestion = "product_name_array";
            //url_get += "product_name_array";
            parameters.put("product_name_array","");
        }
        //------------------------
        else if(activityName.equals(BRAND)){
            whatQuestion = "brand_array";
            //url_get += "brand_array";
            parameters.put("brand_array","");
        }else if(activityName.equals(CHARACTERISTIC)){
            whatQuestion = "characteristic_array";
           // url_get += "characteristic_array";
            parameters.put("characteristic_array","");
        }else if(activityName.equals(TIPE_PACAGING)){
            whatQuestion = "tipe_pacaging_array";
            //url_get += "tipe_pacaging_array";
            parameters.put("tipe_pacaging_array","");
        }else if(activityName.equals(UNIT_MEASURE)){
            whatQuestion = "unit_measure_array";
            //url_get += "unit_measure_array";
            parameters.put("unit_measure_array","");
        }else if(activityName.equals(PROVIDER)){
            whatQuestion = "provider_array";
            //url_get += "provider_array";
            //url_get += "&"+"taxpayer_id="+takeit.getLongExtra("taxpayer_id",x);
            parameters.put("provider_array","");
            parameters.put("taxpayer_id",String.valueOf(takeit.getLongExtra("taxpayer_id",x)));
        }
        setInitialDataPOST(INPUT, parameters,whatQuestion);
        //return url_get;
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
                if(whatQuestion.equals("catalog_array") || whatQuestion.equals("array") ||
                        whatQuestion.equals("product_name_array") || whatQuestion.equals("brand_array") ||
                        whatQuestion.equals("characteristic_array") || whatQuestion.equals("tipe_pacaging_array") ||
                        whatQuestion.equals("unit_measure_array") ) {
                    splitResultAddProduct(result);
                }
                else if(whatQuestion.equals("provider_array")) {
                    splitResultCounterparty(result);
                }
                else Toast.makeText(ActivtyAddProduct.this,"" + result, Toast.LENGTH_SHORT).show();

                //скрыть диалоговое окно
                asyncDialog.dismiss();
            }
        };
        task.execute(url, getParamsString(param));
    }
  /*  private void setInitialData(String url_get, String whatQuestion) {

        InitialData task=new InitialData(){
            protected void onPostExecute(String result) {
                //Do your thing
                if(whatQuestion.equals("catalog_array") || whatQuestion.equals("array") ||
                        whatQuestion.equals("product_name_array") || whatQuestion.equals("brand_array") ||
                   whatQuestion.equals("characteristic_array") || whatQuestion.equals("tipe_pacaging_array") ||
                        whatQuestion.equals("unit_measure_array") ) {
                    splitResultAddProduct(result);
                }else if(whatQuestion.equals("provider_array")) {
                    splitResultCounterparty(result);
                }else Toast.makeText(ActivtyAddProduct.this,"" + result, Toast.LENGTH_SHORT).show();

            }
        };
        task.execute(url_get);
    }*/
    private void splitResultAddProduct(String result){  // разобрать результат с сервера список продуктов и колличество
        alBrands.clear();
        String [] res=result.split("<br>");
        // Toast.makeText(this, "res: "+res[0], Toast.LENGTH_SHORT).show();
        for(int i=0;i<res.length;i++){
            alBrands.add(res[i]);
        }
        //Toast.makeText(this, ""+addProducts.get(0), Toast.LENGTH_SHORT).show();
        adapter.notifyDataSetChanged();
    }
    private void splitResultCounterparty(String result){  // разобрать результат с сервера список продуктов и колличество
        alBrands.clear();
        alBrandsModel.clear();
        String [] res=result.split("<br>");
         //Toast.makeText(this, "res: "+res[0], Toast.LENGTH_SHORT).show();
        for(int i=0;i<res.length;i++){
            String []temp = res[i].split("&nbsp");
            String abbreviation = temp[0];
            String counterparty = temp[1];
            long taxpayer_id = Long.parseLong(temp[2]);

            CounterpartyModel counterpartyModel = new CounterpartyModel
                    (abbreviation,counterparty,taxpayer_id);
            alBrandsModel.add(counterpartyModel);
            alBrands.add(counterpartyModel.toString());
            //alBrands.add(res[i]);
        }
        //Toast.makeText(this, ""+addProducts.get(0), Toast.LENGTH_SHORT).show();
        adapter.notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void addDada(View view) {  //button CATEGORY
        if(activityName.equals(CATEGORY)) {
            addDataCategory();
        }else if(activityName.equals(PRODUCT_NAME)) {
            addDataProductName();
        }else if(activityName.equals(BRAND)) {
            addDataBrand();
        }else if(activityName.equals(CHARACTERISTIC)){
            addDataCharacteristic();
        }else if(activityName.equals(TIPE_PACAGING)){
            addDataTypePackaging();
        }else if(activityName.equals(UNIT_MEASURE)){
            addDataUnitMeasure();
        }


    }

    public void chengeData(View view) { //изменить данные button
        if(activityName.equals(CATALOG)) {
            chengeDataCatalog();
        }else if(activityName.equals(CATEGORY)) {
            chengeDataCategory();
        }else if(activityName.equals(PRODUCT_NAME)) {
            chengeDataProductName();
        }else if(activityName.equals(BRAND)) {
            chengeDataBrand();
        }else if(activityName.equals(CHARACTERISTIC)){
            chengeDataCharacteristic();
        }else if(activityName.equals(TIPE_PACAGING)){
            chengeDataTypePackaging();
        }else if(activityName.equals(UNIT_MEASURE)) {
            chengeDataUnitMeasure();
        }else if(activityName.equals(PROVIDER)){
            chengeDataProvider();
        }
    }
    /*private String firstSimbolMakeSmol(String string){
        if(string == null || string.isEmpty())return string;
        return string.substring(0,1).toUpperCase() + string.substring(1);
    }*/

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        tvChengeFor.setVisibility(View.VISIBLE);
        tvNameForChenge.setVisibility(View.VISIBLE);
        tvNameForChenge.setText(alBrands.get(position).toString());
        textPosition = position;
        //Toast.makeText(this, "lvPosition: "+position, Toast.LENGTH_SHORT).show();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addDataUnitMeasure(){
        String whatQuestion = "add_unit_measure";
        /*url_get = url;
        url_get += "add_unit_measure";
        url_get += "&" + "unit_measure=" + tvBrandName.getText().toString().trim();
        setInitialData(url_get, whatQuestion);*/
        parameters.put("add_unit_measure","");
        parameters.put("unit_measure",tvBrandName.getText().toString().trim());
        setInitialDataPOST(INPUT, parameters, whatQuestion);
        intent = new Intent();
        intent.putExtra("position_return", takeit.getIntExtra("position", x));
        setResult(ADD_UNIT_MEASURE, intent);
        finish();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addDataTypePackaging(){
        String whatQuestion = "add_type_packaging";
        /*url_get = url;
        url_get += "add_type_packaging";
        url_get += "&" + "type_packaging=" + tvBrandName.getText().toString().trim();
        setInitialData(url_get, whatQuestion);*/
        parameters.put("add_type_packaging","");
        parameters.put("type_packaging", tvBrandName.getText().toString().trim());
        setInitialDataPOST(INPUT, parameters, whatQuestion);
        intent = new Intent();
        intent.putExtra("position_return", takeit.getIntExtra("position", x));
        setResult(ADD_TIPE_PACAGING, intent);
        finish();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addDataCharacteristic(){
        String whatQuestion = "add_characteristic";
        /*url_get = url;
        url_get += "add_characteristic";
        url_get += "&" + "characteristic=" + tvBrandName.getText().toString().trim();
        setInitialData(url_get, whatQuestion);*/
        parameters.put("add_characteristic","");
        parameters.put("characteristic", tvBrandName.getText().toString().trim());
        setInitialDataPOST(INPUT, parameters, whatQuestion);
        intent = new Intent();
        intent.putExtra("position_return", takeit.getIntExtra("position", x));
        setResult(ADD_CHARACTERISTIC, intent);
        finish();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addDataProductName(){
        String whatQuestion = "add_product_name";
        /*url_get = url;
        url_get += "add_product_name";
        url_get += "&" + "product_name=" + tvBrandName.getText().toString().trim();
        setInitialData(url_get, whatQuestion);*/
        parameters.put("add_product_name","");
        parameters.put("product_name", tvBrandName.getText().toString().trim());
        setInitialDataPOST(INPUT, parameters, whatQuestion);
        intent = new Intent();
        intent.putExtra("position_return", takeit.getIntExtra("position", x));
        setResult(ADD_PRODUCT_NAME, intent);
        finish();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addDataBrand(){
        String whatQuestion = "add_brand";
        /*url_get = url;
        url_get += "add_brand";
        url_get += "&" + "brand=" + tvBrandName.getText().toString().trim();
        setInitialData(url_get, whatQuestion);*/
        parameters.put("add_brand","");
        parameters.put("brand", tvBrandName.getText().toString().trim());
        setInitialDataPOST(INPUT, parameters, whatQuestion);
        intent = new Intent();
        intent.putExtra("position_return", takeit.getIntExtra("position", x));
        setResult(ADD_BRAND, intent);
        finish();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addDataCategory(){
        String whatQuestion = "add_category";
        /*url_get = url;
        url_get += "add_category";
        url_get += "&" + "category=" + tvBrandName.getText().toString().trim();
        url_get += "&" + "catalog=" + takeit.getStringExtra("catalog_name");
        setInitialData(url_get, whatQuestion);*/
        parameters.put("add_category","");
        parameters.put("category", tvBrandName.getText().toString().trim());
        parameters.put("catalog", takeit.getStringExtra("catalog_name"));
        setInitialDataPOST(INPUT, parameters, whatQuestion);
        intent = new Intent();
        intent.putExtra("position_return", takeit.getIntExtra("position", x));
        setResult(ADD_CATEGORY, intent);
        finish();
    }
    private void chengeDataProvider(){
        if (!tvNameForChenge.getText().toString().equals("")) {
            adb = new AlertDialog.Builder(this);
            String str1 = text.TEXT001;
            String str2 = text.TEXT002 + "\n\n"
                    + ">>" + tvBrandName.getText().toString() + "\n\n"
                    + text.TEXT003 + "\n\n"
                    + ">>" + tvNameForChenge.getText().toString()
                    + text.TEXT004;
            adb.setTitle(str1);
            adb.setMessage(str2);
            adb.setPositiveButton(text.YES_CHENGE,
                    new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String stNameForChenge = tvNameForChenge.getText().toString().trim();
                            String whatQuestion = "update_provider";
                           /* url_get = url;
                            url_get += "chenge_provider";
                            url_get += "&" + "input_product_id=" + input_product_id;
                            url_get += "&" + "taxpayer_id=" + alBrandsModel.get(textPosition).getTaxpayer_id();
                            setInitialData(url_get, whatQuestion);*/
                            parameters.put("chenge_provider","");
                            parameters.put("input_product_id", String.valueOf(input_product_id));
                            parameters.put("taxpayer_id", String.valueOf(alBrandsModel.get(textPosition).getTaxpayer_id()));
                            setInitialDataPOST(INPUT, parameters, whatQuestion);
                            intent = new Intent();
                            intent.putExtra("position_return", takeit.getIntExtra("position", x));
                            intent.putExtra("abbreviation_for_chenge", alBrandsModel.get(textPosition).getAbbreviation());
                            intent.putExtra("name_for_chenge", alBrandsModel.get(textPosition).getCounterparty());
                            intent.putExtra("tax_id", alBrandsModel.get(textPosition).getTaxpayer_id());
                            setResult(CHENGE_PROVIDER, intent);
                            finish();
                        }
                    });
            adb.setNegativeButton(text.NO_BACK,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            tvChengeFor.setVisibility(View.GONE);
                            tvNameForChenge.setText("");
                            tvNameForChenge.setVisibility(View.GONE);
                        }
                    });
            ad = adb.create();
            ad.show();
        } else Toast.makeText(this, "" + text.MAKE_CHOICE, Toast.LENGTH_SHORT).show();
    }
    private void chengeDataUnitMeasure(){
        if (!tvNameForChenge.getText().toString().equals("")) {
            adb = new AlertDialog.Builder(this);
            String str1 = text.TEXT001;
            String str2 = text.TEXT002 + "\n\n"
                    + ">>" + tvBrandName.getText().toString() + "\n\n"
                    + text.TEXT003 + "\n\n"
                    + ">>" + tvNameForChenge.getText().toString()
                    + text.TEXT004;
            adb.setTitle(str1);
            adb.setMessage(str2);
            adb.setPositiveButton(text.YES_CHENGE,
                    new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String stNameForChenge = tvNameForChenge.getText().toString();
                            String whatQuestion = "update_unit_measure";
                          /*  url_get = url;
                            url_get += "chenge_unit_measure";
                            url_get += "&" + "input_product_id=" + input_product_id;
                            url_get += "&" + "unit_measure=" + stNameForChenge;
                            setInitialData(url_get, whatQuestion);*/
                            parameters.put("chenge_unit_measure","");
                            parameters.put("input_product_id", String.valueOf(input_product_id));
                            parameters.put("unit_measure", stNameForChenge);
                            setInitialDataPOST(INPUT, parameters, whatQuestion);
                            intent = new Intent();
                            intent.putExtra("position_return", takeit.getIntExtra("position", x));
                            intent.putExtra("name_for_chenge", stNameForChenge);
                            setResult(CHENGE_UNIT_MEASURE, intent);
                            finish();
                            //Toast.makeText(ActivtyAddProduct.this,
                            //       "url_get: "+url_get, Toast.LENGTH_SHORT).show();
                        }
                    });
            adb.setNegativeButton(text.NO_BACK,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            tvChengeFor.setVisibility(View.GONE);
                            tvNameForChenge.setText("");
                            tvNameForChenge.setVisibility(View.GONE);
                        }
                    });
            ad = adb.create();
            ad.show();
        } else Toast.makeText(this, "" + text.MAKE_CHOICE, Toast.LENGTH_SHORT).show();
    }
    private void chengeDataTypePackaging(){
        if (!tvNameForChenge.getText().toString().equals("")) {
            adb = new AlertDialog.Builder(this);
            String str1 = text.TEXT001;
            String str2 = text.TEXT002 + "\n\n"
                    + ">>" + tvBrandName.getText().toString() + "\n\n"
                    + text.TEXT003 + "\n\n"
                    + ">>" + tvNameForChenge.getText().toString()
                    + text.TEXT004;
            adb.setTitle(str1);
            adb.setMessage(str2);
            adb.setPositiveButton(text.YES_CHENGE,
                    new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String stNameForChenge = tvNameForChenge.getText().toString();
                            String whatQuestion = "update_type_packaging";
                           /* url_get = url;
                            url_get += "chenge_type_packaging";
                            url_get += "&" + "input_product_id=" + input_product_id;
                            url_get += "&" + "type_packaging=" + stNameForChenge;
                            setInitialData(url_get, whatQuestion);*/
                            parameters.put("chenge_type_packaging","");
                            parameters.put("input_product_id", String.valueOf(input_product_id));
                            parameters.put("type_packaging", stNameForChenge);
                            setInitialDataPOST(INPUT, parameters, whatQuestion);
                            intent = new Intent();
                            intent.putExtra("position_return", takeit.getIntExtra("position", x));
                            intent.putExtra("name_for_chenge", stNameForChenge);
                            setResult(CHENGE_TYPE_PACKAGING, intent);
                            finish();
                            //Toast.makeText(ActivtyAddProduct.this,
                            //       "url_get: "+url_get, Toast.LENGTH_SHORT).show();
                        }
                    });
            adb.setNegativeButton(text.NO_BACK,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            tvChengeFor.setVisibility(View.GONE);
                            tvNameForChenge.setText("");
                            tvNameForChenge.setVisibility(View.GONE);
                        }
                    });
            ad = adb.create();
            ad.show();
        } else Toast.makeText(this, "" + text.MAKE_CHOICE, Toast.LENGTH_SHORT).show();
    }
    private void chengeDataCharacteristic(){//update_characteristic
        if (!tvNameForChenge.getText().toString().equals("")) {
            adb = new AlertDialog.Builder(this);
            String str1 = text.TEXT001;
            String str2 = text.TEXT002 + "\n\n"
                    + ">>" + tvBrandName.getText().toString() + "\n\n"
                    + text.TEXT003 + "\n\n"
                    + ">>" + tvNameForChenge.getText().toString()
                    + text.TEXT004;
            adb.setTitle(str1);
            adb.setMessage(str2);
            adb.setPositiveButton(text.YES_CHENGE,
                    new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String stNameForChenge = tvNameForChenge.getText().toString();
                            String whatQuestion = "update_characteristic";
                           /* url_get = url;
                            url_get += "chenge_characteristic";
                            url_get += "&" + "input_product_id=" + input_product_id;
                            url_get += "&" + "characteristic=" + stNameForChenge;
                            setInitialData(url_get, whatQuestion);*/
                            parameters.put("chenge_characteristic","");
                            parameters.put("input_product_id", String.valueOf(input_product_id));
                            parameters.put("characteristic", stNameForChenge);
                            setInitialDataPOST(INPUT, parameters, whatQuestion);
                            intent = new Intent();
                            intent.putExtra("position_return", takeit.getIntExtra("position", x));
                            intent.putExtra("name_for_chenge", stNameForChenge);
                            setResult(CHENGE_CHARACTERISTIC, intent);
                            finish();
                        }
                    });
            adb.setNegativeButton(text.NO_BACK,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            tvChengeFor.setVisibility(View.GONE);
                            tvNameForChenge.setText("");
                            tvNameForChenge.setVisibility(View.GONE);
                        }
                    });
            ad = adb.create();
            ad.show();
        } else Toast.makeText(this, "" + text.MAKE_CHOICE, Toast.LENGTH_SHORT).show();
    }//
    //------------------------------>>
    private void chengeDataProductName(){
        if (!tvNameForChenge.getText().toString().trim().equals("")) {
            adb = new AlertDialog.Builder(this);
            String str1 = text.TEXT001;
            String str2 = text.TEXT002 + "\n\n"
                    + ">>" + tvBrandName.getText().toString() + "\n\n"
                    + text.TEXT003 + "\n\n"
                    + ">>" + tvNameForChenge.getText().toString()
                    + text.TEXT004;
            adb.setTitle(str1);
            adb.setMessage(str2);
            adb.setPositiveButton(text.YES_CHENGE,
                    new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String stNameForChenge = tvNameForChenge.getText().toString();
                            String whatQuestion = "chenge_product_name";
                           /* url_get = url;
                            url_get += "chenge_product_name";
                            url_get += "&" + "input_product_id=" + input_product_id;
                            url_get += "&" + "product_name=" + stNameForChenge;
                            setInitialData(url_get, whatQuestion);*/
                            parameters.put("chenge_product_name","");
                            parameters.put("input_product_id", String.valueOf(input_product_id));
                            parameters.put("product_name", stNameForChenge);
                            setInitialDataPOST(INPUT, parameters, whatQuestion);
                            intent = new Intent();
                            intent.putExtra("position_return", takeit.getIntExtra("position", x));
                            intent.putExtra("name_for_chenge", stNameForChenge);
                            setResult(CHENGE_PRODUCT_NAME, intent);
                            finish();
                        }
                    });
            adb.setNegativeButton(text.NO_BACK,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            tvChengeFor.setVisibility(View.GONE);
                            tvNameForChenge.setText("");
                            tvNameForChenge.setVisibility(View.GONE);
                        }
                    });
            ad = adb.create();
            ad.show();
        } else Toast.makeText(this, "" + text.MAKE_CHOICE, Toast.LENGTH_SHORT).show();
    }
    //---------------------------------
    private void chengeDataBrand(){
        if (!tvNameForChenge.getText().toString().equals("")) {
            adb = new AlertDialog.Builder(this);
            String str1 = text.TEXT001;
            String str2 = text.TEXT002 + "\n\n"
                    + ">>" + tvBrandName.getText().toString() + "\n\n"
                    + text.TEXT003 + "\n\n"
                    + ">>" + tvNameForChenge.getText().toString()
                    + text.TEXT004;
            adb.setTitle(str1);
            adb.setMessage(str2);
            adb.setPositiveButton(text.YES_CHENGE,
                    new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String stNameForChenge = tvNameForChenge.getText().toString();
                            String whatQuestion = "update_brand";
                           /* url_get = url;
                            url_get += "chenge_brand";
                            url_get += "&" + "input_product_id=" + input_product_id;
                            url_get += "&" + "brand=" + stNameForChenge;
                            setInitialData(url_get, whatQuestion);*/
                            parameters.put("chenge_brand","");
                            parameters.put("input_product_id", String.valueOf(input_product_id));
                            parameters.put("brand", stNameForChenge);
                            setInitialDataPOST(INPUT, parameters, whatQuestion);
                            intent = new Intent();
                            intent.putExtra("position_return", takeit.getIntExtra("position", x));
                            intent.putExtra("name_for_chenge", stNameForChenge);
                            setResult(CHENGE_BRAND, intent);
                            finish();
                            //Toast.makeText(ActivtyAddProduct.this,
                            //       "url_get: "+url_get, Toast.LENGTH_SHORT).show();
                        }
                    });
            adb.setNegativeButton(text.NO_BACK,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            tvChengeFor.setVisibility(View.GONE);
                            tvNameForChenge.setText("");
                            tvNameForChenge.setVisibility(View.GONE);
                        }
                    });
            ad = adb.create();
            ad.show();
        } else Toast.makeText(this, "" + text.MAKE_CHOICE, Toast.LENGTH_SHORT).show();
    }
    private void chengeDataCategory(){
        if (!tvNameForChenge.getText().toString().equals("")) {
            adb = new AlertDialog.Builder(this);
            String str1 = text.TEXT001;
            String str2 = text.TEXT002 + "\n\n"
                    + ">>" + tvBrandName.getText().toString() + "\n\n"
                    + text.TEXT003 + "\n\n"
                    + ">>" + tvNameForChenge.getText().toString()
                    + text.TEXT004;
            adb.setTitle(str1);
            adb.setMessage(str2);
            adb.setPositiveButton(text.YES_CHENGE,
                    new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String stNameForChenge = tvNameForChenge.getText().toString();
                            String whatQuestion = "update_category";
                            /*url_get = url;
                            url_get += "chenge_category";
                            url_get += "&" + "input_product_id=" + input_product_id;
                            url_get += "&" + "category=" + stNameForChenge;
                            setInitialData(url_get, whatQuestion);*/
                            parameters.put("chenge_category","");
                            parameters.put("input_product_id", String.valueOf(input_product_id));
                            parameters.put("category", stNameForChenge);
                            setInitialDataPOST(INPUT, parameters, whatQuestion);
                            intent = new Intent();
                            intent.putExtra("position_return", takeit.getIntExtra("position", x));
                            intent.putExtra("name_for_chenge", stNameForChenge);
                            setResult(CHENGE_CATEGORY, intent);
                            finish();
                        }
                    });
            adb.setNegativeButton(text.NO_BACK,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            tvChengeFor.setVisibility(View.GONE);
                            tvNameForChenge.setText("");
                            tvNameForChenge.setVisibility(View.GONE);
                        }
                    });
            ad = adb.create();
            ad.show();
        } else Toast.makeText(this, "" + text.MAKE_CHOICE, Toast.LENGTH_SHORT).show();
    }
    private void chengeDataCatalog(){
        if (!tvNameForChenge.getText().toString().equals("")) {
            adb = new AlertDialog.Builder(this);
            String str1 = text.TEXT001;
            String str2 = text.TEXT002 + "\n\n"
                    + ">>" + tvBrandName.getText().toString() + "\n\n"
                    + text.TEXT003 + "\n\n"
                    + ">>" + tvNameForChenge.getText().toString()
                    + text.TEXT004;
            adb.setTitle(str1);
            adb.setMessage(str2);
            adb.setPositiveButton(text.YES_CHENGE,
                    new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String stNameForChenge = tvNameForChenge.getText().toString();
                            String whatQuestion = "update_catalog";
                            /*url_get = url;
                            url_get += "chenge_catalog";
                            url_get += "&" + "input_product_id=" + input_product_id;
                            url_get += "&" + "catalog=" + stNameForChenge;
                            setInitialData(url_get, whatQuestion);*/
                            parameters.put("chenge_catalog","");
                            parameters.put("input_product_id", String.valueOf(input_product_id));
                            parameters.put("catalog", stNameForChenge);
                            setInitialDataPOST(INPUT, parameters, whatQuestion);
                            intent = new Intent();
                            intent.putExtra("position_return", takeit.getIntExtra("position", x));
                            intent.putExtra("name_for_chenge", stNameForChenge);
                            setResult(CHENGE_CATALOG, intent);
                            finish();
                        }
                    });
            adb.setNegativeButton(text.NO_BACK,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            tvChengeFor.setVisibility(View.GONE);
                            tvNameForChenge.setText("");
                            tvNameForChenge.setVisibility(View.GONE);
                        }
                    });
            ad = adb.create();
            ad.show();
        } else Toast.makeText(this, "" + text.MAKE_CHOICE, Toast.LENGTH_SHORT).show();
    }
}