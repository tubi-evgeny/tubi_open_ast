package ru.tubi.project.models;

import java.io.Serializable;

public class AcceptProductListProvidersModel implements Serializable {
    private int product_id;
    private int product_inventory_id;
    private String category;
    private String brand;                   //3
    private String characteristic;
    private String type_packaging;          //5
    private String unit_measure;
    private int weight_volume;              //7
    private double total_quantity;
    private double price;                   //9
    private int quantity_package;
    private String image_url;               //11
    private String description;
    private String storage_conditions;
    private double total_sale_quantity;     //13
    private double free_balance;
    private double quantity;
    private double quantity_to_order;
    private double quantity_of_colected;
    private double partner_stock_quantity;
    private int order_product_id;
    private int checked;
    private int providerChecked;
    private int agentChecked;
    private int order_id;
    private int counterparty_id;
    private String abbreviation;
    private String counterparty;
    private int taxpayer_id;
    private int order_active;
    private int invoice_key_id;
    private int out_active;
    private int redactor;

    // ProviderCollectProductActivity//splitResult();
    public AcceptProductListProvidersModel(int product_id, int product_inventory_id,
                                           String category, String brand, String characteristic,
                                           String type_packaging, String unit_measure, int weight_volume,
                                           int quantity_package, String image_url, int order_product_id,
                                           double quantity_to_order,double partner_stock_quantity,
                                           int counterparty_id,String abbreviation,
                                           String counterparty,String storage_conditions,
                                           int checked, double quantity_of_colected,int order_active) {
        this.product_id = product_id;
        this.product_inventory_id = product_inventory_id;
        this.category = category;
        this.brand = brand;
        this.characteristic = characteristic;
        this.type_packaging = type_packaging;
        this.unit_measure = unit_measure;
        this.weight_volume = weight_volume;
        this.quantity_package = quantity_package;
        this.image_url = image_url;
        this.quantity_to_order = quantity_to_order;
        this.partner_stock_quantity = partner_stock_quantity;
        this.order_product_id = order_product_id;
        this.counterparty_id = counterparty_id;
        this.abbreviation = abbreviation;
        this.counterparty = counterparty;
        this.storage_conditions=storage_conditions;
        this.checked=checked;
        this.quantity_of_colected=quantity_of_colected;
        this.order_active=order_active;
    }
    // PartnerCollectProductActivity//splitResult();
    public AcceptProductListProvidersModel(int product_id, int product_inventory_id,
                        String category, String brand, String characteristic,
                        String type_packaging, String unit_measure, int weight_volume,
                         int quantity_package, String image_url, int order_product_id,
                         double quantity_to_order,double partner_stock_quantity,
                         int counterparty_id,String abbreviation,
                         String counterparty,String storage_conditions,
                         int checked, double quantity_of_colected, int invoice_key_id
            , int out_active, String description, int redactor) {

        this.product_id = product_id;
        this.product_inventory_id = product_inventory_id;
        this.category = category;
        this.brand = brand;
        this.characteristic = characteristic;
        this.type_packaging = type_packaging;
        this.unit_measure = unit_measure;
        this.weight_volume = weight_volume;
        this.quantity_package = quantity_package;
        this.image_url = image_url;
        this.quantity_to_order = quantity_to_order;
        this.partner_stock_quantity = partner_stock_quantity;
        this.order_product_id = order_product_id;
        this.counterparty_id = counterparty_id;
        this.abbreviation = abbreviation;
        this.counterparty = counterparty;
        this.storage_conditions=storage_conditions;
        this.checked=checked;
        this.quantity_of_colected=quantity_of_colected;
        this.invoice_key_id=invoice_key_id;
        this.out_active=out_active;
        this.description=description;
        this.redactor=redactor;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public double getTotal_quantity() {
        return total_quantity;
    }

    public void setTotal_quantity(double total_quantity) {
        this.total_quantity = total_quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity_package() {
        return quantity_package;
    }

    public void setQuantity_package(int quantity_package) {
        this.quantity_package = quantity_package;
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

    public String getStorage_conditions() {
        return storage_conditions;
    }

    public void setStorage_conditions(String storage_conditions) {
        this.storage_conditions = storage_conditions;
    }

    public double getTotal_sale_quantity() {
        return total_sale_quantity;
    }

    public void setTotal_sale_quantity(double total_sale_quantity) {
        this.total_sale_quantity = total_sale_quantity;
    }

    public double getFree_balance() {
        return free_balance;
    }

    public void setFree_balance(double free_balance) {
        this.free_balance = free_balance;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public int getOrder_product_id() {
        return order_product_id;
    }

    public void setOrder_product_id(int order_product_id) {
        this.order_product_id = order_product_id;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    public int getProviderChecked() {
        return providerChecked;
    }

    public void setProviderChecked(int providerChecked) {
        this.providerChecked = providerChecked;
    }

    public int getAgentChecked() {
        return agentChecked;
    }

    public void setAgentChecked(int agentChecked) {
        this.agentChecked = agentChecked;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
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

    public int getTaxpayer_id() {
        return taxpayer_id;
    }

    public void setTaxpayer_id(int taxpayer_id) {
        this.taxpayer_id = taxpayer_id;
    }

    public double getQuantity_to_order() {
        return quantity_to_order;
    }

    public void setQuantity_to_order(double quantity_to_order) {
        this.quantity_to_order = quantity_to_order;
    }

    public double getQuantity_of_colected() {
        return quantity_of_colected;
    }

    public void setQuantity_of_colected(double quantity_of_colected) {
        this.quantity_of_colected = quantity_of_colected;
    }

    public double getPartner_stock_quantity() {
        return partner_stock_quantity;
    }

    public void setPartner_stock_quantity(double partner_stock_quantity) {
        this.partner_stock_quantity = partner_stock_quantity;
    }

    public int getOrder_active() {
        return order_active;
    }

    public void setOrder_active(int order_active) {
        this.order_active = order_active;
    }

    public int getInvoice_key_id() {
        return invoice_key_id;
    }

    public void setInvoice_key_id(int invoice_key_id) {
        this.invoice_key_id = invoice_key_id;
    }

    public int getOut_active() {
        return out_active;
    }

    public void setOut_active(int out_active) {
        this.out_active = out_active;
    }

    public int getRedactor() {
        return redactor;
    }

    public void setRedactor(int redactor) {
        this.redactor = redactor;
    }
}
