package ru.tubi.project.models;

public class MyProductInPartnerWarehouseModel {
    private int product_id;
    private int product_inventory_id;       //1
    private String category;
    private String product_name;
    private String brand;                   //3
    private String characteristic;
    private String type_packaging;          //5
    private String unit_measure;
    private int weight_volume;              //7
    private int quantity_package;
    private String image_url;               //11

    private int warehouse_info_id;
    private int warehouse_id;
    private String city;
    private String street;
    private int house;
    private String building;

    private double quantity;

    //MyProductInPartnerWarehouseActivity //splitResult();
    public MyProductInPartnerWarehouseModel(
            int product_id, int product_inventory_id, String category, String product_name,
            String brand,
            String characteristic, String type_packaging, String unit_measure,
            int weight_volume, int quantity_package, String image_url, int warehouse_info_id,
            int warehouse_id, String city, String street, int house, String building,
            double quantity) {

        this.product_id = product_id;
        this.product_inventory_id = product_inventory_id;
        this.category = category;
        this.product_name=product_name;
        this.brand = brand;
        this.characteristic = characteristic;
        this.type_packaging = type_packaging;
        this.unit_measure = unit_measure;
        this.weight_volume = weight_volume;
        this.quantity_package = quantity_package;
        this.image_url = image_url;
        this.warehouse_info_id = warehouse_info_id;
        this.warehouse_id = warehouse_id;
        this.city = city;
        this.street = street;
        this.house = house;
        this.building = building;
        this.quantity = quantity;
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

    public int getWarehouse_info_id() {
        return warehouse_info_id;
    }

    public void setWarehouse_info_id(int warehouse_info_id) {
        this.warehouse_info_id = warehouse_info_id;
    }

    public int getWarehouse_id() {
        return warehouse_id;
    }

    public void setWarehouse_id(int warehouse_id) {
        this.warehouse_id = warehouse_id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getHouse() {
        return house;
    }

    public void setHouse(int house) {
        this.house = house;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
}

