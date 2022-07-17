package ru.tubi.project.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ru.tubi.project.R;
import ru.tubi.project.models.CounterpartyModel;
import ru.tubi.project.models.ProductModel;

public class ChoosePartnerAdapter{
       // extends RecyclerView.Adapter<ChoosePartnerAdapter.ViewHolder> {

   /* public interface RecyclerViewClickListener{
        void onClick(View view, int position);

    }
    private final ChoosePartnerAdapter.RecyclerViewClickListener clickListener;

    private final LayoutInflater inflater;
    private final List<CounterpartyModel> partnerList;

    public ChoosePartnerAdapter(Context context, List<CounterpartyModel> partnerList,
                    ChoosePartnerAdapter.RecyclerViewClickListener clickListener) {
        this.inflater =  LayoutInflater.from(context);
        this.partnerList = partnerList;
        this.clickListener=clickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_product,parent,false);
        return new ChoosePartnerAdapter.ViewHolder(view,clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }*/
}
