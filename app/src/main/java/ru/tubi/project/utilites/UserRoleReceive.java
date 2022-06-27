package ru.tubi.project.utilites;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import ru.tubi.project.models.UserModel;

import static ru.tubi.project.Config.ROLE;
import static ru.tubi.project.utilites.Constant.USER_ROLE_RECEIVE;
import static ru.tubi.project.Config.MY_UID;
//import static com.example.tubi.MainActivity.test;

public class UserRoleReceive {

    private String res, DBresult;
    private SQLiteDatabase sqdb;
    private HelperDB my_db;
    private Context context;
    private UserModel userDataModel;
    //String messege = CHECK_CONNECT_INTERNET;

    public void RoleReceive(Context context){
        this.context=context;

        //получить из sqlLite данные пользователя и компании
        UserDataRecovery userDataRecovery = new UserDataRecovery();
        userDataModel = userDataRecovery.getUserDataRecovery(this.context);

        String url = USER_ROLE_RECEIVE;
        url += "&" + "user_uid=" + userDataModel.getUid();
        // url += "&" + "user_uid=" + MY_UID;

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
        try {
            String[] strResult = result.split("<br>");
            for (int i = 0; i < strResult.length; i++) {
                String[] res = strResult[i].split("&nbsp");

                if (res[0].equals("RESULT_OK")) {
                    DBresult = res[1];
                    //записать роль в таблицу DB приложения
                    writeDataRoleInTable_my_user(res[1]);
                } else {
                    return;
                }
            }
        }catch(Exception ex){}
    }
    //записать роль в таблицу DB приложения
    private void writeDataRoleInTable_my_user(String res) {
        ROLE = res;//delete
        UserDataRecovery userDataRecovery = new UserDataRecovery();
        UserModel userDataModel = userDataRecovery.getUserDataRecovery(this.context);

        String roleOld = userDataModel.getRole();
        String roleNew = res;

        if(!roleOld.equals(roleNew)) {
            my_db = new HelperDB(this.context);
            sqdb = my_db.getWritableDatabase();
            ContentValues cv = new ContentValues();

            cv.put(my_db.USER_ROLE, roleNew);
            String[] forUpdate = new String[]{roleOld};

            sqdb.update(HelperDB.TABLE_NAME, cv,
                    HelperDB.USER_ROLE + "=?", forUpdate);
            sqdb.close();
        }
    }


}
