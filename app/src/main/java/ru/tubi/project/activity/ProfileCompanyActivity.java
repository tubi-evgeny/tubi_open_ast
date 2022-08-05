package ru.tubi.project.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import ru.tubi.project.R;
import ru.tubi.project.activity.company_my.WarehouseEditActivity;
import ru.tubi.project.activity.company_my.WarehouseCreateActivity;
import ru.tubi.project.adapters.ProfileCompanyWorehouseAdapter;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.models.WarehouseModel;
import ru.tubi.project.utilites.HelperDB;
import ru.tubi.project.utilites.InitialData;

import java.util.ArrayList;

import ru.tubi.project.utilites.Constant;
import ru.tubi.project.utilites.UserDataRecovery;

import static ru.tubi.project.activity.Config.MY_COMPANY_TAXPAYER_ID;
//import static com.example.tubi.Config.MY_TAXPAYER_ID;
import static ru.tubi.project.free.AllCollor.TUBI_GREY_200;
import static ru.tubi.project.free.AllCollor.alert_dialog_button_green_pressed;
import static ru.tubi.project.free.AllText.BECOME_AN_ARTNER_TEMPO;
import static ru.tubi.project.free.AllText.DATE_ABOUT_COMPANY_MISSING;
import static ru.tubi.project.free.AllText.IMPORTANT_TEXT;
import static ru.tubi.project.free.AllText.I_UNDERSTAND_SMOL;
import static ru.tubi.project.free.AllText.MES_13;
import static ru.tubi.project.free.AllText.MES_14;
import static ru.tubi.project.free.AllText.MES_15;
import static ru.tubi.project.free.AllText.MES_16;
import static ru.tubi.project.free.AllText.MES_17;
import static ru.tubi.project.free.AllText.MES_4;
import static ru.tubi.project.free.AllText.MES_6;
import static ru.tubi.project.free.AllText.PARTNER_TUBI;
import static ru.tubi.project.free.AllText.PARTNER_WAREHOUSE_ROLE_WILL_BE_VERIFIED_TEXT;
import static ru.tubi.project.free.AllText.PARTNER_WAREHOUSE_TEXT;
import static ru.tubi.project.free.AllText.PERFORM;
import static ru.tubi.project.free.AllText.PROFILE_COMPANY;
import static ru.tubi.project.free.AllText.PROVIDER_ROLE_WILL_BE_VERIFIED_TEXT;
import static ru.tubi.project.free.AllText.PROVIDER_TEXT;
import static ru.tubi.project.free.AllText.PROVIDER_TO_MODERATION_TEXT;
import static ru.tubi.project.free.AllText.PROVIDER_WAREHOUSE_TEXT;
import static ru.tubi.project.free.AllText.RETURN_BIG;
import static ru.tubi.project.free.AllText.TAX_ID;
import static ru.tubi.project.free.AllText.UNDERSTOOD_BIG;
import static ru.tubi.project.free.AllText.WHAT_MEAN_BECOME_AGENT;
import static ru.tubi.project.free.AllText.YOUR_CONTRACT_ISNOT_SIGNATURE;

