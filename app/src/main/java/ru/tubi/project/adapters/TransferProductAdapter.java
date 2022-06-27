package ru.tubi.project.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
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

import ru.tubi.project.models.TransferModel;
import ru.tubi.project.models.WarehouseModel;
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

public class TransferProductAdapter
        extends RecyclerView.Adapter<TransferProductAdapter.ViewHolder>{

    public interface OnCheckedChangeListener{
        void isChecked(View view, boolean flag, int position);
    }

    private final TransferProductAdapter.OnCheckedChangeListener checked;
    private final LayoutInflater inflater;
    private final List<TransferModel> transfer_product_list;
    private final List<WarehouseModel> warehouse_info_list;

    public TransferProductAdapter(Context context, List<TransferModel> transfer_product_list,
                                  List<WarehouseModel> warehouse_info_list,
                                  TransferProductAdapter.OnCheckedChangeListener checked) {

        this.inflater = LayoutInflater.from(context);
        this.transfer_product_list = transfer_product_list;
        this.warehouse_info_list=warehouse_info_list;
        this.checked=checked;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_transfer_product,parent,false);
        return new TransferProductAdapter.ViewHolder(view,checked);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TransferModel transfer_product=transfer_product_list.get(position);
        int warehouseListPosition = receiveWarehouseListPosition(transfer_product.getIn_warehouse_id());
        int product_inventory_id = transfer_product.getProductInventory_id();

        WarehouseModel warehouse_info = warehouse_info_list.get(warehouseListPosition);

        int  car_id = transfer_product.getCar_id();

        String warehouse = WAREHOUSE+" № "+warehouse_info.getWarehouse_info_id()+"/"+
                warehouse_info.getWarehouse_id()+" "+warehouse_info.getCity()+
                " "+ST+" "+warehouse_info.getStreet()+" "+warehouse_info.getHouse();
        if(!warehouse_info.getBuilding().isEmpty()){
            warehouse += " "+C+". "+warehouse_info.getBuilding();
        }

        if(car_id != 0){
            //getCarInfo(holder,car_id);
            holder.tvCar.setText(""+transfer_product.getCar_info());
            holder.llCar.setVisibility(View.VISIBLE);
        }else{
            holder.llCar.setVisibility(View.GONE);
        }
        holder.tvWarehous.setText(""+ warehouse);

        //getProductInfo(holder, product_inventory_id);
        holder.tvProductInfo.setText(""+transfer_product.getDescription_docs()
                +" key="+transfer_product.getInvoice_key_id());

        holder.tvQuantity.setText(""+transfer_product.getQuantity());

        if(transfer_product.getOut_active_check() == 0){
            holder.checkBox.setChecked(false);
            holder.checkBox.setClickable(true);
        }else if(transfer_product.getOut_active_check() ==1){
            holder.checkBox.setChecked(true);
            holder.checkBox.setClickable(true);
        }

        try{
            if(transfer_product.getCar_id() == transfer_product_list.get(position - 1).getCar_id()){
                holder.llCar.setVisibility(GONE);
            }else holder.llCar.setVisibility(View.VISIBLE);
        }catch (Exception ex){}
        try{
            if(transfer_product.getIn_warehouse_id() == transfer_product_list.get(position - 1).getIn_warehouse_id()){
                holder.llWarehouse.setVisibility(GONE);
                holder.llInvoiceNum.setVisibility(GONE);
                if(transfer_product.getCar_id() != transfer_product_list.get(position - 1).getCar_id()){
                    holder.llWarehouse.setVisibility(View.VISIBLE);
                    holder.llInvoiceNum.setVisibility(View.VISIBLE);
                }
            }else holder.llWarehouse.setVisibility(View.VISIBLE);
        }catch (Exception ex){}

    }

    @Override
    public int getItemCount() {
        return transfer_product_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final CheckBox checkBox;
        final TextView tvCar, tvWarehous,  tvProductInfo, tvQuantity;
        final ImageView ivImageProduct;
        final LinearLayout llCar,llWarehouse,llInvoiceNum;

        private final TransferProductAdapter.OnCheckedChangeListener mChecked;


        public ViewHolder(@NonNull View itemView,
                          TransferProductAdapter.OnCheckedChangeListener checked) {
            super(itemView);

            llWarehouse=itemView.findViewById(R.id.llWarehouse);
            llInvoiceNum=itemView.findViewById(R.id.llInvoiceNum);
            llCar=itemView.findViewById(R.id.llCar);
            checkBox=itemView.findViewById(R.id.checkBox);
            tvCar=itemView.findViewById(R.id.tvCar);
            tvWarehous=itemView.findViewById(R.id.tvWarehous);
            tvProductInfo=itemView.findViewById(R.id.tvProductInfo);
            tvQuantity=itemView.findViewById(R.id.tvQuantity);
            ivImageProduct=itemView.findViewById(R.id.ivImageProduct);


            mChecked=checked;
            checkBox.setOnClickListener(this);
            llInvoiceNum.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mChecked.isChecked(v,checkBox.isChecked(),getAdapterPosition());
        }
    }
    private int receiveWarehouseListPosition(int warehouse_id){
        int warehouseListPosition = 0;
        for(int i=0;i < warehouse_info_list.size();i++){
            if(warehouse_info_list.get(i).getWarehouse_id() == warehouse_id){
                warehouseListPosition = i;
                i=warehouse_info_list.size();
            }
        }
        return warehouseListPosition;
    }
    private void getCarInfo(ViewHolder holder,int car_id){
        String url = Constant.CARRIER_OFFICE;
        url += "get_car_info";
        url += "&" + "car_id=" + car_id;
        String whatQuestion = "get_car_info";
        setInitialData(holder, url, whatQuestion);
    }
    private void getProductInfo(ViewHolder holder, int product_inventory_id) {
            String url = Constant.PROVIDER_OFFICE;
            url += "get_product_info";
            url += "&" + "product_inventory_id=" + product_inventory_id;
            String whatQuestion = "get_product_info";
            setInitialData(holder, url, whatQuestion);
    }
    private void setInitialData(TransferProductAdapter.ViewHolder holder, String url_get, String whatQuestion) {

        InitialData task=new InitialData(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            protected void onPostExecute(String result) {
                if(whatQuestion.equals("get_product_info")){
                    splitProductRes(holder, result);
                }else if(whatQuestion.equals("get_car_info")){
                    splitCarResult(holder,result);
                }

            }
        };
        task.execute(url_get);
    }
    private void splitCarResult(ViewHolder holder,String result){
        String carInfo="";
        try {
            String[] res = result.split("<br>");
            String[] temp = res[0].split("&nbsp");
            String car_brand=temp[0];
            String car_model=temp[1];
            String registration_num=temp[2];


            carInfo = car_brand+" "+car_model+" "+registration_num;

        }catch (Exception ex){       }
        holder.tvCar.setText(""+carInfo);
    }
    private void splitProductRes(TransferProductAdapter.ViewHolder holder, String result){
        String productInfo="";
        String image_url=null;
        try {
            String[] res = result.split("<br>");
            String[] temp = res[0].split("&nbsp");
            String category=temp[0];
            String brand=temp[1];
            String characteristic=temp[2];
            String type_packaging = temp[3];
            String unit_measure=temp[4];
            String weight_volume=temp[5];
            String quantity_package=temp[6];
            String product_name = temp[7];
            image_url = temp[8];

            productInfo = new FirstSimbolMakeBig().firstSimbolMakeBig(category)+" "
                    +product_name+" "+characteristic+" "+brand+" "+type_packaging+" "
                    +weight_volume+" "+unit_measure+" "+IN_PACKAGE+" "+quantity_package;

        }catch (Exception ex){       }
        holder.tvProductInfo.setText(""+productInfo);

        if(!image_url.equals(null)) {
            new DownloadImage(){
                @Override
                protected void onPostExecute(Bitmap result) {
                    try {
                        int check = result.getWidth();
                        new MakeImageToSquare(result, holder.ivImageProduct);
                    }catch (Exception w) {
                        //bitmap пустой image не найден
                        holder.ivImageProduct.setImageResource(R.drawable.tubi_logo_no_image_300ps);
                    }
                }
            }
                    .execute(ADMIN_PANEL_URL_PREVIEW_IMAGES+image_url);
        }else holder.ivImageProduct.setImageResource(R.drawable.tubi_logo_no_image_300ps);
    }
}
