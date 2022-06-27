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
import ru.tubi.project.models.OrderForCollectModel;
import ru.tubi.project.models.ShipmentProductModel;
import ru.tubi.project.utilites.FirstSimbolMakeBig;

import static android.view.View.GONE;
import static ru.tubi.project.free.AllText.ACTIVE;
import static ru.tubi.project.free.AllText.COLLECT;
import static ru.tubi.project.free.AllText.LOGISTIC_TO_PRODUCT_FALSE;
import static ru.tubi.project.free.AllText.ORDER;
import static ru.tubi.project.free.AllText.TAX_ID_SMALL;

public class ShipmentProduct_partners_Adapter
        extends RecyclerView.Adapter<ShipmentProduct_partners_Adapter.ViewHolder>{

    public interface RecyclerViewClickListener{
        void onClick(View view, int position);
    }
    private final ShipmentProduct_partners_Adapter.RecyclerViewClickListener clickListener;
    private final LayoutInflater inflater;
    private final List<ShipmentProductModel> shipmentList;

    public ShipmentProduct_partners_Adapter(Context context,
                                            List<ShipmentProductModel> shipmentList,
                                            ShipmentProduct_partners_Adapter.RecyclerViewClickListener clickListener ){
        this.inflater=LayoutInflater.from(context);
        this.shipmentList=shipmentList;
        this.clickListener=clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_shipment_product,parent,false);
        return new ShipmentProduct_partners_Adapter.ViewHolder(view,clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShipmentProductModel shipment = shipmentList.get(position);
        holder.tvCompanyInfo.setText(""+shipment.getCompanyInfoString());
        holder.tvWarehous.setText(""+shipment.getWarehouseInfoString());
        if(shipment.getLogistic_product() == 1){
            if( shipment.getCar_id() > 0){
               // holder.llOrder.setBackgroundResource(R.drawable.round_corners_green);
                holder.tvCar.setText(""+shipment.getCar_info_string());
                holder.llCar.setVisibility(View.VISIBLE);
                //holder.tvOrder.setTextSize(16);
            }else {
                holder.llCar.setVisibility(GONE);
            }
        }else {
            //holder.llWarehouse.setBackgroundResource(R.drawable.round_backgraund_gray_200);
            holder.llCar.setVisibility(GONE);
        }
        if(shipment.getLogistic_product() == 1 && shipment.getCar_id() == 0){
            holder.llItem.setBackgroundResource(R.drawable.round_backgraund_gray_200);
            holder.tvWarehous.setBackgroundResource(R.color.tubi_grey_200);
            holder.ivInvoiceNum.setVisibility(GONE);
            holder.tvMessege.setVisibility(View.VISIBLE);
            holder.tvMessege.setText(""+LOGISTIC_TO_PRODUCT_FALSE);
        }else{
            holder.tvMessege.setVisibility(GONE);
        }
        try{
            if(shipment.getCounterparty_id()
                    == shipmentList.get(position-1).getCounterparty_id()){
                holder.llCompanyInfo.setVisibility(GONE);
            }
        }catch(Exception ex){}
        if(shipment.getOut_active() == 1){
            holder.ivInvoiceNum.setImageResource(R.drawable.checkmark_green_140ps);
        }else{
            holder.ivInvoiceNum.setImageResource(R.drawable.doc_150_ps);
        }

    }

    @Override
    public int getItemCount() {
        return shipmentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{

        private LinearLayout llItem, llCompanyInfo, llInvoiceNum, llCar, llWarehouse;
        private ImageView ivInvoiceNum;
        private TextView tvCompanyInfo, tvCar, tvWarehous, tvMessege;

        private final ShipmentProduct_partners_Adapter.RecyclerViewClickListener mListener;


        public ViewHolder(@NonNull View itemView,
                          ShipmentProduct_partners_Adapter.RecyclerViewClickListener listener) {
            super(itemView);
            tvCompanyInfo=itemView.findViewById(R.id.tvCompanyInfo);
            tvCar=itemView.findViewById(R.id.tvCar);
            tvWarehous=itemView.findViewById(R.id.tvWarehous);
            tvMessege=itemView.findViewById(R.id.tvMessege);
            llCompanyInfo=itemView.findViewById(R.id.llCompanyInfo);
            llInvoiceNum=itemView.findViewById(R.id.llInvoiceNum);
            llCar=itemView.findViewById(R.id.llCar);
            llWarehouse=itemView.findViewById(R.id.llWarehouse);
            llItem=itemView.findViewById(R.id.llItem);
            ivInvoiceNum=itemView.findViewById(R.id.ivInvoiceNum);


            mListener=listener;
            //tvInfo.setOnClickListener(this);
            ivInvoiceNum.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());
        }
    }
}
