package ru.tubi.project.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import ru.tubi.project.R;
import ru.tubi.project.models.OrderHistoryFinishModel;

import java.util.List;

import static ru.tubi.project.free.AllText.DELIVERY_TEXT;
import static ru.tubi.project.free.AllText.JOINT_BUY_SHORT_TEXT;
import static ru.tubi.project.free.AllText.JOINT_BUY_TEXT;
import static ru.tubi.project.free.AllText.ORDERS_BY_WAREHOUSE;
import static ru.tubi.project.free.AllText.ORDER_BIG;
import static ru.tubi.project.free.AllText.POSITIONS;
import static ru.tubi.project.free.AllText.WAREHOUSE;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {

    public interface RecyclerViewClickListener {
        void onClick(View view, int position);
    }
    private final RecyclerViewClickListener clickListener;

    private final LayoutInflater inflater;
    private final List<OrderHistoryFinishModel> orders;

    public OrderHistoryAdapter(Context context, List<OrderHistoryFinishModel> orders,
                              RecyclerViewClickListener clickListener){
        this.inflater = LayoutInflater.from(context);
        this.orders = orders;
        this.clickListener=clickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_order_history,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        OrderHistoryFinishModel order = orders.get(position);
        int joint_buy = order.getJoint_buy();
        String order_info = ORDER_BIG+": "+order.getOrder_id();
        if(joint_buy == 1){
            order_info += " "+JOINT_BUY_SHORT_TEXT;
        }
        if(order.getDelivery() == 1){
            order_info += " "+DELIVERY_TEXT;
        }else{
            order_info += " "+WAREHOUSE;
        }

        holder.tvOrderNum.setText(""+order_info);
        holder.tvPositionCount.setText(""+POSITIONS+": "+order.getPositionCount());
        holder.tvDescriptionFirst.setText(""+order.getDescriptionFirst());
        holder.tvDescriptionSecond.setText(""+order.getDescriptionSecond());
        //подчеркивание текста
        holder.tvDate.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        holder.tvDate.setText(""+order.getDate());
        holder.tvGetDate.setText(""+order.getGet_date());

        //если заказ удален то
        if(order.getOrder_deleted()==1){
            holder.ivCondition.setImageResource(R.drawable.krestik_delete_150ps);//ic_delete
        }else{
            //если заказ есть то
            if(order.getExecuted() == 1){
                holder.ivCondition.setImageResource(R.drawable.checkmark_green_140ps);
            }else{
                holder.ivCondition.setImageResource(R.drawable.checkmark_gray_140ps);
            }
        }

        holder.tvSumm.setText(String.format("%.2f", order.getSumm())+" р");

        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                clickListener.onClick(v,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvOrderNum;
        final TextView tvPositionCount;
        final TextView tvDescriptionFirst;
        final TextView tvDescriptionSecond;
        final TextView tvDate , tvGetDate;
        final ImageView ivCondition;
        final TextView tvSumm;
        final LinearLayout llProdInfo;

        public ViewHolder( View itemView) {
            super(itemView);
            tvOrderNum=itemView.findViewById(R.id.tvOrderNum);
            tvPositionCount=itemView.findViewById(R.id.tvPositionCount);
            tvDescriptionFirst=itemView.findViewById(R.id.tvDescriptionFirst);
            tvDescriptionSecond=itemView.findViewById(R.id.tvDescriptionSecond);
            tvDate=itemView.findViewById(R.id.tvDate);
            tvGetDate=itemView.findViewById(R.id.tvGetDate);
            ivCondition=itemView.findViewById(R.id.ivCondition);
            tvSumm=itemView.findViewById(R.id.tvSumm);
            llProdInfo=itemView.findViewById(R.id.llProdInfo);


        }
    }
}
