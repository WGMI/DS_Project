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

public class DatabasePolitical {
    private Context mContext;
    public DatabasePolitical(Context mContext) {
        this.mContext = mContext;
    }
    private DatabaseHelper dbHelper = new DatabaseHelper(mContext);

    public List<Sector_Data> getAdministrative_unit(String county) {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);

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

}
