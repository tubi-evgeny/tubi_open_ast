package ru.tubi.project.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.tubi.project.R;

import ru.tubi.project.models.OrderModel;

import java.util.List;

import static ru.tubi.project.free.AllText.BUILDING;
import static ru.tubi.project.free.AllText.ST;
import static ru.tubi.project.free.AllText.WAREHOUSE;

public class DistributionOrdersByWarehousesAdapter
        extends RecyclerView.Adapter<DistributionOrdersByWarehousesAdapter.ViewHolder>{

    public interface RecyclerViewClickListener{
        void onClick(View view, int position);
    }
    private final DistributionOrdersByWarehousesAdapter.RecyclerViewClickListener clickListener;
    private final LayoutInflater inflater;
    private final List<OrderModel> orders;

    public DistributionOrdersByWarehousesAdapter(Context context,
                                  List<OrderModel> orders,
                                                 DistributionOrdersByWarehousesAdapter.RecyclerViewClickListener clickListener ){
        this.inflater=LayoutInflater.from(context);
        this.orders=orders;
        this.clickListener=clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item__distribution_orders_by_warehouse,parent,false);
        return new DistributionOrdersByWarehousesAdapter.ViewHolder(view,clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderModel order = orders.get(position);

        String warehouseInfo = WAREHOUSE+" № "+order.getWarehouse_info_id()+"/"+
                order.getWarehouse_id()+" "+order.getCity()+" "
                +ST+". "+order.getStreet()+" "+order.getHouse();
        if(!order.getBuilding().isEmpty()){
            warehouseInfo +=" "+BUILDING+" "+order.getBuilding();
        }
        holder.tvMyWarehouseInfo.setText(""+warehouseInfo);

    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tvMyWarehouseInfo;

        private final DistributionOrdersByWarehousesAdapter.RecyclerViewClickListener mListener;

        public ViewHolder(@NonNull View itemView,
                          DistributionOrdersByWarehousesAdapter.RecyclerViewClickListener listener) {
            super(itemView);
            tvMyWarehouseInfo=itemView.findViewById(R.id.tvMyWarehouseInfo);

            mListener=listener;

            tvMyWarehouseInfo.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());
        }
    }
}
