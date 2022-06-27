package ru.tubi.project.activity.company_my;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;

import ru.tubi.project.R;
import ru.tubi.project.activity.invoice.InvoicePDFActivity;
import ru.tubi.project.activity.invoice.InvoiceProviderPDFActivity;
import ru.tubi.project.adapters.OrderBuyerListAdapter;
import ru.tubi.project.adapters.OrderToProviderListAdapter;
import ru.tubi.project.models.InvoiceModel;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.utilites.Constant;
import ru.tubi.project.utilites.InitialData;
import ru.tubi.project.utilites.UserDataRecovery;

import static ru.tubi.project.free.AllText.LIST_OF_AVAILABLE_DOCUMENTS;
import static ru.tubi.project.free.AllText.LOAD_TEXT;
import static ru.tubi.project.free.AllText.ORDERS_TO_PROVDER_TEXT;
import static ru.tubi.project.free.AllText.PRODUCT_INVOICE_DOC;
import static ru.tubi.project.free.AllText.PRODUCT_RECEIPT_DOC;
import static ru.tubi.project.free.AllText.THE_ORDER_TEXT;

public class OrderToProviderListActivity extends AppCompatActivity {

    private int limit = 50, myInvoiceKey_id;
    private int myOrder_partner_id = 0;
    private int myProductInvoice = 0;
    private int myTransferProductInvoice = 0;

    private ArrayList<InvoiceModel> invoice_list = new ArrayList<>();
    private ArrayList<InvoiceModel> invoice_info_list = new ArrayList<>();
    private ArrayList <String> docsNameList = new ArrayList<>();
    private RecyclerView recyclerView;
    private OrderToProviderListAdapter adapter;
    private AlertDialog.Builder adb;
    private AlertDialog ad;
    private UserModel userDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_to_provider_list);
        setTitle(ORDERS_TO_PROVDER_TEXT);//Заказы поставщику

        recyclerView = findViewById(R.id.rvList);

        //получить из sqlLite данные пользователя и компании
        UserDataRecovery userDataRecovery = new UserDataRecovery();
        userDataModel = userDataRecovery.getUserDataRecovery(this);

        //получить список заказов
        ordersList();

        OrderToProviderListAdapter.RecyclerViewClickListener clickListener =
                new OrderToProviderListAdapter.RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        showDocumentsListForPrint(position);
                    }
                };
        adapter = new OrderToProviderListAdapter(this, invoice_list, clickListener);
        recyclerView.setAdapter(adapter);
    }
    //показать список документов которые можно создать и напечатать
    private void showDocumentsListForPrint(int position){
        // docsNameList.clear();
        int order_partner_id = invoice_list.get(position).getOrder_partner_id();
        myInvoiceKey_id = invoice_list.get(position).getInvoice_key_id();

        myOrder_partner_id = order_partner_id;
        //получить номер товарной накладной
        receiveProductInvoiceNumber(order_partner_id);

    }
    //показать документ
    private void goInvoiceActivity(String docName, int docNum, int inv_key){

        Intent intent = new Intent(this, InvoiceProviderPDFActivity.class);
        intent.putExtra("order_partner_id",myOrder_partner_id);
        intent.putExtra("invoice_key_id",inv_key);
        intent.putExtra("docName",docName);
        intent.putExtra("docNum",docNum);
        startActivity(intent);
    }
    //получить номер товарной накладной
    private void receiveProductInvoiceNumber(int order_partner_id){
        invoice_info_list.clear();
        InvoiceModel in_inf= new InvoiceModel("заказ",order_partner_id
                ,0,"заказ № "+order_partner_id);
        invoice_info_list.add(in_inf);

        String url = Constant.PROVIDER_OFFICE;
        url += "receive_invoice_info_list";
        url += "&"+"order_partner_id="+order_partner_id;
        String whatQuestion = "receive_invoice_info_list";
        setInitialData(url, whatQuestion);
        Log.d("A111","OrderToProviderListActivity / receiveProductInvoiceNumber / url="+url);
    }
    //получить список заказов поставщику
    private void ordersList(){
        String url = Constant.PROVIDER_OFFICE;
        url += "receive_list_provider_orders";
        url += "&"+"counterparty_tax_id="+userDataModel.getCompany_tax_id();
        url += "&"+"limit="+limit;
        String whatQuestion = "receive_list_provider_orders";
        setInitialData(url, whatQuestion);

        Log.d("A111","OrderToProviderListActivity / ordersList / url: "+url);
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
                if(whatQuestion.equals("receive_list_provider_orders")){
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
                    int invoice_key_id = Integer.parseInt(temp[2]);

                    String text = document_name+" № "+document_num;

                    InvoiceModel invoice_info = new InvoiceModel(document_name
                            , document_num, invoice_key_id, text);
                    invoice_info_list.add(invoice_info);
                }
            }
            adShowDocsNameList();
        }catch(Exception ex){
            Log.d("A111","OrderToProviderListActivity / splitInvoiceInfoListResult / ex="+ex);
            Toast.makeText(this, "Exception: "+ex, Toast.LENGTH_SHORT).show();
        }
    }
    // разобрать результат с сервера, список продуктов которые собраны для отправки и их колличество
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void splitResult(String result){
        invoice_list.clear();
        //Toast.makeText(this, "res\n"+result, Toast.LENGTH_SHORT).show();
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
                    int order_partner_id = Integer.parseInt(temp[0]);
                    int executed = Integer.parseInt(temp[1]);
                    int out_warehouse_info_id = Integer.parseInt(temp[2]);
                    int out_warehouse_id = Integer.parseInt(temp[3]);
                    String in_companyInfoString_short = temp[4];
                    String in_warehouseInfoString = temp[5];
                    String date_order_start = temp[6];
                    double order_summ = Double.parseDouble(temp[7]);
                    String get_order_date = temp[8];
                    int invoice_key_id = Integer.parseInt(temp[9]);

                    InvoiceModel order_info = new InvoiceModel(order_partner_id, executed
                            , out_warehouse_info_id, out_warehouse_id, in_companyInfoString_short
                            , in_warehouseInfoString, date_order_start, order_summ
                            , get_order_date, invoice_key_id);
                    invoice_list.add(order_info);
                }
            }
        }catch(Exception ex){
            Toast.makeText(this, "Exception: "+ex, Toast.LENGTH_SHORT).show();
        }

        //отсортировать есть доставка или нет и добавить данные доставщика(склад это или транспорт)
        //сортируем лист по 2 полям (logistic_product и car_or_warehouse_id)
        //invoice_list.sort(Comparator.comparing(InvoiceModel::getOut_warehouse_id)
         //       .thenComparing(InvoiceModel::getCharacteristic));

        adapter.notifyDataSetChanged();
    }
    private void adShowDocsNameList( ){
        adb = new AlertDialog.Builder(this);
        ArrayList<InvoiceModel> temp = new ArrayList<>();
        docsNameList.clear();

        for(int i=0;i < invoice_info_list.size();i++){
            docsNameList.add(invoice_info_list.get(i).getText());
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
                String docName = invoice_info_list.get(position).getDocument_name();
                int docNum = invoice_info_list.get(position).getDocument_num();
                int inv_key = invoice_info_list.get(position).getInvoice_key_id();
                goInvoiceActivity(docName, docNum, inv_key);
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