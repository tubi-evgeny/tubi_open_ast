package ru.tubi.project.activity.AdminPanel;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextClock;
import android.widget.Toast;

import ru.tubi.project.R;

import ru.tubi.project.adapters.ListProvidersProcessingAdapter;
import ru.tubi.project.models.CounterpartyModel;
import ru.tubi.project.utilites.InitialData;

import java.util.ArrayList;
import java.util.Comparator;

//import static com.example.tubi.Config.MY_TAXPAYER_ID;
import ru.tubi.project.utilites.Constant;

public class ListProvidersProcessingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<CounterpartyModel> list = new ArrayList<>();
    private ArrayList<CounterpartyModel> finishList = new ArrayList<>();
    private ListProvidersProcessingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_providers_processing);
                         //Поставщики готовность товара к отгрузке
       // setTitle(R.string.provider_product_is_ready_for_delivery);

        recyclerView=(RecyclerView)findViewById(R.id.rvList);

        startList();

        ListProvidersProcessingAdapter.RecyclerViewClickListener clickListener=
                new ListProvidersProcessingAdapter.RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Toast.makeText(ListProvidersProcessingActivity.this,
                               "view: "+view+" \nPosition category: "+position, Toast.LENGTH_SHORT).show();
                        //goProductActivity(position);
                    }
                };
        adapter = new ListProvidersProcessingAdapter(this,clickListener,finishList);
        recyclerView.setAdapter(adapter);
    }

    //получить отчеты от поставщиков о сборке товара и готовности к отправке
    private void startList() {
        String url = Constant.ADMIN_OFFICE;
        url += "list_providers_processing_make_orders";
        String whatQuestion = "list_providers_processing_make_orders";
        setInitialData(url, whatQuestion);
        Log.d("A111","ListProvidersProcessingActivity / startList / url="+url);
    }
    private void setInitialData(String url_get, String whatQuestion) {
        InitialData task=new InitialData(){
          /*  @Override
            protected void onPreExecute() {
                //set message of the dialog
                asyncDialog.setMessage(LOAD_TEXT);
                //show dialog
                asyncDialog.show();
                super.onPreExecute();
            }*/

            @RequiresApi(api = Build.VERSION_CODES.N)
            protected void onPostExecute(String result) {
                //Do your thing
                if(whatQuestion.equals("list_providers_processing_make_orders")){
                    splitResult(result);
                    //Toast.makeText(ListProvidersProcessingActivity.this, ""+result, Toast.LENGTH_SHORT).show();
                }
            }
        };
        task.execute(url_get);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void splitResult(String result){
        Log.d("A111","ListProvidersProcessingActivity / splitResult / result="+result);
        Toast.makeText(this, "res\nпаределать скрипт \nтаблица не существует"+result, Toast.LENGTH_SHORT).show();
        list.clear();
        try {
            String[] res = result.split("<br>");
            String[] one_temp = res[0].split("&nbsp");
            if (one_temp[0].equals("error") || one_temp[0].equals("messege")) {
                Toast.makeText(this, "" + one_temp[1], Toast.LENGTH_LONG).show();
                return;
            } else {
                for (int i = 0; i < res.length; i++) {
                    String[] temp = res[i].split("&nbsp");
                    int counterparty_id = Integer.parseInt(temp[0]);
                    String abbreviation = temp[1];
                    String counterparty = temp[2];
                    String processing_condition = temp[3];
                    int sum_weight_volume = Integer.parseInt(temp[4]);
                    CounterpartyModel provider = new CounterpartyModel(counterparty_id,
                            abbreviation, counterparty, processing_condition, sum_weight_volume);
                    list.add(provider);
                }
                //рассортировать лист по 2 полям(Abbreviation , Counterparty)
                list.sort(Comparator.comparing(CounterpartyModel::getAbbreviation)
                        .thenComparing(CounterpartyModel::getCounterparty));

                uniteProcessingList();
                //adapter.notifyDataSetChanged();
                //sortStartList();
            }
        }catch (Exception e){
            Log.d("A111","ListProvidersProcessingActivity  " +
                    "ошибка полученных данных"+e.toString());
        }
    }
    //бъединить данные заказа по весу в одну строку по поставщику
    private void uniteProcessingList() {
        int id = list.get(0).getCounterparty_id();
        int  countPosition=0, completedProcessing=0;
        int countWeight = 0;
        CounterpartyModel temp=new CounterpartyModel(0,"","",0,0);
        for(int i=0;i < list.size();i++){
            if(id == list.get(i).getCounterparty_id()){
                countWeight += list.get(i).getSum_weight_volume();
                countPosition++;
                if(list.get(i).getProcessing_condition().equals("provider_product_in_box")){
                    completedProcessing++;
                }
                temp = new CounterpartyModel(list.get(i).getCounterparty_id(),
                        list.get(i).getAbbreviation(),list.get(i).getCounterparty(),
                        completedProcessing,countWeight);
            }else{
                int completedPercent =  makeProcessingProgress(countPosition,completedProcessing);
                temp.setCompletedProcessing(completedPercent);

                finishList.add(temp);
                id = list.get(i).getCounterparty_id();
                countWeight=0;
                countPosition=0;
                completedProcessing=0;
                countWeight += list.get(i).getSum_weight_volume();
                countPosition++;
                if(list.get(i).getProcessing_condition().equals("provider_product_in_box")){
                    completedProcessing++;
                }
                temp = new CounterpartyModel(list.get(i).getCounterparty_id(),
                        list.get(i).getAbbreviation(),list.get(i).getCounterparty(),
                        completedProcessing,countWeight);
            }
        }
       /* double dCountPosition = countPosition;
        double x = 100 / dCountPosition;
        double z = completedProcessing * x;
        int completedPercent = (int) z;*/
        int completedPercent =  makeProcessingProgress(countPosition,completedProcessing);
        temp.setCompletedProcessing(completedPercent);

        finishList.add(temp);
        for(int i=0;i<finishList.size();i++){
            int weight = finishList.get(i).getSum_weight_volume();
            //делаем округление и граммы переводим в килограммы
            if((weight % 1000) > 500){
                weight = (weight / 1000) + 1;
            }else weight /= 1000;
            if(weight == 0)weight=1;

            finishList.get(i).setSum_weight_volume(weight);
        }
       /* String st="";
        for(int i=0;i<finishList.size();i++){
            st += finishList.get(i).getSum_weight_volume()+"\n";
        }*/
        adapter.notifyDataSetChanged();
        //Toast.makeText(this, "st: "+st, Toast.LENGTH_SHORT).show();
    }
    private int makeProcessingProgress( int  countPosition,int completedProcessing){
        double dCountPosition = countPosition;
        double x = 100 / dCountPosition;
        double z = completedProcessing * x;
        int completedPercent = (int) z;
        return completedPercent;
    }
}