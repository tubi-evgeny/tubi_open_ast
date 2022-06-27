package ru.tubi.project.activity.Partner;

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
import android.widget.Toast;

import java.util.ArrayList;

import ru.tubi.project.R;
import ru.tubi.project.activity.invoice.InvoicePDFActivity;
import ru.tubi.project.adapters.OrderBuyerListAdapter;
import ru.tubi.project.models.InvoiceModel;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.utilites.Constant;
import ru.tubi.project.utilites.InitialData;
import ru.tubi.project.utilites.UserDataRecovery;

import static ru.tubi.project.free.AllText.LIST_OF_AVAILABLE_DOCUMENTS;
import static ru.tubi.project.free.AllText.LOAD_TEXT;
import static ru.tubi.project.free.AllText.ORDERS_TEXT;
import static ru.tubi.project.free.AllText.ORDER_TEXT;
import static ru.tubi.project.free.AllText.PRODUCTS_RECEIPT_TEXT;
import static ru.tubi.project.free.AllText.PRODUCT_INVOICE_DOC;
import static ru.tubi.project.free.AllText.PRODUCT_RECEIPT_DOC;
import static ru.tubi.project.free.AllText.THE_ORDER_TEXT;
import static ru.tubi.project.free.AllText.TRANSFER_PRODUCT_INVOICE_DOC;

public class OrderBuyerListActivity extends AppCompatActivity {

    private int limit = 50, myInvoiceKey_id;
    private int myOrder_id = 0;
    private int myProductInvoice = 0;
    private int myTransferProductInvoice = 0;

