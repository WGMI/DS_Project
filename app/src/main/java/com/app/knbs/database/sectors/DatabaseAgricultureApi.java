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
import com.app.knbs.database.CountyHelper;
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

public class DatabaseAgricultureApi {
    private Context context;
    public DatabaseAgricultureApi(Context context) {
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

    public void loadAgricultureData(final ProgressDialog d){
        //test();
    }

    private JsonArrayRequest policy(JsonArrayRequest request) {
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }

    //API: Sorry, the page you are looking for could not be found.
    private void insertInto_agriculture_chemical_med_feed_input(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/education/all_diploma_degree",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject yearObject  = response.getJSONObject(0);
                            JSONObject approvedDegreesObject  = response.getJSONObject(1);
                            JSONObject approvedPrivateDegreesObject  = response.getJSONObject(2);
                            JSONObject validatedDiplomasObject  = response.getJSONObject(3);

                            JSONArray yearArray = yearObject.getJSONArray("data");
                            JSONArray approvedDegreesArray = approvedDegreesObject.getJSONArray("data");
                            JSONArray approvedPrivateDegreesArray = approvedPrivateDegreesObject.getJSONArray("data");
                            JSONArray validatedDiplomasArray = validatedDiplomasObject.getJSONArray("data");

                            List<Integer> years = new ArrayList<>();
                            List<Integer> degrees = new ArrayList<>();
                            List<Integer> privateDegrees = new ArrayList<>();
                            List<Integer> validatedDiplomas = new ArrayList<>();

                            for(int i=0;i<yearArray.length();i++){
                                years.add(yearArray.getInt(i));
                                degrees.add(approvedDegreesArray.getInt(i));
                                privateDegrees.add(approvedPrivateDegreesArray.getInt(i));
                                validatedDiplomas.add(validatedDiplomasArray.getInt(i));
                            }

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("education_approved_degree_diploma_programs",null,null);

                            for(int i=0;i<yearArray.length();i++){

                                ContentValues values = new ContentValues();
                                values.put("year",years.get(i));
                                values.put("approved_degree_programmes",degrees.get(i));
                                values.put("approved_private_university_degreeprogrammes",privateDegrees.get(i));
                                values.put("validated_diploma_programmes",validatedDiplomas.get(i));

                                success = db.insertOrThrow("education_approved_degree_diploma_programs",null,values);
                            }

                            db.close();
                            Log.d(TAG, "education_approved_degree_diploma_programs: " + success);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        d.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("test_error", "onResponse: " + error.toString());
                    }
                }
        );

        request = policy(request);
        queue.add(request);
    }

}