package ru.tubi.project.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.tubi.project.R;
import ru.tubi.project.models.InvoiceModel;

import static ru.tubi.project.free.AllText.SUMM_SMOLL;

public class OrderToProviderListAdapter
        extends RecyclerView.Adapter<OrderToProviderListAdapter.ViewHolder>{

    public interface RecyclerViewClickListener{
        void onClick(View view, int position);
    }
    private final OrderToProviderListAdapter.RecyclerViewClickListener clickListener;

    private final LayoutInflater inflater;
    private final List<InvoiceModel> invoice_list;

    public OrderToProviderListAdapter(Context context,
                                      List<InvoiceModel> invoice_list,
                                      OrderToProviderListAdapter.RecyclerViewClickListener clickListener ){
        this.inflater=LayoutInflater.from(context);
        this.invoice_list=invoice_list;
        this.clickListener=clickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_order_to_provider_list,parent,false);
        return new OrderToProviderListAdapter.ViewHolder(view,clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InvoiceModel invoice=invoice_list.get(position);

        holder.tvReceiptDate.setText(""+invoice.getDate_order_start());
        holder.tvReceiptNum.setText("№ "+invoice.getOrder_partner_id());
        holder.tvToGiveDate.setText(""+invoice.getGet_order_date());
        holder.tvMyWarehouse.setText(""+invoice.getOut_warehouse_info_id()+"/"
                +invoice.getOut_warehouse_id());
        holder.tvCounterpartyInfo.setText(""+invoice.getIn_companyInfoString_short()
                +" / key: "+invoice.getInvoice_key_id());
        holder.tvCounterpartyWarehouseInfo.setText(""+invoice.getIn_warehouseInfoString());
        holder.tvReceiptSumm.setText(""+SUMM_SMOLL+": "+invoice.getSumm());

        if(invoice.getExecuted() == 1){
            holder.ivReceiptStatus.setImageResource(R.drawable.checkmark_green_140ps);
        }else{
            holder.ivReceiptStatus.setImageResource(R.drawable.checkmark_gray_140ps);
        }
    }

    @Override
    public int getItemCount() {
        return invoice_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        final TextView tvReceiptDate,tvReceiptNum,tvToGiveDate, tvMyWarehouse
                ,tvCounterpartyInfo, tvCounterpartyWarehouseInfo,tvReceiptSumm;
        final ImageView ivReceiptStatus;
        final LinearLayout llReceipt;

        private final OrderToProviderListAdapter.RecyclerViewClickListener mListener;



        public ViewHolder(@NonNull View itemView,
                          OrderToProviderListAdapter.RecyclerViewClickListener listener) {
            super(itemView);
            llReceipt=itemView.findViewById(R.id.llReceipt);
            ivReceiptStatus=itemView.findViewById(R.id.ivReceiptStatus);
            tvReceiptDate=itemView.findViewById(R.id.tvReceiptDate);
            tvToGiveDate=itemView.findViewById(R.id.tvToGiveDate);
            tvReceiptNum=itemView.findViewById(R.id.tvReceiptNum);
            tvMyWarehouse=itemView.findViewById(R.id.tvMyWarehouse);
            tvCounterpartyInfo=itemView.findViewById(R.id.tvCounterpartyInfo);
            tvCounterpartyWarehouseInfo=itemView.findViewById(R.id.tvCounterpartyWarehouseInfo);
            tvReceiptSumm=itemView.findViewById(R.id.tvReceiptSumm);

            mListener=listener;
            llReceipt.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());
        }
    }
}
