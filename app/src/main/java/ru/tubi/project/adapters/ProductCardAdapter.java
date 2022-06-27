package ru.tubi.project.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import ru.tubi.project.R;
import ru.tubi.project.models.ProductCardModel;
import ru.tubi.project.utilites.DownloadImage;
import ru.tubi.project.utilites.FirstSimbolMakeBig;
import ru.tubi.project.utilites.MakeImageToSquare;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static ru.tubi.project.Config.ADMIN_PANEL_URL_PREVIEW_IMAGES;
import static ru.tubi.project.free.AllCollor.TUBI_BLACK;
import static ru.tubi.project.free.AllCollor.TUBI_GREY_400;
import static ru.tubi.project.free.AllText.GRAMM_SHORT;
import static ru.tubi.project.free.AllText.NO_DELIVERY;
import static ru.tubi.project.free.AllText.ORDER_TEXT;
import static ru.tubi.project.free.AllText.QUANTITY_PACKAGE_SHORT;

public class ProductCardAdapter extends RecyclerView.Adapter<ProductCardAdapter.ViewHolder> {

    public interface OnProductClickListener{                                     //Listener
        void onProductCardClick(ProductCardModel product, int position);                //
    }                                                                   //
    private final ProductCardAdapter.OnProductClickListener onClickListener;

    public interface RecyclerViewClickListener{
        void onClick(View view, int position);

    }
    private final ProductCardAdapter.RecyclerViewClickListener clickListener;

    private final LayoutInflater inflater;
    private final List<ProductCardModel> allPrices;

    public ProductCardAdapter(Context context, List<ProductCardModel>allPrices, OnProductClickListener onClickListener,
                              ProductCardAdapter.RecyclerViewClickListener clickListener) {
        this.inflater = LayoutInflater.from(context);
        this.allPrices = allPrices;
        this.onClickListener = onClickListener;
        this.clickListener=clickListener;
    }

    @Override
    public ProductCardAdapter.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_product_card,parent,false);
        return new ProductCardAdapter.ViewHolder(view, clickListener);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        ProductCardModel myPrice = allPrices.get(position);

        double price = myPrice.getPrice() + myPrice.getProcess_price();
        double quantity=myPrice.getQuantity();
        int quantity_package = myPrice.getQuantity_package();
        String prod_info = new FirstSimbolMakeBig().firstSimbolMakeBig(myPrice.getCategory())+" "
                +myPrice.getProduct_name()+" "+myPrice.getCharacteristic()+" "
                +myPrice.getBrand()+", "+myPrice.getWeight_volume()+" "+myPrice.getUnit_measure()+", "//GRAMM_SHORT+", "
                +quantity_package+" "+QUANTITY_PACKAGE_SHORT;

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String myDate=NO_DELIVERY;
        if(myPrice.getDate_of_sale_millis() != 0){
            myDate = dateFormat.format(new Date(myPrice.getDate_of_sale_millis()));
        }

        if(!myPrice.getImage_url().equals("null")) {
            new DownloadImage(){
                @Override
                protected void onPostExecute(Bitmap result) {
                    try {
                        int check = result.getWidth();
                        // bitmap не пустой изображение есть
                        new MakeImageToSquare(result,holder.ivImageProduct);
                    } catch (Exception w) {
                        //bitmap пустой image не найден
                        holder.ivImageProduct.setImageResource(R.drawable.tubi_logo_no_image_300ps);
                    }
                }
            }
                    .execute(ADMIN_PANEL_URL_PREVIEW_IMAGES+myPrice.getImage_url());
        }else holder.ivImageProduct.setImageResource(R.drawable.tubi_logo_no_image_300ps);

        holder.tvProductDescription.setText(""+prod_info);
        holder.tvProvider.setText("" + new FirstSimbolMakeBig()
                .firstSimbolMakeBig(myPrice.getCounterparty()));
        holder.tvSumm.setText(String.format("%.2f", +quantity*price));
        //holder.tvWeightVolume.setText(""+myPrice.getWeight_volume());
       // holder.tvUnitMeasure.setText(""+myPrice.getUnit_measure());
        holder.tvPrice.setText(String.format("%.2f",+price));
        holder.tvQuantity.setText(""+quantity);
       // holder.tvQuantityPackage.setText(""+quantity_package);

        holder.tvDateForSale.setText(""+myDate);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onProductCardClick(myPrice,position);
            }
        });
        if(quantity == 0 ){
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            holder.tvSumm.setTextColor(TUBI_GREY_400);
            holder.llMinusAll.setVisibility(View.GONE);
            holder.llPlusTen.setVisibility(View.GONE);
            holder.llQuantity.setVisibility(View.GONE);
            holder.tvQuantity.setVisibility(View.GONE);
            holder.tvPlus.setText(ORDER_TEXT);
        }else{
            holder.llQuantity.setVisibility(View.VISIBLE);
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
        return allPrices.size();
    }//products.size();

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final ImageView ivImageProduct;
        final TextView tvProvider;
        final TextView tvProductDescription;
        final TextView tvPrice;
        final TextView tvDateForSale;
        final TextView tvQuantity;
       // final TextView tvQuantityPackage;
        final TextView tvSumm;
       // final TextView tvWeightVolume;
       // final TextView tvUnitMeasure;
        final TextView tvMinus, tvMinusTen;
        final TextView tvPlus, tvPlusTen;
        final LinearLayout llMinus,llMinusTen, llMinusAll, llPlus, llPlusTen, llQuantity;
        //final LinearLayout llMinus, llPlus, llQuantity;

        private final ProductCardAdapter.RecyclerViewClickListener mListener;

        public ViewHolder( View itemView, ProductCardAdapter.RecyclerViewClickListener listener) {
            super(itemView);

            ivImageProduct=itemView.findViewById(R.id.ivImageProduct);
            tvProvider=itemView.findViewById(R.id.tvProvider);
            tvProductDescription=itemView.findViewById(R.id.tvProductDescription);
            tvPrice=itemView.findViewById(R.id.tvPrice);
            //tvOrderForInfo=itemView.findViewById(R.id.tvOrderForInfo);
            tvDateForSale=itemView.findViewById(R.id.tvDateForSale);
            tvQuantity=itemView.findViewById(R.id.tvQuantity);
            //tvQuantityPackage=itemView.findViewById(R.id.tvQuantityPackage);
            tvSumm=itemView.findViewById(R.id.tvSumm);
            //tvWeightVolume=itemView.findViewById(R.id.tvWeightVolume);
            //tvUnitMeasure=itemView.findViewById(R.id.tvUnitMeasure);
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
}
