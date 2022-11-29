package ru.tubi.project.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.tubi.project.R;
import ru.tubi.project.models.CatalogProductProviderModel;
import ru.tubi.project.utilites.DownloadImage;
import ru.tubi.project.utilites.MakeImageToSquare;

import java.util.List;

import static ru.tubi.project.activity.Config.ADMIN_PANEL_URL_PREVIEW_IMAGES;
import static ru.tubi.project.free.AllCollor.TUBI_GREY_100;
import static ru.tubi.project.free.AllCollor.TUBI_WHITE;
import static ru.tubi.project.free.AllText.IN_PACKAGE;
import static ru.tubi.project.free.AllText.PACKAGE_SHORT;

public class CatalogStocksAdapter
        extends RecyclerView.Adapter<CatalogStocksAdapter.ViewHolder>{

    public interface RecyclerViewClickListener{
        void onClick(View view, int position);
    }
    private final RecyclerViewClickListener clickListener;

    private final LayoutInflater inflater;
    private final List<CatalogProductProviderModel> products;

    public CatalogStocksAdapter(Context context,
                                List<CatalogProductProviderModel> products,

                                RecyclerViewClickListener clickListener ){
        this.inflater=LayoutInflater.from(context);
        this.products=products;
        this.clickListener=clickListener;
    }
    @NonNull
    @Override
    public CatalogStocksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_catalog_stocks,parent,false);
        return new ViewHolder(view,clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CatalogStocksAdapter.ViewHolder holder, int position) {
        CatalogProductProviderModel product=products.get(position);

        String description = product.getProduct_name()+" "
                +product.getCharacteristic()+" "+product.getBrand()+" "
                +product.getType_packaging()+" "+product.getWeight_volume()+" "
                +product.getUnit_measure()+" "
                +IN_PACKAGE+" "+product.getQuantity_package();//product.getCategory()
        String strPackageCount = String.format("%.2f",+(product.getFree_balance() / product.getQuantity_package()));

        holder.tvDescription.setText(""+product.getProduct_info()+"("+product.getProduct_name_from_provider()+")");
        holder.tvPrice.setText(""+product.getPrice());
        holder.tvQuantity.setText(""+product.getFree_balance());
        holder.tvPosition.setText(""+(position+1));
        holder.tvPackageCount.setText("("+strPackageCount+" "+PACKAGE_SHORT+")");

        if (!product.getImage_url().equals("null") && product.getBmt() == null) {
            new DownloadImage() {
                @Override
                protected void onPostExecute(Bitmap result) {
                    try {
                        int check = result.getWidth();
                        new MakeImageToSquare(result, holder.ivImageProduct);
                        products.get(position).setBmt(result);
                    } catch (Exception ex) {
                        //bitmap пустой image не найден
                        holder.ivImageProduct.setImageResource(R.drawable.tubi_logo_no_image_300ps);
                    }
                }
            }
            .execute(ADMIN_PANEL_URL_PREVIEW_IMAGES + product.getImage_url());
        }
        else if(product.getBmt() != null){
            new MakeImageToSquare(product.getBmt(), holder.ivImageProduct);
            //Log.d("A111","CatalogStocksAdapter / product.getBmt()");
        }
        else holder.ivImageProduct.setImageResource(R.drawable.tubi_logo_no_image_300ps);

        if(product.getProduct_id() == 0){
            holder.llProdInfo.setBackgroundColor(TUBI_GREY_100);
            /*holder.tvDescription.setBackgroundColor(TUBI_GREY_100);
            holder.tvPrice.setBackgroundColor(TUBI_GREY_100);
            holder.tvQuantity.setBackgroundColor(TUBI_GREY_100);*/

            holder.tvDescription.setClickable(false);
            holder.tvPrice.setClickable(false);
            holder.tvQuantity.setClickable(false);
            holder.ivImageProduct.setClickable(false);
        }else {
            holder.llProdInfo.setBackgroundColor(TUBI_WHITE);
           /* holder.tvDescription.setBackgroundColor(TUBI_WHITE);
            holder.tvPrice.setBackgroundColor(TUBI_WHITE);
            holder.tvQuantity.setBackgroundColor(TUBI_WHITE);*/

            holder.tvDescription.setClickable(true);
            holder.tvPrice.setClickable(true);
            holder.tvQuantity.setClickable(true);
            holder.ivImageProduct.setClickable(true);
        }
      /*  holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(v,position);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return products.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        final TextView tvDescription,tvPrice,tvQuantity, tvPosition, tvPackageCount;
        final ImageView ivImageProduct;
        final LinearLayout llProdInfo;

        private final CatalogStocksAdapter.RecyclerViewClickListener mListener;

        public ViewHolder(@NonNull View itemView,
                          CatalogStocksAdapter.RecyclerViewClickListener listener) {
            super(itemView);
            tvDescription=itemView.findViewById(R.id.tvDescription);
            tvPrice=itemView.findViewById(R.id.tvPrice);
            tvQuantity=itemView.findViewById(R.id.tvQuantity);
            tvPosition=itemView.findViewById(R.id.tvPosition);
            tvPackageCount=itemView.findViewById(R.id.tvPackageCount);
            ivImageProduct=itemView.findViewById(R.id.ivImageProduct);
            llProdInfo=itemView.findViewById(R.id.llProdInfo);

            mListener=listener;
            tvDescription.setOnClickListener(this);
            tvPrice.setOnClickListener(this);
            tvQuantity.setOnClickListener(this);
            ivImageProduct.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            mListener.onClick(v, getAdapterPosition());
        }
    }
}