    private ArrayList<InvoiceModel> invoice_list = new ArrayList<>();
    private ArrayList<InvoiceModel> invoice_info_list = new ArrayList<>();
    private ArrayList <String> docsNameList = new ArrayList<>();
    private RecyclerView recyclerView;
    private OrderBuyerListAdapter adapter;
    private AlertDialog.Builder adb;
    private AlertDialog ad;
    private UserModel userDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_buyer_list);
        setTitle(ORDERS_TEXT);//Заказы

        recyclerView = findViewById(R.id.rvList);

        //получить из sqlLite данные пользователя и компании
        UserDataRecovery userDataRecovery = new UserDataRecovery();
        userDataModel = userDataRecovery.getUserDataRecovery(this);

        //получить список заказов
        ordersList();

        OrderBuyerListAdapter.RecyclerViewClickListener clickListener =
                new OrderBuyerListAdapter.RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        showDocumentsListForPrint(position);
                        //Toast.makeText(OrderBuyerListActivity.this, "hi "+position, Toast.LENGTH_SHORT).show();
                    }
                };

        adapter = new OrderBuyerListAdapter(this, invoice_list, clickListener);
        recyclerView.setAdapter(adapter);
    }
    //показать список документов которые можно создать и напечатать
    private void showDocumentsListForPrint(int position){
       // docsNameList.clear();
        int order_id = invoice_list.get(position).getOrder_id();
        myInvoiceKey_id = invoice_list.get(position).getInvoice_key_id();

        myOrder_id = order_id;
        //получить номер товарной накладной
        receiveProductInvoiceNumber(order_id);

        //docsNameList.add(THE_ORDER_TEXT+" № "+myOrder_id);
        //docsNameList.add(PRODUCT_INVOICE_DOC);
        //adShowDocsNameList();
    }
    //создать документ или показать
    private void goInvoiceActivity(String docName){
        int docNum=0;

        for(int i=0;i < invoice_info_list.size();i++){
            if(invoice_info_list.get(i).getDocument_name().equals(docName)){
                docNum = invoice_info_list.get(i).getDocument_num();
                break;
            }
        }

        Intent intent = new Intent(this, InvoicePDFActivity.class);
        intent.putExtra("order_id",myOrder_id);
        intent.putExtra("invoice_key_id",myInvoiceKey_id);
        intent.putExtra("docName",docName);
        intent.putExtra("docNum",docNum);
        startActivity(intent);
    }
    //получить номер товарной накладной
    private void receiveProductInvoiceNumber(int order_id){
        invoice_info_list.clear();
        InvoiceModel in_inf= new InvoiceModel("заказ",order_id);
        invoice_info_list.add(in_inf);

        String url = Constant.PARTNER_OFFICE;
        url += "receive_invoice_info_list";
        url += "&"+"order_id="+order_id;
        String whatQuestion = "receive_invoice_info_list";
        setInitialData(url, whatQuestion);
    }
    //получить список заказов
    private void ordersList(){
        String url = Constant.PARTNER_OFFICE;
        url += "receive_list_orders";
        url += "&"+"counterparty_tax_id="+userDataModel.getCompany_tax_id();
        url += "&"+"limit="+limit;
        String whatQuestion = "receive_list_orders";
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
                if(whatQuestion.equals("receive_list_orders")){
                    splitResult(result);
                }else if(whatQuestion.equals("receive_invoice_info_list")){
                    splitInvoiceInfoListResult(result);
                }
                //hide the dialog
                 asyncDialog.dismiss();
            }
        };
        task.execute(url_get);
    }
    //получить номер товарной накладной
    private void splitInvoiceInfoListResult(String result){

        //Toast.makeText(this, "res\n"+result, Toast.LENGTH_SHORT).show();
        try{
            String[] res = result.split("<br>");
            String[] one_temp = res[0].split("&nbsp");
            if (one_temp[0].equals("error") || one_temp[0].equals("messege")) {
                Toast.makeText(this, "" + one_temp[1], Toast.LENGTH_LONG).show();
                //return;
            } else {
                for (int i = 0; i < res.length; i++) {
                    String[] temp = res[i].split("&nbsp");
                    String document_name = temp[0];
                    int document_num = Integer.parseInt(temp[1]);

                    InvoiceModel invoice_info = new InvoiceModel(document_name, document_num);
                    invoice_info_list.add(invoice_info);
                }
            }
            adShowDocsNameList();
        }catch(Exception ex){
            Toast.makeText(this, "Exception: "+ex, Toast.LENGTH_SHORT).show();
        }
    }
    // разобрать результат с сервера, список продуктов которые собраны для отправки и их колличество
    private void splitResult(String result){
        invoice_list.clear();
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
                    int order_id = Integer.parseInt(temp[0]);
                    int executed = Integer.parseInt(temp[1]);
                    String buyer_companyInfoString_short = temp[2];
                    long get_order_date_millis = Long.parseLong(temp[3]);
                    String date_order_start = temp[4];
                    double order_summ = Double.parseDouble(temp[5]);
                    String get_order_date = temp[6];
                    int invoice_key_id = Integer.parseInt(temp[7]);

                    InvoiceModel order_info = new InvoiceModel(order_id, executed
                            , buyer_companyInfoString_short, get_order_date_millis
                            , date_order_start, order_summ, get_order_date, invoice_key_id);
                    invoice_list.add(order_info);
                }
            }
        }catch(Exception ex){
            Toast.makeText(this, "Exception: "+ex, Toast.LENGTH_SHORT).show();
        }

        //отсортировать есть доставка или нет и добавить данные доставщика(склад это или транспорт)
        //сортируем лист по 2 полям (logistic_product и car_or_warehouse_id)
        //productList.sort(Comparator.comparing(AcceptProductListProvidersModel::getCategory)
        //        .thenComparing(AcceptProductListProvidersModel::getCharacteristic));

        adapter.notifyDataSetChanged();
    }
    private void adShowDocsNameList( ){
        adb = new AlertDialog.Builder(this);
        docsNameList.clear();

        docsNameList.add(THE_ORDER_TEXT+" № "+invoice_info_list.get(0).getDocument_num());
        docsNameList.add(PRODUCT_INVOICE_DOC+" № -");
        docsNameList.add(PRODUCT_RECEIPT_DOC+" № -");

        for(int i=0;i < invoice_info_list.size();i++){
            if(invoice_info_list.get(i).getDocument_name().equals("товарная накладная")){
                docsNameList.set(1,PRODUCT_INVOICE_DOC+" № "+invoice_info_list.get(i).getDocument_num());
            }
            else if(invoice_info_list.get(i).getDocument_name().equals("товарный чек")){
                docsNameList.set(2,PRODUCT_RECEIPT_DOC+" № "+invoice_info_list.get(i).getDocument_num());
            }
        }

        String st1 = LIST_OF_AVAILABLE_DOCUMENTS;

        ListView listView = new ListView(this);
        ArrayAdapter adapter;

        adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1,docsNameList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String docName = "";
                if(position == 0){
                    docName  = "заказ";
                }else if(position == 1){
                    docName = "товарная накладная";
                }else if(position == 2){
                    docName = "товарный чек";
                }
                goInvoiceActivity(docName);
                ad.cancel();
                //Toast.makeText(OrderBuyerListActivity.this, "allert "+position, Toast.LENGTH_SHORT).show();
            }
        });

        adb.setTitle(st1);
        adb.setView(listView);

        ad=adb.create();
        ad.show();
    }
}