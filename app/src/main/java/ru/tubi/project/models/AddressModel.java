package ru.tubi.project.models;

public class AddressModel {
    private String region;
    private String district;
    private String city;
    private String street;
    private int house;
    private String building;
    private String signboard;

    public AddressModel(String region, String district, String city) {
        this.region = region;
        this.district = district;
        this.city = city;
        this.street = street;
        this.house = house;
        this.building = building;
        this.signboard = signboard;
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

    @Override
    public String toString() {
        return city;
    }
}
