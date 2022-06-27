package ru.tubi.project.utilites;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.io.Serializable;
import java.util.ArrayList;

import ru.tubi.project.models.OrderModel;
import ru.tubi.project.models.UserModel;

import static ru.tubi.project.Config.MY_ABBREVIATION;
import static ru.tubi.project.Config.MY_COMPANY_TAXPAYER_ID;
import static ru.tubi.project.Config.MY_NAME;
import static ru.tubi.project.Config.MY_NAME_COMPANY;
import static ru.tubi.project.Config.MY_UID;
import static ru.tubi.project.Config.ROLE;
import static ru.tubi.project.utilites.HelperDB.TABLE_NAME_ORDER_ID;

public class OrderDataRecoveryUtil extends Fragment {
    private Context context;
    private SQLiteDatabase sqdb;
    private HelperDB my_db ;
    private OrderModel orderDataModel;
    private ArrayList<OrderModel>orderDataModelList = new ArrayList<>();

    /*
     //получить список заказав с характеристиками
        ArrayList<OrderModel> orderDataModelList;
        //получить из sqlLite данные о заказах
        OrderDataRecoveryUtil orderDataRecoveryUtil = new OrderDataRecoveryUtil();
        orderDataModel = orderDataRecoveryUtil.getOrderDataRecovery(this);

        orderDataModel.getOrder_id(); orderDataModel.getDate_millis();
        orderDataModel.getCategory();
     */

    public ArrayList<OrderModel> getOrderDataRecovery(Context context){
        //this.context=context;
        //ArrayList<OrderModel>
                orderDataModelList = goReadUid(context);
        //Toast.makeText(context, "id: "+orderDataModelList.get(0).getOrder_id(), Toast.LENGTH_SHORT).show();
        return orderDataModelList;
    }
    // Проверить есть ли в таблице данные о открытых заказах
    private ArrayList<OrderModel> goReadUid(Context context){
        my_db = new HelperDB(context);
        sqdb = my_db.getWritableDatabase();
        //проверить колличество записей в таблице
        Cursor сursor = sqdb.query(HelperDB.TABLE_NAME_ORDER_ID, null, null,
                null, null, null, null);
        int col0 = сursor.getColumnIndex(HelperDB.ORDER_ID);
        int col1 = сursor.getColumnIndex(HelperDB.DATE_MILLIS);
        int col2 = сursor.getColumnIndex(HelperDB.CATEGORY);

        сursor.moveToFirst();
        while(!сursor.isAfterLast()){
            int order_id = Integer.parseInt(сursor.getString(col0));
            long dateMillis = Long.parseLong(сursor.getString(col1));
            String category = сursor.getString(col2);

            orderDataModel = new OrderModel(order_id, dateMillis, category);
            orderDataModelList.add(orderDataModel);

            сursor.moveToNext();
        }
        sqdb.close();



        return orderDataModelList;
    }
}
