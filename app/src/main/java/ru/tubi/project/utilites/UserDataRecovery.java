package ru.tubi.project.utilites;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import ru.tubi.project.models.UserModel;
import static ru.tubi.project.activity.Config.MY_UID;
import static ru.tubi.project.activity.Config.MY_NAME;
import static ru.tubi.project.activity.Config.MY_ABBREVIATION;
import static ru.tubi.project.activity.Config.MY_NAME_COMPANY;
import static ru.tubi.project.activity.Config.MY_COMPANY_TAXPAYER_ID;
import static ru.tubi.project.activity.Config.ROLE;

public class UserDataRecovery {
    private Context context;
    private SQLiteDatabase sqdb;
    private HelperDB my_db ;//= new HelperDB(this);
    private UserModel userDataModel;

    public UserModel getUserDataRecovery(Context context){
        //this.context=context;
        UserModel userDataModel = goReadUid(context);
        return userDataModel;
    }
    // Проверьте, вошел ли пользователь уже в систему или нет
    private UserModel goReadUid(Context context){
        my_db = new HelperDB(context);
        Cursor yourCursor = my_db.getYourTableContents();
        //проверить колличество записей в таблице
        int i = 0;
        while (yourCursor.moveToNext()) {
            i += 1;
        }
        if(i != 0) {
            //получить его данные
            sqdb = my_db.getWritableDatabase();

            Cursor c = sqdb.query(HelperDB.TABLE_NAME, null, null,
                    null, null, null, null);
            int col0 = c.getColumnIndex(HelperDB.USER_UID);
            int col1 = c.getColumnIndex(HelperDB.USER_NAME);
            int col2 = c.getColumnIndex(HelperDB.ABBREVIATION);
            int col3 = c.getColumnIndex(HelperDB.COUNTERPARTY);
            int col4 = c.getColumnIndex(HelperDB.TAXPAYER_ID);
            int col5 = c.getColumnIndex(HelperDB.USER_ROLE);
            int col6 = c.getColumnIndex(HelperDB.ORDER_ID);
            int col7 = c.getColumnIndex(HelperDB.PARTNER_ROLE);
            int col8 = c.getColumnIndex(HelperDB.USER_PHONE);

            c.moveToFirst();
            String uid = c.getString(col0);
            String myName = c.getString(col1);
            String abbreviation = c.getString(col2);
            String counterparty = c.getString(col3);
            String tax_id = c.getString(col4);
            long company_tax_id = 0;
            //if(!c.getString(col4).isEmpty()){
            try{company_tax_id = Long.parseLong(c.getString(col4)); }catch (Exception ex){  }
           // }
            String user_role = c.getString(col5);
            int order_id = Integer.parseInt(c.getString(col6));
            String partner_role_list = "";
            try{partner_role_list = c.getString(col7);}catch (Exception ex){}
            String userPhone = c.getString((col8));
            sqdb.close();
            // если пользователь в системе то
            if (!uid.isEmpty()) {
                MY_UID = c.getString(col0);
                MY_NAME = c.getString(col1);
                MY_ABBREVIATION=c.getString(col2);
                MY_NAME_COMPANY=c.getString(col3);
                MY_COMPANY_TAXPAYER_ID=c.getString(col4);
                ROLE=c.getString(col5);
            }
            userDataModel = new UserModel(uid, myName, abbreviation, counterparty,
                    company_tax_id, user_role, order_id, partner_role_list, userPhone);
        }
        return userDataModel;
    }
}
