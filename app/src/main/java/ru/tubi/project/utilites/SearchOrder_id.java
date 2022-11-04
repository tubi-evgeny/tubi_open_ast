package ru.tubi.project.utilites;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import ru.tubi.project.models.UserModel;

import static ru.tubi.project.activity.Config.ORDER_ID;
import static ru.tubi.project.activity.Config.PARTNER_COMPANY_TAXPAYER_ID_FOR_AGENT;
import static ru.tubi.project.free.VariablesHelpers.DELIVERY_TO_BUYER_STATUS;
import static ru.tubi.project.utilites.Constant.API;
import static ru.tubi.project.utilites.Constant.SEARCH_MY_ACTIVE_ORDER;
//import static com.example.tubi.utilites.Constant.SEARCH_OPEN_ORDER;
//import static com.example.tubi.MainActivity.test;

public class SearchOrder_id {

    private String url_get;
    private Context context;
    private HelperDB my_db;
    private SQLiteDatabase sqdb;
    private UserModel userDataModel;
    private  UserDataRecovery userDataRecovery = new UserDataRecovery();

    //если есть открытый заказ то получить его номер или 0 если заказа открытого нет
    public void searchStartedOrder(Context context){
        this.context=context;

        //получить из sqlLite данные пользователя и компании
        userDataModel = userDataRecovery.getUserDataRecovery(this.context);

        String user_uid = userDataModel.getUid();
        long company_tax_id = userDataModel.getCompany_tax_id();

        //проверить заказ создает агент продаж
        if(userDataModel.getRole().equals("sales_agent")){
            company_tax_id = PARTNER_COMPANY_TAXPAYER_ID_FOR_AGENT;
        }


       /* url_get = SEARCH_MY_ACTIVE_ORDER;
        url_get += "&" + "user_uid=" + userDataModel.getUid();
        url_get += "&" + "company_tax_id=" + userDataModel.getCompany_tax_id();
        String st="my_started_order";
        setInitialData(url_get,st);

        url_get = API;
        url_get += "search_my_active_orders_list";
        url_get += "&" + "user_uid=" + userDataModel.getUid();
        url_get += "&" + "company_tax_id=" + userDataModel.getCompany_tax_id();
        String whatQuestions="search_my_active_orders_list";
        setInitialData(url_get,whatQuestions);*/

        go(user_uid, company_tax_id);

    }
    //для агента, если есть открытый заказ то получить его номер или 0 если заказа открытого нет
   /* public void searchStartedOrder(Context context, long partner_company_tax_id){
        this.context=context;

        //получить из sqlLite данные пользователя
        userDataModel = userDataRecovery.getUserDataRecovery(this.context);

        //указать uid агента в создателе заказа
        String agent_uid = userDataModel.getUid();

        go(agent_uid, partner_company_tax_id);
    }*/
    private void go(String user_uid, long company_tax_id){

        url_get = SEARCH_MY_ACTIVE_ORDER;
        url_get += "&" + "user_uid=" + user_uid;//userDataModel.getUid();
        url_get += "&" + "company_tax_id=" + company_tax_id;
        String st="my_started_order";
        setInitialData(url_get,st);

        url_get = API;
        url_get += "search_my_active_orders_list";
        url_get += "&" + "user_uid=" + user_uid; //userDataModel.getUid();
        url_get += "&" + "company_tax_id=" + company_tax_id;
        String whatQuestions="search_my_active_orders_list";
        setInitialData(url_get,whatQuestions);
    }

    private void setInitialData(String url_get, String whatQuestion) {

        InitialData task=new InitialData(){
            protected void onPostExecute(String result) {
                //Do your thing
                if(whatQuestion.equals("my_started_order")){
                    splitRes(result);
                }else if(whatQuestion.equals("search_my_active_orders_list")){
                    insertOrders(result);
                }

            }
        };
        task.execute(url_get);
    }
    private void splitRes(String result){
        //if(result.trim().length() > 0){//string.trim().length() == 0
            try{
                //checkOrUpdataOrderId(result);
                result=result.trim();
                if(Integer.parseInt(result) > 0){
                     ORDER_ID = Integer.parseInt(result);
                }else{
                    ORDER_ID = 0;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
      //  }
    }
    //разобрать результат и записать в таблицу
    private void insertOrders(String result){
        try {
            result=result.trim();
            if(!result.isEmpty()){
                my_db = new HelperDB(this.context);
                my_db.clearTableOrders();

                String [] res=result.split("<br>");
                for(int i=0;i<res.length;i++) {
                    String[] temp = res[i].split("&nbsp");

                    int order_id = Integer.parseInt(temp[0]);
                    long date_millis = Long.parseLong(temp[1]);
                    String category = temp[2];
                    String delivery = temp[3];

                    ContentValues cv = new ContentValues();

                    cv.put(my_db.ORDER_ID, order_id);
                    cv.put(my_db.DATE_MILLIS, date_millis);
                    cv.put(my_db.CATEGORY, category);
                    cv.put(my_db.DELIVERY, delivery);

                    sqdb = my_db.getWritableDatabase();
                    sqdb.insert(my_db.TABLE_NAME_ORDER_ID, null, cv);
                    if(order_id != 0 ){
                        DELIVERY_TO_BUYER_STATUS = Integer.parseInt(delivery);
                    }
                }
                sqdb.close();
                Log.d("A111","SearchOrder_id. список заказов "+result);
            }else{
                my_db = new HelperDB(this.context);
                my_db.clearTableOrders();

                ContentValues cv = new ContentValues();

                cv.put(my_db.ORDER_ID, 0);
                cv.put(my_db.DATE_MILLIS, 0);
                cv.put(my_db.CATEGORY, 0);
                cv.put(my_db.DELIVERY, 0);

                sqdb = my_db.getWritableDatabase();
                sqdb.insert(my_db.TABLE_NAME_ORDER_ID, null, cv);

                sqdb.close();
                Log.d("A111","SearchOrder_id. Result is empty. Result create default");
            }

        }catch (Exception ex){
            Log.d("A111","SearchOrder_id. ошибка при получении списка заказов "+ex.toString());
        }
    }
    public void checkOrUpdataOrderId(String result){
       // UserDataRecovery userDataRecovery = new UserDataRecovery();
        UserModel userDataModel = userDataRecovery.getUserDataRecovery(this.context);

        int orderOld = userDataModel.getOrder_id();
        int orderNew = Integer.parseInt(result);
        if(orderOld != orderNew) {
            my_db = new HelperDB(this.context);
            sqdb = my_db.getWritableDatabase();
            ContentValues cv = new ContentValues();

            cv.put(my_db.ORDER_ID, orderNew);
            String[] forUpdate = new String[]{String.valueOf(orderOld)};

            sqdb.update(HelperDB.TABLE_NAME_MY_USER, cv,
                    HelperDB.ORDER_ID + "=?", forUpdate);
            sqdb.close();
        }
    }
}
