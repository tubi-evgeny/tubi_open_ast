package ru.tubi.project.models;

import java.io.Serializable;

public class OrderModel implements Serializable {
    private int product_id;
    private int product_inventory_id;
    private String description;
    private String category;
    private String product_name;
    private String brand;
    private String characteristic;
    private String type_packaging;
    private String unit_measure;
    private int weight_volume;
    private int quantity_package;
    private double price;
    private double price_process;
    private String storage_conditions;
    private double provider_stock_quantity ;
    private double partner_stock_quantity ;
    private double quantity_to_order ;
    private int warehouse_id;
    private int warehouse_info_id;
    private int warehouse_inventory_id;
    private int partner_warehouse_id;
    private String city;
    private String district;
    private String street;
    private int house;
    private String building;
    private int quantityPartnerWarehouse;
    private int quantityPosition;
    private int quantityProductUnits;
    private int checked;
    private double quantity;
    private double quantityToCollect;
    private double quantity_give_away_bad_do_not_receive;
    private int logistic_product;
    private String image_url;
    private int order_product_id;
    private int collected;
    private int counterparty_id;
    private String abbreviation;
    private String counterparty;
    private long date_millis;
    private int order_id;
    private int corrected;
    private int delivery;


    //CollectProductActivity//ArrayList<OrderModel> checkedList
    public OrderModel(int checked) {
        this.checked = checked;

    }
    //CollectProductActivity//goReadUid
    public OrderModel(int order_id, long date_millis, String category) {
        this.order_id = order_id;
        this.date_millis=date_millis;
        this.category=category;

    }
    //OrderDataRecoveryUtil//goReadUid
    public OrderModel(int order_id, long date_millis, String category, int delivery) {
        this.order_id = order_id;
        this.date_millis=date_millis;
        this.category=category;
        this.delivery=delivery;
    }

    //ShowMyOrderActivity //splitResult();
    public OrderModel(int product_id, int product_inventory_id, String category
            ,String product_name, String brand, String characteristic
            ,String type_packaging, String unit_measure, int weight_volume
            , int quantity_package, String image_url, double price, double price_process
            , String storage_conditions, double quantity_to_order
            , int counterparty_id, String abbreviation, String counterparty
            , int corrected) {

        this.product_id = product_id;
        this.product_inventory_id = product_inventory_id;
        this.category = category;
        this.product_name=product_name;
        this.brand = brand;
        this.characteristic = characteristic;
        this.type_packaging = type_packaging;
        this.unit_measure = unit_measure;
        this.weight_volume = weight_volume;
        this.quantity_package = quantity_package;
        this.price = price;
        this.price_process = price_process;
        this.storage_conditions = storage_conditions;
        this.quantity_to_order = quantity_to_order;
        this.image_url = image_url;
        this.counterparty_id = counterparty_id;
        this.abbreviation = abbreviation;
        this.counterparty = counterparty;
        this.corrected=corrected;
    }

    public OrderModel(int product_id, int product_inventory_id, String category
            ,String product_name, String brand, String characteristic
            ,String type_packaging, String unit_measure, int weight_volume
            , int quantity_package, String image_url, double price, double price_process
            , String storage_conditions, double quantity_to_order
            , int counterparty_id, String abbreviation, String counterparty) {

        this.product_id = product_id;
        this.product_inventory_id = product_inventory_id;
        this.category = category;
        this.product_name=product_name;
        this.brand = brand;
        this.characteristic = characteristic;
        this.type_packaging = type_packaging;
        this.unit_measure = unit_measure;
        this.weight_volume = weight_volume;
        this.quantity_package = quantity_package;
        this.price = price;
        this.price_process = price_process;
        this.storage_conditions = storage_conditions;
        this.quantity_to_order = quantity_to_order;
        this.image_url = image_url;
        this.counterparty_id = counterparty_id;
        this.abbreviation = abbreviation;
        this.counterparty = counterparty;
    }
    //OrderDataRecoveryUtil //splitResult();
    public OrderModel(int product_id, int product_inventory_id, String category
            ,String product_name, String brand, String characteristic
            ,String type_packaging, String unit_measure, int weight_volume
            , int quantity_package, String image_url, double price
            , String storage_conditions, double quantity_to_order
            , int counterparty_id, String abbreviation, String counterparty) {

        this.product_id = product_id;
        this.product_inventory_id = product_inventory_id;
        this.category = category;
        this.product_name=product_name;
        this.brand = brand;
        this.characteristic = characteristic;
        this.type_packaging = type_packaging;
        this.unit_measure = unit_measure;
        this.weight_volume = weight_volume;
        this.quantity_package = quantity_package;
        this.price = price;
        this.storage_conditions = storage_conditions;
        this.quantity_to_order = quantity_to_order;
        this.image_url = image_url;
        this.counterparty_id = counterparty_id;
        this.abbreviation = abbreviation;
        this.counterparty = counterparty;
    }

