package ru.tubi.project.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import ru.tubi.project.R;
import ru.tubi.project.adapters.CatalogAdapter;
import ru.tubi.project.models.Catalog;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.utilites.GetColorShopingBox;
import ru.tubi.project.utilites.InitialDataPOST;
import ru.tubi.project.utilites.SearchOrder_id;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.tubi.project.utilites.UserDataRecovery;

import static ru.tubi.project.activity.Config.PARTNER_COMPANY_TAXPAYER_ID_FOR_AGENT;
import static ru.tubi.project.free.AllCollor.RED_600;
import static ru.tubi.project.free.AllCollor.alert_dialog_button_green_pressed;
import static ru.tubi.project.free.AllText.CHECK_CONNECT_INTERNET;
import static ru.tubi.project.free.AllText.LOAD_TEXT;
import static ru.tubi.project.free.AllText.MES_31;
import static ru.tubi.project.free.AllText.MES_32;
import static ru.tubi.project.free.VariablesHelpers.MY_CITY;
import static ru.tubi.project.free.VariablesHelpers.MY_REGION;
import static ru.tubi.project.free.VariablesHelpers.YOUR_ARE_EIGHTEEN;
import static ru.tubi.project.utilites.Constant.GET_CATALOG;
import static ru.tubi.project.utilites.InitialDataPOST.getParamsString;

public class ActivityCatalog extends AppCompatActivity implements SearchView.OnQueryTextListener{

    Intent intent;
    private RecyclerView recyclerView;
    private CatalogAdapter adapter;
    private ArrayList<String> list=new ArrayList<String>();
    private ArrayList<Catalog>listCatalog=new ArrayList<>();
    private String [] strResult;
    private SearchOrder_id searchOrder_id = new SearchOrder_id();
    private UserModel userDataModel;
    private AlertDialog.Builder adb;
    private AlertDialog ad;

    public static final int CATALOG_IS_MINE = 3;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        setTitle(R.string.title_catalog);

        //получить из sqlLite данные пользователя и компании
        UserDataRecovery userDataRecovery = new UserDataRecovery();
        userDataModel = userDataRecovery.getUserDataRecovery(this);

        startList();
       // setInitialData();
        recyclerView=(RecyclerView)findViewById(R.id.rvList);

