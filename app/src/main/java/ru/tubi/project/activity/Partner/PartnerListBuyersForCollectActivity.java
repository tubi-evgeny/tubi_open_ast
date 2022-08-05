package ru.tubi.project.activity.Partner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import ru.tubi.project.R;
import ru.tubi.project.adapters.PartnerListBuyersForCollectAdapter;
import ru.tubi.project.adapters.PartnerListBuyersForCollectAdapter_two;
import ru.tubi.project.models.CarrierPanelModel;
import ru.tubi.project.models.CounterpartyModel;
import ru.tubi.project.utilites.InitialData;

import java.util.ArrayList;
import java.util.Comparator;

import ru.tubi.project.utilites.Constant;

import static ru.tubi.project.activity.Config.MY_COMPANY_TAXPAYER_ID;
import static ru.tubi.project.free.AllCollor.TUBI_BLACK;
import static ru.tubi.project.free.AllText.BUILDING;
import static ru.tubi.project.free.AllText.FOR_COLLECT_PRODUCT;
import static ru.tubi.project.free.AllText.LIST_BUYERS;
import static ru.tubi.project.free.AllText.LOAD_TEXT;
import static ru.tubi.project.free.AllText.TAX_ID_SMALL;
import static ru.tubi.project.free.AllText.WAREHOUSE;

public class PartnerListBuyersForCollectActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView,rvWarehouseList;
    private LinearLayout llWarehouseInfo;
    private TextView tvWarehouseInfo, tvApply;
    private ArrayList<CarrierPanelModel> warehousesInfoList=new ArrayList<>();
    private ArrayList<CounterpartyModel> buyersCompanyList = new ArrayList<>();
    private PartnerListBuyersForCollectAdapter adapter_warehouse;
    private PartnerListBuyersForCollectAdapter_two adapter_buyer;
    private int warehouse_id;
    private String myWarehousInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner_list_buyers_for_collect);
        setTitle(LIST_BUYERS);
        getSupportActionBar().setSubtitle(FOR_COLLECT_PRODUCT);
        //Список покупателей для сборки товара

        recyclerView=findViewById(R.id.rvList);
        llWarehouseInfo=findViewById(R.id.llWarehouseInfo);
        rvWarehouseList=findViewById(R.id.rvWarehouseList);
        tvWarehouseInfo=findViewById(R.id.tvWarehouseInfo);

        llWarehouseInfo.setOnClickListener(this);

        startWarehousesList();

        PartnerListBuyersForCollectAdapter.RecyclerViewClickListener clickListener =
                new PartnerListBuyersForCollectAdapter.RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        warehouse_id = warehousesInfoList.get(position).getWarehouse_id();
                        myWarehousInfo = WAREHOUSE+" № "
                                +warehousesInfoList.get(position).getWarehouse_info_id()+"/"
                                +warehousesInfoList.get(position).getWarehouse_id()+" "
                                +warehousesInfoList.get(position).getCity()+" "
                                +warehousesInfoList.get(position).getStreet()+" "
                                +warehousesInfoList.get(position).getHouse();
                        try{
                            myWarehousInfo += BUILDING+" "+warehousesInfoList.get(position).getBuilding();
                        }catch (Exception ex){}
                        tvWarehouseInfo.setText(""+myWarehousInfo);
                        tvWarehouseInfo.setTextColor(TUBI_BLACK);

                        buyersCompanyList();
                        rvWarehouseList.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                      //  Toast.makeText(PartnerListBuyersForCollectActivity.this,
                      //          "warehouse:\n"+"position: "+position, Toast.LENGTH_SHORT).show();
                    }
                };
        PartnerListBuyersForCollectAdapter_two.RecyclerViewClickListener clickBuyer =
                new PartnerListBuyersForCollectAdapter_two.RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        whatBuyerShowOrder(position);
                        //Toast.makeText(PartnerListBuyersForCollectActivity.this,
                        //        "buyer:\n"+"position: "+position, Toast.LENGTH_SHORT).show();
                    }
                };
    adapter_warehouse = new PartnerListBuyersForCollectAdapter(this,
                                    warehousesInfoList,clickListener);

    adapter_buyer = new PartnerListBuyersForCollectAdapter_two(this,
                                         buyersCompanyList,clickBuyer);
        rvWarehouseList.setAdapter(adapter_warehouse);
        recyclerView.setAdapter(adapter_buyer);
    }

    @Override
    public void onClick(View v) {
        if(v.equals(llWarehouseInfo)){
            rvWarehouseList.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }
    //перейти в список товаров в заказе покупателя
    private void whatBuyerShowOrder(int position){
        //если этот заказ удален то аернуться
        if(buyersCompanyList.get(position).getOrder_deleted() == 1
                && buyersCompanyList.get(position).getCollect_product_for_delete() == 0){
            return;
        }
        String stBuyersCompany = buyersCompanyList.get(position).getAbbreviation()+" "
                +buyersCompanyList.get(position).getCounterparty()+" "+TAX_ID_SMALL+" "
                +buyersCompanyList.get(position).getTaxpayer_id();

        int order_id = buyersCompanyList.get(position).getOrder_id();
        Intent intent = new Intent(this, PartnerCollectProductActivity.class);
        intent.putExtra("order_id",order_id);
        intent.putExtra("deliveryKey",buyersCompanyList.get(position).getDelivery());
        intent.putExtra("myWarehousInfo",myWarehousInfo);
        intent.putExtra("stBuyersCompany",stBuyersCompany);
        intent.putExtra("warehouse_id",warehouse_id);
        intent.putExtra("order_deleted",buyersCompanyList.get(position).getOrder_deleted());
        startActivity(intent);
       // Toast.makeText(this, "order_id: "+order_id, Toast.LENGTH_SHORT).show();
    }
    //получить список покупателей
    private void buyersCompanyList(){
        String url = Constant.PARTNER_OFFICE;
        url += "receive_list_buyers_company";
        url += "&"+"warehouse_id="+warehouse_id;
        String whatQuestion = "receive_list_buyers_company";
        setInitialData(url, whatQuestion);
    }
    //получить список склад партнер контрагента
    private void startWarehousesList(){
        String url = Constant.PARTNER_OFFICE;
        url += "receive_list_partner_warehouse";
        url += "&"+"company_tax_id="+MY_COMPANY_TAXPAYER_ID;
        String whatQuestion = "receive_list_partner_warehouse";
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
                if(whatQuestion.equals("receive_list_partner_warehouse")){
                    splitWarehouseResult(result);
                   // Toast.makeText(PartnerListBuyersForCollectActivity.this,
                       //     "res: "+result, Toast.LENGTH_SHORT).show();
                }else if(whatQuestion.equals("receive_list_buyers_company")){
                    splitBuyersCompanyResult(result);
                   // Toast.makeText(PartnerListBuyersForCollectActivity.this,
                   //         "res: "+result, Toast.LENGTH_SHORT).show();
                }
                //скрыть диалоговое окно
                 asyncDialog.dismiss();
            }
        };
        task.execute(url_get);
    }
    //разобрать результат от сервера список company
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void splitBuyersCompanyResult(String result){
        //Toast.makeText(this, "res:\n"+result, Toast.LENGTH_SHORT).show();
        buyersCompanyList.clear();
        try{
            String[] res = result.split("<br>");
            String[] one_temp = res[0].split("&nbsp");
            if (one_temp[0].equals("error") || one_temp[0].equals("messege")) {
                Toast.makeText(this, "" + one_temp[1], Toast.LENGTH_LONG).show();
               // adapter_buyer.notifyDataSetChanged();
               // return;
            } else {
                for (int i = 0; i < res.length; i++) {
                    String[] temp = res[i].split("&nbsp");
                    int order_id=Integer.parseInt(temp[0]);
                    String abbreviation=temp[1];
                    String counterparty=temp[2];
                    long taxpayer_id = Long.parseLong(temp[3]);
                    int order_deleted = Integer.parseInt(temp[4]);
                    int collect_product_for_delete = Integer.parseInt(temp[5]);
                    int delivery = Integer.parseInt(temp[6]);
                    int completedProcessing= 0;//Integer.parseInt(temp[4]);

                    CounterpartyModel buyer = new CounterpartyModel(order_id,taxpayer_id,
                            abbreviation, counterparty, order_deleted, collect_product_for_delete,
                            delivery, completedProcessing);
                    buyersCompanyList.add(buyer);
                }
                //сортируем лист по 2 полям (logistic_product и car_or_warehouse_id)
                buyersCompanyList.sort(Comparator.comparing(CounterpartyModel::getAbbreviation)
                        .thenComparing(CounterpartyModel::getCounterparty));
            }


          //  adapter_buyer.notifyDataSetChanged();
        }catch (Exception ex){
            Toast.makeText(this, "ex: "+ex, Toast.LENGTH_SHORT).show();
        }
        //Toast.makeText(this, "test: ", Toast.LENGTH_SHORT).show();
        adapter_buyer.notifyDataSetChanged();
    }
    //разобрать результат от сервера список складов
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void splitWarehouseResult(String result){
        warehousesInfoList.clear();
        try{
            String[] res = result.split("<br>");
            String[] one_temp = res[0].split("&nbsp");
            if (one_temp[0].equals("error") || one_temp[0].equals("messege")) {
                Toast.makeText(this, "" + one_temp[1], Toast.LENGTH_LONG).show();
                return;
            } else {
                for (int i = 0; i < res.length; i++) {
                    String[] temp = res[i].split("&nbsp");
                    int warehouse_info_id=Integer.parseInt(temp[0]);
                    int Warehouse_id = Integer.parseInt(temp[1]);
                    String City = temp[2];
                    String Street = temp[3];
                    int House = Integer.parseInt(temp[4]);
                    String Building = "";
                    try {
                        Building = temp[5];
                    } catch (Exception ex) {      }


                    CarrierPanelModel warehouse = new CarrierPanelModel(warehouse_info_id,
                            Warehouse_id, City, Street,House, Building);
                    warehousesInfoList.add(warehouse);
                }
            }
            //сортируем лист по 1 полям (Warehouse_info_id )
            warehousesInfoList.sort(Comparator.comparing(CarrierPanelModel::getWarehouse_info_id));

            //сделать список строк для показа в ListView
            //makeStringListForWarehouse();

            if(warehousesInfoList.size() > 1){
                adapter_warehouse.notifyDataSetChanged();
                //adapLvWarehouse.notifyDataSetChanged();
            }else{
                warehouse_id = warehousesInfoList.get(0).getWarehouse_id();
                myWarehousInfo = WAREHOUSE+" № "+warehousesInfoList.get(0).getWarehouse_info_id()+"/"
                        +warehousesInfoList.get(0).getWarehouse_id()+" "+warehousesInfoList.get(0).getCity()+" "
                        +warehousesInfoList.get(0).getStreet()+" "+warehousesInfoList.get(0).getHouse();
                try{
                    myWarehousInfo += BUILDING+" "+warehousesInfoList.get(0).getBuilding();
                }catch (Exception ex){}
                tvWarehouseInfo.setText(""+myWarehousInfo);
                tvWarehouseInfo.setTextColor(TUBI_BLACK);

                buyersCompanyList();
                rvWarehouseList.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);

                // tvWarehouseInfo.setClickable(false);
                llWarehouseInfo.setClickable(false);

                //startList();
            }
        }catch (Exception ex){

        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        buyersCompanyList();
    }
}