package ru.tubi.project.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.tubi.project.R;
import ru.tubi.project.models.ShopingBoxModel;
import ru.tubi.project.utilites.DownloadImage;

import static ru.tubi.project.activity.Config.ADMIN_PANEL_URL_IMAGES;
import static ru.tubi.project.free.AllCollor.TUBI_BLACK;
import static ru.tubi.project.free.AllCollor.TUBI_GREY_400;

public class ProductInvoiceCreateAdapter
        extends RecyclerView.Adapter<ProductInvoiceCreateAdapter.ViewHolder>{


    public interface RecyclerViewClickListener{
        void onClick(View view, int position);

    }
    private final ProductInvoiceCreateAdapter.RecyclerViewClickListener boxClickListener;

    private final LayoutInflater inflater;
    private final List<ShopingBoxModel> allPrices;

    public ProductInvoiceCreateAdapter(Context context, List<ShopingBoxModel>allPrices,
                                       ProductInvoiceCreateAdapter.RecyclerViewClickListener boxClickListener) {
        this.inflater = LayoutInflater.from(context);
        this.allPrices = allPrices;
        this.boxClickListener=boxClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_shoping_box,parent,false);
        return new ProductInvoiceCreateAdapter.ViewHolder(view, boxClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShopingBoxModel myPrice = allPrices.get(position);

        double price = myPrice.getPrice();
        double quantity=myPrice.getQuantity();
        String category = myPrice.getCategory();
        String brand = myPrice.getBrand();
        String characteristic = myPrice.getCharacteristic();

        if(!myPrice.getImage_url().equals("null")){
            new DownloadImage(holder.ivImageProduct)
                    .execute((ADMIN_PANEL_URL_IMAGES+myPrice.getImage_url()));
        }else holder.ivImageProduct.setImageResource(R.drawable.product_bag_250_ps);
        holder.tvProductDescription.setText(""+category+" "+brand+" "+characteristic);
        holder.tvProvider.setText("" + myPrice.getCounterparty());
        holder.tvSumm.setText(String.format("%.2f", +quantity*price));
        holder.tvWeightVolume.setText(""+myPrice.getWeight_volume());
        holder.tvUnitMeasure.setText(""+myPrice.getUnit_measure());
        holder.tvPrice.setText(""+price);
        holder.tvQuantity.setText(""+quantity);

        if(quantity == 0 ){
            holder.tvQuantity.setTextColor(TUBI_GREY_400);
            holder.tvSumm.setTextColor(TUBI_GREY_400);
        }else{
            holder.tvQuantity.setTextColor(TUBI_BLACK);
            holder.tvSumm.setTextColor(TUBI_BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return allPrices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        final ImageView ivImageProduct;
        final TextView tvProvider;
        final TextView tvProductDescription;
        final TextView tvPrice;
        final TextView tvQuantity;
        final TextView tvSumm;
        final TextView tvWeightVolume;
        final TextView tvUnitMeasure;
        final Button btnMinus;
        final Button btnPlus;

        private final ProductInvoiceCreateAdapter.RecyclerViewClickListener mListener;



        public ViewHolder(@NonNull View itemView,
                          ProductInvoiceCreateAdapter.RecyclerViewClickListener listener) {
            super(itemView);

            ivImageProduct=itemView.findViewById(R.id.ivImageProduct);
            tvProvider=itemView.findViewById(R.id.tvProvider);
            tvProductDescription=itemView.findViewById(R.id.tvProductDescription);
            tvPrice=itemView.findViewById(R.id.tvPrice);
            tvQuantity=itemView.findViewById(R.id.tvQuantity);
            tvSumm=itemView.findViewById(R.id.tvSumm);
            tvWeightVolume=itemView.findViewById(R.id.tvWeightVolume);
            tvUnitMeasure=itemView.findViewById(R.id.tvUnitMeasure);
            btnMinus=itemView.findViewById(R.id.btnMinus);
            btnPlus=itemView.findViewById(R.id.btnPlus);

            mListener=listener;
            btnMinus.setOnClickListener(this);
            btnPlus.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());
        }
    }
}
