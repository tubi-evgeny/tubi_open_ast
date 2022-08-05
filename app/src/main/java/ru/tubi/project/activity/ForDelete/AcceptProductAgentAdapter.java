package ru.tubi.project.activity.ForDelete;

/*public class AcceptProductAgentAdapter
        extends RecyclerView.Adapter<AcceptProductAgentAdapter.ViewHolder> {

    public interface RecyclerViewClickListener{
        void onClick(View view, int position);
    }


    private final AcceptProductAgentAdapter.RecyclerViewClickListener clickListener;

    private final LayoutInflater inflater;
    private final List<AcceptProductListProvidersModel> products;

    public AcceptProductAgentAdapter(Context context, List<AcceptProductListProvidersModel> products,
                                     AcceptProductAgentAdapter.RecyclerViewClickListener clickListener) {
        this.clickListener = clickListener;
        this.inflater = LayoutInflater.from(context);
        this.products = products;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_accept_product,parent,false);
        return new AcceptProductAgentAdapter.ViewHolder(view,clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AcceptProductListProvidersModel product = products.get(position);

        holder.tvProductDescription.setText(""+product.getCategory()+" "+product.getBrand()
                +" "+product.getCharacteristic()+" "+product.getWeight_volume()+" "+product.getUnit_measure()
                +" "+IN_PACKAGE+" "+product.getQuantity_package());
        holder.tvQuantity.setText(""+product.getQuantity());
        holder.tvQuantity.setBackgroundColor(TUBI_YELLOW_200);

        holder.tvQuantityText.setText(QUANTITY_SPLIT_1+"\n"+QUANTITY_SPLIT_2);

        if(!product.getImage_url().equals("null")) {
            new DownloadImage(holder.ivImageProduct)
                    .execute(ADMIN_PANEL_URL_IMAGES+product.getImage_url());
        }else holder.ivImageProduct.setImageResource(R.drawable.tubi_logo_50ps);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
                                                     implements View.OnClickListener{
        final TextView tvProductDescription;
        final  TextView tvQuantity;
        final  TextView tvQuantityText;

        final ImageView ivCheck;
        final ImageView ivImageProduct;

        final RecyclerViewClickListener myListener;

        public ViewHolder(@NonNull View itemView,
                          AcceptProductAgentAdapter.RecyclerViewClickListener listener) {
            super(itemView);

            myListener=listener;

            tvProductDescription = itemView.findViewById(R.id.tvProductDescription);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvQuantityText=itemView.findViewById(R.id.tvQuantityText);

            ivImageProduct = itemView.findViewById(R.id.ivImageProduct);
            ivCheck = itemView.findViewById(R.id.ivCheck);

            ivImageProduct.setOnClickListener(this);
            ivCheck.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myListener.onClick(v,getAdapterPosition());
        }
    }
}*/
