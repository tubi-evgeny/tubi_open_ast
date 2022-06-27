package ru.tubi.project.models;

public class AddProduct {
    private int id;                //0
    private String catalog;
    private int catalog_flag;      //2
    private String category;
    private int category_flag;
    private String product_name;
    private int product_name_flag;
    private String brand;
    private int brand_flag;         //6
    private String characteristic;
    private int characteristic_flag;  //8
    private String type_packaging;
    private int type_packaging_flag;
    private String unit_measure;
    private int unit_measure_flag;
    private int weight_volume;
    private double price;
    private int quantity;
    private int quantity_package;
    private String image;
    private int image_flag;
    private String description;
    private String abbreviation;
    private String counterparty;
    private int counterparty_flag;
    private long taxpayer_id;
    private int description_flag;

    // ActivityAddProductsCheck //splitResultAddProduct();
    public AddProduct(int id, String catalog, int catalog_flag, String category,
                      int category_flag, String product_name, int product_name_flag,
                      String brand, int brand_flag, String characteristic,
                      int characteristic_flag,
                      String type_packaging, int type_packaging_flag, String unit_measure,
                      int unit_measure_flag, int weight_volume, double price, int quantity,
                      int quantity_package, String image, int image_flag, String description,
                      String abbreviation, String counterparty, int counterparty_flag, long taxpayer_id,
                      int description_flag) {
        this.id = id;
        this.catalog = catalog;
        this.catalog_flag = catalog_flag;
        this.category = category;
        this.category_flag = category_flag;
        this.product_name = product_name;
        this.product_name_flag = product_name_flag;
        this.brand = brand;
        this.brand_flag = brand_flag;
        this.characteristic = characteristic;
        this.characteristic_flag = characteristic_flag;
        this.type_packaging = type_packaging;
        this.type_packaging_flag = type_packaging_flag;
        this.unit_measure = unit_measure;
        this.unit_measure_flag = unit_measure_flag;
        this.weight_volume = weight_volume;
        this.price = price;
        this.quantity = quantity;
        this.quantity_package = quantity_package;
        this.image = image;
        this.image_flag = image_flag;
        this.description = description;
        this.abbreviation = abbreviation;
        this.counterparty = counterparty;
        this.counterparty_flag = counterparty_flag;
        this.taxpayer_id=taxpayer_id;
        this.description_flag=description_flag;
    }
/*
    // ActivityAddProductsCheck //splitResultAddProduct();
    public AddProduct(int id, String catalog, int catalog_flag, String category,
                      int category_flag, String product_name, int product_name_flag,
                      String brand, int brand_flag, String characteristic,
                      int characteristic_flag,
                      String type_packaging, int type_packaging_flag, String unit_measure,
                      int unit_measure_flag, int weight_volume, double price, int quantity,
                      int quantity_package, String image, int image_flag, String description,
                      String abbreviation, String counterparty, int counterparty_flag, int taxpayer_id,
                      int description_flag) {
        this.id = id;
        this.catalog = catalog;
        this.catalog_flag = catalog_flag;
        this.category = category;
        this.category_flag = category_flag;
        this.product_name = product_name;
        this.product_name_flag = product_name_flag;
        this.brand = brand;
        this.brand_flag = brand_flag;
        this.characteristic = characteristic;
        this.characteristic_flag = characteristic_flag;
        this.type_packaging = type_packaging;
        this.type_packaging_flag = type_packaging_flag;
        this.unit_measure = unit_measure;
        this.unit_measure_flag = unit_measure_flag;
        this.weight_volume = weight_volume;
        this.price = price;
        this.quantity = quantity;
        this.quantity_package = quantity_package;
        this.image = image;
        this.image_flag = image_flag;
        this.description = description;
        this.abbreviation = abbreviation;
        this.counterparty = counterparty;
        this.counterparty_flag = counterparty_flag;
        this.taxpayer_id=taxpayer_id;
        this.description_flag=description_flag;
    }

 */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public int getCatalog_flag() {
        return catalog_flag;
    }

    public void setCatalog_flag(int catalog_flag) {
        this.catalog_flag = catalog_flag;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCategory_flag() {
        return category_flag;
    }

    public void setCategory_flag(int category_flag) {
        this.category_flag = category_flag;
    }


    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public int getProduct_name_flag() {
        return product_name_flag;
    }

    public void setProduct_name_flag(int product_name_flag) {
        this.product_name_flag = product_name_flag;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getBrand_flag() {
        return brand_flag;
    }

    public void setBrand_flag(int brand_flag) {
        this.brand_flag = brand_flag;
    }

    public String getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(String characteristic) {
        this.characteristic = characteristic;
    }

    public int getCharacteristic_flag() {
        return characteristic_flag;
    }

    public void setCharacteristic_flag(int characteristic_flag) {
        this.characteristic_flag = characteristic_flag;
    }

    public String getType_packaging() {
        return type_packaging;
    }

    public void setType_packaging(String type_packaging) {
        this.type_packaging = type_packaging;
    }

    public int getType_packaging_flag() {
        return type_packaging_flag;
    }

    public void setType_packaging_flag(int type_packaging_flag) {
        this.type_packaging_flag = type_packaging_flag;
    }

    public String getUnit_measure() {
        return unit_measure;
    }

    public void setUnit_measure(String unit_measure) {
        this.unit_measure = unit_measure;
    }

    public int getUnit_measure_flag() {
        return unit_measure_flag;
    }

    public void setUnit_measure_flag(int unit_measure_flag) {
        this.unit_measure_flag = unit_measure_flag;
    }

    public int getWeight_volume() {
        return weight_volume;
    }

    public void setWeight_volume(int weight_volume) {
        this.weight_volume = weight_volume;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity_package() {
        return quantity_package;
    }

    public void setQuantity_package(int quantity_package) {
        this.quantity_package = quantity_package;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getImage_flag() {
        return image_flag;
    }

    public void setImage_flag(int image_flag) {
        this.image_flag = image_flag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public String getCounterparty() {
        return counterparty;
    }

    public void setCounterparty(String counterparty) {
        this.counterparty = counterparty;
    }

    public int getCounterparty_flag() {
        return counterparty_flag;
    }

    public void setCounterparty_flag(int counterparty_flag) {
        this.counterparty_flag = counterparty_flag;
    }

    public long getTaxpayer_id() {
        return taxpayer_id;
    }

    public void setTaxpayer_id(long taxpayer_id) {
        this.taxpayer_id = taxpayer_id;
    }

    public int getDescription_flag() {
        return description_flag;
    }

    public void setDescription_flag(int description_flag) {
        this.description_flag = description_flag;
    }
}
