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

public class DatabaseBuildingConstruction {
    private Context mContext;
    public DatabaseBuildingConstruction(Context mContext) {
        this.mContext = mContext;
    }
    private DatabaseHelper dbHelper = new DatabaseHelper(mContext);

    public List<String> getBuilding_and_construction_ids() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();
        int num;
        String query = "SELECT DISTINCT(industry) FROM building_and_construction_industry_id ";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);

        while(cursor.moveToNext()) {
            category.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return category;
    }

    public List<Sector_Data> getBuilding_and_construction_amount(String county, String select) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT year,amount FROM building_and_construction_amount e" +
                "       JOIN counties c ON e.county_id=c.county_id " +
                "       JOIN  building_and_construction_industry_id al ON e.industry_id=al.industry_id "+
                "       WHERE county_name='"+county+"' AND industry='"+select+"' GROUP BY year ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear(cursor.getString(0));
            data.setSet_A(cursor.getString(1));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }


    public List<Sector_Data> getBuilding_and_construction_amount(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,amount FROM building_and_construction_amount e " +
                "                JOIN counties c ON e.county_id=c.county_id" +
                "                    WHERE county_name!='"+county+"' AND (industry_id='1' OR industry_id='2') GROUP BY year,county_name ";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear(cursor.getString(0));
            data.setSet_A(cursor.getString(1));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getQuarterly_Civil_Engineering_Cost() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,amount FROM building_and_construction_amount e " +
                "                 GROUP BY year";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear(cursor.getString(0));
            data.setSet_A(cursor.getString(1));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getQuarterly_Residential_Building_Cost() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        //String query = "SELECT year,september FROM building_and_construction_quarterly_residential_bulding_cost e " +
        //        "                 GROUP BY year,county_name ";
        String query = "SELECT year,september FROM building_and_construction_quarterly_residential_bulding_cost e where year < 2015 ";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear(cursor.getString(0));
            data.setSet_A(cursor.getString(1));
            list.add(data);
            Log.d(TAG + "listsize", "getQuarterly_Residential_Building_Cost: " + list.size());
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getQuarterly_Non_Residential_Building_Cost() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        //Old query//String query = "SELECT year,september FROM building_and_construction_quarterly_residential_bulding_cost e " + "                 GROUP BY year,county_name ";
        String query = "SELECT year,sept FROM building_and_construction_quarterly_non_residential_build_cost e GROUP BY year";

        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear(cursor.getString(0));
            data.setSet_A(cursor.getString(1));
            list.add(data);
            Log.d(TAG + "listsize", "getQuarterly_Residential_Building_Cost: " + list.size());
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> get_Quarterly_Overall_Construction_Cost() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        //Old query//String query = "SELECT year,september FROM building_and_construction_quarterly_residential_bulding_cost e " + "                 GROUP BY year,county_name ";
        String query = "SELECT year,march,sept,december FROM building_and_construction_quarterly_overal_construction_cost e GROUP BY year";

        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear(cursor.getString(0));
            data.setSet_A(cursor.getString(1));
            list.add(data);
            Log.d(TAG + "listsize", "getQuarterly_Residential_Building_Cost: " + list.size());
        }
        cursor.close();
        db.close();
        return list;
    }
}
