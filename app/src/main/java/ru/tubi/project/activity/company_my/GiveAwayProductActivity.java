package ru.tubi.project.activity.company_my;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ru.tubi.project.activity.MainActivity;
import ru.tubi.project.R;
import ru.tubi.project.activity.ActivityCatalog;
import ru.tubi.project.activity.MenuActivity;
import ru.tubi.project.adapters.GiveAwayProductAdapter;
import ru.tubi.project.activity.invoice.ProductInvoicePDFActivity;
import ru.tubi.project.models.TransferModel;
import ru.tubi.project.utilites.Constant;
import ru.tubi.project.utilites.InitialData;

import static ru.tubi.project.free.AllCollor.TUBI_GREY_200;
import static ru.tubi.project.free.AllCollor.alert_dialog_button_green_pressed;
import static ru.tubi.project.free.AllText.CLOSE_INVOICE;
import static ru.tubi.project.free.AllText.DOCUMENT_SAVED_BIG;
import static ru.tubi.project.free.AllText.DONE_BIG;
import static ru.tubi.project.free.AllText.FOR_SMALL;
import static ru.tubi.project.free.AllText.GIVE_AWAY_PRODUCT;
import static ru.tubi.project.free.AllText.INVOICE;
import static ru.tubi.project.free.AllText.INVOICE_I_AM;
import static ru.tubi.project.free.AllText.LIST_OF_AVAILABLE_DOCUMENTS;
import static ru.tubi.project.free.AllText.LOAD_TEXT;
import static ru.tubi.project.free.AllText.MAKE_CHOICE;
import static ru.tubi.project.free.AllText.MES_19;
import static ru.tubi.project.free.AllText.NOT_SAVED_SMALL;
import static ru.tubi.project.free.AllText.PRESS_NEW;
import static ru.tubi.project.free.AllText.PRICE_NEW_IS_NOT;
import static ru.tubi.project.free.AllText.PRODUCT_INVOICE_DOC;
import static ru.tubi.project.free.AllText.RETURN_BIG;
import static ru.tubi.project.free.AllText.SAVE_BIG;
import static ru.tubi.project.free.AllText.SAVE_INVOICE;
import static ru.tubi.project.free.AllText.SAVE_QUESTION;
import static ru.tubi.project.free.AllText.TRANSFER_PRODUCT_INVOICE_DOC;
import static ru.tubi.project.free.AllText.TRANSPORTATION_SMALL;

public class GiveAwayProductActivity extends AppCompatActivity implements View.OnClickListener {

    private Intent takeit;
    private TextView tvMyWarehouseInfo, tvSaveOrNewInvoice, tvInvoiceNameAndNum;
    private RecyclerView recyclerView;
    private LinearLayout llSaveOrNewInvoice;
    private boolean saveFlag, allCheckFlag;
    private int out_warehouse_id, in_warehouse_id, car_id, logistic_product, invoiceKey=0
                        , saveDocInfo=0, document_num =0;
    public static int saveDocInfotwo=0, invoiceKeyForAdapter = 0;
    private String myWarehousInfo, in_WarehousInfo, car_info_string;
    private ArrayList<TransferModel> product_list = new ArrayList<>();
    private ArrayList <String> docsNameList = new ArrayList<>();

