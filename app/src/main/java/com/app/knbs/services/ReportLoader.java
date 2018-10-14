package com.app.knbs.services;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.app.knbs.database.DBHandler;
import com.app.knbs.database.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.app.knbs.database.DatabaseHelper.TAG;


/**
 * Created by Muganda Imo on 10/1/2018.
 */

public class ReportLoader {

    Context context;
    DatabaseHelper dbHelper = new DatabaseHelper(context);

    public ReportLoader(Context context) {
        this.context = context;
    }

    private JsonArrayRequest policy(JsonArrayRequest request) {
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }

    public void loadReports(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/health/all_sectors",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject sectorNameObject = array.getJSONObject(0);
                            JSONObject reportObject = array.getJSONObject(1);
                            JSONObject sourceObject = array.getJSONObject(2);
                            JSONObject tableObject = array.getJSONObject(3);
                            JSONObject coverageObject = array.getJSONObject(4);
                            JSONObject apiObject = array.getJSONObject(5);

                            JSONArray sectorNameArray = sectorNameObject.getJSONArray("data");
                            JSONArray reportArray = reportObject.getJSONArray("data");
                            JSONArray sourceArray = sourceObject.getJSONArray("data");
                            JSONArray tableArray = tableObject.getJSONArray("data");
                            JSONArray coverageArray = coverageObject.getJSONArray("data");
                            JSONArray apiArray = apiObject.getJSONArray("data");

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("sectors_from_api",null,null);

                            long success = 0;

                            for(int i=0;i<apiArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("sector_id",i+1);
                                values.put("sector_name",sectorNameArray.getString(i));
                                values.put("report",reportArray.getString(i));
                                values.put("coverage",coverageArray.getString(i));
                                values.put("source",sourceArray.getString(i));
                                values.put("table_name",tableArray.getString(i));
                                values.put("api_url",apiArray.getString(i));
                                values.put("favourite",0);
                                values.put("isActive",1);

                                success = db.insert("sectors_from_api",null,values);
                            }

                            d.dismiss();
                            db.close();
                            Log.d(TAG, "sectors_from_api: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "onErrorResponse: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

    public String getApi(String report){
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
        Cursor cursor =
                db.rawQuery("select api_url from sectors_from_api where report LIKE '"+report.trim()+"'",null);
        String api = "failed";
        if(cursor.moveToFirst()){
            api = cursor.getString(0);
        }

        return api;
    }
}
