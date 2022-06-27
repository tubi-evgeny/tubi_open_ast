package ru.tubi.project.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.tubi.project.R;
import ru.tubi.project.models.ListPartnerForModerationAdapterModel;

import java.util.List;

import static ru.tubi.project.free.AllText.TAX_ID;

public class ListPartnerForModerationAdapter
        extends RecyclerView.Adapter<ListPartnerForModerationAdapter.ViewHolder>{
//int count = 0;
    public interface RecyclerViewClickListener {
        void onClick(View view, int position);
    }
    private final ListPartnerForModerationAdapter.RecyclerViewClickListener clickListener;

    private final LayoutInflater inflater;
    private final List<ListPartnerForModerationAdapterModel> list;

    public ListPartnerForModerationAdapter(Context context, List<ListPartnerForModerationAdapterModel> list,
                                           ListPartnerForModerationAdapter.RecyclerViewClickListener clickListener) {
        this.clickListener = clickListener;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ListPartnerForModerationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_list_partner_for_moderation,parent,false);
        return new ListPartnerForModerationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListPartnerForModerationAdapter.ViewHolder holder, int position) {
        ListPartnerForModerationAdapterModel provider = list.get(position);
        holder.tvCounterpartyInfo.setText("" + provider.getAbbreviation()+" "+provider.getCounterparty()+"\n"
                +TAX_ID+": "+provider.getTaxpayer_id());
        holder.tvDate.setText(""+provider.getCreatedDate());
        if(provider.getCount_step() == 0){
            holder.ivMakeSteps.setImageResource(R.drawable.rating_scale_zero_120ps);
        }else if(provider.getCount_step() == 1){
            holder.ivMakeSteps.setImageResource(R.drawable.rating_scale_one_120ps);
        }else if(provider.getCount_step() == 2){
            holder.ivMakeSteps.setImageResource(R.drawable.rating_scale_two_120ps);
        }else if(provider.getCount_step() == 3){
            holder.ivMakeSteps.setImageResource(R.drawable.rating_scale_three_120ps);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(v,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvCounterpartyInfo;
        final TextView tvDate;
        final ImageView ivMakeSteps;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCounterpartyInfo = itemView.findViewById(R.id.tvCounterpartyInfo);
            tvDate = itemView.findViewById(R.id.tvDate);
            ivMakeSteps = itemView.findViewById(R.id.ivMakeSteps);
        }
    }
}
