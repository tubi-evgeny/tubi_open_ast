package ru.tubi.project.utilites;

import android.content.Context;
import android.view.Menu;

import java.util.ArrayList;

import ru.tubi.project.R;
import ru.tubi.project.models.OrderModel;

public class GetColorShopingBox {
    Menu menu;
    Context context;
    private ArrayList<OrderModel> orderDataModelList = new ArrayList<>();
    private OrderDataRecoveryUtil orderDataRecoveryUtil = new OrderDataRecoveryUtil();


    //GetColorShopingBox gc = new GetColorShopingBox();
    //        menu = gc.color(this, menu);
    public Menu color(Context context, Menu menu){
        this.context=context;
        this.menu=menu;
        //получить список заказав с характеристиками
        orderDataModelList.clear();
        orderDataModelList = orderDataRecoveryUtil.getOrderDataRecovery(context);
        try {
            if (orderDataModelList.get(0).getOrder_id() != 0) {
                this.menu.findItem(R.id.shoping_box).setIcon(R.drawable.soping_box_green_60ps);
            }else{
                this.menu.findItem(R.id.shoping_box).setIcon(R.drawable.shoping_box_60ps);
            }
        }catch(Exception ex){
            this.menu.findItem(R.id.shoping_box).setIcon(R.drawable.shoping_box_60ps);
        }
        return this.menu;
    }

}
