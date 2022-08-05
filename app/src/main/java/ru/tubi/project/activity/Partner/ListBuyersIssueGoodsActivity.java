package ru.tubi.project.activity.Partner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import ru.tubi.project.R;
import ru.tubi.project.adapters.ListBuyersIssueGoodsBuyerAdapter;
import ru.tubi.project.adapters.ListBuyersIssueGoodsWarehouseAdapter;
import ru.tubi.project.models.CarrierPanelModel;
import ru.tubi.project.models.CounterpartyModel;
import ru.tubi.project.utilites.InitialData;

import java.util.ArrayList;
import java.util.Comparator;

import ru.tubi.project.utilites.Constant;

import static ru.tubi.project.activity.Config.MY_COMPANY_TAXPAYER_ID;
import static ru.tubi.project.free.AllCollor.TUBI_BLACK;
import static ru.tubi.project.free.AllText.BUILDING;
import static ru.tubi.project.free.AllText.ISSUE_GOODS;
import static ru.tubi.project.free.AllText.LIST_BUYERS;
import static ru.tubi.project.free.AllText.TAX_ID_SMALL;
import static ru.tubi.project.free.AllText.WAREHOUSE;

public class ListBuyersIssueGoodsActivity extends AppCompatActivity
                                            implements View.OnClickListener {

    private RecyclerView rvBuyer,rvWarehouse;
    private LinearLayout llWarehouseInfo;
    private TextView tvWarehouseInfo, tvApply;
    private ArrayList<CarrierPanelModel> warehousesInfoList=new ArrayList<>();
    private ArrayList<CounterpartyModel> buyersCompanyList = new ArrayList<>();
    private CarrierPanelModel warehouseInfoMod;
    private int warehouse_id;
    private String myWarehousInfo;
    private ListBuyersIssueGoodsWarehouseAdapter adapter_warehouse;
    private ListBuyersIssueGoodsBuyerAdapter adapter_buyer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_buyers_issue_goods);
        setTitle(ISSUE_GOODS);
        getSupportActionBar().setSubtitle(LIST_BUYERS);
        //Выдать товар cписок покупателей

        rvBuyer=findViewById(R.id.rvBuyerList);
        llWarehouseInfo=findViewById(R.id.llWarehouseInfo);
        rvWarehouse=findViewById(R.id.rvWarehouseList);
        tvWarehouseInfo=findViewById(R.id.tvWarehouseInfo);

        llWarehouseInfo.setOnClickListener(this);

        startWarehousesList();

        ListBuyersIssueGoodsWarehouseAdapter.RecyclerViewClickListener clickListener =
                new ListBuyersIssueGoodsWarehouseAdapter.RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        warehouse_id = warehousesInfoList.get(position).getWarehouse_id();
                        warehouseInfoMod = warehousesInfoList.get(position);
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
                        rvWarehouse.setVisibility(View.GONE);
                        rvBuyer.setVisibility(View.VISIBLE);
                    }
                };
        ListBuyersIssueGoodsBuyerAdapter.RecyclerViewClickListener clickBuyer =
                new ListBuyersIssueGoodsBuyerAdapter.RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        whatBuyerShowOrder(position);
                        //Toast.makeText(ListBuyersIssueGoodsActivity.this,
                            //    "buyer:\n"+"position: "+position, Toast.LENGTH_SHORT).show();
                    }
                };
        adapter_warehouse = new ListBuyersIssueGoodsWarehouseAdapter(this,
                warehousesInfoList,clickListener);
        adapter_buyer = new ListBuyersIssueGoodsBuyerAdapter(this,
                buyersCompanyList,clickBuyer);
        rvWarehouse.setAdapter(adapter_warehouse);
        rvBuyer.setAdapter(adapter_buyer);
    }

    @Override
    public void onClick(View v) {
        if(v.equals(llWarehouseInfo)){
            rvWarehouse.setVisibility(View.VISIBLE);
            rvBuyer.setVisibility(View.GONE);
            // Toast.makeText(this, "llWarehouseInfo", Toast.LENGTH_SHORT).show();
        }
    }
    //перейти в список товаров в заказе покупателя
    private void whatBuyerShowOrder(int position){
        String stBuyersCompany = buyersCompanyList.get(position).getAbbreviation()+" "
                +buyersCompanyList.get(position).getCounterparty()+" "+TAX_ID_SMALL+" "
                +buyersCompanyList.get(position).getTaxpayer_id();

        int order_id = buyersCompanyList.get(position).getOrder_id();
        Intent intent = new Intent(this, BuyerOrderIssueActivity.class);
        intent.putExtra("order_id",order_id);

        intent.putExtra("myWarehousInfo",myWarehousInfo);
        intent.putExtra("stBuyersCompany",stBuyersCompany);

        intent.putExtra("partnerWarehousInfo",warehouseInfoMod);
        intent.putExtra("buyerCompany",buyersCompanyList.get(position));
        intent.putExtra("warehouse_id",warehouse_id);
        startActivity(intent);
        // Toast.makeText(this, "order_id: "+order_id, Toast.LENGTH_SHORT).show();
    }
    //получить список покупателей
    private void buyersCompanyList(){
        String url = Constant.PARTNER_OFFICE;
        url += "receive_list_buyers_company_for_issue";
        url += "&"+"warehouse_id="+warehouse_id;
        String whatQuestion = "receive_list_buyers_company_for_issue";
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
        InitialData task=new InitialData(){

            @RequiresApi(api = Build.VERSION_CODES.N)
            protected void onPostExecute(String result) {
                if(whatQuestion.equals("receive_list_partner_warehouse")){
                    splitWarehouseResult(result);
                    // Toast.makeText(PartnerListBuyersForCollectActivity.this,
                    //     "res: "+result, Toast.LENGTH_SHORT).show();
                }else if(whatQuestion.equals("receive_list_buyers_company_for_issue")){
                    splitBuyersCompanyResult(result);
                    // Toast.makeText(PartnerListBuyersForCollectActivity.this,
                    //         "res: "+result, Toast.LENGTH_SHORT).show();
                }
                //hide the dialog
                // asyncDialog.dismiss();
            }
        };
        task.execute(url_get);
    }
    //разобрать результат от сервера список company
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void splitBuyersCompanyResult(String result){
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
                    int completedProcessing= 0;//Integer.parseInt(temp[4]);

                    CounterpartyModel buyer = new CounterpartyModel(order_id,taxpayer_id,
                            abbreviation, counterparty, completedProcessing);
                    buyersCompanyList.add(buyer);
                }
                //сортируем лист по 2 полям (logistic_product и car_or_warehouse_id)
                buyersCompanyList.sort(Comparator.comparing(CounterpartyModel::getAbbreviation)
                        .thenComparing(CounterpartyModel::getCounterparty));
            }


            //  adapter_buyer.notifyDataSetChanged();
        }catch (Exception ex){
            // Toast.makeText(this, "ex: "+ex, Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this, "test: ", Toast.LENGTH_SHORT).show();
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
                rvWarehouse.setVisibility(View.GONE);
                rvBuyer.setVisibility(View.VISIBLE);

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