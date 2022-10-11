package ru.tubi.project.utilites;

import ru.tubi.project.activity.Config;

import static ru.tubi.project.activity.Config.COUNTERPARTY_ID;

public class Constant {

    //API Transactions
    private static final String BASE_URL = Config.ADMIN_PANEL_URL;//"http://h102582557.nichost.ru/api/";

     //privacy policy
    public static final String PRIVACY_POLICY_URL = "https://docs.google.com/document/d/1dpl_Q0Jmvrsnf1FkgG_4JEiD6AbipVldnALIK4CoQNE/edit?usp=sharing";

    private static final String OPEN_ORDER_CONDITION = "";
    public static final String GET_CATALOG = BASE_URL + "select_catalog.php?";
    public static final String PUT_CATALOG = BASE_URL + "insert_catalog.php";
    public static final String GET_CATEGORY = BASE_URL + "select_category.php?catalog=";
    public static final String PUT_CATEGORY = BASE_URL + "insert_category.php";//
    public static final String API_TEST = BASE_URL  + "api_test.php?";
    public static final String GET_PRODUCT_AND_QUANTITY = BASE_URL + "api_test.php?show_product_and_quantity";
    public static final String GET_PRODUCT_PRICE_ALL_PROVIDER = BASE_URL  +
                                    "api_test.php?show_product_price_all_provider";
    public static final String SEARCH_MY_OPEN_ORDER = BASE_URL + "api_test.php?search_my_open_order";//<delete>
    public static final String SEARCH_MY_ACTIVE_ORDER = BASE_URL + "api.php?search_my_active_order";

    // add new order if there open order to take it order_id
    public static final String ADD_ORDER = BASE_URL + "api_test.php?add_order&counterparty_id=" + COUNTERPARTY_ID;//<delete>
    public static final String ADD_MY_ORDER = BASE_URL + "api_test.php?add_my_order";
    //add_my_order
    // add new product in order
    public static final String ADD_ORDER_PRODUCT = BASE_URL + "api_test.php?add_order_product";
    public static final String DELETE_ORDER_PRODUCT = BASE_URL + "api_test.php?delete_order_product";
    //check and fix data from <t_input_product>
    public static final String ADD_INPUT_PRODUCT = BASE_URL + "input.php?input_product";
    public static final String ADD_INPUT_CHECK = BASE_URL + "input.php?";
    public static final String INPUT = BASE_URL + "input.php?";
    public static final String API = BASE_URL + "api.php?";

    public static final String SHOPING_BOX = BASE_URL + "shoping_box.php?";
    public static final String SHOW_SHOPING_BOX = BASE_URL + "shoping_box.php?";
    public static final String URL_REGISTER = BASE_URL + "register.php?";
    public static final String URL_LOGIN = BASE_URL + "login.php?";

    public static final String CHENGE_ORDER_ACTIVE = BASE_URL + "api.php?chenge_order_active";
    public static final String WRITE_MESSAGE_FROM_ORDER = BASE_URL + "api.php?write_message_from_order";
    public static final String USER_ROLE_RECEIVE = BASE_URL + "api.php?user_role_receive";
    public static final String TRANSFER_REQUEST_NEW_PROVIDER = BASE_URL + "api.php?transfer_request_new_provider";
    public static final String RECEIVE_MY_ORDER_HISTORY = BASE_URL + "receive_my_order_history.php?receive";
    public static final String PRODUCT_CARD_ADD = BASE_URL + "product_card_add.php?";
    public static final String PROVIDER_OFFICE = BASE_URL + "provider_office.php?";
    public static final String USER_OFFICE = BASE_URL + "user_office.php?";
    public static final String ADMIN_OFFICE = BASE_URL + "admin_office.php?";
    public static final String CARRIER_OFFICE = BASE_URL + "carrier_office.php?";
    public static final String PARTNER_OFFICE = BASE_URL + "partner_office.php?";
    public static final String WAREHOUSE_OFFICE = BASE_URL + "warehouse_office.php?";
    public static final String AGENT_OFFICE = BASE_URL + "agent_office.php?";
    public static final String AUTHORIZATION_USER = BASE_URL + "authorization_user.php?";

    //insert_category.php

}
