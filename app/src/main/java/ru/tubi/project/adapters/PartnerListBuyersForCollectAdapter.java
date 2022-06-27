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

import static ru.tubi.project.free.AllText.BUILDING;
import static ru.tubi.project.free.AllText.WAREHOUSE;

public class PartnerListBuyersForCollectAdapter
        extends RecyclerView.Adapter<PartnerListBuyersForCollectAdapter.ViewHolder>{

    public interface RecyclerViewClickListener{
        void onClick(View view, int position);
    }
    private final PartnerListBuyersForCollectAdapter.RecyclerViewClickListener clickListener;
    private final LayoutInflater inflater;
    private final List<CarrierPanelModel> warehouses;

    public PartnerListBuyersForCollectAdapter(Context context,
                                                 List<CarrierPanelModel> warehouses,
                                              PartnerListBuyersForCollectAdapter.RecyclerViewClickListener clickListener ){
        this.inflater=LayoutInflater.from(context);
        this.warehouses=warehouses;
        this.clickListener=clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_text_view_and_image_view,parent,false);
        return new PartnerListBuyersForCollectAdapter.ViewHolder(view,clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CarrierPanelModel warehouse = warehouses.get(position);
        String stWarehouse = WAREHOUSE+" "+ warehouse.getWarehouse_info_id()+"/"
                +warehouse.getWarehouse_id()+" "+warehouse.getCity()+" "+warehouse.getHouse()+" ";
        try{
            stWarehouse += BUILDING+" "+warehouse.getBuilding();
        }catch (Exception ex){}

        holder.tvInfo.setText(""+stWarehouse);
    }

    @Override
    public int getItemCount() {
        return warehouses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
                                            implements View.OnClickListener {
        private TextView tvInfo;
        private LinearLayout llInfo;

        private final PartnerListBuyersForCollectAdapter.RecyclerViewClickListener mListener;

        public ViewHolder(@NonNull View itemView,
                          PartnerListBuyersForCollectAdapter.RecyclerViewClickListener listener) {
            super(itemView);
            tvInfo=itemView.findViewById(R.id.tvInfo);
            llInfo=itemView.findViewById(R.id.llInfo);
            mListener=listener;
            //tvInfo.setOnClickListener(this);
            llInfo.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());
        }
    }
}
