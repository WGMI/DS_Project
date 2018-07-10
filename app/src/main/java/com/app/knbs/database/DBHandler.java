package com.app.knbs.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Developed by Rodney on 22/11/2016.
 */

public class DBHandler extends SQLiteOpenHelper {

    private static final String TAG = DBHandler.class.getSimpleName();

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "knbs";

    //Tables
    private static final String TABLE_OFFER = "offers";
    private static final String TABLE_EVENT = "events";
    private static final String TABLE_BUSINESS = "business";
    private static final String TABLE_HISTORY = "history";
    private static final String TABLE_REWARD = "redeems";
    private static final String TABLE_BRANCH = "branch";

    //Business Table
    private static final String KEY_BUSINESS_ID = "business_id";
    private static final String KEY_BUSINESS_NAME = "name";
    private static final String KEY_BUSINESS_LOCATION = "location";
    private static final String KEY_BUSINESS_OPEN_HOURS = "open_hours";
    private static final String KEY_INFO_BUSINESS = "info";
    private static final String KEY_LOGO_BUSINESS = "logo";
    private static final String KEY_BUSINESS_NUMBER = "number";
    private static final String KEY_BUSINESS_WEBSITE = "website";
    private static final String KEY_BUSINESS_FAVOURITE = "favourite";
    private static final String KEY_BUSINESS_POINTS = "points";
    private static final String KEY_BUSINESS_LATITUDE = "lat";
    private static final String KEY_BUSINESS_LONGITUDE = "lng";

    //Shop Table
    //Business Table
    private static final String KEY_BRANCH_ID = "branch_id";
    private static final String KEY_SHOP_BUSINESS_ID = "business_id";
    private static final String KEY_BRANCH_NAME = "name";
    private static final String KEY_BRANCH_LOCATION = "location";
    private static final String KEY_BRANCH_OPEN_HOURS = "open_hours";
    private static final String KEY_BRANCH_INFO = "info";
    private static final String KEY_BRANCH_NUMBER = "number";
    private static final String KEY_BRANCH_LATITUDE = "lat";
    private static final String KEY_BRANCH_LONGITUDE = "lng";

    //Offer Table
    private static final String KEY_OFFER_ID = "offer_id";
    private static final String KEY_OFFER_TITLE= "title";
    private static final String KEY_OFFER_MESSAGE = "message";
    private static final String KEY_OFFER_EXPIRE = "expire";
    private static final String KEY_OFFER_TIMESTAMP = "timestamp";
    private static final String KEY_OFFER_PRICE = "offer_price";
    private static final String KEY_OFFER_DISCOUNT = "offer_discount";
    private static final String KEY_OFFER_REDEEM_POINTS = "offer_points";
    private static final String KEY_OFFER_IMAGE = "offer_image";
    private static final String KEY_OFFER_CATEGORY = "offer_category";
    private static final String KEY_OFFER_REDEEM = "offers_redeemed";

    private static final String KEY_OFFER_MONDAY = "monday";
    private static final String KEY_OFFER_TUESDAY = "tuesday";
    private static final String KEY_OFFER_WEDNESDAY = "wednesday";
    private static final String KEY_OFFER_THURSDAY = "thursday";
    private static final String KEY_OFFER_FRIDAY = "friday";
    private static final String KEY_OFFER_SATURDAY = "saturday";
    private static final String KEY_OFFER_SUNDAY = "sunday";

    //reward Table
    private static final String KEY_REWARD_ID = "reward_id";
    private static final String KEY_REWARD_TITLE= "title";
    private static final String KEY_REWARD_MESSAGE = "message";
    private static final String KEY_REWARD_POINTS = "redeem_points";
    private static final String KEY_REWARD_IMAGE = "redeem_image";
    private static final String KEY_REWARD_CATEGORY = "category";

