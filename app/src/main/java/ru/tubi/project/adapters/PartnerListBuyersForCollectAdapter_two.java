package ru.tubi.project.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.tubi.project.R;

import ru.tubi.project.models.CounterpartyModel;
import ru.tubi.project.utilites.FirstSimbolMakeBig;

import java.util.List;

import static android.view.View.GONE;
import static ru.tubi.project.free.AllText.DELETED;
import static ru.tubi.project.free.AllText.DELIVERY_TEXT;
import static ru.tubi.project.free.AllText.DISASSEMBLE;
import static ru.tubi.project.free.AllText.ORDER;
import static ru.tubi.project.free.AllText.ORDER_DELETE_DEFECTIVE;
import static ru.tubi.project.free.AllText.TAX_ID_SMALL;
import static ru.tubi.project.free.AllText.WAREHOUSE;

public class PartnerListBuyersForCollectAdapter_two
        extends RecyclerView.Adapter<PartnerListBuyersForCollectAdapter_two.ViewHolder>{

    public interface RecyclerViewClickListener{
        void onClick(View view, int position);
    }
    private final PartnerListBuyersForCollectAdapter_two.RecyclerViewClickListener clickListener;
    private final LayoutInflater inflater;
    private final List<CounterpartyModel> buyersCompanyList;

    public PartnerListBuyersForCollectAdapter_two(Context context,
                          List<CounterpartyModel> buyersCompanyList,
                          PartnerListBuyersForCollectAdapter_two.RecyclerViewClickListener clickListener ){
        this.inflater=LayoutInflater.from(context);
        this.buyersCompanyList=buyersCompanyList;
        this.clickListener=clickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_list_buyers_for_collect,parent,false);
        return new PartnerListBuyersForCollectAdapter_two.ViewHolder(view,clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CounterpartyModel buyersCompany = buyersCompanyList.get(position);
        String warehouseOrDeliveryInfo = "";
        //если получение на складе
        if(buyersCompany.getDelivery() == 0){
            warehouseOrDeliveryInfo = ""+WAREHOUSE;
        }//если доставка
        else{
            warehouseOrDeliveryInfo = ""+DELIVERY_TEXT;
        }
        String stBuyersCompany = buyersCompany.getAbbreviation()+" "
                +new FirstSimbolMakeBig().firstSimbolMakeBig(buyersCompany.getCounterparty());
        holder.tvInfo.setText(""+stBuyersCompany+" "+TAX_ID_SMALL+" "
                +buyersCompany.getTaxpayer_id());
        holder.tvOrder.setText(""+ORDER+" № "+buyersCompany.getOrder_id()+" "+warehouseOrDeliveryInfo);
        if(buyersCompany.getOrder_deleted() == 1){
            if( buyersCompany.getCollect_product_for_delete() > 0){
                holder.llOrder.setBackgroundResource(R.drawable.round_corners_red_pink100);
                holder.tvOrder.setText(""+ORDER+" № "+buyersCompany.getOrder_id()+" "+DISASSEMBLE);
                holder.tvOrder.setTextSize(16);
            }else {
                holder.llOrder.setBackgroundResource(R.drawable.round_corners_red);
                holder.tvOrder.setText("" + ORDER + " № " + buyersCompany.getOrder_id() + " " + DELETED);
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

        private final PartnerListBuyersForCollectAdapter_two.RecyclerViewClickListener mListener;

        public ViewHolder(@NonNull View itemView,
                          PartnerListBuyersForCollectAdapter_two.RecyclerViewClickListener listener) {
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
