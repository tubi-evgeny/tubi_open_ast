package ru.tubi.project.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import ru.tubi.project.R;
import ru.tubi.project.models.CarrierPanelModel;
import ru.tubi.project.utilites.DownloadImage;
import ru.tubi.project.utilites.FirstSimbolMakeBig;
import ru.tubi.project.utilites.InitialData;

import java.util.List;

import ru.tubi.project.utilites.Constant;
import ru.tubi.project.utilites.MakeImageToSquare;

import static android.view.View.GONE;
import static ru.tubi.project.Config.ADMIN_PANEL_URL_PREVIEW_IMAGES;
import static ru.tubi.project.free.AllText.C;
import static ru.tubi.project.free.AllText.IN_PACKAGE;
import static ru.tubi.project.free.AllText.ST;
import static ru.tubi.project.free.AllText.WAREHOUSE;

public class AcceptProductAdapter
        extends RecyclerView.Adapter<AcceptProductAdapter.ViewHolder>{
    private String car_or_warehouse;

    public interface OnCheckedChangeListener{
        void isChecked(View view, boolean flag, int position);
    }

    private final AcceptProductAdapter.OnCheckedChangeListener checked;
    private final LayoutInflater inflater;
    private final List<CarrierPanelModel> acceptProductList;

    public AcceptProductAdapter(Context context, List<CarrierPanelModel> acceptProductList,
                                AcceptProductAdapter.OnCheckedChangeListener checked) {

        this.inflater = LayoutInflater.from(context);
        this.acceptProductList = acceptProductList;
        this.checked=checked;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_accept_product_to_warehouse,parent,false);
        return new AcceptProductAdapter.ViewHolder(view,checked);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CarrierPanelModel product = acceptProductList.get(position);

        String url;
        String whatQuestion;
        url = Constant.PROVIDER_OFFICE;
        url += "receive_out_warehouse_info";
        url += "&" + "warehouse_id=" + product.getOutWarehouse_id();
        whatQuestion = "receive_out_warehouse_info";
        setInitialData(holder, url, whatQuestion);

       /* if(product.getLogistic_product() == 0){
            holder.llCar.setVisibility(GONE);
        }else */
        if (product.getLogistic_product() == 1) {
            //holder.llCar.setVisibility(View.VISIBLE);
            url = Constant.PROVIDER_OFFICE;
            url += "receive_car_info";
            url += "&" + "car_id=" + product.getCar_id();
            whatQuestion = "receive_car_info";
            setInitialData(holder, url, whatQuestion);
        }

        String description = product.getProduct_name() + " "//product.getCategory() + " " +
                + product.getCharacteristic() + " " + product.getBrand() + " "
                + product.getTypePackaging() + " " + product.getWeight_volume() + " " + product.getUnit_measure() + " "
                + IN_PACKAGE + " " + product.getQuantityPackage();

        //  holder.tvWarehous.setText("hi: "+car_or_warehouse);
        holder.tvProductInfo.setText("" + description);
        holder.tvQuantity.setText("" + product.getQuantity());
        holder.tvDocumentInfo.setText(""
                + product.getDocument_name() + " № " + product.getDocument_num());

        if(!product.getImage_url().equals("null")) {
            new DownloadImage(){
                @Override
                protected void onPostExecute(Bitmap result) {
                    try {
                        int check = result.getWidth();
                        new MakeImageToSquare(result,holder.ivImageProduct);
                    } catch (Exception w) {
                        //bitmap пустой image не найден
                        holder.ivImageProduct.setImageResource(R.drawable.tubi_logo_no_image_300ps);
                    }
                }
            }
                    .execute(ADMIN_PANEL_URL_PREVIEW_IMAGES+product.getImage_url());
        }else holder.ivImageProduct.setImageResource(R.drawable.tubi_logo_no_image_300ps);

        if (product.getChecked() == 0) {
            holder.checkBox.setChecked(false);
            holder.checkBox.setClickable(true);
        } else {
            holder.checkBox.setChecked(true);
            holder.checkBox.setClickable(false);
        }
        if (product.getColorDelivery() == 1) {
            holder.llProdInfo.setBackgroundResource(R.drawable.round_corners_green);
        } else holder.llProdInfo.setBackgroundResource(R.drawable.krugliye_ugli);

        //если транспорт есть то скрываем повторения
        if(position != 0){
            try {
                if (product.getLogistic_product() == 1 && product.getCar_id() ==
                        acceptProductList.get(position - 1).getCar_id()) {
                    holder.llCar.setVisibility(GONE);
                } else if (product.getLogistic_product() == 0) {
                    holder.llCar.setVisibility(GONE);
                }else if (product.getCar_id() == 0) {
                    holder.llCar.setVisibility(GONE);
                }  else {
                    holder.llCar.setVisibility(View.VISIBLE);
                }
            } catch (Exception ex) {
            }

            try {
                if (product.getCar_id() ==
                        acceptProductList.get(position - 1).getCar_id() &&
                        product.getOutWarehouse_id() ==
                                acceptProductList.get(position - 1).getOutWarehouse_id()) {
                    holder.llWarehouse.setVisibility(GONE);
                } else{
                    holder.llWarehouse.setVisibility(View.VISIBLE);
                }
            } catch (Exception ex) {
            }
            //название накладной показать/скрыть
            try {
                if (product.getDocument_name()
                        .equals(acceptProductList.get(position - 1).getDocument_name())
                        && product.getDocument_num()
                        == acceptProductList.get(position - 1).getDocument_num()) {

                    if (product.getOutWarehouse_id() ==
                            acceptProductList.get(position - 1).getOutWarehouse_id()) {
                        holder.llDocumentInfo.setVisibility(GONE);
                    } else {
                        holder.llDocumentInfo.setVisibility(View.VISIBLE);
                    }

                } else {
                    holder.llDocumentInfo.setVisibility(View.VISIBLE);
                }
            } catch (Exception ex) {
            }
        }else{
            if (product.getCar_id() == 0) {
                holder.llCar.setVisibility(GONE);
            }else{
                holder.llCar.setVisibility(View.VISIBLE);
            }
            holder.llWarehouse.setVisibility(View.VISIBLE);
            holder.llDocumentInfo.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return acceptProductList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final CheckBox checkBox;
        final TextView tvCarInfo, tvWarehous,  tvProductInfo, tvDocumentInfo, tvQuantity;
        final ImageView ivImageProduct;
        final LinearLayout llCar, llWarehouse, llDocumentInfo, llProdInfo;

        private final AcceptProductAdapter.OnCheckedChangeListener mChecked;


        public ViewHolder(@NonNull View itemView,
                          AcceptProductAdapter.OnCheckedChangeListener checked) {
            super(itemView);
            llCar=itemView.findViewById(R.id.llCar);
            llWarehouse=itemView.findViewById(R.id.llWarehouse);
            llDocumentInfo=itemView.findViewById(R.id.llDocumentInfo);
            llProdInfo=itemView.findViewById(R.id.llProdInfo);
            checkBox=itemView.findViewById(R.id.checkBox);
            tvCarInfo=itemView.findViewById(R.id.tvCarInfo);
            tvWarehous=itemView.findViewById(R.id.tvWarehous);
            tvDocumentInfo=itemView.findViewById(R.id.tvDocumentInfo);
            tvProductInfo=itemView.findViewById(R.id.tvProductInfo);
            tvQuantity=itemView.findViewById(R.id.tvQuantity);
            ivImageProduct=itemView.findViewById(R.id.ivImageProduct);

            mChecked=checked;
            checkBox.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mChecked.isChecked(v,checkBox.isChecked(),getAdapterPosition());
        }
    }
    private void setInitialData(ViewHolder holder, String url_get, String whatQuestion) {

        InitialData task=new InitialData(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            protected void onPostExecute(String result) {
                if(whatQuestion.equals("receive_out_warehouse_info")){
                    splitWarehouseRes(holder, result);
                }else if(whatQuestion.equals("receive_car_info")){
                    splitCarRes(holder,result);
                }

            }
        };
        task.execute(url_get);
    }
    private void splitCarRes(ViewHolder holder,String result){
        String car="";
        try {
            String[] res = result.split("<br>");
            String[] temp = res[0].split("&nbsp");
            String car_brand=temp[0];
            String car_model=temp[1];
            String registration_num=temp[2];

            car = car_brand+" "+car_model+" "+registration_num;

        }catch (Exception ex){       }
        holder.tvCarInfo.setText(""+car);
    }
    private void splitWarehouseRes(ViewHolder holder,String result){
        String warehouse="";
        try {
            warehouse = result;

           /* String[] res = result.split("<br>");
            String[] temp = res[0].split("&nbsp");
            int outWarehouse_id = Integer.parseInt(temp[0]);
            String outCity = temp[1];
            String outStreet = temp[2];
            int outHouse = Integer.parseInt(temp[3]);
            String outBuilding = "";
            try {
                outBuilding = temp[4];
            } catch (Exception ex) {
            }
            warehouse = WAREHOUSE+" № "+outWarehouse_id+" "+outCity+
                " "+ST+" "+outStreet+" "+outHouse;
            if(!outBuilding.isEmpty()){
            warehouse += " "+C+". "+outBuilding;
            }*/
            holder.tvWarehous.setText(""+warehouse);
        }catch (Exception ex){
            Log.d("A111","AcceptProductAdapter / splitWarehouseRes \n"+result);
        }

    }
}
