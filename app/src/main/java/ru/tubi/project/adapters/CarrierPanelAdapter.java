package ru.tubi.project.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.tubi.project.R;
import ru.tubi.project.models.CarrierPanelModel;

import java.util.List;

import static android.view.View.GONE;
import static ru.tubi.project.free.AllText.C;
import static ru.tubi.project.free.AllText.KG;
import static ru.tubi.project.free.AllText.ST;
import static ru.tubi.project.free.AllText.WAREHOUSE;

public class CarrierPanelAdapter
        extends RecyclerView.Adapter<CarrierPanelAdapter.ViewHolder>{

    public interface OnCheckedChangeListener{
        void isChecked(View view, boolean flag, int position);
    }

    private final CarrierPanelAdapter.OnCheckedChangeListener checked;
    private final LayoutInflater inflater;
    private final List<CarrierPanelModel> deliveryList;

    public CarrierPanelAdapter(Context context, List<CarrierPanelModel> deliveryList,
                               CarrierPanelAdapter.OnCheckedChangeListener checked) {

        this.inflater = LayoutInflater.from(context);
        this.deliveryList = deliveryList;
        this.checked=checked;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_carrier_panel,parent,false);
        return new CarrierPanelAdapter.ViewHolder(view,checked);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CarrierPanelModel delivery=deliveryList.get(position);
        String outWarehouse = WAREHOUSE+" № "+delivery.getOutWarehouse_info_id()+"/"+
                delivery.getOutWarehouse_id()+" "+delivery.getOutCity()+
                " "+ST+" "+delivery.getOutStreet()+" "+delivery.getOutHouse();
        if(!delivery.getOutBuilding().isEmpty()){
            outWarehouse += " "+C+". "+delivery.getOutBuilding();
        }
        String inWarehouse = WAREHOUSE+" № "+delivery.getInWarehouse_info_id()+"/"+
                delivery.getInWarehouse_id()+" "+delivery.getInCity()+
                " "+ST+" "+delivery.getInStreet()+" "+delivery.getInHouse();
        if(!delivery.getInBuilding().isEmpty()){
            inWarehouse += " "+C+". "+delivery.getInBuilding();
        }
        double productMass = (delivery.getProductWeight() * delivery.getQuantity()) / 1000;

        holder.tvOutWarehouses.setText(""+outWarehouse);
        holder.tvInWarehouses.setText(""+inWarehouse);
        holder.tvProductMass.setText(String.format("%.3f",productMass)+" "+KG);
        if(delivery.getStorageTemperature().equals("обычное")){
            holder.ivTemperature.setImageResource(R.drawable.temperature_150ps);
        }else if(delivery.getStorageTemperature().equals("холод")){
            holder.ivTemperature.setImageResource(R.drawable.temperature_5_150ps);
        }else if(delivery.getStorageTemperature().equals("мороз")){
            holder.ivTemperature.setImageResource(R.drawable.temperature_15_150ps);
        }
        if(delivery.getChecked() == 0){
            holder.checkBox.setChecked(false);
        }else holder.checkBox.setChecked(true);
        try{
            if(position != 0 &&
                    delivery.getOutWarehouse_id() == deliveryList.get(position - 1).getOutWarehouse_id() &&
                    delivery.getInWarehouse_id() == deliveryList.get(position - 1).getInWarehouse_id()){
                holder.llWarehouse.setVisibility(GONE);
            }else holder.llWarehouse.setVisibility(View.VISIBLE);
        }catch (Exception ex){}

    }

    @Override
    public int getItemCount() {
        return deliveryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final CheckBox checkBox;
        final TextView tvOutWarehouses, tvInWarehouses, tvProductMass, tvProductWeight;
        final ImageView ivTemperature;
        final LinearLayout llWarehouse;

        private final CarrierPanelAdapter.OnCheckedChangeListener mChecked;

        public ViewHolder(@NonNull View itemView,
                          CarrierPanelAdapter.OnCheckedChangeListener checked) {
            super(itemView);

            llWarehouse=itemView.findViewById(R.id.llWarehouse);
            checkBox=itemView.findViewById(R.id.checkBox);
            tvOutWarehouses=itemView.findViewById(R.id.tvOutWarehouses);
            tvInWarehouses=itemView.findViewById(R.id.tvInWarehouses);
            tvProductMass=itemView.findViewById(R.id.tvProductMass);
            tvProductWeight=itemView.findViewById(R.id.tvProductWeight);
            ivTemperature=itemView.findViewById(R.id.ivTemperature);

            mChecked=checked;
            checkBox.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mChecked.isChecked(v,checkBox.isChecked(),getAdapterPosition());
        }
    }
}