        CatalogAdapter.RecyclerViewClickListener clickListener=
                new CatalogAdapter.RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        goCategoryProductActivity(position);
                    }
                };
        adapter=new CatalogAdapter(this,listCatalog,clickListener);

        recyclerView.setAdapter(adapter);

        //запуск метода обновить меню,
        // нужен для обновления цвета корзины если не пустая
        invalidateOptionsMenu();
    }
    private void goCategoryProductActivity(int position){
        String positionName=listCatalog.get(position).getCategoryName();
        if(!positionName.equals("Мой каталог")) {
            int catalog_id =listCatalog.get(position).getCatalog_id();
            //37 табак, 52 пиво, 53 алкоголь
            if(catalog_id == 37 || catalog_id == 52 || catalog_id == 53){
                //вам есть восемнадцать
                if(YOUR_ARE_EIGHTEEN == 0 ){
                    adMessForYongBoys(position);
                }else{
                    goCategoryActivity(position);
                }
            }else{
                goCategoryActivity(position);
            }
        }else{
            intent = new Intent(this, ActivityProduct.class);
            intent.putExtra("key",CATALOG_IS_MINE);
            startActivity(intent);
        }
    }
    private void goCategoryActivity(int position){
        String positionName=listCatalog.get(position).getCategoryName();
        intent = new Intent(this, ActivityCategory.class);
        intent.putExtra("position", position);//
        intent.putExtra("positionName", positionName);
        startActivity(intent);
    }
    //стартовый лист для запуска.
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void startList(){
        long tax_id = userDataModel.getCompany_tax_id();
        //проверить это агент продаж?
        if(userDataModel.getRole().equals("sales_agent")){
            tax_id = PARTNER_COMPANY_TAXPAYER_ID_FOR_AGENT;
            Log.d("A111","ActivityCatalog / startList / agent");
        }
        final Map<String, String> parameters = new HashMap<>();
        parameters.put("receive_catalog","");
        parameters.put("my_city", MY_CITY);
        parameters.put("my_region", MY_REGION);
        parameters.put("taxpayer_id", String.valueOf(tax_id));
        setInitialDataPOST(GET_CATALOG, parameters);
        Log.d("A111","ActivityCatalog / startList / url="+GET_CATALOG+parameters);
    }
    //получаем данные из сервера б/д
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setInitialDataPOST(String url, Map<String, String> param){
        ProgressDialog asyncDialog = new ProgressDialog(this);

        InitialDataPOST task = new InitialDataPOST(){
            @Override
            protected void onPreExecute() {
                asyncDialog.setMessage(LOAD_TEXT);
                asyncDialog.show();
                super.onPreExecute();
            }
            @Override
            protected void onPostExecute(String s) {
                splitResult(s);

                //скрыть диалоговое окно
                asyncDialog.dismiss();
            }
        };
        task.execute(url, getParamsString(param));
    }
    //получаем данные из сервера б/д.
  /*  private void setInitialData(String url) {
        ProgressDialog asyncDialog = new ProgressDialog(this);

        InitialData task=new InitialData(){
            @Override
            protected void onPreExecute() {
                asyncDialog.setMessage(LOAD_TEXT);
                asyncDialog.show();
                super.onPreExecute();
            }

            protected void onPostExecute(String result) {
                //Do your thing
                splitResult(result);

                //скрыть диалоговое окно
                asyncDialog.dismiss();
            }
        };
        task.execute(url);
    }*/
    //новый лист с данными из сервера б/д
    private void splitResult(String result){
        Log.d("A111",getClass()+" / splitResult / res="+result);
        String[] searchException= result.split(":");
        if(searchException[0].equals("Exception")){
            Toast.makeText(this, ""+CHECK_CONNECT_INTERNET, Toast.LENGTH_LONG).show();
            return;
        }
        String strCategory;
        String strImageURL;
        strResult=result.split("<br>");
        for(int i=0;i<strResult.length;i++){
            String[]twoString=strResult[i].split("&nbsp");
            strCategory=twoString[0];
            int catalog_id = Integer.parseInt(twoString[1]);
            if(twoString.length>2){
                strImageURL=twoString[2];
            }else {
                strImageURL="null";
            }
            Catalog category=new Catalog(strCategory,strImageURL,catalog_id);
            listCatalog.add(category);
        }

        adapter.notifyDataSetChanged();         //выводим новый список
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
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) { //----invalidateOptionsMenu();
        GetColorShopingBox gc = new GetColorShopingBox();
        menu = gc.color(this, menu);
        /*searchOrder_id.searchStartedOrder(this);
        if(userDataModel.getOrder_id() != 0){
            menu.findItem(R.id.shoping_box).setIcon(R.drawable.soping_box_green_60ps);
        }*/
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_and_search,menu);

        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int itemID=item.getItemId();
        if(itemID==R.id.menu){
            intent=new Intent(this,MenuActivity.class);
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

    private void adMessForYongBoys(int position){
        adb = new AlertDialog.Builder(this);
        String st1 = MES_31;
        String st2 = MES_32;
        adb.setTitle(st1);
        adb.setMessage(st2);
        adb.setPositiveButton("Мне исполнилось 18 лет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                YOUR_ARE_EIGHTEEN = 1;
                goCategoryActivity(position);
                ad.cancel();            }
        });
        ad=adb.create();
        ad.show();
        Button buttonbackground1 = ad.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonbackground1.setBackgroundColor(RED_600);
        buttonbackground1.setTextColor(Color.WHITE);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        intent=new Intent(this,ActivityProductCard.class);
        intent.putExtra("searchText",query.trim());
        startActivity(intent);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}