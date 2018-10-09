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
 * Developed by Rodney on 22/02/2018.
 */

public class DatabaseCPI {
    private Context mContext;
    public DatabaseCPI(Context mContext) {
        this.mContext = mContext;
    }
    private DatabaseHelper dbHelper = new DatabaseHelper(mContext);

    public List<String> getCpi_annual_avg_retail_prices_of_certain_consumer_goods_in_kenya_item() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();
        int num;
        String query = "SELECT DISTINCT(item) FROM cpi_annual_avg_retail_prices_of_certain_consumer_goods_in_kenya ";
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

    public List<Sector_Data> getCpi_annual_avg_retail_prices_of_certain_consumer_goods_in_kenya(String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT year ,ksh_per_unit,unit FROM cpi_annual_avg_retail_prices_of_certain_consumer_goods_in_kenya WHERE item='"+choice+"' GROUP BY year ";
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

    public List<String> getCpi_Income_Group() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();
        int num;
        String query = "SELECT DISTINCT(income_group) FROM cpi_consumer_price_index ";
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

    public List<Sector_Data> getCpi_Consumer_Price_Index(String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "select year, avg(total) as 'average' from cpi_consumer_price_index where income_group = '"+choice+"' group by year";
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


    public List<String> getCpi_elementary_aggregates_weights_in_the_cpi_baskets() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();
        int num;
        String query = "SELECT DISTINCT(description) FROM cpi_elementary_aggregates_weights_in_the_cpi_baskets ";
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


    public List<Sector_Data> getCpi_elementary_aggregates_weights_in_the_cpi_baskets(String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT nairobi_lower,nairobi_middle,nairobi_upper,rest_of_urban_areas,kenya FROM cpi_elementary_aggregates_weights_in_the_cpi_baskets WHERE description='"+choice+"' ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear("2015");
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


    public List<String> getCpi_group_weights_for_kenya_cpi_febuary_base_2009() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();
        int num;
        String query = "SELECT DISTINCT(description) FROM cpi_group_weights_for_kenya_cpi_febuary_base_2009 ";
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

    public List<Sector_Data> getCpi_group_weights_for_kenya_cpi_febuary_base_2009(String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        //choice = choice.substring(0, 1).toUpperCase() + choice.substring(1);

        String query = "SELECT " +
                "SUM(CASE WHEN household='Lower' THEN group_weights ELSE 0 END) AS 'Lower' ,\n" +
                "SUM(CASE WHEN household='Middle' THEN group_weights ELSE 0 END) AS 'Middle', \n" +
                "SUM(CASE WHEN household='Upper' THEN group_weights ELSE 0 END) AS 'Upper' ,\n" +
                "SUM(CASE WHEN household=' Rest of Urban Areas ' THEN group_weights ELSE 0 END) AS 'Rest of Urban Areas', \n" +
                "SUM(CASE WHEN household=' All Kenya' THEN group_weights ELSE 0 END) AS 'All Kenya' \n" +
                "FROM cpi_group_weights_for_kenya_cpi_febuary_base_2009 WHERE description='"+choice+"' ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear("2009");
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

    public List<String> getCpi_group_weights_for_kenya_cpi_october_base_1997() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();
        int num;
        String query = "SELECT DISTINCT(item_group) FROM cpi_group_weights_for_kenya_cpi_october_base_1997 ";
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

    public List<Sector_Data> getCpi_group_weights_for_kenya_cpi_october_base_1997(String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        //choice = choice.substring(0, 1).toUpperCase() + choice.substring(1);

        String query = "SELECT " +
                "SUM(CASE WHEN household='Lower' THEN group_weights ELSE 0 END) AS 'Lower' ,\n" +
                "SUM(CASE WHEN household='Middle' THEN group_weights ELSE 0 END) AS 'Middle', \n" +
                "SUM(CASE WHEN household='Upper' THEN group_weights ELSE 0 END) AS 'Upper' ,\n" +
                "SUM(CASE WHEN household=' Rest of Urban Areas ' THEN group_weights ELSE 0 END) AS 'Rest of Urban Areas', \n" +
                "SUM(CASE WHEN household=' All Kenya' THEN group_weights ELSE 0 END) AS 'All Kenya' \n" +
                "FROM cpi_group_weights_for_kenya_cpi_october_base_1997 WHERE item_group='"+choice+"' ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear("2015");
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
}
