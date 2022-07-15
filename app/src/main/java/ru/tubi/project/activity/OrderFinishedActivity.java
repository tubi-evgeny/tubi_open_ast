package ru.tubi.project.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import ru.tubi.project.R;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.utilites.SearchOrder_id;
import ru.tubi.project.utilites.UserDataRecovery;

import static ru.tubi.project.Config.ORDER_ID;
import static ru.tubi.project.free.AllText.DECORATED;
import static ru.tubi.project.free.AllText.FINISHED_ORDER;
import static ru.tubi.project.free.AllText.ON;
import static ru.tubi.project.free.AllText.POINT_OF_ISSUE;
import static ru.tubi.project.free.AllText.YOUR_ORDER;

public class OrderFinishedActivity extends AppCompatActivity {

    LinearLayout lLayoutOrderNumber;
    private TextView tvOrderNumberInfo;
    private Intent intent, takeit;
    private UserModel userDataModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_finished);
        setTitle(FINISHED_ORDER);//ЗАКАЗ ОФОРМЛЕН

        tvOrderNumberInfo = findViewById(R.id.tvOrderNumberInfo);

        lLayoutOrderNumber = findViewById(R.id.lLayoutOrderNumber);
        lLayoutOrderNumber.setBackgroundResource(R.drawable.krugliye_ugli);

        //получить из sqlLite данные пользователя и компании
        UserDataRecovery userDataRecovery = new UserDataRecovery();
        userDataModel = userDataRecovery.getUserDataRecovery(this);

        takeit = getIntent();
        int dayToOrder = takeit.getIntExtra("day",0);
        int order_id = takeit.getIntExtra("order_id",0);
        String date = takeit.getStringExtra("dateGiveOrder");
        String warehouseInfo = takeit.getStringExtra("warehouseInfo");


        tvOrderNumberInfo.setText(""+YOUR_ORDER+" № "+order_id+" "
                +DECORATED+"\n" +ON+" "+date+" \n"+POINT_OF_ISSUE+" \n"+warehouseInfo);

        SearchOrder_id searchOrder_id = new SearchOrder_id();
        searchOrder_id.searchStartedOrder(this);
        ORDER_ID=0;
    }

    @Override
    public void onBackPressed() {
        intent = new Intent(this,ActivityCatalog.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    public boolean onOptionsItemSelected(MenuItem item){
        int itemID=item.getItemId();
        if(itemID==R.id.menu){
            intent=new Intent(this, MenuActivity.class);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

}