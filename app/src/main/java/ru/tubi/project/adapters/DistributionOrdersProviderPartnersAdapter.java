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
import ru.tubi.project.utilites.FirstSimbolMakeBig;

import java.util.List;

import static ru.tubi.project.free.AllCollor.PINK_100;
import static ru.tubi.project.free.AllCollor.TUBI_BLACK;
import static ru.tubi.project.free.AllCollor.TUBI_GREY_600;
import static ru.tubi.project.free.AllCollor.TUBI_WHITE;
import static ru.tubi.project.free.AllCollor.TUBI_YELLOW_200;
import static ru.tubi.project.free.AllText.ASSEAMBLE_PRODUCT_FOR;
import static ru.tubi.project.free.AllText.BUILDING;
import static ru.tubi.project.free.AllText.IN_PACKAGE;
import static ru.tubi.project.free.AllText.ST;
import static ru.tubi.project.free.AllText.WAREHOUSE;

public class DistributionOrdersProviderPartnersAdapter
        extends RecyclerView.Adapter<DistributionOrdersProviderPartnersAdapter.ViewHolder>{

    public interface RecyclerViewClickListener{
        void onClick(View view, int position);
    }

    private final DistributionOrdersProviderPartnersAdapter.RecyclerViewClickListener clickListener;
    private final LayoutInflater inflater;
    private final List<OrderModel> orders;

    public DistributionOrdersProviderPartnersAdapter(Context context,
                                                 List<OrderModel> orders,
                                                     DistributionOrdersProviderPartnersAdapter.RecyclerViewClickListener clickListener ){
        this.inflater=LayoutInflater.from(context);
        this.orders=orders;
        this.clickListener=clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_distribution_orders_provider_partners,parent,false);
        return new DistributionOrdersProviderPartnersAdapter.ViewHolder(view,clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderModel order = orders.get(position);

        if(order.getWarehouse_id() == 000){
            String prowiderWorehouseInfo = ASSEAMBLE_PRODUCT_FOR+"   \n"
                    + WAREHOUSE+" № "+order.getWarehouse_info_id()+"/"+order.getPartner_warehouse_id()+" \n"+
                    order.getCity()+" "+ST+" "+order.getStreet()+" "+order.getHouse();
            if(!order.getBuilding().isEmpty()){
                prowiderWorehouseInfo += " "+BUILDING+" "+order.getBuilding();
            }
            holder.tvOrderProductInfo.setText(""+ prowiderWorehouseInfo);
            holder.tvOrderProductInfo.setBackgroundColor(TUBI_YELLOW_200);
            holder.tvOrderProductInfo.setTextColor(TUBI_BLACK);
            holder.tvOrderProductInfo.setTextSize(18);
            holder.tvOrderQuantity.setVisibility(View.GONE);
            holder.tvAddToOrder.setVisibility(View.GONE);
        }else {
            String orderProductInfo = new FirstSimbolMakeBig()
                    .firstSimbolMakeBig(order.getCategory())+" "+order.getProduct_name()+" "
                    +order.getCharacteristic()+" "
                    +new FirstSimbolMakeBig().firstSimbolMakeBig(order.getBrand())+" "
                    +order.getType_packaging()+" "+order.getWeight_volume()+" "
                    +order.getUnit_measure()+" "+IN_PACKAGE+" "+order.getQuantity_package();

            double quantity_to_order = order.getQuantity_to_order();
            double partner_stock_quantity = order.getPartner_stock_quantity();
            double quantityToCollect = order.getQuantityToCollect();
            double quantity_give_away_bad_do_not_receive=order.getQuantity_give_away_bad_do_not_receive();
            double add_to_order = 0;
            if(quantity_to_order > (partner_stock_quantity + quantity_give_away_bad_do_not_receive)){
                add_to_order = quantity_to_order - partner_stock_quantity
                        - quantityToCollect - quantity_give_away_bad_do_not_receive;
            }

            holder.tvOrderProductInfo.setText("" + orderProductInfo);
            holder.tvOrderProductInfo.setBackgroundColor(TUBI_WHITE);
            holder.tvOrderProductInfo.setTextColor(TUBI_GREY_600);
            holder.tvOrderProductInfo.setTextSize(16);
            holder.tvOrderQuantity.setText(""+quantity_to_order);
            holder.tvAddToOrder.setText(""+ add_to_order);
            holder.tvOrderQuantity.setVisibility(View.VISIBLE);
            holder.tvAddToOrder.setVisibility(View.VISIBLE);
            if(add_to_order > 0){
                holder.tvAddToOrder.setTextSize(22);
                holder.tvAddToOrder.setTextColor(TUBI_BLACK);
            }else if(add_to_order < 0){
                holder.tvOrderProductInfo.setBackgroundColor(PINK_100);
                holder.tvOrderProductInfo.setTextColor(TUBI_BLACK);
            }else{
                holder.tvAddToOrder.setTextSize(18);
                holder.tvAddToOrder.setTextColor(TUBI_GREY_600);
                holder.tvOrderProductInfo.setBackgroundColor(TUBI_WHITE);
                holder.tvOrderProductInfo.setTextColor(TUBI_GREY_600);
            }
          /*  if(add_to_order < 0){
                holder.tvOrderProductInfo.setBackgroundColor(PINK_100);
            }else holder.tvOrderProductInfo.setBackgroundColor(TUBI_WHITE);*/
        }


    }

    @Override
    public int getItemCount() {
        return orders.size();
       // return  3;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tvOrderProductInfo,tvOrderQuantity,tvAddToOrder;

        private final DistributionOrdersProviderPartnersAdapter.RecyclerViewClickListener mListener;


        public ViewHolder(@NonNull View itemView,
                          DistributionOrdersProviderPartnersAdapter.RecyclerViewClickListener listener) {
            super(itemView);
            tvOrderProductInfo=itemView.findViewById(R.id.tvOrderProductInfo);
            tvOrderQuantity=itemView.findViewById(R.id.tvOrderQuantity);
            tvAddToOrder=itemView.findViewById(R.id.tvAddToOrder);

            mListener=listener;

            tvOrderProductInfo.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());
        }
    }
}
