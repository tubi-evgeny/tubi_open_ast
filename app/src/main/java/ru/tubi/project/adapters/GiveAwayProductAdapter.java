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
import ru.tubi.project.models.TransferModel;
import ru.tubi.project.utilites.DownloadImage;
import ru.tubi.project.utilites.MakeImageToSquare;

import static ru.tubi.project.activity.Config.ADMIN_PANEL_URL_PREVIEW_IMAGES;
import static ru.tubi.project.activity.company_my.GiveAwayProductActivity.invoiceKeyForAdapter;

public class GiveAwayProductAdapter
        extends RecyclerView.Adapter<GiveAwayProductAdapter.ViewHolder>{

    public interface OnCheckedChangeListener{
        void isChecked(View view, boolean flag, int position);
    }

    private final GiveAwayProductAdapter.OnCheckedChangeListener checked;
    private final LayoutInflater inflater;
    private final List<TransferModel> transfer_product_list;
    private final int saveDocInfo;
    //private final List<WarehouseModel> warehouse_info_list;

    public GiveAwayProductAdapter(Context context
            , List<TransferModel> transfer_product_list
            ,int saveDocInfo
            , GiveAwayProductAdapter.OnCheckedChangeListener checked) {

        this.inflater = LayoutInflater.from(context);
        this.transfer_product_list = transfer_product_list;
        this.saveDocInfo=saveDocInfo;
        this.checked=checked;
    }
    @NonNull
    @Override
    public GiveAwayProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_give_away_product,parent,false);
        return new GiveAwayProductAdapter.ViewHolder(view,checked);
    }

    @Override
    public void onBindViewHolder(@NonNull GiveAwayProductAdapter.ViewHolder holder, int position) {
        TransferModel transfer_product=transfer_product_list.get(position);
        String description_full = transfer_product.getDescription_docs()
                +"; ("+transfer_product.getProduct_name_from_provider()+")";

        holder.tvProductInfo.setText(""+description_full);//+" saveDocInfo = "+saveDocInfo+" saveDocInfo2 = "+saveDocInfotwo
        holder.tvQuantity.setText(""+transfer_product.getQuantity());

        if(transfer_product.getOut_active() == 0){
            holder.checkBox.setChecked(false);
        }else{
            holder.checkBox.setChecked(true);
        }

        if(transfer_product.getInvoice_key_id() != 0
                && transfer_product.getInvoice_key_id() != invoiceKeyForAdapter){
            holder.checkBox.setClickable(false);
            holder.llGiveAway.setBackgroundResource(R.drawable.round_corners_yellow);
            holder.tvProductInfo.setText(""+description_full
                    +"\n invoice key = "+transfer_product.getInvoice_key_id());
        }else{
            holder.checkBox.setClickable(true);
            holder.llGiveAway.setBackgroundResource(R.drawable.krugliye_ugli);
        }

        if(!transfer_product.getImage_url().equals("null")) {
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
            }
                    .execute(ADMIN_PANEL_URL_PREVIEW_IMAGES+transfer_product.getImage_url());
        }else holder.ivImageProduct.setImageResource(R.drawable.tubi_logo_no_image_300ps);

    }

    @Override
    public int getItemCount() {
        return transfer_product_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final CheckBox checkBox;
        final TextView  tvProductInfo, tvQuantity;
        final ImageView ivImageProduct;
        final LinearLayout llGiveAway;

        private final GiveAwayProductAdapter.OnCheckedChangeListener mChecked;

        public ViewHolder(@NonNull View itemView,
                          GiveAwayProductAdapter.OnCheckedChangeListener checked) {
            super(itemView);
            checkBox=itemView.findViewById(R.id.checkBox);
            tvProductInfo=itemView.findViewById(R.id.tvProductInfo);
            tvQuantity=itemView.findViewById(R.id.tvQuantity);
            ivImageProduct=itemView.findViewById(R.id.ivImageProduct);
            llGiveAway=itemView.findViewById(R.id.llGiveAway);

            mChecked=checked;
            checkBox.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mChecked.isChecked(v,checkBox.isChecked(),getAdapterPosition());
        }
    }
}
