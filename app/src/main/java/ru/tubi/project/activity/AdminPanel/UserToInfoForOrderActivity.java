package ru.tubi.project.activity.AdminPanel;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ru.tubi.project.R;

import ru.tubi.project.adapters.UserToInfoForOrderAdapter;
import ru.tubi.project.models.NewBuyerCheckModel;
import ru.tubi.project.models.ProductInOrderModel;
import ru.tubi.project.models.UserModel;
import ru.tubi.project.utilites.InitialData;

import java.util.ArrayList;

import ru.tubi.project.utilites.Constant;
import ru.tubi.project.utilites.UserDataRecovery;

import static ru.tubi.project.Config.MY_UID;
import static ru.tubi.project.free.AllCollor.TUBI_GREY_200;
import static ru.tubi.project.free.AllCollor.alert_dialog_button_green_pressed;
import static ru.tubi.project.free.AllText.DONE_BIG;
import static ru.tubi.project.free.AllText.LEAVE_A_COMMENT;
import static ru.tubi.project.free.AllText.LOAD_TEXT;
import static ru.tubi.project.free.AllText.ORDER_APPROVED;
import static ru.tubi.project.free.AllText.ORDER_DELETE_DEFECTIVE;
import static ru.tubi.project.free.AllText.ORDER_NEW_BUYER;
import static ru.tubi.project.free.AllText.RESPONSIBLE_PERSON_TEXT;
import static ru.tubi.project.free.AllText.RETURN_BIG;

public class UserToInfoForOrderActivity extends AppCompatActivity {

    private TextView tvDate, tvBuyerGeneralInfo, tvPhone;
    private Intent takeit;
    private NewBuyerCheckModel buyer;
    private RecyclerView recyclerView;
    private UserToInfoForOrderAdapter adapter;
    private ArrayList<ProductInOrderModel> products=new ArrayList<>();
    private UserToInfoForOrderAdapter.RecyclerViewClickListener clickListener;
    private AlertDialog.Builder adb;
    private AlertDialog ad;
    //private String comment;
    private boolean commentFlag=false;

