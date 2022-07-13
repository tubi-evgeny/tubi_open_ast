package ru.tubi.project.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ru.tubi.project.Config;
import ru.tubi.project.R;

import ru.tubi.project.activity.AdminPanel.AdminActivity;
import ru.tubi.project.models.AddressModel;
import ru.tubi.project.models.OrderModel;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.utilites.Constant;
import ru.tubi.project.utilites.HelperDB;
import ru.tubi.project.utilites.InitialData;
import ru.tubi.project.utilites.OrderDataRecoveryUtil;
import ru.tubi.project.utilites.PartnerRoleReceive;
import ru.tubi.project.utilites.SearchOrder_id;
import ru.tubi.project.utilites.UserDataRecovery;
import ru.tubi.project.utilites.UserRoleReceive;

import ru.tubi.project.free.AllText;

import static ru.tubi.project.Config.MY_COMPANY_TAXPAYER_ID;
import static ru.tubi.project.Config.MY_UID;
import static ru.tubi.project.free.AllCollor.TUBI_GREY_200;
import static ru.tubi.project.free.AllText.ENTER_YOUR_CITY;
import static ru.tubi.project.free.AllText.I_UNDERSTAND_SMOL;
import static ru.tubi.project.free.AllText.LOAD_TEXT;
import static ru.tubi.project.free.VariablesHelpers.MY_CITY;
import static ru.tubi.project.free.VariablesHelpers.MY_REGION;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvName, tvMyCity;
    private Button btnCatalog, btnAdminActiv, btnMyCompany, btnMenu;
    private LinearLayout llMyCity;
    private ListView lvMyCity;
    private Intent intent;
    private String url, url_get;
    public static boolean FLAG_ORDER = false;
    private SearchOrder_id searchOrder_id = new SearchOrder_id();
    private HelperDB my_db = new HelperDB(this);
    private SQLiteDatabase sqdb;
    private UserModel userDataModel;
    private ArrayList<OrderModel> orderDataModelList = new ArrayList<>();
    private OrderDataRecoveryUtil orderDataRecoveryUtil = new OrderDataRecoveryUtil();
    private ArrayList<AddressModel> CITY_LIST = new ArrayList<>();
    private AlertDialog.Builder adb;
    private AlertDialog ad;
    private ArrayAdapter adapter;

    //public static String test = "test";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(AllText.MAIN_ACTIVITY);//ГЛАВНАЯ

        btnCatalog=findViewById(R.id.btnCatalog);
        btnMyCompany=findViewById(R.id.btnMyCompany);
        btnMenu=findViewById(R.id.btnMenu);
        tvMyCity=findViewById(R.id.tvMyCity);
        llMyCity=findViewById(R.id.llMyCity);
        lvMyCity=findViewById(R.id.lvMyCity);

        btnCatalog.setOnClickListener(this);
        btnMyCompany.setOnClickListener(this);
        btnMenu.setOnClickListener(this);
        llMyCity.setOnClickListener(this);
        //проверить роль user, получить роли партнера
        searchUserRole();

        //если есть открытый заказ то получить его номер или получить 0 если заказа открытого нет
        searchOrder_id.searchStartedOrder(this);
        //получить из sqlLite данные пользователя и компании
        UserDataRecovery userDataRecovery = new UserDataRecovery();
        userDataModel = userDataRecovery.getUserDataRecovery(this);

        //получить список заказав с характеристиками
        orderDataModelList = orderDataRecoveryUtil.getOrderDataRecovery(this);

        String order_id_string = "";
        for(int i=0;i < orderDataModelList.size();i++){
            order_id_string += orderDataModelList.get(i).getOrder_id();
            if(i != orderDataModelList.size()-1){
                order_id_string += ";";
            }
        }

        tvName = findViewById(R.id.tvName);
        btnAdminActiv = findViewById(R.id.btnAdminActiv);
        btnMyCompany = findViewById(R.id.btnMyCompany);

        if(!userDataModel.getRole().equals("admin")){
            btnAdminActiv.setVisibility(View.GONE);
        }

        String companyRole = "";
        for(int i = 0; i < Config.PARTNER_ROLE_LIST.size(); i++){
            companyRole += Config.PARTNER_ROLE_LIST.get(i)+"\n";
        }

        String activityName ="tubi_relise_2\n"+"phone: "+userDataModel.getPhone();
        tvName.setText("activity: "+activityName+"\n\nMY_NAME: "+ userDataModel.getName()//+"\nUID: "+ userDataModel.getUid()
                +"\nc_name: "+ userDataModel.getAbbreviation()+" "+ userDataModel.getCounterparty()
                +"\ntax-id: "+ userDataModel.getCompany_tax_id()+"\nrole: "+ userDataModel.getRole()
                +"\norder_id: "+ order_id_string+"\ncompany role:\n"+companyRole);

        AddressModel am1 = new AddressModel("Московская область", "Пушкинский район","Королев");
        AddressModel am2 = new AddressModel("Московская область", "Мытищенский район","Мытищи");
        AddressModel am3 = new AddressModel("Смоленская область", "Смоленский район","Смоленск");
        AddressModel am4 = new AddressModel("Московская область", "Мытищенский район","Другой город");
        CITY_LIST.add(am1);
        CITY_LIST.add(am2);
        CITY_LIST.add(am3);
        CITY_LIST.add(am4);

        if(MY_CITY.isEmpty()){
            lvMyCity.setVisibility(View.VISIBLE);
        }else{
            tvMyCity.setText(MY_CITY);
            lvMyCity.setVisibility(View.GONE);
        }
        //запуск метода обновить меню,
        // нужен для обновления цвета корзины если не пустая
        invalidateOptionsMenu();

        lvMyCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MY_CITY = CITY_LIST.get(position).getCity();
                MY_REGION = CITY_LIST.get(position).getRegion();
                tvMyCity.setText(MY_REGION+" "+MY_CITY);
                lvMyCity.setVisibility(View.GONE);
            }
        });

        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,CITY_LIST);
        lvMyCity.setAdapter(adapter);

    }
    @Override
    public void onClick(View v) {
        if(MY_CITY.isEmpty()){
            Toast.makeText(this, ""+ENTER_YOUR_CITY, Toast.LENGTH_SHORT).show();
            return;
        }
        if (v.equals(btnCatalog)) {
            Intent intent = new Intent(this, ActivityCatalog.class);
            startActivity(intent);
        }
        if (v.equals(btnMyCompany)) {
            Intent intent = new Intent(this, CompanyMyActivity.class);
            startActivity(intent);
        }
        else if(v.equals(btnMenu)){
            Intent intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
        }
        //показать список городов
        else if(v.equals(llMyCity)){
            lvMyCity.setVisibility(View.VISIBLE);
        }
    }
    //проверить роль user
    private void searchUserRole(){
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
            int col4 = c.getColumnIndex(HelperDB.TAXPAYER_ID);
            c.moveToFirst();
            String uid = c.getString(col0);
            String company_tax_id = c.getString(col4);
            if (!MY_UID.equals(uid)) {
                MY_UID = uid;
            }
            if (!MY_COMPANY_TAXPAYER_ID.equals(company_tax_id)) {
                MY_COMPANY_TAXPAYER_ID = company_tax_id;
            }
            //получить из sqlLite данные пользователя и компании
            //UserDataRecovery userDataRecovery = new UserDataRecovery();
            //userDataModel = userDataRecovery.getUserDataRecovery(this);

            //получить роли партнера
            PartnerRoleReceive partnerRoleReceive = new PartnerRoleReceive();
            partnerRoleReceive.RoleReceive(this);

            //Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            //проверить роль если изменилась заменить в sqlLite
            UserRoleReceive userRoleReceive = new UserRoleReceive();
            userRoleReceive.RoleReceive(this);
        }
    }
    //обновить корзину
    //этот метод запускает invalidateOptionsMenu();
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        //если есть открытый заказ то получить его номер или получить 0 если заказа открытого нет
        searchOrder_id.searchStartedOrder(this);

        if(userDataModel.getOrder_id() != 0){//Config.ORDER_ID
            menu.findItem(R.id.shoping_box).setIcon(R.drawable.soping_box_green_60ps);
        }
        return super.onPrepareOptionsMenu(menu);
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

    /*public void goMyCompany(View view) {
        Intent intent = new Intent(this, CompanyMyActivity.class);
        startActivity(intent);
    }*/

    //visible только для роли admin
    public void goAdminActiv(View view) {
        Intent intent = new Intent(this, AdminActivity.class);
        startActivity(intent);
    }
    //сохранить можно только если весь товар передан(с галочками)
    private void adMessegeForUser(String messege_name, String messege_text){
        adb = new AlertDialog.Builder(this);

        adb.setTitle(messege_name);
        adb.setMessage(messege_text);

        adb.setNeutralButton(I_UNDERSTAND_SMOL, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               // COUNT_SHOW_MESSEEGE ++;
                ad.cancel();
            }
        });

        ad=adb.create();
        ad.setCanceledOnTouchOutside(false);
        ad.setCancelable(false);
        ad.show();

        Button buttonbackground1 = ad.getButton(DialogInterface.BUTTON_NEUTRAL);
        buttonbackground1.setBackgroundColor(TUBI_GREY_200);
        buttonbackground1.setTextColor(Color.WHITE);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if(MY_CITY.isEmpty()){
            Toast.makeText(this, ""+ENTER_YOUR_CITY, Toast.LENGTH_SHORT).show();
        }
        else{
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
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //return;
    }


}