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

public class OrderBuyerListAdapter
        extends RecyclerView.Adapter<OrderBuyerListAdapter.ViewHolder>{

    public interface RecyclerViewClickListener{
        void onClick(View view, int position);
    }
    private final OrderBuyerListAdapter.RecyclerViewClickListener clickListener;

    private final LayoutInflater inflater;
    private final List<InvoiceModel> invoice_list;

    public OrderBuyerListAdapter(Context context,
                                 List<InvoiceModel> invoice_list,

                                 OrderBuyerListAdapter.RecyclerViewClickListener clickListener ){
        this.inflater=LayoutInflater.from(context);
        this.invoice_list=invoice_list;
        this.clickListener=clickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_order_buyer_list,parent,false);
        return new OrderBuyerListAdapter.ViewHolder(view,clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InvoiceModel invoice=invoice_list.get(position);

        holder.tvReceiptDate.setText(""+invoice.getDate_order_start());
        holder.tvReceiptNum.setText("№ "+invoice.getOrder_id());
        holder.tvToGiveDate.setText(""+invoice.getGet_order_date());
        holder.tvCounterpartyInfo.setText(""+invoice.getCompanyInfoString_short()
                +" / "+invoice.getInvoice_key_id());
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

        final TextView tvReceiptDate,tvReceiptNum,tvToGiveDate
                ,tvCounterpartyInfo,tvReceiptSumm;
        final ImageView ivReceiptStatus;
        final LinearLayout llReceipt;

        private final OrderBuyerListAdapter.RecyclerViewClickListener mListener;

        public ViewHolder(@NonNull View itemView,
                          OrderBuyerListAdapter.RecyclerViewClickListener listener) {
            super(itemView);
            llReceipt=itemView.findViewById(R.id.llReceipt);
            ivReceiptStatus=itemView.findViewById(R.id.ivReceiptStatus);
            tvReceiptDate=itemView.findViewById(R.id.tvReceiptDate);
            tvToGiveDate=itemView.findViewById(R.id.tvToGiveDate);
            tvReceiptNum=itemView.findViewById(R.id.tvReceiptNum);
            tvCounterpartyInfo=itemView.findViewById(R.id.tvCounterpartyInfo);
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
