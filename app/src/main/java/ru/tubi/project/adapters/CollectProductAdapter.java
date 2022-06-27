package ru.tubi.project.adapters;

import android.content.Context;
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
import ru.tubi.project.utilites.FirstSimbolMakeBig;

import java.util.List;

import static ru.tubi.project.free.AllCollor.PINK_100;
import static ru.tubi.project.free.AllCollor.TUBI_BLACK;
import static ru.tubi.project.free.AllCollor.TUBI_GREY_600;
import static ru.tubi.project.free.AllCollor.TUBI_WHITE;
import static ru.tubi.project.free.AllText.IN_PACKAGE;

public class CollectProductAdapter
        extends RecyclerView.Adapter<CollectProductAdapter.ViewHolder>{

    public interface RecyclerViewClickListener{
        void onClick(View view, int position);
    }

    public interface OnCheckedChangeListener{
        void isChecked(View view, boolean flag, int position);
    }


    private final CollectProductAdapter.RecyclerViewClickListener clickListener;
    private final CollectProductAdapter.OnCheckedChangeListener checked;

    private final LayoutInflater inflater;
    private final List<OrderModel> products;

    public CollectProductAdapter(Context context,List<OrderModel> products,
                                 CollectProductAdapter.RecyclerViewClickListener clickListener,
                                 CollectProductAdapter.OnCheckedChangeListener checked) {
        this.clickListener = clickListener;
        this.inflater = LayoutInflater.from(context);
        this.products = products;
        this.checked=checked;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_collect_product,parent,false);
        return new CollectProductAdapter.ViewHolder(view,clickListener,checked);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderModel product=products.get(position);

        String orderProductInfo = new FirstSimbolMakeBig().firstSimbolMakeBig(product.getCategory())
                +" "+product.getProduct_name()+" "+product.getCharacteristic()
                +" "+new FirstSimbolMakeBig().firstSimbolMakeBig(product.getBrand())
                +" "+product.getType_packaging()+" "+product.getWeight_volume()+" "+product.getUnit_measure()
                +" "+IN_PACKAGE+" "+product.getQuantity_package();

        holder.tvDescription.setText(""+orderProductInfo);
        holder.tvQuantity.setText(""+product.getQuantity());

        if(product.getChecked() == 0){
            holder.checkBox2.setChecked(false);
            holder.checkBox2.setClickable(true);
            holder.tvDescription.setTextColor(TUBI_BLACK);
            holder.tvQuantity.setTextColor(TUBI_BLACK);
        }
        else{
            holder.checkBox2.setChecked(true);
            holder.checkBox2.setClickable(false);
            //CompoundButtonCompat.setButtonTintList(holder.checkBox2, new ColorStateList(states, colors));
            holder.tvDescription.setTextColor(TUBI_GREY_600);
            holder.tvQuantity.setTextColor(TUBI_GREY_600);
        }
        //если колличество для сборки = 0
        if(product.getQuantity() == 0){ holder.checkBox2.setClickable(false);}
        //если надо уменьшить(убрать лишнее) собранный товар
        if(product.getQuantity() < 0){
            holder.tvDescription.setBackgroundColor(PINK_100);
        }else{
            holder.tvDescription.setBackgroundColor(TUBI_WHITE);
        }
        if(product.getLogistic_product() == 1){
            holder.ivDelivery.setImageResource(R.drawable.auto_green_150ps);
        }else holder.ivDelivery.setImageResource(R.drawable.auto_grey_150ps);

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView tvDescription,tvQuantity;
        final LinearLayout llAll;
        final CheckBox checkBox2;
        final ImageView ivDelivery;

        private final CollectProductAdapter.RecyclerViewClickListener mListener;
        private final CollectProductAdapter.OnCheckedChangeListener mChecked;

        public ViewHolder(@NonNull View itemView,
                          CollectProductAdapter.RecyclerViewClickListener listener,
                          CollectProductAdapter.OnCheckedChangeListener checked) {
            super(itemView);
            tvDescription=itemView.findViewById(R.id.tvDescription);
            tvQuantity=itemView.findViewById(R.id.tvQuantity);
            llAll=itemView.findViewById(R.id.llAll);
            checkBox2=itemView.findViewById(R.id.checkBox2);
            ivDelivery=itemView.findViewById(R.id.ivDelivery);

            mListener=listener;
            mChecked=checked;
            tvDescription.setOnClickListener(this);
            checkBox2.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mChecked.isChecked(v,checkBox2.isChecked(),getAdapterPosition());
        }
    }
}
