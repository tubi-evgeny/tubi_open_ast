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
import ru.tubi.project.models.AcceptProductListProvidersModel;
import ru.tubi.project.models.ProviderCollectProductModel;
import ru.tubi.project.utilites.DownloadImage;
import ru.tubi.project.utilites.FirstSimbolMakeBig;
import ru.tubi.project.utilites.MakeImageToSquare;

import static ru.tubi.project.Config.ADMIN_PANEL_URL_PREVIEW_IMAGES;
import static ru.tubi.project.free.AllText.CHANGED_TEXT;
import static ru.tubi.project.free.AllText.IN_PACKAGE;

public class ProviderCollectProductDealAdapter
        extends RecyclerView.Adapter<ProviderCollectProductDealAdapter.ViewHolder>{

    public interface OnCheckedChangeListener{
        void isChecked(View view, boolean flag, int position);
    }
    public interface OnClickListener{
        void isClicked(View v, int position);
    }

    private final ProviderCollectProductDealAdapter.OnCheckedChangeListener checked;
    private final ProviderCollectProductDealAdapter.OnClickListener clicked;
    private final LayoutInflater inflater;
    private final List<ProviderCollectProductModel> productList;

    public ProviderCollectProductDealAdapter(Context context
            ,List<ProviderCollectProductModel> productList
            ,ProviderCollectProductDealAdapter.OnCheckedChangeListener checked
            ,ProviderCollectProductDealAdapter.OnClickListener clicked) {

        this.inflater = LayoutInflater.from(context);
        this.productList = productList;
        this.checked=checked;
        this.clicked=clicked;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_provider_collect_product_deal,parent,false);
        return new ProviderCollectProductDealAdapter.ViewHolder(view,checked);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProviderCollectProductModel product=productList.get(position);
        String stProduct = new FirstSimbolMakeBig()
                .firstSimbolMakeBig(product.getProduct_name())+" "
                +product.getCharacteristic()+" "
                +product.getBrand()+" "+product.getType_packaging()+" "
                +product.getWeight_volume()+" "
                +product.getUnit_measure()+" "+IN_PACKAGE+" "+product.getQuantity_package();

        holder.tvProductInfo.setText(""+stProduct);
        holder.tvPartnerStockQuantity.setText(""+product.getProvider_stock_quantity());
        holder.tvQuantityToDeal.setText(""+product.getQuantity_to_deal());
        holder.tvPosition.setText(""+(position+1));
        if(product.getCollected_check() == 1)
              holder.tvQuantityColected.setText(""+product.getQuantity_to_deal());
        else  holder.tvQuantityColected.setText(""+0);

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

        holder.tvQuantityColected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked.isClicked(v,position);
            }
        });

        if (product.getCollected_check() == 0) {
            holder.checkBox.setChecked(false);
            holder.checkBox.setClickable(true);
            holder.tvQuantityColected.setClickable(true);
        } else {
            holder.checkBox.setChecked(true);
            holder.checkBox.setClickable(false);
            holder.tvQuantityColected.setClickable(false);
        }

        if(product.getLogistic_product() == 1){
            holder.ivLogistic.setImageResource(R.drawable.auto_green_150ps);
        }else{
            holder.ivLogistic.setImageResource(R.drawable.auto_grey_150ps);
        }

        if(product.getCorrected() == 1){
            holder.llMain.setBackgroundResource(R.drawable.round_corners_red);
            holder.tvCorrected.setText(""+CHANGED_TEXT);
        }else{
            holder.llMain.setBackgroundResource(R.drawable.krugliye_ugli);
            holder.tvCorrected.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final CheckBox checkBox;
        final TextView tvProductInfo,  tvPartnerStockQuantity, tvQuantityToDeal,
                tvQuantityColected, tvPosition, tvCorrected;//, tvQuantityAdd;//tvPovider,
        final ImageView ivImageProduct,ivLogistic,ivTemperature;
        final LinearLayout llMain;

        private final ProviderCollectProductDealAdapter.OnCheckedChangeListener mChecked;



        public ViewHolder(@NonNull View itemView,
                          ProviderCollectProductDealAdapter.OnCheckedChangeListener checked) {
            super(itemView);
            checkBox=itemView.findViewById(R.id.checkBox);
           // tvPovider=itemView.findViewById(R.id.tvPovider);
            tvProductInfo=itemView.findViewById(R.id.tvProductInfo);
            tvPartnerStockQuantity=itemView.findViewById(R.id.tvPartnerStockQuantity);
            tvQuantityToDeal=itemView.findViewById(R.id.tvQuantityToDeal);
            tvQuantityColected=itemView.findViewById(R.id.tvQuantityColected);
            tvPosition=itemView.findViewById(R.id.tvPosition);
            tvCorrected=itemView.findViewById(R.id.tvCorrected);
            ivTemperature=itemView.findViewById(R.id.ivTemperature);
            ivLogistic=itemView.findViewById(R.id.ivLogistic);
            ivImageProduct=itemView.findViewById(R.id.ivImageProduct);
            llMain=itemView.findViewById(R.id.llMain);

            mChecked=checked;
            checkBox.setOnClickListener(this);
            //tvQuantityColected.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.equals(checkBox)){
                mChecked.isChecked(v,checkBox.isChecked(),getAdapterPosition());
            }
        }
    }
}
