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

import ru.tubi.project.R;
import ru.tubi.project.models.OrderModel;
import ru.tubi.project.utilites.DownloadImage;
import ru.tubi.project.utilites.FirstSimbolMakeBig;
import ru.tubi.project.utilites.MakeImageToSquare;

import java.util.List;

import static ru.tubi.project.activity.Config.ADMIN_PANEL_URL_PREVIEW_IMAGES;
import static ru.tubi.project.free.AllText.CHENGE;
import static ru.tubi.project.free.AllText.IN_PACKAGE;

public class ShowMyOrderAdapter
        extends RecyclerView.Adapter<ShowMyOrderAdapter.ViewHolder>{

    public interface RecyclerViewClickListener{
        void onClick(View view, int position);
    }
    private final ShowMyOrderAdapter.RecyclerViewClickListener clickListener;
    private final LayoutInflater inflater;
    private final List<OrderModel> productList;

    public ShowMyOrderAdapter(Context context,
                                     List<OrderModel> productList,
                              ShowMyOrderAdapter.RecyclerViewClickListener clickListener ){
        this.inflater=LayoutInflater.from(context);
        this.productList=productList;
        this.clickListener=clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_show_my_order,parent,false);
        return new ShowMyOrderAdapter.ViewHolder(view,clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderModel product = productList.get(position);

        String orderProductInfo = new FirstSimbolMakeBig().firstSimbolMakeBig(product.getCategory())+" "
                +product.getProduct_name()+" " +product.getCharacteristic()+" "+product.getBrand()
                +" "+product.getType_packaging()+" "+product.getWeight_volume()+" "
                +product.getUnit_measure()+" "+IN_PACKAGE+" "+product.getQuantity_package();
        double summ = product.getQuantity_to_order()
                * (product.getPrice() + product.getPrice_process());

        holder.tvProductInfo.setText(""+orderProductInfo);
        holder.tvQuantity.setText(""+product.getQuantity_to_order());
        holder.tvPrice.setText(String.format("%.2f",
                +(product.getPrice() + product.getPrice_process())));
        holder.tvSumm.setText(String.format("%.2f",summ));
        holder.tvPosition.setText(""+(position+1));

        if(!product.getImage_url().equals("null")) {
            new DownloadImage(){
                @Override
                protected void onPostExecute(Bitmap result) {
                    try {
                        int check = result.getWidth();
                        new MakeImageToSquare(result,holder.ivImage);
                    } catch (Exception w) {
                        //bitmap пустой image не найден
                        holder.ivImage.setImageResource(R.drawable.tubi_logo_no_image_300ps);
                    }
                }
            }
                    .execute(ADMIN_PANEL_URL_PREVIEW_IMAGES+product.getImage_url());
        }else holder.ivImage.setImageResource(R.drawable.tubi_logo_no_image_300ps);

        if(product.getCorrected() == 1){
            holder.llMain.setBackgroundResource(R.drawable.round_corners_red);
            holder.tvCorrected.setText(""+CHENGE);
            holder.tvCorrected.setVisibility(View.VISIBLE);
        }else{
            holder.llMain.setBackgroundResource(R.drawable.krugliye_ugli);
            holder.tvCorrected.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tvProductInfo, tvQuantity, tvPrice, tvSumm, tvPosition, tvCorrected;
        private ImageView ivImage;
        private LinearLayout llMain;

        private final ShowMyOrderAdapter.RecyclerViewClickListener mListener;

        public ViewHolder(@NonNull View itemView,
                          ShowMyOrderAdapter.RecyclerViewClickListener listener) {
            super(itemView);
            tvProductInfo=itemView.findViewById(R.id.tvProductInfo);
            tvQuantity=itemView.findViewById(R.id.tvQuantity);
            tvPrice=itemView.findViewById(R.id.tvPrice);
            tvSumm=itemView.findViewById(R.id.tvSumm);
            tvPosition=itemView.findViewById(R.id.tvPosition);
            tvCorrected=itemView.findViewById(R.id.tvCorrected);
            ivImage=itemView.findViewById(R.id.ivImage);
            llMain=itemView.findViewById(R.id.llMain);

            mListener=listener;
            ivImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());
        }
    }
}
