package ru.tubi.project.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import ru.tubi.project.R;
import ru.tubi.project.models.ShopingBoxModel;
import ru.tubi.project.utilites.DownloadImage;
import ru.tubi.project.utilites.FirstSimbolMakeBig;
import ru.tubi.project.utilites.MakeImageToSquare;

import java.util.List;

import static ru.tubi.project.Config.ADMIN_PANEL_URL_IMAGES;
import static ru.tubi.project.Config.ADMIN_PANEL_URL_PREVIEW_IMAGES;
import static ru.tubi.project.free.AllCollor.TUBI_BLACK;
import static ru.tubi.project.free.AllCollor.TUBI_GREY_400;
import static ru.tubi.project.free.AllText.QUANTITY_PACKAGE_SHORT;

public class ShopingBoxAdapter extends RecyclerView.Adapter<ShopingBoxAdapter.ViewHolder> {

    public interface OnShopingBoxClickListener{                                     //Listener
        void onShopingBoxClick(ShopingBoxModel shopingBox, int position);                //
    }                                                                   //
    private final ShopingBoxAdapter.OnShopingBoxClickListener onClickListener;

    public interface RecyclerViewClickListener{
        void onClick(View view, int position);

    }
    private final ShopingBoxAdapter.RecyclerViewClickListener boxClickListener;

    private final LayoutInflater inflater;
    private final List<ShopingBoxModel> allPrices;

    public ShopingBoxAdapter(Context context, List<ShopingBoxModel>allPrices, OnShopingBoxClickListener onClickListener,
                             ShopingBoxAdapter.RecyclerViewClickListener boxClickListener) {
        this.inflater = LayoutInflater.from(context);
        this.allPrices = allPrices;
        this.onClickListener = onClickListener;
        this.boxClickListener=boxClickListener;
    }

    @Override
    public ShopingBoxAdapter.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_shoping_box,parent,false);
        return new ShopingBoxAdapter.ViewHolder(view, boxClickListener);
    }

    @Override
    public void onBindViewHolder(ShopingBoxAdapter.ViewHolder holder, int position) {

        ShopingBoxModel myPrice = allPrices.get(position);
        double price = myPrice.getPrice()+myPrice.getPrice_process();
        double quantity=myPrice.getQuantity();
        String prod_info = myPrice.getCategory()+" "+myPrice.getProduct_name()+" "
                +myPrice.getCharacteristic()+" "+myPrice.getBrand()+", "
                +myPrice.getWeight_volume()+" "+myPrice.getUnit_measure()+", "//GRAMM_SHORT+", "
                +myPrice.getQuantity_package()+" "+QUANTITY_PACKAGE_SHORT;

        if(!myPrice.getImage_url().equals("null")){
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
            }       .execute(ADMIN_PANEL_URL_PREVIEW_IMAGES+myPrice.getImage_url());
          //  new DownloadImage(holder.ivImageProduct)
                  //  .execute((ADMIN_PANEL_URL_PREVIEW_IMAGES+myPrice.getImage_url()));
        }else holder.ivImageProduct.setImageResource(R.drawable.product_bag_250_ps);

        holder.tvProductDescription.setText(""+new FirstSimbolMakeBig()
                .firstSimbolMakeBig(prod_info));
        holder.tvProvider.setText("" + new FirstSimbolMakeBig()
                .firstSimbolMakeBig(myPrice.getCounterparty()));
        holder.tvSumm.setText(String.format("%.2f", +quantity*price));
        //holder.tvWeightVolume.setText(""+myPrice.getWeight_volume());
        //holder.tvUnitMeasure.setText(""+myPrice.getUnit_measure());
        holder.tvPrice.setText(String.format("%.2f",+price));
        holder.tvQuantity.setText(""+quantity);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onShopingBoxClick(myPrice,position);
            }
        });
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
    }//products.size();

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final ImageView ivImageProduct;
        final TextView tvProvider;
        final TextView tvProductDescription;
        final TextView tvPrice;
        final TextView tvQuantity;
        final TextView tvSumm;
        //final TextView tvWeightVolume;
        //final TextView tvUnitMeasure;
        final Button btnMinus;
        final Button btnPlus;

        private final ShopingBoxAdapter.RecyclerViewClickListener mListener;

        public ViewHolder( View itemView, ShopingBoxAdapter.RecyclerViewClickListener listener) {
            super(itemView);

            ivImageProduct=itemView.findViewById(R.id.ivImageProduct);
            tvProvider=itemView.findViewById(R.id.tvProvider);
            tvProductDescription=itemView.findViewById(R.id.tvProductDescription);
            tvPrice=itemView.findViewById(R.id.tvPrice);
            tvQuantity=itemView.findViewById(R.id.tvQuantity);
            tvSumm=itemView.findViewById(R.id.tvSumm);
            //tvWeightVolume=itemView.findViewById(R.id.tvWeightVolume);
            //tvUnitMeasure=itemView.findViewById(R.id.tvUnitMeasure);
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
