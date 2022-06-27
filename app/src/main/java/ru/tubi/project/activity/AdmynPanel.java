package ru.tubi.project.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import ru.tubi.project.R;


//этот класс закоментирован 02,09,2021г
// если ничего не сломается то его можно удалить
public class AdmynPanel extends AppCompatActivity {//} implements AdapterView.OnItemClickListener {

  /*  private Intent intent;
    private EditText etCatalog, etCategory;
    private String res="";
    private String stCatalog, stCategory ;
    private String [] strTemp;
    private ListView lvCatalog, lvCategory;
    private ArrayList<String>alCatalog=new ArrayList<>();
    private ArrayList<String>alCategory=new ArrayList<>();
    private ArrayAdapter<String>adap;
    private boolean flag=true, flag2=true;
    //private int catalogPosition;
    private String url_get=GET_CATEGORY;
    private String positionCatalogName;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admyn_panel);
        setTitle("Admin Panel");

       /* etCatalog=findViewById(R.id.etCatalog);
        etCategory=findViewById(R.id.etCategory);
        lvCatalog=findViewById(R.id.listCatalog);
        lvCatalog.setVisibility(View.GONE);
        lvCatalog.setOnItemClickListener(this);

        lvCategory=findViewById(R.id.lvCategory);
        lvCategory.setVisibility(View.GONE);
        lvCategory.setOnItemClickListener(this);*/


      //  EditTextListener();
    }
  /*  private void InitialEditText(){
        stCatalog=etCatalog.getText().toString();
        stCategory=etCategory.getText().toString();
    }

    private void setInitialData() {         //получаем catalog из сервера б/д.
        InitialEditText();
        res="Catalog: ";
        PutCatalogData task=new PutCatalogData(){
            protected void onPostExecute(String result) {
                //Do your thing
                Result(res,result);
            }
        };
        task.execute(PUT_CATALOG,stCatalog);
    }

    public void addCatalog(View view) {
        //setInitialData();
        InitialEditText();   //get text for editText
        res="Catalog: ";
        PutCatalogData task=new PutCatalogData(){    //wryte catalog to сервер б/д.
            protected void onPostExecute(String result) {
                //Do your thing
                //Result(res,result);
            }
        };
        task.execute(PUT_CATALOG,stCatalog);
        //Toast.makeText(this, "Result: "+res, Toast.LENGTH_SHORT).show();
    }

    public void addCategory(View view) {
        InitialEditText();  //get text for editText
        if(stCatalog.equals("")){
            Toast.makeText(this, "Enter catalog name", Toast.LENGTH_SHORT).show();
            return;
        }
        res="Category: ";
        PutCategoryData task=new PutCategoryData(){   //wryte category to сервер б/д.
            protected void onPostExecute(String result){
                Result(res,result);
            }
        };
        task.execute(PUT_CATEGORY,stCatalog,stCategory);
    }
    private void Result(String res, String result){
        Toast.makeText(this, "Result: "+res+" - "+result, Toast.LENGTH_LONG).show();

    }

    @Override                                                            //this listWiew listener
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if(parent==lvCatalog) {
            etCatalog.setText(alCatalog.get(position));
            lvCatalog.setVisibility(View.GONE);
        }
        if(parent==lvCategory){
            etCategory.setText(alCategory.get(position));
            lvCategory.setVisibility(View.GONE);
           // Toast.makeText(this, "Category", Toast.LENGTH_SHORT).show();
        }
       // catalogPosition=position;
    }

    private void EditTextListener(){
        String catalog="catalog";
        String category="category";
        etCatalog.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Toast.makeText(AdmynPanel.this,
                //      "S: "+s+" Start: "+start+" Count: "+count+" After: "+after, Toast.LENGTH_LONG).show();
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lvCatalog.setVisibility(View.VISIBLE);
                InitialData task=new InitialData(){
                    protected void onPostExecute(String result) {
                        //Do your thing
                        if(flag==true){
                            splitResult(catalog,result);
                            flag=false;
                        }
                    }
                };
                task.execute(GET_CATALOG);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etCategory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                positionCatalogName=etCatalog.getText().toString();
                url_get+=positionCatalogName;
                lvCategory.setVisibility(View.VISIBLE);
                InitialData task=new InitialData(){
                    protected void onPostExecute(String result) {
                        //Do your thing
                        if(flag2==true){
                            splitResult(category,result);
                            flag2=false;
                        }
                    }
                };
                task.execute(url_get);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    private void splitResult(String resource,String result){            //новый лист с данными из сервера б/д
        alCatalog.clear();
        alCategory.clear();
        if(resource.equals("catalog")) {
            String strCategory;
            strTemp = result.split("<br>");
            for (int i = 0; i < strTemp.length; i++) {
                String[] twoString = strTemp[i].split("<div>");
                strCategory = twoString[0];
                alCatalog.add(strCategory);
            }
            adap = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, alCatalog);
            lvCatalog.setAdapter(adap);
        }
        if(resource.equals("category")){
            //String strCategory;
            strTemp = result.split("<br>");
            for (int i = 0; i < strTemp.length; i++) {
                alCategory.add(strTemp[i]);
            }
            //Toast.makeText(this, ""+result, Toast.LENGTH_SHORT).show();
            adap = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, alCategory);
            lvCategory.setVisibility(View.VISIBLE);
            lvCategory.setAdapter(adap);
        }
    }

    public void addProduct(View view) {
        //intent=new Intent(this, ActivtyAddProduct.class);
      //  startActivity(intent);
    }*/
}