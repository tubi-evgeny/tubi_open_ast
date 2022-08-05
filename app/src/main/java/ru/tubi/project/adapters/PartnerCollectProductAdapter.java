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

import ru.tubi.project.R;
import ru.tubi.project.models.AcceptProductListProvidersModel;
import ru.tubi.project.utilites.DownloadImage;
import ru.tubi.project.utilites.MakeImageToSquare;

import java.util.List;

import static ru.tubi.project.activity.Config.ADMIN_PANEL_URL_PREVIEW_IMAGES;
import static ru.tubi.project.free.AllText.ISSUED_SMALL;

public class PartnerCollectProductAdapter
        extends RecyclerView.Adapter<PartnerCollectProductAdapter.ViewHolder>{

    public interface OnCheckedChangeListener{
        void isChecked(View view, boolean flag, int position);
    }
    public interface OnClickListener{
        void isClicked(View v, int position);
    }

    private final PartnerCollectProductAdapter.OnCheckedChangeListener checked;
    private final PartnerCollectProductAdapter.OnClickListener clicked;
    private final LayoutInflater inflater;
    private final List<AcceptProductListProvidersModel> productList;
    private final List<Integer> checkedList;

    public PartnerCollectProductAdapter(Context context
            ,List<AcceptProductListProvidersModel> productList
            ,List<Integer> checkedList
            ,PartnerCollectProductAdapter.OnCheckedChangeListener checked
            ,PartnerCollectProductAdapter.OnClickListener clicked) {

        this.inflater = LayoutInflater.from(context);
        this.productList = productList;
        this.checkedList = checkedList;
        this.checked=checked;
        this.clicked=clicked;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_partner_collect_product,parent,false);
        return new PartnerCollectProductAdapter.ViewHolder(view,checked);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AcceptProductListProvidersModel product=productList.get(position);
       /* String stProduct = product.getCategory()+" "+product.getCharacteristic()+" "
                +product.getBrand()+" "+product.getType_packaging()+" "+product.getWeight_volume()+" "
                +product.getUnit_measure()+" "+IN_PACKAGE+" "+product.getQuantity_package();*/
        double quantityAdd = product.getQuantity_to_order() - product.getQuantity_of_colected();

        holder.tvPovider.setText(""+product.getAbbreviation()+" "+product.getCounterparty());
        holder.tvProductInfo.setText(""+product.getDescription());
        holder.tvPartnerStockQuantity.setText(""+product.getPartner_stock_quantity()+"|");
        holder.tvQuantityToOrder.setText(""+product.getQuantity_to_order()+"|");
        holder.tvQuantityColected.setText(""+product.getQuantity_of_colected()+"|");
        holder.tvQuantityAdd.setText(""+quantityAdd);

        if(!product.getImage_url().equals("null")) {
            new DownloadImage(){
                @Override
                protected void onPostExecute(Bitmap result) {
                    new MakeImageToSquare(result,holder.ivImageProduct);
                }
            }
                    .execute(ADMIN_PANEL_URL_PREVIEW_IMAGES+product.getImage_url());
        }else holder.ivImageProduct.setImageResource(R.drawable.tubi_logo_no_image_300ps);


        if(product.getStorage_conditions().equals("обычное")){
            holder.ivTemperature.setImageResource(R.drawable.temperature_150ps);
        }else if(product.getStorage_conditions().equals("холод")){
            holder.ivTemperature.setImageResource(R.drawable.temperature_5_150ps);
        }else if(product.getStorage_conditions().equals("мороз")){
            holder.ivTemperature.setImageResource(R.drawable.temperature_15_150ps);
        }
        if(product.getChecked() == 0){
            //holder.checkBox.setChecked(false);
            holder.checkBox.setClickable(true);
        }else{
            //holder.checkBox.setChecked(true);
            holder.checkBox.setClickable(false);
        }
        if(checkedList.get(position) == 0){
            holder.checkBox.setChecked(false);
        }else{
            holder.checkBox.setChecked(true);
        }
        if(product.getOut_active() == 1){
            holder.llItem.setBackgroundResource(R.drawable.round_corners_red);
            holder.tvMessege.setText(""+ISSUED_SMALL);
        }else {
            holder.llItem.setBackgroundResource(R.drawable.krugliye_ugli);
            holder.tvMessege.setText("");
        }
        holder.ivImageProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked.isClicked(v,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final CheckBox checkBox;
        final TextView tvPovider, tvProductInfo,  tvPartnerStockQuantity, tvQuantityToOrder,
                tvQuantityColected, tvQuantityAdd, tvMessege;
        final ImageView ivImageProduct,ivTemperature;
        final LinearLayout llItem;

        private final PartnerCollectProductAdapter.OnCheckedChangeListener mChecked;

        public ViewHolder(@NonNull View itemView,
                          PartnerCollectProductAdapter.OnCheckedChangeListener checked) {
            super(itemView);
            llItem=itemView.findViewById(R.id.llItem);
            checkBox=itemView.findViewById(R.id.checkBox);
            tvPovider=itemView.findViewById(R.id.tvPovider);
            tvProductInfo=itemView.findViewById(R.id.tvProductInfo);
            tvPartnerStockQuantity=itemView.findViewById(R.id.tvPartnerStockQuantity);
            tvQuantityToOrder=itemView.findViewById(R.id.tvQuantityToOrder);
            tvQuantityColected=itemView.findViewById(R.id.tvQuantityColected);
            tvQuantityAdd=itemView.findViewById(R.id.tvQuantityAdd);
            tvMessege=itemView.findViewById(R.id.tvMessege);
            ivTemperature=itemView.findViewById(R.id.ivTemperature);
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
