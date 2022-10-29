
package ru.tubi.project.activity;

import java.util.ArrayList;

public class Config {

    //your admin panel url
    //public static final String ADMIN_PANEL_URL = "https://h102582557.nichost.ru/api_tubi/";
    public static final String ADMIN_PANEL_URL = "https://h102582557.nichost.ru/test_tubi/";

           //your admin images file
    public static final String ADMIN_PANEL_URL_IMAGES = ADMIN_PANEL_URL+"image/";
    //public static final String ADMIN_PANEL_URL_IMAGES = "https://h102582557.nichost.ru/api/image/";
    //your admin preview images file
    public static final String ADMIN_PANEL_URL_PREVIEW_IMAGES = ADMIN_PANEL_URL+"preview_image/";
    //public static final String ADMIN_PANEL_URL_PREVIEW_IMAGES =
    //                                            "https://h102582557.nichost.ru/api/preview_image/";

       //your counterparty id
    public static int COUNTERPARTY_ID = 0;

    // your unique uid
    public static String MY_UID = null;
    // your name
    public static String MY_NAME = null;

    public static String ROLE = null;
    //public static String ROLE = "user";

    public static String MY_ABBREVIATION = null;
    public static String MY_NAME_COMPANY = null;
    public static String MY_COMPANY_TAXPAYER_ID = null;
    //public static ArrayList<String> PARTNER_ROLE_LIST = new ArrayList<>();

    //инн компании для использования агентом при заказах
    public static long PARTNER_COMPANY_TAXPAYER_ID_FOR_AGENT = 0;
    public static String PARTNER_COMPANY_INFO_FOR_AGENT = "";

         //search open order condition for soping box(condition) for start activity
    public static boolean OPEN_ORDER_CONDITION = false;

            //---order_id from t_order delete every night
    public static int ORDER_ID = 0;
    //колличество показов сообщения
    //public static int COUNT_SHOW_MESSEEGE = 0;

    //проверка роли поставщика
    public static String CHECK_PROVIDER_ROLE = "";
    //проверка роли партнер склада
    public static String CHECK_PARTNER_WAREHOUSE_ROLE = "";
    public static ArrayList<String> PARTNER_ROLE_LIST = new ArrayList<>();//PartnerRoleReceive
    public static String ROLE_PARTNER_TEST  = "";
    public static String CONFIG_TEST  = "";

}
