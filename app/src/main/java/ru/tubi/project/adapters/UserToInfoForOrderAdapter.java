package ru.tubi.project.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.tubi.project.R;

import ru.tubi.project.models.ProductInOrderModel;

import java.util.List;

import static ru.tubi.project.free.AllCollor.RED_600;
import static ru.tubi.project.free.AllCollor.TUBI_BLACK;
import static ru.tubi.project.free.AllCollor.TUBI_GREEN_600;
import static ru.tubi.project.free.AllCollor.TUBI_GREY_800;
import static ru.tubi.project.free.AllCollor.TUBI_WHITE;
import static ru.tubi.project.free.AllText.IN_PACKAGE;
import static ru.tubi.project.free.AllText.ORDER_APPROVED;
import static ru.tubi.project.free.AllText.ORDER_DELETE_DEFECTIVE;
import static ru.tubi.project.free.AllText.RUB;
import static ru.tubi.project.free.AllText.SUMM;

public class UserToInfoForOrderAdapter
        extends RecyclerView.Adapter<UserToInfoForOrderAdapter.ViewHolder> {

    public interface RecyclerViewClickListener{
        void onClick(View view, int position);

    }
    private final UserToInfoForOrderAdapter.RecyclerViewClickListener clickListener;

    private final LayoutInflater inflater;
    private final List<ProductInOrderModel> products;

    public UserToInfoForOrderAdapter(Context context, List<ProductInOrderModel> products,
                       UserToInfoForOrderAdapter.RecyclerViewClickListener clickListener) {
        this.inflater = LayoutInflater.from(context);
        this.products = products;
        this.clickListener=clickListener;
    }
    public View view, view_two;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == R.layout.item_user_to_info_for_order){
            view=inflater.inflate(R.layout.item_user_to_info_for_order,parent,false);
        }else{
            view=inflater.inflate(R.layout.item_bottom_button_2,parent,false);
            view_two = view;
        }

        return new UserToInfoForOrderAdapter.ViewHolder(view,clickListener);
        /*
        View view=inflater.inflate(R.layout.item_user_to_info_for_order,parent,false);
        return new UserToInfoForOrderAdapter.ViewHolder(view);
         */
    }

    @Override
    public int getItemViewType(int position) {
        //если достигнута длинна списка то поменять item
        return (position == products.size()+1) ? R.layout.item_bottom_button_2 : R.layout.item_user_to_info_for_order;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //позийия для вывода панели кнопок
        if(position == products.size()+1){
            holder.tvBtnModerationOk.setText(ORDER_APPROVED);
            holder.tvBtnModerationOk.setBackgroundColor(TUBI_GREEN_600);
            holder.tvBtnDeleteOrder.setText(ORDER_DELETE_DEFECTIVE);
            holder.tvBtnDeleteOrder.setBackgroundColor(RED_600);
            holder.tvBtnDeleteOrder.setTextColor(TUBI_WHITE);

            //позиция для вывода итоговой суммы
        }else if(position == products.size()) {
            int summ=0;
            for(int i=0;i < products.size();i++){
                summ += products.get(i).getQuantity() * products.get(i).getPrice();
            }
            holder.tvDescription.setText(""+SUMM+": " +summ+" "+RUB);
            holder.tvDescription.setTextSize(20);
            holder.tvDescription.setTextColor(TUBI_BLACK);
            holder.tvDescription.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
            holder.tvQuantity.setVisibility(View.GONE);
            holder.tvPrice.setVisibility(View.GONE);
        }else{//вывод списка
            ProductInOrderModel product = products.get(position);

            String description = product.getCategory() + " " + product.getBrand() + " " + product.getCharacteristic() + " "
                    + product.getType_packaging() + " " + product.getWeight_volume() + " " + product.getUnit_measure() + " "
                    + IN_PACKAGE + " " + product.getQuantity_package();

            //holder.tvDescription.setText("hello");
            holder.tvDescription.setText("" + description);
            holder.tvPrice.setText("" + product.getPrice());
            holder.tvQuantity.setText("" + product.getQuantity());
            holder.tvDescription.setTextSize(16);
            holder.tvDescription.setTextColor(TUBI_GREY_800);
            holder.tvDescription.setGravity(Gravity.CENTER_VERTICAL);
            holder.tvQuantity.setVisibility(View.VISIBLE);
            holder.tvPrice.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        //добавить позиции для кнопок, и для итоговой суммы
        return products.size() + 2;
        //return products.size() + 1;
        //return  4;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView tvDescription,tvPrice,tvQuantity;
        public TextView tvBtnModerationOk,tvBtnDeleteOrder;

        public  UserToInfoForOrderAdapter.RecyclerViewClickListener mListener;

        public ViewHolder(@NonNull View itemView, UserToInfoForOrderAdapter.RecyclerViewClickListener listener) {
            super(itemView);
            mListener = listener;

            if(itemView == view_two){
                tvBtnModerationOk=itemView.findViewById(R.id.tvBtnModerationOk);
                tvBtnModerationOk.setOnClickListener(this);
                tvBtnDeleteOrder=itemView.findViewById(R.id.tvBtnDeleteOrder);
                tvBtnDeleteOrder.setOnClickListener(this);
            }else {
                tvDescription = itemView.findViewById(R.id.tvDescription);
                tvPrice = itemView.findViewById(R.id.tvPrice);
                tvQuantity = itemView.findViewById(R.id.tvQuantity);
            }
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());
        }
    }
}
