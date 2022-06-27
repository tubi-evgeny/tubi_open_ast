package ru.tubi.project.models;

public class InvoiceModel {

    private int in_warehouse_id;
    private int out_warehouse_id;
    private int out_warehouse_info_id;
    private String in_warehouseInfoString;
    private int productInventory_id;
    private int warehouseInventory_id;
    private double quantity;
    private int logistic_product_check;
    private int $car_for_logistic_check;
    private int out_active_check;
    private int car_id;
    private String car_info;
    private int invoice_key_id;
    private String description_docs;
    private String product_name_from_provider;
    private double price;
    private String image_url;
    private int order_id;
    private int order_partner_id;
    private int executed;
    private String companyInfoString_short;
    private String in_companyInfoString_short;
    private long get_order_date_millis;
    private String get_order_date;
    private String date_order_start;
    private double summ;
    private String document_name;
    private int document_num;
    private String text;
    private int quantity_package;


    // OrderBuyerListActivity //splitInvoiceInfoListResult()
    public InvoiceModel( String document_name, int document_num) {
        this.document_name = document_name;
        this.document_num = document_num;
    }
    //OrderToProviderListActivity //splitInvoiceInfoListResult();
    public InvoiceModel( String document_name, int document_num
            , int invoice_key_id, String text) {
        this.document_name = document_name;
        this.document_num = document_num;
        this.invoice_key_id=invoice_key_id;
        this.text=text;
    }
    //OrderToProviderListActivity //splitResult();
    public InvoiceModel(int order_partner_id, int executed, int out_warehouse_info_id
            , int out_warehouse_id
            , String in_companyInfoString_short, String in_warehouseInfoString
            , String date_order_start, double order_summ, String get_order_date
            , int invoice_key_id) {

        this.order_partner_id = order_partner_id;
        this.executed = executed;
        this.out_warehouse_info_id = out_warehouse_info_id;
        this.out_warehouse_id = out_warehouse_id;
        this.in_companyInfoString_short = in_companyInfoString_short;
        this.in_warehouseInfoString = in_warehouseInfoString;
        this.date_order_start = date_order_start;
        this.summ = order_summ;
        this.get_order_date = get_order_date;
        this.invoice_key_id = invoice_key_id;
    }
    //ProductReceiptActivity //splitResult()
    //OrderBuyerListActivity //splitResult()
    public InvoiceModel(int order_id, int executed, String buyer_companyInfoString_short
            , long get_order_date_millis, String date_order_start
            , double order_summ, String get_order_date, int invoice_key_id) {

        this.order_id = order_id;
        this.executed = executed;
        this.companyInfoString_short = buyer_companyInfoString_short;
        this.get_order_date_millis = get_order_date_millis;
        this.date_order_start = date_order_start;
        this.summ = order_summ;
        this.get_order_date = get_order_date;
        this.invoice_key_id = invoice_key_id;
    }

    // ProductInvoicePDFActivity //splitInvoiceResult()
    public InvoiceModel(String description_docs, double quantity, double price) {
        this.description_docs=description_docs;
        this.quantity = quantity;
        this.price = price;
    }

    //InvoicePDFActivity//splitInvoiceResult()
    public InvoiceModel(String description_docs, double quantity
            , double price, String product_name_from_provider
            ,int quantity_package) {
        this.description_docs=description_docs;
        this.quantity = quantity;
        this.price = price;
        this.product_name_from_provider = product_name_from_provider;
        this.quantity_package=quantity_package;
    }

    public InvoiceModel(String description_docs, double quantity
            , double price, String product_name_from_provider) {
        this.description_docs=description_docs;
        this.quantity = quantity;
        this.price = price;
        this.product_name_from_provider = product_name_from_provider;
    }

    public int getIn_warehouse_id() {
        return in_warehouse_id;
    }

