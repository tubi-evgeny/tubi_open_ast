package ru.tubi.project.models;

public class CollectProductModel {

    private String buyer_name;
    private String buyer_phone;
    private String abbreviation;
    private String counterparty;
    private long taxpayer_id;
    private int warehouse_info_id;
    private int warehouse_id;
    private String city;
    private String street;
    private int house;
    private String building;
    private int order_id;

    public CollectProductModel(String buyer_name, String buyer_phone, String abbreviation,
                               String counterparty, long taxpayer_id, int warehouse_info_id,
                               int warehouse_id, String city, String street, int house,
                               String building, int order_id) {
        this.buyer_name = buyer_name;
        this.buyer_phone = buyer_phone;
        this.abbreviation = abbreviation;
        this.counterparty = counterparty;
        this.taxpayer_id = taxpayer_id;
        this.warehouse_info_id = warehouse_info_id;
        this.warehouse_id = warehouse_id;
        this.city = city;
        this.street = street;
        this.house = house;
        this.building = building;
        this.order_id = order_id;
    }


    public String getBuyer_name() {
        return buyer_name;
    }

    public void setBuyer_name(String buyer_name) {
        this.buyer_name = buyer_name;
    }

    public String getBuyer_phone() {
        return buyer_phone;
    }

    public void setBuyer_phone(String buyer_phone) {
        this.buyer_phone = buyer_phone;
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

    public long getTaxpayer_id() {
        return taxpayer_id;
    }

    public void setTaxpayer_id(long taxpayer_id) {
        this.taxpayer_id = taxpayer_id;
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

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }
}
