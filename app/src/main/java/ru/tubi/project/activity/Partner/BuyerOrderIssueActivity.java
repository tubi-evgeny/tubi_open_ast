package ru.tubi.project.activity.Partner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ru.tubi.project.R;
import ru.tubi.project.adapters.BuyerOrderIssueAdapter;
import ru.tubi.project.models.CarrierPanelModel;
import ru.tubi.project.models.CounterpartyModel;
import ru.tubi.project.models.OrderModel;
import ru.tubi.project.utilites.InitialData;

import java.util.ArrayList;
import java.util.Comparator;

import ru.tubi.project.utilites.Constant;

import static ru.tubi.project.activity.Config.MY_UID;
import static ru.tubi.project.free.AllCollor.TUBI_GREEN_600;
import static ru.tubi.project.free.AllCollor.TUBI_GREY_200;
import static ru.tubi.project.free.AllText.FOR_ISSUE;
import static ru.tubi.project.free.AllText.FOR_SMALL;
import static ru.tubi.project.free.AllText.ORDER_BUYER;
import static ru.tubi.project.free.AllText.ORDER_COMPLETED;

public class BuyerOrderIssueActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvWarehouseInfo,tvBuyerInfo,tvApply;
    private ImageView ivPdf;
    private RecyclerView rvList;
    private Intent takeit;
    private ArrayList<OrderModel> productList = new ArrayList<>();
    private ArrayList<Integer> checkedList = new ArrayList<>();
    private BuyerOrderIssueAdapter adapter;
    private int providerWarehouse_id,order_id,x=0 , myPosition, myChecked;
    private boolean writeIsOpen = false;
    private CounterpartyModel buyerCompanyMod;
    private CarrierPanelModel partnerWarehousMod;

    public int keyBuyerOrderIssue_Pdf = 23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_order_issue);
        setTitle(ORDER_BUYER);
        getSupportActionBar().setSubtitle(FOR_ISSUE);
        //Заказ покупателя для выдачи

        rvList=findViewById(R.id.rvList);
        tvWarehouseInfo=findViewById(R.id.tvWarehouseInfo);
        tvBuyerInfo=findViewById(R.id.tvBuyerInfo);
        tvApply=findViewById(R.id.tvApply);
        ivPdf=findViewById(R.id.ivPdf);

        tvApply.setOnClickListener(this);
        ivPdf.setOnClickListener(this);

        takeit = getIntent();
        order_id = takeit.getIntExtra("order_id",x);
        providerWarehouse_id = takeit.getIntExtra("warehouse_id",x);
        String myWarehousInfo = takeit.getStringExtra("myWarehousInfo");
        String stBuyersCompany= takeit.getStringExtra("stBuyersCompany");
        buyerCompanyMod= (CounterpartyModel)
                takeit.getSerializableExtra("buyerCompany");
        partnerWarehousMod =(CarrierPanelModel)
                takeit.getSerializableExtra("partnerWarehousInfo");


        tvWarehouseInfo.setText(myWarehousInfo);
        tvBuyerInfo.setText(FOR_SMALL+" "+stBuyersCompany);

        startProductToOrderList();

        BuyerOrderIssueAdapter.OnCheckedChangeListener checked =
                new BuyerOrderIssueAdapter.OnCheckedChangeListener() {
                    @Override
                    public void isChecked(View view, boolean flag, int position) {
                        whatCheckClicked(flag, position);
                        Toast.makeText(BuyerOrderIssueActivity.this, "flag: "+flag+"\nposition: "+position, Toast.LENGTH_SHORT).show();
                    }
                };
        adapter = new BuyerOrderIssueAdapter(this,productList,checked);
        rvList.setAdapter(adapter);
    }
    private void whatCheckClicked(boolean flag, int position){
        myPosition=position;
        if(flag){
            myChecked=1;
        }else myChecked=0;
        //изменить out_active в таблице
        updateCheckedInDB(productList.get(position).getWarehouse_inventory_id(),myChecked);
    }
    @Override
    public void onClick(View v) {
        if(v.equals(tvApply)){
            writeOrderIsCompleted();
            tvApply.setBackgroundColor(TUBI_GREY_200);
            tvApply.setClickable(false);
            adapter.notifyDataSetChanged();
            //Toast.makeText(this, "Apply", Toast.LENGTH_SHORT).show();
        }else if(v.equals(ivPdf)){
            Toast.makeText(this, "Получить документы можно\n" +
                    "Моя компания / Заказы / выбрать заказ", Toast.LENGTH_SHORT).show();
           /* Intent intent=new Intent(this, PdfActivity.class);
            intent.putExtra("key",keyBuyerOrderIssue_Pdf);
            intent.putExtra("order_id",order_id);
            intent.putExtra("partnerWarehousMod",partnerWarehousMod);
            intent.putExtra("buyerCompanyMod",buyerCompanyMod);
            intent.putExtra("productList",productList);
            startActivity(intent);
            Toast.makeText(this, "ivPdf", Toast.LENGTH_SHORT).show();*/
        }
    }
    //проверить лист товаров галочки (товар выдан) и открой кнопку
    private void checkAllCheckedToList(){
        boolean applyFlag=true;
        for(int i=0;i < productList.size();i++){
            if(productList.get(i).getChecked() == 0 ){//|| productList.get(i).getChecked() == 999
                applyFlag = false;
            }
        }
        if(applyFlag){
            tvApply.setBackgroundColor(TUBI_GREEN_600);
            tvApply.setClickable(true);
            writeIsOpen =true;
        }else {
            tvApply.setBackgroundColor(TUBI_GREY_200);
            tvApply.setClickable(false);
            writeIsOpen = false;
        }
    }
    //сделать запись, заказ выполнен
    private void writeOrderIsCompleted(){
        String url = Constant.PARTNER_OFFICE;
        url += "write_order_is_completed";
        url += "&"+"order_id="+order_id;
        url += "&"+"user_uid="+MY_UID;
        String whatQuestion = "write_order_is_completed";
        setInitialData(url, whatQuestion);
    }
    //изменить out_active в таблице
    private void updateCheckedInDB(int warehouse_inventory_id, int checked){
        String url = Constant.PARTNER_OFFICE;
        url += "update_out_active_in_table";
        url += "&"+"warehouse_inventory_id="+warehouse_inventory_id;
        url += "&"+"user_uid="+MY_UID;
        url += "&"+"checked="+checked;
        String whatQuestion = "update_out_active_in_table";
        setInitialData(url, whatQuestion);
    }
    //получить список продуктов для выдачи этому покупателю
    private void startProductToOrderList(){
        String url = Constant.PARTNER_OFFICE;
        url += "receive_list_order_product_for_issue";
        url += "&"+"order_id="+order_id;
        url += "&"+"providerWarehouse_id="+providerWarehouse_id;
        String whatQuestion = "receive_list_order_product_for_issue";
        setInitialData(url, whatQuestion);
    }
    private void setInitialData(String url_get, String whatQuestion) {
        InitialData task=new InitialData(){

            @RequiresApi(api = Build.VERSION_CODES.N)
            protected void onPostExecute(String result) {
                if(whatQuestion.equals("receive_list_order_product_for_issue")){
                    splitResult(result);
                    // Toast.makeText(BuyerOrderIssueActivity.this,
                     //   "res: "+result, Toast.LENGTH_SHORT).show();
                }else if(whatQuestion.equals("update_out_active_in_table")){
                    splitCheckedResult(result);
                }else if(whatQuestion.equals("write_order_is_completed")){
                    splitCloseOrderResult(result);
                }//
                //hide the dialog
                // asyncDialog.dismiss();
            }
        };
        task.execute(url_get);
    }
    //разобрать ответ, заказ закрыт или нет
    private void splitCloseOrderResult(String result){
        Log.d("A111","BuyerOrderIssueActivity / splitCloseOrderResult / res= "+result);
        try {
            String[] res = result.split("<br>");
            String[] one_temp = res[0].split("&nbsp");
            if (one_temp[0].equals("RESULT_OK") ) {
                Toast.makeText(this, ""+ORDER_COMPLETED, Toast.LENGTH_LONG).show();
                tvApply.setBackgroundColor(TUBI_GREY_200);
                tvApply.setClickable(false);
               // onBackPressed();
            }else if (one_temp[0].equals("error") || one_temp[0].equals("messege")) {
                Toast.makeText(this, "" + one_temp[1], Toast.LENGTH_LONG).show();

                tvApply.setBackgroundColor(TUBI_GREEN_600);
                tvApply.setClickable(true);
                adapter.notifyDataSetChanged();
                //return;
            }

        }catch (Exception ex){

        }
    }
    //разобрать ответ от сервера запись удалась или нет
    private void splitCheckedResult(String result){
        try {
            String[] res = result.split("<br>");
            String[] one_temp = res[0].split("&nbsp");
            if (one_temp[0].equals("RESULT_OK") ) {
                productList.get(myPosition).setChecked(myChecked);
            }else if (one_temp[0].equals("error") || one_temp[0].equals("messege")) {
                    Toast.makeText(this, "" + one_temp[1], Toast.LENGTH_LONG).show();
                    adapter.notifyDataSetChanged();
                    //return;
            }

        }catch (Exception ex){

        }
       // adapter.notifyItemChanged(myPosition);
        checkAllCheckedToList();
    }
    // разобрать результат с сервера, список продуктов для выдачи покупателю и их колличество
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void splitResult(String result){
        productList.clear();
        checkedList.clear();
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
                    int product_id = Integer.parseInt(temp[0]);
                    int productInventory_id = Integer.parseInt(temp[1]);
                    String category = temp[2];
                    String brand = temp[3];
                    String characteristic = temp[4];
                    String type_packaging = temp[5];
                    String unit_measure = temp[6];
                    int weight_volume = Integer.parseInt(temp[7]);
                    int quantity_package = Integer.parseInt(temp[8]);
                    String image_url = temp[9];

                    int order_product_id = Integer.parseInt(temp[10]);
                    double quantity_to_order = Double.parseDouble(temp[11]);
                    int warehouse_inventory_id = Integer.parseInt(temp[12]);
                    int collected = Integer.parseInt(temp[13]);
                    int checked = Integer.parseInt(temp[14]);
                    double price = Double.parseDouble(temp[15]);
                    String description = temp[16];

                    OrderModel delivery = new OrderModel(
                            product_id,productInventory_id, category,brand,characteristic,
                            type_packaging, unit_measure,weight_volume,quantity_package,
                            image_url,order_product_id,quantity_to_order,
                            warehouse_inventory_id, collected, checked, price, description);

                    productList.add(delivery);

                }
            }
        }catch(Exception ex){
            Toast.makeText(this, "Exception: "+ex, Toast.LENGTH_SHORT).show();
        }
        //отсортировать есть доставка или нет и добавить данные доставщика(склад это или транспорт)
        //сортируем лист по 2 полям (logistic_product и car_or_warehouse_id)
        productList.sort(Comparator.comparing(OrderModel::getCategory)
                .thenComparing(OrderModel::getCharacteristic));


        for(int i=0;i < productList.size();i++){
            checkedList.add(productList.get(i).getChecked());
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if(writeIsOpen){
            writeOrderIsCompleted();
        }
        super.onBackPressed();
    }
}