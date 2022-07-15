package ru.tubi.project.models;

import java.io.Serializable;

import static ru.tubi.project.free.AllText.C;
import static ru.tubi.project.free.AllText.PHONE_SHORT;
import static ru.tubi.project.free.AllText.ST;

public class DeliveryAddressModel implements Serializable {
    String region;
    String district;
    String city;
    String street;
    int hause;
    String building;
    String additionalInformation;
    String phoneForContact;


    public DeliveryAddressModel(String region, String district, String city
            , String street, int hause, String building
            , String additionalInformation, String phoneForContact) {
        this.region = region;
        this.district = district;
        this.city = city;
        this.street = street;
        this.hause = hause;
        this.building = building;
        this.additionalInformation = additionalInformation;
        this.phoneForContact = phoneForContact;
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

    public int getHause() {
        return hause;
    }

    public void setHause(int hause) {
        this.hause = hause;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public String getPhoneForContact() {
        return phoneForContact;
    }

    public void setPhoneForContact(String phoneForContact) {
        this.phoneForContact = phoneForContact;
    }

    @Override
    public String toString() {
            String delivery_address_info = district+" "+ city + " \n"+ST+". "+ street +" " + hause;
            if(!building.isEmpty()){
                delivery_address_info += " "+C+". " + building;
            }
        delivery_address_info +=" \n"+ additionalInformation +" \n "+PHONE_SHORT+": "+phoneForContact;

        return delivery_address_info;
    }
}
