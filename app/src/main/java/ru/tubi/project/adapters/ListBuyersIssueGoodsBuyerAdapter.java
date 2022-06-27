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
import ru.tubi.project.models.CounterpartyModel;

import java.util.List;

import static android.view.View.GONE;
import static ru.tubi.project.free.AllText.ORDER;
import static ru.tubi.project.free.AllText.TAX_ID_SMALL;

public class ListBuyersIssueGoodsBuyerAdapter
        extends RecyclerView.Adapter<ListBuyersIssueGoodsBuyerAdapter.ViewHolder> {

    public interface RecyclerViewClickListener{
        void onClick(View view, int position);
    }
    private final ListBuyersIssueGoodsBuyerAdapter.RecyclerViewClickListener clickListener;
    private final LayoutInflater inflater;
    private final List<CounterpartyModel> buyersCompanyList;

    public ListBuyersIssueGoodsBuyerAdapter(Context context,
                                                  List<CounterpartyModel> buyersCompanyList,
                                            ListBuyersIssueGoodsBuyerAdapter.RecyclerViewClickListener clickListener ){
        this.inflater=LayoutInflater.from(context);
        this.buyersCompanyList=buyersCompanyList;
        this.clickListener=clickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_list_buyers_issue_goods,parent,false);
        return new ListBuyersIssueGoodsBuyerAdapter.ViewHolder(view,clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CounterpartyModel buyersCompany = buyersCompanyList.get(position);
        String stBuyersCompany = buyersCompany.getAbbreviation()+" "
                +buyersCompany.getCounterparty();
        holder.tvInfo.setText(""+stBuyersCompany+" "+TAX_ID_SMALL+" "
                +buyersCompany.getTaxpayer_id());
        holder.tvOrder.setText(""+ORDER+" № "+buyersCompany.getOrder_id());

        try{
            if(position != 0 &&
                    buyersCompany.getTaxpayer_id() ==
                            buyersCompanyList.get(position - 1).getTaxpayer_id()){
                holder.llInfo.setVisibility(GONE);
            }else holder.llInfo.setVisibility(View.VISIBLE);
        }catch (Exception ex){}
    }

    @Override
    public int getItemCount() {
        return buyersCompanyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
                                    implements View.OnClickListener{

        private LinearLayout llInfo,llOrder;
        private TextView tvInfo,tvOrder;

        private final ListBuyersIssueGoodsBuyerAdapter.RecyclerViewClickListener mListener;

        public ViewHolder(@NonNull View itemView,
                          ListBuyersIssueGoodsBuyerAdapter.RecyclerViewClickListener listener) {
            super(itemView);
            tvInfo=itemView.findViewById(R.id.tvInfo);
            tvOrder=itemView.findViewById(R.id.tvOrder);
            llInfo=itemView.findViewById(R.id.llInfo);
            llOrder=itemView.findViewById(R.id.llOrder);
            mListener=listener;
            //tvInfo.setOnClickListener(this);
            llOrder.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());
        }
    }
}