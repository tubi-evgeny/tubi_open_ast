package ru.tubi.project.models;

import java.io.Serializable;

public class CarrierPanelModel implements Serializable {

    private int warehouse_info_id;
    private int warehouse_id;
    private String city;
    private String street;
    private int house;
    private String building;
    private String warehouse_tipe;//int warehouse_info_id;

    private int outWarehouse_info_id;
    private int outWarehouse_id;
    private String outDistrict;
    private String outCity;
    private String outStreet;
    private int outHouse;
    private String outBuilding;

    private int inWarehouse_info_id;
    private int inWarehouse_id;
    private String inDistrict;
    private String inCity;
    private String inStreet;
    private int inHouse;
    private String inBuilding;

    private double quantity;
    private double productWeight;
    private String storageTemperature;
    private int productInventory_id;
    private int warehouseInventory_id;
    private int checked;
    private int check_give_out;
    private int check_take_in;
    private int check_out_active;

    private String category;
    private String product_name;
    private String brand;
    private String characteristic;
    private String unit_measure;
    private String typePackaging;
    private int quantityPackage;
    private int weight_volume;
    private String image_url;
    private String description;

    private int logistic_product;
    private int car_or_warehouse_id;
    private int car_id;
    private int colorDelivery;
    private int document_num;
    private String document_name;
    private int document_closed;
    private int document_save;
    private int invoice_key_id;


    //AcceptProductActivity-splitResult();
    public CarrierPanelModel( int warehouseInventory_id, int productInventory_id
            , int logistic_product, int checked, String category, String brand
            , String characteristic, String unit_measure, int weight_volume
            , String image_url,double quantity, String typePackaging
            , int quantityPackage, int car_id,int outWarehouse_id
            , int colorDelivery, int document_num, String document_name
            , String product_name) {

        this.quantity = quantity;
        this.productInventory_id = productInventory_id;
        this.warehouseInventory_id = warehouseInventory_id;
        this.checked = checked;
        this.category = category;
        this.product_name=product_name;
        this.brand = brand;
        this.characteristic = characteristic;
        this.unit_measure = unit_measure;
        this.typePackaging = typePackaging;
        this.quantityPackage = quantityPackage;
        this.weight_volume = weight_volume;
        this.image_url = image_url;
        this.logistic_product = logistic_product;
        this.car_id=car_id;
        this.outWarehouse_id = outWarehouse_id;
        this.colorDelivery=colorDelivery;
        this.document_num=document_num;
        this.document_name=document_name;
    }
    //(AcceptProductActivity-splitResult();)
    public CarrierPanelModel( int warehouseInventory_id,
                              int productInventory_id, int logistic_product,
                              int checked, String category,
                              String brand, String characteristic, String unit_measure,
                              int weight_volume, String image_url,double quantity,
                              String typePackaging,  int quantityPackage,
                              int car_id,int outWarehouse_id, int colorDelivery) {
        this.quantity = quantity;
        this.productInventory_id = productInventory_id;
        this.warehouseInventory_id = warehouseInventory_id;
        this.checked = checked;
        this.category = category;
        this.brand = brand;
        this.characteristic = characteristic;
        this.unit_measure = unit_measure;
        this.typePackaging = typePackaging;
        this.quantityPackage = quantityPackage;
        this.weight_volume = weight_volume;
        this.image_url = image_url;
        this.logistic_product = logistic_product;
        this.car_id=car_id;
        this.outWarehouse_id = outWarehouse_id;
        this.colorDelivery=colorDelivery;
    }

