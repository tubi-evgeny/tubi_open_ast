package ru.tubi.project.models;

import java.io.Serializable;

public class WarehouseModel implements Serializable {
    private int warehouse_id;
    private String region;
    private String district;
    private String city;
    private String street;
    private int house;
    private String building;
    private String signboard;
    private String active;
    private String created_at;
    private String created_user_name;
    private String warehouse_type;
    private String warehouse_buyer_type;
    private String warehouse_provider_type;
    private String warehouse_partner_type;
    private boolean providerRole;
    private boolean partnerRole;
    private boolean providerWarehouse;
    private boolean partnerWarehouse;
    private int warehouse_info_id;
    private int warStorageNum;
    private int warProviderNum;
    private int warPartnerNum;

    public WarehouseModel(int warehouse_info_id, int warehouse_id, String warehouse_type) {
        this.warehouse_type = warehouse_type;
        this.warehouse_id = warehouse_id;
        this.warehouse_info_id = warehouse_info_id;
    }

//CatalogInWarehouseActivity // splitResult();
    public WarehouseModel(int warehouse_info_id, String city, String street, int house,
                          String building, int warehouse_id, String warehouse_type) {
        this.warehouse_info_id = warehouse_info_id;
        this.city = city;
        this.street = street;
        this.house = house;
        this.building = building;
        this.warehouse_id = warehouse_id;
        this.warehouse_type = warehouse_type;
    }
// TransferProductActivity // splitResultWarehouse();
    //CollectProductForActivity //splitPartnerWarehouseResult();
    //ShowMyOrderActivity//splitWarehouseResult//
    public WarehouseModel(int warehouse_id, String city, String street,
                          int house, String building,int warehouse_info_id) {
        this.city = city;
        this.street = street;
        this.house = house;
        this.building = building;
        this.warehouse_id = warehouse_id;
        this.warehouse_info_id = warehouse_info_id;
    }


    public WarehouseModel(int warehouse_id,  String city) {
        this.warehouse_id = warehouse_id;
        this.city = city;
    }

    public WarehouseModel(int warehouse_id, String region, String district, String city,
                          String street, int house, String building, String signboard, String active,
                          String created_at, boolean providerRole, boolean partnerRole) {
        this.warehouse_id = warehouse_id;
        this.region = region;
        this.district = district;
        this.city = city;
        this.street = street;
        this.house = house;
        this.building = building;
        this.signboard = signboard;
        this.active = active;
        this.created_at = created_at;
        this.providerRole=providerRole;
        this.partnerRole=partnerRole;
    }

    //ProfileCompanyActivity // splitAllWarehouseResult();
    public WarehouseModel(int warehouse_info_id, String region, String district, String city,
                          String street, int house, String building, String signboard, String active,
                          String created_at, boolean providerWarehouse, boolean partnerWarehouse ,
                          int warStorageNum,int warProviderNum,int warPartnerNum,
                          boolean providerRole, boolean partnerRole) {
        this.warehouse_info_id = warehouse_info_id;
        this.region = region;
        this.district = district;
        this.city = city;
        this.street = street;
        this.house = house;
        this.building = building;
        this.signboard = signboard;
        this.active = active;
        this.created_at = created_at;
        this.providerRole=providerRole;
        this.partnerRole=partnerRole;
        this.providerWarehouse=providerWarehouse;
        this.partnerWarehouse=partnerWarehouse;
        this.warStorageNum = warStorageNum;
        this.warProviderNum = warProviderNum;
        this.warPartnerNum = warPartnerNum;
    }
    public WarehouseModel(int warehouse_id, String region, String district, String city,
                          String street, int house, String building, String signboard, String active,
                          String created_at, boolean providerWarehouse, boolean partnerWarehouse ,
                          boolean providerRole, boolean partnerRole) {
        this.warehouse_id = warehouse_id;
        this.region = region;
        this.district = district;
        this.city = city;
        this.street = street;
        this.house = house;
        this.building = building;
        this.signboard = signboard;
        this.active = active;
        this.created_at = created_at;
        this.providerRole=providerRole;
        this.partnerRole=partnerRole;
        this.providerWarehouse=providerWarehouse;
        this.partnerWarehouse=partnerWarehouse;
    }

    public WarehouseModel(int warehouse_id, String region, String district, String city,
                          String street, int house, String building, String signboard, String active,
                          String created_at) {
        this.warehouse_id = warehouse_id;
        this.region = region;
        this.district = district;
        this.city = city;
        this.street = street;
        this.house = house;
        this.building = building;
        this.signboard = signboard;
        this.active = active;
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return  city + " " + street + " " + house  ;
    }

    public int getWarehouse_id() {
        return warehouse_id;
    }

    public void setWarehouse_id(int warehouse_id) {
        this.warehouse_id = warehouse_id;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
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

    public String getSignboard() {
        return signboard;
    }

    public void setSignboard(String signboard) {
        this.signboard = signboard;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getCreated_user_name() {
        return created_user_name;
    }

    public void setCreated_user_name(String created_user_name) {
        this.created_user_name = created_user_name;
    }

    public String getWarehouse_buyer_type() {
        return warehouse_buyer_type;
    }

    public void setWarehouse_buyer_type(String warehouse_buyer_type) {
        this.warehouse_buyer_type = warehouse_buyer_type;
    }

    public String getWarehouse_provider_type() {
        return warehouse_provider_type;
    }

    public void setWarehouse_provider_type(String warehouse_provider_type) {
        this.warehouse_provider_type = warehouse_provider_type;
    }

    public String getWarehouse_partner_type() {
        return warehouse_partner_type;
    }

    public void setWarehouse_partner_type(String warehouse_partner_type) {
        this.warehouse_partner_type = warehouse_partner_type;
    }

    public boolean isProviderRole() {
        return providerRole;
    }

    public void setProviderRole(boolean providerRole) {
        this.providerRole = providerRole;
    }

    public boolean isPartnerRole() {
        return partnerRole;
    }

    public void setPartnerRole(boolean partnerRole) {
        this.partnerRole = partnerRole;
    }

    public boolean isProviderWarehouse() {
        return providerWarehouse;
    }

    public void setProviderWarehouse(boolean providerWarehouse) {
        this.providerWarehouse = providerWarehouse;
    }

    public boolean isPartnerWarehouse() {
        return partnerWarehouse;
    }

    public void setPartnerWarehouse(boolean partnerWarehouse) {
        this.partnerWarehouse = partnerWarehouse;
    }

    public int getWarehouse_info_id() {
        return warehouse_info_id;
    }

    public void setWarehouse_info_id(int warehouse_info_id) {
        this.warehouse_info_id = warehouse_info_id;
    }

    public int getWarStorageNum() {
        return warStorageNum;
    }

    public void setWarStorageNum(int warStorageNum) {
        this.warStorageNum = warStorageNum;
    }

    public int getWarProviderNum() {
        return warProviderNum;
    }

    public void setWarProviderNum(int warProviderNum) {
        this.warProviderNum = warProviderNum;
    }

    public int getWarPartnerNum() {
        return warPartnerNum;
    }

    public void setWarPartnerNum(int warPartnerNum) {
        this.warPartnerNum = warPartnerNum;
    }

    public String getWarehouse_type() {
        return warehouse_type;
    }

    public void setWarehouse_type(String warehouse_type) {
        this.warehouse_type = warehouse_type;
    }


}
