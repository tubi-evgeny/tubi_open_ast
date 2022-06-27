package ru.tubi.project.activity.ForDelete;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.tubi.project.R;
import ru.tubi.project.models.AcceptProductListProvidersModel;

import java.util.List;

/*public class AcceptProductListProvidersAdapter
            extends RecyclerView.Adapter<AcceptProductListProvidersAdapter.ViewHolder> {

    public interface RecyclerViewClickListener{
        void onClick(View view, int position);
    }

    private final AcceptProductListProvidersAdapter.RecyclerViewClickListener clickListener;

    private final LayoutInflater inflater;
    private final List<AcceptProductListProvidersModel> products;

    public AcceptProductListProvidersAdapter(Context context, List<AcceptProductListProvidersModel> products,
                        AcceptProductListProvidersAdapter.RecyclerViewClickListener clickListener) {
        this.clickListener = clickListener;
        this.inflater = LayoutInflater.from(context);
        this.products = products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_accept_product_list_providers,parent,false);
        return new AcceptProductListProvidersAdapter.ViewHolder(view,clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AcceptProductListProvidersModel product=products.get(position);

        holder.tvCounterparty.setText(""+product.getAbbreviation()+" "+product.getCounterparty());

       holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               clickListener.onClick(v,position);
           }
       });
    }

    @Override
    public int getItemCount() {
       // return 9;
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView tvCounterparty;
        final LinearLayout llCounterparty;

        public ViewHolder(@NonNull View itemView,
                          AcceptProductListProvidersAdapter.RecyclerViewClickListener listener) {
            super(itemView);
            tvCounterparty=itemView.findViewById(R.id.tvCounterparty);
            llCounterparty=itemView.findViewById(R.id.llCounterparty);
        }

        @Override
        public void onClick(View v) {

        }
    }
}*/
