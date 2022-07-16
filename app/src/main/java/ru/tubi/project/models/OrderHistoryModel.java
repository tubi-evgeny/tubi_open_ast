package ru.tubi.project.models;

import java.io.Serializable;


public class OrderHistoryModel implements Serializable {

    private int order_id;
    private String category;
    private String brand;
    private String characteristic;
    private int weight_volume;
    private double price;
    private double price_process;
    private double quantity;
    private int executed;
    private String image_url;
    private String date;
    private String get_date;
    private int order_deleted;
    private int delivery;

    //OrderHistoryActivity //splitOrderHistoryResult();
    public OrderHistoryModel(int order_id, String category, String brand,
                             String characteristic, int weight_volume,
                             double price, double quantity, int executed,
                             String date, String get_date,int order_deleted
            ,double price_process, int delivery) {

        this.category = category;
        this.brand = brand;
        this.characteristic = characteristic;
        this.weight_volume = weight_volume;
        this.price = price;
        this.quantity=quantity;
        this.executed=executed;
        this.order_id=order_id;
        this.date=date;
        this.get_date=get_date;
        this.order_deleted=order_deleted;
        this.price_process = price_process;
        this.delivery=delivery;
    }

    public int getWeight_volume() {
        return weight_volume;
    }

    public void setWeight_volume(int weight_volume) {
        this.weight_volume = weight_volume;
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

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public int getExecuted() {
        return executed;
    }

    public void setExecuted(int executed) {
        this.executed = executed;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGet_date() {
        return get_date;
    }

    public void setGet_date(String get_date) {
        this.get_date = get_date;
    }

    public int getOrder_deleted() {
        return order_deleted;
    }

    public void setOrder_deleted(int order_deleted) {
        this.order_deleted = order_deleted;
    }

    public int getDelivery() {
        return delivery;
    }

    public void setDelivery(int delivery) {
        this.delivery = delivery;
    }
}