    public CarrierPanelModel(int warehouse_id, String city, String street, int house,
                             String building, int warehouseInventory_id,
                             String category, String brand, String characteristic,
                             String unit_measure, int weight_volume, String typePackaging,
                             int quantityPackage, String image_url,
                             double quantity, int checked) {
        this.warehouse_id = warehouse_id;
        this.city = city;
        this.street = street;
        this.house = house;
        this.building = building;
        this.quantity = quantity;
        this.warehouseInventory_id = warehouseInventory_id;
        this.checked = checked;
        this.category = category;
        this.brand = brand;
        this.characteristic = characteristic;
        this.unit_measure = unit_measure;
        this.weight_volume = weight_volume;
        this.typePackaging=typePackaging;
        this.quantityPackage=quantityPackage;
        this.image_url = image_url;
    }
    //DeliveryToWarehouseActivity//makeAcceptOrHandOverList();
    public CarrierPanelModel(int warehouse_id, String city, String street, int house,
                             String building, int warehouseInventory_id,
                             String category, String brand, String characteristic,
                             String unit_measure, int weight_volume, String typePackaging,
                             int quantityPackage, String image_url,
                             double quantity, int checked, int check_out_active,
                             int warehouse_info_id) {
        //int check_give_out;
        // int check_take_in;
        this.warehouse_id = warehouse_id;
        this.city = city;
        this.street = street;
        this.house = house;
        this.building = building;
        this.quantity = quantity;
        this.warehouseInventory_id = warehouseInventory_id;
        this.checked = checked;
        this.category = category;
        this.brand = brand;
        this.characteristic = characteristic;
        this.unit_measure = unit_measure;
        this.weight_volume = weight_volume;
        this.typePackaging=typePackaging;
        this.quantityPackage=quantityPackage;
        this.image_url = image_url;
        this.check_out_active=check_out_active;
        this.warehouse_info_id=warehouse_info_id;
    }
    //HandOverProductActivity//splitResult();
    public CarrierPanelModel(int warehouse_id, String city, String street, int house,
                             String building, int warehouseInventory_id,
                             String category, String brand, String characteristic,
                             String unit_measure, int weight_volume, String typePackaging,
                             int quantityPackage, String image_url,
                             double quantity, int checked,
                             int warehouse_info_id, int document_num
            , int document_closed, int invoice_key_id) {

        this.warehouse_id = warehouse_id;
        this.city = city;
        this.street = street;
        this.house = house;
        this.building = building;
        this.quantity = quantity;
        this.warehouseInventory_id = warehouseInventory_id;
        this.checked = checked;
        this.category = category;
        this.brand = brand;
        this.characteristic = characteristic;
        this.unit_measure = unit_measure;
        this.weight_volume = weight_volume;
        this.typePackaging=typePackaging;
        this.quantityPackage=quantityPackage;
        this.image_url = image_url;
        this.warehouse_info_id=warehouse_info_id;
        this.document_num=document_num;
        this.document_closed=document_closed;
        this.invoice_key_id =invoice_key_id;
    }
    //DeliveryToWarehouseActivity//makeAcceptOrHandOverList();
    public CarrierPanelModel(int warehouse_id, String city, String street, int house,
                             String building, int warehouseInventory_id,
                             String category, String brand, String characteristic,
                             String unit_measure, int weight_volume, String typePackaging,
                             int quantityPackage, String image_url,
                             double quantity, int checked, int check_out_active,
                             int warehouse_info_id, int document_num
            , int document_closed, int document_save,int invoice_key_id) {

        this.warehouse_id = warehouse_id;
        this.city = city;
        this.street = street;
        this.house = house;
        this.building = building;
        this.quantity = quantity;
        this.warehouseInventory_id = warehouseInventory_id;
        this.checked = checked;
        this.category = category;
        this.brand = brand;
        this.characteristic = characteristic;
        this.unit_measure = unit_measure;
        this.weight_volume = weight_volume;
        this.typePackaging=typePackaging;
        this.quantityPackage=quantityPackage;
        this.image_url = image_url;
        this.check_out_active=check_out_active;
        this.warehouse_info_id=warehouse_info_id;
        this.document_num=document_num;
        this.document_closed=document_closed;
        this.document_save =document_save;
        this.invoice_key_id =invoice_key_id;
    }
    //DeliveryToReceiveGoodsActivity//makeAcceptOrHandOverList();
    public CarrierPanelModel(int warehouse_id, String city, String street, int house,
                             String building, int warehouseInventory_id,
                             String category, String brand, String characteristic,
                             String unit_measure, int weight_volume, String typePackaging,
                             int quantityPackage, String image_url,
                             double quantity, int checked, int check_out_active,
                             int warehouse_info_id, int document_num
            ,int document_closed, int document_save,int invoice_key_id
            ,String product_name, String description) {

        this.warehouse_id = warehouse_id;
        this.city = city;
        this.street = street;
        this.house = house;
        this.building = building;
        this.quantity = quantity;
        this.warehouseInventory_id = warehouseInventory_id;
        this.checked = checked;
        this.category = category;
        this.brand = brand;
        this.characteristic = characteristic;
        this.unit_measure = unit_measure;
        this.weight_volume = weight_volume;
        this.typePackaging=typePackaging;
        this.quantityPackage=quantityPackage;
        this.image_url = image_url;
        this.check_out_active=check_out_active;
        this.warehouse_info_id=warehouse_info_id;
        this.document_num=document_num;
        this.document_closed=document_closed;
        this.document_save =document_save;
        this.invoice_key_id =invoice_key_id;
        this.description = description;
        this.product_name = product_name;
    }
    //DeliveryToReceiveGoodsActivity//splitResult();
    public CarrierPanelModel(int outWarehouse_id,  String outCity,
                             String outStreet, int outHouse, String outBuilding,
                             int inWarehouse_id,  String inCity,  String inStreet,
                             int inHouse, String inBuilding,  int warehouseInventory_id,
                             String category,  String brand, String characteristic,
                             String unit_measure,  int weight_volume, String image_url,
                             double quantity,String typePackaging, int quantityPackage,
                             int check_take_in, int check_give_out, int check_out_active,
                             int outWarehouse_info_id,int inWarehouse_info_id
            ,int document_num, int document_closed, int document_save
            ,int invoice_key_id, String product_name, String description) {

        this.outWarehouse_id = outWarehouse_id;
        this.outCity = outCity;
        this.outStreet = outStreet;
        this.outHouse = outHouse;
        this.outBuilding = outBuilding;
        this.inWarehouse_id = inWarehouse_id;
        this.inCity = inCity;
        this.inStreet = inStreet;
        this.inHouse = inHouse;
        this.inBuilding = inBuilding;
        this.quantity = quantity;
        this.warehouseInventory_id = warehouseInventory_id;
        this.category = category;
        this.brand = brand;
        this.characteristic = characteristic;
        this.unit_measure = unit_measure;
        this.weight_volume = weight_volume;
        this.image_url = image_url;
        this.typePackaging=typePackaging;
        this.quantityPackage=quantityPackage;
        this.check_take_in = check_take_in;
        this.check_give_out = check_give_out;
        this.check_out_active=check_out_active;
        this.inWarehouse_info_id=inWarehouse_info_id;
        this.outWarehouse_info_id=outWarehouse_info_id;
        this.document_num=document_num;
        this.document_closed=document_closed;
        this.document_save =document_save;
        this.invoice_key_id =invoice_key_id;
        this.description = description;
        this.product_name = product_name;
    }
    //DeliveryToWarehouseActivity//splitResult();
    public CarrierPanelModel(int outWarehouse_id,  String outCity,
                             String outStreet, int outHouse, String outBuilding,
                             int inWarehouse_id,  String inCity,  String inStreet,
                             int inHouse, String inBuilding,  int warehouseInventory_id,
                             String category,  String brand, String characteristic,
                             String unit_measure,  int weight_volume, String image_url,
                             double quantity,String typePackaging, int quantityPackage,
                             int check_take_in, int check_give_out, int check_out_active,
                             int outWarehouse_info_id,int inWarehouse_info_id
            ,int document_num, int document_closed, int document_save
            ,int invoice_key_id) {

        this.outWarehouse_id = outWarehouse_id;
        this.outCity = outCity;
        this.outStreet = outStreet;
        this.outHouse = outHouse;
        this.outBuilding = outBuilding;
        this.inWarehouse_id = inWarehouse_id;
        this.inCity = inCity;
        this.inStreet = inStreet;
        this.inHouse = inHouse;
        this.inBuilding = inBuilding;
        this.quantity = quantity;
        this.warehouseInventory_id = warehouseInventory_id;
        this.category = category;
        this.brand = brand;
        this.characteristic = characteristic;
        this.unit_measure = unit_measure;
        this.weight_volume = weight_volume;
        this.image_url = image_url;
        this.typePackaging=typePackaging;
        this.quantityPackage=quantityPackage;
        this.check_take_in = check_take_in;
        this.check_give_out = check_give_out;
        this.check_out_active=check_out_active;
        this.inWarehouse_info_id=inWarehouse_info_id;
        this.outWarehouse_info_id=outWarehouse_info_id;
        this.document_num=document_num;
        this.document_closed=document_closed;
        this.document_save =document_save;
        this.invoice_key_id =invoice_key_id;
    }
    //DeliveryToWarehouseActivity//splitResult();
    public CarrierPanelModel(int outWarehouse_id,  String outCity,
                             String outStreet, int outHouse, String outBuilding,
                             int inWarehouse_id,  String inCity,  String inStreet,
                             int inHouse, String inBuilding,  int warehouseInventory_id,
                             String category,  String brand, String characteristic,
                             String unit_measure,  int weight_volume, String image_url,
                             double quantity,String typePackaging, int quantityPackage,
                             int check_take_in, int check_give_out, int check_out_active,
                             int outWarehouse_info_id,int inWarehouse_info_id
            ,int document_num, int document_closed) {

        this.outWarehouse_id = outWarehouse_id;
        this.outCity = outCity;
        this.outStreet = outStreet;
        this.outHouse = outHouse;
        this.outBuilding = outBuilding;
        this.inWarehouse_id = inWarehouse_id;
        this.inCity = inCity;
        this.inStreet = inStreet;
        this.inHouse = inHouse;
        this.inBuilding = inBuilding;
        this.quantity = quantity;
        this.warehouseInventory_id = warehouseInventory_id;
        this.category = category;
        this.brand = brand;
        this.characteristic = characteristic;
        this.unit_measure = unit_measure;
        this.weight_volume = weight_volume;
        this.image_url = image_url;
        this.typePackaging=typePackaging;
        this.quantityPackage=quantityPackage;
        this.check_take_in = check_take_in;
        this.check_give_out = check_give_out;
        this.check_out_active=check_out_active;
        this.inWarehouse_info_id=inWarehouse_info_id;
        this.outWarehouse_info_id=outWarehouse_info_id;
        this.document_num=document_num;
        this.document_closed=document_closed;
    }
    //DeliveryToWarehouseActivity//
    public CarrierPanelModel(int outWarehouse_id,  String outCity,
                             String outStreet, int outHouse, String outBuilding,
                             int inWarehouse_id,  String inCity,  String inStreet,
                             int inHouse, String inBuilding,  int warehouseInventory_id,
                             String category,  String brand, String characteristic,
                             String unit_measure,  int weight_volume, String image_url,
                             double quantity,String typePackaging, int quantityPackage,
                             int check_take_in, int check_give_out, int check_out_active,
                             int outWarehouse_info_id,int inWarehouse_info_id) {
        this.outWarehouse_id = outWarehouse_id;
        this.outCity = outCity;
        this.outStreet = outStreet;
        this.outHouse = outHouse;
        this.outBuilding = outBuilding;
        this.inWarehouse_id = inWarehouse_id;
        this.inCity = inCity;
        this.inStreet = inStreet;
        this.inHouse = inHouse;
        this.inBuilding = inBuilding;
        this.quantity = quantity;
        this.warehouseInventory_id = warehouseInventory_id;
        this.category = category;
        this.brand = brand;
        this.characteristic = characteristic;
        this.unit_measure = unit_measure;
        this.weight_volume = weight_volume;
        this.image_url = image_url;
        this.typePackaging=typePackaging;
        this.quantityPackage=quantityPackage;
        this.check_take_in = check_take_in;
        this.check_give_out = check_give_out;
        this.check_out_active=check_out_active;
        this.inWarehouse_info_id=inWarehouse_info_id;
        this.outWarehouse_info_id=outWarehouse_info_id;
    }
    // CarrierPanelActivity //makeWarehouseListForNextActivity();
    public CarrierPanelModel(int warehouse_id, String city, String street, int house,
                             String building,int warehouse_info_id, int checked) {
        this.warehouse_id = warehouse_id;
        this.city = city;
        this.street = street;
        this.house = house;
        this.building = building;
        this.warehouse_info_id=warehouse_info_id;
        this.checked = checked;
    }
    // PartnerListBuyersForCollectActivity//splitWarehouseResult();
    public CarrierPanelModel(int warehouse_info_id,int warehouse_id, String city, String street, int house,
                             String building) {
        this.warehouse_id = warehouse_id;
        this.city = city;
        this.street = street;
        this.house = house;
        this.building = building;
        this.warehouse_info_id=warehouse_info_id;
    }
    // AcceptProductActivity//splitWarehouseResult();
    public CarrierPanelModel(int warehouse_id, String city, String street, int house,
                             String building, int warehouse_info_id,String warehouse_tipe,
                             int checked) {
        this.warehouse_id = warehouse_id;
        this.city = city;
        this.street = street;
        this.house = house;
        this.building = building;
        this.warehouse_info_id=warehouse_info_id;
        this.warehouse_tipe=warehouse_tipe;
        this.checked = checked;
    }
    public CarrierPanelModel(int warehouse_id, String city, String street, int house,
                             String building, int checked) {
        this.warehouse_id = warehouse_id;
        this.city = city;
        this.street = street;
        this.house = house;
        this.building = building;
        this.checked = checked;
    }
    //CarrierPanelActivity // splitResult();
    public CarrierPanelModel(int outWarehouse_id, String outCity,
                             String outStreet, int outHouse, String outBuilding,
                             int inWarehouse_id, String inCity,
                             String inStreet, int inHouse, String inBuilding,
                             double quantity, double productWeight,
                             String storageTemperature, int warehouseInventory_id,
                             int outWarehouse_info_id, int inWarehouse_info_id, int checked) {
        this.outWarehouse_id = outWarehouse_id;
        this.outCity = outCity;
        this.outStreet = outStreet;
        this.outHouse = outHouse;
        this.outBuilding = outBuilding;
        this.inWarehouse_id = inWarehouse_id;
        this.inCity = inCity;
        this.inStreet = inStreet;
        this.inHouse = inHouse;
        this.inBuilding = inBuilding;
        this.quantity = quantity;
        this.productWeight = productWeight;
        this.storageTemperature = storageTemperature;
        this.warehouseInventory_id=warehouseInventory_id;
        this.checked = checked;
        this.inWarehouse_info_id=inWarehouse_info_id;
        this.outWarehouse_info_id=outWarehouse_info_id;
    }

