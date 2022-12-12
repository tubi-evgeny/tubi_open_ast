package ru.tubi.project.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.tubi.project.R;
import ru.tubi.project.models.OrderModel;
import ru.tubi.project.models.ProviderCollectProductModel;

public class CorrectedQuantityFromDeletedOrdersAdapter
        extends RecyclerView.Adapter<CorrectedQuantityFromDeletedOrdersAdapter.ViewHolder>{

    public interface RecyclerViewClickListener{
        void onClick(View view, int position);
    }

    private final CorrectedQuantityFromDeletedOrdersAdapter.RecyclerViewClickListener clickListener;
    private final LayoutInflater inflater;
    private final List<ProviderCollectProductModel> products;

    public CorrectedQuantityFromDeletedOrdersAdapter(Context context, List<ProviderCollectProductModel> products,
                   CorrectedQuantityFromDeletedOrdersAdapter.RecyclerViewClickListener clickListener) {
        this.clickListener = clickListener;
        this.inflater = LayoutInflater.from(context);
        this.products = products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_deleted_order_goods_quantity,parent,false);
        return new CorrectedQuantityFromDeletedOrdersAdapter.ViewHolder(view,clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProviderCollectProductModel product=products.get(position);

        holder.tvProductInfo.setText(""+product.getProduct_info());
        holder.tvAllOrdersQuantity.setText("в заказе  "+product.getQuantity_full_orders());
        holder.tvQuantityForDeleted.setText(" удалить  "+product.getQuantity_deleted_product());

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView tvProductInfo;
        final TextView tvAllOrdersQuantity;
        final TextView tvQuantityForDeleted;
        final Button btnRejection;
        final Button btnPerform;


        private final CorrectedQuantityFromDeletedOrdersAdapter.RecyclerViewClickListener mListener;

        public ViewHolder(@NonNull View itemView
                , CorrectedQuantityFromDeletedOrdersAdapter.RecyclerViewClickListener listener) {
            super(itemView);
            tvProductInfo=itemView.findViewById(R.id.tvProductInfo);
            tvAllOrdersQuantity=itemView.findViewById(R.id.tvAllOrdersQuantity);
            tvQuantityForDeleted=itemView.findViewById(R.id.tvQuantityForDeleted);
            btnRejection=itemView.findViewById(R.id.btnRejection);
            btnPerform=itemView.findViewById(R.id.btnPerform);

            mListener=listener;
            btnRejection.setOnClickListener(this);
            btnPerform.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onClick(view,getAdapterPosition());
        }
    }
}
