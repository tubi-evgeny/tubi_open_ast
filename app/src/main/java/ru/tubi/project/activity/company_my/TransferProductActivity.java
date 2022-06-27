package ru.tubi.project.activity.company_my;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import ru.tubi.project.R;

import ru.tubi.project.adapters.TransferProductAdapter;
import ru.tubi.project.activity.invoice.ProductInvoicePDFActivity;
import ru.tubi.project.models.OrderModel;
import ru.tubi.project.models.TransferModel;
import ru.tubi.project.models.WarehouseModel;
import ru.tubi.project.utilites.InitialData;

import java.util.ArrayList;
import java.util.Comparator;

import ru.tubi.project.utilites.Constant;

import static ru.tubi.project.free.AllText.C;
import static ru.tubi.project.free.AllText.LIST_OF_AVAILABLE_DOCUMENTS;
import static ru.tubi.project.free.AllText.LOAD_TEXT;
import static ru.tubi.project.free.AllText.PRODUCT_INVOICE_DOC;
import static ru.tubi.project.free.AllText.ST;
import static ru.tubi.project.free.AllText.TRANSFER_PRODUCT;
import static ru.tubi.project.free.AllText.TRANSFER_PRODUCT_INVOICE_DOC;
import static ru.tubi.project.free.AllText.WAREHOUSE;

public class TransferProductActivity extends AppCompatActivity {

