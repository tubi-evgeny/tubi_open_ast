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
import ru.tubi.project.models.CarrierPanelModel;

import java.util.List;

import static android.view.View.GONE;
import static ru.tubi.project.free.AllText.BUILDING;
import static ru.tubi.project.free.AllText.WAREHOUSE_PARTNER;
import static ru.tubi.project.free.AllText.WAREHOUSE_PROVIDER;
import static ru.tubi.project.free.AllText.WAREHOUSE_STORAGE;

public class AcceptProductWarehouseAdapter
        extends RecyclerView.Adapter<AcceptProductWarehouseAdapter.ViewHolder>{

    public interface RecyclerViewClickListener{
        void onClick(View view, int position);
    }
    private final RecyclerViewClickListener clickListener;

    private final LayoutInflater inflater;
    private final List<CarrierPanelModel> warehousesInfoList;

    public AcceptProductWarehouseAdapter(Context context,
                            List<CarrierPanelModel> warehousesInfoList,
                            AcceptProductWarehouseAdapter.RecyclerViewClickListener clickListener) {
        this.inflater =  LayoutInflater.from(context);
        this.warehousesInfoList = warehousesInfoList;
        this.clickListener=clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_accept_product_to_warehouse_02,parent,false);
        return new AcceptProductWarehouseAdapter.ViewHolder(view,clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CarrierPanelModel warehousesInfo=warehousesInfoList.get(position);

        String warehouse_info = "№ "+warehousesInfo.getWarehouse_info_id()+"/"+
                warehousesInfo.getWarehouse_id()+" "
                +warehousesInfo.getCity()+" "+warehousesInfo.getStreet()+" "
                +warehousesInfo.getHouse();
        if(!warehousesInfo.getBuilding().equals("")){
            warehouse_info += " "+BUILDING+" "+warehousesInfo.getBuilding();
        }
        String warehouseType = warehousesInfo.getWarehouse_tipe();
        if(warehouseType.equals("partner")){
            warehouseType=WAREHOUSE_PARTNER;
        }else if(warehouseType.equals("provider")){
            warehouseType=WAREHOUSE_PROVIDER;
        }else if(warehouseType.equals("storage")){
            warehouseType=WAREHOUSE_STORAGE;
        }

        holder.tvWarehousTipe.setText(""+warehouseType);
        holder.tvWarehouseInfo.setText(""+warehouse_info);
        try{
            if(warehousesInfo.getWarehouse_tipe().equals(
                    warehousesInfoList.get(position - 1).getWarehouse_tipe())){
                holder.llWarehouseTipe.setVisibility(GONE);
            }else holder.llWarehouseTipe.setVisibility(View.VISIBLE);
        }catch (Exception ex){}

    }

    @Override
    public int getItemCount() {
        return warehousesInfoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final LinearLayout llWarehouseInfo,llWarehouseTipe;
        final TextView tvWarehouseInfo,tvWarehousTipe;

        private final AcceptProductWarehouseAdapter.RecyclerViewClickListener clickListener;

        public ViewHolder(@NonNull View itemView,
                          RecyclerViewClickListener click) {
            super(itemView);
            llWarehouseInfo=itemView.findViewById(R.id.llWarehouseInfo);
            llWarehouseTipe=itemView.findViewById(R.id.llWarehouseTipe);
            tvWarehousTipe=itemView.findViewById(R.id.tvWarehousTipe);
            tvWarehouseInfo=itemView.findViewById(R.id.tvWarehouseInfo);

            clickListener=click;
            llWarehouseInfo.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(v,getAdapterPosition());
        }
    }
}
