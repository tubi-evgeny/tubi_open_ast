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

import ru.tubi.project.R;
import ru.tubi.project.models.WarehouseModel;

import java.util.List;

import static ru.tubi.project.free.AllText.BUILDING;

public class ProfileCompanyWorehouseAdapter
            extends RecyclerView.Adapter<ProfileCompanyWorehouseAdapter.ViewHolder> {

    public interface RecyclerViewClickListener{
        void onClick(View view, int position);
    }
    private final ProfileCompanyWorehouseAdapter.RecyclerViewClickListener clickListener;
    private final LayoutInflater inflater;
    private final List<WarehouseModel> warehouses;

    public ProfileCompanyWorehouseAdapter(Context context,
                            List<WarehouseModel> warehouses,
                            ProfileCompanyWorehouseAdapter.RecyclerViewClickListener clickListener){
        this.inflater=LayoutInflater.from(context);
        this.warehouses=warehouses;
        this.clickListener=clickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_profile_company_warehouse_002,parent,false);
        return new ProfileCompanyWorehouseAdapter.ViewHolder(view,clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WarehouseModel warehouse = warehouses.get(position);
        String warhouseInfo = warehouse.getCity()+" "+warehouse.getStreet()+" "+warehouse.getHouse();
        if(!warehouse.getBuilding().equals(null) && !warehouse.getBuilding().isEmpty()){
            warhouseInfo += " "+BUILDING+" "+warehouse.getBuilding();
        }
        holder.tvWarehouseInfo.setText(""+warhouseInfo);

        if(warehouse.isProviderRole()) {
            holder.ivCheckProviderWarhouse.setImageResource(R.drawable.checkmark_yellow_140ps);
        }
        if(warehouse.isProviderWarehouse()){
            holder.ivCheckProviderWarhouse.setImageResource(R.drawable.checkmark_green_140ps);
        }
        if(warehouse.isProviderRole()==false && warehouse.isProviderWarehouse()==false) {
            holder.ivCheckProviderWarhouse.setImageResource(R.drawable.checkmark_gray_140ps);
        }

        if(warehouse.isPartnerRole()) {
            holder.ivCheckPartnerWarehouse.setImageResource(R.drawable.checkmark_yellow_140ps);
        }
        if(warehouse.isPartnerWarehouse()){
            holder.ivCheckPartnerWarehouse.setImageResource(R.drawable.checkmark_green_140ps);
        }
        if(warehouse.isPartnerRole()==false && warehouse.isPartnerWarehouse()==false) {
            holder.ivCheckPartnerWarehouse.setImageResource(R.drawable.checkmark_gray_140ps);
        }
        if(warehouse.getWarStorageNum() > 0){
            holder.tvWarStorageNum.setText(
                    "№"+warehouse.getWarehouse_info_id()+"/"+warehouse.getWarStorageNum());
            holder.ivCheckBuyerWarehouse.setImageResource(R.drawable.checkmark_green_140ps);
        }
        if(warehouse.getWarProviderNum() > 0){
            holder.tvWarProviderNum.setText(
                    "№"+warehouse.getWarehouse_info_id()+"/"+warehouse.getWarProviderNum());
        }
        if(warehouse.getWarPartnerNum() > 0){
            holder.tvWarPartnerNum.setText(
                    "№"+warehouse.getWarehouse_info_id()+"/"+warehouse.getWarPartnerNum());
        }


    }

    @Override
    public int getItemCount() {
        return warehouses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView tvWarehouseInfo, tvWarStorageNum, tvWarProviderNum, tvWarPartnerNum,
                tvBuyerWarehouse, tvProviderWarehouse, tvPartnerWarehouse;
        final ImageView ivWarehouseEdit, ivCheckBuyerWarehouse, ivCheckProviderWarhouse,
                ivCheckPartnerWarehouse;
        final LinearLayout llBuyerWarehouse;

        private final ProfileCompanyWorehouseAdapter.RecyclerViewClickListener mListener;

        public ViewHolder(@NonNull View itemView,
                          ProfileCompanyWorehouseAdapter.RecyclerViewClickListener listener) {
            super(itemView);

            llBuyerWarehouse=itemView.findViewById(R.id.llBuyerWarehouse);
            tvWarehouseInfo=itemView.findViewById(R.id.tvWarehouseInfo);
            tvWarStorageNum=itemView.findViewById(R.id.tvWarStorageNum);
            tvWarProviderNum=itemView.findViewById(R.id.tvWarProviderNum);
            tvWarPartnerNum=itemView.findViewById(R.id.tvWarPartnerNum);
            tvBuyerWarehouse=itemView.findViewById(R.id.tvBuyerWarehouse);
            tvProviderWarehouse=itemView.findViewById(R.id.tvProviderWarehouse);
            tvPartnerWarehouse=itemView.findViewById(R.id.tvPartnerWarehouse);
            ivCheckBuyerWarehouse=itemView.findViewById(R.id.ivCheckBuyerWarehouse);
            ivCheckProviderWarhouse=itemView.findViewById(R.id.ivCheckProviderWarhouse);
            ivCheckPartnerWarehouse=itemView.findViewById(R.id.ivCheckPartnerWarehouse);
            ivWarehouseEdit=itemView.findViewById(R.id.ivWarehouseEdit);

            mListener=listener;

            llBuyerWarehouse.setOnClickListener(this);
            ivWarehouseEdit.setOnClickListener(this);
           // ivCheckBuyerWarehouse.setOnClickListener(this);
            ivCheckProviderWarhouse.setOnClickListener(this);
            ivCheckPartnerWarehouse.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());
        }
    }

   /* private void setInitialData(String url_get) {

        InitialData task=new InitialData(){
          /*  @Override
            protected void onPreExecute() {
                asyncDialog.setMessage(LOAD_TEXT);
                asyncDialog.show();
                super.onPreExecute();
            }

            protected void onPostExecute(String result) {
                //Do your thing
                //if(whatQuestion.equals("check_partner_warehouse_role")){
                    //splitResult(result,whatQuestion);
                //}
            }
        };
        task.execute(url_get);
    }*/
}
