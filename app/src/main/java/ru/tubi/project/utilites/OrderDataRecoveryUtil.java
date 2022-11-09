package ru.tubi.project.utilites;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import ru.tubi.project.models.OrderModel;

public class OrderDataRecoveryUtil extends Fragment {
    private Context context;
    private SQLiteDatabase sqdb;
    private HelperDB my_db ;
    private OrderModel orderDataModel;
    private ArrayList<OrderModel>orderDataModelList = new ArrayList<>();

    public ArrayList<OrderModel> getOrderDataRecovery(Context context){

                orderDataModelList = goReadUid(context);

        String order_id_string = "";
        for(int i=0;i < orderDataModelList.size();i++){
            order_id_string += orderDataModelList.get(i).getOrder_id();
            if(i != orderDataModelList.size()-1){
                order_id_string += ";";
            }
        }
            Log.d("A111","OrderDataRecoveryUtil " +
                    "/ getOrderDataRecovery / order_id string = "+order_id_string);
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
        int col3 = сursor.getColumnIndex(HelperDB.DELIVERY);
        int col4 = сursor.getColumnIndex(HelperDB.JOINT_BUY);

        сursor.moveToFirst();
        while(!сursor.isAfterLast()){
            int order_id = Integer.parseInt(сursor.getString(col0));
            long dateMillis = Long.parseLong(сursor.getString(col1));
            String category = сursor.getString(col2);
            int delivery = Integer.parseInt(сursor.getString(col3));
            int joint_buy = Integer.parseInt(сursor.getString(col4));

            orderDataModel = new OrderModel(order_id, dateMillis, category, delivery,joint_buy);
            orderDataModelList.add(orderDataModel);

            сursor.moveToNext();
        }
        sqdb.close();



        return orderDataModelList;
    }
}
