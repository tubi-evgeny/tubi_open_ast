package ru.tubi.project.activity.ForDelete;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import ru.tubi.project.R;

//import ru.tubi.project.adapters.AcceptProductListProvidersAdapter;
import ru.tubi.project.models.AcceptProductListProvidersModel;
import ru.tubi.project.utilites.InitialData;

import java.util.ArrayList;
import java.util.Comparator;

//import static com.example.tubi.Config.MY_TAXPAYER_ID;
import ru.tubi.project.utilites.Constant;

/*public class AcceptProductListProvidersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<AcceptProductListProvidersModel> list=new ArrayList<>();
    private AcceptProductListProvidersAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_product_list_providers);
        //Поступление товара, поставщики

        recyclerView=(RecyclerView)findViewById(R.id.rvList);

        startList();

        AcceptProductListProvidersAdapter.RecyclerViewClickListener clickListener=
                new AcceptProductListProvidersAdapter.RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        ButtonClicked(view,position);
                       // Toast.makeText(AcceptProductListProvidersActivity.this,
                        //        "click position "+position+"\n"+"view "+view, Toast.LENGTH_SHORT).show();
                    }
                };
        adapter=new AcceptProductListProvidersAdapter(this,list,clickListener);
        recyclerView.setAdapter(adapter);
    }
    private void ButtonClicked(View view, int position){
        Intent intent = new Intent(this, AcceptProductAgentActivity.class);
        //intent.putExtra("counterparty_id",list.get(position).getCounterparty_id());
        intent.putExtra("provider_info",list.get(position));
        startActivity(intent);
    }
    //получить список поставщиков заказы которых готовы к приемке на склад
    private void startList() {
        String url = Constant.ADMIN_OFFICE;
        url += "list_provider_which_sellect_order";
        String whatQuestion = "list_provider_which_sellect_order";
        setInitialData(url, whatQuestion);
    }

    private void setInitialData(String url_get, String whatQuestion) {
        // ProgressDialog asyncDialog = new ProgressDialog(this);

        InitialData task=new InitialData(){
          /*  @Override
            protected void onPreExecute() {
                //set message of the dialog
                asyncDialog.setMessage(LOAD_TEXT);
                //show dialog
                asyncDialog.show();
                super.onPreExecute();
            }*/

      /*      @RequiresApi(api = Build.VERSION_CODES.N)
            protected void onPostExecute(String result) {
                //Do your thing
                if(whatQuestion.equals("list_provider_which_sellect_order")){
                    splitResult(result);
                    //Toast.makeText(CatalogProviderActivity.this, ""+result, Toast.LENGTH_SHORT).show();
                }
                //hide the dialog
                // asyncDialog.dismiss();
            }
        };
        task.execute(url_get);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void splitResult(String result){
        list.clear();
        String [] res=result.split("<br>");
        String[]one_temp = res[0].split("&nbsp");
        if(one_temp[0].equals("error") || one_temp[0].equals("messege")){
            // Toast.makeText(this, ""+one_temp[1], Toast.LENGTH_LONG).show();
            return;
        }else{
            for(int i=0;i<res.length;i++){
                String[]temp = res[i].split("&nbsp");

                 int counterparty_id=Integer.parseInt(temp[0]);
                 String abbreviation=temp[1];
                 String counterparty=temp[2];
                 int taxpayer_id=Integer.parseInt(temp[3]);

                AcceptProductListProvidersModel products=new AcceptProductListProvidersModel(counterparty_id,
                        abbreviation, counterparty, taxpayer_id);
                list.add(products);
            }
            // receiveOrderProcessing();
            sortStartList();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sortStartList() {
        //сортируем лист по 2 полям (Product_id и Product_inventory_id)
        list.sort(Comparator.comparing(AcceptProductListProvidersModel::getAbbreviation)
                .thenComparing(AcceptProductListProvidersModel::getCounterparty));
        adapter.notifyDataSetChanged();
    }
//}
*/