    //BuyerOrderIssueActivity //splitResult();
    public OrderModel(int product_id, int product_inventory_id, String category,
                      String brand, String characteristic, String type_packaging,
                      String unit_measure, int weight_volume, int quantity_package,
                      String image_url,int order_product_id,double quantity_to_order,
                      int warehouse_inventory_id, int collected, int checked
            ,double price, String description) {

        this.product_id = product_id;
        this.product_inventory_id = product_inventory_id;
        this.category = category;
        this.brand = brand;
        this.characteristic = characteristic;
        this.type_packaging = type_packaging;
        this.unit_measure = unit_measure;
        this.weight_volume = weight_volume;
        this.quantity_package = quantity_package;
        this.image_url=image_url;
        this.order_product_id=order_product_id;
        this.quantity_to_order = quantity_to_order;
        this.warehouse_inventory_id = warehouse_inventory_id;
        this.collected=collected;
        this.checked = checked;
        this.price = price;
        this.description=description;
    }
    // CollectProductActivity//splitResult();
    // CollectProductActivity//sortStartList()
    public OrderModel(int warehouse_id, int product_id, int product_inventory_id,
                      String category, String product_name, String brand, String characteristic, String type_packaging,
                      String unit_measure, int weight_volume, int quantity_package, double quantity,
                      int partner_warehouse_id, String city, String street, int house,
                      String building, int checked, int warehouse_inventory_id
            , int logistic_product) {

        this.product_id = product_id;
        this.product_inventory_id = product_inventory_id;
        this.category = category;
        this.product_name=product_name;
        this.brand = brand;
        this.characteristic = characteristic;
        this.type_packaging = type_packaging;
        this.unit_measure = unit_measure;
        this.weight_volume = weight_volume;
        this.quantity_package = quantity_package;
        this.quantity=quantity;
        this.warehouse_id = warehouse_id;
        this.partner_warehouse_id = partner_warehouse_id;
        this.city = city;
        this.street = street;
        this.house = house;
        this.building = building;
        this.checked=checked;
        this.warehouse_inventory_id=warehouse_inventory_id;
        this.logistic_product=logistic_product;
    }
    //DistributionOrdersProviderPartnersActivity// splitResult();
    public OrderModel(int warehouse_id,int product_id, int product_inventory_id,
                      String category, String product_name, String brand,
                      String characteristic, String type_packaging, String unit_measure,
                      int weight_volume, int quantity_package, double provider_stock_quantity,
                      double partner_stock_quantity, double quantity_to_order,
                      int partner_warehouse_id, String city, String street, int house,
                      String building,double quantityToCollect,
                      double quantity_give_away_bad_do_not_receive,int warehouse_info_id) {

        this.product_id = product_id;
        this.product_inventory_id = product_inventory_id;
        this.category = category;
        this.product_name=product_name;
        this.brand = brand;
        this.characteristic = characteristic;
        this.type_packaging = type_packaging;
        this.unit_measure = unit_measure;
        this.weight_volume = weight_volume;
        this.quantity_package = quantity_package;
        this.provider_stock_quantity = provider_stock_quantity;
        this.partner_stock_quantity = partner_stock_quantity;
        this.quantity_to_order = quantity_to_order;
        this.warehouse_id = warehouse_id;
        this.partner_warehouse_id = partner_warehouse_id;
        this.city = city;
        this.street = street;
        this.house = house;
        this.building = building;
        this.quantityToCollect=quantityToCollect;
        this.quantity_give_away_bad_do_not_receive=quantity_give_away_bad_do_not_receive;
        this.warehouse_info_id=warehouse_info_id;
    }
    //CollectProductActivity //onCreate();
    public OrderModel(int warehouse_id,int product_id, int product_inventory_id, String category, String brand,
                      String characteristic, String type_packaging, String unit_measure,
                      int weight_volume, int quantity_package, double provider_stock_quantity,
                      double partner_stock_quantity, double quantity_to_order,
                      int partner_warehouse_id, String city, String street, int house,
                      String building,double quantityToCollect,int warehouse_info_id) {

        this.product_id = product_id;
        this.product_inventory_id = product_inventory_id;
        this.category = category;
        this.brand = brand;
        this.characteristic = characteristic;
        this.type_packaging = type_packaging;
        this.unit_measure = unit_measure;
        this.weight_volume = weight_volume;
        this.quantity_package = quantity_package;
        this.provider_stock_quantity = provider_stock_quantity;
        this.partner_stock_quantity = partner_stock_quantity;
        this.quantity_to_order = quantity_to_order;
        this.warehouse_id = warehouse_id;
        this.partner_warehouse_id = partner_warehouse_id;
        this.city = city;
        this.street = street;
        this.house = house;
        this.building = building;
        this.quantityToCollect=quantityToCollect;
        this.warehouse_info_id=warehouse_info_id;
    }

