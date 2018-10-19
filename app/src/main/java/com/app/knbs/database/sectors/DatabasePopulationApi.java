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
import com.app.knbs.services.ReportLoader;
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
    private ReportLoader loader = new ReportLoader(context);
    private CountyHelper countyHelper = new CountyHelper();

    public void loadData(final ProgressDialog d){
        insertInto_population_by_sex_and_school_attendance(d);
        insertInto_population_households_type_floor_material_main_dwelling_unit(d);
        insertInto_population_percentage_households_ownership_household_assets(d);
        insertInto_population_households_by_main_source_of_water(d);
        insertInto_population_populationprojectionsbyselectedagegroup(d);

        insertInto_population_populationprojectionsbyspecialagegroups(d);
        insertInto_population_by_sex_and_age_groups(d);
        insertInto_population_kihibs_by_broad_age_group(d);
        insertInto_population_kihibs_children_under_18_by_orphanhood(d);
        insertInto_population_kihibs_distribution_by_sex(d);

        insertInto_Population_Distribution_of_households_by_size(d);
        insertInto_get_population_kihibs_marital_status_above_18_years(d);
        insertInto_population_kihibs_hholds_by_sex_of_household_head(d);
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
                loader.getApi("Population by Sex and School Attendance (3 Years and Above)"),
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
                loader.getApi("Households by Main Type of Floor Material for the Main Dwelling Unit"),
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
                loader.getApi("Percentage of Households by Ownership of Household Assets"),
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
                loader.getApi("Households by Main Source of Water"),
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

    private void insertInto_population_populationprojectionsbyselectedagegroup(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Population Projections By Selected Age Group"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject countyObject  = response.getJSONObject(0);
                            JSONObject to4Object  = response.getJSONObject(1);
                            JSONObject to24Object  = response.getJSONObject(5);
                            JSONObject to44Object  = response.getJSONObject(9);
                            JSONObject to64Object  = response.getJSONObject(11);
                            JSONObject yearObject  = response.getJSONObject(19);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray to4Array = to4Object.getJSONArray("data");
                            JSONArray to24Array = to24Object.getJSONArray("data");
                            JSONArray to44Array = to44Object.getJSONArray("data");
                            JSONArray to64Array = to64Object.getJSONArray("data");
                            JSONArray yearArray = yearObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("population_and_vs_populationprojectionsbyselectedagegroup",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("population_projection_id",i+1);
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));
                                values.put("range_0_4",to4Array.getInt(i));
                                values.put("range_5_9",to4Array.getInt(i));
                                values.put("range_10_14",to4Array.getInt(i));
                                values.put("range_15_19",to4Array.getInt(i));
                                values.put("range_15_19",to4Array.getInt(i));
                                values.put("range_20_24",to24Array.getInt(i));
                                values.put("range_25_29",to24Array.getInt(i));
                                values.put("range_30_34",to24Array.getInt(i));
                                values.put("range_35_39",to24Array.getInt(i));
                                values.put("range_40_44",to44Array.getInt(i));
                                values.put("range_45_49",to44Array.getInt(i));
                                values.put("range_50_54",to44Array.getInt(i));
                                values.put("range_55_59",to44Array.getInt(i));
                                values.put("range_60_64",to64Array.getInt(i));
                                values.put("range_65_69",to64Array.getInt(i));
                                values.put("range_65_69",to64Array.getInt(i));
                                values.put("range_70_74",to64Array.getInt(i));
                                values.put("range_75_79",to64Array.getInt(i));
                                values.put("range_80_plus",to64Array.getInt(i));
                                values.put("gender","");
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("population_and_vs_populationprojectionsbyselectedagegroup",null,values);
                            }

                            db.close();
                            Log.d(TAG, "population_and_vs_populationprojectionsbyselectedagegroup: " + success);

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

    private void insertInto_population_populationprojectionsbyspecialagegroups(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Population Projections By Special Age Group"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject countyObject  = response.getJSONObject(0);
                            JSONObject u1Object  = response.getJSONObject(1);
                            JSONObject r18andBelowObject  = response.getJSONObject(8);
                            JSONObject r18plusObject  = response.getJSONObject(9);
                            JSONObject r65plusObject  = response.getJSONObject(12);
                            JSONObject genderObject  = response.getJSONObject(13);
                            JSONObject yearObject  = response.getJSONObject(14);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray u1Array = u1Object.getJSONArray("data");
                            JSONArray r18andBelowArray = r18andBelowObject.getJSONArray("data");
                            JSONArray r18plusArray = r18plusObject.getJSONArray("data");
                            JSONArray r65plusArray = r65plusObject.getJSONArray("data");
                            JSONArray genderArray = genderObject.getJSONArray("data");
                            JSONArray yearArray = yearObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("population_and_vs_populationprojectionsbyspecialagegroups",null,null);

                            for(int i=0;i<yearArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("selected_age_group_id",i+1);
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));
                                values.put("under_1",u1Array.getInt(i));
                                values.put("range_1_2",0);
                                values.put("range_3_5",0);
                                values.put("range_6_13",0);
                                values.put("range_14_17",0);
                                values.put("range_18_24",0);
                                values.put("range_18_34",0);
                                values.put("range_less_18",r18andBelowArray.getInt(i));
                                values.put("range_18_plus",r18plusArray.getInt(i));
                                values.put("range_15_49",0);
                                values.put("range_15_64",0);
                                values.put("range_65_plus",r65plusArray.getInt(i));
                                values.put("gender",genderArray.getString(i));
                                values.put("year",yearArray.getInt(i));

                                success = db.insertOrThrow("population_and_vs_populationprojectionsbyspecialagegroups",null,values);
                            }

                            db.close();
                            Log.d(TAG, "population_and_vs_populationprojectionsbyspecialagegroups: " + success);

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

    private void insertInto_population_by_type_of_disability(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Population by Type of Disability"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject disObject  = response.getJSONObject(0);
                            JSONObject maleObject  = response.getJSONObject(1);
                            JSONObject femaleObject  = response.getJSONObject(2);
                            JSONObject totalObject  = response.getJSONObject(3);

                            JSONArray disArray = disObject.getJSONArray("data");
                            JSONArray maleArray = maleObject.getJSONArray("data");
                            JSONArray femaleArray = femaleObject.getJSONArray("data");
                            JSONArray totalArray = totalObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("population_by_type_of_disability",null,null);

                            for(int i=0;i<totalArray.length();i++){
                                ContentValues values = new ContentValues();

                                values.put("selected_age_group_id",i+1);

                                success = db.insertOrThrow("population_by_type_of_disability",null,values);
                            }

                            db.close();
                            Log.d(TAG, "population_by_type_of_disability: " + success);

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
    }//Bad Api

    private void insertInto_population_by_sex_and_age_groups(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Population by Sex and Age Groups"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject maleObject  = response.getJSONObject(0);
                            JSONObject femaleObject  = response.getJSONObject(1);
                            JSONObject totalObject  = response.getJSONObject(2);
                            JSONObject ageObject  = response.getJSONObject(3);

                            JSONArray maleArray = maleObject.getJSONArray("data");
                            JSONArray femaleArray = femaleObject.getJSONArray("data");
                            JSONArray totalArray = totalObject.getJSONArray("data");
                            JSONArray ageArray = ageObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("population_by_sex_and_age_groups",null,null);

                            for(int i=0;i<totalArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("group_id",i+1);
                                values.put("female",femaleArray.getInt(i));
                                values.put("male",maleArray.getInt(i));
                                values.put("total_population",totalArray.getInt(i));
                                values.put("age_group",ageArray.getString(i));

                                success = db.insertOrThrow("population_by_sex_and_age_groups",null,values);
                            }

                            db.close();
                            Log.d(TAG, "population_by_sex_and_age_groups: " + success);

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

    private void insertInto_population_kihibs_by_broad_age_group(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Population by broad age group"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject countyObject  = response.getJSONObject(0);
                            JSONObject ageDependObject  = response.getJSONObject(5);
                            JSONObject childObject  = response.getJSONObject(6);
                            JSONObject oldObject  = response.getJSONObject(7);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray ageDependArray = ageDependObject.getJSONArray("data");
                            JSONArray childArray = childObject.getJSONArray("data");
                            JSONArray oldArray = oldObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("population_kihibs_by_broad_age_group",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("group_id",i+1);
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));
                                values.put("range_0_14",0);
                                values.put("range_15_64",0);
                                values.put("over_65",0);
                                values.put("not_stated",0);
                                values.put("age_depend_ratio",ageDependArray.getDouble(i));
                                values.put("child_depend_ratio",childArray.getDouble(i));
                                values.put("old_age_depend_ratio",oldArray.getDouble(i));
                                values.put("child_depend_ratio",childArray.getDouble(i));
                                values.put("individuals",0);

                                success = db.insertOrThrow("population_kihibs_by_broad_age_group",null,values);
                            }

                            db.close();
                            Log.d(TAG, "population_kihibs_by_broad_age_group: " + success);

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

    private void insertInto_population_kihibs_children_under_18_by_orphanhood(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Population of Children under 18 by orphanhood"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject countyObject  = response.getJSONObject(0);
                            JSONObject livingWBothObject  = response.getJSONObject(1);
                            JSONObject faliveObject  = response.getJSONObject(2);
                            JSONObject fdeadObject  = response.getJSONObject(3);
                            JSONObject maliveObject  = response.getJSONObject(4);
                            JSONObject mdeadObject  = response.getJSONObject(5);
                            JSONObject baliveObject  = response.getJSONObject(6);
                            JSONObject ofaliveObject  = response.getJSONObject(7);
                            JSONObject omaliveObject  = response.getJSONObject(8);
                            JSONObject bothDeadObject  = response.getJSONObject(9);
                            JSONObject missInfoObject  = response.getJSONObject(10);
                            JSONObject orphanObject  = response.getJSONObject(11);
                            JSONObject indObject  = response.getJSONObject(12);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray lwbArray = livingWBothObject.getJSONArray("data");
                            JSONArray faArray = faliveObject.getJSONArray("data");
                            JSONArray fdArray = fdeadObject.getJSONArray("data");
                            JSONArray maArray = maliveObject.getJSONArray("data");
                            JSONArray mdArray = mdeadObject.getJSONArray("data");
                            JSONArray baArray = baliveObject.getJSONArray("data");
                            JSONArray ofaArray = ofaliveObject.getJSONArray("data");
                            JSONArray omaArray = omaliveObject.getJSONArray("data");
                            JSONArray bdArray = bothDeadObject.getJSONArray("data");
                            JSONArray miArray = missInfoObject.getJSONArray("data");
                            JSONArray orArray = orphanObject.getJSONArray("data");
                            JSONArray iArray = indObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("population_kihibs_children_under_18_by_orphanhood",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("distribution_id",i+1);
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));
                                values.put("living_with_both",lwbArray.getDouble(i));
                                values.put("father_alive",faArray.getDouble(i));
                                values.put("father_deceased",fdArray.getDouble(i));
                                values.put("mother_alive",maArray.getDouble(i));
                                values.put("mother_deceased",mdArray.getDouble(i));
                                values.put("both_alive",baArray.getDouble(i));
                                values.put("only_father_alive",ofaArray.getDouble(i));
                                values.put("only_mother_alive",omaArray.getDouble(i));
                                values.put("both_parents_deceased",bdArray.getDouble(i));
                                values.put("missing_info",miArray.getDouble(i));
                                values.put("orphanhood",orArray.getDouble(i));
                                values.put("individuals",iArray.getInt(i));

                                success = db.insertOrThrow("population_kihibs_children_under_18_by_orphanhood",null,values);
                            }

                            db.close();
                            Log.d(TAG, "population_kihibs_children_under_18_by_orphanhood: " + success);

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

    private void insertInto_population_kihibs_distribution_by_sex(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Population Distribution by sex"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject countyObject  = response.getJSONObject(0);
                            JSONObject malePerObject  = response.getJSONObject(2);
                            JSONObject femalePerObject  = response.getJSONObject(4);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray maleArray = malePerObject.getJSONArray("data");
                            JSONArray femaleArray = femalePerObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("population_kihibs_distribution_by_sex",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("distribution_id",i+1);
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));
                                values.put("male_per_cent",maleArray.getDouble(i));
                                values.put("female_per_cent",femaleArray.getDouble(i));

                                success = db.insertOrThrow("population_kihibs_distribution_by_sex",null,values);
                            }

                            db.close();
                            Log.d(TAG, "population_kihibs_distribution_by_sex: " + success);

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

    private void insertInto_Population_Distribution_of_households_by_size(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Population Distribution of households by size"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject countyObject  = response.getJSONObject(0);
                            JSONObject to2Object  = response.getJSONObject(1);
                            JSONObject to4Object  = response.getJSONObject(2);
                            JSONObject to6Object  = response.getJSONObject(3);
                            JSONObject to7Object  = response.getJSONObject(4);
                            JSONObject houseObject  = response.getJSONObject(5);
                            JSONObject meanObject  = response.getJSONObject(6);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray to2Array = to2Object.getJSONArray("data");
                            JSONArray to4Array = to4Object.getJSONArray("data");
                            JSONArray to6Array = to6Object.getJSONArray("data");
                            JSONArray to7Array = to7Object.getJSONArray("data");
                            JSONArray houseArray = houseObject.getJSONArray("data");
                            JSONArray meanArray = meanObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("population_kihibs_distribution_of_households_by_size",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("distribution_id",i+1);
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));
                                values.put("range_1_2_persons",to2Array.getDouble(i));
                                values.put("range_3_4_persons",to4Array.getDouble(i));
                                values.put("range_5_6_persons",to6Array.getDouble(i));
                                values.put("over_7_persons",to7Array.getDouble(i));
                                values.put("households",houseArray.getInt(i));
                                values.put("mean_hhold_size",meanArray.getDouble(i));

                                success = db.insertOrThrow("population_kihibs_distribution_of_households_by_size",null,values);
                            }

                            db.close();
                            Log.d(TAG, "population_kihibs_distribution_of_households_by_size: " + success);

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

    private void insertInto_get_population_kihibs_marital_status_above_18_years(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Population Marital Status above 18 years"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject countyObject  = response.getJSONObject(0);
                            JSONObject monObject  = response.getJSONObject(1);
                            JSONObject polyObject  = response.getJSONObject(2);
                            JSONObject ltObject  = response.getJSONObject(3);
                            JSONObject sepObject  = response.getJSONObject(4);
                            JSONObject divObject  = response.getJSONObject(5);
                            JSONObject widObject  = response.getJSONObject(6);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray monArray = monObject.getJSONArray("data");
                            JSONArray polyArray = polyObject.getJSONArray("data");
                            JSONArray ltArray = ltObject.getJSONArray("data");
                            JSONArray sepArray = sepObject.getJSONArray("data");
                            JSONArray divArray = divObject.getJSONArray("data");
                            JSONArray widArray = widObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("population_kihibs_marital_status_above_18_years",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("status_id",i+1);
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));
                                values.put("monogamous",monArray.getDouble(i));
                                values.put("polygamous",polyArray.getDouble(i));
                                values.put("living_together",ltArray.getDouble(i));
                                values.put("seperated",sepArray.getDouble(i));
                                values.put("divorced",divArray.getInt(i));
                                values.put("widow_widower",widArray.getDouble(i));
                                values.put("never_married",0);
                                values.put("individuals",0);

                                success = db.insertOrThrow("population_kihibs_marital_status_above_18_years",null,values);
                            }

                            db.close();
                            Log.d(TAG, "population_kihibs_marital_status_above_18_years: " + success);

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

    private void insertInto_population_kihibs_hholds_by_sex_of_household_head(final ProgressDialog d){
        d.show();
        RequestQueue queue = VolleySingleton.getInstance(context).getQueue();
        JsonArrayRequest request = new JsonArrayRequest(
                loader.getApi("Population by sex according to  household head"),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject countyObject  = response.getJSONObject(0);
                            JSONObject maleObject  = response.getJSONObject(1);
                            JSONObject femaleObject  = response.getJSONObject(2);
                            JSONObject houseObject  = response.getJSONObject(3);

                            JSONArray countyArray = countyObject.getJSONArray("data");
                            JSONArray maleArray = maleObject.getJSONArray("data");
                            JSONArray femaleArray = femaleObject.getJSONArray("data");
                            JSONArray houseArray = houseObject.getJSONArray("data");

                            long success = 0;

                            SQLiteDatabase db = SQLiteDatabase.openDatabase(dbHelper.pathToSaveDBFile, null, SQLiteDatabase.OPEN_READWRITE);
                            db.delete("population_kihibs_hholds_by_sex_of_household_head",null,null);

                            for(int i=0;i<countyArray.length();i++){
                                ContentValues values = new ContentValues();
                                values.put("head_id",i+1);
                                values.put("county_id",countyHelper.getCountyId(countyArray.getString(i)));
                                values.put("male",maleArray.getDouble(i));
                                values.put("female",femaleArray.getDouble(i));
                                values.put("households",houseArray.getInt(i));

                                success = db.insertOrThrow("population_kihibs_hholds_by_sex_of_household_head",null,values);
                            }

                            db.close();
                            Log.d(TAG, "population_kihibs_hholds_by_sex_of_household_head: " + success);

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