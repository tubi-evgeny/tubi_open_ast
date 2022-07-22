package ru.tubi.project.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import ru.tubi.project.R;
import ru.tubi.project.adapters.CatalogStocksAdapter;
import ru.tubi.project.adapters.MessageFeedAdapter;
import ru.tubi.project.models.CatalogProductProviderModel;
import ru.tubi.project.models.MessageModel;
import ru.tubi.project.models.OrderModel;
import ru.tubi.project.utilites.Constant;
import ru.tubi.project.utilites.InitialData;

import static ru.tubi.project.free.AllText.LOAD_TEXT;
import static ru.tubi.project.free.AllText.MESSAGE_FEED;
import static ru.tubi.project.free.VariablesHelpers.MESSAGE_NUMBER;

public class MessageFeedActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MessageFeedAdapter adapter;
    private ArrayList<MessageModel> messageList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_feed);
        setTitle(""+MESSAGE_FEED);

        recyclerView=findViewById(R.id.rvList);

        MessageModel messageModel = new MessageModel("hello this is first message");
        messageList.add(messageModel);

        startList();

        adapter = new MessageFeedAdapter(this,messageList);

        recyclerView.setAdapter(adapter);
    }
    //получить сообщения
    private void startList(){
        String url= Constant.API;
        url += "receive_message_list";
        String whatQuestion = "receive_message_list";
        setInitialData(url, whatQuestion);
        Log.d("A111","MessageFeedActivity / startList / url="+url);
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
                asyncDialog.setMessage(LOAD_TEXT);
                asyncDialog.show();
                super.onPreExecute();
            }

            protected void onPostExecute(String result) {
                if(whatQuestion.equals("receive_message_list")){
                    splitResultMessageArray(result);
                }

                //скрыть диалоговое окно
                asyncDialog.dismiss();
            }
        };
        task.execute(url_get);
    }

    private void splitResultMessageArray(String result){
        Log.d("A111","MessageFeedActivity / splitResultMessageArray / result="+result);
        messageList.clear();
        try{
            String [] res=result.split("<br>");

            String  message_text="";
            for(int i=0; i < res.length;i++){
                message_text = res[i];

                MessageModel messageModel = new MessageModel(message_text);
                messageList.add(messageModel);
            }
            adapter.notifyDataSetChanged();

            MESSAGE_NUMBER = res.length;

            invalidateOptionsMenu();
        }catch (Exception ex){
            Toast.makeText(this, "ex: "+ex, Toast.LENGTH_SHORT).show();
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK,intent);
        finish();
        super.onBackPressed();
    }
}