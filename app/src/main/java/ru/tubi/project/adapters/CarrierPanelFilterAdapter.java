package ru.tubi.project.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.tubi.project.R;
import ru.tubi.project.models.CarrierPanelModel;

import java.util.List;

import static ru.tubi.project.free.AllText.C;
import static ru.tubi.project.free.AllText.ST;
import static ru.tubi.project.free.AllText.WAREHOUSE;

public class CarrierPanelFilterAdapter
        extends RecyclerView.Adapter<CarrierPanelFilterAdapter.ViewHolder>{

    public interface OnCheckedChangeListener{
        void isChecked(View view, boolean flag, int position);
    }

    private final CarrierPanelFilterAdapter.OnCheckedChangeListener checked;
    private final LayoutInflater inflater;
    private final List<CarrierPanelModel> warehouseList;

    public CarrierPanelFilterAdapter(Context context, List<CarrierPanelModel> warehouseList,
                                     CarrierPanelFilterAdapter.OnCheckedChangeListener checked) {

        this.inflater = LayoutInflater.from(context);
        this.warehouseList = warehouseList;
        this.checked=checked;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_checked_and_textview,parent,false);
        return new CarrierPanelFilterAdapter.ViewHolder(view,checked);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CarrierPanelModel warehouse=warehouseList.get(position);
        String st = WAREHOUSE+" № "+warehouse.getWarehouse_info_id()+"/"+
                warehouse.getWarehouse_id()+" "+warehouse.getCity()+
                " "+ST+" "+warehouse.getStreet()+" "+warehouse.getHouse();
        if(!warehouse.getBuilding().isEmpty()){
            st += " "+C+". "+warehouse.getBuilding();
        }
        holder.tvWarehouse.setText(""+st);

        if(warehouse.getChecked() == 0){
            holder.checkBox.setChecked(false);
        }else holder.checkBox.setChecked(true);

    }

    @Override
    public int getItemCount() {
        return warehouseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
                                     implements View.OnClickListener {
        final CheckBox checkBox;
        final TextView tvWarehouse;

        private final CarrierPanelFilterAdapter.OnCheckedChangeListener mChecked;


        public ViewHolder(@NonNull View itemView,
                          CarrierPanelFilterAdapter.OnCheckedChangeListener checked) {
            super(itemView);
            checkBox=itemView.findViewById(R.id.checkBox);
            tvWarehouse=itemView.findViewById(R.id.tvWarehouse);

            mChecked=checked;
            checkBox.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mChecked.isChecked(v,checkBox.isChecked(),getAdapterPosition());
        }
    }
}
