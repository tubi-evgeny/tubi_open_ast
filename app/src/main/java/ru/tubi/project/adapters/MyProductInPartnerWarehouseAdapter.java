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
import ru.tubi.project.models.MyProductInPartnerWarehouseModel;
import ru.tubi.project.utilites.DownloadImage;
import ru.tubi.project.utilites.FirstSimbolMakeBig;
import ru.tubi.project.utilites.MakeImageToSquare;

import static ru.tubi.project.Config.ADMIN_PANEL_URL_PREVIEW_IMAGES;
import static ru.tubi.project.free.AllText.BUILDING;
import static ru.tubi.project.free.AllText.IN_PACKAGE;
import static ru.tubi.project.free.AllText.ST;

public class MyProductInPartnerWarehouseAdapter
        extends RecyclerView.Adapter<MyProductInPartnerWarehouseAdapter.ViewHolder>{

    public interface RecyclerViewClickListener{
        void onClick(View view, int position);
    }
    private final MyProductInPartnerWarehouseAdapter.RecyclerViewClickListener clickListener;

    private final LayoutInflater inflater;
    private final List<MyProductInPartnerWarehouseModel> products;

    public MyProductInPartnerWarehouseAdapter(Context context,
                                List<MyProductInPartnerWarehouseModel> products,
                                MyProductInPartnerWarehouseAdapter.RecyclerViewClickListener clickListener ){
        this.inflater=LayoutInflater.from(context);
        this.products=products;
        this.clickListener=clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_my_product_in_partner_warehouse,parent,false);
        return new MyProductInPartnerWarehouseAdapter.ViewHolder(view,clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MyProductInPartnerWarehouseModel product=products.get(position);

        String description = new FirstSimbolMakeBig().firstSimbolMakeBig(product.getCategory())
                +" "+product.getProduct_name()+" "
                +product.getCharacteristic()+" "+product.getBrand()+" "
                +product.getType_packaging()+" "+product.getWeight_volume()+" "
                +product.getUnit_measure()+" "+IN_PACKAGE+" "
                +product.getQuantity_package();
        String warehouseInfo = product.getCity()+" "
                +ST+". "+product.getStreet()+" "+product.getHouse();
        if(!product.getBuilding().isEmpty()){
            warehouseInfo +=" "+BUILDING+" "+product.getBuilding();
        }
        holder.tvDescription.setText(""+description);
        holder.tvWarehouseInfo.setText(""+warehouseInfo);
        holder.tvQuantity.setText(""+product.getQuantity());

        if(!product.getImage_url().equals("null")) {
            new DownloadImage() {
                @Override
                protected void onPostExecute(Bitmap result) {
                    try {
                        int check = result.getWidth();
                        new MakeImageToSquare(result, holder.ivImageProduct);
                    }catch (Exception ex){ //bitmap пустой image не найден
                        holder.ivImageProduct.setImageResource(R.drawable.tubi_logo_no_image_300ps);}
                    //new MakeImageToSquare().MakeImageToSquare(result,holder.ivImageProduct);
                }
            }
                    .execute(ADMIN_PANEL_URL_PREVIEW_IMAGES + product.getImage_url());
        }else holder.ivImageProduct.setImageResource(R.drawable.tubi_logo_no_image_300ps);

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        final TextView tvDescription, tvQuantity, tvWarehouseInfo;
        final ImageView ivImageProduct;
        final LinearLayout llProdInfo, llWarehouse;

        private final MyProductInPartnerWarehouseAdapter.RecyclerViewClickListener mListener;

        public ViewHolder(@NonNull View itemView,
                          MyProductInPartnerWarehouseAdapter.RecyclerViewClickListener listener) {
            super(itemView);
            tvDescription=itemView.findViewById(R.id.tvDescription);
            tvQuantity=itemView.findViewById(R.id.tvQuantity);
            tvWarehouseInfo=itemView.findViewById(R.id.tvWarehouseInfo);
            ivImageProduct=itemView.findViewById(R.id.ivImageProduct);
            llProdInfo=itemView.findViewById(R.id.llProdInfo);
            llWarehouse=itemView.findViewById(R.id.llWarehouse);

            mListener=listener;
            tvDescription.setOnClickListener(this);
            tvQuantity.setOnClickListener(this);
            ivImageProduct.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());
        }
    }
}
