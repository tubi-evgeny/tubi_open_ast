package ru.tubi.project.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.tubi.project.R;
import ru.tubi.project.models.OrderForCollectModel;
import ru.tubi.project.utilites.FirstSimbolMakeBig;

import static android.view.View.GONE;
import static ru.tubi.project.free.AllText.ACTIVE;
import static ru.tubi.project.free.AllText.COLLECT;
import static ru.tubi.project.free.AllText.ORDER;
import static ru.tubi.project.free.AllText.TAX_ID_SMALL;

public class OrderForCollect_partners_Adapter
        extends RecyclerView.Adapter<OrderForCollect_partners_Adapter.ViewHolder>{

    public interface RecyclerViewClickListener{
        void onClick(View view, int position);
    }
    private final OrderForCollect_partners_Adapter.RecyclerViewClickListener clickListener;
    private final LayoutInflater inflater;
    private final List<OrderForCollectModel> buyersCompanyList;

    public OrderForCollect_partners_Adapter(Context context,
                                            List<OrderForCollectModel> buyersCompanyList,
                                            OrderForCollect_partners_Adapter.RecyclerViewClickListener clickListener ){
        this.inflater=LayoutInflater.from(context);
        this.buyersCompanyList=buyersCompanyList;
        this.clickListener=clickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_list_buyers_for_collect,parent,false);
        return new OrderForCollect_partners_Adapter.ViewHolder(view,clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderForCollectModel buyersCompany = buyersCompanyList.get(position);
        String stBuyersCompany = buyersCompany.getAbbreviation()+" "
                +new FirstSimbolMakeBig().firstSimbolMakeBig(buyersCompany.getCounterparty());
        holder.tvInfo.setText(""+stBuyersCompany+" "+TAX_ID_SMALL+" "
                +buyersCompany.getTaxpayer_id());
        holder.tvOrder.setText(""+ORDER+" № "+buyersCompany.getOrder_partner_id());
        if(buyersCompany.getOrder_active() == 1){
            if( buyersCompany.getCollected() == 1){
                holder.llOrder.setBackgroundResource(R.drawable.round_corners_green);
                holder.tvOrder.setText(""+ORDER+" № "+buyersCompany.getOrder_partner_id()+" "+COLLECT);
                holder.tvOrder.setTextSize(16);
            }else {
                holder.llOrder.setBackgroundResource(R.drawable.round_corners_red);
                holder.tvOrder.setText("" + ORDER + " № " + buyersCompany.getOrder_partner_id() + " " + ACTIVE);
                holder.tvOrder.setTextSize(16);
            }
        }else {
            holder.llOrder.setBackgroundResource(R.drawable.round_backgraund_gray_200);
        }

        try{
            if(position != 0 &&
                    buyersCompany.getTaxpayer_id() ==
                            buyersCompanyList.get(position - 1).getTaxpayer_id()){
                holder.llInfo.setVisibility(GONE);
            }else holder.llInfo.setVisibility(View.VISIBLE);
        }catch (Exception ex){}
    }

    @Override
    public int getItemCount() {
        return buyersCompanyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        private LinearLayout llInfo, llOrder;
        private TextView tvInfo, tvOrder;

        private final OrderForCollect_partners_Adapter.RecyclerViewClickListener mListener;

        public ViewHolder(@NonNull View itemView,
                          OrderForCollect_partners_Adapter.RecyclerViewClickListener listener) {
            super(itemView);
            tvInfo=itemView.findViewById(R.id.tvInfo);
            tvOrder=itemView.findViewById(R.id.tvOrder);
            llInfo=itemView.findViewById(R.id.llInfo);
            llOrder=itemView.findViewById(R.id.llOrder);
            mListener=listener;
            //tvInfo.setOnClickListener(this);
            llOrder.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());
        }
    }
}
