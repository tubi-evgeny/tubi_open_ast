package ru.tubi.project.models;

import android.graphics.Bitmap;

import java.io.Serializable;

public class CatalogProductProviderModel implements Serializable {
    private int product_id;
    private int product_inventory_id;       //1
    private String category;
    private String product_name;
    private String brand;                   //3
    private String characteristic;
    private String type_packaging;          //5
    private String unit_measure;
    private int weight_volume;              //7
    private double total_quantity;
    private double price;                   //9
    private int quantity_package;
    private String image_url;               //11
    private String description;
    private double total_sale_quantity;     //13
    private double free_balance;
    private double quantity;
    private int order_product_id;
    private int checked;
    private int order_id;
    private String product_info;
    private String product_name_from_provider;
    private Bitmap bmt;

    public CatalogProductProviderModel(String characteristic) {
        this.characteristic = characteristic;
    }

    public CatalogProductProviderModel(int order_product_id,int product_id, int product_inventory_id, String category,
                  String brand, String characteristic, String type_packaging, String unit_measure,
                  int weight_volume, double price, int quantity_package, String image_url,
                                       double quantity,int checked,int order_id) {
        this.order_product_id=order_product_id;
        this.product_id = product_id;
        this.product_inventory_id = product_inventory_id;
        this.category = category;
        this.brand = brand;
        this.characteristic = characteristic;
        this.type_packaging = type_packaging;
        this.unit_measure = unit_measure;
        this.weight_volume = weight_volume;
        this.price = price;
        this.quantity_package = quantity_package;
        this.image_url = image_url;
        this.quantity = quantity;
        this.checked=checked;
        this.order_id=order_id;
    }
    // CatalogStocksActivity//splitResult();
    public CatalogProductProviderModel(int product_id, int product_inventory_id
            , String category, String product_name, String brand, String characteristic
            , String type_packaging, String unit_measure, int weight_volume
            , double total_quantity, double price, int quantity_package
            , String image_url, String description, double total_sale_quantity
            , double free_balance,String product_info, String product_name_from_provider
            , Bitmap bmt) {
        this.product_id = product_id;
        this.product_inventory_id = product_inventory_id;
        this.category = category;
        this.product_name=product_name;
        this.brand = brand;
        this.characteristic = characteristic;
        this.type_packaging = type_packaging;
        this.unit_measure = unit_measure;
        this.weight_volume = weight_volume;
        this.total_quantity = total_quantity;
        this.price = price;
        this.quantity_package = quantity_package;
        this.image_url = image_url;
        this.description = description;
        this.total_sale_quantity = total_sale_quantity;
        this.free_balance = free_balance;
        this.product_info = product_info;
        this.product_name_from_provider = product_name_from_provider;
        this.bmt = bmt;
    }
    // CatalogStocksActivity//copyProductcard();
    public CatalogProductProviderModel(int product_id, int product_inventory_id
            , String category, String product_name, String brand, String characteristic
            , String type_packaging, String unit_measure, int weight_volume
            , double total_quantity, double price, int quantity_package
            , String image_url, String description, double total_sale_quantity
            , double free_balance,String product_info, String product_name_from_provider) {
        this.product_id = product_id;
        this.product_inventory_id = product_inventory_id;
        this.category = category;
        this.product_name=product_name;
        this.brand = brand;
        this.characteristic = characteristic;
        this.type_packaging = type_packaging;
        this.unit_measure = unit_measure;
        this.weight_volume = weight_volume;
        this.total_quantity = total_quantity;
        this.price = price;
        this.quantity_package = quantity_package;
        this.image_url = image_url;
        this.description = description;
        this.total_sale_quantity = total_sale_quantity;
        this.free_balance = free_balance;
        this.product_info = product_info;
        this.product_name_from_provider = product_name_from_provider;
    }
    // WriteOutProductActivity//splitResult();
    public CatalogProductProviderModel(int product_id, int product_inventory_id, String category,
                   String product_name, String brand, String characteristic, String type_packaging,
                   String unit_measure,
                   int weight_volume, double total_quantity, double price, int quantity_package,
                   String image_url, String description, double total_sale_quantity,
                   double free_balance) {
        this.product_id = product_id;
        this.product_inventory_id = product_inventory_id;
        this.category = category;
        this.product_name=product_name;
        this.brand = brand;
        this.characteristic = characteristic;
        this.type_packaging = type_packaging;
        this.unit_measure = unit_measure;
        this.weight_volume = weight_volume;
        this.total_quantity = total_quantity;
        this.price = price;
        this.quantity_package = quantity_package;
        this.image_url = image_url;
        this.description = description;
        this.total_sale_quantity = total_sale_quantity;
        this.free_balance = free_balance;
    }


    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getProduct_inventory_id() {
        return product_inventory_id;
    }

    public void setProduct_inventory_id(int product_inventory_id) {
        this.product_inventory_id = product_inventory_id;
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

    public double getTotal_quantity() {
        return total_quantity;
    }

    public void setTotal_quantity(double total_quantity) {
        this.total_quantity = total_quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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

    public double getTotal_sale_quantity() {
        return total_sale_quantity;
    }

    public void setTotal_sale_quantity(double total_sale_quantity) {
        this.total_sale_quantity = total_sale_quantity;
    }


    public double getFree_balance() {
        return free_balance;
    }

    public void setFree_balance(double free_balance) {
        this.free_balance = free_balance;
    }


    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public int getOrder_product_id() {
        return order_product_id;
    }

    public void setOrder_product_id(int order_product_id) {
        this.order_product_id = order_product_id;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getProduct_info() {
        return product_info;
    }

    public void setProduct_info(String product_info) {
        this.product_info = product_info;
    }

    public String getProduct_name_from_provider() {
        return product_name_from_provider;
    }

    public void setProduct_name_from_provider(String product_name_from_provider) {
        this.product_name_from_provider = product_name_from_provider;
    }

    public Bitmap getBmt() {
        return bmt;
    }

    public void setBmt(Bitmap bmt) {
        this.bmt = bmt;
    }

    @Override
    public String toString() {
        return   product_info+ " " + product_name_from_provider;//return   category + " " + brand + " " + characteristic;
    }

}
