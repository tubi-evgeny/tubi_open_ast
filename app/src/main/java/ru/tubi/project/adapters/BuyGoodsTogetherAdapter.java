package ru.tubi.project.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.tubi.project.R;
import ru.tubi.project.models.ProductCardModel;
import ru.tubi.project.utilites.DownloadImage;
import ru.tubi.project.utilites.MakeImageToSquare;

import static ru.tubi.project.activity.Config.ADMIN_PANEL_URL_PREVIEW_IMAGES;

public class BuyGoodsTogetherAdapter extends RecyclerView.Adapter<BuyGoodsTogetherAdapter.ViewHolder> {

    public interface RecyclerViewClickListener{
        void onClick(View view, int position);

    }
    private final BuyGoodsTogetherAdapter.RecyclerViewClickListener clickListener;

    private final LayoutInflater inflater;
    private final List<ProductCardModel> products;

    public BuyGoodsTogetherAdapter(Context context, List<ProductCardModel> products,
                                   BuyGoodsTogetherAdapter.RecyclerViewClickListener clickListener) {
        this.inflater =  LayoutInflater.from(context);
        this.products = products;
        this.clickListener=clickListener;
    }
    @NonNull
    @Override
    public BuyGoodsTogetherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item__buy_goods_together,parent,false);
       /* int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = width/2;
        view.setLayoutParams(params);
        LinearLayout ll = view.findViewById(R.id.llParent);*/
        return new BuyGoodsTogetherAdapter.ViewHolder(view,clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BuyGoodsTogetherAdapter.ViewHolder holder, int position) {
        ProductCardModel product=products.get(position);
        double price = product.getPrice() + product.getProcess_price();


        holder.tvQuantity.setText(""+(product.getMin_sell()-product.getQuantity_joint()));
        holder.tvPrice.setText(""+String.format("%.2f",+price));
        if(!product.getImage_url().equals("null")) {
            new DownloadImage(){
                @Override
                protected void onPostExecute(Bitmap result) {
                    try {
                        int check = result.getWidth();
                        new MakeImageToSquare(result, holder.ivImage);
                    }catch (Exception w) {
                        //bitmap пустой image не найден
                        holder.ivImage.setImageResource(R.drawable.tubi_logo_no_image_300ps);
                    }
                }
            }
                    .execute(ADMIN_PANEL_URL_PREVIEW_IMAGES+product.getImage_url());
        }else holder.ivImage.setImageResource(R.drawable.tubi_logo_no_image_300ps);

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final BuyGoodsTogetherAdapter.RecyclerViewClickListener mListener;
        final ImageView ivImage;
        final TextView tvQuantity;
        final TextView tvPrice;
        final LinearLayout llParent;

        public ViewHolder(@NonNull View itemView, BuyGoodsTogetherAdapter.RecyclerViewClickListener listener) {
            super(itemView);
            mListener=listener;
            ivImage=itemView.findViewById(R.id.ivImage);
            tvQuantity=itemView.findViewById(R.id.tvQuantity);
            tvPrice=itemView.findViewById(R.id.tvPrice);
            llParent=itemView.findViewById(R.id.llParent);

            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            ViewGroup.LayoutParams params = llParent.getLayoutParams();
            params.height = (int) (width*0.45);
            llParent.setLayoutParams(params);
            params = ivImage.getLayoutParams();
            params.height = width/3;
            ivImage.setLayoutParams(params);

            llParent.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());
        }
    }
}
