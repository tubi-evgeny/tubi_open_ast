package ru.tubi.project.models;

import java.io.Serializable;

import static ru.tubi.project.free.AllText.TAX_ID;

public class CounterpartyModel implements Serializable {
    private int counterparty_id;
    private String abbreviation;
    private String counterparty;
    private long taxpayer_id;
    private String processing_condition;
    private int completedProcessing;
    private int sum_weight_volume;
    private double sum_weight;
    private int order_id;
    private String buyer_phone;
    private int order_deleted;
    private int collect_product_for_delete;
    private int delivery;
    private String companyInfoString;

    // CollectProductForActivity//splitAllWriteOffOrders();
    public CounterpartyModel(int order_id, String abbreviation, String counterparty,
                             long taxpayer_id ) {
        this.order_id = order_id;
        this.abbreviation = abbreviation;
        this.counterparty = counterparty;
        this.taxpayer_id = taxpayer_id;
    }
    //ChoosePartnerActivity / splitResultCounterpartyArray();
    public CounterpartyModel(int order_id, String abbreviation
            , String counterparty, long taxpayer_id
            ,String companyInfoString) {
        this.order_id = order_id;
        this.abbreviation = abbreviation;
        this.counterparty = counterparty;
        this.taxpayer_id = taxpayer_id;
        this.companyInfoString=companyInfoString;
    }

    public CounterpartyModel(int order_id,long taxpayer_id, String abbreviation
            , String counterparty, int order_deleted
            , int collect_product_for_delete,int completedProcessing) {
        this.order_id = order_id;
        this.abbreviation = abbreviation;
        this.counterparty = counterparty;
        this.taxpayer_id = taxpayer_id;
        this.completedProcessing = completedProcessing;
        this.order_deleted = order_deleted;
        this.collect_product_for_delete = collect_product_for_delete;

    }
    // PartnerListBuyersForCollectActivity//splitBuyersCompanyResult();
    public CounterpartyModel(int order_id,long taxpayer_id, String abbreviation
            , String counterparty, int order_deleted
            , int collect_product_for_delete,int delivery, int completedProcessing) {
        this.order_id = order_id;
        this.abbreviation = abbreviation;
        this.counterparty = counterparty;
        this.taxpayer_id = taxpayer_id;
        this.completedProcessing = completedProcessing;
        this.order_deleted = order_deleted;
        this.collect_product_for_delete = collect_product_for_delete;
        this.delivery=delivery;

    }
    // ListBuyersIssueGoodsActivity//splitBuyersCompanyResult();
    public CounterpartyModel(int order_id,long taxpayer_id, String abbreviation, String counterparty,
                             int completedProcessing) {
        this.order_id = order_id;
        this.abbreviation = abbreviation;
        this.counterparty = counterparty;
        this.taxpayer_id = taxpayer_id;
        this.completedProcessing = completedProcessing;

    }
    public CounterpartyModel(int counterparty_id, String abbreviation, String counterparty,
                             int completedProcessing, int sum_weight_volume) {
        this.counterparty_id = counterparty_id;
        this.abbreviation = abbreviation;
        this.counterparty = counterparty;
        this.completedProcessing = completedProcessing;
        this.sum_weight_volume=sum_weight_volume;
    }

    public CounterpartyModel(int counterparty_id, String abbreviation, String counterparty,
                             String processing_condition, int sum_weight_volume) {
        this.counterparty_id = counterparty_id;
        this.abbreviation = abbreviation;
        this.counterparty = counterparty;
        this.processing_condition = processing_condition;
        this.sum_weight_volume=sum_weight_volume;
    }

    public CounterpartyModel(String abbreviation, String counterparty, long taxpayer_id) {
        this.abbreviation = abbreviation;
        this.counterparty = counterparty;
        this.taxpayer_id = taxpayer_id;
    }

    public int getCounterparty_id() {
        return counterparty_id;
    }

    public void setCounterparty_id(int counterparty_id) {
        this.counterparty_id = counterparty_id;
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

    @Override
    public String toString() {
        return  abbreviation + " " +counterparty + " " + TAX_ID +" "+  taxpayer_id ;
    }

    public String getProcessing_condition() {
        return processing_condition;
    }

    public void setProcessing_condition(String processing_condition) {
        this.processing_condition = processing_condition;
    }

    public int getCompletedProcessing() {
        return completedProcessing;
    }

    public void setCompletedProcessing(int completedProcessing) {
        this.completedProcessing = completedProcessing;
    }

    public int getSum_weight_volume() {
        return sum_weight_volume;
    }

    public void setSum_weight_volume(int sum_weight_volume) {
        this.sum_weight_volume = sum_weight_volume;
    }

    public double getSum_weight() {
        return sum_weight;
    }

    public void setSum_weight(double sum_weight) {
        this.sum_weight = sum_weight;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getBuyer_phone() {
        return buyer_phone;
    }

    public void setBuyer_phone(String buyer_phone) {
        this.buyer_phone = buyer_phone;
    }

    public int getOrder_deleted() {
        return order_deleted;
    }

    public void setOrder_deleted(int order_deleted) {
        this.order_deleted = order_deleted;
    }

    public int getCollect_product_for_delete() {
        return collect_product_for_delete;
    }

    public void setCollect_product_for_delete(int collect_product_for_delete) {
        this.collect_product_for_delete = collect_product_for_delete;
    }

    public int getDelivery() {
        return delivery;
    }

    public void setDelivery(int delivery) {
        this.delivery = delivery;
    }

    public String getCompanyInfoString() {
        return companyInfoString;
    }

    public void setCompanyInfoString(String companyInfoString) {
        this.companyInfoString = companyInfoString;
    }
}
