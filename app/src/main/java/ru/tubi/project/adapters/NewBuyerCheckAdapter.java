package ru.tubi.project.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.tubi.project.R;
import ru.tubi.project.models.NewBuyerCheckModel;

import java.util.List;

import static ru.tubi.project.free.AllText.RESPONSIBLE_PERSON_TEXT;
import static ru.tubi.project.free.AllText.RUB;
import static ru.tubi.project.free.AllText.SUMM;

public class NewBuyerCheckAdapter extends RecyclerView.Adapter<NewBuyerCheckAdapter.ViewHolder> {

    public interface RecyclerViewClickListener {
        void onClick(View view, int position);
    }


    private final NewBuyerCheckAdapter.RecyclerViewClickListener clickListener;

    private final LayoutInflater inflater;
    private final List<NewBuyerCheckModel> list;

    public NewBuyerCheckAdapter(Context context, List<NewBuyerCheckModel> list,
                                NewBuyerCheckAdapter.RecyclerViewClickListener clickListener) {
        this.clickListener = clickListener;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @NonNull
    @Override
    public NewBuyerCheckAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_new_buyer_check,parent,false);
        return new NewBuyerCheckAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewBuyerCheckAdapter.ViewHolder holder, int position) {
        NewBuyerCheckModel buyer = list.get(position);
        holder.tvBuyerInfo.setText("" + buyer.getAbbreviation()+" "+buyer.getCounterparty()+"\n"
                +"\n"+RESPONSIBLE_PERSON_TEXT+": \n"
                +buyer.getName());
        holder.tvDate.setText(""+buyer.getCreatedDate());
        holder.tvOrderSumm.setText(""+SUMM+": "+buyer.getOrderSumm()+" "+RUB);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(v,position);
            }
        });
    }

    @Override
    public int getItemCount()  {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvBuyerInfo;
        final TextView tvDate;
        final TextView tvOrderSumm;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBuyerInfo = itemView.findViewById(R.id.tvBuyerInfo);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvOrderSumm = itemView.findViewById(R.id.tvOrderSumm);
        }
    }
}
