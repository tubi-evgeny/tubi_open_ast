package ru.tubi.project.models;

public class ShipmentProductModel {

    private int counterparty_id;
    private String companyInfoString;
    private int in_warehouse_id;
    private String warehouseInfoString;
    private int logistic_product;
    private int car_id;
    private String car_info_string;
    private int out_active;

    //ShipmentProductActivity //splitBuyersCompanyResult()
    public ShipmentProductModel(int counterparty_id, String companyInfoString
            , int in_warehouse_id, String warehouseInfoString
            , int logistic_product, int car_id, String car_info_string
            ,int out_active) {
        this.counterparty_id = counterparty_id;
        this.companyInfoString = companyInfoString;
        this.in_warehouse_id = in_warehouse_id;
        this.warehouseInfoString = warehouseInfoString;
        this.logistic_product = logistic_product;
        this.car_id = car_id;
        this.car_info_string = car_info_string;
        this.out_active = out_active;
    }

    public ShipmentProductModel(int counterparty_id, String companyInfoString
            , int in_warehouse_id, String warehouseInfoString
            , int logistic_product, int car_id, String car_info_string) {
        this.counterparty_id = counterparty_id;
        this.companyInfoString = companyInfoString;
        this.in_warehouse_id = in_warehouse_id;
        this.warehouseInfoString = warehouseInfoString;
        this.logistic_product = logistic_product;
        this.car_id = car_id;
        this.car_info_string = car_info_string;
    }

    public int getCounterparty_id() {
        return counterparty_id;
    }

    public void setCounterparty_id(int counterparty_id) {
        this.counterparty_id = counterparty_id;
    }

    public String getCompanyInfoString() {
        return companyInfoString;
    }

    public void setCompanyInfoString(String companyInfoString) {
        this.companyInfoString = companyInfoString;
    }

    public int getIn_warehouse_id() {
        return in_warehouse_id;
    }

    public void setIn_warehouse_id(int in_warehouse_id) {
        this.in_warehouse_id = in_warehouse_id;
    }

    public String getWarehouseInfoString() {
        return warehouseInfoString;
    }

    public void setWarehouseInfoString(String warehouseInfoString) {
        this.warehouseInfoString = warehouseInfoString;
    }

    public int getLogistic_product() {
        return logistic_product;
    }

    public void setLogistic_product(int logistic_product) {
        this.logistic_product = logistic_product;
    }

    public int getCar_id() {
        return car_id;
    }

    public void setCar_id(int car_id) {
        this.car_id = car_id;
    }

    public String getCar_info_string() {
        return car_info_string;
    }

    public void setCar_info_string(String car_info_string) {
        this.car_info_string = car_info_string;
    }

    public int getOut_active() {
        return out_active;
    }

    public void setOut_active(int out_active) {
        this.out_active = out_active;
    }
}
