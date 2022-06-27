package ru.tubi.project.utilites;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import ru.tubi.project.models.UserModel;

import static ru.tubi.project.Config.MY_COMPANY_TAXPAYER_ID;
import static ru.tubi.project.Config.PARTNER_ROLE_LIST;
import static ru.tubi.project.Config.ROLE_PARTNER_TEST;
import static ru.tubi.project.utilites.Constant.API;

public class PartnerRoleReceive {

    private SQLiteDatabase sqdb;
    private HelperDB my_db;
    private Context context;
    private UserModel userDataModel;

    public void RoleReceive(Context context){
        this.context=context;

        //получить из sqlLite данные пользователя и компании
        UserDataRecovery userDataRecovery = new UserDataRecovery();
        userDataModel = userDataRecovery.getUserDataRecovery(this.context);

        String url = API;
        url += "partner_role_receive";
        url += "&" + "counterparty_tax_id=" + userDataModel.getCompany_tax_id();//MY_COMPANY_TAXPAYER_ID;

        //получить результат от сервера
        setInitialData(url);

    }

    private void setInitialData(String url) {         //получаем данные из сервера б/д.

        InitialData task=new InitialData(){

            protected void onPostExecute(String result) {
                splitResult(result);
            }
        };
        task.execute(url);
    }
    private void splitResult(String result){            //новый лист с данными из сервера б/д
        PARTNER_ROLE_LIST.clear();
        ROLE_PARTNER_TEST = result;
        String partner_role_all = "";
        try {
            String[] res = result.split("<br>");
            for (int i = 0; i < res.length; i++) {
                PARTNER_ROLE_LIST.add(res[i]);
                partner_role_all += res[i]+"&nbsp";
            }
            writeDataCompanyRoleInTable(partner_role_all);
            //test = partner_role_all;
        }catch (Exception ex){
            PARTNER_ROLE_LIST.add("null");
        }

    }
    private void  writeDataCompanyRoleInTable(String partner_role_all){
        UserDataRecovery userDataRecovery = new UserDataRecovery();
        UserModel userDataModel = userDataRecovery.getUserDataRecovery(this.context);

        String roleOld = userDataModel.getPartner_role_list();
        String roleNew = partner_role_all;

        try {
            /*if (roleOld == null && !roleNew.isEmpty()) {//!roleOld.equals(roleNew)
                 goWrite(roleOld, roleNew);
                Toast.makeText(context, "hi1", Toast.LENGTH_SHORT).show();
            } else */
                if (!roleOld.equals(roleNew)) {
                goWrite(roleOld, roleNew);
            }
        }catch (Exception ex) {
            Toast.makeText(context, "ex: " + ex, Toast.LENGTH_SHORT).show();
        }
    }
    private void goWrite(String roleOld, String roleNew ){
        my_db = new HelperDB(this.context);
        sqdb = my_db.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(my_db.PARTNER_ROLE, roleNew);
        String[] forUpdate = new String[]{roleOld};

        sqdb.update(HelperDB.TABLE_NAME, cv,
                HelperDB.PARTNER_ROLE + "=?", forUpdate);
        sqdb.close();
    }

}
