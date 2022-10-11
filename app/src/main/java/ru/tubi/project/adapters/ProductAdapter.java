package ru.tubi.project.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.RecyclerView;

import ru.tubi.project.R;
import ru.tubi.project.models.ProductModel;
import ru.tubi.project.utilites.DownloadImage;
import ru.tubi.project.utilites.FirstSimbolMakeBig;
import ru.tubi.project.utilites.MakeImageToSquare;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static ru.tubi.project.activity.Config.ADMIN_PANEL_URL_PREVIEW_IMAGES;
import static ru.tubi.project.free.AllCollor.TUBI_BLACK;
import static ru.tubi.project.free.AllCollor.TUBI_GREY_400;
import static ru.tubi.project.free.AllText.DELIVERY_TEXT;
import static ru.tubi.project.free.AllText.MORE_SMALL;
import static ru.tubi.project.free.AllText.NO_DELIVERY;
import static ru.tubi.project.free.AllText.ORDER_TEXT;
import static ru.tubi.project.free.VariablesHelpers.DELIVERY_TO_BUYER_STATUS;
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
        //int quantityPackage = product.getQuantity_package();
      /*  String productInfo=new FirstSimbolMakeBig().firstSimbolMakeBig(product.getCategory()  + " "
                + product.getProduct_name() + " " +product.getCharacteristic() + " "
                +product.getBrand())+", "+product.getWeight_volume()+" "
                +product.getUnit_measure()+", "//GRAMM_SHORT+", "
                +quantityPackage+" "+QUANTITY_PACKAGE_SHORT;*/
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

        holder.tvCountProvider.setText(""+product.getCount_product_provider()+" "+provider);
        holder.tvPercentNoGoods.setText(""+product.getPercent_no_goods()+" %");
        holder.tvProductInfo.setText(""+new FirstSimbolMakeBig()
                .firstSimbolMakeBig(product.getDescription()));
        holder.tvPrice.setText(String.format("%.2f",+price));
        holder.tvSumm.setText(String.format("%.2f",+quantity*price));
        holder.tvQuantity.setText(""+quantity);
        holder.tvDateForSale.setText(""+myDate);
        if(DELIVERY_TO_BUYER_STATUS == 1)holder.tvDeliveryStatus.setText(""+DELIVERY_TEXT);
        else holder.tvDeliveryStatus.setText("");
        //если свободные запасы боьше 3х уп то показать только 3 уп
        if(product.getFree_inventory() > (product.getQuantity_package() * 3)){
            holder.tvStocksGoods.setText(""+MORE_SMALL+" "
                    +(product.getQuantity_package() * 3));
        }else{
            //запас меньше 3х упаковок
            holder.tvStocksGoods.setText(""+product.getFree_inventory());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onProductClick(product,position);
            }                                                                    //
        });
        if(product.getGuarante_there_is_goods() == 1){
            holder.ivGuaranteThereIsGoods.setImageResource(R.drawable.oval_green_shape);
        }else{
            holder.ivGuaranteThereIsGoods.setImageResource(R.drawable.oval_grey_shape);
        }
        if(quantity == 0 ){
            holder.tvSumm.setTextColor(TUBI_GREY_400);
            holder.llMinusAll.setVisibility(View.GONE);
            holder.llQuantity.setVisibility(View.GONE);
            holder.tvQuantity.setVisibility(View.GONE);
            holder.tvPlus.setText(ORDER_TEXT+" "+product.getMin_sell());
        }else{
            holder.llQuantity.setVisibility(View.VISIBLE);
            holder.llMinusAll.setVisibility(View.VISIBLE);
            holder.tvPlus.setText("+"+product.getMultiple_of());

            if(quantity > product.getMin_sell()){
                holder.tvMinus.setText("-"+product.getMultiple_of());
            }else{
                holder.tvMinus.setText("-"+product.getMin_sell());
            }
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
        final TextView tvPercentNoGoods;
        final TextView tvProductInfo;
        final TextView tvPrice;
        final TextView tvSumm;
        final TextView tvQuantity, tvStocksGoods;
        final TextView tvDateForSale;
        final TextView tvMinus;//, tvMinusTen;
        final TextView tvPlus;//, tvPlusTen;
        final TextView tvDeliveryStatus;

        final ImageView ivGuaranteThereIsGoods;
        final LinearLayout llProviderIfo, llMinus,llMinusAll, llPlus, llQuantity;//llPlusTen,llMinusTen,  ;

        private final RecyclerViewClickListener mListener;

        public ViewHolder( View itemView,RecyclerViewClickListener listener) {
            super(itemView);
            ivImageProduct=itemView.findViewById(R.id.ivImageProduct);
            tvCountProvider=itemView.findViewById(R.id.tvCountProvider);
            tvPercentNoGoods=itemView.findViewById(R.id.tvPercentNoGoods);
            tvProductInfo=itemView.findViewById(R.id.tvProductInfo);
            tvPrice=itemView.findViewById(R.id.tvPrice);
            tvSumm=itemView.findViewById(R.id.tvSumm);
            tvQuantity=itemView.findViewById(R.id.tvQuantity);
            tvStocksGoods=itemView.findViewById(R.id.tvStocksGoods);
            tvDateForSale=itemView.findViewById(R.id.tvDateForSale);
            tvMinus=itemView.findViewById(R.id.tvMinus);
            //tvMinusTen=itemView.findViewById(R.id.tvMinusTen);
            tvPlus=itemView.findViewById(R.id.tvPlus);
            tvDeliveryStatus=itemView.findViewById(R.id.tvDeliveryStatus);

            ivGuaranteThereIsGoods=itemView.findViewById(R.id.ivGuaranteThereIsGoods);
            llMinusAll=itemView.findViewById(R.id.llMinusAll);
            llMinus=itemView.findViewById(R.id.llMinus);
            llProviderIfo=itemView.findViewById(R.id.llProviderIfo);
            llPlus=itemView.findViewById(R.id.llPlus);
            //llPlusTen=itemView.findViewById(R.id.llPlusTen);
            llQuantity=itemView.findViewById(R.id.llQuantity);

            mListener=listener;
            llMinus.setOnClickListener(this);
            //llMinusTen.setOnClickListener(this);
            llPlus.setOnClickListener(this);
            llProviderIfo.setOnClickListener(this);
            tvQuantity.setOnClickListener(this);
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
