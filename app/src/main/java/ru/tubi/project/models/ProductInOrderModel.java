package ru.tubi.project.models;

public class ProductInOrderModel {
    private int product_id;
    private int product_inventory_id;       //1
    private String category;
    private String brand;                   //3
    private String characteristic;
    private String type_packaging;          //5
    private String unit_measure;
    private int weight_volume;              //7
    private double quantity;
    private double price;                   //9
    private int quantity_package;
    private String image_url;               //11
    private String description;
    //private double total_sale_quantity;     //13
    //private double free_balance;

    public ProductInOrderModel(int product_id, int product_inventory_id, String category,
                               String brand, String characteristic, String type_packaging,
                               String unit_measure, int weight_volume, double quantity,
                               double price, int quantity_package, String image_url, String description) {
        this.product_id = product_id;
        this.product_inventory_id = product_inventory_id;
        this.category = category;
        this.brand = brand;
        this.characteristic = characteristic;
        this.type_packaging = type_packaging;
        this.unit_measure = unit_measure;
        this.weight_volume = weight_volume;
        this.quantity = quantity;
        this.price = price;
        this.quantity_package = quantity_package;
        this.image_url = image_url;
        this.description = description;
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

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
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
}
