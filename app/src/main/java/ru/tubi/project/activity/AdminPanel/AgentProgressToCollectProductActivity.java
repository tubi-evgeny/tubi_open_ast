package ru.tubi.project.activity.AdminPanel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import ru.tubi.project.R;
import ru.tubi.project.utilites.InitialData;

import ru.tubi.project.utilites.Constant;

import static ru.tubi.project.free.AllText.LOAD_TEXT;

public class AgentProgressToCollectProductActivity extends AppCompatActivity {

    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_progress_to_collect_product);
        //Title - Готовность товара к отгрузке покупателю

        startList();
        recyclerView=(RecyclerView)findViewById(R.id.rvList);


    }

    private void startList() {
        String url = Constant.ADMIN_OFFICE;
        url += "show_all_order_for_collect";
        String whatQuestion ="show_all_order_for_collect";
        setInitialData(url, whatQuestion);
    }
    private void setInitialData(String url, String whatQuestion) {
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
                if(whatQuestion.equals("show_all_order_for_collect")) {
                   // splitResult(result);
                    Toast.makeText(AgentProgressToCollectProductActivity.this,
                            ""+result, Toast.LENGTH_SHORT).show();
                }
                //скрыть диалоговое окно
                asyncDialog.dismiss();
            }
        };
        task.execute(url);

    }
}