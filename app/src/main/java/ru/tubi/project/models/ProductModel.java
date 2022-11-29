package ru.tubi.project.models;

import java.io.Serializable;

import ru.tubi.project.utilites.FirstSimbolMakeBig;

public class ProductModel implements Serializable {
    private int product_id;                 //0
    private int product_inventory_id;//
    private String catalog;
    private String category;
    private String product_name;
    private String brand;
    private String characteristic;          //4
    private String unit_measure;//
    private int weight_volume;//
    private int quantity_package;// 6
    private double price;
    private String image_url;//             //8
    private String description;//
    private String counterparty;//          //10
    private double quantity;
    private int count_product_provider;
    private String abbreviation;
    private long date_of_sale_millis;
    private double process_price;
    private int provider_warehouse_id;
    private int min_sell;
    private int multiple_of;
    private double free_inventory;
    private int guarante_there_is_goods;
    private int percent_no_goods;
    private int quantity_joint;
    private String product_info;
    private String type_packaging;
    private String in_product_name;
    private String storage_conditions;


    // ActivityProduct//splitResultProductArray
    public ProductModel(int product_id, int product_inventory_id, String category
            , String product_name, String brand, String characteristic
            , String unit_measure, int weight_volume, double price, double process_price
            , String image_url, int min_sell, int multiple_of, String description
            , double quantity
            , int count_product_provider, int quantity_package, long date_of_sale_millis
            , int provider_warehouse_id, double free_inventory, int guarante_there_is_goods
            , int percent_no_goods) {

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
        this.min_sell = min_sell;
        this.multiple_of = multiple_of;
        this.description = description;
        this.quantity = quantity;
        this.count_product_provider = count_product_provider;
        this.quantity_package=quantity_package;
        this.date_of_sale_millis=date_of_sale_millis;
        this.process_price=process_price;
        this.provider_warehouse_id=provider_warehouse_id;
        this.free_inventory=free_inventory;
        this.guarante_there_is_goods=guarante_there_is_goods;
        this.percent_no_goods=percent_no_goods;
    }
    //RedactProductCardActivity / splitResult();
    public ProductModel(int product_id, int product_inventory_id, String category
            , String product_name, String brand, String characteristic, String type_packaging
            , String unit_measure, int weight_volume, int quantity_package
            , String image_url, String storage_conditions, double price, String in_product_name
            , int min_sell, int multiple_of, String description, String product_info
            , String catalog) {

        this.product_id = product_id;
        this.product_inventory_id = product_inventory_id;
        this.category = category;
        this.product_name=product_name;
        this.brand = brand;
        this.characteristic = characteristic;
        this.type_packaging = type_packaging;
        this.unit_measure = unit_measure;
        this.weight_volume = weight_volume;
        this.quantity_package=quantity_package;
        this.image_url = image_url;
        this.storage_conditions = storage_conditions;
        this.price = price;
        this.in_product_name=in_product_name;
        this.min_sell = min_sell;
        this.multiple_of = multiple_of;
        this.description = description;
        this.product_info=product_info;
        this.catalog=catalog;
    }
    public ProductModel(int product_id, int product_inventory_id
            , String image_url, double price, double process_price
            , int min_sell, int quantity_joint, String product_info) {
        this.product_id = product_id;
        this.product_inventory_id = product_inventory_id;
        this.image_url = image_url;
        this.price = price;
        this.process_price=process_price;
        this.min_sell = min_sell;
        this.quantity_joint=quantity_joint;
        this.product_info=product_info;
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

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
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

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public int getCount_product_provider() {
        return count_product_provider;
    }

    public void setCount_product_provider(int count_product_provider) {
        this.count_product_provider = count_product_provider;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }


    public long getDate_of_sale_millis() {
        return date_of_sale_millis;
    }

    public void setDate_of_sale_millis(long date_of_sale_millis) {
        this.date_of_sale_millis = date_of_sale_millis;
    }

    public double getProcess_price() {
        return process_price;
    }

    public void setProcess_price(double process_price) {
        this.process_price = process_price;
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


    public int getGuarante_there_is_goods() {
        return guarante_there_is_goods;
    }

    public void setGuarante_there_is_goods(int guarante_there_is_goods) {
        this.guarante_there_is_goods = guarante_there_is_goods;
    }

    public int getPercent_no_goods() {
        return percent_no_goods;
    }

    public void setPercent_no_goods(int percent_no_goods) {
        this.percent_no_goods = percent_no_goods;
    }

    public int getQuantity_joint() {
        return quantity_joint;
    }

    public void setQuantity_joint(int quantity_joint) {
        this.quantity_joint = quantity_joint;
    }

    public String getProduct_info() {
        return product_info;
    }

    public void setProduct_info(String product_info) {
        this.product_info = product_info;
    }

    public String getType_packaging() {
        return type_packaging;
    }

    public void setType_packaging(String type_packaging) {
        this.type_packaging = type_packaging;
    }

    public String getIn_product_name() {
        return in_product_name;
    }

    public void setIn_product_name(String in_product_name) {
        this.in_product_name = in_product_name;
    }

    public String getStorage_conditions() {
        return storage_conditions;
    }

    public void setStorage_conditions(String storage_conditions) {
        this.storage_conditions = storage_conditions;
    }

    @Override
    public String toString() {
        return new FirstSimbolMakeBig().firstSimbolMakeBig(category)  + " "
                + product_name  + " " + characteristic  + " " + brand;
                          // +" цена: " +price+ " руб." + " поставщик "+provider
    }


}
