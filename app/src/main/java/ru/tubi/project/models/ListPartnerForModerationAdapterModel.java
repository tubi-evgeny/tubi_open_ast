package ru.tubi.project.models;

import java.io.Serializable;

public class ListPartnerForModerationAdapterModel implements Serializable {
    private int counterparty_id;
    private String abbreviation;
    private String counterparty;
    private long taxpayer_id;
    private String user;
    private int user_id;
    private int role_for_moderation_id;
    private int role_partner_id;
    private String createdDate;
    private String phone;
    private int count_step;

    public ListPartnerForModerationAdapterModel(int counterparty_id, String abbreviation,
                                                String counterparty, long taxpayer_id, int role_partner_id,
                                                String createdDate, String user, String phone,
                                                int count_step, int user_id) {
        this.counterparty_id = counterparty_id;
        this.abbreviation = abbreviation;
        this.counterparty = counterparty;
        this.taxpayer_id = taxpayer_id;
        this.role_partner_id = role_partner_id;
        this.createdDate = createdDate;
        this.user=user;
        this.phone=phone;
        this.count_step = count_step;
        this.user_id=user_id;
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getRole_for_moderation_id() {
        return role_for_moderation_id;
    }

    public void setRole_for_moderation_id(int role_for_moderation_id) {
        this.role_for_moderation_id = role_for_moderation_id;
    }

    public int getRole_partner_id() {
        return role_partner_id;
    }

    public void setRole_partner_id(int role_partner_id) {
        this.role_partner_id = role_partner_id;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getCount_step() {
        return count_step;
    }

    public void setCount_step(int count_step) {
        this.count_step = count_step;
    }
}
