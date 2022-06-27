package ru.tubi.project.activity.ForDelete;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import ru.tubi.project.R;
//import ru.tubi.project.adapters.AcceptProductAgentAdapter;
import ru.tubi.project.models.AcceptProductListProvidersModel;
import ru.tubi.project.utilites.InitialData;

import java.util.ArrayList;
import java.util.Comparator;

//import static com.example.tubi.Config.MY_TAXPAYER_ID;
import ru.tubi.project.utilites.Constant;

import static ru.tubi.project.free.AllText.ACCEPT_PRODUCT;

/*public class AcceptProductAgentActivity extends AppCompatActivity {

    private TextView tvCounterparty;
    private RecyclerView recyclerView;
    private AcceptProductAgentAdapter adapter;
    private ArrayList<AcceptProductListProvidersModel> listProduct=new ArrayList<>();
    private ArrayList<AcceptProductListProvidersModel> list=new ArrayList<>();
    private Intent takeit;
    private int counterparty_id, x;
    private AcceptProductListProvidersModel provider_info;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.d_activity_accept_product_agent);
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        setTitle(null);
        actionBar.setSubtitle(ACCEPT_PRODUCT);//Принять товар

        tvCounterparty=findViewById(R.id.tvCounterparty);
        recyclerView=(RecyclerView)findViewById(R.id.rvList);

        takeit = getIntent();
        //counterparty_id = takeit.getIntExtra("counterparty_id",x);
        provider_info = (AcceptProductListProvidersModel)takeit.getSerializableExtra("provider_info");
        Toast.makeText(this, "counterparty_id; "+provider_info.getCounterparty_id()+
                "\nname: "+provider_info.getCounterparty(), Toast.LENGTH_SHORT).show();

        tvCounterparty.setText(provider_info.getAbbreviation()+" "+provider_info.getCounterparty());

        startList();

        AcceptProductAgentAdapter.RecyclerViewClickListener clickListener=
                new AcceptProductAgentAdapter.RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        //ButtonClicked(view,position);
                        Toast.makeText(AcceptProductAgentActivity.this,
                                "click position "+position+"\n"+"view "+view, Toast.LENGTH_SHORT).show();
                    }
                };

        adapter=new AcceptProductAgentAdapter(this,listProduct,clickListener);
        recyclerView.setAdapter(adapter);
    }
    // агент получает список товаров поступивших от поставщика на склад агента
    private void startList() {
        String url = Constant.ADMIN_OFFICE;
        url += "receive_list_product_arrived_from_provider";
        url += "&"+"taxpayer_id="+provider_info.getTaxpayer_id();
        String whatQuestion = "receive_list_product_arrived_from_provider";
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

       /*     @RequiresApi(api = Build.VERSION_CODES.N)
            protected void onPostExecute(String result) {
                //Do your thing
                if(whatQuestion.equals("receive_list_product_arrived_from_provider")){
                    splitResult(result);
                   // Toast.makeText(AcceptProductAgentActivity.this, "res: "+result, Toast.LENGTH_SHORT).show();
                }
                //hide the dialog
                // asyncDialog.dismiss();
            }
        };
        task.execute(url_get);
    }
    // разобрать результат с сервера список продуктов и колличество
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void splitResult(String result){
        listProduct.clear();
        String [] res=result.split("<br>");
        String[]one_temp = res[0].split("&nbsp");
        if(one_temp[0].equals("error") || one_temp[0].equals("messege")){
            // Toast.makeText(this, ""+one_temp[1], Toast.LENGTH_LONG).show();
            return;
        }else{
            for(int i=0;i<res.length;i++){
                String[]temp = res[i].split("&nbsp");

                int order_product_id= Integer.parseInt(temp[0]);
                int product_id = Integer.parseInt(temp[1]);
                int product_inventory_id = Integer.parseInt(temp[2]);
                String category = temp[3];
                String brand = temp[4];
                String characteristic = temp[5];
                String type_packaging = temp[6];
                String unit_measure = temp[7];
                int weight_volume = Integer.parseInt(temp[8]);
                //double price = Double.parseDouble(temp[9]);
                int quantity_package = Integer.parseInt(temp[9]);
                String image_url = temp[10];
                double quantity = Double.parseDouble(temp[11]);
                int providerChecked = Integer.parseInt(temp[12]);
                int order_id = Integer.parseInt(temp[13]);
                int agentChecked = Integer.parseInt(temp[14]);

                AcceptProductListProvidersModel products=new AcceptProductListProvidersModel(
                        product_id, product_inventory_id, category, brand, characteristic, type_packaging,
                        unit_measure, weight_volume, quantity_package, image_url, quantity, order_product_id,
                        providerChecked, agentChecked, order_id);
                listProduct.add(products);
            }
            sortStartList();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sortStartList() {
        //сортируем лист по 2 полям (Product_id и Product_inventory_id)
        listProduct.sort(Comparator.comparing(AcceptProductListProvidersModel::getProduct_id)
                .thenComparing(AcceptProductListProvidersModel::getProduct_inventory_id));

        list.addAll(listProduct);
        listProduct.clear();
        AcceptProductListProvidersModel product = list.get(0);
        double quantity=0;
        int id = list.get(0).getProduct_inventory_id();
        for (int i = 0; i < list.size(); i++) {
            if (id == list.get(i).getProduct_inventory_id()) {
                quantity +=list.get(i).getQuantity();
                product = list.get(i);
            }else{
                product.setQuantity(quantity);
                quantity=0;
                listProduct.add(product);
                id = list.get(i).getProduct_inventory_id();
                quantity +=list.get(i).getQuantity();
                product = list.get(i);
            }
        }
        product.setQuantity(quantity);
        listProduct.add(product);

        adapter.notifyDataSetChanged();

    }

}*/