    public void setIn_warehouse_id(int in_warehouse_id) {
        this.in_warehouse_id = in_warehouse_id;
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

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public int getLogistic_product_check() {
        return logistic_product_check;
    }

    public void setLogistic_product_check(int logistic_product_check) {
        this.logistic_product_check = logistic_product_check;
    }

    public int get$car_for_logistic_check() {
        return $car_for_logistic_check;
    }

    public void set$car_for_logistic_check(int $car_for_logistic_check) {
        this.$car_for_logistic_check = $car_for_logistic_check;
    }

    public int getOut_active_check() {
        return out_active_check;
    }

    public void setOut_active_check(int out_active_check) {
        this.out_active_check = out_active_check;
    }

    public int getCar_id() {
        return car_id;
    }

    public void setCar_id(int car_id) {
        this.car_id = car_id;
    }

    public String getCar_info() {
        return car_info;
    }

    public void setCar_info(String car_info) {
        this.car_info = car_info;
    }

    public int getInvoice_key_id() {
        return invoice_key_id;
    }

    public void setInvoice_key_id(int invoice_key_id) {
        this.invoice_key_id = invoice_key_id;
    }

    public String getDescription_docs() {
        return description_docs;
    }

    public void setDescription_docs(String description_docs) {
        this.description_docs = description_docs;
    }


    public String getProduct_name_from_provider() {
        return product_name_from_provider;
    }

    public void setProduct_name_from_provider(String product_name_from_provider) {
        this.product_name_from_provider = product_name_from_provider;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getExecuted() {
        return executed;
    }

    public void setExecuted(int executed) {
        this.executed = executed;
    }

    public String getCompanyInfoString_short() {
        return companyInfoString_short;
    }

    public void setCompanyInfoString_short(String companyInfoString_short) {
        this.companyInfoString_short = companyInfoString_short;
    }

    public long getGet_order_date_millis() {
        return get_order_date_millis;
    }

    public void setGet_order_date_millis(long get_order_date_millis) {
        this.get_order_date_millis = get_order_date_millis;
    }

    public String getDate_order_start() {
        return date_order_start;
    }

    public void setDate_order_start(String date_order_start) {
        this.date_order_start = date_order_start;
    }

    public double getSumm() {
        return summ;
    }

    public void setSumm(double summ) {
        this.summ = summ;
    }

    public String getGet_order_date() {
        return get_order_date;
    }

    public void setGet_order_date(String get_order_date) {
        this.get_order_date = get_order_date;
    }

    public String getDocument_name() {
        return document_name;
    }

    public void setDocument_name(String document_name) {
        this.document_name = document_name;
    }

    public int getDocument_num() {
        return document_num;
    }

    public void setDocument_num(int document_num) {
        this.document_num = document_num;
    }

    public int getOut_warehouse_id() {
        return out_warehouse_id;
    }

    public void setOut_warehouse_id(int out_warehouse_id) {
        this.out_warehouse_id = out_warehouse_id;
    }

    public String getIn_warehouseInfoString() {
        return in_warehouseInfoString;
    }

    public void setIn_warehouseInfoString(String in_warehouseInfoString) {
        this.in_warehouseInfoString = in_warehouseInfoString;
    }

    public int getOrder_partner_id() {
        return order_partner_id;
    }

    public void setOrder_partner_id(int order_partner_id) {
        this.order_partner_id = order_partner_id;
    }

    public String getIn_companyInfoString_short() {
        return in_companyInfoString_short;
    }

    public void setIn_companyInfoString_short(String in_companyInfoString_short) {
        this.in_companyInfoString_short = in_companyInfoString_short;
    }

    public int getOut_warehouse_info_id() {
        return out_warehouse_info_id;
    }

    public void setOut_warehouse_info_id(int out_warehouse_info_id) {
        this.out_warehouse_info_id = out_warehouse_info_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getQuantity_package() {
        return quantity_package;
    }

    public void setQuantity_package(int quantity_package) {
        this.quantity_package = quantity_package;
    }
}
