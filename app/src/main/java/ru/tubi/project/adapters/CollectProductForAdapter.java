package ru.tubi.project.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.tubi.project.R;
import ru.tubi.project.models.CatalogProductProviderModel;
import ru.tubi.project.models.CollectProductModel;

import static ru.tubi.project.free.AllText.C;
import static ru.tubi.project.free.AllText.ORDER;
import static ru.tubi.project.free.AllText.PHONE_SHORT;
import static ru.tubi.project.free.AllText.ST;
import static ru.tubi.project.free.AllText.TAX_ID_SMALL;
import static ru.tubi.project.free.AllText.WAREHOUSE;

public class CollectProductForAdapter
        extends RecyclerView.Adapter<CollectProductForAdapter.ViewHolder>{

    public interface RecyclerViewClickListener{
        void onClick(View view, int position);
    }
    private final CollectProductForAdapter.RecyclerViewClickListener clickListener;

    private final LayoutInflater inflater;
    private final List<CollectProductModel> openOrdersList;

    public CollectProductForAdapter(Context context,
                                  List<CollectProductModel> openOrdersList,

                                    CollectProductForAdapter.RecyclerViewClickListener clickListener ){
        this.inflater=LayoutInflater.from(context);
        this.openOrdersList=openOrdersList;
        this.clickListener=clickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_textview,parent,false);
        return new CollectProductForAdapter.ViewHolder(view,clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CollectProductModel openOrder=openOrdersList.get(position);


        String WriteOffOrders_info = ORDER+" №"+openOrder.getOrder_id()+"\n"+openOrder.getBuyer_name()+" "+PHONE_SHORT+": "+
                openOrder.getBuyer_phone()+"\n"+openOrder.getAbbreviation()+" "+
                openOrder.getCounterparty()+" "+TAX_ID_SMALL+": "+openOrder.getTaxpayer_id()+"\n"
                +WAREHOUSE+" № "+openOrder.getWarehouse_info_id()+"/"+openOrder.getWarehouse_id()
                +" "+openOrder.getCity()+" "+ST+" "+openOrder.getStreet()+" "+openOrder.getHouse();
        if(!openOrder.getBuilding().isEmpty()){
            WriteOffOrders_info += " "+C+". "+openOrder.getBuilding();
        }

         holder.text.setText(""+WriteOffOrders_info);
    }

    @Override
    public int getItemCount() {
        return openOrdersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView text;

        private final CollectProductForAdapter.RecyclerViewClickListener mListener;

        public ViewHolder(@NonNull View itemView,
                          CollectProductForAdapter.RecyclerViewClickListener listener) {
            super(itemView);
            text=itemView.findViewById(R.id.text);

            mListener=listener;
            text.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());
        }
    }
}
