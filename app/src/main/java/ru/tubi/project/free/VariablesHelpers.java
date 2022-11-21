package ru.tubi.project.free;

import java.util.ArrayList;

import ru.tubi.project.models.AddressModel;
import ru.tubi.project.models.DeliveryAddressModel;

public class VariablesHelpers {

    public static String MESSAGE_FROM_ORDER_ACTIVITY = "";

    public static String MY_CITY = "";
    public static String MY_DICTRICT = "";
    public static String MY_REGION = "";

    //статус доставки, доставвка есть=1; нет=0;
    public static int DELIVERY_TO_BUYER_STATUS = 0;
    //приложение делает доставку (доставка открыта)да=1; нет=0;
    public static int DELIVERY_OPEN = 0;

    public static DeliveryAddressModel DELIVERY_ADDRESS_LAST  =
            new DeliveryAddressModel("","","",""
                    ,0,"","", "");

    public static int  ORDER_FINISHED_ACTIVITY_MESSEGE = 0;

    public static int MESSAGE_NUMBER = 0;

    //замена фото из склад товаров
    public static int CHENGE_IMAGE_ALERT_SHOW = 0;

    //сообщение в UserToInfoForOrderActivity
    public static int DIALOG_BUY_GOODS_TOGETHER = 0;

    //склад который выбран для получения совместной покупки
    public static int WAREHOUSE_ID_FOR_JOINT_BUY = 0;

    //вам есть восемнадцать
    public static int YOUR_ARE_EIGHTEEN = 0;
}
