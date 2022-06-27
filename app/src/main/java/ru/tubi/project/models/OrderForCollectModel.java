package ru.tubi.project.models;

public class OrderForCollectModel {

    private int order_partner_id;
    private String abbreviation;
    private String counterparty;
    private long taxpayer_id ;
    private int order_active;
    private int collected;
    private long get_order_date_millis;

    public OrderForCollectModel(int order_partner_id, String abbreviation
            , String counterparty, long taxpayer_id, int order_active
            , int collected, long get_order_date_millis) {
        this.order_partner_id = order_partner_id;
        this.abbreviation = abbreviation;
        this.counterparty = counterparty;
        this.taxpayer_id = taxpayer_id;
        this.order_active = order_active;
        this.collected = collected;
        this.get_order_date_millis = get_order_date_millis;
    }

    public int getOrder_partner_id() {
        return order_partner_id;
    }

    public void setOrder_partner_id(int order_partner_id) {
        this.order_partner_id = order_partner_id;
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

    public int getOrder_active() {
        return order_active;
    }

    public void setOrder_active(int order_active) {
        this.order_active = order_active;
    }

    public int getCollected() {
        return collected;
    }

    public void setCollected(int collected) {
        this.collected = collected;
    }

    public long getGet_order_date_millis() {
        return get_order_date_millis;
    }

    public void setGet_order_date_millis(long get_order_date_millis) {
        this.get_order_date_millis = get_order_date_millis;
    }
}
