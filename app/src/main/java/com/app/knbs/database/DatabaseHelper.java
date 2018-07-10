package com.app.knbs.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.app.knbs.adapter.model.Sector_Data;
import com.app.knbs.adapter.model.Sector;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public final static String TAG = "DatabaseHelper";
    private final Context myContext;
    private static final String DATABASE_NAME = "knbs.db";
    private static final int DATABASE_VERSION = 2;
    public String pathToSaveDBFile  ;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.myContext = context;

        //pathToSaveDBFile = myContext.getFilesDir().getAbsolutePath()+"/"+DATABASE_NAME;
        pathToSaveDBFile = "data/data/com.app.knbs/databases";
        //pathToSaveDBFile = new StringBuffer(filePath).append("/").append(DATABASE_NAME).toString();
    }


    public void prepareDatabase() throws IOException {

        try {
            copyDataBase();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        boolean dbExist = checkDataBase();
        if(dbExist) {
            Log.d(TAG, "Database exists. V "+getVersionId());

            int currentDBVersion = getVersionId();
            if (DATABASE_VERSION > currentDBVersion) {
                Log.d(TAG, "Database version is higher than old.");
                deleteDb();
                try {
                    copyDataBase();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
            }

        } else {
            try {
                copyDataBase();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }


    }
    private boolean checkDataBase() {
        boolean checkDB = false;
        try {

            File file = new File(pathToSaveDBFile);
            checkDB = file.exists();
        } catch(SQLiteException e) {
            Log.d(TAG, e.getMessage());
        }
        return checkDB;
    }
    private void copyDataBase() throws IOException {
        OutputStream os = new FileOutputStream(pathToSaveDBFile);
        InputStream is = myContext.getAssets().open("sqlite/"+DATABASE_NAME);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
        is.close();
        os.flush();
        os.close();
        Log.e(TAG, "Database Created");
    }
    private void deleteDb() {
        File file = new File(pathToSaveDBFile);
        if(file.exists()) {
            file.delete();
            Log.d(TAG, "Database deleted.");
        }
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        myContext.deleteDatabase(DATABASE_NAME);
    }

    public String checkSectorsReports(String sector_table, String county ) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        String num;
        String query = "SELECT * FROM "+sector_table+" t JOIN counties c ON t.county_id=c.county_id WHERE county_name='"+county+"' ";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount()+"";
        Log.d(TAG, "Table Count "+num+"\n"+query);

        cursor.close();
        db.close();
        return num;
    }

    private void testGraph() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT year,nse_20_share_index FROM money_and_banking_nairobi_securities_exchange where year > 2014 GROUP BY year";

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
        for(Sector_Data d : list){
            Log.d(TAG, "testGraph: " + d.getSet_A() + "\n" + d.getYear() + "\n");
        }
    }

    public List<Sector_Data> getYears(String table) {
        //testGraph();
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        String query = "SELECT DISTINCT(year) FROM "+table+" ORDER BY year ASC ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear(cursor.getString(0));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<String> getCounties(String table) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();

        String query = "SELECT DISTINCT(county_name) FROM "+table+" t JOIN counties c ON t.county_id=c.county_id ";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
            category.add("Select County");
        while(cursor.moveToNext()) {
            category.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return category;
    }

    public List<Sector> getSectorsReports(String sector_name, String region) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT * FROM sectors WHERE sector_name like '"+sector_name+"' AND coverage='"+region+"' AND isActive='1'";
        if(sector_name.contains("Environment") && region.contains("nation")){
            query = "SELECT * FROM sectors WHERE sector_name like '%Environment and Natural Resources%' AND coverage='national' and isActive='1'";
        }
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        if(num == 0){
            query = "SELECT * FROM sectors WHERE sector_name='"+sector_name+"' AND coverage='"+region+"' AND isActive=1";
            cursor = db.rawQuery(query, null);
            num = cursor.getCount();
        }
        Log.d(TAG, "rows "+num+"\n"+query);
        List<Sector> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector sector = new Sector();
            sector.setSectorID(cursor.getString(0));
            sector.setSector(cursor.getString(1));
            sector.setReport(cursor.getString(2));
            sector.setCoverage(cursor.getString(3));
            sector.setSource(cursor.getString(4));
            sector.setTable(cursor.getString(5));
            sector.setApi(cursor.getString(6));
            sector.setFavourite(cursor.getString(7));
            list.add(sector);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<Sector> getFavouriteReports(String region) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        int num;
        String query = "SELECT * FROM sectors WHERE favourite=1 AND coverage='"+region+"' ";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);
        List<Sector> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector sector = new Sector();
            sector.setSectorID(cursor.getString(0));
            sector.setSector(cursor.getString(1));
            sector.setReport(cursor.getString(2));
            sector.setCoverage(cursor.getString(3));
            sector.setSource(cursor.getString(4));
            sector.setTable(cursor.getString(5));
            sector.setApi(cursor.getString(6));
            sector.setFavourite(cursor.getString(7));
            list.add(sector);
        }
        cursor.close();
        db.close();
        return list;
    }

    public List<String> getSectors() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> sectors = new ArrayList<>();
        int num;
        String query = "SELECT DISTINCT(sector_name) FROM sectors WHERE (sector_name!='Agriculture' AND sector_name!='Education' AND sector_name!='Governance'" +
                "                 AND sector_name!='Population and Vital Statistics' AND sector_name!='Public Finance' AND sector_name!='Public Health' ) ";
        Cursor cursor = db.rawQuery(query, null);
        num = cursor.getCount();
        Log.d(TAG, "rows "+num+"\n"+query);
            sectors.add("Select Sector");
        while(cursor.moveToNext()) {
            sectors.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return sectors;
    }

    public List<String> getSectorData() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        List<String> category = new ArrayList<>();
        String query = "SELECT * FROM sectors WHERE isActive='1' ";
        Cursor cursor = db.rawQuery(query, null);
        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);

        while(cursor.moveToNext()) {
            Log.d("Sector",cursor.getString(0)+" "+cursor.getString(2)+" "+cursor.getString(7));
        }
        cursor.close();
        db.close();
        return category;
    }

    public void addFavourite(String sector, String status){
        String updateQuery = "UPDATE sectors SET favourite="+status+" WHERE sector_id='"+sector+"' ";
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
        Cursor cursor = db.rawQuery(updateQuery, null);
        System.out.print("Count "+cursor.getCount()+"\n"+updateQuery);
        cursor.close();
    }



    public List<Sector_Data> getAdministrative_unit(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

        String query = "SELECT SUM(divisions),SUM(locations),SUM(sub_locations) FROM administrative_unit e" +
                "       JOIN counties c ON e.county_id=c.county_id " +
                "       WHERE county_name='"+county+"' GROUP BY county_name";
        Cursor cursor = db.rawQuery(query, null);

        Log.d(TAG, "rows "+cursor.getCount()+"\n"+query);
        List<Sector_Data> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Sector_Data data = new Sector_Data();
            data.setYear("2014");
            data.setSet_A(cursor.getString(0));
            data.setSet_B(cursor.getString(1));
            data.setSet_C(cursor.getString(2));
            list.add(data);
        }
        cursor.close();
        db.close();
        return list;
    }

    private int getVersionId() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        String query = "SELECT version_id FROM dbVersion";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        int v =  cursor.getInt(0);
        cursor.close();
        cursor.close();  db.close();
        return v;
    }
}