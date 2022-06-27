package ru.tubi.project.activity.AdminPanel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import ru.tubi.project.activity.MainActivity;
import ru.tubi.project.R;
import ru.tubi.project.activity.ActivityCatalog;
import ru.tubi.project.activity.CalendarActivity;
import ru.tubi.project.activity.MenuActivity;
import ru.tubi.project.activity.ShopingBox;
import ru.tubi.project.utilites.InitialData;

import java.util.concurrent.TimeUnit;

import ru.tubi.project.utilites.Constant;

import static ru.tubi.project.free.AllText.ADMIN_PANEL;
import static ru.tubi.project.free.AllText.CHECK_CONNECT_INTERNET;
import static ru.tubi.project.free.AllText.UPDATING;

public class AdminActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout llCheckNewBuyer,llAcceptProd,llCheckProductList,
                        llCandidateForProvider,llCandidateForPartnerWarehouse,
                        llCreateWarehouse,llListPoviderProcessing,llImageForModeration,
                        llCalendar;
    private ImageView ivCheckNewBuyer,ivCheckProductList,ivForProvider,ivPartnerWarehouse;
    private Intent intent;
    int count = 0;
    boolean inputProductFlag=false, candidateForProviderFlag = false,
            candidateForPartnerWarehouseFlag = false ,orderForNewBuyerFlag = false;
    private Button btnCheckProductList,btnCandidateForProvider,btnCheckNewBuyer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        setTitle(ADMIN_PANEL);//ПАНЕЛЬ АДМИНИСТРАТОРА

       // btnCheckProductList=findViewById(R.id.btnCheckProductList);
       // btnCandidateForProvider=findViewById(R.id.btnCandidateForProvider);
        //btnCheckNewBuyer=findViewById(R.id.btnCheckNewBuyer);
        llCheckNewBuyer=findViewById(R.id.llCheckNewBuyer);
        llCheckProductList=findViewById(R.id.llCheckProductList);
        llCandidateForProvider=findViewById(R.id.llCandidateForProvider);
        llCandidateForPartnerWarehouse=findViewById(R.id.llCandidateForPartnerWarehouse);
        llListPoviderProcessing=findViewById(R.id.llListPoviderProcessing);
        llImageForModeration=findViewById(R.id.llImageForModeration);
        llCalendar=findViewById(R.id.llCalendar);
        ivCheckNewBuyer=findViewById(R.id.ivCheckNewBuyer);
        ivCheckProductList=findViewById(R.id.ivCheckProductList);
        ivForProvider=findViewById(R.id.ivForProvider);
        ivPartnerWarehouse=findViewById(R.id.ivPartnerWarehouse);

        llCheckNewBuyer.setOnClickListener(this);
        llCheckProductList.setOnClickListener(this);
        llCandidateForProvider.setOnClickListener(this);
        llCandidateForPartnerWarehouse.setOnClickListener(this);
        llListPoviderProcessing.setOnClickListener(this);
        llImageForModeration.setOnClickListener(this);
        llCalendar.setOnClickListener(this);

        //проверить наличие данных для модерации
        searchDataForModeration();
   }
    //проверить наличие данных для модерации
    private void searchDataForModeration() {

            String url= Constant.ADMIN_OFFICE;
            searchNewInputProduct(url);
            searchNewCandidateForProviders(url);
            searchNewCandidateForPartnerWarehouse(url);
            searchOrderForNewBuyer(url);
    }
    private void searchOrderForNewBuyer(String url){
        String url_get = url;
        url_get += "search_order_for_new_buyer";
        String whatQuestion = "search_order_for_new_buyer";
        setInitialData( url_get, whatQuestion);
    }

    private void searchNewCandidateForProviders(String url) {
        String url_get = url;
        url_get += "search_new_candidate_for_provider";
        String whatQuestion = "search_new_candidate_for_provider";
        setInitialData( url_get, whatQuestion);
    }
    private void searchNewCandidateForPartnerWarehouse(String url){
        String url_get = url;
        url_get += "search_new_candidate_for_partner_warehouse";
        String whatQuestion = "search_new_candidate_for_partner_warehouse";
        setInitialData( url_get, whatQuestion);
    }
    private void searchNewInputProduct(String url) {
        String url_get = url;
        url_get += "search_new_input_product";
        String whatQuestion = "search_new_input_product";
        setInitialData( url_get, whatQuestion);
    }

    private void setInitialData(String url_get, String whatQuestion) {

        InitialData task=new InitialData(){

            protected void onPostExecute(String result) {
                //Do your thing
                if(whatQuestion.equals("search_new_input_product")) {
                    splitResult(result,whatQuestion);
                }else if(whatQuestion.equals("search_new_candidate_for_provider")){
                    splitResult(result,whatQuestion);
                }else if(whatQuestion.equals("search_new_candidate_for_partner_warehouse")){
                    splitResult(result,whatQuestion);
                   // Toast.makeText(AdminActivity.this, ""+result, Toast.LENGTH_SHORT).show();
                }else if(whatQuestion.equals("search_order_for_new_buyer")) {
                    splitResult(result, whatQuestion);
                }
            }
        };
        task.execute(url_get);
    }
    private void splitResult(String result, String whatQuestion){  // разобрать результат с сервера список продуктов и колличество
        String [] res=result.split("<br>");
        String[]one_temp = res[0].split("&nbsp");
        if(one_temp[0].equals("RESULT_OK")){
            if(whatQuestion.equals("search_new_input_product")){
                inputProductFlag=true;
                invalidateOptionsMenu();
               // btnCheckProductList.setTextColor(TUBI_WHITE);
               // btnCheckProductList.setBackgroundColor(TUBI_GREEN_600);
                ivCheckProductList.setImageResource(R.drawable.strelka_dalshe_red_60ps);
            }else if(whatQuestion.equals("search_new_candidate_for_provider")){
                candidateForProviderFlag=true;
                invalidateOptionsMenu();
                ivForProvider.setImageResource(R.drawable.strelka_dalshe_red_60ps);
            }else if(whatQuestion.equals("search_new_candidate_for_partner_warehouse")){
                candidateForPartnerWarehouseFlag=true;
                invalidateOptionsMenu();
                ivPartnerWarehouse.setImageResource(R.drawable.strelka_dalshe_red_60ps);
            }else if(whatQuestion.equals("search_order_for_new_buyer")){
                orderForNewBuyerFlag=true;
                invalidateOptionsMenu();
                //btnCheckNewBuyer.setTextColor(TUBI_WHITE);
                //btnCheckNewBuyer.setBackgroundColor(TUBI_GREEN_600);
                ivCheckNewBuyer.setImageResource(R.drawable.strelka_dalshe_red_60ps);
            }
        }else if(one_temp[0].equals("error") || one_temp[0].equals("messege")){
            if(whatQuestion.equals("search_new_input_product")){
                inputProductFlag=false;
                invalidateOptionsMenu();
               // btnCheckProductList.setTextColor(TUBI_BLACK);
               // btnCheckProductList.setBackgroundColor(TUBI_GREY_200);
               // btnCheckProductList.setClickable(false);
                ivCheckProductList.setImageResource(R.drawable.strelka_dalshe_60ps);
                llCheckProductList.setClickable(false);
            }
            else if(whatQuestion.equals("search_new_candidate_for_provider")){
                candidateForProviderFlag=false;
                invalidateOptionsMenu();
                ivForProvider.setImageResource(R.drawable.strelka_dalshe_60ps);
                llCandidateForProvider.setClickable(false);
            }
            else if(whatQuestion.equals("search_new_candidate_for_partner_warehouse")){
                candidateForPartnerWarehouseFlag=false;
                invalidateOptionsMenu();
                ivPartnerWarehouse.setImageResource(R.drawable.strelka_dalshe_60ps);
                //ivPartnerWarehouse.setBackgroundColor(TUBI_WHITE);
                llCandidateForPartnerWarehouse.setClickable(false);
            }
            else if(whatQuestion.equals("search_order_for_new_buyer")){
                orderForNewBuyerFlag=false;
                invalidateOptionsMenu();
               // btnCheckNewBuyer.setTextColor(TUBI_BLACK);
               // btnCheckNewBuyer.setBackgroundColor(TUBI_GREY_200);
                //btnCheckNewBuyer.setClickable(false);
                ivCheckNewBuyer.setImageResource(R.drawable.strelka_dalshe_60ps);
                llCheckNewBuyer.setClickable(false);
            }
            Toast.makeText(this, ""+one_temp[1], Toast.LENGTH_LONG).show();
            return;
        }else{
            Toast.makeText(this, ""+CHECK_CONNECT_INTERNET, Toast.LENGTH_LONG).show();
            return;
        }
    }

    /*public void goCheckNewBuyer(View view) {
        Intent intent = new Intent(this, NewBuyerCheckActivity.class);
        startActivity(intent);
    }*/

  /*  public void goAddProductsCheck(View view) {
        Intent intent = new Intent(this, ActivityAddProductsCheck.class);
        startActivity(intent);
    }*/

    @Override
    public void onClick(View v) {
        if(v.equals(llCheckNewBuyer)){
            Intent intent = new Intent(this, NewBuyerCheckActivity.class);
            startActivity(intent);
        }
        else  if(v.equals(llCheckProductList)){
            Intent intent = new Intent(this, ActivityAddProductsCheck.class);
            startActivity(intent);
        }
        else  if(v.equals(llCandidateForProvider)){
            Intent intent = new Intent(this, ListPartnerForModerationActivity.class);
            intent.putExtra("condidate","condidate_for_provider");
            startActivity(intent);
        }
        else if(v.equals(llCandidateForPartnerWarehouse)){
            Intent intent = new Intent(this, ListPartnerForModerationActivity.class);
            intent.putExtra("condidate","candidate_for_partner_warehouse");
            startActivity(intent);
            //Toast.makeText(this, "llCandidateForPartnerWarehouse", Toast.LENGTH_SHORT).show();
        }
        else if(v.equals(llListPoviderProcessing)){
            Intent intent = new Intent(this, ListProvidersProcessingActivity.class);
            startActivity(intent);
        }
        else if(v.equals(llImageForModeration)){
            Toast.makeText(this, "Создать Activity(страницу) для модерации фотографий(t_image_moderation)", Toast.LENGTH_SHORT).show();
           // Intent intent = new Intent(this, ListProvidersProcessingActivity.class);
           // startActivity(intent);
        }
        else if(v.equals(llCalendar)){
             Intent intent = new Intent(this, CalendarActivity.class);
             startActivity(intent);
        }//
    }


    //слушатель возврата по методу Back(); из предыдущей активности
    //нужен для обновления необходимой информации
    @Override
    protected void onRestart() {
        super.onRestart();
        //проверить наличие данных для модерации
        searchDataForModeration();
    }
    //обновить корзину
    //этот метод запускается этим invalidateOptionsMenu();
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        try {
            TimeUnit.MILLISECONDS.sleep(500);
            if(inputProductFlag){  //if(count%2 != 0){
                menu.findItem(R.id.update).setIcon(R.drawable.simbol_update_60ps_green);
            }else if(candidateForProviderFlag){  //if(count%2 != 0){
                menu.findItem(R.id.update).setIcon(R.drawable.simbol_update_60ps_green);
            }
            if(orderForNewBuyerFlag){
                menu.findItem(R.id.update).setIcon(R.drawable.simbol_update_red_dot_60ps_green);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return super.onPrepareOptionsMenu(menu);
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
        if(itemID==R.id.update){
            count++;
            reCreateActivity();
           // invalidateOptionsMenu();
            Toast.makeText(this, ""+UPDATING, Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu,menu);
        menu.findItem(R.id.category).setVisible(false);
        menu.findItem(R.id.shoping_box).setVisible(false);
        menu.findItem(R.id.update).setVisible(true);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    private void reCreateActivity() {

        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }

}