    private Intent takeit;
    private TextView tvMyWarehouseInfo;
    private int out_warehouse_id, in_warehouse_id, car_id, x;
    private OrderModel orderInfo;
    private RecyclerView recyclerView;
    private ArrayList<TransferModel> transfer_product_list = new ArrayList<>();
    private ArrayList<TransferModel> product_list = new ArrayList<>();
    private ArrayList<WarehouseModel> warehouse_info_list = new ArrayList<>();
    private ArrayList <String> docsNameList = new ArrayList<>();
    private ArrayList <Integer> inventory_id_list = new ArrayList<>();
    private AlertDialog.Builder adb;
    private AlertDialog ad;
    private TransferProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_product);
        setTitle(TRANSFER_PRODUCT);//Передать товар

        recyclerView=(RecyclerView)findViewById(R.id.rvList);
        tvMyWarehouseInfo=findViewById(R.id.tvMyWarehouseInfo);

        takeit = getIntent();
        orderInfo = (OrderModel)takeit.getSerializableExtra("my_warehouse");
        String warehouse = WAREHOUSE+" № "+orderInfo.getWarehouse_info_id()+"/"+
                orderInfo.getWarehouse_id()+" "+orderInfo.getCity()+
                " "+ST+" "+orderInfo.getStreet()+" "+orderInfo.getHouse();
        if(!orderInfo.getBuilding().isEmpty()){
            warehouse += " "+C+". "+orderInfo.getBuilding();
        }
        tvMyWarehouseInfo.setText(""+warehouse);

        startList();

        //Toast.makeText(this, "warehouse: "+orderInfo.getWarehouse_id(), Toast.LENGTH_SHORT).show();
        TransferProductAdapter.OnCheckedChangeListener checkedChangeListener =
                new TransferProductAdapter.OnCheckedChangeListener() {
                    @Override
                    public void isChecked(View view, boolean flag, int position) {
                        whatClicked(view,flag, position);
                       // whatCheckClicked(flag, position);
                    }
                };
        adapter=new TransferProductAdapter(this,transfer_product_list,
                warehouse_info_list,checkedChangeListener);
        recyclerView.setAdapter(adapter);
    }
    private void whatClicked(View view, boolean flag, int position){
        String string=String.valueOf(view);
        String str[]=string.split("/");
        if(str[1].equals("checkBox}")) {
            whatCheckClicked(flag, position);
        }else if(str[1].equals("llInvoiceNum}")) {
            getInvoiceListForCreate(position);
            //Toast.makeText(this, "view\n"+view, Toast.LENGTH_SHORT).show();

        }
    }
    //получить список доступных к созданию документов
    private void getInvoiceListForCreate(int position){
        docsNameList.clear();
        car_id = transfer_product_list.get(position).getCar_id();

        //makeProductListForPDF(position);

        docsNameList.add(PRODUCT_INVOICE_DOC);
        if(car_id != 0){
            docsNameList.add(TRANSFER_PRODUCT_INVOICE_DOC);
        }
        adShowDocsNameList();
    }
    private void makeProductListForPDF(int position){
        product_list.clear();
        inventory_id_list.clear();
        String  inventory_id_string = "";
        int car_id = transfer_product_list.get(position).getCar_id();
        int in_warehouse_id = transfer_product_list.get(position).getIn_warehouse_id();
        int invoice_key_id = transfer_product_list.get(position).getInvoice_key_id();
        for(int i=position;i < transfer_product_list.size();i++){
            if(car_id == transfer_product_list.get(position).getCar_id()
                    && in_warehouse_id == transfer_product_list.get(position).getIn_warehouse_id()){
                product_list.add(transfer_product_list.get(position));
                inventory_id_string += transfer_product_list.get(position).getProductInventory_id()+";";
                //inventory_id_list.add(transfer_product_list.get(position).getProductInventory_id());
            }
        }
        //получить описание товара
        receiveProductInfo(inventory_id_string, invoice_key_id);
    }

    private void whatCheckClicked( boolean flag, int position){
        //добавить ключ(документы) и установить или отменить галочку (передан товар)
        int warehouseInventory_id = transfer_product_list.get(position).getWarehouseInventory_id();
        int checked = 0;
        if(flag)checked=1;
        String url = Constant.PROVIDER_OFFICE;
        url += "chenge_checked_provider";
        url += "&"+"warehouseInventory_id="+warehouseInventory_id;
        url += "&"+"checked="+checked;
        String whatQuestion = "chenge_checked_provider";
        setInitialData(url, whatQuestion);
    }
    //получить описание товара
    private void receiveProductInfo(String inventory_id_string, int invoice_key_id){
        String url = Constant.PROVIDER_OFFICE;
        url += "receive_product_info";
        url += "&"+"inventory_id_string="+inventory_id_string;
        url += "&"+"invoice_key_id="+invoice_key_id;
        String whatQuestion = "receive_product_info";
        setInitialData(url, whatQuestion);
    }
    //получить информацию по (склад назначения)
    private void receiveWarehouseInfo(){
        warehouse_info_list.clear();
        for(int i=0;i < transfer_product_list.size();i++){
            int in_warehouse_id = transfer_product_list.get(i).getIn_warehouse_id();
            String url = Constant.PROVIDER_OFFICE;
            url += "receive_warehouse_info";
            url += "&"+"in_warehouse_id="+in_warehouse_id;
            String whatQuestion = "receive_warehouse_info";
            setInitialData(url, whatQuestion);
        }
    }
    //получить список получателей(перевозчиков) и все собранные товары для выдачи получателям
    private void startList() {
        String url = Constant.PROVIDER_OFFICE;
        url += "receive_list_recipient_and_product_for_them";
        url += "&"+"warehouse_id="+orderInfo.getWarehouse_id();
        String whatQuestion = "receive_list_recipient_and_product_for_them";
        setInitialData(url, whatQuestion);
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

            @RequiresApi(api = Build.VERSION_CODES.N)
            protected void onPostExecute(String result) {
                //Do your thing
                if(whatQuestion.equals("receive_list_recipient_and_product_for_them")){
                    splitResult(result);
                }else if(whatQuestion.equals("receive_warehouse_info")){
                    splitResultWarehouse(result);
                }
                else if(whatQuestion.equals("receive_product_info")){
                    splitProductInfoList(result);
                }
                 asyncDialog.dismiss();
            }
        };
        task.execute(url_get);
    }
    //разобрать информацию о товарах
    private void splitProductInfoList(String result){
        try{
            String[] res = result.split("<br>");
            String[] one_temp = res[0].split("&nbsp");
            if (one_temp[0].equals("error") || one_temp[0].equals("messege")) {
                Toast.makeText(this, "" + one_temp[1], Toast.LENGTH_LONG).show();
                return;
            } else {
                for (int i = 0; i < res.length; i++) {
                    String docsName = res[i];
                    docsNameList.add(docsName);
                }
                adShowDocsNameList();
            }
        }catch(Exception ex){
            Toast.makeText(this, "Exception: TransferProductActivity -> splitInvoiceList\n"+ex, Toast.LENGTH_SHORT).show();
        }
    }
    //разобрать какие документы можно создать
   /* private void splitInvoiceList(String result){
        docsNameList.clear();
        try{
            String[] res = result.split("<br>");
            String[] one_temp = res[0].split("&nbsp");
            if (one_temp[0].equals("error") || one_temp[0].equals("messege")) {
                Toast.makeText(this, "" + one_temp[1], Toast.LENGTH_LONG).show();
                return;
            } else {
                for (int i = 0; i < res.length; i++) {
                    String docsName = res[i];
                   docsNameList.add(docsName);
                }
                adShowDocsNameList();
            }
        }catch(Exception ex){
            Toast.makeText(this, "Exception: TransferProductActivity -> splitInvoiceList\n"+ex, Toast.LENGTH_SHORT).show();
        }
    }*/
    //разобрать результат данные о складе
    private void splitResultWarehouse(String result){
        //deliveryList.clear();
        try{
            String[] res = result.split("<br>");
            String[] one_temp = res[0].split("&nbsp");
            if (one_temp[0].equals("error") || one_temp[0].equals("messege")) {
                Toast.makeText(this, "" + one_temp[1], Toast.LENGTH_LONG).show();
                return;
            } else {
                for (int i = 0; i < res.length; i++) {
                    String[] temp = res[i].split("&nbsp");
                    int warehouse_id=Integer.parseInt(temp[0]);
                    String city=temp[1];
                    String street=temp[2];
                    int house=Integer.parseInt(temp[3]);
                    String building="";
                    try { building=temp[4]; }catch (Exception ex){     }
                    int warehouse_info_id = Integer.parseInt(temp[5]);

                    WarehouseModel warehouse = new WarehouseModel(warehouse_id,
                             city, street, house, building,warehouse_info_id);

                    warehouse_info_list.add(warehouse);
                }
            }
        }catch(Exception ex){
            Toast.makeText(this, "Exception: "+ex, Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();
        // Toast.makeText(this, "count: "+deliveryList.size(), Toast.LENGTH_SHORT).show();
        //sortStartList();
    }
    // разобрать результат с сервера, список получателей(транспорт)
    // и продуктов которые собраны для выдачи им и их колличество
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void splitResult(String result){
       // Toast.makeText(this, "res:\n"+result, Toast.LENGTH_SHORT).show();
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
                        int logistic_product_check = Integer.parseInt(temp[3]);
                        int $car_for_logistic_check = Integer.parseInt(temp[4]);
                        int out_active_check = Integer.parseInt(temp[5]);
                        int in_warehouse_id = Integer.parseInt(temp[6]);
                        int car_id = Integer.parseInt(temp[7]);
                        int invoice_key_id = Integer.parseInt(temp[8]);
                        String description_docs = temp[9];
                        String car_info = temp[10];
                        double price = Double.parseDouble(temp[11]);

                        TransferModel transfer_product = new TransferModel(productInventory_id
                                ,warehouseInventory_id, quantity, logistic_product_check
                                ,$car_for_logistic_check, out_active_check, in_warehouse_id
                                ,car_id, invoice_key_id, description_docs, car_info, price);

                        transfer_product_list.add(transfer_product);
                    }
                }
            }
        }catch(Exception ex){
             Toast.makeText(this, "Exception: "+ex, Toast.LENGTH_SHORT).show();
        }

        //сортируем лист по 2 полям (partner_warehouse_id и Product_inventory_id)
        transfer_product_list.sort(Comparator.comparing(TransferModel::getCar_id)
                .thenComparing(TransferModel::getIn_warehouse_id));

        receiveWarehouseInfo();
    }
    private void goInvoiceActivity(int position){
        String invoiceName = docsNameList.get(position);
        if(invoiceName.equals("товарная накладная")) {
            Intent intent = new Intent(this, ProductInvoicePDFActivity.class);
            intent.putExtra("invoiceName", invoiceName);
            intent.putExtra("product_list", product_list);
            intent.putExtra("out_warehouse_id", out_warehouse_id);
            intent.putExtra("in_warehouse_id", in_warehouse_id);
            intent.putExtra("car_id", car_id);
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

    private void adShowDocsNameList( ){
        adb = new AlertDialog.Builder(this);
        String st1 = LIST_OF_AVAILABLE_DOCUMENTS;

        ListView listView = new ListView(this);
        ArrayAdapter adapter;

        adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1,docsNameList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goInvoiceActivity(position);
                //goInvoiceActivity(docsNameList.get(position));
            }
        });

        adb.setTitle(st1);
        adb.setView(listView);

        ad=adb.create();
        ad.show();
    }

}