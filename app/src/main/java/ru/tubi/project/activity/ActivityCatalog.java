package ru.tubi.project.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import ru.tubi.project.R;
import ru.tubi.project.adapters.CatalogAdapter;
import ru.tubi.project.models.Catalog;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.utilites.InitialData;
import ru.tubi.project.utilites.SearchOrder_id;

import java.util.ArrayList;

import ru.tubi.project.utilites.UserDataRecovery;

import static ru.tubi.project.free.AllText.CHECK_CONNECT_INTERNET;
import static ru.tubi.project.free.AllText.LOAD_TEXT;
import static ru.tubi.project.utilites.Constant.GET_CATALOG;

public class ActivityCatalog extends AppCompatActivity {

    Intent intent;
    private RecyclerView recyclerView;
    private CatalogAdapter adapter;
    private ArrayList<String> list=new ArrayList<String>();
    private ArrayList<Catalog>listCatalog=new ArrayList<>();
    private String [] strResult;
    private SearchOrder_id searchOrder_id = new SearchOrder_id();
    private UserModel userDataModel;

    public static final int CATALOG_IS_MINE = 3;

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
            intent = new Intent(this, ActivityCategory.class);
            intent.putExtra("position", position);//
            intent.putExtra("positionName", positionName);
            startActivity(intent);
        }else{
           // Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show();
            intent = new Intent(this, ActivityProduct.class);
            intent.putExtra("key",CATALOG_IS_MINE);
            startActivity(intent);
        }
    }
    //стартовый лист для запуска.
    private void startList(){
        String url = GET_CATALOG;
        url += "receive_catalog";
        url += "&" + "taxpayer_id=" + userDataModel.getCompany_tax_id();//MY_COMPANY_TAXPAYER_ID;
        setInitialData(url);
    }

    //получаем данные из сервера б/д.
    private void setInitialData(String url) {
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
    }
    //новый лист с данными из сервера б/д
    private void splitResult(String result){
        //Toast.makeText(this, "res\n"+result, Toast.LENGTH_SHORT).show();
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
            if(twoString.length>1){
                strImageURL=twoString[1];
            }else {
                strImageURL="null";
            }
            Catalog category=new Catalog(strCategory,strImageURL);
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
        searchOrder_id.searchStartedOrder(this);
        if(userDataModel.getOrder_id() != 0){
            menu.findItem(R.id.shoping_box).setIcon(R.drawable.soping_box_green_60ps);
        }
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu,menu);
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
}