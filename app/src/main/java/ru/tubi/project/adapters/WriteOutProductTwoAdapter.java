package ru.tubi.project.adapters;

import android.content.Context;
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
import ru.tubi.project.models.CatalogProductProviderModel;
import ru.tubi.project.utilites.DownloadImage;
import ru.tubi.project.utilites.MakeImageToSquare;

import static ru.tubi.project.activity.Config.ADMIN_PANEL_URL_IMAGES;
import static ru.tubi.project.free.AllText.IN_PACKAGE;

public class WriteOutProductTwoAdapter
        extends RecyclerView.Adapter<WriteOutProductTwoAdapter.ViewHolder>{

    public interface RecyclerViewClickListener{
        void onClick(View view, int position);
    }
    private final WriteOutProductTwoAdapter.RecyclerViewClickListener clickListener;

    private final LayoutInflater inflater;
    private final List<CatalogProductProviderModel> products;

    public WriteOutProductTwoAdapter(Context context,
                                  List<CatalogProductProviderModel> products,

                                     WriteOutProductTwoAdapter.RecyclerViewClickListener clickListener ){
        this.inflater=LayoutInflater.from(context);
        this.products=products;
        this.clickListener=clickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_catalog_stocks,parent,false);
        return new WriteOutProductTwoAdapter.ViewHolder(view,clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)  {
        CatalogProductProviderModel product=products.get(position);

        String description = product.getCategory()+" "+product.getBrand()+" "+product.getCharacteristic()+" "
                +product.getType_packaging()+" "+product.getWeight_volume()+" "+product.getUnit_measure()+" "
                +IN_PACKAGE+" "+product.getQuantity_package();

        holder.tvDescription.setText(""+description);
        holder.tvPrice.setText(""+product.getPrice());
        holder.tvQuantity.setText(""+product.getFree_balance());

        if(!product.getImage_url().equals("null")) {
            new DownloadImage(){
                @Override
                protected void onPostExecute(Bitmap result) {
                    new MakeImageToSquare(result,holder.ivImageProduct);
                }
            }
                    .execute(ADMIN_PANEL_URL_IMAGES+product.getImage_url());
        }else holder.ivImageProduct.setImageResource(R.drawable.tubi_logo_no_image_300ps);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView tvDescription,tvPrice,tvQuantity;
        final ImageView ivImageProduct;
        final LinearLayout llProdInfo;

        private final WriteOutProductTwoAdapter.RecyclerViewClickListener mListener;

        public ViewHolder(@NonNull View itemView,
                          WriteOutProductTwoAdapter.RecyclerViewClickListener listener) {
            super(itemView);
            tvDescription=itemView.findViewById(R.id.tvDescription);
            tvPrice=itemView.findViewById(R.id.tvPrice);
            tvQuantity=itemView.findViewById(R.id.tvQuantity);
            ivImageProduct=itemView.findViewById(R.id.ivImageProduct);
            llProdInfo=itemView.findViewById(R.id.llProdInfo);

            mListener=listener;
            llProdInfo.setOnClickListener(this);
            // tvPrice.setOnClickListener(this);
            //tvQuantity.setOnClickListener(this);
            //ivImageProduct.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());
        }
    }
}
