package ru.tubi.project.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import ru.tubi.project.R;
import ru.tubi.project.models.Catalog;
import ru.tubi.project.utilites.DownloadImage;

import java.util.List;

import static ru.tubi.project.activity.Config.ADMIN_PANEL_URL_IMAGES;

public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.ViewHolder> {

    public interface RecyclerViewClickListener{
        void onClick(View view, int position);
    }
    private final RecyclerViewClickListener clickListener;

    private final LayoutInflater inflater;
    private final List<Catalog> categoryes;

    public CatalogAdapter(Context context, List<Catalog> categoryes,
                          RecyclerViewClickListener clickListener) {
        this.inflater =  LayoutInflater.from(context);
        this.categoryes = categoryes;
        this.clickListener=clickListener;
    }


    @Override
    public CatalogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_catalog,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CatalogAdapter.ViewHolder holder, int position) {
        Catalog category=categoryes.get(position);//Category

        holder.tvCategoryName.setText(""+category.getCategoryName());
        if(!category.getImageURL().equals("null")) {
            new DownloadImage(holder.ivCategory)
                        .execute(ADMIN_PANEL_URL_IMAGES+category.getImageURL());
        }else holder.ivCategory.setImageResource(R.drawable.tubi_logo_50ps);


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
        final ImageView ivCategory;


        public ViewHolder( View itemView) {
            super(itemView);
            ivCategory=itemView.findViewById(R.id.ivCategory);
            tvCategoryName=itemView.findViewById(R.id.tvCategoriName);
        }
    }
}