    private GiveAwayProductAdapter adapter;
    private AlertDialog.Builder adb;
    private AlertDialog ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_away_product);
        setTitle(GIVE_AWAY_PRODUCT);//Отдать товар

        recyclerView=(RecyclerView)findViewById(R.id.rvList);
        tvMyWarehouseInfo=findViewById(R.id.tvMyWarehouseInfo);
        tvSaveOrNewInvoice=findViewById(R.id.tvSaveOrNewInvoice);
        tvInvoiceNameAndNum=findViewById(R.id.tvInvoiceNameAndNum);
        llSaveOrNewInvoice=findViewById(R.id.llSaveOrNewInvoice);

        llSaveOrNewInvoice.setOnClickListener(this);

        takeit = getIntent();
        out_warehouse_id=takeit.getIntExtra("out_warehouse_id",0);
        in_warehouse_id=takeit.getIntExtra("in_warehouse_id",0);
        car_id=takeit.getIntExtra("car_id",0);
        logistic_product=takeit.getIntExtra("logistic_product",0);
        myWarehousInfo=takeit.getStringExtra("myWarehousInfo");
        in_WarehousInfo=takeit.getStringExtra("in_WarehousInfo");
        car_info_string=takeit.getStringExtra("car_info_string");

        if(car_id != 0) tvMyWarehouseInfo.setText(""+myWarehousInfo+"\n"+FOR_SMALL+" "
                +in_WarehousInfo+"\n"+TRANSPORTATION_SMALL+" "+car_info_string);
        else tvMyWarehouseInfo.setText(""+myWarehousInfo+"\n"+FOR_SMALL+" "+in_WarehousInfo);

        //получить ключ документа или создать
        //receiveInvoiceKey();

        //получить список товаров
        receiveProductList();

        GiveAwayProductAdapter.OnCheckedChangeListener checkedChangeListener =
                new GiveAwayProductAdapter.OnCheckedChangeListener() {
                    @Override
                    public void isChecked(View view, boolean flag, int position) {
                        whatCheckClicked(flag, position);
                        //Toast.makeText(GiveAwayProductActivity.this, "check", Toast.LENGTH_SHORT).show();
                    }
                };
        adapter=new GiveAwayProductAdapter(this,product_list,saveDocInfo,checkedChangeListener);
        recyclerView.setAdapter(adapter);

    }
    @Override
    public void onClick(View v) {
        if(v.equals(llSaveOrNewInvoice)){
            //проверить есть хоть oдин товар в документе
            boolean fullFlag = false;
            for(int i=0;i < product_list.size();i++){
                if(product_list.get(i).getOut_active() == 1
                        && product_list.get(i).getInvoice_key_id() == invoiceKey){
                    fullFlag = true;
                    break;
                }
            }

            if(tvSaveOrNewInvoice.getText().toString()
                    .equals(getResources().getText(R.string.new_text))){
                //получить/создать номер товарной накладной
                receiveOrCreateInvoiceProductNumber();
            }else if(tvSaveOrNewInvoice.getText().toString()
                    .equals(getResources().getText(R.string.save))){

                if(fullFlag){
                    //сохранить накладную /документ(save = 1)
                    createInvoiceKeySave();

                    //обновить/пересоздать активность
                    finish();
                    startActivity(getIntent());
                   // recreate();

                }else{
                    Toast.makeText(this, ""+MAKE_CHOICE, Toast.LENGTH_SHORT).show();
                }
            }else{
                if(fullFlag){
                    //закрыь ключ / документ
                    adCloseInvoice();

                }else{
                    Toast.makeText(this, ""+MAKE_CHOICE, Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    //сделать правильную кнопку
    private void makeRightButton(){
        //документ еще не создан
        if(document_num == 0){
            tvSaveOrNewInvoice.setText(getResources().getText(R.string.new_text));
        }else{
            //документ есть но не сохранен / сохранить
            if(saveDocInfo == 0){
                tvSaveOrNewInvoice.setText(getResources().getText(R.string.save));
            }
            //документ есть и сохранен / закрыть
            else{
                tvSaveOrNewInvoice.setText(getResources().getText(R.string.to_close));
            }
        }
    }
    //сохранить документ(save = 1)
   /* private void receiveInvoiceKeySave(){
        String url = Constant.WAREHOUSE_OFFICE;
        url += "receive_invoice_key_save";
        url += "&"+"invoice_key="+invoiceKey;
        String whatQuestion = "receive_invoice_key_save";
        setInitialData(url, whatQuestion);
    }*/
    //закрыь ключ / документ
    private void closeInvoiceKey(){
        String url = Constant.WAREHOUSE_OFFICE;
        url += "close_invoice_key";
        url += "&"+"invoice_key="+invoiceKey;
        String whatQuestion = "close_invoice_key";
        setInitialData(url, whatQuestion);
    }
    //сохранить документ(save = 1)
    private void createInvoiceKeySave(){
        String url = Constant.WAREHOUSE_OFFICE;
        url += "create_invoice_key_save";
        url += "&"+"invoice_key="+invoiceKey;
        String whatQuestion = "create_invoice_key_save";
        setInitialData(url, whatQuestion);
    }
    //получить номер товарной накладной
    //(сохраненной/открытой)
    private void receiveInvoice(){
        String url = Constant.WAREHOUSE_OFFICE;
        url += "receive_invoice";
        url += "&"+"invoice_key="+invoiceKey;
        String whatQuestion = "receive_invoice";
        setInitialData(url, whatQuestion);
    }
    /*
     //сохранить документ(close = 1)
    private void createInvoiceKeyClosed(){
        String url = Constant.WAREHOUSE_OFFICE;
        url += "create_invoice_key_closed";
        url += "&"+"invoice_key="+invoiceKey;
        String whatQuestion = "create_invoice_key_closed";
        setInitialData(url, whatQuestion);
    }
     */
    //проверить документ закрыт или в работе close = 0 or =1;
   /* private void receiveInvoiceKeyClosed(){
        String url = Constant.WAREHOUSE_OFFICE;
        url += "receive_invoice_key_closed";
        url += "&"+"invoice_key="+invoiceKey;
        String whatQuestion = "receive_invoice_key_closed";
        setInitialData(url, whatQuestion);
    }*/
    //указать ключ для документов в таблицах к этой записи
    //и установить или отменить галочку (передан товар)
    private void whatCheckClicked(boolean flag, int position){
        //если накладная не открыта, попросить открыть
        if(tvSaveOrNewInvoice.getText().toString()
                .equals(getResources().getText(R.string.new_text))){
            Toast.makeText(this, ""+PRESS_NEW, Toast.LENGTH_LONG).show();
            adapter.notifyDataSetChanged();
            return;
        }
        int invoice_key_id = product_list.get(position).getInvoice_key_id();
        int warehouseInventory_id = product_list.get(position).getWarehouseInventory_id();

        if(invoice_key_id != invoiceKey){
            product_list.get(position).setInvoice_key_id(invoiceKey);
            invoice_key_id = invoiceKey;
        }
        //установить или отменить галочку (передан товар)
        int checked = 0;
        if(flag){
            checked=1;
        }else{
            //если галочка снята то ключ из таблиц тоже убрать
            invoice_key_id = 0;
        }

        product_list.get(position).setOut_active(checked);

        //указать ключ для документов в таблицах к этой записи
        //установить или отменить галочку (передан товар)
        String url = Constant.WAREHOUSE_OFFICE;
        url += "write_to_invoice_key";
        url += "&"+"warehouse_inventory_id="+warehouseInventory_id;
        url += "&"+"invoice_key="+invoice_key_id;
        String whatQuestion = "write_to_invoice_key";
        setInitialData(url, whatQuestion);

        Log.d("A111","write_to_invoice_key / url: "+url);

        //установить или отменить галочку (передан товар)
        url = Constant.WAREHOUSE_OFFICE;
        url += "chenge_checked_provider";
        url += "&"+"warehouse_inventory_id="+warehouseInventory_id;
        url += "&"+"checked="+checked;
        whatQuestion = "chenge_checked_provider";
        setInitialData(url, whatQuestion);

        Log.d("A111","chenge_checked_provider / url: "+url);
    }
    //получить список товаров для выдачи и оформления документов
    private void receiveProductList(){
        String url = Constant.WAREHOUSE_OFFICE;
        url += "receive_product_list";
        url += "&"+"out_warehouse_id="+out_warehouse_id;
        url += "&"+"in_warehouse_id="+in_warehouse_id;
        url += "&"+"car_id="+car_id;
        //url += "&"+"logistic_product="+logistic_product;
        String whatQuestion = "receive_product_list";
        setInitialData(url, whatQuestion);
    }
    //получить ключ документа или создать, проверить сохранен документ?
    private void receiveInvoiceKey(){
        String url = Constant.PROVIDER_OFFICE;
        url += "receive_invoice_key";
        url += "&"+"out_warehouse_id="+out_warehouse_id;
        url += "&"+"in_warehouse_id="+in_warehouse_id;
        url += "&"+"car_id="+car_id;
        String whatQuestion = "receive_invoice_key";
        setInitialData(url, whatQuestion);
    }
    //получить/создать номер товарной накладной
    private void receiveOrCreateInvoiceProductNumber(){
        String url = Constant.API;
        url += "receive_or_create_invoice_product_number";
        url += "&"+"invoiceKey="+invoiceKey;
        String whatQuestion = "receive_or_create_invoice_product_number";
        setInitialData(url, whatQuestion);

        //обновить/пересоздать активность
        finish();
        startActivity(getIntent());
        //recreate();
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
                if(whatQuestion.equals("receive_invoice_key")){
                    splitInvoiceKeyResult(result);
                }else if(whatQuestion.equals("receive_product_list")){
                    splitProductListResult(result);
                }
                else if(whatQuestion.equals("receive_invoice")){
                    splitInvoiceNumResult(result);
                }
                else if(whatQuestion.equals("create_invoice_key_save")){
                    splitInvoiceKeySaveResult(result);
                }

                //скрыть диалоговое окно
                asyncDialog.dismiss();
            }
        };
        task.execute(url_get);
    }
    private void splitInvoiceNumResult(String result){
        try {
            document_num = Integer.parseInt(result);

            if(allCheckFlag && document_num == 0){
                llSaveOrNewInvoice.setClickable(false);
                Log.d("A111","GiveAwayProductActivity / splitProductListResult " +
                        "/ allCheckFlag = "+allCheckFlag+" document_num = "+document_num);
            }
            else {
                llSaveOrNewInvoice.setClickable(true);
            }
            tvInvoiceNameAndNum.setText(INVOICE_I_AM+" № "+document_num);

            //сделать правильную кнопку
            makeRightButton();

        }catch(Exception ex){
            Log.d("A111","GiveAwayProductActivity / splitInvoiceNumResult / ex: "+ex);
        }

    }
    private void splitInvoiceKeySaveResult(String result){
        try{
            String[] res = result.split("<br>");
            String[] one_temp = res[0].split("&nbsp");
            if (result.equals("RESULT_OK")){
                saveDocInfo = 1;
                saveDocInfotwo=saveDocInfo;
                adapter.notifyDataSetChanged();
                //получить список товаров
                receiveProductList();
                Toast.makeText(this, ""+DOCUMENT_SAVED_BIG, Toast.LENGTH_LONG).show();
            }
            else if (one_temp[0].equals("error") || one_temp[0].equals("messege")) {
                Toast.makeText(this, "" + one_temp[1], Toast.LENGTH_LONG).show();
                return;
            }
        }catch(Exception ex){
            Toast.makeText(this, "Exception: "+ex, Toast.LENGTH_SHORT).show();
        }
    }
    //поолучить статус документа создан сохранен или нет
   /* private void splitInvoiceKeyStatusResult(String result){
        int document_status = Integer.parseInt(result);

        boolean checkFlag = true;
        for(int i=0;i < product_list.size();i++){
            if(product_list.get(i).getInvoice_key_id() == 0){
                checkFlag = false;
            }
        }

        adShowDocsNameList(document_status, checkFlag);
    }*/
    //разобрать список товаров к открузке
    // и продуктов которые собраны для выдачи им и их колличество
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void splitProductListResult(String result){
        product_list.clear();
        allCheckFlag = true;
        //Toast.makeText(this, "result\n"+result, Toast.LENGTH_SHORT).show();
        try{
            String[] res = result.split("<br>");
            String[] one_temp = res[0].split("&nbsp");
            if (one_temp[0].equals("error") || one_temp[0].equals("messege")) {
                Toast.makeText(this, "" + one_temp[1], Toast.LENGTH_LONG).show();
                return;
            } else {
                for (int i = 0; i < res.length; i++) {
                    String[] temp = res[i].split("&nbsp");
                    if(temp[0].equals("messege")){
                        Toast.makeText(this, "" + temp[1], Toast.LENGTH_LONG).show();
                    }else {
                        int warehouseInventory_id = Integer.parseInt(temp[0]);
                        int productInventory_id = Integer.parseInt(temp[1]);
                        double quantity = Double.parseDouble(temp[2]);
                        int invoice_key_id = Integer.parseInt(temp[3]);
                        String image_url = temp[4];
                        String description_docs = temp[5];
                        int out_active = Integer.parseInt(temp[6]);
                        String product_name_from_provider = temp[7];

                        TransferModel transfer_product = new TransferModel(warehouseInventory_id
                                , productInventory_id, quantity, invoice_key_id, image_url
                                , description_docs, product_name_from_provider, out_active);

                        product_list.add(transfer_product);

                        if(out_active == 0) allCheckFlag=false;
                    }
                }
            }
        }catch(Exception ex){
            Log.d("A111","GiveAwayProductActivity / splitProductListResult / ex = "+ex);
            Toast.makeText(this, "Exception: "+ex, Toast.LENGTH_SHORT).show();
        }

        //сортируем лист по 2 полям (partner_warehouse_id и Product_inventory_id)
       // product_list.sort(Comparator.comparing(TransferModel::getCar_id)
        //        .thenComparing(TransferModel::getIn_warehouse_id));

        adapter.notifyDataSetChanged();
        if(product_list.size() > 0){
            //получить ключ документа или создать
            receiveInvoiceKey();

           /* if(allCheckFlag && document_num == 0){
                llSaveOrNewInvoice.setClickable(false);
                Log.d("A111","GiveAwayProductActivity / splitProductListResult " +
                        "/ allCheckFlag = "+allCheckFlag+" document_num = "+document_num);
            }
            else {
                llSaveOrNewInvoice.setClickable(true);
            }*/
        }else{
            llSaveOrNewInvoice.setClickable(false);
        }

    }
    private void splitInvoiceKeyResult(String result){
        //Toast.makeText(this, "res:\n"+result, Toast.LENGTH_SHORT).show();
        try{
            String[] res = result.split("<br>");
            String[] one_temp = res[0].split("&nbsp");
            if (one_temp[0].equals("error") || one_temp[0].equals("messege")) {
                Toast.makeText(this, "" + one_temp[1], Toast.LENGTH_LONG).show();
                // adapter_buyer.notifyDataSetChanged();
                onBackPressed();
            } else if(one_temp[0].equals("RESULT_OK") ){
                invoiceKey = Integer.parseInt(one_temp[1]);
                saveDocInfo = Integer.parseInt(one_temp[2]);

                invoiceKeyForAdapter = invoiceKey;
                saveDocInfotwo=saveDocInfo;
            }
            //получить номер товарной накладной
            //(сохраненной/открытой)
            receiveInvoice();

            //получить список товаров
            //receiveProductList();

        }catch (Exception ex){
            Toast.makeText(this, "ex: OrdersShipmentActivity / splitBuyersCompanyResult\n"
                    +ex, Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this, "invoiceKey= "+invoiceKey+"\nsaveKey: "
                + saveDocInfo, Toast.LENGTH_SHORT).show();
    }


    private void goInvoiceActivity(int position){
        //если статус документа (не сохренен) то PDF не сохраняем
        //и наоборот если статус документа (сохренен) то PDF сохраняем
        String invoiceName = docsNameList.get(position);
        if(invoiceName.equals("товарная накладная")) {
            Intent intent = new Intent(this, ProductInvoicePDFActivity.class);
            intent.putExtra("invoiceKey", invoiceKey);
            intent.putExtra("invoiceName", invoiceName);
            intent.putExtra("product_list", product_list);
            intent.putExtra("out_warehouse_id", out_warehouse_id);
            intent.putExtra("in_warehouse_id", in_warehouse_id);
            intent.putExtra("car_id", car_id);
            intent.putExtra("myWarehousInfo", myWarehousInfo);
            intent.putExtra("in_WarehousInfo", in_WarehousInfo);
            intent.putExtra("car_info_string", car_info_string);
            intent.putExtra("saveDocInfo", saveDocInfo);
            //myWarehousInfo+"\n"+FOR_SMALL+" "
            //                +in_WarehousInfo+"\n"+TRANSPORTATION_SMALL+" "+car_info_string
            startActivity(intent);
        }else if(invoiceName.equals("транспортная накладная")){
           /* Intent intent = new Intent(this, TransportInvoicePDFActivity.class);
            intent.putExtra("invoiceName", invoiceName);
            intent.putExtra("out_warehouse_id", out_warehouse_id);
            intent.putExtra("in_warehouse_id", in_warehouse_id);
            intent.putExtra("car_id", car_id);
            startActivity(intent);
            */
        }

    }
    //закрыть ключ / накладную
    private void adCloseInvoice( ){
        adb = new AlertDialog.Builder(this);
        String st1 = CLOSE_INVOICE;
        String st2 = MES_19;

        adb.setPositiveButton(CLOSE_INVOICE, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //закрыь ключ / документ
                closeInvoiceKey();

                //обновить/пересоздать активность
                finish();
                startActivity(getIntent());
               // recreate();

                ad.cancel();
            }
        });
        adb.setNeutralButton(RETURN_BIG, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ad.cancel();
            }
        });
        adb.setTitle(st1);
        adb.setMessage(st2);

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
    //сохранить можно только если весь товар передан(с галочками)
    private void adShowDocsNameList( ){
        docsNameList.clear();
        saveFlag = false;

        boolean checkFlag = true;
        for(int i=0;i < product_list.size();i++){
            if(product_list.get(i).getInvoice_key_id() == 0){
                checkFlag = false;
            }
        }

        adb = new AlertDialog.Builder(this);
        String st1 = LIST_OF_AVAILABLE_DOCUMENTS;
       // String st2 = NOT_SAVED_SMALL;
        //String st3 = SAVE_SMALL;

        ListView listView = new ListView(this);
        ArrayAdapter adapter;

        adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1,docsNameList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(saveFlag == true && position == 0){
                    //сохранить документ(save = 1)
                    createInvoiceKeySave();
                    ad.cancel();
                }else{
                    goInvoiceActivity(position);
                    ad.cancel();
                }
            }
        });

        adb.setTitle(st1);
        if(saveDocInfo == 0){
            if(checkFlag){
                docsNameList.add(SAVE_QUESTION);//Сохранить?
                docsNameList.add(PRODUCT_INVOICE_DOC);//товарная накладная
                if(car_id != 0){
                    docsNameList.add(TRANSFER_PRODUCT_INVOICE_DOC);//транспортная накладная
                }
                //adb.setMessage(SAVE_SMALL);
                saveFlag = true;
            }else{
                docsNameList.add(PRODUCT_INVOICE_DOC);//товарная накладная
                if(car_id != 0){
                    docsNameList.add(TRANSFER_PRODUCT_INVOICE_DOC);//транспортная накладная
                }
                adb.setMessage(NOT_SAVED_SMALL);
                saveFlag=false;
            }
        }else{
            docsNameList.add(PRODUCT_INVOICE_DOC);//товарная накладная
            if(car_id != 0){
                docsNameList.add(TRANSFER_PRODUCT_INVOICE_DOC);//транспортная накладная
            }
        }
        //adb.setMessage(st2);
        adb.setView(listView);

        ad=adb.create();
        ad.show();
    }
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

    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent;
        int itemID=item.getItemId();
        if(itemID==R.id.menu){
            intent=new Intent(this, MenuActivity.class);
            startActivity(intent);
        }
        if(itemID==R.id.main){
            intent=new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        if(itemID==R.id.category){
            intent=new Intent(this, ActivityCatalog.class);
            startActivity(intent);
        }
        if(itemID==R.id.invoice_list){
            //adShowDocsNameList();
            Toast.makeText(this, "Раздел находится в разработке. \nВоспользуйтесь /Моя компания/ Заказы/ далее выбрать накладную", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_and_invoice,menu);
        return true;
    }


}