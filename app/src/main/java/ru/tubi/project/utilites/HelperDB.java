package ru.tubi.project.utilites;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import ru.tubi.project.activity.LoginActivity;

import static android.content.ContentValues.TAG;

public class HelperDB extends SQLiteOpenHelper {

    //имена таблиц
    public static String TABLE_NAME = "my_user";

    public static final String TABLE_NAME_MY_USER = "my_user";
    public static final String TABLE_NAME_ORDER_ID = "order_id";

    private static final String DATABASE_NAME = "tubi.db";
    private static final int DATABASE_VERSION = 17;

    public static final String USER_NAME = "name";
    public static final String USER_PHONE = "phone";
    public static final String USER_UID = "uid";
    public static final String ABBREVIATION = "abbreviation";
    public static final String COUNTERPARTY = "counterparty";
    public static final String TAXPAYER_ID = "taxpayer_id";
    public static final String USER_CREATED_AT = "created_at";
    public static final String USER_UPDATED_AT = "updated_at";
    public static final String USER_ROLE = "role";
    public static final String PARTNER_ROLE = "partner_role";

    public static final String ORDER_ID = "order_id";
    public static final String DATE_MILLIS = "date_millis";
    public static final String CATEGORY = "category";
    public static final String DELIVERY = "delivery";


    String SQL_Create = "";
    String SQL_Delete = "";

    public HelperDB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Создайте обе таблицы как часть баз данных
        try {
            SQL_Create = "CREATE TABLE " + TABLE_NAME_MY_USER + " (";
            SQL_Create += USER_NAME + " TEXT, ";
            SQL_Create += USER_PHONE + " TEXT, ";
            SQL_Create += USER_UID + " TEXT, ";
            SQL_Create += ABBREVIATION + " TEXT, ";
            SQL_Create += COUNTERPARTY + " TEXT, ";
            SQL_Create += TAXPAYER_ID + " TEXT, ";
            SQL_Create += USER_CREATED_AT + " TEXT, ";
            SQL_Create += USER_UPDATED_AT + " TEXT, ";
            SQL_Create += ORDER_ID + " TEXT, ";
            SQL_Create += USER_ROLE + " TEXT, ";
            SQL_Create += PARTNER_ROLE + " TEXT);";
            db.execSQL(SQL_Create);

            //создать и внести значения по умолчанию
            SQL_Create = "CREATE TABLE " + TABLE_NAME_ORDER_ID + " (";
            SQL_Create += ORDER_ID + " TEXT, ";
            SQL_Create += DATE_MILLIS + " TEXT, ";
            SQL_Create += CATEGORY + " TEXT, ";
            SQL_Create += DELIVERY + " TEXT);";
            db.execSQL(SQL_Create);

            Log.d("A111", "While creating db: ");
        }
        catch (Exception e) {
            //БД каждый раз создается ( а не открывается) таблица ужу существует
            Log.d("A111", "Error while creating db: " + e.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        SQL_Delete = "DROP TABLE IF EXISTS " + TABLE_NAME_MY_USER;
        db.execSQL(SQL_Delete);

        SQL_Delete = "DROP TABLE IF EXISTS " + TABLE_NAME_ORDER_ID;
        db.execSQL(SQL_Delete);

        onCreate(db);
    }

    public void deleteUsers() {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            // Delete All Rows
            db.delete(TABLE_NAME_MY_USER, null, null);
            db.delete(TABLE_NAME_ORDER_ID, null, null);
            db.close();

            Log.d("A111", "Deleted all user info from sqlite");
        }catch (Exception ex){
            Log.d("A111", "Error deleted sqlite "+ex.toString());
        }


    }
    public void clearTableOrders() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_NAME_ORDER_ID, null, null);
        db.close();

        Log.d("A111", "HelperDB. Deleted all order info from sqlite");
    }

    public Cursor getYourTableContents() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME_MY_USER, null);

        return data;
    }
}