    private UserModel userDataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_to_info_for_order);
        setTitle(ORDER_NEW_BUYER);//Заказ нов. покупателя

        tvDate=findViewById(R.id.tvDate);
        tvBuyerGeneralInfo=findViewById(R.id.tvBuyerGeneralInfo);
        tvPhone=findViewById(R.id.tvPhone);

        //получить из sqlLite данные пользователя и компании
        UserDataRecovery userDataRecovery = new UserDataRecovery();
        userDataModel = userDataRecovery.getUserDataRecovery(this);


        takeit = getIntent();
        buyer = (NewBuyerCheckModel) takeit.getSerializableExtra("buyer");
        //Toast.makeText(this, "order_id: "+buyer.getCounterparty()+" \n"+buyer.getOrder_id(), Toast.LENGTH_SHORT).show();

        startList();

        tvDate.setText(""+buyer.getCreatedDate());
        tvBuyerGeneralInfo.setText(""+buyer.getAbbreviation()+" "+buyer.getCounterparty()+"\n"
                +RESPONSIBLE_PERSON_TEXT+": "+buyer.getName());
        tvPhone.setText(""+buyer.getPhone());

        clickListener= new UserToInfoForOrderAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                whatButtonClicked(view);
                //Toast.makeText(UserToInfoForOrderActivity.this, "view: "+view+
                  //      " \nposition: "+position, Toast.LENGTH_SHORT).show();
            }
        };

        recyclerView=(RecyclerView)findViewById(R.id.rvList);
        adapter=new UserToInfoForOrderAdapter(this,products,clickListener);
        recyclerView.setAdapter(adapter);

    }
    private void whatButtonClicked(View view){
        String string=String.valueOf(view);
        String str[]=string.split("/");

        if(str[1].equals("tvBtnModerationOk}")){
            alertDialogCommentOk(ORDER_APPROVED);

        }else if(str[1].equals("tvBtnDeleteOrder}")){
            alertDialogCommentDelete(ORDER_DELETE_DEFECTIVE);
        }
    }
    private void orderApproved(String comment){
        String url = Constant.ADMIN_OFFICE;
        url += "order_approved";
        url += "&"+"order_id="+buyer.getOrder_id();
        url += "&"+"user_id="+MY_UID;
        url += "&"+"comment="+comment;
        String whatQuestion = "order_approved";
        setInitialData(url,whatQuestion);
        Intent intent=new Intent(this,NewBuyerCheckActivity.class);
        startActivity(intent);
        Toast.makeText(this, ""+comment, Toast.LENGTH_SHORT).show();
    }
    private void orderDelete(String comment){
        String url = Constant.ADMIN_OFFICE;
        url += "order_delete";
        url += "&"+"order_id="+buyer.getOrder_id();
        url += "&"+"user_id="+userDataModel.getUid();//MY_UID;
        url += "&"+"comment="+comment;
        String whatQuestion = "order_delete";
        setInitialData(url,whatQuestion);
        Intent intent=new Intent(this,NewBuyerCheckActivity.class);
        startActivity(intent);
        Toast.makeText(this, ""+comment, Toast.LENGTH_SHORT).show();
    }

    private void startList() {
        String whatQuestion = "list_product_in_order";
        String url = Constant.ADMIN_OFFICE;
        url += "list_product_in_order";
        url += "&"+"order_id="+buyer.getOrder_id();
        setInitialData(url,whatQuestion);
    }
    private void setInitialData(String url_get, String whatQuestion) {
        ProgressDialog asyncDialog = new ProgressDialog(this);

        InitialData task=new InitialData(){
            @Override
            protected void onPreExecute() {
                //set message of the dialog
                asyncDialog.setMessage(LOAD_TEXT);
                //show dialog
                asyncDialog.show();
                super.onPreExecute();
            }

            protected void onPostExecute(String result) {
                if(whatQuestion.equals("list_product_in_order")){
                    splitResult(result);
                }
                //hide the dialog
                asyncDialog.dismiss();
            }
        };
        task.execute(url_get);
    }
    // разобрать результат с сервера список продуктов
    private void splitResult(String result){
        products.clear();
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
                    double quantity = Double.parseDouble(temp[8]);
                    double price = Double.parseDouble(temp[9]);
                    int quantity_package = Integer.parseInt(temp[10]);
                    String image_url = temp[11];
                    String description = temp[12];
                    if(description.isEmpty()) description="is not";

                    ProductInOrderModel product = new ProductInOrderModel(product_id,
                            product_inventory_id, category, brand, characteristic, type_packaging,
                            unit_measure, weight_volume, quantity, price, quantity_package,
                            image_url, description);
                    //Toast.makeText(this, ""+temp[0], Toast.LENGTH_SHORT).show();
                    products.add(product);
                }
                //startListProduct.addAll(products);
                adapter.notifyDataSetChanged();
            }
        }catch (Exception ex){
            Toast.makeText(this, "ex: "+ex, Toast.LENGTH_SHORT).show();
        }
    }
    private void alertDialogCommentOk(String str){
        adb = new AlertDialog.Builder(this);
        EditText etComment=new EditText(this);
        etComment.requestFocus();

        String st1 = LEAVE_A_COMMENT;
        String st2 = str;
        adb.setTitle(st1);
        adb.setMessage(st2);
        adb.setView(etComment);
        adb.setPositiveButton(DONE_BIG, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                orderApproved(ORDER_APPROVED+": "+etComment.getText().toString());
                ad.cancel();
            }
        });
        adb.setNeutralButton(RETURN_BIG, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ad.cancel();
            }
        });

        ad=adb.create();
        ad.setCanceledOnTouchOutside(false);
        ad.setCancelable(false);
        ad.getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        ad.show();

        Button buttonbackground1 = ad.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonbackground1.setBackgroundColor(alert_dialog_button_green_pressed);
        buttonbackground1.setTextColor(Color.WHITE);
        Button buttonbackground2 = ad.getButton(DialogInterface.BUTTON_NEUTRAL);
        buttonbackground2.setBackgroundColor(TUBI_GREY_200);
        buttonbackground2.setTextColor(Color.WHITE);
    }
    private void alertDialogCommentDelete(String str){
        adb = new AlertDialog.Builder(this);
        EditText etComment=new EditText(this);
        etComment.requestFocus();
        String st1 = LEAVE_A_COMMENT;
        String st2 = str;
        adb.setTitle(st1);
        adb.setMessage(st2);
        adb.setView(etComment);
        adb.setPositiveButton(DONE_BIG, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                orderDelete(ORDER_DELETE_DEFECTIVE+": "+etComment.getText().toString());
                ad.cancel();
            }
        });
        adb.setNeutralButton(RETURN_BIG, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ad.cancel();
            }
        });

        ad=adb.create();
        ad.setCanceledOnTouchOutside(false);
        ad.setCancelable(false);
        ad.getWindow().setSoftInputMode (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        ad.show();

        Button buttonbackground1 = ad.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonbackground1.setBackgroundColor(alert_dialog_button_green_pressed);
        buttonbackground1.setTextColor(Color.WHITE);
        Button buttonbackground2 = ad.getButton(DialogInterface.BUTTON_NEUTRAL);
        buttonbackground2.setBackgroundColor(TUBI_GREY_200);
        buttonbackground2.setTextColor(Color.WHITE);
    }

}