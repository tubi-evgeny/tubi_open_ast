package ru.tubi.project.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
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
import ru.tubi.project.models.AcceptProductListProvidersModel;
import ru.tubi.project.utilites.DownloadImage;
import ru.tubi.project.utilites.MakeImageToSquare;

import static ru.tubi.project.activity.Config.ADMIN_PANEL_URL_PREVIEW_IMAGES;
import static ru.tubi.project.free.AllText.IN_PACKAGE;

public class ProviderCollectProductAdapter
        extends RecyclerView.Adapter<ProviderCollectProductAdapter.ViewHolder>{

    public interface OnCheckedChangeListener{
        void isChecked(View view, boolean flag, int position);
    }

    private final ProviderCollectProductAdapter.OnCheckedChangeListener checked;
    private final LayoutInflater inflater;
    private final List<AcceptProductListProvidersModel> productList;


    public ProviderCollectProductAdapter(Context context, List<AcceptProductListProvidersModel> productList,
                                         ProviderCollectProductAdapter.OnCheckedChangeListener checked) {

        this.inflater = LayoutInflater.from(context);
        this.productList = productList;
        this.checked=checked;
    }

    @NonNull
    @Override
    public ProviderCollectProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_partner_collect_product,parent,false);
        return new ProviderCollectProductAdapter.ViewHolder(view,checked);
    }

    @Override
    public void onBindViewHolder(@NonNull ProviderCollectProductAdapter.ViewHolder holder, int position) {
        AcceptProductListProvidersModel product=productList.get(position);
        String stProduct = product.getCategory()+" "+product.getCharacteristic()+" "
                +product.getBrand()+" "+product.getType_packaging()+" "+product.getWeight_volume()+" "
                +product.getUnit_measure()+" "+IN_PACKAGE+" "+product.getQuantity_package();
        double quantityAdd = product.getQuantity_to_order() - product.getQuantity_of_colected();

        holder.llProvider.setVisibility(View.GONE);
        //holder.tvPovider.setText(""+product.getAbbreviation()+" "+product.getCounterparty());
        holder.tvProductInfo.setText(""+stProduct);
        holder.tvPartnerStockQuantity.setText(""+product.getPartner_stock_quantity());
        holder.tvQuantityToOrder.setText(""+product.getQuantity_to_order());
        holder.tvQuantityColected.setText(""+product.getQuantity_of_colected());
        holder.tvQuantityAdd.setText(""+quantityAdd);

        if(!product.getImage_url().equals("null")) {
                new DownloadImage() {
                    @Override
                    protected void onPostExecute(Bitmap result) {
                        try {
                            int width = (int) (result.getWidth());
                            new MakeImageToSquare(result, holder.ivImageProduct);
                        }catch(Exception ex){
                            Log.d("A111","ProviderCollectProductAdapter " +
                                    "/ DownloadImage() / ex = "+ex);
                        }
                    }
                }
                .execute(ADMIN_PANEL_URL_PREVIEW_IMAGES + product.getImage_url());

        }else holder.ivImageProduct.setImageResource(R.drawable.tubi_logo_no_image_300ps);


        if(product.getStorage_conditions().equals("обычное")){
            holder.ivTemperature.setImageResource(R.drawable.temperature_150ps);
        }else if(product.getStorage_conditions().equals("холод")){
            holder.ivTemperature.setImageResource(R.drawable.temperature_5_150ps);
        }else if(product.getStorage_conditions().equals("мороз")){
            holder.ivTemperature.setImageResource(R.drawable.temperature_15_150ps);
        }
        /*if(product.getOrder_active() == 1) {
            if (product.getChecked() == 0) {
                holder.checkBox.setChecked(false);
            } else {
                holder.checkBox.setChecked(true);
                holder.checkBox.setClickable(false);
            }
        }else{
            //holder.checkBox.setChecked(false);
            holder.checkBox.setClickable(false);
        }*/
        holder.checkBox.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final CheckBox checkBox;
        final TextView tvPovider, tvProductInfo,  tvPartnerStockQuantity, tvQuantityToOrder,
                tvQuantityColected, tvQuantityAdd;
        final LinearLayout llProvider;
        final ImageView ivImageProduct,ivTemperature;


        private final ProviderCollectProductAdapter.OnCheckedChangeListener mChecked;


        public ViewHolder(@NonNull View itemView,
                          ProviderCollectProductAdapter.OnCheckedChangeListener checked) {
            super(itemView);
            checkBox=itemView.findViewById(R.id.checkBox);
            tvPovider=itemView.findViewById(R.id.tvPovider);
            tvProductInfo=itemView.findViewById(R.id.tvProductInfo);
            tvPartnerStockQuantity=itemView.findViewById(R.id.tvPartnerStockQuantity);
            tvQuantityToOrder=itemView.findViewById(R.id.tvQuantityToOrder);
            tvQuantityColected=itemView.findViewById(R.id.tvQuantityColected);
            tvQuantityAdd=itemView.findViewById(R.id.tvQuantityAdd);
            ivTemperature=itemView.findViewById(R.id.ivTemperature);
            llProvider=itemView.findViewById(R.id.llProvider);
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