    public CarrierPanelModel(int outWarehouse_id, String outCity,
                             String outStreet, int outHouse, String outBuilding,
                             int inWarehouse_id, String inCity,
                             String inStreet, int inHouse, String inBuilding,
                             double quantity, double productWeight,
                             String storageTemperature, int warehouseInventory_id,
                             int checked) {
        this.outWarehouse_id = outWarehouse_id;
        this.outCity = outCity;
        this.outStreet = outStreet;
        this.outHouse = outHouse;
        this.outBuilding = outBuilding;
        this.inWarehouse_id = inWarehouse_id;
        this.inCity = inCity;
        this.inStreet = inStreet;
        this.inHouse = inHouse;
        this.inBuilding = inBuilding;
        this.quantity = quantity;
        this.productWeight = productWeight;
        this.storageTemperature = storageTemperature;
        this.warehouseInventory_id=warehouseInventory_id;
        this.checked = checked;
    }

    public int getWarehouse_info_id() {
        return warehouse_info_id;
    }

    public void setWarehouse_info_id(int warehouse_info_id) {
        this.warehouse_info_id = warehouse_info_id;
    }

    public int getWarehouse_id() {
        return warehouse_id;
    }

    public void setWarehouse_id(int warehouse_id) {
        this.warehouse_id = warehouse_id;
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

    public String getWarehouse_tipe() {
        return warehouse_tipe;
    }

    public void setWarehouse_tipe(String warehouse_tipe) {
        this.warehouse_tipe = warehouse_tipe;
    }

    public int getOutWarehouse_id() {
        return outWarehouse_id;
    }

    public void setOutWarehouse_id(int outWarehouse_id) {
        this.outWarehouse_id = outWarehouse_id;
    }

    public String getOutCity() {
        return outCity;
    }

    public void setOutCity(String outCity) {
        this.outCity = outCity;
    }

    public String getOutDistrict() {
        return outDistrict;
    }

    public void setOutDistrict(String outDistrict) {
        this.outDistrict = outDistrict;
    }

    public String getOutStreet() {
        return outStreet;
    }

    public void setOutStreet(String outStreet) {
        this.outStreet = outStreet;
    }

    public int getOutHouse() {
        return outHouse;
    }

    public void setOutHouse(int outHouse) {
        this.outHouse = outHouse;
    }

    public String getOutBuilding() {
        return outBuilding;
    }

    public void setOutBuilding(String outBuilding) {
        this.outBuilding = outBuilding;
    }

    public int getOutWarehouse_info_id() {
        return outWarehouse_info_id;
    }

    public void setOutWarehouse_info_id(int outWarehouse_info_id) {
        this.outWarehouse_info_id = outWarehouse_info_id;
    }

    public int getInWarehouse_info_id() {
        return inWarehouse_info_id;
    }

    public void setInWarehouse_info_id(int inWarehouse_info_id) {
        this.inWarehouse_info_id = inWarehouse_info_id;
    }

    public int getInWarehouse_id() {
        return inWarehouse_id;
    }

    public void setInWarehouse_id(int inWarehouse_id) {
        this.inWarehouse_id = inWarehouse_id;
    }

    public String getInCity() {
        return inCity;
    }

    public void setInCity(String inCity) {
        this.inCity = inCity;
    }

    public String getInDistrict() {
        return inDistrict;
    }

    public void setInDistrict(String inDistrict) {
        this.inDistrict = inDistrict;
    }

    public String getInStreet() {
        return inStreet;
    }

    public void setInStreet(String inStreet) {
        this.inStreet = inStreet;
    }

    public int getInHouse() {
        return inHouse;
    }

    public void setInHouse(int inHouse) {
        this.inHouse = inHouse;
    }

    public String getInBuilding() {
        return inBuilding;
    }

    public void setInBuilding(String inBuilding) {
        this.inBuilding = inBuilding;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(double productWeight) {
        this.productWeight = productWeight;
    }

    public String getStorageTemperature() {
        return storageTemperature;
    }

    public void setStorageTemperature(String storageTemperature) {
        this.storageTemperature = storageTemperature;
    }

    public int getProductInventory_id() {
        return productInventory_id;
    }

    public void setProductInventory_id(int productInventory_id) {
        this.productInventory_id = productInventory_id;
    }

    public int getWarehouseInventory_id() {
        return warehouseInventory_id;
    }

    public void setWarehouseInventory_id(int warehouseInventory_id) {
        this.warehouseInventory_id = warehouseInventory_id;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(String characteristic) {
        this.characteristic = characteristic;
    }

    public String getUnit_measure() {
        return unit_measure;
    }

    public void setUnit_measure(String unit_measure) {
        this.unit_measure = unit_measure;
    }

    public String getTypePackaging() {
        return typePackaging;
    }

    public void setTypePackaging(String typePackaging) {
        this.typePackaging = typePackaging;
    }

    public int getQuantityPackage() {
        return quantityPackage;
    }

    public void setQuantityPackage(int quantityPackage) {
        this.quantityPackage = quantityPackage;
    }

    public int getWeight_volume() {
        return weight_volume;
    }

    public void setWeight_volume(int weight_volume) {
        this.weight_volume = weight_volume;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCheck_give_out() {
        return check_give_out;
    }

    public void setCheck_give_out(int check_give_out) {
        this.check_give_out = check_give_out;
    }

    public int getCheck_take_in() {
        return check_take_in;
    }

    public void setCheck_take_in(int check_take_in) {
        this.check_take_in = check_take_in;
    }


    public int getCheck_out_active() {
        return check_out_active;
    }

    public void setCheck_out_active(int check_out_active) {
        this.check_out_active = check_out_active;
    }

    public int getLogistic_product() {
        return logistic_product;
    }

    public void setLogistic_product(int logistic_product) {
        this.logistic_product = logistic_product;
    }

    public int getCar_or_warehouse_id() {
        return car_or_warehouse_id;
    }

    public void setCar_or_warehouse_id(int car_or_warehouse_id) {
        this.car_or_warehouse_id = car_or_warehouse_id;
    }

    public int getCar_id() {
        return car_id;
    }

    public void setCar_id(int car_id) {
        this.car_id = car_id;
    }

    public int getColorDelivery() {
        return colorDelivery;
    }

    public void setColorDelivery(int colorDelivery) {
        this.colorDelivery = colorDelivery;
    }


    public int getDocument_num() {
        return document_num;
    }

    public void setDocument_num(int document_num) {
        this.document_num = document_num;
    }

    public String getDocument_name() {
        return document_name;
    }

    public void setDocument_name(String document_name) {
        this.document_name = document_name;
    }

    public int getDocument_closed() {
        return document_closed;
    }

    public void setDocument_closed(int document_closed) {
        this.document_closed = document_closed;
    }

    public int getDocument_save() {
        return document_save;
    }

    public void setDocument_save(int document_save) {
        this.document_save = document_save;
    }

    public int getInvoice_key_id() {
        return invoice_key_id;
    }

    public void setInvoice_key_id(int invoice_key_id) {
        this.invoice_key_id = invoice_key_id;
    }
}
