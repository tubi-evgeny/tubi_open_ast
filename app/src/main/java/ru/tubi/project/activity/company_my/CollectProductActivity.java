package ru.tubi.project.activity.company_my;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import ru.tubi.project.R;
import ru.tubi.project.adapters.CollectProductAdapter;
import ru.tubi.project.models.OrderModel;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.utilites.InitialData;

import java.util.ArrayList;

import ru.tubi.project.utilites.Constant;
import ru.tubi.project.utilites.UserDataRecovery;

import static ru.tubi.project.activity.Config.MY_UID;
import static ru.tubi.project.free.AllCollor.TUBI_GREEN_600;
import static ru.tubi.project.free.AllCollor.TUBI_GREY_200;
import static ru.tubi.project.free.AllText.BUILDING;
import static ru.tubi.project.free.AllText.COLLECT_PRODUCT;
import static ru.tubi.project.free.AllText.FOR_SMALL;
import static ru.tubi.project.free.AllText.ST;
import static ru.tubi.project.free.AllText.WAREHOUSE;

public class CollectProductActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private TextView tvProviderWarehouseInfo,tvPartnerWarehouseInfo,tvApply;
    private ArrayList<OrderModel> listProduct=new ArrayList<>();
    private ArrayList<OrderModel> list=new ArrayList<>();
    private ArrayList<OrderModel> listIntentProduct = new ArrayList<>();
    private ArrayList<OrderModel> checkedList = new ArrayList<>();
    private CollectProductAdapter adapter;
   // private ProviderListBuyersOrdersAdapter.OnCheckedChangeListener checked ;
    private Intent takeit;
    private OrderModel my_warehouse_info;
    private int myWarehouseId, partnerWarehouseId,x;
    private AlertDialog.Builder adb;
    private AlertDialog ad;
    private Switch switchOnOffDelivery;
    private boolean deliveryFlag = true;
    private UserModel userDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect_product);
        setTitle(""+COLLECT_PRODUCT);//Сборка товара

        recyclerView=(RecyclerView)findViewById(R.id.rvList);
        tvProviderWarehouseInfo=findViewById(R.id.tvProviderWarehouseInfo);
        tvPartnerWarehouseInfo=findViewById(R.id.tvPartnerWarehouseInfo);
        tvApply = findViewById(R.id.tvApply);
        switchOnOffDelivery=findViewById(R.id.switchOnOffDelivery);

        //получить из sqlLite данные пользователя и компании
        UserDataRecovery userDataRecovery = new UserDataRecovery();
        userDataModel = userDataRecovery.getUserDataRecovery(this);

        tvApply.setOnClickListener(this);

        tvApply.setClickable(false);

        takeit = getIntent();
        String my_warehouse = takeit.getStringExtra ("my_warehouse");
        listIntentProduct = (ArrayList<OrderModel>)takeit.getSerializableExtra("listProduct");
        myWarehouseId = listIntentProduct.get(0).getWarehouse_id();//my_warehouse_info.getWarehouse_id();
        partnerWarehouseId = listIntentProduct.get(0).getPartner_warehouse_id();// my_warehouse_info.getPartner_warehouse_id();

        String partnerWarehouse = FOR_SMALL +" "+WAREHOUSE+" № "
                +listIntentProduct.get(0).getWarehouse_info_id()+"/"
                +listIntentProduct.get(0).getPartner_warehouse_id()+" "
                +listIntentProduct.get(0).getCity()+" "+ST+" "+listIntentProduct.get(0).getStreet()+" "
                +listIntentProduct.get(0).getHouse();
        if(!listIntentProduct.get(0).getBuilding().isEmpty()){
            partnerWarehouse += " "+BUILDING+" "+listIntentProduct.get(0).getBuilding();
        }
        tvProviderWarehouseInfo.setText(""+my_warehouse);
        tvPartnerWarehouseInfo.setText(""+partnerWarehouse);

        startList();

        switchOnOffDelivery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    switchOnOffDelivery.setText(R.string.delivery_is_needed);
                    deliveryFlag=true;
                }else{
                    switchOnOffDelivery.setText(R.string.without_delivery);
                    deliveryFlag=false;
                }
                editLogisticProduct();
            }
        });
        CollectProductAdapter.RecyclerViewClickListener clickListener =
                new CollectProductAdapter.RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                    }
                };
        CollectProductAdapter.OnCheckedChangeListener checkedChangeListener =
                new CollectProductAdapter.OnCheckedChangeListener() {
                    @Override
                    public void isChecked(View view, boolean flag, int position) {
                        whatCheckedClicked(flag, position);
                       // Toast.makeText(CollectProductActivity.this,
                              //  "checked: "+view +" \nposition: "+position+"\nflag: "+flag, Toast.LENGTH_SHORT).show();
                    }
                };

        adapter=new CollectProductAdapter(this,listProduct, clickListener,checkedChangeListener);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onClick(View v) {
        if (v.equals(tvApply)) {
            writeCheckToTable();
           // Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show();
        }
    }
    //записать товары в таблицу t_warehouse_inventory_in_out и в listProduct
    private void writeCheckToTable() {

        for (int i = 0; i < listProduct.size(); i++) {
          if(checkedList.get(i).getChecked() == 1 && listProduct.get(i).getChecked() == 0){
              //добавить собранный товар в warehouse_inventory_in_out
              addProductForMoving(i);
              listProduct.get(i).setChecked(1);
              int product_inventory_id = listProduct.get(i).getProduct_inventory_id();
              double quantity_for_collect = listProduct.get(i).getQuantity();
              for(int j=0;j < listIntentProduct.size();j++){
                  if(product_inventory_id == listIntentProduct.get(j).getProduct_inventory_id()){

                      double quantityToCollect = listIntentProduct.get(j).getQuantityToCollect();
                      //сложить колличество в заказх с колличеством которое добавляем в заказ
                      listIntentProduct.get(j).setQuantityToCollect(quantity_for_collect+quantityToCollect);
                  }
              }
          }
        }
        tvApply.setBackgroundColor(TUBI_GREY_200);
        tvApply.setClickable(false);
        checkedList.clear();
        startList();
       // writeCheckToListProduct();
       // adapter.notifyDataSetChanged();
    }
    //записать клики по товару в лист
    private void whatCheckedClicked(boolean flag, int position){
        tvApply.setBackgroundColor(TUBI_GREEN_600);
        tvApply.setClickable(true);
        //пишем список нажатий на check в лист будем использовать при записи в таблицу
        int checked=0;
        if(flag)checked=1;
        checkedList.get(position).setChecked(checked);
    }
    //редакторовать доставку в warehouse_inventory
    private void editLogisticProduct(){
        //переписать доставку у всех товаров из списка
        for(int i=0; i < list.size();i++){
            int warehouse_inventory_id = list.get(i).getWarehouse_inventory_id();
            int logistic_product=0;
            if(deliveryFlag){
                logistic_product=1;
            }
           // Toast.makeText(this, "id: "+warehouse_inventory_id+"\ndeliveryFlag: "+deliveryFlag, Toast.LENGTH_SHORT).show();

            String url = Constant.PROVIDER_OFFICE;
            url += "chenge_logistic_product_info";
            url += "&"+"warehouse_inventory_id="+warehouse_inventory_id;
            url += "&"+"logistic_product="+logistic_product;
            url += "&"+"user_uid="+MY_UID;

            String whatQuestion = "chenge_logistic_product_info";
            setInitialData(url, whatQuestion);
        }
        //обновить список
        startList();
    }

    //сделать запись в (warehouse_inventory_in_out) о том что товар собран
    private void addProductForMoving(int position){
        double quantity = listProduct.get(position).getQuantity();
        int product_inventory_id = listProduct.get(position).getProduct_inventory_id();
        int myWarehouse_id = listProduct.get(position).getWarehouse_id();
        int partnerWarehouse_id = listProduct.get(position).getPartner_warehouse_id();
        String transaction_name = "sale";//на реализацию //"moving";//перемещение
        int collected = 1;//1=yes
        String user_uid = userDataModel.getUid();//MY_UID;
        int logistic_product=0;
        if(deliveryFlag){
            logistic_product=1;
        }

        String url = Constant.PROVIDER_OFFICE;
        url += "write_collect_product";
        url += "&"+"my_warehouse_id="+myWarehouse_id;
        url += "&"+"partner_warehouse_id="+partnerWarehouse_id;
        url += "&"+"product_inventory_id="+product_inventory_id;
        url += "&"+"quantity="+quantity;
        url += "&"+"transaction_name="+transaction_name;
        url += "&"+"collected="+collected;
        url += "&"+"user_uid="+user_uid;
        url += "&"+"logistic_product="+logistic_product;
        String whatQuestion = "write_collect_product";
        setInitialData(url, whatQuestion);
    }

    //получить все собранные warhouse_inventory_id но не отправленные и показать их
    private void startList() {
        String url = Constant.PROVIDER_OFFICE;
        url += "receive_list_collect_product";
        url += "&"+"my_warehouse_id="+myWarehouseId;
        url += "&"+"partner_warehouse_id="+partnerWarehouseId;
        String whatQuestion = "receive_list_collect_product";
        setInitialData(url, whatQuestion);
    }
    private void setInitialData(String url_get, String whatQuestion) {
        // ProgressDialog asyncDialog = new ProgressDialog(this);

        InitialData task=new InitialData(){
          /*  @Override
            protected void onPreExecute() {
                //set message of the dialog
                asyncDialog.setMessage(LOAD_TEXT);
                //show dialog
                asyncDialog.show();
                super.onPreExecute();
            }*/

            @RequiresApi(api = Build.VERSION_CODES.N)
            protected void onPostExecute(String result) {
                //Do your thing
                if(whatQuestion.equals("receive_list_collect_product")){
                    splitResult(result);
                    //Toast.makeText(CollectProductActivity.this,
                    //        "res: "+result, Toast.LENGTH_SHORT).show();
                }
                //hide the dialog
                // asyncDialog.dismiss();
            }
        };
        task.execute(url_get);
    }
    // разобрать результат с сервера, список продуктов которые собраны для отправки и их колличество
    private void splitResult(String result){
        list.clear();
        try{
            String[] res = result.split("<br>");
            String[] one_temp = res[0].split("&nbsp");
            if (one_temp[0].equals("error") || one_temp[0].equals("messege")) {
                Toast.makeText(this, "" + one_temp[1], Toast.LENGTH_LONG).show();
                return;
            } else {
                for (int i = 0; i < res.length; i++) {
                    String[] temp = res[i].split("&nbsp");

                    int warehouse_id = Integer.parseInt(temp[0]);
                    int product_id = Integer.parseInt(temp[1]);
                    int product_inventory_id = Integer.parseInt(temp[2]);
                    String category = temp[3];
                    String brand = temp[4];
                    String characteristic = temp[5];
                    String type_packaging = temp[6];
                    String unit_measure = temp[7];
                    int weight_volume = Integer.parseInt(temp[8]);
                    int quantity_package = Integer.parseInt(temp[9]);
                    double quantity = Double.parseDouble(temp[10]);
                    //double partner_stock_quantity = Double.parseDouble(temp[11]);
                    //double quantity_to_order = Double.parseDouble(temp[12]);
                    int partner_warehouse_id = Integer.parseInt(temp[11]);
                    String city = "";
                    String street = "";
                    int house = 0;
                    String building = "";
                    int checked = Integer.parseInt(temp[12]);
                    int warehouse_inventory_id = Integer.parseInt(temp[13]);
                    int logistic_product=0;
                    try{
                        logistic_product = Integer.parseInt(temp[14]);
                    }catch (Exception ex){
                       // Toast.makeText(this, "Ex: "+ex, Toast.LENGTH_SHORT).show();
                    }
                    String product_name=temp[15];


                    OrderModel order = new OrderModel(warehouse_id, product_id, product_inventory_id,
                    category, product_name, brand, characteristic, type_packaging,  unit_measure, weight_volume,
                    quantity_package, quantity,  partner_warehouse_id, city, street,  house,
                     building, checked, warehouse_inventory_id,logistic_product);

                list.add(order);
                if(logistic_product == 0){
                    deliveryFlag=false;
                   // switchOnOffDelivery.setChecked(deliveryFlag);
                }
                }
            }
        }catch(Exception ex){
            //Toast.makeText(this, "Exception: "+ex, Toast.LENGTH_SHORT).show();
        }
            // receiveOrderProcessing();
            sortStartList();

    }
    private void sortStartList(){
        listProduct.clear();

        for(int i=0;i < listIntentProduct.size();i++){
            double quantity_to_order = listIntentProduct.get(i).getQuantity_to_order();
            double partner_stock_quantity = listIntentProduct.get(i).getPartner_stock_quantity();
            double quantityToCollect = listIntentProduct.get(i).getQuantityToCollect();
            double quantity_give_away_bad_do_not_receive=listIntentProduct.get(i).getQuantity_give_away_bad_do_not_receive();
            //double quantity = quantity_to_order - partner_stock_quantity - quantityToCollect;
            double quantity = 0;
            if(quantity_to_order > (partner_stock_quantity + quantity_give_away_bad_do_not_receive)){
                quantity = quantity_to_order - partner_stock_quantity
                        - quantityToCollect - quantity_give_away_bad_do_not_receive;
            }

            int checked = 0;
            int logistic_product=0;
            if(deliveryFlag){
                logistic_product=1;
            }

            OrderModel order = new OrderModel(listIntentProduct.get(i).getWarehouse_id(),
                    listIntentProduct.get(i).getProduct_id(), listIntentProduct.get(i).getProduct_inventory_id(),
                    listIntentProduct.get(i).getCategory(), listIntentProduct.get(i).getProduct_name(),
                    listIntentProduct.get(i).getBrand(),
                    listIntentProduct.get(i).getCharacteristic(), listIntentProduct.get(i).getType_packaging(),
                    listIntentProduct.get(i).getUnit_measure(), listIntentProduct.get(i).getWeight_volume(),
                    listIntentProduct.get(i).getQuantity_package(), quantity,
                    listIntentProduct.get(i).getPartner_warehouse_id(), listIntentProduct.get(i).getCity(),
                    listIntentProduct.get(i).getStreet(),  listIntentProduct.get(i).getHouse(),
                    listIntentProduct.get(i).getBuilding(), checked,
                    listIntentProduct.get(i).getWarehouse_inventory_id(), logistic_product);
            listProduct.add(order);
            for(int j=0;j < list.size();j++){

                if(list.get(j).getProduct_inventory_id() ==
                        listIntentProduct.get(i).getProduct_inventory_id()){

                    order = new OrderModel(list.get(j).getWarehouse_id(),
                            list.get(j).getProduct_id(), list.get(j).getProduct_inventory_id(),
                            list.get(j).getCategory(), list.get(j).getProduct_name(),
                            list.get(j).getBrand(),
                            list.get(j).getCharacteristic(), list.get(j).getType_packaging(),
                            list.get(j).getUnit_measure(), list.get(j).getWeight_volume(),
                            list.get(j).getQuantity_package(), list.get(j).getQuantity(),
                            list.get(j).getPartner_warehouse_id(), list.get(j).getCity(),
                            list.get(j).getStreet(),  list.get(j).getHouse(),
                            list.get(j).getBuilding(), list.get(j).getChecked(),
                            list.get(j).getWarehouse_inventory_id(),list.get(j).getLogistic_product());
                    listProduct.add(order);
                }
            }
        }
        for(int i=0;i < listProduct.size();i++){
            OrderModel tempo = new OrderModel(listProduct.get(i).getChecked());
            checkedList.add(tempo);
        }
        switchOnOffDelivery.setChecked(deliveryFlag);
        //Toast.makeText(this, "count: "+listProduct.size(), Toast.LENGTH_SHORT).show();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        // поместите строку для передачи обратно в intent и закрыть это действие
        Intent intent = new Intent();
        //intent.putExtra("message", buyerMessage);
        setResult(RESULT_OK, intent);
        finish();
    }


}