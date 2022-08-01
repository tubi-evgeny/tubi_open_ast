package ru.tubi.project.activity.company_my;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import ru.tubi.project.R;
import ru.tubi.project.utilites.InitialData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static ru.tubi.project.free.AllText.LOAD_TEXT;
import static ru.tubi.project.free.AllText.RECEIVE_PRODUCT_INFO;

public class InfoForProductCardFillActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener {

    private TextView tvProduct, tvRequestInfo;
    private EditText etRequestData;
    private ListView lvRequestList;
    private Intent intent;
    private ArrayList<String> List=new ArrayList<>();
    private ArrayList<String> copyList = new ArrayList<>();
    private ArrayAdapter<String> adap;
    private String request, product;
    private int x;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_for_product_card_fill);
        setTitle(RECEIVE_PRODUCT_INFO);//ПОЛУЧИТЬ ДАННЫЕ ТОВАРА

        tvProduct=findViewById(R.id.tvProduct);
        tvRequestInfo=findViewById(R.id.tvRequestInfo);
        etRequestData=findViewById(R.id.etRequestData);
        lvRequestList=findViewById(R.id.lvRequestList);
        lvRequestList.setOnItemClickListener(this);

        etRequestData.setSelection(etRequestData.getText().length());

        intent=getIntent();
        String url = intent.getStringExtra("url");
        product = intent.getStringExtra("product");
        request = intent.getStringExtra("request");
        String request_name = intent.getStringExtra("request_name");

        String st=request;
        tvRequestInfo.setText(""+st+" "+request_name);
        tvProduct.setText(Html.fromHtml(product));

        EditTextListener();
        startData(url);

    }

    private void EditTextListener(){
        etRequestData.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {       }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {       }
            @Override
            public void afterTextChanged(Editable s) {
                goNewArrayWork();
            }
        });

        KeyboardVisibilityEvent.setEventListener(this, new KeyboardVisibilityEventListener() {
            @Override
            public void onVisibilityChanged(boolean b) {
                if (b) {
                    tvProduct.setVisibility(View.GONE);
                }else{
                        tvProduct.setVisibility(View.VISIBLE);
                        Toast.makeText(InfoForProductCardFillActivity.this, "keyboard hidden", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void goNewArrayWork(){
        makeCopyList();
        
        ArrayList<String> tempList = new ArrayList<>();
        tempList.add("text");

        if(!etRequestData.getText().toString().equals("")) {
            String string = etRequestData.getText().toString();
            tempList.clear();
            String[] stTemp = string.split(" ");

            for (int a = 0; a < stTemp.length; a++) {
                //делаем все буквы строчными
                String st = stTemp[a].toLowerCase();

                //делаем все буквы строчными
                // String st = string.toLowerCase();
                //char simb[] = string.toCharArray();
                char simb[] = st.toCharArray();
               // Toast.makeText(this, "st: " + simb[0], Toast.LENGTH_SHORT).show();
                for (int i = 0; i < simb.length; i++) {
                    for (int j = 0; j < copyList.size(); j++) {
                        String[] stList = copyList.get(j).split(" ");
                        for (int k = 0; k < stList.length; k++) {
                            //делаем все буквы строчными
                            String stringforChar = stList[k].toLowerCase();
                            char[] tempSimb = stringforChar.toCharArray();
                            if(simb.length <= tempSimb.length){
                                if (simb[i] == tempSimb[i]) {
                                    tempList.add(copyList.get(j));
                                }
                            }
                        }
                    }
                    copyList.clear();
                    //метод удаляет дубликаты с сохранением позиций
                    //Set<String> set = new LinkedHashSet<>(tempList);
                    Set<String> set = new HashSet<>(tempList);
                    copyList.addAll(set);
                    tempList.clear();

                }
            }
        }

        adap = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, copyList);
        lvRequestList.setAdapter(adap);
        //adap.notifyDataSetChanged();
    }

    private void makeCopyList() {
        copyList = (ArrayList) List.clone();
    }

    public void goWriteData(View view) {

        String res = request;

        if(!etRequestData.getText().toString().equals("")) {
            res = etRequestData.getText().toString().trim();
        }
        // поместите строку для передачи обратно в intent и закрыть это действие
        intent = new Intent();
        intent.putExtra("request", res);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void startData(String url) {
        setInitialData(url);
        Log.d("A111","InfoForProductCardFillActivity / startData / url= "+url);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        etRequestData.setText(copyList.get(position));
        etRequestData.setSelection(etRequestData.getText().length());
    }

    private void setInitialData(String url_get) {
        ProgressDialog asyncDialog = new ProgressDialog(this);

        InitialData task=new InitialData(){
            @Override
            protected void onPreExecute() {
                asyncDialog.setMessage(LOAD_TEXT);
                asyncDialog.show();
                super.onPreExecute();
            }
            @Override
            protected void onPostExecute(String result) {
                    splitResult(result);

                //скрыть диалоговое окно
                asyncDialog.dismiss();
            }
        };
        task.execute(url_get);
    }

    // разобрать результат с сервера категории
    private void splitResult(String result){
        try {
            List.clear();
            if (!result.equals("")) {
                String[] res = result.split("<br>");
                String[] temp = res[0].split("&nbsp");

                for (int i = 0; i < temp.length; i++) {

                    List.add(temp[i]);
                }
            }
            makeCopyList();
        }catch (Exception ex){}
        adap = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, copyList);

        lvRequestList.setAdapter(adap);
    }
}
