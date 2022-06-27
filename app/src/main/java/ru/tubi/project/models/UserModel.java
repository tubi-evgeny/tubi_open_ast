package ru.tubi.project.models;

public class UserModel {
    private String uid;
    private String name;
    private String phone;
    private String abbreviation;
    private String counterparty;
    private String taxpayer_id;
    private long company_tax_id;
    private String create_at;
    private String updated_at;
    private String role;
    private String partner_role_list;
    private int order_id;

    //UserDataRecovery //goReadUid();
    public UserModel(String uid, String name, String abbreviation, String counterparty,
                     long company_tax_id, String role, int order_id, String partner_role_list,
                     String phone) {
        this.uid = uid;
        this.name = name;
        this.abbreviation = abbreviation;
        this.counterparty = counterparty;
        this.company_tax_id = company_tax_id;
        this.role = role;
        this.order_id = order_id;
        this.partner_role_list=partner_role_list;
        this.phone=phone;
    }
    /*
     //UserDataRecovery //goReadUid();
    public UserModel(String uid, String name, String abbreviation, String counterparty,
                     long company_tax_id, String role, int order_id, String partner_role_list) {
        this.uid = uid;
        this.name = name;
        this.abbreviation = abbreviation;
        this.counterparty = counterparty;
        this.company_tax_id = company_tax_id;
        this.role = role;
        this.order_id = order_id;
        this.partner_role_list=partner_role_list;
    }
     */
    //UserDataRecovery //goReadUid();
  /*  public UserModel(String uid, String name, String abbreviation, String counterparty,
                     int company_tax_id, String role, int order_id, String partner_role_list) {
        this.uid = uid;
        this.name = name;
        this.abbreviation = abbreviation;
        this.counterparty = counterparty;
        this.company_tax_id = company_tax_id;
        this.role = role;
        this.order_id = order_id;
        this.partner_role_list=partner_role_list;
    }*/

    public UserModel(String uid, String name, String phone, String abbreviation,
                     String counterparty, String taxpayer_id, String role, String create_at,
                     String updated_at) {

        this.name = name;
        this.phone = phone;
        this.uid = uid;
        this.abbreviation = abbreviation;
        this.counterparty = counterparty;
        this.taxpayer_id = taxpayer_id;
        this.create_at = create_at;
        this.updated_at = updated_at;
        this.role = role;
    }
        //конструктор регистрации role не нужен
    public UserModel(String uid, String name, String phone, String create_at,
                     String updated_at) {
        this.uid = uid;
        this.name = name;
        this.phone = phone;
        this.create_at = create_at;
        this.updated_at = updated_at;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getTaxpayer_id() {
        return taxpayer_id;
    }

    public void setTaxpayer_id(String taxpayer_id) {
        this.taxpayer_id = taxpayer_id;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public long getCompany_tax_id() {
        return company_tax_id;
    }

    public void setCompany_tax_id(long company_tax_id) {
        this.company_tax_id = company_tax_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getPartner_role_list() {
        return partner_role_list;
    }

    public void setPartner_role_list(String partner_role_list) {
        this.partner_role_list = partner_role_list;
    }
}