    public OrderModel(int warehouse_id,int product_id, int product_inventory_id, String category, String brand,
                      String characteristic, String type_packaging, String unit_measure,
                      int weight_volume, int quantity_package, double provider_stock_quantity,
                      double partner_stock_quantity, double quantity_to_order,
                      int partner_warehouse_id, String city, String street, int house,
                      String building,double quantityToCollect) {

        this.product_id = product_id;
        this.product_inventory_id = product_inventory_id;
        this.category = category;
        this.brand = brand;
        this.characteristic = characteristic;
        this.type_packaging = type_packaging;
        this.unit_measure = unit_measure;
        this.weight_volume = weight_volume;
        this.quantity_package = quantity_package;
        this.provider_stock_quantity = provider_stock_quantity;
        this.partner_stock_quantity = partner_stock_quantity;
        this.quantity_to_order = quantity_to_order;
        this.warehouse_id = warehouse_id;
        this.partner_warehouse_id = partner_warehouse_id;
        this.city = city;
        this.street = street;
        this.house = house;
        this.building = building;
        this.quantityToCollect=quantityToCollect;
    }
    //DistributionOrdersByWarehousesActivity// splitResult();
    public OrderModel(int warehouse_info_id, String city, String street, int house,
                        String building, int warehouse_id) {
        this.warehouse_id = warehouse_id;
        this.city = city;
        this.street = street;
        this.house = house;
        this.building = building;
        this.warehouse_info_id=warehouse_info_id;
    }

    public OrderModel(int warehouse_id, String city, String street, int house, String building) {
        this.warehouse_id = warehouse_id;
        this.city = city;
        this.street = street;
        this.house = house;
        this.building = building;
    }

    public OrderModel(int warehouse_id, String city, String district, String street,
                      int house, String building, int quantityPartnerWarehouse,
                      int quantityPosition,  int quantityProductUnits) {
        this.warehouse_id = warehouse_id;
        this.city = city;
        this.district = district;
        this.street = street;
        this.house=house;
        this.building = building;
        this.quantityPartnerWarehouse = quantityPartnerWarehouse;
        this.quantityPosition = quantityPosition;
        this.quantityProductUnits = quantityProductUnits;
    }

    public int getWarehouse_id() {
        return warehouse_id;
    }

    public void setWarehouse_id(int warehouse_id) {
        this.warehouse_id = warehouse_id;
    }

    public int getWarehouse_info_id() {
        return warehouse_info_id;
    }

