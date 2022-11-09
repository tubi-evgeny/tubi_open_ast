package ru.tubi.project.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import ru.tubi.project.R;
import ru.tubi.project.models.ShopingBoxModel;
import ru.tubi.project.utilites.DownloadImage;
import ru.tubi.project.utilites.FirstSimbolMakeBig;
import ru.tubi.project.utilites.MakeImageToSquare;

import java.util.List;

import static ru.tubi.project.activity.Config.ADMIN_PANEL_URL_PREVIEW_IMAGES;
import static ru.tubi.project.activity.ShopingBox.JOIN_BUY_MEANING;
import static ru.tubi.project.free.AllCollor.TUBI_BLACK;
import static ru.tubi.project.free.AllCollor.TUBI_GREY_400;
import static ru.tubi.project.free.AllText.MORE_SMALL;
import static ru.tubi.project.free.AllText.ORDER_TEXT;
import static ru.tubi.project.free.AllText.QUANTITY_PACKAGE_SHORT;

public class ShopingBoxAdapter extends RecyclerView.Adapter<ShopingBoxAdapter.ViewHolder> {

    public interface OnShopingBoxClickListener{
        void onShopingBoxClick(ShopingBoxModel shopingBox, int position);
    }
    private final ShopingBoxAdapter.OnShopingBoxClickListener onClickListener;

    public interface RecyclerViewClickListener{
        void onClick(View view, int position);

    }
    private final ShopingBoxAdapter.RecyclerViewClickListener boxClickListener;

    private final LayoutInflater inflater;
    private final List<ShopingBoxModel> allPrices;
   // private final int joint_buy;

    public ShopingBoxAdapter(Context context, List<ShopingBoxModel>allPrices, OnShopingBoxClickListener onClickListener,
                             ShopingBoxAdapter.RecyclerViewClickListener boxClickListener){
                    //,int joint_buy) {
        this.inflater = LayoutInflater.from(context);
        this.allPrices = allPrices;
        this.onClickListener = onClickListener;
        this.boxClickListener=boxClickListener;
        //this.joint_buy = joint_buy;
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
        }else holder.ivImageProduct.setImageResource(R.drawable.product_bag_250_ps);

        holder.tvProductDescription.setText(""+myPrice.getProduct_info());
        holder.tvProvider.setText("" + new FirstSimbolMakeBig()
                .firstSimbolMakeBig(myPrice.getCounterparty()));
        holder.tvSumm.setText(String.format("%.2f", +quantity*price));
        holder.tvPrice.setText(String.format("%.2f",+price));
        holder.tvQuantity.setText(""+quantity);

        //если свободные запасы боьше 3х уп то показать только 3 уп
        if(myPrice.getFree_inventory() > (myPrice.getQuantity_package() * 3)){
            holder.tvStocksGoods.setText(""+MORE_SMALL+" "
                    +(myPrice.getQuantity_package() * 3));
        }else{
            //запас меньше 3х упаковок
            holder.tvStocksGoods.setText(""+myPrice.getFree_inventory());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onShopingBoxClick(myPrice,position);
            }
        });
        if(quantity == 0 ){
            holder.tvQuantity.setTextColor(TUBI_GREY_400);
            holder.tvSumm.setTextColor(TUBI_GREY_400);
            holder.tvPlus.setText(ORDER_TEXT+" "+myPrice.getMin_sell());
        }else{
            holder.tvQuantity.setTextColor(TUBI_BLACK);
            holder.tvSumm.setTextColor(TUBI_BLACK);
            holder.tvPlus.setText("+"+myPrice.getMultiple_of());

            if(quantity > myPrice.getMin_sell()){
                holder.tvMinus.setText("-"+myPrice.getMultiple_of());
            }else{
                holder.tvMinus.setText("-"+myPrice.getMin_sell());
            }
        }
        if(JOIN_BUY_MEANING == 1){
            holder.llPlus.setVisibility(View.GONE);
            holder.llMinus.setVisibility(View.GONE);
        }else{
            holder.llPlus.setVisibility(View.VISIBLE);
            holder.llMinus.setVisibility(View.VISIBLE);
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
        final TextView tvQuantity, tvStocksGoods;
        final TextView tvSumm;
        final TextView tvMinus;
        final TextView tvPlus;
        final LinearLayout llMinus, llPlus;

       // final Button btnMinus;
        //final Button btnPlus;

        private final ShopingBoxAdapter.RecyclerViewClickListener mListener;

        public ViewHolder( View itemView, ShopingBoxAdapter.RecyclerViewClickListener listener) {
            super(itemView);

            ivImageProduct=itemView.findViewById(R.id.ivImageProduct);
            tvProvider=itemView.findViewById(R.id.tvProvider);
            tvProductDescription=itemView.findViewById(R.id.tvProductDescription);
            tvPrice=itemView.findViewById(R.id.tvPrice);
            tvQuantity=itemView.findViewById(R.id.tvQuantity);
            tvStocksGoods=itemView.findViewById(R.id.tvStocksGoods);
            tvSumm=itemView.findViewById(R.id.tvSumm);
            tvMinus=itemView.findViewById(R.id.tvMinus);
            tvPlus=itemView.findViewById(R.id.tvPlus);

            llMinus=itemView.findViewById(R.id.llMinus);
            llPlus=itemView.findViewById(R.id.llPlus);

            mListener=listener;
            llMinus.setOnClickListener(this);
            llPlus.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());
        }
    }
}
