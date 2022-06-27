package ru.tubi.project.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import ru.tubi.project.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    public interface RecyclerViewClickListener{
        void onClick(View view, int position);
    }
    private final CategoryAdapter.RecyclerViewClickListener clickListener;

    private final LayoutInflater inflater;
    private final List<String> categoryes;

    public CategoryAdapter(Context context, List<String> categoryes,
                           CategoryAdapter.RecyclerViewClickListener clickListener) {
        this.inflater =  LayoutInflater.from(context);
        this.categoryes = categoryes;
        this.clickListener=clickListener;
    }

    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_category_products,parent,false);
        return new CategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryAdapter.ViewHolder holder, int position) {
        String category=categoryes.get(position);//Category

        holder.tvCategoryName.setText(""+category);

        holder.itemView.setOnClickListener(new View.OnClickListener() {// Listener
            @Override                                                  //
            public void onClick(View v) {                             //
                clickListener.onClick(v,position);         //
            }                                                           //
        });
    }

    @Override
    public int getItemCount() {
        return categoryes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvCategoryName;

        public ViewHolder( View itemView) {
            super(itemView);
            tvCategoryName=itemView.findViewById(R.id.tvCategoriName);
        }
    }
}
