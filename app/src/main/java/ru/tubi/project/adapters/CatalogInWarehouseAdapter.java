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

import ru.tubi.project.models.WarehouseModel;

import java.util.List;

import static ru.tubi.project.free.AllText.BUILDING;
import static ru.tubi.project.free.AllText.ST;
import static ru.tubi.project.free.AllText.WAREHOUSE;
import static ru.tubi.project.free.AllText.WAREHOUSE_PARTNER;
import static ru.tubi.project.free.AllText.WAREHOUSE_PROVIDER;
import static ru.tubi.project.free.AllText.WAREHOUSE_STORAGE;

public class CatalogInWarehouseAdapter
        extends RecyclerView.Adapter<CatalogInWarehouseAdapter.ViewHolder>{

    public interface RecyclerViewClickListener{
        void onClick(View view, int position);
    }
    private final CatalogInWarehouseAdapter.RecyclerViewClickListener clickListener;
    private final LayoutInflater inflater;
    private final List<WarehouseModel> warehouses;

    public CatalogInWarehouseAdapter(Context context,
                                     List<WarehouseModel> warehouses,
                                     CatalogInWarehouseAdapter.RecyclerViewClickListener clickListener ){
        this.inflater=LayoutInflater.from(context);
        this.warehouses=warehouses;
        this.clickListener=clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_catalog_in_warehouse,parent,false);
        return new CatalogInWarehouseAdapter.ViewHolder(view,clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WarehouseModel warehouse = warehouses.get(position);
        String warehouseType = warehouse.getWarehouse_type();
        String warehouse_num = WAREHOUSE+" № "+
                warehouse.getWarehouse_info_id()+"/"+warehouse.getWarehouse_id();
        String warehouseInfo = warehouse.getCity()+" "
                +ST+". "+warehouse.getStreet()+" "+warehouse.getHouse();
        if(!warehouse.getBuilding().isEmpty()){
            warehouseInfo +=" "+BUILDING+" "+warehouse.getBuilding();
        }
        if(warehouseType.equals("partner")){
            warehouseType=WAREHOUSE_PARTNER;
        }else if(warehouseType.equals("provider")){
            warehouseType=WAREHOUSE_PROVIDER;
        }else if(warehouseType.equals("storage")){
            warehouseType=WAREHOUSE_STORAGE;
        }
        holder.tvWarehouseType.setText(""+warehouseType);
        holder.tvWarehouse_id.setText(""+warehouse_num);
        holder.tvWarehouse_info.setText(""+warehouseInfo);

        try{
            if(warehouse.getWarehouse_type().equals(warehouses.get(position-1).getWarehouse_type())){
                holder.llWarehouseType.setVisibility(View.GONE);
            }else holder.llWarehouseType.setVisibility(View.VISIBLE);
        }catch (Exception ex){}

    }

    @Override
    public int getItemCount() {
        return warehouses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tvWarehouseType,tvWarehouse_id,tvWarehouse_info;
        private LinearLayout llWarehouseType,llWarehouse;

        private final CatalogInWarehouseAdapter.RecyclerViewClickListener mListener;

        public ViewHolder(@NonNull View itemView,
                          CatalogInWarehouseAdapter.RecyclerViewClickListener listener) {
            super(itemView);
            llWarehouseType=itemView.findViewById(R.id.llWarehouseType);
            llWarehouse=itemView.findViewById(R.id.llWarehouse);
            tvWarehouseType=itemView.findViewById(R.id.tvWarehouseType);
            tvWarehouse_id=itemView.findViewById(R.id.tvWarehouse_id);
            tvWarehouse_info=itemView.findViewById(R.id.tvWarehouse_info);

            mListener=listener;
            llWarehouse.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());
        }
    }
}
