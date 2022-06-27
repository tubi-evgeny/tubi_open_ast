package ru.tubi.project.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import ru.tubi.project.R;
import ru.tubi.project.models.ProductModel;
import ru.tubi.project.utilites.DownloadImage;
import ru.tubi.project.utilites.FirstSimbolMakeBig;
import ru.tubi.project.utilites.MakeImageToSquare;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static ru.tubi.project.Config.ADMIN_PANEL_URL_IMAGES;
import static ru.tubi.project.Config.ADMIN_PANEL_URL_PREVIEW_IMAGES;
import static ru.tubi.project.free.AllCollor.TUBI_BLACK;
import static ru.tubi.project.free.AllCollor.TUBI_GREEN_600;
import static ru.tubi.project.free.AllCollor.TUBI_GREY_400;
import static ru.tubi.project.free.AllCollor.TUBI_WHITE;
import static ru.tubi.project.free.AllText.GRAMM_SHORT;
import static ru.tubi.project.free.AllText.NO_DELIVERY;
import static ru.tubi.project.free.AllText.ORDER_TEXT;
import static ru.tubi.project.free.AllText.QUANTITY_PACKAGE_SHORT;
import static ru.tubi.project.free.AllText.SUPPLIER_ONE;
import static ru.tubi.project.free.AllText.SUPPLIER_THREE;
import static ru.tubi.project.free.AllText.SUPPLIER_TWO;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    public interface OnProductClickListener{                                     //Listener
        void onProductClick(ProductModel product, int position);                //
    }                                                                   //
    private final OnProductClickListener onClickListener;

    public interface RecyclerViewClickListener{
        void onClick(View view, int position);

    }
    private final ProductAdapter.RecyclerViewClickListener clickListener;

    private final LayoutInflater inflater;
    private final List<ProductModel> categoryes;

    public ProductAdapter(Context context, List<ProductModel> categoryes, OnProductClickListener onClickListener,
                          ProductAdapter.RecyclerViewClickListener clickListener) {
        this.inflater =  LayoutInflater.from(context);
        this.categoryes = categoryes;
        this.onClickListener=onClickListener;
        this.clickListener=clickListener;
    }

    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_product,parent,false);
        return new ProductAdapter.ViewHolder(view,clickListener);
    }

    @Override
    public void onBindViewHolder(ProductAdapter.ViewHolder holder, int position) {
        ProductModel product=categoryes.get(position);
        double price = product.getPrice() + product.getProcess_price();
        double quantity = product.getQuantity();
        int quantityPackage = product.getQuantity_package();
        String productInfo=new FirstSimbolMakeBig().firstSimbolMakeBig(product.getCategory()  + " "
                + product.getProduct_name() + " " +product.getCharacteristic() + " "
                +product.getBrand())+", "+product.getWeight_volume()+" "
                +product.getUnit_measure()+", "//GRAMM_SHORT+", "
                +quantityPackage+" "+QUANTITY_PACKAGE_SHORT;
        String provider = getTheRightIdea(product.getCount_product_provider());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String myDate=NO_DELIVERY;
        if(product.getDate_of_sale_millis() != 0){
            myDate = dateFormat.format(new Date(product.getDate_of_sale_millis()));
        }

        if(!product.getImage_url().equals("null")) {
            new DownloadImage(){
                @Override
                protected void onPostExecute(Bitmap result) {
                    try {
                        int check = result.getWidth();
                        new MakeImageToSquare(result, holder.ivImageProduct);
                    }catch (Exception w) {
                        //bitmap пустой image не найден
                        holder.ivImageProduct.setImageResource(R.drawable.tubi_logo_no_image_300ps);
                    }
                }
            }
                    .execute(ADMIN_PANEL_URL_PREVIEW_IMAGES+product.getImage_url());
        }else holder.ivImageProduct.setImageResource(R.drawable.tubi_logo_no_image_300ps);

        holder.tvCountProvider.setText(product.getCount_product_provider()+" "+provider);
        holder.tvCategoryName.setText(""+productInfo);
        holder.tvPrice.setText(String.format("%.2f",+price));
        //holder.tvWeightVolume.setText(""+categoryes.get(position).getWeight_volume());
        //holder.tvUnitMeasure.setText(""+categoryes.get(position).getUnit_measure());
        holder.tvSumm.setText(String.format("%.2f",+quantity*price));
        holder.tvQuantity.setText(""+quantity);
        holder.tvDateForSale.setText(""+myDate);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onProductClick(product,position);
            }// Listener                                                                      //
        });
        if(quantity == 0 ){
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            holder.tvSumm.setTextColor(TUBI_GREY_400);
           // holder.llMinus.setVisibility(View.GONE);
            //holder.llMinusTen.setVisibility(View.GONE);
            holder.llMinusAll.setVisibility(View.GONE);
            holder.llPlusTen.setVisibility(View.GONE);
            holder.llQuantity.setVisibility(View.GONE);
            holder.tvQuantity.setVisibility(View.GONE);
            holder.tvPlus.setText(ORDER_TEXT);
        }else{
            holder.llQuantity.setVisibility(View.VISIBLE);
           // holder.llMinus.setVisibility(View.VISIBLE);
           // holder.llMinusTen.setVisibility(View.VISIBLE);
            holder.llMinusAll.setVisibility(View.VISIBLE);
            holder.llPlusTen.setVisibility(View.VISIBLE);
            holder.tvPlus.setText("+");
            holder.tvQuantity.setTextColor(TUBI_BLACK);
            holder.tvSumm.setTextColor(TUBI_BLACK);
            holder.tvQuantity.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public int getItemCount() {
        return categoryes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView ivImageProduct;
        final TextView tvCountProvider;
        final TextView tvCategoryName;
        final TextView tvPrice;
        //final TextView tvWeightVolume;
        //final TextView tvUnitMeasure;
        final TextView tvSumm;
        final TextView tvQuantity;
        final TextView tvDateForSale;
        final TextView tvMinus, tvMinusTen;
        final TextView tvPlus, tvPlusTen;
        final LinearLayout llMinus,llMinusTen, llMinusAll, llPlus, llPlusTen, llQuantity;

        private final RecyclerViewClickListener mListener;

        public ViewHolder( View itemView,RecyclerViewClickListener listener) {
            super(itemView);
            ivImageProduct=itemView.findViewById(R.id.ivImageProduct);
            tvCountProvider=itemView.findViewById(R.id.tvCountProvider);
            tvCategoryName=itemView.findViewById(R.id.tvCategoriName);
            tvPrice=itemView.findViewById(R.id.tvPrice);
            //tvWeightVolume=itemView.findViewById(R.id.tvWeightVolume);
            //tvUnitMeasure=itemView.findViewById(R.id.tvUnitMeasure);
            tvSumm=itemView.findViewById(R.id.tvSumm);
            tvQuantity=itemView.findViewById(R.id.tvQuantity);
            tvDateForSale=itemView.findViewById(R.id.tvDateForSale);
            tvMinus=itemView.findViewById(R.id.tvMinus);
            tvMinusTen=itemView.findViewById(R.id.tvMinusTen);
            tvPlus=itemView.findViewById(R.id.tvPlus);
            tvPlusTen=itemView.findViewById(R.id.tvPlusTen);

            llMinusAll=itemView.findViewById(R.id.llMinusAll);
            llMinus=itemView.findViewById(R.id.llMinus);
            llMinusTen=itemView.findViewById(R.id.llMinusTen);
            llPlus=itemView.findViewById(R.id.llPlus);
            llPlusTen=itemView.findViewById(R.id.llPlusTen);
            llQuantity=itemView.findViewById(R.id.llQuantity);
            mListener=listener;
            llMinus.setOnClickListener(this);
            llMinusTen.setOnClickListener(this);
            llPlus.setOnClickListener(this);
            llPlusTen.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v, getAdapterPosition());
        }
    }
    private String getTheRightIdea(int count){
        //склоненние для поставщик
        String provider = "";
        if(count == 1){
            provider = SUPPLIER_ONE;
        }else if(count > 1 && count < 5){
            provider = SUPPLIER_TWO;
        }else{
            provider = SUPPLIER_THREE;
        }
        return provider;
    }
}