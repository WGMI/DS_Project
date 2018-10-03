package com.app.knbs.database.sectors;

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
import com.app.knbs.database.DatabaseHelper;
import com.app.knbs.services.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.app.knbs.database.DatabaseHelper.TAG;

/**
 * Developed by Rodney on 28/02/2018.
 */

public class DatabaseHealthApi {
    private Context context;
    public DatabaseHealthApi(Context context) {
        this.context = context;
    }
    private DatabaseHelper dbHelper = new DatabaseHelper(context);

    public void test(){
        SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = db.rawQuery("SELECT * FROM education_csa_adulteducationcentresbysubcounty where county_id = 30 OR county_id = '30'",null);

        while(cursor.moveToNext()){

            Log.d(TAG + "test", "test: " + cursor.getInt(0) + "\n"
                    + cursor.getInt(1) + "\n"
                    + cursor.getInt(2) + "\n"
                    + cursor.getInt(3) + "\n"
                    + cursor.getInt(4) + "\n" );
        }

        cursor.close();
        db.close();
    }

    public void loadHealthData(final ProgressDialog d){
        insertInto_health_percentage_incidence_of_diseases_in_kenya(d);
    }

    private JsonArrayRequest policy(JsonArrayRequest request) {
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }

    private void insertInto_health_percentage_incidence_of_diseases_in_kenya(final ProgressDialog d){
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/health/all_health_percentage_incidence_of_diseases_in_kenya",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray array) {
                        try {
                            JSONObject yearObject = array.getJSONObject(0);
                            JSONObject percentageIncedenceObject = array.getJSONObject(1);
                            JSONObject diseaseObject = array.getJSONObject(2);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray percentageIncedenceArray = percentageIncedenceObject.getJSONArray("data");
                            JSONArray diseaseArray = diseaseObject.getJSONArray("data");

                            List<Integer> years = new ArrayList<>();
                            List<Double> percentageIncedences = new ArrayList<>();
                            List<String> diseases = new ArrayList<>();

                            for(int i=0;i<yearArray.length();i++){
                                years.add(yearArray.getInt(i));
                                percentageIncedences.add(percentageIncedenceArray.getDouble(i));
                                diseases.add(diseaseArray.getString(i));
                            }

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile,null,SQLiteDatabase.OPEN_READWRITE);
                            db.delete("health_percentage_incidence_of_diseases_in_kenya",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("incidence_id",i+1);
                                values.put("year",years.get(i));
                                values.put("percentage_incidence",percentageIncedences.get(i));
                                values.put("disease",diseases.get(i));

                                success = db.insertOrThrow("health_percentage_incidence_of_diseases_in_kenya",null,values);
                            }

                            db.close();
                            Log.d(TAG, "health_percentage_incidence_of_diseases_in_kenya: " + success);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "health_percentage_incidence_of_diseases_in_kenya: " + volleyError.toString());
                    }
                }
        );
        request = policy(request);
        queue.add(request);
    }

}