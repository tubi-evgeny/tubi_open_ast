package ru.tubi.project.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.tubi.project.R;
import ru.tubi.project.models.CarrierPanelModel;
import ru.tubi.project.utilites.DownloadImage;
import ru.tubi.project.utilites.MakeImageToSquare;

import static android.view.View.GONE;
import static ru.tubi.project.Config.ADMIN_PANEL_URL_PREVIEW_IMAGES;
import static ru.tubi.project.free.AllText.C;
import static ru.tubi.project.free.AllText.INVOICE_PRODUCT;
import static ru.tubi.project.free.AllText.IN_PACKAGE;
import static ru.tubi.project.free.AllText.ST;
import static ru.tubi.project.free.AllText.WAREHOUSE;

public class HandOverProductAdapter
        extends RecyclerView.Adapter<HandOverProductAdapter.ViewHolder>{

    public interface OnCheckedChangeListener{
        void isChecked(View view, boolean flag, int position);
    }

    private final HandOverProductAdapter.OnCheckedChangeListener checked;
    private final LayoutInflater inflater;
    private final List<CarrierPanelModel> deliveryList;

    public HandOverProductAdapter(Context context, List<CarrierPanelModel> deliveryList,
                                  HandOverProductAdapter.OnCheckedChangeListener checked) {

        this.inflater = LayoutInflater.from(context);
        this.deliveryList = deliveryList;
        this.checked=checked;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_hand_over_product,parent,false);
        return new HandOverProductAdapter.ViewHolder(view,checked);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CarrierPanelModel delivery=deliveryList.get(position);
        String warehouse = WAREHOUSE+" № "+delivery.getWarehouse_info_id()+"/"+
                delivery.getWarehouse_id()+" "+delivery.getCity()+
                " "+ST+" "+delivery.getStreet()+" "+delivery.getHouse();
        if(!delivery.getBuilding().isEmpty()){
            warehouse += " "+C+". "+delivery.getBuilding();
        }

        String description = delivery.getCategory()+" "+delivery.getBrand()+" "+delivery.getCharacteristic()+" "
                +delivery.getTypePackaging()+" "+delivery.getWeight_volume()+" "+delivery.getUnit_measure()+" "
                +IN_PACKAGE+" "+delivery.getQuantityPackage();

        holder.tvWarehous.setText(""+ warehouse);
        holder.tvDocument_info.setText(""+INVOICE_PRODUCT+" № "+delivery.getDocument_num());
        holder.tvProductInfo.setText(""+description);
        holder.tvQuantity.setText(""+delivery.getQuantity());
        if(!delivery.getImage_url().equals("null")) {
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
                    .execute(ADMIN_PANEL_URL_PREVIEW_IMAGES+delivery.getImage_url());
        }else holder.ivImageProduct.setImageResource(R.drawable.tubi_logo_no_image_300ps);

        if(delivery.getChecked() == 0){
            holder.checkBox.setChecked(false);
            holder.checkBox.setClickable(true);
        }else {
            holder.checkBox.setChecked(true);
            holder.checkBox.setClickable(false);
        }
        try{
            if(delivery.getWarehouse_id() == deliveryList.get(position - 1).getWarehouse_id()){
                holder.llWarehouse.setVisibility(GONE);
            }else {
                holder.llWarehouse.setVisibility(View.VISIBLE);
            }
        }catch (Exception ex){}
        try{
            if(delivery.getDocument_num() == deliveryList.get(position - 1).getDocument_num()){

                if(delivery.getInvoice_key_id() ==
                        deliveryList.get(position - 1).getInvoice_key_id()){
                    holder.llDocument_info.setVisibility(GONE);
                }else{
                    holder.llDocument_info.setVisibility(View.VISIBLE);
                }

            }else {
                holder.llDocument_info.setVisibility(View.VISIBLE);
            }
        }catch(Exception ex){}

        //если поставщик поставил галочку выдано out_active = 1;
        // а волитель еще не принял take_in=0; то
        /*if(delivery.getCheck_out_active() == 1 && delivery.getChecked() == 0){
            holder.llProdInfo.setBackgroundResource(R.drawable.round_corners_green);
        }else{
            holder.llProdInfo.setBackgroundResource(R.drawable.krugliye_ugli);
        }*/
        if(delivery.getDocument_closed() == 1){
            holder.ivCheckmark.setImageResource(R.drawable.checkmark_green_140ps);
            holder.checkBox.setClickable(true);
        }else{
            holder.ivCheckmark.setImageResource(R.drawable.checkmark_gray_140ps);
            holder.checkBox.setClickable(false);
        }
    }

    @Override
    public int getItemCount() {
        return deliveryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        final CheckBox checkBox;
        final ImageView ivCheckmark;
        final TextView tvWarehous, tvDocument_info, tvProductInfo, tvQuantity;
        final ImageView ivImageProduct;
        final LinearLayout llProdInfo,llWarehouse,llDocument_info;

        private final HandOverProductAdapter.OnCheckedChangeListener mChecked;


        public ViewHolder(@NonNull View itemView,
                          HandOverProductAdapter.OnCheckedChangeListener checked) {
            super(itemView);
            llProdInfo=itemView.findViewById(R.id.llProdInfo);
            llWarehouse=itemView.findViewById(R.id.llWarehouse);
            llDocument_info=itemView.findViewById(R.id.llDocument_info);
            checkBox=itemView.findViewById(R.id.checkBox);
            ivCheckmark=itemView.findViewById(R.id.ivCheckmark);
            tvWarehous=itemView.findViewById(R.id.tvWarehous);
            tvDocument_info=itemView.findViewById(R.id.tvDocument_info);
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
}
