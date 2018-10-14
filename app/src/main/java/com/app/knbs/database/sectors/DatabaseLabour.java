package com.app.knbs.database.sectors;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.app.knbs.adapter.model.Sector_Data;
import com.app.knbs.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import static com.app.knbs.database.DatabaseHelper.TAG;

/**
 * Developed by Rodney on 27/02/2018.
 */

public class DatabaseLabour {
    private Context mContext;
    private Context context;
    public DatabaseLabour(Context mContext) {
        this.mContext = mContext;
    }
    private DatabaseHelper dbHelper = new DatabaseHelper(mContext);

    public List<String> getPrivate_Sector_Categories(){
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> categories = new ArrayList<>();
        int num;
        String query = "SELECT DISTINCT(private_sector) FROM labour_average_wage_earnings_per_employee_private_sector ";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);

        while(cursor.moveToNext()) {
            categories.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return categories;
    }

    public List<Sector_Data> getAverage_Wage_Earnings_Per_Employee_in_Private_Sector(String choice){
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        //choice = choice.substring(0, 1).toUpperCase() + choice.substring(1);
        //String query = "SELECT year,number FROM education_es_teachertraineesprivateenrolment WHERE gender='"+choice+"' GROUP BY year ";

        String query = "select year,wage_earnings from labour_average_wage_earnings_per_employee_private_sector where private_sector = '"+choice+"'";
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

    public List<Sector_Data> getTotal_recorded_employment() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        //choice = choice.substring(0, 1).toUpperCase() + choice.substring(1);
        //String query = "SELECT year,number FROM education_es_teachertraineesprivateenrolment WHERE gender='"+choice+"' GROUP BY year ";

        String query = "SELECT year ," +
                "SUM(CASE WHEN sector_category='Modern Sector' THEN total_employment ELSE 0 END) AS Modern, \n" +
                "SUM(CASE WHEN sector_category='Informal Sector' THEN total_employment ELSE 0 END) AS Informal \n" +
                "FROM labour_total_recorded_employment GROUP BY year ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear(cursor.getString(0));
            data.setSet_A(cursor.getString(1));
            data.setSet_B(cursor.getString(2));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }


    public List<Sector_Data> getLabour_employment_public_sector(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT year,wage_employment FROM labour_employment_public_sector e "+
                "JOIN labour_sectors ON e.sector_id="+
                "JOIN counties c ON e.county_id=c.county_id WHERE county_name='"+county+"' GROUP BY year" ;
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

    public List<Sector_Data> getTotal_recorded_employment_public_sector() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        //choice = choice.substring(0, 1).toUpperCase() + choice.substring(1);
        //String query = "SELECT year,number FROM education_es_teachertraineesprivateenrolment WHERE gender='"+choice+"' GROUP BY year ";

        /*String query = "SELECT year ," +
                "SUM(CASE WHEN sector_category='Modern Sector' THEN total_employment ELSE 0 END) AS Modern, \n" +
                "SUM(CASE WHEN sector_category='Informal Sector' THEN total_employment ELSE 0 END) AS Informal \n" +
                "FROM labour_total_recorded_employment GROUP BY year ";*/

        String query = "SELECT year ," +
                "SUM(CASE WHEN public_sector='Agriculture, Forestry and Fishing' THEN wage_earnings ELSE 0 END) AS Agriculture, \n" +
                "SUM(CASE WHEN public_sector='Manufacturing' THEN wage_earnings ELSE 0 END) AS Manufacturing \n" +
                "FROM average_wage_earnings_per_employee_public_sector GROUP BY year ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        Log.d(TAG, "query "+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear(cursor.getString(0));
            data.setSet_A(cursor.getString(1));
            data.setSet_B(cursor.getString(2));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector_Data> getMemorandum_Items_in_Public_Sector() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        //choice = choice.substring(0, 1).toUpperCase() + choice.substring(1);
        //String query = "SELECT year,number FROM education_es_teachertraineesprivateenrolment WHERE gender='"+choice+"' GROUP BY year ";

        /*String query = "SELECT year ," +
                "SUM(CASE WHEN sector_category='Modern Sector' THEN total_employment ELSE 0 END) AS Modern, \n" +
                "SUM(CASE WHEN sector_category='Informal Sector' THEN total_employment ELSE 0 END) AS Informal \n" +
                "FROM labour_total_recorded_employment GROUP BY year ";*/

        String query = "SELECT year ," +
                "SUM(CASE WHEN memorandum_item='Ministries and Other Eextra-Budgetary Institutions ' THEN wage_earnings ELSE 0 END) AS Ministries, \n" +
                "SUM(CASE WHEN memorandum_item='County Governments' THEN wage_earnings ELSE 0 END) AS 'County Governments' \n" +
                "FROM memorandum_items_in_public_sector GROUP BY year ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear(cursor.getString(0));
            data.setSet_A(cursor.getString(1));
            data.setSet_B(cursor.getString(2));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<String> getWage_employment_by_industry_and_sex_industry() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();
        int num;
        String query = "SELECT DISTINCT(industry) FROM labour_wage_employment_by_industry_and_sex ";
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

    public List<Sector_Data> getWage_employment_by_industry_and_sex(String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        //choice = choice.substring(0, 1).toUpperCase() + choice.substring(1);

        String query = "SELECT year ," +
                "SUM(CASE WHEN gender='male' THEN wage_employment ELSE 0 END) AS Male, \n" +
                "SUM(CASE WHEN gender='female' THEN wage_employment ELSE 0 END) AS Female \n" +
                "FROM labour_wage_employment_by_industry_and_sex WHERE industry='"+choice+"' GROUP BY year ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear(cursor.getString(0));
            data.setSet_A(cursor.getString(1));
            data.setSet_B(cursor.getString(2));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<String> getWage_employment_by_industry_in_private_sector_industry() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();
        int num;
        String query = "SELECT DISTINCT(private_sector) FROM labour_wage_employment_by_industry_in_private_sector ";
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


    public List<Sector_Data> getWage_employment_by_industry_in_private_sector(String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT year ,wage_employment FROM labour_wage_employment_by_industry_in_private_sector WHERE private_sector='"+choice+"' GROUP BY year ";
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


    public List<String> getWage_employment_by_industry_in_public_sector_industry() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();
        int num;
        String query = "SELECT DISTINCT(public_sector) FROM labour_wage_employment_by_industry_in_public_sector ";
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

    public List<Sector_Data> getWage_employment_by_industry_in_public_sector(String choice) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT year ,wage_employment FROM labour_wage_employment_by_industry_in_public_sector WHERE public_sector='"+choice+"' GROUP BY year ";
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
        //
        return list;
    }

}