    //Event Table
    private static final String KEY_EVENT_ID = "event_id";
    private static final String KEY_EVENT_NAME = "name";
    private static final String KEY_EVENT_DATE = "event_date";
    private static final String KEY_EVENT_TIME = "event_time";
    private static final String KEY_EVENT_TIMESTAMP = "timestamp";
    private static final String KEY_EVENT_LOCATION = "location";
    private static final String KEY_EVENT_IMAGE = "event_image";
    private static final String KEY_EVENT_CHARGE = "charge";
    private static final String KEY_EVENT_DETAILS = "details";
    private static final String KEY_EVENT_CATEGORY = "event_category";
    private static final String KEY_EVENT_LAT = "lat";
    private static final String KEY_EVENT_LNG = "lng";

    //Transaction Table
    private static final String KEY_HISTORY_ID = "history_id";
    private static final String KEY_HISTORY_TYPE = "history_type";
    private static final String KEY_HISTORY_POINTS = "history_point";
    private static final String KEY_HISTORY_DATE = "history_date";

    private Long tsLong = System.currentTimeMillis()/1000;
    private String timestamp = tsLong.toString();

    private String CREATE_TABLE_BUSINESS = "CREATE TABLE IF NOT EXISTS " + TABLE_BUSINESS + "("
            + KEY_BUSINESS_ID+ " INTEGER PRIMARY KEY,"
            + KEY_BUSINESS_NAME + " TEXT NOT NULL ,"
            + KEY_BUSINESS_LOCATION + " TEXT NOT NULL ,"
            + KEY_BUSINESS_OPEN_HOURS + " TEXT NOT NULL ,"
            + KEY_INFO_BUSINESS + " TEXT NOT NULL ,"
            + KEY_LOGO_BUSINESS + " TEXT NOT NULL ,"
            + KEY_BUSINESS_NUMBER + " TEXT NOT NULL ,"
            + KEY_BUSINESS_WEBSITE + " TEXT NOT NULL ,"
            + KEY_BUSINESS_POINTS + " TEXT NOT NULL DEFAULT 0 ,"
            + KEY_BUSINESS_FAVOURITE + " INTEGER DEFAULT 0,"
            + KEY_BUSINESS_LATITUDE + " DOUBLE NOT NULL ,"
            + KEY_BUSINESS_LONGITUDE + " DOUBLE NOT NULL);";

    private String CREATE_TABLE_BRANCH = "CREATE TABLE IF NOT EXISTS " + TABLE_BRANCH + "("
            + KEY_BRANCH_ID+ " INTEGER PRIMARY KEY,"
            + KEY_SHOP_BUSINESS_ID + " TEXT NOT NULL ,"
            + KEY_BRANCH_NAME + " TEXT NOT NULL ,"
            + KEY_BRANCH_LOCATION + " TEXT NOT NULL ,"
            + KEY_BRANCH_OPEN_HOURS + " TEXT NOT NULL ,"
            + KEY_BRANCH_INFO + " TEXT NOT NULL ,"
            + KEY_BRANCH_NUMBER + " TEXT NOT NULL ,"
            + KEY_BRANCH_LATITUDE + " DOUBLE NOT NULL ,"
            + KEY_BRANCH_LONGITUDE + " DOUBLE NOT NULL);";

    private String CREATE_TABLE_OFFER = "CREATE TABLE IF NOT EXISTS " + TABLE_OFFER + "("
            + KEY_OFFER_ID+ " INTEGER PRIMARY KEY,"
            + KEY_BUSINESS_ID + " TEXT NOT NULL ,"
            + KEY_BRANCH_ID + " TEXT NOT NULL ,"
            + KEY_OFFER_TITLE + " TEXT NOT NULL ,"
            + KEY_OFFER_MESSAGE + " TEXT ,"
            + KEY_OFFER_CATEGORY + " TEXT NOT NULL ,"
            + KEY_OFFER_PRICE + " TEXT NOT NULL ,"
            + KEY_OFFER_DISCOUNT + " TEXT NOT NULL ,"
            + KEY_OFFER_REDEEM_POINTS + " TEXT NOT NULL ,"
            + KEY_OFFER_REDEEM + " TEXT NOT NULL ,"
            + KEY_OFFER_EXPIRE + " TEXT NOT NULL ,"
            + KEY_OFFER_TIMESTAMP + " TEXT NOT NULL ,"
            + KEY_OFFER_MONDAY + " TEXT NOT NULL ,"
            + KEY_OFFER_TUESDAY + " TEXT NOT NULL ,"
            + KEY_OFFER_WEDNESDAY + " TEXT NOT NULL ,"
            + KEY_OFFER_THURSDAY + " TEXT NOT NULL ,"
            + KEY_OFFER_FRIDAY + " TEXT NOT NULL ,"
            + KEY_OFFER_SATURDAY + " TEXT NOT NULL ,"
            + KEY_OFFER_SUNDAY + " TEXT NOT NULL ,"
            + KEY_OFFER_IMAGE + " TEXT NOT NULL);";

