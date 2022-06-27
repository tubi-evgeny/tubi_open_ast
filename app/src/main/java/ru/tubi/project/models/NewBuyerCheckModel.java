package ru.tubi.project.models;

import java.io.Serializable;

public class NewBuyerCheckModel implements Serializable {
    private String name;
    private String abbreviation;
    private String counterparty;
    private String createdDate;
    private String orderSumm;
    private int order_id;
    private int phone;

    public NewBuyerCheckModel(String name, String abbreviation, String counterparty,
                    String createdDate,String orderSumm,int order_id,int phone) {
        this.name = name;
        this.abbreviation = abbreviation;
        this.counterparty = counterparty;
        this.createdDate = createdDate;
        this.orderSumm=orderSumm;
        this.order_id=order_id;
        this.phone=phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getOrderSumm() {
        return orderSumm;
    }

    public void setOrderSumm(String orderSumm) {
        this.orderSumm = orderSumm;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }
}
