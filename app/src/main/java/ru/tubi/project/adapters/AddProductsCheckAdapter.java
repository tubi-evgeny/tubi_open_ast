package ru.tubi.project.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ru.tubi.project.R;
import ru.tubi.project.models.AddProduct;
import ru.tubi.project.utilites.DownloadImage;
import ru.tubi.project.utilites.MakeImageToSquare;

import java.util.List;

import static ru.tubi.project.Config.ADMIN_PANEL_URL_IMAGES;
import static ru.tubi.project.Config.ADMIN_PANEL_URL_PREVIEW_IMAGES;
import static ru.tubi.project.free.AllText.DONT_EXISTS;
import static ru.tubi.project.free.AllText.DOWNLOAD_DATA;
import static ru.tubi.project.free.AllText.SHOW_MORE;
import static ru.tubi.project.free.AllText.TAX_ID;

public class AddProductsCheckAdapter extends RecyclerView.Adapter<AddProductsCheckAdapter.ViewHolder>{

    public interface RecyclerViewClickListener{
        void onClick(View view, int position);

    }
    private final AddProductsCheckAdapter.RecyclerViewClickListener clickListener;

    private final LayoutInflater inflater;
    private final List<AddProduct> addProductsList;

    public AddProductsCheckAdapter(Context context, List addProductsList,
                                   AddProductsCheckAdapter.RecyclerViewClickListener clickListener) {
        this.inflater=LayoutInflater.from(context);
        this.addProductsList=addProductsList;
        this.clickListener=clickListener;
    }
    public View view, view_one, view_two;
    @Override
    public AddProductsCheckAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == R.layout.item_add_products_check){
            view=inflater.inflate(R.layout.item_add_products_check,parent,false);
            //view_one = view;
        }else {
            view=inflater.inflate(R.layout.item_bottom_button,parent,false);
            view_two = view;
        }

        return new AddProductsCheckAdapter.ViewHolder(view,clickListener);
    }

    @Override
    public int getItemViewType(int position) {
        return (position == addProductsList.size()) ? R.layout.item_bottom_button : R.layout.item_add_products_check;
        //if(position == addProductsList.size()) return R.layout.item_add_products_check;
        //else return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull AddProductsCheckAdapter.ViewHolder holder, int position) {

        if(position == addProductsList.size()){
            holder.tvBtnDownloadData.setText(DOWNLOAD_DATA);
            holder.tvBtnShowMore.setText(SHOW_MORE);
            /*holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });*/
        }else {
            AddProduct addProduct = addProductsList.get(position);
            if (!addProduct.getImage().equals("null")) {
                new DownloadImage(holder.ivAddProduct)
                        .execute(ADMIN_PANEL_URL_PREVIEW_IMAGES + addProduct.getImage());
            } else holder.ivAddProduct.setImageResource(R.drawable.tubi_logo_no_image_300ps);
          /*  if(!addProduct.getImage().equals("null")) {
                new DownloadImage(){
                    @Override
                    protected void onPostExecute(Bitmap result) {
                        new MakeImageToSquare(result,holder.ivAddProduct);
                    }
                }
                        .execute(ADMIN_PANEL_URL_IMAGES+addProduct.getImage());
            }else holder.ivAddProduct.setImageResource(R.drawable.tubi_logo_no_image_300ps);*/

            if(addProduct.getCatalog().equals("0")){
                holder.tvCatalog.setText(""+DONT_EXISTS );
            }else{
                holder.tvCatalog.setText("" + addProduct.getCatalog());
            }
            holder.tvCategory.setText("" + addProduct.getCategory());
            holder.tvProductName.setText(""+addProduct.getProduct_name());
            holder.tvBrand.setText("" + addProduct.getBrand());
            holder.tvCharacteristic.setText("" + addProduct.getCharacteristic());
            holder.tvTipePacaging.setText("" + addProduct.getType_packaging());
            holder.tvUnitMeasure.setText("" + addProduct.getUnit_measure());
            holder.tvWeightVolume.setText("" + addProduct.getWeight_volume());
            holder.tvPrice.setText("" + addProduct.getPrice());
            holder.tvQuantity.setText("" + addProduct.getQuantity());
            holder.tvQuantityPackage.setText("" + addProduct.getQuantity_package());
            holder.tvCounterparty.setText("" +addProduct.getAbbreviation()+" "
                    + addProduct.getCounterparty()+"  "+TAX_ID+": "+addProduct.getTaxpayer_id());
            holder.tvDescription.setText("" + addProduct.getDescription());
            //подсветить красным если в базе нет таких данных
            if(addProduct.getCatalog_flag() == 0) {
                holder.tvCatalog.setTextColor((Color.RED));
            }else holder.tvCatalog.setTextColor(Color.BLACK);
            if (addProduct.getCategory_flag() == 0) {
                holder.tvCategory.setTextColor(Color.RED);
            } else holder.tvCategory.setTextColor(Color.BLACK);
            if (addProduct.getProduct_name_flag() == 0) {
                holder.tvProductName.setTextColor(Color.RED);
            } else holder.tvProductName.setTextColor(Color.BLACK);
            if (addProduct.getBrand_flag() == 0) {
                holder.tvBrand.setTextColor(Color.RED);
            } else holder.tvBrand.setTextColor(Color.BLACK);
            if(addProduct.getCharacteristic_flag() == 0){
                holder.tvCharacteristic.setTextColor(Color.RED);
            } else holder.tvCharacteristic.setTextColor(Color.BLACK);
            if (addProduct.getType_packaging_flag() == 0) {
                holder.tvTipePacaging.setTextColor(Color.RED);
            } else holder.tvTipePacaging.setTextColor(Color.BLACK);
            if (addProduct.getUnit_measure_flag() == 0) {
                holder.tvUnitMeasure.setTextColor(Color.RED);
            } else holder.tvUnitMeasure.setTextColor(Color.BLACK);
            if (addProduct.getCounterparty_flag() == 0) {
                holder.tvCounterparty.setTextColor(Color.RED);
            } else holder.tvCounterparty.setTextColor(Color.BLACK);
            if(addProduct.getDescription_flag() == 0){
                holder.tvDescription.setTextColor(Color.RED);
            }else holder.tvDescription.setTextColor(Color.BLACK);
        }
    }

    @Override
    public int getItemCount() {
        return addProductsList.size() + 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView ivAddProduct;
        public TextView tvCatalog,tvCategory,tvProductName,tvBrand,tvCharacteristic,tvTipePacaging,
                tvUnitMeasure,tvWeightVolume,tvPrice,tvQuantity,tvQuantityPackage,
                tvCounterparty,tvDescription ;
        public TextView tvBtnDownloadData,tvBtnShowMore;
       // public Button button;//

        public  AddProductsCheckAdapter.RecyclerViewClickListener mListener;

        public ViewHolder(@NonNull View itemView,RecyclerViewClickListener listener) {
            super(itemView);
            mListener = listener;

            if(itemView == view_two){
                tvBtnDownloadData=itemView.findViewById(R.id.tvBtnDownloadData);
                tvBtnDownloadData.setOnClickListener(this);
                tvBtnShowMore=itemView.findViewById(R.id.tvBtnShowMore);
                tvBtnShowMore.setOnClickListener(this);
            }else {

                ivAddProduct = itemView.findViewById(R.id.ivAddProduct);
                tvCatalog = itemView.findViewById(R.id.tvCatalog);
                tvCategory = itemView.findViewById(R.id.tvCategory);
                tvProductName = itemView.findViewById(R.id.tvProductName);
                tvBrand = itemView.findViewById(R.id.tvBrand);
                tvCharacteristic = itemView.findViewById(R.id.tvCharacteristic);
                tvTipePacaging = itemView.findViewById(R.id.tvTipePacaging);
                tvUnitMeasure = itemView.findViewById(R.id.tvUnitMeasure);
                tvWeightVolume = itemView.findViewById(R.id.tvWeightVolume);
                tvPrice = itemView.findViewById(R.id.tvPrice);
                tvQuantity = itemView.findViewById(R.id.tvQuantity);
                tvQuantityPackage = itemView.findViewById(R.id.tvQuantityPackage);
                tvCounterparty = itemView.findViewById(R.id.tvCounterparty);
                tvDescription = itemView.findViewById(R.id.tvDescription);

                tvCatalog.setOnClickListener(this);
                tvCategory.setOnClickListener(this);
                tvProductName.setOnClickListener(this);
                tvBrand.setOnClickListener(this);
                tvCharacteristic.setOnClickListener(this);
                tvTipePacaging.setOnClickListener(this);
                tvUnitMeasure.setOnClickListener(this);
                tvCounterparty.setOnClickListener(this);
                tvDescription.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());
        }
    }
}