    private String CREATE_TABLE_REWARD = "CREATE TABLE IF NOT EXISTS " + TABLE_REWARD + "("
            + KEY_REWARD_ID+ " INTEGER PRIMARY KEY,"
            + KEY_BUSINESS_ID + " TEXT NOT NULL ,"
            + KEY_REWARD_TITLE + " TEXT NOT NULL ,"
            + KEY_REWARD_MESSAGE + " TEXT NOT NULL ,"
            + KEY_REWARD_CATEGORY + " TEXT NOT NULL ,"
            + KEY_REWARD_POINTS + " TEXT NOT NULL ,"
            + KEY_REWARD_IMAGE + " TEXT NOT NULL);";

    private String CREATE_TABLE_EVENT = "CREATE TABLE IF NOT EXISTS " + TABLE_EVENT + "("
            + KEY_EVENT_ID+ " INTEGER PRIMARY KEY,"
            + KEY_BUSINESS_ID + " INTEGER DEFAULT 0,"
            + KEY_EVENT_NAME + " TEXT NOT NULL ,"
            + KEY_EVENT_DATE + " TEXT NOT NULL ,"
            + KEY_EVENT_TIME + " TEXT NOT NULL ,"
            + KEY_EVENT_TIMESTAMP + " TEXT NOT NULL ,"
            + KEY_EVENT_LOCATION + " TEXT NOT NULL ,"
            + KEY_EVENT_CATEGORY + " TEXT NOT NULL ,"
            + KEY_EVENT_IMAGE + " TEXT NOT NULL ,"
            + KEY_EVENT_DETAILS + " TEXT NOT NULL ,"
            + KEY_EVENT_CHARGE + " TEXT NOT NULL ,"
            + KEY_EVENT_LAT + " DOUBLE NOT NULL ,"
            + KEY_EVENT_LNG + " DOUBLE NOT NULL);";

    private String CREATE_TABLE_HISTORY = "CREATE TABLE IF NOT EXISTS " + TABLE_HISTORY + "("
            + KEY_HISTORY_ID + " INTEGER PRIMARY KEY,"
            + KEY_BUSINESS_ID + " TEXT NOT NULL ,"
            + KEY_HISTORY_TYPE + " TEXT NOT NULL ,"
            + KEY_HISTORY_POINTS + " TEXT NOT NULL ,"
            + KEY_HISTORY_DATE + " TEXT NOT NULL);";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    private static final String DATABASE_ALTER_OFFER = "ALTER TABLE "
            + TABLE_OFFER + " ADD COLUMN " + KEY_OFFER_REDEEM + " string;";


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_BUSINESS);
        db.execSQL(CREATE_TABLE_OFFER);
        db.execSQL(CREATE_TABLE_REWARD);
        db.execSQL(CREATE_TABLE_EVENT);
        db.execSQL(CREATE_TABLE_HISTORY);
        db.execSQL(CREATE_TABLE_BRANCH);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion < 2) {
            db.execSQL(DATABASE_ALTER_OFFER);
        }/*
        if (oldVersion < 3) {
            db.execSQL(DATABASE_ALTER_OFFER_1);
        }*/
    }

    public String dayOfTheWeek(){
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);
        dayOfTheWeek = dayOfTheWeek.toLowerCase();

        return dayOfTheWeek;
    }

}
