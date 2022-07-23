package ru.tubi.project.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import ru.tubi.project.R;

import ru.tubi.project.activity.Partner.ListBuyersIssueGoodsActivity;
import ru.tubi.project.activity.Partner.PartnerListBuyersForCollectActivity;
import ru.tubi.project.activity.Partner.AcceptProductActivity;
import ru.tubi.project.activity.Partner.OrderBuyerListActivity;
import ru.tubi.project.activity.agent.ChoosePartnerActivity;
import ru.tubi.project.activity.agent.CodeGeneratorActivity;
import ru.tubi.project.activity.company_my.CatalogInWarehouseActivity;
import ru.tubi.project.activity.company_my.CollectProductForActivity;
import ru.tubi.project.activity.ForDelete.DistributionOrdersByWarehousesActivity;
import ru.tubi.project.activity.company_my.OrderForCollectActivity;
import ru.tubi.project.activity.company_my.OrderToProviderListActivity;
import ru.tubi.project.activity.company_my.ShipmentProductActivity;
import ru.tubi.project.activity.logistics.CarrierPanelActivity;
import ru.tubi.project.activity.logistics.DeliveryToReceiveGoodsActivity;
import ru.tubi.project.activity.logistics.HandOverProductActivity;
import ru.tubi.project.activity.logistics.IntercityDeliveryCalendarActivity;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.utilites.SearchOrder_id;
import ru.tubi.project.utilites.UserDataRecovery;

import static android.view.View.GONE;
import static ru.tubi.project.Config.ORDER_ID;
import static ru.tubi.project.free.AllText.MY_COMPANY;

