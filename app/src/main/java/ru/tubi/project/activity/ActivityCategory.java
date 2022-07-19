package ru.tubi.project.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import ru.tubi.project.R;

import java.util.ArrayList;

import ru.tubi.project.adapters.CategoryAdapter;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.utilites.Constant;
import ru.tubi.project.utilites.GetColorShopingBox;
import ru.tubi.project.utilites.InitialData;
import ru.tubi.project.utilites.SearchOrder_id;
import ru.tubi.project.utilites.UserDataRecovery;

import static ru.tubi.project.free.AllText.LOAD_TEXT;

public class ActivityCategory extends AppCompatActivity {

    Intent intent, takeit;
    private RecyclerView recyclerView;
    private CategoryAdapter adapter;
    private ArrayList<String> list=new ArrayList<String>();
    private String clickPosition="";
    private String url;
    private String positionCatalogName;
    private SearchOrder_id searchOrder_id = new SearchOrder_id();
    private UserModel userDataModel;

    public static final int CATEGORY_ACTIVITY =4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        setTitle(R.string.title_category_products);//Категории

        recyclerView=(RecyclerView)findViewById(R.id.rvList);

        //запуск метода обновить меню,
        // нужен для обновления цвета корзины если не пустая
        invalidateOptionsMenu();

        takeit=getIntent();
        positionCatalogName=takeit.getStringExtra("positionName");


        //получить из sqlLite данные пользователя и компании
        UserDataRecovery userDataRecovery = new UserDataRecovery();
        userDataModel = userDataRecovery.getUserDataRecovery(this);

        //если есть открытый заказ то получить его номер или получить 0 если заказа открытого нет
        searchOrder_id.searchStartedOrder(this);

        CategoryAdapter.RecyclerViewClickListener clickListener=
                new CategoryAdapter.RecyclerViewClickListener() {


                    @Override
                    public void onClick(View view, int position) {
                        //Toast.makeText(ActivityCategory.this,
                         //       " Position category: "+position, Toast.LENGTH_SHORT).show();
                         goProductActivity(position);
                    }

                };

        adapter=new CategoryAdapter(this,list,clickListener);

        recyclerView.setAdapter(adapter);
        startList();
        //setInitialData();
    }
    private void goProductActivity(int position){
        intent = new Intent(this, ActivityProduct.class);
        String category = list.get(position);
        intent.putExtra("category",category);
        intent.putExtra("key",CATEGORY_ACTIVITY);
        startActivity(intent);
    }
    private void splitResult(String result){
        String [] res=result.split("<br>");
       // Toast.makeText(this, "res: "+res[0], Toast.LENGTH_SHORT).show();
        for(int i=0;i<res.length;i++){
            list.add(res[i]);
        }
        adapter.notifyDataSetChanged();
    }
    private void startList(){
        url= Constant.GET_CATEGORY;
        url += positionCatalogName;
       // url += "&" + "taxpayer_id=" + MY_COMPANY_TAXPAYER_ID;
        setInitialData(url);
        Log.d("A111","ActivityCategory / startList / url="+url);
    }
    private void setInitialData(String url) {
        ProgressDialog asyncDialog = new ProgressDialog(this);
        //проверка соединения интернета
        if ( !isOnline() ){
            Toast.makeText(getApplicationContext(),
                    "Нет соединения с интернетом!",Toast.LENGTH_LONG).show();
            return;
        }
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
        //searchOrder_id.searchStartedOrder(this);
       /* if(userDataModel.getOrder_id() != 0){//ORDER_ID
            menu.findItem(R.id.shoping_box).setIcon(R.drawable.soping_box_green_60ps);
        }*/
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
            intent=new Intent(this, MainActivity.class);
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
    //проверка соединения интернета
    protected boolean isOnline() {
        String cs = Context.CONNECTIVITY_SERVICE;
        ConnectivityManager cm = (ConnectivityManager)
                getSystemService(cs);
        if (cm.getActiveNetworkInfo() == null) {
            return false;
        } else {
            return true;
        }
    }
}