package com.app.knbs.database.sectors;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.app.knbs.adapter.model.Sector_Data;
import com.app.knbs.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import static com.app.knbs.database.DatabaseHelper.TAG;

/**
 * Developed by Rodney on 27/02/2018.
 */

public class DatabaseICT {
    private Context mContext;
    private Context context;
    public DatabaseICT(Context mContext) {
        this.mContext = mContext;
    }
    private DatabaseHelper dbHelper = new DatabaseHelper(mContext);

    public List<Sector_Data> gethouseholdsownedictequipmentservices(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT computer,television FROM ict_kihibs_households_owned_ict_equipment_services e" +
                " JOIN counties c ON e.county_id=c.county_id" +
                " WHERE county_name='"+county+"' group by county_name";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear("2014");
            data.setSet_A(cursor.getString(0));
            data.setSet_B(cursor.getString(1));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> gethouseholdswithoutinternetbyreason(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT dont_need,lack_skills,no_network,access_elsewhere,service_cost_high FROM ict_kihibs_households_without_internet_by_reason e" +
                " JOIN counties c ON e.county_id=c.county_id" +
                " WHERE county_name='"+county+"' group by county_name";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear("2014");
            data.setSet_A(cursor.getString(0));
            data.setSet_B(cursor.getString(1));
            data.setSet_C(cursor.getString(2));
            data.setSet_D(cursor.getString(3));
            data.setTotal(cursor.getString(4));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> gethouseholdswithtv(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT digital_tv,pay_tv,free_to_air,ip_tv,none FROM ict_kihibs_households_with_tv e" +
                " JOIN counties c ON e.county_id=c.county_id" +
                " WHERE county_name='"+county+"' group by county_name";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear("2014");
            data.setSet_A(cursor.getString(0));
            data.setSet_B(cursor.getString(1));
            data.setSet_C(cursor.getString(2));
            data.setSet_D(cursor.getString(3));
            data.setTotal(cursor.getString(4));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> gethouseholdswithinternetbytype(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT fixed_wired,fixed_wireless,mobile_broadband,mobile,other FROM ict_kihibs_households_with_internet_by_type e" +
                " JOIN counties c ON e.county_id=c.county_id" +
                " WHERE county_name='"+county+"' group by county_name";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear("2014");
            data.setSet_A(cursor.getString(0));
            data.setSet_B(cursor.getString(1));
            data.setSet_C(cursor.getString(2));
            data.setSet_D(cursor.getString(3));
            data.setTotal(cursor.getString(4));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getpopulationabove18byreasonnothavingphone(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT too_young,dont_need,restricted,no_network,no_electricity FROM ict_kihibs_population_above18by_reasonnothaving_phone e" +
                " JOIN counties c ON e.county_id=c.county_id" +
                " WHERE county_name='"+county+"' group by county_name";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear("2014");
            data.setSet_A(cursor.getString(0));
            data.setSet_B(cursor.getString(1));
            data.setSet_C(cursor.getString(2));
            data.setSet_D(cursor.getString(3));
            data.setTotal(cursor.getString(4));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getpopulationabove18subscribedmobilemoney(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT mobile_money,mobile_banking FROM ict_kihibs_population_above18subscribed_mobilemoney e" +
                " JOIN counties c ON e.county_id=c.county_id" +
                " WHERE county_name='"+county+"' group by county_name";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear("2014");
            data.setSet_A(cursor.getString(0));
            data.setSet_B(cursor.getString(1));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getpopulationbyictequipmentandservicesused(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT television,radio,mobile,computer,internet FROM ict_kihibs_population_by_ictequipment_and_servicesused e" +
                " JOIN counties c ON e.county_id=c.county_id" +
                " WHERE county_name='"+county+"' group by county_name";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear("2014");
            data.setSet_A(cursor.getString(0));
            data.setSet_B(cursor.getString(1));
            data.setSet_C(cursor.getString(2));
            data.setSet_D(cursor.getString(3));
            data.setTotal(cursor.getString(4));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getpopulationthatdidntuseinternetbyreason(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT too_young,dont_need,lack_skill,expensive,no_internet FROM ict_kihibs_population_that_didntuseinternet_by_reason e" +
                " JOIN counties c ON e.county_id=c.county_id" +
                " WHERE county_name='"+county+"' group by county_name";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear("2014");
            data.setSet_A(cursor.getString(0));
            data.setSet_B(cursor.getString(1));
            data.setSet_C(cursor.getString(2));
            data.setSet_D(cursor.getString(3));
            data.setTotal(cursor.getString(4));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getpopulationthatusedinternetbypurpose(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT newspaper,banking,research,informative,social_nets FROM ict_kihibs_population_that_used_internet_by_purpose e" +
                " JOIN counties c ON e.county_id=c.county_id" +
                " WHERE county_name='"+county+"' group by county_name";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear("2014");
            data.setSet_A(cursor.getString(0));
            data.setSet_B(cursor.getString(1));
            data.setSet_C(cursor.getString(2));
            data.setSet_D(cursor.getString(3));
            data.setTotal(cursor.getString(4));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getpopulationwhousedinternetbyplace(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT work,cyber,ed_centre,at_home,another_home FROM ict_kihibs_population_who_used_internet_by_place e" +
                " JOIN counties c ON e.county_id=c.county_id" +
                " WHERE county_name='"+county+"' group by county_name";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear("2014");
            data.setSet_A(cursor.getString(0));
            data.setSet_B(cursor.getString(1));
            data.setSet_C(cursor.getString(2));
            data.setSet_D(cursor.getString(3));
            data.setTotal(cursor.getString(4));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getpopulationwithmobilephone(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT phone FROM ict_kihibs_population_withmobilephone_andaveragesims e" +
                " JOIN counties c ON e.county_id=c.county_id" +
                " WHERE county_name='"+county+"' group by county_name";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear("2014");
            data.setSet_A(cursor.getString(0));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }
}