public class CompanyMyActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvCmpanyName,tvProductReceipt,tvProductInvoice, tvProviderProductInvoice
            ,tvOrders, tvOrdersForProvider, tvOrdersProviders,tvBuyHistory;
    private Button  btnOrdersBuyers, btnSaleHistory;
    private LinearLayout llReceiptOfGoods, llAcceptProduct_buy,llOrdersProviders,llBuyHistory,llOrdersShipment
            ,llInventoryOfGoods, llGoMyCatalog
            ,llOrderForCollect, llSaleProduct,llLogistics,llPartner,llAcceptProduct_partner
            ,llCollectProductFor,llCollectProduct,llGiveAwayProduct,llCarrierPanel
            ,llIntercityDeliveryCal,llDeliveryToWarehouse,llHandOverProduct
            ,llAgent, llCreateOrder, llInvitePartner, llMyPartners;
    private Intent intent;
    private SearchOrder_id searchOrder_id = new SearchOrder_id();

    private UserModel userDataModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_my);
        setTitle(MY_COMPANY);//МОЯ КОМПАНИЯ

        tvCmpanyName=findViewById(R.id.tvCmpanyName);
        //btnOrdersBuyers=findViewById(R.id.btnOrdersBuyers);
        btnSaleHistory=findViewById(R.id.btnSaleHistory);
       // btnGoMyCatalog=findViewById(R.id.btnGoMyCatalog);
        tvOrdersForProvider=findViewById(R.id.tvOrdersForProvider);
        tvProviderProductInvoice=findViewById(R.id.tvProviderProductInvoice);
        tvOrders=findViewById(R.id.tvOrders);
        tvProductReceipt=findViewById(R.id.tvProductReceipt);
        tvProductInvoice=findViewById(R.id.tvProductInvoice);

        llReceiptOfGoods=findViewById(R.id.llReceiptOfGoods);
        llInventoryOfGoods=findViewById(R.id.llInventoryOfGoods);
        llAcceptProduct_buy=findViewById(R.id.llAcceptProduct_buy);
        llBuyHistory=findViewById(R.id.llBuyHistory);
        llOrdersShipment=findViewById(R.id.llOrdersShipment);
        llGoMyCatalog=findViewById(R.id.llGoMyCatalog);
        llOrderForCollect=findViewById(R.id.llOrderForCollect);
        llSaleProduct=findViewById(R.id.llSaleProduct);
        llLogistics=findViewById(R.id.llLogistics);
        llPartner=findViewById(R.id.llPartner);
        llCollectProductFor=findViewById(R.id.llCollectProductFor);
        llAcceptProduct_partner=findViewById(R.id.llAcceptProduct_partner);
        llCollectProduct=findViewById(R.id.llCollectProduct);
        llGiveAwayProduct=findViewById(R.id.llGiveAwayProduct);
        llCarrierPanel=findViewById(R.id.llCarrierPanel);
        llIntercityDeliveryCal=findViewById(R.id.llIntercityDeliveryCal);
        llDeliveryToWarehouse=findViewById(R.id.llDeliveryToWarehouse);
        llHandOverProduct=findViewById(R.id.llHandOverProduct);
        llAgent=findViewById(R.id.llAgent);
        llCreateOrder=findViewById(R.id.llCreateOrder);
        llInvitePartner=findViewById(R.id.llInvitePartner);
        llMyPartners=findViewById(R.id.llMyPartners);

        llAcceptProduct_buy.setOnClickListener(this);
        llBuyHistory.setOnClickListener(this);
        llOrdersShipment.setOnClickListener(this);
        llGoMyCatalog.setOnClickListener(this);
        llOrderForCollect.setOnClickListener(this);
        llCollectProductFor.setOnClickListener(this);
        llAcceptProduct_partner.setOnClickListener(this);
        llCollectProduct.setOnClickListener(this);
        llGiveAwayProduct.setOnClickListener(this);
        //btnOrdersBuyers.setOnClickListener(this);
        btnSaleHistory.setOnClickListener(this);
       // btnGoMyCatalog.setOnClickListener(this);
        llCarrierPanel.setOnClickListener(this);
        llIntercityDeliveryCal.setOnClickListener(this);
        llDeliveryToWarehouse.setOnClickListener(this);
        llHandOverProduct.setOnClickListener(this);
        tvOrdersForProvider.setOnClickListener(this);
        tvProviderProductInvoice.setOnClickListener(this);
        tvOrders.setOnClickListener(this);
        tvProductReceipt.setOnClickListener(this);
        tvProductInvoice.setOnClickListener(this);
        llCreateOrder.setOnClickListener(this);
        llInvitePartner.setOnClickListener(this);
        llMyPartners.setOnClickListener(this);

        llSaleProduct.setVisibility(GONE);
        llPartner.setVisibility(GONE);
        llLogistics.setVisibility(GONE);
        llCarrierPanel.setVisibility(GONE);
        llDeliveryToWarehouse.setVisibility(GONE);
        llHandOverProduct.setVisibility(GONE);
        llAgent.setVisibility(GONE);

        //получить из sqlLite данные пользователя и компании
        UserDataRecovery userDataRecovery = new UserDataRecovery();
        userDataModel = userDataRecovery.getUserDataRecovery(this);

        //подчеркивание текста
        tvCmpanyName.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvCmpanyName.setText(""+userDataModel.getAbbreviation()
                +" "+userDataModel.getCounterparty());
        // tvCmpanyName.setText(""+MY_ABBREVIATION+" "+MY_NAME_COMPANY);
       // String st = userDataModel.getPartner_role_list();
        String []partner_role_list = userDataModel.getPartner_role_list().split("&nbsp");
        for(int i=0;i < partner_role_list.length;i++){
            if(partner_role_list[i].equals("provider_business")){
                llSaleProduct.setVisibility(View.VISIBLE);
            }else if(partner_role_list[i].equals("partner_warehouse")){
                llPartner.setVisibility(View.VISIBLE);
            }else if(partner_role_list[i].equals("logistics_manager")){
                llLogistics.setVisibility(View.VISIBLE);
                llCarrierPanel.setVisibility(View.VISIBLE);
            }else if(partner_role_list[i].equals("logistics_partner")){
                llLogistics.setVisibility(View.VISIBLE);
                llDeliveryToWarehouse.setVisibility(View.VISIBLE);
                llHandOverProduct.setVisibility(View.VISIBLE);
            }
        }
        if(userDataModel.getRole().equals("admin")){
            llLogistics.setVisibility(View.VISIBLE);
            llCarrierPanel.setVisibility(View.VISIBLE);
            llAgent.setVisibility(View.VISIBLE);
           // llDeliveryToWarehouse.setVisibility(View.VISIBLE);
        }
        else if(userDataModel.getRole().equals("sales_agent")){
            llAgent.setVisibility(View.VISIBLE);
            llReceiptOfGoods.setVisibility(GONE);
            llInventoryOfGoods.setVisibility(GONE);
        }

    }

    @Override
    public void onClick(View v) {

        if(v.equals(llBuyHistory)){//tvBuyHistory
            Intent intent = new Intent(this, OrderHistoryActivity.class);
            startActivity(intent);
        }
        else if(v.equals(llGoMyCatalog)){
            Intent intent=new Intent(this, CatalogInWarehouseActivity.class);
            startActivity(intent);
        }
        else if(v.equals(llOrderForCollect)){
            Intent intent=new Intent(this, OrderForCollectActivity.class);
            startActivity(intent);
        }
        else if(v.equals(llOrdersShipment)){
            Intent intent=new Intent(this, ShipmentProductActivity.class);
            startActivity(intent);
            //Toast.makeText(this, "llOrdersShipment", Toast.LENGTH_SHORT).show();
        }
       /* else if(v.equals(btnGoMyCatalog)){
            Intent intent=new Intent(this, CatalogInWarehouseActivity.class);
            startActivity(intent);
        }*/
       /* else if(v.equals(btnOrdersBuyers)){
            Intent intent=new Intent(this, DistributionOrdersByWarehousesActivity.class);
            startActivity(intent);
        }*/
        else if(v.equals(llAcceptProduct_buy)){//
            Intent intent=new Intent(this, AcceptProductActivity.class);
            intent.putExtra("which_warehouse","storage_provider_warehouse");
            startActivity(intent);
        }else if(v.equals(llCollectProductFor)){// llCollectProductFor
            Intent intent=new Intent(this, CollectProductForActivity.class);
            startActivity(intent);
        }else if(v.equals(llAcceptProduct_partner)){
            Intent intent=new Intent(this, AcceptProductActivity.class);
            intent.putExtra("which_warehouse","partner_warehouse");
            startActivity(intent);
        }else if(v.equals(llCollectProduct)){
            Intent intent=new Intent(this, PartnerListBuyersForCollectActivity.class);
            startActivity(intent);
        }
        else if(v.equals(llGiveAwayProduct)){
            Intent intent=new Intent(this, ListBuyersIssueGoodsActivity.class);
            startActivity(intent);
        }
        else if(v.equals(tvOrdersForProvider)){
            Intent intent=new Intent(this, OrderToProviderListActivity.class);
            startActivity(intent);
        }
        else if(v.equals(tvProviderProductInvoice)){
            //Intent intent=new Intent(this, OrderBuyerListActivity.class);
            //startActivity(intent);
            Toast.makeText(this, "Раздел находится в разработке", Toast.LENGTH_SHORT).show();
        }
        else if(v.equals(tvOrders)){
            Intent intent=new Intent(this, OrderBuyerListActivity.class);
            startActivity(intent);
        }
        else if(v.equals(tvProductReceipt)){
            //Intent intent=new Intent(this, ListBuyersIssueGoodsActivity.class);
            // startActivity(intent);
            Toast.makeText(this, "Раздел находится в разработке", Toast.LENGTH_SHORT).show();
        }
        else if(v.equals(tvProductInvoice)){
            //Intent intent=new Intent(this, ListBuyersIssueGoodsActivity.class);
            // startActivity(intent);
            Toast.makeText(this, "Раздел находится в разработке", Toast.LENGTH_SHORT).show();
        }
        else if(v.equals(llCarrierPanel)){//tvCarrierPanel
            Intent intent=new Intent(this, CarrierPanelActivity.class);
            startActivity(intent);
        }
        else if(v.equals(llIntercityDeliveryCal)){
            Intent intent=new Intent(this, IntercityDeliveryCalendarActivity.class);
            startActivity(intent);
        }
        else if(v.equals(llDeliveryToWarehouse)){
            Intent intent=new Intent(this, DeliveryToReceiveGoodsActivity.class);
            startActivity(intent);
        }else if(v.equals(llHandOverProduct)){
            Intent intent=new Intent(this, HandOverProductActivity.class);
            startActivity(intent);
        }else if(v.equals(llCreateOrder)){
            Intent intent=new Intent(this, ChoosePartnerActivity.class);
            startActivity(intent);
        }else if(v.equals(llInvitePartner)){
            Intent intent=new Intent(this, CodeGeneratorActivity.class);
            startActivity(intent);
        }
        else if(v.equals(llMyPartners)){
            Toast.makeText(this, "Раздел в разработке", Toast.LENGTH_SHORT).show();
        }

    }
    //слушатель возврата по методу Back(); из предыдущей активности
    //нужен для обновления необходимой информации
    @Override
    protected void onRestart() {
        super.onRestart();
        //запуск метода обновить меню,
        // нужен для обновления цвета корзины если не пустая
        invalidateOptionsMenu();
    }
    //обновить корзину
    //этот метод запускает invalidateOptionsMenu();
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        searchOrder_id.searchStartedOrder(this);
        if(ORDER_ID != 0){
            menu.findItem(R.id.shoping_box).setIcon(R.drawable.soping_box_green_60ps);
        }
           // menu.findItem(R.id.shoping_box).setShowAsAction(GONE);
          //  menu.findItem(R.id.shoping_box).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item){
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
        if(itemID==R.id.shoping_box){
            intent=new Intent(this, ShopingBox.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }
}