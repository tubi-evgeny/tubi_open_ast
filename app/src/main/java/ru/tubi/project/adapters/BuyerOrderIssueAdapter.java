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

import ru.tubi.project.R;

import ru.tubi.project.models.OrderModel;
import ru.tubi.project.utilites.DownloadImage;
import ru.tubi.project.utilites.MakeImageToSquare;

import java.util.List;

import static ru.tubi.project.activity.Config.ADMIN_PANEL_URL_PREVIEW_IMAGES;
import static ru.tubi.project.free.AllCollor.TUBI_BLACK;
import static ru.tubi.project.free.AllCollor.TUBI_GREY_600;

public class BuyerOrderIssueAdapter
        extends RecyclerView.Adapter<BuyerOrderIssueAdapter.ViewHolder>{

    public interface OnCheckedChangeListener{
        void isChecked(View view, boolean flag, int position);
    }

    private final BuyerOrderIssueAdapter.OnCheckedChangeListener checked;
    private final LayoutInflater inflater;
    private final List<OrderModel> productList;

    public BuyerOrderIssueAdapter(Context context, List<OrderModel> productList,
                                  BuyerOrderIssueAdapter.OnCheckedChangeListener checked) {

        this.inflater = LayoutInflater.from(context);
        this.productList = productList;
        this.checked=checked;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_buyer_order_issue,parent,false);
        return new BuyerOrderIssueAdapter.ViewHolder(view,checked);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderModel product=productList.get(position);
        /*String stProduct = product.getCategory()+" "+product.getCharacteristic()+" "
                +product.getBrand()+" "+product.getType_packaging()+" "+product.getWeight_volume()+" "
                +product.getUnit_measure()+" "+IN_PACKAGE+" "+product.getQuantity_package();*/

        holder.tvDescription.setText(""+product.getDescription());
        holder.tvQuantity.setText(""+product.getQuantity_to_order());

        if(!product.getImage_url().equals("null")) {
            new DownloadImage(){
                @Override
                protected void onPostExecute(Bitmap result) {
                    try {
                        int check = result.getWidth();
                        new MakeImageToSquare(result,holder.ivImageProduct);
                    } catch (Exception w) {
                        //bitmap пустой image не найден
                        holder.ivImageProduct.setImageResource(R.drawable.tubi_logo_no_image_300ps);
                    }
                }
            }
                    .execute(ADMIN_PANEL_URL_PREVIEW_IMAGES+product.getImage_url());
        }else holder.ivImageProduct.setImageResource(R.drawable.tubi_logo_no_image_300ps);


        if(product.getChecked() == 1){
            holder.llAll.setBackgroundResource(R.drawable.krugliye_ugli);
            holder.tvDescription.setTextColor(TUBI_GREY_600);
            holder.tvQuantity.setTextColor(TUBI_GREY_600);

            holder.checkBox.setChecked(true);
            holder.checkBox.setClickable(false);
        }else {
            //товар собран на складе партнера  0=нет; 1=да;
            if (product.getCollected() == 0) {
                holder.checkBox.setClickable(false);
                holder.llAll.setBackgroundResource(R.drawable.round_backgraund_white_black);
            } else {
                holder.checkBox.setClickable(true);
                holder.llAll.setBackgroundResource(R.drawable.round_corners_green);
            }
            holder.tvDescription.setTextColor(TUBI_BLACK);
            holder.tvQuantity.setTextColor(TUBI_BLACK);

            holder.checkBox.setChecked(false);
           // holder.checkBox.setClickable(true);
        }

        /*if(product.getChecked() == 999){
            holder.llAll.setBackgroundResource(R.drawable.round_backgraund_gray_200);
           // holder.checkBox.setVisibility(View.GONE);
            holder.checkBox.setClickable(false);
        }else */
        /*if(product.getChecked() == 0){
            holder.checkBox.setChecked(false);
            holder.checkBox.setClickable(true);
        }else{
            holder.checkBox.setChecked(true);
            holder.checkBox.setClickable(false);
        }*/

    }

    @Override
    public int getItemCount() {
         return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final CheckBox checkBox;
        final TextView tvDescription, tvQuantity;
        final ImageView ivImageProduct;
        final LinearLayout llAll;

        private final BuyerOrderIssueAdapter.OnCheckedChangeListener mChecked;


        public ViewHolder(@NonNull View itemView,
                          BuyerOrderIssueAdapter.OnCheckedChangeListener checked) {
            super(itemView);
            llAll=itemView.findViewById(R.id.llAll);
            checkBox=itemView.findViewById(R.id.checkBox);
            tvDescription=itemView.findViewById(R.id.tvDescription);
            tvQuantity=itemView.findViewById(R.id.tvQuantity);
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
