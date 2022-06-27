package ru.tubi.project.activity.company_my;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;

import ru.tubi.project.R;
import ru.tubi.project.adapters.MyProductInPartnerWarehouseAdapter;
import ru.tubi.project.models.CatalogProductProviderModel;
import ru.tubi.project.models.MyProductInPartnerWarehouseModel;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.utilites.Constant;
import ru.tubi.project.utilites.InitialData;
import ru.tubi.project.utilites.UserDataRecovery;

import static ru.tubi.project.free.AllText.FROM_PARTNERS;
import static ru.tubi.project.free.AllText.LOAD_TEXT;
import static ru.tubi.project.free.AllText.MY_STOCKS;

public class MyProductInPartnerWarehouseActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<MyProductInPartnerWarehouseModel> listProduct = new ArrayList<>();
    private MyProductInPartnerWarehouseAdapter adapter;

    private UserModel userDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_product_in_partner_warehouse);
        setTitle(MY_STOCKS);//мои запасы у партнеров
        getSupportActionBar().setSubtitle(FROM_PARTNERS);

        recyclerView = (RecyclerView) findViewById(R.id.rvList);

        //получить из sqlLite данные пользователя и компании
        UserDataRecovery userDataRecovery = new UserDataRecovery();
        userDataModel = userDataRecovery.getUserDataRecovery(this);


        startList();

        MyProductInPartnerWarehouseAdapter.RecyclerViewClickListener clickListener =
                new MyProductInPartnerWarehouseAdapter.RecyclerViewClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        WhatButtonClicked(view, position);
                    }
                };
        adapter = new MyProductInPartnerWarehouseAdapter(this, listProduct, clickListener);
        recyclerView.setAdapter(adapter);

    }
    private void WhatButtonClicked(View view, int position){
        String string = String.valueOf(view);
        String str[] = string.split("/");

        if (str[1].equals("tvDescription}") || str[1].equals("tvQuantity}")) {
            Toast.makeText(this, "tvDescription", Toast.LENGTH_SHORT).show();
        }
    }
    private void startList() {
        String url = Constant.PROVIDER_OFFICE;
        url += "&" + "receive_my_product_int_partner_warehouses";
        url += "&" + "counterparty_tax_id=" + userDataModel.getCompany_tax_id();//MY_COMPANY_TAXPAYER_ID;
        String whatQuestion = "receive_my_product_int_partner_warehouses";
        setInitialData(url, whatQuestion);

    }
    private void setInitialData(String url_get, String whatQuestion) {
        ProgressDialog asyncDialog = new ProgressDialog(this);

        InitialData task = new InitialData() {
            @Override
            protected void onPreExecute() {
                asyncDialog.setMessage(LOAD_TEXT);
                asyncDialog.show();
                super.onPreExecute();
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            protected void onPostExecute(String result) {
                if (whatQuestion.equals("receive_my_product_int_partner_warehouses")) {
                    splitResult(result);
                }
                asyncDialog.dismiss();
            }
        };
        task.execute(url_get);
    }
    // разобрать результат с сервера список продуктов и колличество
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void splitResult(String result) {
        listProduct.clear();
        try {
            String[] res = result.split("<br>");
            String[] one_temp = res[0].split("&nbsp");
            if (one_temp[0].equals("error") || one_temp[0].equals("messege")) {
                Toast.makeText(this, "" + one_temp[1], Toast.LENGTH_LONG).show();
                return;
            } else {
                for (int i = 0; i < res.length; i++) {
                    String[] temp = res[i].split("&nbsp");

                    int product_id = Integer.parseInt(temp[0]);
                    int product_inventory_id = Integer.parseInt(temp[1]);
                    String category = temp[2];
                    String brand = temp[3];
                    String characteristic = temp[4];
                    String type_packaging = temp[5];
                    String unit_measure = temp[6];
                    int weight_volume = Integer.parseInt(temp[7]);
                    int quantity_package = Integer.parseInt(temp[8]);
                    String image_url = temp[9];

                    int warehouse_info_id = Integer.parseInt(temp[10]);
                    int warehouse_id = Integer.parseInt(temp[11]);
                    String city = temp[12];
                    String street = temp[13];
                    int house = Integer.parseInt(temp[14]);
                    String building = temp[15];

                    double partner_stock_quantity = Double.parseDouble(temp[16]);
                    String product_name = temp[17];

                    MyProductInPartnerWarehouseModel product = new MyProductInPartnerWarehouseModel(
                            product_id, product_inventory_id, category, product_name, brand,
                            characteristic,
                            type_packaging, unit_measure,  weight_volume, quantity_package,
                            image_url,
                            warehouse_info_id, warehouse_id, city, street, house, building,
                            partner_stock_quantity);

                    listProduct.add(product);

                }
                //сортируем лист по 4 полям
                // (getCategory , getProduct_name, getCharacteristic, getBrand )
                //здесь собраны товары которые находятся на модерации
                listProduct.sort(Comparator.comparing(MyProductInPartnerWarehouseModel::getCategory)
                        .thenComparing(MyProductInPartnerWarehouseModel::getProduct_name)
                        .thenComparing(MyProductInPartnerWarehouseModel::getCharacteristic)
                        .thenComparing(MyProductInPartnerWarehouseModel::getBrand));

                adapter.notifyDataSetChanged();
            }
        }catch (Exception ex){
            Toast.makeText(this, "ex: "+ex, Toast.LENGTH_SHORT).show();
        }
    }
}