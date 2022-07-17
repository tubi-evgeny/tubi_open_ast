package ru.tubi.project.activity.agent;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

import ru.tubi.project.R;
import ru.tubi.project.activity.CompanyDateFormActivity;
import ru.tubi.project.adapters.ChoosePartnerAdapter;
import ru.tubi.project.adapters.ProductAdapter;
import ru.tubi.project.models.CatalogProductProviderModel;
import ru.tubi.project.models.CounterpartyModel;
import ru.tubi.project.models.ProductModel;
import ru.tubi.project.utilites.InitialData;

import static ru.tubi.project.free.AllText.CHOOSE_A_PARTNER;
import static ru.tubi.project.free.AllText.LOAD_TEXT;
import static ru.tubi.project.utilites.Constant.AGENT_OFFICE;
import static ru.tubi.project.utilites.Constant.API;

public class ChoosePartnerActivity extends AppCompatActivity implements View.OnClickListener {

    //private RecyclerView recyclerView;
    private ListView lvList;
    private ImageView ivAddPartner;
    private ChoosePartnerAdapter adapter;
    private ArrayAdapter adap;
    private EditText etSearchTextInList;
    private ArrayList<CounterpartyModel> startPartnerList = new ArrayList<>();
    private ArrayList<CounterpartyModel> partnerList = new ArrayList<>();
    private ArrayList<CounterpartyModel> parsePartnerList = new ArrayList<>();
    private ArrayList <String> partnerStringList = new ArrayList<>();
    private static final int COMPANY_DATE_FORM_REQUEST_CODE = 23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_partner);
        setTitle(""+CHOOSE_A_PARTNER);//Выбрать партнера

        lvList=findViewById(R.id.lvList);
        etSearchTextInList = findViewById(R.id.etSearchTextInList);
        ivAddPartner = findViewById(R.id.ivAddPartner);

        ivAddPartner.setOnClickListener(this);

        startPartnerList();

        lvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //go
                Toast.makeText(ChoosePartnerActivity.this, "position="+position, Toast.LENGTH_SHORT).show();
            }
        });
        etSearchTextInList.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {  }
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getNeedProduct();
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        adap = new ArrayAdapter(this, android.R.layout.simple_list_item_1,partnerStringList);
        lvList.setAdapter(adap);
    }
    @Override
    public void onClick(View v) {
        if (v.equals(ivAddPartner)) {
            Intent intent = new Intent(this, CompanyDateFormActivity.class);
            intent.putExtra("agentKey",1);
            startActivityForResult(intent, COMPANY_DATE_FORM_REQUEST_CODE);
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getNeedProduct() {
        //Toast.makeText(this, "click edit: ", Toast.LENGTH_SHORT).show();
        partnerList.clear();
        parsePartnerList.clear();
        parsePartnerList.addAll(startPartnerList);

        //меняем регистр букв на нижний
        String searchSimbol = etSearchTextInList.getText().toString();
        //Toast.makeText(this, "searchSimbol: " + searchSimbol, Toast.LENGTH_SHORT).show();
        for (int i = 0; i < parsePartnerList.size(); i++) {

            ArrayList<String> forParse = new ArrayList<>();
            //берем строку и меняем регистр букв на нижний
            forParse.add(parsePartnerList.get(i).toString().toLowerCase());
            ArrayList<String> res;
            //ведем поиск строки по запросу (ищем совпадения)
            res = (ArrayList<String>) forParse.stream()
                    .filter(lang -> lang.contains(searchSimbol))
                    .collect(Collectors.toList());
            if (res.size() != 0) {
                //пишем в новый каталог
                partnerList.add(parsePartnerList.get(i));
            }
        }
        createPartnerStringList();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void createPartnerStringList(){
        partnerStringList.clear();
        //сортируем лист по 2 полям
        partnerList.sort(Comparator.comparing(CounterpartyModel::getAbbreviation)
                .thenComparing(CounterpartyModel::getCounterparty));

        for(int i=0;i < partnerList.size();i++){
            partnerStringList.add(partnerList.get(i).getCompanyInfoString());
        }
        adap.notifyDataSetChanged();
    }

    private void startPartnerList() {
        String url = AGENT_OFFICE;
        url += "receive_counterparty_list";
        String whatQuestion = "receive_counterparty_list";
        setInitialData(url,whatQuestion);
    }
    private void setInitialData(String url_get, String whatQuestion) {
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
                // if( whatQuestion.equals("get_product_and_quantity")){
                asyncDialog.setMessage(LOAD_TEXT);
                asyncDialog.show();
                //  }
                super.onPreExecute();
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            protected void onPostExecute(String result) {
                if(whatQuestion.equals("receive_counterparty_list")){
                    splitResultCounterpartyArray(result);
                }

                //скрыть диалоговое окно
                asyncDialog.dismiss();
            }
        };
        task.execute(url_get);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void splitResultCounterpartyArray(String result){
        try {

            String [] res=result.split("<br>");
            String[]temp = res[0].split("&nbsp");
            if(temp[0].equals("error") || temp[0].equals("messege")){
                Toast.makeText(this, ""+temp[1], Toast.LENGTH_LONG).show();
            }else{
                for(int i=0;i<res.length;i++){
                    try {
                        temp = res[i].split("&nbsp");
                        int order_id = Integer.parseInt(temp[0]);;
                        String abbreviation = temp[1];
                        String counterparty = temp[2];
                        long taxpayer_id =Long.parseLong(temp[3]);
                        String companyInfoString = temp[4];


                        CounterpartyModel my_counterparty = new CounterpartyModel(order_id
                                , abbreviation, counterparty, taxpayer_id, companyInfoString);
                        startPartnerList.add(my_counterparty);
                        partnerList.clear();
                        partnerList.addAll(startPartnerList);
                       // Log.d("A111","ChoosePartnerActivity / splitResultCounterpartyArray / companyInfoString="+companyInfoString);
                    }catch (Exception ex){
                        Log.d("A111","ERROR / ChoosePartnerActivity / splitResultCounterpartyArray / Exception="+ex);
                    }
                }
            }
            createPartnerStringList();
        }catch (Exception ex){
            Toast.makeText(this, "Exception: "+ex, Toast.LENGTH_SHORT).show();
        }
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