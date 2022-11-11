package ru.tubi.project.models;

import java.io.Serializable;

public class OrderHistoryFinishModel implements Serializable {

    private int order_id;
    private int positionCount;
    private String descriptionFirst;
    private String descriptionSecond;
    private String date;
    private String get_date;
    private int executed;
    private double summ;
    private int order_deleted;
    private int delivery;
    private int joint_buy;

    //OrderHistoryActivity / makeOrderHistoryList()
    public OrderHistoryFinishModel(int order_id, int positionCount, String descriptionFirst,
                                   String descriptionSecond, String date, String get_date,
                                   int executed, double summ, int order_deleted
            , int delivery, int joint_buy) {
        this.order_id = order_id;
        this.positionCount = positionCount;
        this.descriptionFirst = descriptionFirst;
        this.descriptionSecond = descriptionSecond;
        this.date = date;
        this.get_date=get_date;
        this.executed=executed;
        this.summ = summ;
        this.order_deleted=order_deleted;
        this.delivery=delivery;
        this.joint_buy=joint_buy;
    }
    public OrderHistoryFinishModel(int order_id, int positionCount, String descriptionFirst,
                                   String descriptionSecond, String date, String get_date,
                                   int executed, double summ, int order_deleted, int delivery) {
        this.order_id = order_id;
        this.positionCount = positionCount;
        this.descriptionFirst = descriptionFirst;
        this.descriptionSecond = descriptionSecond;
        this.date = date;
        this.get_date=get_date;
        this.executed=executed;
        this.summ = summ;
        this.order_deleted=order_deleted;
        this.delivery=delivery;
    }
  /*  public OrderHistoryFinishModel(int order_id, int positionCount, String descriptionFirst,
                                   String descriptionSecond, String date, String get_date,
                                   int executed, double summ, int order_deleted) {
        this.order_id = order_id;
        this.positionCount = positionCount;
        this.descriptionFirst = descriptionFirst;
        this.descriptionSecond = descriptionSecond;
        this.date = date;
        this.get_date=get_date;
        this.executed=executed;
        this.summ = summ;
        this.order_deleted=order_deleted;
    }*/

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getPositionCount() {
        return positionCount;
    }

    public void setPositionCount(int positionCount) {
        this.positionCount = positionCount;
    }

    public String getDescriptionFirst() {
        return descriptionFirst;
    }

    public void setDescriptionFirst(String descriptionFirst) {
        this.descriptionFirst = descriptionFirst;
    }

    public String getDescriptionSecond() {
        return descriptionSecond;
    }

    public void setDescriptionSecond(String descriptionSecond) {
        this.descriptionSecond = descriptionSecond;
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

    public int getExecuted() {
        return executed;
    }

    public void setExecuted(int executed) {
        this.executed = executed;
    }


    public double getSumm() {
        return summ;
    }

    public void setSumm(double summ) {
        this.summ = summ;
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

    public int getJoint_buy() {
        return joint_buy;
    }

    public void setJoint_buy(int joint_buy) {
        this.joint_buy = joint_buy;
    }

}

