package ru.tubi.project.models;

import java.io.Serializable;

import ru.tubi.project.utilites.FirstSimbolMakeBig;

public class ProductModel implements Serializable {
    private int product_id;                 //0
    private int product_inventory_id;//
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

    // ActivityProduct//splitResultProductArray
    public ProductModel(int product_id, int product_inventory_id, String category
            , String product_name, String brand, String characteristic
            , String unit_measure, int weight_volume, double price, double process_price
            , String image_url, String abbreviation, String counterparty, double quantity
            , int count_product_provider, int quantity_package, long date_of_sale_millis
            ,int provider_warehouse_id) {

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
        this.counterparty = counterparty;
        this.quantity = quantity;
        this.count_product_provider = count_product_provider;
        this.abbreviation = abbreviation;
        this.quantity_package=quantity_package;
        this.date_of_sale_millis=date_of_sale_millis;
        this.process_price=process_price;
        this.provider_warehouse_id=provider_warehouse_id;
    }
    /*
        // ActivityProduct//splitResultProductArray
    public ProductModel(int product_id, int product_inventory_id, String category,
                        String product_name, String brand,
                        String characteristic, String unit_measure, int weight_volume, double price,
                        String image_url, String abbreviation, String counterparty, double quantity,
                        int count_product_provider, int quantity_package) {
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
        this.counterparty = counterparty;
        this.quantity = quantity;
        this.count_product_provider = count_product_provider;
        this.abbreviation = abbreviation;
        this.quantity_package=quantity_package;
    }
     */


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

    @Override
    public String toString() {
        return new FirstSimbolMakeBig().firstSimbolMakeBig(category)  + " "
                + product_name  + " " + characteristic  + " " + brand;
                          // +" цена: " +price+ " руб." + " поставщик "+provider
    }


}