    public void setWarehouse_info_id(int warehouse_info_id) {
        this.warehouse_info_id = warehouse_info_id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
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

    public int getQuantityPartnerWarehouse() {
        return quantityPartnerWarehouse;
    }

    public void setQuantityPartnerWarehouse(int quantityPartnerWarehouse) {
        this.quantityPartnerWarehouse = quantityPartnerWarehouse;
    }

    public int getQuantityPosition() {
        return quantityPosition;
    }

    public void setQuantityPosition(int quantityPosition) {
        this.quantityPosition = quantityPosition;
    }

    public int getQuantityProductUnits() {
        return quantityProductUnits;
    }

    public void setQuantityProductUnits(int quantityProductUnits) {
        this.quantityProductUnits = quantityProductUnits;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getProduct_inventory_id() {
        return product_inventory_id;
    }

    public void setProduct_inventory_id(int product_inventory_id) {
        this.product_inventory_id = product_inventory_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getType_packaging() {
        return type_packaging;
    }

    public void setType_packaging(String type_packaging) {
        this.type_packaging = type_packaging;
    }

    public String getUnit_measure() {
        return unit_measure;
    }

    public void setUnit_measure(String unit_measure) {
        this.unit_measure = unit_measure;
    }

    public int getWeight_volume() {
        return weight_volume;
    }

    public void setWeight_volume(int weight_volume) {
        this.weight_volume = weight_volume;
    }

    public int getQuantity_package() {
        return quantity_package;
    }

    public void setQuantity_package(int quantity_package) {
        this.quantity_package = quantity_package;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public double getPrice_process() {
        return price_process;
    }

    public void setPrice_process(double price_process) {
        this.price_process = price_process;
    }

    public double getProvider_stock_quantity() {
        return provider_stock_quantity;
    }

    public void setProvider_stock_quantity(double provider_stock_quantity) {
        this.provider_stock_quantity = provider_stock_quantity;
    }

    public double getPartner_stock_quantity() {
        return partner_stock_quantity;
    }

    public void setPartner_stock_quantity(double partner_stock_quantity) {
        this.partner_stock_quantity = partner_stock_quantity;
    }

    public double getQuantity_to_order() {
        return quantity_to_order;
    }

    public void setQuantity_to_order(double quantity_to_order) {
        this.quantity_to_order = quantity_to_order;
    }

    public int getPartner_warehouse_id() {
        return partner_warehouse_id;
    }

    public void setPartner_warehouse_id(int partner_warehouse_id) {
        this.partner_warehouse_id = partner_warehouse_id;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getQuantityToCollect() {
        return quantityToCollect;
    }

    public void setQuantityToCollect(double quantityToCollect) {
        this.quantityToCollect = quantityToCollect;
    }

    public double getQuantity_give_away_bad_do_not_receive() {
        return quantity_give_away_bad_do_not_receive;
    }

    public void setQuantity_give_away_bad_do_not_receive(double quantity_give_away_bad_do_not_receive) {
        this.quantity_give_away_bad_do_not_receive = quantity_give_away_bad_do_not_receive;
    }

    public int getWarehouse_inventory_id() {
        return warehouse_inventory_id;
    }

    public void setWarehouse_inventory_id(int warehouse_inventory_id) {
        this.warehouse_inventory_id = warehouse_inventory_id;
    }

    public int getLogistic_product() {
        return logistic_product;
    }

    public void setLogistic_product(int logistic_product) {
        this.logistic_product = logistic_product;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public int getOrder_product_id() {
        return order_product_id;
    }

    public void setOrder_product_id(int order_product_id) {
        this.order_product_id = order_product_id;
    }

    public int getCollected() {
        return collected;
    }

    public void setCollected(int collected) {
        this.collected = collected;
    }

    public String getStorage_conditions() {
        return storage_conditions;
    }

    public void setStorage_conditions(String storage_conditions) {
        this.storage_conditions = storage_conditions;
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

    public long getDate_millis() {
        return date_millis;
    }

    public void setDate_millis(long date_millis) {
        this.date_millis = date_millis;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getCorrected() {
        return corrected;
    }

    public void setCorrected(int corrected) {
        this.corrected = corrected;
    }

    public int getDelivery() {
        return delivery;
    }

    public void setDelivery(int delivery) {
        this.delivery = delivery;
    }
}
