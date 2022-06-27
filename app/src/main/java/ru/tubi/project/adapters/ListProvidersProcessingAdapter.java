package ru.tubi.project.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.tubi.project.R;
import ru.tubi.project.models.CounterpartyModel;

import java.util.List;

public class ListProvidersProcessingAdapter extends RecyclerView.Adapter<ListProvidersProcessingAdapter.ViewHolder> {

    public interface RecyclerViewClickListener{
        void onClick(View view, int position);
    }
    private final ListProvidersProcessingAdapter.RecyclerViewClickListener clickListener;

    private final LayoutInflater inflater;
    private final List<CounterpartyModel> providers;

    public ListProvidersProcessingAdapter(Context context, RecyclerViewClickListener clickListener,
                                          List<CounterpartyModel> providers) {
        this.clickListener = clickListener;
        this.inflater = LayoutInflater.from(context);
        this.providers = providers;
    }


    @NonNull
    @Override
    public ListProvidersProcessingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_list_providers_processing,parent,false);
        return new ListProvidersProcessingAdapter.ViewHolder(view,clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ListProvidersProcessingAdapter.ViewHolder holder, int position) {
        CounterpartyModel provider = providers.get(position);

        holder.tvProvider.setText(""+provider.getAbbreviation()+" "+provider.getCounterparty());
        holder.tvVolume.setText("");
        holder.tvWeight.setText(""+provider.getSum_weight_volume());
        holder.tvProcessing.setText(""+provider.getCompletedProcessing()+"%");
    }

    @Override
    public int getItemCount() {
        return providers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView tvProvider;
        final TextView tvVolume;
        final TextView tvWeight;
        final TextView tvProcessing;

        private final ListProvidersProcessingAdapter.RecyclerViewClickListener mListener;
        public ViewHolder(@NonNull View itemView,
                            ListProvidersProcessingAdapter.RecyclerViewClickListener listener) {
            super(itemView);
            tvProvider=itemView.findViewById(R.id.tvProvider);
            tvVolume=itemView.findViewById(R.id.tvVolume);
            tvWeight=itemView.findViewById(R.id.tvWeight);
            tvProcessing=itemView.findViewById(R.id.tvProcessing);

            mListener=listener;
            tvProvider.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v,getAdapterPosition());
        }
    }
}
