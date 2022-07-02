package ru.tubi.project.models;

import java.io.Serializable;

public class ShopingBoxModel implements Serializable {
    private int order_product_id ;
    private int product_id;                 //1
    private int product_inventory_id;
    private String category;                //3
    String product_name;
    private String brand;
    private String characteristic;          //5
    private String unit_measure;
    private int weight_volume;            //7
    private double price;
    double price_process;
    private String image_url;             //9
    private String description;
    private String counterparty;          //11
    private double quantity;
    private int quantity_package;
    private int provider_warehouse_id;
    private int min_sell;
    private int multiple_of;
    private double free_inventory;
    private String product_info;

    // ShopingBox//splitResultShoringBoxArray();
    public ShopingBoxModel(int order_product_id, int product_id, int product_inventory_id
            ,String category, String product_name, String brand, String characteristic
            , String unit_measure, int weight_volume, double price,double price_process
            , String image_url, String description_prod, String counterparty
            , double quantity, int quantity_package, int provider_warehouse_id
            , int min_sell, int multiple_of, double free_inventory
            , String product_info) {

        this.order_product_id = order_product_id;
        this.product_id = product_id;
        this.product_inventory_id = product_inventory_id;
        this.category = category;
        this.product_name=product_name;
        this.brand = brand;
        this.characteristic = characteristic;
        this.unit_measure = unit_measure;
        this.weight_volume = weight_volume;
        this.price = price;
        this.price_process=price_process;
        this.image_url = image_url;
        this.counterparty=counterparty;
        this.quantity=quantity;
        this.quantity_package=quantity_package;
        this.provider_warehouse_id=provider_warehouse_id;
        this.min_sell = min_sell;
        this.multiple_of = multiple_of;
        this.free_inventory = free_inventory;
        this.product_info = product_info;
    }

    public ShopingBoxModel(int order_product_id, int product_id, int product_inventory_id,
                           String category, String product_name, String brand,
                           String characteristic, String unit_measure, int weight_volume,
                           double price,double price_process, String image_url, String description,
                           String counterparty, double quantity, int quantity_package
            , int provider_warehouse_id) {
        this.order_product_id = order_product_id;
        this.product_id = product_id;
        this.product_inventory_id = product_inventory_id;
        this.category = category;
        this.product_name=product_name;
        this.brand = brand;
        this.characteristic = characteristic;
        this.unit_measure = unit_measure;
        this.weight_volume = weight_volume;
        this.price = price;
        this.price_process=price_process;
        this.image_url = image_url;
        this.description = description;
        this.counterparty=counterparty;
        this.quantity=quantity;
        this.quantity_package=quantity_package;
        this.provider_warehouse_id=provider_warehouse_id;
    }
    // ProductInvoiceCreateActivity//splitResultShoringBoxArray();
    public ShopingBoxModel(int order_product_id, int product_id, int product_inventory_id,
                           String category,  String brand,
                           String characteristic, String unit_measure, int weight_volume,
                           double price, String image_url, String description,
                           String counterparty, double quantity) {
        this.order_product_id = order_product_id;
        this.product_id = product_id;
        this.product_inventory_id = product_inventory_id;
        this.category = category;
        this.product_name=product_name;
        this.brand = brand;
        this.characteristic = characteristic;
        this.unit_measure = unit_measure;
        this.weight_volume = weight_volume;
        this.price = price;
        this.image_url = image_url;
        this.description = description;
        this.counterparty=counterparty;
        this.quantity=quantity;
    }
    public int getOrder_product_id() {
        return order_product_id;
    }

    public void setOrder_product_id(int order_product_id) {
        this.order_product_id = order_product_id;
    }

    public int getProduct_inventory_id() {
        return product_inventory_id;
    }

    public void setProduct_inventory_id(int product_inventory_id) {
        this.product_inventory_id = product_inventory_id;
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

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(String counterparty) {
        this.counterparty = counterparty;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice_process() {
        return price_process;
    }

    public void setPrice_process(double price_process) {
        this.price_process = price_process;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public int getQuantity_package() {
        return quantity_package;
    }

    public void setQuantity_package(int quantity_package) {
        this.quantity_package = quantity_package;
    }

    public int getProvider_warehouse_id() {
        return provider_warehouse_id;
    }

    public void setProvider_warehouse_id(int provider_warehouse_id) {
        this.provider_warehouse_id = provider_warehouse_id;
    }

    public int getMin_sell() {
        return min_sell;
    }

    public void setMin_sell(int min_sell) {
        this.min_sell = min_sell;
    }

    public int getMultiple_of() {
        return multiple_of;
    }

    public void setMultiple_of(int multiple_of) {
        this.multiple_of = multiple_of;
    }

    public double getFree_inventory() {
        return free_inventory;
    }

    public void setFree_inventory(double free_inventory) {
        this.free_inventory = free_inventory;
    }

    public String getProduct_info() {
        return product_info;
    }

    public void setProduct_info(String product_info) {
        this.product_info = product_info;
    }

    @Override
    public String toString() {
        return category  + " " + characteristic + " " +  brand;
        // +" цена: " +price+ " руб." + " поставщик "+provider
    }
}
