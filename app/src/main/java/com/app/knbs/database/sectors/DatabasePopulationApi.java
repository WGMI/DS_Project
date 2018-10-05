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

public class DatabasePopulationApi {
    private Context context;
    public DatabasePopulationApi(Context context) {
        this.context = context;
    }
    private DatabaseHelper dbHelper = new DatabaseHelper(context);

    public void loadData(final ProgressDialog d){
        //test();
        insertInto_population_by_sex_and_school_attendance(d);
        insertInto_population_households_type_floor_material_main_dwelling_unit(d);
        insertInto_population_percentage_households_ownership_household_assets(d);
        insertInto_population_households_by_main_source_of_water(d);
    }

    private JsonArrayRequest policy(JsonArrayRequest request) {
        request.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }

    //Change to national in DB and check table name
    private void insertInto_population_populationbysexhouseholdsdensityandcensusyears(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/population/all_population_populationbysexhouseholdsdensityandcensusyears",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject maleObject  = response.getJSONObject(0);
                            JSONObject femaleObject  = response.getJSONObject(1);
                            JSONObject totalObject  = response.getJSONObject(2);
                            JSONObject HHSObject  = response.getJSONObject(3);
                            JSONObject avgHHSSizeObject  = response.getJSONObject(4);
                            JSONObject approxAreaObject  = response.getJSONObject(5);
                            JSONObject densityObject  = response.getJSONObject(6);
                            JSONObject censusYearObject  = response.getJSONObject(7);

                            JSONArray maleArray = maleObject.getJSONArray("data");
                            JSONArray femaleArray = femaleObject.getJSONArray("data");
                            JSONArray totalArray = totalObject.getJSONArray("data");
                            JSONArray HHSArray = HHSObject.getJSONArray("data");
                            JSONArray avgHHSSizeArray = avgHHSSizeObject.getJSONArray("data");
                            JSONArray approxAreaArray = approxAreaObject.getJSONArray("data");
                            JSONArray densityArray = densityObject.getJSONArray("data");
                            JSONArray censusYearArray = censusYearObject.getJSONArray("data");

                            List<Integer> males = new ArrayList<>();
                            List<Integer> females = new ArrayList<>();
                            List<Integer> totals = new ArrayList<>();
                            List<Integer> HHSList = new ArrayList<>();
                            List<Integer> avgHHSSizeList = new ArrayList<>();
                            List<Integer> densities = new ArrayList<>();
                            List<Integer> censusYears = new ArrayList<>();

                            for(int i=0;i<censusYearArray.length();i++){
                                males.add(maleArray.getInt(i));
                                females.add(femaleArray.getInt(i));
                                totals.add(totalArray.getInt(i));
                                HHSList.add(HHSArray.getInt(i));
                                avgHHSSizeList.add(avgHHSSizeArray.getInt(i));
                                densities.add(densityArray.getInt(i));
                                censusYears.add(censusYearArray.getInt(i));
                            }

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("population_populationbysexhouseholdsdensityandcensusyears",null,null);

                            for(int i=0;i<censusYearArray.length();i++){

                                ContentValues values = new ContentValues();


                                success = db.insertOrThrow("population_populationbysexhouseholdsdensityandcensusyears",null,values);
                            }

                            db.close();
                            Log.d(TAG, "population_populationbysexhouseholdsdensityandcensusyears: " + success);

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

    private void insertInto_population_by_sex_and_school_attendance(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/population/all_population_by_sex_and_school_attendance",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject eduLevelObject  = response.getJSONObject(0);
                            JSONObject maleObject  = response.getJSONObject(1);
                            JSONObject femaleObject  = response.getJSONObject(2);
                            JSONObject totalPopObject  = response.getJSONObject(3);

                            JSONArray eduLevelArray = eduLevelObject.getJSONArray("data");
                            JSONArray maleArray = maleObject.getJSONArray("data");
                            JSONArray femaleArray = femaleObject.getJSONArray("data");
                            JSONArray totalPopArray = totalPopObject.getJSONArray("data");

                            //List<Integer> eduLevels = new ArrayList<>();
                            List<String> eduLevels = new ArrayList<>();
                            List<String> males = new ArrayList<>();
                            List<String> females = new ArrayList<>();
                            List<String> totalPopList = new ArrayList<>();

                            for(int i=0;i<totalPopArray.length();i++){
                                eduLevels.add(eduLevelArray.getString(i));
                                males.add(maleArray.getString(i));
                                females.add(femaleArray.getString(i));
                                totalPopList.add(totalPopArray.getString(i));
                            }

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("population_by_sex_and_school_attendance",null,null);

                            for(int i=0;i<totalPopArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("attendance_id",i+1);
                                values.put("education_level",eduLevels.get(i));
                                values.put("female",females.get(i));
                                values.put("male",males.get(i));
                                values.put("total_population",totalPopList.get(i));

                                success = db.insertOrThrow("population_by_sex_and_school_attendance",null,values);
                            }


                            db.close();
                            d.dismiss();
                            Log.d(TAG, "population_by_sex_and_school_attendance: " + success);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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

    private void insertInto_population_households_type_floor_material_main_dwelling_unit(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/population/all_population_households_type_floor_material_main_dwelling_unit",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject materialObject  = response.getJSONObject(0);
                            JSONObject householdsObject  = response.getJSONObject(1);

                            JSONArray materialArray = materialObject.getJSONArray("data");
                            JSONArray householdsArray = householdsObject.getJSONArray("data");

                            List<String> materials = new ArrayList<>();
                            List<Integer> households = new ArrayList<>();

                            for(int i=0;i<householdsArray.length();i++){
                                materials.add(materialArray.getString(i));
                                households.add(householdsArray.getInt(i));
                            }

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("population_households_type_floor_material_main_dwelling_unit",null,null);

                            for(int i=0;i<householdsArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("floor_material_id",i+1);
                                values.put("material",materials.get(i));
                                values.put("households",households.get(i));

                                success = db.insertOrThrow("population_households_type_floor_material_main_dwelling_unit",null,values);
                            }


                            db.close();
                            Log.d(TAG, "population_households_type_floor_material_main_dwelling_unit: " + success);

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

    private void insertInto_population_percentage_households_ownership_household_assets(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/population/all_population_percentage_households_ownership_household_assets",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject assetObject  = response.getJSONObject(0);
                            JSONObject percentageObject  = response.getJSONObject(1);

                            JSONArray assetArray = assetObject.getJSONArray("data");
                            JSONArray percentageArray = percentageObject.getJSONArray("data");

                            List<String> assets = new ArrayList<>();
                            List<Float> percentages = new ArrayList<>();

                            for(int i=0;i<assetArray.length();i++){
                                assets.add(assetArray.getString(i));
                                percentages.add(Float.parseFloat(String.valueOf(percentageArray.getString(i))));
                            }

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("population_percentage_households_ownership_household_assets",null,null);

                            for(int i=0;i<assetArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("ownership_id",i+1);
                                values.put("asset",assets.get(i));
                                values.put("percentage",percentages.get(i));

                                success = db.insertOrThrow("population_percentage_households_ownership_household_assets",null,values);
                            }


                            db.close();
                            Log.d(TAG, "population_percentage_households_ownership_household_assets: " + success);

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

    private void insertInto_population_households_by_main_source_of_water(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                "http://156.0.232.97:8000/population/all_population_households_by_main_source_of_water",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject sourceObject  = response.getJSONObject(0);
                            JSONObject totalObject  = response.getJSONObject(1);

                            JSONArray sourceArray = sourceObject.getJSONArray("data");
                            JSONArray totalArray = totalObject.getJSONArray("data");

                            List<String> sources = new ArrayList<>();
                            List<Integer> totals = new ArrayList<>();

                            for(int i=0;i<sourceArray.length();i++){
                                sources.add(sourceArray.getString(i));
                                totals.add(totalArray.getInt(i));
                            }

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("population_households_by_main_source_of_water",null,null);

                            for(int i=0;i<sourceArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("source_of_water_id",i+1);
                                values.put("source",sources.get(i));
                                values.put("total",totals.get(i));

                                success = db.insertOrThrow("population_households_by_main_source_of_water",null,values);
                            }


                            db.close();
                            Log.d(TAG, "population_households_by_main_source_of_water: " + success);

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