package ru.tubi.project.models;

public class ProviderCollectProductModel {
    private int product_id;
    private int productInventory_id;
    private String category;
    private String product_name;
    private String brand;
    private String characteristic;
    private String type_packaging;
    private String unit_measure;
    private int weight_volume;
    private int quantity_package;
    private String image_url;
    private String storage_conditions;
    private int warehouse_inventory_id;
    private double quantity_to_deal;
    private int logistic_product;
    private int car_id;
    private double provider_stock_quantity;
    private int collected_check;
    private long get_order_date_millis;
    private int corrected;
    private String description;
    private String product_name_from_provider;
    private int order_product_part_id;
    private int status_collect_provider;
    private double quantity_full_orders;
    private double quantity_deleted_product;
    private String product_info;
    private int deleted_goods_id;

    //ProviderCollectProductActivity / splitDeletedGoodsListResult();
    public ProviderCollectProductModel(int order_product_part_id , int productInventory_id
            , int warehouse_inventory_id, double quantity_full_orders, double quantity_deleted_product
            , int status_collect_provider, String product_info, int deleted_goods_id) {
        this.order_product_part_id = order_product_part_id;
        this.productInventory_id = productInventory_id;
        this.warehouse_inventory_id = warehouse_inventory_id;
        this.quantity_full_orders = quantity_full_orders;
        this.quantity_deleted_product = quantity_deleted_product;
        this.status_collect_provider = status_collect_provider;
        this.product_info = product_info;
        this.deleted_goods_id = deleted_goods_id;
    }

    //ProviderCollectProductActivity //splitProductForCollectResult();
    public ProviderCollectProductModel(int product_id, int productInventory_id
            , String category, String brand, String characteristic, String type_packaging
            , String unit_measure, int weight_volume, int quantity_package
            , String image_url, String storage_conditions, int warehouse_inventory_id
            , double quantity_to_deal, int logistic_product, int car_id
            , double provider_stock_quantity, int collected_check
            , long get_order_date_millis, String product_name, int corrected
            ,String description, String product_name_from_provider) {

        this.product_id = product_id;
        this.productInventory_id = productInventory_id;
        this.category = category;
        this.product_name=product_name;
        this.brand = brand;
        this.characteristic = characteristic;
        this.type_packaging = type_packaging;
        this.unit_measure = unit_measure;
        this.weight_volume = weight_volume;
        this.quantity_package = quantity_package;
        this.image_url = image_url;
        this.storage_conditions = storage_conditions;
        this.warehouse_inventory_id = warehouse_inventory_id;
        this.quantity_to_deal = quantity_to_deal;
        this.logistic_product = logistic_product;
        this.car_id = car_id;
        this.provider_stock_quantity = provider_stock_quantity;
        this.collected_check = collected_check;
        this.get_order_date_millis = get_order_date_millis;
        this.corrected=corrected;
        this.description=description;
        this.product_name_from_provider=product_name_from_provider;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getProductInventory_id() {
        return productInventory_id;
    }

    public void setProductInventory_id(int productInventory_id) {
        this.productInventory_id = productInventory_id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(String characteristic) {
        this.characteristic = characteristic;
    }

    public String getType_packaging() {
        return type_packaging;
    }

    public void setType_packaging(String type_packaging) {
        this.type_packaging = type_packaging;
    }

    public String getUnit_measure() {
        return unit_measure;
    }

    public void setUnit_measure(String unit_measure) {
        this.unit_measure = unit_measure;
    }

    public int getWeight_volume() {
        return weight_volume;
    }

    public void setWeight_volume(int weight_volume) {
        this.weight_volume = weight_volume;
    }

    public int getQuantity_package() {
        return quantity_package;
    }

    public void setQuantity_package(int quantity_package) {
        this.quantity_package = quantity_package;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getStorage_conditions() {
        return storage_conditions;
    }

    public void setStorage_conditions(String storage_conditions) {
        this.storage_conditions = storage_conditions;
    }

    public int getWarehouse_inventory_id() {
        return warehouse_inventory_id;
    }

    public void setWarehouse_inventory_id(int warehouse_inventory_id) {
        this.warehouse_inventory_id = warehouse_inventory_id;
    }

    public double getQuantity_to_deal() {
        return quantity_to_deal;
    }

    public void setQuantity_to_deal(double quantity_to_deal) {
        this.quantity_to_deal = quantity_to_deal;
    }

    public int getLogistic_product() {
        return logistic_product;
    }

    public void setLogistic_product(int logistic_product) {
        this.logistic_product = logistic_product;
    }

    public int getCar_id() {
        return car_id;
    }

    public void setCar_id(int car_id) {
        this.car_id = car_id;
    }

    public double getProvider_stock_quantity() {
        return provider_stock_quantity;
    }

    public void setProvider_stock_quantity(double provider_stock_quantity) {
        this.provider_stock_quantity = provider_stock_quantity;
    }

    public int getCollected_check() {
        return collected_check;
    }

    public void setCollected_check(int collected_check) {
        this.collected_check = collected_check;
    }

    public long getGet_order_date_millis() {
        return get_order_date_millis;
    }

    public void setGet_order_date_millis(long get_order_date_millis) {
        this.get_order_date_millis = get_order_date_millis;
    }

    public int getCorrected() {
        return corrected;
    }

    public void setCorrected(int corrected) {
        this.corrected = corrected;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProduct_name_from_provider() {
        return product_name_from_provider;
    }

    public void setProduct_name_from_provider(String product_name_from_provider) {
        this.product_name_from_provider = product_name_from_provider;
    }

    public int getOrder_product_part_id() {
        return order_product_part_id;
    }

    public void setOrder_product_part_id(int order_product_part_id) {
        this.order_product_part_id = order_product_part_id;
    }

    public int getStatus_collect_provider() {
        return status_collect_provider;
    }

    public void setStatus_collect_provider(int status_collect_provider) {
        this.status_collect_provider = status_collect_provider;
    }

    public double getQuantity_full_orders() {
        return quantity_full_orders;
    }

    public void setQuantity_full_orders(double quantity_full_orders) {
        this.quantity_full_orders = quantity_full_orders;
    }

    public double getQuantity_deleted_product() {
        return quantity_deleted_product;
    }

    public void setQuantity_deleted_product(double quantity_deleted_product) {
        this.quantity_deleted_product = quantity_deleted_product;
    }

    public String getProduct_info() {
        return product_info;
    }

    public void setProduct_info(String product_info) {
        this.product_info = product_info;
    }

    public int getDeleted_goods_id() {
        return deleted_goods_id;
    }

    public void setDeleted_goods_id(int deleted_goods_id) {
        this.deleted_goods_id = deleted_goods_id;
    }
}
