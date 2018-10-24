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
 * Developed by Rodney on 28/02/2018.
 */

public class DatabasePoverty {
    private Context mContext;
    public DatabasePoverty(Context mContext) {
        this.mContext = mContext;
    }
    private DatabaseHelper dbHelper = new DatabaseHelper(mContext);

    public List<Sector_Data> getconsumptionexpenditureandquintiledistribution(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT quarter1,quarter2,quarter3,quarter4,quarter5 FROM poverty_consumption_expenditure_and_quintile_distribution e" +
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

    public List<String> getpointofpurchasedfooditems() {
        List<String> list = new ArrayList<>();
        list.add("supermarket");
        list.add("open_market");
        list.add("kiosk");
        list.add("general_shop");
        list.add("specialised_shop");
        list.add("other_formal_points");
        list.add("informal_sources");
        return list;
    }

    public List<Sector_Data> getdistributionofhouseholdsbypointofpurchasedfooditems(String county,String selection) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT "+selection+" FROM poverty_distribution_of_households_by_pointofpurchasedfooditems e" +
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

    public List<Sector_Data> getdistributionofhouseholdfoodconsumption(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT purchases,stock,own_production,gifts FROM poverty_distribution_of_household_food_consumption e" +
                " JOIN counties c ON e.county_id=c.county_id" +
                " WHERE county_name='"+county+"' group by county_name";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear("2014");
            data.setSet_A(cursor.getString(0));
            data.setSet_B(cursor.getString(1));
            data.setSet_C(cursor.getString(2));
            data.setSet_D(cursor.getString(3));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getfoodxpenditureperadult(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT food FROM poverty_food_and_non_food_expenditure_per_adult_equivalent e" +
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

    public List<Sector_Data> getfoodestimatesbyresidenceandcounty(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT headcount_rate,distribution_of_the_poor,poverty_gap,severity_of_poverty FROM poverty_food_estimates_by_residence_and_county e" +
                " JOIN counties c ON e.county_id=c.county_id" +
                " WHERE county_name='"+county+"' group by county_name";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear("2014");
            data.setSet_A(cursor.getString(0));
            data.setSet_B(cursor.getString(1));
            data.setSet_C(cursor.getString(2));
            data.setSet_D(cursor.getString(3));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> gethardcoreestimatesbyresidenceandcounty(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT headcount_rate,distribution_of_the_poor,poverty_gap,severity_of_poverty FROM poverty_hardcore_estimates_by_residence_and_county e" +
                " JOIN counties c ON e.county_id=c.county_id" +
                " WHERE county_name='"+county+"' group by county_name";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear("2014");
            data.setSet_A(cursor.getString(0));
            data.setSet_B(cursor.getString(1));
            data.setSet_C(cursor.getString(2));
            data.setSet_D(cursor.getString(3));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> geoverallestimatesbyresidenceandcounty(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT headcount_rate,distribution_of_the_poor,poverty_gap,severity_of_poverty FROM poverty_overall_estimates_by_residence_and_count e" +
                " JOIN counties c ON e.county_id=c.county_id" +
                " WHERE county_name='"+county+"' group by county_name";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear("2014");
            data.setSet_A(cursor.getString(0));
            data.setSet_B(cursor.getString(1));
            data.setSet_C(cursor.getString(2));
            data.setSet_D(cursor.getString(3));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }
}
