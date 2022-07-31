package ru.tubi.project.free;

import java.util.ArrayList;

import ru.tubi.project.models.AddressModel;
import ru.tubi.project.models.DeliveryAddressModel;

public class VariablesHelpers {

    public static String MESSAGE_FROM_ORDER_ACTIVITY = "";

    public static String MY_CITY = "";
    public static String MY_DICTRICT = "";
    public static String MY_REGION = "";

    public static int DELIVERY_TO_BUYER_STATUS = 0;

    public static DeliveryAddressModel DELIVERY_ADDRESS_LAST  =
            new DeliveryAddressModel("","","",""
                    ,0,"","", "");

    public static int  ORDER_FINISHED_ACTIVITY_MESSEGE = 0;

    public static int MESSAGE_NUMBER = 0;
}
