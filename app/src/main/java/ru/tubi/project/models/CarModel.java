package ru.tubi.project.models;

public class CarModel {
    private int car_id;
    private String car_brand;
    private String car_model;
    private String registration_num;
    private int max_cargo_weght;

    public CarModel(int car_id, String car_brand, String car_model,
                    String registration_num) {
        this.car_id = car_id;
        this.car_brand = car_brand;
        this.car_model = car_model;
        this.registration_num = registration_num;
    }

    public CarModel(int car_id, String car_brand, String car_model,
                    String registration_num, int max_cargo_weght) {
        this.car_id = car_id;
        this.car_brand = car_brand;
        this.car_model = car_model;
        this.registration_num = registration_num;
        this.max_cargo_weght = max_cargo_weght;
    }

    public int getCar_id() {
        return car_id;
    }

    public void setCar_id(int car_id) {
        this.car_id = car_id;
    }

    public String getCar_brand() {
        return car_brand;
    }

    public void setCar_brand(String car_brand) {
        this.car_brand = car_brand;
    }

    public String getCar_model() {
        return car_model;
    }

    public void setCar_model(String car_model) {
        this.car_model = car_model;
    }

    public String getRegistration_num() {
        return registration_num;
    }

    public void setRegistration_num(String registration_num) {
        this.registration_num = registration_num;
    }

    public int getMax_cargo_weght() {
        return max_cargo_weght;
    }

    public void setMax_cargo_weght(int max_cargo_weght) {
        this.max_cargo_weght = max_cargo_weght;
    }
}
