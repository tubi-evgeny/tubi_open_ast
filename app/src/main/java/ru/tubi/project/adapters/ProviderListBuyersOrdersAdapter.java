package ru.tubi.project.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.CompoundButtonCompat;
import androidx.recyclerview.widget.RecyclerView;

import ru.tubi.project.R;
import ru.tubi.project.models.CatalogProductProviderModel;

import java.util.List;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.WHITE;
import static ru.tubi.project.free.AllText.IN_PACKAGE;
import static ru.tubi.project.free.AllText.ORDER_PROD;

public class ProviderListBuyersOrdersAdapter
        extends RecyclerView.Adapter<ProviderListBuyersOrdersAdapter.ViewHolder>{

    public interface RecyclerViewClickListener{
        void onClick(View view, int position);
    }

    public interface OnCheckedChangeListener{
        void isChecked(View view, boolean flag, int position);
    }

    private final ProviderListBuyersOrdersAdapter.RecyclerViewClickListener clickListener;
    private final OnCheckedChangeListener checked;

    private final LayoutInflater inflater;
    private final List<CatalogProductProviderModel> products;

    public ProviderListBuyersOrdersAdapter(Context context,List<CatalogProductProviderModel> products,
                  ProviderListBuyersOrdersAdapter.RecyclerViewClickListener clickListener,
                                           OnCheckedChangeListener checked) {
        this.clickListener = clickListener;
        this.inflater = LayoutInflater.from(context);
        this.products = products;
        this.checked=checked;
    }


    @NonNull
    @Override
    public ProviderListBuyersOrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_provider_list_buyers_orders,parent,false);
        return new ProviderListBuyersOrdersAdapter.ViewHolder(view,clickListener,checked);
    }

    @Override
    public void onBindViewHolder(@NonNull ProviderListBuyersOrdersAdapter.ViewHolder holder, int position) {
        CatalogProductProviderModel product=products.get(position);

        String description = product.getCategory()+" "+product.getBrand()+" "+product.getCharacteristic()+" "
                +product.getType_packaging()+" "+product.getWeight_volume()+" "+product.getUnit_measure()+" "
                +IN_PACKAGE+" "+product.getQuantity_package();

        holder.tvDescription.setText(ORDER_PROD+" № "+product.getOrder_id()+"-"+product.getOrder_product_id());
        holder.tvQuantity.setText(""+product.getQuantity());

        int states[][] = {{android.R.attr.state_checked}, {}};
        int colors[] = {R.color.white, R.color.tubi_green_600};
        CompoundButtonCompat.setButtonTintList(holder.checkBox2, new ColorStateList(states, colors));

        if(product.getOrder_product_id() == 900112200){
            holder.llAll.setBackgroundColor(GREEN);
            holder.tvDescription.setText(""+description);
            holder.tvDescription.setTextColor(BLACK);
            holder.tvQuantity.setText(""+product.getQuantity());
            holder.tvQuantity.setTextColor(BLACK);
            if(product.getChecked() == 0){holder.checkBox2.setChecked(false); }
            else{                         holder.checkBox2.setChecked(true);  }
        }else {
            holder.llAll.setBackgroundColor(WHITE);

             if(product.getChecked() == 0){holder.checkBox2.setChecked(false); }
            else{                         holder.checkBox2.setChecked(true);  }
        }
    }

    @Override
    public int getItemCount() {
        //return 4;
         return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView tvDescription,tvQuantity;
        final LinearLayout llAll;
        final CheckBox checkBox2;

        private final ProviderListBuyersOrdersAdapter.RecyclerViewClickListener mListener;
        private final OnCheckedChangeListener mChecked;

        public ViewHolder(@NonNull View itemView,
                     ProviderListBuyersOrdersAdapter.RecyclerViewClickListener listener,
                          OnCheckedChangeListener checked) {
            super(itemView);
            tvDescription=itemView.findViewById(R.id.tvDescription);
            tvQuantity=itemView.findViewById(R.id.tvQuantity);
            llAll=itemView.findViewById(R.id.llAll);
            checkBox2=itemView.findViewById(R.id.checkBox2);

            mListener=listener;
            mChecked=checked;
            tvDescription.setOnClickListener(this);
            checkBox2.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //mListener.onClick(v, getAdapterPosition());
            mChecked.isChecked(v,checkBox2.isChecked(),getAdapterPosition());
        }
    }
}
