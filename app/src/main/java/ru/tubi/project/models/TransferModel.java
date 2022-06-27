package ru.tubi.project.models;

import java.io.Serializable;

public class TransferModel implements Serializable {
    private int in_warehouse_id;
    private int productInventory_id;
    private int warehouseInventory_id;
    private double quantity;
    private int logistic_product_check;
    private int $car_for_logistic_check;
    private int out_active_check;
    private int car_id;
    private String car_info;
    private int invoice_key_id;
    private String description_docs;
    private String product_name_from_provider;
    private double price;
    private String image_url;
    private int out_active;


    //GiveAwayProductActivity //splitProductListResult()
    public TransferModel( int warehouseInventory_id, int productInventory_id
            ,double quantity, int invoice_key_id, String image_url
            , String description_docs, String product_name_from_provider
            , int out_active) {

        this.productInventory_id = productInventory_id;
        this.warehouseInventory_id = warehouseInventory_id;
        this.quantity = quantity;
        this.image_url = image_url;
        this.invoice_key_id=invoice_key_id;
        this.description_docs=description_docs;
        this.product_name_from_provider=product_name_from_provider;
        this.out_active=out_active;
    }
    public TransferModel( int warehouseInventory_id, int productInventory_id
            ,double quantity, int invoice_key_id, String image_url
            , String description_docs, int out_active) {

        this.productInventory_id = productInventory_id;
        this.warehouseInventory_id = warehouseInventory_id;
        this.quantity = quantity;
        this.image_url = image_url;
        this.invoice_key_id=invoice_key_id;
        this.description_docs=description_docs;
        this.out_active=out_active;
    }
    //GiveAwayProductActivity //splitProductListResult()
    public TransferModel( int warehouseInventory_id, int productInventory_id
            ,double quantity, int invoice_key_id, String image_url, String description_docs) {

        this.productInventory_id = productInventory_id;
        this.warehouseInventory_id = warehouseInventory_id;
        this.quantity = quantity;
        this.image_url = image_url;
        this.invoice_key_id=invoice_key_id;
        this.description_docs=description_docs;
    }
    //TransferProductActivity //splitResult()
    public TransferModel( int productInventory_id, int warehouseInventory_id
            ,double quantity, int logistic_product_check, int $car_for_logistic_check
            ,int out_active_check, int in_warehouse_id,int car_id, int invoice_key_id
            ,String description_docs, String car_info, double price) {

        this.in_warehouse_id = in_warehouse_id;
        this.productInventory_id = productInventory_id;
        this.warehouseInventory_id = warehouseInventory_id;
        this.quantity = quantity;
        this.logistic_product_check = logistic_product_check;
        this.$car_for_logistic_check = $car_for_logistic_check;
        this.out_active_check = out_active_check;
        this.car_id=car_id;
        this.invoice_key_id=invoice_key_id;
        this.description_docs=description_docs;
        this.car_info=car_info;
        this.price=price;
    }

    public int getIn_warehouse_id() {
        return in_warehouse_id;
    }

    public void setIn_warehouse_id(int in_warehouse_id) {
        this.in_warehouse_id = in_warehouse_id;
    }

    public int getProductInventory_id() {
        return productInventory_id;
    }

    public void setProductInventory_id(int productInventory_id) {
        this.productInventory_id = productInventory_id;
    }

    public int getWarehouseInventory_id() {
        return warehouseInventory_id;
    }

    public void setWarehouseInventory_id(int warehouseInventory_id) {
        this.warehouseInventory_id = warehouseInventory_id;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public int getLogistic_product_check() {
        return logistic_product_check;
    }

    public void setLogistic_product_check(int logistic_product_check) {
        this.logistic_product_check = logistic_product_check;
    }

    public int get$car_for_logistic_check() {
        return $car_for_logistic_check;
    }

    public void set$car_for_logistic_check(int $car_for_logistic_check) {
        this.$car_for_logistic_check = $car_for_logistic_check;
    }

    public int getOut_active_check() {
        return out_active_check;
    }

    public void setOut_active_check(int out_active_check) {
        this.out_active_check = out_active_check;
    }

    public int getCar_id() {
        return car_id;
    }

    public void setCar_id(int car_id) {
        this.car_id = car_id;
    }

    public int getInvoice_key_id() {
        return invoice_key_id;
    }

    public void setInvoice_key_id(int invoice_key_id) {
        this.invoice_key_id = invoice_key_id;
    }

    public String getDescription_docs() {
        return description_docs;
    }

    public void setDescription_docs(String description_docs) {
        this.description_docs = description_docs;
    }

    public String getProduct_name_from_provider() {
        return product_name_from_provider;
    }

    public void setProduct_name_from_provider(String product_name_from_provider) {
        this.product_name_from_provider = product_name_from_provider;
    }

    public String getCar_info() {
        return car_info;
    }

    public void setCar_info(String car_info) {
        this.car_info = car_info;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public int getOut_active() {
        return out_active;
    }

    public void setOut_active(int out_active) {
        this.out_active = out_active;
    }
}

