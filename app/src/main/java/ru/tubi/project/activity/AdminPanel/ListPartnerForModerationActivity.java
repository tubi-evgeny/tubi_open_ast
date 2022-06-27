package ru.tubi.project.activity.AdminPanel;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import ru.tubi.project.R;
import ru.tubi.project.adapters.ListPartnerForModerationAdapter;
import ru.tubi.project.models.ListPartnerForModerationAdapterModel;
import ru.tubi.project.utilites.InitialData;

import java.util.ArrayList;

import ru.tubi.project.utilites.Constant;

import static ru.tubi.project.free.AllText.CONDIDATE;
import static ru.tubi.project.free.AllText.TO_PARTNER_WAREHOUSE;
import static ru.tubi.project.free.AllText.TO_SUPPLIERS_PRODUCT;

public class ListPartnerForModerationActivity extends AppCompatActivity {

    private Intent takeit;
    private RecyclerView recyclerView;
    private ListPartnerForModerationAdapter adapter;
    private ArrayList<ListPartnerForModerationAdapterModel> list=new ArrayList<>();
    private String url ,condidateFormInfo, condidateForm="";
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_partner_for_moderation);
        setTitle(CONDIDATE);//Кaндидаты
        actionBar = getSupportActionBar();
        //setTitle(LIST_PROVIDER_FOR_MODERATION_TEXT);//Лист кандидатов в поставщики

        recyclerView=(RecyclerView)findViewById(R.id.rvList);

        takeit = getIntent();
        condidateFormInfo = takeit.getStringExtra("condidate");

        makeSubtitle();
        makeStartList();

        ListPartnerForModerationAdapter.RecyclerViewClickListener clickListener=
                new ListPartnerForModerationAdapter.RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        goAddProviderActivity(position);
                        //Toast.makeText(ListProviderForModerationActivity.this,
                       //         "Position: "+position, Toast.LENGTH_SHORT).show();
                    }
                };

        adapter=new ListPartnerForModerationAdapter(this, list, clickListener);

        recyclerView.setAdapter(adapter);
    }

    private void makeSubtitle() {

        if(condidateFormInfo.equals("condidate_for_provider")){
            condidateForm = TO_SUPPLIERS_PRODUCT;//в поставщики товаров
        }
        else if(condidateFormInfo.equals("candidate_for_partner_warehouse")){
            condidateForm = TO_PARTNER_WAREHOUSE;//в партнеры склада
        }
        //candidate_for_partner_warehouse
        actionBar.setSubtitle(condidateForm);
       // makeStartList();
    }

    private void goAddProviderActivity(int position){
        //int tax_id_provider = list.get(position).getTaxpayer_id();
        Intent intent = new Intent(this, AddPartnerActivity.class);
        intent.putExtra("contractInfo",condidateFormInfo);
        intent.putExtra("provider_info",list.get(position));
        startActivity(intent);
    }

    private void makeStartList() {
        url = Constant.ADMIN_OFFICE;
        String url_get = url;
        String whatQuestion="";

        if(condidateFormInfo.equals("condidate_for_provider")){
            url_get += "show_provider_candidates";
            whatQuestion ="show_provider_candidates";
        }
        else if(condidateFormInfo.equals("candidate_for_partner_warehouse")){
            url_get += "candidate_for_partner_warehouse";
            whatQuestion ="candidate_for_partner_warehouse";
        }

        setInitialData(url_get, whatQuestion);
    }
    private void setInitialData(String url_get, String whatQuestion) {

        InitialData task=new InitialData(){
            protected void onPostExecute(String result) {
                if(whatQuestion.equals("show_provider_candidates")){
                    splitResult(result);
                }
                else if(whatQuestion.equals("candidate_for_partner_warehouse")){
                    splitResult(result);
                }
            }
        };
        task.execute(url_get);
    }
    private void splitResult(String result){  // разобрать результат с сервера список продуктов и колличество
        list.clear();
        try {
            String[] res = result.split("<br>");
            String[] one_temp = res[0].split("&nbsp");
            if (one_temp[0].equals("error") || one_temp[0].equals("message")) {
                Toast.makeText(this, "" + one_temp[1], Toast.LENGTH_LONG).show();
                return;
            } else {
                for (int i = 0; i < res.length; i++) {
                    String[] temp = res[i].split("&nbsp");
                    int counterparty_id=Integer.parseInt(temp[0]);
                    String abbreviation=temp[1];
                    String counterparty=temp[2];
                    long taxpayer_id=Long.parseLong(temp[3]);
                    int role_partner_id=Integer.parseInt(temp[4]);
                    String createdDate=temp[5];
                    String user=temp[6];
                    String phone=temp[7];
                    int count_step=Integer.parseInt(temp[8]);
                    int user_id=Integer.parseInt(temp[9]);

                    ListPartnerForModerationAdapterModel provider =
                            new ListPartnerForModerationAdapterModel(counterparty_id, abbreviation,
                                    counterparty, taxpayer_id, role_partner_id, createdDate, user,
                                    phone, count_step, user_id);
                    list.add(provider);
                }
                adapter.notifyDataSetChanged();
            }
        }catch(Exception ex){
            Toast.makeText(this, "ex: "+ex, Toast.LENGTH_SHORT).show();
        }
    }
    //слушатель возврата по методу Back(); из предыдущей активности
    //нужен для обновления необходимой информации
    @Override
    protected void onRestart() {
        super.onRestart();
        makeStartList();
        //ниже обновление страницы без картинок
      /*  finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);*/
    }
}