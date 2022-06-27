package ru.tubi.project.activity.AdminPanel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import ru.tubi.project.R;
import ru.tubi.project.adapters.NewBuyerCheckAdapter;
import ru.tubi.project.models.NewBuyerCheckModel;
import ru.tubi.project.utilites.InitialData;

import java.util.ArrayList;

import ru.tubi.project.utilites.Constant;

import static ru.tubi.project.free.AllText.LIST_NEW_BUYERS;

public class NewBuyerCheckActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<NewBuyerCheckModel> list = new ArrayList<>();
    private NewBuyerCheckAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_buyer_check);
        setTitle(LIST_NEW_BUYERS);//Лист новых покупателей

        recyclerView=(RecyclerView)findViewById(R.id.rvList);

        makeStartList();

        NewBuyerCheckAdapter.RecyclerViewClickListener clickListener=
                new NewBuyerCheckAdapter.RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        goUserToInfoForOrder(position);
                       // Toast.makeText(NewBuyerCheckActivity.this,
                            //     "Position: "+position, Toast.LENGTH_SHORT).show();
                    }
                };

        adapter=new NewBuyerCheckAdapter(this, list, clickListener);

        recyclerView.setAdapter(adapter);
    }
    private void makeStartList() {
        String url = Constant.ADMIN_OFFICE;
        String url_get = url;
        url_get += "show_new_buyer";
        String whatQuestion ="show_new_buyer";
        setInitialData(url_get, whatQuestion);
    }
    private void setInitialData(String url_get, String whatQuestion) {

        InitialData task=new InitialData(){
            protected void onPostExecute(String result) {
                //Do your thing
                if(whatQuestion.equals("show_new_buyer")){
                    splitResult(result);
                }
            }
        };
        task.execute(url_get);
    }
    private void splitResult(String result){  // разобрать результат с сервера список продуктов и колличество
        list.clear();
        try {

            String [] res=result.split("<br>");
            //Toast.makeText(this, ""+res[0], Toast.LENGTH_SHORT).show();
            String[]one_temp = res[0].split("&nbsp");
            if(one_temp[0].equals("error") || one_temp[0].equals("messege")){
                Toast.makeText(this, ""+one_temp[1], Toast.LENGTH_LONG).show();
                return;
            }else{
                for(int i=0;i<res.length;i++){
                    String[]temp = res[i].split("&nbsp");
                    NewBuyerCheckModel buyer = new NewBuyerCheckModel(temp[0],temp[1],temp[2],temp[3],
                            temp[4],Integer.parseInt(temp[5]),Integer.parseInt(temp[6]));
                    list.add(buyer);
                }
                adapter.notifyDataSetChanged();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void goUserToInfoForOrder(int position){
        Intent intent = new Intent(this,UserToInfoForOrderActivity.class);
        intent.putExtra("buyer",list.get(position));
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, AdminActivity.class);
        startActivity(intent);
    }
}