public class ProfileCompanyActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvCompanyName,tvCompanyGeneralInfo,tvProvider,tvPartnerWarehouse;
    private ImageView   ivEditCompany,ivCounterpartyRoleStatus,ivRolePartnerWarehouse,
            ivQuestionPartnerWarehouse;

    private LinearLayout llAddCounterpartyRole,llAddRolePartnerWarehouse,llCreateWarehouse;
    private SQLiteDatabase sqdb;
    private HelperDB my_db;
    private AlertDialog.Builder adb;
    private AlertDialog ad;
    private ArrayList<WarehouseModel> listAllWarehouse = new ArrayList<>();
   // WarehouseModel warehouse;
    private String abbreviation,counterparty,taxpayer_id;
    private boolean providerFlag = false, partnerFlag=false;
   // private ListView lvWarehouse;
    private RecyclerView recyclerView;
    private ProfileCompanyWorehouseAdapter adapter;
    private Intent intent;
    private String warehouseTipe = "", warehouseActive = "";
    private UserModel userDataModel;
    private UserDataRecovery userDataRecovery = new UserDataRecovery();
    private static final int RETURN_COMPANY_DATE = 3;
    private static final int RETURN_COMPANY_WAREHOUSE = 4;
    private static final int RETURN_EDIT_WAREHOUSE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_company);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        setTitle(null);
        actionBar.setSubtitle(PROFILE_COMPANY);//Профиль компании

        tvCompanyName=findViewById(R.id.tvCompanyName);
        tvCompanyGeneralInfo=findViewById(R.id.tvCompanyGeneralInfo);
        tvProvider=findViewById(R.id.tvProvider);
        tvPartnerWarehouse=findViewById(R.id.tvPartnerWarehouse);
        ivEditCompany=findViewById(R.id.ivEditCompany);
        ivCounterpartyRoleStatus=findViewById(R.id.ivCounterpartyRoleStatus);
        ivRolePartnerWarehouse=findViewById(R.id.ivRolePartnerWarehouse);
        ivQuestionPartnerWarehouse=findViewById(R.id.ivQuestionPartnerWarehouse);
        llAddCounterpartyRole=findViewById(R.id.llAddCounterpartyRole);
        llAddRolePartnerWarehouse=findViewById(R.id.llAddRolePartnerWarehouse);
        llCreateWarehouse=findViewById(R.id.llCreateWarehouse);

        recyclerView=(RecyclerView)findViewById(R.id.rvList);

        tvProvider.setOnClickListener(this);
        tvPartnerWarehouse.setOnClickListener(this);
        ivEditCompany.setOnClickListener(this);
        llAddCounterpartyRole.setOnClickListener(this);
        llAddRolePartnerWarehouse.setOnClickListener(this);
        llCreateWarehouse.setOnClickListener(this);
        ivQuestionPartnerWarehouse.setOnClickListener(this);

        tvProvider.setClickable(false);
        tvPartnerWarehouse.setClickable(false);

        //получить информацию о компании
        receiveUserGeneralInfo();

        //если данные о компании есть то отключить редактирование
        //проверить есть ли данные о компании
        checkDateCompanyDontEmpty();

        //проверить запросы на присвоение ролей
        checkPartnerProviderRole();
        checkPartnerWarehouseRole();
        //получить список складов компании
        receiveAllWarehose();

        //получить из sqlLite данные пользователя и компании
        userDataModel = userDataRecovery.getUserDataRecovery(this);
       // checkRole();

        ProfileCompanyWorehouseAdapter.RecyclerViewClickListener clickListener =
                new ProfileCompanyWorehouseAdapter.RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        whatButtonClicked(view,position);
                       // Toast.makeText(ProfileCompanyActivity.this, "A: "+position, Toast.LENGTH_SHORT).show();
                    }
                };

        adapter=new ProfileCompanyWorehouseAdapter(this,listAllWarehouse,clickListener);

    }
    private void whatButtonClicked(View view,int position){
        String st = String.valueOf(view);
        String [] clickName = st.split("/");
        if(clickName[1].equals("ivWarehouseEdit}")){
            goEditWarehouse(position);
        }else  if(clickName[1].equals("llBuyerWarehouse}")){
            Toast.makeText(this, ""+MES_13, Toast.LENGTH_SHORT).show();
        }else  if(clickName[1].equals("ivCheckProviderWarhouse}")){
            if(providerFlag){
                adEditProwiderWarehouse(position);
            }else Toast.makeText(this, ""+YOUR_CONTRACT_ISNOT_SIGNATURE, Toast.LENGTH_SHORT).show();
        }else  if(clickName[1].equals("ivCheckPartnerWarehouse}")){
            if(partnerFlag){
                alertDialogEditPartnerWarehouse(position);
            }else Toast.makeText(this, ""+YOUR_CONTRACT_ISNOT_SIGNATURE, Toast.LENGTH_SHORT).show();
        }
    }
    private void createTipeWarehouse(String tipe, int position ,String active){
       // int warehouse_id = listAllWarehouse.get(position).getWarehouse_id();
        int warehouse_info_id = listAllWarehouse.get(position).getWarehouse_info_id();
        String url = Constant.PROVIDER_OFFICE;
        url+= "create_tipe_warehouse";
        url += "&"+"warehouse_info_id="+warehouse_info_id;
        url += "&"+"warehouse_tipe="+tipe;
        url += "&"+"active="+active;
        String whatQuestion = "create_tipe_warehouse";
        setInitialData(url,whatQuestion);
    }
    private void goEditWarehouse(int position){
        //int warehouse_id = listAllWarehouse.get(position).getWarehouse_id();
        intent = new Intent(this, WarehouseEditActivity.class);
        intent.putExtra("warehouse_info",listAllWarehouse.get(position));
        intent.putExtra("providerFlag",providerFlag);
        intent.putExtra("partnerFlag",partnerFlag);
        startActivityForResult(intent, RETURN_EDIT_WAREHOUSE);
    }

    private void checkPartnerProviderRole(){
        String url = Constant.ADMIN_OFFICE;
        url += "check_partner_provider_role";
        url += "&" +"counterparty_tax_id="+MY_COMPANY_TAXPAYER_ID;
        url += "&"+"role="+"provider_business";
        String whatQuestion ="check_partner_provider_role";
        setInitialData(url,whatQuestion);
        Log.d("A111","ProfileCompanyActivity / checkPartnerProviderRole / url="+url);
    }
    private void checkPartnerWarehouseRole() {
        String url = Constant.ADMIN_OFFICE;
        url += "check_partner_warehouse_role";
        url += "&" +"counterparty_tax_id="+MY_COMPANY_TAXPAYER_ID;
        url += "&"+"role="+"partner_warehouse";
        String whatQuestion ="check_partner_warehouse_role";
        setInitialData(url,whatQuestion);
    }
    private void receiveAllWarehose() {
        String url = Constant.PROVIDER_OFFICE;
        url += "receive_all_my_warehouse";
        url += "&" +"counterparty_tax_id="+MY_COMPANY_TAXPAYER_ID;
        String whatQuestion ="receive_all_my_warehouse";
        setInitialData(url,whatQuestion);
    }
    private void setInitialData(String url_get, String whatQuestion) {
        InitialData task=new InitialData(){

            protected void onPostExecute(String result) {
                //Do your thing
                if(whatQuestion.equals("check_partner_warehouse_role")){
                    splitResult(result,whatQuestion);
                }else if(whatQuestion.equals("check_partner_provider_role")){
                    splitResult(result,whatQuestion);
                }
                else if(whatQuestion.equals("receive_all_my_warehouse")){
                    splitAllWarehouseResult(result);
                }
                else if(whatQuestion.equals("create_tipe_warehouse")){
                    warehouseListRestart();
                    // Toast.makeText(ProfileCompanyActivity.this, "resultDB: "+result, Toast.LENGTH_SHORT).show();
                }
            }
        };
        task.execute(url_get);
    }
    private void warehouseListRestart(){
       receiveAllWarehose();
    }
    private void splitAllWarehouseResult(String result){
        listAllWarehouse.clear();
        String [] res=result.split("<br>");
        String[]temp = res[0].split("&nbsp");
        if(temp[0].equals("error") || temp[0].equals("messege")){
            Toast.makeText(this, ""+temp[1], Toast.LENGTH_LONG).show();
        }else if(!result.isEmpty()){
            try{
                for(int i=0;i < res.length;i++){
                    temp = res[i].split("&nbsp");
                    int warehouse_info_id=Integer.parseInt(temp[0]);
                    String region=temp[1];
                    String district=temp[2];
                    String city =temp[3];
                    String street=temp[4];
                    int house=Integer.parseInt(temp[5]);
                    String building=temp[6];
                    String signboard=temp[7];
                    String active=temp[8];
                    String created_at=temp[9];
                    boolean providerWarehouse=Boolean.valueOf(temp[10].equals("1"));
                    boolean partnerWarehouse =Boolean.valueOf(temp[11].equals("1"));
                    int warStorageNum=Integer.parseInt(temp[12]);
                    int warProviderNum=Integer.parseInt(temp[13]);
                    int warPartnerNum=Integer.parseInt(temp[14]);
                    boolean providerRole=providerFlag;
                    boolean partnerRole=partnerFlag;
                        WarehouseModel warehouse = new WarehouseModel(warehouse_info_id,region,
                                district,city, street,house,building,signboard,active,created_at,
                                providerWarehouse,partnerWarehouse,warStorageNum,warProviderNum,
                                warPartnerNum, providerRole,partnerRole);

                        listAllWarehouse.add(warehouse);
                }
            }catch(Exception ex){
                Toast.makeText(this, "ex: "+ex, Toast.LENGTH_SHORT).show();
            }
        }
       // Toast.makeText(this, "res: "+listAllWarehouse.get(0).isPartnerWarehouse(), Toast.LENGTH_SHORT).show();
        recyclerView.setAdapter(adapter);
    }
    // разобрать результат с сервера
    private void splitResult(String result, String whatContract){
        Log.d("A111","ProfileCompanyActivity / splitResult / result ="+result);

       try {
           String[] res = result.split("<br>");
           String[] temp = res[0].split("&nbsp");
           if (temp[0].equals("RESULT_OK")) {
               //компании роль на рассмотрении
               int contractActive = Integer.parseInt(temp[1]);
               makeCheckedColor(contractActive, whatContract);
           } else if (temp[0].equals("RESULT_NO")) {

           } else if (temp[0].equals("error")) {
               Toast.makeText(this, "" + temp[1], Toast.LENGTH_LONG).show();
           }/* else {
               Toast.makeText(this, "" + CHECK_CONNECT_INTERNET, Toast.LENGTH_LONG).show();
           }*/
       }catch (Exception ex){
           Log.d("A111","ERROR / ProfileCompanyActivity / splitResult / ex="+ex);
       }
    }
    private void makeCheckedColor(int contractActive,String whatContract){
        if(whatContract.equals("check_partner_warehouse_role")){
            //проверить роль партнера склада если запрос отправлен то закрыть кнопку
            //и сменить галочку на желтую
            if(contractActive == 0){
                ivRolePartnerWarehouse.setImageResource(R.drawable.checkmark_yellow_140ps);
                llAddRolePartnerWarehouse.setClickable(false);
                tvPartnerWarehouse.setClickable(true);
                tvPartnerWarehouse.setText(""+BECOME_AN_ARTNER_TEMPO);

            }
            //проверить роль
            if(contractActive == 1){
                tvPartnerWarehouse.setText(""+PARTNER_TUBI);
                llAddRolePartnerWarehouse.setClickable(false);
                tvPartnerWarehouse.setClickable(false);
                ivRolePartnerWarehouse.setImageResource(R.drawable.checkmark_green_140ps);
                partnerFlag=true;
            }
        }else if(whatContract.equals("check_partner_provider_role")){
            //проверить роль поставщика если запрос отправлен то закрыть кнопку
            //и сменить галочку на желтую
            if(contractActive == 0){
                ivCounterpartyRoleStatus.setImageResource(R.drawable.checkmark_yellow_140ps);
                llAddCounterpartyRole.setClickable(false);
                tvProvider.setClickable(true);
                tvProvider.setText(""+PROVIDER_TO_MODERATION_TEXT);
                //сохранить результат временная роль в SQLlite
                // (сделать позже, после перезагрузки данные стираются)

            }
            //проверить роль
            if(contractActive == 1){
                tvProvider.setText(""+PROVIDER_TEXT);
                llAddCounterpartyRole.setClickable(false);
                tvProvider.setClickable(false);
                ivCounterpartyRoleStatus.setImageResource(R.drawable.checkmark_green_140ps);
                providerFlag = true;
            }
        }
       // adapter.notifyDataSetChanged();
    }

    //проверить есть ли данные о компании
    private boolean checkDateCompanyDontEmpty(){
        if(!counterparty.equals("")){
            ivEditCompany.setVisibility(View.GONE);
            return true;
        }else return false;
    }
    //заполнить activity
    private void fillActivity() {
        tvCompanyName.setText(""+abbreviation+" "+counterparty);
        tvCompanyGeneralInfo.setText(""+TAX_ID+" "+taxpayer_id);
    }
    //получить информацию о company
    private void receiveUserGeneralInfo() {
        my_db = new HelperDB(this);
        sqdb = my_db.getWritableDatabase();

        Cursor c = sqdb.query(HelperDB.TABLE_NAME, null, null,
                null, null, null, null);

        int col2 = c.getColumnIndex(HelperDB.ABBREVIATION);
        int col3 = c.getColumnIndex(HelperDB.COUNTERPARTY);
        int col4 = c.getColumnIndex(HelperDB.TAXPAYER_ID);

        c.moveToFirst();
        abbreviation = c.getString(col2);
        counterparty = c.getString(col3);
        taxpayer_id = c.getString(col4);
        sqdb.close();
        //заполнить activity
        fillActivity();
    }

    @Override
    public void onClick(View v) {
        if(v.equals(ivEditCompany)){
            //введите данные компании
            enterCompanyDate();
        }
        else if(v.equals(llAddCounterpartyRole)){
            //проверить есть ли данные о компании
            if(checkDateCompanyDontEmpty()){
                //данные о компании есть //сообщение о дальнейших действиях
                allertDialog("provider");
            }else {
                //если данных о компании нет  //введите данные компании
                enterCompanyDate();
            }
        }else if(v.equals(llAddRolePartnerWarehouse)){
            //проверить есть ли данные о компании
            if(checkDateCompanyDontEmpty()){
                //данные о компании есть //сообщение о дальнейших действиях
                allertDialog("partnerWarehouse");
            }else {
                //если данных о компании нет  //введите данные компании
                enterCompanyDate();
            }
        }else if(v.equals(tvProvider)){
            Toast.makeText(this, ""+PROVIDER_ROLE_WILL_BE_VERIFIED_TEXT, Toast.LENGTH_LONG).show();
        }
        else if(v.equals(tvPartnerWarehouse)){
            Toast.makeText(this, ""+PARTNER_WAREHOUSE_ROLE_WILL_BE_VERIFIED_TEXT, Toast.LENGTH_LONG).show();
        }
        else if(v.equals(ivQuestionPartnerWarehouse)){
            allertDialogQuestion();
        }
        else if(v.equals(llCreateWarehouse)){
            if(userDataModel.getCompany_tax_id() != 0) {
                Intent intent = new Intent(this, WarehouseCreateActivity.class);
                intent.putExtra("providerFlag", providerFlag);
                intent.putExtra("partnerFlag", partnerFlag);
                startActivityForResult(intent, RETURN_COMPANY_WAREHOUSE);
            }else{
                Toast.makeText(this, ""+DATE_ABOUT_COMPANY_MISSING, Toast.LENGTH_LONG).show();
            }
        }
    }

    //показать договор с поставщиком для ознакомления
    private void showContractProviderActivity(String string){
        Intent intent= new Intent(this, ContractFormActivity.class);
        intent.putExtra("show_contract",string);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RETURN_COMPANY_DATE && resultCode == RESULT_OK ){
            //получить(обнoвить) информацию о  company
            receiveUserGeneralInfo();
            //если данные о компании есть то отключить редактирование
            //проверить есть ли данные о компании
            checkDateCompanyDontEmpty();
        }else if(requestCode == RETURN_COMPANY_WAREHOUSE && resultCode == RESULT_OK){
            receiveAllWarehose();
        }else if(requestCode == RETURN_EDIT_WAREHOUSE && resultCode == RESULT_OK){
            receiveAllWarehose();
        }
    }
    //введите данные компании
    private void enterCompanyDate(){
        Intent intent = new Intent(this,CompanyDateFormActivity.class);
        //intent.putExtra("message",MES_1_PROFILE);
        startActivityForResult(intent, RETURN_COMPANY_DATE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,MenuActivity.class);
        startActivity(intent);
        finish();
    }

    private void adEditProwiderWarehouse(int position){
        boolean providerWarehouseFlag = listAllWarehouse.get(position).isProviderWarehouse();
        adb = new AlertDialog.Builder(this);
        String st1 = PROVIDER_WAREHOUSE_TEXT;
        String Warehouse_info = listAllWarehouse.get(position).toString();
        String st2 = Warehouse_info+"\n\n"+MES_16;
        String st3 = Warehouse_info+"\n\n"+MES_17;
        adb.setTitle(st1);
        if(providerWarehouseFlag==false){
            adb.setMessage(st2);
            warehouseTipe = "provider";
            warehouseActive = "1";
        }else {
            adb.setMessage(st3);
            warehouseTipe = "provider";
            warehouseActive = "0";
        }

        adb.setPositiveButton(PERFORM, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                createTipeWarehouse(warehouseTipe, position,warehouseActive);
                //Toast.makeText(ProfileCompanyActivity.this, "test", Toast.LENGTH_SHORT).show();
            }
        });
        adb.setNeutralButton(RETURN_BIG, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ad.cancel();
            }
        });

        ad=adb.create();
        ad.setCanceledOnTouchOutside(false);
        ad.setCancelable(false);
        ad.show();

        Button buttonbackground1 = ad.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonbackground1.setBackgroundColor(alert_dialog_button_green_pressed);
        buttonbackground1.setTextColor(Color.WHITE);
        Button buttonbackground2 = ad.getButton(DialogInterface.BUTTON_NEUTRAL);
        buttonbackground2.setBackgroundColor(TUBI_GREY_200);
        buttonbackground2.setTextColor(Color.WHITE);
    }
    private void alertDialogEditPartnerWarehouse(int position){
        boolean partnerWarehouseFlag = listAllWarehouse.get(position).isPartnerWarehouse();
        adb = new AlertDialog.Builder(this);
        String st1 = PARTNER_WAREHOUSE_TEXT;
        String Warehouse_info = listAllWarehouse.get(position).toString();
        String st2 = Warehouse_info+"\n\n"+MES_14;
        String st3 = Warehouse_info+"\n\n"+MES_15;
        adb.setTitle(st1);
        if(partnerWarehouseFlag==false){
            adb.setMessage(st2);
            warehouseTipe = "partner";
            warehouseActive = "1";
        }else {
            adb.setMessage(st3);
            warehouseTipe = "partner";
            warehouseActive = "0";
        }

        adb.setPositiveButton(PERFORM, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                createTipeWarehouse(warehouseTipe, position,warehouseActive);
                //Toast.makeText(ProfileCompanyActivity.this, "test", Toast.LENGTH_SHORT).show();
            }
        });
        adb.setNeutralButton(RETURN_BIG, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ad.cancel();
            }
        });

        ad=adb.create();
        ad.setCanceledOnTouchOutside(false);
        ad.setCancelable(false);
        ad.show();

        Button buttonbackground1 = ad.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonbackground1.setBackgroundColor(alert_dialog_button_green_pressed);
        buttonbackground1.setTextColor(Color.WHITE);
        Button buttonbackground2 = ad.getButton(DialogInterface.BUTTON_NEUTRAL);
        buttonbackground2.setBackgroundColor(TUBI_GREY_200);
        buttonbackground2.setTextColor(Color.WHITE);
    }
    //сообщение о дальнейших действиях
    private void allertDialog(String contract) {
        adb = new AlertDialog.Builder(this);
        String st1 = IMPORTANT_TEXT;
        String st2 = MES_4;
        adb.setTitle(st1);
        adb.setMessage(st2);
        adb.setPositiveButton(UNDERSTOOD_BIG, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //показать договор с поставщиком для ознакомления
                showContractProviderActivity(contract);
                //showContractProviderActivity("provider");
            }
        });
        adb.setNeutralButton(RETURN_BIG, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ad.cancel();
            }
        });
        ad=adb.create();
        ad.setCanceledOnTouchOutside(false);
        ad.setCancelable(false);
        ad.show();
        Button buttonbackground1 = ad.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonbackground1.setBackgroundColor(alert_dialog_button_green_pressed);
        buttonbackground1.setTextColor(Color.WHITE);
        Button buttonbackground2 = ad.getButton(DialogInterface.BUTTON_NEUTRAL);
        buttonbackground2.setBackgroundColor(TUBI_GREY_200);
        buttonbackground2.setTextColor(Color.WHITE);
    }
    //сообщение разьяснение о предоставлении склада
    private void allertDialogQuestion() {
        adb = new AlertDialog.Builder(this);
        String st1 = WHAT_MEAN_BECOME_AGENT;
        String st2 = MES_6;
        adb.setTitle(st1);
        adb.setMessage(st2);
        adb.setPositiveButton(I_UNDERSTAND_SMOL, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //показать договор с поставщиком для ознакомления
                //showContractProviderActivity();
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

}

