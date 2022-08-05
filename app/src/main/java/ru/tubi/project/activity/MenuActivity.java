package ru.tubi.project.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import ru.tubi.project.R;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.utilites.GetColorShopingBox;
import ru.tubi.project.utilites.HelperDB;

import ru.tubi.project.utilites.Constant;
import ru.tubi.project.utilites.SearchOrder_id;
import ru.tubi.project.utilites.UserDataRecovery;

import static ru.tubi.project.activity.Config.MY_ABBREVIATION;
import static ru.tubi.project.activity.Config.MY_COMPANY_TAXPAYER_ID;
import static ru.tubi.project.activity.Config.MY_NAME;
import static ru.tubi.project.activity.Config.MY_NAME_COMPANY;
//import static com.example.tubi.Config.MY_TAXPAYER_ID;
import static ru.tubi.project.activity.Config.MY_UID;
import static ru.tubi.project.activity.Config.ORDER_ID;
import static ru.tubi.project.free.AllCollor.alert_dialog_button_green_pressed;
import static ru.tubi.project.free.AllText.LAST_ORDERS_TEXT;
import static ru.tubi.project.free.AllText.MES_5;
import static ru.tubi.project.free.AllText.MY_PERSONALY_OFFICE_TEXT;
import static ru.tubi.project.free.AllText.THIS_ACTION_IS_NOT_AVALABLE_TO_AGENT;
import static ru.tubi.project.free.AllText.TO_BE_PROVIDER_SIMPLE_TEXT;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    private Intent intent;
    private LinearLayout llMyProfile, llMyCompany, llMyOrderHistory,llWantToBeProvider,
            llQuestionAnsver, llPrivacyPolicy;
    private TextView tvMyPersonalyOffice,tvOrderHistory;
    private AlertDialog.Builder adb;
    private AlertDialog ad;
    private SQLiteDatabase sqdb;
    private HelperDB my_db = new HelperDB(this);
    private UserModel userDataModel;
    private UserDataRecovery userDataRecovery = new UserDataRecovery();

    private int myOrder_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setTitle(R.string.menu_menu);

        llMyProfile = findViewById(R.id.llMyProfile);
        llMyCompany=findViewById(R.id.llMyCompany);
        llMyOrderHistory=findViewById(R.id.llMyOrderHistory);
        llQuestionAnsver=findViewById(R.id.llQuestionAnsver);
        llWantToBeProvider=findViewById(R.id.llWantToBeProvider);
        llPrivacyPolicy=findViewById(R.id.llPrivacyPolicy);
        tvMyPersonalyOffice=findViewById(R.id.tvMyPersonalyOffice);
        tvOrderHistory=findViewById(R.id.tvOrderHistory);

        llMyProfile.setOnClickListener(this);
        llMyCompany.setOnClickListener(this);
        llMyOrderHistory.setOnClickListener(this);
        llQuestionAnsver.setOnClickListener(this);
        llWantToBeProvider.setOnClickListener(this);
        llPrivacyPolicy.setOnClickListener(this);

        //получить из sqlLite данные пользователя и компании
        userDataModel = userDataRecovery.getUserDataRecovery(this);

        tvMyPersonalyOffice.setText(""+MY_PERSONALY_OFFICE_TEXT);
        tvOrderHistory.setText(""+LAST_ORDERS_TEXT);

        invalidateOptionsMenu();
    }
    // удалить пользователя из таблицы SQLlite
    public void logoutUser(View view) {
      /*  SQLiteDatabase db = my_db.getWritableDatabase();
        // Delete All Rows
        db.delete("my_user", null, null);
        db.close();*/
        my_db.deleteUsers();

        MY_UID = null;
        MY_NAME = null;
        MY_ABBREVIATION = null;
        MY_NAME_COMPANY = null;
        MY_COMPANY_TAXPAYER_ID = null;
        ORDER_ID = 0;
        // Запуск действия по входу в систему
        Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onClick(View v) {
        //если роль агент продаж то запретить иметь компанию,
        // у него нет своих заказов, и поставщиком он не может стать
        if(userDataModel.getRole().equals("sales_agent")){
            if(v.equals(llMyCompany) || v.equals(llMyOrderHistory)
                    || v.equals(llWantToBeProvider)){
                Toast.makeText(this, ""+THIS_ACTION_IS_NOT_AVALABLE_TO_AGENT, Toast.LENGTH_LONG).show();
                return;
            }
        }

        if(v.equals(llMyProfile)){
            Intent intent = new Intent(this, ProfileUserActivity.class);
            startActivity(intent);
            //Toast.makeText(this, "A:", Toast.LENGTH_SHORT).show();
        }else if(v.equals(llMyCompany)){
            Intent intent = new Intent(this, ProfileCompanyActivity.class);
            startActivity(intent);
        }else if(v.equals(llMyOrderHistory)){
            Intent intent = new Intent(this, OrderHistoryActivity.class);
            startActivity(intent);
        }else if(v.equals(llWantToBeProvider)){
            //показать пояснения и перейти в активити для оформления
            alertDialogToBeProvider();
        }
        else if(v.equals(llQuestionAnsver)){

            UserDataRecovery userDataRecovery = new UserDataRecovery();
            UserModel userDataModel = userDataRecovery.getUserDataRecovery(this);

            SearchOrder_id searchOrder_id = new SearchOrder_id();
            searchOrder_id.searchStartedOrder(this);
         /*   String url = SEARCH_MY_ACTIVE_ORDER;
            url += "&" + "user_uid=" + userDataModel.getUid();
            InitialData task=new InitialData(){
                protected void onPostExecute(String result) {
                    myOrder_id = Integer.parseInt(result);
                    Toast.makeText(MenuActivity.this, "myOrder_id: "+myOrder_id, Toast.LENGTH_SHORT).show();
                }
            };
            task.execute(url);*/

        alertDialogTempoBuyerInfo(userDataModel);//

        }else if(v.equals(llPrivacyPolicy)){
            //показать политику конфедециальности
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.PRIVACY_POLICY_URL)));
        }

    }

    private void alertDialogTempoBuyerInfo(UserModel userDataModel){//
        adb = new AlertDialog.Builder(this);
        String companyRole = "";
        for(int i = 0; i < Config.PARTNER_ROLE_LIST.size(); i++){
            companyRole += Config.PARTNER_ROLE_LIST.get(i)+"\n";
        }
        String st1 = "Раздел вопросы / ответы";
        String st = "находится в разработке\n приносим свои извенения";

        String res = st;
        adb.setTitle(st1);
        adb.setMessage(res);

        adb.setPositiveButton("понятно", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //showProfileCompanyActivity();
                ad.cancel();
            }
        });
        ad=adb.create();
        ad.show();
    }
    private void alertDialogToBeProvider() {
        adb = new AlertDialog.Builder(this);

        String st1 = TO_BE_PROVIDER_SIMPLE_TEXT;
        String st2 = MES_5;
        adb.setTitle(st1);
        adb.setMessage(st2);

        adb.setPositiveButton("понятно", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //перейти в моя компания
               showProfileCompanyActivity();
            }
        });
        ad=adb.create();
        ad.setCanceledOnTouchOutside(false);
        ad.setCancelable(false);
        ad.show();
        Button buttonbackground1 = ad.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonbackground1.setBackgroundColor(alert_dialog_button_green_pressed);
        buttonbackground1.setTextColor(Color.WHITE);
    }
    //перейти в моя компания
    private void showProfileCompanyActivity() {
        Intent intent = new Intent(this,ProfileCompanyActivity.class);
        startActivity(intent);
    }



    @Override
    public boolean onPrepareOptionsMenu(Menu menu) { //----invalidateOptionsMenu();
        GetColorShopingBox gc = new GetColorShopingBox();
        menu = gc.color(this, menu);
       /* if(OPEN_ORDER_CONDITION == true){
            menu.findItem(R.id.shoping_box).setIcon(R.drawable.soping_box_green_60ps);
        }*/
        return super.onPrepareOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int itemID=item.getItemId();
        if(itemID==R.id.menu){
            intent=new Intent(this, MenuActivity.class);
            startActivity(intent);
        }
        if(itemID==R.id.main){
            intent=new Intent(this,MainActivity.class);
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
// Проверьте, вошел ли пользователь уже в систему или нет
   /* private String goReadUid(){
        String string="";
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

            c.moveToFirst();
            String uid = c.getString(col0);
            String myName = c.getString(col1);
            String abbreviation = c.getString(col2);
            String counterparty = c.getString(col3);
            String tax_id = c.getString(col4);
            String user_role = c.getString(col5);
            sqdb.close();

        string = "MY_NAME: "+ myName+"\nUID: "+ uid
                +"\nabb: "+ abbreviation+"\nc_name: "+ counterparty
                +"\ntax-id: "+ tax_id+"\nrole: "+ user_role;
        }
        return string;
    }*/